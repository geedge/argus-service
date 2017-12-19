package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfJitTagSrcReport extends DfJitLogEntity<DfJitTagSrcReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1338482031809189715L;

	@ApiModelProperty(value = "标签", required = true)
	private Integer tag;

	/**
	 * 查询部分
	 */
	private String searchTag;

	public DfJitTagSrcReport() {
		super();
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	@JsonIgnore
	public String getSearchTag() {
		return searchTag;
	}

	public void setSearchTag(String searchTag) {
		this.searchTag = searchTag;
	}

}