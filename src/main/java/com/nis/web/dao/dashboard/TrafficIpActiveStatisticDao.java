package com.nis.web.dao.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficIpActiveStatistic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface TrafficIpActiveStatisticDao {

//	List<TrafficIpActiveStatistic> getIpList(TrafficIpActiveStatistic trafficIpActiveStatistic);
	TrafficIpActiveStatistic getMaxStatTime();

	ArrayList<TrafficIpActiveStatistic> ipActiveFiveMinute( @Param("ipAddr") String ipAddr,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate,@Param("entranceId")Integer entranceId);

	HashMap ipActiveOneHour(@Param("ipAddr") String ipAddr,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate,@Param("entranceId")Integer entranceId);

	ArrayList<LinkedHashMap> ipActiveChart(@Param("beginDate")Date beginDate,@Param("endDate")Date endDate,@Param("entranceId")Integer entranceId);
}