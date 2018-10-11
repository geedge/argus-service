package com.nis.domain.restful.dashboard;

import java.util.Date;

import com.nis.domain.BaseEntity;

public class TrafficIpActiveStatistic extends BaseEntity<TrafficIpActiveStatistic> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9198831010652509505L;

	private Integer statId;

    private String ipAddr;

    private Integer areaId;

    private Long linkNum;

    private Long c2sPktNum;

    private Long s2cPktNum;

    private Long c2sByteLen;

    private Long s2cByteLen;

    private Date statTime;

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
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

	public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }

	@Override
	public String toString() {
		return "TrafficIpActiveStatistic [statId=" + statId + ", ipAddr=" + ipAddr + ", areaId=" + areaId + ", linkNum="
				+ linkNum + ", c2sPktNum=" + c2sPktNum + ", s2cPktNum=" + s2cPktNum + ", c2sByteLen=" + c2sByteLen
				+ ", s2cByteLen=" + s2cByteLen + ", statTime=" + statTime + "]";
	}
    
}