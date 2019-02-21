package com.nis.web.dao.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.AppConnRecordStatistic;
import com.nis.domain.restful.dashboard.TrafficAppFocusStatistic;
import com.nis.domain.restful.dashboard.TrafficAppBpsStatistic;
import com.nis.domain.restful.dashboard.TrafficAppPpsStatistic;
import com.nis.domain.restful.dashboard.TrafficAppStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficAppStatisticDao {

	TrafficAppStatistic getMaxStatTime();
	List<Map> appChart(@Param("statTime")Date statTime);
	List<Map> getAppList(@Param("beginTime")String beginTime,@Param("endTime")String endTime,@Param("appType")Integer[] appType,@Param("entranceId")Integer entranceId);
	List<TrafficAppFocusStatistic> getAppTrend(TrafficAppFocusStatistic entity);
	List<TrafficAppBpsStatistic> getAppBpsTrend(TrafficAppBpsStatistic entity);
	List<TrafficAppPpsStatistic> getAppPpsTrend(TrafficAppPpsStatistic entity);
	List<AppConnRecordStatistic> appConnRecordTop100(AppConnRecordStatistic entity);
	List<AppConnRecordStatistic> appConnRecordTotal(AppConnRecordStatistic entity);
}