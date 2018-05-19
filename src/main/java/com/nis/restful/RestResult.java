/**   
* @Title: RestError.java 
* @Package com.nis.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月24日 上午11:36:30 
* @version V1.0   
*/
package com.nis.restful;

import org.springframework.http.HttpStatus;

/** 
* @ClassName: RestError 
* @Description: TODO(用于构建web服务的错误对象) 
* @author (darnell)  
* @date 2016年8月24日 上午11:36:30 
* @version V1.0 
*/
public class RestResult {
	
	/**
	 * http状态码
	 */
	private HttpStatus status;
	
	/**
	 * 业务编码
	 */
	private RestBusinessCode businessCode;
	
	 /**
     * 结果信息
     */
	private String msg;
	
	/**
	 * 请求来源
	 */
	private String fromUri;
	
	private Object data;
	/**
	 * A版：4 B版：2 C版：1
	 */
	private String activeSys;
	
	/**
	 * 日志数据来源本地：0  数据中心：1
	 */
	private String logSource;
	
	/**
	 * 追踪状态码
	 */
	private String traceCode;
	
	public RestResult(){
		
	}
	
	public RestResult(HttpStatus status, RestBusinessCode businessCode, String msg,String fromUri,String activeSys,String fromSign,String traceCode) {
		super();
		this.status = status;
		this.businessCode = businessCode;
		this.msg = msg;
		this.fromUri = fromUri;
		this.activeSys=activeSys;
		this.logSource=logSource;
		this.traceCode=traceCode;
	}
	
	
	 /** 
	* @return status 
	*/
	public HttpStatus getStatus() {
		return status;
	}

	/** 
	* @param status 要设置的 status 
	*/
	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	

	/** 
	* @return businessCode 
	*/
	public RestBusinessCode getBusinessCode() {
		return businessCode;
	}

	/** 
	* @param businessCode 要设置的 businessCode 
	*/
	public void setBusinessCode(RestBusinessCode businessCode) {
		this.businessCode = businessCode;
	}

	/** 
	* @return msg 
	*/
	public String getMsg() {
		return msg;
	}

	/** 
	* @param msg 要设置的 msg 
	*/
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/** 
	* @return fromUri 
	*/
	public String getFromUri() {
		return fromUri;
	}

	/** 
	* @param fromUri 要设置的 fromUri 
	*/
	public void setFromUri(String fromUri) {
		this.fromUri = fromUri;
	}
	
	public String getActiveSys() {
		return activeSys;
	}
	
	public void setActiveSys(String activeSys) {
		this.activeSys = activeSys;
	}

	public String getLogSource() {
		return logSource;
	}
	public void setLogSource(String logSource) {
		this.logSource = logSource;
	}
	
	/** 
	* @return data 
	*/
	public Object getData() {
		return data;
	}

	/** 
	* @param data 要设置的 data 
	*/
	public void setData(Object data) {
		this.data = data;
	}

	public String getTraceCode() {
		return traceCode;
	}

	public void setTraceCode(String traceCode) {
		this.traceCode = traceCode;
	}

	public String toString() {
	        return new StringBuilder().append("HttpStatus:").append(getStatus().value())
	                .append(" errorcode:")
	                .append(this.getBusinessCode().getValue())
	                .append(" errorReason:")
	                .append(this.getBusinessCode().getErrorReason())
	                .append(" errorMsg:")
	                .append(this.getMsg())
	                .append(" errorURL").append(this.getFromUri())
	                .toString();
	    }

}
