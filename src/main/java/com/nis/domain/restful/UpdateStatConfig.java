/**
 * 
 */
package com.nis.domain.restful;

import java.io.Serializable;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:CancleConfigSource
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年6月1日 下午6:08:09
 * @version V1.0
 */
public class UpdateStatConfig implements Serializable{

	private static final long serialVersionUID = 6346336602908201877L;
	@ApiModelProperty(value = "配置ID", required = true)
	private Long cfgId;
	@ApiModelProperty(value = "业务ID", required = true)
	private Integer service;
	@ApiModelProperty(value = "是否有效", required = true)
	private Integer isValid;
	
	/**
	 * 
	 */
	public UpdateStatConfig() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param cfgId
	 * @param service
	 * @param isValid
	 */
	public UpdateStatConfig(Long cfgId, Integer service, Integer isValid) {
		super();
		this.cfgId = cfgId;
		this.service = service;
		this.isValid = isValid;
	}
	public Long getCfgId() {
		return cfgId;
	}
	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}
	public Integer getService() {
		return service;
	}
	public void setService(Integer service) {
		this.service = service;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
}
