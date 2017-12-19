package com.nis.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.nis.domain.Page;
import com.nis.util.redis.SaveRedisListThread;
import com.nis.web.service.SpringContextHolder;

public class HiveJDBC {
	private final static Logger logger = Logger.getLogger(HiveJDBC.class);
	static Connection conn = null;
	static ResultSet rs = null;
	static Statement st = null;
	static Properties prop = new Properties();
	static String driverName = "";
	static String url = "";
	static String username = "";
	static String password = "";
	static {
		try {
			prop.load(Configurations.class.getResourceAsStream("/jdbc.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getConn(String searchActiveSys) throws Exception {

		if (null != searchActiveSys && searchActiveSys.equals("4")) {
			driverName = prop.getProperty("jdbc.hiveA.driver").trim();
			url = prop.getProperty("jdbc.hiveA.url").trim();
			username = prop.getProperty("jdbc.hiveA.username").trim();
			password = prop.getProperty("jdbc.hiveA.password").trim();
		} else {
			driverName = prop.getProperty("jdbc.hiveB.driver").trim();
			url = prop.getProperty("jdbc.hiveB.url").trim();
			username = prop.getProperty("jdbc.hiveB.username").trim();
			password = prop.getProperty("jdbc.hiveB.password").trim();
		}
		Class.forName(driverName);
		conn = DriverManager.getConnection(url, username, password);

	}

	public static ResultSet query(String sql, String searchActiveSys) throws Exception {
		logger.info("开始连接数据中心日志库--------------------------");
		getConn(searchActiveSys);
		logger.info("连接数据中心日志库成功--------------------------");
		st = conn.createStatement();
		if (null != searchActiveSys && searchActiveSys.equals("4")) {
			st.execute("use xa_dfbhit_hive");
		} else {
			st.execute("use xa_z2_mesalog_hive");
		}
		rs = st.executeQuery(sql);
		return rs;
	}

	public static void closeConn() {
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

	public static void main(String[] args) throws SQLException {
		try {
			// System.out.println(sdf.parse("2016-09-10 10:20:22").getTime());
			// ResultSet rs = query("");
			Map map = new HashMap();
			map.put("time", new Date());
			map.put("index", 1);
			for (Object key : map.keySet()) {
				Object obj = map.get(key);
				System.out.println(obj);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public static Map<String, List> tableMapping(Page page, String redisKey, ResultSet rs, Class entityClass,
			Object... obj) throws Exception {
		Map<String, List> mapList = new HashMap<String, List>();
		Map<String, String> filedAndColumnMap = getColumn2FiledMap(entityClass);
		List<String> listString = new ArrayList<String>();
		List listObject = new ArrayList();
		List<String> columnList = null;
		if (null != obj && obj.length > 0) {
			columnList = new ArrayList<String>();
			for (int i = 0; i < obj.length; i++) {
				columnList.add(obj[i].toString().toLowerCase());
			}
		}
		// ResultSet rs = HiveJDBC.query(sql.toString());
		ResultSetMetaData metaData = rs.getMetaData();
		while (rs.next()) {
			Map map = new HashMap();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				Object value = rs.getObject(i);
				String filedName = filedAndColumnMap.get(metaData.getColumnName(i).toString().toLowerCase());
				if (null != value) {
					if (null != columnList && columnList.contains(filedName.toLowerCase())) {
						long time = 0l;
						if (null != value && !value.toString().equals("")) {
							time = Long.parseLong(value.toString());
						}
						map.put(filedName, new Date(time * 1000));
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
		HiveJDBC.closeConn();
		if (null == listString || listString.size() == 0 || null == listObject || listObject.size() == 0) {
			return null;
		} else {
			if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
				new SaveRedisListThread(redisKey, listString, Constants.HIVE_EXPIRE).start();
			}
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
			mapList.put("str", new ArrayList());
		}
		if (listObject.size() >= startNum) {
			if (listObject.size() >= endNum) {
				mapList.put("obj", listObject.subList(startNum, endNum));
			} else {
				mapList.put("obj", listObject.subList(startNum, listObject.size()));
			}

		} else {
			mapList.put("obj", new ArrayList());
		}
		return mapList;
	}

	public static Object map2Obj(Map map, Class type) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		Object obj = type.newInstance();
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
			String name = propertyDescriptor.getName();
			String fieldTypeName = propertyDescriptor.getPropertyType().getName();
			if (map.containsKey(name)) {
				Object value = map.get(name);
				if (null != value && !value.equals("")) {
					if (fieldTypeName.equals("java.lang.String")) {
						propertyDescriptor.getWriteMethod().invoke(obj, new String[] { value.toString() });
					} else if (fieldTypeName.equals("java.lang.Integer")) {
						propertyDescriptor.getWriteMethod().invoke(obj,
								new Integer[] { Integer.parseInt(value.toString()) });
						// propertyDescriptor.getWriteMethod().invoke(obj, new
						// Integer[] { 0 });
					} else if (fieldTypeName.equals("java.lang.Long")) {
						propertyDescriptor.getWriteMethod().invoke(obj,
								new Long[] { Long.parseLong(value.toString()) });
						// propertyDescriptor.getWriteMethod().invoke(obj, new
						// Long[] { 0l });
					} else if (fieldTypeName.equals("java.lang.Boolean")) {
						propertyDescriptor.getWriteMethod().invoke(obj,
								new Boolean[] { Boolean.parseBoolean(value.toString()) });
					} else if (fieldTypeName.equals("java.lang.Character")) {
						propertyDescriptor.getWriteMethod().invoke(obj, value.toString().toCharArray());
					} else if (fieldTypeName.equals("java.lang.Byte")) {
						propertyDescriptor.getWriteMethod().invoke(obj, value.toString().getBytes());
					} else if (fieldTypeName.equals("java.lang.Short")) {
						propertyDescriptor.getWriteMethod().invoke(obj,
								new Short[] { Short.parseShort(value.toString()) });
					} else if (fieldTypeName.equals("java.lang.Float")) {
						propertyDescriptor.getWriteMethod().invoke(obj,
								new Float[] { Float.parseFloat(value.toString()) });
					} else if (fieldTypeName.equals("java.lang.Double")) {
						propertyDescriptor.getWriteMethod().invoke(obj,
								new Double[] { Double.parseDouble(value.toString()) });
					} else if (fieldTypeName.equals("java.math.BigDecimal")) {
						propertyDescriptor.getWriteMethod().invoke(obj,
								new BigDecimal[] { BigDecimal.valueOf(Long.parseLong(value.toString())) });
						// propertyDescriptor.getWriteMethod().invoke(obj, new
						// BigDecimal[] { new BigDecimal(0) });
					} else if (fieldTypeName.equals("java.util.Date")) {
						propertyDescriptor.getWriteMethod().invoke(obj, new Date[] { (Date) value });
					}
				}

			}
		}
		return obj;

	}

	public static Map<String, String> getColumn2FiledMap(Class clazz) {
		Map<String, String> map = new HashMap<String, String>();
		SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
		ResultMap resultMap = sqlSessionFactory.getConfiguration().getResultMap(clazz.getSimpleName() + "Map");
		List<ResultMapping> mapping = resultMap.getResultMappings();
		for (ResultMapping mapp : mapping) {
			map.put(mapp.getColumn().toLowerCase(), mapp.getProperty());
		}
		return map;

	}

	public static void close() {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
