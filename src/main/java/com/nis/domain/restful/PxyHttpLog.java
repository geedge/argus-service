package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:PxyHttpLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月19日 下午5:22:30
 * @version V1.0
 */
public class PxyHttpLog  extends LogEntity<PxyHttpLog> {

	private static final long serialVersionUID = 6553069367645925554L;
	@ApiModelProperty(value="url地址", required=true)
    protected String url;
    @ApiModelProperty(value="请求行", required=true)
    protected String reqLine;
    @ApiModelProperty(value="应答行", required=true)
    protected String resLine;
    @ApiModelProperty(value="Cookie值", required=true)
    protected String cookie;
    @ApiModelProperty(value="Referer值", required=true)
    protected String referer;
    @ApiModelProperty(value="UA值", required=true)
    protected String userAgent;
    @ApiModelProperty(value="Content-Length值", required=true)
    protected String contentLen;
    @ApiModelProperty(value="Content-Type值", required=true)
    protected String contentType;
    @ApiModelProperty(value="set-Cookie值", required=true)
    protected String setCookie;
    @ApiModelProperty(value="原始请求头", required=true)
    protected String reqHeader;
    @ApiModelProperty(value="原始应答头", required=true)
    protected String respHeader;
    @ApiModelProperty(value="原始请求体", required=true)
    protected String reqBody;
    @ApiModelProperty(value="原始应答体", required=true)
    protected String respBody;
    @ApiModelProperty(value="域名", required=true)
    protected String website;
    
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
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
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
	public String getSetCookie() {
		return setCookie;
	}
	public void setSetCookie(String setCookie) {
		this.setCookie = setCookie;
	}
	public String getReqHeader() {
		return reqHeader;
	}
	public void setReqHeader(String reqHeader) {
		this.reqHeader = reqHeader;
	}
	public String getRespHeader() {
		return respHeader;
	}
	public void setRespHeader(String respHeader) {
		this.respHeader = respHeader;
	}
	public String getReqBody() {
		return reqBody;
	}
	public void setReqBody(String reqBody) {
		this.reqBody = reqBody;
	}
	public String getRespBody() {
		return respBody;
	}
	public void setRespBody(String respBody) {
		this.respBody = respBody;
	}
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}
}
