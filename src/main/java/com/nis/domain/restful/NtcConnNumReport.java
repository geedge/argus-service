package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcConnNumReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5957594320933224337L;
	@ApiModelProperty(value = "日志数量", required = true)
	protected Long num;
	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "统计时间", required = true)
	protected Date reportTime;

	/**
	 * 查询业务类型：1:drop   2:loop	3:newLink  4:clockLink  5:liveLink  6:pass
	 */
	protected String searchBusinessType = "1";
	protected String searchReportStartTime;
	protected String searchReportEndTime;

	/**
	 * 
	 */
	public NtcConnNumReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NtcConnNumReport(Long num, Date reportTime) {
		super();
		this.num = num;
		this.reportTime = reportTime;
	}

	public Long getNum() {
		return num;
	}

	public void setnum(Long num) {
		this.num = num;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	@JsonIgnore
	public String getSearchBusinessType() {
		return searchBusinessType;
	}

	public void setSearchBusinessType(String searchBusinessType) {
		this.searchBusinessType = searchBusinessType;
	}

	@JsonIgnore
	public String getSearchReportStartTime() {
		return searchReportStartTime;
	}

	public void setSearchReportStartTime(String searchReportStartTime) {
		this.searchReportStartTime = searchReportStartTime;
	}

	@JsonIgnore
	public String getSearchReportEndTime() {
		return searchReportEndTime;
	}

	public void setSearchReportEndTime(String searchReportEndTime) {
		this.searchReportEndTime = searchReportEndTime;
	}

}
