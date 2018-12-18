package com.nis.web.dao.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidDataSource;
import com.nis.domain.Page;
import com.nis.domain.restful.NtcAsnRecord;
import com.nis.domain.restful.NtcConnRecordPercent;
import com.nis.web.service.SpringContextHolder;
import com.zdjizhi.utils.CommonUtil;
import com.zdjizhi.utils.StringUtil;

/**
 * 
 * <p>
 * Title: LogJDBCByDruid
 * </p>
 * <p>
 * Description: 使用druid连接池对hive或clickhouse进行查询并解析结果
 * </p>
 * <p>
 * Company: IIE
 * </p>
 * 
 * @author rkg
 * @date 2018年8月20日
 *
 */
@Repository
public class LocalLogJDBCByDruid {
	private final static Logger logger = LoggerFactory.getLogger(LocalLogJDBCByDruid.class);

	static NumberFormat num = NumberFormat.getPercentInstance();

	static DruidDataSource datasource = null;
	Connection conn = null;
	ResultSet rs = null;
	Statement st = null;

	private static Connection getConnection() throws SQLException {
		if (datasource == null) {
			datasource = (DruidDataSource) SpringContextHolder.getBean("LocalClickHouseDataSourceByDruid");
		}
		return datasource.getConnection();
	}

	/**
	 * 根据sql从数据中心中获取日志并set到list中
	 * 
	 * @param page
	 * @param sql
	 * @param entityClass
	 * @throws Exception
	 */
	public <T> void getTableData(Page<T> page, String sql, Class<?> entityClass) throws Exception {
		List<T> listObject = new ArrayList<T>();
		try {
			Map<String, String> filedAndColumnMap = getColumn2FiledMap(entityClass);
			// 不从Object... obj中获取需要date类型的字段了,调用的时候容易漏写,改为反射获取date类型的字段
			List<String> columnList = getDateColumn(entityClass);
			conn = getConnection();
			logger.info("连接数据中心日志库成功--------------------------");
			st = conn.createStatement();
			logger.info("开始执行日志查询语句sql={}", sql);
			rs = st.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					Object value = rs.getObject(i);
					String filedName = filedAndColumnMap.get(metaData.getColumnName(i).toString().toLowerCase());
					if (!StringUtil.isEmpty(value)) {
						// 如果是日期类型的属性需要把时间戳转换成日期，如果时间戳为0直接把值设置为null
						if (null != columnList && columnList.contains(filedName.toLowerCase())) {
							long time = 0L;
							time = Long.parseLong(value.toString());
							map.put(filedName, time == 0L ? null : new Date(time * 1000));
						} else {
							map.put(filedName, value);
						}
					} else {
						map.put(filedName, null);
					}
				}
				listObject.add((T) map2Obj(map, entityClass));
			}
			if (null == listObject || listObject.size() == 0) {
				page.setList(new ArrayList());
			} else {
				page.setList(listObject);
			}
			logger.info("执行日志查询语句成功,sql={}", sql);
		} finally {
			closeConn();
		}
	}

	/**
	 * 获取单向,流量包数和流量字节数在总的流量包数和字节数的占比
	 * 
	 * @param ntcConnRecordPercent
	 * @param sql
	 * @param isPersent
	 * @return
	 * @throws Exception
	 */
	public NtcConnRecordPercent getNtcConnRecordPercentData(NtcConnRecordPercent ntcConnRecordPercent, String sql,
			boolean isPersent) throws Exception {
		try {
			conn = getConnection();
			logger.info("连接数据中心日志库成功--------------------------");
			st = conn.createStatement();
			logger.info("开始执行日志查询语句sql={}", sql);
			rs = st.executeQuery(sql);
			num.setMinimumFractionDigits(2);// 保留两位小数
			while (rs.next()) {
				if (isPersent) {
					String allBps = rs.getString("bps");
					if (!allBps.equals("0")) {
						ntcConnRecordPercent.setBpsPercent(
								num.format(Double.valueOf(ntcConnRecordPercent.getBps()) / Double.valueOf(allBps)));
						ntcConnRecordPercent.setBps(subStr(ntcConnRecordPercent.getBps()));
					} else {
						ntcConnRecordPercent.setBpsPercent("0%");
					}
					String allPps = rs.getString("pps");
					if (!allPps.equals("0")) {
						ntcConnRecordPercent.setPpsPercent(
								num.format(Double.valueOf(ntcConnRecordPercent.getPps()) / Double.valueOf(allPps)));
						String ppsNum =ntcConnRecordPercent.getPps();
						if(ntcConnRecordPercent.getPps().contains(".")) {
							int idx =ntcConnRecordPercent.getPps().lastIndexOf(".");//查找小数点的位置
							ppsNum= ntcConnRecordPercent.getPps().substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
						}
						ntcConnRecordPercent.setPps(ppsNum);
					} else {
						ntcConnRecordPercent.setPpsPercent("0%");
					}
				} else {
					String bps = rs.getString("bps");
					String pps = rs.getString("pps");
					ntcConnRecordPercent.setBps(bps);
					ntcConnRecordPercent.setPps(pps);

				}
			}
			logger.info("执行日志查询语句成功,sql={}", sql);
		} finally {
			closeConn();
		}
		return ntcConnRecordPercent;
	}

	/**
	 * 获取asn,源目的的流量包数和字节数
	 * 
	 * @param ntcAsnRecord
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<NtcAsnRecord> getNtcAsnRecordData(String sql) throws Exception {
		List<NtcAsnRecord> list = new LinkedList<>();
		try {
			conn = getConnection();
			logger.info("连接数据中心日志库成功--------------------------");
			st = conn.createStatement();
			logger.info("开始执行日志查询语句sql={}", sql);
			rs = st.executeQuery(sql);
			num.setMinimumFractionDigits(2);// 保留两位小数
			while (rs.next()) {
				NtcAsnRecord ntcAsnRecord = new NtcAsnRecord();
				String bps = rs.getString("bps");
				String pps = rs.getString("pps");
				String ppsNum =pps;
				if(pps.contains(".")) {
					int idx =pps.lastIndexOf(".");//查找小数点的位置
					ppsNum= pps.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
				}
				ntcAsnRecord.setBps(subStr(bps));
				ntcAsnRecord.setPps(ppsNum);
				ntcAsnRecord.setAsn(rs.getString("asn"));
				list.add(ntcAsnRecord);
			}
			logger.info("执行日志查询语句成功,sql={}", sql);
		} finally {
			closeConn();
		}
		return list;
	}

	public List<String> getAllIp(Integer frontier) throws Exception {
		List<String> list = new LinkedList<>();
		try {
//			String sql = "select distinct ip from  ip_location_database_local where  ip like '145%'	and frontier='"+frontier+"'";
			String sql = "select distinct ip from  ip_location_database_local where   frontier='"+frontier+"'";
			conn = getConnection();
			logger.info("连接数据中心日志库成功--------------------------");
			st = conn.createStatement();
			logger.info("开始执行日志查询语句sql={}", sql);
			rs = st.executeQuery(sql);
			num.setMinimumFractionDigits(2);// 保留两位小数
			while (rs.next()) {
				String bps = rs.getString("ip");
				if(bps!=null&&!bps.trim().equals("")) {
					list.add(bps);
				}
			}
			logger.info("执行日志查询语句成功,sql={}", sql);
		} finally {
			closeConn();
		}
		return list;
	}

	/**
	 * 截取字符串后两位
	 * 
	 * @param val
	 * @return
	 */
	public String subStr(String val) {
		if (val != null && val.contains(".")) {
			int length = val.indexOf(".") + 3;
			if (val.length() > length) {
				return val.substring(0, length);
			}

		}
		return val;

	}

	/**
	 * 关闭数据库连接
	 */
	private void closeConn() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (st != null) {
				st.close();
				st = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
			logger.info("关闭数据中心连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("关闭数据中心连接失败,失败原因" + e);
		}
	}

	/**
	 * 反射获取类中date类型的字段名称
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private static List<String> getDateColumn(Class<?> type) throws Exception {
		List<String> columnList = new ArrayList<String>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
			String name = propertyDescriptor.getName();
			String fieldTypeName = propertyDescriptor.getPropertyType().getName();
			if (fieldTypeName.equals("java.util.Date")) {
				columnList.add(name.toLowerCase());
			}
		}
		return columnList;
	}

	/**
	 * 将map中的数据利用反射set到Class中,并返回设置好值的对象
	 * 
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	private static Object map2Obj(Map<String, Object> map, Class<?> beanClass) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		Object obj = beanClass.newInstance();
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
			String name = propertyDescriptor.getName();
			String fieldTypeName = propertyDescriptor.getPropertyType().getName();
			if (map.containsKey(name)) {
				Object value = map.get(name);
				if (!StringUtil.isEmpty(value)) {
					if (fieldTypeName.equals("java.lang.String")) {
						propertyDescriptor.getWriteMethod().invoke(obj, value.toString());
					} else if (fieldTypeName.equals("java.lang.Integer")) {
						propertyDescriptor.getWriteMethod().invoke(obj, Integer.parseInt(value.toString()));
					} else if (fieldTypeName.equals("java.lang.Long")) {
						propertyDescriptor.getWriteMethod().invoke(obj, Long.parseLong(value.toString()));
					} else if (fieldTypeName.equals("java.lang.Boolean")) {
						propertyDescriptor.getWriteMethod().invoke(obj, Boolean.parseBoolean(value.toString()));
					} else if (fieldTypeName.equals("java.lang.Character")) {
						propertyDescriptor.getWriteMethod().invoke(obj, value.toString().toCharArray());
					} else if (fieldTypeName.equals("java.lang.Byte")) {
						propertyDescriptor.getWriteMethod().invoke(obj, value.toString().getBytes());
					} else if (fieldTypeName.equals("java.lang.Short")) {
						propertyDescriptor.getWriteMethod().invoke(obj, Short.parseShort(value.toString()));
					} else if (fieldTypeName.equals("java.lang.Float")) {
						propertyDescriptor.getWriteMethod().invoke(obj, Float.parseFloat(value.toString()));
					} else if (fieldTypeName.equals("java.lang.Double")) {
						propertyDescriptor.getWriteMethod().invoke(obj, Double.parseDouble(value.toString()));
					} else if (fieldTypeName.equals("java.math.BigDecimal")) {
						propertyDescriptor.getWriteMethod().invoke(obj,
								BigDecimal.valueOf(Long.parseLong(value.toString())));
					} else if (fieldTypeName.equals("java.util.Date")) {
						propertyDescriptor.getWriteMethod().invoke(obj, (Date) value);
					}
				}
			}
		}
		return obj;
	}

	/**
	 * 根据class从DfLogSearchDao.xml中获取对应的resultMap里column与property的关系,key是column
	 * 
	 * @param clazz
	 * @return
	 */
	private static Map<String, String> getColumn2FiledMap(Class<?> clazz) {
		Map<String, String> map = new HashMap<String, String>();
		SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
		ResultMap resultMap = sqlSessionFactory.getConfiguration().getResultMap(clazz.getSimpleName() + "Map");
		List<ResultMapping> mapping = resultMap.getResultMappings();
		for (ResultMapping mapp : mapping) {
			map.put(mapp.getColumn().toLowerCase(), mapp.getProperty());
		}
		return map;

	}

	/**
	 * 执行count的sql并将结果和计算的last的值set到page对象中
	 * 
	 * @param page
	 * @param sql
	 * @throws Exception
	 */
	public <T> void getCount(Page<T> page, String sql) throws Exception {
		try {
			conn = getConnection();
			logger.info("连接数据中心日志库成功--------------------------");
			st = conn.createStatement();
			logger.info("开始执行查询日志总条数sql={}", sql);
			rs = st.executeQuery(sql);
			String countStr = null;
			while (rs.next()) {
				countStr = rs.getObject(1).toString();
				break;
			}
			if (countStr == null || countStr.trim().equals("")) {
				logger.info("获取数据中心日志总条数成功,总共0条日志,sql={}", sql);
				page.setCount(0l);
				page.setLast(1);
			} else {
				Long count = Long.valueOf(countStr);
				logger.info("获取数据中心日志总条数成功,总共{}条日志,sql={}", count, sql);
				page.setCount(count);
				page.setLast(getLastPageNum(count.intValue(), page.getPageSize()));
			}
		} finally {
			closeConn();
		}
	}

	private int getLastPageNum(int totalCount, int pageSize) {
		int pageNum = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			pageNum++;
		}
		return pageNum;
	}

}
