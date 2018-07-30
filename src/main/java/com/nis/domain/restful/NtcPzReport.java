package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcPzReport extends NtcReportEntity<NtcPzReport>{

	private static final long serialVersionUID = 987165268916441272L;
	@JsonInclude(value=Include.NON_NULL)
	@ApiModelProperty(value="配置ID", required=true)
	protected Long cfgId;
	
	protected String searchCfgId;
	
	public Long getCfgId() {
		return cfgId;
	}

	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}

	@JsonIgnore
	public String getSearchCfgId() {
		return searchCfgId;
	}

	public void setSearchCfgId(String searchCfgId) {
		this.searchCfgId = searchCfgId;
	}
}
