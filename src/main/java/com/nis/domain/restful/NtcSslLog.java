package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcSslLog extends LogEntity {

    private static final long serialVersionUID = 164434539180653065L;


    @ApiModelProperty(value="版本信息", required=true)
    protected String version;

    @ApiModelProperty(value="Server Name Indication", required=true, notes = "主要解决一台服务器只能使用一个证书-域名的缺点，通过sni可支持一台服务器为多台域名提供服务")
    protected String sni;

    @ApiModelProperty(value="SAN-Subject Alternative Name Certificate", required=true, notes = "数字证书的一种形式，运行SSL证书绑定多个域名和子域名。另EV证书，扩展验证证书，只能绑定一个域名")
    protected String san;

    @ApiModelProperty(value="证书授权机构", required=true)
    protected String cn;

    @ApiModelProperty(value="SNI", required=true)
    protected String searchSni ;
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSni() {
        return sni;
    }

    public void setSni(String sni) {
        this.sni = sni;
    }

    public String getSan() {
        return san;
    }

    public void setSan(String san) {
        this.san = san;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
    
    /**
	 * @param searchSni the searchSni to set
	 */
	public void setSearchSni(String searchSni) {
		this.searchSni = searchSni;
	}
	
	/**
	 * @return the searchSni
	 */
	@JsonIgnore
	public String getSearchSni() {
		return searchSni;
	}
}
