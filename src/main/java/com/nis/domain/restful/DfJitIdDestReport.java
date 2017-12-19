package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfJitIdDestReport extends DfJitLogEntity<DfJitIdDestReport> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3056813909262889527L;

	@ApiModelProperty(value = "配置id", required = true)
	private Long id;

	/**
	 * 查询部分
	 */
	private String searchId;

	public DfJitIdDestReport() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@JsonIgnore
	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}


	
}