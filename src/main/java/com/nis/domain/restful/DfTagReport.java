package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: dfTagReport
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2016年10月31日上午11:25:38
 * @version V1.0
 */
public class DfTagReport extends DfReportEntity<DfTagReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6425675908119093476L;
	@ApiModelProperty(value = "标签", required = true)
	protected Integer tag;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;

	protected String searchTag;
	protected String searchService;

	@JsonIgnore
	public String getSearchTag() {
		return searchTag;
	}

	public void setSearchTag(String searchTag) {
		this.searchTag = searchTag;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}
}
