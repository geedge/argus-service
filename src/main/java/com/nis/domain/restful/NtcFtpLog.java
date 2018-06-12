package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcFtpLog  extends LogEntity {

    private static final long serialVersionUID = -4856757288296929083L;

    @ApiModelProperty(value = "FTP链接", required = true)
    protected String ftpUrl;


    @ApiModelProperty(value = "FTP内容", required = true)
    protected String ftpContent;


    public String getFtpUrl() {
        return ftpUrl;
    }

    public void setFtpUrl(String ftpUrl) {
        this.ftpUrl = ftpUrl;
    }

    public String getFtpContent() {
        return ftpContent;
    }

    public void setFtpContent(String ftpContent) {
        this.ftpContent = ftpContent;
    }
}
