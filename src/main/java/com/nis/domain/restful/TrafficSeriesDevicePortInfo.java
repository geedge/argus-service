package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nis.util.JsonDateDeserializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class TrafficSeriesDevicePortInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private long id;
	private String area;
	private Integer port;
	private String status;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "commitTime", notes = "时间")
	private Date commitTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class)
	public Date getCommitTime() {
		return commitTime;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}
}
