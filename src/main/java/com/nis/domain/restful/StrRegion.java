/**   
* @Title: IpRegion.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午10:36:06 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: IpRegion 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月29日 下午10:36:06 
* @version V1.0 
*/
public class StrRegion implements Serializable {
	
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
	@ApiModelProperty(value="匹配区域", required=true)
	private String district;
	@ApiModelProperty(value="关键字", required=true)
	private String keywords;
	@ApiModelProperty(value="表达式类型", required=true)
	private Integer exprType;
	@ApiModelProperty(value="匹配方式", required=true)
	private Integer matchMethod;
	@ApiModelProperty(value="是否HEX", required=true)
	private Integer isHexbin;
	@ApiModelProperty(value="是否有效", required=true)
	private int isValid;
	@ApiModelProperty(value="操作时间", required=true)
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
	* @return district 
	*/
	public String getDistrict() {
		return district;
	}
	/** 
	* @param district 要设置的 district 
	*/
	public void setDistrict(String district) {
		this.district = district;
	}
	/** 
	* @return keywords 
	*/
	public String getKeywords() {
		return keywords;
	}
	/** 
	* @param keywords 要设置的 keywords 
	*/
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/** 
	* @return exprType 
	*/
	public Integer getExprType() {
		return exprType;
	}
	/** 
	* @param exprType 要设置的 exprType 
	*/
	public void setExprType(Integer exprType) {
		this.exprType = exprType;
	}
	/** 
	* @return matchMethod 
	*/
	public Integer getMatchMethod() {
		return matchMethod;
	}
	/** 
	* @param matchMethod 要设置的 matchMethod 
	*/
	public void setMatchMethod(Integer matchMethod) {
		this.matchMethod = matchMethod;
	}
	/** 
	* @return isHexbin 
	*/
	public Integer getIsHexbin() {
		return isHexbin;
	}
	/** 
	* @param isHexbin 要设置的 isHexbin 
	*/
	public void setIsHexbin(Integer isHexbin) {
		this.isHexbin = isHexbin;
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
	@JsonSerialize(using=JsonDateSerializer.class)
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
