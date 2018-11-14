package com.nis.web.service.restful;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nis.domain.MaatXmlConfig;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.JedisUtils;

import redis.clients.jedis.Pipeline;

public class DelRegionThreadByPipeline extends CommRegionThreadMethod implements Callable<Integer> {
	private static Logger logger = LoggerFactory.getLogger(DelRegionThreadByPipeline.class);

	long groupId;
	List<String> regionList;
	Pipeline pipelined;
	MaatXmlConfig maatXmlConfig;
	int tmpStorageReuseRegionDB;
	int idRelaRedisDBIndex;

	public DelRegionThreadByPipeline(long groupId, List<String> regionList, Pipeline pipelined, MaatXmlConfig maatXmlConfig,
			int tmpStorageReuseRegionDB, int idRelaRedisDBIndex) {
		super();
		this.groupId = groupId;
		this.regionList = regionList;
		this.pipelined = pipelined;
		this.maatXmlConfig = maatXmlConfig;
		this.tmpStorageReuseRegionDB = tmpStorageReuseRegionDB;
		this.idRelaRedisDBIndex = idRelaRedisDBIndex;
	}

	private void removeReuseReionByPipelined(long groupId, List<String> regionList, Pipeline pipelined,
			MaatXmlConfig maatXmlConfig, int tmpStorageReuseRegionDB, int idRelaRedisDBIndex) {
		if (regionList != null && regionList.size() > 0) {
			pipelined.select(tmpStorageReuseRegionDB);
			for (String tableAndId : regionList) {
				String regionKey = "EFFECTIVE_RULE:" + tableAndId;
				if (JedisUtils.exists(regionKey, tmpStorageReuseRegionDB)) {
					pipelined.del(regionKey);
					String groupStr = "GROUPREGION:" + groupId;
					String groupCompileVal = JedisUtils.get(groupStr, idRelaRedisDBIndex);
					StringBuffer newGroupRegion = new StringBuffer();

					if (groupCompileVal != null && !groupCompileVal.trim().equals("")) {
						Set<Integer> redisDBSet = new HashSet<Integer>();
						String[] split = groupCompileVal.split(";");
						if (split != null && split.length > 0) {
							for (String compileStr : split) {
								if (compileStr.split("-")[0].equals(regionKey)) {
									String[] dbArr = compileStr.split("-")[1].split(",");
									for (String db : dbArr) {
										if (db != null && !db.trim().equals("")) {
											redisDBSet.add(Integer.parseInt(db.trim()));
										}
									}
									if (split.length == 1) {
										pipelined.select(idRelaRedisDBIndex);
										pipelined.del(groupStr);
									}
								} else {
									newGroupRegion.append(compileStr + ";");
								}
							}

							if (newGroupRegion.length() > 0) {
								pipelined.select(idRelaRedisDBIndex);
								pipelined.set(groupStr, newGroupRegion.substring(0, newGroupRegion.length() - 1));
							}

						}
						for (Integer redisDb : redisDBSet) {
							if (JedisUtils.exists(regionKey, redisDb)) {
								pipelined.select(redisDb);

								String isValidKey = regionKey.replace("EFFECTIVE_RULE", "OBSOLETE_RULE");
								pipelined.rename(regionKey, isValidKey);

								String maatVersionStr = JedisUtils.get("MAAT_VERSION", redisDb);
								if (maatVersionStr == null) {
									maatVersionStr = "0";
								}
								if (maatVersionStr != null) {
									Double maatVersion = Double.valueOf(maatVersionStr) + 1D;
									updateMaatInfoByPipelined(maatXmlConfig.getExpressionList(), isValidKey, pipelined,
											maatVersion, redisDb, true);
									pipelined.incrBy("MAAT_VERSION", 1l);
									logger.info("向{}号redis数据库更新了MAAT_VERSION,更新后版本是{}", redisDb,
											Integer.valueOf(maatVersionStr) + 1);
								}

							} else {
								throw new ServiceRuntimeException(
										"从" + tmpStorageReuseRegionDB + "号redis库中删除分组复用域配置时,regionKey=" + regionKey
												+ "不存在,请检查配置参数是否正确,或redis数据是否正确",
										RestBusinessCode.KeyNotExistsInRedis.getValue());
							}
						}
					}

				} else {
					throw new ServiceRuntimeException("临时存放region的" + tmpStorageReuseRegionDB + "号redis库中regionKey="
							+ regionKey + "不存在,请检查配置参数是否正确", RestBusinessCode.KeyNotExistsInRedis.getValue());
				}
			}
		} else {
			throw new ServiceRuntimeException("删除分组复用域配置时,参数都为空,请检查配置参数是否正确",
					RestBusinessCode.ConfigSourceIsNull.getValue());
		}

	}

	@Override
	public Integer call() throws Exception {
		removeReuseReionByPipelined(groupId, regionList, pipelined, maatXmlConfig, tmpStorageReuseRegionDB,
				idRelaRedisDBIndex);
		return 1;
	}

}
