package com.nis.web.service.restful;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.datasource.DynamicJedisDataBase;
import com.nis.web.dao.impl.BaseRedisDao;

@Service("testRedisServiceimpl")
public class TestRedisServiceimpl extends BaseRedisDao<String, String> implements TestRedisService {
	@Transactional
	public void saveConfigCompile() {
		DynamicJedisDataBase.setRedisDataBase(2, redisTemplate);
		for (int i = 0; i < 10; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 5) {
				// int a = 1 / 0;
			}
		}
		// 切换redis数据库
		DynamicJedisDataBase.setRedisDataBase(3, redisTemplate);
		for (int i = 10; i < 20; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 5) {
				// int a = 1 / 0;
			}
		}
		DynamicJedisDataBase.setRedisDataBase(4, redisTemplate);
		for (int i = 20; i < 30; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 25) {
				 int a = 1 / 0;
			}
		}
	}
}
