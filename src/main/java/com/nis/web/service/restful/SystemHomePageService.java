package com.nis.web.service.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	public Page<CfgLogInfo> getCfgSortLogStat(String searchReportStartTime, String searchReportEndTime,
			HttpServletRequest request, HttpServletResponse response) {

		List<CfgLogInfo> list = new LinkedList<CfgLogInfo>();
		Page<CfgLogInfo> page = new Page<>();
		Date endTime = null;
		Date startTime = null;
		if (null != searchReportStartTime && null != searchReportEndTime && !"".equals(searchReportStartTime)
				&& !"".equals(searchReportEndTime)) {
			logger.info("开始获取配置日志TOP5,参数为{"
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
			logger.info("获取配置日志TOP5API请求参数为空,默认查询最近24小时");
		}

		list = systemHomePageDao.getCfgSortLogStatList(startTime, endTime);
		if (null != list && 0 != list.size()) {
			list = converCfgLogList(list, startTime, endTime);
		} else {
			logger.info("没有查到配置日志Top的数据");
		}
		logger.trace("查询配置日志TOP5结束");
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
	 */
	public List<CfgLogInfo> converCfgLogList(List<CfgLogInfo> list, Date startTime, Date endTime) {
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
	}

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
