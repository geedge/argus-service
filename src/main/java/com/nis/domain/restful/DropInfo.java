package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @description 丢弃量统计API
 * @author dell
 * @date 2018年12月5日11:22:21
 */

@ApiModel(value = "丢弃量对象", description = "用来封装丢弃量")
public class DropInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6058402409885962868L;

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "动作")
	private String label;// 动作

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "接收字节数")
	private long sum;// 接收字节数

	@JsonInclude()
	@ApiModelProperty(value = "统计时间")
	private Date reportTime;// 五分钟统计时间

	private long baseNum;
	private String searchReportStartTime;
	private String searchReportEndTime;
	private int searchBusinessType;


	@JsonIgnore
	public int getSearchBusinessType() {
		return searchBusinessType;
	}

	public void setSearchBusinessType(int searchBusinessType) {
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
	public long getBaseNum() {
		return baseNum;
	}

	public void setBaseNum(long baseNum) {
		this.baseNum = baseNum;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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
		return "DropInfo [label=" + label + ", sum=" + sum + ", reportTime=" + reportTime + "]";
	}

}
