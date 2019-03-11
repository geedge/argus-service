package com.nis.util.redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nis.util.JsonMapper;
import com.nis.web.service.SpringContextHolder;

public class SaveRedisThread extends Thread {
	private static RedisDao redisDao = SpringContextHolder.getBean(RedisDao.class);
	protected static final Logger logger =LoggerFactory.getLogger(SaveRedisThread.class);
	private String key;
	private Object value;
	private int expire;
	
	public SaveRedisThread( String key, Object value,int expire){
		super(SaveRedisThread.class.getSimpleName());
		this.key = key;
		this.value = value;
		this.expire = expire;
	}
	
	@Override
	public void run() {
		try {
			logger.info("saveRedis开始--"+System.currentTimeMillis());
			redisDao.saveString(key, JsonMapper.toJsonString(value), expire);
		} catch (Exception e) {
			logger.error(e.getMessage());
			try {
				if(redisDao.exists(key)){
					redisDao.del(key);
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage());
			}
		}finally {
			logger.info("saveRedis结束--"+System.currentTimeMillis());
		}
	}
}
