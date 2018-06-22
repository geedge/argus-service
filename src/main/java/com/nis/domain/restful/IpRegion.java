/**   
* @Title: StrRegion.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午10:29:45 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: StrRegion 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月29日 下午10:29:45 
* @version V1.0 
*/
public class IpRegion implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="域ID", required=true)
	private Long regionId;
	@ApiModelProperty(value="分组ID", required=true)
	private Long groupId;
	
	@ApiModelProperty(value="业务表名称", required=true)
	private String tableName;
	
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
	@ApiModelProperty(value="协议", required=true)
	private Integer protocol;
	@ApiModelProperty(value="方向", required=true)
	private Integer direction;
	@ApiModelProperty(value="有效标志", required=true)
	private Integer isValid;
	@ApiModelProperty(value="操作时间", required=true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date opTime;
	@ApiModelProperty(value="版本序列号", required=true)
	private Long procSeq;
	
	@ApiModelProperty(value = "本地更新时间	", required = true)
	private Date lastUpdate;
	
	
	@JsonIgnore
	public Long getProcSeq() {
		return procSeq;
	}

	public void setProcSeq(Long procSeq) {
		this.procSeq = procSeq;
	}
	@JsonIgnore
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/** 
	* @return regionId 
	*/
	public Long getRegionId() {
		return regionId;
	}

	/** 
	* @param regionId 要设置的 regionId 
	*/
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	/** 
	* @return groupId 
	*/
	public Long getGroupId() {
		return groupId;
	}

	/** 
	* @param groupId 要设置的 groupId 
	*/
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/** 
	* @return addrType 
	*/
	public Integer getAddrType() {
		return addrType;
	}

	/** 
	* @param addrType 要设置的 addrType 
	*/
	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}

	/** 
	* @return srcIp 
	*/
	public String getSrcIp() {
		return srcIp;
	}

	/** 
	* @param srcIp 要设置的 srcIp 
	*/
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	/** 
	* @return maskSrcIp 
	*/
	public String getMaskSrcIp() {
		return maskSrcIp;
	}

	/** 
	* @param maskSrcIp 要设置的 maskSrcIp 
	*/
	public void setMaskSrcIp(String maskSrcIp) {
		this.maskSrcIp = maskSrcIp;
	}

	/** 
	* @return srcPort 
	*/
	public String getSrcPort() {
		return srcPort;
	}

	/** 
	* @param srcPort 要设置的 srcPort 
	*/
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	/** 
	* @return maskSrcPort 
	*/
	public String getMaskSrcPort() {
		return maskSrcPort;
	}

	/** 
	* @param maskSrcPort 要设置的 maskSrcPort 
	*/
	public void setMaskSrcPort(String maskSrcPort) {
		this.maskSrcPort = maskSrcPort;
	}

	/** 
	* @return dstIp 
	*/
	public String getDstIp() {
		return dstIp;
	}

	/** 
	* @param dstIp 要设置的 dstIp 
	*/
	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}

	/** 
	* @return maskDstIp 
	*/
	public String getMaskDstIp() {
		return maskDstIp;
	}

	/** 
	* @param maskDstIp 要设置的 maskDstIp 
	*/
	public void setMaskDstIp(String maskDstIp) {
		this.maskDstIp = maskDstIp;
	}

	/** 
	* @return dstPort 
	*/
	public String getDstPort() {
		return dstPort;
	}

	/** 
	* @param dstPort 要设置的 dstPort 
	*/
	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}

	/** 
	* @return maskDstPort 
	*/
	public String getMaskDstPort() {
		return maskDstPort;
	}

	/** 
	* @param maskDstPort 要设置的 maskDstPort 
	*/
	public void setMaskDstPort(String maskDstPort) {
		this.maskDstPort = maskDstPort;
	}

	/** 
	* @return protocol 
	*/
	public Integer getProtocol() {
		return protocol;
	}

	/** 
	* @param protocol 要设置的 protocol 
	*/
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}

	/** 
	* @return direction 
	*/
	public Integer getDirection() {
		return direction;
	}

	/** 
	* @param direction 要设置的 direction 
	*/
	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	/** 
	* @return isValid 
	*/
	public Integer getIsValid() {
		return isValid;
	}

	/** 
	* @param isValid 要设置的 isValid 
	*/
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	/** 
	* @return opTime 
	*/
	public Date getOpTime() {
		return opTime;
	}

	/** 
	* @param opTime 要设置的 opTime 
	*/
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	
	
	

}
