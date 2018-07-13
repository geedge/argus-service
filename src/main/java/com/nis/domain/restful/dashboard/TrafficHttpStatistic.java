package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficHttpStatistic {
    private Integer statId;

    private Integer webType;

    private Integer webId;

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

	public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public Integer getWebType() {
        return webType;
    }

    public void setWebType(Integer webType) {
        this.webType = webType;
    }

    public Integer getWebId() {
        return webId;
    }

    public void setWebId(Integer webId) {
        this.webId = webId;
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