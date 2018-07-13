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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> systemList(){
		List<Map> result = new ArrayList<Map>();
		List<TrafficUaStatistic> list = trafficUaStatisticDao.systemList();
		for (TrafficUaStatistic ua : list) {
			Map map = new HashMap();
			map.put("osType",ua.getOsType());
			map.put("count",ua.getCount());
			map.put("pktNum",ua.getPktNum());
			map.put("byteLen",ua.getByteLen());
			result.add(map);
		}
		return result;
	}
	public List<Map> getBrowserBySystem(Integer osType ){
		List<Map> list = trafficUaStatisticDao.getBrowserBySystem(osType);
		return list;
	}

	public List<Map> browserList() {
		List<Map> result = new ArrayList<Map>();
		List<TrafficUaStatistic> list = trafficUaStatisticDao.browserList();
		for (TrafficUaStatistic ua : list) {
			Map map = new HashMap();
			map.put("bsType",ua.getBsType());
			map.put("count",ua.getCount());
			map.put("pktNum",ua.getPktNum());
			map.put("byteLen",ua.getByteLen());
			result.add(map);
		}
		return result;
	}
	public List<Map> getSystemBybrowser(Integer bsType ){
		List<Map> list = trafficUaStatisticDao.getSystemBybrowser(bsType);
		return list;
	}
	public List<Map> websiteList() {
		List<Map> result = new ArrayList<Map>();
		List<TrafficHttpStatistic> list = trafficHttpStatisticDao.websiteList();
		for (TrafficHttpStatistic website : list) {
			Map map = new HashMap();
			map.put("webId",website.getWebId());
			map.put("count",website.getCount());
			map.put("pktNum",website.getPktNum());
			map.put("byteLen",website.getByteLen());
			result.add(map);
		}
		return result;
	}
	public List<Map> getTypeBywebsite(Integer webId ){
		List<Map> list = trafficHttpStatisticDao.getTypeBywebsite(webId);
		return list;
	}
}
