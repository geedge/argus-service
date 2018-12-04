package com.nis.domain.restful.dashboard;

import java.util.Date;

public class NtcTotalReport {
	
	private Integer statId 			;//int(11) NOT NULL AUTO_INCREMENT COMMENT id自增,
	private Long rejectNum	 	;//int(11) NOT NULL COMMENT 封堵数量,
	private Long monitorNum 		;//int(11) NOT NULL COMMENT 监测数量,
	private Long c2sPktNum 		;//int(11) DEFAULT NULL COMMENT c2s包数,
	private Long s2cPktNum 		;//int(11) DEFAULT NULL COMMENT s2c包数,
	private Long c2sByteLen		;//int(11) NOT NULL COMMENT c2s字节数,
	private Long s2cByteLen 		;//int(11) NOT NULL COMMENT s2c字节数,
	private Long newUniConnNum 	;//int(11) DEFAULT NULL COMMENT 新建链接数,
	private Long liveConnNum 	;//int(11) DEFAULT NULL COMMENT 活跃链接数,
	private Long dropConnNum 	;//int(11) DEFAULT NULL COMMENT 丢弃链接数,
	private Long loopConnNum 	;//int(11) DEFAULT NULL COMMENT 回流链接数,
	private Date reportTime 		;//datetime NOT NULL COMMENT 统计时间,
	
	public Integer getStatId() {
		return statId;
	}
	public void setStatId(Integer statId) {
		this.statId = statId;
	}
	
	public Long getRejectNum() {
		return rejectNum;
	}
	public void setRejectNum(Long rejectNum) {
		this.rejectNum = rejectNum;
	}
	public Long getMonitorNum() {
		return monitorNum;
	}
	public void setMonitorNum(Long monitorNum) {
		this.monitorNum = monitorNum;
	}
	public Long getC2sPktNum() {
		return c2sPktNum;
	}
	public void setC2sPktNum(Long c2sPktNum) {
		this.c2sPktNum = c2sPktNum;
	}
	public Long getS2cPktNum() {
		return s2cPktNum;
	}
	public void setS2cPktNum(Long s2cPktNum) {
		this.s2cPktNum = s2cPktNum;
	}
	public Long getC2sByteLen() {
		return c2sByteLen;
	}
	public void setC2sByteLen(Long c2sByteLen) {
		this.c2sByteLen = c2sByteLen;
	}
	public Long getS2cByteLen() {
		return s2cByteLen;
	}
	public void setS2cByteLen(Long s2cByteLen) {
		this.s2cByteLen = s2cByteLen;
	}
	public Long getNewUniConnNum() {
		return newUniConnNum;
	}
	public void setNewUniConnNum(Long newUniConnNum) {
		this.newUniConnNum = newUniConnNum;
	}
	public Long getLiveConnNum() {
		return liveConnNum;
	}
	public void setLiveConnNum(Long liveConnNum) {
		this.liveConnNum = liveConnNum;
	}
	public Long getDropConnNum() {
		return dropConnNum;
	}
	public void setDropConnNum(Long dropConnNum) {
		this.dropConnNum = dropConnNum;
	}
	public Long getLoopConnNum() {
		return loopConnNum;
	}
	public void setLoopConnNum(Long loopConnNum) {
		this.loopConnNum = loopConnNum;
	}
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

}