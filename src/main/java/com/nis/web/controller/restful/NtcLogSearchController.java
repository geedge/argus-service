package com.nis.web.controller.restful;

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
import com.nis.domain.restful.NtcAppLog;
import com.nis.domain.restful.NtcBgpLog;
import com.nis.domain.restful.NtcDdosLog;
import com.nis.domain.restful.NtcDnsLog;
import com.nis.domain.restful.NtcFtpLog;
import com.nis.domain.restful.NtcHttpLog;
import com.nis.domain.restful.NtcIpLog;
import com.nis.domain.restful.NtcIpsecLog;
import com.nis.domain.restful.NtcKeywordsUrlLog;
import com.nis.domain.restful.NtcL2tpLog;
import com.nis.domain.restful.NtcMailLog;
import com.nis.domain.restful.NtcOpenvpnLog;
import com.nis.domain.restful.NtcP2pLog;
import com.nis.domain.restful.NtcPptpLog;
import com.nis.domain.restful.NtcSshLog;
import com.nis.domain.restful.NtcSslLog;
import com.nis.domain.restful.NtcStreamingMediaLog;
import com.nis.domain.restful.NtcVoipLog;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.JsonMapper;
import com.nis.util.LogJDBCByDruid;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.HiveSqlService;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.NtcLogService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zdjizhi.utils.StringUtil;

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
//			ResultSet rs = HiveSqlService.getResultSet(page, ntcIpLog,
//					getTableName(NtcIpLog.class.getSimpleName() + "HiveTable", "ntc_ip_log"),
//					getCol2Col(), orderBy, null);
//			Map<String, List> tableMapping = HiveJDBC.tableMapping(page, null, rs, NtcIpLog.class, "foundTime",
//					"recvTime");
			String sql = HiveSqlService.getSql(page, ntcIpLog,
					getTableName(NtcIpLog.class.getSimpleName() + "HiveTable", "ntc_ip_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null,sql , NtcIpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcIpLogPage.setList(new ArrayList<NtcIpLog>());
			} else {
				List<Object> list = new ArrayList<Object>();
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
//											getTableName(
//													NtcIpLog.class.getSimpleName() + "HiveTable", "ntc_ip_log"),
//											getCol2Col(), null));
				} else {
					ntcIpLogPage.setList(new ArrayList());
					ntcIpLogPage.setCount(0L);
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
			String sql = HiveSqlService.getSql(page, ntcHttpLog,
					getTableName(NtcHttpLog.class.getSimpleName() + "HiveTable", "ntc_http_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcHttpLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcDnsLog,
					getTableName(NtcDnsLog.class.getSimpleName() + "HiveTable", "ntc_dns_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcDnsLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcMailLog,
					getTableName(NtcMailLog.class.getSimpleName() + "HiveTable", "ntc_mail_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcMailLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcSslLog,
					getTableName(NtcSslLog.class.getSimpleName() + "HiveTable", "ntc_ssl_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcSslLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcPptpLog,
					getTableName(NtcPptpLog.class.getSimpleName() + "HiveTable", "ntc_pptp_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcPptpLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcL2tpLog,
					getTableName(NtcL2tpLog.class.getSimpleName() + "HiveTable", "ntc_l2tp_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcL2tpLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcOpenvpnLog,
					getTableName(NtcOpenvpnLog.class.getSimpleName() + "HiveTable", "ntc_openvpn_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcOpenvpnLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcIpsecLog,
					getTableName(NtcIpsecLog.class.getSimpleName() + "HiveTable", "ntc_ipsec_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcIpsecLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcSshLog,
					getTableName(NtcSshLog.class.getSimpleName() + "HiveTable", "ntc_ssh_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcSshLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcFtpLog,
					getTableName(NtcFtpLog.class.getSimpleName() + "HiveTable", "ntc_ftp_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcFtpLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcAppLog,
					getTableName(NtcAppLog.class.getSimpleName() + "HiveTable", "ntc_app_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcAppLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, ntcDdosLog,
					getTableName(NtcDdosLog.class.getSimpleName() + "HiveTable", "ntc_ddos_log"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcDdosLog.class, "foundTime",
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
	
	@RequestMapping(value = "/ntcP2pLogs", method = RequestMethod.GET)
	@ApiOperation(value = "P2P日志查询", httpMethod = "GET", notes = "对应配置为“文件传输管理-P2P”，存储动作为阻断的命中日志。对日志功能“文件传输管理-P2P日志”提供数据基础查询服务")
	public Map<String, ?> ntcP2pLogs(Page page, NtcP2pLog ntcP2pLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcP2pLog> ntcP2pLogPage = null;
		try {
			resetTime(ntcP2pLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcP2pLog, NtcP2pLog.class, page);
			ntcP2pLogPage = new Page<NtcP2pLog>();
			ntcP2pLogPage.setPageNo(page.getPageNo());
			ntcP2pLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcP2pLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			
			String sql = HiveSqlService.getSql(page, ntcP2pLog,
					getTableName(NtcP2pLog.class.getSimpleName() + "HiveTable", "ntc_p2p_log"),
					getCol2Col(), orderBy, null);
			
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcP2pLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcP2pLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcP2pLog> List = (java.util.List<NtcP2pLog>) JsonMapper.fromJsonList(jsonString,
							NtcP2pLog.class);
					ntcP2pLogPage.setList(List);
					ntcP2pLogPage.setCount(List.size());

				} else {
					ntcP2pLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "P2P日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "P2P日志检索成功",
				ntcP2pLogPage, 0);
	}
	
	@RequestMapping(value = "/ntcBgpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "BGP日志查询", httpMethod = "GET", notes = "对应配置为“其他-BGP”，存储动作为阻断和监测的命中日志。对日志功能“BGP日志”提供数据基础查询服务")
	public Map<String, ?> ntcBgpLogs(Page page, NtcBgpLog ntcBgpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcBgpLog> ntcBgpLogPage = null;
		try {
			resetTime(ntcBgpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcBgpLog, NtcBgpLog.class, page);
			ntcBgpLogPage = new Page<NtcBgpLog>();
			ntcBgpLogPage.setPageNo(page.getPageNo());
			ntcBgpLogPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcBgpLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			
			String sql = HiveSqlService.getSql(page, ntcBgpLog,
					getTableName(NtcBgpLog.class.getSimpleName() + "HiveTable", "ntc_bgp_log"),
					getCol2Col(), orderBy, null);
			
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcBgpLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				ntcBgpLogPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
//				if (tableMapping.get("obj").size() > page.getPageSize()) {
//					list = tableMapping.get("obj").subList(0, page.getPageSize());
//				} else {
//					list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
//				}
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcBgpLog> List = (java.util.List<NtcBgpLog>) JsonMapper.fromJsonList(jsonString,
							NtcBgpLog.class);
					ntcBgpLogPage.setList(List);
					ntcBgpLogPage.setCount(List.size());

				} else {
					ntcBgpLogPage.setList(new ArrayList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "BGP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "BGP日志检索成功",
				ntcBgpLogPage, 0);
	}
	
	@RequestMapping(value = "/ntcVoipLogs", method = RequestMethod.GET)
	@ApiOperation(value = "NTC VoIP日志查询", httpMethod = "GET", notes = "对日志功能“NTC VoIP日志”提供数据基础查询服务")
	public Map<String, ?> ntcVoipLogs(Page page, NtcVoipLog ntcVoipLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcVoipLog> logPage = null;
		try {
			resetTime(ntcVoipLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcVoipLog, NtcVoipLog.class, page);
			logPage = new Page<NtcVoipLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcVoipLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			String sql = HiveSqlService.getSql(page, ntcVoipLog,
					getTableName(NtcVoipLog.class.getSimpleName() + "HiveTable", "NTC_VOIP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcVoipLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcVoipLog> List = (java.util.List<NtcVoipLog>) JsonMapper.fromJsonList(jsonString,
							NtcVoipLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "NTC VoIP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "NTC VoIP日志检索成功",
				logPage, 0);
	}
		
	@RequestMapping(value = "/ntcStreamMediaLogs", method = RequestMethod.GET)
	@ApiOperation(value = "流媒体协议日志查询", httpMethod = "GET", notes = "对日志功能“流媒体协议日志”提供数据基础查询服务")
	public Map<String, ?> ntcStreamMediaLogs(Page page, NtcStreamingMediaLog ntcStreamMediaLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);

		Page<NtcStreamingMediaLog> logPage = null;
		try {
			resetTime(ntcStreamMediaLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcStreamMediaLog, NtcStreamingMediaLog.class, page);
			logPage = new Page<NtcStreamingMediaLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcStreamingMediaLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			String sql = HiveSqlService.getSql(page, ntcStreamMediaLog,
					getTableName(NtcStreamingMediaLog.class.getSimpleName() + "HiveTable", "NTC_STREAMING_MEDIA_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcStreamingMediaLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcStreamingMediaLog> List = (java.util.List<NtcStreamingMediaLog>) JsonMapper.fromJsonList(jsonString,
							NtcStreamingMediaLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "流媒体协议日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "流媒体协议日志检索成功",
				logPage, 0);
	}
	
	@RequestMapping(value = "/ntcKeywordsUrlLogs", method = RequestMethod.GET)
	@ApiOperation(value = "NTC关键字转URL日志查询", httpMethod = "GET", notes = "对日志功能“NTC关键字转URL日志”提供数据基础查询服务")
	public Map<String, ?> ntcKeywordsUrlLogs(Page page, NtcKeywordsUrlLog ntcKeywordsUrlLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
				request, null);
		
		Page<NtcStreamingMediaLog> logPage = null;
		try {
			resetTime(ntcKeywordsUrlLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, ntcKeywordsUrlLog, NtcKeywordsUrlLog.class, page);
			logPage = new Page<NtcStreamingMediaLog>();
			logPage.setPageNo(page.getPageNo());
			logPage.setPageSize(page.getPageSize());
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcStreamingMediaLog.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "found_Time";
			}
			String sql = HiveSqlService.getSql(page, ntcKeywordsUrlLog,
					getTableName(NtcKeywordsUrlLog.class.getSimpleName() + "HiveTable", "NTC_KEYWORDS_URL_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new LogJDBCByDruid().tableMapping(page, null, sql, NtcStreamingMediaLog.class, "foundTime",
					"recvTime");
			if (tableMapping == null) {
				logPage.setList(new ArrayList());
			} else {
				List list = tableMapping.get("obj");
				if (list.size() > 0) {
					String jsonString = JsonMapper.toJsonString(list);
					List<NtcStreamingMediaLog> List = (java.util.List<NtcStreamingMediaLog>) JsonMapper.fromJsonList(jsonString,
							NtcStreamingMediaLog.class);
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
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "NTC关键字转URL日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "NTC关键字转URL日志检索成功",
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
