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
    protected String appLabel;
    @ApiModelProperty(value = "发送c2s包数", required = true)
    protected String c2sSendPktNum;
    @ApiModelProperty(value = "发送s2c包数", required = true)
    protected String s2cSendPktNum;
    @ApiModelProperty(value = "发送c2s字节数", required = true)
    protected String c2sSendByteNum;
    @ApiModelProperty(value = "发送s2c字节数", required = true)
    protected String s2cSendByteNum;
    @ApiModelProperty(value = "接收c2s包数", required = true)
    protected String c2sRecvPktNum;
    @ApiModelProperty(value = "接收s2c包数", required = true)
    protected String s2cRecvPktNum;
    @ApiModelProperty(value = "接收c2s字节数", required = true)
    protected String c2sRecvByteNum;
    @ApiModelProperty(value = "接收s2c字节数", required = true)
    protected String s2cRecvByteNum;
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appLabel) {
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
	public String getC2sRecvPktNum() {
		return c2sRecvPktNum;
	}
	public void setC2sRecvPktNum(String c2sRecvPktNum) {
		this.c2sRecvPktNum = c2sRecvPktNum;
	}
	public String getS2cRecvPktNum() {
		return s2cRecvPktNum;
	}
	public void setS2cRecvPktNum(String s2cRecvPktNum) {
		this.s2cRecvPktNum = s2cRecvPktNum;
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
