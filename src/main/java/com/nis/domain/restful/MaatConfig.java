package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * <p>Title: MaatConfig</p>
 * <p>Description: 将编译,组,域配置封装bean</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月22日 
 *
 */
public class MaatConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 编译配置
	 */
	private Map<String, String> compileMap;
	/**
	 * 分组配置
	 */
	private Map<String, String> groupMap;
	/**
	 * ip域配置
	 */
	private Map<String, String> ipRegionMap;
	/**
	 * 数值域配置
	 */
	private Map<String, String> numRegionMap;
	/**
	 * 字符串域配置
	 */
	private Map<String, String> strRegionMap;
	/**
	 * 增强字符串域配置
	 */
	private Map<String, String> strStrRegionMap;

	/**
	 * 业务类型
	 */
	private Integer service;

	public Map<String, String> getCompileMap() {
		return compileMap;
	}

	public void setCompileMap(Map<String, String> compileMap) {
		this.compileMap = compileMap;
	}

	public Map<String, String> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(Map<String, String> groupMap) {
		this.groupMap = groupMap;
	}

	public Map<String, String> getIpRegionMap() {
		return ipRegionMap;
	}

	public void setIpRegionMap(Map<String, String> ipRegionMap) {
		this.ipRegionMap = ipRegionMap;
	}

	public Map<String, String> getNumRegionMap() {
		return numRegionMap;
	}

	public void setNumRegionMap(Map<String, String> numRegionMap) {
		this.numRegionMap = numRegionMap;
	}

	public Map<String, String> getStrRegionMap() {
		return strRegionMap;
	}

	public void setStrRegionMap(Map<String, String> strRegionMap) {
		this.strRegionMap = strRegionMap;
	}

	public Map<String, String> getStrStrRegionMap() {
		return strStrRegionMap;
	}

	public void setStrStrRegionMap(Map<String, String> strStrRegionMap) {
		this.strStrRegionMap = strStrRegionMap;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

}
