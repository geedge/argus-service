package com.nis.web.dao.dashboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficHttpStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficHttpStatisticDao {
    List<TrafficHttpStatistic> websiteList();

	List<Map> getTypeBywebsite(@Param("webId") Integer webId);
	
	Map websiteTypeOthers(@Param("webType") List webType,@Param("webId") Integer webId);

	Integer preWebsiteListCount(@Param("webId") Integer webId);
}