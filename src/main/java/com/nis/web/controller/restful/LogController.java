package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.DkBehaviorLog;
import com.nis.domain.restful.PxyHttpLog;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Constants;
import com.nis.util.ExceptionUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.LogDataService;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.NtcLogService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * <p>
 * Title: LogController
 * </p>
 * <p>
 * Description: 日志查询controller
 * </p>
 * <p>
 * Company: IIE
 * </p>
 * 
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
	@Autowired
	private LogDataService logDataService;

	@RequestMapping(value = "/dkBehaviorLogs", method = RequestMethod.GET)
	@ApiOperation(value = "行为识别日志查询", httpMethod = "GET", notes = "对日志功能“行为识别”提供数据基础查询服务")
	public Map<String, ?> dkBehaviorLogs(Page page, DkBehaviorLog dkBehaviorLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		try {
			resetTime(dkBehaviorLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, dkBehaviorLog, DkBehaviorLog.class, page);
			logDataService.getData(page, dkBehaviorLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("行为识别日志检索失败:"+e.getMessage());
			logger.error("行为识别日志检索失败："+ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"行为识别日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"行为识别日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"行为识别日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "行为识别日志检索成功", page, 0);
	}

	@RequestMapping(value = "/pxyHttpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "PXY HTTP日志查询", httpMethod = "GET", notes = "对日志功能“控制策略”-“HTTP日志”提供数据基础查询服务")
	public Map<String, ?> pxyHttpLogs(Page page, PxyHttpLog pxyHttpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(pxyHttpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, pxyHttpLog, PxyHttpLog.class, page);
			logDataService.getData(page, pxyHttpLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("PXY HTTP日志检索失败:"+e.getMessage());
			logger.error("PXY HTTP日志检索失败："+ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"PXY HTTP日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"PXY HTTP日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"PXY HTTP日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "PXY HTTP日志检索成功", page,
				0);
	}

}
