package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName:NtcCollectVoipLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author zdx
 * @date 2018年11月5日 下午5:50:18
 * @version V1.0
 */
public class NtcCollectVoipLog implements Serializable{

	private static final long serialVersionUID = -8215121930207772717L;

	@ApiModelProperty(value = "节目ID", required = true)
	protected String pid;

	@ApiModelProperty(value = "发现时间", required = true)
	protected Date foundTime;
	@ApiModelProperty(value = "接收时间", required = true)
	protected Date recvTime;
	@ApiModelProperty(value = "处理机IP", required = true)
	protected String capIp;
	@ApiModelProperty(value = "VOIP协议", required = true)
	protected String voipProtocol;
	@ApiModelProperty(value = "RTP服务端ip地址", required = true)
	protected String rtpDIp;
	@ApiModelProperty(value = "RTP客户端ip地址", required = true)
	protected String rtpSIp;
	@ApiModelProperty(value = "RTP服务端端口", required = true)
	protected Integer rtpDPort;
	@ApiModelProperty(value = "RTP客户端端口", required = true)
	protected Integer rtpSPort;
	@ApiModelProperty(value = "主叫VOIP语音文件存放服务器IP", required = true)
	protected String fromToStoreIp;
	@ApiModelProperty(value = "主叫VOIP语音文件存放服务器URL", required = true)
	protected String fromToStoreUrl;
	@ApiModelProperty(value = "被叫VOIP语音文件存放服务器IP", required = true)
	protected String toFromStoreIp;
	@ApiModelProperty(value = "被叫VOIP语音文件存放服务器URL", required = true)
	protected String toFromStoreUrl;
	@ApiModelProperty(value = "VOIP通话时长（秒）", required = true)
	protected String duation;
	@ApiModelProperty(value = "SIP服务端ip地址", required = true)
	protected String sipDIp;
	@ApiModelProperty(value = "SIP客户端ip地址", required = true)
	protected String sipSIp;
	@ApiModelProperty(value = "SIP服务端端口", required = true)
	protected Integer sipDPort;
	@ApiModelProperty(value = "SIP客户端端口", required = true)
	protected Integer sipSPort;
	@ApiModelProperty(value = "SIP会话ID", required = true)
	protected String callId;
	@ApiModelProperty(value = "SIP请求URI", required = true)
	protected String requestUri;
	@ApiModelProperty(value = "SIP主叫VOIP账号", required = true)
	protected String callingAccount;
	@ApiModelProperty(value = "SIP被叫VOIP账号", required = true)
	protected String calledAccount;
	@ApiModelProperty(value = "SIP相应字段", required = true)
	protected String contacts;
	@ApiModelProperty(value = "SIP相应字段", required = true)
	protected String via;
	@ApiModelProperty(value = "SIP相应字段", required = true)
	protected String route;
	@ApiModelProperty(value = "SIP相应字段", required = true)
	protected String recordRoute;
	@ApiModelProperty(value = "SIP相应字段", required = true)
	protected String userAgent;
	@ApiModelProperty(value = "SIP相应字段", required = true)
	protected String server;
	
	protected String searchVoipProtocol;//协议类型
	protected String searchRtpDIp;//RTP服务端ip
	protected String searchRtpSIp;//RTP客户端ip
	protected String searchSipDIp;//SIP服务端ip
	protected String searchSipSIp;//SIP客户端ip
	protected String searchCapIp;//处理机IP
	protected String searchFoundStartTime;//开始发现时间
	protected String searchFoundEndTime;//结束发现时间
	
	public NtcCollectVoipLog(String pid, Date foundTime, Date recvTime,
			String capIp, String voipProtocol, String rtpDIp, String rtpSIp,
			Integer rtpDPort, Integer rtpSPort, String fromToStoreIp,
			String fromToStoreUrl, String toFromStoreIp, String toFromStoreUrl,
			String duation, String sipDIp, String sipSIp, Integer sipDPort,
			Integer sipSPort, String callId, String requestUri,
			String callingAccount, String calledAccount, String contacts,
			String via, String route, String recordRoute, String userAgent,
			String server) {
		super();
		this.pid = pid;
		this.foundTime = foundTime;
		this.recvTime = recvTime;
		this.capIp = capIp;
		this.voipProtocol = voipProtocol;
		this.rtpDIp = rtpDIp;
		this.rtpSIp = rtpSIp;
		this.rtpDPort = rtpDPort;
		this.rtpSPort = rtpSPort;
		this.fromToStoreIp = fromToStoreIp;
		this.fromToStoreUrl = fromToStoreUrl;
		this.toFromStoreIp = toFromStoreIp;
		this.toFromStoreUrl = toFromStoreUrl;
		this.duation = duation;
		this.sipDIp = sipDIp;
		this.sipSIp = sipSIp;
		this.sipDPort = sipDPort;
		this.sipSPort = sipSPort;
		this.callId = callId;
		this.requestUri = requestUri;
		this.callingAccount = callingAccount;
		this.calledAccount = calledAccount;
		this.contacts = contacts;
		this.via = via;
		this.route = route;
		this.recordRoute = recordRoute;
		this.userAgent = userAgent;
		this.server = server;
	}

	public NtcCollectVoipLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the voipProtocol
	 */
	public String getVoipProtocol() {
		return voipProtocol;
	}

	/**
	 * @param voipProtocol
	 *            the voipProtocol to set
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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getFoundTime() {
		return foundTime;
	}

	public void setFoundTime(Date foundTime) {
		this.foundTime = foundTime;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	public String getCapIp() {
		return capIp;
	}

	public void setCapIp(String capIp) {
		this.capIp = capIp;
	}

	public String getRtpDIp() {
		return rtpDIp;
	}

	public void setRtpDIp(String rtpDIp) {
		this.rtpDIp = rtpDIp;
	}

	public String getRtpSIp() {
		return rtpSIp;
	}

	public void setRtpSIp(String rtpSIp) {
		this.rtpSIp = rtpSIp;
	}

	public Integer getRtpDPort() {
		return rtpDPort;
	}

	public void setRtpDPort(Integer rtpDPort) {
		this.rtpDPort = rtpDPort;
	}

	public Integer getRtpSPort() {
		return rtpSPort;
	}

	public void setRtpSPort(Integer rtpSPort) {
		this.rtpSPort = rtpSPort;
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

	public String getSipDIp() {
		return sipDIp;
	}

	public void setSipDIp(String sipDIp) {
		this.sipDIp = sipDIp;
	}

	public String getSipSIp() {
		return sipSIp;
	}

	public void setSipSIp(String sipSIp) {
		this.sipSIp = sipSIp;
	}

	public Integer getSipDPort() {
		return sipDPort;
	}

	public void setSipDPort(Integer sipDPort) {
		this.sipDPort = sipDPort;
	}

	public Integer getSipSPort() {
		return sipSPort;
	}

	public void setSipSPort(Integer sipSPort) {
		this.sipSPort = sipSPort;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getRecordRoute() {
		return recordRoute;
	}

	public void setRecordRoute(String recordRoute) {
		this.recordRoute = recordRoute;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	@JsonIgnore
	public String getSearchVoipProtocol() {
		return searchVoipProtocol;
	}

	public void setSearchVoipProtocol(String searchVoipProtocol) {
		this.searchVoipProtocol = searchVoipProtocol;
	}
	@JsonIgnore
	public String getSearchRtpDIp() {
		return searchRtpDIp;
	}

	public void setSearchRtpDIp(String searchRtpDIp) {
		this.searchRtpDIp = searchRtpDIp;
	}
	@JsonIgnore
	public String getSearchRtpSIp() {
		return searchRtpSIp;
	}

	public void setSearchRtpSIp(String searchRtpSIp) {
		this.searchRtpSIp = searchRtpSIp;
	}
	@JsonIgnore
	public String getSearchSipDIp() {
		return searchSipDIp;
	}

	public void setSearchSipDIp(String searchSipDIp) {
		this.searchSipDIp = searchSipDIp;
	}
	@JsonIgnore
	public String getSearchSipSIp() {
		return searchSipSIp;
	}

	public void setSearchSipSIp(String searchSipSIp) {
		this.searchSipSIp = searchSipSIp;
	}
	@JsonIgnore
	public String getSearchCapIp() {
		return searchCapIp;
	}

	public void setSearchCapIp(String searchCapIp) {
		this.searchCapIp = searchCapIp;
	}
	@JsonIgnore
	public String getSearchFoundStartTime() {
		return searchFoundStartTime;
	}

	public void setSearchFoundStartTime(String searchFoundStartTime) {
		this.searchFoundStartTime = searchFoundStartTime;
	}

	@JsonIgnore
	public String getSearchFoundEndTime() {
		return searchFoundEndTime;
	}

	public void setSearchFoundEndTime(String searchFoundEndTime) {
		this.searchFoundEndTime = searchFoundEndTime;
	}
}
