package com.nis.domain.restful;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * <p>Title: AbnormalMachine</p>
 * <p>Description: nms上报的异常机器ip信息</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年8月17日 
 *
 */
@ApiModel(value = "AbnormalMachine对象", description = "nms上报的异常机器ip信息类")
public class AbnormalMachine implements Serializable {
	private static final long serialVersionUID = 1L;

	// 与TrafficNmsServerStatistic中id是主外键关系,TrafficNmsServerStatistic中id是主键
	@JsonIgnore
	private Integer nmsServerId;

	// 主机名称
	@ApiModelProperty(value = "hostName", notes = "主机名称")
	private String hostName;
	private String ip;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getNmsServerId() {
		return nmsServerId;
	}

	public void setNmsServerId(Integer nmsServerId) {
		this.nmsServerId = nmsServerId;
	}

}
