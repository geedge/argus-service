package com.nis.web.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcAsnRecord;
import com.nis.domain.restful.NtcConnRecordPercent;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.web.dao.impl.LocalLogJDBCByDruid;
import com.nis.web.dao.impl.LogJDBCByDruid;
import com.zdjizhi.utils.StringUtil;

/**
 * 从clickhouse或者hive中查询数据并set到page.list返回给界面
 * 
 * @author rkg
 *
 */
@Service
public class LogDataService {
	private final static Logger logger = LoggerFactory.getLogger(LogDataService.class);

	@Autowired
	private LogJDBCByDruid logJDBCByDruid;
	@Autowired
	private LocalLogJDBCByDruid localLogJDBCByDruid;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	private static Map<String, Map<String, String>> col2col = new HashMap<String, Map<String, String>>();
	static {
		Map<String, String> startMap = new HashMap<String, String>();
		startMap.put("start", "foundTime");
		col2col.put("searchFoundStartTime", startMap);
		Map<String, String> endMap = new HashMap<String, String>();
		endMap.put("end", "foundTime");
		col2col.put("searchFoundEndTime", endMap);
	}

	public NtcConnRecordPercent getNtcConnRecordPercent(NtcConnRecordPercent ntcConnRecordPercent) throws Exception {
		long startTime = sdf.parse(ntcConnRecordPercent.getSearchFoundStartTime().toString().trim()).getTime() / 1000;
		long endTime = sdf.parse(ntcConnRecordPercent.getSearchFoundEndTime().toString().trim()).getTime() / 1000;
		if (endTime - startTime < 0) {
			throw new RestServiceException("searchFoundStartTime() can not exceed searchFoundEndTime",
					RestBusinessCode.param_formate_error.getValue());
		}
		long second = endTime - startTime;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(s2c_pkt_num + s2c_pkt_num)*8/");
		sql.append(second);
		sql.append(" AS pps ,  SUM(c2s_byte_num + s2c_byte_num)*8/1024/1024/1024/");
		sql.append(second);
		sql.append(
				" AS bps  FROM tbs_ods_ntc_conn_record_log_local t WHERE found_time IN ( SELECT DISTINCT found_time FROM tbs_ods_ntc_conn_record_log_local WHERE  found_time >= ");
		sql.append(startTime);
		sql.append(" and found_time< ");
		sql.append(endTime);
		StringBuffer totalSql = new StringBuffer();
		totalSql.append(sql);
		totalSql.append(" and stream_dir in(1,2,3)) and stream_dir in(1,2,3)");
		sql.append(" and stream_dir in(1,2) ) and stream_dir in(1,2)");
		localLogJDBCByDruid.getNtcConnRecordPercentData(ntcConnRecordPercent, sql.toString(), false);
		localLogJDBCByDruid.getNtcConnRecordPercentData(ntcConnRecordPercent, totalSql.toString(), true);
		return ntcConnRecordPercent;
	}

	public void getNtcAsnRecord(Page page, NtcAsnRecord ntcAsnRecord) throws Exception {
		long startTime = sdf.parse(ntcAsnRecord.getSearchFoundStartTime().toString().trim()).getTime() / 1000;
		long endTime = sdf.parse(ntcAsnRecord.getSearchFoundEndTime().toString().trim()).getTime() / 1000;
		if (endTime - startTime < 0) {
			throw new RestServiceException("searchFoundStartTime() can not exceed searchFoundEndTime",
					RestBusinessCode.param_formate_error.getValue());
		}
		
		
		
		String asn = "s_asn  ";
		if (ntcAsnRecord.getSearchAsnType().trim().equals("1")) {
			asn = "d_asn  ";
		}

		long second = endTime - startTime;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(s2c_pkt_num + s2c_pkt_num)*8/");
		sql.append(second);
		sql.append(" AS pps ,  SUM(c2s_byte_num + s2c_byte_num)*8/1024/1024/");
		sql.append(second);
		sql.append(" AS bps,");
		sql.append(asn);
		sql.append(
				" asn FROM tbs_ods_ntc_conn_record_log_local t WHERE found_time IN ( SELECT DISTINCT found_time FROM tbs_ods_ntc_conn_record_log_local WHERE  found_time >= ");
		sql.append(startTime);
		sql.append(" and found_time< ");
		sql.append(endTime);
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(1) from (");
		countSql.append(sql);
		countSql.append(" ) group by ");
		countSql.append(asn+")");
		Integer startNum = (page.getPageNo() - 1) * page.getPageSize();
		Integer limitCount = startNum + page.getPageSize();

		sql.append(" limit " + limitCount + " ) group by "+asn+" 	order by bps  desc,pps desc  limit " + startNum + "," + page.getPageSize());

		localLogJDBCByDruid.getCount(page, countSql.toString());

		page.setList(localLogJDBCByDruid.getNtcAsnRecordData(sql.toString()));
	}

	/**
	 * 根据类名加对应的标识获取hive或者clickhouse中对应的表名
	 * 
	 * @param key
	 * @param defaultTableName
	 * @return
	 */
	private String getTableName(String key, String defaultTableName) {
		if (Constants.ISUSECLICKHOUSE) {
			key = key.replace("HiveTable", "ClickHouseTable");
		}
		return Configurations.getStringProperty(key, defaultTableName);
	}

	/**
	 * 根据page及obj对象中的属性值,利用反射拼接对应的查询sql(支持分页,排序) 和计算总量的sql并去对应的数据库中查询,
	 * 并将结果set到page对象中,返回给界面展示
	 * 
	 * @param page
	 * @param obj
	 * @throws Exception
	 */
	public <T> void getData(Page<T> page, Object obj) throws Exception {
		String className = obj.getClass().getSimpleName();
		String tableName = getTableName(className + "HiveTable", "");
		if (tableName == null || tableName.trim().equals("")) {
			throw new RuntimeException("日志类" + className + "对应的表名为空,请检查配置文件");
		}
		String orderBy = " order by ";
		if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
			orderBy = orderBy + Page.getOrderBySql(className, page.getOrderBy());
		} else {
			orderBy = orderBy + "found_Time desc ";
		}
		if (Constants.ISUSECLICKHOUSE) {
			getDataFromClickHouse(page, obj, tableName, className, orderBy.toLowerCase());
		} else {
			getDataFromHive(page, obj, tableName, className, orderBy.toLowerCase());
		}

	}

	/**
	 * 从clickhouse中查询数据,注意clickhouse区分大小写,目前和百分点商定都是用小写
	 * 
	 * @param page      里面含有pagesize和pageno,order by
	 * @param bean      日志类对象(从DfLogSearchDao.xml中获取对应的map,类名+Map),用来获取各个属性对应的数据库字段名
	 * @param tableName 表名
	 * @param className 类名
	 * @param orderBy   orderby条件
	 * @throws Exception
	 */
	private <T> void getDataFromClickHouse(Page<T> page, Object bean, String tableName, String className,
			String orderBy) throws Exception {
		tableName = tableName.toLowerCase();
		String showColmun = getFiledsSql(className, page.getFields());
		StringBuffer sql = new StringBuffer();
		Map<String, String> filedAndColumnMap = getFiledAndColumnMap(bean.getClass());
		if (null == showColmun || showColmun.equals("")) {
			for (String key : filedAndColumnMap.keySet()) {
				if (!filedAndColumnMap.get(key).toLowerCase().equals("id")) {
					sql.append(filedAndColumnMap.get(key) + ",");
				}
			}
		} else {
			sql.append(showColmun);
		}
		String sqlTrim = sql.toString().trim();
		if (sqlTrim.endsWith(",")) {
			sqlTrim = sqlTrim.substring(0, sqlTrim.length() - 1);
		}
		sql.setLength(0);
		sql.append(" select " + sqlTrim.toLowerCase() + "  from " + tableName.toLowerCase() + " t where ");
		StringBuffer whereFoundTime = new StringBuffer();
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(1) from " + tableName + " where ");

		StringBuffer whereSB = new StringBuffer();
		if (!StringUtil.isEmpty(bean)) {
			Class<?> clazz = bean.getClass();
			Map<String, String> filedsType = null;
			filedsType = getFiledsType(bean);
			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					// 现在gwall日志表结构中只有数值和字符串两种类型,数值都是int类型没有bigint所以不需要加L,
					Field f = fields[i];
					String key = f.getName();// 获取字段名
					if (f.getType().getName().equals("java.lang.String") && key.startsWith("search")) {
						Object value = getFieldValue(bean, key);
						if (!StringUtil.isEmpty(value)) {
							setFieldValue(bean, key, value.toString().trim());
							if (key.endsWith("Time")) {// 日期开始或结束的字段
								if (col2col.containsKey(key)) {
									value = sdf.parse(value.toString().trim()).getTime() / 1000;
									if (col2col.get(key).get("start") != null) {
										whereFoundTime.append(" and "
												+ filedAndColumnMap.get(col2col.get(key).get("start")).toLowerCase()
												+ ">=" + value);
									} else {
										whereFoundTime.append(" and "
												+ filedAndColumnMap.get(col2col.get(key).get("end")).toLowerCase() + "<"
												+ value);
									}
								}
							} else {
								if (key.toLowerCase().startsWith("search")) {
									key = key.replace("search", "");
									key = key.substring(0, 1).toLowerCase() + key.substring(1);
								}
								// clickhouse写法
								String type = filedsType.get(key).trim();
								if (type.equals("java.lang.String")) {
									String field = filedAndColumnMap.get(key).toLowerCase();
									if (field.equals("url") || field.equals("website")) {
										whereSB.append(" and " + field + " like '" + value.toString().trim() + "%'");
									} else {
										whereSB.append(" and " + field + "='"
												+ StringEscapeUtils.unescapeHtml4(value.toString().trim()) + "'");
									}
								} else if (type.equals("java.lang.Integer") || type.equals("int")
										|| type.equals("java.lang.Long") || type.equals("long")) {
									whereSB.append(" and " + filedAndColumnMap.get(key).toLowerCase() + "="
											+ value.toString().trim());
								}

							}
						}

					}
				}

			}
		}
		Integer startNum = (page.getPageNo() - 1) * page.getPageSize();
		StringBuffer foundTimeSql = new StringBuffer();
		foundTimeSql.append("select  found_time  from " + tableName + " where ");
		Integer limitCount = startNum + page.getPageSize();
		if (whereSB.length() == 0) {// 没有其他查询条件只有默认的found_time条件
			if (whereFoundTime.length() > 0) {
				int indexOf = whereFoundTime.indexOf("and") + "and".length();
				countSql.append(whereFoundTime.substring(indexOf));

				foundTimeSql
						.append(whereFoundTime.substring(indexOf) + orderBy.toLowerCase() + "  limit " + limitCount);
				sql.append(" found_time in(" + foundTimeSql + ") ");
			} else {
				throw new RuntimeException("从clickhouse的" + tableName + "表查询时,必须要有一个where条件");
			}
		} else {
			int foundIndexOf = whereFoundTime.append(whereSB).indexOf("and") + "and".length();
			countSql.append(whereFoundTime.substring(foundIndexOf));
			foundTimeSql
					.append(whereFoundTime.substring(foundIndexOf) + orderBy.toLowerCase() + "  limit " + limitCount);

			int indexOf = whereSB.indexOf("and") + "and".length();
			sql.append(whereSB.substring(indexOf) + " and found_time in(" + foundTimeSql + ") ");
		}
		sql.append(orderBy.toLowerCase() + "  limit " + startNum + "," + page.getPageSize());// clickhouse的分页与mysql相同

		if (tableName.toUpperCase().equals("TBS_ODS_NTC_CONN_RECORD_LOG_LOCAL")) {
			searchFromLocalCK(page, bean, sql, countSql);
		} else {
			searchFromDataCenter(page, bean, sql, countSql);
		}

	}

	/**
	 * 从hive中查询数据
	 * 
	 * @param page      里面含有pagesize和pageno,order by
	 * @param bean      日志类对象(从DfLogSearchDao.xml中获取对应的map,类名+Map),用来获取各个属性对应的数据库字段名
	 * @param tableName 表名
	 * @param className 类名
	 * @param orderBy   orderby条件
	 * @throws Exception
	 */
	private <T> void getDataFromHive(Page<T> page, Object bean, String tableName, String className, String orderBy)
			throws Exception {
		tableName = tableName.toLowerCase();
		String showColmun = getFiledsSql(className, page.getFields());
		StringBuffer sql = new StringBuffer();
		Map<String, String> filedAndColumnMap = getFiledAndColumnMap(bean.getClass());
		if (null == showColmun || showColmun.equals("")) {
			for (String key : filedAndColumnMap.keySet()) {
				if (!filedAndColumnMap.get(key).toLowerCase().equals("id")) {
					sql.append(filedAndColumnMap.get(key) + ",");
				}
			}
		} else {
			sql.append(showColmun);
		}
		String sqlTrim = sql.toString().trim();
		if (sqlTrim.endsWith(",")) {
			sqlTrim = sqlTrim.substring(0, sqlTrim.length() - 1);
		}
		sql.setLength(0);
		sql.append(" select " + sqlTrim.toLowerCase() + "  from (select " + sqlTrim.toLowerCase()
				+ ",row_number() over(partition by found_time_partition " + orderBy + ") as row_num from "
				+ tableName.toLowerCase() + " ");
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(1) from " + tableName + "  ");

		StringBuffer whereSB = new StringBuffer();
		if (!StringUtil.isEmpty(bean)) {
			Class<?> clazz = bean.getClass();
			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				// 获取所有的字段包括public,private,protected,private
				// Field[] fields = bean.getClass().getDeclaredFields();
				Field[] fields = clazz.getDeclaredFields();
				Long foundTimePartStart = null;
				Long foundTimePartEnd = null;
				for (int i = 0; i < fields.length; i++) {
					// 现在gwall日志表结构中只有数值和字符串两种类型,数值都是int类型没有bigint所以不需要加L,
					Field f = fields[i];
					String key = f.getName();// 获取字段名
					String typeName = f.getType().getName();
					if (f.getType().getName().equals("java.lang.String") && key.startsWith("search")) {
						Object value = getFieldValue(bean, key);
						if (!StringUtil.isEmpty(value)) {
							setFieldValue(bean, key, value.toString().trim());
							if (key.endsWith("Time")) {// 日期开始或结束的字段
								if (col2col.containsKey(key)) {
									Long partition = Long.parseLong(sdf2.format(sdf.parse(value.toString().trim())));
									value = sdf.parse(value.toString().trim()).getTime() / 1000;
									if (key.toLowerCase().equals("searchfoundstarttime")) {
										foundTimePartStart = partition;
									}
									if (key.toLowerCase().equals("searchfoundendtime")) {
										foundTimePartEnd = partition;
									}
									if (col2col.get(key).get("start") != null) {
										// sql.append(" and " +
										// filedAndColumnMap.get(col2col.get(key).get("start"))
										// + ">=to_date('" +
										// value.toString().trim()
										// + "','yyyy-mm-dd HH24:mi:ss')");
										whereSB.append(" and "
												+ filedAndColumnMap.get(col2col.get(key).get("start")).toLowerCase()
												+ ">=" + value);
									} else {
										// sql.append(" and " +
										// filedAndColumnMap.get(col2col.get(key).get("end"))
										// + "<=to_date('" +
										// value.toString().trim()
										// + "','yyyy-mm-dd HH24:mi:ss')");
										whereSB.append(" and "
												+ filedAndColumnMap.get(col2col.get(key).get("end")).toLowerCase() + "<"
												+ value);
									}
								}
							} else {
								if (key.toLowerCase().startsWith("search")) {
									key = key.replace("search", "");
									key = key.substring(0, 1).toLowerCase() + key.substring(1);
								}

								if (typeName.equals("java.lang.String")) {
									String field = filedAndColumnMap.get(key);
									if (field.equals("url")) {
										whereSB.append(" and " + field + " like '" + value.toString().trim() + "%'");
									} else {
										whereSB.append(" and " + field + "='" + value.toString().trim() + "'");
									}
								} else if (typeName.equals("java.lang.Integer") || typeName.equals("int")) {
									whereSB.append(
											" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim());

								} else if (typeName.equals("java.lang.Long") || typeName.equals("long")) {
									whereSB.append(
											" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim() + "L");
								}

							}
						}

					}
				}
				if (null != foundTimePartStart) {
					// sql.append(" and found_time_partition>=" + foundTimePartStart + "L");
					whereSB.append(" and found_time_partition>=" + foundTimePartStart);
				}
				if (null != foundTimePartEnd) {
					// sql.append(" and found_time_partition<" + foundTimePartEnd + "L");
					whereSB.append(" and found_time_partition<=" + foundTimePartEnd);
				}

			}
		}
		if (whereSB.length() > 0) {
			int indexOf = whereSB.indexOf("and") + "and".length();
			sql.append(" where " + whereSB.substring(indexOf));
			countSql.append(" where " + whereSB.substring(indexOf));

		}
		Integer startNum = (page.getPageNo() - 1) * page.getPageSize() + 1;
		Integer endNum = startNum - 1 + page.getPageSize();
		sql.append("  ) t   where row_Num between " + startNum + " and " + endNum);
		searchFromDataCenter(page, bean, sql, countSql);
	}

	/**
	 * 执行sql
	 * 
	 * @param page
	 * @param bean
	 * @param selSql
	 * @param countSql
	 * @throws Exception
	 */
	private <T> void searchFromDataCenter(Page<T> page, Object bean, StringBuffer selSql, StringBuffer countSql)
			throws Exception {
//		if (Constants.ISOPENLOGCOUNTANDLAST) {
		logJDBCByDruid.getCount(page, countSql.toString());
//		}
		if (page.getCount() > 0) {
			logJDBCByDruid.getTableData(page, selSql.toString(), bean.getClass());
			if (page.getLast() > 100) {
				page.setLast(100);
			}
		} else {
			logger.info("没有查询到数据,sql={}", countSql.toString());
		}

	}

	private <T> void searchFromLocalCK(Page<T> page, Object bean, StringBuffer selSql, StringBuffer countSql)
			throws Exception {
//		if (Constants.ISOPENLOGCOUNTANDLAST) {
		localLogJDBCByDruid.getCount(page, countSql.toString());
//		}
		if (page.getCount() > 0) {
			localLogJDBCByDruid.getTableData(page, selSql.toString(), bean.getClass());
			if (page.getLast() > 100) {
				page.setLast(100);
			}
		} else {
			logger.info("没有查询到数据,sql={}", countSql.toString());
		}

	}

	/**
	 * 利用反射获取class中各个属性的数据类型,key是属性名称,value是数据类型
	 * 
	 * @param obj
	 * @return
	 */
	private static Map<String, String> getFiledsType(Object obj) {
		Field[] fields = obj.getClass().getSuperclass().getDeclaredFields();
		Field[] superfields = obj.getClass().getDeclaredFields();
		Map<String, String> infoMap = new HashMap<String, String>();
		for (int i = 0; i < fields.length; i++) {
			infoMap.put(fields[i].getName(), fields[i].getType().toString().replace("class", ""));
		}
		for (int i = 0; i < superfields.length; i++) {
			infoMap.put(superfields[i].getName(), superfields[i].getType().toString().replace("class", ""));
		}
		return infoMap;
	}

	/**
	 * 将fileds中的字段根据DfLogSearchDao.xml中对应的resultMap转换为数据库中的字段
	 * 
	 * @param mapName
	 * @param         fileds与界面商定好的是传日志类中的对象名(界面没有表结构不知道对象属性对应的数据库字段名称是什么),不是数据库中的字段名
	 * @return
	 * @throws Exception
	 */
	private static String getFiledsSql(String mapName, String fileds) throws Exception {
		if (!StringUtil.isBlank(fileds)) {
			String[] fieldsColoumn = null;
			// 所有字段名
			List<String> columnList = new ArrayList<String>();
			// 所有属性名
			List<String> propertyList = new ArrayList<String>();
			// 属性名称为key，字段名称为value
			Map<String, String> columnMap = new HashMap<String, String>();
			// 解析Fileds的字段/属性名称
			fieldsColoumn = fileds.split(",");

			// 从resultMap中获取字段名称和属性名称
			if (fieldsColoumn != null) {
				SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
				ResultMap map = sqlSessionFactory.getConfiguration().getResultMap(mapName + "Map");
				List<ResultMapping> mapping = map.getResultMappings();
				for (ResultMapping mapp : mapping) {
					columnList.add(mapp.getColumn().toLowerCase());
					propertyList.add(mapp.getProperty());
					columnMap.put(mapp.getProperty(), mapp.getColumn());
				}
			}
			if (fieldsColoumn != null) {
				fileds = "";
				for (String column : fieldsColoumn) {
					if (!StringUtil.isBlank(column)) {
						column = column.trim();
						if (columnList.contains(column)) {
							fileds += "," + column;
						} else if (propertyList.contains(column)) {
							fileds += "," + columnMap.get(column).toString();
						}
					}
				}
				if (!StringUtil.isBlank(fileds)) {
					fileds = fileds.substring(1);
				}

			}

		}
		return fileds;
	}

	/**
	 * 根据class从DfLogSearchDao.xml中获取对应的resultMap里column与property的关系,key是property
	 * 
	 * @param clazz
	 * @return
	 */
	private static Map<String, String> getFiledAndColumnMap(Class<?> clazz) {
		Map<String, String> map = new HashMap<String, String>();
		SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
		ResultMap resultMap = sqlSessionFactory.getConfiguration().getResultMap(clazz.getSimpleName() + "Map");
		List<ResultMapping> mapping = resultMap.getResultMappings();
		for (ResultMapping mapp : mapping) {
			map.put(mapp.getProperty(), mapp.getColumn().toLowerCase());
		}
		return map;

	}

	/**
	 * 利用反射通过get方法获取bean中字段fieldName的值
	 * 
	 * @param bean
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private static Object getFieldValue(Object bean, String fieldName) throws Exception {
		StringBuffer result = new StringBuffer();
		String methodName = result.append("get").append(fieldName.substring(0, 1).toUpperCase())
				.append(fieldName.substring(1)).toString();

		Object rObject = null;
		Method method = null;

		@SuppressWarnings("rawtypes")
		Class[] classArr = new Class[0];
		method = bean.getClass().getMethod(methodName, classArr);
		rObject = method.invoke(bean, new Object[0]);

		return rObject;
	}

	/**
	 * 利用反射调用bean.set方法将value设置到字段
	 * 
	 * @param bean
	 * @param fieldName
	 * @param value
	 * @throws Exception
	 */
	private static void setFieldValue(Object bean, String fieldName, Object value) throws Exception {
		StringBuffer result = new StringBuffer();
		String methodName = result.append("set").append(fieldName.substring(0, 1).toUpperCase())
				.append(fieldName.substring(1)).toString();
		/**
		 * 利用发射调用bean.set方法将value设置到字段
		 */
		Class[] classArr = new Class[1];
		classArr[0] = "java.lang.String".getClass();
		Method method = bean.getClass().getMethod(methodName, classArr);
		method.invoke(bean, value);
	}

}
