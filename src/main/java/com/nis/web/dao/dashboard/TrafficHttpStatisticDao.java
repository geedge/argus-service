package com.nis.web.dao.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficHttpStatistic;
import com.nis.web.dao.MyBatisDao;

@MyBatisDao
public interface TrafficHttpStatisticDao {
	TrafficHttpStatistic getMaxStatTime();

	List<TrafficHttpStatistic> websiteList(@Param("statTime") Date statTime);

	// 获取域名分类之后属于的网站
	List<Map> getDomainByWebsiteList(@Param("statTime") Date statTime, @Param("endTime") Date endTime);

	List<Map> getDomainByWebsiteServiceId(@Param("webIdList") List webIdList,@Param("statTime") Date statTime, @Param("endTime") Date endTime);

	Map websiteDomainOthers(@Param("webIdList") List webIdList, @Param("statTime") Date statTime,
			@Param("endTime") Date endTime);

	List<Map> getDomainByTopicList(@Param("statTime") Date statTime, @Param("endTime") Date endTime);
	List<Map> getHttpStatisticNoLinkAndPkt(@Param("statTime") Date statTime, @Param("endTime") Date endTime);
	List<Map> getTrafficHttpStatistic(@Param("statTime") Date statTime, @Param("endTime") Date endTime);
	List getIdByWebSiteId(@Param("websiteId") Integer websiteId);
}