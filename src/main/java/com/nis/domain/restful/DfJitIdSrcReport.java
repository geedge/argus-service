package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfJitIdSrcReport extends DfJitLogEntity<DfJitIdSrcReport> {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1789859856962153038L;

	@ApiModelProperty(value = "配置id", required = true)
	private Long id;

	/**
	 * 查询部分
	 */
	private String searchId;

	public DfJitIdSrcReport() {
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