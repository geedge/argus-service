package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcHttpLog extends LogEntity {

    private static final long serialVersionUID = 2553033624540656138L;

    @ApiModelProperty(value="TCP初始序列号", required=true)
    protected Long c2sIsn;

    @ApiModelProperty(value="是否HTTP代理标志", required=true)
    protected Long httpProxyFlag;

    @ApiModelProperty(value="HTTP会话序列号", required=true)
    protected Long httpSeq;

    @ApiModelProperty(value="url地址", required=true)
    protected String url;

    @ApiModelProperty(value="请求行", required=true)
    protected String reqLine;

    @ApiModelProperty(value="应答行", required=true)
    protected String resLine;


    public Long getC2sIsn() {
        return c2sIsn;
    }

    public void setC2sIsn(Long c2sIsn) {
        this.c2sIsn = c2sIsn;
    }

    public Long getHttpProxyFlag() {
        return httpProxyFlag;
    }

    public void setHttpProxyFlag(Long httpProxyFlag) {
        this.httpProxyFlag = httpProxyFlag;
    }

    public Long getHttpSeq() {
        return httpSeq;
    }

    public void setHttpSeq(Long httpSeq) {
        this.httpSeq = httpSeq;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReqLine() {
        return reqLine;
    }

    public void setReqLine(String reqLine) {
        this.reqLine = reqLine;
    }

    public String getResLine() {
        return resLine;
    }

    public void setResLine(String resLine) {
        this.resLine = resLine;
    }
}
