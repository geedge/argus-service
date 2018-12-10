package com.nis.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.CfgLogInfo;
import com.nis.domain.restful.DropInfo;
import com.nis.domain.restful.NtcAreaHomeReport;

@MyBatisDao
public interface SystemHomePageDao {

	List<NtcAreaHomeReport> getTrafficAreaStatList(@Param("startTime") Date searchReportStartTime,
			@Param("endTime") Date searchReportEndTime);

	List<CfgLogInfo> getCfgSortLogStatList(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	List<DropInfo> getBlockAndDropStatListAll(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	List<DropInfo> getBlockAndDropStatListMinute(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
