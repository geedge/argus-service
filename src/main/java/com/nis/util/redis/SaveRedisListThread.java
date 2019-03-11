package com.nis.util.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nis.web.service.SpringContextHolder;

public class SaveRedisListThread extends Thread {
	private static RedisDao redisDao = SpringContextHolder.getBean(RedisDao.class);
	protected static final Logger logger =LoggerFactory.getLogger(SaveRedisListThread.class);

	private String key;
	private List<String> value;
	private int expire;

	public SaveRedisListThread(String key, List<String> value, int expire) {
		super(SaveRedisListThread.class.getSimpleName());
		this.key = key;
		this.value = value;
		this.expire = expire;
	}

	@Override
	public void run() {
		try {
			redisDao.saveList(value, key, expire);
		} catch (Exception e) {
			logger.error(e.getMessage());
			try {
				if (redisDao.exists(key)) {
					redisDao.del(key);
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage());
			}
		}
	}
}
