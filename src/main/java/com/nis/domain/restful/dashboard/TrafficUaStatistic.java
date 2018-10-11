package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficUaStatistic {
    private Integer statId;

    private Integer bsType;
    private Integer osType;

    private Integer entranceId;

    private Long c2sPktNum;

    private Long s2cPktNum;

    private Long c2sByteLen;

    private Long s2cByteLen;

    private Date statTime;
    
    // 自定义字段
    private Long count;
    private Long pktNum;
    private Long byteLen;
    

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

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getPktNum() {
		return pktNum;
	}

	public void setPktNum(Long pktNum) {
		this.pktNum = pktNum;
	}

	public Long getByteLen() {
		return byteLen;
	}

	public void setByteLen(Long byteLen) {
		this.byteLen = byteLen;
	}

	public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }
}