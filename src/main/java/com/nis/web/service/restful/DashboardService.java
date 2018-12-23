package com.nis.web.service.restful;

import com.beust.jcommander.internal.Maps;
import com.nis.domain.restful.NtcEntranceReport;
import com.nis.domain.restful.dashboard.*;
import com.nis.util.DateUtils;
import com.nis.util.StringUtils;
import com.nis.web.dao.dashboard.*;
import com.nis.web.service.BaseService;
import com.zdjizhi.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service
public class DashboardService extends BaseService {

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
	 * 流量统计 数据显示 info：先查询最近时间，根据时间条件查询数据 效率提高
	 */
	public List<Map> getTotalReportList() {
		List<Map> totalReportList = new ArrayList<Map>();
		List<Map> newData = new ArrayList<Map>();
		// 流量统计阻断丢弃回流等数据显示最近时间
		NtcTotalReport maxReportTime = ntcTotalReportDao.getMaxReportTime();
		if (maxReportTime != null && maxReportTime.getReportTime() != null) {
			Date reportTime = maxReportTime.getReportTime();
			totalReportList = ntcTotalReportDao.getTotalReportList(reportTime);// 返回阻断，监测等結果
		}
		Map maxRecvtTime = ntcTotalReportDao.getMaxStatTime();
		if (maxRecvtTime != null && maxRecvtTime.get("statTime") != null) {
			Date recvTime = (Date) maxRecvtTime.get("statTime");
			newData = ntcTotalReportDao.getNetFlowPortInfoNew(recvTime);
		}
		// 统计带宽的流入流出 单位 五分钟 的 byte
		if (StringUtil.isNotEmpty(newData)) {
			Double inoctets = Double.parseDouble(newData.get(0).get("inoctets").toString());
			Double outoctets = Double.parseDouble(newData.get(0).get("outoctets").toString());

			if (StringUtil.isEmpty(totalReportList)) {
				Map valueMap = Maps.newHashMap();
				valueMap.put("inoctets", inoctets);
				valueMap.put("outoctets", outoctets);
				totalReportList.add(valueMap);
			} else {
				totalReportList.get(0).put("inoctets", inoctets);
				totalReportList.get(0).put("outoctets", outoctets);
			}
		}
		return totalReportList;
	}

	/**
	 * 根据ip46,协议tcp，udp查询带宽
	 * 
	 * @param addrType
	 * @param transType
	 * @return
	 */
	/*
	 * public List<HashMap> getBandwidthTrans(String addrType, Integer
	 * transType,String beginDate,String endDate) { ArrayList<HashMap> listMap = new
	 * ArrayList<HashMap>(); List<TrafficTransStatistic> bandwidthList = new
	 * ArrayList<TrafficTransStatistic>(); Map maxStatTime =
	 * ntcTotalReportDao.getMaxStatTime(); if (maxStatTime != null &&
	 * maxStatTime.get("statTime") != null) { Date stat = (Date)
	 * maxStatTime.get("statTime"); HashMap m = new HashMap(); bandwidthList =
	 * ntcTotalReportDao.getBandwidthTrans(stat, addrType, transType); List timeList
	 * = new ArrayList(); List linkList = new ArrayList(); List gbpsList = new
	 * ArrayList(); List ppsList = new ArrayList(); if (bandwidthList != null &&
	 * bandwidthList.size() > 0) { for (TrafficTransStatistic tt : bandwidthList) {
	 * if (tt.getTime() != null) { timeList.add(tt.getTime()); if (tt.getLinkNum()
	 * != null) { linkList.add(tt.getLinkNum()); } if (tt.getPps() != null) {
	 * ppsList.add(tt.getPps()); } if (tt.getGbps() != null) {
	 * gbpsList.add(tt.getGbps()); } } } } m.put("linkNum", linkList); m.put("gbps",
	 * gbpsList); m.put("pps", ppsList); m.put("statTime", timeList);
	 * listMap.add(m); } return listMap; }
	 */
	/**
	 * 根据ip46,协议tcp，udp查询带宽
	 * 
	 * @param addrType
	 * @param transType
	 * @return
	 */
	public List<HashMap> getBandwidthTrans(String addrType, Integer transType, String beginDate, String endDate) {
		ArrayList<HashMap> listMap = new ArrayList<HashMap>();

		HashMap m1 = getBandwidthTransEntrance(addrType, transType, beginDate, endDate, 1);
		if (m1 != null && m1.size() > 0) {
			m1.put("entranceId", 1);// 局点1.2 不同来源
			listMap.add(m1);
		}
		return listMap;

	}

	/**
	 * 根据ip46,协议tcp，udp查询带宽
	 * 
	 * @param addrType
	 * @param transType
	 * @return
	 */
	public List<HashMap> getBandwidthTrans2(String addrType, Integer transType, String beginDate, String endDate) {
		ArrayList<HashMap> listMap = new ArrayList<HashMap>();

		HashMap m2 = getBandwidthTransEntrance(addrType, transType, beginDate, endDate, 2);
		if (m2 != null && m2.size() > 0) {
			m2.put("entranceId", 2);
			listMap.add(m2);
		}
		return listMap;

	}

	/**
	 * 查询单个局点流量的数据信息
	 * 
	 * @param addrType
	 * @param transType
	 * @param beginDate
	 * @param endDate
	 * @param entranceId
	 * @return
	 */
	public HashMap getBandwidthTransEntrance(String addrType, Integer transType, String beginDate, String endDate,
			Integer entranceId) {
		long start = System.currentTimeMillis();
		ArrayList<HashMap> listMap = new ArrayList<HashMap>();
		List<TrafficTransStatistic> bandwidthList = new ArrayList<TrafficTransStatistic>();
		HashMap resulMap = new HashMap();
		bandwidthList = ntcTotalReportDao.getBandwidthTrans(entranceId, beginDate, endDate, addrType, transType);
		List timeList = new ArrayList();
		List linkList = new ArrayList();
		List gbpsList = new ArrayList();
		List ppsList = new ArrayList();
		if (bandwidthList != null && bandwidthList.size() > 0) {
			Map<String, Comparable> m = new HashMap<String, Comparable>();
			int inter = 1000 * 60 * 5;// 间隔时间为五分钟
			// 开始时间，结束时间 时间戳
			Long b = dateToStamp(beginDate);
			Long e = dateToStamp(endDate);
			int num = 0;
			Long pointTime = b;
			while (pointTime < e) {
				Map rm = new HashMap();
				Long sumL = 0l;
				Long sumP = 0l;
				Long sumG = 0l;
				if (pointTime >= e) {
					break; // 停止
				}
				for (TrafficTransStatistic tt : bandwidthList) {
					// 实际时间
					String time = tt.getTime();
					Long t = dateToStamp(time);
					if (t >= pointTime && t < pointTime + inter) {
						// 范围之内分到此pointTime组
						sumL = sumL + tt.getLinkNum();
						sumP = sumP + tt.getPps();
						sumG = sumG + tt.getGbps();
					}
				}
				// 在结束时间只有当值大于0时才记录数据，防止折线降为0引起误会
				if (pointTime >= e - inter && sumL > 0) {
					linkList.add(sumL);
				}
				if (pointTime >= e - inter && sumP > 0) {
					ppsList.add(sumP);
				}
				if (pointTime >= e - inter && sumG > 0) {
					gbpsList.add(sumG);
				}
				if (pointTime >= e - inter && (sumL > 0 || sumG > 0 || sumP > 0)) {
					timeList.add(stampToDate(pointTime));
				}
				if (pointTime < e - inter) {
					timeList.add(stampToDate(pointTime));
					linkList.add(sumL);
					gbpsList.add(sumG);
					ppsList.add(sumP);
				}

				num = num + 1;
				pointTime = b + inter * num;
			}
			resulMap.put("linkNum", linkList);
			resulMap.put("gbps", gbpsList);
			resulMap.put("pps", ppsList);
			resulMap.put("statTime", timeList);
		}
		return resulMap;
	}

	/**
	 * 将结果格式为时间间隔相同，数量补0数据
	 * 
	 * @param begin
	 * @param end
	 * @param li
	 * @return
	 */
	public static List<Map> formatDateData(String begin, String end, List<Map> li) {
		List<Map> resList = new ArrayList<Map>();
		Map<String, Comparable> m = new HashMap<String, Comparable>();
		int inter = 1000 * 60 * 60;// 间隔时间为一小时
		// 开始时间，结束时间 时间戳
		Long b = dateToStamp(begin);
		Long e = dateToStamp(end);
		int num = 0;
		Long pointTime = b;
		while (pointTime < e) {
			Map rm = new HashMap();
			Long sum = 0l;
			if (pointTime >= e) {
				break; // 停止
			}
			for (Map<?, ?> map : li) {
				// 实际时间
				String time = (String) map.get("time");
				Long t = dateToStamp(time);
				if (t >= pointTime && t < pointTime + inter) {
					// 范围之内分到此pointTime组
					sum = sum + (Long) map.get("count");
				}
			}
			rm.put("time", stampToDate(pointTime));
			rm.put("sum", sum);
			num = num + 1;
			pointTime = b + inter * num;
			resList.add(rm);
		}
		return resList;
	}

	/**
	 * 日期格式字符串转换成时间戳
	 * 
	 * @param date   字符串日期
	 * @param format 如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Long dateToStamp(String date_str) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(date_str).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0l;
	}

	/**
	 * 时间戳 换成日期格式字符串转
	 * 
	 * @param 时间     1544602212000
	 * @param format 如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String stampToDate(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");// 这个是你要转成后的时间的格式
		String sd = sdf.format(new Date(timeStamp)); // 时间戳转换成时间
		return sd;
	}

	/**
	 * 根据service 动作查询近五分钟变化趋势 entrance 默认为1,2
	 */
/*	
  public List<HashMap> getActionTrans(String begin, String end, String serviceType) {
		String sql = "";
		String sqlBlock = "((service>=16 and service<=40) or (service>=258 and service<=273) or (service=576))"; // 阻断
		String sqlMonitor = "((service>=128 and service<=152) or (service>=384 and service<=513) or (service=592) or (service>=848 and service<=1030) or (service=1152))"; // 监测
		String sqlDrop = "((service>=1040 and service<=1042) or (service=3) or (service=5) or (service=25) or (service=28) or (service=274))"; // 丢弃
		String sqlLoop = "((service=4) or (service>=832 and service<=834))"; // 回流
		// 区分动作
		if (StringUtils.isNotBlank(serviceType)) {
			if (serviceType.equalsIgnoreCase("block")) {
				sql = sqlBlock;
			}
			if (serviceType.equalsIgnoreCase("monitor")) {
				sql = sqlMonitor;
			}
			if (serviceType.equalsIgnoreCase("drop")) {
				sql = sqlDrop;
			}
			if (serviceType.equalsIgnoreCase("loop")) {
				sql = sqlLoop;
			}
		}
		ArrayList<HashMap> listMap = new ArrayList<HashMap>();
		List<NtcEntranceReport> entrance1 = new ArrayList<NtcEntranceReport>();
		List<NtcEntranceReport> entrance2 = new ArrayList<NtcEntranceReport>();
		HashMap m1 = new HashMap();
		HashMap m2 = new HashMap();
		entrance1 = ntcTotalReportDao.getActionTrans(begin, end, 1, sql);
		entrance2 = ntcTotalReportDao.getActionTrans(begin, end, 2, sql);
		List timeList = new ArrayList();
		List sumList1 = new ArrayList();
		List sumList2 = new ArrayList();

		Map<String, Comparable> m = new HashMap<String, Comparable>();
		int inter = 1000 * 60 * 5;// 间隔时间为五分钟
		// 开始时间，结束时间 时间戳
		Long b = dateToStamp(begin);
		Long e = dateToStamp(end);
		int num = 0;
		Long pointTime = b;
		while (pointTime < e) {
			Map rm = new HashMap();
			Long sum1 = 0l;
			Long sum2 = 0l;
			if (pointTime >= e) {
				break; // 停止
			}
			for (NtcEntranceReport e1 : entrance1) {
				// 实际时间
				String time = e1.getTime();
				Long t = dateToStamp(time);
				if (t >= pointTime && t < pointTime + inter) {
					// 范围之内分到此pointTime组
					sum1 = sum1 + e1.getSum();
				}
			}
			for (NtcEntranceReport e2 : entrance2) {
				// 实际时间
				String time = e2.getTime();
				Long t = dateToStamp(time);
				if (t >= pointTime && t < pointTime + inter) {
					// 范围之内分到此pointTime组
					sum2 = sum2 + e2.getSum();
				}
			}
			// 在结束时间只有当值大于0时才记录数据，防止折线降为0引起误会
			if (pointTime >= e - inter && sum1 > 0) {
				sumList1.add(sum1);
			}
			if (pointTime >= e - inter && sum2 > 0) {
				sumList2.add(sum2);
			}
			if (pointTime >= e - inter && (sum1 > 0 || sum2 > 0)) {
				timeList.add(stampToDate(pointTime));
			}
			if (pointTime < e - inter) {
				sumList1.add(sum1);
				sumList2.add(sum2);
				timeList.add(stampToDate(pointTime));
			}
			num = num + 1;
			pointTime = b + inter * num;
		}
		m1.put("count", sumList1);
		m1.put("statTime", timeList);
		m1.put("entranceId", 1);
		m2.put("count", sumList2);//
		m2.put("statTime", timeList);
		m2.put("entranceId", 2);
		listMap.add(m1);
		listMap.add(m2);
		return listMap;
	}
	*/
	/**
	 * 根据service 动作查询近五分钟变化趋势 entrance 默认为1,2
	 */
	public List<HashMap> getActionTrans2(String begin, String end, String serviceType) {
		String sql = "";
		String sqlBlock = "((service>=16 and service<=40) or (service>=258 and service<=273) or (service=576))"; // 阻断
		String sqlMonitor = "((service>=128 and service<=152) or (service>=384 and service<=513) or (service=592) or (service>=848 and service<=1030) or (service=1152))"; // 监测
		String sqlDrop = "((service>=1040 and service<=1042) or (service=3) or (service=5) or (service=25) or (service=28) or (service=274))"; // 丢弃
		String sqlLoop = "((service=4) or (service>=832 and service<=834))"; // 回流
		// 区分动作
		if (StringUtils.isNotBlank(serviceType)) {
			if (serviceType.equalsIgnoreCase("action_reject")) {
				sql = sqlBlock;
			}
			if (serviceType.equalsIgnoreCase("action_monit")) {
				sql = sqlMonitor;
			}
			if (serviceType.equalsIgnoreCase("action_drop")) {
				sql = sqlDrop;
			}
			if (serviceType.equalsIgnoreCase("action_loop")) {
				sql = sqlLoop;
			}
		}
		ArrayList<HashMap> listMap = new ArrayList<HashMap>();
		List<NtcEntranceReport> entrance1 = new ArrayList<NtcEntranceReport>();
		List<NtcEntranceReport> entrance2 = new ArrayList<NtcEntranceReport>();
		HashMap m1 = new HashMap();
		HashMap m2 = new HashMap();
		entrance1 = ntcTotalReportDao.getActionTrans(begin, end, 1, sql);
		entrance2 = ntcTotalReportDao.getActionTrans(begin, end, 2, sql);
		
		Map<String, Comparable> m = new HashMap<String, Comparable>();
		Date beginDate = DateUtils.parseDate(begin);
		Date endDate = DateUtils.parseDate(end);
		
			List<Date> dateRangeList = new ArrayList<Date>();
			List<String> strDateRangeList = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
			while (calendar.getTime().compareTo(endDate)<=0) {
				dateRangeList.add(calendar.getTime());
				strDateRangeList.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.MINUTE, 5);
			}
			//存放每个时间点的总数
			Map<String,List<Long>> ipCountListMap = new HashMap<String, List<Long>>();
			int index=0;
			List sumList1 =new ArrayList();
			List sumList2 =new ArrayList();
			for (int i = 0; i < dateRangeList.size(); i++) {
				//存放一个时间点中总数
				Map<String, Long> ipCountMap = new HashMap<String, Long>();
				Date date = dateRangeList.get(i);
				Long num1 = 0L;
				Long num2 = 0L;
				for (int j = index; j < entrance1.size(); j++) {
					NtcEntranceReport ipInfo = entrance1.get(j);
					if (ipInfo.getReportTime()!=null){
						if(ipInfo.getReportTime().compareTo(date)>=0&&(i+1<dateRangeList.size()&&ipInfo.getReportTime().compareTo(dateRangeList.get(i+1))<0)) {
								num1=num1+ipInfo.getSum();
						}else{
							index = j;
							break;
						}
					}
				}
				sumList1.add(num1);
				for (int j = index; j < entrance2.size(); j++) {
					NtcEntranceReport ipInfo = entrance2.get(j);
					if (ipInfo.getReportTime()!=null){
						if(ipInfo.getReportTime().compareTo(date)>=0&&(i+1<dateRangeList.size()&&ipInfo.getReportTime().compareTo(dateRangeList.get(i+1))<0)) {
							num2=num2+ipInfo.getSum();
						}else{
							index = j;
							break;
						}
					}
				}
				sumList2.add(num2);
			}
			//整合 count time 
			m1.put("count", sumList1);
			m1.put("statTime", strDateRangeList);
			m1.put("entranceId", 1);
			m2.put("count", sumList2);//
			m2.put("statTime", strDateRangeList);
			m2.put("entranceId", 2);
			listMap.add(m1);
			listMap.add(m2);
		return listMap;
	}

	/**
	 * 最近活跃端口时间五分钟数据
	 * 
	 * @return List
	 */
	public List<Map> getPortActiveList() {
		List<Map> list = new ArrayList<Map>();
		TrafficPortActiveStatistic maxStatTime = trafficPortActiveStatisticDao.getMaxStatTime();
		if (maxStatTime != null && maxStatTime.getStatTime() != null) {
			Date statTime = maxStatTime.getStatTime();
			List<TrafficPortActiveStatistic> portActiveList = trafficPortActiveStatisticDao.getPortActiveList(statTime);
			// 上个时间五分钟数据
			if (portActiveList != null && portActiveList.size() > 0) {
				for (TrafficPortActiveStatistic port : portActiveList) {
					if (port.getPort() != null) {
						Map map = new HashMap();
						map.put("port", port.getPort());
						map.put("sum", port.getSum());
						TrafficPortActiveStatistic portActiveOld = trafficPortActiveStatisticDao
								.getPortActiveOld(port.getPort(), statTime);
						if (portActiveOld != null && portActiveOld.getSum() != null) {
							map.put("preSum", portActiveOld.getSum());
						} else {
							map.put("preSum", 0);
						}
						list.add(map);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 活跃IP最近五分钟数据TOP10
	 * 
	 * @return
	 */
	/*
	 * public List<HashMap> ipActiveFiveMinute(Date beginDate,Date endDate) {
	 * TrafficIpActiveStatistic maxStatTime =
	 * trafficIpActiveStatisticDao.getMaxStatTime(); ArrayList<HashMap> listMap =
	 * new ArrayList<HashMap>(); if (maxStatTime != null &&
	 * maxStatTime.getStatTime() != null) { Date statTime =
	 * maxStatTime.getStatTime(); // 查询最近五分钟TOP10 ArrayList<LinkedHashMap>
	 * ipListTop10 = trafficIpActiveStatisticDao.ipActiveChart(beginDate,endDate);
	 * if (ipListTop10 != null && ipListTop10.size() > 0) { for (LinkedHashMap map :
	 * ipListTop10) { HashMap m = new HashMap(); if (map.get("ipAddr") != null) {
	 * String ipAddr = (String) map.get("ipAddr"); m.put("ipAddr", ipAddr); //
	 * 根据五分钟TOP10IP，查询TOP10中每个IP最近一小时的变化 long beforeTime = beginDate.getTime(); long
	 * afterTime = endDate.getTime(); Long inter=(afterTime - beforeTime) / (1000 *
	 * 60 * 5); ArrayList<TrafficIpActiveStatistic> ipList =
	 * trafficIpActiveStatisticDao.ipActiveFiveMinute(inter,ipAddr,
	 * beginDate,endDate); List linkList = new ArrayList(); List timeList = new
	 * ArrayList(); if (ipList != null && ipList.size() > 0) { for
	 * (TrafficIpActiveStatistic ip : ipList) { if (ip.getLinkNum() != null &&
	 * ip.getTime() != null) { linkList.add(ip.getLinkNum());
	 * timeList.add(ip.getTime()); } } } m.put("linkNum", linkList);
	 * m.put("statTime", timeList); listMap.add(m); } } } }
	 * 
	 * return listMap; }
	 */
	/**
	 * 获取活跃IPtop10 的趋势图
	 */
	public List<Map> ipActiveFiveMinute(Date beginDate, Date endDate) {
		List<Map> listMap = new ArrayList<Map>();
		List<LinkedHashMap> ipListTop10 = trafficIpActiveStatisticDao.ipActiveChart(beginDate, endDate);
		if (ipListTop10 != null && ipListTop10.size() > 0) {
			StringBuilder ipStr = new StringBuilder();
			for (LinkedHashMap map : ipListTop10) {
				ipStr.append("'"+(String) map.get("ipAddr")+"',");
			}
			if (!StringUtil.isEmpty(ipStr)&&ipStr.indexOf(",")>0) {
				ipStr.deleteCharAt(ipStr.lastIndexOf(","));
			}
			//如果所有IP在最后一个时间点的count都为0，则移除最后一个点
			//开始划分时间段,间隔5分钟
			List<Date> dateRangeList = new ArrayList<Date>();
			List<String> strDateRangeList = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
			while (calendar.getTime().compareTo(endDate)<=0) {
				dateRangeList.add(calendar.getTime());
				strDateRangeList.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.MINUTE, 5);
			}
			
			ArrayList<TrafficIpActiveStatistic> ipInfoList = trafficIpActiveStatisticDao.ipActiveFiveMinute(ipStr.toString(),
				beginDate, endDate);
			//存放每个IP，每个时间点的总数
			Map<String,List<Long>> ipCountListMap = new HashMap<String, List<Long>>();
			int index=0;
			for (int i = 0; i < dateRangeList.size(); i++) {
				//存放一个时间点中每个IP的总数
				Map<String, Long> ipCountMap = new HashMap<String, Long>();
				Date date = dateRangeList.get(i);
				for (int j = index; j < ipInfoList.size(); j++) {
					TrafficIpActiveStatistic ipInfo = ipInfoList.get(j);
					if (ipInfo.getStatTime()!=null){
						if(ipInfo.getStatTime().compareTo(date)>=0&&(i+1<dateRangeList.size()&&ipInfo.getStatTime().compareTo(dateRangeList.get(i+1))<0)) {
							Long num = ipInfo.getCount();
							if (ipCountMap.containsKey(ipInfo.getIpAddr())) {
								num=num+ipCountMap.get(ipInfo.getIpAddr());
							}
							ipCountMap.put(ipInfo.getIpAddr(), num);
						}else{
							index = j;
							break;
						}
					}
				}
				for (LinkedHashMap map : ipListTop10) {
					String ip = (String) map.get("ipAddr");
					Long sum = 0l;
					if (ipCountMap.containsKey(ip)) {
						sum=ipCountMap.get(ip);
					}
					if (ipCountListMap.containsKey(ip)) {
						ipCountListMap.get(ip).add(sum);
					}else{
						List<Long> list = new ArrayList<Long>();
						list.add(sum);
						ipCountListMap.put(ip, list);
					}
				}
			}
			//整合IP count time 
			Iterator iterator = ipCountListMap.keySet().iterator();
			while (iterator.hasNext()) {
				String ip = iterator.next()+"";
				Map ipTrafficMap = new HashMap();
				ipTrafficMap.put("ipAddr", ip);
				ipTrafficMap.put("linkNum", ipCountListMap.get(ip));
				ipTrafficMap.put("statTime", strDateRangeList);
				listMap.add(ipTrafficMap);
			}
		}
		return listMap;
	}

	/**
	 * 根据活跃IP最近五分钟TOP10，查询近1小时最大值
	 * 
	 * @return
	 */
	public List<HashMap> ipActiveOneHour(Date beginDate, Date endDate) {
		TrafficIpActiveStatistic maxStatTime = trafficIpActiveStatisticDao.getMaxStatTime();
		ArrayList<HashMap> listMap = new ArrayList<HashMap>();
		if (maxStatTime != null && maxStatTime.getStatTime() != null) {
			Date statTime = maxStatTime.getStatTime();
			ArrayList<LinkedHashMap> list = trafficIpActiveStatisticDao.ipActiveChart(beginDate, endDate);
			if (list != null && list.size() > 0) {
				for (LinkedHashMap map : list) {
					if (map.get("ipAddr") != null) {
						String ipAddr = (String) map.get("ipAddr");
						HashMap resMap = trafficIpActiveStatisticDao.ipActiveOneHour(ipAddr, beginDate,endDate);
						if(resMap!=null){
							listMap.add(resMap);
						}
					}
				}
			}
		}

		return listMap;
	}

	/**
	 * 活跃IP首页top10
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<LinkedHashMap> ipActiveChart(Date beginDate, Date endDate) {
//		TrafficIpActiveStatistic maxStatTime = trafficIpActiveStatisticDao.getMaxStatTime();
		ArrayList<LinkedHashMap> list = new ArrayList<>();
//		if (maxStatTime != null && maxStatTime.getStatTime() != null) {
//			Date statTime = maxStatTime.getStatTime();
		list = trafficIpActiveStatisticDao.ipActiveChart(beginDate, endDate);
		if (list != null && list.size() > 0) {
			for (LinkedHashMap map : list) {
				if (map.get("ipAddr") != null) {
					Map m = new LinkedHashMap();
					map.put("pktNum", 0);
					map.put("byteLen", 0);
				}
			}
		}
//		}
		return list;
	}

	/**
	 * 协议统计
	 * 
	 * @return
	 */
	public List<Map> protocolChart() {
		TrafficProtocolStatistic maxStatTime = trafficProtocolStatisticDao.getMaxStatTime();
		List<Map> list = new ArrayList<Map>();
		if (maxStatTime != null && maxStatTime.getStatTime() != null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficProtocolStatisticDao.protocolChart(statTime);
			// 当不查询包，字节时 设为0
			if (list != null && list.size() > 0) {
				Map map = new HashMap();
				map.put("pktNum", 0);
				map.put("byteLen", 0);
			}
		}
		return list;
	}

	/**
	 * 协议统计报表
	 * 
	 * @return
	 */
	public List<Map> getProtocolList(String startTime, String endTime,Integer[] protoType) {
//		TrafficProtocolStatistic maxStatTime = trafficProtocolStatisticDao.getMaxStatTime();
		List<Map> list = new ArrayList<Map>();
//		if (maxStatTime != null && maxStatTime.getStatTime() != null) {
//			Date statTime = maxStatTime.getStatTime();
			list = trafficProtocolStatisticDao.getProtocolList(startTime, endTime,protoType);
//		}
		return list;
	}

	/**
	 * app应用top10 图
	 * 
	 * @return
	 */
	public List<Map> appChart() {
		List<Map> list = new ArrayList<Map>();
		TrafficAppStatistic maxStatTime = trafficAppStatisticDao.getMaxStatTime();
		if (maxStatTime != null && maxStatTime.getStatTime() != null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficAppStatisticDao.appChart(statTime);
			// 当不查询包，字节时 设为0
			if (list != null && list.size() > 0) {
				Map map = new HashMap();
				map.put("pktNum", 0);
				map.put("byteLen", 0);
			}
		}
		return list;
	}

	/**
	 * App统计报表
	 * 
	 * @return
	 */
	public List<Map> getAppList(String startTime, String endTime, Integer[] appType) {
//		TrafficAppStatistic maxStatTime = trafficAppStatisticDao.getMaxStatTime();
		List<Map> list = new ArrayList<Map>();
//		if (maxStatTime != null && maxStatTime.getStatTime() != null) {
//			Date statTime = maxStatTime.getStatTime();
		list = trafficAppStatisticDao.getAppList(startTime, endTime, appType);
//		}
		return list;
	}

//	操作系统列表 排名TOP10
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> systemList() {
		List<Map> result = new ArrayList<Map>();
		TrafficUaStatistic maxStatTime = trafficUaStatisticDao.getMaxStatTime();
		if (maxStatTime != null) {
			Date statTime = maxStatTime.getStatTime();
			List<TrafficUaStatistic> list = trafficUaStatisticDao.systemList(statTime);
			Long preCount = 0l;
			for (TrafficUaStatistic ua : list) {
				Map map = new HashMap();
				map.put("osType", ua.getOsType());
				map.put("count", ua.getCount());
				map.put("pktNum", 0);
				map.put("byteLen", 0);
				preCount = trafficUaStatisticDao.preSystemListCount(ua.getOsType(), statTime);// 上个时段的量 用于与现在对比
				if (preCount != null) {
					map.put("preCount", preCount);
				} else {
					map.put("preCount", 0);
				}
				result.add(map);
			}
		}
		return result;
	}

	public List<Map> getBrowserBySystem(Integer osType) {
		List<Map> list = new ArrayList<Map>();
		TrafficUaStatistic maxStatTime = trafficUaStatisticDao.getMaxStatTime();
		if (maxStatTime != null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficUaStatisticDao.getBrowserBySystem(osType, statTime);
			List bsType = new ArrayList();
			// 查新固定操系统下的除了TOP10以外的others
			if (list != null && list.size() > 0) {
				for (Map map : list) {
					bsType.add(map.get("bsType"));
				}
				if (list.size() > 10) {
					Map others = new HashMap();
					others = trafficUaStatisticDao.systemOthers(bsType, osType, statTime);
					if (others != null && others.size() > 0) {
						others.put("bsType", "-1");
						list.add(others);
					}
				}
			}
		}
		return list;
	}

	public List<Map> browserList() {
		List<Map> result = new ArrayList<Map>();
		TrafficUaStatistic maxStatTime = trafficUaStatisticDao.getMaxStatTime();
		if (maxStatTime != null) {
			Date statTime = maxStatTime.getStatTime();
			List<TrafficUaStatistic> list = trafficUaStatisticDao.browserList(statTime);
			Long preCount = 0l;
			if (list != null && list.size() > 0) {
				for (TrafficUaStatistic ua : list) {
					Map map = new HashMap();
					map.put("bsType", ua.getBsType());
					map.put("count", ua.getCount());
					map.put("pktNum", 0);
					map.put("byteLen", 0);
					preCount = trafficUaStatisticDao.preBrowserListCount(ua.getBsType(), statTime);// 上个时段的量 用于与现在对比
					if (preCount != null) {
						map.put("preCount", preCount);
					} else {
						map.put("preCount", 0);
					}
					result.add(map);
				}
			}
		}
		return result;
	}

	public List<Map> getSystemBybrowser(Integer bsType) {
		List<Map> list = new ArrayList<Map>();
		TrafficUaStatistic maxStatTime = trafficUaStatisticDao.getMaxStatTime();
		if (maxStatTime != null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficUaStatisticDao.getSystemBybrowser(bsType, statTime);
			List osType = new ArrayList();
			// 查询固定操系统下的除了TOP10以外的others
			if (list != null && list.size() > 0) {
				for (Map map : list) {
					osType.add(map.get("osType"));
				}
				if (list.size() > 10) {
					Map others = new HashMap();
					others = trafficUaStatisticDao.browserOthers(osType, bsType, statTime);
					if (others != null && others.size() > 0) {
						others.put("osType", "-1");
						list.add(others);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * 网站子域名详情
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<Map> getWebsiteDetailsById(Integer websiteServiceId, Date beginDate, Date endDate) {
		List<Map> list = null;
		if (beginDate != null && endDate != null) {
			list = getDomainByWebsiteServiceId(websiteServiceId, beginDate, endDate, null);
		} else {
			Date startTime = getBeforeByHourTime(1);// 获取上一个小时
			list = getDomainByWebsiteServiceId(websiteServiceId, startTime, new Date(), "top");
		}
		return list;
	}

	/**
	 * 根据网站服务查询子域名
	 * 
	 * @param websiteServiceId
	 * @return list
	 */
	public List<Map> getDomainByWebsiteServiceId(Integer websiteServiceId, Date beginDate, Date endDate, String top) {

//		Date statTime = getBeforeByHourTime(1);// 获取上一个小时

		List idList = trafficHttpStatisticDao.getIdByWebSiteId(websiteServiceId);

		List<Map> matchList = trafficHttpStatisticDao.getDomainByWebsiteServiceId(idList, beginDate, endDate);// 获取webid和count的对应关系,key是webid,val是count
// 获取webid和count的对应关系,key是webid,val是count
		Set<Long> set = new HashSet<>();
		Map<Long, List<Map>> countMap = new HashMap();// 存储count和map的对应关系,后面根据count过滤,获取top10的count
		for (Map map : matchList) {
			String countStr = String.valueOf(map.get("count"));
			long count = Long.parseLong(countStr);
			set.add(count);
			if (countMap.containsKey(count)) {// 将每个count和对应的viewmap放到map中,count可能相同所以value是list
				countMap.get(count).add(map);
			} else {
				List<Map> listMap = new ArrayList<>();
				listMap.add(map);
				countMap.put(count, listMap);
			}
		}
		List<Map> top10Data = new ArrayList();
		if (top != null) {
			top10Data = getTop10Data(set, countMap, 1);
		} else {
			top10Data = getTop10Data(set, countMap, 2);
		}
		List<String> notTop10List = new ArrayList<>();// 获取不在top10中的webid,用来查询标记为orther
		for (Map map : matchList) {
			String webIdStr = String.valueOf(map.get("webId"));
			boolean exist = false;
			for (Map map1 : top10Data) {
				String webIdStr1 = String.valueOf(map1.get("webId"));
				if (webIdStr1 != null && webIdStr != null && webIdStr1.equals(webIdStr)) {
					exist = true;
				}
				if (!exist) {
					notTop10List.add(webIdStr);
				}
			}
		}

		// 查询固定网站下的域名除了TOP10以外的others域名
		if (matchList != null && matchList.size() > 10) {
			Map others = trafficHttpStatisticDao.websiteDomainOthers(notTop10List, beginDate, endDate);
			if (others != null && others.size() > 0) {
				others.put("webId", "-1");
				top10Data.add(others);
			}
		}
		return top10Data;

	}

	/**
	 * 
	 * 网站详情
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<Map> getWebsiteDetails(Date beginDate, Date endDate, Integer domain[], Integer entranceId) {
		List<Map> list = trafficHttpStatisticDao.getTrafficHttpDomain(beginDate, endDate, domain, entranceId);
		return list;
	}

	/**
	 * 
	 * 网站TOP10 默认一小时
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<Map> getWebsiteTop10() {
		Date startTime = getBeforeByHourTime(1);// 获取上一个小时
		List<Map> list = getDomainByWebsiteList(startTime, new Date(), "top");
		return list;
	}

	/**
	 * 获取网站列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getDomainByWebsiteList(Date beginDate, Date endDate, String top) {
		Map<String, List<String>> websiteIdAndIdMap = new HashMap<>();// 存储websiteServiceId和id的对应关系,一个websiteServiceId有多个id
		List<Map> websiteIdAndidList = trafficHttpStatisticDao.getDomainByWebsiteList(beginDate, endDate);// 获取website_service_id和id的对应关系,group
																											// by
																											// website_service_id,id
		for (Map map : websiteIdAndidList) {
			Object websiteServiceIdObj = map.get("websiteServiceId");
			Object idObj = map.get("id");
			if (websiteServiceIdObj != null && idObj != null) {
				if (websiteIdAndIdMap.containsKey(String.valueOf(websiteServiceIdObj))) {
					websiteIdAndIdMap.get(String.valueOf(websiteServiceIdObj)).add(String.valueOf(idObj));
				} else {
					List<String> list = new ArrayList<>();
					list.add(String.valueOf(idObj));
					websiteIdAndIdMap.put(String.valueOf(websiteServiceIdObj), list);
				}
			}
		}

		Set<Long> set = new HashSet<>();
		Map<Long, List<Map>> countAndViewMap = new HashMap<>();// 存储count和map的对应关系,后面根据count过滤,获取top10的count
		List<Map> webIdAndCountList = trafficHttpStatisticDao.getHttpStatisticNoLinkAndPkt(beginDate, endDate);// 获取最近一小时的webid和count的关系
		List<Map> prevWebIdAndCountList = trafficHttpStatisticDao.getHttpStatisticNoLinkAndPkt(getBeforeByHourTime(2),
				beginDate);// 获取最近一小时的webid和count的关系
		for (String websiteServiceId : websiteIdAndIdMap.keySet()) {// 遍历上面获取的websiteServiceId和id的对应关系,拼接json
			Map viewMap = new HashMap<>();
			long count = 0l;// 记录当前websiteServiceId所有的count
			long prevCount = 0l;// 记录当前websiteServiceId所有的count
			List<String> idList = websiteIdAndIdMap.get(websiteServiceId);// 根据websiteServiceId获取对应的id
			for (String id : idList) {
				for (Map webIdAndCountMap : webIdAndCountList) {// 遍历webid和count
					String webId = String.valueOf(webIdAndCountMap.get("webId"));
					if (webId != null && webId.equals(id)) {// 如果webid和id相等则获取count数据并统计
						String countStr = String.valueOf(webIdAndCountMap.get("byteCount"));
						if (countStr != null) {
							count += Long.parseLong(countStr);// 将count累加
						}
					}
				}
				for (Map webIdAndCountMap : prevWebIdAndCountList) {// 遍历webid和count
					String webId = String.valueOf(webIdAndCountMap.get("webId"));
					if (webId != null && webId.equals(id)) {// 如果webid和id相等则获取count数据并统计
						String countStr = String.valueOf(webIdAndCountMap.get("byteCount"));
						if (countStr != null) {
							prevCount += Long.parseLong(countStr);// 将count累加
						}
					}
				}

			}
			viewMap.put("websiteServiceId", websiteServiceId);
			viewMap.put("count", count);
			viewMap.put("pktNum", 0);
			viewMap.put("byteLen", 0);
			viewMap.put("preCount", prevCount);

			if (countAndViewMap.containsKey(count)) {// 将每个count和对应的viewmap放到map中,count可能相同所以value是list
				countAndViewMap.get(count).add(viewMap);
			} else {
				List<Map> listMap = new ArrayList<>();
				listMap.add(viewMap);
				countAndViewMap.put(count, listMap);
			}
			set.add(count);
		}
		List<Map> dataList = new ArrayList();
		if (top != null) {
			dataList = getTop10Data(set, countAndViewMap, 1);// 取top10
		} else {
			dataList = getTop10Data(set, countAndViewMap, 2);
		}
		return dataList;
	}

	/**
	 * 
	 * 主题详情
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<Map> getTopicDetails(Date beginDate, Date endDate) {
		List<Map> topicAndDomainList = getTopicAndDomainList(beginDate, endDate, null);
		return topicAndDomainList;
	}

	/**
	 * 
	 * 主题TOP10 默认一小时
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<Map> getTopicTop10() {
		Date startTime = getBeforeByHourTime(1);// 获取上一个小时
		List<Map> topicAndDomainList = getTopicAndDomainListNoLink(startTime, new Date(), "top");
		return topicAndDomainList;
	}

	/**
	 * 主题网站分类，域名
	 * 
	 **/
	public List<Map> getTopicAndDomainListNoLink(Date beginDate, Date endDate, String top) {

		Map<String, List<String>> topicIdAndIdMap = new HashMap<>();// 存储topicid和id的对应关系,一个topicid有多个id
		List<Map> topicIdAndIdList = trafficHttpStatisticDao.getDomainByTopicList(beginDate, endDate);// 获取最近一个小时topicId和id的对应关系,group
		for (Map map : topicIdAndIdList) {
			Object topicIdObj = map.get("topicId");
			Object idObj = map.get("id");
			if (topicIdObj != null && idObj != null) {
				if (topicIdAndIdMap.containsKey(String.valueOf(topicIdObj))) {
					topicIdAndIdMap.get(String.valueOf(topicIdObj)).add(String.valueOf(idObj));
				} else {
					List<String> list = new ArrayList<>();
					list.add(String.valueOf(idObj));
					topicIdAndIdMap.put(String.valueOf(topicIdObj), list);
				}
			}
		}

		Set<Long> set = new HashSet<>();
		Map<Long, List<Map>> countAndViewMap = new HashMap<>();// 存储count和map的对应关系,后面根据count过滤,获取top10的count
		List<Map> webIdAndCountList = trafficHttpStatisticDao.getHttpStatisticNoLinkAndPkt(beginDate, endDate);// 获取最近一小时的webid和count的关系
		for (String topicId : topicIdAndIdMap.keySet()) {// 遍历上面获取的topicid和id的对应关系,拼接json
			Map viewMap = new HashMap<>();

			List<Map> list = new ArrayList<>();
			long count = 0l;// 记录当前topicid所有的count
//			long linkNum = 0l;
//			long packets = 0l;
			List<String> idList = topicIdAndIdMap.get(topicId);// 根据topicid获取对应的id
			for (String id : idList) {
				for (Map webIdAndCountMap : webIdAndCountList) {// 遍历webid和count
					String webId = String.valueOf(webIdAndCountMap.get("webId"));
					if (webId != null && webId.equals(id)) {// 如果webid和id相等则获取count数据并统计
						String countStr = String.valueOf(webIdAndCountMap.get("byteCount"));
//						String linkNumStr = String.valueOf(webIdAndCountMap.get("linkNum"));
//						String packetsStr = String.valueOf(webIdAndCountMap.get("pktCount"));
						if (countStr != null) {
							Long pl = (Long.parseLong(countStr))/(1024*1024*1024);
							if(pl>0) {
								count += Long.parseLong(countStr);// 将count累加 转换单位小于0的不加
							}
						}
//						if (linkNumStr != null) {
//							linkNum += Long.parseLong(linkNumStr);// 将count累加
//						}
//						if (packetsStr != null) {
//							packets += Long.parseLong(packetsStr);// 将count累加
//						}
//						webIdAndCountMap.remove("linkNum");
//						webIdAndCountMap.remove("pktCount");
						list.add(webIdAndCountMap);
					}
				}
			}
			Collections.sort(list, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					if(o1==null&&o2!=null){
						return 1;
					}
					if(o1!=null&&o2==null){
						return -1;
					}
					if(o1==o2&&o1==null){
						return 0;
					}
					Map m1=(Map)o1;
					Map m2=(Map)o2;
					Object ob1 = m1.get("byteCount");
					Object ob2 = m2.get("byteCount");
					Long b1=Long.parseLong(ob1.toString());
					Long b2=Long.parseLong(ob2.toString());
					return -(b1).compareTo(b2);
				}
			});
			if (list.size() > 0) {
				viewMap.put("count", count);
//				viewMap.put("linkNum", linkNum);
//				viewMap.put("packets", packets);
				viewMap.put("domainData", list);
				viewMap.put("topicId", topicId);
				if (countAndViewMap.containsKey(count)) {// 将每个count和对应的viewmap放到map中,count可能相同所以value是list
					countAndViewMap.get(count).add(viewMap);
				} else {
					List<Map> listMap = new ArrayList<>();
					listMap.add(viewMap);
					countAndViewMap.put(count, listMap);
				}
				set.add(count);
			}
		}
		List<Map> dataList = new ArrayList();
		if (top != null) {
			dataList = getTop10Data(set, countAndViewMap, 1);
		} else {
			dataList = getTop10Data(set, countAndViewMap, 2);
		}
		return dataList;
	}

	
	
	
	
	
	public List<Map> getTopicAndDomainList(Date beginDate, Date endDate, String top) {

		Map<String, List<String>> topicIdAndIdMap = new HashMap<>();// 存储topicid和id的对应关系,一个topicid有多个id
		List<Map> topicIdAndIdList = trafficHttpStatisticDao.getDomainByTopicList(beginDate, endDate);// 获取最近一个小时topicId和id的对应关系,group
		for (Map map : topicIdAndIdList) {
			Object topicIdObj = map.get("topicId");
			Object idObj = map.get("id");
			if (topicIdObj != null && idObj != null) {
				if (topicIdAndIdMap.containsKey(String.valueOf(topicIdObj))) {
					topicIdAndIdMap.get(String.valueOf(topicIdObj)).add(String.valueOf(idObj));
				} else {
					List<String> list = new ArrayList<>();
					list.add(String.valueOf(idObj));
					topicIdAndIdMap.put(String.valueOf(topicIdObj), list);
				}
			}
		}

		Set<Long> set = new HashSet<>();
		Map<Long, List<Map>> countAndViewMap = new HashMap<>();// 存储count和map的对应关系,后面根据count过滤,获取top10的count
		List<Map> webIdAndCountList = trafficHttpStatisticDao.getTrafficHttpStatistic(beginDate, endDate);// 获取最近一小时的webid和count的关系
		for (String topicId : topicIdAndIdMap.keySet()) {// 遍历上面获取的topicid和id的对应关系,拼接json
			Map viewMap = new HashMap<>();
			List<Map> list = new ArrayList<>();
			long count = 0l;// 记录当前topicid所有的count
			long linkNum = 0l;
			long packets = 0l;
			List<String> idList = topicIdAndIdMap.get(topicId);// 根据topicid获取对应的id
			for (String id : idList) {
				for (Map webIdAndCountMap : webIdAndCountList) {// 遍历webid和count
					String webId = String.valueOf(webIdAndCountMap.get("webId"));
					if (webId != null && webId.equals(id)) {// 如果webid和id相等则获取count数据并统计
						String countStr = String.valueOf(webIdAndCountMap.get("byteCount"));
						String linkNumStr = String.valueOf(webIdAndCountMap.get("linkNum"));
						String packetsStr = String.valueOf(webIdAndCountMap.get("pktCount"));
						if (countStr != null) {
							count += Long.parseLong(countStr);// 将count累加
						}
						if (linkNumStr != null) {
							linkNum += Long.parseLong(linkNumStr);// 将count累加
						}
						if (packetsStr != null) {
							packets += Long.parseLong(packetsStr);// 将count累加
						}
						webIdAndCountMap.remove("linkNum");
						webIdAndCountMap.remove("pktCount");
						list.add(webIdAndCountMap);
					}
				}
			}
			if (list.size() > 0) {
				viewMap.put("count", count);
				viewMap.put("linkNum", linkNum);
				viewMap.put("packets", packets);
				viewMap.put("domainData", list);
				viewMap.put("topicId", topicId);
				if (countAndViewMap.containsKey(count)) {// 将每个count和对应的viewmap放到map中,count可能相同所以value是list
					countAndViewMap.get(count).add(viewMap);
				} else {
					List<Map> listMap = new ArrayList<>();
					listMap.add(viewMap);
					countAndViewMap.put(count, listMap);
				}
				set.add(count);
			}
		}
		List<Map> dataList = new ArrayList();
		if (top != null) {
			dataList = getTop10Data(set, countAndViewMap, 1);
		} else {
			dataList = getTop10Data(set, countAndViewMap, 2);
		}
		return dataList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String[] getFiledTypeByName(String fieldName) {
		if (fieldName == null) {
			return null;
		}
		String fieldType = null;
		if ("addr_type".equals(fieldName)) {
			fieldType = ntcTotalReportDao.getAddrType();
		}
		if ("trans_type".equals(fieldName)) {
			fieldType = ntcTotalReportDao.getTransType();
		}
		if ("entrance_id".equals(fieldName)) {
			fieldType = ntcTotalReportDao.getEntranceId();
		}
		return fieldType == null ? null : fieldType.split(",");
	}

	public Map<String, Map> getBandwidthTransEntrance(String beginDate, String endDate) throws ParseException {
		Map<String, Map> resultMap = new HashMap<String, Map>();
		Map<String, ArrayList<TrafficTransStatistic>> listMap = new HashMap<String, ArrayList<TrafficTransStatistic>>();
		List<TrafficTransStatistic> bandwidthListIPvx = new ArrayList<TrafficTransStatistic>();
		List<TrafficTransStatistic> bandwidthListProtocol = new ArrayList<TrafficTransStatistic>();
		bandwidthListIPvx = ntcTotalReportDao.getBandwidthTransIPVx(beginDate, endDate);
		bandwidthListProtocol = ntcTotalReportDao.getBandwidthTransProtocol(beginDate, endDate);
		Map<String, Map> trafficMap = new HashMap<String, Map>();
		if((!StringUtil.isEmpty(bandwidthListIPvx)&&bandwidthListIPvx.size()>0)||(!StringUtil.isEmpty(bandwidthListProtocol)&&bandwidthListProtocol.size()>0)){
			//划分时间段
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = sdf.parse(beginDate);
			Date endTime = sdf.parse(endDate);
			List<Date> dateRangeList = new ArrayList<Date>();
			List<String> strDateRangeList = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTime);
			
			while (calendar.getTime().compareTo(endTime)<=0) {
				dateRangeList.add(calendar.getTime());
				strDateRangeList.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.MINUTE, 5);
			}
			String[] addrTypes = getFiledTypeByName("addr_type");
			String[] transTypes = getFiledTypeByName("trans_type");
			String[] entranceIds = getFiledTypeByName("entrance_id");
			if(bandwidthListIPvx!=null&&bandwidthListIPvx.size()>0&&bandwidthListProtocol!=null&&bandwidthListProtocol.size()>0&&addrTypes!=null&&addrTypes.length>0&&transTypes!=null&&transTypes.length>0
					&&entranceIds!=null&&entranceIds.length>0){
				//按区域和类型构建Map
				for (String entranceId : entranceIds) {
					for (String addrType : addrTypes) {
						listMap.put("ipv" + addrType + "Type" + entranceId, new ArrayList<TrafficTransStatistic>());
					}
				}
				for (String entranceId : entranceIds) {
					for (String transType : transTypes) {
						listMap.put("trans" + transType + "Type" + entranceId, new ArrayList<TrafficTransStatistic>());
					}
				}
				//按区域和类型提取数据到列表中
				for (TrafficTransStatistic tts : bandwidthListIPvx) {
					String addrType = tts.getAddrType();
					
					Integer entranceId = tts.getEntranceId();
					Integer transType = tts.getTransType();
					String key = "ipv" + addrType + "Type" + entranceId;
					listMap.get(key).add(tts);
				}
				for (TrafficTransStatistic tts : bandwidthListProtocol) {
					String addrType = tts.getAddrType();
					Integer entranceId = tts.getEntranceId();
					Integer transType = tts.getTransType();
					String key = "trans" + transType + "Type" + entranceId;
					listMap.get(key).add(tts);
				}
				Set<Entry<String, ArrayList<TrafficTransStatistic>>> entrySet = listMap.entrySet();
				for (Entry<String, ArrayList<TrafficTransStatistic>> entry : entrySet) {
					String key = entry.getKey();
					String entranceId = key.substring(key.length() - 1);
					ArrayList<TrafficTransStatistic> value = entry.getValue();
					resultMap.put(key, getNewData(dateRangeList,strDateRangeList,value));
				}
				return resultMap;
			}
		}
		return null;
	}

	private Map getNewData(List<Date> dateRangeList,List<String> timeList, List<TrafficTransStatistic> bandwidthList) {
		Map<String,List> resulMap = new HashMap<String,List>();
		int index = 0;
		for (int i = 0; i < dateRangeList.size(); i++) {
			Date date = dateRangeList.get(i);
			Map<String, Long> countMap = new HashMap<String, Long>();
			Long linkNum = 0L;
			Long ppsNum = 0L;
			Long gbpsNum = 0L;
			for (int j = index; j < bandwidthList.size(); j++) {
				TrafficTransStatistic info = bandwidthList.get(j);
				if (info.getStatTime()!=null){
					if(info.getStatTime().compareTo(date)>=0&&(i+1<dateRangeList.size()&&info.getStatTime().compareTo(dateRangeList.get(i+1))<0)) {
						linkNum =info.getLinkNum() +linkNum;
						ppsNum  = info.getPps()+ppsNum;
						gbpsNum  = info.getGbps()+gbpsNum;
					}else{
						index = j;
						break;
					}
				}
			}
			countMap.put("linkNum", linkNum);
			countMap.put("pps", ppsNum);
			countMap.put("gbps", gbpsNum);
			Iterator iterator = countMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next()+"";
				if (resulMap.containsKey(key)) {
					resulMap.get(key).add(countMap.get(key));
				}else {
					List<Long> list = new ArrayList<Long>();
					list.add(countMap.get(key));
					resulMap.put(key, list);
				}
			}
		}
		resulMap.put("statTime", timeList);
		return resulMap;
	}

	/**
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param domain
	 * @param entranceId
	 * @return
	 */
	/*
	 * public List<HashMap> getDomainTrans(String beginDate, String endDate,
	 * Integer domain, Integer entranceId) { List<HashMap> listMap = new
	 * ArrayList<HashMap>(); HashMap resulMap = new HashMap();
	 * List<TrafficHttpFocusStatistic> domainList = new
	 * ArrayList<TrafficHttpFocusStatistic>(); domainList =
	 * trafficHttpStatisticDao.getDomainTrans(entranceId, beginDate, endDate,
	 * domain); List timeList = new ArrayList(); List countList = new
	 * ArrayList(); if (domainList != null && domainList.size() > 0) {
	 * Map<String, Comparable> m = new HashMap<String, Comparable>(); int inter
	 * = 1000 * 60 * 30;// 间隔时间为30分钟 // 开始时间，结束时间 时间戳 Long b =
	 * dateToStamp(beginDate); Long e = dateToStamp(endDate); int num = 0; Long
	 * pointTime = b; while (pointTime < e) { Map rm = new HashMap(); Long sumL
	 * = 0l; if (pointTime >= e) { break; // 停止 } for (TrafficHttpFocusStatistic
	 * tt : domainList) { // 实际时间 String time = tt.getTime(); Long t =
	 * dateToStamp(time); if (t >= pointTime && t < pointTime + inter) { //
	 * 范围之内分到此pointTime组 sumL = sumL + tt.getCount(); } } //
	 * 在结束时间只有当值大于0时才记录数据，防止折线降为0引起误会 if (pointTime >= e - inter && sumL > 0) {
	 * countList.add(sumL); timeList.add(stampToDate(pointTime)); } if
	 * (pointTime < e - inter) { timeList.add(stampToDate(pointTime));
	 * countList.add(sumL); } num = num + 1; pointTime = b + inter * num; }
	 * resulMap.put("count", countList); resulMap.put("statTime", timeList);
	 * listMap.add(resulMap); } return listMap; }
	 */
	/**
	 * 优化后的网站详情域名趋势查询
	 * @param beginDate
	 * @param endDate
	 * @param domain
	 * @param entranceId
	 * @return
	 */
	public List<HashMap> getDomainTrans2(String begin, String end, Integer domain, Integer entranceId) {
		List<TrafficHttpFocusStatistic> domainList = new ArrayList<TrafficHttpFocusStatistic>();
		domainList = trafficHttpStatisticDao.getDomainTrans(entranceId, begin, end, domain);
		Date beginDate = DateUtils.parseDate(begin);
		Date endDate = DateUtils.parseDate(end);
		List<HashMap> listMap = new ArrayList<HashMap>();
			List<Date> dateRangeList = new ArrayList<Date>();
			List<String> strDateRangeList = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
			while (calendar.getTime().compareTo(endDate)<=0) {
				dateRangeList.add(calendar.getTime());
				strDateRangeList.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.MINUTE, 5);
			}
			//存放每个时间点的总数
			Map<String,List<Long>> ipCountListMap = new HashMap<String, List<Long>>();
			int index=0;
			List countList =new ArrayList();
			for (int i = 0; i < dateRangeList.size(); i++) {
				//存放一个时间点中总数
				Map<String, Long> ipCountMap = new HashMap<String, Long>();
				Date date = dateRangeList.get(i);
				Long num = 0L;
				for (int j = index; j < domainList.size(); j++) {
					TrafficHttpFocusStatistic ipInfo = domainList.get(j);
					if (ipInfo.getStatTime()!=null){
						if(ipInfo.getStatTime().compareTo(date)>=0&&(i+1<dateRangeList.size()&&ipInfo.getStatTime().compareTo(dateRangeList.get(i+1))<0)) {
								num=num+ipInfo.getCount();
						}else{
							index = j;
							break;
						}
					}
				}
				countList.add(num);
			}
			//整合 count time 
			HashMap ipTrafficMap = new HashMap();
			ipTrafficMap.put("count", countList);
			ipTrafficMap.put("statTime", strDateRangeList);
			listMap.add(ipTrafficMap);
		return listMap;
	}

}
