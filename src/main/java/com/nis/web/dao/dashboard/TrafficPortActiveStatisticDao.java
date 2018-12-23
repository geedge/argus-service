package com.nis.web.dao.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficPortActiveStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficPortActiveStatisticDao {

	TrafficPortActiveStatistic getMaxStatTime();
	List<TrafficPortActiveStatistic> getPortActiveList(@Param("statTime")Date statTime);
	TrafficPortActiveStatistic getPortActiveOld(@Param("port")Integer port,@Param("statTime")Date statTime);
	//获取端口详情趋势
	ArrayList<TrafficPortActiveStatistic>  findTrafficPortActive(@Param("statStartTime")String statStartTime,@Param("statEndTime")String statEndTime,@Param("port")String port);
	ArrayList<LinkedHashMap>  findTrafficPortActiveTopN(@Param("statStartTime")String statStartTime,@Param("statEndTime")String statEndTime);
	
}