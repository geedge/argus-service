package com.nis.domain.restful.dashboard;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TrafficAppPpsStatistic extends AppTrendEntity {

	private Date time;
	private Long c2sNum;
	private Long s2cNum;
	private Long totalNum;
	private String searchIspCode;// 运营商
	private String ispNum;// device_id ,entrance_id 拼接

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Long getC2sNum() {
		return c2sNum;
	}

	public void setC2sNum(Long c2sNum) {
		this.c2sNum = c2sNum;
	}

	public Long getS2cNum() {
		return s2cNum;
	}

	public void setS2cNum(Long s2cNum) {
		this.s2cNum = s2cNum;
	}

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
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
