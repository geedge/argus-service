package com.nis.web.task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nis.domain.restful.ConfigSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Constants;
import com.nis.util.ExceptionUtil;
import com.nis.util.JedisUtils;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.SpringContextHolder;
import com.nis.web.service.restful.ConfigSourcesService;
import com.zdjizhi.utils.JsonMapper;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisException;

@Component
@PropertySource(value = { "classpath:nis.properties" })
public class SyncAllConfigTask {
	private static Logger logger = LoggerFactory.getLogger(SyncAllConfigTask.class);
//	@Autowired
//	private JedisCluster jedisCluster;
	@Autowired
	protected ConfigSourcesService configSourcesService;

	/**
	 * 每次使用获取新连接,避免集群某些机器宕机后影响连接使用,注意需要在applicationContext-redis.xml中修改jedisCluster的scope为 prototype
	 * 
	 * @return
	 * @throws JedisException
	 */
	public static JedisCluster getResource() throws JedisException {
		JedisCluster jedisCluster = SpringContextHolder.getBean(JedisCluster.class);
		if (jedisCluster == null) {
			throw new ServiceRuntimeException("无法获取redis-cluster连接,请联系管理员检查程序",
					RestBusinessCode.CannotConnectionRedis.getValue());
		}
		return jedisCluster;

	}

	@Scheduled(cron = "${syncUiAndServiceConfigCron}")
	public void syncRedisToCluster() {
		String requestId = UUID.randomUUID().toString();
		JedisCluster jedisCluster = getResource();
		Map<Integer, Map<String, String>> map = null;
		try {
			if (lock(jedisCluster, requestId)) {// 避免集群环境下同一秒钟所有的机器都执行这个定时任务
//				if (true) {// 避免集群环境下同一秒钟所有的机器都执行这个定时任务
				String allConfigSyncStatus = jedisCluster.get("allConfigSyncStatus");
				if (allConfigSyncStatus != null) {// 配置初始化完成
					if (allConfigSyncStatus.trim().equals("1")) {
						map = getAllSeqAndVersion();
						// 设置配置同步状态为正在进行
						configSourcesService.setAllConfigSyncStatus("2");
						logger.warn("开始执行配置全量导入操作,将allConfigSyncStatus值设置为2正在进行导入操作");
						Map<String, String> maatMap = new HashMap<>();
						Map<String, String> unMaatMap = new HashMap<>();
						String allConfigSyncKey = jedisCluster.get("allConfigSyncKey");
						if (allConfigSyncKey != null && !allConfigSyncKey.trim().equals("")) {
							String[] split = org.apache.commons.lang.StringUtils.split(allConfigSyncKey, ";");
							for (String key : split) {
								String val = jedisCluster.get(key);
								String md5 = DigestUtils.md5Hex(val);
								if (key.startsWith("UNMAAT")) {
									unMaatMap.put(md5, val);
								} else if (key.startsWith("MAAT")) {
									maatMap.put(md5, val);
								}
							}
							flushRedisDb();
							addConfigToRedis(maatMap, true);
							addConfigToRedis(unMaatMap, false);
							logger.warn("执行配置全量导入成功,将allConfigSyncStatus值设置为3,导入成功");
							// 设置配置同步状态为写redis成功
							configSourcesService.setAllConfigSyncStatus("3");

							// 删除存储全量配置key的关系key
							jedisCluster.del("allConfigSyncKey");
							for (String key : split) {
								jedisCluster.del(key);
							}
							logger.warn("删除allConfigSyncKey,及其中的内容成功");
						}
					} else {
						logger.info(
								"集群中allConfigSyncStatus的值是{}[开始:0(界面下发同步状态),初始化:1(配置接收完成状态),进行中:2(服务写redis),已完成:3(服务写redis完毕),失败:-1(服务写redis失败)],暂不执行全量配置同步操作",
								allConfigSyncStatus);
					}
				} else {
					logger.info("未从集群中获取到allConfigSyncStatus的值,暂不执行全量配置同步操作");
				}

			} else {
				logger.info("没有从rediscluster中获取到allConfigSyncDistributedLock分布式锁,暂时不执行数据同步!");
			}
		} catch (Exception e) {
			logger.error("同步界面配置到redis中失败,失败原因:{}", ExceptionUtil.getExceptionMsg(e));
			// 设置配置同步状态为写redis失败
			configSourcesService.setAllConfigSyncStatus("-1");
			logger.error("执行配置全量导入失败,将allConfigSyncStatus值设置为-1,导入失败");
		} finally {
			unlock(jedisCluster, requestId);
			closeConn(jedisCluster);
			if (map != null && map.size() > 0) {
				recoverRedisData(map);
			}
		}
	}

	/**
	 * 保存数据入库,有验证逻辑
	 * 
	 * @param map
	 * @param isMaat
	 * @throws Exception
	 */
	public void addConfigToRedisYZ(Map<String, String> map, boolean isMaat) throws Exception {
		long time = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		if (isMaat) {
			for (Entry<String, String> entry : map.entrySet()) {
				ConfigSource configSource = new JsonMapper().fromJson(entry.getValue(), ConfigSource.class);
				configSourcesService.saveMaatConfig(new AuditLogThread(), time, configSource.getConfigCompileList(),
						sb);
			}
		} else {
			for (Entry<String, String> entry : map.entrySet()) {
				String value = entry.getValue();
				configSourcesService.saveCommonSources(new AuditLogThread(), time, value);
			}

		}

	}

	/**
	 * 保存数据入库,无验证逻辑
	 * 
	 * @param map
	 * @param isMaat
	 * @throws Exception
	 */
	private void addConfigToRedis(Map<String, String> map, boolean isMaat) throws Exception {
		if (isMaat) {
			for (Entry<String, String> entry : map.entrySet()) {
				ConfigSource configSource = new JsonMapper().fromJson(entry.getValue(), ConfigSource.class);
				configSourcesService.saveMaatConfig(configSource.getConfigCompileList());
			}
		} else {
			for (Entry<String, String> entry : map.entrySet()) {
				String value = entry.getValue();
				configSourcesService.saveCommonSources(value);
			}

		}

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
	private Boolean lock(JedisCluster jedisCluster, String requestId) {
		String key = "allConfigSyncDistributedLock";
		String var1 = jedisCluster.set(key, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME,
				Constants.CONFIGSYNCLOCKTIME);
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
	private boolean unlock(JedisCluster jedisCluster, String requestId) {
		String key = "allConfigSyncDistributedLock";
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
	 * 从配置redis库中获取没个redisdb的maat_version,0,14,15号库不会有maat_version所以就不获取了
	 * 
	 * @return
	 */
	private Map<Integer, Map<String, String>> getAllSeqAndVersion() {
		// 第一个key是redisdb,第二个key是redis的
		Map<Integer, Map<String, String>> map = new HashMap<>();
		for (int i = 1; i < 14; i++) {
			String maatVersionStr = JedisUtils.get("MAAT_VERSION", i);
			if (!map.containsKey(i)) {
				Map<String, String> keyValMap = new HashMap<>();
				if (maatVersionStr != null) {
					keyValMap.put("MAAT_VERSION", maatVersionStr);
					map.put(i, keyValMap);
				}

			}
		}
//		String seqCompileid = JedisUtils.get("SEQ_COMPILEID", 0);
//		String seqGroupid = JedisUtils.get("SEQ_GROUPID", 0);
//		String seqRegionid = JedisUtils.get("SEQ_REGIONID", 0);
//		Map<String, String> keyValMap = new HashMap<>();
//		keyValMap.put("SEQ_COMPILEID", seqCompileid);
//		keyValMap.put("SEQ_GROUPID", seqGroupid);
//		keyValMap.put("SEQ_REGIONID", seqRegionid);
//		map.put(0, keyValMap);
		return map;
	}

	/**
	 * 清空配置redis库,不清空0号库
	 */
	private void flushRedisDb() {// 不清空0号库
		for (int i = 1; i < 16; i++) {
			JedisUtils.getResource(i).flushDB();
		}
	}

	/**
	 * 恢复配置redis库中各个索引的maat_version
	 * 
	 * @param map
	 */
	private void recoverRedisData(Map<Integer, Map<String, String>> map) {
		for (Integer redisDB : map.keySet()) {
			Map<String, String> keyValMap = map.get(redisDB);
			for (String redisKey : keyValMap.keySet()) {
				JedisUtils.set(redisKey, String.valueOf(Long.parseLong(keyValMap.get(redisKey)) + 2l), 0, redisDB);
			}
		}
	}

}
