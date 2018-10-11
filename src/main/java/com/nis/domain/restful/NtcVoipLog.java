package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * 	@ClassName:NtcVoipLog
 * 	@Description:TODO(这里用一句话描述这个类的作用)
 *	@author zdx
 *	@date 2018年10月9日 下午6:30:10
 *	@version V1.0
 */
public class NtcVoipLog  extends LogEntity<NtcVoipLog> {
	private static final long serialVersionUID = -1868408809534973479L;
    @ApiModelProperty(value="VOIP通话时长（秒）", required=true)
    protected String duation;
    @ApiModelProperty(value="VOIP协议", required=true)
    protected String voipProtocol;
    @ApiModelProperty(value="主叫VOIP账号", required=true)
    protected String callingAccount;
    @ApiModelProperty(value="被叫VOIP账号", required=true)
    protected String calledAccount;
    @ApiModelProperty(value="VOIP主叫电信号码", required=true)
    protected String callingNumber;
    @ApiModelProperty(value="VOIP被叫电信号码", required=true)
    protected String calledNumber;
	/**
	 * @return the voipProtocol
	 */
	public String getVoipProtocol() {
		return voipProtocol;
	}
	/**
	 * @param voipProtocol the voipProtocol to set
	 */
	public void setVoipProtocol(String voipProtocol) {
		this.voipProtocol = voipProtocol;
	}
	public String getDuation() {
		return duation;
	}
	public void setDuation(String duation) {
		this.duation = duation;
	}
	public String getCallingAccount() {
		return callingAccount;
	}
	public void setCallingAccount(String callingAccount) {
		this.callingAccount = callingAccount;
	}
	public String getCalledAccount() {
		return calledAccount;
	}
	public void setCalledAccount(String calledAccount) {
		this.calledAccount = calledAccount;
	}
	public String getCallingNumber() {
		return callingNumber;
	}
	public void setCallingNumber(String callingNumber) {
		this.callingNumber = callingNumber;
	}
	public String getCalledNumber() {
		return calledNumber;
	}
	public void setCalledNumber(String calledNumber) {
		this.calledNumber = calledNumber;
	}
}
