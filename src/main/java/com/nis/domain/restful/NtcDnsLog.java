package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;


/**
 * Created by darnell on 2018/6/11.
 */
public class NtcDnsLog extends LogEntity {

    private static final long serialVersionUID = -6980951963455847281L;

    @ApiModelProperty(value="递归请求", required=true)
    protected Integer rd;

    @ApiModelProperty(value="请求/应答", required=true)
    protected Integer qr;

    @ApiModelProperty(value="递归应答", required=true)
    protected Integer ra;

    @ApiModelProperty(value="资源记录", required=true)
    protected String rr;

    @ApiModelProperty(value="查询类型", required=true)
    protected Integer qType;

    @ApiModelProperty(value="查询类", required=true)
    protected Integer qclass;

    @ApiModelProperty(value="OPCODE", required=true)
    protected Integer opcode;

    @ApiModelProperty(value="查询内容", required=true)
    protected String qname;


    @ApiModelProperty(value="别名", required=true)
    protected String cname;

    @ApiModelProperty(value="DNS_SUB", required=true)
    protected Integer dnsSub ;

    public Integer getRd() {
        return rd;
    }

    public void setRd(Integer rd) {
        this.rd = rd;
    }

    public Integer getQr() {
        return qr;
    }

    public void setQr(Integer qr) {
        this.qr = qr;
    }

    public Integer getRa() {
        return ra;
    }

    public void setRa(Integer ra) {
        this.ra = ra;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }

    public Integer getqType() {
        return qType;
    }

    public void setqType(Integer qType) {
        this.qType = qType;
    }

    public Integer getQclass() {
        return qclass;
    }

    public void setQclass(Integer qclass) {
        this.qclass = qclass;
    }

    public Integer getOpcode() {
        return opcode;
    }

    public void setOpcode(Integer opcode) {
        this.opcode = opcode;
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getDnsSub() {
        return dnsSub;
    }

    public void setDnsSub(Integer dnsSub) {
        this.dnsSub = dnsSub;
    }
}
