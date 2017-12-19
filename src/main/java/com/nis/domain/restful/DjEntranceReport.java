package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DfEntranceReport
 * @Description: 管控局点（带私有标签）实时统计模型
 * @author (rkg)
 * @date 2017年01月05日 下午3:41:50
 * @version V1.0
 */
public class DjEntranceReport extends DfReportEntity<DjEntranceReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6093854240033900622L;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "局点信息", required = true)
	protected Long entraceId;

	protected String searchService;
	protected String searchEntraceId;

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Long getEntraceId() {
		return entraceId;
	}

	public void setEntraceId(Long entraceId) {
		this.entraceId = entraceId;
	}

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

	@JsonIgnore
	public String getSearchEntraceId() {
		return searchEntraceId;
	}

	public void setSearchEntraceId(String searchEntraceId) {
		this.searchEntraceId = searchEntraceId;
	}

}
