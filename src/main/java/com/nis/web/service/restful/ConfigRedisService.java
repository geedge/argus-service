package com.nis.web.service.restful;

import java.util.List;
import java.util.Map;

import com.nis.domain.restful.MaatConfig;

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
	 *  保存非maat配置接口,例如ysp文件样例,ysp标志样例
	 * @param listMap 配置集合,一个map是一个集合
	 * @param service 业务类型
	 */
	public void saveUnMaatConfig(List<Map<String, String>> listMap, int service);

	/**
	 * 保存maat配置接口,例如ysp配置,文本类配置
	 * @param maatConfigList
	 * @param service 业务类型
	 */
	public void saveMaatConfig(List<MaatConfig> maatConfigList, int service);

	/**
	 * 获取指定key的自增长值
	 * @param key 需要自增的key
	 * @return
	 */
	public Long getIncrId(String key);

	/**
	 * 删除非maat类配置
	 * @param compileIdList 配置id集合 
	 * @param service 配置id所属的业务类型
	 */
	public void delUnMaatConfig(List<Long> compileIdList, int service);

	/**
	 * 删除maat类配置
	 * @param compileIdList 配置id集合 
	 * @param service 配置id所属的业务类型
	 */
	public void delMaatConfig(List<Long> compileIdList, int service);
}
