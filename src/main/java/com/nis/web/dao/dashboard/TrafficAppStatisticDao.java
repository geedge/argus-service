package com.nis.web.dao.dashboard;

import java.util.List;
import java.util.Map;

import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficAppStatisticDao {

	List<Map> appChart();
}