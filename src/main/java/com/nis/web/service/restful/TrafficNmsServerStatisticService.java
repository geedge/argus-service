package com.nis.web.service.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.restful.AbnormalMachine;
import com.nis.domain.restful.TrafficNmsServerStatistic;
import com.nis.web.dao.TrafficNmsServerStatisticDao;

@Service
public class TrafficNmsServerStatisticService {
	@Autowired
	TrafficNmsServerStatisticDao trafficNmsServerStatisticDao;

	public TrafficNmsServerStatistic saveNmsServer(TrafficNmsServerStatistic trafficNmsServerStatistic) {
		trafficNmsServerStatisticDao.insert(trafficNmsServerStatistic);
		return trafficNmsServerStatistic;
	}

	public void saveAbnormalMachine(Integer id, List<AbnormalMachine> abnormalMachineList) {
		trafficNmsServerStatisticDao.insertAbnormalMachine(id, abnormalMachineList);
	}

	public void delNmsServer(TrafficNmsServerStatistic trafficNmsServerStatistic) {
		trafficNmsServerStatisticDao.delete(trafficNmsServerStatistic);
	}
}
