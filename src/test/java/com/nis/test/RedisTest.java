package com.nis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.nis.web.service.restful.TestRedisService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations =
// {"classpath:applicationContext-*.xml,classpath:spring-mvc.xml,classpath:mybatis-config.xml"
// })
@ContextConfiguration(locations = { "classpath:applicationContext-redis.xml", "classpath:spring-mvc.xml"
		// ,"classpath:mybatis-config.xml"
})
public class RedisTest {

	@Autowired
	TestRedisService configService;

	@Test
	public void testTrans() {
		configService.saveConfigCompile();
	}

}
