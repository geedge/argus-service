package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcPptpLog extends LogEntity {

    private static final long serialVersionUID = -1558260429830428665L;


    @ApiModelProperty(value="通道类型", required=true)
    protected Integer tunnelType;
    @ApiModelProperty(value="加密方式", required=true)
    protected Integer encryptMode;

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
