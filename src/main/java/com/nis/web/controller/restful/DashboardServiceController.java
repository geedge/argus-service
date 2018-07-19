package com.nis.web.controller.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nis.web.service.AuditLogThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.util.JsonMapper;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DashboardService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "${servicePath}/log/v1")
@Api(value = "DashboardServiceController", description = "统计分析图表基本服务接口")
public class DashboardServiceController extends BaseRestController {
	
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	@Autowired
	public DashboardService dashboardService;
		
	/**
	 * 协议统计
	 */
	@RequestMapping(value="trafficProtocol", method = RequestMethod.GET)
	@ApiOperation(value = "协议统计", httpMethod = "GET", notes = "对应协议统计实时统计查询服务。")
	public Map<String,?> trafficProtocol(Model model, HttpServletRequest request, HttpServletResponse response){
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			List<Map> ipActiveChart = dashboardService.protocolChart();
				if (ipActiveChart.size() > 0) {
					String jsonString = JsonMapper.toJsonString(ipActiveChart);
					list = (java.util.List<HashMap>) JsonMapper.fromJsonList(jsonString,HashMap.class);
					logger.info(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "协议统计图表数据检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "IP地址日志检索成功",
				list, 0);
	}
	
	
	/**
	 * 活跃IP TOP10
	 */
	@RequestMapping(value = "trafficIpActive", method = RequestMethod.GET)
	@ApiOperation(value = "活跃IP统计", httpMethod = "GET", notes = "对应活跃IP实时统计查询服务。")
	public Map<String,?> trafficIpActive(Model model, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<LinkedHashMap> list = new ArrayList<LinkedHashMap>();
		try {
			List<LinkedHashMap> ipActiveChart = dashboardService.ipActiveChart();
				if (ipActiveChart.size() > 0) {
					list = ipActiveChart;
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "活跃IP实时统计数据检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "活跃IP实时统计数据检索成功",list, 0);
	}
	/**
	 * app流量分析 TOP10
	 */
	@RequestMapping(value = "trafficApp", method = RequestMethod.GET)
	@ApiOperation(value = "app流量分析统计", httpMethod = "GET", notes = "对应app流量分析实时统计查询服务。")
	public Map<String, ?> trafficApp(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> appChart = dashboardService.appChart();
			if (appChart.size() > 0) {
				list = appChart;
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "App流量统计数据检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "App流量统计数据检索成功",list, 0);
	}
	@RequestMapping(value = "trafficOsList", method = RequestMethod.GET)
	@ApiOperation(value = "操作系统流量分析统计", httpMethod = "GET", notes = "对应终端用户的操作系统列表显示")
	public Map<String,?> trafficOsList(Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> osChart = dashboardService.systemList();
			if (osChart.size() > 0) {
				list = osChart;
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "操作系统流量分析统计检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "操作系统流量分析统计检索成功",list, 0);
	}
	@RequestMapping(value = "trafficBrowserChart", method = RequestMethod.GET)
	@ApiOperation(value = "操作系统下浏览器流量分析统计", httpMethod = "GET", notes = "对应终端用户某个操作系统的浏览器分类统计显示")
	public Map<String, ?> trafficBrowserChart(Integer osType,Model model, HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> osChart = dashboardService.getBrowserBySystem(osType);
			if (osChart.size() > 0) {
				list=osChart;
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "操作系统下浏览器分类统计数据检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "操作系统下浏览器分类统计数据检索成功",list, 0);
	}
	@RequestMapping(value = "trafficBsList", method = RequestMethod.GET)
	@ApiOperation(value = "浏览器流量分析统计", httpMethod = "GET", notes = "对应终端用户的浏览器列表显示")
	public Map<String, ?> trafficBsList(Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> bsChart = dashboardService.browserList();
			if (bsChart.size() > 0) {
				list = bsChart;
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "浏览器流量分析统计数据检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "浏览器流量分析统计数据检索成功",list, 0);
	}
	@RequestMapping(value = "trafficSystemChart", method = RequestMethod.GET)
	@ApiOperation(value = "浏览器下操作系统流量分析统计", httpMethod = "GET", notes = "对应终端用户某个浏览器的操作系统分类统计显示")
	public Map<String,?> trafficSystemChart(Integer bsType,Model model, HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> bsChart = dashboardService.getSystemBybrowser(bsType);
			if (bsChart.size() > 0) {
				list = bsChart;
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "浏览器下操作系统流量统计数据检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "浏览器下操作系统流量统计数据检索成功",list, 0);
	}
	@RequestMapping(value = "trafficWebsiteList", method = RequestMethod.GET)
	@ApiOperation(value = "网站流量分析统计", httpMethod = "GET", notes = "对应网站http分类显示")
	public Map<String,?> trafficWebsiteList(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> websiteChart = dashboardService.websiteList();
			if (websiteChart.size() > 0) {
				list = websiteChart;
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "网站流量分析统计数据检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "网站流量分析统计数据检索成功",list, 0);
	}
	@RequestMapping(value = "trafficWebTypeChart", method = RequestMethod.GET)
	@ApiOperation(value = "http网站分类分析统计", httpMethod = "GET", notes = "对应某个网站类型分类统计图")
	public Map<String, ?> trafficWebTypeChart(Integer webId,Model model, HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> websiteChart = dashboardService.getTypeBywebsite(webId);
			if (websiteChart.size() > 0) {
				list = websiteChart;
			}
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "http网站分类分析数据检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "http网站分类分析数据检索成功",list, 0);
	}
}
