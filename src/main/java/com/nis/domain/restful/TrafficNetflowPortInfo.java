package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class TrafficNetflowPortInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private long stat_id;
	private long port;
	private String nodeName;
	private String nodeIp;
	private String portDesc;
	private long bandwidth;
	private long inoctets;
	private long outoctets;
	private long inoctetsSpeed;
	private long outoctetsSpeed;
	private long inpktsSpeed;
	private long outpktsSpeed;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "dataArriveTime", notes = "")
	private Date recvTime;
	public long getStat_id() {
		return stat_id;
	}
	public void setStat_id(long stat_id) {
		this.stat_id = stat_id;
	}
	public long getPort() {
		return port;
	}
	public void setPort(long port) {
		this.port = port;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeIp() {
		return nodeIp;
	}
	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}
	public String getPortDesc() {
		return portDesc;
	}
	public void setPortDesc(String portDesc) {
		this.portDesc = portDesc;
	}
	public long getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(long bandwidth) {
		this.bandwidth = bandwidth;
	}
	public long getInoctets() {
		return inoctets;
	}
	public void setInoctets(long inoctets) {
		this.inoctets = inoctets;
	}
	public long getOutoctets() {
		return outoctets;
	}
	public void setOutoctets(long outoctets) {
		this.outoctets = outoctets;
	}
	public long getInoctetsSpeed() {
		return inoctetsSpeed;
	}
	public void setInoctetsSpeed(long inoctetsSpeed) {
		this.inoctetsSpeed = inoctetsSpeed;
	}
	public long getOutoctetsSpeed() {
		return outoctetsSpeed;
	}
	public void setOutoctetsSpeed(long outoctetsSpeed) {
		this.outoctetsSpeed = outoctetsSpeed;
	}
	public long getInpktsSpeed() {
		return inpktsSpeed;
	}
	public void setInpktsSpeed(long inpktsSpeed) {
		this.inpktsSpeed = inpktsSpeed;
	}
	public long getOutpktsSpeed() {
		return outpktsSpeed;
	}
	public void setOutpktsSpeed(long outpktsSpeed) {
		this.outpktsSpeed = outpktsSpeed;
	}
	public Date getRecvTime() {
		return recvTime;
	}
	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

}
