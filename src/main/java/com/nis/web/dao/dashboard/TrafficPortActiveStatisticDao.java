package com.nis.web.dao.dashboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficPortActiveStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficPortActiveStatisticDao {

	List<TrafficPortActiveStatistic> getPortActiveList();
	TrafficPortActiveStatistic getPortActiveOld(@Param("port")Integer port);
}