package com.nis.web.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseRedisDao<K, V> {

	@Autowired
	protected RedisTemplate<K, V> redisTemplate;
	
}
