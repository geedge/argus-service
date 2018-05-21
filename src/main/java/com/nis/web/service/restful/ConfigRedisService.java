package com.nis.web.service.restful;

import java.util.Map;

/**
 * 
 * <p>Title: ConfigRedisService</p>
 * <p>Description: 保存配置到redis中</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月17日 
 *
 */
public interface ConfigRedisService {
	/**
	 * 保存ysp样例配置接口
	 * @param service 业务类型
	 * @param map 
	 */
	public void saveConfigYSPDemoCompile(int service, Map<String, String> map);

	/**
	 * 保存ysp配置接口
	 * @param service
	 * @param map
	 */
	public void saveConfigYSPCompile(int service, Map<String, String> map);

	/**
	 * 获取指定key的自增长值
	 * @param key 需要自增的key
	 * @return
	 */
	public Long getIncrId(String key);
}
