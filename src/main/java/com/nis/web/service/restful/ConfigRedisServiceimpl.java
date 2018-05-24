package com.nis.web.service.restful;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nis.datasource.DynamicJedisDataBase;
import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.domain.MaatXmlSeq;
import com.nis.domain.restful.MaatConfig;
import com.nis.util.Configurations;
import com.nis.util.ReadMaatXmlUtil;
import com.nis.web.dao.impl.BaseRedisDao;

@Service()
public class ConfigRedisServiceimpl extends BaseRedisDao<String, String> implements ConfigRedisService {
	/**
	 * 第一个key是业务类型,第二个key是type(编译配置,分组配置,域配置)value是表名
	 */
	private static Map<Integer, Map<Integer, String>> sercieNameMap = new HashMap<Integer, Map<Integer, String>>();
	/**
	 * key是业务类型,value是业务类型对应的redisdbIndex
	 */
	private static Map<Integer, Integer> serviceDBIndexmap = new HashMap<Integer, Integer>();

	static {
		String service = Configurations.getStringProperty("service", "");
		if (service != null && !service.trim().equals("")) {
			String[] split = service.split(";");
			for (String str : split) {
				String type = Configurations.getStringProperty(str, "");
				if (type != null && !type.trim().equals("")) {
					Map<Integer, String> typeMap = new HashMap<Integer, String>();
					String[] typeArrs = type.split(";");
					for (String typeStr : typeArrs) {
						String[] typeArr = typeStr.split(":");
						typeMap.put(Integer.parseInt(typeArr[0]), typeArr[1]);
					}
					sercieNameMap.put(Integer.parseInt(str), typeMap);
				}

			}
		}

		String serviceDBIndexs = Configurations.getStringProperty("serviceDBIndex", "");
		if (serviceDBIndexs != null && !serviceDBIndexs.trim().equals("")) {

			String[] serviceDBIndexArr = serviceDBIndexs.split(";");
			for (String serviceDBIndexStr : serviceDBIndexArr) {
				String[] serviceDBIndex = serviceDBIndexStr.split(":");

				serviceDBIndexmap.put(Integer.parseInt(serviceDBIndex[0]), Integer.parseInt(serviceDBIndex[1]));
			}

		}
	}

	@Autowired
	private OperRedisService operRedisService;

	@Transactional
	public void saveUnMaatConfig(List<Map<String, String>> listMap, int service) {
		if (listMap != null && listMap.size() > 0) {
			String maatVersionStr = operRedisService.getMaatVersion(getRedisDBByService(service));
			if (maatVersionStr != null) {
				Long maatVersion = Long.valueOf(maatVersionStr) + 1;

				for (Map<String, String> map : listMap) {
					// String serviceStr = map.get("service");
					// int service = Integer.parseInt(serviceStr);
					DynamicJedisDataBase.setRedisDataBase(getRedisDBByService(service), redisTemplate);
					MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
					if (maatXmlConfig != null) {
						List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
						String maatKey = "";
						for (MaatXmlExpr maatXmlExpr : expressionList) {
							if (16 == maatXmlExpr.getType().intValue()) {
								StringBuffer keyBF = new StringBuffer();
								String[] keySplit = maatXmlExpr.getKeyExpression().split(";");
								for (String keyStr : keySplit) {
									if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("[")) {
										keyStr = keyStr.trim().replace("[", "").replace("]", "");
										keyBF.append(map.get(keyStr));
									} else {
										keyBF.append(keyStr.trim());
									}
								}
								StringBuffer valBF = new StringBuffer();
								String[] valSplit = maatXmlExpr.getValueExpression().split(";");
								for (String valStr : valSplit) {
									if (!StringUtils.isEmpty(valStr) && valStr.trim().startsWith("[")) {
										valStr = valStr.trim().replace("[", "").replace("]", "");
										valBF.append(map.get(valStr));
									} else if (valStr.equals("&nbsp")) {
										valBF.append(" ");
									} else if (valStr.equals("\\t")) {// xml中是字符串的\t这里判断的时候需要转义为\\t,但是添加的时候需要添加\t不是\\t
										valBF.append("\t");
									} else if (valStr.equals("\\n")) {
										valBF.append("\n");
									}
								}
								// System.out.println(keyBF.toString());
								// System.out.println(valBF.toString());
								maatKey = keyBF.toString();
								redisTemplate.opsForValue().set(keyBF.toString().toUpperCase(), valBF.toString());
								break;
							}
							// redisTemplate.boundValueOps(keyBF.toString().toUpperCase()).set(valBF.toString());

						}
						for (MaatXmlExpr maatXmlExpr : expressionList) {
							if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
								String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
								redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset, maatVersion);
							} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
								Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
								redisTemplate.boundZSetOps("MAAT_RULE_TIMER").add(maatKey, score);
							} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
								Long nowTime = new Date().getTime();
								nowTime = nowTime / 1000l;
								Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
								redisTemplate.boundZSetOps("MAAT_VERSION_TIMER").add(maatVersion + "", score);
							}
						}
						List<MaatXmlSeq> seqList = maatXmlConfig.getSequenceList();
						for (MaatXmlSeq maatXmlSeq : seqList) {
							// DynamicJedisDataBase.setRedisDataBase(maatXmlSeq.getRedisDB(),
							// redisTemplate);
							String seqKey = maatXmlSeq.getSequenceKey();
							if (!seqKey.toUpperCase().equals("MAAT_VERSION")) {
								Integer operation = maatXmlSeq.getOperation();
								if (operation == 1) {
									redisTemplate.boundValueOps(seqKey.toUpperCase()).increment(1l);
								}
							}

						}
					}
				}
				redisTemplate.boundValueOps("MAAT_VERSION").set(maatVersion + "");
			}
		}
	}

	public void test() {

		DynamicJedisDataBase.setRedisDataBase(2, redisTemplate);
		for (int i = 0; i < 10; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 5) {
				// int a = 1 / 0;
			}
		}
		// 切换redis数据库
		DynamicJedisDataBase.setRedisDataBase(3, redisTemplate);
		for (int i = 10; i < 20; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 5) {
				// int a = 1 / 0;
			}
		}
		DynamicJedisDataBase.setRedisDataBase(4, redisTemplate);
		for (int i = 20; i < 30; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 25) {
				int a = 1 / 0;
			}
		}
	}

	@Transactional
	public void saveMaatConfig(List<MaatConfig> maatConfigList, int service) {
		if (maatConfigList != null && maatConfigList.size() > 0) {
			String maatVersionStr = operRedisService.getMaatVersion(getRedisDBByService(service));
			if (maatVersionStr != null) {
				Long maatVersion = Long.valueOf(maatVersionStr) + 1;
				for (MaatConfig maatConfig : maatConfigList) {
					// Integer service = maatConfig.getService();
					// if (service != null) {
					DynamicJedisDataBase.setRedisDataBase(getRedisDBByService(service), redisTemplate);
					MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
					setConfig(maatConfig, maatXmlConfig, maatVersion, service);
					// }
				}
				redisTemplate.boundValueOps("MAAT_VERSION").set(maatVersion + "");
			}
		}
	}

	public void setConfig(MaatConfig maatConfig, MaatXmlConfig maatXmlConfig, Long maatVersion, int service) {
		Map<String, String> compileMap = maatConfig.getCompileMap();
		setCommonConfig(maatXmlConfig, compileMap, 10, maatVersion.doubleValue(), service);// 10代表是编译配置
		Map<String, String> groupMap = maatConfig.getGroupMap();
		setCommonConfig(maatXmlConfig, groupMap, 11, maatVersion.doubleValue(), service);// 11代表是分组配置
		Map<String, String> ipRegionMap = maatConfig.getIpRegionMap();
		setCommonConfig(maatXmlConfig, ipRegionMap, 12, maatVersion.doubleValue(), service);// 12代表是ip类域配置
		Map<String, String> numRegionMap = maatConfig.getNumRegionMap();
		setCommonConfig(maatXmlConfig, numRegionMap, 13, maatVersion.doubleValue(), service);// 13代表是数值类配置
		Map<String, String> strRegionMap = maatConfig.getStrRegionMap();
		setCommonConfig(maatXmlConfig, strRegionMap, 14, maatVersion.doubleValue(), service);// 14代表是字符串类域配置
		Map<String, String> strStrRegionMap = maatConfig.getStrStrRegionMap();
		setCommonConfig(maatXmlConfig, strStrRegionMap, 15, maatVersion.doubleValue(), service);// 15代表是增强字符串类域配置
		updateCommonKey(maatXmlConfig);
	}

	public void setCommonConfig(MaatXmlConfig maatXmlConfig, Map<String, String> map, int type, Double maatVersion,
			int service) {
		if (maatXmlConfig != null && map != null && map.size() > 0) {
			List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
			String maatKey = "";
			for (MaatXmlExpr maatXmlExpr : expressionList) {
				if (type == maatXmlExpr.getType().intValue()) {
					StringBuffer keyBF = new StringBuffer();
					String[] keySplit = maatXmlExpr.getKeyExpression().split(";");
					for (String keyStr : keySplit) {
						if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("[")) {
							keyStr = keyStr.trim().replace("[", "").replace("]", "");
							keyBF.append(map.get(keyStr));
						} else if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("{")) {
							keyStr = keyStr.trim().replace("{", "").replace("}", "");
							if (keyStr.toLowerCase().contains("table_name")) {
								keyBF.append(getTableName(service, type));
							}

						} else {
							keyBF.append(keyStr.trim());
						}
					}
					StringBuffer valBF = new StringBuffer();
					String[] valSplit = maatXmlExpr.getValueExpression().split(";");
					for (String valStr : valSplit) {
						if (!StringUtils.isEmpty(valStr) && valStr.trim().startsWith("[")) {
							valStr = valStr.trim().replace("[", "").replace("]", "");
							valBF.append(map.get(valStr));
						} else if (valStr.equals("&nbsp")) {
							valBF.append(" ");
						} else if (valStr.equals("\\t")) {// xml中是字符串的\t这里判断的时候需要转义为\\t,但是添加的时候需要添加\t不是\\t
							valBF.append("\t");
						} else if (valStr.equals("\\n")) {
							valBF.append("\n");
						}
					}
					maatKey = keyBF.toString();
					// System.out.println(keyBF.toString());
					// System.out.println(valBF.toString());
					redisTemplate.opsForValue().set(maatKey.toUpperCase(), valBF.toString());
					// redisTemplate.boundValueOps(keyBF.toString()).set(valBF.toString());
					break;
				}

			}
			for (MaatXmlExpr maatXmlExpr : expressionList) {
				if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
					String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
					redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset, maatVersion);
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
					Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
					redisTemplate.boundZSetOps("MAAT_RULE_TIMER").add(maatKey, score);
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
					Long nowTime = new Date().getTime();
					nowTime = nowTime / 1000l;
					Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
					redisTemplate.boundZSetOps("MAAT_VERSION_TIMER").add(maatVersion + "", score);
				}
			}

		}
	}

	public void updateCommonKey(MaatXmlConfig maatXmlConfig) {
		List<MaatXmlSeq> seqList = maatXmlConfig.getSequenceList();
		for (MaatXmlSeq maatXmlSeq : seqList) {
			// DynamicJedisDataBase.setRedisDataBase(maatXmlSeq.getRedisDB(),
			// redisTemplate);
			String seqKey = maatXmlSeq.getSequenceKey();
			if (!seqKey.toUpperCase().equals("MAAT_VERSION")) {
				Integer operation = maatXmlSeq.getOperation();
				if (operation == 1) {
					redisTemplate.boundValueOps(seqKey).increment(1l);
				}
			}
		}
	}

	public Long getIncrId(String key) {
		//DynamicJedisDataBase.setRedisDataBase(getRedisDBByService(service), redisTemplate);
		// RedisAtomicLong atomicLong = new RedisAtomicLong(key,
		// redisTemplate.getConnectionFactory());
		//
		// Long id = atomicLong.getAndIncrement() + 1;
		// if ((id == null || id.longValue() == 0) && liveTime > 0) {
		// atomicLong.expire(liveTime, TimeUnit.SECONDS);
		// }
		// return id;
		return redisTemplate.boundValueOps(key.toUpperCase()).increment(1l);
		// return redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
	}

	public static Integer getRedisDBByService(Integer service) {
		Integer redisIndex = serviceDBIndexmap.get(service);
		if (redisIndex == null) {
			return 0;
		}
		return redisIndex;
	}

	/**
	 * 根据业务类型和具体的type获取对应的表名
	//	 * @param service 业务类型
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

	public static void main(String[] args) {
		String filePath = "http://10.0.6.192/group1/M00/00/01/CgAGwFr9LriANfj6AADz3NN2rlY448.jpg";
		String substr = filePath.substring(filePath.indexOf("group"));
		String[] split = "maat_update_status".split(";");
		System.out.println(substr);

		getRedisDBByService(80);

	}
}
