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

import com.fasterxml.jackson.annotation.JsonFormat;
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
	@ApiModelProperty(value = "分组ID", required = true)
	private Long groupId;
	@ApiModelProperty(value = "编译ID", required = true)
	private Long compileId;
	@ApiModelProperty(value = "有效标志", required = true)
	private Integer isValid;
	@ApiModelProperty(value = "操作时间", required = true)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date opTime;


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

}
