package com.nis.web.dao.dashboard;

import java.util.List;
import java.util.Map;

import com.nis.domain.restful.dashboard.TrafficPortActiveStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficPortActiveStatisticDao {

	List<TrafficPortActiveStatistic> getPortActiveList();
}