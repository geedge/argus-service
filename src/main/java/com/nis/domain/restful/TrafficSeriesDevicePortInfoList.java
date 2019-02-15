package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;

public class TrafficSeriesDevicePortInfoList implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<TrafficSeriesDevicePortInfo> trafficSeriesDevicePortInfoList;

	public List<TrafficSeriesDevicePortInfo> getTrafficSeriesDevicePortInfoList() {
		return trafficSeriesDevicePortInfoList;
	}

	public void setTrafficSeriesDevicePortInfoList(List<TrafficSeriesDevicePortInfo> trafficSeriesDevicePortInfoList) {
		this.trafficSeriesDevicePortInfoList = trafficSeriesDevicePortInfoList;
	}

}
