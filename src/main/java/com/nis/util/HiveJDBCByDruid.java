package com.nis.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.nis.domain.Page;
import com.nis.web.service.SpringContextHolder;
/**
 * 
 * <p>Title: HiveJDBCByDruid</p>
 * <p>Description: 使用druid连接池对hive进行查询并解析结果</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年8月20日 
 *
 */
public class HiveJDBCByDruid {
	private final static Logger logger = LoggerFactory.getLogger(HiveJDBCByDruid.class);
	static DruidDataSource datasource = null;
	Connection conn = null;
	ResultSet rs = null;
	Statement st = null;

	public static Connection getConnection() throws SQLException {
		if (datasource == null) {
			datasource = (DruidDataSource) SpringContextHolder.getBean("HiveDataSourceByDruid");
		}
		return datasource.getConnection();
	}

	/**
	 * 将结果利用反射映射成对象集合
	 * 
	 * @param rs
	 *            resultSet
	 * @param entityClass
	 *            实体类
	 * @param obj
	 *            那些字段需要转换为date类型(由于数据中心表结构中没有date类型数据,其日期用long型表示,界面中需要显示yyyy-MM-dd
	 *            hh:mm:ss形式,所以需要将long转换为date)
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<Object>> tableMapping(Page<?> page, String redisKey, String sql, Class<?> entityClass, Object... obj)
			throws Exception {
		Map<String, List<Object>> mapList = new HashMap<String, List<Object>>();
		try {
			Map<String, String> filedAndColumnMap = getColumn2FiledMap(entityClass);
			List<Object> listString = new ArrayList<Object>();
			List<Object> listObject = new ArrayList<Object>();
			// 不从Object... obj中获取需要date类型的字段了,调用的时候容易漏写,改为反射获取date类型的字段
			List<String> columnList = getDateColumn(entityClass);
			conn = getConnection();
			logger.info("连接数据中心日志库成功--------------------------");
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			logger.info("执行查询语句成功sql={}", sql);
			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				Map<String,Object> map = new HashMap<String,Object>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					Object value = rs.getObject(i);
					String filedName = filedAndColumnMap.get(metaData.getColumnName(i).toString().toLowerCase());
					if (!StringUtil.isEmpty(value)) {
						// 如果是日期类型的属性需要把时间戳转换成日期，如果时间戳为0直接把值设置为null
						if (null != columnList && columnList.contains(filedName.toLowerCase())) {
							long time = 0l;
							time = Long.parseLong(value.toString());
							map.put(filedName, time == 0l ? null : new Date(time * 1000));
							// map.put(filedName, new
							// Date(Long.parseLong("1476583810000")));
						} else {
							map.put(filedName, value);
						}
					} else {
						map.put(filedName, null);
					}
				}
				listString.add(JsonMapper.toJsonString(map2Obj(map, entityClass)));
				listObject.add(map2Obj(map, entityClass));
			}
			logger.info("开始关闭数据中心连接");
			HiveDataSource.closeConn();
			if (null == listString || listString.size() == 0 || null == listObject || listObject.size() == 0) {
				return null;
			} else {
				//暂时没有往缓存写的操作
//				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
//					new SaveRedisListThread(redisKey, listString, Constants.HIVE_EXPIRE).start();
//				}
			}
			// sublist包前不包后,0-30实际获取的是0-29的数据
			Integer startNum = (page.getPageNo() - 1) * page.getPageSize();
			Integer endNum = startNum - 1 + page.getPageSize() + 1;
			if (listString.size() >= startNum) {
				if (listString.size() >= endNum) {
					mapList.put("str", listString.subList(startNum, endNum));
				} else {
					mapList.put("str", listString.subList(startNum, listString.size()));
				}

			} else {
				mapList.put("str", new ArrayList<Object>());
			}
			if (listObject.size() >= startNum) {
				if (listObject.size() >= endNum) {
					mapList.put("obj", listObject.subList(startNum, endNum));
				} else {
					mapList.put("obj", listObject.subList(startNum, listObject.size()));
				}
			} else {
				mapList.put("obj", new ArrayList<Object>());
			}

		} finally {
			closeConn();
		}
		return mapList;
	}

	public void closeConn() {
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
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static List<String> getDateColumn(Class<?> type) throws Exception {
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

	public static Object map2Obj(Map<String, Object> map, Class<?> beanClass) throws Exception {
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



	public static Map<String, String> getColumn2FiledMap(Class<?> clazz) {
		Map<String, String> map = new HashMap<String, String>();
		SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
		ResultMap resultMap = sqlSessionFactory.getConfiguration().getResultMap(clazz.getSimpleName() + "Map");
		List<ResultMapping> mapping = resultMap.getResultMappings();
		for (ResultMapping mapp : mapping) {
			map.put(mapp.getColumn().toLowerCase(), mapp.getProperty());
		}
		return map;

	}

}
