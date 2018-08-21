package com.nis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nis.domain.MaatXmlExpr;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;

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
	private static Logger logger = LoggerFactory.getLogger(ServiceAndRDBIndexReal.class);
	/**
	 * 第一个key是业务类型,第二个key是type(编译配置,分组配置,域配置)value是表名
	 */
	private static Map<Integer, Map<Integer, List<String>>> sercieNameMap = new HashMap<Integer, Map<Integer, List<String>>>();

	/**
	 * 非maat结构业务类型与表名对应关系,key是业务类型,value是表名
	 */
	private static Map<Integer, String> unMaatSercieNameMap = new HashMap<Integer, String>();

	/**
	 * key是业务类型,value是业务类型对应的redisdbIndex
	 */
	private static Map<Integer, List<Integer>> serviceDBIndexmap = new HashMap<Integer, List<Integer>>();
	/**
	 * key是业务类型,value是业务类型对应的动作(action)
	 */
	private static Map<Integer, Integer> serviceActionMap = new HashMap<Integer, Integer>();

	/**
	 * 保存maat分发到阀门需要添加到域的属性：maat2Valve
	 * Map<service,Map<regionType,fields>>
	 */
	private static Map<Integer, Map<String, String[]>> maatToValveMap = new HashMap<Integer, Map<String, String[]>>();

	/**
	 * 阀门保存的redis数据库Index，在seriveTable.properties中定义
	 */
	private static Integer valveDBIndex = 7;
	static {

		String unMaatService = Configurations.getStringProperty("unMaatService", "");
		if (unMaatService != null && !unMaatService.trim().equals("")) {
			String[] split = unMaatService.split(";");
			for (String str : split) {
				String[] serviceAction = str.split(":");
				serviceActionMap.put(Integer.valueOf(serviceAction[0]), Integer.valueOf(serviceAction[1]));

				String serviceType = Configurations.getStringProperty(serviceAction[0], "");
				if (serviceType != null && !serviceType.trim().equals("")) {
					String[] typeArrs = serviceType.split(";");
					for (String typeStr : typeArrs) {
						String[] typeArr = typeStr.split(":");
						String tableNameArr[] = typeArr[1].split(",");
						for (String tableName : tableNameArr) {
							unMaatSercieNameMap.put(Integer.parseInt(serviceAction[0]), tableName.toUpperCase());
						}
					}
				}
			}
		}

		String service = Configurations.getStringProperty("service", "");
		if (service != null && !service.trim().equals("")) {
			String[] split = service.split(";");
			for (String str : split) {
				String[] serviceAction = str.split(":");
				serviceActionMap.put(Integer.valueOf(serviceAction[0]), Integer.valueOf(serviceAction[1]));
				String type = Configurations.getStringProperty(serviceAction[0], "");
				if (type != null && !type.trim().equals("")) {
					Map<Integer, List<String>> typeMap = new HashMap<Integer, List<String>>();
					String[] typeArrs = type.split(";");
					for (String typeStr : typeArrs) {
						String[] typeArr = typeStr.split(":");
						int tableType = Integer.parseInt(typeArr[0]);
						String tableNameArr[] = typeArr[1].split(",");
						for (String tableName : tableNameArr) {
							if (typeMap.containsKey(tableType)) {
								typeMap.get(tableType).add(tableName.toUpperCase());
							} else {
								List<String> list = new ArrayList<String>();
								list.add(tableName.toUpperCase());
								typeMap.put(tableType, list);
							}
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

		String maat2Valve = Configurations.getStringProperty("maat2Valve", "");
		if (maat2Valve != null && !maat2Valve.trim().equals("")) {
			String[] maat2ValveAry = maat2Valve.split(";");
			for (String maat2ValveStr : maat2ValveAry) {
				String[] serviceAndRegion = maat2ValveStr.split(":");
				if (serviceAndRegion != null && serviceAndRegion.length == 2) {
					String[] regionAndFields = serviceAndRegion[1].split("@");
					Map<String, String[]> fieldMap = new HashMap<String, String[]>();
					String[] fields = regionAndFields[1].split("&");
					// 同一service有不同的域类型，多个之间用“|”分隔
					if (regionAndFields[0].contains("|")) {
						String[] regionTypeAry = regionAndFields[0].split("\\|");
						for (String regionType : regionTypeAry) {
							fieldMap.put(regionType, fields);
						}
					} else {
						fieldMap.put(regionAndFields[0], fields);
					}

					maatToValveMap.put(Integer.parseInt(serviceAndRegion[0]), fieldMap);

				}
			}
		}
	}

	public static void main(String[] args) {
		// getUnMaatTable();
		getMaatTable();
	}

	public static void getUnMaatTable() {
		Map<Integer, String> typeTable = new HashMap<Integer, String>();

		Map<String, String> map = new HashMap<String, String>();
		String unMaatService = Configurations.getStringProperty("unMaatService", "");
		if (unMaatService != null && !unMaatService.trim().equals("")) {
			String[] split = unMaatService.split(";");
			for (String str : split) {
				String[] serviceAction = str.split(":");
				String action = serviceAction[1];
				map.put(serviceAction[0], action);
				String serviceType = Configurations.getStringProperty(serviceAction[0], "");
				if (serviceType != null && !serviceType.trim().equals("")) {
					String[] typeArrs = serviceType.split(";");
					for (String typeStr : typeArrs) {
						String[] typeArr = typeStr.split(":");
						String tableNameArr[] = typeArr[1].split(",");
						for (String tableName : tableNameArr) {
							if (!typeTable.containsKey(Integer.parseInt(serviceAction[0]))) {
								typeTable.put(Integer.parseInt(serviceAction[0]), tableName.toUpperCase());
							} else {
								System.out.println();
							}
							map.put(serviceAction[0], action);
							unMaatSercieNameMap.put(Integer.parseInt(serviceAction[0]), tableName.toUpperCase());
						}
					}
				}
			}
		}
		for (Integer service : typeTable.keySet()) {
			System.out.println(
					"0x" + Integer.toHexString(service) + "--servie=" + service + "--table=" + typeTable.get(service)
							+ "---redisDB=" + serviceDBIndexmap.get(service) + "--action=" + map.get(service.toString())
							+ "--HexAction=0x" + Integer.toHexString(Integer.parseInt(map.get(service.toString()))));
		}

	}

	public static void getMaatTable() {
		Map<Integer, Set<String>> typeMap = new HashMap<Integer, Set<String>>();
		String serviceStr = Configurations.getStringProperty("service", "");
		if (serviceStr != null && !serviceStr.trim().equals("")) {
			String[] split = serviceStr.split(";");
			for (String str : split) {
				String[] serviceAction = str.split(":");
				Integer ser = Integer.parseInt(serviceAction[0]);
				String type = Configurations.getStringProperty(serviceAction[0], "");
				if (type != null && !type.trim().equals("")) {
					String[] typeArrs = type.split(";");
					for (String typeStr : typeArrs) {
						String[] typeArr = typeStr.split(":");
						String tableNameArr[] = typeArr[1].split(",");
						for (String tableName : tableNameArr) {
							if (typeMap.containsKey(ser)) {
								typeMap.get(ser).add(tableName.toUpperCase());
							} else {
								Set<String> list = new HashSet<String>();
								list.add(tableName.toUpperCase());
								typeMap.put(ser, list);
							}
						}
					}
				}

			}
		}
		if (serviceStr != null && !serviceStr.trim().equals("")) {
			String[] split = serviceStr.split(";");
			for (String str : split) {
				String[] serviceAction = str.split(":");
				Integer ser = Integer.parseInt(serviceAction[0]);
				String action = serviceAction[1];
				System.out.println("0x" + Integer.toHexString(ser) + "--servie=" + ser + "--table="
						+ typeMap.get(ser) + "---redisDB=" + serviceDBIndexmap.get(ser) + "--action=" + action
						+ "--HexAction=0x" + Integer.toHexString(Integer.parseInt(action)));
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
	 * 根据业务类型和具体的type获取maat类型对应的表名
	 * @param service 业务类型
	 * @param type 10代表是编译配置,11代表是分组配置,12代表是ip类域配置,13代表是数值类配置,14代表是字符串类域配置,15代表是增强字符串类域配置
	 * @return
	 */
	public static String getMaatTableName(int service, int type, String tableName) {
		Map<Integer, List<String>> typeMap = sercieNameMap.get(service);
		if (typeMap != null && typeMap.size() > 0) {
			List<String> tableList = typeMap.get(type);
			if (tableList != null && tableList.size() > 0) {
				if (tableName == null || tableName.trim().equals("")) {
					if (tableList.size() > 1) {
						logger.error("未从业务类型和表对应关系中,找到业务类型:{},配置类型:{}表名:{}对应的真实表名", service, type, tableName);
						throw new ServiceRuntimeException("在applicationConfig-rule.properties配置文件中，业务类型:" + service
								+ ",配置类型:" + type + "对应多个表,请输入具体的表名", RestBusinessCode.NotFoundTableName.getValue());
					} else {
						return tableList.get(0);
					}
				} else {
					// 保存tableName在表名列表中的序号
					int index = -1;
					for (int i = 0; i < tableList.size(); i++) {
						String str = tableList.get(i);
						if (str.toLowerCase().contains(tableName.toLowerCase())) {
							index = i;
						}
					}
					if (index != -1 && tableList != null) {
						return tableList.get(index);
					} else {
						logger.error("未从业务类型和表对应关系中,找到业务类型:{},配置类型:{}表名:{}对应的真实表名", service, type, tableName);
						throw new ServiceRuntimeException("无法从applicationConfig-rule.properties配置文件中,找到回调类配置service为"
								+ service + ",配置类型:" + type + "对应的真实表名", RestBusinessCode.NotFoundTableName.getValue());
					}

				}
			}

		}
		return null;

	}

	/**
	 * 根据业务类型获取unmaat配置表名
	 * @param service
	 * @return
	 */
	public static String getUnMaatTableName(int service) {
		if (unMaatSercieNameMap != null && unMaatSercieNameMap.size() > 0) {
			return unMaatSercieNameMap.get(service);
		}
		return null;
	}

	public static Integer getActionByService(Integer service) {
		return serviceActionMap.get(service);
	}

	public static Map<Integer, Map<Integer, List<String>>> getSercieNameMap() {
		return sercieNameMap;
	}

	public static void setSercieNameMap(Map<Integer, Map<Integer, List<String>>> sercieNameMap) {
		ServiceAndRDBIndexReal.sercieNameMap = sercieNameMap;
	}

	public static Map<Integer, List<Integer>> getServiceDBIndexmap() {
		return serviceDBIndexmap;
	}

	public static void setServiceDBIndexmap(Map<Integer, List<Integer>> serviceDBIndexmap) {
		ServiceAndRDBIndexReal.serviceDBIndexmap = serviceDBIndexmap;
	}

	public static Map<Integer, Map<String, String[]>> getMaatToValveMap() {
		return maatToValveMap;
	}

	public static void setMaatToValveMap(Map<Integer, Map<String, String[]>> maatToValveMap) {
		ServiceAndRDBIndexReal.maatToValveMap = maatToValveMap;
	}

	/**
	 * @param valveDBIndex the valveDBIndex to set
	 */
	public static void setValveDBIndex(Integer valveDBIndex) {
		ServiceAndRDBIndexReal.valveDBIndex = valveDBIndex;
	}

	/**
	 * @return the valveDBIndex
	 */
	public static Integer getValveDBIndex() {
		return valveDBIndex;
	}

	public static Map<Integer, String> getUnMaatSercieNameMap() {
		return unMaatSercieNameMap;
	}

	public static void setUnMaatSercieNameMap(Map<Integer, String> unMaatSercieNameMap) {
		ServiceAndRDBIndexReal.unMaatSercieNameMap = unMaatSercieNameMap;
	}

}
