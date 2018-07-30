package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcTagReport extends NtcReportEntity<NtcTagReport>{


	private static final long serialVersionUID = -3973713578165502900L;
	@JsonInclude(value=Include.NON_NULL)
	@ApiModelProperty(value="标签", required=true)
	protected Integer tag;
	
	protected String searchTag;

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
