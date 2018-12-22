package com.nis.web.dao.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficProtocolStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficProtocolStatisticDao {

	TrafficProtocolStatistic getMaxStatTime();
	List<Map> protocolChart(@Param("statTime")Date statTime);
	List<Map> getProtocolList(@Param("beginTime")String beginTime,@Param("endTime")String endTime,@Param("protoType")Integer[] protoType);

}