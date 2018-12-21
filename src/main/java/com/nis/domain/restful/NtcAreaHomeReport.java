package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @description 地域流量API
 * @author dell
 * @date 2018年12月5日11:01:13
 *
 */

@ApiModel(value = "地域流量对象", description = "用来封装地域流量")
public class NtcAreaHomeReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3597060253861898787L;
	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "局点(出入口)")
	private int entranceId;// 局点(出入口)

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "地域")
	private String area;// 地域

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "接收字节数")
	private long sum;// 接收字节数

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "统计时间")
	private Date reportTime;// 统计时间	

	private long baseNum;
	private String searchReportStartTime;
	private String searchReportEndTime;

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
	public long getBaseNum() {
		return baseNum;
	}

	public void setBaseNum(long baseNum) {
		this.baseNum = baseNum;
	}

	public int getEntranceId() {
		return entranceId;
	}

	public void setEntranceId(int entranceId) {
		this.entranceId = entranceId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public long getSum() {
		return sum;
	}

	public void setSum(long sum) {
		this.sum = sum;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	@Override
	public String toString() {
		return "NtcAreaHomeReport [entranceId=" + entranceId + ", area=" + area + ", sum=" + sum + ", reportTime="
				+ reportTime + "]";
	}
}
