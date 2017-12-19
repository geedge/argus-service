/**   
* @Title: DfHttpResLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月7日 上午9:59:30 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfHttpResLog 
* @Description: 协议响应XX日志
* @author (ddm)  
* @date 2016年9月7日 上午9:59:30 
* @version V1.0 
*/
public class DfHttpResLog extends LogEntity<DfHttpResLog>{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -6230615037776378250L;
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
	@ApiModelProperty(value="应答行", required=true)
	protected String resLine;
	@ApiModelProperty(value="应答头转储文件", required=true)
	protected String resHdrFile;
	@ApiModelProperty(value="应答体体转储文件", required=true)
	protected String resBodyFile;
	
	@ApiModelProperty(value="SET-Cookie", required=true)
	protected String setCookie;
	@ApiModelProperty(value="CONTENT-LEN值", required=true)
	protected String contentLen;
	@ApiModelProperty(value="CONTENT-TYPE值", required=true)
	protected String contentType;
	@ApiModelProperty(value="请求头用户自定义域名称", required=true)
	protected String resUserDefineKey;
	@ApiModelProperty(value="请求头用户自定义域值", required=true)
	protected String resUserDefineValue;
	
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
	public String getResLine() {
		return resLine;
	}
	public void setResLine(String resLine) {
		this.resLine = resLine;
	}
	public String getResHdrFile() {
		return resHdrFile;
	}
	public void setResHdrFile(String resHdrFile) {
		this.resHdrFile = resHdrFile;
	}
	public String getResBodyFile() {
		return resBodyFile;
	}
	public void setResBodyFile(String resBodyFile) {
		this.resBodyFile = resBodyFile;
	}

	public String getSetCookie() {
		return setCookie;
	}

	public void setSetCookie(String setCookie) {
		this.setCookie = setCookie;
	}

	public String getContentLen() {
		return contentLen;
	}

	public void setContentLen(String contentLen) {
		this.contentLen = contentLen;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getResUserDefineKey() {
		return resUserDefineKey;
	}

	public void setResUserDefineKey(String resUserDefineKey) {
		this.resUserDefineKey = resUserDefineKey;
	}

	public String getResUserDefineValue() {
		return resUserDefineValue;
	}

	public void setResUserDefineValue(String resUserDefineValue) {
		this.resUserDefineValue = resUserDefineValue;
	}

	
}
