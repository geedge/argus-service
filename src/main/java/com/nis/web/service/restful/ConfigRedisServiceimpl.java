package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.domain.MaatXmlSeq;
import com.nis.domain.restful.CompileAndGroupRelations;
import com.nis.domain.restful.GroupAndRegionRelations;
import com.nis.domain.restful.MaatConfig;
import com.nis.domain.restful.MaatRelation;
import com.nis.listener.CompileGroupRegionRela;
import com.nis.util.Configurations;
import com.nis.util.Exceptions;
import com.nis.util.ReadMaatXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.web.service.SpringContextHolder;

@Service()
public class ConfigRedisServiceimpl implements ConfigRedisService {
	private static Logger logger = LoggerFactory.getLogger(ConfigRedisServiceimpl.class);

	@Transactional
	public boolean saveUnMaatConfig(Map<Integer, List<Map<String, String>>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			int count = 0;
			for (Integer redisDBIndex : configMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
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
								String maatKey = null;
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
										logger.info("向{}号redis数据库添加了一条配置,key是{},value是{}", redisDBIndex,
												keyBF.toString().toUpperCase(), valBF.toString());
										break;
									}
								}
								for (MaatXmlExpr maatXmlExpr : expressionList) {
									if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
										if (maatKey != null) {
											String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
											redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset, maatVersion);
											logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}",
													redisDBIndex, zset.toUpperCase(), maatVersion);

										}
									} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
										if (maatKey != null) {
											Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
											redisTemplate.boundZSetOps("MAAT_RULE_TIMER").add(maatKey, score);

											logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}", redisDBIndex,
													maatKey, score);

										}
									} else if (maatXmlExpr.getKeyExpression().toUpperCase()
											.equals("MAAT_VERSION_TIMER")) {
										Long nowTime = new Date().getTime();
										nowTime = nowTime / 1000l;
										Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
										redisTemplate.boundZSetOps("MAAT_VERSION_TIMER").add(maatVersion + "", score);

										logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex,
												maatVersion, score);
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
						redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
						logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
								Integer.valueOf(maatVersionStr) + 1);
						count++;
					}
				}
			}
			if (count == configMap.size()) {
				return true;
			}
		}
		return false;
	}

	/**
	 *  下发配置成功后,需要更新编译,组,域等配置id的对应关系
	 */
	public void addMaatRelation(Map<Integer, List<MaatConfig>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			for (Integer redisDBIndex : configMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
					// MaatRelation maatRelation =
					// CompileGroupRegionRela.getIdRelationMap().get(redisDBIndex);
					List<MaatConfig> maatConfigList = configMap.get(redisDBIndex);
					if (maatConfigList != null && maatConfigList.size() > 0) {
						for (MaatConfig maatConfig : maatConfigList) {
							Map<String, String> compileMap = maatConfig.getCompileMap();
							String cfgIdStr = compileMap.get("compile_id");
							CompileAndGroupRelations compileAndGroupRelations = new CompileAndGroupRelations();
							compileAndGroupRelations.setCompileId(Long.valueOf(cfgIdStr));
							compileAndGroupRelations.setGroupIdList(new ArrayList<GroupAndRegionRelations>());

							addGroupAndRegionRelations(maatConfig.getIpRegionMapList(), compileAndGroupRelations);
							addGroupAndRegionRelations(maatConfig.getNumRegionMapList(), compileAndGroupRelations);
							addGroupAndRegionRelations(maatConfig.getStrRegionMapList(), compileAndGroupRelations);
							addGroupAndRegionRelations(maatConfig.getStrStrRegionMapList(), compileAndGroupRelations);
							addGroupAndRegionRelations(maatConfig.getFileDigestRegionMapList(),
									compileAndGroupRelations);
							addGroupAndRegionRelations(maatConfig.getFileLikeRegionMapList(), compileAndGroupRelations);

							CompileGroupRegionRela.addIdRelation(redisDBIndex, compileAndGroupRelations);
						}
					}
				}
			}
		}

	}

	/**
	 * 封装组id与域id对应关系并添加到对象中
	 * @param regionMapList
	 * @param compileAndGroupRelations
	 */
	private void addGroupAndRegionRelations(List<Map<String, String>> regionMapList,
			CompileAndGroupRelations compileAndGroupRelations) {
		if (regionMapList != null && regionMapList.size() > 0 && compileAndGroupRelations != null) {
			for (Map<String, String> map : regionMapList) {
				String regionIdStr = map.get("region_id");
				String groupIdStr = map.get("group_id");
				GroupAndRegionRelations groupAndRegionRelations = new GroupAndRegionRelations();
				groupAndRegionRelations.setGroupId(Long.valueOf(groupIdStr));
				groupAndRegionRelations.setRegionId(Long.valueOf(regionIdStr));
				compileAndGroupRelations.getGroupIdList().add(groupAndRegionRelations);
			}
		}

	}

	@Override
	@Transactional
	public boolean saveMaatConfig(Map<Integer, List<MaatConfig>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			int count = 0;
			for (Integer redisDBIndex : configMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
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
								setConfig(maatConfig, maatXmlConfig, maatVersion, service, redisTemplate, redisDBIndex);
							}
							redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
							logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
									Integer.valueOf(maatVersionStr) + 1);
							count++;
						}

					}

				}
			}
			if (count == configMap.size()) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 将编译,分组,域等配置拆开分别添加到redis中
	 * @param maatConfig
	 * @param maatXmlConfig
	 * @param maatVersion
	 * @param service
	 * @param redisTemplate
	 * @param redisDBIndex
	 */
	public void setConfig(MaatConfig maatConfig, MaatXmlConfig maatXmlConfig, Long maatVersion, int service,
			RedisTemplate<String, String> redisTemplate, Integer redisDBIndex) {
		Map<String, String> compileMap = maatConfig.getCompileMap();
		setCommonConfig(maatXmlConfig, compileMap, 10, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex);// 10代表是编译配置
		List<Map<String, String>> groupMapList = maatConfig.getGroupMapList();
		if (groupMapList != null && groupMapList.size() > 0) {
			for (Map<String, String> map : groupMapList) {
				setCommonConfig(maatXmlConfig, map, 11, maatVersion.doubleValue(), service, redisTemplate,
						redisDBIndex);// 11代表是分组配置
			}
		}

		List<Map<String, String>> ipRegionMapList = maatConfig.getIpRegionMapList();
		if (ipRegionMapList != null && ipRegionMapList.size() > 0) {
			for (Map<String, String> map : ipRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 12, maatVersion.doubleValue(), service, redisTemplate,
						redisDBIndex);// 12代表是ip类域配置
			}
		}
		List<Map<String, String>> numRegionMapList = maatConfig.getNumRegionMapList();
		if (numRegionMapList != null && numRegionMapList.size() > 0) {
			for (Map<String, String> map : numRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 13, maatVersion.doubleValue(), service, redisTemplate,
						redisDBIndex);// 13代表是数值类配置
			}
		}
		List<Map<String, String>> strRegionMapList = maatConfig.getStrRegionMapList();
		if (strRegionMapList != null && strRegionMapList.size() > 0) {
			for (Map<String, String> map : strRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 14, maatVersion.doubleValue(), service, redisTemplate,
						redisDBIndex);// 14代表是字符串类域配置
			}
		}
		List<Map<String, String>> strStrRegionMapList = maatConfig.getStrStrRegionMapList();
		if (strStrRegionMapList != null && strStrRegionMapList.size() > 0) {
			for (Map<String, String> map : strStrRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 15, maatVersion.doubleValue(), service, redisTemplate,
						redisDBIndex);// 15代表是增强字符串类域配置
			}
		}
		List<Map<String, String>> fileDigestRegionMapList = maatConfig.getFileDigestRegionMapList();
		if (fileDigestRegionMapList != null && fileDigestRegionMapList.size() > 0) {
			for (Map<String, String> map : fileDigestRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 16, maatVersion.doubleValue(), service, redisTemplate,
						redisDBIndex);// 16代表是文件摘要类域配置
			}
		}
		List<Map<String, String>> fileLikeRegionMapList = maatConfig.getFileLikeRegionMapList();
		if (fileLikeRegionMapList != null && fileLikeRegionMapList.size() > 0) {
			for (Map<String, String> map : fileLikeRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 17, maatVersion.doubleValue(), service, redisTemplate,
						redisDBIndex);// 17代表是文本相似性域配置
			}
		}
		// updateCommonKey(maatXmlConfig);
	}

	/**
	 * 将整理好的数据添加到redis中
	 * @param maatXmlConfig
	 * @param map
	 * @param type
	 * @param maatVersion
	 * @param service
	 * @param redisTemplate
	 * @param redisDBIndex
	 */
	public void setCommonConfig(MaatXmlConfig maatXmlConfig, Map<String, String> map, int type, Double maatVersion,
			int service, RedisTemplate<String, String> redisTemplate, Integer redisDBIndex) {
		if (maatXmlConfig != null && map != null && map.size() > 0) {
			List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
			String maatKey = null;
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
					redisTemplate.opsForValue().set(maatKey.toUpperCase(), valBF.toString());
					logger.info("向{}号redis数据库添加了一条配置,key是{},value是{}", redisDBIndex, maatKey.toUpperCase(),
							valBF.toString());

					// redisTemplate.boundValueOps(keyBF.toString()).set(valBF.toString());
					break;
				}

			}
			for (MaatXmlExpr maatXmlExpr : expressionList) {
				if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
					if (maatKey != null) {
						String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
						redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset, maatVersion);
						logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}", redisDBIndex,
								zset.toUpperCase(), maatVersion);
					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
					if (maatKey != null) {
						Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
						redisTemplate.boundZSetOps("MAAT_RULE_TIMER").add(maatKey, score);
						logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}", redisDBIndex, maatKey, score);

					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
					Long nowTime = new Date().getTime();
					nowTime = nowTime / 1000l;
					Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
					redisTemplate.boundZSetOps("MAAT_VERSION_TIMER").add(maatVersion + "", score);
					logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex, maatVersion, score);
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
		Long id = redisTemplate.boundValueOps(key.toUpperCase()).increment(1l);
		logger.info("从0号redis数据库获取{}成功,自增后的值是{}", key, id);
		return id;
	}

	@Override
	@Transactional
	public boolean delUnMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			int count = 0;
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
										String maatKey = null;
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
												logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是{}", redisDBIndex,
														oldKey.toString().toUpperCase(),
														keyBF.toString().toUpperCase());
												break;
											}
											// redisTemplate.boundValueOps(keyBF.toString().toUpperCase()).set(valBF.toString());

										}
										for (MaatXmlExpr maatXmlExpr : expressionList) {
											if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_UPDATE_STATUS")) {
												if (maatKey != null) {
													String zset = maatKey.replace("OBSOLETE_RULE:", "DEL,");
													redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset,
															maatVersion);
													logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}",
															redisDBIndex, zset.toUpperCase(), maatVersion);
												}
											} else if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_RULE_TIMER")) {
												if (maatKey != null) {
													Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
													redisTemplate.boundZSetOps("MAAT_RULE_TIMER").add(maatKey, score);
													logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}",
															redisDBIndex, maatKey, score);
												}
											} else if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_VERSION_TIMER")) {
												Long nowTime = new Date().getTime();
												nowTime = nowTime / 1000l;
												Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
												redisTemplate.boundZSetOps("MAAT_VERSION_TIMER").add(maatVersion + "",
														score);
												logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}",
														redisDBIndex, maatVersion, score);
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
								redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
								logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
										Integer.valueOf(maatVersionStr) + 1);
								count++;
							}
						}

					}
				}

			}
			if (count == idMap.size()) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean delMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			int count = 0;
			for (Integer redisDBIndex : idMap.keySet()) {
				RedisTemplate<String, String> redisTemplate = SpringContextHolder
						.getBean("redisTemplate" + redisDBIndex);
				Map<Integer, List<Long>> serviceConfigMap = idMap.get(redisDBIndex);
				if (serviceConfigMap != null && serviceConfigMap.size() > 0) {
					String maatVersionStr = redisTemplate.opsForValue().get("MAAT_VERSION");
					if (maatVersionStr == null) {
						maatVersionStr = "0";
					}
					MaatRelation maatRelation = CompileGroupRegionRela.getIdRelationMap().get(redisDBIndex);
					if (maatVersionStr != null) {
						Long maatVersion = Long.valueOf(maatVersionStr) + 1;
						if (maatRelation != null && redisTemplate != null) {
							for (Integer service : serviceConfigMap.keySet()) {
								MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
								removeConfig(serviceConfigMap.get(service), maatXmlConfig, maatVersion, service,
										redisTemplate, maatRelation, redisDBIndex);

							}
							redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
							logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
									Integer.valueOf(maatVersionStr) + 1);
							count++;
						}
					}
				}
			}
			if (count == idMap.size()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除maat类配置
	 * @param idList 配置id集合
	 * @param maatXmlConfig maat.xml中关于当前业务类型的key和value写法的对象
	 * @param maatVersion 版本号
	 * @param service 业务类型
	 * @param redisTemplate 
	 * @param maatRelation id对应关系对象
	 */
	private void removeConfig(List<Long> idList, MaatXmlConfig maatXmlConfig, Long maatVersion, int service,
			RedisTemplate<String, String> redisTemplate, MaatRelation maatRelation, int redisDBIndex) {
		if (maatRelation != null && idList != null && idList.size() > 0 && maatXmlConfig != null) {
			for (Long id : idList) {
				Set<Long> compileIdList = new HashSet<Long>();
				compileIdList.add(id);
				removeCommonConfig(maatXmlConfig, compileIdList, 10, maatVersion.doubleValue(), service, redisTemplate,
						redisDBIndex);// 10代表是编译配置
				Set<Long> groupSet = maatRelation.getCompileAndGroupMap().get(id);
				if (groupSet != null && groupSet.size() > 0) {
					removeCommonConfig(maatXmlConfig, groupSet, 11, maatVersion.doubleValue(), service, redisTemplate,
							redisDBIndex);// 11代表是分组配置
					for (Long groupId : groupSet) {
						Set<Long> regionIdSet = maatRelation.getGroupAndRegionMap().get(groupId);
						if (regionIdSet != null && regionIdSet.size() > 0) {
							removeCommonConfig(maatXmlConfig, regionIdSet, 12, maatVersion.doubleValue(), service,
									redisTemplate, redisDBIndex);// 12代表是ip类域配置
							removeCommonConfig(maatXmlConfig, regionIdSet, 13, maatVersion.doubleValue(), service,
									redisTemplate, redisDBIndex);// 13代表是数值类配置
							removeCommonConfig(maatXmlConfig, regionIdSet, 14, maatVersion.doubleValue(), service,
									redisTemplate, redisDBIndex);// 14代表是字符串类域配置
							removeCommonConfig(maatXmlConfig, regionIdSet, 15, maatVersion.doubleValue(), service,
									redisTemplate, redisDBIndex);// 15代表是增强字符串类域配置
							removeCommonConfig(maatXmlConfig, regionIdSet, 16, maatVersion.doubleValue(), service,
									redisTemplate, redisDBIndex);// 16代表是文件摘要类域配置
							removeCommonConfig(maatXmlConfig, regionIdSet, 17, maatVersion.doubleValue(), service,
									redisTemplate, redisDBIndex);// 17代表是文本相似性域配置
						}
					}
				}
			}
		}
	}

	/**
	 * 删除maat配置时,对redis进行相关操作,主要是重命名key和记录相关状态
	 * @param maatXmlConfig
	 * @param idList
	 * @param type
	 * @param maatVersion
	 * @param service
	 * @param redisTemplate
	 */
	private void removeCommonConfig(MaatXmlConfig maatXmlConfig, Set<Long> idList, int type, Double maatVersion,
			int service, RedisTemplate<String, String> redisTemplate, int redisDBIndex) {
		if (maatXmlConfig != null && idList != null && idList.size() > 0) {
			List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
			String maatKey = null;
			for (Long id : idList) {
				for (MaatXmlExpr maatXmlExpr : expressionList) {
					if (type == maatXmlExpr.getType().intValue()) {
						StringBuffer keyBF = new StringBuffer();
						String[] keySplit = maatXmlExpr.getKeyExpression().split(";");
						for (String keyStr : keySplit) {
							if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("[")) {
								keyStr = keyStr.trim().replace("[", "").replace("]", "");
								keyBF.append(id);
							} else if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("{")) {
								keyStr = keyStr.trim().replace("{", "").replace("}", "");
								if (keyStr.toLowerCase().contains("table_name")) {
									keyBF.append(ServiceAndRDBIndexReal.getTableName(service, type));
								}

							} else {
								keyBF.append(keyStr.trim());
							}
						}
						String oldKey = keyBF.toString().toUpperCase();
						maatKey = oldKey.replace("EFFECTIVE_RULE", "OBSOLETE_RULE");
						if (redisTemplate.hasKey(oldKey)) {
							redisTemplate.rename(oldKey, maatKey.toUpperCase());
							logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是{}", redisDBIndex, oldKey,
									maatKey.toUpperCase());
							break;
						} else {
							throw new RuntimeException(redisDBIndex + "号redis库中不存在key=" + oldKey + "请检查id映射关系是否正确");
						}
					}
				}
				for (MaatXmlExpr maatXmlExpr : expressionList) {
					if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
						if (maatKey != null) {
							String zset = maatKey.replace("OBSOLETE_RULE:", "DEL,");
							redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset, maatVersion);

							logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}", redisDBIndex,
									zset.toUpperCase(), maatVersion);

						}

					} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
						if (maatKey != null) {

							Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
							redisTemplate.boundZSetOps("MAAT_RULE_TIMER").add(maatKey, score);
							logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}", redisDBIndex, maatKey, score);

						}
					} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
						Long nowTime = new Date().getTime();
						nowTime = nowTime / 1000l;
						Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
						redisTemplate.boundZSetOps("MAAT_VERSION_TIMER").add(maatVersion + "", score);
						logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex, maatVersion,
								score);

					}
				}
			}
		}
	}

	/**
	 * 删除配置成功后,需要更新编译,组,域等配置id的对应关系
	 * @param idMap
	 */
	public void delMaatRelation(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			for (Integer redisDBIndex : idMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
					// MaatRelation maatRelation =
					// CompileGroupRegionRela.getIdRelationMap().get(redisDBIndex);
					Map<Integer, List<Long>> map = idMap.get(redisDBIndex);
					if (map != null && map.size() > 0) {
						for (Integer service : map.keySet()) {
							List<Long> idList = map.get(service);
							if (idList != null && idList.size() > 0) {
								for (Long compileId : idList) {
									CompileGroupRegionRela.delIdRelation(redisDBIndex, compileId);
								}
							}
						}
					}
				}
			}
		}
	}

}
