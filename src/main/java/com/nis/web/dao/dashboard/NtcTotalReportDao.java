package com.nis.web.dao.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.NtcEntranceReport;
import com.nis.domain.restful.dashboard.NtcTotalReport;
import com.nis.domain.restful.dashboard.TrafficTransStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface NtcTotalReportDao {
	NtcTotalReport getMaxReportTime();
	Map getMaxStatTime();
	List<Map> getTotalReportList(@Param("reportTime") Date reportTime);
	List<Map> getNetFlowPortInfoNew(@Param("statTime") Date statTime);
	List<TrafficTransStatistic> getBandwidthTrans(@Param("entranceId") Integer entranceId,@Param("beginDate") String beginDate,@Param("endDate") String endDate,@Param("addrType") String addrType,@Param("transType") Integer transType);
	List<TrafficTransStatistic> getBandwidthTransIPVx(@Param("beginDate") String beginDate,@Param("endDate") String endDate);
	List<TrafficTransStatistic> getBandwidthTransProtocol(@Param("beginDate") String beginDate,@Param("endDate") String endDate);
	String getAddrType();
	String getTransType();
	String getEntranceId();
	List<TrafficTransStatistic> getBandwidthTrans2(@Param("beginDate") String beginDate,@Param("endDate") String endDate);
	Map getEntranceMaxReportTime();
	List<NtcEntranceReport> getActionTrans(@Param("beginDate") String beginDate,@Param("endDate") String endDate ,@Param("entranceId") Integer entranceId,@Param("serviceSql") String serviceSql);
}