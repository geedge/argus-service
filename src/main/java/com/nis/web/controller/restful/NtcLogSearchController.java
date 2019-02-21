package com.nis.web.controller.restful;

import com.nis.datasource.CustomerContextHolder;
import com.nis.domain.Page;
import com.nis.domain.restful.*;
import com.nis.domain.restful.dashboard.TrafficAsnStatistic;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.ExceptionUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.*;
import com.nis.web.service.restful.NtcLogService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zdjizhi.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @ClassName:NtcLogSearchController
 * @Description:TODO(NTC配置命中日志基本服务接口)
 * @author (zdx)
 * @date 2018年7月24日 下午5:26:42
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@Api(value = "NtcLogSearchController", description = "NTC配置命中日志基本服务接口")
public class NtcLogSearchController extends BaseRestController {
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	@Autowired
	protected NtcSslRecordLogsService  ntcSslRecordLogsService;//ssl泛收

	@Autowired
	protected NtcHttpRecordLogsService ntcHttpRecordLogsService;//http泛收
	
	@Autowired
	protected NtcLogService ntcLogService;
	@Autowired
	private LogDataService logDataService;

	@RequestMapping(value = "/ntcIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "IP地址日志查询", httpMethod = "GET", notes = "对应配置为IP地址管理，存储动作为阻断与监测的命中日志。对日志功能IP地址提供数据基础查询服务")
	public Map<String, ?> ntcIpLogs(Page page, NtcIpLog ntcIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		try {
			resetTime(ntcIpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcIpLog, NtcIpLog.class, page);
			logDataService.getData(page, ntcIpLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("IP地址日志检索失败:" + e.getMessage());
			logger.error("IP地址日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"IP地址日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"IP地址日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"IP地址日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}

		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "IP地址日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcHttpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-HTTP”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-HTTP”提供数据基础查询服务")
	public Map<String, ?> ntcHttpLogs(Page page, NtcHttpLog ntcHttpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcHttpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcHttpLog, NtcHttpLog.class, page);
			logDataService.getData(page, ntcHttpLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("Http日志检索失败:" + e.getMessage());
			logger.error("Http日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"Http日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"Http日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"Http日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Http日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcDnsLogs", method = RequestMethod.GET)
	@ApiOperation(value = "DNS日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-DNS”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-DNS”提供数据基础查询服务。")
	public Map<String, ?> ntcDnsLogs(Page page, NtcDnsLog ntcDnsLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcDnsLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcDnsLog, NtcDnsLog.class, page);
			logDataService.getData(page, ntcDnsLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("Dns日志检索失败:" + e.getMessage());
			logger.error("Dns日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"Dns日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"Dns日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"Dns日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Dns日志检索成功", page, 0);
	}
	
	//日志  Mail  和  Mail泛收 用同一个接口
	@RequestMapping(value = "/ntcMailLogs", method = RequestMethod.GET)
	@ApiOperation(value = "EMAIL日志查询", httpMethod = "GET", notes = "对应配置为“邮件管理”，存储动作为阻断与监测的命中日志。对日志功能“邮件管理”提供数据基础查询服务。")
	public Map<String, ?> ntcMailLogs(Page page, NtcMailLog ntcMailLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		//判断searchCfgId是否为null
//		if(ntcMailLog.getSearchCfgId() ==null){
//			ntcMailLog.setSearchCfgId("-1");
//		}
		try {
			resetTime(ntcMailLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcMailLog, NtcMailLog.class, page);
			logDataService.getData(page, ntcMailLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("EMAIL日志检索失败:" + e.getMessage());
			logger.error("EMAIL日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"EMAIL日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"EMAIL日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"EMAIL日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "EMAIL日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcSslLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSL日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-SSL”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-SSL”提供数据基础查询服务。")
	public Map<String, ?> ntcSslLogs(Page page, NtcSslLog ntcSslLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcSslLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcSslLog, NtcSslLog.class, page);
			logDataService.getData(page, ntcSslLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("SSL日志检索失败:" + e.getMessage());
			logger.error("SSL日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"SSL日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"SSL日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"SSL日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "SSL日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcPptpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "PPTP日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-PPTP”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理-PPTP”提供数据基础查询服务。")
	public Map<String, ?> ntcPptpLogs(Page page, NtcPptpLog ntcPptpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcPptpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcPptpLog, NtcPptpLog.class, page);
			logDataService.getData(page, ntcPptpLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("PPTP日志检索失败:" + e.getMessage());
			logger.error("PPTP日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"PPTP日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"PPTP日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"PPTP日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "PPTP日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcL2tpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "L2TP日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-L2TP”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- L2TP”提供数据基础查询服务。")
	public Map<String, ?> ntcL2tpLogs(Page page, NtcL2tpLog ntcL2tpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcL2tpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcL2tpLog, NtcL2tpLog.class, page);
			logDataService.getData(page, ntcL2tpLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("L2TP日志检索失败:" + e.getMessage());
			logger.error("L2TP日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"L2TP日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"L2TP日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"L2TP日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "L2TP日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcOpenvpnLogs", method = RequestMethod.GET)
	@ApiOperation(value = "OPENVPN日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-OPENVPN”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- OPENVPN”提供数据基础查询服务。")
	public Map<String, ?> ntcOpenvpnLogs(Page page, NtcOpenvpnLog ntcOpenvpnLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcOpenvpnLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcOpenvpnLog, NtcOpenvpnLog.class, page);
			logDataService.getData(page, ntcOpenvpnLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("Openvpn日志检索失败:" + e.getMessage());
			logger.error("Openvpn日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"Openvpn日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"Openvpn日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"Openvpn日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Openvpn日志检索成功", page,
				0);
	}

	@RequestMapping(value = "/ntcIpsecLogs", method = RequestMethod.GET)
	@ApiOperation(value = "IPSEC日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-IPSEC”，存储动作为监测的命中日志。对日志功能“隧道管理- IPSEC”提供数据基础查询服务。")
	public Map<String, ?> ntcIpsecLogs(Page page, NtcIpsecLog ntcIpsecLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcIpsecLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcIpsecLog, NtcIpsecLog.class, page);
			logDataService.getData(page, ntcIpsecLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("IPSEC日志检索失败:" + e.getMessage());
			logger.error("IPSEC日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"IPSEC日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"IPSEC日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"IPSEC日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "IPSEC日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcSshLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSH日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-SSH”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- SSH”提供数据基础查询服务。")
	public Map<String, ?> ntcSshLogs(Page page, NtcSshLog ntcSshLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcSshLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcSshLog, NtcSshLog.class, page);
			logDataService.getData(page, ntcSshLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("SSH日志检索失败:" + e.getMessage());
			logger.error("SSH日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"SSH日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"SSH日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"SSH日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "SSH日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcFtpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "FTP日志查询", httpMethod = "GET", notes = "对应配置为“文件传输-FTP”，存储动作为阻断与监测的命中日志。对日志功能“文件传输-FTP”提供数据基础查询服务。")
	public Map<String, ?> ntcFtpLogs(Page page, NtcFtpLog ntcFtpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcFtpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcFtpLog, NtcFtpLog.class, page);

			logDataService.getData(page, ntcFtpLog);

		} catch (Exception e) {
			auditLogThread.setExceptionInfo("FTP日志检索失败:" + e.getMessage());
			logger.error("FTP日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"FTP日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"FTP日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"FTP日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "FTP日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcAppLogs", method = RequestMethod.GET)
	@ApiOperation(value = "App日志查询", httpMethod = "GET", notes = "对应配置为“App管理”，存储动作为阻断与监测的命中日志。对日志功能“APP策略日志”提供数据基础查询服务")
	public Map<String, ?> ntcAppLogs(Page page, NtcAppLog ntcAppLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		try {
			resetTime(ntcAppLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcAppLog, NtcAppLog.class, page);
			logDataService.getData(page, ntcAppLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("App日志检索失败:" + e.getMessage());
			logger.error("App日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"App日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "App日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcDdosLogs", method = RequestMethod.GET)
	@ApiOperation(value = "DDos日志查询", httpMethod = "GET", notes = "对应配置为“DDOS日志监控”，存储动作为丢弃的命中日志。对日志功能“DDOS日志监控”提供数据基础查询服务")
	public Map<String, ?> ntcDdosLogs(Page page, NtcDdosLog ntcDdosLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcDdosLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcDdosLog, NtcDdosLog.class, page);
			logDataService.getData(page, ntcDdosLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("DDos日志检索失败:" + e.getMessage());
			logger.error("DDos日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"DDos日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"DDos日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"DDos日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "DDos日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcP2pLogs", method = RequestMethod.GET)
	@ApiOperation(value = "P2P日志查询", httpMethod = "GET", notes = "对应配置为“文件传输管理-P2P”，存储动作为阻断的命中日志。对日志功能“文件传输管理-P2P日志”提供数据基础查询服务")
	public Map<String, ?> ntcP2pLogs(Page page, NtcP2pLog ntcP2pLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcP2pLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcP2pLog, NtcP2pLog.class, page);

			logDataService.getData(page, ntcP2pLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("P2P日志检索失败:" + e.getMessage());
			logger.error("P2P日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"P2P日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"P2P日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"P2P日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "P2P日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcBgpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "BGP日志查询", httpMethod = "GET", notes = "对应配置为“其他-BGP”，存储动作为阻断和监测的命中日志。对日志功能“BGP日志”提供数据基础查询服务")
	public Map<String, ?> ntcBgpLogs(Page page, NtcBgpLog ntcBgpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcBgpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcBgpLog, NtcBgpLog.class, page);

			logDataService.getData(page, ntcBgpLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("BGP日志检索失败:" + e.getMessage());
			logger.error("BGP日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"BGP日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"BGP日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"BGP日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "BGP日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcVoipLogs", method = RequestMethod.GET)
	@ApiOperation(value = "NTC VoIP日志查询", httpMethod = "GET", notes = "对日志功能“NTC VoIP日志”提供数据基础查询服务")
	public Map<String, ?> ntcVoipLogs(Page page, NtcVoipLog ntcVoipLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcVoipLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcVoipLog, NtcVoipLog.class, page);
			logDataService.getData(page, ntcVoipLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("NTC VoIP日志检索失败:" + e.getMessage());
			logger.error("NTC VoIP日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"NTC VoIP日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"NTC VoIP日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"NTC VoIP日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "NTC VoIP日志检索成功", page,
				0);
	}

	@RequestMapping(value = "/ntcStreamMediaLogs", method = RequestMethod.GET)
	@ApiOperation(value = "流媒体协议日志查询", httpMethod = "GET", notes = "对日志功能“流媒体协议日志”提供数据基础查询服务")
	public Map<String, ?> ntcStreamMediaLogs(Page page, NtcStreamingMediaLog ntcStreamMediaLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcStreamMediaLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcStreamMediaLog, NtcStreamingMediaLog.class,
					page);
			logDataService.getData(page, ntcStreamMediaLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("流媒体协议日志检索失败:" + e.getMessage());
			logger.error("流媒体协议日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"流媒体协议日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"流媒体协议日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"流媒体协议日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "流媒体协议日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcKeywordsUrlLogs", method = RequestMethod.GET)
	@ApiOperation(value = "NTC关键字转URL日志查询", httpMethod = "GET", notes = "对日志功能“NTC关键字转URL日志”提供数据基础查询服务")
	public Map<String, ?> ntcKeywordsUrlLogs(Page page, NtcKeywordsUrlLog ntcKeywordsUrlLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(ntcKeywordsUrlLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcKeywordsUrlLog, NtcKeywordsUrlLog.class, page);
			logDataService.getData(page, ntcKeywordsUrlLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("NTC关键字转URL日志检索失败:" + e.getMessage());
			logger.error("NTC关键字转URL日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"NTC关键字转URL日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"NTC关键字转URL日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"NTC关键字转URL日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "NTC关键字转URL日志检索成功", page,
				0);
	}

	@RequestMapping(value = "/ntcCollectVoipLogs", method = RequestMethod.GET)
	@ApiOperation(value = "VoIp泛收日志查询", httpMethod = "GET", notes = "对日志功能“VoIp泛收日志查询”提供数据基础查询服务")
	public Map<String, ?> ntcCollectVoipLogs(Page page, NtcCollectVoipLog ntcCollectVoipLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			if (StringUtil.isEmpty(ntcCollectVoipLog.getSearchFoundStartTime())
					&& StringUtil.isEmpty(ntcCollectVoipLog.getSearchFoundEndTime())) {
				Map<String, String> map = DateUtils.getLocalTime(null, null, Constants.LOG_LOCAL_TIME, "minute");
				ntcCollectVoipLog.setSearchFoundStartTime(map.get("startTime"));
				ntcCollectVoipLog.setSearchFoundEndTime(map.get("endTime"));
			}

			ntcLogService.collectConditionCheck(auditLogThread, start, ntcCollectVoipLog, NtcCollectVoipLog.class,
					page);
			logDataService.getData(page, ntcCollectVoipLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("VoIp泛收日志检索失败:" + e.getMessage());
			logger.error("VoIp泛收日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"VoIp泛收日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"VoIp泛收日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"VoIp泛收日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "VoIp泛收日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcCollectRadiusLogs", method = RequestMethod.GET)
	@ApiOperation(value = "RADIUS泛收日志查询", httpMethod = "GET", notes = "对日志功能“RADIUS泛收日志查询”提供数据基础查询服务")
	public Map<String, ?> ntcCollectRadiusLogs(Page page, NtcCollectRadiusLog ntcCollectRadiusLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			if (StringUtil.isEmpty(ntcCollectRadiusLog.getSearchFoundStartTime())
					&& StringUtil.isEmpty(ntcCollectRadiusLog.getSearchFoundEndTime())) {
				Map<String, String> map = DateUtils.getLocalTime(null, null, Constants.LOG_LOCAL_TIME, "minute");
				ntcCollectRadiusLog.setSearchFoundStartTime(map.get("startTime"));
				ntcCollectRadiusLog.setSearchFoundEndTime(map.get("endTime"));
			}

			ntcLogService.queryConditionCheck(auditLogThread, start, ntcCollectRadiusLog, NtcCollectRadiusLog.class,
					page);
			logDataService.getData(page, ntcCollectRadiusLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("RADIUS泛收日志检索失败:" + e.getMessage());
			logger.error("RADIUS泛收日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"RADIUS泛收日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"RADIUS泛收日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"RADIUS泛收日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "RADIUS泛收日志检索成功", page,
				0);
	}

	@RequestMapping(value = "/ntcConnRecordLogs", method = RequestMethod.GET)
	@ApiOperation(value = "通联关系日志查询", httpMethod = "GET", notes = "对日志功能“通联关系日志查询”提供数据基础查询服务")
	public Map<String, ?> ntcConnRecordLogs(Page page, NtcConnRecordLog ntcConnRecordLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

		try {
			if (StringUtil.isBlank(ntcConnRecordLog.getSearchFoundStartTime())
					&& StringUtil.isBlank(ntcConnRecordLog.getSearchFoundEndTime())) {
				Map<String, String> map = DateUtils.getLocalTime(null, null, Constants.LOG_LOCAL_TIME, "minute");
				ntcConnRecordLog.setSearchFoundStartTime(map.get("startTime"));
				ntcConnRecordLog.setSearchFoundEndTime(map.get("endTime"));
			}

			ntcLogService.ntcConnRecordLogsConditionCheck(auditLogThread, start, ntcConnRecordLog, NtcConnRecordLog.class, page);

			logDataService.getData(page, ntcConnRecordLog);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("通联关系日志检索失败:" + e.getMessage());
			logger.error("通联关系日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"通联关系日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"通联关系日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"通联关系日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "通联关系日志检索成功", page, 0);
	}

	@RequestMapping(value = "/ntcConnRecordPercent", method = RequestMethod.GET)
	@ApiOperation(value = "通联关系日志百分比查询", httpMethod = "GET", notes = "对日志功能“通联关系日志百分比查询”提供数据基础查询服务")
	public Map<String, ?> ntcConnRecordPercent(NtcConnRecordPercent ntcConnRecordPercent, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		try {
			if (StringUtil.isBlank(ntcConnRecordPercent.getSearchFoundStartTime())
					&& StringUtil.isBlank(ntcConnRecordPercent.getSearchFoundEndTime())) {
				Map<String, String> map = DateUtils.getLocalTime(null, null, Constants.LOG_LOCAL_TIME, "minute");
				ntcConnRecordPercent.setSearchFoundStartTime(map.get("startTime"));
				ntcConnRecordPercent.setSearchFoundEndTime(map.get("endTime"));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				if (!StringUtil.isBlank(ntcConnRecordPercent.getSearchFoundEndTime())) {
					sdf.setLenient(false);
					sdf.parse(ntcConnRecordPercent.getSearchFoundEndTime());
				}

			} catch (ParseException e) {
				throw new RestServiceException("searchFoundEndTime参数格式错误",
						RestBusinessCode.param_formate_error.getValue());
			} catch (Exception e) {
				throw new RestServiceException("searchFoundEndTime参数错误",
						RestBusinessCode.param_formate_error.getValue());
			}

			try {
				if (!StringUtil.isBlank(ntcConnRecordPercent.getSearchFoundStartTime())) {
					sdf.setLenient(false);
					sdf.parse(ntcConnRecordPercent.getSearchFoundStartTime());
				}
			} catch (ParseException e) {
				throw new RestServiceException("searchFoundStartTime参数格式错误",
						RestBusinessCode.param_formate_error.getValue());
			} catch (Exception e) {
				throw new RestServiceException("searchFoundStartTime参数错误",
						RestBusinessCode.param_formate_error.getValue());
			}
			try {
				if (!StringUtil.isBlank(ntcConnRecordPercent.getSearchEntranceId())) {
					Integer.parseInt(ntcConnRecordPercent.getSearchEntranceId());
				}
			} catch (NumberFormatException e) {
				throw new RestServiceException("searchEntranceId参数格式错误",
						RestBusinessCode.param_formate_error.getValue());
			} catch (Exception e) {
				throw new RestServiceException("searchEntranceId参数错误", RestBusinessCode.param_formate_error.getValue());
			}

			logDataService.getNtcConnRecordPercent(ntcConnRecordPercent);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("通联关系日志检索失败:" + e.getMessage());
			logger.error("通联关系日志百分比检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"通联关系日志百分比检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"通联关系日志百分比检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"通联关系日志百分比检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "通联关系日志百分比检索成功",
				ntcConnRecordPercent, 0);
	}

	@RequestMapping(value = "/ntcAsnRecordold", method = RequestMethod.GET)
	@ApiOperation(value = "ASN通联关系(源,目的)查询", httpMethod = "GET", notes = "对日志功能“ASN通联关系(源,目的)查询”提供数据基础查询服务")
	public Map<String, ?> ntcAsnRecord(Page page, TrafficAsnStatistic ntcAsnRecord, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		try {
			if (StringUtil.isEmpty(ntcAsnRecord.getSearchFoundStartTime())
					&& StringUtil.isEmpty(ntcAsnRecord.getSearchFoundEndTime())) {
				Map<String, String> map = DateUtils.getLocalTime(null, null, Constants.LOG_LOCAL_TIME, "minute");
				ntcAsnRecord.setSearchFoundStartTime(map.get("startTime"));
				ntcAsnRecord.setSearchFoundEndTime(map.get("endTime"));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				if (!StringUtil.isBlank(ntcAsnRecord.getSearchFoundEndTime())) {
					sdf.setLenient(false);
					sdf.parse(ntcAsnRecord.getSearchFoundEndTime());
				}

			} catch (ParseException e) {
				throw new RestServiceException("searchFoundEndTime参数格式错误",
						RestBusinessCode.param_formate_error.getValue());
			} catch (Exception e) {
				throw new RestServiceException("searchFoundEndTime参数错误",
						RestBusinessCode.param_formate_error.getValue());
			}

			try {
				if (!StringUtil.isBlank(ntcAsnRecord.getSearchFoundStartTime())) {
					sdf.setLenient(false);
					sdf.parse(ntcAsnRecord.getSearchFoundStartTime());
				}

			} catch (ParseException e) {
				throw new RestServiceException("searchFoundStartTime参数格式错误",
						RestBusinessCode.param_formate_error.getValue());
			} catch (Exception e) {
				throw new RestServiceException("searchFoundStartTime参数错误",
						RestBusinessCode.param_formate_error.getValue());
			}
			try {
				if (!StringUtil.isBlank(ntcAsnRecord.getSearchAsnType())) {
					Integer.parseInt(ntcAsnRecord.getSearchAsnType());
				} else {
					throw new RestServiceException("searchAsnType参数不能为空",
							RestBusinessCode.param_formate_error.getValue());
				}
			} catch (NumberFormatException e) {
				throw new RestServiceException("searchAsnType参数格式错误" + e.getMessage(),
						RestBusinessCode.param_formate_error.getValue());
			} catch (Exception e) {
				throw new RestServiceException("searchAsnType参数错误" + e.getMessage(),
						RestBusinessCode.param_formate_error.getValue());
			}

			logDataService.getNtcAsnRecord(page, ntcAsnRecord);
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("ASN通联关系(源,目的)日志检索失败:" + e.getMessage());
			logger.error("ASN通联关系(源,目的)检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"ASN通联关系(源,目的)检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"ASN通联关系(源,目的)检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"ASN通联关系(源,目的)检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "ASN通联关系(源,目的)检索成功",
				page, 0);
	}
	
	@RequestMapping(value = "/ntcSslRecordLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSL泛收日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-SSL”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-SSL”提供数据基础查询服务。")
	public Map<String, ?> ntcSslRecordLogs(Page page, NtcSslRecordLog ntcSslRecordLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		try {
			resetTime(ntcSslRecordLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcSslRecordLog, NtcSslRecordLog.class, page);
//			logDataService.getData(page, ntcSslRecordLog);
			//将数据源切换到本地clickhouse
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_B);
			ntcSslRecordLogsService.getNtcSslRecordLogsList(page, ntcSslRecordLog);
			CustomerContextHolder.clearCustomerType();
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("SSL泛收日志检索失败:" + e.getMessage());
			logger.error("SSL泛收日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"SSL泛收日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"SSL泛收日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"SSL泛收日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "SSL泛收日志检索成功", page, 0);
	}
	
	@RequestMapping(value = "/ntcHttpRecordLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP泛收日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-HTTP”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-HTTP”提供数据基础查询服务")
	public Map<String, ?> ntcHttpRecordLogs(Page page, NtcHttpRecordLog ntcHttpRecordLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		try {
			resetTime(ntcHttpRecordLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcHttpRecordLog, NtcHttpRecordLog.class, page);
//			logDataService.getData(page, ntcHttpRecordLog);
			//将数据源切换到本地clickhouse
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_B);
			ntcHttpRecordLogsService.getNtcHttpRecordLogsList(page, ntcHttpRecordLog);
			CustomerContextHolder.clearCustomerType();
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("Http泛收日志检索失败:" + e.getMessage());
			logger.error("Http泛收日志检索失败：" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"Http泛收日志检索失败：" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"Http泛收日志检索失败：" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"Http泛收日志检索失败：" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Http泛收日志检索成功", page, 0);
	}
}
