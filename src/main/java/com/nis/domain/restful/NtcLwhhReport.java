package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcLwhhReport extends NtcReportEntity<NtcLwhhReport> {

	private static final long serialVersionUID = -5980925900825684234L;
	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "性质", required = true)
	protected Integer lwhh;

	protected String searchLwhh;

	/**
	 * @param lwhh the lwhh to set
	 */
	public void setLwhh(Integer lwhh) {
		this.lwhh = lwhh;
	}

	/**
	 * @return the lwhh
	 */
	public Integer getLwhh() {
		return lwhh;
	}

	/**
	 * @return the searchLwhh
	 */
	@JsonIgnore
	public String getSearchLwhh() {
		return searchLwhh;
	}

	/**
	 * @param searchLwhh the searchLwhh to set
	 */
	public void setSearchLwhh(String searchLwhh) {
		this.searchLwhh = searchLwhh;
	}
}
