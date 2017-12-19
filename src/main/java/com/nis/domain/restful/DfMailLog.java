/**   
* @Title: DfMailLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月7日 上午10:16:30 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfMailLog 
* @Description: MAILXX日志
* @author (ddm)  
* @date 2016年9月7日 上午10:59:09 
* @version V1.0 
*/
public class DfMailLog extends LogEntity<DfMailLog>{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 4734386604389185710L;
	
	
	@ApiModelProperty(value="邮件协议类型", required=true)
	protected String mailProto;
	@ApiModelProperty(value="发件人", required=true)
	protected String mailFrom;
	@ApiModelProperty(value="收件人", required=true)
	protected String mailTo;
	@ApiModelProperty(value="主题", required=true)
	protected String subject;	
	@ApiModelProperty(value="EML文件转储路径", required=true)
	protected String emlFile;
	
	protected String searchMailFrom;
	protected String searchMailTo;
	
	@JsonIgnore
	public String getSearchMailFrom() {
		return searchMailFrom;
	}
	public void setSearchMailFrom(String searchMailFrom) {
		this.searchMailFrom = searchMailFrom;
	}
	public void setSearchMailTo(String searchMailTo) {
		this.searchMailTo = searchMailTo;
	}
	@JsonIgnore
	public String getSearchMailTo() {
		return searchMailTo;
	}
	
	public String getMailProto() {
		return mailProto;
	}
	public void setMailProto(String mailProto) {
		this.mailProto = mailProto;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
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

	
}
