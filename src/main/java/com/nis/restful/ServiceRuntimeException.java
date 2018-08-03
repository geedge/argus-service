package com.nis.restful;

import com.nis.web.service.AuditLogThread;
import org.springframework.util.StringUtils;

public class ServiceRuntimeException extends RuntimeException{
	
	private static final long serialVersionUID = -4340771633400022100L;

	private int errorCode;
	
	private int logSource;
	
	private String traceCode;
	
	public ServiceRuntimeException(){
	}
	
	public ServiceRuntimeException(AuditLogThread thread, long time, String message) {
		super(message);
		this.errorCode = RestBusinessCode.unknow_error.getValue(); //未知错误
		this.traceCode = thread.getTraceCode();
		thread.setConsumerTime(time);
		thread.setBusinessCode(this.getErrorCode());
		if(StringUtils.isEmpty(thread.getExceptionInfo())) {
            thread.setExceptionInfo(message);
        }

		new Thread(thread).start();
	}

	public ServiceRuntimeException(String message,int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	public ServiceRuntimeException(AuditLogThread thread, long time, String message, int errorCode) {
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
