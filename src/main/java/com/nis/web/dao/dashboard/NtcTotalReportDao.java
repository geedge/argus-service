package com.nis.web.dao.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.NtcTotalReport;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface NtcTotalReportDao {
	NtcTotalReport getMaxReportTime();
	Map getMaxRecvTime();
	List<Map> getTotalReportList(@Param("reportTime") Date reportTime);
	List<Map> getNetFlowPortInfoNew(@Param("recvTime") Date recvTime);
	List<Map> getNetFlowPortInfoOld(@Param("recvTime") Date recvTime);
}