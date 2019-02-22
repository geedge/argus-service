package com.nis.web.service.restful;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Maps;
import com.nis.domain.Page;
import com.nis.domain.restful.NtcConnNumReport;
import com.nis.domain.restful.NtcEntranceReport;
import com.nis.domain.restful.dashboard.AppConnRecordStatistic;
import com.nis.domain.restful.dashboard.NtcTotalReport;
import com.nis.domain.restful.dashboard.SysDeviceInfo;
import com.nis.domain.restful.dashboard.TrafficAppFocusStatistic;
import com.nis.domain.restful.dashboard.TrafficAppBpsStatistic;
import com.nis.domain.restful.dashboard.TrafficAppPpsStatistic;
import com.nis.domain.restful.dashboard.TrafficAppStatistic;
import com.nis.domain.restful.dashboard.TrafficAsnStatistic;
import com.nis.domain.restful.dashboard.TrafficHttpFocusStatistic;
import com.nis.domain.restful.dashboard.TrafficIpActiveStatistic;
import com.nis.domain.restful.dashboard.TrafficPortActiveStatistic;
import com.nis.domain.restful.dashboard.TrafficProtocolStatistic;
import com.nis.domain.restful.dashboard.TrafficTransStatistic;
import com.nis.domain.restful.dashboard.TrafficUaStatistic;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.StringUtils;
import com.nis.web.dao.IspInfoDao;
import com.nis.web.dao.dashboard.NtcTotalReportDao;
import com.nis.web.dao.dashboard.TrafficAppStatisticDao;
import com.nis.web.dao.dashboard.TrafficAsnStatisticDao;
import com.nis.web.dao.dashboard.TrafficHttpStatisticDao;
import com.nis.web.dao.dashboard.TrafficIpActiveStatisticDao;
import com.nis.web.dao.dashboard.TrafficPortActiveStatisticDao;
import com.nis.web.dao.dashboard.TrafficProtocolStatisticDao;
import com.nis.web.dao.dashboard.TrafficUaStatisticDao;
import com.nis.web.service.BaseService;
import com.zdjizhi.utils.StringUtil;

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
	public TrafficAsnStatisticDao trafficAsnStatisticDao;
	@Autowired
	public TrafficPortActiveStatisticDao trafficPortActiveStatisticDao;
	@Autowired
	private IspInfoDao ispInfoDao;
	// ip地址类型
	private final String[] addrTypes = { "4", "6" };
	// tcp udp
	private final String[] transTypes = { "17", "6" };
	// 区域
	private final String[] entranceIds = { "1", "2" };

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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
						sumP = sumP + Long.parseLong(tt.getPps().substring(0, tt.getPps().lastIndexOf(".")));
						sumG = sumG + Long.parseLong(tt.getGbps().substring(0, tt.getGbps().lastIndexOf(".")));
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
	@Deprecated
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
	 * @param date_str
	 *            字符串日期
	 * @return
	 */
	@Deprecated
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
	 * @param timeStamp
	 *            1544602212000
	 * @return
	 */
	@Deprecated
	public static String stampToDate(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");// 这个是你要转成后的时间的格式
		String sd = sdf.format(new Date(timeStamp)); // 时间戳转换成时间
		return sd;
	}

	/**
	 * 根据service 动作查询近五分钟变化趋势 entrance 默认为1,2
	 */
	/*
	 * public List<HashMap> getActionTrans(String begin, String end, String
	 * serviceType) { String sql = ""; String sqlBlock =
	 * "((service>=16 and service<=40) or (service>=258 and service<=273) or (service=576))"
	 * ; // 阻断 String sqlMonitor =
	 * "((service>=128 and service<=152) or (service>=384 and service<=513) or (service=592) or (service>=848 and service<=1030) or (service=1152))"
	 * ; // 监测 String sqlDrop =
	 * "((service>=1040 and service<=1042) or (service=3) or (service=5) or (service=25) or (service=28) or (service=274))"
	 * ; // 丢弃 String sqlLoop = "((service=4) or (service>=832 and service<=834))";
	 * // 回流 // 区分动作 if (StringUtils.isNotBlank(serviceType)) { if
	 * (serviceType.equalsIgnoreCase("block")) { sql = sqlBlock; } if
	 * (serviceType.equalsIgnoreCase("monitor")) { sql = sqlMonitor; } if
	 * (serviceType.equalsIgnoreCase("drop")) { sql = sqlDrop; } if
	 * (serviceType.equalsIgnoreCase("loop")) { sql = sqlLoop; } }
	 * ArrayList<HashMap> listMap = new ArrayList<HashMap>();
	 * List<NtcEntranceReport> entrance1 = new ArrayList<NtcEntranceReport>();
	 * List<NtcEntranceReport> entrance2 = new ArrayList<NtcEntranceReport>();
	 * HashMap m1 = new HashMap(); HashMap m2 = new HashMap(); entrance1 =
	 * ntcTotalReportDao.getActionTrans(begin, end, 1, sql); entrance2 =
	 * ntcTotalReportDao.getActionTrans(begin, end, 2, sql); List timeList = new
	 * ArrayList(); List sumList1 = new ArrayList(); List sumList2 = new
	 * ArrayList();
	 * 
	 * Map<String, Comparable> m = new HashMap<String, Comparable>(); int inter =
	 * 1000 * 60 * 5;// 间隔时间为五分钟 // 开始时间，结束时间 时间戳 Long b = dateToStamp(begin); Long
	 * e = dateToStamp(end); int num = 0; Long pointTime = b; while (pointTime < e)
	 * { Map rm = new HashMap(); Long sum1 = 0l; Long sum2 = 0l; if (pointTime >= e)
	 * { break; // 停止 } for (NtcEntranceReport e1 : entrance1) { // 实际时间 String time
	 * = e1.getTime(); Long t = dateToStamp(time); if (t >= pointTime && t <
	 * pointTime + inter) { // 范围之内分到此pointTime组 sum1 = sum1 + e1.getSum(); } } for
	 * (NtcEntranceReport e2 : entrance2) { // 实际时间 String time = e2.getTime(); Long
	 * t = dateToStamp(time); if (t >= pointTime && t < pointTime + inter) { //
	 * 范围之内分到此pointTime组 sum2 = sum2 + e2.getSum(); } } //
	 * 在结束时间只有当值大于0时才记录数据，防止折线降为0引起误会 if (pointTime >= e - inter && sum1 > 0) {
	 * sumList1.add(sum1); } if (pointTime >= e - inter && sum2 > 0) {
	 * sumList2.add(sum2); } if (pointTime >= e - inter && (sum1 > 0 || sum2 > 0)) {
	 * timeList.add(stampToDate(pointTime)); } if (pointTime < e - inter) {
	 * sumList1.add(sum1); sumList2.add(sum2); timeList.add(stampToDate(pointTime));
	 * } num = num + 1; pointTime = b + inter * num; } m1.put("count", sumList1);
	 * m1.put("statTime", timeList); m1.put("entranceId", 1); m2.put("count",
	 * sumList2);// m2.put("statTime", timeList); m2.put("entranceId", 2);
	 * listMap.add(m1); listMap.add(m2); return listMap; }
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

		if (!StringUtil.isEmpty(entrance1) || !StringUtil.isEmpty(entrance2)) {
			Map<String, Comparable> m = new HashMap<String, Comparable>();
			Date beginDate = DateUtils.parseDate(begin);
			Date endDate = DateUtils.parseDate(end);

			List<Date> dateRangeList = new ArrayList<Date>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (calendar.getTime().compareTo(endDate) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, Constants.TREND_TIME_INTERVAL);
			}
			int index1 = 0;
			int index2 = 0;
			List sumList1 = new ArrayList();
			List sumList2 = new ArrayList();
			Long sumEnt1 = 0L;
			Long sumEnt2 = 0L;
			for (int i = 0; i < dateRangeList.size(); i++) {
				// 存放一个时间点中总数
				List listEnt1 = new ArrayList();
				List listEnt2 = new ArrayList();
				Map<String, Long> ipCountMap = new HashMap<String, Long>();
				Date date = dateRangeList.get(i);
				Long num1 = 0L;
				Long num2 = 0L;
				for (int j = index1; j < entrance1.size(); j++) {
					NtcEntranceReport ipInfo = entrance1.get(j);
					if (ipInfo.getReportTime() != null) {
						if (ipInfo.getReportTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? ipInfo.getReportTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							num1 = num1 + ipInfo.getSum();
						} else {
							index1 = j;
							break;
						}
					}
				}

				for (int j = index2; j < entrance2.size(); j++) {
					NtcEntranceReport ipInfo = entrance2.get(j);
					if (ipInfo.getReportTime() != null) {
						if (ipInfo.getReportTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? ipInfo.getReportTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							num2 = num2 + ipInfo.getSum();
						} else {
							index2 = j;
							break;
						}
					}
				}
				// 最后一个时间点,全为0 直接跳过不存入列表
				if (i + 1 == dateRangeList.size() && num1.compareTo(0L) == 0 && num2.compareTo(0L) == 0) {
					break;
				}
				sumEnt1 += num1;
				listEnt1.add(date.getTime());
				listEnt1.add(num1);
				sumList1.add(listEnt1);

				sumEnt2 += num2;
				listEnt2.add(date.getTime());
				listEnt2.add(num2);
				sumList2.add(listEnt2);
			}
			// 整合 count time
			m1.put("sum", sumEnt1);
			m1.put("result", sumList1);
			m1.put("entranceId", 1);
			m2.put("sum", sumEnt2);//
			m2.put("result", sumList2);
			m2.put("entranceId", 2);
			listMap.add(m1);
			listMap.add(m2);
		}
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
	public List<Map> ipActiveFiveMinute(Date beginDate, Date endDate, Integer entranceId) {
		List<Map> listMap = new ArrayList<Map>();
		List<LinkedHashMap> ipListTop10 = trafficIpActiveStatisticDao.ipActiveChart(beginDate, endDate, entranceId);
		if (ipListTop10 != null && ipListTop10.size() > 0) {
			StringBuilder ipStr = new StringBuilder();
			for (LinkedHashMap map : ipListTop10) {
				ipStr.append("'" + (String) map.get("ipAddr") + "',");
			}
			if (!StringUtil.isEmpty(ipStr) && ipStr.indexOf(",") > 0) {
				ipStr.deleteCharAt(ipStr.lastIndexOf(","));
			}
			// 如果所有IP在最后一个时间点的count都为0，则移除最后一个点
			// 开始划分时间段,间隔5分钟
			List<Date> dateRangeList = new ArrayList<Date>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (calendar.getTime().compareTo(endDate) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, Constants.TREND_TIME_INTERVAL);
			}

			ArrayList<TrafficIpActiveStatistic> ipInfoList = trafficIpActiveStatisticDao
					.ipActiveFiveMinute(ipStr.toString(), beginDate, endDate, entranceId);
			// 存放每个IP，每个时间点的总数
			Map<String, List> ipCountListMap = new LinkedHashMap<String, List>();
			// 如果所有IP的最后一个点都为0，删除
			Boolean delLastFlag = true;
			int index = 0;
			for (int i = 0; i < dateRangeList.size(); i++) {
				// 存放一个时间点中每个IP的总数
				Map<String, Long> ipCountMap = new LinkedHashMap<String, Long>();
				Date date = dateRangeList.get(i);
				for (int j = index; j < ipInfoList.size(); j++) {
					TrafficIpActiveStatistic ipInfo = ipInfoList.get(j);
					if (ipInfo.getStatTime() != null) {
						if (ipInfo.getStatTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? ipInfo.getStatTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							Long num = ipInfo.getCount();
							if (ipCountMap.containsKey(ipInfo.getIpAddr())) {
								num = num + ipCountMap.get(ipInfo.getIpAddr());
							}
							ipCountMap.put(ipInfo.getIpAddr(), num);
						} else {
							index = j;
							break;
						}
					}
				}
				for (LinkedHashMap map : ipListTop10) {
					String ip = (String) map.get("ipAddr");
					List sumList = new ArrayList();
					sumList.add(date.getTime());
					// 最后一个时间点
					if (i + 1 == dateRangeList.size() && !StringUtil.isEmpty(ipCountMap.get(ip))) {
						delLastFlag = false;
					}
					Long sum = 0l;
					if (ipCountMap.containsKey(ip)) {
						sum = ipCountMap.get(ip);
					}
					sumList.add(sum);
					if (ipCountListMap.containsKey(ip)) {
						List ipList = ipCountListMap.get(ip);
						ipList.add(sumList);
					} else {
						List iplist = new ArrayList();
						iplist.add(sumList);
						ipCountListMap.put(ip, iplist);
					}
				}
			}
			// 整合IP计算总数
			Iterator iterator = ipCountListMap.keySet().iterator();
			while (iterator.hasNext()) {
				String ip = iterator.next() + "";
				Map ipTrafficMap = new HashMap();
				ipTrafficMap.put("ipAddr", ip);
				Long total = 0L;
				for (Object obj : ipCountListMap.get(ip)) {
					List<Long> list = (List) obj;
					total += list.get(1);
				}
				if (delLastFlag) {
					ipCountListMap.get(ip).remove(ipCountListMap.get(ip).size() - 1);
				}
				ipTrafficMap.put("result", ipCountListMap.get(ip));
				ipTrafficMap.put("sum", total);
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
	public List<HashMap> ipActiveOneHour(Date beginDate, Date endDate, Integer entranceId) {

		// TrafficIpActiveStatistic maxStatTime =
		// trafficIpActiveStatisticDao.getMaxStatTime();
		ArrayList<HashMap> listMap = new ArrayList<HashMap>();
		// if (maxStatTime != null && maxStatTime.getStatTime() != null) {
		// Date statTime = maxStatTime.getStatTime();
		ArrayList<LinkedHashMap> list = trafficIpActiveStatisticDao.ipActiveChart(beginDate, endDate, entranceId);
		if (list != null && list.size() > 0) {
			for (LinkedHashMap map : list) {
				if (map.get("ipAddr") != null) {
					String ipAddr = (String) map.get("ipAddr");
					HashMap resMap = trafficIpActiveStatisticDao.ipActiveOneHour(ipAddr, beginDate, endDate,
							entranceId);
					if (resMap != null) {
						listMap.add(resMap);
					}
				}
			}
		}
		// }

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
		// TrafficIpActiveStatistic maxStatTime =
		// trafficIpActiveStatisticDao.getMaxStatTime();
		ArrayList<LinkedHashMap> list = new ArrayList<>();
		// if (maxStatTime != null && maxStatTime.getStatTime() != null) {
		// Date statTime = maxStatTime.getStatTime();
		list = trafficIpActiveStatisticDao.ipActiveChart(beginDate, endDate, null);
		if (list != null && list.size() > 0) {
			for (LinkedHashMap map : list) {
				if (map.get("ipAddr") != null) {
					Map m = new LinkedHashMap();
					map.put("pktNum", 0);
					map.put("byteLen", 0);
				}
			}
		}
		// }
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
	public List<Map> getProtocolList(String startTime, String endTime, Integer[] protoType, Integer entranceId,
			Integer searchDirection) {
		// TrafficProtocolStatistic maxStatTime =
		// trafficProtocolStatisticDao.getMaxStatTime();
		List<Map> list = new ArrayList<Map>();
		// if (maxStatTime != null && maxStatTime.getStatTime() != null) {
		// Date statTime = maxStatTime.getStatTime();
		list = trafficProtocolStatisticDao.getProtocolList(startTime, endTime, protoType, entranceId, searchDirection);
		// }
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
	public List<Map> getAppList(String startTime, String endTime, Integer[] appType, Integer entranceId) {
		// TrafficAppStatistic maxStatTime = trafficAppStatisticDao.getMaxStatTime();
		List<Map> list = new ArrayList<Map>();
		// if (maxStatTime != null && maxStatTime.getStatTime() != null) {
		// Date statTime = maxStatTime.getStatTime();
		list = trafficAppStatisticDao.getAppList(startTime, endTime, appType, entranceId);
		// }
		return list;
	}

	// 操作系统列表 排名TOP10
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

		// Date statTime = getBeforeByHourTime(1);// 获取上一个小时

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
			// long linkNum = 0l;
			// long packets = 0l;
			List<String> idList = topicIdAndIdMap.get(topicId);// 根据topicid获取对应的id
			for (String id : idList) {
				for (Map webIdAndCountMap : webIdAndCountList) {// 遍历webid和count
					String webId = String.valueOf(webIdAndCountMap.get("webId"));
					if (webId != null && webId.equals(id)) {// 如果webid和id相等则获取count数据并统计
						String countStr = String.valueOf(webIdAndCountMap.get("byteCount"));
						// String linkNumStr = String.valueOf(webIdAndCountMap.get("linkNum"));
						// String packetsStr = String.valueOf(webIdAndCountMap.get("pktCount"));
						if (countStr != null) {
							count += Long.parseLong(countStr);// 将count累加 转换单位小于0的不加
						}
						// if (linkNumStr != null) {
						// linkNum += Long.parseLong(linkNumStr);// 将count累加
						// }
						// if (packetsStr != null) {
						// packets += Long.parseLong(packetsStr);// 将count累加
						// }
						// webIdAndCountMap.remove("linkNum");
						// webIdAndCountMap.remove("pktCount");
						list.add(webIdAndCountMap);
					}
				}
			}
			Collections.sort(list, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					if (o1 == null && o2 != null) {
						return 1;
					}
					if (o1 != null && o2 == null) {
						return -1;
					}
					if (o1 == o2 && o1 == null) {
						return 0;
					}
					Map m1 = (Map) o1;
					Map m2 = (Map) o2;
					Object ob1 = m1.get("byteCount");
					Object ob2 = m2.get("byteCount");
					Long b1 = Long.parseLong(ob1.toString());
					Long b2 = Long.parseLong(ob2.toString());
					return -(b1).compareTo(b2);
				}
			});
			if (list.size() > 0) {
				viewMap.put("count", count);
				// viewMap.put("linkNum", linkNum);
				// viewMap.put("packets", packets);
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

	public Map<String, Map> getBandwidthTransEntrance(String beginDate, String endDate, String searchQuotaType,
			Integer searchDirection) throws ParseException {
		Map<String, Map> resultMap = new HashMap<String, Map>();
		Map<String, ArrayList<TrafficTransStatistic>> listMap = new HashMap<String, ArrayList<TrafficTransStatistic>>();
		List<TrafficTransStatistic> bandwidthListIPvx = new ArrayList<TrafficTransStatistic>();
		List<TrafficTransStatistic> bandwidthListProtocol = new ArrayList<TrafficTransStatistic>();
		bandwidthListIPvx = ntcTotalReportDao.getBandwidthTransIPVx(beginDate, endDate, searchDirection);
		bandwidthListProtocol = ntcTotalReportDao.getBandwidthTransProtocol(beginDate, endDate, searchDirection);
		Map<String, Map> trafficMap = new HashMap<String, Map>();
		if ((!StringUtil.isEmpty(bandwidthListIPvx) && bandwidthListIPvx.size() > 0)
				|| (!StringUtil.isEmpty(bandwidthListProtocol) && bandwidthListProtocol.size() > 0)) {
			// 划分时间段
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = sdf.parse(beginDate);
			Date endTime = sdf.parse(endDate);
			List<Date> dateRangeList = new ArrayList<Date>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTime);

			while (calendar.getTime().compareTo(endTime) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, Constants.TREND_TIME_INTERVAL);
			}
			if (bandwidthListIPvx != null && bandwidthListIPvx.size() > 0 && bandwidthListProtocol != null
					&& bandwidthListProtocol.size() > 0 && addrTypes != null && addrTypes.length > 0
					&& transTypes != null && transTypes.length > 0 && entranceIds != null && entranceIds.length > 0) {
				// 按区域和类型构建Map
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
				// 按区域和类型提取数据到列表中
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
				// 所有的数据最后一个点都为0，删除
				Boolean delLastFlag = true;
				Set<Entry<String, ArrayList<TrafficTransStatistic>>> entrySet = listMap.entrySet();
				for (Entry<String, ArrayList<TrafficTransStatistic>> entry : entrySet) {
					String key = entry.getKey();
					String entranceId = key.substring(key.length() - 1);
					ArrayList<TrafficTransStatistic> value = entry.getValue();
					resultMap.put(key, getNewData(dateRangeList, value, searchQuotaType, delLastFlag));
				}
				// 删除最后一个点
				if (delLastFlag) {
					Iterator iterator = resultMap.keySet().iterator();
					while (iterator.hasNext()) {
						String key = iterator.next() + "";
						List list = (List) resultMap.get(key).get("result");
						list.remove(list.size() - 1);
					}
				}
			}
		}
		return resultMap;
	}

	private Map getNewData(List<Date> dateRangeList, List<TrafficTransStatistic> bandwidthList, String searchQuotaType,
			Boolean delLastFlag) {
		Map restMap = new HashMap();
		List<List> restList = new ArrayList<List>();
		int index = 0;
		Long sum = 0l;
		for (int i = 0; i < dateRangeList.size(); i++) {
			Date date = dateRangeList.get(i);

			List countList = new ArrayList();
			countList.add(date.getTime());
			long num = 0l;
			for (int j = index; j < bandwidthList.size(); j++) {
				TrafficTransStatistic info = bandwidthList.get(j);
				if (info.getStatTime() != null) {
					if (info.getStatTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
							? info.getStatTime().compareTo(dateRangeList.get(i + 1)) < 0
							: true)) {
						if ("pps".equalsIgnoreCase(searchQuotaType)) {
							num = Long.parseLong(info.getPps().substring(0, info.getPps().lastIndexOf("."))) + num;
						} else if ("linkNumber".equalsIgnoreCase(searchQuotaType)) {
							num = Long.parseLong(info.getPps().substring(0, info.getPps().lastIndexOf("."))) + num;
						} else {
							num = Long.parseLong(info.getGbps().substring(0, info.getGbps().lastIndexOf("."))) + num;
						}
					} else {
						index = j;
						break;
					}
				}
			}
			countList.add(num);
			sum += num;
			restList.add(countList);
			// 最后一个时间点
			if (i + 1 == dateRangeList.size() && num != 0l) {
				delLastFlag = false;
			}
		}
		restMap.put("sum", sum);
		restMap.put("result", restList);
		return restMap;
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
	 * public List<HashMap> getDomainTrans(String beginDate, String endDate, Integer
	 * domain, Integer entranceId) { List<HashMap> listMap = new
	 * ArrayList<HashMap>(); HashMap resulMap = new HashMap();
	 * List<TrafficHttpFocusStatistic> domainList = new
	 * ArrayList<TrafficHttpFocusStatistic>(); domainList =
	 * trafficHttpStatisticDao.getDomainTrans(entranceId, beginDate, endDate,
	 * domain); List timeList = new ArrayList(); List countList = new ArrayList();
	 * if (domainList != null && domainList.size() > 0) { Map<String, Comparable> m
	 * = new HashMap<String, Comparable>(); int inter = 1000 * 60 * 30;// 间隔时间为30分钟
	 * // 开始时间，结束时间 时间戳 Long b = dateToStamp(beginDate); Long e =
	 * dateToStamp(endDate); int num = 0; Long pointTime = b; while (pointTime < e)
	 * { Map rm = new HashMap(); Long sumL = 0l; if (pointTime >= e) { break; // 停止
	 * } for (TrafficHttpFocusStatistic tt : domainList) { // 实际时间 String time =
	 * tt.getTime(); Long t = dateToStamp(time); if (t >= pointTime && t < pointTime
	 * + inter) { // 范围之内分到此pointTime组 sumL = sumL + tt.getCount(); } } //
	 * 在结束时间只有当值大于0时才记录数据，防止折线降为0引起误会 if (pointTime >= e - inter && sumL > 0) {
	 * countList.add(sumL); timeList.add(stampToDate(pointTime)); } if (pointTime <
	 * e - inter) { timeList.add(stampToDate(pointTime)); countList.add(sumL); } num
	 * = num + 1; pointTime = b + inter * num; } resulMap.put("count", countList);
	 * resulMap.put("statTime", timeList); listMap.add(resulMap); } return listMap;
	 * }
	 */
	/**
	 * 优化后的网站详情域名趋势查询
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param domain
	 * @param entranceId
	 * @return
	 */
	public List<HashMap> getDomainTrans2(String begin, String end, Integer domain, Integer entranceId) {
		List<TrafficHttpFocusStatistic> domainList = new ArrayList<TrafficHttpFocusStatistic>();
		domainList = trafficHttpStatisticDao.getDomainTrans(entranceId, begin, end, domain);
		List<HashMap> listMap = new ArrayList<HashMap>();
		if (!StringUtil.isEmpty(domainList)) {
			Date beginDate = DateUtils.parseDate(begin);
			Date endDate = DateUtils.parseDate(end);

			List<Date> dateRangeList = new ArrayList<Date>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (calendar.getTime().compareTo(endDate) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, Constants.TREND_TIME_INTERVAL);
			}
			int index = 0;
			List resultList = new ArrayList();
			Long total = 0L;
			for (int i = 0; i < dateRangeList.size(); i++) {
				// 存放一个时间点中总数
				List sumList = new ArrayList();
				Date date = dateRangeList.get(i);
				Long num = 0L;
				for (int j = index; j < domainList.size(); j++) {
					TrafficHttpFocusStatistic ipInfo = domainList.get(j);
					if (ipInfo.getStatTime() != null) {
						if (ipInfo.getStatTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? ipInfo.getStatTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							num = num + ipInfo.getCount();
						} else {
							index = j;
							break;
						}
					}
				}

				// 最后一个时间点，值为0不记录
				if (i + 1 == dateRangeList.size() && num.compareTo(0L) == 0) {
					break;
				}
				sumList.add(date.getTime());
				sumList.add(num);
				resultList.add(sumList);
				total += num;
			}
			// 整合 count time
			HashMap ipTrafficMap = new HashMap();
			ipTrafficMap.put("result", resultList);
			ipTrafficMap.put("sum", total);
			listMap.add(ipTrafficMap);
		}
		return listMap;
	}

	/**
	 * 获取活跃Port top10 的趋势图
	 * 
	 * @throws ParseException
	 */
	public List<Map> findTrafficPortActive(String beginDate, String endDate, Integer[] portArray)
			throws ParseException {
		List<Map> listMap = new ArrayList<Map>();
		StringBuilder portStr = new StringBuilder();
		List<LinkedHashMap> portListTop10 = null;
		if (portArray != null && portArray.length > 0) {// 如果有传人参数，获取传人参数
			for (int pi = 0; pi < portArray.length; pi++) {
				portStr.append("'" + portArray[pi] + "',");
			}
		} else {// 否则获取TOP10
			portListTop10 = trafficPortActiveStatisticDao.findTrafficPortActiveTopN(beginDate, endDate);
			for (LinkedHashMap map : portListTop10) {
				portStr.append("'" + map.get("port") + "',");
			}
		}
		if (!StringUtil.isEmpty(portStr) && portStr.indexOf(",") > 0) {
			portStr.deleteCharAt(portStr.lastIndexOf(","));
		}
		Date beginDate1 = DateUtils.strToDate(beginDate, "yyyy-MM-dd HH:mm:ss");
		Date endDate1 = DateUtils.strToDate(endDate, "yyyy-MM-dd HH:mm:ss");
		// if (portListTop10 != null && portListTop10.size() > 0) {
		// StringBuilder portStr = new StringBuilder();
		if (!StringUtil.isEmpty(portStr)) {
			// 如果所有Port在最后一个时间点的count都为0，则移除最后一个点
			Boolean delLastFlag = true;
			// 开始划分时间段,间隔5分钟
			List<Date> dateRangeList = new ArrayList<Date>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate1);
			while (calendar.getTime().compareTo(endDate1) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, Constants.TREND_TIME_INTERVAL);
			}

			ArrayList<TrafficPortActiveStatistic> portInfoList = trafficPortActiveStatisticDao
					.findTrafficPortActive(beginDate, endDate, portStr.toString());
			// 存放每个Port，每个时间点的总数
			Map<String, List<Long>> portCountListMap = new LinkedHashMap<String, List<Long>>();
			int index = 0;
			for (int i = 0; i < dateRangeList.size(); i++) {
				// 存放一个时间点中每个IP的总数
				Map<String, Long> portCountMap = new LinkedHashMap<String, Long>();
				Date date = dateRangeList.get(i);
				for (int j = index; j < portInfoList.size(); j++) {
					TrafficPortActiveStatistic portInfo = portInfoList.get(j);
					if (portInfo.getStatTime() != null) {
						if (portInfo.getStatTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? portInfo.getStatTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							Long num = portInfo.getSum();
							if (portCountMap.containsKey(portInfo.getPort())) {
								num = num + portCountMap.get(portInfo.getPort());
							}
							portCountMap.put(portInfo.getPort().toString(), num);
						} else {
							index = j;
							break;
						}
					}
				}
				if (portArray != null && portArray.length > 0) {// 如果有传人参数，获取传人参数
					for (int m = 0; m < portArray.length; m++) {
						String port = portArray[m].toString();
						List sumList = new ArrayList();
						sumList.add(date.getTime());
						Long sum = 0l;
						if (portCountMap.containsKey(port)) {
							sum = portCountMap.get(port);
						}
						sumList.add(sum);
						// 最后一个时间点
						if (i + 1 == dateRangeList.size() && sum.compareTo(0L) > 0) {
							delLastFlag = false;
						}
						if (portCountListMap.containsKey(port)) {
							List list = portCountListMap.get(port);
							list.add(sumList);
						} else {
							List list = new ArrayList();
							list.add(sumList);
							portCountListMap.put(port, list);
						}
					}

				} else if (portListTop10 != null) {
					for (LinkedHashMap map : portListTop10) {
						String port = map.get("port").toString();
						List sumList = new ArrayList();
						sumList.add(date.getTime());
						Long sum = 0l;
						if (portCountMap.containsKey(port)) {
							sum = portCountMap.get(port);
						}
						sumList.add(sum);
						// 最后一个时间点
						if (i + 1 == dateRangeList.size() && sum.compareTo(0L) > 0) {
							delLastFlag = false;
						}
						if (portCountListMap.containsKey(port)) {
							List list = portCountListMap.get(port);
							list.add(sumList);
						} else {
							List list = new ArrayList();
							list.add(sumList);
							portCountListMap.put(port, list);
						}
					}
				}
			}
			// 整合port sum time
			Iterator iterator = portCountListMap.keySet().iterator();
			while (iterator.hasNext()) {
				String port = iterator.next() + "";
				Map trafficPortActiveMap = new HashMap();
				Long total = 0L;
				for (Object obj : portCountListMap.get(port)) {
					List<Long> list = (List) obj;
					total += list.get(1);
				}
				if (delLastFlag) {
					List list = portCountListMap.get(port);
					list.remove(list.size() - 1);
				}
				trafficPortActiveMap.put("port", port);
				trafficPortActiveMap.put("result", portCountListMap.get(port));
				trafficPortActiveMap.put("sum", total);
				listMap.add(trafficPortActiveMap);
			}
		}
		return listMap;
	}

	/**
	 * 根据指标(drop,loop,New Link,Close Link,Pass,Live Link)统计各业务的趋势
	 * 
	 * @throws ParseException
	 */
	public Map trendTotalReport(NtcConnNumReport ntcConnNumReport) {
		Map restMap = new HashMap();
		List<NtcConnNumReport> list = ntcTotalReportDao.trendTotalReport(ntcConnNumReport);
		if (list != null && list.size() > 0) {
			// 开始划分时间段,间隔5分钟
			Date beginDate;
			Date endDate;
			try {
				beginDate = DateUtils.strToDate(ntcConnNumReport.getSearchReportStartTime(), "yyyy-MM-dd HH:mm:ss");
				endDate = DateUtils.strToDate(ntcConnNumReport.getSearchReportEndTime(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				throw new RestServiceException("searchReportStartTime或searchReportEndTime参数格式错误",
						RestBusinessCode.param_formate_error.getValue());
			}
			List<Date> dateRangeList = new ArrayList<Date>();
			List<String> strDateRangeList = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (calendar.getTime().compareTo(endDate) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, Constants.TREND_TIME_INTERVAL);
			}
			int index = 0;
			List sumList = new ArrayList();
			Long totalNum = 0L;
			for (int i = 0; i < dateRangeList.size(); i++) {
				// 存放一个时间点的总数
				List tempList = new ArrayList();
				Date date = dateRangeList.get(i);
				Long sum = 0l;
				for (int j = index; j < list.size(); j++) {
					NtcConnNumReport info = list.get(j);
					if (info.getReportTime() != null) {
						if (info.getReportTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? info.getReportTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							sum = sum + info.getNum();
						} else {
							index = j;
							break;
						}
					}
				}

				// 最后一个时间点，值为0不记录
				if (i + 1 == dateRangeList.size() && sum.compareTo(0L) == 0) {
					break;
				}
				totalNum += sum;
				tempList.add(date.getTime());
				tempList.add(sum);
				sumList.add(tempList);
			}
			restMap.put("sum", totalNum);
			restMap.put("result", sumList);
		}
		return restMap;
	}

	public Map getAppTrend(TrafficAppFocusStatistic entity) {
		List<TrafficAppFocusStatistic> appFocusList = new ArrayList<TrafficAppFocusStatistic>();
		appFocusList = trafficAppStatisticDao.getAppTrend(entity);
		Map resultMap = new HashMap();
		if (!StringUtil.isEmpty(appFocusList)) {
			Date beginDate = DateUtils.parseDate(entity.getSearchStartTime());
			Date endDate = DateUtils.parseDate(entity.getSearchEndTime());
			List<Date> dateRangeList = new ArrayList<Date>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (calendar.getTime().compareTo(endDate) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, 1);
			}
			int index = 0;
			List resultSipList = new ArrayList();
			List resultDipList = new ArrayList();
			Long sipTotal = 0L;
			Long dipTotal = 0L;
			for (int i = 0; i < dateRangeList.size(); i++) {
				// 存放一个时间点中总数
				List sipList = new ArrayList();
				List dipList = new ArrayList();
				Date date = dateRangeList.get(i);
				Long sipNum = 0L;
				Long dipNum = 0L;
				for (int j = index; j < appFocusList.size(); j++) {
					TrafficAppFocusStatistic info = appFocusList.get(j);
					if (info.getTime() != null) {
						if (info.getTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? info.getTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							sipNum = sipNum + info.getSipNum();
							dipNum = dipNum + info.getDipNum();
						} else {
							index = j;
							break;
						}
					}
				}

				// 最后一个时间点
				if (i + 1 == dateRangeList.size() && sipNum.compareTo(0L) == 0 && dipNum.compareTo(0L) == 0) {
					break;
				}
				sipList.add(date.getTime());
				sipList.add(sipNum);
				resultSipList.add(sipList);
				dipList.add(date.getTime());
				dipList.add(dipNum);
				resultDipList.add(dipList);
				sipTotal += sipNum;
				dipTotal += dipNum;
			}
			resultMap.put("sipResult", resultSipList);
			resultMap.put("dipResult", resultDipList);
			resultMap.put("sipSum", sipTotal);
			resultMap.put("dipSum", dipTotal);
		}
		return resultMap;
	}
	
	
	

	public Map getAppBpsTrend(TrafficAppBpsStatistic entity) {
		List<TrafficAppBpsStatistic> list = new ArrayList<TrafficAppBpsStatistic>();
		list = trafficAppStatisticDao.getAppBpsTrend(entity);
		Map resultMap = new HashMap();
		Long gbps = 1024l*1024l*1024l*60l;
		if (!StringUtil.isEmpty(list)) {
			Date beginDate = DateUtils.parseDate(entity.getSearchStartTime());
			Date endDate = DateUtils.parseDate(entity.getSearchEndTime());
			List<Date> dateRangeList = new ArrayList<Date>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (calendar.getTime().compareTo(endDate) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, 1);
			}
			int index = 0;
			List resultC2sList = new ArrayList();
			List resultS2cList = new ArrayList();
			List resultTotalList = new ArrayList();
			// Long c2sTotal = 0L;
			// Long s2cTotal = 0L;
			// Long c2sS2cTotal = 0L;
			for (int i = 0; i < dateRangeList.size(); i++) {
				// 存放一个时间点中总数
				List c2sList = new ArrayList();
				List s2cList = new ArrayList();
				List totalList = new ArrayList();
				Date date = dateRangeList.get(i);
				Long c2sNum = 0L;
				Long s2cNum = 0L;
				Long totalNum = 0L;
				for (int j = index; j < list.size(); j++) {
					TrafficAppBpsStatistic info = list.get(j);
					if (info.getTime() != null) {
						if (info.getTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? info.getTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							c2sNum = c2sNum + info.getC2sNum();
							s2cNum = s2cNum + info.getS2cNum();
							totalNum = totalNum + info.getTotalNum();
						} else {
							index = j;
							break;
						}
					}
				}

				// 最后一个时间点
				if (i + 1 == dateRangeList.size() && s2cNum.compareTo(0L) == 0 && c2sNum.compareTo(0L) == 0) {
					break;
				}
				c2sList.add(date.getTime());
				c2sList.add(String.format("%.2f", c2sNum * 8 / gbps.doubleValue()));
				resultC2sList.add(c2sList);
				s2cList.add(date.getTime());
				s2cList.add(String.format("%.2f", s2cNum * 8 / gbps.doubleValue()));
				resultS2cList.add(s2cList);

				totalList.add(date.getTime());
				totalList.add(String.format("%.2f", totalNum * 8 / gbps.doubleValue()));
				resultTotalList.add(totalList);

				// c2sTotal+=c2sNum;
				// s2cTotal+=s2cNum;
				// c2sS2cTotal+=totalNum;
			}
			resultMap.put("c2sResult", resultC2sList);
			resultMap.put("s2cResult", resultS2cList);
			resultMap.put("totalResult", resultTotalList);
			// resultMap.put("c2sSum", c2sTotal*8/1024/1024/1024/60);
			// resultMap.put("s2cSum", s2cTotal*8/1024/1024/1024/60);
			// resultMap.put("totalSum", c2sS2cTotal*8/1024/1024/1024/60);
		}
		return resultMap;

	}

	public Map getAppPpsTrend(TrafficAppPpsStatistic entity) {
		List<TrafficAppPpsStatistic> list = new ArrayList<TrafficAppPpsStatistic>();
		list = trafficAppStatisticDao.getAppPpsTrend(entity);
		Map resultMap = new HashMap();
		if (!StringUtil.isEmpty(list)) {
			Date beginDate = DateUtils.parseDate(entity.getSearchStartTime());
			Date endDate = DateUtils.parseDate(entity.getSearchEndTime());
			List<Date> dateRangeList = new ArrayList<Date>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (calendar.getTime().compareTo(endDate) < 0) {
				dateRangeList.add(calendar.getTime());
				calendar.add(Calendar.MINUTE, 1);
			}
			int index = 0;
			List resultC2sList = new ArrayList();
			List resultS2cList = new ArrayList();
			List resultTotalList = new ArrayList();
			// Long c2sTotal = 0L;
			// Long s2cTotal = 0L;
			// Long c2sS2cTotal = 0L;
			for (int i = 0; i < dateRangeList.size(); i++) {
				// 存放一个时间点中总数
				List c2sList = new ArrayList();
				List s2cList = new ArrayList();
				List totalList = new ArrayList();
				Date date = dateRangeList.get(i);
				Long c2sNum = 0L;
				Long s2cNum = 0L;
				Long totalNum = 0L;
				if (i == 33) {
					System.out.println();
				}
				for (int j = index; j < list.size(); j++) {
					TrafficAppPpsStatistic info = list.get(j);
					if (info.getTime() != null) {
						if (info.getTime().compareTo(date) >= 0 && (i + 1 < dateRangeList.size()
								? info.getTime().compareTo(dateRangeList.get(i + 1)) < 0
								: true)) {
							c2sNum = c2sNum + info.getC2sNum();
							s2cNum = s2cNum + info.getS2cNum();
							totalNum = totalNum + info.getTotalNum();
						} else {
							index = j;
							break;
						}
					}
				}

				// 最后一个时间点
				if (i + 1 == dateRangeList.size() && s2cNum.compareTo(0L) == 0 && c2sNum.compareTo(0L) == 0) {
					break;
				}
				c2sList.add(date.getTime());
				c2sList.add(String.format("%.2f", c2sNum / 60d));
				resultC2sList.add(c2sList);
				s2cList.add(date.getTime());
				s2cList.add(String.format("%.2f", s2cNum / 60d));
				resultS2cList.add(s2cList);

				totalList.add(date.getTime());
				totalList.add(String.format("%.2f", totalNum / 60d));
				resultTotalList.add(totalList);

				// c2sTotal+=c2sNum;
				// s2cTotal+=s2cNum;
				// c2sS2cTotal+=totalNum;
			}
			resultMap.put("c2sResult", resultC2sList);
			resultMap.put("s2cResult", resultS2cList);
			resultMap.put("totalResult", resultTotalList);
			// resultMap.put("c2sSum", c2sTotal);
			// resultMap.put("s2cSum", s2cTotal);
			// resultMap.put("totalSum", c2sS2cTotal);
		}
		return resultMap;

	}

	public List<AppConnRecordStatistic> appConnRecordTop100(AppConnRecordStatistic entity) {
		DecimalFormat df = new DecimalFormat("##.##");
		List<AppConnRecordStatistic> list = new ArrayList<AppConnRecordStatistic>();
		list = trafficAppStatisticDao.appConnRecordTop100(entity);
		if (!StringUtil.isEmpty(list)) {
			// 统计总量
			AppConnRecordStatistic sum = trafficAppStatisticDao.appConnRecordTotal(entity).get(0);
			for (AppConnRecordStatistic obj : list) {
				obj.setBytePercent(df.format(obj.getByteNum() / sum.getByteNum() * 100));
				obj.setPktPercent(df.format(obj.getPktNum() / sum.getPktNum() * 100));
				obj.setLogPercent(df.format(obj.getLogNum() / sum.getLogNum() * 100));
			}
		}
		return list;
	}

	public void getTrafficAsnStatisticList(Page page, TrafficAsnStatistic ntcAsnRecord) {
		ntcAsnRecord.setPage(page);
		page.setList(trafficAsnStatisticDao.getTrafficAsnStatisticList(ntcAsnRecord));
	}

	/**
	 * 根据ispcode获取当前运营商下所有的entrance_id,device_id,link_id组合
	 * 
	 * @param ispCode
	 * @return
	 */
	public String getIspNum(String ispCode) {
		String result = "'NA'";
		List<String> ispNum = ispInfoDao.getIspNum(ispCode);
		if (ispNum != null && ispNum.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (String ispNumStr : ispNum) {
				sb.append("'");
				sb.append(ispNumStr);
				sb.append("'");
				sb.append(",");
			}
			result = sb.substring(0, sb.length() - 1);

		}
		return result;

	}

	public List<SysDeviceInfo> getIspListByIspNum(String ispNum) {

		return ispInfoDao.getIspListByIspNum(ispNum);
	}
}
