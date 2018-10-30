package com.nis.web.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.nis.domain.Page;
import com.nis.util.Constants;
import com.nis.util.LogJDBCByDruid;
import com.zdjizhi.utils.StringUtil;
import com.nis.util.redis.SaveRedisThread;

public class HiveSqlService {
	private final static Logger logger = LoggerFactory.getLogger(HiveSqlService.class);
	static DruidDataSource datasource = null;
	Connection conn = null;
	ResultSet rs = null;
	Statement st = null;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

	private static Map<String, String> getFiledsType(Object o) {
		Field[] fields = o.getClass().getSuperclass().getDeclaredFields();
		Field[] superfields = o.getClass().getDeclaredFields();
		Map<String, String> infoMap = new HashMap<String, String>();
		for (int i = 0; i < fields.length; i++) {
			infoMap.put(fields[i].getName(), fields[i].getType().toString().replace("class", ""));
		}
		for (int i = 0; i < superfields.length; i++) {
			infoMap.put(superfields[i].getName(), superfields[i].getType().toString().replace("class", ""));
		}
		return infoMap;
	}

	public static String getSql(Page page, Object bean, String tableName, Map<String, Map<String, String>> col2col,
			String orderBy, String searchActiveSys) throws Exception {
		if (Constants.ISUSECLICKHOUSE) {
			return getSqlByClickHouse(page, bean, tableName, col2col, orderBy, searchActiveSys);
		} else {
			return getSqlByHive(page, bean, tableName, col2col, orderBy, searchActiveSys);
		}
	}

	public static String getSqlByClickHouse(Page page, Object bean, String tableName,
			Map<String, Map<String, String>> col2col, String orderBy, String searchActiveSys) throws Exception {
		tableName = tableName.toLowerCase();
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
		sql.append(" select " + sqlTrim.toLowerCase() + "  from " + tableName.toLowerCase() + " t ");
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
									Long partition = Long.parseLong(sdf2.format(sdf.parse(value.toString().trim())));
									value = sdf.parse(value.toString().trim()).getTime() / 1000;
									if (col2col.get(key).get("start") != null) {
										whereSB.append(" and "
												+ filedAndColumnMap.get(col2col.get(key).get("start")).toLowerCase()
												+ ">=" + value);
									} else {
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

								// clickhouse写法
								String type = filedsType.get(key).trim();
								if (type.equals("java.lang.String")) {
									String field = filedAndColumnMap.get(key).toLowerCase();
									if (field.equals("url")) {
										whereSB.append(" and " + field + " like '" + value.toString().trim() + "%'");
									} else {
										whereSB.append(" and " + field + "='" + value.toString().trim() + "'");
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
		if (whereSB.length() > 0) {
			int indexOf = whereSB.indexOf("and") + "and".length();
			sql.append(" where " + whereSB.substring(indexOf));
		}
		Integer startNum = (page.getPageNo() - 1) * page.getPageSize();
		if (orderBy.toLowerCase().contains("asc") || orderBy.toLowerCase().contains("desc")) {
			sql.append(" order by " + orderBy.toLowerCase());
		} else {
			sql.append(" order by " + orderBy.toLowerCase() + " desc");
		}
		sql.append("  limit " + startNum + "," + page.getPageSize());// clickhouse的分页与mysql相同

		return sql.toString();
	}

	public static String getSqlByHive(Page page, Object bean, String tableName,
			Map<String, Map<String, String>> col2col, String orderBy, String searchActiveSys) throws Exception {
		tableName = tableName.toLowerCase();
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
		if (orderBy.toLowerCase().contains("asc") || orderBy.toLowerCase().contains("desc")) {
			orderBy = " order by " + orderBy;
		} else {
			orderBy = " order by " + orderBy + " desc ";
		}
		sql.setLength(0);
		sql.append(" select " + sqlTrim.toLowerCase() + "  from (select " + sqlTrim.toLowerCase()
				+ ",row_number() over(partition by found_time_partition " + orderBy + ") as row_num from "
				+ tableName.toLowerCase() + " ");
		StringBuffer whereSB = new StringBuffer();
		if (!StringUtil.isEmpty(bean)) {
			Class<?> clazz = bean.getClass();
			Map<String, String> filedsType = null;
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
		}
		Integer startNum = (page.getPageNo() - 1) * page.getPageSize() + 1;
		Integer endNum = startNum - 1 + page.getPageSize();
		sql.append("  ) t   where row_Num between " + startNum + " and " + endNum);
		logger.info("获取数据中心日志sql==================={}", sql);
		return sql.toString();
	}

	public static Long getLogCount(Object bean, String tableName, Map<String, Map<String, String>> col2col) throws Exception {
		if (Constants.ISUSECLICKHOUSE) {
			return getLogCountFromClickHouse(bean, tableName, col2col);
		} else {
			return getLogCountFromHive(bean, tableName, col2col);
		}
	}

	public static Long getLogCountFromClickHouse(Object bean, String tableName,
			Map<String, Map<String, String>> col2col) throws Exception {
		tableName = tableName.toLowerCase();
		StringBuffer sql = new StringBuffer();
		Map<String, String> filedAndColumnMap = getFiledAndColumnMap(bean.getClass());
		sql.append("select count(1) from " + tableName + "  ");
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
									Long partition = Long.parseLong(sdf2.format(sdf.parse(value.toString().trim())));
									value = sdf.parse(value.toString().trim()).getTime() / 1000;
									if (col2col.get(key).get("start") != null) {
										whereSB.append(" and "
												+ filedAndColumnMap.get(col2col.get(key).get("start")).toLowerCase()
												+ ">=" + value);
									} else {
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

								// clickhouse写法
								String type = filedsType.get(key).trim();
								if (type.equals("java.lang.String")) {
									String field = filedAndColumnMap.get(key).toLowerCase();
									if (field.equals("url")) {
										whereSB.append(" and " + field + " like '" + value.toString().trim() + "%'");
									} else {
										whereSB.append(" and " + field + "='" + value.toString().trim() + "'");
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
		if (whereSB.length() > 0) {
			int indexOf = whereSB.indexOf("and") + "and".length();
			sql.append(" where " + whereSB.substring(indexOf));
		}
		logger.info("获取数据中心日志总条数sql==================" + sql.toString());
		Long count = new LogJDBCByDruid().getCount(sql.toString());
		return count;
	}

	public static Long getLogCountFromHive(Object bean, String tableName, Map<String, Map<String, String>> col2col)
			throws Exception {
		tableName = tableName.toLowerCase();
		StringBuffer sql = new StringBuffer();
		Map<String, String> filedAndColumnMap = getFiledAndColumnMap(bean.getClass());
		sql.append("select count(1) from " + tableName + "  ");
		StringBuffer whereSB = new StringBuffer();
		if (bean != null) {
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
		}
		logger.info("获取数据中心日志总条数sql==================" + sql.toString());
		Long count = new LogJDBCByDruid().getCount(sql.toString());
		return count;
	}

	public static String getFiledsSql(String mapName, String fileds) throws Exception {
		String[] fieldsColoumn = null;
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

	public static Map<String, String> getFiledAndColumnMap(Class<?> clazz) {
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
}
