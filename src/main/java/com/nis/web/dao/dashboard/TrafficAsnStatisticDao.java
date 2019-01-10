package com.nis.web.dao.dashboard;

import java.util.List;

import com.nis.domain.restful.dashboard.TrafficAsnStatistic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface TrafficAsnStatisticDao {

	List<TrafficAsnStatistic> getTrafficAsnStatisticList(TrafficAsnStatistic trafficAsnStatistic);

}