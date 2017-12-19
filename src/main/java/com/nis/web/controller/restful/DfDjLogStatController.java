package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.DfDjLogStatistics;
import com.nis.domain.restful.DfDjPzLogStatistics;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.JsonMapper;
import com.nis.util.redis.RedisDao;
import com.nis.util.redis.SaveRedisThread;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfdjLogStatService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: LogStatisticsController
 * @Description: 封堵/监测日志统计查询服务
 * @author (zbc)
 * @date 2016年11月14日 下午4:00:00
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DfDjLogStatController extends BaseRestController {
	
	protected final Logger logger = Logger.getLogger(this.getClass());
	protected String logSource="0";

	@Autowired
	protected DfdjLogStatService logStatService;
	@Autowired
	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	
	/*@RequestMapping(value="/dfLogStatistics", method = RequestMethod.GET)
	@ApiOperation(value="封堵日志总量统计查询" , httpMethod = "GET",  notes="get log list")
	public Map dfLogStatistics(
			@RequestParam(value = "searchStatActiveSys", required = false, defaultValue = "2") String searchStatActiveSys,
			@RequestParam(value = "searchService", required = true) String searchService,
			DfDjLogStatistics logStatistics, HttpServletRequest request, HttpServletResponse response) {
		
		if(!"4".equals(searchStatActiveSys) ) {
			searchStatActiveSys="2";
			logStatistics.setSearchStatActiveSys(searchStatActiveSys);
		}
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		
		DfDjLogStatistics entity = null;
		try{
			logStatService.queryConditionCheck(thread,start,logStatistics, DfDjLogStatistics.class, null);
			
			entity = logStatService.findDfLogStatistics(logStatistics);
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵日志总量统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchStatActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "封堵日志总量统计检索成功",
				entity, searchStatActiveSys, logSource);
	}
		
	@RequestMapping(value="/dfPzLogStatistics", method = RequestMethod.GET)
	@ApiOperation(value="封堵配置日志总量统计查询" , httpMethod = "GET",  notes="get log list")
	public Map dfPzLogStatistics(
			@RequestParam(value = "searchStatActiveSys", required = false, defaultValue = "2") String searchStatActiveSys,
			@RequestParam(value = "searchService", required = true) String searchService,
			Page<DfDjPzLogStatistics> page, DfDjPzLogStatistics logStatistics, 
			HttpServletRequest request, HttpServletResponse response) {
		
		if(!"4".equals(searchStatActiveSys) ) {
			searchStatActiveSys="2";
			logStatistics.setSearchStatActiveSys(searchStatActiveSys);
		}
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		
		Page<DfDjPzLogStatistics> logPage = null;
		boolean keyExists = false;
		String key = "";
		
		try {
			logStatService.queryConditionCheck(thread,start,logStatistics, DfDjPzLogStatistics.class, page);
			
			if (Constants.IS_OPEN_REDIS) {
				// 根据查询条件获取key
				key = logStatService.getJedisKey(request, true);
				// 判断key是否存在
				keyExists = redisDao.exists(key);
			}
			// 存在则直接从redis中查询
			if (keyExists) {
				logPage = (Page<DfDjPzLogStatistics>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
			} else {
				// 不存在则查询数据库并保存查询结果到redis中
				logPage = logStatService.findDfPzLogStatistics(
						new Page<DfDjPzLogStatistics>(request, response, DfDjPzLogStatistics.class), logStatistics);

				if (Constants.IS_OPEN_REDIS)
					new SaveRedisThread(key, logPage, Constants.ORACLE_EXPIRE).start();
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"封堵配置日志总量统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchStatActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "封堵配置日志总量统计检索成功",
				logPage, searchStatActiveSys, logSource);
	}

	@RequestMapping(value="/djLogStatistics", method = RequestMethod.GET)
	@ApiOperation(value="监测日志总量统计查询" , httpMethod = "GET",  notes="get log list")
	public Map djLogStatistics(
			@RequestParam(value = "searchStatActiveSys", required = false, defaultValue = "2") String searchStatActiveSys,
			@RequestParam(value = "searchService", required = true) String searchService,
			DfDjLogStatistics logStatistics, HttpServletRequest request, HttpServletResponse response) {
		
		if(!"4".equals(searchStatActiveSys) ) {
			searchStatActiveSys="2";
			logStatistics.setSearchStatActiveSys(searchStatActiveSys);
		}
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		
		DfDjLogStatistics entity = null;
		try{
			logStatService.queryConditionCheck(thread,start,logStatistics, DfDjLogStatistics.class, null);
			
			entity = logStatService.findDjLogStatistics(logStatistics);
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测日志总量统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchStatActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测日志总量统计检索成功",
				entity, searchStatActiveSys, logSource);
	}

	@RequestMapping(value="/djPzLogStatistics", method = RequestMethod.GET)
	@ApiOperation(value="监测配置日志总量统计查询" , httpMethod = "GET",  notes="get log list")
	public Map djPzLogStatistics(
			@RequestParam(value = "searchStatActiveSys", required = false, defaultValue = "2") String searchStatActiveSys,
			@RequestParam(value = "searchService", required = true) String searchService,
			Page<DfDjPzLogStatistics> page, DfDjPzLogStatistics logStatistics, 
			HttpServletRequest request, HttpServletResponse response) {
		
		if(!"4".equals(searchStatActiveSys) ) {
			searchStatActiveSys="2";
			logStatistics.setSearchStatActiveSys(searchStatActiveSys);
		}
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		
		Page<DfDjPzLogStatistics> logPage = null;
		boolean keyExists = false;
		String key = "";
		
		try {
			logStatService.queryConditionCheck(thread,start,logStatistics, DfDjPzLogStatistics.class, page);
			
			if (Constants.IS_OPEN_REDIS) {
				// 根据查询条件获取key
				key = logStatService.getJedisKey(request, true);
				// 判断key是否存在
				keyExists = redisDao.exists(key);
			}
			// 存在则直接从redis中查询
			if (keyExists) {
				logPage = (Page<DfDjPzLogStatistics>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
			} else {
				// 不存在则查询数据库并保存查询结果到redis中
				logPage = logStatService.findDjPzLogStatistics(
						new Page<DfDjPzLogStatistics>(request, response, DfDjPzLogStatistics.class), logStatistics);

				if (Constants.IS_OPEN_REDIS)
					new SaveRedisThread(key, logPage, Constants.ORACLE_EXPIRE).start();
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(!(e instanceof RestServiceException)){
				e=new RestServiceException(thread,System.currentTimeMillis()-start,"监测配置日志总量统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchStatActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测配置日志总量统计检索成功",
				logPage, searchStatActiveSys, logSource);
	}*/
}
