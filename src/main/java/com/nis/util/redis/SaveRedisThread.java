package com.nis.util.redis;
import org.apache.log4j.Logger;

import com.nis.util.Constants;
import com.nis.util.JsonMapper;
import com.nis.util.redis.RedisDao;
import com.nis.web.service.SpringContextHolder;

public class SaveRedisThread extends Thread {
	private static RedisDao redisDao = SpringContextHolder.getBean(RedisDao.class);
	protected final Logger logger = Logger.getLogger(this.getClass());

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
