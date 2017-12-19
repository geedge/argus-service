package com.nis.domain.restful;

import java.io.Serializable;
import com.nis.domain.Page;
import com.nis.domain.ServicesRequestLog;

public class ServicesRequestLogBean extends ServicesRequestLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3111023683958691133L;
	private String opStartTime;
	private String opEndTime;
	private String requestStartTime;
	private String requestEndTime;
	private Page page;

	public String getOpStartTime() {
		return opStartTime;
	}

	public void setOpStartTime(String opStartTime) {
		this.opStartTime = opStartTime;
	}

	public String getOpEndTime() {
		return opEndTime;
	}

	public void setOpEndTime(String opEndTime) {
		this.opEndTime = opEndTime;
	}

	public String getRequestStartTime() {
		return requestStartTime;
	}

	public void setRequestStartTime(String requestStartTime) {
		this.requestStartTime = requestStartTime;
	}

	public String getRequestEndTime() {
		return requestEndTime;
	}

	public void setRequestEndTime(String requestEndTime) {
		this.requestEndTime = requestEndTime;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
