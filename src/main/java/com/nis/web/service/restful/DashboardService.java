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

import java.text.SimpleDateFormat;
import java.util.*;

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
	public List<HashMap> getActionTrans(String begin, String end, String serviceType) {
		String sql = "";
		String sqlBlock = "((service>=16 and service<=40) or (service>=258 and service<=273) or (service=576))"; // 阻断
		String sqlMonitor = "((service>=128 and service<=152) or (service>=384 and service<=513) or (service=592) or (service>=848 and service<=1030) or (service=1152))"; // 监测
		// 区分动作
		if (StringUtils.isNotBlank(serviceType)) {
			if (serviceType.equalsIgnoreCase("block")) {
				sql = sqlBlock;
			}
			if (serviceType.equalsIgnoreCase("monitor")) {
				sql = sqlMonitor;
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
		m2.put("count", sumList2);// [{link1:[],time:[],entrance:"1"},]
		m2.put("statTime", timeList);
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
	public List<HashMap> ipActiveFiveMinute(Date beginDate, Date endDate) {
		ArrayList<HashMap> listMap = new ArrayList<HashMap>();
		ArrayList<LinkedHashMap> ipListTop10 = trafficIpActiveStatisticDao.ipActiveChart(beginDate, endDate);
		if (ipListTop10 != null && ipListTop10.size() > 0) {
			for (LinkedHashMap map : ipListTop10) {
				HashMap m1 = new HashMap();
				String ipAddr = (String) map.get("ipAddr");
				m1.put("ipAddr", ipAddr);
				ArrayList<TrafficIpActiveStatistic> ipList = trafficIpActiveStatisticDao.ipActiveFiveMinute(ipAddr,
						beginDate, endDate);
				List timeList = new ArrayList();
				List sumList1 = new ArrayList();

				Map<String, Comparable> m = new HashMap<String, Comparable>();
				int inter = 1000 * 60 * 5;// 间隔时间为五分钟
				// 开始时间，结束时间 时间戳
				Long b = dateToStamp(DateUtils.formatDateTime(beginDate));
				Long e = dateToStamp(DateUtils.formatDateTime(endDate));
				int num = 0;
				Long pointTime = b;
				while (pointTime < e) {
					Map rm = new HashMap();
					Long sum1 = 0l;
					if (pointTime >= e) {
						break; // 停止
					}
					for (TrafficIpActiveStatistic e1 : ipList) {
						// 实际时间
						String time = e1.getTime();
						Long t = dateToStamp(time);
						if (t >= pointTime && t < pointTime + inter) {
							// 范围之内分到此pointTime组
							sum1 = sum1 + e1.getCount();
						}
					}
					// 在结束时间只有当值大于0时才记录数据，防止折线降为0引起误会
					if (pointTime >= e - inter && sum1 > 0) {
						sumList1.add(sum1);
						timeList.add(stampToDate(pointTime));
					}
					if (pointTime < e - inter) {
						sumList1.add(sum1);
						timeList.add(stampToDate(pointTime));
					}
					num = num + 1;
					pointTime = b + inter * num;
				}
				m1.put("linkNum", sumList1);
				m1.put("statTime", timeList);
				listMap.add(m1);
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
						ArrayList<HashMap> iplList = trafficIpActiveStatisticDao.ipActiveOneHour(ipAddr, beginDate,
								endDate);
						listMap.add(iplList.get(0));
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
	public List<Map> getProtocolList(String startTime, String endTime) {
		TrafficProtocolStatistic maxStatTime = trafficProtocolStatisticDao.getMaxStatTime();
		List<Map> list = new ArrayList<Map>();
		if (maxStatTime != null && maxStatTime.getStatTime() != null) {
			Date statTime = maxStatTime.getStatTime();
			list = trafficProtocolStatisticDao.getProtocolList(statTime, startTime, endTime);
		}
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
	public List<Map> getAppList(String startTime, String endTime, Integer appType) {
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
	public List<Map> getWebsiteDetails(Date beginDate, Date endDate) {
		List<Map> list = trafficHttpStatisticDao.getTrafficHttpStatistic(beginDate, endDate);
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
		List<Map> webIdAndCountList = trafficHttpStatisticDao.getTrafficHttpStatistic(beginDate, endDate);// 获取最近一小时的webid和count的关系
		List<Map> prevWebIdAndCountList = trafficHttpStatisticDao.getTrafficHttpStatistic(getBeforeByHourTime(2),
				beginDate);// 获取最近一小时的webid和count的关系
		for (String websiteServiceId : websiteIdAndIdMap.keySet()) {// 遍历上面获取的websiteServiceId和id的对应关系,拼接json
			Map viewMap = new HashMap<>();
			long count = 0l;// 记录当前websiteServiceId所有的count
			long linkNum = 0l;//
			long packets = 0l;//
			long prevCount = 0l;// 记录当前websiteServiceId所有的count
			List<String> idList = websiteIdAndIdMap.get(websiteServiceId);// 根据websiteServiceId获取对应的id
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
			viewMap.put("linkNum", linkNum);
			viewMap.put("packets", packets);
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
		List<Map> topicAndDomainList = getTopicAndDomainList(startTime, new Date(), "top");
		return topicAndDomainList;
	}

	/**
	 * 主题网站分类，域名
	 * 
	 **/
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
			viewMap.put("topicId", topicId);
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
			viewMap.put("count", count);
			viewMap.put("linkNum", linkNum);
			viewMap.put("packets", packets);
			viewMap.put("domainData", list);
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
			dataList = getTop10Data(set, countAndViewMap, 1);
		} else {
			dataList = getTop10Data(set, countAndViewMap, 2);
		}
		return dataList;
	}

}
