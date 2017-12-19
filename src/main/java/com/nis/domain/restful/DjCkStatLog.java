package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DjCkStatLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8465748814742964183L;
	@ApiModelProperty(value = "ID", required = true)
	private Long statId;
	@ApiModelProperty(value = "配置ID", required = true)
	private Long configId;
	@ApiModelProperty(value = "业务类型", required = true)
	private Integer service;
	@ApiModelProperty(value = "生效系统", required = true)
	private Integer activeSys;
	@ApiModelProperty(value = "国际出入口", required = true)
	private Long gjCkId;
	@ApiModelProperty(value = "总计", required = true)
	private Long sum;
	@ApiModelProperty(value = "统计时间", required = true)
	private Date capTime;

	private String searchCapStartTime;
	private String searchCapEndTime;
	private String searchGjCkId;
	private String searchCfgId;
	private String searchService;
	private String searchActiveSys;
	/**
	 * 当前实体分页对象
	 */
	private Page<DjCkStatLog> page;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	private Map<String, String> sqlMap;

	public DjCkStatLog() {
		super();
	}

	@JsonIgnore
	public Long getStatId() {
		return statId;
	}

	public void setStatId(Long statId) {
		this.statId = statId;
	}

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Long getGjCkId() {
		return gjCkId;
	}

	public void setGjCkId(Long gjCkId) {
		this.gjCkId = gjCkId;
	}

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getCapTime() {
		return capTime;
	}

	public void setCapTime(Date capTime) {
		this.capTime = capTime;
	}

	@JsonIgnore
	public String getSearchCapStartTime() {
		return searchCapStartTime;
	}

	public void setSearchCapStartTime(String searchCapStartTime) {
		this.searchCapStartTime = searchCapStartTime;
	}

	@JsonIgnore
	public String getSearchCapEndTime() {
		return searchCapEndTime;
	}

	public void setSearchCapEndTime(String searchCapEndTime) {
		this.searchCapEndTime = searchCapEndTime;
	}

	@JsonIgnore
	public String getSearchGjCkId() {
		return searchGjCkId;
	}

	public void setSearchGjCkId(String searchGjCkId) {
		this.searchGjCkId = searchGjCkId;
	}

	@JsonIgnore
	public String getSearchCfgId() {
		return searchCfgId;
	}

	public void setSearchCfgId(String searchCfgId) {
		this.searchCfgId = searchCfgId;
	}

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

	@JsonIgnore
	@XmlTransient
	public Page<DjCkStatLog> getPage() {
		if (page == null) {
			page = new Page<DjCkStatLog>();
		}
		return page;
	}

	public void setPage(Page<DjCkStatLog> page) {
		this.page = page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null) {
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	public Integer getActiveSys() {
		return activeSys;
	}

	public void setActiveSys(Integer activeSys) {
		this.activeSys = activeSys;
	}

	@JsonIgnore
	public String getSearchActiveSys() {
		return searchActiveSys;
	}

	public void setSearchActiveSys(String searchActiveSys) {
		this.searchActiveSys = searchActiveSys;
	}

}
