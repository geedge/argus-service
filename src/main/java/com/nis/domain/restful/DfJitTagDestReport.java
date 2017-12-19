package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfJitTagDestReport extends DfJitLogEntity<DfJitTagDestReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8057168628547445607L;

	@ApiModelProperty(value = "标签", required = true)
	private Integer tag;

	/**
	 * 查询部分
	 */
	private String searchTag;

	public DfJitTagDestReport() {
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