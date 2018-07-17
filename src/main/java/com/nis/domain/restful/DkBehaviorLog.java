package com.nis.domain.restful;

import java.util.Date;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:DkBehaviorLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月12日 下午5:09:21
 * @version V1.0
 */
public class DkBehaviorLog  extends LogEntity<DkBehaviorLog> {
	private static final long serialVersionUID = -589390375816690510L;
	@ApiModelProperty(value = "协议标签", required = true)
    protected Integer appLabel;
    @ApiModelProperty(value = "发送c2s包数", required = true)
    protected String c2sSendPktNum;
    @ApiModelProperty(value = "发送s2c包数", required = true)
    protected String s2cSendPktNum;
    @ApiModelProperty(value = "发送c2s字节数", required = true)
    protected String c2sSendByteNum;
    @ApiModelProperty(value = "发送s2c字节数", required = true)
    protected String s2cSendByteNum;
    @ApiModelProperty(value = "接收c2s包数", required = true)
    protected String c2sRecvSendPktNum;
    @ApiModelProperty(value = "接收s2c包数", required = true)
    protected String s2cRecvSendPktNum;
    @ApiModelProperty(value = "接收c2s字节数", required = true)
    protected String c2sRecvByteNum;
    @ApiModelProperty(value = "接收s2c字节数", required = true)
    protected String s2cRecvByteNum;
	public Integer getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(Integer appLabel) {
		this.appLabel = appLabel;
	}
	public String getC2sSendPktNum() {
		return c2sSendPktNum;
	}
	public void setC2sSendPktNum(String c2sSendPktNum) {
		this.c2sSendPktNum = c2sSendPktNum;
	}
	public String getS2cSendPktNum() {
		return s2cSendPktNum;
	}
	public void setS2cSendPktNum(String s2cSendPktNum) {
		this.s2cSendPktNum = s2cSendPktNum;
	}
	public String getC2sSendByteNum() {
		return c2sSendByteNum;
	}
	public void setC2sSendByteNum(String c2sSendByteNum) {
		this.c2sSendByteNum = c2sSendByteNum;
	}
	public String getS2cSendByteNum() {
		return s2cSendByteNum;
	}
	public void setS2cSendByteNum(String s2cSendByteNum) {
		this.s2cSendByteNum = s2cSendByteNum;
	}
	public String getC2sRecvSendPktNum() {
		return c2sRecvSendPktNum;
	}
	public void setC2sRecvSendPktNum(String c2sRecvSendPktNum) {
		this.c2sRecvSendPktNum = c2sRecvSendPktNum;
	}
	public String getS2cRecvSendPktNum() {
		return s2cRecvSendPktNum;
	}
	public void setS2cRecvSendPktNum(String s2cRecvSendPktNum) {
		this.s2cRecvSendPktNum = s2cRecvSendPktNum;
	}
	public String getC2sRecvByteNum() {
		return c2sRecvByteNum;
	}
	public void setC2sRecvByteNum(String c2sRecvByteNum) {
		this.c2sRecvByteNum = c2sRecvByteNum;
	}
	public String getS2cRecvByteNum() {
		return s2cRecvByteNum;
	}
	public void setS2cRecvByteNum(String s2cRecvByteNum) {
		this.s2cRecvByteNum = s2cRecvByteNum;
	}
}
