/**   
* @Title: DfKeyConvertUrl.java 
* @Package com.nis.domain.restful 
* @Description: 关键字业务转换URL日志
* @author （DDM）  
* @date 2016年9月27日 上午9:27:30 
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
* @ClassName: DfKeyConvertUrl 
* @Description: 关键字业务转换URL日志
* @author (DDM)  
* @date 2016年9月27日 上午9:27:30  
* @version V1.0 
*/
public class DfKeyConvertUrl implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 7985571037717090551L;
	
	@ApiModelProperty(value="序列号", required=true)
	protected Integer id;
	@ApiModelProperty(value="命中关键字ID", required=true)
	protected Integer keyId;
	@ApiModelProperty(value="URL地址", required=true)
	protected String url;
	@ApiModelProperty(value="操作时间", required=true)
	protected Date opTime;
	
	
	protected String searchId;
	protected String optStartTime;
	protected String optEndTime;
	
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
	@JsonIgnore
	public String getSearchId() {
		return searchId;
	}
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	
	
	/**
	 * 当前实体分页对象
	 */
	protected Page<DfKeyConvertUrl> page;
	
	
	@JsonIgnore
	@XmlTransient
	public Page<DfKeyConvertUrl> getPage() {
		if (page == null){
			page = new Page<DfKeyConvertUrl>();
		}
		return page;
	}
	
	public Page<DfKeyConvertUrl> setPage(Page<DfKeyConvertUrl> page) {
		this.page = page;
		return page;
	}	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getKeyId() {
		return keyId;
	}
	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
