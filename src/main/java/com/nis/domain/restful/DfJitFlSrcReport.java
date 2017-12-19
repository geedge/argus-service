package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfJitFlSrcReport extends DfJitLogEntity<DfJitFlSrcReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1866611122134109630L;
	@ApiModelProperty(value = "分类", required = true)
	private Integer fl;
	@ApiModelProperty(value = "性质", required = true)
	private Integer xz;

	/**
	 * 查询部分
	 */
	private String searchFl;
	private String searchXz;

	public DfJitFlSrcReport() {
		super();
	}

	public Integer getFl() {
		return fl;
	}

	public void setFl(Integer fl) {
		this.fl = fl;
	}

	public Integer getXz() {
		return xz;
	}

	public void setXz(Integer xz) {
		this.xz = xz;
	}

	@JsonIgnore
	public String getSearchFl() {
		return searchFl;
	}

	public void setSearchFl(String searchFl) {
		this.searchFl = searchFl;
	}

	@JsonIgnore
	public String getSearchXz() {
		return searchXz;
	}

	public void setSearchXz(String searchXz) {
		this.searchXz = searchXz;
	}

}