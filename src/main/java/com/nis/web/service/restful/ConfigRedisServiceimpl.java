package com.nis.web.service.restful;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.domain.MaatXmlSeq;
import com.nis.domain.restful.MaatConfig;
import com.nis.domain.restful.MaatRelation;
import com.nis.listener.CompileGroupRegionRela;
import com.nis.util.ReadMaatXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.web.service.SpringContextHolder;

@Service()
public class ConfigRedisServiceimpl implements ConfigRedisService {
	private static Logger logger = LoggerFactory.getLogger(ConfigRedisServiceimpl.class);

	@Transactional
	public void saveTestConfig(List<Map<String, String>> listMap, int service) {
		RedisTemplate<String, String> redisTemplate0 = SpringContextHolder.getBean("redisTemplate2");
		RedisTemplate<String, String> redisTemplate1 = SpringContextHolder.getBean("redisTemplate1");
		System.out.println(redisTemplate0.opsForValue().get("MAAT_VERSION"));
		System.out.println(redisTemplate1.opsForValue().get("MAAT_VERSION"));
		redisTemplate0.boundValueOps("MAAT_VERSION").increment(2l);
		if (service == 2) {
			int i = service / 0;
		}
		redisTemplate1.boundValueOps("MAAT_VERSION").increment(1l);

		// redisTemplate0.exec();
		// redisTemplate1.exec();

	}

	@Transactional
	public void saveUnMaatConfig(Map<Integer, List<Map<String, String>>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			for (Integer redisDBIndex : configMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex <= 15) {
					RedisTemplate<String, String> redisTemplate = SpringContextHolder
							.getBean("redisTemplate" + redisDBIndex);
					List<Map<String, String>> listMap = configMap.get(redisDBIndex);

					String maatVersionStr = redisTemplate.opsForValue().get("MAAT_VERSION");
					if (maatVersionStr == null) {
						maatVersionStr = "0";
					}
					if (maatVersionStr != null) {
						Long maatVersion = Long.valueOf(maatVersionStr) + 1;
						for (Map<String, String> map : listMap) {
							String serviceStr = map.get("service");
							int service = Integer.parseInt(serviceStr);
							MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
							if (maatXmlConfig != null) {
								List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
								String maatKey = "";
								for (MaatXmlExpr maatXmlExpr : expressionList) {
									if (0 == maatXmlExpr.getType().intValue()) {
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
										maatKey = keyBF.toString();
										redisTemplate.opsForValue().set(keyBF.toString().toUpperCase(),
												valBF.toString());
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
									} else if (maatXmlExpr.getKeyExpression().toUpperCase()
											.equals("MAAT_VERSION_TIMER")) {
										Long nowTime = new Date().getTime();
										nowTime = nowTime / 1000l;
										Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
										redisTemplate.boundZSetOps("MAAT_VERSION_TIMER").add(maatVersion + "", score);
									}
								}
								List<MaatXmlSeq> seqList = maatXmlConfig.getSequenceList();
								for (MaatXmlSeq maatXmlSeq : seqList) {
									// setRedisDataBase(maatXmlSeq.getRedisDB(),
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

					}
					redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
				}
			}
		}

	}

	@Override
	public void addMaatRelation(List<MaatConfig> maatConfigList, int service) {
		Integer redisDBIndex = ServiceAndRDBIndexReal.getRedisDBByService(service);
		MaatRelation maatRelation = CompileGroupRegionRela.getIdRelationMap().get(redisDBIndex);

		for (MaatConfig maatConfig : maatConfigList) {
			// MaatXmlConfig maatXmlConfig =
			// ReadMaatXmlUtil.getMaatConfigByService(service);

			List<Map<String, String>> groupMapList = maatConfig.getGroupMapList();
			if (groupMapList != null && groupMapList.size() > 0) {
				for (Map<String, String> map : groupMapList) {
					String compileIdStr = map.get("compile_id");
					String groupIdStr = map.get("group_id");
					if (compileIdStr != null && !compileIdStr.equals("") && groupIdStr != null
							&& !groupIdStr.equals("")) {
						Long groupId = Long.valueOf(groupIdStr);
						Long compileId = Long.valueOf(compileIdStr);
						Map<Long, Set<Long>> groupAndCompileMap = maatRelation.getGroupAndCompileMap();
						if (groupAndCompileMap != null) {
							if (groupAndCompileMap.containsKey(groupId)) {
								groupAndCompileMap.get(groupId).add(compileId);
							} else {
								Set<Long> set = new HashSet<Long>();
								set.add(compileId);
								groupAndCompileMap.put(groupId, set);
							}
						} else {
							Map<Long, Set<Long>> groupAndCompileMap1 = new ConcurrentHashMap<Long, Set<Long>>();
							Set<Long> set = new HashSet<Long>();
							set.add(compileId);
							groupAndCompileMap1.put(groupId, set);
							maatRelation.setGroupAndCompileMap(groupAndCompileMap1);
						}

						Map<Long, Set<Long>> compileAndGroupMap = maatRelation.getCompileAndGroupMap();
						if (compileAndGroupMap != null) {
							if (compileAndGroupMap.containsKey(compileId)) {
								compileAndGroupMap.get(compileId).add(groupId);
							} else {
								Set<Long> set = new HashSet<Long>();
								set.add(groupId);
								compileAndGroupMap.put(compileId, set);
							}
						} else {
							Map<Long, Set<Long>> compileAndGroupMap1 = new ConcurrentHashMap<Long, Set<Long>>();
							Set<Long> set = new HashSet<Long>();
							set.add(groupId);
							compileAndGroupMap1.put(compileId, set);
							maatRelation.setCompileAndGroupMap(compileAndGroupMap1);
						}
					}
				}
			}

			List<Map<String, String>> ipRegionMapList = maatConfig.getIpRegionMapList();
			setReionAndGroupRela(ipRegionMapList, maatRelation);
			List<Map<String, String>> numRegionMapList = maatConfig.getNumRegionMapList();
			setReionAndGroupRela(numRegionMapList, maatRelation);
			List<Map<String, String>> strRegionMapList = maatConfig.getStrRegionMapList();
			setReionAndGroupRela(strRegionMapList, maatRelation);
			List<Map<String, String>> strStrRegionMapList = maatConfig.getStrStrRegionMapList();
			setReionAndGroupRela(strStrRegionMapList, maatRelation);
			List<Map<String, String>> fileDigestRegionMapList = maatConfig.getFileDigestRegionMapList();
			setReionAndGroupRela(fileDigestRegionMapList, maatRelation);
			List<Map<String, String>> fileLikeRegionMapList = maatConfig.getFileLikeRegionMapList();
			setReionAndGroupRela(fileLikeRegionMapList, maatRelation);

		}
	}

	public void setReionAndGroupRela(List<Map<String, String>> regionMapList, MaatRelation maatRelation) {
		if (regionMapList != null && regionMapList.size() > 0) {
			for (Map<String, String> map : regionMapList) {
				String regionIdStr = map.get("region_id");
				String groupIdStr = map.get("group_id");
				if (regionIdStr != null && !regionIdStr.equals("") && groupIdStr != null && !groupIdStr.equals("")) {
					Long groupId = Long.valueOf(groupIdStr);
					Long regionId = Long.valueOf(regionIdStr);
					Map<Long, Set<Long>> groupAndRegionMap = maatRelation.getGroupAndRegionMap();
					if (groupAndRegionMap != null) {
						if (groupAndRegionMap.containsKey(groupId)) {
							groupAndRegionMap.get(groupId).add(regionId);
						} else {
							Set<Long> set = new HashSet<Long>();
							set.add(regionId);
							groupAndRegionMap.put(groupId, set);
						}
					} else {
						Map<Long, Set<Long>> groupAndRegionMap1 = new ConcurrentHashMap<Long, Set<Long>>();
						Set<Long> set = new HashSet<Long>();
						set.add(regionId);
						groupAndRegionMap1.put(groupId, set);
						maatRelation.setGroupAndRegionMap(groupAndRegionMap1);
					}
				}
			}
		}
	}

	@Override
	public void saveMaatConfig(Map<Integer, List<MaatConfig>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			for (Integer redisDBIndex : configMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex <= 15) {
					RedisTemplate<String, String> redisTemplate = SpringContextHolder
							.getBean("redisTemplate" + redisDBIndex);
					String maatVersionStr = redisTemplate.opsForValue().get("MAAT_VERSION");
					if (maatVersionStr == null) {
						maatVersionStr = "0";
					}
					if (maatVersionStr != null) {
						Long maatVersion = Long.valueOf(maatVersionStr) + 1;
						List<MaatConfig> maatConfigList = configMap.get(redisDBIndex);
						if (maatConfigList != null && maatConfigList.size() > 0) {
							for (MaatConfig maatConfig : maatConfigList) {
								int service = maatConfig.getService();
								MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
								setConfig(maatConfig, maatXmlConfig, maatVersion, service, redisTemplate);
							}
						}

					}
					redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
				}
			}
		}
	}

	public void setConfig(MaatConfig maatConfig, MaatXmlConfig maatXmlConfig, Long maatVersion, int service,
			RedisTemplate<String, String> redisTemplate) {
		Map<String, String> compileMap = maatConfig.getCompileMap();
		setCommonConfig(maatXmlConfig, compileMap, 10, maatVersion.doubleValue(), service, redisTemplate);// 10代表是编译配置
		List<Map<String, String>> groupMapList = maatConfig.getGroupMapList();
		if (groupMapList != null && groupMapList.size() > 0) {
			for (Map<String, String> map : groupMapList) {
				setCommonConfig(maatXmlConfig, map, 11, maatVersion.doubleValue(), service, redisTemplate);// 11代表是分组配置
			}
		}

		List<Map<String, String>> ipRegionMapList = maatConfig.getIpRegionMapList();
		if (ipRegionMapList != null && ipRegionMapList.size() > 0) {
			for (Map<String, String> map : ipRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 12, maatVersion.doubleValue(), service, redisTemplate);// 12代表是ip类域配置
			}
		}
		List<Map<String, String>> numRegionMapList = maatConfig.getNumRegionMapList();
		if (numRegionMapList != null && numRegionMapList.size() > 0) {
			for (Map<String, String> map : numRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 13, maatVersion.doubleValue(), service, redisTemplate);// 13代表是数值类配置
			}
		}
		List<Map<String, String>> strRegionMapList = maatConfig.getStrRegionMapList();
		if (strRegionMapList != null && strRegionMapList.size() > 0) {
			for (Map<String, String> map : strRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 14, maatVersion.doubleValue(), service, redisTemplate);// 14代表是字符串类域配置
			}
		}
		List<Map<String, String>> strStrRegionMapList = maatConfig.getStrStrRegionMapList();
		if (strStrRegionMapList != null && strStrRegionMapList.size() > 0) {
			for (Map<String, String> map : strStrRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 15, maatVersion.doubleValue(), service, redisTemplate);// 15代表是增强字符串类域配置
			}
		}
		List<Map<String, String>> fileDigestRegionMapList = maatConfig.getFileDigestRegionMapList();
		if (fileDigestRegionMapList != null && fileDigestRegionMapList.size() > 0) {
			for (Map<String, String> map : fileDigestRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 16, maatVersion.doubleValue(), service, redisTemplate);// 16代表是文件摘要类域配置
			}
		}
		List<Map<String, String>> fileLikeRegionMapList = maatConfig.getFileLikeRegionMapList();
		if (fileLikeRegionMapList != null && fileLikeRegionMapList.size() > 0) {
			for (Map<String, String> map : fileLikeRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 17, maatVersion.doubleValue(), service, redisTemplate);// 17代表是文本相似性域配置
			}
		}
		// updateCommonKey(maatXmlConfig);
	}

	public void setCommonConfig(MaatXmlConfig maatXmlConfig, Map<String, String> map, int type, Double maatVersion,
			int service, RedisTemplate<String, String> redisTemplate) {
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
								keyBF.append(ServiceAndRDBIndexReal.getTableName(service, type));
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
					System.out.println(keyBF.toString());
					System.out.println(valBF.toString());
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

	public void updateCommonKey(MaatXmlConfig maatXmlConfig, RedisTemplate<String, String> redisTemplate) {
		List<MaatXmlSeq> seqList = maatXmlConfig.getSequenceList();
		for (MaatXmlSeq maatXmlSeq : seqList) {
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
		RedisTemplate<String, String> redisTemplate = SpringContextHolder.getBean("redisTemplate0");
		return redisTemplate.boundValueOps(key.toUpperCase()).increment(1l);
		// return redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
	}

	@Override
	public void delUnMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			for (Integer redisDBIndex : idMap.keySet()) {
				RedisTemplate<String, String> redisTemplate = SpringContextHolder
						.getBean("redisTemplate" + redisDBIndex);
				Map<Integer, List<Long>> serviceConfigMap = idMap.get(redisDBIndex);
				if (serviceConfigMap != null && serviceConfigMap.size() > 0) {
					String maatVersionStr = redisTemplate.opsForValue().get("MAAT_VERSION");
					if (maatVersionStr == null) {
						maatVersionStr = "0";
					}
					if (maatVersionStr != null) {
						Long maatVersion = Long.valueOf(maatVersionStr) + 1;
						for (Integer service : serviceConfigMap.keySet()) {
							List<Long> compileIdList = serviceConfigMap.get(service);
							if (compileIdList != null && compileIdList.size() > 0) {
								for (Long id : compileIdList) {
									MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
									if (maatXmlConfig != null) {
										List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
										String maatKey = "";
										for (MaatXmlExpr maatXmlExpr : expressionList) {
											if (0 == maatXmlExpr.getType().intValue()) {
												StringBuffer keyBF = new StringBuffer();
												String[] keySplit = maatXmlExpr.getKeyExpression().split(";");
												for (String keyStr : keySplit) {
													if (!StringUtils.isEmpty(keyStr)
															&& keyStr.toUpperCase().equals("EFFECTIVE_RULE")) {
														keyStr = "OBSOLETE_RULE";
														keyBF.append(keyStr.trim());
													} else if (!StringUtils.isEmpty(keyStr)
															&& keyStr.trim().startsWith("[")) {
														// keyStr = keyStr.trim().replace("[", "").replace("]", "");
														keyBF.append(id);
													} else {
														keyBF.append(keyStr.trim());
													}
												}
												maatKey = keyBF.toString();
												String oldKey = maatKey.replace("OBSOLETE_RULE", "EFFECTIVE_RULE");
												redisTemplate.rename(oldKey.toString().toUpperCase(),
														keyBF.toString().toUpperCase());
												break;
											}
											// redisTemplate.boundValueOps(keyBF.toString().toUpperCase()).set(valBF.toString());

										}
										for (MaatXmlExpr maatXmlExpr : expressionList) {
											if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_UPDATE_STATUS")) {
												String zset = maatKey.replace("OBSOLETE_RULE:", "DEL,");
												redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset, maatVersion);
											} else if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_RULE_TIMER")) {
												Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
												redisTemplate.boundZSetOps("MAAT_RULE_TIMER").add(maatKey, score);
											} else if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_VERSION_TIMER")) {
												Long nowTime = new Date().getTime();
												nowTime = nowTime / 1000l;
												Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
												redisTemplate.boundZSetOps("MAAT_VERSION_TIMER").add(maatVersion + "",
														score);
											}
										}
										List<MaatXmlSeq> seqList = maatXmlConfig.getSequenceList();
										for (MaatXmlSeq maatXmlSeq : seqList) {
											// setRedisDataBase(maatXmlSeq.getRedisDB(),
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

							}

						}
						redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
					}
				}

			}

		}

	}

	@Override
	public void delMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			for (Integer redisDBIndex : idMap.keySet()) {
				RedisTemplate<String, String> redisTemplate = SpringContextHolder
						.getBean("redisTemplate" + redisDBIndex);
				Map<Integer, List<Long>> serviceConfigMap = idMap.get(redisDBIndex);
				if (serviceConfigMap != null && serviceConfigMap.size() > 0) {
					String maatVersionStr = redisTemplate.opsForValue().get("MAAT_VERSION");
					if (maatVersionStr == null) {
						maatVersionStr = "0";
					}
					if (maatVersionStr != null) {
						Long maatVersion = Long.valueOf(maatVersionStr) + 1;
						for (Integer service : serviceConfigMap.keySet()) {

						}

						redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
					}
				}
			}
		}

	}

	@Override
	public void delMaatRelation(List<Long> compileIdList, int service) {
		// TODO Auto-generated method stub

	}

}
