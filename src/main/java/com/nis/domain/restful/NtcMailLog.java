package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by darnell on 2018/6/11.
 */
public class NtcMailLog extends LogEntity{

    private static final long serialVersionUID = 2541894088824218908L;

    @ApiModelProperty(value="邮件协议类型", required=true)
    protected String mailProto;
    @ApiModelProperty(value="发件人", required=true)
    protected String mailFrom;
    @ApiModelProperty(value="收件人", required=true)
    protected String mailTo;
    @ApiModelProperty(value="主题", required=true)
    protected String subject;
    @ApiModelProperty(value="EML文件KEY", required=true)
    protected String emlKey;
    @ApiModelProperty(value="EML文件转储路径", required=true)
    protected String emlFile;

    public String getMailProto() {
        return mailProto;
    }

    public void setMailProto(String mailProto) {
        this.mailProto = mailProto;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmlFile() {
        return emlFile;
    }

    public void setEmlFile(String emlFile) {
        this.emlFile = emlFile;
    }
    
    /**
	 * @return the emlKey
	 */
	public String getEmlKey() {
		return emlKey;
	}
	
	/**
	 * @param emlKey the emlKey to set
	 */
	public void setEmlKey(String emlKey) {
		this.emlKey = emlKey;
	}
	
}
