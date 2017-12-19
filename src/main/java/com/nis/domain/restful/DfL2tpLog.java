/**   
* @Title: DfL2tpLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月7日 下午1:23:30 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfL2tpLog 
* @Description: FTPXX日志
* @author (ddm)  
* @date 2016年9月7日 下午1:23:30 
* @version V1.0 
*/
public class DfL2tpLog extends LogEntity<DfL2tpLog>{
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -6210309297068552716L;
	
	@ApiModelProperty(value="通道类型", required=true)
	protected Integer tunnelType;
	@ApiModelProperty(value="加密方式", required=true)
	protected Integer encryptMode;
	@ApiModelProperty(value="用户名称", required=true)
	protected String chapName;
	@ApiModelProperty(value="内容类型", required=true)
	protected Integer contentType;
	
	public String getChapName() {
		return chapName;
	}
	public void setChapName(String chapName) {
		this.chapName = chapName;
	}
	protected String searchEncryptMode;
	
	@JsonIgnore
	public String getSearchEncryptMode() {
		return searchEncryptMode;
	}
	public void setSearchEncryptMode(String searchEncryptMode) {
		this.searchEncryptMode = searchEncryptMode;
	}

	public Integer getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(Integer tunnelType) {
		this.tunnelType = tunnelType;
	}
	public Integer getEncryptMode() {
		return encryptMode;
	}
	public void setEncryptMode(Integer encryptMode) {
		this.encryptMode = encryptMode;
	}
	public Integer getContentType() {
		return contentType;
	}
	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}
}
