package com.nis.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import com.google.common.collect.Lists;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;
import com.nis.web.service.SpringContextHolder;

public class JedisUtils {
	private static Logger logger = LoggerFactory.getLogger(JedisUtils.class);
	private static JedisPool jedisPool = SpringContextHolder.getBean(JedisPool.class);

	/**
	 * 获取缓存
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
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取" + key + "对应的值失败", RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取缓存
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
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取" + key + "对应的值失败", RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	* <b>可以作为获取唯一id的方法</b><br/>
	* 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
	* 
	* @param String
	*            key
	* @param long number 要减去的值
	* @return long 相加后的值
	* */
	public static long incrBy(String key, long number, int redisDb) {
		Jedis jedis = getResource(redisDb);
		long len = jedis.incrBy(key, number);
		returnResource(jedis);
		return len;
	}

	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
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
			throw new ServiceRuntimeException("向" + redisDb + "号redisDB中设置zset失败,key=" + key + ",value=" + value, RestBusinessCode.ZsetFailed.getValue());
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取List缓存
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
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取" + key + "对应的值失败", RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取List缓存
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
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中获取" + key + "对应的值失败", RestBusinessCode.KeyNotExistsInRedis.getValue());
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 缓存是否存在
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
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中判断" + key + "是否存在失败", RestBusinessCode.ExistsKeyFailed.getValue());
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 缓存是否存在
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
			throw new ServiceRuntimeException("从" + redisDb + "号redisDB中判断" + key + "是否存在失败", RestBusinessCode.ExistsKeyFailed.getValue());
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取资源
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource(int redisDb) throws JedisException {
		Jedis jedis = null;
		if (jedisPool == null) {
			throw new ServiceRuntimeException("redis连接池为空,请联系管理员检查程序",RestBusinessCode.CannotConnectionRedis.getValue());
		}
		try {
			jedis = jedisPool.getResource();
			jedis.select(redisDb);
		} catch (JedisException e) {
			returnBrokenResource(jedis);
			throw new ServiceRuntimeException("获取redis连接失败,请联系管理员检查程序", RestBusinessCode.CannotConnectionRedis.getValue());
		}
		return jedis;

	}

	/**
	 * 归还资源
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
	 * @param key
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * @param key
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtils.unserialize(bytes);
	}

}
