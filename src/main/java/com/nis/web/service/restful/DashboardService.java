package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.dashboard.TrafficHttpStatistic;
import com.nis.domain.restful.dashboard.TrafficIpActiveStatistic;
import com.nis.domain.restful.dashboard.TrafficUaStatistic;
import com.nis.web.dao.dashboard.TrafficAppStatisticDao;
import com.nis.web.dao.dashboard.TrafficHttpStatisticDao;
import com.nis.web.dao.dashboard.TrafficIpActiveStatisticDao;
import com.nis.web.dao.dashboard.TrafficProtocolStatisticDao;
import com.nis.web.dao.dashboard.TrafficUaStatisticDao;
import com.nis.web.service.BaseService;

@Service
public class DashboardService extends BaseService{

	@Autowired
	public TrafficIpActiveStatisticDao trafficIpActiveStatisticDao;
	@Autowired
	public TrafficProtocolStatisticDao trafficProtocolStatisticDao;
	@Autowired
	public TrafficAppStatisticDao trafficAppStatisticDao;
	@Autowired
	public TrafficUaStatisticDao trafficUaStatisticDao;
	@Autowired
	public TrafficHttpStatisticDao trafficHttpStatisticDao;
	
	/**
	 * 分页查询活跃IPtop100
	 * @return
	 */
	public  Page<TrafficIpActiveStatistic> getList(Page<TrafficIpActiveStatistic> page,TrafficIpActiveStatistic entry){
			// 设置分页参数
			entry.setPage(page);
			// 执行分页查询
			page.setList(trafficIpActiveStatisticDao.getList(entry));
			return page;
	}
	
	public List<LinkedHashMap> ipActiveChart(){
		
		ArrayList<LinkedHashMap> list = trafficIpActiveStatisticDao.ipActiveChart();
		
		return list;
	}

	public List<Map> protocolChart() {
		List<Map> list = trafficProtocolStatisticDao.protocolChart();
		return list;
	}

	public List<Map> appChart() {
		List<Map> list = trafficAppStatisticDao.appChart();
		return list;
	}
	
//	操作系统列表 排名TOP10
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> systemList(){
		List<Map> result = new ArrayList<Map>();
		List<TrafficUaStatistic> list = trafficUaStatisticDao.systemList();
		Integer preCount=0;
		for (TrafficUaStatistic ua : list) {
			Map map = new HashMap();
			map.put("osType",ua.getOsType());
			map.put("count",ua.getCount());
			map.put("pktNum",ua.getPktNum());
			map.put("byteLen",ua.getByteLen());
			preCount = trafficUaStatisticDao.preSystemListCount(ua.getOsType());//上个时段的量 用于与现在对比
			if(preCount!=null){
				map.put("preCount",preCount);
			}else{
				map.put("preCount",0);
			}
			result.add(map);
		}
		
		return result;
	}
	public List<Map> getBrowserBySystem(Integer osType ){
		List<Map> list = trafficUaStatisticDao.getBrowserBySystem(osType);
		List bsType = new ArrayList();
		//查新固定操系统下的除了TOP10以外的others
		if(list!=null&& list.size()>0){
			for (Map map : list) {
				bsType.add(map.get("bsType"));
			}
			Map others = new HashMap();
			others = trafficUaStatisticDao.systemOthers(bsType,osType);
			if(others!=null&&others.size()>0){
			others.put("bsType", "-1");
			list.add(others);
			}
		}
		return list;
	}

	public List<Map> browserList() {
		List<Map> result = new ArrayList<Map>();
		List<TrafficUaStatistic> list = trafficUaStatisticDao.browserList();
		Integer preCount=0;
		for (TrafficUaStatistic ua : list) {
			Map map = new HashMap();
			map.put("bsType",ua.getBsType());
			map.put("count",ua.getCount());
			map.put("pktNum",ua.getPktNum());
			map.put("byteLen",ua.getByteLen());
			preCount = trafficUaStatisticDao.preBrowserListCount(ua.getBsType());//上个时段的量 用于与现在对比
			if(preCount!=null){
				map.put("preCount",preCount);
			}else{
				map.put("preCount",0);
			}
			result.add(map);
		}
		return result;
	}
	public List<Map> getSystemBybrowser(Integer bsType ){
		List<Map> list = trafficUaStatisticDao.getSystemBybrowser(bsType);
		List osType = new ArrayList();
		//查询固定操系统下的除了TOP10以外的others
		if(list!=null&& list.size()>0){
			for (Map map : list) {
				osType.add(map.get("osType"));
			}
			Map others = new HashMap();
			others = trafficUaStatisticDao.browserOthers(osType,bsType);
			if(others!=null&&others.size()>0){
			others.put("osType", "-1");
			list.add(others);
			}
		}
		return list;
	}
	public List<Map> websiteList() {
		List<Map> result = new ArrayList<Map>();
		List<TrafficHttpStatistic> list = trafficHttpStatisticDao.websiteList();
		Integer preCount=0;
		for (TrafficHttpStatistic website : list) {
			Map map = new HashMap();
			map.put("webId",website.getWebId());
			map.put("count",website.getCount());
			map.put("pktNum",website.getPktNum());
			map.put("byteLen",website.getByteLen());
			preCount = trafficHttpStatisticDao.preWebsiteListCount(website.getWebId());//上个时段的量 用于与现在对比
			if(preCount!=null){
				map.put("preCount",preCount);
			}else{
				map.put("preCount",0);
			}
			result.add(map);
		}
		return result;
	}
	public List<Map> getTypeBywebsite(Integer webId ){
		List<Map> list = trafficHttpStatisticDao.getTypeBywebsite(webId);
		List webType = new ArrayList();
		//查询固定网站下的除了TOP10以外的others分类
		if(list!=null&& list.size()>0){
			for (Map map : list) {
				webType.add(map.get("webType"));
			}
			Map others = new HashMap();
			others = trafficHttpStatisticDao.websiteTypeOthers(webType,webId);
			if(others!=null&&others.size()>0){
				others.put("webType", "-1");
				list.add(others);
			}
		}
		return list;
	}
}
