package com.nis.web.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.nis.web.service.SpringContextHolder;

public class BaseRedisDao {
	private static Logger logger = LoggerFactory.getLogger(BaseRedisDao.class);

	// @Autowired
	// protected RedisTemplate<K, V> redisTemplate1;

	// public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
	// StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	// redisTemplate.setKeySerializer(stringRedisSerializer);
	// redisTemplate.setValueSerializer(stringRedisSerializer);
	// this.redisTemplate = redisTemplate;
	// }
	// protected static void setRedisDataBase(int index, RedisTemplate
	// redisTemplate) {
	// JedisConnectionFactory connectionFactory =
	// SpringContextHolder.getBean("connectionFactory" + index);
	// connectionFactory.setDatabase(index);
	// redisTemplate.setConnectionFactory(connectionFactory);
	// }

	/**
	 * 利用redis连接对象判断key是否存在,在事务中判断key是否存在时由于判断是否开启了multi(事务需要开启multi),直接返回了null,导致判断key的时候出现了问题
	 * @param index 几号redis库
	 * @param key  需要判断的key
	 * @param redisTemplate  RedisTemplate对象
	 * @return
	 */
	public static Boolean keyIsExist(int index, String key) {
		RedisTemplate redisTemplate = SpringContextHolder.getBean("redisTemplateNoTrans" + index);
		if (redisTemplate != null) {
			redisTemplate.setEnableTransactionSupport(false);
			Boolean hasKey = redisTemplate.hasKey(key);
			if (hasKey == null) {
				logger.error("判断key是否存在时出现错误");
			}
			return hasKey;
		} else {
			throw new RuntimeException("从" + index + "号redis库中判断" + key + "是否存在时失败,失败原因:redisTemplate为null请联系开发人员");
		}
	}

	/**
	 * 获取自增长id
	 * @param index
	 * @param key
	 * @return
	 */
	public static Long getIncr(int index, String key) {
		RedisTemplate<String, String> redisTemplate = SpringContextHolder.getBean("redisTemplateNoTrans" + index);
		if (redisTemplate != null) {
			redisTemplate.setEnableTransactionSupport(false);
			Long id = redisTemplate.boundValueOps(key.toUpperCase()).increment(1l);
			return id;
		} else {
			throw new RuntimeException("从" + index + "号redis库中获取" + key + "的自增长值时失败,失败原因:redisTemplate为null请联系开发人员");
		}
	}

	public static String getValByKey(int index, String key) {
		RedisTemplate<String, String> redisTemplate = SpringContextHolder.getBean("redisTemplateNoTrans" + index);
		if (redisTemplate != null) {
			redisTemplate.setEnableTransactionSupport(false);
			return redisTemplate.opsForValue().get(key);
		} else {
			throw new RuntimeException("从" + index + "号redis库中获取" + key + "的值时失败,失败原因:redisTemplate为null请联系开发人员");
		}
	}

}
