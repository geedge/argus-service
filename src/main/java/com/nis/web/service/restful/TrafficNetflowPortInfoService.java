package com.nis.web.service.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.restful.TrafficNetflowPortInfo;
import com.nis.web.dao.TrafficNetflowPortInfoDao;

@Service
public class TrafficNetflowPortInfoService {
	@Autowired
	TrafficNetflowPortInfoDao trafficNetflowPortInfoDao;

	@Transactional
	public void saveTrafficNetflowPortInfo(List<TrafficNetflowPortInfo> trafficNetflowPortInfoList) {
		for (TrafficNetflowPortInfo trafficNetflowPortInfo : trafficNetflowPortInfoList) {
			trafficNetflowPortInfoDao.insert(trafficNetflowPortInfo);
		}
	}
}
