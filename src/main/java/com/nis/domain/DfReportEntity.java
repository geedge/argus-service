package com.nis.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.nis.domain.Page;
import com.nis.util.Configurations;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfReportEntity<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2990823464993846973L;
	@ApiModelProperty(value = "A系统", required = true)
	protected Long asum;
	@ApiModelProperty(value = "B系统", required = true)
	protected Long bsum;
	@ApiModelProperty(value = "C系统", required = true)
	protected Long csum;
	@ApiModelProperty(value = "统计时间", required = true)
	protected Date reportTime;

	@ApiModelProperty(value = "全A+单B", required = true)
	protected Long absum;

	public Long getAsum() {
		return asum;
	}

	public void setAsum(Long asum) {
		this.asum = asum;
	}

	public Long getBsum() {
		return bsum;
	}

	public void setBsum(Long bsum) {
		this.bsum = bsum;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	protected String searchReportStartTime;
	protected String searchReportEndTime;

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

	/**
	 * 当前实体分页对象
	 */
	protected Page<T> page;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	@JsonIgnore
	@XmlTransient
	public Page<T> getPage() {
		if (page == null) {
			page = new Page<T>();
		}
		return page;
	}

	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null) {
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	/**
	 * 获取数据库名称
	 */
	@JsonIgnore
	public String getDbName() {
		return Configurations.getStringProperty("jdbc.type", "mysql");
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Long getAbsum() {
		return absum;
	}

	public void setAbsum(Long absum) {
		this.absum = absum;
	}

	public Long getCsum() {
		return csum;
	}

	public void setCsum(Long csum) {
		this.csum = csum;
	}

}
