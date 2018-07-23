package com.nis.web.dao.dashboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficUaStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficUaStatisticDao {

    List<TrafficUaStatistic> systemList();

	List<Map> getBrowserBySystem(@Param("osType") Integer osType);

	List<TrafficUaStatistic> browserList();

	List<Map> getSystemBybrowser(@Param("bsType") Integer bsType);
	
	Map systemOthers(@Param("bsType") List bsType,@Param("osType") Integer osType);

	Map browserOthers(@Param("osType") List osType,@Param("bsType") Integer bsType);
	
	Integer preSystemListCount(@Param("osType") Integer osType);

	Integer preBrowserListCount(@Param("bsType") Integer bsType);
}