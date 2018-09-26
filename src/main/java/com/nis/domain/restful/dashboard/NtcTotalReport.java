package com.nis.domain.restful.dashboard;

import java.util.Date;

public class NtcTotalReport {
	
	private Integer statId 			;//int(11) NOT NULL AUTO_INCREMENT COMMENT id自增,
	private Integer rejectNum	 	;//int(11) NOT NULL COMMENT 封堵数量,
	private Integer monitorNum 		;//int(11) NOT NULL COMMENT 监测数量,
	private Integer c2sPktNum 		;//int(11) DEFAULT NULL COMMENT c2s包数,
	private Integer s2cPktNum 		;//int(11) DEFAULT NULL COMMENT s2c包数,
	private Integer c2sByteLen		;//int(11) NOT NULL COMMENT c2s字节数,
	private Integer s2cByteLen 		;//int(11) NOT NULL COMMENT s2c字节数,
	private Integer newUniConnNum 	;//int(11) DEFAULT NULL COMMENT 新建链接数,
	private Integer liveConnNum 	;//int(11) DEFAULT NULL COMMENT 活跃链接数,
	private Integer dropConnNum 	;//int(11) DEFAULT NULL COMMENT 丢弃链接数,
	private Integer loopConnNum 	;//int(11) DEFAULT NULL COMMENT 回流链接数,
	private Date reportTime 		;//datetime NOT NULL COMMENT 统计时间,
	
	public Integer getStatId() {
		return statId;
	}
	public void setStatId(Integer statId) {
		this.statId = statId;
	}
	public Integer getRejectNum() {
		return rejectNum;
	}
	public void setRejectNum(Integer rejectNum) {
		this.rejectNum = rejectNum;
	}
	public Integer getMonitorNum() {
		return monitorNum;
	}
	public void setMonitorNum(Integer monitorNum) {
		this.monitorNum = monitorNum;
	}
	public Integer getC2sPktNum() {
		return c2sPktNum;
	}
	public void setC2sPktNum(Integer c2sPktNum) {
		this.c2sPktNum = c2sPktNum;
	}
	public Integer getS2cPktNum() {
		return s2cPktNum;
	}
	public void setS2cPktNum(Integer s2cPktNum) {
		this.s2cPktNum = s2cPktNum;
	}
	public Integer getC2sByteLen() {
		return c2sByteLen;
	}
	public void setC2sByteLen(Integer c2sByteLen) {
		this.c2sByteLen = c2sByteLen;
	}
	public Integer getS2cByteNen() {
		return s2cByteLen;
	}
	public void setS2cByteNen(Integer s2cByteLen) {
		this.s2cByteLen = s2cByteLen;
	}
	public Integer getNewUniConnNum() {
		return newUniConnNum;
	}
	public void setNewUniConnNum(Integer newUniConnNum) {
		this.newUniConnNum = newUniConnNum;
	}
	public Integer getLiveConnNum() {
		return liveConnNum;
	}
	public void setLiveConnNum(Integer liveConnNum) {
		this.liveConnNum = liveConnNum;
	}
	public Integer getDropConnNum() {
		return dropConnNum;
	}
	public void setDropConnNum(Integer dropConnNum) {
		this.dropConnNum = dropConnNum;
	}
	public Integer getLoopConnNum() {
		return loopConnNum;
	}
	public void setLoopConnNum(Integer loopConnNum) {
		this.loopConnNum = loopConnNum;
	}
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

}