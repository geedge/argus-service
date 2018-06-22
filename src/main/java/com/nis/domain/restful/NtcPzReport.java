package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcPzReport implements Serializable{

	private static final long serialVersionUID = 987165268916441272L;
	@ApiModelProperty(value="配置ID", required=true)
	protected Long cfgId;
	@ApiModelProperty(value="业务类型", required=true)
	protected Integer service;
	@ApiModelProperty(value="日志数量", required=true)
	protected Long sum;
	@ApiModelProperty(value="统计时间", required=true)
	protected Date reportTime;
	
	/**
	 * 业务类型：1:配置命中总量业务  2:配置报表业务
	 */
	protected String searchBusinessType = "1";
	protected String searchReportStartTime;
	protected String searchReportEndTime;
	protected Long searchReportStartTimeCluster;
	protected Long searchReportEndTimeCluster;
	protected String searchService;
	protected String searchCfgId;
	
    protected Page<NtcPzReport> page;
	
	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	/**
	 * 
	 */
	public NtcPzReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param cfgId
	 * @param service
	 */
	public NtcPzReport(Long cfgId, Long sum) {
		super();
		this.cfgId = cfgId;
		this.sum = sum;
	}


	public Long getCfgId() {
		return cfgId;
	}

	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}

	@JsonSerialize(using=JsonDateSerializer.class)
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
	@JsonIgnore
	public Long getSearchReportStartTimeCluster() {
		return searchReportStartTimeCluster;
	}

	public void setSearchReportStartTimeCluster(Long searchReportStartTimeCluster) {
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
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}
	@JsonIgnore
	public String getSearchCfgId() {
		return searchCfgId;
	}

	public void setSearchCfgId(String searchCfgId) {
		this.searchCfgId = searchCfgId;
	}

	@JsonIgnore
	@XmlTransient
	public Page<NtcPzReport> getPage() {
		if (page == null){
			page = new Page<NtcPzReport>();
		}
		return page;
	}
	
	public Page<NtcPzReport> setPage(Page<NtcPzReport> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
}
