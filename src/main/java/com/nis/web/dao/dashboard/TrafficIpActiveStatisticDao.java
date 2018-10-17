package com.nis.web.dao.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficIpActiveStatistic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface TrafficIpActiveStatisticDao {

	List<TrafficIpActiveStatistic> getIpList(TrafficIpActiveStatistic trafficIpActiveStatistic);

	ArrayList<TrafficIpActiveStatistic> ipActiveFiveMinute(@Param("ipAddr") String ipAddr);

	ArrayList<HashMap> ipActiveOneHour(@Param("ipAddr") String ipAddr);

	ArrayList<LinkedHashMap> ipActiveChart();
}