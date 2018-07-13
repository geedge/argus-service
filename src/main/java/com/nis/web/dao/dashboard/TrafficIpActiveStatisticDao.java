package com.nis.web.dao.dashboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.nis.domain.restful.dashboard.TrafficIpActiveStatistic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface TrafficIpActiveStatisticDao {

	List<TrafficIpActiveStatistic> getList(TrafficIpActiveStatistic trafficIpActiveStatistic);
	
	ArrayList<LinkedHashMap> ipActiveChart();
}