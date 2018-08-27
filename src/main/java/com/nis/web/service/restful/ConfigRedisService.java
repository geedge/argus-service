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
	 * 下发非maat配置接口,key是redisDBIndex,value是配置集合
	 * @param configMap
	 * @return 成功返回true,失败返回false或抛出异常
	 */
	public boolean saveUnMaatConfig(Map<Integer, List<Map<String, String>>> configMap);

	/**
	 * 保存 maat配置接口,key是redisDBIndex,value是配置集合
	 * @param configMap
	 * @return 成功返回true,失败返回false或抛出异常
	 */
	public boolean saveMaatConfig(Map<Integer, List<MaatConfig>> configMap);

	/**
	 * 分组复用域配置新增接口,key是redisDBIndex,value是配置集合
	 * @param configMap
	 * @return 成功返回true,失败返回false或抛出异常
	 */
	public boolean saveGroupReuseConfig(Map<Integer, List<MaatConfig>> configMap);

	/**
	 * 获取指定key的自增长值
	 * @param key 需要自增的key
	 * @return
	 */
	public Long getIncrId(String key);

	/**
	 * 删除非maat类配置,第一个key是redisDBIndex,第二个key是业务类型,value是配置id集合
	 * @param idMap
	 * @param isInvalid 是否将配置置为无效
	 * @return 成功返回true,失败返回false或抛出异常
	 */
	public boolean delUnMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap, boolean isInvalid);

	/**
	 * 删除maat类配置,第一个key是redisDBIndex,第二个key是业务类型,value是配置id集合
	 * @param idMap
	 * @return 成功返回true,失败返回false或抛出异常
	 */
	public boolean delMaatConfig(Map<Integer, Map<Integer, List<Long>>> idMap);

	/**
	 * 删除分组复用的域配置
	 * @param maatMap key是service
	 * @return
	 */
	public boolean delGroupReuseConfig(Map<Integer, List<MaatConfig>> configMap);

}
