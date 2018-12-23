package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficHttpFocusStatistic {

	private Long statId;
	private Integer webId;
	private Long uniqueNum;
	private Long linkNum;
	private Long c2sPktNum;
	private Long s2cPktNum;
	private Long c2sByteLen;
	private Long s2cByteLen;
	private Integer entranceId;
	private Date statTime;
	
	private String time;
	private Long count;
	public Long getStatId() {
		return statId;
	}
	public void setStatId(Long statId) {
		this.statId = statId;
	}
	public Integer getWebId() {
		return webId;
	}
	public void setWebId(Integer webId) {
		this.webId = webId;
	}
	public Long getUniqueNum() {
		return uniqueNum;
	}
	public void setUniqueNum(Long uniqueNum) {
		this.uniqueNum = uniqueNum;
	}
	public Long getLinkNum() {
		return linkNum;
	}
	public void setLinkNum(Long linkNum) {
		this.linkNum = linkNum;
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
	public Integer getEntranceId() {
		return entranceId;
	}
	public void setEntranceId(Integer entranceId) {
		this.entranceId = entranceId;
	}
	public Date getStatTime() {
		return statTime;
	}
	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
