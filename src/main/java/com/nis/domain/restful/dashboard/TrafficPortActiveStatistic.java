package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficPortActiveStatistic {
//流量统计之活跃端口统计
	private Integer statId;
	private Integer port;
	private Integer sum;
	private Date statTime;
	  
	public Integer getStatId() {
		return statId;
	}
	public void setStatId(Integer statId) {
		this.statId = statId;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getSum() {
		return sum;
	}
	public void setSum(Integer sum) {
		this.sum = sum;
	}
	public Date getStatTime() {
		return statTime;
	}
	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}
	  
	  
}
