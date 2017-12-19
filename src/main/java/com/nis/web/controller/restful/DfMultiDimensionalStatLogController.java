/**   
* @Title: DfStatLogController.java 
* @Package com.nis.web.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月13日 下午1:22:15     
* @version V1.0   
*/
package com.nis.web.controller.restful;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.StatLogEntity;
import com.nis.domain.restful.DfLwhhAttrDaily;
import com.nis.domain.restful.DfLwhhAttrMonth;
import com.nis.domain.restful.DfLwhhTagDaily;
import com.nis.domain.restful.DfLwhhTagMonth;
import com.nis.domain.restful.DfSrcIpAttrDaily;
import com.nis.domain.restful.DfSrcIpAttrMonth;
import com.nis.domain.restful.DfSrcIpTagDaily;
import com.nis.domain.restful.DfSrcIpTagMonth;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.redis.RedisDao;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfMultiDimensionalStatLogService;
import com.wordnik.swagger.annotations.ApiOperation;

/** 
* @ClassName: DfStatLogController 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (ddm)  
* @date 2016年9月13日 下午1:22:15   
* @version V1.0 
*/
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DfMultiDimensionalStatLogController extends BaseRestController{
	protected final Logger logger = Logger.getLogger(this.getClass());
	protected String logSource="0";
	protected String activeSys=Constants.ACTIVESYS_ALL;
	
	@Autowired
	protected DfMultiDimensionalStatLogService dfMultiDimensionalStatLogService;	
	@Autowired
	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	
	
	@RequestMapping(value="dfLwhhAttrDailySources", method = RequestMethod.GET)
	@ApiOperation(value="封堵来文函号-性质多维日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfLwhhAttrDailySources(
			Page page,DfLwhhAttrDaily dfLwhhAttrDaily, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfLwhhAttrDaily> dfLwhhAttrDailyPage =null;
		try {
			resetTime(dfLwhhAttrDaily, "daily");
			
			dfMultiDimensionalStatLogService.queryConditionCheck(thread,start,dfLwhhAttrDaily, DfLwhhAttrDaily.class, page);
			
			dfLwhhAttrDailyPage = dfMultiDimensionalStatLogService.dfLwhhAttrDaily(new Page<DfLwhhAttrDaily>(request, response,DfLwhhAttrDaily.class), dfLwhhAttrDaily);
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵来文函号-性质多维日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵来文函号-标签多维日报表检索成功",dfLwhhAttrDailyPage,activeSys,logSource);
	}
	@RequestMapping(value="dfLwhhAttrMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="封堵来文函号-性质多维月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfLwhhAttrMonthSources(
			Page page,DfLwhhAttrMonth dfLwhhAttrMonth, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfLwhhAttrMonth> dfLwhhAttrMonthPage =null;
		try {
			resetTime(dfLwhhAttrMonth, "month");
			
			
			//查询条件校验
			dfMultiDimensionalStatLogService.queryConditionCheck(thread,start,dfLwhhAttrMonth, DfLwhhAttrMonth.class, page);
			
			dfLwhhAttrMonthPage = dfMultiDimensionalStatLogService.dfLwhhAttrMonth(new Page<DfLwhhAttrMonth>(request, response,DfLwhhAttrMonth.class), dfLwhhAttrMonth);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵来文函号-性质多维月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵来文函号-性质多维月报表检索成功",dfLwhhAttrMonthPage,activeSys,logSource);
	}
	
	
	
	@RequestMapping(value="dfLwhhTagDailySources", method = RequestMethod.GET)
	@ApiOperation(value="封堵来文函号-标签多维日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfLwhhTagDailySources(
			Page page,DfLwhhTagDaily dfLwhhTagDaily, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfLwhhTagDaily> dfLwhhTagDailyPage =null;
		try {
			resetTime(dfLwhhTagDaily, "daily");
			
			dfMultiDimensionalStatLogService.queryConditionCheck(thread,start,dfLwhhTagDaily, DfLwhhTagDaily.class, page);
			
			dfLwhhTagDailyPage = dfMultiDimensionalStatLogService.dfLwhhTagDaily(new Page<DfLwhhTagDaily>(request, response,DfLwhhTagDaily.class), dfLwhhTagDaily);
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵来文函号-标签多维日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵来文函号-标签多维日报表检索成功",dfLwhhTagDailyPage,activeSys,logSource);
	}
	@RequestMapping(value="dfLwhhTagMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="封堵来文函号-标签多维月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfLwhhTagMonthSources(
			Page page,DfLwhhTagMonth dfLwhhTagMonth, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfLwhhTagMonth> dfLwhhTagMonthPage =null;
		try {
			resetTime(dfLwhhTagMonth, "month");
			
			
			//查询条件校验
			dfMultiDimensionalStatLogService.queryConditionCheck(thread,start,dfLwhhTagMonth, DfLwhhTagMonth.class, page);
			
			dfLwhhTagMonthPage = dfMultiDimensionalStatLogService.dfLwhhTagMonth(new Page<DfLwhhTagMonth>(request, response,DfLwhhTagMonth.class), dfLwhhTagMonth);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵来文函号-标签多维月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵来文函号-标签多维月报表检索成功",dfLwhhTagMonthPage,activeSys,logSource);
	}
	
	
	
	@RequestMapping(value="dfSrcIpAttrDailySources", method = RequestMethod.GET)
	@ApiOperation(value="封堵省-性质多维日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfSrcIpAttrDailySources(
			Page page,DfSrcIpAttrDaily dfSrcIpAttrDaily, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfSrcIpAttrDaily> dfSrcIpAttrDailyPage =null;
		try {
			resetTime(dfSrcIpAttrDaily, "daily");
			
			dfMultiDimensionalStatLogService.queryConditionCheck(thread,start,dfSrcIpAttrDaily, DfSrcIpAttrDaily.class, page);
			
			dfSrcIpAttrDailyPage = dfMultiDimensionalStatLogService.dfSrcIpAttrDaily(new Page<DfSrcIpAttrDaily>(request, response,DfSrcIpAttrDaily.class), dfSrcIpAttrDaily);
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵省-性质多维日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵省-标签多维日报表检索成功",dfSrcIpAttrDailyPage,activeSys,logSource);
	}
	@RequestMapping(value="dfSrcIpAttrMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="封堵省-性质多维月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfSrcIpAttrMonthSources(
			Page page,DfSrcIpAttrMonth dfSrcIpAttrMonth, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfSrcIpAttrMonth> dfSrcIpAttrMonthPage =null;
		try {
			resetTime(dfSrcIpAttrMonth, "month");
			
			
			//查询条件校验
			dfMultiDimensionalStatLogService.queryConditionCheck(thread,start,dfSrcIpAttrMonth, DfSrcIpAttrMonth.class, page);
			
			dfSrcIpAttrMonthPage = dfMultiDimensionalStatLogService.dfSrcIpAttrMonth(new Page<DfSrcIpAttrMonth>(request, response,DfSrcIpAttrMonth.class), dfSrcIpAttrMonth);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵省-性质多维月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵省-性质多维月报表检索成功",dfSrcIpAttrMonthPage,activeSys,logSource);
	}
	
	
	
	@RequestMapping(value="dfSrcIpTagDailySources", method = RequestMethod.GET)
	@ApiOperation(value="封堵省-标签多维日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfSrcIpTagDailySources(
			Page page,DfSrcIpTagDaily dfSrcIpTagDaily, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfSrcIpTagDaily> dfSrcIpTagDailyPage =null;
		try {
			resetTime(dfSrcIpTagDaily, "daily");
			
			dfMultiDimensionalStatLogService.queryConditionCheck(thread,start,dfSrcIpTagDaily, DfSrcIpTagDaily.class, page);
			
			dfSrcIpTagDailyPage = dfMultiDimensionalStatLogService.dfSrcIpTagDaily(new Page<DfSrcIpTagDaily>(request, response,DfSrcIpTagDaily.class), dfSrcIpTagDaily);
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵省-标签多维日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵省-标签多维日报表检索成功",dfSrcIpTagDailyPage,activeSys,logSource);
	}
	@RequestMapping(value="dfSrcIpTagMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="封堵省-标签多维月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfSrcIpTagMonthSources(
			Page page,DfSrcIpTagMonth dfSrcIpTagMonth, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfSrcIpTagMonth> dfSrcIpTagMonthPage =null;
		try {
			resetTime(dfSrcIpTagMonth, "month");
			
			
			//查询条件校验
			dfMultiDimensionalStatLogService.queryConditionCheck(thread,start,dfSrcIpTagMonth, DfSrcIpTagMonth.class, page);
			
			dfSrcIpTagMonthPage = dfMultiDimensionalStatLogService.dfSrcIpTagMonth(new Page<DfSrcIpTagMonth>(request, response,DfSrcIpTagMonth.class), dfSrcIpTagMonth);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵省-标签多维月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵省-标签多维月报表检索成功",dfSrcIpTagMonthPage,activeSys,logSource);
	}
	
	/**
	 * 
	 * @Title: resetTime
	 * @Description: (日报表默认查询本地前一天的数据)
	 * @param @param entity
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public void resetTime(StatLogEntity<?> entity,String type)  throws Exception{
		Map<String, String> map = DateUtils.getLocalTime(entity.getSearchStatStartTime(),
				entity.getSearchStatEndTime(), Constants.REPORT_LOCAL_TIME,type);
		entity.setSearchStatStartTime(map.get("startTime"));
		entity.setSearchStatEndTime(map.get("endTime"));
	}
	
}
