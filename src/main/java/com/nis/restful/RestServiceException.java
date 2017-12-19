package com.nis.restful;

import org.springframework.util.StringUtils;

import com.nis.web.service.SaveRequestLogThread;

public class RestServiceException extends RuntimeException{
	
	private int errorCode;
	
	private String logSource;
	
	private String activeSys;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4854901217206974677L;
	

	public RestServiceException(){
	}
	
	
//	public RestServiceException(String message) {  
//        super(message);
//        this.errorCode = 998; //未知错误
//    }
	public RestServiceException(SaveRequestLogThread thread,long time,String message) {  
		super(message);
		this.errorCode = 998; //未知错误
		thread.setConsumerTime(time);
		thread.setBusinessCode(this.getErrorCode());
		if(StringUtils.isEmpty(thread.getExceptionInfo()))
			thread.setExceptionInfo(message);
		new Thread(thread).start();
	}

	public RestServiceException(String message,int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	public RestServiceException(SaveRequestLogThread thread,long time,String message,int errorCode) {
		super(message);
		this.errorCode = errorCode;
		thread.setConsumerTime(time);
		thread.setBusinessCode(this.getErrorCode());
		if(StringUtils.isEmpty(thread.getExceptionInfo()))
			thread.setExceptionInfo(message);
		new Thread(thread).start();
	}

	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getLogSource() {
		return logSource;
	}
	public void setLogSource(String logSource) {
		this.logSource = logSource;
	}
	public String getActiveSys() {
		return activeSys;
	}
	public void setActiveSys(String activeSys) {
		this.activeSys = activeSys;
	}
	

}
