package com.nis.datasource;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.nis.web.service.SpringContextHolder;

/**
 * 
 * <p>Title: DynamicJedisDataBase</p>
 * <p>Description: 设置使用redis的哪个数据库</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月17日 
 *
 */
public class DynamicJedisDataBase {
	/**
	 * 设置redisTemplate使用哪个数据库,默认使用的是数据库0
	 * @param index
	 * @param redisTemplate
	 */
	public static void setRedisDataBase1(int index, RedisTemplate redisTemplate) {
		JedisConnectionFactory connectionFactory = SpringContextHolder.getBean("connectionFactory"+index);
		connectionFactory.setDatabase(index);
		redisTemplate.setConnectionFactory(connectionFactory);
	}
}
