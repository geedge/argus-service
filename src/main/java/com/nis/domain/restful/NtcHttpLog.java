package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcHttpLog extends LogEntity {

    private static final long serialVersionUID = 2553033624540656138L;

    @ApiModelProperty(value="url地址", required=true)
    protected String url;

    @ApiModelProperty(value="请求头转储文件", required=true)
    protected String reqHdrFile;

    @ApiModelProperty(value="请求体转储文件", required=true)
    protected String reqBodyFile;
    
    @ApiModelProperty(value="应答头转储文件", required=true)
    protected String resHdrFile;

    @ApiModelProperty(value="应答体转储文件", required=true)
    protected String resBodyFile;
    
    @ApiModelProperty(value="网站域名", required=true)
    protected String website;
    
    @ApiModelProperty(value="请求头转储文件KEY", required=true)
    protected String reqHdrKey;

    @ApiModelProperty(value="请求体转储文件KEY", required=true)
    protected String reqBodyKey;
    
    @ApiModelProperty(value="应答头转储文件KEY", required=true)
    protected String resHdrKey;

    @ApiModelProperty(value="应答体转储文件KEY", required=true)
    protected String resBodyKey;
    
    protected String searchUrl; //命中url
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	public String getReqHdrKey() {
		return reqHdrKey;
	}

	public void setReqHdrKey(String reqHdrKey) {
		this.reqHdrKey = reqHdrKey;
	}

	public String getReqBodyKey() {
		return reqBodyKey;
	}

	public void setReqBodyKey(String reqBodyKey) {
		this.reqBodyKey = reqBodyKey;
	}

	public String getResHdrKey() {
		return resHdrKey;
	}

	public void setResHdrKey(String resHdrKey) {
		this.resHdrKey = resHdrKey;
	}

	public String getResBodyKey() {
		return resBodyKey;
	}

	public void setResBodyKey(String resBodyKey) {
		this.resBodyKey = resBodyKey;
	}
	
	/**
	 * @return the searchUrl
	 */
	public String getSearchUrl() {
		return searchUrl;
	}
	
	/**
	 * @param searchUrl the searchUrl to set
	 */
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
}
