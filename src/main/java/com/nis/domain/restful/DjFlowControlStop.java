/**   
* @Title: DjFlowControlStop.java 
* @Package com.nis.domain.restful 
* @Description: 监测规则流控实体
* @author （zbc）  
* @date 2016年9月8日 下午17:51:30 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.Page;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DjFlowControlStop 
* @Description: 监测规则流控实体
* @author (zbc)  
* @date 2016年9月8日 下午17:51:30 
* @version V1.0 
*/
public class DjFlowControlStop implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 5092754867586897432L;
	
	
	@ApiModelProperty(value="配置ID", required=true)
	protected Long cfgId;
	@ApiModelProperty(value="业务", required=true)
	protected Integer service;
	@ApiModelProperty(value="动作", required=true)
	protected Integer action;
	@ApiModelProperty(value="操作时间", required=true)
	protected Date opTime;
	
	
	/*protected String searchCfgId;*/
	protected String optStartTime;
	protected String optEndTime;
	
	/*@JsonIgnore
	public String getSearchCfgId() {
		return searchCfgId;
	}
	public void setSearchCfgId(String searchCfgId) {
		this.searchCfgId = searchCfgId;
	}*/
	@JsonIgnore
	public String getOptStartTime() {
		return optStartTime;
	}
	public void setOptStartTime(String optStartTime) {
		this.optStartTime = optStartTime;
	}
	
	@JsonIgnore
	public String getOptEndTime() {
		return optEndTime;
	}
	public void setOptEndTime(String optEndTime) {
		this.optEndTime = optEndTime;
	}
	
	
	
	
	/**
	 * 当前实体分页对象
	 */
	protected Page<DjFlowControlStop> page;
	
	
	@JsonIgnore
	@XmlTransient
	public Page<DjFlowControlStop> getPage() {
		if (page == null){
			page = new Page<DjFlowControlStop>();
		}
		return page;
	}
	
	public Page<DjFlowControlStop> setPage(Page<DjFlowControlStop> page) {
		this.page = page;
		return page;
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
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	
	
	

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
	
}
