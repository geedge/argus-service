package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.dashboard.NtcTotalReport;
import com.nis.domain.restful.dashboard.TrafficAppStatistic;
import com.nis.domain.restful.dashboard.TrafficHttpStatistic;
import com.nis.domain.restful.dashboard.TrafficIpActiveStatistic;
import com.nis.domain.restful.dashboard.TrafficPortActiveStatistic;
import com.nis.domain.restful.dashboard.TrafficProtocolStatistic;
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
		List<Map> totalReportList =new ArrayList<Map>();
		NtcTotalReport maxReportTime = ntcTotalReportDao.getMaxReportTime();
		if(maxReportTime!=null&&maxReportTime.getReportTime()!=null) {
			Date reportTime = maxReportTime.getReportTime();
			totalReportList = ntcTotalReportDao.getTotalReportList(reportTime);
		}
		Map maxRecvtTime = ntcTotalReportDao.getMaxRecvTime();
		List<Map> newData = new ArrayList<Map>();
//		List<Map> oldData = new ArrayList<Map>();
		if(maxRecvtTime!=null&&maxRecvtTime.get("recvTime")!=null) {
			Date recvTime = (Date) maxRecvtTime.get("recvTime");
			newData = ntcTotalReportDao.getNetFlowPortInfoNew(recvTime);
//			oldData = ntcTotalReportDao.getNetFlowPortInfoOld(recvTime);
		}
		//统计带宽的流入流出 单位 五分钟 的 byte
		Double inoctets=0d;
		Double outoctets=0d;
		if(newData!=null&&newData.size()>0&&newData.get(0)!=null){
			Double newInoctets=Double.parseDouble(newData.get(0).get("inoctets").toString()) ;
			Double newOutoctets=Double.parseDouble(newData.get(0).get("outoctets").toString()) ;
			//结果为当前五分钟减去上个五分钟
			inoctets=newInoctets;
			outoctets=newOutoctets;
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
	/**
	 * 最近活跃端口时间五分钟数据
	 * @return
	 */
	public List<Map> getPortActiveList(){
		List<Map> list = new ArrayList<Map>();
		TrafficPortActiveStatistic maxStatTime = trafficPortActiveStatisticDao.getMaxStatTime();
		if(maxStatTime!=null&&maxStatTime.getStatTime()!=null) {
			Date statTime = maxStatTime.getStatTime();
			List<TrafficPortActiveStatistic> portActiveList = trafficPortActiveStatisticDao.getPortActiveList(statTime);
			//上个时间五分钟数据
			if(portActiveList!=null&&portActiveList.size()>0) {
				for (TrafficPortActiveStatistic port : portActiveList) {
					if(port.getPort()!=null) {
						Map map = new HashMap();
						map.put("port", port.getPort());
						map.put("sum", port.getSum());
						TrafficPortActiveStatistic portActiveOld = trafficPortActiveStatisticDao.getPortActiveOld(port.getPort(),statTime);
						if(portActiveOld!=null&&portActiveOld.getSum()!=null){
							map.put("preSum",portActiveOld.getSum());
						}else{
							map.put("preSum",0);
						}
						list.add(map);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 分页查询活跃IPtop100
	 * @return
	 */
/*	public  Page<TrafficIpActiveStatistic> getIpList(Page<TrafficIpActiveStatistic> page,TrafficIpActiveStatistic entry){
			// 设置分页参数
			entry.setPage(page);
			// 执行分页查询
			page.setList(trafficIpActiveStatisticDao.getIpList(entry));
			return page;
	}*/
	public List<HashMap> ipActiveFiveMinute(){
		TrafficIpActiveStatistic maxStatTime = trafficIpActiveStatisticDao.getMaxStatTime();
		ArrayList<HashMap> listMap=new ArrayList<HashMap>();
		if(maxStatTime!=null&&maxStatTime.getStatTime()!=null) {
			Date statTime = maxStatTime.getStatTime();
			ArrayList<LinkedHashMap> list = trafficIpActiveStatisticDao.ipActiveChart(statTime);
			if(list!=null&&list.size()>0) {
				for (LinkedHashMap map : list) {
					HashMap m = new HashMap();
					if(map.get("ipAddr")!=null) {
						String ipAddr = (String) map.get("ipAddr");
						m.put("ipAddr", ipAddr);
						ArrayList<TrafficIpActiveStatistic>  ipList = trafficIpActiveStatisticDao.ipActiveFiveMinute(ipAddr,statTime);
						List linkList = new ArrayList();
						List timeList = new ArrayList();
						if(ipList!=null&&ipList.size()>0) {
							for (TrafficIpActiveStatistic ip : ipList) {
								if(ip.getLinkNum()!=null&&ip.getTime()!=null) {
									linkList.add(ip.getLinkNum());
									timeList.add(ip.getTime());
								}
							}
						}
						m.put("linkNum",linkList);
						m.put("statTime",timeList);
						listMap.add(m);
					}
				}
			}
		}
		
		return listMap;
	}
	public List<HashMap> ipActiveOneHour(){
		TrafficIpActiveStatistic maxStatTime = trafficIpActiveStatisticDao.getMaxStatTime();
		ArrayList<HashMap> listMap=new ArrayList<HashMap>();
		if(maxStatTime!=null&&maxStatTime.getStatTime()!=null) {
			Date statTime = maxStatTime.getStatTime();
			ArrayList<LinkedHashMap> list = trafficIpActiveStatisticDao.ipActiveChart(statTime);
			if(list!=null&&list.size()>0) {
				for (LinkedHashMap map : list) {
					if(map.get("ipAddr")!=null) {
						String ipAddr = (String) map.get("ipAddr");
						ArrayList<HashMap> iplList = trafficIpActiveStatisticDao.ipActiveOneHour(ipAddr,statTime);
						listMap.add(iplList.get(0));
					}
				}
			}
		}
		
		return listMap;
	}
	@SuppressWarnings("rawtypes")
	public List<LinkedHashMap> ipActiveChart(){
		TrafficIpActiveStatistic maxStatTime = trafficIpActiveStatisticDao.getMaxStatTime();
		ArrayList<LinkedHashMap> list = new ArrayList<>();
		if(maxStatTime!=null&&maxStatTime.getStatTime()!=null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficIpActiveStatisticDao.ipActiveChart(statTime);
			if(list!=null&&list.size()>0) {
				for (LinkedHashMap map : list) {
					if(map.get("ipAddr")!=null) {
						Map m = new LinkedHashMap();
						map.put("pktNum",0);
						map.put("byteLen",0);
					}
				}
			}
		}
		return list;
	}

	public List<Map> protocolChart() {
		TrafficProtocolStatistic maxStatTime = trafficProtocolStatisticDao.getMaxStatTime();
		List<Map> list=new ArrayList<Map>();
		if(maxStatTime!=null&&maxStatTime.getStatTime()!=null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficProtocolStatisticDao.protocolChart(statTime);
			//当不查询包，字节时 设为0
			if(list!=null&&list.size()>0) {
				Map map = new HashMap();
				map.put("pktNum",0);
				map.put("byteLen",0);
			}
		}
		return list;
	}

	public List<Map> appChart() {
		List<Map> list=new ArrayList<Map>();
		TrafficAppStatistic maxStatTime = trafficAppStatisticDao.getMaxStatTime();
		if(maxStatTime!=null&&maxStatTime.getStatTime()!=null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficAppStatisticDao.appChart(statTime);
			//当不查询包，字节时 设为0
			if(list!=null&&list.size()>0) {
				Map map = new HashMap();
				map.put("pktNum",0);
				map.put("byteLen",0);
			}
		}
		return list;
	}
	
//	操作系统列表 排名TOP10
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> systemList(){
		List<Map> result = new ArrayList<Map>();
		TrafficUaStatistic maxStatTime = trafficUaStatisticDao.getMaxStatTime();
			if(maxStatTime!=null) {
				Date statTime = maxStatTime.getStatTime();
				List<TrafficUaStatistic> list = trafficUaStatisticDao.systemList(statTime);
				Integer preCount=0;
				for (TrafficUaStatistic ua : list) {
					Map map = new HashMap();
					map.put("osType",ua.getOsType());
					map.put("count",ua.getCount());
					map.put("pktNum",0);
					map.put("byteLen",0);
					preCount = trafficUaStatisticDao.preSystemListCount(ua.getOsType(),statTime);//上个时段的量 用于与现在对比
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
	public List<Map> getBrowserBySystem(Integer osType ){
		List<Map> list = new ArrayList<Map>();
		TrafficUaStatistic maxStatTime = trafficUaStatisticDao.getMaxStatTime();
			if(maxStatTime!=null) {
				Date statTime = maxStatTime.getStatTime();
				list = trafficUaStatisticDao.getBrowserBySystem(osType,statTime);
				List bsType = new ArrayList();
				//查新固定操系统下的除了TOP10以外的others
				if(list!=null&& list.size()>0){
					for (Map map : list) {
						bsType.add(map.get("bsType"));
					}
					Map others = new HashMap();
					others = trafficUaStatisticDao.systemOthers(bsType,osType,statTime);
					if(others!=null&&others.size()>0){
					others.put("bsType", "-1");
					list.add(others);
					}
				}
		}
		return list;
	}

	public List<Map> browserList() {
		List<Map> result = new ArrayList<Map>();
		TrafficUaStatistic maxStatTime = trafficUaStatisticDao.getMaxStatTime();
		if(maxStatTime!=null) {
			Date statTime = maxStatTime.getStatTime();
			List<TrafficUaStatistic> list = trafficUaStatisticDao.browserList(statTime);
			Integer preCount=0;
			if(list!=null&&list.size()>0){
					for (TrafficUaStatistic ua : list) {
						Map map = new HashMap();
						map.put("bsType",ua.getBsType());
						map.put("count",ua.getCount());
						map.put("pktNum",0);
						map.put("byteLen",0);
						preCount = trafficUaStatisticDao.preBrowserListCount(ua.getBsType(),statTime);//上个时段的量 用于与现在对比
						if(preCount!=null){
							map.put("preCount",preCount);
						}else{
							map.put("preCount",0);
						}
						result.add(map);
					}
				}
			}
		return result;
	}
	public List<Map> getSystemBybrowser(Integer bsType ){
		List<Map> list = new ArrayList<Map>();
		TrafficUaStatistic maxStatTime = trafficUaStatisticDao.getMaxStatTime();
		if(maxStatTime!=null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficUaStatisticDao.getSystemBybrowser(bsType,statTime);
			List osType = new ArrayList();
			//查询固定操系统下的除了TOP10以外的others
			if(list!=null&& list.size()>0){
				for (Map map : list) {
					osType.add(map.get("osType"));
				}
				Map others = new HashMap();
				others = trafficUaStatisticDao.browserOthers(osType,bsType,statTime);
				if(others!=null&&others.size()>0){
				others.put("osType", "-1");
				list.add(others);
				}
			}	
		}
		return list;
	}
	public List<Map> websiteList() {
		List<Map> result = new ArrayList<Map>();
		TrafficHttpStatistic maxStatTime = trafficHttpStatisticDao.getMaxStatTime();
		if(maxStatTime!=null) {
			List<TrafficHttpStatistic> list = trafficHttpStatisticDao.websiteList(maxStatTime.getStatTime());
			Integer preCount=0;
			if(list!=null&&list.size()>0){
				for (TrafficHttpStatistic website : list) {
					Map map = new HashMap();
					map.put("webId",website.getWebId());
					map.put("count",website.getCount());
					map.put("pktNum",0);
					map.put("byteLen",0);
					preCount = trafficHttpStatisticDao.preWebsiteListCount(website.getWebId(),maxStatTime.getStatTime());//上个时段的量 用于与现在对比
					if(preCount!=null){
						map.put("preCount",preCount);
					}else{
						map.put("preCount",0);
					}
					result.add(map);
				}
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
		List<Map> list = new ArrayList<Map>();
		TrafficHttpStatistic maxStatTime = trafficHttpStatisticDao.getMaxStatTime();
		if(maxStatTime!=null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficHttpStatisticDao.getDomainByWebsiteServiceId(websiteServiceId,statTime);
			List webIdList = new ArrayList();
			//查询固定网站下的域名除了TOP10以外的others域名
			if(list!=null&& list.size()>0){
				for (Map map : list) {
					webIdList.add(map.get("webId"));
				}
				Map others = new HashMap();
				others = trafficHttpStatisticDao.websiteDomainOthers(webIdList,websiteServiceId,statTime);
				if(others!=null&&others.size()>0){
					others.put("webId", "-1");
					list.add(others);
				}
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
		List<Map> result = new ArrayList<Map>();
		TrafficHttpStatistic maxStatTime = trafficHttpStatisticDao.getMaxStatTime();
		if(maxStatTime!=null) {
			Date statTime = maxStatTime.getStatTime();
			List<TrafficHttpStatistic> websiteList = trafficHttpStatisticDao.getDomainByWebsiteList(statTime);
			if(websiteList!=null&&websiteList.size()>0){
				Integer preCount=0;
				for (TrafficHttpStatistic website : websiteList) {
					Map map = new HashMap();
					map.put("websiteServiceId",website.getWesiteServiceId());
					map.put("count",website.getCount());
					map.put("pktNum",0);
					map.put("byteLen",0);
					preCount = trafficHttpStatisticDao.preWebsiteListCount(website.getWesiteServiceId(),statTime);//上个时段的量 用于与现在对比
					if(preCount!=null){
						map.put("preCount",preCount);
					}else{
						map.put("preCount",0);
					}
					result.add(map);
				}
			}
		}
		return result;
	}
	/**
	 *主题网站分类，域名 
	 * 
	 * **/
	public List<Map> getTopicAndDomainList(){
		List<Map> topicList = new ArrayList<Map>();
		TrafficHttpStatistic maxStatTime = trafficHttpStatisticDao.getMaxStatTime();
		if(maxStatTime!=null) {
			Date statTime = maxStatTime.getStatTime();
			//按照主题分类 获得主题
			topicList = trafficHttpStatisticDao.getDomainByTopicList(statTime);
			if(topicList!=null&&topicList.size()>0){
				for (Map m : topicList) {
					List<Map> domainList= new ArrayList<Map>();
					if(m!=null&&m.get("topicId")!=null){
						domainList = trafficHttpStatisticDao.getDomainByTopicId(m.get("topicId"),statTime);
						m.put("domainData", domainList);
					}
				}
			}	
		}
		return topicList;
	}
}
