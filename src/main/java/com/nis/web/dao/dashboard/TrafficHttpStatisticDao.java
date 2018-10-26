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
    List<TrafficHttpStatistic> websiteList(@Param("statTime")Date statTime);
	
	//获取域名分类之后属于的网站
	List<TrafficHttpStatistic> getDomainByWebsiteList(@Param("statTime")Date statTime);
	Integer preWebsiteListCount(@Param("websiteServiceId") Integer websiteServiceId,@Param("statTime") Date statTime);
	List<Map> getDomainByWebsiteServiceId(@Param("websiteServiceId") Integer websiteServiceId,@Param("statTime") Date statTime);
	Map websiteDomainOthers(@Param("webIdList") List webIdList,@Param("websiteServiceId") Integer websiteServiceId,@Param("statTime") Date statTime);

	List<Map> getDomainByTopicList(@Param("statTime") Date statTime);

	List<Map> getDomainByTopicId(@Param("topicId")Object TopicId,@Param("statTime")Date statTime);
}