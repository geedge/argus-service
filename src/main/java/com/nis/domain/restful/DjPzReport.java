package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: DjPzReport
 * @Description: TODO(一句话描述这个类)
 * @author (ZBC)
 * @date 2016年11月22日下午06:00:00
 * @version V1.0
 */
public class DjPzReport extends DfReportEntity<DjPzReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5378450857152481993L;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "编译配置ID", required = true)
	protected Long cfgId;

	protected String searchCfgId;
	protected String searchId;
	protected String searchService;

	@JsonIgnore
	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
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

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Long getCfgId() {
		return cfgId;
	}

	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}

}
