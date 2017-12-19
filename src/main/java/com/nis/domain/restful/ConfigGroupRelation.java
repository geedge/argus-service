/**   
* @Title: ConfigGroupRelation.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午10:14:39 
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
 * @ClassName: ConfigGroupRelation
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (darnell)
 * @date 2016年8月29日 下午10:14:39
 * @version V1.0
 */
public class ConfigGroupRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9021829042309605139L;
	@ApiModelProperty(value = "id", required = true)
	private Long id;
	@ApiModelProperty(value = "分组ID", required = true)
	private Long groupId;
	@ApiModelProperty(value = "编译ID", required = true)
	private Long compileId;
	@ApiModelProperty(value = "有效标志", required = true)
	private Integer isValid;
	@ApiModelProperty(value = "操作时间", required = true)
	private Date opTime;
	@ApiModelProperty(value = "本地更新时间", required = true)
	private Date lastUpdate;
	@ApiModelProperty(value = "版本序列号", required = true)
	private Long procSeq;

	@JsonIgnore
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            要设置的 groupId
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return compileId
	 */
	public Long getCompileId() {
		return compileId;
	}

	/**
	 * @param compileId
	 *            要设置的 compileId
	 */
	public void setCompileId(Long compileId) {
		this.compileId = compileId;
	}

	/**
	 * @return isValid
	 */
	public Integer getIsValid() {
		return isValid;
	}

	/**
	 * @param isValid
	 *            要设置的 isValid
	 */
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return opTime
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * @param opTime
	 *            要设置的 opTime
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@JsonIgnore
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@JsonIgnore
	public Long getProcSeq() {
		return procSeq;
	}

	public void setProcSeq(Long procSeq) {
		this.procSeq = procSeq;
	}

}
