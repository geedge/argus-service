/**
 *@Title: DnsFakeIp.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月3日 下午7:59:34
 *@version  版本号
 */
package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DnsFakeIp.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月3日 下午7:59:34
 * @version V1.0
 */
public class DnsFakeIp extends BaseEntity<DnsFakeIp> {
	
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -4761419421560750644L;
	@ApiModelProperty(value="序号", required=true)
	private Long id;
	@ApiModelProperty(value="组号", required=true)
	private Integer groupId;
	@ApiModelProperty(value="地址类型", required=true)
	private Integer addrType;
	@ApiModelProperty(value="源IP地址", required=true)
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
	@ApiModelProperty(value="协议(tcp/udp)", required=true)
	private Integer protocol;
	@ApiModelProperty(value="方向，0双向，1单向", required=true)
	private Integer direction;
	@ApiModelProperty(value="有效标志， 0无效，1有效", required=true)
	private Integer isValid;
	@ApiModelProperty(value="操作时间", required=true)
	private Date opTime;
	@ApiModelProperty(value="预留1", required=false)
	private Integer yl1;
	@ApiModelProperty(value="预留2", required=false)
	private Integer yl2;
	@ApiModelProperty(value="预留3", required=false)
	private String yl3;
	@ApiModelProperty(value="预留4", required=false)
	private String yl4;
	@ApiModelProperty(value="预留5", required=false)
	private String yl5;
	private Date lastUpdate;
	private Long procSeq;
	
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
	/**
	 * id
	 * @return  id
	*/
	
	public Long getId() {
		return id;
	}
	/**
	 * maskSrcIp
	 * @return  maskSrcIp
	*/
	
	public String getMaskSrcIp() {
		return maskSrcIp;
	}
	/**
	 * @param maskSrcIp the maskSrcIp to set
	 */
	public void setMaskSrcIp(String maskSrcIp) {
		this.maskSrcIp = maskSrcIp;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * groupId
	 * @return  groupId
	*/
	
	public Integer getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	/**
	 * addrType
	 * @return  addrType
	*/
	
	public Integer getAddrType() {
		return addrType;
	}
	/**
	 * @param addrType the addrType to set
	 */
	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}
	/**
	 * srcIp
	 * @return  srcIp
	*/
	
	public String getSrcIp() {
		return srcIp;
	}
	/**
	 * @param srcIp the srcIp to set
	 */
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	/**
	 * maskSrcPort
	 * @return  maskSrcPort
	*/
	
	public String getMaskSrcPort() {
		return maskSrcPort;
	}
	/**
	 * @param maskSrcPort the maskSrcPort to set
	 */
	public void setMaskSrcPort(String maskSrcPort) {
		this.maskSrcPort = maskSrcPort;
	}
	/**
	 * srcPort
	 * @return  srcPort
	*/
	
	public String getSrcPort() {
		return srcPort;
	}
	/**
	 * @param srcPort the srcPort to set
	 */
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	/**
	 * dstIp
	 * @return  dstIp
	*/
	
	public String getDstIp() {
		return dstIp;
	}
	/**
	 * @param dstIp the dstIp to set
	 */
	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}
	/**
	 * maskDstIp
	 * @return  maskDstIp
	*/
	
	public String getMaskDstIp() {
		return maskDstIp;
	}
	/**
	 * @param maskDstIp the maskDstIp to set
	 */
	public void setMaskDstIp(String maskDstIp) {
		this.maskDstIp = maskDstIp;
	}
	/**
	 * dstPort
	 * @return  dstPort
	*/
	
	public String getDstPort() {
		return dstPort;
	}
	/**
	 * @param dstPort the dstPort to set
	 */
	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}
	/**
	 * maskDstPort
	 * @return  maskDstPort
	*/
	
	public String getMaskDstPort() {
		return maskDstPort;
	}
	/**
	 * @param maskDstPort the maskDstPort to set
	 */
	public void setMaskDstPort(String maskDstPort) {
		this.maskDstPort = maskDstPort;
	}
	/**
	 * protocol
	 * @return  protocol
	*/
	
	public Integer getProtocol() {
		return protocol;
	}
	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	/**
	 * direction
	 * @return  direction
	*/
	
	public Integer getDirection() {
		return direction;
	}
	/**
	 * @param direction the direction to set
	 */
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	/**
	 * isValid
	 * @return  isValid
	*/
	
	public Integer getIsValid() {
		return isValid;
	}
	/**
	 * @param isValid the isValid to set
	 */
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	/**
	 * optTime
	 * @return  optTime
	*/
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}
	/**
	 * @param optTime the optTime to set
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	/**
	 * yl1
	 * @return  yl1
	*/
	
	public Integer getYl1() {
		return yl1;
	}
	/**
	 * @param yl1 the yl1 to set
	 */
	public void setYl1(Integer yl1) {
		this.yl1 = yl1;
	}
	/**
	 * yl2
	 * @return  yl2
	*/
	
	public Integer getYl2() {
		return yl2;
	}
	/**
	 * @param yl2 the yl2 to set
	 */
	public void setYl2(Integer yl2) {
		this.yl2 = yl2;
	}
	/**
	 * yl3
	 * @return  yl3
	*/
	
	public String getYl3() {
		return yl3;
	}
	/**
	 * @param yl3 the yl3 to set
	 */
	public void setYl3(String yl3) {
		this.yl3 = yl3;
	}
	/**
	 * yl4
	 * @return  yl4
	*/
	
	public String getYl4() {
		return yl4;
	}
	/**
	 * @param yl4 the yl4 to set
	 */
	public void setYl4(String yl4) {
		this.yl4 = yl4;
	}
	/**
	 * yl5
	 * @return  yl5
	*/
	
	public String getYl5() {
		return yl5;
	}
	/**
	 * @param yl5 the yl5 to set
	 */
	public void setYl5(String yl5) {
		this.yl5 = yl5;
	}
	
	
	
	
}
