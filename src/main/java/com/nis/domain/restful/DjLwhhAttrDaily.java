package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.nis.domain.StatLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: DfLwhhAttrReport
 * @Description: TODO(来文函号、性质多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午2:29:28 
 * @version V1.0
 */
public class DjLwhhAttrDaily extends StatLogEntity{
	
	private static final long serialVersionUID = -1183216703585029756L;
	
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "来文函号", required = true)
	protected Long lwhh;
	@ApiModelProperty(value = "性质", required = true)
	protected Long attrType;

	
	protected String searchService;
	protected String searchLwhh;
	protected String searchAttrType;
	
	
	public Integer getService() {
		return service;
	}
	public void setService(Integer service) {
		this.service = service;
	}
	public Long getLwhh() {
		return lwhh;
	}
	public void setLwhh(Long lwhh) {
		this.lwhh = lwhh;
	}
	public Long getAttrType() {
		return attrType;
	}
	public void setAttrType(Long attrType) {
		this.attrType = attrType;
	}
	
	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}
	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}
	
	@JsonIgnore
	public String getSearchLwhh() {
		return searchLwhh;
	}
	public void setSearchLwhh(String searchLwhh) {
		this.searchLwhh = searchLwhh;
	}
	@JsonIgnore
	public String getSearchAttrType() {
		return searchAttrType;
	}
	public void setSearchAttrType(String searchAttrType) {
		this.searchAttrType = searchAttrType;
	}

	

}
