package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficAppFocusStatistic extends AppTrendEntity{

	private Date time;
	private Long sipNum;
	private Long dipNum;

	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Long getSipNum() {
		return sipNum;
	}
	public void setSipNum(Long sipNum) {
		this.sipNum = sipNum;
	}
	public Long getDipNum() {
		return dipNum;
	}
	public void setDipNum(Long dipNum) {
		this.dipNum = dipNum;
	}
}
