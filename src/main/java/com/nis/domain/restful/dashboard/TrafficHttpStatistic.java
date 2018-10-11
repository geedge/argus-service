package com.nis.domain.restful.dashboard;

import java.util.Date;

public class TrafficHttpStatistic {
    private Integer statId;

    private Integer webType;

    private Integer webId;

    private Long c2sPktNum;

    private Long s2cPktNum;

    private Long c2sByteLen;

    private Long s2cByteLen;

    private Date statTime;
    
    // 自定义字段
    private Long count;
    private Long pktNum;
    private Long byteLen;
    private Integer websiteServiceId;//网站id

    
    public Integer getWesiteServiceId() {
		return websiteServiceId;
	}

	public void setWebsiteServiceId(Integer websiteServiceId) {
		this.websiteServiceId = websiteServiceId;
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

	public Integer getWebsiteServiceId() {
		return websiteServiceId;
	}

	public Date getStatTime() {
        return statTime;
    }

    public void setStatTime(Date statTime) {
        this.statTime = statTime;
    }
}