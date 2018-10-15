package com.nis.web.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nis.util.Configurations;
import com.nis.util.ExceptionUtil;
import com.nis.util.JedisUtils;
import com.nis.web.service.restful.ConfigJedisServiceimpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;
import redis.clients.util.JedisClusterCRC16;

@Component
@PropertySource(value = { "classpath:nis.properties", "classpath:jdbc.properties" })
public class SyncRedisToCluster {
	private static Logger logger = LoggerFactory.getLogger(ConfigJedisServiceimpl.class);
	private static int redisStatisticsRealDBIndex = Configurations.getIntProperty("redisStatisticsRealDBIndex", 14);

	@Autowired
	private JedisCluster jedisCluster;
	// @Autowired
	// private JedisSentinelPool jedisSentinelPool;

	// @Scheduled(cron = "0/3 * * * * ?")
	// @Scheduled(cron = "${syncRedisToClusterCron}")
	public void syncRedisToCluster() {
		try {
			// keys("EFFECTIVE_RULE*");
			// keys("OBSOLETE_RULE*");
			String clusterMaatVersionStr = jedisCluster.get("MAAT_VERSION");
			String redisMaatVersionStr = JedisUtils.get("MAAT_VERSION", redisStatisticsRealDBIndex);
			if (clusterMaatVersionStr != null && !clusterMaatVersionStr.trim().equals("")) {
				if (redisMaatVersionStr != null && !redisMaatVersionStr.trim().equals("")) {
					Integer clusterMaatVersion = Integer.valueOf(clusterMaatVersionStr);
					Integer redisMaatVersion = Integer.valueOf(redisMaatVersionStr);
					if (redisMaatVersion < clusterMaatVersion) {// 如果主从库比集群库的版本号小则下发全量
						logger.info("redis集群中的MAAT_VERSION为大于配置库中的MAAT_VERSION,开始执行全量同步");
						syncAllData(redisMaatVersionStr);
					} else if (redisMaatVersion > clusterMaatVersion) {// 获取增量的数据
						logger.info("redis集群中的MAAT_VERSION为小于配置库中的MAAT_VERSION,开始执行增量同步,score是{}-{}",
								clusterMaatVersion, +redisMaatVersion);
						syncData(clusterMaatVersion.doubleValue(), redisMaatVersion.doubleValue(), redisMaatVersionStr);
					} else {
						logger.info("redis集群中的MAAT_VERSION与配置库中的MAAT_VERSION相等,暂不执行配置同步操作");
					}
				} else {
					logger.info("redis配置库中MAAT_VERSION为null,但是redis集群中的MAAT_VERSION为{},集群与配置库的数据不同步,开始删除集群中的配置",
							clusterMaatVersionStr);
					delClusterData();
				}
			} else {
				if (redisMaatVersionStr != null && !redisMaatVersionStr.trim().equals("")) {
					logger.info("redis配置库中的MAAT_VERSION为{},redis集群中的MAAT_VERSION为null,开始执行全量同步", redisMaatVersionStr);
					syncAllData(redisMaatVersionStr);
				} else {
					logger.info("redis配置库中和redis集群中的MAAT_VERSION都为null,暂时不执行全量同步");
				}
			}
		} catch (Exception e) {
			logger.error("同步配置库配置到3A-redisCluster失败,失败原因:{}", ExceptionUtil.getExceptionMsg(e));
		}
	}

	public void deleteRedisKeyStartWith(String redisKeyStartWith) {
		try {
			Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
			for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {

				Jedis jedis = entry.getValue().getResource();
				// 判断非从节点(因为若主从复制，从节点会跟随主节点的变化而变化)
				if (!jedis.info("replication").contains("role:slave")) {
					Set<String> keys = jedis.keys(redisKeyStartWith + "*");
					if (keys.size() > 0) {
						Map<Integer, List<String>> map = new HashMap<>();
						for (String key : keys) {
							// cluster模式执行多key操作的时候，这些key必须在同一个slot上，不然会报:JedisDataException:
							// CROSSSLOT Keys in request don't hash to the same slot
							int slot = JedisClusterCRC16.getSlot(key);
							// 按slot将key分组，相同slot的key一起提交
							if (map.containsKey(slot)) {
								map.get(slot).add(key);
							} else {
								ArrayList<String> list = new ArrayList<String>();
								list.add(key);
								map.put(slot, list);
							}
						}
						for (Map.Entry<Integer, List<String>> integerListEntry : map.entrySet()) {
							jedis.del(integerListEntry.getValue()
									.toArray(new String[integerListEntry.getValue().size()]));
							// jedisCluster.del(integerListEntry.getValue()
							// .toArray(new String[integerListEntry.getValue().size()]));
							logger.debug("从redis集群{}删除key={},成功", entry.getKey(), integerListEntry.getValue());
						}
					}
				}
			}
		} finally {
		}
	}

	public TreeSet<String> keys(String pattern) {
		TreeSet<String> keys = new TreeSet<>();
		// 获取所有的节点
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		// 遍历节点 获取所有符合条件的KEY
		for (String k : clusterNodes.keySet()) {
			logger.info("Getting keys from: {}", k);
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				keys.addAll(connection.keys(pattern));
			} catch (Exception e) {
				logger.error("从{}获取{}失败,失败原因{}", k, pattern, e);
			} finally {
				connection.close();// 用完一定要close这个链接！！！
			}
		}
		// for (String string : keys) {
		// System.out.println(string);
		// }
		logger.debug("Keys gotten!");
		return keys;
	}

	public void delClusterData() {
		// 清空集群库配置数据
		if (jedisCluster.exists("MAAT_UPDATE_STATUS")) {
			jedisCluster.del("MAAT_UPDATE_STATUS");
			logger.info("删除MAAT_UPDATE_STATUS成功");
		}
		if (jedisCluster.exists("MAAT_VERSION")) {
			jedisCluster.del("MAAT_VERSION");
			logger.info("删除MAAT_VERSION成功");
		}
		deleteRedisKeyStartWith("EFFECTIVE_RULE*");
		logger.info("删除EFFECTIVE_RULE*成功");
		deleteRedisKeyStartWith("OBSOLETE_RULE*");
		logger.info("删除OBSOLETE_RULE*成功");
	}

	public void syncAllData(String version) {
		delClusterData();
		syncData(null, null, version);
	}

	public void syncData(Double min, Double max, String verion) {
		Jedis resource = JedisUtils.getResource(redisStatisticsRealDBIndex);
		Set<Tuple> zrangeByScoreWithScores = null;
		if (min == null && max == null) {
			zrangeByScoreWithScores = resource.zrangeByScoreWithScores("MAAT_UPDATE_STATUS", "-inf", "+inf");// 获取所有的maat_update_status
		} else {
			zrangeByScoreWithScores = resource.zrangeByScoreWithScores("MAAT_UPDATE_STATUS", min, max);// 获取增量的数据
		}
		for (Tuple tuple : zrangeByScoreWithScores) {
			String key = tuple.getElement();
			String zset = key;
			double score = tuple.getScore();
			if (key.contains("ADD")) {
				key = key.replace("ADD,", "EFFECTIVE_RULE:");
			} else if (key.contains("DEL")) {
				key = key.replace("DEL,", "OBSOLETE_RULE:");
			}
			String val = JedisUtils.get(key, redisStatisticsRealDBIndex);
			if (val != null && !val.trim().equals("")) {
				jedisCluster.set(key, val);
				logger.debug("向redis集群中插入了一条数据key是{},value是{}", key.toUpperCase(), val);
			}
			jedisCluster.zadd("MAAT_UPDATE_STATUS", score, zset);
			logger.debug("向redis集群中更新了MAAT_UPDATE_STATUS,内容是{},SCORES是{}", zset.toUpperCase(), score);
			// jedisCluster.incr("MAAT_VERSION");
		}
		jedisCluster.set("MAAT_VERSION", verion);
		logger.info("更新了redis集群中的MAAT_VERSION,更新后值是{}", verion);
		logger.info("向redis集群同步数据成功");
	}

}
