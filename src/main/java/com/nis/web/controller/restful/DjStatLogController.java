/**   
* @Title: DjStatLogController.java 
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
import com.nis.domain.restful.DjAttrStatLogDaily;
import com.nis.domain.restful.DjAttrStatLogMonth;
import com.nis.domain.restful.DjDestIpCounrtyStatLogDaily;
import com.nis.domain.restful.DjDestIpCounrtyStatLogMonth;
import com.nis.domain.restful.DjEntrStatLogDaily;
import com.nis.domain.restful.DjEntrStatLogMonth;
import com.nis.domain.restful.DjLwhhStatLogDaily;
import com.nis.domain.restful.DjLwhhStatLogMonth;
import com.nis.domain.restful.DjSrcIpDomesticStatLogDaily;
import com.nis.domain.restful.DjSrcIpDomesticStatLogMonth;
import com.nis.domain.restful.DjStatLogDaily;
import com.nis.domain.restful.DjStatLogMonth;
import com.nis.domain.restful.DjTagStatLogDaily;
import com.nis.domain.restful.DjTagStatLogMonth;
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
import com.nis.web.service.restful.DjStatLogService;
import com.wordnik.swagger.annotations.ApiOperation;

/** 
* @ClassName: DjStatLogController 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (ddm)  
* @date 2016年9月13日 下午1:22:15   
* @version V1.0 
*/
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DjStatLogController extends BaseRestController{
	protected final Logger logger = Logger.getLogger(this.getClass());
	protected String logSource="0";
	protected String activeSys=Constants.ACTIVESYS_ALL;
	
	@Autowired
	protected DjStatLogService djStatLogService;	
	@Autowired
	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	@RequestMapping(value="djStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="监测日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map djStatLogDailySources(
			Page page,DjStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchConfigId())) {
				Long.parseLong(dailyLog.getSearchConfigId());
			}
			
			djStatLogService.queryConditionCheck(thread,start,dailyLog, DjStatLogDaily.class, page);
			
			dailyLogPage = djStatLogService.djStatLogDaily(new Page<DjStatLogDaily>(request, response,DjStatLogDaily.class), dailyLog);
			
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	@RequestMapping(value="djStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="监测日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map djStatLogMonthSources(
			Page page,DjStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchConfigId())) {
				Long.parseLong(monthLog.getSearchConfigId());
			}
			
			//查询条件校验
			djStatLogService.queryConditionCheck(thread,start,monthLog, DjStatLogMonth.class, page);
			
			monthLogPage = djStatLogService.djStatLogMonth(new Page<DjStatLogMonth>(request, response,DjStatLogMonth.class), monthLog);
				
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	
	/**
	 * 标签日报表
	 * 
	 * @Title: djTagStatLogDailySources
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
	@RequestMapping(value="djTagStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="标签监测日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map djTagStatLogDailySources(
			Page page,DjTagStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		//查询条件校验
		Page<DjTagStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchTag())) {
				Integer.parseInt(dailyLog.getSearchTag());
			}
			
			djStatLogService.queryConditionCheck(thread,start,dailyLog, DjTagStatLogDaily.class, page);
			
			dailyLogPage = djStatLogService.djTagStatLogDaily(new Page<DjTagStatLogDaily>(request, response,DjTagStatLogDaily.class), dailyLog);
				
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"标签监测日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "标签监测日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * 标签月报表
	 * 
	 * @Title: djTagStatLogDailySources
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
	@RequestMapping(value="djTagStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="标签监测日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map djTagStatLogMonthSources(
			Page page,DjTagStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjTagStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchTag())) {
				Integer.parseInt(monthLog.getSearchTag());
			}
			
			//查询条件校验
			djStatLogService.queryConditionCheck(thread,start,monthLog, DjTagStatLogMonth.class, page);
			
			monthLogPage = djStatLogService.djTagStatLogMonth(new Page<DjTagStatLogMonth>(request, response,DjTagStatLogMonth.class), monthLog);
				
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"标签监测日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "标签监测日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	
	/**
	 * @Title: djTagStatLogDailySources
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
	@RequestMapping(value="djAttrStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="性质监测日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map djAttrStatLogDailySources(
			Page page,DjAttrStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjAttrStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchAttrType())) {
				Integer.parseInt(dailyLog.getSearchAttrType());
			}
			
			djStatLogService.queryConditionCheck(thread,start,dailyLog, DjAttrStatLogDaily.class, page);
			
			dailyLogPage = djStatLogService.djAttrStatLogDaily(new Page<DjAttrStatLogDaily>(request, response,DjAttrStatLogDaily.class), dailyLog);
				
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"性质监测日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "性质监测日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * @Title: djAttrStatLogDailySources
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
	@RequestMapping(value="djAttrStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="性质监测日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map djAttrStatLogMonthSources(
			Page page,DjAttrStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjAttrStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchAttrType())) {
				Integer.parseInt(monthLog.getSearchAttrType());
			}
			
			//查询条件校验
			djStatLogService.queryConditionCheck(thread,start,monthLog, DjAttrStatLogMonth.class, page);
			
			monthLogPage = djStatLogService.djAttrStatLogMonth(new Page<DjAttrStatLogMonth>(request, response,DjAttrStatLogMonth.class), monthLog);
			
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"性质监测日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "性质监测日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	
	/**
	 * @Title: djEntrStatLogDailySources
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
	@RequestMapping(value="djEntrStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="局点监测日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map djEntrStatLogDailySources(
			Page page,DjEntrStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjEntrStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchEntranceId())) {
				Integer.parseInt(dailyLog.getSearchEntranceId());
			}
			
			djStatLogService.queryConditionCheck(thread,start,dailyLog, DjEntrStatLogDaily.class, page);
			
			dailyLogPage = djStatLogService.djEntrStatLogDaily(new Page<DjEntrStatLogDaily>(request, response,DjEntrStatLogDaily.class), dailyLog);
				
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"局点监测日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "局点监测日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * @Title: djEntrStatLogDailySources
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
	@RequestMapping(value="djEntrStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="局点监测日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map djEntrStatLogMonthSources(
			Page page,DjEntrStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjEntrStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchEntranceId())) {
				Integer.parseInt(monthLog.getSearchEntranceId());
			}
			//查询条件校验
			djStatLogService.queryConditionCheck(thread,start,monthLog, DjEntrStatLogMonth.class, page);
			
			monthLogPage = djStatLogService.djEntrStatLogMonth(new Page<DjEntrStatLogMonth>(request, response,DjEntrStatLogMonth.class), monthLog);
			
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"局点监测日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "局点监测日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	/**
	 * @Title: djLwhhStatLogDailySources
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
	@RequestMapping(value="djLwhhStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="来文函号监测日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map djLwhhStatLogDailySources(
			Page page,DjLwhhStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		//查询条件校验
		Page<DjLwhhStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			if (!StringUtil.isBlank(dailyLog.getSearchLwhh())) {
				Integer.parseInt(dailyLog.getSearchLwhh());
			}
			
			djStatLogService.queryConditionCheck(thread,start,dailyLog, DjLwhhStatLogDaily.class, page);
			
			dailyLogPage = djStatLogService.djLwhhStatLogDaily(new Page<DjLwhhStatLogDaily>(request, response,DjLwhhStatLogDaily.class), dailyLog);
			
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"来文函号监测日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "来文函号监测日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * @Title: djLwhhStatLogDailySources
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
	@RequestMapping(value="djLwhhStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="来文函号监测日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map djLwhhStatLogMonthSources(
			Page page,DjLwhhStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjLwhhStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			
			if (!StringUtil.isBlank(monthLog.getSearchLwhh())) {
				Integer.parseInt(monthLog.getSearchLwhh());
			}
			//查询条件校验
			djStatLogService.queryConditionCheck(thread,start,monthLog, DjLwhhStatLogMonth.class, page);
			
			monthLogPage = djStatLogService.djLwhhStatLogMonth(new Page<DjLwhhStatLogMonth>(request, response,DjLwhhStatLogMonth.class), monthLog);
				
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
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"来文函号监测日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "来文函号监测日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	
	/**
	 * 境内源IP日报表
	 * 
	 * @Title: djSrcIpDomesticStatLogDailySources
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
	@RequestMapping(value="djSrcIpDomesticStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="境内源IP监测日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map djSrcIpDomesticStatLogDailySources(
			Page page,DjSrcIpDomesticStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjSrcIpDomesticStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			djStatLogService.queryConditionCheck(thread,start,dailyLog, DjSrcIpDomesticStatLogDaily.class, page);
			
			dailyLogPage = djStatLogService.djSrcIpDomesticStatLogDaily(new Page<DjSrcIpDomesticStatLogDaily>(request, response,DjSrcIpDomesticStatLogDaily.class), dailyLog);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"境内源IP监测日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "境内源IP监测日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * 境内源IP月报表
	 * 
	 * @Title: djSrcIpDomesticStatLogMonthSources
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
	@RequestMapping(value="djSrcIpDomesticStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="境内源IP监测日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map djSrcIpDomesticStatLogMonthSources(
			Page page,DjSrcIpDomesticStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjSrcIpDomesticStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			//查询条件校验
			djStatLogService.queryConditionCheck(thread,start,monthLog, DjSrcIpDomesticStatLogMonth.class, page);
			
			monthLogPage = djStatLogService.djSrcIpDomesticStatLogMonth(new Page<DjSrcIpDomesticStatLogMonth>(request, response,DjSrcIpDomesticStatLogMonth.class), monthLog);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"境内源IP监测日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "境内源IP监测日志配置月报表检索成功",monthLogPage,activeSys,logSource);
	}
	/**
	 * 国家目的IP日报表
	 * 
	 * @Title: djDestIpCounrtyStatLogDailySources
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
	@RequestMapping(value="djDestIpCountryStatLogDailySources", method = RequestMethod.GET)
	@ApiOperation(value="国家目的IP监测日志配置日报表" , httpMethod = "GET",  notes="get log list")
	public Map djDestIpCounrtyStatLogDailySources(
			Page page,DjDestIpCounrtyStatLogDaily dailyLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjDestIpCounrtyStatLogDaily> dailyLogPage =null;
		try {
			resetTime(dailyLog, "daily");
			
			djStatLogService.queryConditionCheck(thread,start,dailyLog, DjDestIpCounrtyStatLogDaily.class, page);
			
			dailyLogPage = djStatLogService.djDestIpCounrtyStatLogDaily(new Page<DjDestIpCounrtyStatLogDaily>(request, response,DjDestIpCounrtyStatLogDaily.class), dailyLog);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"国家目的IP监测日志配置日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "国家目的IP监测日志配置日报表检索成功",dailyLogPage,activeSys,logSource);
	}
	/**
	 * 国家目的IP月报表
	 * 
	 * @Title: djDestIpCounrtyStatLogMonthSources
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
	@RequestMapping(value="djDestIpCountryStatLogMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="国家目的IP监测日志配置月报表" , httpMethod = "GET",  notes="get log list")
	public Map djDestIpCounrtyStatLogMonthSources(
			Page page,DjDestIpCounrtyStatLogMonth monthLog, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjDestIpCounrtyStatLogMonth> monthLogPage =null;
		try {
			resetTime(monthLog, "month");
			//查询条件校验
			djStatLogService.queryConditionCheck(thread,start,monthLog, DjDestIpCounrtyStatLogMonth.class, page);
			
			monthLogPage = djStatLogService.djDestIpCounrtyStatLogMonth(new Page<DjDestIpCounrtyStatLogMonth>(request, response,DjDestIpCounrtyStatLogMonth.class), monthLog);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"国家目的IP监测日志配置月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "国家目的IP监测日志配置月报表检索成功",monthLogPage,activeSys,logSource);
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
