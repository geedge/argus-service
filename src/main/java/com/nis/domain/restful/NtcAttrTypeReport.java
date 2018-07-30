package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcAttrTypeReport extends NtcReportEntity<NtcAttrTypeReport>{

	private static final long serialVersionUID = -5609071907676757108L;
	@JsonInclude(value=Include.NON_NULL)
	@ApiModelProperty(value="性质", required=true)
	protected Integer attrType ;
	
	protected String searchAttrType;

	public Integer getAttrType() {
		return attrType;
	}

	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}
	@JsonIgnore
	public String getSearchAttrType() {
		return searchAttrType;
	}

	public void setSearchAttrType(String searchAttrType) {
		this.searchAttrType = searchAttrType;
	}
	
}
