package com.nis.domain.restful;

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
	@ApiModelProperty(value = "c2s包数", required = true)
    protected String c2sPktNum;
    @ApiModelProperty(value = "s2c包数", required = true)
    protected String s2cPktNum;
    @ApiModelProperty(value = "c2s字节数", required = true)
    protected String c2sByteNum;
    @ApiModelProperty(value = "s2c字节数", required = true)
    protected String s2cByteNum;
	public String getAppLabel() {
		return appLabel;
	}
	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}
	public String getC2sPktNum() {
		return c2sPktNum;
	}
	public void setC2sPktNum(String c2sPktNum) {
		this.c2sPktNum = c2sPktNum;
	}
	public String getS2cPktNum() {
		return s2cPktNum;
	}
	public void setS2cPktNum(String s2cPktNum) {
		this.s2cPktNum = s2cPktNum;
	}
	public String getC2sByteNum() {
		return c2sByteNum;
	}
	public void setC2sByteNum(String c2sByteNum) {
		this.c2sByteNum = c2sByteNum;
	}
	public String getS2cByteNum() {
		return s2cByteNum;
	}
	public void setS2cByteNum(String s2cByteNum) {
		this.s2cByteNum = s2cByteNum;
	}
	
}
