package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcSshLog  extends LogEntity {

    private static final long serialVersionUID = -5032732529386809500L;


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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHostKey() {
        return hostKey;
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    public String getHostCookie() {
        return hostCookie;
    }

    public void setHostCookie(String hostCookie) {
        this.hostCookie = hostCookie;
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
