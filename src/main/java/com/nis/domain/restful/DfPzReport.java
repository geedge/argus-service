package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: dfPzReport
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2016年10月31日上午11:11:22
 * @version V1.0
 */
public class DfPzReport extends DfReportEntity<DfPzReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6348665724846062686L;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "编译配置ID", required = true)
	protected Long cfgId;

	protected String searchCfgId;
	protected String searchService;

	@JsonIgnore
	public String getSearchCfgId() {
		return searchCfgId;
	}

	public void setSearchCfgId(String searchCfgId) {
		this.searchCfgId = searchCfgId;
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

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

}
