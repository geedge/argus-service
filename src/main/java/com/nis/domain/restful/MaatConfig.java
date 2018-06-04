package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;
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
	private List<Map<String, String>> groupMapList;
	/**
	 * ip域配置
	 */
	private List<Map<String, String>> ipRegionMapList;
	/**
	 * 数值域配置
	 */
	private List<Map<String, String>> numRegionMapList;
	/**
	 * 字符串域配置
	 */
	private List<Map<String, String>> strRegionMapList;
	/**
	 * 增强字符串域配置
	 */
	private List<Map<String, String>> strStrRegionMapList;
	/**
	 * 文件摘要类域配置
	 */
	private List<Map<String, String>> fileDigestRegionMapList;
	/**
	 * 文本相似性域配置
	 */
	private List<Map<String, String>> fileLikeRegionMapList;

	/**
	 * 区域ip域配置
	 */
	private List<Map<String, String>> ipClientRangeMapList;
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

	public List<Map<String, String>> getGroupMapList() {
		return groupMapList;
	}

	public void setGroupMapList(List<Map<String, String>> groupMapList) {
		this.groupMapList = groupMapList;
	}

	public List<Map<String, String>> getIpRegionMapList() {
		return ipRegionMapList;
	}

	public void setIpRegionMapList(List<Map<String, String>> ipRegionMapList) {
		this.ipRegionMapList = ipRegionMapList;
	}

	public List<Map<String, String>> getNumRegionMapList() {
		return numRegionMapList;
	}

	public void setNumRegionMapList(List<Map<String, String>> numRegionMapList) {
		this.numRegionMapList = numRegionMapList;
	}

	public List<Map<String, String>> getStrRegionMapList() {
		return strRegionMapList;
	}

	public void setStrRegionMapList(List<Map<String, String>> strRegionMapList) {
		this.strRegionMapList = strRegionMapList;
	}

	public List<Map<String, String>> getStrStrRegionMapList() {
		return strStrRegionMapList;
	}

	public void setStrStrRegionMapList(List<Map<String, String>> strStrRegionMapList) {
		this.strStrRegionMapList = strStrRegionMapList;
	}

	public List<Map<String, String>> getFileDigestRegionMapList() {
		return fileDigestRegionMapList;
	}

	public void setFileDigestRegionMapList(List<Map<String, String>> fileDigestRegionMapList) {
		this.fileDigestRegionMapList = fileDigestRegionMapList;
	}

	public List<Map<String, String>> getFileLikeRegionMapList() {
		return fileLikeRegionMapList;
	}

	public void setFileLikeRegionMapList(List<Map<String, String>> fileLikeRegionMapList) {
		this.fileLikeRegionMapList = fileLikeRegionMapList;
	}

	public List<Map<String, String>> getIpClientRangeMapList() {
		return ipClientRangeMapList;
	}

	public void setIpClientRangeMapList(List<Map<String, String>> ipClientRangeMapList) {
		this.ipClientRangeMapList = ipClientRangeMapList;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

}
