package com.nis.domain.restful.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppConnRecordStatistic extends AppTrendEntity{
	
	protected String ipAddr;
	protected String pktNum;
	protected String byteNum;
	protected String logNum;	//日志数量
	
	public AppConnRecordStatistic() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppConnRecordStatistic(String ipAddr, String pktNum, String byteNum,
			String logNum) {
		super();
		this.ipAddr = ipAddr;
		this.pktNum = pktNum;
		this.byteNum = byteNum;
		this.logNum = logNum;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getPktNum() {
		return pktNum;
	}
	public void setPktNum(String pktNum) {
		this.pktNum = pktNum;
	}
	public String getByteNum() {
		return byteNum;
	}
	public void setByteNum(String byteNum) {
		this.byteNum = byteNum;
	}
	public String getLogNum() {
		return logNum;
	}
	public void setLogNum(String logNum) {
		this.logNum = logNum;
	}
}
