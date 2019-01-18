package com.nis.domain.restful.dashboard;

import java.io.Serializable;
import java.util.Date;

public class SysDeviceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2642565275696732948L;
	private Long id;
	private String deviceType;// '设备类型',
	private String ipAddr;// 通信运营商编码,
	private Integer deviceId;//'设备ID，相同设备可对应多个运营商',
	private Integer linkId;//'链路号索引',
	private Integer isp;//'运营商编码，例如ktel-mxpe:1001',
	private Integer entranceId;// '地域 1-astana 2-alamty',
	private Integer status;// '0-未接入 1-接入 2-部分接入',
	private Date createTime;
	
	private String ispNum;
	private String ispName;//'运营商',
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public Integer getIsp() {
		return isp;
	}
	public void setIsp(Integer isp) {
		this.isp = isp;
	}
	public Integer getEntranceId() {
		return entranceId;
	}
	public void setEntranceId(Integer entranceId) {
		this.entranceId = entranceId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getIspNum() {
		return ispNum;
	}
	public void setIspNum(String ispNum) {
		this.ispNum = ispNum;
	}
	public String getIspName() {
		return ispName;
	}
	public void setIspName(String ispName) {
		this.ispName = ispName;
	}
	
}
