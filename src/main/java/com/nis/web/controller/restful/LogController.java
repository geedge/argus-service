package com.nis.web.controller.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zdjizhi.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.LogEntity;
import com.nis.domain.Page;
import com.nis.domain.restful.DkBehaviorLog;
import com.nis.domain.restful.PxyHttpLog;
import com.nis.restful.RestServiceException;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.LogJDBCByDruid;
import com.nis.util.JsonMapper;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.HiveSqlService;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.NtcLogService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * <p>Title: LogController</p>
 * <p>Description: 日志查询controller</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年7月2日 
 *
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@Api(value = "LogController", description = "配置命中日志基本服务接口")
public class LogController extends BaseRestController {
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	@Autowired
	protected NtcLogService ntcLogService;

	@RequestMapping(value = "/dkBehaviorLogs", method = RequestMethod.GET)
	@ApiOperation(value = "行为识别日志查询", httpMethod = "GET", notes = "对日志功能“行为识别”提供数据基础查询服务")
	public Map<String, ?> dkBehaviorLogs(Page page, DkBehaviorLog dkBehaviorLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<DkBehaviorLog> logPage = null;
		try {
			resetTime(dkBehaviorLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, dkBehaviorLog, DkBehaviorLog.class, page);
			logPage = new Page<DkBehaviorLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(DkBehaviorLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			String sql = HiveSqlService.getSql(page, dkBehaviorLog,
					getTableName(DkBehaviorLog.class.getSimpleName() + "HiveTable", "DK_BEHAVIOR_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, DkBehaviorLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<DkBehaviorLog> List = (java.util.List<DkBehaviorLog>) JsonMapper.fromJsonList(jsonString,
							DkBehaviorLog.class);
					logPage.setList(List);
					logPage.setCount(List.size());

				} else {
					logPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "行为识别日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "行为识别日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/pxyHttpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "PXY HTTP日志查询", httpMethod = "GET", notes = "对日志功能“控制策略”-“HTTP日志”提供数据基础查询服务")
	public Map<String, ?> pxyHttpLogs(Page page, PxyHttpLog pxyHttpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<PxyHttpLog> logPage = null;
		try {
			resetTime(pxyHttpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, pxyHttpLog, PxyHttpLog.class, page);
			logPage = new Page<PxyHttpLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(PxyHttpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			String sql = HiveSqlService.getSql(page, pxyHttpLog,
					getTableName(PxyHttpLog.class.getSimpleName() + "HiveTable", "PXY_HTTP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, PxyHttpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<PxyHttpLog> List = (java.util.List<PxyHttpLog>) JsonMapper.fromJsonList(jsonString,
							PxyHttpLog.class);
					logPage.setList(List);
					logPage.setCount(List.size());

				} else {
					logPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "PXY HTTP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "PXY HTTP日志检索成功",
				logPage, 0);
	}
	

	
	
	/**
	 *判断开始和结束时间是否为null,如果为null则初始化时间
	 * @param entity
	 * @throws Exception
	 */
	public void resetTime(LogEntity<?> entity) throws Exception {
		if (StringUtil.isEmpty(entity.getSearchFoundStartTime())&& StringUtil.isEmpty(entity.getSearchFoundEndTime())) {
			Map<String, String> map = DateUtils.getLocalTime(entity.getSearchFoundStartTime(),
					entity.getSearchFoundEndTime(), Constants.LOG_LOCAL_TIME, "minute");
			entity.setSearchFoundStartTime(map.get("startTime"));
			entity.setSearchFoundEndTime(map.get("endTime"));
		}
	}

	/**
	 * 将searchFoundStartTime,searchFoundEndTime与foundTime进行关联
	 * @return
	 */
	public Map<String, Map<String, String>> getCol2Col() {
		Map<String, Map<String, String>> col2col = new HashMap<String, Map<String, String>>();
		Map<String, String> startMap = new HashMap<String, String>();
		startMap.put("start", "foundTime");
		col2col.put("searchFoundStartTime", startMap);
		Map<String, String> endMap = new HashMap<String, String>();
		endMap.put("end", "foundTime");
		col2col.put("searchFoundEndTime", endMap);
		return col2col;
	}

}
