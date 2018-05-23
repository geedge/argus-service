package com.nis.web.service.restful;

import org.springframework.stereotype.Service;

import com.nis.datasource.DynamicJedisDataBase;
import com.nis.web.dao.impl.BaseRedisDao;

@Service()
public class OperRedisService extends BaseRedisDao<String, String> {

	public String getMaatVersion(int redisIndex) {
		DynamicJedisDataBase.setRedisDataBase(redisIndex, redisTemplate);
		// RedisAtomicLong atomicLong = new RedisAtomicLong(key,
		// redisTemplate.getConnectionFactory());
		//
		// Long id = atomicLong.getAndIncrement() + 1;
		// if ((id == null || id.longValue() == 0) && liveTime > 0) {
		// atomicLong.expire(liveTime, TimeUnit.SECONDS);
		// }
		// return id;

		return redisTemplate.opsForValue().get("MAAT_VERSION");

		// return redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
		// return redisTemplate.boundValueOps("MAAT_VERSION").increment(1l);
	}
}
