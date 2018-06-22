/**   
* @Title: NumRegion.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午10:46:00 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: NumRegion 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月29日 下午10:46:00 
* @version V1.0 
*/
public class NumRegion implements Serializable{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -4566003594954790608L;
	
	@ApiModelProperty(value="域ID", required=true)
	private Long regionId;
	@ApiModelProperty(value="分组ID", required=true)
	private Long groupId;
	
	@ApiModelProperty(value="业务表名称", required=true)
	private String tableName;
	
	@ApiModelProperty(value="数值下界", required=true)
	private Long lowBoundary;
	@ApiModelProperty(value="数值上界", required=true)
	private Long upBoundary;
	@ApiModelProperty(value="是否有效", required=true)
	private int isValid;
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
	* @return lowBoundary 
	*/
	public Long getLowBoundary() {
		return lowBoundary;
	}

	/** 
	* @param lowBoundary 要设置的 lowBoundary 
	*/
	public void setLowBoundary(Long lowBoundary) {
		this.lowBoundary = lowBoundary;
	}

	/** 
	* @return upBoundary 
	*/
	public Long getUpBoundary() {
		return upBoundary;
	}

	/** 
	* @param upBoundary 要设置的 upBoundary 
	*/
	public void setUpBoundary(Long upBoundary) {
		this.upBoundary = upBoundary;
	}

	/** 
	* @return isValid 
	*/
	public int getIsValid() {
		return isValid;
	}

	/** 
	* @param isValid 要设置的 isValid 
	*/
	public void setIsValid(int isValid) {
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
