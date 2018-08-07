package com.nis.domain;

import java.util.Date;

public class SwaggerLog extends BaseEntity<SwaggerLog> {
	private static final long serialVersionUID = 5351369774420087482L;
	private String operator;
	private String version;
	private Integer opAction;
	private Date opTime;
	private String requestContent;
	private Date requestTime;
	private Long consumerTime;
	private String requestIp;
	private Integer businessCode;
	private String exceptionInfo;
	private String serverIp;
	private String traceCode;

	private String exception;// 界面的异常信息框

	private Date beginDate; // 开始日期
	private Date endDate; // 结束日期


	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getOpAction() {
		return opAction;
	}

	public void setOpAction(Integer opAction) {
		this.opAction = opAction;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Long getConsumerTime() {
		return consumerTime;
	}

	public void setConsumerTime(Long consumerTime) {
		this.consumerTime = consumerTime;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public Integer getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(Integer businessCode) {
		this.businessCode = businessCode;
	}

	public String getExceptionInfo() {
		return exceptionInfo;
	}

	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getTraceCode() {
		return traceCode;
	}

	public void setTraceCode(String traceCode) {
		this.traceCode = traceCode;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
