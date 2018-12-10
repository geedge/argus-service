package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @description 配置日志TOP统计API
 * @author dell
 * @date 2018年12月5日11:22:21
 */

@ApiModel(value = "配置日志对象", description = "用来封装配置日志")
public class CfgLogInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6058402409885962868L;

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "业务码")
	private String serviceCode;// 业务码

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "业务中文名称")
	private String serviceNameZh;// 业务中文名称

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "业务英文名称")
	private String serviceNameEn;// 业务英文名称

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "业务俄文名称")
	private String serviceNameRu;// 业务俄文名称

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "日志量")
	private long sum;// 日志量

	@JsonInclude(value = Include.NON_NULL)
	@ApiModelProperty(value = "统计时间")
	private Date reportTime;// 五分钟统计时间

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceNameZh() {
		return serviceNameZh;
	}

	public void setServiceNameZh(String serviceNameZh) {
		this.serviceNameZh = serviceNameZh;
	}

	public String getServiceNameEn() {
		return serviceNameEn;
	}

	public void setServiceNameEn(String serviceNameEn) {
		this.serviceNameEn = serviceNameEn;
	}

	public String getServiceNameRu() {
		return serviceNameRu;
	}

	public void setServiceNameRu(String serviceNameRu) {
		this.serviceNameRu = serviceNameRu;
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
		return "CfgLogInfo [serviceCode=" + serviceCode + ", serviceNameZh=" + serviceNameZh + ", serviceNameEn="
				+ serviceNameEn + ", serviceNameRu=" + serviceNameRu + ", sum=" + sum + ", reportTime=" + reportTime
				+ "]";
	}

}
