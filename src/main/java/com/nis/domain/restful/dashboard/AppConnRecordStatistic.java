package com.nis.domain.restful.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppConnRecordStatistic extends AppTrendEntity{
	
	protected String ipAddr;
	protected Double pktNum;
	protected String pktPercent;
	protected Double byteNum;
	protected String bytePercent;
	protected Double logNum;	//日志数量
	protected String logPercent;
	protected String searchQuota="1"; //统计指标 1:连接数 2：包数 3：字节数
	
	private String searchIspCode;// 运营商
	private String ispName;// 运营商
	private String ispNum;// device_id ,entrance_id 拼接
	
	public AppConnRecordStatistic() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AppConnRecordStatistic(String ipAddr, Double pktNum,
			String pktPercent, Double byteNum, String bytePercent,
			Double logNum, String logPercent,String ispName) {
		super();
		this.ipAddr = ipAddr;
		this.pktNum = pktNum;
		this.pktPercent = pktPercent;
		this.byteNum = byteNum;
		this.bytePercent = bytePercent;
		this.logNum = logNum;
		this.logPercent = logPercent;
		this.ispName = ispName;
	}

	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Double getPktNum() {
		return pktNum;
	}

	public void setPktNum(Double pktNum) {
		this.pktNum = pktNum;
	}

	public String getPktPercent() {
		return pktPercent;
	}

	public void setPktPercent(String pktPercent) {
		this.pktPercent = pktPercent;
	}

	public Double getByteNum() {
		return byteNum;
	}

	public void setByteNum(Double byteNum) {
		this.byteNum = byteNum;
	}

	public String getBytePercent() {
		return bytePercent;
	}

	public void setBytePercent(String bytePercent) {
		this.bytePercent = bytePercent;
	}

	public Double getLogNum() {
		return logNum;
	}

	public void setLogNum(Double logNum) {
		this.logNum = logNum;
	}

	public String getLogPercent() {
		return logPercent;
	}

	public void setLogPercent(String logPercent) {
		this.logPercent = logPercent;
	}

	@JsonIgnore
	public String getSearchQuota() {
		return searchQuota;
	}

	public void setSearchQuota(String searchQuota) {
		this.searchQuota = searchQuota;
	}

	public String getSearchIspCode() {
		return searchIspCode;
	}
	@JsonIgnore
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

	public String getIspName() {
		return ispName;
	}

	public void setIspName(String ispName) {
		this.ispName = ispName;
	}

}
