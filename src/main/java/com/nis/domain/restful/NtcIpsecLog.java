package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcIpsecLog extends LogEntity {

    private static final long serialVersionUID = 7413267107457939517L;


    @ApiModelProperty(value="交换协议", required=true)
    protected Integer exProtocol;
    @ApiModelProperty(value="ISAKMP模式", required=true)
    protected Integer isakmpMode;

    public Integer getExProtocol() {
        return exProtocol;
    }

    public void setExProtocol(Integer exProtocol) {
        this.exProtocol = exProtocol;
    }

    public Integer getIsakmpMode() {
        return isakmpMode;
    }

    public void setIsakmpMode(Integer isakmpMode) {
        this.isakmpMode = isakmpMode;
    }
}
