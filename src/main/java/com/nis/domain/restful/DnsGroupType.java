/**
 *@Title: DnsGroupType.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月3日 下午7:47:28
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
 * @ClassName: DnsGroupType.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月3日 下午7:47:28
 * @version V1.0
 */
public class DnsGroupType extends BaseEntity<DnsGroupType> {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7012538382953700633L;
	@ApiModelProperty(value="序号", required=true)
	private Long id;
	@ApiModelProperty(value="组号", required=true)
	private Integer groupId;
	@ApiModelProperty(value="组名", required=true)
	private String groupName;
	@ApiModelProperty(value="应答类型号", required=true)
	private Integer resTypeId;
	@ApiModelProperty(value="操作时间", required=true)
	private Date opTime;
	@ApiModelProperty(value="有效标志", required=true)
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
	
	/**
	 * groupName
	 * @return  groupName
	*/
	
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	 * resTypeId
	 * @return  resTypeId
	*/
	
	public Integer getResTypeId() {
		return resTypeId;
	}
	/**
	 * @param resTypeId the resTypeId to set
	 */
	public void setResTypeId(Integer resTypeId) {
		this.resTypeId = resTypeId;
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
	
}
