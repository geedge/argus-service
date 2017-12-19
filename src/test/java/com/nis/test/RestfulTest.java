/**   
* @Title: RestfulTest.java 
* @Package com.nis.test 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月15日 下午3:31:17 
* @version V1.0   
*/
package com.nis.test;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.nis.domain.ControlLog;
import com.nis.domain.Page;
import com.nis.util.StringUtil;

/** 
* @ClassName: RestfulTest 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月15日 下午3:31:17 
* @version V1.0 
*/
public class RestfulTest {
	public static final String REST_SERVICE_URL = "http://127.0.0.1:8080/gk/service/v1/";
	
	/**
	 * 
	* @Title: testLogQuery 
	* @Description: TODO(测试获取所有日志信息) 
	* @param     入参
	* @return void    返回类型
	* @author （darnell） 
	* @throws
	* @date 2016年8月17日 下午5:26:39 
	* @version V1.0
	 */
	@Test
	public void testLogQuery() {
		RestTemplate restTemplate = new RestTemplate();
		Page page = restTemplate.getForObject(REST_SERVICE_URL + "logs", Page.class);
		Assert.assertEquals(page.getList().size() > 0, true);
		
	}
	
	/**
	 * 
	* @Title: getLog 
	* @Description: TODO(测试获取某条日志信息) 
	* @param     入参
	* @return void    返回类型
	* @author （darnell） 
	* @throws
	* @date 2016年8月17日 下午5:26:55 
	* @version V1.0
	 */
	@Test
	public void getLog() {
		RestTemplate restTemplate = new RestTemplate();
		ControlLog log = restTemplate.getForObject(REST_SERVICE_URL + "logs/102", ControlLog.class);
		Assert.assertEquals(StringUtil.isEmpty(log), false);
	}
	
	

}
