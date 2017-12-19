package com.nis.domain;

public class SrcIp {
    private Integer seqId;

    private Integer ipStartAddr;

    private Integer ipEndAddr;

    private Integer ipAddrRange;

    private String ipStartString;

    private String ipEndString;

    private Integer port;

    private Long ispId;

    private Long areaId;

    private Integer flag;

    private String description;

    private Integer yl1;

    private String yl2;
    
    
    private String searchIp;
    

    public String getSearchIp() {
		return searchIp;
	}

	public void setSearchIp(String searchIp) {
		this.searchIp = searchIp;
	}

	public Integer getSeqId() {
        return seqId;
    }

    public void setSeqId(Integer seqId) {
        this.seqId = seqId;
    }

    public Integer getIpStartAddr() {
        return ipStartAddr;
    }

    public void setIpStartAddr(Integer ipStartAddr) {
        this.ipStartAddr = ipStartAddr;
    }

    public Integer getIpEndAddr() {
        return ipEndAddr;
    }

    public void setIpEndAddr(Integer ipEndAddr) {
        this.ipEndAddr = ipEndAddr;
    }

    public Integer getIpAddrRange() {
        return ipAddrRange;
    }

    public void setIpAddrRange(Integer ipAddrRange) {
        this.ipAddrRange = ipAddrRange;
    }

    public String getIpStartString() {
        return ipStartString;
    }

    public void setIpStartString(String ipStartString) {
        this.ipStartString = ipStartString == null ? null : ipStartString.trim();
    }

    public String getIpEndString() {
        return ipEndString;
    }

    public void setIpEndString(String ipEndString) {
        this.ipEndString = ipEndString == null ? null : ipEndString.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Long getIspId() {
        return ispId;
    }

    public void setIspId(Long ispId) {
        this.ispId = ispId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getYl1() {
        return yl1;
    }

    public void setYl1(Integer yl1) {
        this.yl1 = yl1;
    }

    public String getYl2() {
        return yl2;
    }

    public void setYl2(String yl2) {
        this.yl2 = yl2 == null ? null : yl2.trim();
    }
}