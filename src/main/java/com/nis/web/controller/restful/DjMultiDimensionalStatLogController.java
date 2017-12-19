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
import com.nis.domain.restful.DjLwhhAttrDaily;
import com.nis.domain.restful.DjLwhhAttrMonth;
import com.nis.domain.restful.DjLwhhTagDaily;
import com.nis.domain.restful.DjLwhhTagMonth;
import com.nis.domain.restful.DjSrcIpAttrDaily;
import com.nis.domain.restful.DjSrcIpAttrMonth;
import com.nis.domain.restful.DjSrcIpTagDaily;
import com.nis.domain.restful.DjSrcIpTagMonth;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.redis.RedisDao;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DjMultiDimensionalStatLogService;
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
public class DjMultiDimensionalStatLogController extends BaseRestController{
	protected final Logger logger = Logger.getLogger(this.getClass());
	protected String logSource="0";
	protected String activeSys=Constants.ACTIVESYS_ALL;
	
	@Autowired
	protected DjMultiDimensionalStatLogService djMultiDimensionalStatLogService;	
	@Autowired
	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	
	
	@RequestMapping(value="djLwhhAttrDailySources", method = RequestMethod.GET)
	@ApiOperation(value="监测来文函号-性质多维日报表" , httpMethod = "GET",  notes="get log list")
	public Map djLwhhAttrDailySources(
			Page page,DjLwhhAttrDaily djLwhhAttrDaily, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjLwhhAttrDaily> djLwhhAttrDailyPage =null;
		try {
			resetTime(djLwhhAttrDaily, "daily");
			
			djMultiDimensionalStatLogService.queryConditionCheck(thread,start,djLwhhAttrDaily, DjLwhhAttrDaily.class, page);
			
			djLwhhAttrDailyPage = djMultiDimensionalStatLogService.djLwhhAttrDaily(new Page<DjLwhhAttrDaily>(request, response,DjLwhhAttrDaily.class), djLwhhAttrDaily);
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测来文函号-性质多维日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测来文函号-标签多维日报表检索成功",djLwhhAttrDailyPage,activeSys,logSource);
	}
	@RequestMapping(value="djLwhhAttrMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="监测来文函号-性质多维月报表" , httpMethod = "GET",  notes="get log list")
	public Map djLwhhAttrMonthSources(
			Page page,DjLwhhAttrMonth djLwhhAttrMonth, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjLwhhAttrMonth> djLwhhAttrMonthPage =null;
		try {
			resetTime(djLwhhAttrMonth, "month");
			
			
			//查询条件校验
			djMultiDimensionalStatLogService.queryConditionCheck(thread,start,djLwhhAttrMonth, DjLwhhAttrMonth.class, page);
			
			djLwhhAttrMonthPage = djMultiDimensionalStatLogService.djLwhhAttrMonth(new Page<DjLwhhAttrMonth>(request, response,DjLwhhAttrMonth.class), djLwhhAttrMonth);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测来文函号-性质多维月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测来文函号-性质多维月报表检索成功",djLwhhAttrMonthPage,activeSys,logSource);
	}
	
	
	
	@RequestMapping(value="djLwhhTagDailySources", method = RequestMethod.GET)
	@ApiOperation(value="监测来文函号-标签多维日报表" , httpMethod = "GET",  notes="get log list")
	public Map djLwhhTagDailySources(
			Page page,DjLwhhTagDaily djLwhhTagDaily, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjLwhhTagDaily> djLwhhTagDailyPage =null;
		try {
			resetTime(djLwhhTagDaily, "daily");
			
			djMultiDimensionalStatLogService.queryConditionCheck(thread,start,djLwhhTagDaily, DjLwhhTagDaily.class, page);
			
			djLwhhTagDailyPage = djMultiDimensionalStatLogService.djLwhhTagDaily(new Page<DjLwhhTagDaily>(request, response,DjLwhhTagDaily.class), djLwhhTagDaily);
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测来文函号-标签多维日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测来文函号-标签多维日报表检索成功",djLwhhTagDailyPage,activeSys,logSource);
	}
	@RequestMapping(value="djLwhhTagMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="监测来文函号-标签多维月报表" , httpMethod = "GET",  notes="get log list")
	public Map djLwhhTagMonthSources(
			Page page,DjLwhhTagMonth djLwhhTagMonth, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjLwhhTagMonth> djLwhhTagMonthPage =null;
		try {
			resetTime(djLwhhTagMonth, "month");
			
			
			//查询条件校验
			djMultiDimensionalStatLogService.queryConditionCheck(thread,start,djLwhhTagMonth, DjLwhhTagMonth.class, page);
			
			djLwhhTagMonthPage = djMultiDimensionalStatLogService.djLwhhTagMonth(new Page<DjLwhhTagMonth>(request, response,DjLwhhTagMonth.class), djLwhhTagMonth);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测来文函号-标签多维月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测来文函号-标签多维月报表检索成功",djLwhhTagMonthPage,activeSys,logSource);
	}
	
	
	
	@RequestMapping(value="djSrcIpAttrDailySources", method = RequestMethod.GET)
	@ApiOperation(value="监测省-性质多维日报表" , httpMethod = "GET",  notes="get log list")
	public Map djSrcIpAttrDailySources(
			Page page,DjSrcIpAttrDaily djSrcIpAttrDaily, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjSrcIpAttrDaily> djSrcIpAttrDailyPage =null;
		try {
			resetTime(djSrcIpAttrDaily, "daily");
			
			djMultiDimensionalStatLogService.queryConditionCheck(thread,start,djSrcIpAttrDaily, DjSrcIpAttrDaily.class, page);
			
			djSrcIpAttrDailyPage = djMultiDimensionalStatLogService.djSrcIpAttrDaily(new Page<DjSrcIpAttrDaily>(request, response,DjSrcIpAttrDaily.class), djSrcIpAttrDaily);
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测省-性质多维日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测省-标签多维日报表检索成功",djSrcIpAttrDailyPage,activeSys,logSource);
	}
	@RequestMapping(value="djSrcIpAttrMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="监测省-性质多维月报表" , httpMethod = "GET",  notes="get log list")
	public Map djSrcIpAttrMonthSources(
			Page page,DjSrcIpAttrMonth djSrcIpAttrMonth, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjSrcIpAttrMonth> djSrcIpAttrMonthPage =null;
		try {
			resetTime(djSrcIpAttrMonth, "month");
			
			
			//查询条件校验
			djMultiDimensionalStatLogService.queryConditionCheck(thread,start,djSrcIpAttrMonth, DjSrcIpAttrMonth.class, page);
			
			djSrcIpAttrMonthPage = djMultiDimensionalStatLogService.djSrcIpAttrMonth(new Page<DjSrcIpAttrMonth>(request, response,DjSrcIpAttrMonth.class), djSrcIpAttrMonth);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测省-性质多维月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测省-性质多维月报表检索成功",djSrcIpAttrMonthPage,activeSys,logSource);
	}
	
	
	
	@RequestMapping(value="djSrcIpTagDailySources", method = RequestMethod.GET)
	@ApiOperation(value="监测省-标签多维日报表" , httpMethod = "GET",  notes="get log list")
	public Map djSrcIpTagDailySources(
			Page page,DjSrcIpTagDaily djSrcIpTagDaily, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjSrcIpTagDaily> djSrcIpTagDailyPage =null;
		try {
			resetTime(djSrcIpTagDaily, "daily");
			
			djMultiDimensionalStatLogService.queryConditionCheck(thread,start,djSrcIpTagDaily, DjSrcIpTagDaily.class, page);
			
			djSrcIpTagDailyPage = djMultiDimensionalStatLogService.djSrcIpTagDaily(new Page<DjSrcIpTagDaily>(request, response,DjSrcIpTagDaily.class), djSrcIpTagDaily);
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测省-标签多维日报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测省-标签多维日报表检索成功",djSrcIpTagDailyPage,activeSys,logSource);
	}
	@RequestMapping(value="djSrcIpTagMonthSources", method = RequestMethod.GET)
	@ApiOperation(value="监测省-标签多维月报表" , httpMethod = "GET",  notes="get log list")
	public Map djSrcIpTagMonthSources(
			Page page,DjSrcIpTagMonth djSrcIpTagMonth, HttpServletRequest request, HttpServletResponse response) {
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		Page<DjSrcIpTagMonth> djSrcIpTagMonthPage =null;
		try {
			resetTime(djSrcIpTagMonth, "month");
			
			
			//查询条件校验
			djMultiDimensionalStatLogService.queryConditionCheck(thread,start,djSrcIpTagMonth, DjSrcIpTagMonth.class, page);
			
			djSrcIpTagMonthPage = djMultiDimensionalStatLogService.djSrcIpTagMonth(new Page<DjSrcIpTagMonth>(request, response,DjSrcIpTagMonth.class), djSrcIpTagMonth);
				
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测省-标签多维月报表检索失败");
			}
			((RestServiceException) e).setActiveSys(activeSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测省-标签多维月报表检索成功",djSrcIpTagMonthPage,activeSys,logSource);
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
