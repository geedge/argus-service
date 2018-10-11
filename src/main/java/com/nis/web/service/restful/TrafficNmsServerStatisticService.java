package com.nis.web.service.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.restful.TrafficNmsServerStatistic;
import com.nis.web.dao.TrafficNmsServerStatisticDao;

@Service
public class TrafficNmsServerStatisticService {
	@Autowired
	TrafficNmsServerStatisticDao trafficNmsServerStatisticDao;

	@Transactional
	public void saveNmsInfo(List<TrafficNmsServerStatistic> trafficNmsServerList) {
		for (TrafficNmsServerStatistic trafficNmsServerStatistic : trafficNmsServerList) {
			trafficNmsServerStatisticDao.insert(trafficNmsServerStatistic);
			trafficNmsServerStatisticDao.insertAbnormalMachine(trafficNmsServerStatistic.getId(),
					trafficNmsServerStatistic.getAbnormalMachineList());
		}
	}
}
