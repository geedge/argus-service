package com.nis.web.service.restful;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.JedisUtils;

import redis.clients.jedis.Pipeline;

public class SaveRegionThreadByPipeline  extends CommRegionThreadMethod implements Callable<Integer>{
	private static Logger logger = LoggerFactory.getLogger(SaveRegionThreadByPipeline.class);

	List<Map<String, String>> regionMapList;
	MaatXmlConfig maatXmlConfig;
	Pipeline pipelined;
	int type;
	int tmpStorageReuseRegionDB;
	int idRelaRedisDBIndex;

	public SaveRegionThreadByPipeline(List<Map<String, String>> regionMapList, MaatXmlConfig maatXmlConfig,
			Pipeline pipelined, int type, int tmpStorageReuseRegionDB, int idRelaRedisDBIndex) {
		super();
		this.regionMapList = regionMapList;
		this.maatXmlConfig = maatXmlConfig;
		this.pipelined = pipelined;
		this.type = type;
		this.tmpStorageReuseRegionDB = tmpStorageReuseRegionDB;
		this.idRelaRedisDBIndex = idRelaRedisDBIndex;
	}

	private void addTmpReionByPipeLine(List<Map<String, String>> regionMapList, MaatXmlConfig maatXmlConfig,
			Pipeline pipelined, int type, int tmpStorageReuseRegionDB, int idRelaRedisDBIndex) {
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
								String keyVal = map.get(keyStr);
								if (keyVal != null && !keyVal.equals("")) {
									keyBF.append(keyVal);
								} else {
									throw new ServiceRuntimeException("未从map中获取到" + keyStr + "的值,请检查数据或配置文件是否正确",
											RestBusinessCode.NotFoundValueByKey.getValue());
								}
							} else if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("{")) {
								keyStr = keyStr.trim().replace("{", "").replace("}", "");
								if (keyStr.toLowerCase().contains("table_name")) {
									String argTableName = map.get("table_name");
									if (argTableName == null) {
										throw new ServiceRuntimeException(
												"添加分组复用域配置时,必须要传入表名,请检查参数,配置类型:" + type + ",对应的真实表名",
												RestBusinessCode.NotFoundTableName.getValue());
									} else {
										keyBF.append(argTableName);
									}

								}

							} else {
								keyBF.append(keyStr.trim());
							}
						}
						String groupId = null;
						StringBuffer valBF = new StringBuffer();
						String[] valSplit = maatXmlExpr.getValueExpression().split(";");
						for (String valStr : valSplit) {
							if (!StringUtils.isEmpty(valStr) && valStr.trim().startsWith("[")) {
								valStr = valStr.trim().replace("[", "").replace("]", "");
								// if (service != null && service.intValue() == 1028
								// && valStr.toLowerCase().equals("op_time") && type == 12) {
								if (valStr.toLowerCase().equals("op_time") && type == 12) {
									String user_region = map.get("user_region");
									valBF.append(user_region + "\t");
								}
								String val = map.get(valStr);
								if (val != null) {
									valBF.append(val);
									if (valStr.equals("group_id")) {
										groupId = val;
									}
								} else {
									// 所有在maat.xml中配置的属性都不可以为空
									throw new ServiceRuntimeException(
											"未从map中获取到" + valStr + "的值,无法拼接redisValue,请检查数据或配置文件是否正确",
											RestBusinessCode.NotFoundValueByKey.getValue());
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
						pipelined.select(tmpStorageReuseRegionDB);
						maatKey = keyBF.toString();
						pipelined.set(maatKey.toUpperCase(), valBF.toString());
						logger.info("向{}号redis数据库添加了一条配置,key是{},value是{}", tmpStorageReuseRegionDB,
								maatKey.toUpperCase(), valBF.toString());

						String groupIdStr = "GROUPCOMPILE:" + groupId;
						String groupCompileVal = JedisUtils.get(groupIdStr, idRelaRedisDBIndex);
						if (groupCompileVal != null && !groupCompileVal.trim().equals("")) {
							Set<Integer> redisDBSet = new HashSet<Integer>();
							String[] split = groupCompileVal.split(";");
							if (split != null && split.length > 0) {
								for (String compileStr : split) {
									String[] dbArr = compileStr.split("-")[1].split(",");
									for (String db : dbArr) {
										if (db != null && !db.trim().equals("")) {
											redisDBSet.add(Integer.parseInt(db.trim()));
										}
									}
								}
							}
							String groupRegionStr = "GROUPREGION:" + groupId;
							String regionVal = JedisUtils.get(groupIdStr, idRelaRedisDBIndex);
							if (regionVal != null && !regionVal.trim().equals("")) {
								pipelined.append(groupRegionStr, ";" + maatKey.toUpperCase() + "-"
										+ redisDBSet.toString().replace("[", "").replace("]", ""));
							} else {
								pipelined.set(groupRegionStr, maatKey.toUpperCase() + "-"
										+ redisDBSet.toString().replace("[", "").replace("]", ""));
							}
							for (Integer redisDb : redisDBSet) {
								pipelined.select(redisDb);
								pipelined.set(maatKey.toUpperCase(), valBF.toString());
								logger.info("向{}号redis数据库添加了一条配置,key是{},value是{}", redisDb, maatKey.toUpperCase(),
										valBF.toString());
								String maatVersionStr = JedisUtils.get("MAAT_VERSION", redisDb);
								if (maatVersionStr == null) {
									maatVersionStr = "0";
								}
								if (maatVersionStr != null) {
									Double maatVersion = Double.valueOf(maatVersionStr) + 1D;
									updateMaatInfoByPipelined(expressionList, maatKey, pipelined, maatVersion, redisDb,
											false);
									pipelined.incrBy("MAAT_VERSION", 1l);
									logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDb,
											Integer.valueOf(maatVersionStr) + 1);
								}
							}
						}
					}
				}
			}
		}
	}



//	@Override
//	public void run() {
//		addTmpReionByPipeLine(regionMapList, maatXmlConfig, pipelined, type, tmpStorageReuseRegionDB,
//				idRelaRedisDBIndex);
//
//	}

	@Override
	public Integer call() throws Exception {
		addTmpReionByPipeLine(regionMapList, maatXmlConfig, pipelined, type, tmpStorageReuseRegionDB,
				idRelaRedisDBIndex);
		return 1;
	}

}
