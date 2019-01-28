package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 
 * @ClassName:NtcConnRecordLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author rkg
 * @date 2018年12月12日 下午2:00:16
 * @version V1.0
 */
public class NtcConnRecordPercent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bps;
	private String bpsPercent;
	private String pps;
	private String ppsPercent;
	private String searchFoundStartTime;// 开始发现时间
	private String searchFoundEndTime;// 结束发现时间
	private String searchEntranceId;
	protected String searchIspCode;// 运营商

	public String getBps() {
		return bps;
	}

	public void setBps(String bps) {
		this.bps = bps;
	}

	public String getBpsPercent() {
		return bpsPercent;
	}

	public void setBpsPercent(String bpsPercent) {
		this.bpsPercent = bpsPercent;
	}

	public String getPps() {
		return pps;
	}

	public void setPps(String pps) {
		this.pps = pps;
	}

	public String getPpsPercent() {
		return ppsPercent;
	}

	public void setPpsPercent(String ppsPercent) {
		this.ppsPercent = ppsPercent;
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
	@JsonIgnore
	public String getSearchEntranceId() {
		return searchEntranceId;
	}

	public void setSearchEntranceId(String searchEntranceId) {
		this.searchEntranceId = searchEntranceId;
	}
	@JsonIgnore
	public String getSearchIspCode() {
		return searchIspCode;
	}

	public void setSearchIspCode(String searchIspCode) {
		this.searchIspCode = searchIspCode;
	}
}
