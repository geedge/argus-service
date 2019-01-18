package com.nis.domain.restful.dashboard;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TrafficAppFocusStatistic extends AppTrendEntity{

	private Date time;
	private Long sipNum;
	private Long dipNum;
	
	private String searchIspCode;// 运营商
	private String ispNum;// device_id ,entrance_id 拼接

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
	public String getSearchIspCode() {
		return searchIspCode;
	}
	public void setSearchIspCode(String searchIspCode) {
		this.searchIspCode = searchIspCode;
	}
	@JsonIgnore
	public String getIspNum() {
		return ispNum;
	}
	public void setIspNum(String ispNum) {
		this.ispNum = ispNum;
	}
	
}
