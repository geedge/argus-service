/**   
* @Title: DjHttpReqLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （zbc）  
* @date 2016年9月5日 下午10:18:32 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DjHttpReqLog 
* @Description: HTTP协议监测请求日志
* @author (zbc)  
* @date 2016年9月5日 下午1:58:33 
* @version V1.0 
*/
public class DjHttpReqLog extends LogEntity<DjHttpReqLog>{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -2103649235895046586L;

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
	@ApiModelProperty(value="请求头转储文件", required=true)
	protected String reqHdrFile;	
	@ApiModelProperty(value="请求体转储文件", required=true)
	protected String reqBodyFile;
	
	@ApiModelProperty(value="Cookie值", required=true)
	protected String cookie;
	@ApiModelProperty(value="referer值", required=true)
	protected String referer;
	@ApiModelProperty(value="UA值", required=true)
	protected String ua;
	@ApiModelProperty(value="请求头用户自定义域名称", required=true)
	protected String reqUserDefineKey;
	@ApiModelProperty(value="请求头用户自定义域值", required=true)
	protected String reqUserDefineValue;
	
	
	protected String searchUrl;
	
	
	@JsonIgnore
	public String getSearchUrl() {
		return searchUrl;
	}
	
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
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
	public String getReqHdrFile() {
		return reqHdrFile;
	}
	public void setReqHdrFile(String reqHdrFile) {
		this.reqHdrFile = reqHdrFile;
	}
	public String getReqBodyFile() {
		return reqBodyFile;
	}
	public void setReqBodyFile(String reqBodyFile) {
		this.reqBodyFile = reqBodyFile;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getReqUserDefineKey() {
		return reqUserDefineKey;
	}

	public void setReqUserDefineKey(String reqUserDefineKey) {
		this.reqUserDefineKey = reqUserDefineKey;
	}

	public String getReqUserDefineValue() {
		return reqUserDefineValue;
	}

	public void setReqUserDefineValue(String reqUserDefineValue) {
		this.reqUserDefineValue = reqUserDefineValue;
	}
	
}
