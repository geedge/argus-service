/**   
* @Title: DfConfigCompile.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午9:36:28 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nis.util.JsonDateDeserializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: ConfigCompileStartStop
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (rkg)
 * @date 2018年12月22日 下午4:36:28
 * @version V1.0
 */
public class ConfigCompileStartStop implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 546753235262247839L;
	@ApiModelProperty(value = "配置ID", required = true)
	private Long compileId;
	@ApiModelProperty(value = "有效标志", required = true)
	private Integer isValid;
	@ApiModelProperty(value = "业务类型", required = true)
	private Integer service;
	@ApiModelProperty(value = "操作时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date opTime;

	public Long getCompileId() {
		return compileId;
	}

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
	 * @param isValid 要设置的 isValid
	 */
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	
	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	/**
	 * @return opTime
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * @param opTime 要设置的 opTime
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

}
