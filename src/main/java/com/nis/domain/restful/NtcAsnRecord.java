package com.nis.domain.restful;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @ClassName:NtcAsnRecord
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author rkg
 * @date 2018年12月12日 下午2:00:16
 * @version V1.0
 */
public class NtcAsnRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bps;
	private String pps;
	private String sAsn;
	private String dAsn;
	private String searchFoundStartTime;// 开始发现时间
	private String searchFoundEndTime;// 结束发现时间

	public String getBps() {
		return bps;
	}

	public void setBps(String bps) {
		this.bps = bps;
	}

	public String getPps() {
		return pps;
	}

	public void setPps(String pps) {
		this.pps = pps;
	}

	public String getsAsn() {
		return sAsn;
	}

	public void setsAsn(String sAsn) {
		this.sAsn = sAsn;
	}

	public String getdAsn() {
		return dAsn;
	}

	public void setdAsn(String dAsn) {
		this.dAsn = dAsn;
	}

	@JsonIgnore
	public String getSearchFoundStartTime() {
		return searchFoundStartTime;
	}

	public void setSearchFoundStartTime(String searchFoundStartTime) {
		this.searchFoundStartTime = searchFoundStartTime;
	}

	@JsonIgnore
	public String getSearchFoundEndTime() {
		return searchFoundEndTime;
	}

	public void setSearchFoundEndTime(String searchFoundEndTime) {
		this.searchFoundEndTime = searchFoundEndTime;
	}
}
