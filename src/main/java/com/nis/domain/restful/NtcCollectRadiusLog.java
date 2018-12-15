package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * 	@ClassName:NtcCollectRadiusLog
 * 	@Description:TODO(这里用一句话描述这个类的作用)
 *	@author zdx
 *	@date 2018年12月12日 下午2:00:16
 *	@version V1.0
 */
public class NtcCollectRadiusLog  extends LogEntity<NtcCollectRadiusLog> {
	
	private static final long serialVersionUID = 6757490542745612556L;
	@ApiModelProperty(value="报文类型", required=true)
    protected Integer code;
    @ApiModelProperty(value="ISN接入的IP", required=true)
    protected String nasIp;
    @ApiModelProperty(value="服务器下发的用户地址", required=true)
    protected String framedIp;
    @ApiModelProperty(value="用户名", required=true)
    protected String account;
    
    protected String searchNasIp;//ISN接入的IP
	protected String searchFramedIp;//服务器下发的用户地址
	protected String searchAccount;//用户名
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getNasIp() {
		return nasIp;
	}
	public void setNasIp(String nasIp) {
		this.nasIp = nasIp;
	}
	public String getFramedIp() {
		return framedIp;
	}
	public void setFramedIp(String framedIp) {
		this.framedIp = framedIp;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	@JsonIgnore
	public String getSearchNasIp() {
		return searchNasIp;
	}
	public void setSearchNasIp(String searchNasIp) {
		this.searchNasIp = searchNasIp;
	}
	@JsonIgnore
	public String getSearchFramedIp() {
		return searchFramedIp;
	}
	public void setSearchFramedIp(String searchFramedIp) {
		this.searchFramedIp = searchFramedIp;
	}
	@JsonIgnore
	public String getSearchAccount() {
		return searchAccount;
	}
	public void setSearchAccount(String searchAccount) {
		this.searchAccount = searchAccount;
	}
	
    
}
