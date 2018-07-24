package com.nis.web.controller.restful;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.LogEntity;
import com.nis.domain.Page;
import com.nis.domain.restful.DkBehaviorLog;
import com.nis.domain.restful.MmAvIpLog;
import com.nis.domain.restful.MmAvUrlLog;
import com.nis.domain.restful.MmPicIpLog;
import com.nis.domain.restful.MmPicUrlLog;
import com.nis.domain.restful.MmPornAudioLevelLog;
import com.nis.domain.restful.MmPornVideoLevelLog;
import com.nis.domain.restful.MmSampleAudioLog;
import com.nis.domain.restful.MmSamplePicLog;
import com.nis.domain.restful.MmSampleVideoLog;
import com.nis.domain.restful.MmSampleVoipLog;
import com.nis.domain.restful.MmVoipAccountLog;
import com.nis.domain.restful.MmVoipIpLog;
import com.nis.domain.restful.NtcAppLog;
import com.nis.domain.restful.NtcDdosLog;
import com.nis.domain.restful.NtcDnsLog;
import com.nis.domain.restful.NtcFtpLog;
import com.nis.domain.restful.NtcHttpLog;
import com.nis.domain.restful.NtcIpLog;
import com.nis.domain.restful.NtcIpsecLog;
import com.nis.domain.restful.NtcL2tpLog;
import com.nis.domain.restful.NtcMailLog;
import com.nis.domain.restful.NtcOpenvpnLog;
import com.nis.domain.restful.NtcPptpLog;
import com.nis.domain.restful.NtcSshLog;
import com.nis.domain.restful.NtcSslLog;
import com.nis.domain.restful.PxyHttpLog;
import com.nis.restful.RestServiceException;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.HiveJDBC;
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

	@RequestMapping(value = "/ntcIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "IP地址日志查询", httpMethod = "GET", notes = "对应配置为IP地址管理，存储动作为阻断与监测的命中日志。对日志功能IP地址提供数据基础查询服务")
	public Map<String, ?> ntcIpLogs(Page page, NtcIpLog ntcIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);
		Page<NtcIpLog> ntcIpLogPage = null;
		try {
			resetTime(ntcIpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcIpLog, NtcIpLog.class, page);
			ntcIpLogPage = new Page<NtcIpLog>();
			ntcIpLogPage.setPageNo(page.getPageNo());
			ntcIpLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcIpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcIpLog,
					Configurations.getStringProperty(NtcIpLog.class.getSimpleName() + "HiveTable", "ntc_ip_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcIpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcIpLogPage.setList(new ArrayList());
			} else {
				List list = new ArrayList();
				list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcIpLog> List = (java.util.List<NtcIpLog>) JsonMapper.fromJsonList(jsonString,
							NtcIpLog.class);
					ntcIpLogPage.setList(List);
					ntcIpLogPage.setCount(List.size());
//					ntcIpLogPage
//							.setCount(
//									HiveSqlService.getHivePageCount(ntcIpLog, null,
//											Configurations.getStringProperty(
//													NtcIpLog.class.getSimpleName() + "HiveTable", "ntc_ip_log"),
//											getCol2Col(), null));
				} else {
					ntcIpLogPage.setList(new ArrayList());
					ntcIpLogPage.setCount(0l);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "IP地址日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "IP地址日志检索成功",
				ntcIpLogPage, 0);
	}

	@RequestMapping(value = "/ntcHttpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-HTTP”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-HTTP”提供数据基础查询服务")
	public Map<String, ?> ntcHttpLogs(Page page, NtcHttpLog ntcHttpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcHttpLog> ntcHttpLogPage = null;
		try {
			resetTime(ntcHttpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcHttpLog, NtcHttpLog.class, page);
			ntcHttpLogPage = new Page<NtcHttpLog>();
			ntcHttpLogPage.setPageNo(page.getPageNo());
			ntcHttpLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcHttpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcHttpLog,
					Configurations.getStringProperty(NtcHttpLog.class.getSimpleName() + "HiveTable", "ntc_http_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcHttpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcHttpLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcHttpLog> List = (java.util.List<NtcHttpLog>) JsonMapper.fromJsonList(jsonString,
							NtcHttpLog.class);
					ntcHttpLogPage.setList(List);
					ntcHttpLogPage.setCount(List.size());

				} else {
					ntcHttpLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "Http日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Http日志检索成功",
				ntcHttpLogPage, 0);
	}

	@RequestMapping(value = "/ntcDnsLogs", method = RequestMethod.GET)
	@ApiOperation(value = "DNS日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-DNS”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-DNS”提供数据基础查询服务。")
	public Map<String, ?> ntcDnsLogs(Page page, NtcDnsLog ntcDnsLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcDnsLog> ntcDnsLogPage = null;
		try {
			resetTime(ntcDnsLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcDnsLog, NtcDnsLog.class, page);
			ntcDnsLogPage = new Page<NtcDnsLog>();
			ntcDnsLogPage.setPageNo(page.getPageNo());
			ntcDnsLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcDnsLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcDnsLog,
					Configurations.getStringProperty(NtcDnsLog.class.getSimpleName() + "HiveTable", "ntc_dns_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcDnsLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcDnsLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcDnsLog> List = (java.util.List<NtcDnsLog>) JsonMapper.fromJsonList(jsonString,
							NtcDnsLog.class);
					ntcDnsLogPage.setList(List);
					ntcDnsLogPage.setCount(List.size());

				} else {
					ntcDnsLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "Dns日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Dns日志检索成功",
				ntcDnsLogPage, 0);
	}

	@RequestMapping(value = "/ntcMailLogs", method = RequestMethod.GET)
	@ApiOperation(value = "EMAIL日志查询", httpMethod = "GET", notes = "对应配置为“邮件管理”，存储动作为阻断与监测的命中日志。对日志功能“邮件管理”提供数据基础查询服务。")
	public Map<String, ?> ntcMailLogs(Page page, NtcMailLog ntcMailLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcMailLog> ntcMailLogPage = null;
		try {
			resetTime(ntcMailLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcMailLog, NtcMailLog.class, page);
			ntcMailLogPage = new Page<NtcMailLog>();
			ntcMailLogPage.setPageNo(page.getPageNo());
			ntcMailLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcMailLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcMailLog,
					Configurations.getStringProperty(NtcMailLog.class.getSimpleName() + "HiveTable", "ntc_mail_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcMailLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcMailLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcMailLog> List = (java.util.List<NtcMailLog>) JsonMapper.fromJsonList(jsonString,
							NtcMailLog.class);
					ntcMailLogPage.setList(List);
					ntcMailLogPage.setCount(List.size());

				} else {
					ntcMailLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "Mail日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Mail日志检索成功",
				ntcMailLogPage, 0);
	}

	@RequestMapping(value = "/ntcSslLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSL日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-SSL”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-SSL”提供数据基础查询服务。")
	public Map<String, ?> ntcSslLogs(Page page, NtcSslLog ntcSslLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcSslLog> ntcSslLogPage = null;
		try {
			resetTime(ntcSslLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcSslLog, NtcSslLog.class, page);
			ntcSslLogPage = new Page<NtcSslLog>();
			ntcSslLogPage.setPageNo(page.getPageNo());
			ntcSslLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcSslLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcSslLog,
					Configurations.getStringProperty(NtcSslLog.class.getSimpleName() + "HiveTable", "ntc_ssl_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcSslLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcSslLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcSslLog> List = (java.util.List<NtcSslLog>) JsonMapper.fromJsonList(jsonString,
							NtcSslLog.class);
					ntcSslLogPage.setList(List);
					ntcSslLogPage.setCount(List.size());

				} else {
					ntcSslLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "SSL日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "SSL日志检索成功",
				ntcSslLogPage, 0);
	}

	@RequestMapping(value = "/ntcPptpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "PPTP日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-PPTP”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理-PPTP”提供数据基础查询服务。")
	public Map<String, ?> ntcPptpLogs(Page page, NtcPptpLog ntcPptpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcPptpLog> ntcPptpLogPage = null;
		try {
			resetTime(ntcPptpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcPptpLog, NtcPptpLog.class, page);
			ntcPptpLogPage = new Page<NtcPptpLog>();
			ntcPptpLogPage.setPageNo(page.getPageNo());
			ntcPptpLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcPptpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcPptpLog,
					Configurations.getStringProperty(NtcPptpLog.class.getSimpleName() + "HiveTable", "ntc_pptp_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcPptpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcPptpLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcPptpLog> List = (java.util.List<NtcPptpLog>) JsonMapper.fromJsonList(jsonString,
							NtcPptpLog.class);
					ntcPptpLogPage.setList(List);
					ntcPptpLogPage.setCount(List.size());
				} else {
					ntcPptpLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "PPTP日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "PPTP日志检索成功",
				ntcPptpLogPage, 0);
	}

	@RequestMapping(value = "/ntcL2tpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "L2TP日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-L2TP”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- L2TP”提供数据基础查询服务。")
	public Map<String, ?> ntcL2tpLogs(Page page, NtcL2tpLog ntcL2tpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcL2tpLog> ntcL2tpLogPage = null;
		try {
			resetTime(ntcL2tpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcL2tpLog, NtcL2tpLog.class, page);
			ntcL2tpLogPage = new Page<NtcL2tpLog>();
			ntcL2tpLogPage.setPageNo(page.getPageNo());
			ntcL2tpLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcL2tpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcL2tpLog,
					Configurations.getStringProperty(NtcL2tpLog.class.getSimpleName() + "HiveTable", "ntc_l2tp_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcL2tpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcL2tpLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcL2tpLog> List = (java.util.List<NtcL2tpLog>) JsonMapper.fromJsonList(jsonString,
							NtcL2tpLog.class);
					ntcL2tpLogPage.setList(List);
					ntcL2tpLogPage.setCount(List.size());

				} else {
					ntcL2tpLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "L2TP日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "L2TP日志检索成功",
				ntcL2tpLogPage, 0);
	}

	@RequestMapping(value = "/ntcOpenvpnLogs", method = RequestMethod.GET)
	@ApiOperation(value = "OPENVPN日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-OPENVPN”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- OPENVPN”提供数据基础查询服务。")
	public Map<String, ?> ntcOpenvpnLogs(Page page, NtcOpenvpnLog ntcOpenvpnLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcOpenvpnLog> ntcOpenvpnLogPage = null;
		try {
			resetTime(ntcOpenvpnLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcOpenvpnLog, NtcOpenvpnLog.class, page);
			ntcOpenvpnLogPage = new Page<NtcOpenvpnLog>();
			ntcOpenvpnLogPage.setPageNo(page.getPageNo());
			ntcOpenvpnLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcOpenvpnLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcOpenvpnLog,
					Configurations.getStringProperty(NtcOpenvpnLog.class.getSimpleName() + "HiveTable", "ntc_openvpn_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcOpenvpnLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcOpenvpnLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcOpenvpnLog> List = (java.util.List<NtcOpenvpnLog>) JsonMapper.fromJsonList(jsonString,
							NtcOpenvpnLog.class);
					ntcOpenvpnLogPage.setList(List);
					ntcOpenvpnLogPage.setCount(List.size());

				} else {
					ntcOpenvpnLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "Openvpn日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Openvpn日志检索成功",
				ntcOpenvpnLogPage, 0);
	}

	@RequestMapping(value = "/ntcIpsecLogs", method = RequestMethod.GET)
	@ApiOperation(value = "IPSEC日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-IPSEC”，存储动作为监测的命中日志。对日志功能“隧道管理- IPSEC”提供数据基础查询服务。")
	public Map<String, ?> ntcIpsecLogs(Page page, NtcIpsecLog ntcIpsecLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcIpsecLog> ntcIpsecLogPage = null;
		try {
			resetTime(ntcIpsecLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcIpsecLog, NtcIpsecLog.class, page);
			ntcIpsecLogPage = new Page<NtcIpsecLog>();
			ntcIpsecLogPage.setPageNo(page.getPageNo());
			ntcIpsecLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcIpsecLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcIpsecLog,
					Configurations.getStringProperty(NtcIpsecLog.class.getSimpleName() + "HiveTable", "ntc_ipsec_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcIpsecLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcIpsecLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcIpsecLog> List = (java.util.List<NtcIpsecLog>) JsonMapper.fromJsonList(jsonString,
							NtcIpsecLog.class);
					ntcIpsecLogPage.setList(List);
					ntcIpsecLogPage.setCount(List.size());

				} else {
					ntcIpsecLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "IPSEC日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "IPSEC日志检索成功",
				ntcIpsecLogPage, 0);
	}

	@RequestMapping(value = "/ntcSshLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSH日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-SSH”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- SSH”提供数据基础查询服务。")
	public Map<String, ?> ntcSshLogs(Page page, NtcSshLog ntcSshLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcSshLog> ntcSshLogPage = null;
		try {
			resetTime(ntcSshLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcSshLog, NtcSshLog.class, page);
			ntcSshLogPage = new Page<NtcSshLog>();
			ntcSshLogPage.setPageNo(page.getPageNo());
			ntcSshLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcSshLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcSshLog,
					Configurations.getStringProperty(NtcSshLog.class.getSimpleName() + "HiveTable", "ntc_ssh_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcSshLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcSshLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcSshLog> List = (java.util.List<NtcSshLog>) JsonMapper.fromJsonList(jsonString,
							NtcSshLog.class);
					ntcSshLogPage.setList(List);
					ntcSshLogPage.setCount(List.size());

				} else {
					ntcSshLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "SSH日志检索失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "SSH日志检索成功",
				ntcSshLogPage, 0);
	}

	@RequestMapping(value = "/ntcFtpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "FTP日志查询", httpMethod = "GET", notes = "对应配置为“文件传输-FTP”，存储动作为阻断与监测的命中日志。对日志功能“文件传输-FTP”提供数据基础查询服务。")
	public Map<String, ?> ntcFtpLogs(Page page, NtcFtpLog ntcFtpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcFtpLog> ntcFtpLogPage = null;
		try {
			resetTime(ntcFtpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcFtpLog, NtcFtpLog.class, page);
			ntcFtpLogPage = new Page<NtcFtpLog>();
			ntcFtpLogPage.setPageNo(page.getPageNo());
			ntcFtpLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcFtpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcFtpLog,
					Configurations.getStringProperty(NtcFtpLog.class.getSimpleName() + "HiveTable", "ntc_ftp_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcFtpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcFtpLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcFtpLog> List = (java.util.List<NtcFtpLog>) JsonMapper.fromJsonList(jsonString,
							NtcFtpLog.class);
					ntcFtpLogPage.setList(List);
					ntcFtpLogPage.setCount(List.size());

				} else {
					ntcFtpLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "FTP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "FTP日志检索成功",
				ntcFtpLogPage, 0);
	}
	@RequestMapping(value = "/ntcAppLogs", method = RequestMethod.GET)
	@ApiOperation(value = "App日志查询", httpMethod = "GET", notes = "对应配置为“App管理”，存储动作为阻断与监测的命中日志。对日志功能“APP策略日志”提供数据基础查询服务")
	public Map<String, ?> ntcAppLogs(Page page, NtcAppLog ntcAppLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcAppLog> ntcAppLogPage = null;
		try {
			resetTime(ntcAppLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcAppLog, NtcAppLog.class, page);
			ntcAppLogPage = new Page<NtcAppLog>();
			ntcAppLogPage.setPageNo(page.getPageNo());
			ntcAppLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcAppLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcAppLog,
					Configurations.getStringProperty(NtcAppLog.class.getSimpleName() + "HiveTable", "ntc_app_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcAppLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcAppLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcAppLog> List = (java.util.List<NtcAppLog>) JsonMapper.fromJsonList(jsonString,
							NtcAppLog.class);
					ntcAppLogPage.setList(List);
					ntcAppLogPage.setCount(List.size());

				} else {
					ntcAppLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "App日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "App日志检索成功",
				ntcAppLogPage, 0);
	}
	@RequestMapping(value = "/ntcDdosLogs", method = RequestMethod.GET)
	@ApiOperation(value = "DDos日志查询", httpMethod = "GET", notes = "对应配置为“DDOS日志监控”，存储动作为丢弃的命中日志。对日志功能“DDOS日志监控”提供数据基础查询服务")
	public Map<String, ?> ntcDdosLogs(Page page, NtcDdosLog ntcDdosLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcDdosLog> ntcDdosLogPage = null;
		try {
			resetTime(ntcDdosLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcDdosLog, NtcDdosLog.class, page);
			ntcDdosLogPage = new Page<NtcDdosLog>();
			ntcDdosLogPage.setPageNo(page.getPageNo());
			ntcDdosLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcDdosLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, ntcDdosLog,
					Configurations.getStringProperty(NtcDdosLog.class.getSimpleName() + "HiveTable", "ntc_ddos_log"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcDdosLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcDdosLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcDdosLog> List = (java.util.List<NtcDdosLog>) JsonMapper.fromJsonList(jsonString,
							NtcDdosLog.class);
					ntcDdosLogPage.setList(List);
					ntcDdosLogPage.setCount(List.size());

				} else {
					ntcDdosLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "DDos日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "DDos日志检索成功",
				ntcDdosLogPage, 0);
	}
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
			ResultSet rs = HiveSqlService.getResultSet(page, dkBehaviorLog,
					Configurations.getStringProperty(DkBehaviorLog.class.getSimpleName() + "HiveTable", "DK_BEHAVIOR_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, DkBehaviorLog.class, "foundTime",
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
	@RequestMapping(value = "/mmAvIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "音视频IP日志查询", httpMethod = "GET", notes = "对日志功能“音视频IP日志”提供数据基础查询服务")
	public Map<String, ?> mmAvIpLogs(Page page, MmAvIpLog mmAvIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmAvIpLog> logPage = null;
		try {
			resetTime(mmAvIpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmAvIpLog, MmAvIpLog.class, page);
			logPage = new Page<MmAvIpLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmAvIpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmAvIpLog,
					Configurations.getStringProperty(MmAvIpLog.class.getSimpleName() + "HiveTable", "MM_AV_IP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmAvIpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmAvIpLog> List = (java.util.List<MmAvIpLog>) JsonMapper.fromJsonList(jsonString,
							MmAvIpLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "音视频IP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "音视频IP日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/mmAvUrlLogs", method = RequestMethod.GET)
	@ApiOperation(value = "音视频URL日志查询", httpMethod = "GET", notes = "对日志功能“音视频URL日志”提供数据基础查询服务")
	public Map<String, ?> mmAvUrlLogs(Page page, MmAvUrlLog mmAvUrlLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmAvUrlLog> logPage = null;
		try {
			resetTime(mmAvUrlLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmAvUrlLog, MmAvUrlLog.class, page);
			logPage = new Page<MmAvUrlLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmAvUrlLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmAvUrlLog,
					Configurations.getStringProperty(MmAvUrlLog.class.getSimpleName() + "HiveTable", "MM_AV_URL_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmAvUrlLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmAvUrlLog> List = (java.util.List<MmAvUrlLog>) JsonMapper.fromJsonList(jsonString,
							MmAvUrlLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "音视频URL日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "音视频URL日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/mmPicIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "图片IP日志查询", httpMethod = "GET", notes = "对日志功能“图片IP日志”提供数据基础查询服务")
	public Map<String, ?> mmPicIpLogs(Page page, MmPicIpLog mmPicIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmPicIpLog> logPage = null;
		try {
			resetTime(mmPicIpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmPicIpLog, MmPicIpLog.class, page);
			logPage = new Page<MmPicIpLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmPicIpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmPicIpLog,
					Configurations.getStringProperty(MmPicIpLog.class.getSimpleName() + "HiveTable", "MM_PIC_IP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmPicIpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmPicIpLog> List = (java.util.List<MmPicIpLog>) JsonMapper.fromJsonList(jsonString,
							MmPicIpLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "图片IP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "图片IP日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/mmPicUrlLogs", method = RequestMethod.GET)
	@ApiOperation(value = "图片URL日志查询", httpMethod = "GET", notes = "对日志功能“图片URL日志”提供数据基础查询服务")
	public Map<String, ?> mmPicUrlLogs(Page page, MmPicUrlLog mmPicUrlLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmPicUrlLog> logPage = null;
		try {
			resetTime(mmPicUrlLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmPicUrlLog, MmPicUrlLog.class, page);
			logPage = new Page<MmPicUrlLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmPicUrlLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmPicUrlLog,
					Configurations.getStringProperty(MmPicUrlLog.class.getSimpleName() + "HiveTable", "MM_PIC_URL_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmPicUrlLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmPicUrlLog> List = (java.util.List<MmPicUrlLog>) JsonMapper.fromJsonList(jsonString,
							MmPicUrlLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "图片URL日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "图片URL日志检索成功",
				logPage, 0);
	}
	
	@RequestMapping(value = "/mmVoipIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "VOIP IP日志查询", httpMethod = "GET", notes = "对日志功能“VOIP IP日志”提供数据基础查询服务")
	public Map<String, ?> mmVoipIpLogs(Page page, MmVoipIpLog mmVoipIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmVoipIpLog> logPage = null;
		try {
			resetTime(mmVoipIpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmVoipIpLog, MmVoipIpLog.class, page);
			logPage = new Page<MmVoipIpLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmVoipIpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmVoipIpLog,
					Configurations.getStringProperty(MmVoipIpLog.class.getSimpleName() + "HiveTable", "MM_VOIP_IP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmVoipIpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmVoipIpLog> List = (java.util.List<MmVoipIpLog>) JsonMapper.fromJsonList(jsonString,
							MmVoipIpLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "VOIP IP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "VOIP IP日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/mmVoipAccountLogs", method = RequestMethod.GET)
	@ApiOperation(value = "VOIP Account日志查询", httpMethod = "GET", notes = "对日志功能“VOIP Account日志”提供数据基础查询服务")
	public Map<String, ?> mmVoipAccountLogs(Page page, MmVoipAccountLog mmVoipLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmVoipAccountLog> logPage = null;
		try {
			resetTime(mmVoipLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmVoipLog, MmVoipAccountLog.class, page);
			logPage = new Page<MmVoipAccountLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmVoipAccountLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmVoipLog,
					Configurations.getStringProperty(MmVoipAccountLog.class.getSimpleName() + "HiveTable", "MM_VOIP_ACCOUNT_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmVoipAccountLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmVoipAccountLog> List = (java.util.List<MmVoipAccountLog>) JsonMapper.fromJsonList(jsonString,
							MmVoipAccountLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "VOIP Account日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "VOIP Account日志检索成功",
				logPage, 0);
	}
	
	@RequestMapping(value = "/mmSampleAudioLogs", method = RequestMethod.GET)
	@ApiOperation(value = "音频样例日志查询", httpMethod = "GET", notes = "对日志功能“音频样例日志”提供数据基础查询服务")
	public Map<String, ?> mmSampleAudioLogs(Page page, MmSampleAudioLog mmSampleAudioLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmSampleAudioLog> logPage = null;
		try {
			resetTime(mmSampleAudioLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSampleAudioLog, MmSampleAudioLog.class, page);
			logPage = new Page<MmSampleAudioLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmSampleAudioLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmSampleAudioLog,
					Configurations.getStringProperty(MmSampleAudioLog.class.getSimpleName() + "HiveTable", "MM_SAMPLE_AUDIO_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmSampleAudioLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmSampleAudioLog> List = (java.util.List<MmSampleAudioLog>) JsonMapper.fromJsonList(jsonString,
							MmSampleAudioLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "音频样例日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "音频样例日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/mmSampleVideoLogs", method = RequestMethod.GET)
	@ApiOperation(value = "视频样例日志查询", httpMethod = "GET", notes = "对日志功能“视频样例日志”提供数据基础查询服务")
	public Map<String, ?> mmSampleVideoLogs(Page page, MmSampleVideoLog mmSampleVideoLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmSampleVideoLog> logPage = null;
		try {
			resetTime(mmSampleVideoLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSampleVideoLog, MmSampleVideoLog.class, page);
			logPage = new Page<MmSampleVideoLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmSampleVideoLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmSampleVideoLog,
					Configurations.getStringProperty(MmSampleVideoLog.class.getSimpleName() + "HiveTable", "MM_SAMPLE_VIDEO_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmSampleVideoLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmSampleVideoLog> List = (java.util.List<MmSampleVideoLog>) JsonMapper.fromJsonList(jsonString,
							MmSampleVideoLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "视频样例日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "视频样例日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/mmPornAudioLevelLogs", method = RequestMethod.GET)
	@ApiOperation(value = "音频色情日志查询", httpMethod = "GET", notes = "对日志功能“音频色情日志”提供数据基础查询服务")
	public Map<String, ?> mmPornAudioLevelLogs(Page page, MmPornAudioLevelLog mmPornAudioLevelLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmPornAudioLevelLog> logPage = null;
		try {
			resetTime(mmPornAudioLevelLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmPornAudioLevelLog, MmPornAudioLevelLog.class, page);
			logPage = new Page<MmPornAudioLevelLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmPornAudioLevelLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmPornAudioLevelLog,
					Configurations.getStringProperty(MmPornAudioLevelLog.class.getSimpleName() + "HiveTable", "MM_PORN_AUDIO_LEVEL_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmPornAudioLevelLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmPornAudioLevelLog> List = (java.util.List<MmPornAudioLevelLog>) JsonMapper.fromJsonList(jsonString,
							MmPornAudioLevelLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "音频色情日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "音频色情日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/mmPornVideoLevelLogs", method = RequestMethod.GET)
	@ApiOperation(value = "视频色情日志查询", httpMethod = "GET", notes = "对日志功能“视频色情日志”提供数据基础查询服务")
	public Map<String, ?> mmPornVideoLevelLogs(Page page, MmPornVideoLevelLog mmPornVideoLevelLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmPornVideoLevelLog> logPage = null;
		try {
			resetTime(mmPornVideoLevelLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmPornVideoLevelLog, MmPornVideoLevelLog.class, page);
			logPage = new Page<MmPornVideoLevelLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmPornVideoLevelLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmPornVideoLevelLog,
					Configurations.getStringProperty(MmPornVideoLevelLog.class.getSimpleName() + "HiveTable", "MM_PRON_VIDEO_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmPornVideoLevelLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmPornVideoLevelLog> List = (java.util.List<MmPornVideoLevelLog>) JsonMapper.fromJsonList(jsonString,
							MmPornVideoLevelLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "视频色情日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "视频色情日志检索成功",
				logPage, 0);
	}
	
	@RequestMapping(value = "/mmSamplePicLogs", method = RequestMethod.GET)
	@ApiOperation(value = "图片样例日志查询", httpMethod = "GET", notes = "对日志功能“图片样例日志”提供数据基础查询服务")
	public Map<String, ?> mmSamplePicLogs(Page page, MmSamplePicLog mmSamplePicLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmSamplePicLog> logPage = null;
		try {
			resetTime(mmSamplePicLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSamplePicLog, MmSamplePicLog.class, page);
			logPage = new Page<MmSamplePicLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmSamplePicLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmSamplePicLog,
					Configurations.getStringProperty(MmSamplePicLog.class.getSimpleName() + "HiveTable", "MM_SAMPLE_PIC_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmSamplePicLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmSamplePicLog> List = (java.util.List<MmSamplePicLog>) JsonMapper.fromJsonList(jsonString,
							MmSamplePicLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "图片样例日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "图片样例日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/mmSampleVoipLogs", method = RequestMethod.GET)
	@ApiOperation(value = "VOIP样例日志查询", httpMethod = "GET", notes = "对日志功能“VOIP样例日志”提供数据基础查询服务")
	public Map<String, ?> mmSampleVoipLogs(Page page, MmSampleVoipLog mmSampleVoipLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmSampleVoipLog> logPage = null;
		try {
			resetTime(mmSampleVoipLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSampleVoipLog, MmSampleVoipLog.class, page);
			logPage = new Page<MmSampleVoipLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmSampleVoipLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmSampleVoipLog,
					Configurations.getStringProperty(MmSampleVoipLog.class.getSimpleName() + "HiveTable", "MM_SAMPLE_VOIP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmSampleVoipLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmSampleVoipLog> List = (java.util.List<MmSampleVoipLog>) JsonMapper.fromJsonList(jsonString,
							MmSampleVoipLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "VOIP样例日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "VOIP样例日志检索成功",
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
			ResultSet rs = HiveSqlService.getResultSet(page, pxyHttpLog,
					Configurations.getStringProperty(PxyHttpLog.class.getSimpleName() + "HiveTable", "PXY_HTTP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, PxyHttpLog.class, "foundTime",
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> map = DateUtils.getLocalTime(entity.getSearchFoundStartTime(),
				entity.getSearchFoundEndTime(), Constants.LOG_LOCAL_TIME, "log");
		entity.setSearchFoundStartTime(map.get("startTime"));
		entity.setSearchFoundEndTime(map.get("endTime"));
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
