package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class SystemFunStatus extends BaseEntity<SystemFunStatus> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3916377519679733752L;
	
	
	@ApiModelProperty(value="配置ID", required=true)
	private Long id;
	@ApiModelProperty(value="系统功能", required=true)
	private Long function;
	@ApiModelProperty(value="回传数据类型", required=true)
	private Long backData;
	@ApiModelProperty(value="生效范围", required=true)
	private String effectiveRange;
	@ApiModelProperty(value="生效系统", required=true)
	private Integer activeSys;
	@ApiModelProperty(value="有效标志", required=true)
	private Integer isValid;
	@ApiModelProperty(value="操作时间", required=true)
	private Date opTime;
	
	@ApiModelProperty(value="版本序列号")
	private Long procSeq;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public Long getProcSeq() {
		return procSeq;
	}
	public void setProcSeq(Long procSeq) {
		this.procSeq = procSeq;
	}
	
	public Long getFunction() {
		return function;
	}
	public void setFunction(Long function) {
		this.function = function;
	}
	
	public Long getBackData() {
		return backData;
	}
	public void setBackData(Long backData) {
		this.backData = backData;
	}
	
	public String getEffectiveRange() {
		return effectiveRange;
	}
	public void setEffectiveRange(String effectiveRange) {
		this.effectiveRange = effectiveRange;
	}
	
	public Integer getActiveSys() {
		return activeSys;
	}
	public void setActiveSys(Integer activeSys) {
		this.activeSys = activeSys;
	}
	
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	
}
