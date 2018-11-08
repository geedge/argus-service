package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcRadiusReport implements Serializable{
	
	private static final long serialVersionUID = 7635016645942704971L;
	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "接入IP", required = true)
	protected String nasIp;
	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "用户名", required = true)
	protected String account;
	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "次数", required = true)
	protected Long num;
	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "统计时间", required = true)
	protected Date reportTime;

	/**
	 * 业务类型：1:用户名和接口IP列表 2:根据用户，统计IP变化趋势 3：IP根据，统计用户变化趋势
	 */
	protected String searchBusinessType = "1";
	protected String searchReportStartTime;
	protected String searchReportEndTime;
	protected Long searchReportStartTimeCluster;
	protected Long searchReportEndTimeCluster;

	protected String searchNasIp;
	protected String searchAccount;
	protected String groupType;

	public String getNasIp() {
		return nasIp;
	}

	public void setNasIp(String nasIp) {
		this.nasIp = nasIp;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getSearchBusinessType() {
		return searchBusinessType;
	}
	@JsonIgnore
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
	@JsonIgnore
	public Long getSearchReportStartTimeCluster() {
		return searchReportStartTimeCluster;
	}

	public void setSearchReportStartTimeCluster(
			Long searchReportStartTimeCluster) {
		this.searchReportStartTimeCluster = searchReportStartTimeCluster;
	}
	@JsonIgnore
	public Long getSearchReportEndTimeCluster() {
		return searchReportEndTimeCluster;
	}

	public void setSearchReportEndTimeCluster(Long searchReportEndTimeCluster) {
		this.searchReportEndTimeCluster = searchReportEndTimeCluster;
	}
	@JsonIgnore
	public String getSearchNasIp() {
		return searchNasIp;
	}

	public void setSearchNasIp(String searchNasIp) {
		this.searchNasIp = searchNasIp;
	}
	@JsonIgnore
	public String getSearchAccount() {
		return searchAccount;
	}

	public void setSearchAccount(String searchAccount) {
		this.searchAccount = searchAccount;
	}
	@JsonIgnore
	public String getGroupType() {
		return groupType;
	}
	
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

}
