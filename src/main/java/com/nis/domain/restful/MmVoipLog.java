package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:MmVoipIpLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月16日 下午2:57:12
 * @version V1.0
 */
public class MmVoipLog  extends LogEntity<MmVoipLog> {

	private static final long serialVersionUID = -6131067803206270492L;
//	@ApiModelProperty(value="VOIP接收时间", required=true)
//  protected String recvTime;
    @ApiModelProperty(value="VOIP通话时长（秒）", required=true)
    protected String duation;
    @ApiModelProperty(value="VOIP协议", required=true)
    protected String protocol;
    @ApiModelProperty(value="主叫VOIP账号", required=true)
    protected String callingAccount;
    @ApiModelProperty(value="被叫VOIP账号", required=true)
    protected String calledAccount;
    @ApiModelProperty(value="VOIP主叫电信号码", required=true)
    protected String callingNumber;
    @ApiModelProperty(value="VOIP被叫电信号码", required=true)
    protected String calledNumber;
    @ApiModelProperty(value="主叫VOIP语音文件存放服务器IP", required=true)
    protected String fromToStoreIp;
    @ApiModelProperty(value="主叫VOIP语音文件存放服务器URL", required=true)
    protected String fromToStoreUrl;
    @ApiModelProperty(value="被叫VOIP语音文件存放服务器IP", required=true)
    protected String toFromStoreIp;
    @ApiModelProperty(value="被叫VOIP语音文件存放服务器URL", required=true)
    protected String toFromStoreUrl;
    @ApiModelProperty(value="节目ID", required=true)
    protected String pid;
    @ApiModelProperty(value="节目访问地址", required=true)
    protected String url;
    @ApiModelProperty(value="封堵现场片段路径", required=true)
    protected String logUri;
    @ApiModelProperty(value="封堵类型", required=true)
    protected Integer fdType;
    
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogUri() {
		return logUri;
	}
	public void setLogUri(String logUri) {
		this.logUri = logUri;
	}
	public Integer getFdType() {
		return fdType;
	}
	public void setFdType(Integer fdType) {
		this.fdType = fdType;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
	public String getFromToStoreIp() {
		return fromToStoreIp;
	}
	public void setFromToStoreIp(String fromToStoreIp) {
		this.fromToStoreIp = fromToStoreIp;
	}
	public String getFromToStoreUrl() {
		return fromToStoreUrl;
	}
	public void setFromToStoreUrl(String fromToStoreUrl) {
		this.fromToStoreUrl = fromToStoreUrl;
	}
	public String getToFromStoreIp() {
		return toFromStoreIp;
	}
	public void setToFromStoreIp(String toFromStoreIp) {
		this.toFromStoreIp = toFromStoreIp;
	}
	public String getToFromStoreUrl() {
		return toFromStoreUrl;
	}
	public void setToFromStoreUrl(String toFromStoreUrl) {
		this.toFromStoreUrl = toFromStoreUrl;
	}
}
