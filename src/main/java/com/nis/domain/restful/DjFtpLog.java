/**   
* @Title: DjFtpLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （zbc）  
* @date 2016年9月7日 下午17:51:30 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DjFtpLog 
* @Description: FTPXX日志
* @author (zbc)  
* @date 2016年9月7日 下午17:51:30 
* @version V1.0 
*/
public class DjFtpLog extends LogEntity<DjFtpLog>{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 172160853794852639L;
	
	@ApiModelProperty(value="FTP链接", required=true)
	protected String ftpUrl;

	public String getFtpUrl() {
		return ftpUrl;
	}
	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}
}
