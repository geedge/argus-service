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

}
