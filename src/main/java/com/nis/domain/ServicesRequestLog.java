/**
 *@Title: ServicesRequestLog.java
 *@Package com.nis.domain 
 *@Description TODO
 *@author dell
 *@date 2016年10月13日 下午3:39:14
 *@version  版本号
 */
package com.nis.domain;

import java.io.Serializable;
import java.util.Date;

import com.nis.util.JsonMapper;

/**
 * @ClassName: ServicesRequestLog.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年10月13日 下午3:39:14
 * @version V1.0
 */
public class ServicesRequestLog implements Serializable {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 3374409440640840339L;
	private Long id;
	private String operator;
	private String version;
	private Integer opAction;
	private Date opTime;
	private String requestContent;
	private Date requestTime;
	private Long consumerTime;
	private String requestIp;
	private Integer businessCode;
	private String exceptionInfo;
	private String serverIp;
	
	/**
	 * serverIp
	 * @return  serverIp
	*/
	
	public String getServerIp() {
		return serverIp;
	}
	/**
	 * @param serverIp the serverIp to set
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	/**
	 * businessCode
	 * @return  businessCode
	*/
	
	public Integer getBusinessCode() {
		return businessCode;
	}
	/**
	 * @param businessCode the businessCode to set
	 */
	public void setBusinessCode(Integer businessCode) {
		this.businessCode = businessCode;
	}
	/**
	 * id
	 * @return  id
	*/
	
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * operator
	 * @return  operator
	*/
	
	public String getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * version
	 * @return  version
	*/
	
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * opAction
	 * @return  opAction
	*/
	
	public Integer getOpAction() {
		return opAction;
	}
	/**
	 * @param opAction the opAction to set
	 */
	public void setOpAction(Integer opAction) {
		this.opAction = opAction;
	}
	/**
	 * opTime
	 * @return  opTime
	*/
	
	public Date getOpTime() {
		return opTime;
	}
	/**
	 * @param opTime the opTime to set
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	/**
	 * requestContent
	 * @return  requestContent
	*/
	
	public String getRequestContent() {
		return requestContent;
	}
	/**
	 * @param requestContent the requestContent to set
	 */
	public void setRequestContent(String requestContent) {
			this.requestContent = requestContent;
	}
	/**
	 * @param requestContent the requestContent to set
	 */
	public void setRequestContent(Object requestContent) {
		this.requestContent = JsonMapper.getInstance().toJsonString(requestContent);
	}
	/**
	 * requestTime
	 * @return  requestTime
	*/
	
	public Date getRequestTime() {
		return requestTime;
	}
	/**
	 * @param requestTime the requestTime to set
	 */
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	/**
	 * consumerTime
	 * @return  consumerTime
	*/
	
	public Long getConsumerTime() {
		return consumerTime;
	}
	/**
	 * @param consumerTime the consumerTime to set
	 */
	public void setConsumerTime(Long consumerTime) {
		this.consumerTime = consumerTime;
	}
	/**
	 * requestIp
	 * @return  requestIp
	*/
	
	public String getRequestIp() {
		return requestIp;
	}
	/**
	 * @param requestIp the requestIp to set
	 */
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	/**
	 * exceptionInfo
	 * @return  exceptionInfo
	*/
	
	public String getExceptionInfo() {
		return exceptionInfo;
	}
	/**
	 * @param exceptionInfo the exceptionInfo to set
	 */
	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}
	
}
