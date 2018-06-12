package com.nis.restful;

import org.springframework.util.StringUtils;

import com.nis.web.service.SaveRequestLogThread;

public class RestServiceException extends RuntimeException{
	
	private int errorCode;
	
	private int logSource;
	
	private String traceCode;
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
		this.traceCode = thread.getTraceCode();
		thread.setConsumerTime(time);
		thread.setBusinessCode(this.getErrorCode());
		if(StringUtils.isEmpty(thread.getExceptionInfo())) {
            thread.setExceptionInfo(message);
        }

		new Thread(thread).start();
	}

	public RestServiceException(String message,int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	public RestServiceException(SaveRequestLogThread thread,long time,String message,int errorCode) {
		super(message);
		this.errorCode = errorCode;
		this.traceCode = thread.getTraceCode();
		thread.setConsumerTime(time);
		thread.setBusinessCode(this.getErrorCode());
		if(StringUtils.isEmpty(thread.getExceptionInfo())) {
            thread.setExceptionInfo(message);
        }

		new Thread(thread).start();
	}

	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

    public int getLogSource() {
        return logSource;
    }

    public void setLogSource(int logSource) {
        this.logSource = logSource;
    }

	public String getTraceCode() {
		return traceCode;
	}

	public void setTraceCode(String traceCode) {
		this.traceCode = traceCode;
	}
	
}
