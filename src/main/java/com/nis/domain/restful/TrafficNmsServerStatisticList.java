package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * 
 * <p>Title: TrafficNmsServerStatistic</p>
 * <p>Description: 记录nms服务器信息,全网机器总量,正常在线机器总数,异常机器列表</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年8月17日 
 *
 */
@ApiModel(value = "TrafficNmsServerStatistic对象", description = "nms上报的服务器相关信息对象类")
public class TrafficNmsServerStatisticList implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<TrafficNmsServerStatistic> trafficNmsServerList;

	public List<TrafficNmsServerStatistic> getTrafficNmsServerList() {
		return trafficNmsServerList;
	}

	public void setTrafficNmsServerList(List<TrafficNmsServerStatistic> trafficNmsServerList) {
		this.trafficNmsServerList = trafficNmsServerList;
	}

}
