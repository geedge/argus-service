/**   
* @Title: DjPptpLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author (zbc)  
* @date 2016年9月7日 下午18:02:30
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DjPptpLog 
* @Description: FTPXX日志
* @author (zbc)  
* @date 2016年9月7日 下午18:02:30 
* @version V1.0 
*/
public class DjPptpLog extends LogEntity<DjPptpLog>{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 4819972774275203145L;
	
	@ApiModelProperty(value="通道类型", required=true)
	protected Integer tunnelType;
	@ApiModelProperty(value="加密方式", required=true)
	protected Integer encryptMode;
	
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
	
	
	
}
