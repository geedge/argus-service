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
import com.nis.domain.restful.MmVoipLog;
import com.nis.domain.restful.NtcAppLog;
import com.nis.domain.restful.NtcAttrTypeReport;
import com.nis.domain.restful.NtcDdosLog;
import com.nis.domain.restful.NtcDestipCountryReport;
import com.nis.domain.restful.NtcDnsLog;
import com.nis.domain.restful.NtcEntranceReport;
import com.nis.domain.restful.NtcFtpLog;
import com.nis.domain.restful.NtcHttpLog;
import com.nis.domain.restful.NtcIpLog;
import com.nis.domain.restful.NtcIpsecLog;
import com.nis.domain.restful.NtcL2tpLog;
import com.nis.domain.restful.NtcLwhhReport;
import com.nis.domain.restful.NtcMailLog;
import com.nis.domain.restful.NtcOpenvpnLog;
import com.nis.domain.restful.NtcPptpLog;
import com.nis.domain.restful.NtcPzReport;
import com.nis.domain.restful.NtcReportEntity;
import com.nis.domain.restful.NtcServiceReport;
import com.nis.domain.restful.NtcSrcipDomesticReport;
import com.nis.domain.restful.NtcSshLog;
import com.nis.domain.restful.NtcSslLog;
import com.nis.domain.restful.NtcTagReport;
import com.nis.restful.RestServiceException;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.HiveJDBC;
import com.nis.util.JsonMapper;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.HiveSqlService;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.LogTestService;
import com.nis.web.service.restful.NtcReportService;
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
	public LogTestService testService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	@Autowired
	protected NtcReportService ntcReportService;

	@RequestMapping(value = "/ntcIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "IP地址日志查询", httpMethod = "GET", notes = "对应配置为IP地址管理，存储动作为阻断与监测的命中日志。对日志功能IP地址提供数据基础查询服务")
	public Map<String, ?> ntcIpLogs(Page page, NtcIpLog ntcIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);
		Page<NtcIpLog> ntcIpLogPage = null;
		try {
			resetTime(ntcIpLog);
			testService.queryConditionCheck(auditLogThread, start, ntcIpLog, NtcIpLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcHttpLog> ntcHttpLogPage = null;
		try {
			resetTime(ntcHttpLog);
			testService.queryConditionCheck(auditLogThread, start, ntcHttpLog, NtcHttpLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcDnsLog> ntcDnsLogPage = null;
		try {
			resetTime(ntcDnsLog);
			testService.queryConditionCheck(auditLogThread, start, ntcDnsLog, NtcDnsLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcMailLog> ntcMailLogPage = null;
		try {
			resetTime(ntcMailLog);
			testService.queryConditionCheck(auditLogThread, start, ntcMailLog, NtcMailLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcSslLog> ntcSslLogPage = null;
		try {
			resetTime(ntcSslLog);
			testService.queryConditionCheck(auditLogThread, start, ntcSslLog, NtcSslLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcPptpLog> ntcPptpLogPage = null;
		try {
			resetTime(ntcPptpLog);
			testService.queryConditionCheck(auditLogThread, start, ntcPptpLog, NtcPptpLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcL2tpLog> ntcL2tpLogPage = null;
		try {
			resetTime(ntcL2tpLog);
			testService.queryConditionCheck(auditLogThread, start, ntcL2tpLog, NtcL2tpLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcOpenvpnLog> ntcOpenvpnLogPage = null;
		try {
			resetTime(ntcOpenvpnLog);
			testService.queryConditionCheck(auditLogThread, start, ntcOpenvpnLog, NtcOpenvpnLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcIpsecLog> ntcIpsecLogPage = null;
		try {
			resetTime(ntcIpsecLog);
			testService.queryConditionCheck(auditLogThread, start, ntcIpsecLog, NtcIpsecLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcSshLog> ntcSshLogPage = null;
		try {
			resetTime(ntcSshLog);
			testService.queryConditionCheck(auditLogThread, start, ntcSshLog, NtcSshLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcFtpLog> ntcFtpLogPage = null;
		try {
			resetTime(ntcFtpLog);
			testService.queryConditionCheck(auditLogThread, start, ntcFtpLog, NtcFtpLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcAppLog> ntcAppLogPage = null;
		try {
			resetTime(ntcAppLog);
			testService.queryConditionCheck(auditLogThread, start, ntcAppLog, NtcAppLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcDdosLog> ntcDdosLogPage = null;
		try {
			resetTime(ntcDdosLog);
			testService.queryConditionCheck(auditLogThread, start, ntcDdosLog, NtcDdosLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<DkBehaviorLog> logPage = null;
		try {
			resetTime(dkBehaviorLog);
			testService.queryConditionCheck(auditLogThread, start, dkBehaviorLog, DkBehaviorLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmAvIpLog> logPage = null;
		try {
			resetTime(mmAvIpLog);
			testService.queryConditionCheck(auditLogThread, start, mmAvIpLog, MmAvIpLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmAvUrlLog> logPage = null;
		try {
			resetTime(mmAvUrlLog);
			testService.queryConditionCheck(auditLogThread, start, mmAvUrlLog, MmAvUrlLog.class, page);
			logPage = new Page<MmAvUrlLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmAvIpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmAvUrlLog,
					Configurations.getStringProperty(MmAvUrlLog.class.getSimpleName() + "HiveTable", "MM_AV_URL_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmAvIpLog.class, "foundTime",
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmPicIpLog> logPage = null;
		try {
			resetTime(mmPicIpLog);
			testService.queryConditionCheck(auditLogThread, start, mmPicIpLog, MmPicIpLog.class, page);
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
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmPicUrlLog> logPage = null;
		try {
			resetTime(mmPicUrlLog);
			testService.queryConditionCheck(auditLogThread, start, mmPicUrlLog, MmPicUrlLog.class, page);
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
	
	@RequestMapping(value = "/mmVoipLogs", method = RequestMethod.GET)
	@ApiOperation(value = "VOIP日志查询", httpMethod = "GET", notes = "对日志功能“VOIP日志”提供数据基础查询服务")
	public Map<String, ?> mmVoipLogs(Page page, MmVoipLog mmVoipLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<MmVoipLog> logPage = null;
		try {
			resetTime(mmVoipLog);
			testService.queryConditionCheck(auditLogThread, start, mmVoipLog, MmAvIpLog.class, page);
			logPage = new Page<MmVoipLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(MmVoipLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			ResultSet rs = HiveSqlService.getResultSet(page, mmVoipLog,
					Configurations.getStringProperty(MmVoipLog.class.getSimpleName() + "HiveTable", "MM_VOIP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, MmVoipLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<MmVoipLog> List = (java.util.List<MmVoipLog>) JsonMapper.fromJsonList(jsonString,
							MmVoipLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "VOIP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "VOIP日志检索成功",
				logPage, 0);
	}
	@RequestMapping(value = "/ntcPzReport", method = RequestMethod.GET)
	@ApiOperation(value = "配置日志总量统计", httpMethod = "GET", notes = "配置命中日志数量实时统计报表，对外提供多种数据表现形式，具体可应用于界面配置命中总量业务、配置报表业务等")
	public Map<String, ?> ntcPzReport(Page page, NtcPzReport ntcPzReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<?> ntcPzReportPage = null;
		try {
			resetReportTime(ntcPzReport);
			//验证实时报表
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcPzReport, NtcPzReport.class, page);
			//验证serachCfgId
			ntcReportService.checkNumericCondition(saveLogThread,start,ntcPzReport.getSearchCfgId(),"searchCfgId");
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcPzReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			ntcPzReportPage = ntcReportService.findNtcPzReport(new Page<NtcPzReport>(request, response, NtcPzReport.class),
					ntcPzReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "配置日志总量统计失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "配置日志总量统计成功",
				ntcPzReportPage, 0);
	}

	@RequestMapping(value = "/ntcServiceReport", method = RequestMethod.GET)
	@ApiOperation(value = "业务类型统计查询服务", httpMethod = "GET", notes = "业务类型统计查询服务，基于业务类型（service）维度聚合")
	public Map<String, ?> ntcServiceReport(Page page, NtcServiceReport ntcServiceReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcServiceReport);
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcServiceReport, NtcServiceReport.class, page);
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcServiceReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcServiceReport(new Page<NtcServiceReport>(request, response, NtcServiceReport.class),
					ntcServiceReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "业务类型统计查询失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "业务类型统计查询成功",
				reportPage, 0);
	}
	
	
	@RequestMapping(value = "/ntcTagReport", method = RequestMethod.GET)
	@ApiOperation(value = "标签统计查询服务", httpMethod = "GET", notes = "标签统计查询服务，基于标签与业务类型（service）维度聚合")
	public Map<String, ?> ntcTagReport(Page page, NtcTagReport ntcTagReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcTagReport);
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcTagReport, NtcTagReport.class, page);
			//验证serachTag
			ntcReportService.checkNumericCondition(saveLogThread,start,ntcTagReport.getSearchTag(),"searchTag");
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcTagReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcTagReport(new Page<NtcTagReport>(request, response, NtcTagReport.class),
					ntcTagReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "标签统计查询失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "标签统计查询成功",
				reportPage, 0);
	}
	@RequestMapping(value = "/ntcAttrTypeReport", method = RequestMethod.GET)
	@ApiOperation(value = "性质统计查询服务", httpMethod = "GET", notes = "性质统计查询服务，基于性质与业务类型（service）维度聚合")
	public Map<String, ?> ntcAttrTypeReport(Page page, NtcAttrTypeReport ntcAttrTypeReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcAttrTypeReport);
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcAttrTypeReport, NtcAttrTypeReport.class, page);
			//验证searchAttrType
			ntcReportService.checkNumericCondition(saveLogThread,start,ntcAttrTypeReport.getSearchAttrType(),"searchAttrType");
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcAttrTypeReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcAttrTypeReport(new Page<NtcAttrTypeReport>(request, response, NtcAttrTypeReport.class),
					ntcAttrTypeReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "性质统计查询失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "性质统计查询成功",
				reportPage, 0);
	}
	
	@RequestMapping(value = "/ntcLwhhReport", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号统计查询服务", httpMethod = "GET", notes = "来文函号统计查询服务，基于来文函号与业务类型（service）维度聚合")
	public Map<String, ?> ntcLwhhReport(Page page, NtcLwhhReport ntcLwhhReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcLwhhReport);
			//验证实时报表
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcLwhhReport, NtcLwhhReport.class, page);
			//验证serachCfgId
			ntcReportService.checkNumericCondition(saveLogThread,start,ntcLwhhReport.getSearchLwhh(),"searchLwhh");
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcLwhhReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcLwhhReport(new Page<NtcLwhhReport>(request, response, NtcLwhhReport.class),
					ntcLwhhReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "来文函号统计查询失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "来文函号统计查询成功",
				reportPage, 0);
	}
	
	@RequestMapping(value = "/ntcSrcipDomesticReport", method = RequestMethod.GET)
	@ApiOperation(value = "境内源IP统计查询服务", httpMethod = "GET", notes = "境内源IP统计查询服务，基于源IP所属省、市与业务类型（service）维度聚合")
	public Map<String, ?> ntcSrcipDomesticReport(Page page, NtcSrcipDomesticReport ntcSrcipDomesticReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcSrcipDomesticReport);
			//验证实时报表
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcSrcipDomesticReport, NtcSrcipDomesticReport.class, page);
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcSrcipDomesticReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcSrcipDomesticReport(new Page<NtcSrcipDomesticReport>(request, response, NtcSrcipDomesticReport.class),
					ntcSrcipDomesticReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "境内源IP统计查询失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "境内源IP统计查询成功",
				reportPage, 0);
	}
	
	@RequestMapping(value = "/ntcDestipCountryReport", method = RequestMethod.GET)
	@ApiOperation(value = "各国家目的IP统计查询服务", httpMethod = "GET", notes = "各国家目的IP统计查询服务，基于目的IP所属国家与业务类型（service）维度聚合")
	public Map<String, ?> ntcDestipCountryReport(Page page, NtcDestipCountryReport ntcDestipCountryReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcDestipCountryReport);
			//验证实时报表
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcDestipCountryReport, NtcDestipCountryReport.class, page);
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcDestipCountryReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcDestipCountryReport(new Page<NtcDestipCountryReport>(request, response, NtcDestipCountryReport.class),
					ntcDestipCountryReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "各国家目的IP统计查询失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "各国家目的IP统计查询成功",
				reportPage, 0);
	}
	
	@RequestMapping(value = "/ntcEntranceReport", method = RequestMethod.GET)
	@ApiOperation(value = "运营商局点统计查询服务", httpMethod = "GET", notes = "运营商局点统计查询服务，基于出入口与业务类型（service）维度聚合")
	public Map<String, ?> ntcEntranceReport(Page page, NtcEntranceReport ntcEntranceReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcEntranceReport);
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcEntranceReport, NtcEntranceReport.class, page);
			//验证searchEntrance
			ntcReportService.checkNumericCondition(saveLogThread,start,ntcEntranceReport.getSearchEntrance(),"searchEntrance");
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcEntranceReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcEntranceReport(new Page<NtcEntranceReport>(request, response, NtcEntranceReport.class),
					ntcEntranceReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "运营商局点统计查询失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "运营商局点统计查询成功",
				reportPage, 0);
	}
	/**
	 * @Description:
	 * @author (zdx)
	 * @date 2018年7月12日 上午11:01:30
	 * @param entity
	 * @throws Exception
	 */
	public void resetReportTime(NtcReportEntity<?> entity) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> map = DateUtils.getLocalTime(entity.getSearchReportStartTime(),
				entity.getSearchReportEndTime(), Constants.PZ_REPORT_TIME, "minute");
		entity.setSearchReportStartTime(map.get("startTime"));
		entity.setSearchReportEndTime(map.get("endTime"));
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
