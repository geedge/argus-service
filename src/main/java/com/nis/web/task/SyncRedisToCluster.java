package com.nis.web.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.ExceptionUtil;
import com.nis.util.JedisUtils;
import com.nis.web.service.SpringContextHolder;
import com.nis.web.service.restful.ConfigJedisServiceimpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.JedisClusterCRC16;

@Component
@PropertySource(value = { "classpath:nis.properties", "classpath:jdbc.properties" })
public class SyncRedisToCluster {
	private static Logger logger = LoggerFactory.getLogger(ConfigJedisServiceimpl.class);
	private static int redisStatisticsRealDBIndex = Configurations.getIntProperty("redisStatisticsRealDBIndex", 14);

	/**
	 * 每次使用获取新连接,避免集群某些机器宕机后影响连接使用,注意需要在applicationContext-redis.xml中修改jedisCluster的scope为
	 * prototype
	 * 
	 * @return
	 * @throws JedisException
	 */
	public JedisCluster getResource() throws JedisException {
		JedisCluster jedisCluster = SpringContextHolder.getBean(JedisCluster.class);
		if (jedisCluster == null) {
			throw new ServiceRuntimeException("无法获取redis-cluster连接,请联系管理员检查程序",
					RestBusinessCode.CannotConnectionRedis.getValue());
		}
		return jedisCluster;

	}

	/**
	 * 关闭集群连接
	 * 
	 * @param jedisCluster
	 */
	private void closeConn(JedisCluster jedisCluster) {
		if (jedisCluster != null) {
			try {
				jedisCluster.close();
			} catch (Exception e) {
				throw new ServiceRuntimeException("释放redis-cluster连接失败",
						RestBusinessCode.CannotConnectionRedis.getValue());
			}
		}
	}

//	@Scheduled(cron = "${syncRedisToClusterCron}")
	public void syncRedisToCluster() {
		JedisCluster jedisCluster = getResource();
		String requestId = UUID.randomUUID().toString();
		try {
			if (lock(jedisCluster, requestId)) {// 避免集群环境下同一秒钟所有的机器都执行这个定时任务
//				 keys("EFFECTIVE_RULE*");
//				 keys("OBSOLETE_RULE*");
				String clusterMaatVersionStr = jedisCluster.get("MAAT_VERSION");
				String redisMaatVersionStr = JedisUtils.get("MAAT_VERSION", redisStatisticsRealDBIndex);
				if (clusterMaatVersionStr != null && !clusterMaatVersionStr.trim().equals("")) {
					if (redisMaatVersionStr != null && !redisMaatVersionStr.trim().equals("")) {
						Integer clusterMaatVersion = Integer.valueOf(clusterMaatVersionStr);
						Integer redisMaatVersion = Integer.valueOf(redisMaatVersionStr);
						if (redisMaatVersion < clusterMaatVersion) {// 如果主从库比集群库的版本号小则下发全量
							logger.info("redis集群中的MAAT_VERSION为大于配置库中的MAAT_VERSION,开始执行全量同步");
							syncAllData(jedisCluster, redisMaatVersionStr);
						} else if (redisMaatVersion > clusterMaatVersion) {// 获取增量的数据
							logger.info("redis集群中的MAAT_VERSION为小于配置库中的MAAT_VERSION,开始执行增量同步,score是{}-{}",
									clusterMaatVersion, +redisMaatVersion);
							syncData(jedisCluster, clusterMaatVersion.doubleValue(), redisMaatVersion.doubleValue(),
									redisMaatVersionStr);
						} else {
							logger.info("redis集群中的MAAT_VERSION与配置库中的MAAT_VERSION相等,暂不执行配置同步操作");
						}
					} else {
						logger.info("redis配置库中MAAT_VERSION为null,但是redis集群中的MAAT_VERSION为{},集群与配置库的数据不同步,开始删除集群中的配置",
								clusterMaatVersionStr);
						delClusterData(jedisCluster);
					}
				} else {
					if (redisMaatVersionStr != null && !redisMaatVersionStr.trim().equals("")) {
						logger.info("redis配置库中的MAAT_VERSION为{},redis集群中的MAAT_VERSION为null,开始执行全量同步",
								redisMaatVersionStr);
						syncAllData(jedisCluster, redisMaatVersionStr);
					} else {
						logger.info("redis配置库中和redis集群中的MAAT_VERSION都为null,暂时不执行全量同步");
					}
				}
			} else {
				logger.info("没有从rediscluster中获取到configSyncDistributedLock分布式锁,暂时不执行数据同步!");
			}
		} catch (Exception e) {
			logger.error("同步配置库配置到3A-redisCluster失败,失败原因:{}", ExceptionUtil.getExceptionMsg(e));
		} finally {
			unlock(jedisCluster, requestId);
			closeConn(jedisCluster);
		}
	}

	// 删除之前的key
	public void deleteRedisKeyStartWith(JedisCluster jedisCluster, String redisKeyStartWith) {
		try {
			Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
			for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {

				Jedis jedis = entry.getValue().getResource();
				// 判断非从节点(因为若主从复制，从节点会跟随主节点的变化而变化)
				if (!jedis.info("replication").contains("role:slave")) {
					List<String> keys = getKeyByScan(redisKeyStartWith + "*", jedis);
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

	public TreeSet<String> keys(JedisCluster jedisCluster, String pattern) {
		TreeSet<String> keys = new TreeSet<>();
		// 获取所有的节点
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		// 遍历节点 获取所有符合条件的KEY
		for (String k : clusterNodes.keySet()) {
			logger.info("Getting keys from: {}", k);
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				keys.addAll(getKeyByScan(pattern, connection));
			} catch (Exception e) {
				logger.error("从{}获取{}失败,失败原因{}", k, pattern, e);
			} finally {
				connection.close();// 用完一定要close这个链接！！！
			}
		}
		for (String string : keys) {
//			System.out.println(string);
		}
		logger.debug("Keys gotten!");
		return keys;
	}

	public void delClusterData(JedisCluster jedisCluster) {
		// 清空集群库配置数据
		if (jedisCluster.exists("MAAT_UPDATE_STATUS")) {
			jedisCluster.del("MAAT_UPDATE_STATUS");
			logger.info("删除MAAT_UPDATE_STATUS成功");
		}
		if (jedisCluster.exists("MAAT_VERSION")) {
			jedisCluster.del("MAAT_VERSION");
			logger.info("删除MAAT_VERSION成功");
		}
		deleteRedisKeyStartWith(jedisCluster, "EFFECTIVE_RULE*");
		logger.info("删除EFFECTIVE_RULE*成功");
		deleteRedisKeyStartWith(jedisCluster, "OBSOLETE_RULE*");
		logger.info("删除OBSOLETE_RULE*成功");
	}

	public void syncAllData(JedisCluster jedisCluster, String version) {
		delClusterData(jedisCluster);
		syncData(jedisCluster, null, null, version);
	}

	public void syncData(JedisCluster jedisCluster, Double min, Double max, String verionStr) {
		Jedis resource = JedisUtils.getResource(redisStatisticsRealDBIndex);
		Set<Tuple> zrangeByScoreWithScores = null;
		if (min == null && max == null) {
			int version = Integer.parseInt(verionStr);
			int maxVersion = 1000;
			int count = version / maxVersion;
			if ((version % maxVersion) != 0) {
				count++;
			}
			min = 0D;
			for (int i = 0; i < count; i++) {
				int start = i * maxVersion + 1;
				int end = (i + 1) * maxVersion;
				if (end > version) {
					end = version;
				}
				min = Double.parseDouble(start + "");
				max = Double.parseDouble(end + "");
				// 分批获取,避免一次性获取太多,造成redis阻塞
				zrangeByScoreWithScores = resource.zrangeByScoreWithScores("MAAT_UPDATE_STATUS", min, max);// 获取所有的maat_update_status
				syncData(jedisCluster, zrangeByScoreWithScores);
			}
		} else {
			zrangeByScoreWithScores = resource.zrangeByScoreWithScores("MAAT_UPDATE_STATUS", min, max);// 获取增量的数据
			syncData(jedisCluster, zrangeByScoreWithScores);
		}
		jedisCluster.set("MAAT_VERSION", verionStr);
		logger.info("更新了redis集群中的MAAT_VERSION,更新后值是{}", verionStr);
		logger.info("向redis集群同步数据成功");
	}

	private void syncData(JedisCluster jedisCluster, Set<Tuple> zrangeByScoreWithScores) {
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
	}

	// 设置成功返回的结果OK
	private static final String LOCK_SUCCESS = "OK";
	// NX -- Only set the key if it does not already exist. XX -- Only set the key
	// if it already exist
	private static final String SET_IF_NOT_EXIST = "NX";
	// 失效单位秒(EX)还是毫秒(PX)
	private static final String SET_WITH_EXPIRE_TIME = "EX";
	private static final Long UNLOCK_SUCCESS = 1L;

	/**
	 * 尝试获取分布式锁,如果没有key就set,有key就不操作
	 * 
	 * @param requestId 请求标识(UUID.randomUUID().toString()),正常情况下是谁加的锁,谁去解锁不能a加的锁,b去解锁
	 * @return 是否获取成功
	 */
	public Boolean lock(JedisCluster jedisCluster, String requestId) {
		String key = "configSyncDistributedLock";
		String var1 = jedisCluster.set(key, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, Constants.REDISLOCKTIME);
		if (LOCK_SUCCESS.equals(var1)) {
			return true;
		}
		return false;
	}

	/**
	 * 解除redis分布式锁
	 * 
	 * @param requestId
	 */
	protected boolean unlock(JedisCluster jedisCluster, String requestId) {
		String key = "configSyncDistributedLock";
		// 这个字符串是个lua脚本，代表的意思是如果根据key拿到的value跟传入的value相同就执行del，否则就返回0【保证安全性】
		String luaScript = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else  return 0 end";
		// 这个命令就是去执行lua脚本，KEYS的集合就是第二个参数，ARGV的集合就是第三参数【保证解锁的原子操作】
		Object var2 = jedisCluster.eval(luaScript, Collections.singletonList(key),
				Collections.singletonList(requestId));
		if (UNLOCK_SUCCESS == var2) {
			return true;
		}
		return false;
	}

	/**
	 * 利用scan模糊匹配key
	 * 
	 * @param pattern
	 * @param resource
	 * @return
	 */
	public List<String> getKeyByScan(String pattern, Jedis resource) {
		List<String> list = new ArrayList<>();
		int count = 1000;
		String cursor = ScanParams.SCAN_POINTER_START;
		ScanParams scanParams = new ScanParams();
		scanParams.count(count);
		scanParams.match(pattern);
		do {
			ScanResult<String> scanResult = resource.scan(cursor, scanParams);
			list.addAll(scanResult.getResult());
			cursor = scanResult.getStringCursor();
		} while (!"0".equals(cursor));
		return list;
	}
}
