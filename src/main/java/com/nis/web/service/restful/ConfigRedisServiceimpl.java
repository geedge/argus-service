package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.nis.domain.restful.MaatConfig;
import com.nis.util.Configurations;
import com.nis.util.ReadMaatXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.web.dao.impl.BaseRedisDao;
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
					if (listMap != null && listMap.size() > 0) {
						String maatVersionStr = BaseRedisDao.getValByKey(redisDBIndex, "MAAT_VERSION");
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
															throw new RuntimeException(
																	"后台错误:未从业务类型和表对应关系中,找到非maat配置业务类型:" + service
																			+ "对应的真实表名");
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
												BaseRedisDao.getIncr(redisDBIndex, seqKey.toUpperCase());
												// redisTemplate.boundValueOps().increment(1l);
											}
										}

									}
								} else {
									throw new RuntimeException(
											"后台错误:无法从maat.xml中获取业务类型" + service + "对应的规则,请检查业务类型是否正确");
								}
							}
							BaseRedisDao.getIncr(redisDBIndex, "MAAT_VERSION");
							logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
									Integer.valueOf(maatVersionStr) + 1);
							count++;
						}
					} else {
						throw new RuntimeException("后台错误:向" + redisDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确");
					}
				} else {
					throw new RuntimeException("后台错误:redis数据库编号:" + redisDBIndex + "不正确,请检查数据库编号");
				}
			}
			if (count == configMap.size()) {
				return true;
			}
		} else {
			throw new RuntimeException("后台错误:参数不能为空");
		}
		return false;
	}

	/**
	 *  下发配置成功后,需要更新编译,组,域等配置id的对应关系
	 */
	private void addMaatRelation(Map<Integer, List<MaatConfig>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			int idRelaRedisDBIndex = Configurations.getIntProperty("idRelaRedisDBIndex", 15);
			RedisTemplate<String, String> redisTemplate = SpringContextHolder
					.getBean("redisTemplate" + idRelaRedisDBIndex);
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
								redisTemplate.opsForValue().set(groupIdStr, sb.substring(0, sb.length() - 1));// 保存分组id和域id的关系(每个域配置,只会属于一个组)
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
							redisTemplate.opsForValue().set(compile, sb.substring(0, sb.length() - 1));// 保存分组id和域id的关系(每个域配置,只会属于一个组)
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
							redisTemplate.opsForValue().append(group, sb.substring(0, sb.length() - 1));// 保存分组id和域id的关系(每个域配置,只会属于一个组)
							// redisTemplate.opsForValue().set(group, sb.substring(0, sb.length() - 1));//
							// 保存分组id和域id的关系(每个域配置,只会属于一个组)
						}

					}
				} else {
					throw new RuntimeException("后台错误:redis数据库编号:" + redisDBIndex + "不正确,请检查数据库编号");
				}
			}
		} else {
			throw new RuntimeException("后台错误:参数不能为空");
		}

	}

	/**
	 * 单独找一个redis-db记录配置信息,方便实时统计程序获取所有配置的分类性质,标签等内容,(为什么实时统计不从配置库redisdb获取呢,
	 * 因为配置有多分发的情况,会发送到不同的redisdb,每个redisdb的配置条数不一定相同,有的配置所有的redisdb都有,有的只在某一个redisdb中有,实时统计程序不好获取配置这些配置内容,
	 * 最重要的是,前端机不需要这些属性,所以在配置库有可能不带分类性质,标签等属性,maat.xml如果配置了则有,没有配置则就没有这些属性.所以单独找一个reidsdb存放带分类性质,标签等属性的配置)
	 * @param configMap
	 */
	private void addStatisticsReal(Map<Integer, List<MaatConfig>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			for (Integer redisDBIndex : configMap.keySet()) {
				int redisStatisticsRealDBIndex = Configurations.getIntProperty("redisStatisticsRealDBIndex", 14);
				if (redisStatisticsRealDBIndex >= 0
						&& redisStatisticsRealDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 16)) {
					List<MaatConfig> list = configMap.get(redisDBIndex);
					if (list != null && list.size() > 0) {
						RedisTemplate<String, String> redisTemplate = SpringContextHolder
								.getBean("redisTemplate" + redisStatisticsRealDBIndex);
						String redisStatisticsReal = Configurations.getStringProperty("redis-statisticsReal",
								"[COMPILE_ID];\t;[SERVICE];\t;[ACTION];\t;[CONT_TYPE];\t;[ATTR_TYPE];\t;[CONT_LABEL];\t;[TASK_ID];\t;[AFFAIR_ID];\t;[DO_BLACKLIST];\t;[DO_LOG];\t;[EFFECTIVE_RANGE];\t;[START_TIME];\t;[END_TIME];\t;[USER_REGION];\t;[IS_VALID];\t;[GROUP_NUM];\t;[FATHER_CFG_ID];\t;[OP_TIME]");
						String[] redisStatisticsRealArr = redisStatisticsReal.split(";");
						String maatVersionStr = BaseRedisDao.getValByKey(redisStatisticsRealDBIndex, "MAAT_VERSION");
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
													throw new RuntimeException(
															"后台错误:未从map中获取到" + keyStr + "的值,无法拼接redisKey,请检查数据是否正确");
												}
											} else if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("{")) {
												keyStr = keyStr.trim().replace("{", "").replace("}", "");
												if (keyStr.toLowerCase().contains("table_name")) {
													String argTableName = compileMap.get("table_name");
													String maatTableName = ServiceAndRDBIndexReal.getMaatTableName(
															service, 10, argTableName == null ? null : argTableName);
													if (maatTableName == null) {
														throw new RuntimeException("后台错误:未从业务类型和表对应关系中,找到业务类型:"
																+ service + ",配置类型:10,对应的真实表名");
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
													throw new RuntimeException(
															"后台错误:未从map中获取到" + valStr + "的值,无法拼接redisValue,请检查数据是否正确");

												}
											} else if (valStr.equals("\t")) {// xml中是字符串的\t这里判断的时候需要转义为\\t,但是添加的时候需要添加\t不是\\t
												valBF.append("\t");
											} else {
												valBF.append(valStr.trim());
											}
										}
										String maatKey = keyBF.toString();
										redisTemplate.opsForValue().set(maatKey.toUpperCase(), valBF.toString());
										String zset = maatKey.replace("EFFECTIVE_RULE:", "ADD,");
										redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset, maatVersion);
										logger.info("向{}号redis数据库添加了一条配置,key是{},value是{}", redisDBIndex,
												maatKey.toUpperCase(), valBF.toString());
										break;
									}

								}
							}
							BaseRedisDao.getIncr(redisStatisticsRealDBIndex, "MAAT_VERSION");
						}
					} else {
						throw new RuntimeException(
								"后台错误:向" + redisStatisticsRealDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确");
					}

				} else {
					throw new RuntimeException("后台错误:redis数据库编号:" + redisStatisticsRealDBIndex + "不正确,请检查数据库编号");
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
										throw new RuntimeException(
												"后台错误:未从业务类型和表对应关系中,找到业务类型:" + service + ",配置类型:" + type + ",对应的真实表名");
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
	@Transactional
	public boolean saveMaatConfig(Map<Integer, List<MaatConfig>> configMap) {
		if (configMap != null && configMap.size() > 0) {
			int count = 0;
			for (Integer redisDBIndex : configMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
					RedisTemplate<String, String> redisTemplate = SpringContextHolder
							.getBean("redisTemplate" + redisDBIndex);
					String maatVersionStr = BaseRedisDao.getValByKey(redisDBIndex, "MAAT_VERSION");
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
							BaseRedisDao.getIncr(redisDBIndex, "MAAT_VERSION");
							// redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
							logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
									Integer.valueOf(maatVersionStr) + 1);
							count++;
						} else {
							throw new RuntimeException(
									"后台错误:向" + redisDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确");
						}

					}

				} else {
					throw new RuntimeException("后台错误:redis数据库编号:" + redisDBIndex + "不正确,请检查数据库编号");
				}
			}
			if (count == configMap.size()) {
				addMaatRelation(configMap);
				addStatisticsReal(configMap);
				return true;
			}
		} else {
			throw new RuntimeException("后台错误:参数不能为空");
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
		String compileId = compileMap.get("compile_id");
		if (compileMap != null && compileMap.size() > 0) {
			setCommonConfig(maatXmlConfig, compileMap, 10, maatVersion.doubleValue(), service, redisTemplate,
					redisDBIndex, null);// 10代表是编译配置
		} else {
			throw new RuntimeException(
					"后台错误:无法从参数中获取" + redisDBIndex + "号redis库,业务类型为:" + service + "的编译配置信息,请检查配置参数是否正确");
		}

		List<Map<String, String>> groupMapList = maatConfig.getGroupMapList();
		if (groupMapList != null && groupMapList.size() > 0) {
			for (Map<String, String> map : groupMapList) {
				setCommonConfig(maatXmlConfig, map, 11, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex,
						compileId);// 11代表是分组配置
			}
		} else {
			throw new RuntimeException(
					"后台错误:无法从参数中获取" + redisDBIndex + "号redis库,业务类型为:" + service + "的分组配置信息,请检查配置参数是否正确");
		}

		List<Map<String, String>> ipRegionMapList = maatConfig.getIpRegionMapList();
		if (ipRegionMapList != null && ipRegionMapList.size() > 0) {
			for (Map<String, String> map : ipRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 12, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex,
						null);// 12代表是ip类域配置
			}
		}
		List<Map<String, String>> numRegionMapList = maatConfig.getNumRegionMapList();
		if (numRegionMapList != null && numRegionMapList.size() > 0) {
			for (Map<String, String> map : numRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 13, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex,
						null);// 13代表是数值类配置
			}
		}
		List<Map<String, String>> strRegionMapList = maatConfig.getStrRegionMapList();
		if (strRegionMapList != null && strRegionMapList.size() > 0) {
			for (Map<String, String> map : strRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 14, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex,
						null);// 14代表是字符串类域配置
			}
		}
		List<Map<String, String>> strStrRegionMapList = maatConfig.getStrStrRegionMapList();
		if (strStrRegionMapList != null && strStrRegionMapList.size() > 0) {
			for (Map<String, String> map : strStrRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 15, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex,
						null);// 15代表是增强字符串类域配置
			}
		}
		List<Map<String, String>> fileDigestRegionMapList = maatConfig.getFileDigestRegionMapList();
		if (fileDigestRegionMapList != null && fileDigestRegionMapList.size() > 0) {
			for (Map<String, String> map : fileDigestRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 16, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex,
						null);// 16代表是文件摘要类域配置
			}
		}
		List<Map<String, String>> fileLikeRegionMapList = maatConfig.getFileLikeRegionMapList();
		if (fileLikeRegionMapList != null && fileLikeRegionMapList.size() > 0) {
			for (Map<String, String> map : fileLikeRegionMapList) {
				setCommonConfig(maatXmlConfig, map, 17, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex,
						null);// 17代表是文本相似性域配置
			}
		}

		List<Map<String, String>> ipclientList = maatConfig.getIpClientRangeMapList();
		if (ipclientList != null && ipclientList.size() > 0) {
			for (Map<String, String> map : ipclientList) {
				setCommonConfig(maatXmlConfig, map, 18, maatVersion.doubleValue(), service, redisTemplate, redisDBIndex,
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
			int service, RedisTemplate<String, String> redisTemplate, Integer redisDBIndex, String compileId) {
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
								throw new RuntimeException("后台错误:未从map中获取到" + keyStr + "的值,无法拼接redisKey,请检查数据是否正确");
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
									throw new RuntimeException(
											"后台错误:未从业务类型和表对应关系中,找到业务类型:" + service + ",配置类型:" + type + ",对应的真实表名");
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
								valBF.append(val);
							} else {
								if (type != 10 && type != 11) {// 域配置
									if (valStr.toLowerCase().equals("service") || valStr.toLowerCase().equals("action")
											|| valStr.toLowerCase().equals("user_region")) {// 域配置中只有这三个可以为空
										// 删除前面的\t
									} else {// 其他不可以为空
										throw new RuntimeException(
												"后台错误:未从map中获取到" + valStr + "的值,无法拼接redisValue,请检查数据是否正确");
									}
								} else {// 编译配置或分组配置 所有在maat.xml中配置的属性都不可以为空
									// if (!valStr.toLowerCase().equals("service")&&!
									// valStr.toLowerCase().equals("action")
									// &&!valStr.toLowerCase().equals("user_region") &&type==10) {
									throw new RuntimeException(
											"后台错误:未从map中获取到" + valStr + "的值,无法拼接redisValue,请检查数据是否正确");
								}
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
		} else {
			throw new RuntimeException("后台错误:无法获取业务类型" + service + "对应的maat规则或传入的配置信息有误,请检查!");
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
		Long id = BaseRedisDao.getIncr(0, key);
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
					String maatVersionStr = BaseRedisDao.getValByKey(redisDBIndex, "MAAT_VERSION");
					if (maatVersionStr == null) {
						throw new RuntimeException("后台错误:从" + redisDBIndex
								+ "号redis库中获取MAAT_VERSION的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");
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
													} else if (!StringUtils.isEmpty(keyStr)
															&& keyStr.trim().startsWith("{")) {
														keyStr = keyStr.trim().replace("{", "").replace("}", "");
														if (keyStr.toLowerCase().contains("table_name")) {
															String maatTableName = ServiceAndRDBIndexReal
																	.getUnMaatTableName(service);
															if (maatTableName == null) {
																throw new RuntimeException(
																		"后台错误:未从业务类型和表对应关系中,找到非maat配置业务类型:" + service
																				+ "对应的真实表名");
															} else {
																keyBF.append(maatTableName);
															}
														}
													} else {
														keyBF.append(keyStr.trim());
													}
												}
												maatKey = keyBF.toString();
												String oldKey = maatKey.replace("OBSOLETE_RULE", "EFFECTIVE_RULE");
												if (BaseRedisDao.keyIsExist(redisDBIndex,
														oldKey.toString().toUpperCase())) {
													redisTemplate.rename(oldKey.toString().toUpperCase(),
															keyBF.toString().toUpperCase());
													logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是{}", redisDBIndex,
															oldKey.toString().toUpperCase(),
															keyBF.toString().toUpperCase());
													break;
												} else {
													throw new RuntimeException("后台错误:" + redisDBIndex
															+ "号redis库中不存在key=" + oldKey + "请检查id映射关系是否正确");
												}
											}
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

													BaseRedisDao.getIncr(redisDBIndex, seqKey.toUpperCase());
													// redisTemplate.boundValueOps(seqKey.toUpperCase()).increment(1l);
												}
											}

										}
									} else {
										throw new RuntimeException(
												"后台错误:无法从maat.xml中获取业务类型" + service + "对应的规则,请检查业务类型是否正确");
									}
								}
								BaseRedisDao.getIncr(redisDBIndex, "MAAT_VERSION");
								// redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
								logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
										Integer.valueOf(maatVersionStr) + 1);
								count++;
							} else {
								throw new RuntimeException("后台错误:无法从参数中获取" + redisDBIndex + "号redis库,业务类型为:" + service
										+ "的配置id信息,请检查配置参数是否正确");
							}
						}

					}
				} else {
					throw new RuntimeException("后台错误:向" + redisDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确");
				}

			}
			if (count == idMap.size()) {
				return true;
			}
		} else {
			throw new RuntimeException("后台错误:参数信息有误,请检查!");
		}
		return false;
	}

	@Override
	@Transactional
	public boolean delMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			int count = 0;
			int idRelaRedisDBIndex = Configurations.getIntProperty("idRelaRedisDBIndex", 15);
			for (Integer redisDBIndex : idMap.keySet()) {
				RedisTemplate<String, String> redisTemplate = SpringContextHolder
						.getBean("redisTemplate" + redisDBIndex);
				Map<Integer, List<Long>> serviceConfigMap = idMap.get(redisDBIndex);
				if (serviceConfigMap != null && serviceConfigMap.size() > 0) {
					String maatVersionStr = BaseRedisDao.getValByKey(redisDBIndex, "MAAT_VERSION");
					if (maatVersionStr == null) {
						throw new RuntimeException("后台错误:从" + redisDBIndex
								+ "号redis库中获取MAAT_VERSION的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");
					}
					// MaatRelation maatRelation =
					// CompileGroupRegionRela.getIdRelationMap().get(redisDBIndex);
					if (maatVersionStr != null) {
						Long maatVersion = Long.valueOf(maatVersionStr) + 1;
						// if (maatRelation != null && redisTemplate != null) {
						for (Integer service : serviceConfigMap.keySet()) {
							MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
							removeConfig(serviceConfigMap.get(service), maatXmlConfig, maatVersion, service,
									redisTemplate, redisDBIndex, idRelaRedisDBIndex);

						}
						BaseRedisDao.getIncr(redisDBIndex, "MAAT_VERSION");
						// redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
						logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDBIndex,
								Integer.valueOf(maatVersionStr) + 1);
						count++;
						// }
					}
				} else {
					throw new RuntimeException("后台错误:向" + redisDBIndex + "号redis库中添加配置时,未发现对应的配置信息,请检查配置参数是否正确");
				}
			}
			if (count == idMap.size()) {
				delMaatRelation(idMap);
				delStatisticsReal(idMap);
				return true;
			}
		} else {
			throw new RuntimeException("后台错误:参数信息有误,请检查!");
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
			RedisTemplate<String, String> redisTemplate, int redisDBIndex, int idRelaRedisDBIndex) {

		if (idList != null && idList.size() > 0 && maatXmlConfig != null) {
			for (Long id : idList) {
				removeCompileAndGroupConfig(maatXmlConfig, id + "", 10, maatVersion.doubleValue(), service,
						redisTemplate, redisDBIndex, null);// 10代表是编译配置
				String compileStr = redisDBIndex + ":COMPILEGROUP:" + id;
				// 获取当前编译下所有的分组id
				String groupCompileStrs = BaseRedisDao.getValByKey(idRelaRedisDBIndex, compileStr);
				if (groupCompileStrs != null && !groupCompileStrs.trim().equals("")) {
					String[] split = groupCompileStrs.split(";");
					for (String groupId : split) {
						String compileGroupStr = BaseRedisDao.getValByKey(idRelaRedisDBIndex, groupId);
						if (compileGroupStr != null && !compileGroupStr.trim().equals("")) {
							String[] compileGroupArr = compileGroupStr.split(";");// 获取组对应的编译id
							if (compileGroupArr != null && compileGroupArr.length == 1) {// 如果只有一个编译id且与上面的编译id相同则说明未被分组复用,可以将其下的所有域置失效,否则不处理域配置,只把编译,分组关系置为无效
								for (String compileId : compileGroupArr) {
									if (compileId.equals(compileStr)) {//
										String groupRegionKey = groupId.replace("GROUPCOMPILE", "GROUPREGION");
										String regionStr = BaseRedisDao.getValByKey(idRelaRedisDBIndex, groupRegionKey);
										if (regionStr != null && !regionStr.trim().equals("")) {
											String[] regionKeyArr = regionStr.split(";");
											if (regionKeyArr != null && regionKeyArr.length > 0) {
												removeRegionConfig(maatXmlConfig, regionKeyArr,
														maatVersion.doubleValue(), service, redisTemplate,
														redisDBIndex);
											}
										} else {
											throw new RuntimeException(
													"后台错误:从" + idRelaRedisDBIndex + "号redis库中获取" + groupRegionKey
															+ "的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");

										}
									}
								}
							}
							removeCompileAndGroupConfig(maatXmlConfig,
									groupId.replace(redisDBIndex + ":GROUPCOMPILE:", ""), 11, maatVersion.doubleValue(),
									service, redisTemplate, redisDBIndex, id + "");// 11代表是分组配置
						} else {
							throw new RuntimeException("后台错误:从" + idRelaRedisDBIndex + "号redis库中获取" + groupId
									+ "的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");

						}
					}
				} else {
					throw new RuntimeException("后台错误:从" + idRelaRedisDBIndex + "号redis库中获取" + compileStr
							+ "的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");
				}
			}
		} else {
			throw new RuntimeException("后台错误:无法获取内存中记录的id映射关系,无法获取业务类型" + service + "对应的maat规则或传入的配置id有误,请检查!");
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
			int service, RedisTemplate<String, String> redisTemplate, int redisDBIndex, String compileId) {
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
									throw new RuntimeException(
											"后台错误:未从业务类型和表对应关系中,找到业务类型:" + service + ",配置类型:" + type + ",对应的真实表名");
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
					if (BaseRedisDao.keyIsExist(redisDBIndex, oldKey)) {
						redisTemplate.rename(oldKey, maatKey.toUpperCase());
						logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是{}", redisDBIndex, oldKey,
								maatKey.toUpperCase());
						break;
					} else {
						throw new RuntimeException("后台错误:" + redisDBIndex + "号redis库中不存在key=" + oldKey
								+ "请检查id映射关系是否正确,或该配置已经被取消,已经被取消的配置不可再次取消,否则将抛出异常");
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
					logger.info("向{}号redis数据库更新了MAAT_VERSION_TIMER,内容是{},SCORES是{}", redisDBIndex, maatVersion, score);

				}
			}
		} else {
			throw new RuntimeException("后台错误:无法获取业务类型" + service + "对应的maat规则,请检查!");
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
			RedisTemplate<String, String> redisTemplate, int redisDBIndex) {
		if (maatXmlConfig != null && regionArr != null && regionArr.length > 0) {
			List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
			String maatKey = null;
			for (String oldKey : regionArr) {
				oldKey = oldKey.replace(redisDBIndex + ":", "");
				maatKey = oldKey.replace("EFFECTIVE_RULE", "OBSOLETE_RULE");
				if (BaseRedisDao.keyIsExist(redisDBIndex, oldKey)) {
					redisTemplate.rename(oldKey, maatKey.toUpperCase());
					logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是{}", redisDBIndex, oldKey, maatKey.toUpperCase());
				} else {
					throw new RuntimeException("后台错误:" + redisDBIndex + "号redis库中不存在key=" + oldKey + "请检查id映射关系是否正确");
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
		} else {
			throw new RuntimeException("后台错误:无法获取业务类型" + service + "对应的maat规则或传入的配置信息有误,请检查!");
		}
	}

	/**
	 * 取消分类性质,标签等信息
	 */
	private void delStatisticsReal(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			int redisStatisticsRealDBIndex = Configurations.getIntProperty("redisStatisticsRealDBIndex", 14);
			RedisTemplate<String, String> redisTemplate = SpringContextHolder
					.getBean("redisTemplate" + redisStatisticsRealDBIndex);
			String maatVersionStr = BaseRedisDao.getValByKey(redisStatisticsRealDBIndex, "MAAT_VERSION");
			if (maatVersionStr == null) {
				throw new RuntimeException("后台错误:从" + redisStatisticsRealDBIndex
						+ "号redis库中获取MAAT_VERSION的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");
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
									if (BaseRedisDao.keyIsExist(redisStatisticsRealDBIndex,
											effectiveRuleKey.toUpperCase())) {
										redisTemplate.rename(effectiveRuleKey.toUpperCase(), effectiveRuleKey
												.toUpperCase().replace("EFFECTIVE_RULE", "OBSOLETE_RULE"));
										logger.info("向{}号redis数据库修改了一条配置,修改前key是{},修改后key是:{}",
												redisStatisticsRealDBIndex, effectiveRuleKey.toUpperCase(), effectiveRuleKey
												.toUpperCase().replace("EFFECTIVE_RULE", "OBSOLETE_RULE"));
										
										String zset = effectiveRuleKey.replace("EFFECTIVE_RULE:", "DEL,");
										redisTemplate.boundZSetOps("MAAT_UPDATE_STATUS").add(zset, maatVersion);
										logger.info("向{}号redis数据库更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}",
												redisStatisticsRealDBIndex, zset.toUpperCase(), maatVersion);
									} else {
										throw new RuntimeException("后台错误:从" + redisStatisticsRealDBIndex + "号redis库中判断"
												+ effectiveRuleKey.toUpperCase()
												+ "组和域关系时不存在,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");

									}

								}
							} else {
								throw new RuntimeException("后台错误:删除配置时,配置id不能为空,请联系开发人员检查删除逻辑是否正确");
							}

						}

						BaseRedisDao.getIncr(redisStatisticsRealDBIndex, "MAAT_VERSION");
						logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisStatisticsRealDBIndex,
								Integer.valueOf(maatVersionStr) + 1);

					} else {
						throw new RuntimeException("后台错误:删除配置时,配置id不能为空,请联系开发人员检查删除逻辑是否正确");
					}

				} else {
					throw new RuntimeException("后台错误:redis数据库编号:" + redisStatisticsRealDBIndex + "不正确,请检查数据库编号");
				}
				break;// 因为所有的value都是相同的所以只取消一次就可以了
			}

		}

	}

	/**
	 * 删除配置成功后,需要更新编译,组,域等配置id的对应关系
	 * @param idMap
	 */
	private void delMaatRelation(Map<Integer, Map<Integer, List<Long>>> idMap) {
		if (idMap != null && idMap.size() > 0) {
			int idRelaRedisDBIndex = Configurations.getIntProperty("idRelaRedisDBIndex", 15);
			RedisTemplate<String, String> redisTemplate = SpringContextHolder
					.getBean("redisTemplate" + idRelaRedisDBIndex);
			for (Integer redisDBIndex : idMap.keySet()) {
				if (redisDBIndex >= 0 && redisDBIndex < Configurations.getIntProperty("maxRedisDBIndex", 6)) {
					Map<Integer, List<Long>> map = idMap.get(redisDBIndex);
					if (map != null && map.size() > 0) {
						for (Integer service : map.keySet()) {
							List<Long> idList = map.get(service);
							if (idList != null && idList.size() > 0) {
								for (Long compileId : idList) {
									String compileStr = redisDBIndex + ":COMPILEGROUP:" + compileId;
									String groupCompileStr = BaseRedisDao.getValByKey(idRelaRedisDBIndex, compileStr);// 根据编译id获取该编译下的分组关系
									if (groupCompileStr != null && !groupCompileStr.equals("")) {
										String[] groupCompileStrSplit = groupCompileStr.split(";");// 得到分组关系
										for (String groupCompile : groupCompileStrSplit) {// 遍历所有分组关系
											// String compileGroupStr = redisTemplate.opsForValue()
											// .get(groupCompile.toUpperCase());// 获取当前分组关系对应的编译信息

											String compileGroupStr = BaseRedisDao.getValByKey(idRelaRedisDBIndex,
													groupCompile.toUpperCase());// 获取当前分组关系对应的编译信息

											if (compileGroupStr != null && !compileGroupStr.equals("")) {
												String[] compileGroupStrSplit = compileGroupStr.split(";");
												if (compileGroupStrSplit != null && compileGroupStrSplit.length == 1
														&& compileGroupStr.equals(compileStr.toUpperCase())) {// 当前的分组关系只属于当前的compileid,说明没有被分组复用,需要将编译配置,分组关系,域配置,置无效
													if (compileGroupStrSplit[0].toUpperCase()
															.equals(compileStr.toUpperCase())) {
														String groupRegion = groupCompile.replaceAll("GROUPCOMPILE",
																"GROUPREGION");
														if (BaseRedisDao.keyIsExist(idRelaRedisDBIndex, groupRegion)) {
															redisTemplate.delete(groupRegion);// 删除组对应的域
														} else {
															throw new RuntimeException("后台错误:从" + idRelaRedisDBIndex
																	+ "号redis库中判断" + groupRegion
																	+ "组和域关系时不存在,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");

														}

														if (BaseRedisDao.keyIsExist(idRelaRedisDBIndex,
																groupCompile.toUpperCase())) {
															redisTemplate.delete(groupCompile.toUpperCase());// 删除当前组所对应的编译
														} else {
															throw new RuntimeException("后台错误:从" + idRelaRedisDBIndex
																	+ "号redis库中判断" + groupCompile.toUpperCase()
																	+ "组和域关系时不存在,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");

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
													redisTemplate.opsForValue().set(groupCompile,
															sb.substring(0, sb.length() - 1));
												}
											} else {
												throw new RuntimeException("后台错误:从" + idRelaRedisDBIndex + "号redis库中获取"
														+ groupCompile.toUpperCase()
														+ "的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");

											}
										}
										if (BaseRedisDao.keyIsExist(idRelaRedisDBIndex, compileStr.toUpperCase())) {
											redisTemplate.delete(compileStr.toUpperCase());// 删除编译下面所有的组
										}

									} else {
										throw new RuntimeException("后台错误:从" + idRelaRedisDBIndex + "号redis库中获取"
												+ compileStr + "的值为null,redis中不存在该值,请联系开发人员检查删除逻辑是否正确或redis数据是否出现了异常");

									}
								}
							}
						}
					}
				} else {
					throw new RuntimeException("后台错误:redis数据库编号:" + redisDBIndex + "不正确,请检查数据库编号");
				}
			}
		} else {
			throw new RuntimeException("后台错误:参数不能为空");
		}
	}
}
