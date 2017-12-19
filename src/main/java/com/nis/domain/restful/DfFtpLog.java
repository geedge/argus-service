/**   
* @Title: DfFtpLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月7日 上午10:16:30 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfFtpLog 
* @Description: FTPXX日志
* @author (ddm)  
* @date 2016年9月7日 上午12:13:24 
* @version V1.0 
*/
public class DfFtpLog extends LogEntity<DfFtpLog>{

	/**
	 * 
	 */
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -3468485265584781756L;
	
	@ApiModelProperty(value="FTP链接", required=true)
	protected String ftpUrl;

	public String getFtpUrl() {
		return ftpUrl;
	}
	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}
}
