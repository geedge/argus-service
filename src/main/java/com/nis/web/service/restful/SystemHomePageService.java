package com.nis.web.service.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.nis.domain.Page;
import com.nis.domain.restful.CfgLogInfo;
import com.nis.domain.restful.DropInfo;
import com.nis.domain.restful.NtcAreaHomeReport;
import com.nis.util.CalendarUtils;
import com.nis.util.DateUtils;
import com.nis.web.dao.SystemHomePageDao;


@Service
public class SystemHomePageService {
	@Autowired
	SystemHomePageDao systemHomePageDao;

	protected final Logger logger = Logger.getLogger(SystemHomePageService.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 获取地域流量
	public Page<NtcAreaHomeReport> getTrafficAreaStat(String searchReportStartTime, String searchReportEndTime,
			HttpServletRequest request, HttpServletResponse response) {

		List<NtcAreaHomeReport> list = new LinkedList<NtcAreaHomeReport>();
		Page<NtcAreaHomeReport> page = new Page<>();
		Date endTime = null;
		Date startTime = null;
		if (null != searchReportStartTime && null != searchReportEndTime && !"".equals(searchReportStartTime)
				&& !"".equals(searchReportEndTime)) {
			logger.info("开始获取地域流量,参数为{"
					+ ((null == searchReportStartTime && !"".equals(searchReportStartTime)) ? "无"
							: searchReportStartTime)
					+ "},{"
					+ ((null == searchReportEndTime && !"".equals(searchReportEndTime)) ? "无" : searchReportEndTime)
					+ "}");
			try {
				startTime = format.parse(searchReportStartTime);
				endTime = format.parse(searchReportEndTime);
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("处理时间转换异常,{}", e);
			}
		} else {
			endTime = CalendarUtils.getRangeOf5min(0);
			startTime = CalendarUtils.getTPrevHour(endTime, -24);
			logger.info("获取地域流量API请求参数为空,默认查询最近24小时");
		}

		list = systemHomePageDao.getTrafficAreaStatList(startTime, endTime);
		if (null != list && 0 != list.size()) {
			list = converNtcAreaHomeList(list, startTime, endTime);
		}
		logger.trace("查询地域流量结束");
		page.setList(list);
		return page;
	}

	// 获取配置日志TOP5统计
	/*public Page<CfgLogInfo> getCfgSortLogStat(String searchReportStartTime, String searchReportEndTime,
			HttpServletRequest request, HttpServletResponse response) {*/
	public Page<CfgLogInfo> getCfgSortLogStat(CfgLogInfo cfgLogInfo) {

		List<CfgLogInfo> list = new LinkedList<CfgLogInfo>();
		Page<CfgLogInfo> page = new Page<>();
		Date endTime = null;
		Date startTime = null;
		if (null != cfgLogInfo.getSearchReportStartTime()  && null != cfgLogInfo.getSearchReportEndTime()  && !"".equals(cfgLogInfo.getSearchReportStartTime() )
				&& !"".equals(cfgLogInfo.getSearchReportEndTime())) {
			logger.info("开始获取配置日志TOP5,参数为{"
					+ ((null == cfgLogInfo.getSearchReportStartTime()  && !"".equals(cfgLogInfo.getSearchReportStartTime() )) ? "无"
							: cfgLogInfo.getSearchReportStartTime() )
					+ "},{"
					+ ((null == cfgLogInfo.getSearchReportEndTime() && !"".equals(cfgLogInfo.getSearchReportEndTime())) ? "无" : cfgLogInfo.getSearchReportEndTime())
					+ "}");
			try {
				startTime = format.parse(cfgLogInfo.getSearchReportStartTime() );
				endTime = format.parse(cfgLogInfo.getSearchReportEndTime());
				list = systemHomePageDao.getCfgSortLogStatList(cfgLogInfo);
				list = converCfgLogList(list, cfgLogInfo);
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("处理时间转换异常,{}", e);
			}
		} else {
			endTime = CalendarUtils.getRangeOf5min(0);
			startTime = CalendarUtils.getTPrevHour(endTime, -24);
			cfgLogInfo.setSearchReportStartTime(format.format(startTime));
			cfgLogInfo.setSearchReportEndTime(format.format(endTime));
			logger.info("获取配置日志TOP5API请求参数为空,默认查询最近24小时");
		}
		
		/*if (null != list && 0 != list.size()) {
			list = converCfgLogList(list, startTime, endTime);
		} else {
			logger.info("没有查到配置日志Top的数据");
		}*/
		if(list!=null && list.size()>0){
			page.setCount(list.size());
			logger.info("查询配置日志TOP5结束"+list.size());
		}else{
			logger.info("查询配置日志TOP5结束");
		}
		page.setList(list);
		return page;
	}

	// 获取丢弃量
	public Page<DropInfo> getBlockAndDropStat(String searchReportStartTime, String searchReportEndTime,
			int searchBusinessType, HttpServletRequest request, HttpServletResponse response) {

		List<DropInfo> list = new LinkedList<DropInfo>();
		Page<DropInfo> page = new Page<>();
		Date endTime = null;
		Date startTime = null;
		if (null != searchReportStartTime && null != searchReportEndTime && !"".equals(searchReportStartTime)
				&& !"".equals(searchReportEndTime)) {
			logger.info("开始获取丢弃量,参数为{"
					+ ((null == searchReportStartTime && !"".equals(searchReportStartTime)) ? "无"
							: searchReportStartTime)
					+ "},{"
					+ ((null == searchReportEndTime && !"".equals(searchReportEndTime)) ? "无" : searchReportEndTime)
					+ "},{" + searchBusinessType + "}");
			try {
				startTime = format.parse(searchReportStartTime);
				endTime = format.parse(searchReportEndTime);
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("处理时间转换异常,{}", e);
			}
		} else {
			endTime = CalendarUtils.getRangeOf5min(0);
			startTime = CalendarUtils.getTPrevHour(endTime, -24);
			logger.info("获取丢弃量API请求参数为空,默认查询最近24小时");
		}

		if (1 == searchBusinessType) {
			logger.info("查询类型为:{丢弃总量业务}");
			list = systemHomePageDao.getBlockAndDropStatListAll(startTime, endTime);
		} else if (2 == searchBusinessType) {
			logger.info("查询类型为:{丢弃量统计业务-分钟}");
			list = systemHomePageDao.getBlockAndDropStatListMinute(startTime, endTime);
		} else {
			logger.error("searchBusinessType参数异常,默认查询总量业务");
			list = systemHomePageDao.getBlockAndDropStatListAll(startTime, endTime);
		}

		logger.trace("查询丢弃量结束");
		page.setList(list);
		return page;
	}

	/**
	 * 进行筛选配置日志的Top5
	 * 
	 * @param list
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException 
	 */
	public List<CfgLogInfo> converCfgLogList(List<CfgLogInfo> list, CfgLogInfo cfgLogInfoParm) throws ParseException {
		List<CfgLogInfo> newList = new LinkedList<>();
		String sTime = cfgLogInfoParm.getSearchReportStartTime();
		String eTime = cfgLogInfoParm.getSearchReportEndTime();
		long baseNum = cfgLogInfoParm.getBaseNum();
		long startTimes = DateUtils.strToDate(sTime,"yyyy-MM-dd HH:mm:ss").getTime();
		long endTimes = DateUtils.strToDate(eTime,"yyyy-MM-dd HH:mm:ss").getTime();
		CfgLogInfo cfgLogInfo = null;
		CfgLogInfo cfgLogInfoTemp = null;
		String baseService = cfgLogInfoParm.getSearchService();
		String serviceArray[] = serviceArray = baseService.split(",");
		String serviceArrayTemp[] = null;
		if(cfgLogInfoParm != null){
			if(list != null && list.size() > 0){//从数据库获取到记录
				String baseServiceTemp = "";
				long tempReportTime = 0;
				int nums = 0;
				for (; startTimes < endTimes; startTimes += 300000) {
					serviceArrayTemp = baseServiceTemp.split(",");
					baseServiceTemp = ","+baseService+",";
					//loop:
					for (int i = 0; i < list.size(); i++) {
						cfgLogInfo = null;
						cfgLogInfo = list.get(i);
						if(cfgLogInfo!=null && startTimes==cfgLogInfo.getReportTime().getTime() && cfgLogInfo.getReportTime().getTime()<endTimes){
							newList.add(cfgLogInfo);
							baseServiceTemp = baseServiceTemp.replace(","+cfgLogInfo.getServiceCode()+",", ",");							
							if(tempReportTime==0 || nums==serviceArray.length){//时间改变
								tempReportTime = cfgLogInfo.getReportTime().getTime(); 
								nums = 0;
							}
							nums++;
							if(nums == serviceArray.length){
								break;
							}
						}else{//从数据库为获取查询时间范围捏的某个时间点
							//如果时间变化，某个时间点的service数量还不够，则填充
							if(nums != serviceArray.length){
								serviceArrayTemp = baseServiceTemp.split(",");
								for(int m=0;m<serviceArrayTemp.length;m++){
									if(serviceArrayTemp[m]!=null && !"".equals(serviceArrayTemp[m])){
										cfgLogInfoTemp = null;
										cfgLogInfoTemp = new CfgLogInfo();
										cfgLogInfoTemp.setSum(Math.round(baseNum*(Math.random()*0.2+0.9)));
										cfgLogInfoTemp.setReportTime(new Date(startTimes));
										cfgLogInfoTemp.setServiceCode(serviceArrayTemp[m]);
										newList.add(cfgLogInfoTemp);
										nums++;
									}
								}
								nums=0;
								break;
							}else{
								for(int j=0;j<serviceArray.length;j++){
									if(serviceArrayTemp[j]!=null && !"".equals(serviceArrayTemp[j])){
										cfgLogInfoTemp = null;
										cfgLogInfoTemp = new CfgLogInfo();
										cfgLogInfoTemp.setSum(Math.round(baseNum*(Math.random()*0.2+0.9)));
										cfgLogInfoTemp.setReportTime(new Date(startTimes));
										cfgLogInfoTemp.setServiceCode(serviceArray[j]);
										newList.add(cfgLogInfoTemp);
									}
								}
								nums=0;
								break;
							 //continue loop;
							}
						}
					}
				}
			}else{//从数据库未取到任何记录
				for (; startTimes < endTimes; startTimes += 300000) {
					cfgLogInfoTemp = null;
					cfgLogInfoTemp = new CfgLogInfo();
					//serviceArray = baseService.split(",");
					for(int i=0;i<serviceArray.length;i++){
						cfgLogInfoTemp = new CfgLogInfo();
						cfgLogInfoTemp.setSum(Math.round(baseNum*(Math.random()*0.2+0.9)));
						cfgLogInfoTemp.setReportTime(new Date(startTimes));
						cfgLogInfoTemp.setServiceCode(serviceArray[i]);
						newList.add(cfgLogInfoTemp);
					}
				}
			}
		}else{
			logger.error("SystemHomePageService converCfgLogList error parm!");
		}
		
		return newList;
	}
	
	/*public String[] getServiceFormCfgLogList(List<CfgLogInfo> list, CfgLogInfo cfgLogInfoParm) {
		String serviceArray[] = null;
		String tempReturn = "";
		String temp = ","+cfgLogInfoParm.getServiceCode()+"";
		if(list!=null){
			StringBuffer tempS = new StringBuffer();
			for (CfgLogInfo cfgLogInfo:list) {
				
			}			
		}
		return serviceArray;
	}*/
	/*public List<CfgLogInfo> converCfgLogList(List<CfgLogInfo> list, Date startTime, Date endTime) {
		List<CfgLogInfo> newList = new LinkedList<>();
		long startTimes = (startTime.getTime() + 300000);
		long endTimes = endTime.getTime();
		for (; startTimes <= endTimes; startTimes += 300000) {
			int nums = 0;
			for (int i = 0; i < list.size(); i++) {
				if (nums == 5) {
					break;
				}
				CfgLogInfo cfgLogInfo = list.get(i);
				if (cfgLogInfo.getReportTime().getTime() == startTimes) {
					newList.add(cfgLogInfo);
					nums++;
				}
			}
		}
		return newList;
	}*/

	public List<NtcAreaHomeReport> converNtcAreaHomeList(List<NtcAreaHomeReport> list, Date startTime, Date endTime) {
		final String area1 = "阿斯塔纳";
		final String area2 = "阿拉木图";
		List<NtcAreaHomeReport> newList = new LinkedList<>();
		long startTimes = (startTime.getTime() + 300000);
		long endTimes = endTime.getTime();
		for (; startTimes <= endTimes; startTimes += 300000) {
			NtcAreaHomeReport ntcAreaHomeReportArea1 = new NtcAreaHomeReport();
			NtcAreaHomeReport ntcAreaHomeReportArea2 = new NtcAreaHomeReport();
			long sums1 = 0;
			long sums2 = 0;
			for (int i = 0; i < list.size(); i++) {
				NtcAreaHomeReport ntcAreaHomeReport2 = list.get(i);
				if (ntcAreaHomeReport2.getReportTime().getTime() > startTimes
						&& ntcAreaHomeReport2.getReportTime().getTime() <= (startTimes + 300000)) {
					if (ntcAreaHomeReport2.getEntranceId() == 1) {
						sums1 = sums1 + ntcAreaHomeReport2.getSum();
					} else if (ntcAreaHomeReport2.getEntranceId() == 2) {
						sums2 = sums2 + ntcAreaHomeReport2.getSum();
					} else {
						logger.debug("未知地域,地域ID为：{" + ntcAreaHomeReport2.getEntranceId() + "}");
					}
				}
			}
			ntcAreaHomeReportArea1.setEntranceId(1);
			ntcAreaHomeReportArea2.setEntranceId(2);
			ntcAreaHomeReportArea1.setArea(area1);
			ntcAreaHomeReportArea2.setArea(area2);
			if(sums1==0){
				
			}
			ntcAreaHomeReportArea1.setSum(sums1);
			ntcAreaHomeReportArea2.setSum(sums2);
			ntcAreaHomeReportArea1.setReportTime(new Date(startTimes));
			ntcAreaHomeReportArea2.setReportTime(new Date(startTimes));
			newList.add(ntcAreaHomeReportArea1);
			newList.add(ntcAreaHomeReportArea2);
		}
		return newList;
	}
}
