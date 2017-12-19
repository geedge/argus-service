/**   
* @Title: ControlLog.java 
* @Package com.nis.domain 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月15日 下午4:11:12 
* @version V1.0   
*/
package com.nis.domain;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: ControlLog 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月15日 下午4:11:12 
* @version V1.0 
*/
@ApiModel
public class ControlLog extends BaseEntity<ControlLog> {
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 7644628891063420679L;
	/** 
	*/ 
	@ApiModelProperty(value="域名", required=true)
	private String domain;
	@ApiModelProperty(value="标题", required=true)
	private String title;
	@ApiModelProperty(value="源IP", required=true)
	private String srcIp;
	@ApiModelProperty(value="应答IP", required=true)
	private String resIp;
	@ApiModelProperty(value="状态", required=true)
	private Integer status;
	@ApiModelProperty(value="操作时间", required=true)
	private Date optTime;
	/** 
	* @return domain 
	*/
	public String getDomain() {
		return domain;
	}
	/** 
	* @param domain 要设置的 domain 
	*/
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/** 
	* @return title 
	*/
	public String getTitle() {
		return title;
	}
	/** 
	* @param title 要设置的 title 
	*/
	public void setTitle(String title) {
		this.title = title;
	}
	/** 
	* @return srcIp 
	*/
	public String getSrcIp() {
		return srcIp;
	}
	/** 
	* @param srcIp 要设置的 srcIp 
	*/
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	/** 
	* @return resIp 
	*/
	public String getResIp() {
		return resIp;
	}
	/** 
	* @param resIp 要设置的 resIp 
	*/
	public void setResIp(String resIp) {
		this.resIp = resIp;
	}
	
	/** 
	* @return status 
	*/
	public Integer getStatus() {
		return status;
	}
	/** 
	* @param status 要设置的 status 
	*/
	public void setStatus(Integer status) {
		this.status = status;
	}
	/** 
	* @return optTime 
	*/
	public Date getOptTime() {
		return optTime;
	}
	/** 
	* @param optTime 要设置的 optTime 
	*/
	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}
	
	
	

}
