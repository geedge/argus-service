package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficProtocolStatistic {
    private Integer statId;

    private Integer protoType;

    private Integer entranceId;

    private Integer c2sPktNum;

    private Integer s2cPktNum;

    private Integer c2sByteLen;

    private Integer s2cByteLen;

    private Date statTime;

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public Integer getProtoType() {
        return protoType;
    }

    public void setProtoType(Integer protoType) {
        this.protoType = protoType;
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