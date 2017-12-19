/**   
* @Title: DfOpenvpnLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月7日 下午1:57:20 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfOpenvpnLog 
* @Description: OPENVPNXX日志
* @author (ddm)  
* @date 2016年9月7日 下午2:06:45
* @version V1.0 
*/
public class DfOpenvpnLog extends LogEntity<DfOpenvpnLog>{
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -5331151634423058789L;
	
	@ApiModelProperty(value="版本信息", required=true)
	protected String version;
	@ApiModelProperty(value="加密方式", required=true)
	protected String encryptMode;
	@ApiModelProperty(value="是否有HMAC", required=true)
	protected Integer hmac;
	@ApiModelProperty(value="通道类型", required=true)
	protected Integer tunnelType;
	
	protected String searchEncryptMode;
	
	@JsonIgnore
	public String getSearchEncryptMode() {
		return searchEncryptMode;
	}
	public void setSearchEncryptMode(String searchEncryptMode) {
		this.searchEncryptMode = searchEncryptMode;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getEncryptMode() {
		return encryptMode;
	}
	public void setEncryptMode(String encryptMode) {
		this.encryptMode = encryptMode;
	}
	public Integer getHmac() {
		return hmac;
	}
	public void setHmac(Integer hmac) {
		this.hmac = hmac;
	}	
	public Integer getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(Integer tunnelType) {
		this.tunnelType = tunnelType;
	}
	
}
