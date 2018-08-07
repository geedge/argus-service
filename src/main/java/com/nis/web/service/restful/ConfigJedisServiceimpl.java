package com.nis.web.service.restful;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.domain.MaatXmlSeq;
import com.nis.domain.restful.MaatConfig;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Configurations;
import com.nis.util.JedisUtils;
import com.nis.util.ReadMaatXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;

@Service()
public class ConfigJedisServiceimpl implements ConfigRedisService {
	private static Logger logger = LoggerFactory.getLogger(ConfigJedisServiceimpl.class);
	private static final int TAPREDISDB = Configurations.getIntProperty("tapRedisDb", 7);
	// 用于在实时统计配置后面添加时间,方便读取入库时间
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	public boolean saveUnMaatConfig(Map<Integer, List<Map<String, String>>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			int count = 0;
			Jedis resource = JedisUtils.getResource(0);
			Transaction transaction = resource.multi();
			try {
				for (Integer redisDBIndex : configMap.keySet()) {
					if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
						transaction.select(redisDBIndex);
						List<Map<String, String>> listMap = configMap.get(redisDBIndex);
						if (listMap != null && listMap.size() > 0) {
							String maatVersionStr = JedisUtils.get("MAAT_VERSION", redisDBIndex);
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
													} else if (!StringUtils.isEmpty(keyStr)
															&& keyStr.trim().startsWith("{")) {
														keyStr = keyStr.trim().replace("{", "").replace("}", "");
														if (keyStr.toLowerCase().contains("table_name")) {
															String maatTableName = ServiceAndRDBIndexReal
																	.getUnMaatTableName(service);
															if (maatTableName == null) {
																throw new ServiceRuntimeException(
																		"无法从applicationConfig-rule.properties配置文件中,找到回调类配置service为" + service
																				+ "对应的真实表名",RestBusinessCode.NotFoundTableName.getValue());
															} else {
																keyBF.append(maatTableName);
															}
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
													} else {
														valBF.append(valStr);
													}
												}
												maatKey = keyBF.toString();
												transaction.set(keyBF.toString().toUpperCase(), valBF.toString());
												logger.info("向{}号redis数据库添加了一条配置,key是{},value是{}", redisDBIndex,
														keyBF.toString().toUpperCase(), valBF.toString());
												break;
											}
										}
										for (MaatXmlExpr maatXmlExpr : expressionList) {
											if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_UPDATE_STATUS")) {
												if (maatKey != null) {
													String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
													transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);
													logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}",
															redisDBIndex, zset.toUpperCase(), maatVersion);

												}
											} else if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_RULE_TIMER")) {
												if (maatKey != null) {
													Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
													transaction.zadd("MAAT_RULE_TIMER", score, maatKey);
													logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}",
															redisDBIndex, maatKey, score);

												}
											} else if (maatXmlExpr.getKeyExpression().toUpperCase()
													.equals("MAAT_VERSION_TIMER")) {
												Long nowTime = new Date().getTime();
												nowTime = nowTime / 1000l;
												Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
												transaction.zadd("MAAT_VERSION_TIMER", score, maatVersion + "");
												logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}",
														redisDBIndex, maatVersion, score);
											}
										}
										List<MaatXmlSeq> seqList = maatXmlConfig.getSequenceList();
										for (MaatXmlSeq maatXmlSeq : seqList) {
											String seqKey = maatXmlSeq.getSequenceKey();
											if (!seqKey.toUpperCase().equals("MAAT_VERSION")) {
												Integer operation = maatXmlSeq.getOperation();
												if (operation == 1) {
													transaction.incrBy(seqKey.toUpperCase(), 1l);
												}
											}

										}
									} else {
										throw new ServiceRuntimeException(
												"无法从applicationConfig-maatRedis.xml配置文件中获取service为" + service + "对应的规则,请检查业务类型是否正确",RestBusinessCode.NotFoundRedisRule.getValue());
									}
								}
								transaction.incrBy("MAAT_VERSION", 1l);
								logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
										Integer.valueOf(maatVersionStr) + 1);
								count++;
							}
						} else {
							throw new ServiceRuntimeException("向" + redisDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
						}
					} else {
						throw new ServiceRuntimeException("redis数据库编号:" + redisDBIndex + "不正确,请检查数据库编号",RestBusinessCode.DbIndexNotInRange.getValue());
					}
				}
				if (count == configMap.size()) {
					transaction.exec();
					return true;
				} else {
					transaction.discard();
				}
			} catch (JedisConnectionException e) {
				String error = "连接redis异常,保存非maat类配置失败," + e.getMessage();
//				logger.error(error);
				throw new ServiceRuntimeException(error, RestBusinessCode.CannotConnectionRedis.getValue());
				
			} catch (Exception e) {
				transaction.discard();
				String error = "保存非maat类配置发生了异常," + e.getMessage();
//				logger.error(error);
				throw new ServiceRuntimeException(error, RestBusinessCode.SaveDataInError.getValue());
			} finally {
				// 释放连接到连接池
				JedisUtils.returnResource(resource);
			}
		} else {
			throw new ServiceRuntimeException("向redis库添加配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
		}
		return false;
	}

	/**
	 *  下发配置成功后,需要更新编译,组,域等配置id的对应关系
	 */
	private void addMaatRelation(Map<Integer, List<MaatConfig>> configMap, Transaction transaction) {
		if (configMap != null && configMap.size() > 0) {
			int idRelaRedisDBIndex = Configurations.getIntProperty("idRelaRedisDBIndex", 15);
			transaction.select(idRelaRedisDBIndex);
			for (Integer redisDBIndex : configMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
					List<MaatConfig> maatConfigList = configMap.get(redisDBIndex);
					if (maatConfigList != null && maatConfigList.size() > 0) {
						Map<String, List<String>> compileAndGroupMap = new HashMap<String, List<String>>();
						Map<String, List<String>> groupAndCompileMap = new HashMap<String, List<String>>();
						for (MaatConfig maatConfig : maatConfigList) {
							String compileId = maatConfig.getCompileMap().get("compile_id");
							List<Map<String, String>> groupMapList = maatConfig.getGroupMapList();
							if (groupMapList != null && groupMapList.size() > 0) {
								for (Map<String, String> map : groupMapList) {
									String cfgIdStr = redisDBIndex + ":COMPILEGROUP:" + map.get("compile_id");
									String groupIdStr = redisDBIndex + ":GROUPCOMPILE:" + map.get("group_id");
									// + map.get("compile_id");
									if (cfgIdStr != null && groupIdStr != null && !cfgIdStr.equals("")
											&& !groupIdStr.equals("")) {
										if (compileAndGroupMap.containsKey(cfgIdStr.toUpperCase())) {
											compileAndGroupMap.get(cfgIdStr.toUpperCase())
													.add(groupIdStr.toUpperCase());
										} else {
											List<String> list = new ArrayList<String>();
											list.add(groupIdStr.toUpperCase());
											compileAndGroupMap.put(cfgIdStr.toUpperCase(), list);
										}
										if (groupAndCompileMap.containsKey(groupIdStr.toUpperCase())) {
											groupAndCompileMap.get(groupIdStr.toUpperCase())
													.add(cfgIdStr.toUpperCase());
										} else {
											List<String> list = new ArrayList<String>();
											list.add(cfgIdStr.toUpperCase());
											groupAndCompileMap.put(groupIdStr.toUpperCase(), list);
										}
									}
								}
							}

							Map<String, List<String>> map = new HashMap<String, List<String>>();
							int service = maatConfig.getService();
							MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
							addGroupAndRegionRelations(maatXmlConfig, service, 12, maatConfig.getIpRegionMapList(), map,
									redisDBIndex, compileId);
							addGroupAndRegionRelations(maatXmlConfig, service, 13, maatConfig.getNumRegionMapList(),
									map, redisDBIndex, compileId);
							addGroupAndRegionRelations(maatXmlConfig, service, 14, maatConfig.getStrRegionMapList(),
									map, redisDBIndex, compileId);
							addGroupAndRegionRelations(maatXmlConfig, service, 15, maatConfig.getStrStrRegionMapList(),
									map, redisDBIndex, compileId);
							addGroupAndRegionRelations(maatXmlConfig, service, 16,
									maatConfig.getFileDigestRegionMapList(), map, redisDBIndex, compileId);
							addGroupAndRegionRelations(maatXmlConfig, service, 17,
									maatConfig.getFileLikeRegionMapList(), map, redisDBIndex, compileId);
							addGroupAndRegionRelations(maatXmlConfig, service, 18, maatConfig.getIpClientRangeMapList(),
									map, redisDBIndex, compileId);

							for (String groupIdStr : map.keySet()) {
								List<String> list = map.get(groupIdStr);
								StringBuffer sb = new StringBuffer();
								if (list != null && list.size() > 0) {
									for (String regionIdStr : list) {
										sb.append(regionIdStr);
										sb.append(";");
									}
								}

								transaction.set(groupIdStr, sb.substring(0, sb.length() - 1));// 保存分组id和域id的关系(每个域配置,只会属于一个组)
							}

						}

						for (String compile : compileAndGroupMap.keySet()) {
							List<String> list = compileAndGroupMap.get(compile);
							StringBuffer sb = new StringBuffer();
							if (list != null && list.size() > 0) {
								for (String groupIdStr : list) {
									sb.append(groupIdStr);
									sb.append(";");
								}
							}
							transaction.set(compile, sb.substring(0, sb.length() - 1));// 保存分组id和域id的关系(每个域配置,只会属于一个组)
						}
						for (String group : groupAndCompileMap.keySet()) {
							List<String> list = groupAndCompileMap.get(group);
							StringBuffer sb = new StringBuffer();
							if (list != null && list.size() > 0) {
								for (String compile : list) {
									sb.append(compile);
									sb.append(";");
								}
							}
							transaction.append(group, sb.substring(0, sb.length() - 1));// 保存分组id和域id的关系(每个域配置,只会属于一个组)
							// redisTemplate.opsForValue().set(group, sb.substring(0, sb.length() - 1));//
							// 保存分组id和域id的关系(每个域配置,只会属于一个组)
						}

					}
				} else {
					throw new ServiceRuntimeException("redis数据库编号:" + redisDBIndex + "不正确,请检查数据库编号",RestBusinessCode.DbIndexNotInRange.getValue());
				}
			}
		} else {
			throw new ServiceRuntimeException("向redis库中配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
		}

	}

	/**
	 * 单独找一个redis-db记录配置信息,方便实时统计程序获取所有配置的分类性质,标签等内容,(为什么实时统计不从配置库redisdb获取呢,
	 * 因为配置有多分发的情况,会发送到不同的redisdb,每个redisdb的配置条数不一定相同,有的配置所有的redisdb都有,有的只在某一个redisdb中有,实时统计程序不好获取配置这些配置内容,
	 * 最重要的是,前端机不需要这些属性,所以在配置库有可能不带分类性质,标签等属性,maat.xml如果配置了则有,没有配置则就没有这些属性.所以单独找一个reidsdb存放带分类性质,标签等属性的配置)
	 * @param configMap
	 */
	private void addStatisticsReal(Map<Integer, List<MaatConfig>> configMap, Transaction transaction) {
		if (configMap != null && configMap.size() > 0) {
			for (Integer redisDBIndex : configMap.keySet()) {
				int redisStatisticsRealDBIndex = Configurations.getIntProperty("redisStatisticsRealDBIndex", 14);
				if (redisStatisticsRealDBIndex >= 0
						&& redisStatisticsRealDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 16)) {
					List<MaatConfig> list = configMap.get(redisDBIndex);
					if (list != null && list.size() > 0) {
						transaction.select(redisStatisticsRealDBIndex);
						String redisStatisticsReal = Configurations.getStringProperty("redis-statisticsReal",
								"[COMPILE_ID];\t;[SERVICE];\t;[ACTION];\t;[CONT_TYPE];\t;[ATTR_TYPE];\t;[CONT_LABEL];\t;[TASK_ID];\t;[AFFAIR_ID];\t;[DO_BLACKLIST];\t;[DO_LOG];\t;[EFFECTIVE_RANGE];\t;[START_TIME];\t;[END_TIME];\t;[USER_REGION];\t;[IS_VALID];\t;[GROUP_NUM];\t;[FATHER_CFG_ID];\t;[OP_TIME]");
						String[] redisStatisticsRealArr = redisStatisticsReal.split(";");

						String maatVersionStr = JedisUtils.get("MAAT_VERSION", redisStatisticsRealDBIndex);
						if (maatVersionStr == null) {
							maatVersionStr = "0";
						}
						if (maatVersionStr != null) {
							Long maatVersion = Long.valueOf(maatVersionStr) + 1;
							for (MaatConfig maatConfig : list) {
								int service = maatConfig.getService();
								MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
								Map<String, String> compileMap = maatConfig.getCompileMap();
								for (MaatXmlExpr maatXmlExpr : maatXmlConfig.getExpressionList()) {
									if (10 == maatXmlExpr.getType().intValue()) {
										StringBuffer keyBF = new StringBuffer();
										String[] keySplit = maatXmlExpr.getKeyExpression().split(";");
										for (String keyStr : keySplit) {
											if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("[")) {
												keyStr = keyStr.trim().replace("[", "").replace("]", "");
												String keyVal = compileMap.get(keyStr);
												if (keyVal != null && !keyVal.equals("")) {
													keyBF.append(keyVal);
												} else {
													throw new ServiceRuntimeException("未从map中获取到" + keyStr + "的值,请检查数据或配置文件是否正确",RestBusinessCode.NotFoundValueByKey.getValue());
												}
											} else if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("{")) {
												keyStr = keyStr.trim().replace("{", "").replace("}", "");
												if (keyStr.toLowerCase().contains("table_name")) {
													String argTableName = compileMap.get("table_name");
													String maatTableName = ServiceAndRDBIndexReal.getMaatTableName(
															service, 10, argTableName == null ? null : argTableName);
													if (maatTableName == null) {
														throw new ServiceRuntimeException(
																"无法从applicationConfig-rule.properties配置文件中,找到回调类配置service为" + service
																		+ ",配置类型:10,对应的真实表名",RestBusinessCode.NotFoundTableName.getValue());
													} else {
														keyBF.append(maatTableName);
													}

												}

											} else {
												keyBF.append(keyStr.trim());
											}
										}
										StringBuffer valBF = new StringBuffer();
										for (String valStr : redisStatisticsRealArr) {
											if (!StringUtils.isEmpty(valStr) && valStr.trim().startsWith("[")) {
												valStr = valStr.trim().replace("[", "").replace("]", "");
												String val = compileMap.get(valStr.toLowerCase());
												if (val != null) {
													valBF.append(val);
												} else {
													// 编译配置或分组配置 所有在maat.xml中配置的属性都不可以为空
													// if (!valStr.toLowerCase().equals("service")&&!
													// valStr.toLowerCase().equals("action")
													// &&!valStr.toLowerCase().equals("user_region") &&type==10) {
													throw new ServiceRuntimeException("未从map中获取到" + valStr + "的值,请检查数据或配置文件是否正确",RestBusinessCode.NotFoundValueByKey.getValue());

												}
											} else if (valStr.equals("\t")) {// xml中是字符串的\t这里判断的时候需要转义为\\t,但是添加的时候需要添加\t不是\\t
												valBF.append("\t");
											} else {
												valBF.append(valStr.trim());
											}
										}
										//向实时统计库中添加时间标识,方便人眼直观识别入库时间
										valBF.append("\t");
										valBF.append(sdf.format(new Date()));
										String maatKey = keyBF.toString();
										transaction.set(maatKey.toUpperCase(), valBF.toString());
										String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
										transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);

										logger.info("向{}号redis数据库添加了一条配置,key是{},value是{}", redisDBIndex,
												maatKey.toUpperCase(), valBF.toString());
										break;
									}

								}
							}
							transaction.incrBy("MAAT_VERSION", 1l);
						}
					} else {
						throw new ServiceRuntimeException(
								"向" + redisStatisticsRealDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
					}

				} else {
					throw new ServiceRuntimeException("redis数据库编号:" + redisStatisticsRealDBIndex + "不正确,请检查数据库编号",RestBusinessCode.DbIndexNotInRange.getValue());
				}
				break;// configMap中所有的value都是相同的,在记录配置新增或者取消时只记录一次即可
			}
		}
	}

	/**
	 * 封装组id与域id对应关系并添加到对象中
	 * @param regionMapList
	 * @param compileAndGroupRelations
	 */
	private Map<String, List<String>> addGroupAndRegionRelations(MaatXmlConfig maatXmlConfig, int service, int type,
			List<Map<String, String>> regionMapList, Map<String, List<String>> groupAndRegionMap, int redisDBIndex,
			String compileId) {
		if (regionMapList != null && regionMapList.size() > 0) {
			for (Map<String, String> map : regionMapList) {
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
									String argTableName = map.get("table_name");
									String maatTableName = ServiceAndRDBIndexReal.getMaatTableName(service, type,
											argTableName == null ? null : argTableName);
									if (maatTableName == null) {
										throw new ServiceRuntimeException("无法从applicationConfig-rule.properties配置文件中,找到回调类配置service为"+ service + ",配置类型:" + type + 
												",对应的真实表名",RestBusinessCode.NotFoundTableName.getValue());
									} else {
										keyBF.append(maatTableName);
									}
								}

							} else {
								keyBF.append(keyStr.trim());
							}
						}
						maatKey = keyBF.toString();
						break;
					}
				}
				// String groupIdStr = redisDBIndex + ":GROUPREGION:" + map.get("group_id") +
				// compileId;
				String groupIdStr = redisDBIndex + ":GROUPREGION:" + map.get("group_id");
				if (groupAndRegionMap.containsKey(groupIdStr.toUpperCase())) {
					groupAndRegionMap.get(groupIdStr.toUpperCase()).add(redisDBIndex + ":" + maatKey.toUpperCase());
				} else {
					List<String> list = new ArrayList<String>();
					list.add(redisDBIndex + ":" + maatKey.toUpperCase());
					groupAndRegionMap.put(groupIdStr.toUpperCase(), list);
				}
			}
		}
		return groupAndRegionMap;

	}

	@Override
	public boolean saveMaatConfig(Map<Integer, List<MaatConfig>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			int count = 0;
			Jedis resource = JedisUtils.getResource(0);
			Transaction transaction = resource.multi();
			try {
				for (Integer redisDBIndex : configMap.keySet()) {
					if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
						transaction.select(redisDBIndex);
						String maatVersionStr = JedisUtils.get("MAAT_VERSION", redisDBIndex);
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
									setConfig(maatConfig, maatXmlConfig, maatVersion, service, transaction,
											redisDBIndex);
								}
								transaction.incrBy("MAAT_VERSION", 1l);
								logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
										Integer.valueOf(maatVersionStr) + 1);
								count++;
							} else {
								throw new ServiceRuntimeException("向" + redisDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
							}
						}
					} else {
						throw new ServiceRuntimeException("redis数据库编号:" + redisDBIndex + "不正确,请检查数据库编号",RestBusinessCode.DbIndexNotInRange.getValue());
					}
				}
				if (count == configMap.size()) {
					addMaatRelation(configMap, transaction);
					addStatisticsReal(configMap, transaction);
					transaction.exec();
					return true;
				} else {
					transaction.exec();
				}
			} catch (JedisConnectionException e) {
				String error = "连接redis异常,保存maat类配置失败" + e.getMessage();
				logger.error(error);
				throw new ServiceRuntimeException(error, RestBusinessCode.CannotConnectionRedis.getValue());
			} catch (Exception e) {
				transaction.discard();
				String error = "保存maat类配置发生了异常" + e.getMessage();
				logger.error(error);
				throw new ServiceRuntimeException(error, RestBusinessCode.SaveDataInError.getValue());
			} finally {
				// 释放连接到连接池
				JedisUtils.returnResource(resource);
			}
		} else {
			throw new ServiceRuntimeException("写入Redis数据库中的配置信息不能为空",RestBusinessCode.ConfigSourceIsNull.getValue());
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
			Transaction transaction, Integer redisDBIndex) {
		Map<String, String> compileMap = maatConfig.getCompileMap();
		String compileId = compileMap.get("compile_id");
		if (compileMap != null && compileMap.size() > 0) {
			setCommonConfig(maatXmlConfig, compileMap, 10, maatVersion.doubleValue(), service, transaction,
					redisDBIndex, null);// 10代表是编译配置
		} else {
			throw new ServiceRuntimeException("无法从参数中获取" + redisDBIndex + "号redis库,业务类型为:" + service + "的编译配置信息,请检查配置参数是否正确",RestBusinessCode.NotFoundCompileInfo.getValue());
		}

		List<Map<String, String>> groupMapList = maatConfig.getGroupMapList();
		if (groupMapList != null && groupMapList.size() > 0) {
			for (Map<String, String> map : groupMapList) {
				setCommonConfig(maatXmlConfig, map, 11, maatVersion.doubleValue(), service, transaction, redisDBIndex,
						compileId);// 11代表是分组配置
			}
		} else {
			throw new ServiceRuntimeException("无法从参数中获取" + redisDBIndex + "号redis库,业务类型为:" + service + "的分组配置信息,请检查配置参数是否正确",RestBusinessCode.NotFoundGroupInfo.getValue());
		}

		List<Map<String, String>> ipRegionMapList = maatConfig.getIpRegionMapList();
		if (ipRegionMapList != null && ipRegionMapList.size() > 0) {
			for (Map<String, String> map : ipRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 12, maatVersion.doubleValue(), service, transaction, redisDBIndex,
						null);// 12代表是ip类域配置
			}
		}
		List<Map<String, String>> numRegionMapList = maatConfig.getNumRegionMapList();
		if (numRegionMapList != null && numRegionMapList.size() > 0) {
			for (Map<String, String> map : numRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 13, maatVersion.doubleValue(), service, transaction, redisDBIndex,
						null);// 13代表是数值类配置
			}
		}
		List<Map<String, String>> strRegionMapList = maatConfig.getStrRegionMapList();
		if (strRegionMapList != null && strRegionMapList.size() > 0) {
			for (Map<String, String> map : strRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 14, maatVersion.doubleValue(), service, transaction, redisDBIndex,
						null);// 14代表是字符串类域配置
			}
		}
		List<Map<String, String>> strStrRegionMapList = maatConfig.getStrStrRegionMapList();
		if (strStrRegionMapList != null && strStrRegionMapList.size() > 0) {
			for (Map<String, String> map : strStrRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 15, maatVersion.doubleValue(), service, transaction, redisDBIndex,
						null);// 15代表是增强字符串类域配置
			}
		}
		List<Map<String, String>> fileDigestRegionMapList = maatConfig.getFileDigestRegionMapList();
		if (fileDigestRegionMapList != null && fileDigestRegionMapList.size() > 0) {
			for (Map<String, String> map : fileDigestRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 16, maatVersion.doubleValue(), service, transaction, redisDBIndex,
						null);// 16代表是文件摘要类域配置
			}
		}
		List<Map<String, String>> fileLikeRegionMapList = maatConfig.getFileLikeRegionMapList();
		if (fileLikeRegionMapList != null && fileLikeRegionMapList.size() > 0) {
			for (Map<String, String> map : fileLikeRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 17, maatVersion.doubleValue(), service, transaction, redisDBIndex,
						null);// 17代表是文本相似性域配置
			}
		}

		List<Map<String, String>> ipclientList = maatConfig.getIpClientRangeMapList();
		if (ipclientList != null && ipclientList.size() > 0) {
			for (Map<String, String> map : ipclientList) {
				setCommonConfig(maatXmlConfig, map, 18, maatVersion.doubleValue(), service, transaction, redisDBIndex,
						null);// 18代表是区域ip域配置
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
			int service, Transaction transaction, Integer redisDBIndex, String compileId) {
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
							String keyVal = map.get(keyStr);
							if (keyVal != null && !keyVal.equals("")) {
								keyBF.append(keyVal);
							} else {
								throw new ServiceRuntimeException("未从map中获取到" + keyStr + "的值,请检查数据或配置文件是否正确",RestBusinessCode.NotFoundValueByKey.getValue());
							}
							if (type == 11 && keyStr.toLowerCase().equals("group_id")) {
								keyBF.append(compileId);
							}
						} else if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("{")) {
							keyStr = keyStr.trim().replace("{", "").replace("}", "");
							if (keyStr.toLowerCase().contains("table_name")) {
								String argTableName = map.get("table_name");
								String maatTableName = ServiceAndRDBIndexReal.getMaatTableName(service, type,
										argTableName == null ? null : argTableName);
								if (maatTableName == null) {
									throw new ServiceRuntimeException("无法从applicationConfig-rule.properties配置文件中,找到回调类配置service为"+ service + ",配置类型:" + type + 
											",对应的真实表名",RestBusinessCode.NotFoundTableName.getValue());
								} else {
									keyBF.append(maatTableName);
								}

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
							String val = map.get(valStr);
							if (val != null) {
								if (TAPREDISDB == redisDBIndex && type != 10 && type != 11
										&& valStr.toLowerCase().equals("op_time")) {// 域配置并且是op_time时在op_time前面添加如下内容
									Map<String, String[]> map2 = ServiceAndRDBIndexReal.getMaatToValveMap()
											.get(service);
									if (map2 != null && map2.size() > 0) {
										String[] arr = null;
										if (type == 12) {// IP类
											arr = map2.get("ipRegion");
										} else if (type == 13) {// 数值类
											arr = map2.get("numRegion");
										} else if (type == 14) {// 字符串类
											arr = map2.get("strRegion");
										} else if (type == 15) {// 增强字符串类
											arr = map2.get("strStrRegion");
										} else if (type == 16) {// 文件摘要类
											arr = map2.get("fileDigestRegion");
										} else if (type == 17) {// 文件摘要类
											arr = map2.get("fileLikeRegion");
										} else if (type == 18) {// 文件摘要类
											arr = map2.get("ipClientRange");
										}
										if (arr != null && arr.length > 0) {
											for (String str : arr) {
												valBF.append(map.get(str.toLowerCase()) + "\t");
											}
										}
									}
								}
								valBF.append(val);
							} else {
								// 所有在maat.xml中配置的属性都不可以为空
								throw new ServiceRuntimeException("未从map中获取到" + valStr + "的值,无法拼接redisValue,请检查数据或配置文件是否正确",RestBusinessCode.NotFoundValueByKey.getValue());
							}
						} else if (valStr.equals("&nbsp")) {
							valBF.append(" ");
						} else if (valStr.equals("\\t")) {// xml中是字符串的\t这里判断的时候需要转义为\\t,但是添加的时候需要添加\t不是\\t
							valBF.append("\t");
						} else if (valStr.equals("\\n")) {
							valBF.append("\n");
						} else {
							valBF.append(valStr.trim());
						}
					}
					maatKey = keyBF.toString();
					transaction.set(maatKey.toUpperCase(), valBF.toString());
					logger.info("向{}号redis数据库添加了一条配置,key是{},value是{}", redisDBIndex, maatKey.toUpperCase(),
							valBF.toString());
					break;
				}

			}
			for (MaatXmlExpr maatXmlExpr : expressionList) {
				if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
					if (maatKey != null) {
						String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
						transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);
						logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}", redisDBIndex,
								zset.toUpperCase(), maatVersion);
					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
					if (maatKey != null) {
						Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
						transaction.zadd("MAAT_RULE_TIMER", score, maatKey);
						logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}", redisDBIndex, maatKey, score);

					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
					Long nowTime = new Date().getTime();
					nowTime = nowTime / 1000l;
					Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
					transaction.zadd("MAAT_VERSION_TIMER", score, maatVersion + "");
					logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex, maatVersion, score);
				}
			}
		} else {
			if (maatXmlConfig == null) {
				throw new ServiceRuntimeException(
						"无法从applicationConfig-maatRedis.xml配置文件中,获取service为" + service + "对应的规则,请检查业务类型是否正确",RestBusinessCode.NotFoundRedisRule.getValue());
			} else {
				throw new ServiceRuntimeException("向redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
			}
		}
	}

	public Long getIncrId(String key) {
		Long id = JedisUtils.incrBy(key, 1l, 0);
		logger.info("从0号redis数据库获取{}成功,自增后的值是{}", key, id);
		return id;
	}

	@Override
	public boolean delUnMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap, boolean isInvalid) {
		if (idMap != null && idMap.size() > 0) {
			int count = 0;
			Jedis resource = JedisUtils.getResource(0);
			Transaction transaction = resource.multi();
			try {
				for (Integer redisDBIndex : idMap.keySet()) {
					transaction.select(redisDBIndex);
					Map<Integer, List<Long>> serviceConfigMap = idMap.get(redisDBIndex);
					if (serviceConfigMap != null && serviceConfigMap.size() > 0) {

						String maatVersionStr = JedisUtils.get("MAAT_VERSION", redisDBIndex);
						if (maatVersionStr == null) {
							throw new ServiceRuntimeException("从" + redisDBIndex
									+ "号redis库中获取MAAT_VERSION的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常",RestBusinessCode.GetMaatVersionFailure.getValue());
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

															if (isInvalid) {
																keyStr = "OBSOLETE_RULE";
															} else {
																keyStr = "EFFECTIVE_RULE";
															}

															keyBF.append(keyStr.trim());
														} else if (!StringUtils.isEmpty(keyStr)
																&& keyStr.trim().startsWith("[")) {
															// keyStr = keyStr.trim().replace("[", "").replace("]", "");
															keyBF.append(id);
														} else if (!StringUtils.isEmpty(keyStr)
																&& keyStr.trim().startsWith("{")) {
															keyStr = keyStr.trim().replace("{", "").replace("}", "");
															if (keyStr.toLowerCase().contains("table_name")) {
																String maatTableName = ServiceAndRDBIndexReal
																		.getUnMaatTableName(service);
																if (maatTableName == null) {
																	throw new ServiceRuntimeException(
																			"无法从applicationConfig-rule.properties配置文件中,找到回调类配置service为" + service
																					+ "对应的真实表名",RestBusinessCode.NotFoundTableName.getValue());
																} else {
																	keyBF.append(maatTableName);
																}
															}
														} else {
															keyBF.append(keyStr.trim());
														}
													}
													String oldKey = null;
													maatKey = keyBF.toString();
													if (isInvalid) {
														oldKey = maatKey.replace("OBSOLETE_RULE", "EFFECTIVE_RULE");
													} else {
														oldKey = maatKey.replace("EFFECTIVE_RULE", "OBSOLETE_RULE");
													}

													if (JedisUtils.exists(oldKey.toString().toUpperCase(),
															redisDBIndex)) {
														transaction.rename(oldKey.toString().toUpperCase(),
																maatKey.toString().toUpperCase());
														logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是{}",
																redisDBIndex, oldKey.toString().toUpperCase(),
																maatKey.toString().toUpperCase());
														break;
													} else {
														throw new ServiceRuntimeException(redisDBIndex + "号redis库中不存在key="
																+ oldKey + "请检查id映射关系是否正确",RestBusinessCode.KeyNotExistsInRedis.getValue());
													}
												}
											}
											for (MaatXmlExpr maatXmlExpr : expressionList) {
												if (maatXmlExpr.getKeyExpression().toUpperCase()
														.equals("MAAT_UPDATE_STATUS")) {
													if (maatKey != null) {
														if (isInvalid) {
															String zset = maatKey.replace("OBSOLETE_RULE:", "DEL,");
															transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);
															logger.info(
																	"向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}",
																	redisDBIndex, zset.toUpperCase(), maatVersion);
														} else {
															String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
															transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);
															logger.info(
																	"向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}",
																	redisDBIndex, zset.toUpperCase(), maatVersion);
														}
													}
												} else if (maatXmlExpr.getKeyExpression().toUpperCase()
														.equals("MAAT_RULE_TIMER")) {
													if (maatKey != null) {
														Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
														transaction.zadd("MAAT_RULE_TIMER", score, maatKey);
														logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}",
																redisDBIndex, maatKey, score);
													}
												} else if (maatXmlExpr.getKeyExpression().toUpperCase()
														.equals("MAAT_VERSION_TIMER")) {
													Long nowTime = new Date().getTime();
													nowTime = nowTime / 1000l;
													Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
													transaction.zadd("MAAT_VERSION_TIMER", score, maatVersion + "");
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
														transaction.incrBy(seqKey.toUpperCase(), 1l);
														// redisTemplate.boundValueOps(seqKey.toUpperCase()).increment(1l);
													}
												}

											}
										} else {
											throw new ServiceRuntimeException(
													"无法从applicationConfig-maatRedis.xml配置文件中获取service为" + service + "对应的规则,请检查业务类型是否正确",RestBusinessCode.NotFoundRedisRule.getValue());
										}
									}
									transaction.incrBy("MAAT_VERSION", 1l);
									// redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
									logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
											Integer.valueOf(maatVersionStr) + 1);
									count++;
								} else {
									throw new ServiceRuntimeException("无法从参数中获取" + redisDBIndex + "号redis库,业务类型为:" + service
											+ "的配置id信息,请检查配置参数是否正确",RestBusinessCode.CompileIdListIsNull.getValue());
								}
							}

						}
					} else {
						throw new ServiceRuntimeException("向" + redisDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ServiceAndCompileMapIsNull.getValue());
					}

				}
				if (count == idMap.size()) {
					transaction.exec();
					return true;
				} else {
					transaction.discard();
				}
			} catch (JedisConnectionException e) {
				String error = "连接redis异常,删除非maat类配置失败," + e.getMessage();
//				logger.error(error);
				throw new ServiceRuntimeException(error, RestBusinessCode.CannotConnectionRedis.getValue());
			} catch (Exception e) {
				transaction.discard();
				String error = "删除非maat类配置发生了异常," + e.getMessage();
//				logger.error(error);
				throw new ServiceRuntimeException(error, RestBusinessCode.DeleteDataInError.getValue());
			} finally {
				// 释放连接到连接池
				JedisUtils.returnResource(resource);
			}
		} else {
			throw new ServiceRuntimeException("状态更新操作Map参数信息不能为空,请检查!",RestBusinessCode.ConfigInfoMapIsNull.getValue());
		}
		return false;
	}

	@Override
	public boolean delMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			int count = 0;
			Jedis resource = JedisUtils.getResource(0);
			Transaction transaction = resource.multi();
			try {
				int idRelaRedisDBIndex = Configurations.getIntProperty("idRelaRedisDBIndex", 15);
				for (Integer redisDBIndex : idMap.keySet()) {
					//按序号选择Redis数据库
					transaction.select(redisDBIndex);
					Map<Integer, List<Long>> serviceConfigMap = idMap.get(redisDBIndex);
					if (serviceConfigMap != null && serviceConfigMap.size() > 0) {
						String maatVersionStr = JedisUtils.get("MAAT_VERSION", redisDBIndex);
						if (maatVersionStr != null) {
							Long maatVersion = Long.valueOf(maatVersionStr) + 1;
							for (Integer service : serviceConfigMap.keySet()) {
								MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
								removeConfig(serviceConfigMap.get(service), maatXmlConfig, maatVersion, service,
										transaction, redisDBIndex, idRelaRedisDBIndex);

							}
							transaction.incrBy("MAAT_VERSION", 1l);
							logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
									Integer.valueOf(maatVersionStr) + 1);
							count++;
						}else{
							throw new ServiceRuntimeException("从" + redisDBIndex
									+ "号redis库中获取MAAT_VERSION的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常",RestBusinessCode.GetMaatVersionFailure.getValue());
						}
					} else {
						throw new ServiceRuntimeException("从" + redisDBIndex + "号redis库删除配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
					}
				}
				if (count == idMap.size()) {
					delMaatRelation(idMap, transaction);
					delStatisticsReal(idMap, transaction);
					transaction.exec();
					return true;
				} else {
					transaction.discard();
				}

			} catch (JedisConnectionException e) {
				String error = "连接redis异常,删除maat配置失败," + e.getMessage();
//				logger.error(error);
				throw new ServiceRuntimeException(error, RestBusinessCode.CannotConnectionRedis.getValue());
				
			} catch (Exception e) {
				transaction.discard();
				
//				logger.error(error);
				int businessCode = RestBusinessCode.service_runtime_error.getValue();
				if (e instanceof ServiceRuntimeException) {
					businessCode = ((ServiceRuntimeException) e).getErrorCode();
				}
				throw new ServiceRuntimeException("删除maat配置发生了异常,"+ e.getMessage(),businessCode);	
			} finally {
				// 释放连接到连接池
				JedisUtils.returnResource(resource);
			}
		} else {
			throw new ServiceRuntimeException("Map参数信息不能为空,请检查!",RestBusinessCode.ConfigInfoMapIsNull.getValue());
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
			Transaction transaction, int redisDBIndex, int idRelaRedisDBIndex) {

		if (idList != null && idList.size() > 0 && maatXmlConfig != null) {
			for (Long id : idList) {
				//删除(重命名)编译配置
				removeCompileAndGroupConfig(maatXmlConfig, id + "", 10, maatVersion.doubleValue(), service, transaction,
						redisDBIndex, null);// 10代表是编译配置
				//拼接编译与分组关系的Redis Key
				String compileStr = redisDBIndex + ":COMPILEGROUP:" + id;
				//获取当前编译配置与分组配置的关联关系
				String groupCompileStrs = JedisUtils.get(compileStr, idRelaRedisDBIndex);
				if (groupCompileStrs != null && !groupCompileStrs.trim().equals("")) {
					String[] split = groupCompileStrs.split(";");
					for (String groupId : split) {
						String compileGroupStr = JedisUtils.get(groupId, idRelaRedisDBIndex);
						if (compileGroupStr != null && !compileGroupStr.trim().equals("")) {
							String[] compileGroupArr = compileGroupStr.split(";");// 获取组对应的编译id
							if (compileGroupArr != null && compileGroupArr.length == 1) {// 如果只有一个编译id且与上面的编译id相同则说明未被分组复用,可以将其下的所有域置失效,否则不处理域配置,只把编译,分组关系置为无效
								for (String compileId : compileGroupArr) {
									if (compileId.equals(compileStr)) {//
										String groupRegionKey = groupId.replace("GROUPCOMPILE", "GROUPREGION");
										String regionStr = JedisUtils.get(groupRegionKey, idRelaRedisDBIndex);
										if (regionStr != null && !regionStr.trim().equals("")) {
											String[] regionKeyArr = regionStr.split(";");
											if (regionKeyArr != null && regionKeyArr.length > 0) {
												//根据分组与域关联关系找到对应域配置然后删除(重命名)
												removeRegionConfig(maatXmlConfig, regionKeyArr,
														maatVersion.doubleValue(), service, transaction, redisDBIndex);
											}
										} else {
											throw new ServiceRuntimeException(
													"从" + idRelaRedisDBIndex + "号redis库中无法获取MAAT配置分组与域的关联关系，key为"+groupRegionKey,RestBusinessCode.KeyNotExistsInRedis.getValue());
										}
									}
								}
							}
							//根据分组与域关联关系找到对应域配置然后删除(重命名)
							removeCompileAndGroupConfig(maatXmlConfig,
									groupId.replace(redisDBIndex + ":GROUPCOMPILE:", ""), 11, maatVersion.doubleValue(),
									service, transaction, redisDBIndex, id + "");// 11代表是分组配置
						} else {
							throw new ServiceRuntimeException(
									"从" + idRelaRedisDBIndex + "号redis库中无法获取MAAT配置分组与编译的关联关系，key为"+groupId,RestBusinessCode.KeyNotExistsInRedis.getValue());
						}
					}
				} else {
					throw new ServiceRuntimeException(
							"从" + idRelaRedisDBIndex + "号redis库中无法获取MAAT配置编译与分组关联关系，key为"+compileStr,RestBusinessCode.KeyNotExistsInRedis.getValue());
				}
			}
		} else {
			if (StringUtils.isEmpty(maatXmlConfig)) {
				throw new ServiceRuntimeException(
						"无法从applicationConfig-maatRedis.xml配置文件中获取service为" + service + "对应的规则,请检查业务类型是否正确",RestBusinessCode.NotFoundRedisRule.getValue());
			}else {
				throw new ServiceRuntimeException("删除redis库中配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
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
	private void removeCompileAndGroupConfig(MaatXmlConfig maatXmlConfig, String id, int type, Double maatVersion,
			int service, Transaction transaction, int redisDBIndex, String compileId) {
		if (maatXmlConfig != null) {
			List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
			String maatKey = null;
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
								String maatTableName = ServiceAndRDBIndexReal.getMaatTableName(service, type, null);
								if (maatTableName == null) {
									throw new ServiceRuntimeException(
											"无法从applicationConfig-rule.properties配置文件中,找到回调类配置service为" + service
													+ "对应的真实表名",RestBusinessCode.NotFoundTableName.getValue());
								} else {
									keyBF.append(maatTableName);
								}
							}

						} else {
							keyBF.append(keyStr.trim());
						}
					}
					String oldKey = keyBF.toString().toUpperCase();
					if (compileId != null) {
						oldKey += compileId;
					}
					maatKey = oldKey.replace("EFFECTIVE_RULE", "OBSOLETE_RULE");
					if (JedisUtils.exists(oldKey, redisDBIndex)) {
						transaction.rename(oldKey, maatKey.toUpperCase());
						logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是{}", redisDBIndex, oldKey,
								maatKey.toUpperCase());
						break;
					} else {
						throw new ServiceRuntimeException(redisDBIndex + "号redis库中不存在key=" + oldKey
								+ "请检查id映射关系是否正确,或该配置已经被取消,已经被取消的配置不可再次取消,否则将抛出异常",RestBusinessCode.KeyNotExistsInRedis.getValue());
					}
				}
			}
			for (MaatXmlExpr maatXmlExpr : expressionList) {
				if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
					if (maatKey != null) {
						String zset = maatKey.replace("OBSOLETE_RULE:", "DEL,");

						transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);

						logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}", redisDBIndex,
								zset.toUpperCase(), maatVersion);

					}

				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
					if (maatKey != null) {
						Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
						transaction.zadd("MAAT_RULE_TIMER", score, maatKey);
						logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}", redisDBIndex, maatKey, score);

					}
				} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
					Long nowTime = new Date().getTime();
					nowTime = nowTime / 1000l;
					Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
					transaction.zadd("MAAT_VERSION_TIMER", score, maatVersion + "");
					logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex, maatVersion, score);

				}
			}
		} else {
			throw new ServiceRuntimeException(
					"无法从applicationConfig-maatRedis.xml配置文件中获取service为" + service + "对应的规则,请检查业务类型是否正确",RestBusinessCode.NotFoundRedisRule.getValue());
		}
	}

	/**
	 * 删除域配置,重名key,记录相关信息MAAT_UPDATE_STATUS,MAAT_RULE_TIMER等
	 * @param maatXmlConfig
	 * @param regionArr
	 * @param maatVersion
	 * @param service
	 * @param redisTemplate
	 * @param redisDBIndex
	 */
	private void removeRegionConfig(MaatXmlConfig maatXmlConfig, String[] regionArr, Double maatVersion, int service,
			Transaction transaction, int redisDBIndex) {
		if (maatXmlConfig != null && regionArr != null && regionArr.length > 0) {
			List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
			String maatKey = null;
			for (String oldKey : regionArr) {
				oldKey = oldKey.replace(redisDBIndex + ":", "");
				maatKey = oldKey.replace("EFFECTIVE_RULE", "OBSOLETE_RULE");
				if (JedisUtils.exists(oldKey, redisDBIndex)) {
					transaction.rename(oldKey, maatKey.toUpperCase());
					logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是{}", redisDBIndex, oldKey, maatKey.toUpperCase());
				} else {
					throw new ServiceRuntimeException(redisDBIndex + "号redis库中不存在key=" + oldKey + ",请检查id映射关系是否正确",RestBusinessCode.KeyNotExistsInRedis.getValue());
				}

				for (MaatXmlExpr maatXmlExpr : expressionList) {
					if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_UPDATE_STATUS")) {
						if (maatKey != null) {
							String zset = maatKey.replace("OBSOLETE_RULE:", "DEL,");
							transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);
							logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}", redisDBIndex,
									zset.toUpperCase(), maatVersion);

						}

					} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_RULE_TIMER")) {
						if (maatKey != null) {
							Double score = 0d;// 界面下发的配置没有超时时间所以这里设置为0
							transaction.zadd("MAAT_RULE_TIMER", score, maatKey);
							logger.info("向{}号redis数据库更新了MAAT_RULE_TIMER,内容是{},SCORES是{}", redisDBIndex, maatKey, score);

						}
					} else if (maatXmlExpr.getKeyExpression().toUpperCase().equals("MAAT_VERSION_TIMER")) {
						Long nowTime = new Date().getTime();
						nowTime = nowTime / 1000l;
						Double score = nowTime.doubleValue();// 使用redis自带的time,得到当前时间的秒
						transaction.zadd("MAAT_VERSION_TIMER", score, maatVersion + "");
						logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex, maatVersion,
								score);

					}
				}
			}
		} else {
			if (maatXmlConfig == null) {
				throw new ServiceRuntimeException(
						"无法从applicationConfig-maatRedis.xml配置文件中获取service为" + service + "对应的规则,请检查业务类型是否正确",RestBusinessCode.NotFoundRedisRule.getValue());
			} 

		}
	}

	/**
	 * 编译配置用于实时统计
	 * 取消分类性质,标签等信息
	 */
	private void delStatisticsReal(Map<Integer, Map<Integer, List<Long>>> idMap, Transaction transaction) {
		if (idMap != null && idMap.size() > 0) {
			int redisStatisticsRealDBIndex = Configurations.getIntProperty("redisStatisticsRealDBIndex", 14);
			transaction.select(redisStatisticsRealDBIndex);
			String maatVersionStr = JedisUtils.get("MAAT_VERSION", redisStatisticsRealDBIndex);
			if (maatVersionStr == null) {
				throw new ServiceRuntimeException("从" + redisStatisticsRealDBIndex
						+ "号redis库中获取MAAT_VERSION失败",RestBusinessCode.KeyNotExistsInRedis.getValue());
			}
			double maatVersion = Double.valueOf(maatVersionStr) + 1D;
			for (Integer redisDBIndex : idMap.keySet()) {
				if (redisStatisticsRealDBIndex >= 0
						&& redisStatisticsRealDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 16)) {
					Map<Integer, List<Long>> map = idMap.get(redisDBIndex);
					if (map != null && map.size() > 0) {
						for (Integer service : map.keySet()) {
							String maatTableName = ServiceAndRDBIndexReal.getMaatTableName(service, 10, null);
							List<Long> idList = map.get(service);
							if (idList != null && idList.size() > 0) {
								for (Long compileId : idList) {
									String effectiveRuleKey = "EFFECTIVE_RULE:" + maatTableName + "," + compileId;
									if (JedisUtils.exists(effectiveRuleKey.toUpperCase(), redisStatisticsRealDBIndex)) {
										transaction.rename(effectiveRuleKey.toUpperCase(), effectiveRuleKey
												.toUpperCase().replace("EFFECTIVE_RULE", "OBSOLETE_RULE"));
										logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是:{}",
												redisStatisticsRealDBIndex, effectiveRuleKey.toUpperCase(),
												effectiveRuleKey.toUpperCase().replace("EFFECTIVE_RULE",
														"OBSOLETE_RULE"));

										String zset = effectiveRuleKey.replace("EFFECTIVE_RULE:", "DEL,");
										transaction.zadd("MAAT_UPDATE_STATUS", maatVersion, zset);
										logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}",
												redisStatisticsRealDBIndex, zset.toUpperCase(), maatVersion);
									} else {
										throw new ServiceRuntimeException("从" + redisStatisticsRealDBIndex + "号redisDB中判断" + effectiveRuleKey.toUpperCase() + "是否存在失败", RestBusinessCode.ExistsKeyFailed.getValue());
									}

								}
							} else {
								throw new ServiceRuntimeException("取消配置时,未发现配置Id信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
							}

						}
						transaction.incrBy("MAAT_VERSION", 1l);
						logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisStatisticsRealDBIndex,
								Integer.valueOf(maatVersionStr) + 1);

					} else {
						throw new ServiceRuntimeException("删除redis库中配置时,未发现对应的配置信息,请检查配置参数是否正确",RestBusinessCode.ConfigSourceIsNull.getValue());
					}

				} else {
					throw new ServiceRuntimeException("redis数据库编号:" + redisStatisticsRealDBIndex + "不正确,请检查数据库编号",RestBusinessCode.DbIndexNotInRange.getValue());
				}
				break;// 因为所有的value都是相同的所以只取消一次就可以了
			}

		}

	}

	/**
	 * 删除配置成功后,需要更新编译,组,域等配置id的对应关系
	 * @param idMap
	 */
	private void delMaatRelation(Map<Integer, Map<Integer, List<Long>>> idMap, Transaction transaction) {
		if (idMap != null && idMap.size() > 0) {
			int idRelaRedisDBIndex = Configurations.getIntProperty("idRelaRedisDBIndex", 15);
			transaction.select(idRelaRedisDBIndex);
			for (Integer redisDBIndex : idMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
					Map<Integer, List<Long>> map = idMap.get(redisDBIndex);
					if (map != null && map.size() > 0) {
						for (Integer service : map.keySet()) {
							List<Long> idList = map.get(service);
							if (idList != null && idList.size() > 0) {
								for (Long compileId : idList) {
									String compileStr = redisDBIndex + ":COMPILEGROUP:" + compileId;
									String groupCompileStr = JedisUtils.get(compileStr, idRelaRedisDBIndex);// 根据编译id获取该编译下的分组关系
									if (groupCompileStr != null && !groupCompileStr.equals("")) {
										String[] groupCompileStrSplit = groupCompileStr.split(";");// 得到分组关系
										for (String groupCompile : groupCompileStrSplit) {// 遍历所有分组关系
											// String compileGroupStr = redisTemplate.opsForValue()
											// .get(groupCompile.toUpperCase());// 获取当前分组关系对应的编译信息

											String compileGroupStr = JedisUtils.get(groupCompile.toUpperCase(),
													idRelaRedisDBIndex);// 获取当前分组关系对应的编译信息

											if (compileGroupStr != null && !compileGroupStr.equals("")) {
												String[] compileGroupStrSplit = compileGroupStr.split(";");
												if (compileGroupStrSplit != null && compileGroupStrSplit.length == 1
														&& compileGroupStr.equals(compileStr.toUpperCase())) {// 当前的分组关系只属于当前的compileid,说明没有被分组复用,需要将编译配置,分组关系,域配置,置无效
													if (compileGroupStrSplit[0].toUpperCase()
															.equals(compileStr.toUpperCase())) {
														String groupRegion = groupCompile.replaceAll("GROUPCOMPILE",
																"GROUPREGION");
														//删除分组与域的关联关系
														if (JedisUtils.exists(groupRegion, idRelaRedisDBIndex)) {
															transaction.del(groupRegion);
														} else {
															throw new ServiceRuntimeException(
																	"从" + idRelaRedisDBIndex + "号redis库中无法获取MAAT配置分组与域的关联关系，key为"+groupRegion,RestBusinessCode.KeyNotExistsInRedis.getValue());
														}
														//删除分组与编译的关联关系
														if (JedisUtils.exists(groupCompile.toUpperCase(),
																idRelaRedisDBIndex)) {
															transaction.del(groupCompile.toUpperCase());// 删除当前组所对应的编译
														} else {
															throw new ServiceRuntimeException(
																	"从" + idRelaRedisDBIndex + "号redis库中无法获取MAAT配置分组与编译的关联关系，key为"+groupCompile.toUpperCase(),RestBusinessCode.KeyNotExistsInRedis.getValue());
														}

													}
												} else {// 当前的分组被复用了,属于多个编译,需要将当前的分组关系置为无效
													Set<String> groupCompileSet = new HashSet<String>();
													for (String compileGroup : compileGroupStrSplit) {
														if (!compileGroup.equals(compileStr.toUpperCase())) {
															groupCompileSet.add(compileGroup);
														}
													}
													StringBuffer sb = new StringBuffer();
													// 更新组对应的编译
													for (String str : groupCompileSet) {
														sb.append(str);
														sb.append(";");
													}
													transaction.set(groupCompile, sb.substring(0, sb.length() - 1));
												}
											} else {
												throw new ServiceRuntimeException(
														"从" + idRelaRedisDBIndex + "号redis库中无法获取MAAT配置分组与编译的关联关系，key为"+groupCompile.toUpperCase(),RestBusinessCode.KeyNotExistsInRedis.getValue());
											}
										}
										if (JedisUtils.exists(compileStr.toUpperCase(), idRelaRedisDBIndex)) {
											transaction.del(compileStr.toUpperCase());// 删除编译与分组的关联关系
										}

									} else {
										throw new ServiceRuntimeException("从" + idRelaRedisDBIndex + "号redis库中获取" + compileStr
												+ "的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常",RestBusinessCode.KeyNotExistsInRedis.getValue());

									}
								}
							}
						}
					}
				} else {
					throw new ServiceRuntimeException("redis数据库编号:" + redisDBIndex + "不正确,请检查数据库编号",RestBusinessCode.DbIndexNotInRange.getValue());
				}
			}
		} else {
			throw new ServiceRuntimeException("状态更新操作Map参数信息不能为空,请检查配置参数是否正确",RestBusinessCode.ConfigInfoMapIsNull.getValue());
		}
	}
}
