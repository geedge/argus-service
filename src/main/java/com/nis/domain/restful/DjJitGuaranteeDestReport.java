package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DjJitGuaranteeDestReport extends DfJitLogEntity<DjJitGuaranteeDestReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4886806743747030623L;

	@ApiModelProperty(value = "保障期", required = true)
	private Integer guarantee;

	/**
	 * 查询部分
	 */
	private String searchGuarantee;

	public DjJitGuaranteeDestReport() {
		super();
	}

	public Integer getGuarantee() {
		return guarantee;
	}

	public void setGuarantee(Integer guarantee) {
		this.guarantee = guarantee;
	}

	@JsonIgnore
	public String getSearchGuarantee() {
		return searchGuarantee;
	}

	public void setSearchGuarantee(String searchGuarantee) {
		this.searchGuarantee = searchGuarantee;
	}

}