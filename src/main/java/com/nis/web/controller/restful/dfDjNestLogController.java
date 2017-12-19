package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.datanucleus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.DfDjNestLog;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.JsonMapper;
import com.nis.util.redis.RedisDao;
import com.nis.util.redis.SaveRedisThread;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfDjNestLogService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * @ClassName: dfDjNestLogController
 * @Description: 嵌套日志查询服务
 * @author (zbc)
 * @date 2016年11月11日 下午4:40:00
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class dfDjNestLogController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	protected DfDjNestLogService dfDjNestLogService;
	@Autowired
	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	
	protected String logSource = "0";
	
	
	@RequestMapping(value = "/dfDjNestLogs", method = RequestMethod.GET)
	@ApiOperation(value = "嵌套日志分页获取", httpMethod = "GET", notes = "get log list")
	public Map dfDjNestLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			@RequestParam(value = "searchLayerId", required = true) String searchLayerId,
			Page page, DfDjNestLog dfDjNestLog, HttpServletRequest request, HttpServletResponse response) {
		
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys))searchActiveSys=Constants.ACTIVESYS_B;
		if(StringUtils.notEmpty(searchLayerId)) {
			dfDjNestLog.setSearchLayerId(searchLayerId);
		}
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		boolean keyExists = false;
		String key = "";
		Page<DfDjNestLog> dfDjNestLogPage = null;
		
		try {
			// 请求参数校验
			dfDjNestLogService.queryConditionCheck(thread, start, dfDjNestLog, DfDjNestLog.class, page);
			
			if (Constants.IS_OPEN_REDIS) {
				// 根据查询条件获取key
				key = dfDjNestLogService.getJedisKey(request, true);
				// 判断key是否存在
				keyExists = redisDao.exists(key);
			}
			// 存在则直接从redis中查询
			if (keyExists) {
				dfDjNestLogPage = (Page<DfDjNestLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
			} else {
				// 不存在则查询数据库并保存查询结果到redis中
				dfDjNestLogPage = dfDjNestLogService
						.findDfDjNestLogPage(new Page<DfDjNestLog>(request, response, DfDjNestLog.class), dfDjNestLog);

				if (Constants.IS_OPEN_REDIS)
					new SaveRedisThread(key, dfDjNestLogPage, Constants.ORACLE_EXPIRE).start();
			}
			
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "嵌套日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "嵌套日志检索成功",
				dfDjNestLogPage, searchActiveSys, logSource);
	}
}
