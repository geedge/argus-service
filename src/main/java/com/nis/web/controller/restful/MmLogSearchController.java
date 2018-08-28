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
import com.nis.restful.RestServiceException;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.HiveJDBCByDruid;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.HiveSqlService;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.NtcLogService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName:MmLogSearchController
 * @Description:TODO(MM配置命中日志基本服务接口)
 * @author (zdx)
 * @date 2018年7月24日 下午5:27:11
 * @version V1.0
 */

@RestController
@RequestMapping("${servicePath}/log/v1")
@Api(value = "MmLogSearcgController", description = "MM配置命中日志基本服务接口")
public class MmLogSearchController extends BaseRestController {
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	@Autowired
	protected NtcLogService ntcLogService;
	
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
			String sql = HiveSqlService.getSql(page, mmAvIpLog,
					Configurations.getStringProperty(MmAvIpLog.class.getSimpleName() + "HiveTable", "MM_AV_IP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmAvIpLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmAvUrlLog,
					Configurations.getStringProperty(MmAvUrlLog.class.getSimpleName() + "HiveTable", "MM_AV_URL_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmAvUrlLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmPicIpLog,
					Configurations.getStringProperty(MmPicIpLog.class.getSimpleName() + "HiveTable", "MM_PIC_IP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmPicIpLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmPicUrlLog,
					Configurations.getStringProperty(MmPicUrlLog.class.getSimpleName() + "HiveTable", "MM_PIC_URL_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmPicUrlLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmVoipIpLog,
					Configurations.getStringProperty(MmVoipIpLog.class.getSimpleName() + "HiveTable", "MM_VOIP_IP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmVoipIpLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmVoipLog,
					Configurations.getStringProperty(MmVoipAccountLog.class.getSimpleName() + "HiveTable", "MM_VOIP_ACCOUNT_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmVoipAccountLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmSampleAudioLog,
					Configurations.getStringProperty(MmSampleAudioLog.class.getSimpleName() + "HiveTable", "MM_SAMPLE_AUDIO_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmSampleAudioLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmSampleVideoLog,
					Configurations.getStringProperty(MmSampleVideoLog.class.getSimpleName() + "HiveTable", "MM_SAMPLE_VIDEO_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmSampleVideoLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmPornAudioLevelLog,
					Configurations.getStringProperty(MmPornAudioLevelLog.class.getSimpleName() + "HiveTable", "MM_PORN_AUDIO_LEVEL_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmPornAudioLevelLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmPornVideoLevelLog,
					Configurations.getStringProperty(MmPornVideoLevelLog.class.getSimpleName() + "HiveTable", "MM_PRON_VIDEO_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmPornVideoLevelLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmSamplePicLog,
					Configurations.getStringProperty(MmSamplePicLog.class.getSimpleName() + "HiveTable", "MM_SAMPLE_PIC_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmSamplePicLog.class, "foundTime",
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
			String sql = HiveSqlService.getSql(page, mmSampleVoipLog,
					Configurations.getStringProperty(MmSampleVoipLog.class.getSimpleName() + "HiveTable", "MM_SAMPLE_VOIP_LOG"),
					getCol2Col(), orderBy, null);
			Map<String, List<Object>> tableMapping = new HiveJDBCByDruid().tableMapping(page, null, sql, MmSampleVoipLog.class, "foundTime",
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
	
	
	/**
	 *判断开始和结束时间是否为null,如果为null则初始化时间
	 * @param entity
	 * @throws Exception
	 */
	public void resetTime(LogEntity<?> entity) throws Exception {
		if (StringUtil.isEmpty(entity.getSearchFoundStartTime())&&StringUtil.isEmpty(entity.getSearchFoundEndTime())) {
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
