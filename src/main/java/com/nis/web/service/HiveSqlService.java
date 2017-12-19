package com.nis.web.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.nis.domain.Page;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.HiveDataSource;
import com.nis.util.HiveJDBC;
import com.nis.util.StringUtil;
import com.nis.util.redis.SaveRedisThread;

public class HiveSqlService {
	private final static Logger logger = Logger.getLogger(HiveJDBC.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static ResultSet getResultSet(Page page, Object bean, String tableName,
			Map<String, Map<String, String>> col2col, String orderBy, String searchActiveSys) throws Exception {
		tableName = tableName.toLowerCase();
		tableName = Configurations.getStringProperty(tableName, "t_" + tableName).trim();
		String showColmun = getFiledsSql(bean.getClass().getSimpleName(), page.getFields());
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
		sql.append(" select " + sqlTrim + "  from " + tableName + " t where 1=1  ");
		if (bean != null) {
			Class<?> clazz = bean.getClass();
			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				// 获取所有的字段包括public,private,protected,private
				// Field[] fields = bean.getClass().getDeclaredFields();
				Field[] fields = clazz.getDeclaredFields();
				Long foundTimePartStart = null;
				Long foundTimePartEnd = null;
				for (int i = 0; i < fields.length; i++) {
					Field f = fields[i];
					String key = f.getName();// 获取字段名
					if (f.getType().getName().equals("java.lang.String") && key.startsWith("search")) {
						Object value = getFieldValue(bean, key);
						if (value != null) {
							setFieldValue(bean, key, value.toString().trim());
							if (key.endsWith("Time")) {// 日期开始或结束的字段
								if (col2col.containsKey(key)) {
									value = sdf.parse(value.toString().trim()).getTime() / 1000;
									if (key.toLowerCase().equals("searchfoundstarttime")) {
										foundTimePartStart = Long.parseLong(value.toString()) / 3600;
									}
									if (key.toLowerCase().equals("searchfoundendtime")) {
										foundTimePartEnd = Long.parseLong(value.toString()) / 3600;
									}
									if (col2col.get(key).get("start") != null) {
										// sql.append(" and " +
										// filedAndColumnMap.get(col2col.get(key).get("start"))
										// + ">=to_date('" +
										// value.toString().trim()
										// + "','yyyy-mm-dd HH24:mi:ss')");
										sql.append(" and " + filedAndColumnMap.get(col2col.get(key).get("start")) + ">="
												+ value + "L");
									} else {
										// sql.append(" and " +
										// filedAndColumnMap.get(col2col.get(key).get("end"))
										// + "<=to_date('" +
										// value.toString().trim()
										// + "','yyyy-mm-dd HH24:mi:ss')");
										sql.append(" and " + filedAndColumnMap.get(col2col.get(key).get("end")) + "<"
												+ value + "L");
									}
								}
							} else {
								if (key.toLowerCase().startsWith("search")) {
									key = key.replace("search", "");
									key = key.substring(0, 1).toLowerCase() + key.substring(1);
								}

								if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& (key.toLowerCase().equals("cfgid")
												|| key.toLowerCase().equals("entranceid"))) {
									sql.append(
											" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim() + "L");
								} else if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& (key.toLowerCase().equals("protocol") || key.toLowerCase().equals("serverip")
												|| key.toLowerCase().equals("clientip")
												|| key.toLowerCase().equals("url")
												|| key.toLowerCase().equals("mailfrom")
												|| key.toLowerCase().equals("mailto")
												|| key.toLowerCase().equals("encryptmode")
												|| key.toLowerCase().equals("exprotocol")
												|| key.toLowerCase().equals("cljip"))) {
									sql.append(" and " + filedAndColumnMap.get(key) + "='" + value.toString().trim()
											+ "'");
								} else if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& key.toLowerCase().equals("servicetype")) {
									sql.append(" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim());
								}
							}
						}

					}
				}

				//if (null != searchActiveSys && !searchActiveSys.equals(Constants.ACTIVESYS_A)) {// B版数据库才有found_time_partition字段,A版毛衣found_time_partition分区字段
				if (null != searchActiveSys){//为A版日志库添加分区字段
					if (null != foundTimePartStart) {
						sql.append(" and found_time_partition>=" + foundTimePartStart + "L");
					}
					if (null != foundTimePartEnd) {
						sql.append(" and found_time_partition<" + foundTimePartEnd + "L");
					}
				}

			}
		}
		Integer startNum = (page.getPageNo() - 1) * page.getPageSize() + 1;
		Integer endNum = startNum - 1 + page.getPageSize();
		// sql.append(" order by " + orderBy + " limit 10000) t1) t2 where
		// row_Num between " + startNum + " and " + endNum);
		sql.append("  limit " + Constants.EVERY_GETHIVEDATANUM);
		logger.info("获取数据中心日志sql===================" + sql);
		// ResultSet query = HiveJDBC.query(sql.toString());
		ResultSet query = HiveDataSource.query(sql.toString(), searchActiveSys);
		logger.info("获取数据中心日志成功");
		return query;
	}

	public static Long getHivePageCount(Object bean, String countKey, String tableName,
			Map<String, Map<String, String>> col2col, String searchActiveSys) throws Exception {
		tableName = tableName.toLowerCase();
		tableName = Configurations.getStringProperty(tableName, "t_" + tableName).trim();
		StringBuffer countSql = new StringBuffer();
		Map<String, String> filedAndColumnMap = getFiledAndColumnMap(bean.getClass());
		countSql.append("select count(1) from " + tableName + " where 1=1 ");
		if (bean != null) {
			Class<?> clazz = bean.getClass();
			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				// 获取所有的字段包括public,private,protected,private
				// Field[] fields = bean.getClass().getDeclaredFields();
				Field[] fields = clazz.getDeclaredFields();
				Long foundTimePartStart = null;
				Long foundTimePartEnd = null;
				for (int i = 0; i < fields.length; i++) {
					Field f = fields[i];
					String key = f.getName();// 获取字段名
					if (f.getType().getName().equals("java.lang.String") && key.startsWith("search")) {
						Object value = getFieldValue(bean, key);
						if (value != null) {
							setFieldValue(bean, key, value.toString().trim());
							if (key.endsWith("Time")) {// 日期开始或结束的字段
								if (col2col.containsKey(key)) {
									value = sdf.parse(value.toString().trim()).getTime() / 1000;
									if (key.toLowerCase().equals("searchfoundstarttime")) {
										foundTimePartStart = Long.parseLong(value.toString()) / 3600;
									}
									if (key.toLowerCase().equals("searchfoundendtime")) {
										foundTimePartEnd = Long.parseLong(value.toString()) / 3600;
									}
									if (col2col.get(key).get("start") != null) {
										countSql.append(" and " + filedAndColumnMap.get(col2col.get(key).get("start"))
												+ ">=" + value + "L");
									} else {
										countSql.append(" and " + filedAndColumnMap.get(col2col.get(key).get("end"))
												+ "<" + value + "L");
									}
								}
							} else {
								if (key.toLowerCase().startsWith("search")) {
									key = key.replace("search", "");
									key = key.substring(0, 1).toLowerCase() + key.substring(1);
								}

								if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& (key.toLowerCase().equals("cfgid")
												|| key.toLowerCase().equals("entranceid"))) {
									countSql.append(
											" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim() + "L");
								} else if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& (key.toLowerCase().equals("protocol") || key.toLowerCase().equals("serverip")
												|| key.toLowerCase().equals("clientip")
												|| key.toLowerCase().equals("url")
												|| key.toLowerCase().equals("mailfrom")
												|| key.toLowerCase().equals("mailto")
												|| key.toLowerCase().equals("encryptmode")
												|| key.toLowerCase().equals("exprotocol")
												|| key.toLowerCase().equals("cljip"))) {
									countSql.append(" and " + filedAndColumnMap.get(key) + "='"
											+ value.toString().trim() + "'");
								} else if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& key.toLowerCase().equals("servicetype")) {
									countSql.append(
											" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim());
								}
							}
						}

					}
				}
				//if (null != searchActiveSys && !searchActiveSys.equals(Constants.ACTIVESYS_A)) {// B版数据库才有found_time_partition字段,A版毛衣found_time_partition分区字段
				if (null != searchActiveSys){
					if (null != foundTimePartStart) {
						countSql.append(" and found_time_partition>=" + foundTimePartStart + "L");
					}
					if (null != foundTimePartEnd) {
						countSql.append(" and found_time_partition<" + foundTimePartEnd + "L");
					}
				}
			}
		}
		logger.info("获取数据中心日志总条数sql==================" + countSql.toString());
		// ResultSet countRs = HiveJDBC.query(countSql.toString());
		ResultSet countRs = HiveDataSource.query(countSql.toString(), searchActiveSys);
		String countStr = null;
		while (countRs.next()) {
			countStr = countRs.getObject(1).toString();
			break;
		}
		if (countStr == null || countStr.equals("")) {
			logger.info("获取数据中心日志总条数成功总共===================0条配置");
			return 0l;
		}
		Long count = Long.valueOf(countStr);
		logger.info("获取数据中心日志总条数成功总共===================" + count + "条配置");
		// HiveJDBC.closeConn();
		if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
			new SaveRedisThread(countKey, count, Constants.HIVE_EXPIRE).start();
		}
		return count;
	}

	public static ResultSet getResultSet2(Page page, Object bean, String tableName,
			Map<String, Map<String, String>> col2col, String orderBy, String searchActiveSys) throws Exception {
		tableName = tableName.toLowerCase();
		tableName = Configurations.getStringProperty(tableName, "t_" + tableName).trim();
		String showColmun = getFiledsSql(bean.getClass().getSimpleName(), page.getFields());
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
		sql.append("select " + sqlTrim + " from(select " + sqlTrim + ",row_Num from(select " + sqlTrim
				+ ",row_number() over() as row_Num from " + tableName + " t where 1=1  ");
		if (bean != null) {
			Class<?> clazz = bean.getClass();
			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				// 获取所有的字段包括public,private,protected,private
				// Field[] fields = bean.getClass().getDeclaredFields();
				Field[] fields = clazz.getDeclaredFields();
				Long foundTimePartStart = null;
				Long foundTimePartEnd = null;
				for (int i = 0; i < fields.length; i++) {
					Field f = fields[i];
					String key = f.getName();// 获取字段名
					if (f.getType().getName().equals("java.lang.String") && key.startsWith("search")) {
						Object value = getFieldValue(bean, key);
						if (value != null) {
							setFieldValue(bean, key, value.toString().trim());
							if (key.endsWith("Time")) {// 日期开始或结束的字段
								if (col2col.containsKey(key)) {
									value = sdf.parse(value.toString().trim()).getTime() / 1000;
									if (key.toLowerCase().equals("searchfoundstarttime")) {
										foundTimePartStart = Long.parseLong(value.toString()) / 3600;
									}
									if (key.toLowerCase().equals("searchfoundendtime")) {
										foundTimePartEnd = Long.parseLong(value.toString()) / 3600;
									}
									if (col2col.get(key).get("start") != null) {
										// sql.append(" and " +
										// filedAndColumnMap.get(col2col.get(key).get("start"))
										// + ">=to_date('" +
										// value.toString().trim()
										// + "','yyyy-mm-dd HH24:mi:ss')");
										sql.append(" and " + filedAndColumnMap.get(col2col.get(key).get("start")) + ">="
												+ value + "L");
									} else {
										// sql.append(" and " +
										// filedAndColumnMap.get(col2col.get(key).get("end"))
										// + "<=to_date('" +
										// value.toString().trim()
										// + "','yyyy-mm-dd HH24:mi:ss')");
										sql.append(" and " + filedAndColumnMap.get(col2col.get(key).get("end")) + "<"
												+ value + "L");
									}
								}
							} else {
								if (key.toLowerCase().startsWith("search")) {
									key = key.replace("search", "");
									key = key.substring(0, 1).toLowerCase() + key.substring(1);
								}

								if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& (key.toLowerCase().equals("cfgid")
												|| key.toLowerCase().equals("entranceid"))) {
									sql.append(
											" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim() + "L");
								} else if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& (key.toLowerCase().equals("protocol") || key.toLowerCase().equals("serverip")
												|| key.toLowerCase().equals("clientip")
												|| key.toLowerCase().equals("cljip"))) {
									sql.append(" and " + filedAndColumnMap.get(key) + "='" + value.toString().trim()
											+ "'");
								} else if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& key.toLowerCase().equals("servicetype")) {
									sql.append(" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim());
								}
							}
						}

					}
				}
				if (null != searchActiveSys && !searchActiveSys.equals(Constants.ACTIVESYS_A)) {// B版数据库才有found_time_partition字段,A版毛衣found_time_partition分区字段
					if (null != foundTimePartStart) {
						sql.append(" and found_time_partition>=" + foundTimePartStart + "L");
					}
					if (null != foundTimePartEnd) {
						sql.append(" and found_time_partition<" + foundTimePartEnd + "L");
					}
				}

			}
		}
		Integer startNum = (page.getPageNo() - 1) * page.getPageSize() + 1;
		Integer endNum = startNum - 1 + page.getPageSize();
		sql.append(
				" order by " + orderBy + " limit 10000) t1) t2 where row_Num between " + startNum + " and " + endNum);
		logger.info("获取数据中心日志sql===================" + sql);
		ResultSet query = HiveJDBC.query(sql.toString(), searchActiveSys);
		logger.info("获取数据中心日志成功");
		return query;
	}

	public static Long getHivePageCount2(Object bean, String tableName, Map<String, Map<String, String>> col2col,
			String searchActiveSys) throws Exception {
		tableName = tableName.toLowerCase();
		tableName = Configurations.getStringProperty(tableName, "t_" + tableName).trim();
		StringBuffer countSql = new StringBuffer();
		Map<String, String> filedAndColumnMap = getFiledAndColumnMap(bean.getClass());
		countSql.append("select count(1) from " + tableName + " where 1=1 ");
		if (bean != null) {
			Class<?> clazz = bean.getClass();
			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				// 获取所有的字段包括public,private,protected,private
				// Field[] fields = bean.getClass().getDeclaredFields();
				Field[] fields = clazz.getDeclaredFields();
				Long foundTimePartStart = null;
				Long foundTimePartEnd = null;
				for (int i = 0; i < fields.length; i++) {
					Field f = fields[i];
					String key = f.getName();// 获取字段名
					if (f.getType().getName().equals("java.lang.String") && key.startsWith("search")) {
						Object value = getFieldValue(bean, key);
						if (value != null) {
							setFieldValue(bean, key, value.toString().trim());
							if (key.endsWith("Time")) {// 日期开始或结束的字段
								if (col2col.containsKey(key)) {
									value = sdf.parse(value.toString().trim()).getTime() / 1000;
									if (key.toLowerCase().equals("searchfoundstarttime")) {
										foundTimePartStart = Long.parseLong(value.toString()) / 3600;
									}
									if (key.toLowerCase().equals("searchfoundendtime")) {
										foundTimePartEnd = Long.parseLong(value.toString()) / 3600;
									}
									if (col2col.get(key).get("start") != null) {
										countSql.append(" and " + filedAndColumnMap.get(col2col.get(key).get("start"))
												+ ">=" + value + "L");
									} else {
										countSql.append(" and " + filedAndColumnMap.get(col2col.get(key).get("end"))
												+ "<" + value + "L");
									}
								}
							} else {
								if (key.toLowerCase().startsWith("search")) {
									key = key.replace("search", "");
									key = key.substring(0, 1).toLowerCase() + key.substring(1);
								}

								if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& (key.toLowerCase().equals("cfgid")
												|| key.toLowerCase().equals("entranceid"))) {
									countSql.append(
											" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim() + "L");
								} else if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& (key.toLowerCase().equals("protocol") || key.toLowerCase().equals("serverip")
												|| key.toLowerCase().equals("clientip")
												|| key.toLowerCase().equals("cljip"))) {
									countSql.append(" and " + filedAndColumnMap.get(key) + "='"
											+ value.toString().trim() + "'");
								} else if (!value.toString().trim().equals("") && filedAndColumnMap.containsKey(key)
										&& key.toLowerCase().equals("servicetype")) {
									countSql.append(
											" and " + filedAndColumnMap.get(key) + "=" + value.toString().trim());
								}
							}
						}

					}
				}
				if (null != searchActiveSys && !searchActiveSys.equals(Constants.ACTIVESYS_A)) {// B版数据库才有found_time_partition字段,A版毛衣found_time_partition分区字段
					if (null != foundTimePartStart) {
						countSql.append(" and found_time_partition>=" + foundTimePartStart + "L");
					}
					if (null != foundTimePartEnd) {
						countSql.append(" and found_time_partition<" + foundTimePartEnd + "L");
					}
				}
			}
		}
		logger.info("获取数据中心日志总条数sql==================" + countSql.toString());
		ResultSet countRs = HiveJDBC.query(countSql.toString(), searchActiveSys);
		String countStr = null;
		while (countRs.next()) {
			countStr = countRs.getObject(1).toString();
			break;
		}
		if (countStr == null || countStr.equals("")) {
			logger.info("获取数据中心日志总条数成功总共===================0条配置");
			return 0l;
		}
		Long count = Long.valueOf(countStr);
		logger.info("获取数据中心日志总条数成功总共===================" + count + "条配置");
		HiveJDBC.closeConn();
		return count;
	}

	public static String getFiledsSql(String mapName, String fileds) throws Exception {
		String[] fieldsColoumn = null;
		String orderByStr = "";
		// 所有字段名
		List<String> columnList = new ArrayList<String>();
		// 所有属性名
		List<String> propertyList = new ArrayList<String>();
		// 属性名称为key，字段名称为value
		Map<String, String> columnMap = new HashMap<String, String>();

		if (!StringUtil.isBlank(fileds)) {
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

	public static Map<String, String> getFiledAndColumnMap(Class clazz) {
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
	 * 利用发射调用bean.set方法将value设置到字段
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

	/**
	 * 比较开始时间或者结束时间是否比当前系统时间早48小时
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 * @throws ParseException
	 */
	public static boolean ifTimeGreaterThan48(String startTime, String endTime) throws Exception {
		logger.info("ifTimeGreaterThan48方法开始" + System.currentTimeMillis());
		if (null != startTime && !startTime.equals("") && null != endTime && !endTime.equals("")) {// 开始和结束时间都不为空
			Date startDate = sdf.parse(startTime);
			Date endDate = sdf.parse(endTime);
			if (startDate.getTime() < endDate.getTime()) {// 开始时间比结束时间早
				logger.info("ifTimeGreaterThan48方法结束" + System.currentTimeMillis());
				return gt48(endTime);
			} else {// 开始时间比结束时间晚,不符合一般情况
				logger.info("ifTimeGreaterThan48方法结束" + System.currentTimeMillis());
				return false;
			}
		} else if (null != endTime && !endTime.equals("")) {// 开始时间为空,结束时间不为空
			logger.info("ifTimeGreaterThan48方法结束" + System.currentTimeMillis());
			return gt48(endTime);
		} else if (null != startTime && !startTime.equals("")) {// 结束时间为空,开始时间不为空
			logger.info("ifTimeGreaterThan48方法结束" + System.currentTimeMillis());
			return gt48(startTime);
		} else {// 开始和结束时间都为空
			logger.info("ifTimeGreaterThan48方法结束" + System.currentTimeMillis());
			return false;
		}

	}

	public static boolean gt48(String eqTime) throws ParseException {
		logger.info("gt48方法开始" + System.currentTimeMillis());
		Date eqDate = sdf.parse(eqTime);
		Long dateNum = eqDate.getTime();
		Long currentDate = new Date().getTime();
		Long time = 0l;
		if (dateNum < currentDate) {
			time = currentDate - dateNum;// 获取结束时间与当前系统时间的时间差毫秒数
		} else {
			logger.info("gt48方法结束" + System.currentTimeMillis());
			return false;// 结束时间比当前系统时间晚,不符合从数据中心查询数据要求(当前日期48小时前数据从数据中心查询)
		}
		double hours = time.doubleValue() / (1000 * 60 * 60);
		Long datacenterTime = Constants.DATACENTER_TIME;
		double doubleValue = datacenterTime.doubleValue();
		if (hours > doubleValue) {
			logger.info("gt48方法结束" + System.currentTimeMillis());
			return true;// 符合要求
		} else {
			logger.info("gt48方法结束" + System.currentTimeMillis());
			return false;
		}

	}

	public static void main(String[] args) {
		Long datacenterTime = Constants.DATACENTER_TIME;
		double doubleValue = datacenterTime.doubleValue();
		System.out.println(doubleValue);
	}
}
