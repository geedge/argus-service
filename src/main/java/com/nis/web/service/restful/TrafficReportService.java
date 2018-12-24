package com.nis.web.service.restful;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcRadiusReport;
import com.nis.domain.restful.dashboard.TrafficTransStatistic;
import com.nis.web.dao.TrafficReportDao;
import com.nis.web.dao.dashboard.NtcTotalReportDao;
import com.nis.web.service.BaseLogService;

@Service
public class TrafficReportService extends BaseLogService {
	
	@Autowired
	protected TrafficReportDao trafficReportDao;


	public Page<NtcRadiusReport> findNtcRadiusReport(Page page,NtcRadiusReport entity) throws Exception {
		entity.setPage(page);
		List resultList = null;
		if ("1".equals(entity.getSearchBusinessType())) {
			resultList = new ArrayList<Map>();
			Map<String, List<NtcRadiusReport>> restMap = new HashMap<String, List<NtcRadiusReport>>();
			List<NtcRadiusReport> accountList = trafficReportDao.findAccounList(entity);
			restMap.put("accountList", accountList);
			List<NtcRadiusReport> nasIpList = trafficReportDao.findNasIpList(entity);
			restMap.put("nasIpList", nasIpList);
			resultList.add(restMap);
		} else {
			resultList = new ArrayList<NtcRadiusReport>();
			List<NtcRadiusReport> list = trafficReportDao.findNtcRadiusReport(entity);

			String regionName = "nasIp";
			if ("3".equals(entity.getSearchBusinessType())) {
				regionName = "account";
			}
			PropertyDescriptor pd = new PropertyDescriptor(regionName, NtcRadiusReport.class);
			Method method = pd.getReadMethod();
			NtcRadiusReport temp = null;
			Object tempVal = null;
			for (int i = 0; i < list.size(); i++) {
				if (i > 0) {
					NtcRadiusReport obj = list.get(i);
					Object objVal = method.invoke(obj);
					if (objVal != null && tempVal != null && !objVal.toString().equals(tempVal.toString())) {
						temp = obj;
						tempVal = method.invoke(temp);
						resultList.add(obj);
					}
				} else {
					temp = list.get(i);
					tempVal = method.invoke(temp);
					resultList.add(list.get(i));
				}
			}
		}
		page.setList(resultList);
		return page;

	}
	
	
	
	
	
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
		bandwidthList = trafficReportDao.getBandwidthTrans(entranceId, beginDate, endDate, addrType, transType);
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

}
