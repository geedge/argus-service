package com.nis.web.dao.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficUaStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficUaStatisticDao {
	
	TrafficUaStatistic getMaxStatTime();

    List<TrafficUaStatistic> systemList(@Param("statTime") Date statTime);

	List<Map> getBrowserBySystem(@Param("osType") Integer osType,@Param("statTime") Date statTime);

	List<TrafficUaStatistic> browserList(@Param("statTime") Date statTime);

	List<Map> getSystemBybrowser(@Param("bsType") Integer bsType,@Param("statTime") Date statTime);
	
	Map systemOthers(@Param("bsType") List bsType,@Param("osType") Integer osType,@Param("statTime") Date statTime);

	Map browserOthers(@Param("osType") List osType,@Param("bsType") Integer bsType,@Param("statTime") Date statTime);
	
	Long preSystemListCount(@Param("osType") Integer osType,@Param("statTime") Date statTime);

	Long preBrowserListCount(@Param("bsType") Integer bsType,@Param("statTime") Date statTime);
}