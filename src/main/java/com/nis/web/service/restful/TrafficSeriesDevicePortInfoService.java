package com.nis.web.service.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.restful.TrafficSeriesDevicePortInfo;
import com.nis.web.dao.TrafficSeriesDevicePortInfoDao;

@Service
public class TrafficSeriesDevicePortInfoService {
	@Autowired
	TrafficSeriesDevicePortInfoDao trafficSeriesDevicePortInfoDao;

	@Transactional
	public void saveTrafficSeriesDevicePortInfo(List<TrafficSeriesDevicePortInfo> trafficSeriesDevicePortInfoList) {
		for (TrafficSeriesDevicePortInfo trafficSeriesDevicePortInfo : trafficSeriesDevicePortInfoList) {
			trafficSeriesDevicePortInfoDao.insert(trafficSeriesDevicePortInfo);
		}
	}
}
