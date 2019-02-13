package com.nis.util;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;
import com.nis.web.service.SpringContextHolder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;

public class JedisUtils {
	private static Logger logger = LoggerFactory.getLogger(JedisUtils.class);

	/**
	 * 获取缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key, int redisDb) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource(redisDb);
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取" + key + "对应的值失败",
					RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static Object getObject(String key, int redisDb) {
		Object value = null;
		Jedis jedis = null;
		try {
			jedis = getResource(redisDb);
			if (jedis.exists(getBytesKey(key))) {
				value = toObject(jedis.get(getBytesKey(key)));
				logger.debug("getObject {} = {}", key, value);
			}
		} catch (Exception e) {
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取" + key + "对应的值失败",
					RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * <b>可以作为获取唯一id的方法</b><br/>
	 * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
	 * 
	 * @param String key
	 * @param        long number 要减去的值
	 * @return long 相加后的值
	 */
	public static long incrBy(String key, long number, int redisDb) {
		Jedis jedis = null;
		long len = 0l;
		try {
			jedis = getResource(redisDb);
			len = jedis.incrBy(key, number);
		} catch (Exception e) {
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取自增值" + key + "失败",
					RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return len;
	}

	/**
	 * 设置缓存
	 * 
	 * @param key          键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds, int redisDb) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource(redisDb);
			result = jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			throw new ServiceRuntimeException("向" + redisDb + "号redisDB中设置zset失败,key=" + key + ",value=" + value,
					RestBusinessCode.ZsetFailed.getValue());
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取List缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static List<String> getList(String key, int redisDb) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource(redisDb);
			if (jedis.exists(key)) {
				value = jedis.lrange(key, 0, -1);
				logger.debug("getList {} = {}", key, value);
			}
		} catch (Exception e) {
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取" + key + "对应的值失败",
					RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取List缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key, int redisDb) {
		List<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource(redisDb);
			if (jedis.exists(getBytesKey(key))) {
				List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectList {} = {}", key, value);
			}
		} catch (Exception e) {
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取" + key + "对应的值失败",
					RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 缓存是否存在
	 * 
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key, int redisDb) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource(redisDb);
			result = jedis.exists(key);
			logger.debug("exists {}", key);
		} catch (Exception e) {
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中判断" + key + "是否存在失败",
					RestBusinessCode.ExistsKeyFailed.getValue());
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 缓存是否存在
	 * 
	 * @param key 键
	 * @return
	 */
	public static boolean existsObject(String key, int redisDb) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource(redisDb);
			result = jedis.exists(getBytesKey(key));
			logger.debug("existsObject {}", key);
		} catch (Exception e) {
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中判断" + key + "是否存在失败",
					RestBusinessCode.ExistsKeyFailed.getValue());
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource(int redisDb) throws JedisException {
		JedisSentinelPool jedisSentinelPool = SpringContextHolder.getBean(JedisSentinelPool.class);

		if (jedisSentinelPool == null) {
			throw new ServiceRuntimeException("redis连接池为空,请联系管理员检查程序",
					RestBusinessCode.CannotConnectionRedis.getValue());
		}
		Jedis jedis = null;
		try {
			jedis = jedisSentinelPool.getResource();
			jedis.select(redisDb);
		} catch (JedisException e) {
			returnBrokenResource(jedis);
			logger.error("获取redis连接失败,异常信息:{}", ExceptionUtil.getExceptionMsg(e));
			throw new ServiceRuntimeException("获取redis连接失败,请联系管理员检查程序",
					RestBusinessCode.CannotConnectionRedis.getValue());
		}
		return jedis;

	}

	/**
	 * 归还资源
	 * 
	 * @param jedis
	 * @param isBroken
	 */
	public static void returnBrokenResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 释放资源
	 * 
	 * @param jedis
	 * @param isBroken
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 获取byte[]类型Key
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			return StringUtils.getBytes((String) object);
		} else {
			return ObjectUtils.serialize(object);
		}
	}

	/**
	 * Object转换byte[]类型
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * 
	 * @param key
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtils.unserialize(bytes);
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
	public static Boolean lock(String requestId) {
		Jedis resource = getResource(0);
		String key = "redisDistributedLock";
		String var1 = resource.set(key, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, Constants.REDISLOCKTIME);
		returnResource(resource);
		if (LOCK_SUCCESS.equals(var1)) {
			return true;
		}
		return false;
	}

	/**
	 * 解锁操作
	 * 
	 * @param value 客户端标识(requestId)
	 * @return
	 */
	public static Boolean unLock(String value) {
		Jedis resource = getResource(0);
		String key = "redisDistributedLock";
		// 这个字符串是个lua脚本，代表的意思是如果根据key拿到的value跟传入的value相同就执行del，否则就返回0【保证安全性】
		String luaScript = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else  return 0 end";
		// 这个命令就是去执行lua脚本，KEYS的集合就是第二个参数，ARGV的集合就是第三参数【保证解锁的原子操作】
		Object var2 = resource.eval(luaScript, Collections.singletonList(key), Collections.singletonList(value));
		returnResource(resource);
		if (UNLOCK_SUCCESS == var2) {
			return true;
		}
		return false;
	}

	/**
	 * 重试机制
	 * 
	 * @param value 客户端标识
	 * @return
	 */
	public static Boolean lockRetry(String value) {
		Boolean flag = false;
		try {
			for (int i = 0; i < Constants.REDISRETRYNUM; i++) {
				flag = lock(value);
				if (flag) {
					break;
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			logger.error("尝试获取redis分布式锁失败,失败原因:{}", ExceptionUtil.getExceptionMsg(e));
		}
		return flag;
	}

}
