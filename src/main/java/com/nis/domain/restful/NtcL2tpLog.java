package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcL2tpLog extends LogEntity {

    private static final long serialVersionUID = -3793435902270137597L;


    @ApiModelProperty(value="通道类型", required=true)
    protected Integer tunnelType;
    @ApiModelProperty(value="加密方式", required=true)
    protected Integer encryptMode;
    @ApiModelProperty(value="用户名称", required=true)
    protected String chapName;

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

    public String getChapName() {
        return chapName;
    }

    public void setChapName(String chapName) {
        this.chapName = chapName;
    }
}
