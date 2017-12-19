package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.nis.domain.StatLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: DfSrcIpAttrReport
 * @Description: TODO(境内ip、性质多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午2:29:28 
 * @version V1.0
 */
public class DjSrcIpAttrMonth extends StatLogEntity{
	
	private static final long serialVersionUID = -1183216703585029756L;
	
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "省", required = true)
	protected String srcProvince;
	@ApiModelProperty(value = "性质", required = true)
	protected Long attrType;

	
	protected String searchService;
	protected String searchSrcProvince;
	protected String searchAttrType;
	
	
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
	public String getSearchSrcProvince() {
		return searchSrcProvince;
	}
	public void setSearchSrcProvince(String searchSrcProvince) {
		this.searchSrcProvince = searchSrcProvince;
	}
	@JsonIgnore
	public String getSearchAttrType() {
		return searchAttrType;
	}
	public void setSearchAttrType(String searchAttrType) {
		this.searchAttrType = searchAttrType;
	}

	

}
