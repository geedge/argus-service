package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.nis.domain.StatLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: DfSrcIpTagReport
 * @Description: TODO(境内ip、标签多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午2:29:28 
 * @version V1.0
 */
public class DfSrcIpTagDaily extends StatLogEntity{
	
	private static final long serialVersionUID = -1183216703585029756L;
	
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "省", required = true)
	protected String srcProvince;
	@ApiModelProperty(value = "标签", required = true)
	protected Long tag;

	
	protected String searchService;
	protected String searchSrcProvince;
	protected String searchTag;
	
	
	public Integer getService() {
		return service;
	}
	public void setService(Integer service) {
		this.service = service;
	}
	
	public void setSrcProvince(String srcProvince) {
		this.srcProvince = srcProvince;
	}
	public String getSrcProvince() {
		return srcProvince;
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
	public String getSearchSrcProvince() {
		return searchSrcProvince;
	}
	public void setSearchSrcProvince(String searchSrcProvince) {
		this.searchSrcProvince = searchSrcProvince;
	}
	@JsonIgnore
	public String getSearchTag() {
		return searchTag;
	}
	public void setSearchTag(String searchTag) {
		this.searchTag = searchTag;
	}

	

}
