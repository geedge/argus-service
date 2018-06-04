package com.nis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Title: ServiceAndRDBIndexReal</p>
 * <p>Description: 业务类型与各个表名的对应关系,业务类型与redisDBIndex的对应关系</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月30日 
 *
 */
public class ServiceAndRDBIndexReal {

	/**
	 * 第一个key是业务类型,第二个key是type(编译配置,分组配置,域配置)value是表名
	 */
	private static Map<Integer, Map<Integer, String>> sercieNameMap = new HashMap<Integer, Map<Integer, String>>();
	/**
	 * key是业务类型,value是业务类型对应的redisdbIndex
	 */
	private static Map<Integer, List<Integer>> serviceDBIndexmap = new HashMap<Integer, List<Integer>>();
	/**
	 * key是业务类型,value是业务类型对应的动作(action)
	 */
	private static Map<Integer, Integer> serviceActionMap = new HashMap<Integer, Integer>();
	static {
		String service = Configurations.getStringProperty("service", "");
		if (service != null && !service.trim().equals("")) {
			String[] split = service.split(";");
			for (String str : split) {
				String[] serviceAction = str.split(":");
				serviceActionMap.put(Integer.valueOf(serviceAction[0]), Integer.valueOf(serviceAction[1]));
				String type = Configurations.getStringProperty(serviceAction[0], "");
				if (type != null && !type.trim().equals("")) {
					Map<Integer, String> typeMap = new HashMap<Integer, String>();
					String[] typeArrs = type.split(";");
					for (String typeStr : typeArrs) {
						String[] typeArr = typeStr.split(":");
						String tableNameArr[] = typeArr[1].split(",");
						for (String tableName : tableNameArr) {
							typeMap.put(Integer.parseInt(typeArr[0]), tableName.toUpperCase());
						}
					}
					sercieNameMap.put(Integer.parseInt(serviceAction[0]), typeMap);
				}

			}
		}
		String serviceDBIndexs = Configurations.getStringProperty("serviceDBIndex", "");
		if (serviceDBIndexs != null && !serviceDBIndexs.trim().equals("")) {
			String[] serviceDBIndexArr = serviceDBIndexs.split(";");
			for (String serviceDBIndexStr : serviceDBIndexArr) {
				String[] serviceDBIndex = serviceDBIndexStr.split(":");
				List<Integer> redisDbList = new ArrayList<Integer>();
				String[] redisDbArr = serviceDBIndex[1].split(",");
				for (String redisDbStr : redisDbArr) {
					redisDbList.add(Integer.valueOf(redisDbStr));
				}
				serviceDBIndexmap.put(Integer.parseInt(serviceDBIndex[0]), redisDbList);
			}
		}
	}
/**
 * 根据业务类型获取这个
 * @param service
 * @return
 */
	public static List<Integer> getRedisDBByService(Integer service) {
		List<Integer> redisIndexList = serviceDBIndexmap.get(service);
		if (redisIndexList == null) {
			return null;
		}
		return redisIndexList;
	}

	/**
	 * 根据业务类型和具体的type获取对应的表名
	 * @param service 业务类型
	 * @param type 10代表是编译配置,11代表是分组配置,12代表是ip类域配置,13代表是数值类配置,14代表是字符串类域配置,15代表是增强字符串类域配置
	 * @return
	 */
	public static String getTableName(int service, int type) {
		Map<Integer, String> typeMap = sercieNameMap.get(service);
		if (typeMap != null && typeMap.size() > 0) {
			return typeMap.get(type);
		} else {
			return "";
		}

	}
	public static Integer getActionByService(Integer service) {
		return serviceActionMap.get(service);
	}
	
	public static Map<Integer, Map<Integer, String>> getSercieNameMap() {
		return sercieNameMap;
	}

	public static void setSercieNameMap(Map<Integer, Map<Integer, String>> sercieNameMap) {
		ServiceAndRDBIndexReal.sercieNameMap = sercieNameMap;
	}

	public static Map<Integer, List<Integer>> getServiceDBIndexmap() {
		return serviceDBIndexmap;
	}

	public static void setServiceDBIndexmap(Map<Integer, List<Integer>> serviceDBIndexmap) {
		ServiceAndRDBIndexReal.serviceDBIndexmap = serviceDBIndexmap;
	}
	
	
	
	
}
