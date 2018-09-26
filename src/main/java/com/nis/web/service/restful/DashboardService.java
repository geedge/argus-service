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
import com.nis.domain.restful.dashboard.TrafficPortActiveStatistic;
import com.nis.domain.restful.dashboard.TrafficUaStatistic;
import com.nis.web.dao.dashboard.NtcTotalReportDao;
import com.nis.web.dao.dashboard.TrafficAppStatisticDao;
import com.nis.web.dao.dashboard.TrafficHttpStatisticDao;
import com.nis.web.dao.dashboard.TrafficIpActiveStatisticDao;
import com.nis.web.dao.dashboard.TrafficPortActiveStatisticDao;
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
	@Autowired
	public NtcTotalReportDao ntcTotalReportDao;
	@Autowired
	public TrafficPortActiveStatisticDao trafficPortActiveStatisticDao;
	
	/**
	 * 流量统计 数据显示
	 * @return Map
	 */
	public List<Map> getTotalReportList(){
		List<Map> totalReportList = ntcTotalReportDao.getTotalReportList();
		//统计带宽的流入流出 单位 五分钟 的 byte
		List<Map> newData = ntcTotalReportDao.getNetFlowPortInfoNew();
		List<Map> oldData = ntcTotalReportDao.getNetFlowPortInfoOld();
		Double inoctets=0d;
		Double outoctets=0d;
		if(newData!=null&&newData.size()>0&&oldData!=null&&oldData.size()>0&&newData.get(0)!=null&&oldData.get(0)!=null){
			Double newInoctets=Double.parseDouble(newData.get(0).get("inoctets").toString()) ;
			Double newOutoctets=Double.parseDouble(newData.get(0).get("outoctets").toString()) ;
			Double oldInoctets=Double.parseDouble(oldData.get(0).get("inoctets").toString());
			Double oldOutoctets=Double.parseDouble(oldData.get(0).get("outoctets").toString());
			//结果为当前五分钟减去上个五分钟
			inoctets=newInoctets-oldInoctets;
			outoctets=newOutoctets-oldOutoctets;
			if(inoctets<0||outoctets<0){
				inoctets=0d;
				outoctets=0d;
			}
		}
		if(totalReportList!=null&&totalReportList.size()>0){
			for (Map map : totalReportList) {
				map.put("inoctets", inoctets);
				map.put("outoctets", outoctets);
			}
		}
		
		return totalReportList;
	}
	public List<TrafficPortActiveStatistic> getPortActiveList(){
		List<TrafficPortActiveStatistic> portActiveList = trafficPortActiveStatisticDao.getPortActiveList();
		
		return portActiveList;
	}
	
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
		if(list!=null&&list.size()>0){
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
		if(list!=null&&list.size()>0){
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
		}
		return result;
	}
	/**
	 * 根据网站服务查询子域名
	 * @param websiteServiceId
	 * @return list
	 */
	public List<Map> getDomainByWebsiteServiceId(Integer websiteServiceId ){
		List<Map> list = trafficHttpStatisticDao.getDomainByWebsiteServiceId(websiteServiceId);
		List webIdList = new ArrayList();
		//查询固定网站下的域名除了TOP10以外的others域名
		if(list!=null&& list.size()>0){
			for (Map map : list) {
				webIdList.add(map.get("webId"));
			}
			Map others = new HashMap();
			others = trafficHttpStatisticDao.websiteDomainOthers(webIdList,websiteServiceId);
			if(others!=null&&others.size()>0){
				others.put("webId", "-1");
				list.add(others);
			}
		}
		return list;
	}
	
	/**
		获取网站列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getDomainByWebsiteList(){
		List<TrafficHttpStatistic> websiteList = trafficHttpStatisticDao.getDomainByWebsiteList();
		List<Map> result = new ArrayList<Map>();
		if(websiteList!=null&&websiteList.size()>0){
			Integer preCount=0;
			for (TrafficHttpStatistic website : websiteList) {
				Map map = new HashMap();
				map.put("websiteServiceId",website.getWesiteServiceId());
				map.put("count",website.getCount());
				map.put("pktNum",website.getPktNum());
				map.put("byteLen",website.getByteLen());
				preCount = trafficHttpStatisticDao.preWebsiteListCount(website.getWesiteServiceId());//上个时段的量 用于与现在对比
				if(preCount!=null){
					map.put("preCount",preCount);
				}else{
					map.put("preCount",0);
				}
				result.add(map);
			}
		}
		
		return result;
	}
	/**
	 *主题网站分类，域名 
	 * 
	 * **/
	public List<Map> getTopicAndDomainList(){
		//按照主题分类 获得主题
		List<Map> topicList = trafficHttpStatisticDao.getDomainByTopicList();
		if(topicList!=null&&topicList.size()>0){
			for (Map m : topicList) {
				List<Map> domainList= new ArrayList<Map>();
				if(m!=null&&m.get("topicId")!=null){
					domainList = trafficHttpStatisticDao.getDomainByTopicId(m.get("topicId"));
					m.put("domainData", domainList);
				}
			}
		}
		return topicList;
	}
}
