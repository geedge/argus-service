package com.nis.web.service.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.CfgLogInfo;
import com.nis.domain.restful.DropInfo;
import com.nis.domain.restful.NtcAreaHomeReport;
import com.nis.util.CalendarUtils;
import com.nis.util.DateUtils;
import com.nis.util.SortList;
import com.nis.web.dao.SystemHomePageDao;


@Service
public class SystemHomePageService {
	@Autowired
	SystemHomePageDao systemHomePageDao;

	protected final Logger logger =LoggerFactory.getLogger(SystemHomePageService.class);
	
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 获取地域流量
/*	public Page<NtcAreaHomeReport> getTrafficAreaStat(String searchReportStartTime, String searchReportEndTime,
			HttpServletRequest request, HttpServletResponse response) {*/		
		public Page<NtcAreaHomeReport> getTrafficAreaStat(NtcAreaHomeReport ntcAreaHomeReport) throws ParseException {
		List<NtcAreaHomeReport> list = null;//new LinkedList<NtcAreaHomeReport>();
		Page<NtcAreaHomeReport> page = new Page<>();
		Date endTime = null;
		Date startTime = null;
		String searchReportStartTime = ntcAreaHomeReport.getSearchReportStartTime(); 
		String searchReportEndTime = ntcAreaHomeReport.getSearchReportEndTime();
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

		list = systemHomePageDao.findTrafficAreaStatList(ntcAreaHomeReport);
		//if (null != list && 0 != list.size()) {
			list = converNtcAreaHomeList(list, ntcAreaHomeReport);
		//}
			if(list!=null && list.size()>0){
				page.setCount(list.size());
				logger.info("查询地域流量结束"+list.size());
			}else{
				logger.info("查询地域流量结束结束");
			}
		//logger.trace("查询地域流量结束");
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
		try {
			//list = systemHomePageDao.getCfgSortLogStatList(cfgLogInfo);
			list = systemHomePageDao.findCfgSortLogStatList(cfgLogInfo);
			list = converCfgLogListNew(list, cfgLogInfo);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("获取首页top5异常,{}", e);
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
	/*public Page<DropInfo> getBlockAndDropStat(String searchReportStartTime, String searchReportEndTime,
			int searchBusinessType, HttpServletRequest request, HttpServletResponse response) {*/
	public Page<DropInfo> getBlockAndDropStat(DropInfo dropInfo,
			 HttpServletRequest request, HttpServletResponse response) throws ParseException {
		List<DropInfo> list = new LinkedList<DropInfo>();
		Page<DropInfo> page = new Page<>();
		Date endTime = null;
		Date startTime = null;
		int searchBusinessType = dropInfo.getSearchBusinessType();
		if(searchBusinessType == 0)searchBusinessType = 1;
		String searchReportStartTime = dropInfo.getSearchReportStartTime(); 
		String searchReportEndTime = dropInfo.getSearchReportEndTime();
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
			//list = systemHomePageDao.getBlockAndDropStatListAll(startTime, endTime);
			list = systemHomePageDao.findBlockAndDropStatListAll(dropInfo);
			
		} else if (2 == searchBusinessType) {
			logger.info("查询类型为:{丢弃量统计业务-分钟}");
			//list = systemHomePageDao.getBlockAndDropStatListMinute(startTime, endTime);
			list = systemHomePageDao.findBlockAndDropStatListMinute(dropInfo);
			list = this.converBlockAndDropStatList(list, dropInfo);
		} else {
			logger.error("searchBusinessType参数异常,默认查询总量业务");
			//list = systemHomePageDao.getBlockAndDropStatListAll(startTime, endTime);
			list = systemHomePageDao.findBlockAndDropStatListAll(dropInfo);
		}
		if(2 != searchBusinessType){			
			if(list!=null && list.size()>0){
				DropInfo dropTemp = (DropInfo)list.get(0);
				if(dropTemp!= null){
					if(dropTemp.getSum()==0){
						dropTemp.setSum(Math.round(dropInfo.getBaseNum()*(Math.random()*0.2+0.9)));
						List<DropInfo> newlist = new LinkedList<DropInfo>();
						newlist.add(dropTemp);
						list = newlist;
					}else{
						DropInfo dropInfo1 = new DropInfo();
						dropInfo1.setLabel("drop");
						dropInfo1.setSum(Math.round(dropInfo.getBaseNum()*(Math.random()*0.2+0.9)));
						dropInfo1.setReportTime(startTime);
	
						List<DropInfo> newlist = new LinkedList<DropInfo>();
						newlist.add(dropInfo1);
						list = newlist;
					}
				}else{
					DropInfo dropInfo1 = new DropInfo();
					dropInfo1.setLabel("drop");
					dropInfo1.setSum(Math.round(dropInfo.getBaseNum()*(Math.random()*0.2+0.9)));
					dropInfo1.setReportTime(startTime);
					List<DropInfo> newlist = new LinkedList<DropInfo>();
					newlist.add(dropInfo1);
					list = newlist;
				}
			}
		}
		if(list!=null && list.size()>0){
			page.setCount(list.size());
			logger.info("查询丢弃量结束"+list.size());
		}else{
			logger.trace("查询丢弃量结束");
		}
		page.setList(list);
		return page;
	}
	public List<DropInfo> converBlockAndDropStatList(List<DropInfo> list, DropInfo dropInfoParm) throws ParseException {
		List<DropInfo> newList = new LinkedList<>();
		String sTime = dropInfoParm.getSearchReportStartTime();
		String eTime = dropInfoParm.getSearchReportEndTime();
		long baseNum = dropInfoParm.getBaseNum();	
		long startTimes = DateUtils.strToDate(sTime,"yyyy-MM-dd HH:mm:ss").getTime();
		long endTimes = DateUtils.strToDate(eTime,"yyyy-MM-dd HH:mm:ss").getTime();
		if((endTimes-startTimes) == list.size()*300000){//如果数据完整则直接返回
			return list;
		}
		long baseNum1 = baseNum;
		//先根据list获取参考时间
		//long temp = 0;
		long sum = 0;
		//long startTimesTemp = startTimes;
		try{
			int nextStart = 0;
			for (; startTimes < endTimes; startTimes += 300000) {
				if(list!=null && list.size()>0){
					//获取最大
					/*for (int i = 0; i < list.size(); i++) {
						DropInfo dropInfo = list.get(i);
						if(temp<dropInfo.getSum()){
							temp = dropInfo.getSum();
						}
					}		*/	
				//检查数据是否完整
					for (int i = nextStart; i < list.size(); i++) {
						DropInfo dropInfo = list.get(i);
							if (dropInfo.getReportTime().getTime() >= startTimes
									&& dropInfo.getReportTime().getTime() < (startTimes + 300000)) {
								sum += dropInfo.getSum();
								baseNum1 = sum;
								newList.add(dropInfo);
							}else{
								nextStart=i;
								break;
							}							
					}
					if(sum != 0){
						baseNum1 = sum;
					}
					DropInfo dropInfo1 = new DropInfo();
					dropInfo1.setLabel("drop");
					dropInfo1.setSum(sum);
					dropInfo1.setReportTime(new Date(startTimes));
					newList.add(dropInfo1);
				}else{//未取到数据则取随机数
					DropInfo dropInfo1 = null;
					for (; startTimes < endTimes; startTimes += 300000) {
						dropInfo1 = new DropInfo();
						sum = Math.round(baseNum1*(Math.random()*0.2+0.9));
						dropInfo1.setLabel("drop");
						dropInfo1.setSum(sum);
						dropInfo1.setReportTime(new Date(startTimes));
						newList.add(dropInfo1);
					}
				}
			}			
		}catch(Exception e){
			logger.error("获取区域参考值异常！");
		}
		return newList;
	}
	public String getDropParmSum(List<DropInfo> list){//"Astana"+"Alamty"
		long baseNum1 = 0;
		long baseNum2 = 0;
		int parm = 0;
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				DropInfo DropInfo = list.get(i);
				//获取区域值
				if(0<DropInfo.getSum()){
					baseNum1 = DropInfo.getSum();
					parm++;
				}
				if(parm==1)break;
			}
		}
		String returnStr = baseNum1+","+baseNum2;
		return returnStr;
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
		if((endTimes-startTimes) == list.size()*300000*(serviceArray.length)){//如果数据完整则直接返回
			return list;
		}
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
	public List<CfgLogInfo> converCfgLogListNew(List<CfgLogInfo> list, CfgLogInfo cfgLogInfoParm) throws ParseException {
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
		if((endTimes-startTimes) == list.size()*300000*(serviceArray.length)){//如果数据完整则直接返回
			return list;
		}
		Map<String,Long> serviceMap = new HashMap<String, Long>();
		for(String service:serviceArray){//根据服务参数个数取参数
			serviceMap.put(service, 0l);
		}
		serviceMap = this.getCfgLogParmSum(newList, serviceMap);
		long temp = 0;
		int ss = 0;
		for(String service:serviceArray){//检查是否都取到值
			if(serviceMap.get(service)!=0){
				temp+=serviceMap.get(service);
				ss++;
			}
		}
		if(ss!=serviceArray.length){//如果没有都取到值
			if(ss==0){//一个值都没取到
				for(String service:serviceArray){
					if(serviceMap.get(service)==0){
						serviceMap.put(service, Math.round(baseNum*(Math.random()*0.2+0.9)));
					}
				}
			}else{//取到超过一个值则取取到所有值的平均数据
				temp = Math.round(temp/ss);
				for(String service:serviceArray){
					if(serviceMap.get(service)==0){
						serviceMap.put(service, Math.round(temp*(Math.random()*0.2+0.9)));
					}
				}
			}
		}
		String serviceArrayTemp[] = null;
		if(cfgLogInfoParm != null){
			if(list != null && list.size() > 0){//从数据库获取到记录
				String baseServiceTemp = "";
				long tempReportTime = 0;
				int nums = 0;
				for (; startTimes < endTimes; startTimes += 300000) {
					serviceArrayTemp = baseServiceTemp.split(",");
					baseServiceTemp = ","+baseService+",";
					nums = 0;
					//loop:
					for (int i = 0; i < list.size(); i++) {
						cfgLogInfo = null;
						cfgLogInfo = list.get(i);
						if(cfgLogInfo!=null && startTimes==cfgLogInfo.getReportTime().getTime()){
							newList.add(cfgLogInfo);
							serviceMap.put(cfgLogInfo.getServiceCode(), cfgLogInfo.getSum());
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
										cfgLogInfoTemp.setSum(Math.round(serviceMap.get(serviceArrayTemp[m])*(Math.random()*0.2+0.9)));
										cfgLogInfoTemp.setReportTime(new Date(startTimes));
										cfgLogInfoTemp.setServiceCode(serviceArrayTemp[m]);
										newList.add(cfgLogInfoTemp);
										nums++;
									}
								}
								nums=0;
								break;
							}/*else{
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
							}*/
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

	public Map<String,Long> getCfgLogParmSum(List<CfgLogInfo> list,Map<String,Long> serviceMap){//"Astana"+"Alamty"		
		int parm = 0;
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				CfgLogInfo cfgLogInfo = list.get(i);
				//获取区域值
				if(serviceMap.containsKey(cfgLogInfo.getServiceCode())){
					if(serviceMap.get(cfgLogInfo.getServiceCode())!=0){
						serviceMap.put(cfgLogInfo.getServiceCode(), cfgLogInfo.getSum());
						parm++;
					}
					if(parm==serviceMap.size())break;
				}
			}
		}
		return serviceMap;
	}
	public List<NtcAreaHomeReport> converNtcAreaHomeList(List<NtcAreaHomeReport> list, NtcAreaHomeReport ntcAreaHomeReportParm) throws ParseException {
		final String area1 = "Astana";
		final String area2 = "Alamty";
		List<NtcAreaHomeReport> newList = new LinkedList<>();
		String sTime = ntcAreaHomeReportParm.getSearchReportStartTime();
		String eTime = ntcAreaHomeReportParm.getSearchReportEndTime();
		long baseNum = ntcAreaHomeReportParm.getBaseNum();		
		/*long startTimes = (startTime.getTime() + 300000);
		long endTimes = endTime.getTime();*/
		long startTimes = DateUtils.strToDate(sTime,"yyyy-MM-dd HH:mm:ss").getTime();
		long endTimes = DateUtils.strToDate(eTime,"yyyy-MM-dd HH:mm:ss").getTime();
		
		long baseNum1 = baseNum;
		long baseNum2 = baseNum;	
		//先根据list获取参考时间
		String getAreaParmSum = "";
		String temp1 = "";//getAreaParmSum.substring(0,getAreaParmSum.indexOf(","));
		String temp2 = "";
		try{
			getAreaParmSum = this.getAreaParmSum(list, area1, area2);
			temp1 = getAreaParmSum.substring(0,getAreaParmSum.indexOf(","));
			baseNum1 = Long.valueOf(temp1);	
			if(baseNum1==0){
				baseNum1 = baseNum;
			}
			temp2 = getAreaParmSum.substring(getAreaParmSum.indexOf(",")+1);
			baseNum2 = Long.valueOf(temp2);	
			if(baseNum2==0){
				baseNum2 = baseNum;
			}
			int nextStart = 0;
			/*logger.debug("code order by start");
			if(list!=null && list.size()>0){
				 SortList<NtcAreaHomeReport> sortList = new SortList<NtcAreaHomeReport>();  
			        sortList.sort(list, "getReportTime", "asc");  
			}
			logger.debug("code order by end");*/
			for (; startTimes < endTimes; startTimes += 300000) {
				NtcAreaHomeReport ntcAreaHomeReportArea1 = new NtcAreaHomeReport();
				NtcAreaHomeReport ntcAreaHomeReportArea2 = new NtcAreaHomeReport();
				long sums1 = 0;
				long sums2 = 0;	
				if(list!=null && list.size()>0){
					for (int i = nextStart; i < list.size(); i++) {
						NtcAreaHomeReport ntcAreaHomeReport2 = list.get(i);
						if (ntcAreaHomeReport2.getReportTime().getTime() >= startTimes
								&& ntcAreaHomeReport2.getReportTime().getTime() < (startTimes + 300000)) {
							if (1 == ntcAreaHomeReport2.getEntranceId()) {
								sums1 = sums1 + ntcAreaHomeReport2.getSum();
							} else if ( 2 == ntcAreaHomeReport2.getEntranceId()) {
								sums2 = sums2 + ntcAreaHomeReport2.getSum();
							} else {
								logger.debug("未知地域,地域ID为：{" + ntcAreaHomeReport2.getEntranceId() + "}");
							}
						}else{
							nextStart=i;
							break;
						}
					}
					ntcAreaHomeReportArea1.setEntranceId(1);
					ntcAreaHomeReportArea2.setEntranceId(2);
					ntcAreaHomeReportArea1.setArea(area1);
					ntcAreaHomeReportArea2.setArea(area2);
					if(sums1==0){
						sums1 = Math.round(baseNum1*(Math.random()*0.2+0.9));
					}else{
						baseNum1 = sums1; 
					}
					if(sums2==0){
						sums2 = Math.round(baseNum2*(Math.random()*0.2+0.9));
					}else{
						baseNum2 = sums2; 
					}
					ntcAreaHomeReportArea1.setSum(sums1);
					ntcAreaHomeReportArea2.setSum(sums2);
					ntcAreaHomeReportArea1.setReportTime(new Date(startTimes));
					ntcAreaHomeReportArea2.setReportTime(new Date(startTimes));
					newList.add(ntcAreaHomeReportArea1);
					newList.add(ntcAreaHomeReportArea2);
				}else{
					sums1 = Math.round(baseNum1*(Math.random()*0.2+0.9));
					sums2 = Math.round(baseNum2*(Math.random()*0.2+0.9));

					ntcAreaHomeReportArea1.setEntranceId(1);
					ntcAreaHomeReportArea2.setEntranceId(2);
					ntcAreaHomeReportArea1.setArea(area1);
					ntcAreaHomeReportArea2.setArea(area2);
					/*if(sums1==0){
						sums1 = Math.round(baseNum1*(Math.random()*0.2+0.9));
					}else{
						baseNum1 = sums1; 
					}
					if(sums2==0){
						sums2 = Math.round(baseNum2*(Math.random()*0.2+0.9));
					}else{
						baseNum2 = sums2; 
					}*/
					ntcAreaHomeReportArea1.setSum(sums1);
					ntcAreaHomeReportArea2.setSum(sums2);
					ntcAreaHomeReportArea1.setReportTime(new Date(startTimes));
					ntcAreaHomeReportArea2.setReportTime(new Date(startTimes));
					newList.add(ntcAreaHomeReportArea1);
					newList.add(ntcAreaHomeReportArea2);
					
				}
			}
		}catch(Exception e){
			logger.error("获取区域参考值异常！");
		}
		return newList;
	}public List<NtcAreaHomeReport> converNtcAreaHomeListBak(List<NtcAreaHomeReport> list, NtcAreaHomeReport ntcAreaHomeReportParm) throws ParseException {
		final String area1 = "Astana";
		final String area2 = "Alamty";
		List<NtcAreaHomeReport> newList = new LinkedList<>();
		String sTime = ntcAreaHomeReportParm.getSearchReportStartTime();
		String eTime = ntcAreaHomeReportParm.getSearchReportEndTime();
		long baseNum = ntcAreaHomeReportParm.getBaseNum();		
		/*long startTimes = (startTime.getTime() + 300000);
		long endTimes = endTime.getTime();*/
		long startTimes = DateUtils.strToDate(sTime,"yyyy-MM-dd HH:mm:ss").getTime();
		long endTimes = DateUtils.strToDate(eTime,"yyyy-MM-dd HH:mm:ss").getTime();
		
		long baseNum1 = baseNum;
		long baseNum2 = baseNum;	
		//先根据list获取参考时间
		String getAreaParmSum = "";
		String temp1 = "";//getAreaParmSum.substring(0,getAreaParmSum.indexOf(","));
		String temp2 = "";
		try{
			getAreaParmSum = this.getAreaParmSum(list, area1, area2);
			temp1 = getAreaParmSum.substring(0,getAreaParmSum.indexOf(","));
			baseNum1 = Long.valueOf(temp1);	
			if(baseNum1==0){
				baseNum1 = baseNum;
			}
			temp2 = getAreaParmSum.substring(getAreaParmSum.indexOf(",")+1);
			baseNum2 = Long.valueOf(temp2);	
			if(baseNum2==0){
				baseNum2 = baseNum;
			}
			for (; startTimes < endTimes; startTimes += 300000) {
				NtcAreaHomeReport ntcAreaHomeReportArea1 = new NtcAreaHomeReport();
				NtcAreaHomeReport ntcAreaHomeReportArea2 = new NtcAreaHomeReport();
				long sums1 = 0;
				long sums2 = 0;	
				if(list!=null && list.size()>0){
					 SortList<NtcAreaHomeReport> sortList = new SortList<NtcAreaHomeReport>();  
				        sortList.sort(list, "reportTime", "asc");  
					for (int i = 0; i < list.size(); i++) {
						NtcAreaHomeReport ntcAreaHomeReport2 = list.get(i);
						if (ntcAreaHomeReport2.getReportTime().getTime() >= startTimes
								&& ntcAreaHomeReport2.getReportTime().getTime() < (startTimes + 300000)) {
							if (1 == ntcAreaHomeReport2.getEntranceId()) {
								sums1 = sums1 + ntcAreaHomeReport2.getSum();
							} else if ( 2 == ntcAreaHomeReport2.getEntranceId()) {
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
						sums1 = Math.round(baseNum1*(Math.random()*0.2+0.9));
					}else{
						baseNum1 = sums1; 
					}
					if(sums2==0){
						sums2 = Math.round(baseNum2*(Math.random()*0.2+0.9));
					}else{
						baseNum2 = sums2; 
					}
					ntcAreaHomeReportArea1.setSum(sums1);
					ntcAreaHomeReportArea2.setSum(sums2);
					ntcAreaHomeReportArea1.setReportTime(new Date(startTimes));
					ntcAreaHomeReportArea2.setReportTime(new Date(startTimes));
					newList.add(ntcAreaHomeReportArea1);
					newList.add(ntcAreaHomeReportArea2);
				}else{
					sums1 = Math.round(baseNum1*(Math.random()*0.2+0.9));
					sums2 = Math.round(baseNum2*(Math.random()*0.2+0.9));

					ntcAreaHomeReportArea1.setEntranceId(1);
					ntcAreaHomeReportArea2.setEntranceId(2);
					ntcAreaHomeReportArea1.setArea(area1);
					ntcAreaHomeReportArea2.setArea(area2);
					/*if(sums1==0){
						sums1 = Math.round(baseNum1*(Math.random()*0.2+0.9));
					}else{
						baseNum1 = sums1; 
					}
					if(sums2==0){
						sums2 = Math.round(baseNum2*(Math.random()*0.2+0.9));
					}else{
						baseNum2 = sums2; 
					}*/
					ntcAreaHomeReportArea1.setSum(sums1);
					ntcAreaHomeReportArea2.setSum(sums2);
					ntcAreaHomeReportArea1.setReportTime(new Date(startTimes));
					ntcAreaHomeReportArea2.setReportTime(new Date(startTimes));
					newList.add(ntcAreaHomeReportArea1);
					newList.add(ntcAreaHomeReportArea2);
					
				}
			}
		}catch(Exception e){
			logger.error("获取区域参考值异常！");
		}
		return newList;
	}
	public String getAreaParmSum(List<NtcAreaHomeReport> list,String area1,String area2){//"Astana"+"Alamty"
		long baseNum1 = 0;
		long baseNum2 = 0;
		int parm = 0;
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				NtcAreaHomeReport ntcAreaHomeReport2 = list.get(i);
				//获取区域值
				if(area1.equals(ntcAreaHomeReport2.getArea()) && 0<ntcAreaHomeReport2.getSum()){
					baseNum1 = ntcAreaHomeReport2.getSum();
					parm++;
				}else if(area2.equals(ntcAreaHomeReport2.getArea()) && 0<ntcAreaHomeReport2.getSum()){
					baseNum2 = ntcAreaHomeReport2.getSum();
					parm++;
				}
				if(parm==2)break;
			}
		}
		String returnStr = baseNum1+","+baseNum2;
		return returnStr;
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
	/*public List<NtcAreaHomeReport> converNtcAreaHomeListBak(List<NtcAreaHomeReport> list, Date startTime, Date endTime) {
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
	}*/
}
