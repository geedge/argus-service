package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfJitAffairDestReport extends DfJitLogEntity<DfJitAffairDestReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6367339032317209141L;
	@ApiModelProperty(value = "子话题", required = true)
	private Integer affair;
	@ApiModelProperty(value = "事件", required = true)
	private Integer topic;

	/**
	 * 查询部分
	 */
	private String searchAffair;
	private String searchTopic;

	public DfJitAffairDestReport() {
		super();
	}

	public Integer getAffair() {
		return affair;
	}

	public void setAffair(Integer affair) {
		this.affair = affair;
	}

	public Integer getTopic() {
		return topic;
	}

	public void setTopic(Integer topic) {
		this.topic = topic;
	}

	@JsonIgnore
	public String getSearchAffair() {
		return searchAffair;
	}

	public void setSearchAffair(String searchAffair) {
		this.searchAffair = searchAffair;
	}

	@JsonIgnore
	public String getSearchTopic() {
		return searchTopic;
	}

	public void setSearchTopic(String searchTopic) {
		this.searchTopic = searchTopic;
	}

}