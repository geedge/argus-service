package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcOpenvpnLog  extends LogEntity {

    private static final long serialVersionUID = 1114102412562665190L;


    @ApiModelProperty(value="版本信息", required=true)
    protected String version;
    @ApiModelProperty(value="加密方式", required=true)
    protected String encryptMode;
    @ApiModelProperty(value="是否有HMAC", required=true)
    protected Integer hmac;
    @ApiModelProperty(value="通道类型", required=true)
    protected Integer tunnelType;

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
