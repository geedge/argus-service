package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DfLwhhReport
 * @Description: 管控来文函号（带私有标签）实时统计模型
 * @author (rkg)
 * @date 2017年01月05日 下午3:41:50
 * @version V1.0
 */
public class DjLwhhReport extends DfReportEntity<DjLwhhReport> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7545794734532459964L;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "来文函号ID", required = true)
	protected Integer lwhh;

	protected String searchService;
	protected String searchLwhh;

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getLwhh() {
		return lwhh;
	}

	public void setLwhh(Integer lwhh) {
		this.lwhh = lwhh;
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

}
