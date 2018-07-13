package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficUaStatistic {
    private Integer statId;

    private Integer bsType;
    private Integer osType;

    private Integer entranceId;

    private Integer c2sPktNum;

    private Integer s2cPktNum;

    private Integer c2sByteLen;

    private Integer s2cByteLen;

    private Date statTime;
    
    // 自定义字段
    private Integer count;
    private Integer pktNum;
    private Integer byteLen;
    
    public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getPktNum() {
		return pktNum;
	}

	public void setPktNum(Integer pktNum) {
		this.pktNum = pktNum;
	}

	public Integer getByteLen() {
		return byteLen;
	}

	public void setByteLen(Integer byteLen) {
		this.byteLen = byteLen;
	}

	public Integer getOsType() {
		return osType;
	}

	public void setOsType(Integer osType) {
		this.osType = osType;
	}

	public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public Integer getBsType() {
        return bsType;
    }

    public void setBsType(Integer bsType) {
        this.bsType = bsType;
    }

    public Integer getEntranceId() {
        return entranceId;
    }

    public void setEntranceId(Integer entranceId) {
        this.entranceId = entranceId;
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

    public Integer getS2cByteLen() {
        return s2cByteLen;
    }

    public void setS2cByteLen(Integer s2cByteLen) {
        this.s2cByteLen = s2cByteLen;
    }

    public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }
}