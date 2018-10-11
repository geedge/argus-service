package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;

public class TrafficNetflowPortInfoList implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<TrafficNetflowPortInfo> trafficNetflowPortInfoList;

	public List<TrafficNetflowPortInfo> getTrafficNetflowPortInfoList() {
		return trafficNetflowPortInfoList;
	}

	public void setTrafficNetflowPortInfoList(List<TrafficNetflowPortInfo> trafficNetflowPortInfoList) {
		this.trafficNetflowPortInfoList = trafficNetflowPortInfoList;
	}

}
