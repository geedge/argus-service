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

import com.nis.domain.DfReportEntity;
import com.nis.domain.Page;
import com.nis.domain.StatLogEntity;
import com.nis.domain.restful.DfAttrStatLogDaily;
import com.nis.domain.restful.DfAttrStatLogMonth;
import com.nis.domain.restful.DfDestIpCounrtyStatLogDaily;
import com.nis.domain.restful.DfDestIpCounrtyStatLogMonth;
import com.nis.domain.restful.DfEntrStatLogDaily;
import com.nis.domain.restful.DfEntrStatLogMonth;
import com.nis.domain.restful.DfLwhhStatLogDaily;
import com.nis.domain.restful.DfLwhhStatLogMonth;
import com.nis.domain.restful.DfSrcIpDomesticStatLogDaily;
import com.nis.domain.restful.DfSrcIpDomesticStatLogMonth;
import com.nis.domain.restful.DfStatLogDaily;
import com.nis.domain.restful.DfStatLogMonth;
import com.nis.domain.restful.DfTagStatLogDaily;
import com.nis.domain.restful.DfTagStatLogMonth;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.util.redis.RedisDao;
import com.nis.util.redis.SaveRedisThread;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfStatLogService;
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
public class DfStatLogController extends BaseRestController{
	protected final Logger logger = Logger.getLogger(this.getClass());
	protected String logSource="0";
	protected String activeSys=Constants.ACTIVESYS_ALL;
	
	@Autowired
	protected DfStatLogService dfStatLogService;	
	@Autowired
	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	@RequestMapping(value="dfStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="封堵日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfStatLogDailySources(
			Page page,DfStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchConfigId())) {
				Long.parseLong(dailyLog.getSearchConfigId());
			}
			
			dfStatLogService.queryConditionCheck(thread,start,dailyLog, DfStatLogDaily.class, page);
			
			dailyLogPage = dfStatLogService.dfStatLogDaily(new Page<DfStatLogDaily>(request, response,DfStatLogDaily.class), dailyLog);
			
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchConfigId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	@RequestMapping(value="dfStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="封堵日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfStatLogMonthSources(
			Page page,DfStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchConfigId())) {
				Long.parseLong(monthLog.getSearchConfigId());
			}
			
			//查询条件校验
			dfStatLogService.queryConditionCheck(thread,start,monthLog, DfStatLogMonth.class, page);
			
			monthLogPage = dfStatLogService.dfStatLogMonth(new Page<DfStatLogMonth>(request, response,DfStatLogMonth.class), monthLog);
				
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchConfigId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "封堵日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	
	/**
	 * 标签日报表
	 * 
	 * @Title: dfTagStatLogDailySources
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfTagStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="标签封堵日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfTagStatLogDailySources(
			Page page,DfTagStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		//查询条件校验
		Page<DfTagStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchTag())) {
				Integer.parseInt(dailyLog.getSearchTag());
			}
			
			dfStatLogService.queryConditionCheck(thread,start,dailyLog, DfTagStatLogDaily.class, page);
			
			dailyLogPage = dfStatLogService.dfTagStatLogDaily(new Page<DfTagStatLogDaily>(request, response,DfTagStatLogDaily.class), dailyLog);
				
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchTag参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"标签封堵日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "标签封堵日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * 标签月报表
	 * 
	 * @Title: dfTagStatLogDailySources
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfTagStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="标签封堵日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfTagStatLogMonthSources(
			Page page,DfTagStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfTagStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchTag())) {
				Integer.parseInt(monthLog.getSearchTag());
			}
			
			//查询条件校验
			dfStatLogService.queryConditionCheck(thread,start,monthLog, DfTagStatLogMonth.class, page);
			
			monthLogPage = dfStatLogService.dfTagStatLogMonth(new Page<DfTagStatLogMonth>(request, response,DfTagStatLogMonth.class), monthLog);
				
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchTag参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"标签封堵日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "标签封堵日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	
	/**
	 * @Title: dfTagStatLogDailySources
	 * @Description: (性质日报表)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfAttrStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="性质封堵日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfAttrStatLogDailySources(
			Page page,DfAttrStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfAttrStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchAttrType())) {
				Integer.parseInt(dailyLog.getSearchAttrType());
			}
			
			dfStatLogService.queryConditionCheck(thread,start,dailyLog, DfAttrStatLogDaily.class, page);
			
			dailyLogPage = dfStatLogService.dfAttrStatLogDaily(new Page<DfAttrStatLogDaily>(request, response,DfAttrStatLogDaily.class), dailyLog);
				
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchAttrType参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"性质封堵日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "性质封堵日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * @Title: dfAttrStatLogDailySources
	 * @Description: (性质月报表)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfAttrStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="性质封堵日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfAttrStatLogMonthSources(
			Page page,DfAttrStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfAttrStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchAttrType())) {
				Integer.parseInt(monthLog.getSearchAttrType());
			}
			
			//查询条件校验
			dfStatLogService.queryConditionCheck(thread,start,monthLog, DfAttrStatLogMonth.class, page);
			
			monthLogPage = dfStatLogService.dfAttrStatLogMonth(new Page<DfAttrStatLogMonth>(request, response,DfAttrStatLogMonth.class), monthLog);
			
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchAttrType参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"性质封堵日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "性质封堵日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	
	/**
	 * @Title: dfEntrStatLogDailySources
	 * @Description: (局点日报表)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfEntrStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="局点封堵日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfEntrStatLogDailySources(
			Page page,DfEntrStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfEntrStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchEntranceId())) {
				Integer.parseInt(dailyLog.getSearchEntranceId());
			}
			
			dfStatLogService.queryConditionCheck(thread,start,dailyLog, DfEntrStatLogDaily.class, page);
			
			dailyLogPage = dfStatLogService.dfEntrStatLogDaily(new Page<DfEntrStatLogDaily>(request, response,DfEntrStatLogDaily.class), dailyLog);
				
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchEntranceId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"局点封堵日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "局点封堵日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * @Title: dfEntrStatLogDailySources
	 * @Description: (局点月报表)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfEntrStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="局点封堵日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfEntrStatLogMonthSources(
			Page page,DfEntrStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfEntrStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchEntranceId())) {
				Integer.parseInt(monthLog.getSearchEntranceId());
			}
			//查询条件校验
			dfStatLogService.queryConditionCheck(thread,start,monthLog, DfEntrStatLogMonth.class, page);
			
			monthLogPage = dfStatLogService.dfEntrStatLogMonth(new Page<DfEntrStatLogMonth>(request, response,DfEntrStatLogMonth.class), monthLog);
			
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchEntranceId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"局点封堵日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "局点封堵日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	/**
	 * @Title: dfLwhhStatLogDailySources
	 * @Description: (局点日报表)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfLwhhStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="来文函号封堵日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfLwhhStatLogDailySources(
			Page page,DfLwhhStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		//查询条件校验
		Page<DfLwhhStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchLwhh())) {
				Integer.parseInt(dailyLog.getSearchLwhh());
			}
			
			dfStatLogService.queryConditionCheck(thread,start,dailyLog, DfLwhhStatLogDaily.class, page);
			
			dailyLogPage = dfStatLogService.dfLwhhStatLogDaily(new Page<DfLwhhStatLogDaily>(request, response,DfLwhhStatLogDaily.class), dailyLog);
			
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchLwhh参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"来文函号封堵日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "来文函号封堵日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * @Title: dfLwhhStatLogDailySources
	 * @Description: (局点月报表)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfLwhhStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="来文函号封堵日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfLwhhStatLogMonthSources(
			Page page,DfLwhhStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfLwhhStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchLwhh())) {
				Integer.parseInt(monthLog.getSearchLwhh());
			}
			//查询条件校验
			dfStatLogService.queryConditionCheck(thread,start,monthLog, DfLwhhStatLogMonth.class, page);
			
			monthLogPage = dfStatLogService.dfLwhhStatLogMonth(new Page<DfLwhhStatLogMonth>(request, response,DfLwhhStatLogMonth.class), monthLog);
				
		}catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"SearchLwhh参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(activeSys);
			restE.setLogSource(logSource);
			throw restE;
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"来文函号封堵日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "来文函号封堵日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	
	/**
	 * 境内源IP日报表
	 * 
	 * @Title: dfSrcIpDomesticStatLogDailySources
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfSrcIpDomesticStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="境内源IP封堵日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfSrcIpDomesticStatLogDailySources(
			Page page,DfSrcIpDomesticStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfSrcIpDomesticStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			dfStatLogService.queryConditionCheck(thread,start,dailyLog, DfSrcIpDomesticStatLogDaily.class, page);
			
			dailyLogPage = dfStatLogService.dfSrcIpDomesticStatLogDaily(new Page<DfSrcIpDomesticStatLogDaily>(request, response,DfSrcIpDomesticStatLogDaily.class), dailyLog);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"境内源IP封堵日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "境内源IP封堵日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * 境内源IP月报表
	 * 
	 * @Title: dfSrcIpDomesticStatLogMonthSources
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfSrcIpDomesticStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="境内源IP封堵日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfSrcIpDomesticStatLogMonthSources(
			Page page,DfSrcIpDomesticStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfSrcIpDomesticStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			//查询条件校验
			dfStatLogService.queryConditionCheck(thread,start,monthLog, DfSrcIpDomesticStatLogMonth.class, page);
			
			monthLogPage = dfStatLogService.dfSrcIpDomesticStatLogMonth(new Page<DfSrcIpDomesticStatLogMonth>(request, response,DfSrcIpDomesticStatLogMonth.class), monthLog);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"境内源IP封堵日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "境内源IP封堵日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	/**
	 * 国家目的IP日报表
	 * 
	 * @Title: dfDestIpCounrtyStatLogDailySources
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfDestIpCountryStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="国家目的IP封堵日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map dfDestIpCounrtyStatLogDailySources(
			Page page,DfDestIpCounrtyStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfDestIpCounrtyStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			dfStatLogService.queryConditionCheck(thread,start,dailyLog, DfDestIpCounrtyStatLogDaily.class, page);
			
			dailyLogPage = dfStatLogService.dfDestIpCounrtyStatLogDaily(new Page<DfDestIpCounrtyStatLogDaily>(request, response,DfDestIpCounrtyStatLogDaily.class), dailyLog);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"国家目的IP封堵日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "国家目的IP封堵日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * 国家目的IP月报表
	 * 
	 * @Title: dfDestIpCounrtyStatLogMonthSources
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param page
	 * @param @param dailyLog
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	@RequestMapping(value="dfDestIpCountryStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="国家目的IP封堵日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map dfDestIpCounrtyStatLogMonthSources(
			Page page,DfDestIpCounrtyStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DfDestIpCounrtyStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			//查询条件校验
			dfStatLogService.queryConditionCheck(thread,start,monthLog, DfDestIpCounrtyStatLogMonth.class, page);
			
			monthLogPage = dfStatLogService.dfDestIpCounrtyStatLogMonth(new Page<DfDestIpCounrtyStatLogMonth>(request, response,DfDestIpCounrtyStatLogMonth.class), monthLog);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"国家目的IP封堵日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "国家目的IP封堵日志配置月报表检索成功",monthLogPage,activeSys,logSource);
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
