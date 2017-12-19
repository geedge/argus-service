/**
 *@Title: DnsResponseStrategy.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月3日 下午7:01:02
 *@version  版本号
 */
package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DnsResponseStrategy.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月3日 下午7:01:02
 * @version V1.0
 */
@ApiModel

public class DnsResponseStrategy extends BaseEntity<DnsResponseStrategy> {

	
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -4484073380296557600L;
	@ApiModelProperty(value="序号", required=true)
	private Long id;
	@ApiModelProperty(value="请求策略号", required=true)
	private Integer reqStrateId;
	@ApiModelProperty(value="策略名称", required=true)
	private String strateName;
	@ApiModelProperty(value="应答组1的组号", required=false)
	private Integer resGroupOneId;
	@ApiModelProperty(value="应答组1的应答数", required=false)
	private Integer resGroupOneNum;
	@ApiModelProperty(value="应答组2的组号", required=false)
	private Integer resGroupTwoId;
	@ApiModelProperty(value="应答组2的应答数", required=false)
	private Integer resGroupTwoNum;
	@ApiModelProperty(value="应答组3的组号", required=false)
	private Integer resGroupThreeId;
	@ApiModelProperty(value="应答组3的应答数", required=false)
	private Integer resGroupThreeNum;
	@ApiModelProperty(value="应答组4的组号", required=false)
	private Integer resGroupFourId;
	@ApiModelProperty(value="应答组4的应答数", required=false)
	private Integer resGroupFourNum;
	@ApiModelProperty(value="应答组5的组号", required=false)
	private Integer resGroupFiveId;
	@ApiModelProperty(value="应答组5的应答数", required=false)
	private Integer resGroupFiveNum;
	@ApiModelProperty(value="Auth记录组号", required=false)
	private Integer authGroup;
	@ApiModelProperty(value="Additional记录组号", required=false)
	private Integer addGroup;
	@ApiModelProperty(value="操作时间", required=true)
	private Date opTime;
	@ApiModelProperty(value="有效标识", required=true)
	private Integer isValid;
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
	@ApiModelProperty(value="最小生存时间", required=true)
	private Integer minTtl;
	@ApiModelProperty(value="最大生存时间", required=true)
	private Integer maxTtl;
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
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * reqStrateId
	 * @return  reqStrateId
	*/
	
	public Integer getReqStrateId() {
		return reqStrateId;
	}
	/**
	 * @param reqStrateId the reqStrateId to set
	 */
	public void setReqStrateId(Integer reqStrateId) {
		this.reqStrateId = reqStrateId;
	}
	/**
	 * strateName
	 * @return  strateName
	*/
	
	public String getStrateName() {
		return strateName;
	}
	/**
	 * @param strateName the strateName to set
	 */
	public void setStrateName(String strateName) {
		this.strateName = strateName;
	}
	/**
	 * resGroupOneId
	 * @return  resGroupOneId
	*/
	
	public Integer getResGroupOneId() {
		return resGroupOneId;
	}
	/**
	 * @param resGroupOneId the resGroupOneId to set
	 */
	public void setResGroupOneId(Integer resGroupOneId) {
		this.resGroupOneId = resGroupOneId;
	}
	/**
	 * resGroupOneNum
	 * @return  resGroupOneNum
	*/
	
	public Integer getResGroupOneNum() {
		return resGroupOneNum;
	}
	/**
	 * @param resGroupOneNum the resGroupOneNum to set
	 */
	public void setResGroupOneNum(Integer resGroupOneNum) {
		this.resGroupOneNum = resGroupOneNum;
	}
	/**
	 * resGroupTwoId
	 * @return  resGroupTwoId
	*/
	
	public Integer getResGroupTwoId() {
		return resGroupTwoId;
	}
	/**
	 * @param resGroupTwoId the resGroupTwoId to set
	 */
	public void setResGroupTwoId(Integer resGroupTwoId) {
		this.resGroupTwoId = resGroupTwoId;
	}
	/**
	 * resGroupTwoNum
	 * @return  resGroupTwoNum
	*/
	
	public Integer getResGroupTwoNum() {
		return resGroupTwoNum;
	}
	/**
	 * @param resGroupTwoNum the resGroupTwoNum to set
	 */
	public void setResGroupTwoNum(Integer resGroupTwoNum) {
		this.resGroupTwoNum = resGroupTwoNum;
	}
	/**
	 * authGroup
	 * @return  authGroup
	*/
	
	public Integer getAuthGroup() {
		return authGroup;
	}
	/**
	 * @param authGroup the authGroup to set
	 */
	public void setAuthGroup(Integer authGroup) {
		this.authGroup = authGroup;
	}
	/**
	 * addGroup
	 * @return  addGroup
	*/
	
	public Integer getAddGroup() {
		return addGroup;
	}
	/**
	 * @param addGroup the addGroup to set
	 */
	public void setAddGroup(Integer addGroup) {
		this.addGroup = addGroup;
	}
	/**
	 * opTime
	 * @return  opTime
	*/
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}
	/**
	 * @param opTime the opTime to set
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
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
	/**
	 * minTtl
	 * @return  minTtl
	*/
	
	public Integer getMinTtl() {
		return minTtl;
	}
	/**
	 * @param minTtl the minTtl to set
	 */
	public void setMinTtl(Integer minTtl) {
		this.minTtl = minTtl;
	}
	/**
	 * maxTtl
	 * @return  maxTtl
	*/
	
	public Integer getMaxTtl() {
		return maxTtl;
	}
	/**
	 * @param maxTtl the maxTtl to set
	 */
	public void setMaxTtl(Integer maxTtl) {
		this.maxTtl = maxTtl;
	}
	/**
	 * resGroupThreeId
	 * @return  resGroupThreeId
	*/
	
	public Integer getResGroupThreeId() {
		return resGroupThreeId;
	}
	/**
	 * @param resGroupThreeId the resGroupThreeId to set
	 */
	public void setResGroupThreeId(Integer resGroupThreeId) {
		this.resGroupThreeId = resGroupThreeId;
	}
	/**
	 * resGroupThreeNum
	 * @return  resGroupThreeNum
	*/
	
	public Integer getResGroupThreeNum() {
		return resGroupThreeNum;
	}
	/**
	 * @param resGroupThreeNum the resGroupThreeNum to set
	 */
	public void setResGroupThreeNum(Integer resGroupThreeNum) {
		this.resGroupThreeNum = resGroupThreeNum;
	}
	/**
	 * resGroupFourId
	 * @return  resGroupFourId
	*/
	
	public Integer getResGroupFourId() {
		return resGroupFourId;
	}
	/**
	 * @param resGroupFourId the resGroupFourId to set
	 */
	public void setResGroupFourId(Integer resGroupFourId) {
		this.resGroupFourId = resGroupFourId;
	}
	/**
	 * resGroupFourNum
	 * @return  resGroupFourNum
	*/
	
	public Integer getResGroupFourNum() {
		return resGroupFourNum;
	}
	/**
	 * @param resGroupFourNum the resGroupFourNum to set
	 */
	public void setResGroupFourNum(Integer resGroupFourNum) {
		this.resGroupFourNum = resGroupFourNum;
	}
	/**
	 * resGroupFiveId
	 * @return  resGroupFiveId
	*/
	
	public Integer getResGroupFiveId() {
		return resGroupFiveId;
	}
	/**
	 * @param resGroupFiveId the resGroupFiveId to set
	 */
	public void setResGroupFiveId(Integer resGroupFiveId) {
		this.resGroupFiveId = resGroupFiveId;
	}
	/**
	 * resGroupFiveNum
	 * @return  resGroupFiveNum
	*/
	
	public Integer getResGroupFiveNum() {
		return resGroupFiveNum;
	}
	/**
	 * @param resGroupFiveNum the resGroupFiveNum to set
	 */
	public void setResGroupFiveNum(Integer resGroupFiveNum) {
		this.resGroupFiveNum = resGroupFiveNum;
	}
	
	

}
