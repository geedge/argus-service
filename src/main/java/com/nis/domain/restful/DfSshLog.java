/**   
* @Title: DfSshLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月7日 下午2:21:10
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfSshLog 
* @Description: SSHXX日志
* @author (ddm)  
* @date 2016年9月7日 下午2:21:10
* @version V1.0 
*/
public class DfSshLog extends LogEntity<DfSshLog>{
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -8122097197353956263L;
	
	@ApiModelProperty(value="版本信息", required=true)
	protected String version;
	@ApiModelProperty(value="主机密钥", required=true)
	protected String hostKey;
	@ApiModelProperty(value="主机cookie", required=true)
	protected String hostCookie;
	@ApiModelProperty(value="加密方式", required=true)
	protected String encryptMode;
	@ApiModelProperty(value="消息认证码", required=true)
	protected String mac;
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
	public String getHostCookie() {
		return hostCookie;
	}
	public String getHostKey() {
		return hostKey;
	}
	public void setHostCookie(String hostCookie) {
		this.hostCookie = hostCookie;
	}
	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}
	public String getEncryptMode() {
		return encryptMode;
	}
	public void setEncryptMode(String encryptMode) {
		this.encryptMode = encryptMode;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Integer getTunnelType() {
		return tunnelType;
	}
	public void setTunnelType(Integer tunnelType) {
		this.tunnelType = tunnelType;
	}

	
}
