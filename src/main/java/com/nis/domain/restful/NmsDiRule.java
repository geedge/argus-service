package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NmsDiRule implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private long id;
	private long detectionInfoId;
	private long serviceIndex;
	private long serviceCode;
	private String serviceDesc;
	private long agedTime;
	private String clientNum;
	private String refluxPort;
	private String ruleNumber;
	private String usedRuleNum;
	private String leftRuleNum;
	private String hitTotalNum;
	private String detectionedState;
	private long seqId;
	private long detectionSetInfoId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "dataCheckTime", notes = "")
	private Date dataCheckTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "dataArriveTime", notes = "")
	private Date dataArriveTime;
	private long dataCheckTimeDigital;
	private long dataArriveTimeDigital;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDetectionInfoId() {
		return detectionInfoId;
	}

	public void setDetectionInfoId(long detectionInfoId) {
		this.detectionInfoId = detectionInfoId;
	}

	public long getServiceIndex() {
		return serviceIndex;
	}

	public void setServiceIndex(long serviceIndex) {
		this.serviceIndex = serviceIndex;
	}

	public long getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(long serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public long getAgedTime() {
		return agedTime;
	}

	public void setAgedTime(long agedTime) {
		this.agedTime = agedTime;
	}

	public String getClientNum() {
		return clientNum;
	}

	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}

	public String getRefluxPort() {
		return refluxPort;
	}

	public void setRefluxPort(String refluxPort) {
		this.refluxPort = refluxPort;
	}

	public String getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(String ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public String getUsedRuleNum() {
		return usedRuleNum;
	}

	public void setUsedRuleNum(String usedRuleNum) {
		this.usedRuleNum = usedRuleNum;
	}

	public String getLeftRuleNum() {
		return leftRuleNum;
	}

	public void setLeftRuleNum(String leftRuleNum) {
		this.leftRuleNum = leftRuleNum;
	}

	public String getHitTotalNum() {
		return hitTotalNum;
	}

	public void setHitTotalNum(String hitTotalNum) {
		this.hitTotalNum = hitTotalNum;
	}

	public String getDetectionedState() {
		return detectionedState;
	}

	public void setDetectionedState(String detectionedState) {
		this.detectionedState = detectionedState;
	}

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public long getDetectionSetInfoId() {
		return detectionSetInfoId;
	}

	public void setDetectionSetInfoId(long detectionSetInfoId) {
		this.detectionSetInfoId = detectionSetInfoId;
	}

	public Date getDataCheckTime() {
		return dataCheckTime;
	}

	public void setDataCheckTime(Date dataCheckTime) {
		this.dataCheckTime = dataCheckTime;
	}

	public Date getDataArriveTime() {
		return dataArriveTime;
	}

	public void setDataArriveTime(Date dataArriveTime) {
		this.dataArriveTime = dataArriveTime;
	}

	public long getDataCheckTimeDigital() {
		return dataCheckTimeDigital;
	}

	public void setDataCheckTimeDigital(long dataCheckTimeDigital) {
		this.dataCheckTimeDigital = dataCheckTimeDigital;
	}

	public long getDataArriveTimeDigital() {
		return dataArriveTimeDigital;
	}

	public void setDataArriveTimeDigital(long dataArriveTimeDigital) {
		this.dataArriveTimeDigital = dataArriveTimeDigital;
	}

}
