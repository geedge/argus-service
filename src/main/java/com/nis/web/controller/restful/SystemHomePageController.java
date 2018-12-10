package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.CfgLogInfo;
import com.nis.domain.restful.DropInfo;
import com.nis.domain.restful.NtcAreaHomeReport;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestResult;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.SystemHomePageService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @Description 系统首页整体图API(1.地域流量API 2.配置统计API 3.日志统计API 4.丢弃量API)
 * @author dell
 * @date 2018年12月5日10:34:03
 *
 */
@RestController
@RequestMapping("${servicePath}")
@Api(value = "SystemHomePageController", description = "系统首页整体图接口")
@SuppressWarnings("all")
public class SystemHomePageController extends BaseRestController {

	@Autowired
	ServicesRequestLogService servicesRequestLogService;

	@Autowired
	SystemHomePageService systemHomePageService;

	@RequestMapping(value = "/log/v1/trafficAreaStat", method = RequestMethod.GET)
	@ApiOperation(value = "地域流量获取", httpMethod = "GET", notes = "对地域流量获取服务信息进行查询", response = Map.class)
	public Map<String, ?> trafficAreaStat(String searchReportStartTime, String searchReportEndTime,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();

		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<NtcAreaHomeReport> trafficAreaStatPage = null;

		try {
			trafficAreaStatPage = systemHomePageService.getTrafficAreaStat(searchReportStartTime, searchReportEndTime,
					request, response);

		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
						"获取地域流量失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			} else {
				throw ((RestServiceException) e);
			}
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "获取地域流量成功",
				trafficAreaStatPage, 0);
	}

	@RequestMapping(value = "/log/v1/cfgSortLogStat", method = RequestMethod.GET)
	@ApiOperation(value = "配置日志TOP5统计获取", httpMethod = "GET", notes = "对配置日志TOP5统计信息进行查询", response = Map.class)
	public Map<String, ?> cfgSortLogStat(String searchReportStartTime, String searchReportEndTime,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();

		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<CfgLogInfo> cfgSortLogStat = null;

		try {
			cfgSortLogStat = systemHomePageService.getCfgSortLogStat(searchReportStartTime, searchReportEndTime,
					request, response);

		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
						"获取配置日志TOP5统计失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			} else {
				throw ((RestServiceException) e);
			}
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "获取配置日志TOP5统计成功",
				cfgSortLogStat, 0);
	}

	@RequestMapping(value = "/log/v1/blockAndDropStat", method = RequestMethod.GET)
	@ApiOperation(value = "丢弃量获取", httpMethod = "GET", notes = "对阻断和丢弃量进行查询", response = Map.class)
	public Map<String, ?> blockAndDropStat(String searchReportStartTime, String searchReportEndTime,
			@RequestParam(required = true) int searchBusinessType, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();

		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DropInfo> dropInfo = null;

		try {
			dropInfo = systemHomePageService.getBlockAndDropStat(searchReportStartTime, searchReportEndTime,
					searchBusinessType, request, response);

		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
						"获取丢弃量失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			} else {
				throw ((RestServiceException) e);
			}
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "获取丢弃量成功", dropInfo, 0);

	}

}
