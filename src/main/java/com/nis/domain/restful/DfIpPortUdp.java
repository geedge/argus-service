/**
 *@Title: DfIpPortUdp.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016�?9�?13�? 上午9:11:55
 *@version  版本�?
 */
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: DfIpPortUdp.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016�?9�?13�? 上午9:11:55
 * @version V1.0
 */
public class DfIpPortUdp extends BaseEntity<DfIpPortUdp>{
	/**
	 * @Fields serialVersionUID:TODO（用�?句话描述这个变量表示�?么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -6162892672391456265L;
	@ApiModelProperty(value="序号", required=true)
	private Long cfgId;
	@ApiModelProperty(value="地址类型", required=true)
	private Integer addrType;
	@ApiModelProperty(value="源IP", required=true)
	private String srcIp;
	@ApiModelProperty(value="源IP掩码", required=true)
	private String maskSrcIp;
	@ApiModelProperty(value="源端口", required=true)
	private String srcPort;
	@ApiModelProperty(value="源端口掩码", required=true)
	private String maskSrcPort;
	@ApiModelProperty(value="目的IP", required=true)
	private String dstIp;
	@ApiModelProperty(value="目的IP掩码", required=true)
	private String maskDstIp;
	@ApiModelProperty(value="目的端口", required=true)
	private String dstPort;
	@ApiModelProperty(value="目的端口掩码", required=true)
	private String maskDstPort;
	@ApiModelProperty(value="协议udp,固定为0", required=true)
	private Integer protocol;
	@ApiModelProperty(value="方向，0双向，1单向", required=true)
	private Integer direction;
	@ApiModelProperty(value="有效标志， 0无效，1有效", required=true)
	private Integer isValid;
	@ApiModelProperty(value="序号", required=true)
	private Date opTime;
	@ApiModelProperty(value="生效范围", required=true)
	private String effectiveRange;
	@ApiModelProperty(value="生效系统", required=true)
	private Integer activeSys;
	private Date lastUpdate;
	private Long procSeq;
	
	/**
	 * effectiveRange
	 * @return  effectiveRange
	*/
	
	public String getEffectiveRange() {
		return effectiveRange;
	}

	/**
	 * @param effectiveRange the effectiveRange to set
	 */
	public void setEffectiveRange(String effectiveRange) {
		this.effectiveRange = effectiveRange;
	}

	/**
	 * activeSys
	 * @return  activeSys
	*/
	
	public Integer getActiveSys() {
		return activeSys;
	}

	/**
	 * @param activeSys the activeSys to set
	 */
	public void setActiveSys(Integer activeSys) {
		this.activeSys = activeSys;
	}

	/**
	 * lastUpdate
	 * @return  lastUpdate
	*/
	@JsonIgnore
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * procSeq
	 * @return  procSeq
	*/
	@JsonIgnore
	public Long getProcSeq() {
		return procSeq;
	}

	/**
	 * @param procSeq the procSeq to set
	 */
	public void setProcSeq(Long procSeq) {
		this.procSeq = procSeq;
	}

	/* (non-Javadoc)
	 * @see com.nis.domain.BaseEntity#getId()
	 */
	@Override
	@JsonIgnore
	public Long getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	/* (non-Javadoc)
	 * @see com.nis.domain.BaseEntity#setId(java.lang.Long)
	 */
	@Override
	@JsonIgnore
	public void setId(Long id) {
		// TODO Auto-generated method stub
		super.setId(id);
	}

	public Long getCfgId() {
		return cfgId;
	}

	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}

	public Integer getAddrType() {
		return addrType;
	}

	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp == null ? null : srcIp.trim();
	}

	public String getMaskSrcIp() {
		return maskSrcIp;
	}

	public void setMaskSrcIp(String maskSrcIp) {
		this.maskSrcIp = maskSrcIp == null ? null : maskSrcIp.trim();
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort == null ? null : srcPort.trim();
	}

	public String getMaskSrcPort() {
		return maskSrcPort;
	}

	public void setMaskSrcPort(String maskSrcPort) {
		this.maskSrcPort = maskSrcPort == null ? null : maskSrcPort.trim();
	}

	public String getDstIp() {
		return dstIp;
	}

	public void setDstIp(String dstIp) {
		this.dstIp = dstIp == null ? null : dstIp.trim();
	}

	public String getMaskDstIp() {
		return maskDstIp;
	}

	public void setMaskDstIp(String maskDstIp) {
		this.maskDstIp = maskDstIp == null ? null : maskDstIp.trim();
	}

	public String getDstPort() {
		return dstPort;
	}

	public void setDstPort(String dstPort) {
		this.dstPort = dstPort == null ? null : dstPort.trim();
	}

	public String getMaskDstPort() {
		return maskDstPort;
	}

	public void setMaskDstPort(String maskDstPort) {
		this.maskDstPort = maskDstPort == null ? null : maskDstPort.trim();
	}

	public Integer getProtocol() {
		return protocol;
	}

	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
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
