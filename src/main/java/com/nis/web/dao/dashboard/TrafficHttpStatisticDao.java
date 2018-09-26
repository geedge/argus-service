package com.nis.web.dao.dashboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.dashboard.TrafficHttpStatistic;
import com.nis.web.dao.MyBatisDao;
@MyBatisDao
public interface TrafficHttpStatisticDao {
    List<TrafficHttpStatistic> websiteList();
	
	//获取域名分类之后属于的网站
	List<TrafficHttpStatistic> getDomainByWebsiteList();
	Integer preWebsiteListCount(@Param("websiteServiceId") Integer websiteServiceId);
	List<Map> getDomainByWebsiteServiceId(@Param("websiteServiceId") Integer websiteServiceId);
	Map websiteDomainOthers(@Param("webIdList") List webIdList,@Param("websiteServiceId") Integer websiteServiceId);

	List<Map> getDomainByTopicList();

	List<Map> getDomainByTopicId(@Param("topicId")Object TopicId);
}