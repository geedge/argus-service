package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: DfLwhhTagReport
 * @Description: TODO(来文函号、标签多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午2:29:28 
 * @version V1.0
 */
public class DfLwhhTagReport extends DfReportEntity<DfLwhhTagReport> {
	
	private static final long serialVersionUID = -1183216703585029756L;
	
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "来文函号", required = true)
	protected Long lwhh;
	@ApiModelProperty(value = "标签", required = true)
	protected Long tag;

	
	protected String searchService;
	protected String searchLwhh;
	protected String searchTag;
	
	
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
	public Long getTag() {
		return tag;
	}
	public void setTag(Long tag) {
		this.tag = tag;
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
	public String getSearchTag() {
		return searchTag;
	}
	public void setSearchTag(String searchTag) {
		this.searchTag = searchTag;
	}

	

}
