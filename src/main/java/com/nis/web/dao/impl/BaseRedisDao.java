package com.nis.web.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import com.nis.web.service.SpringContextHolder;

@Repository
public abstract class BaseRedisDao<K, V> {

	//@Autowired
	protected RedisTemplate<K, V> redisTemplate1;

	// public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
	// StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	// redisTemplate.setKeySerializer(stringRedisSerializer);
	// redisTemplate.setValueSerializer(stringRedisSerializer);
	// this.redisTemplate = redisTemplate;
	// }
	protected static void setRedisDataBase(int index, RedisTemplate redisTemplate) {
		JedisConnectionFactory connectionFactory = SpringContextHolder.getBean("connectionFactory" + index);
		//connectionFactory.setDatabase(index);
		redisTemplate.setConnectionFactory(connectionFactory);
	}
}
