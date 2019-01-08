package com.nis.web.controller.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.datasource.CustomerContextHolder;
import com.nis.domain.restful.NtcConnNumReport;
import com.nis.domain.restful.NtcRadiusReport;
import com.nis.domain.restful.dashboard.AppConnRecordStatistic;
import com.nis.domain.restful.dashboard.AppTrendEntity;
import com.nis.domain.restful.dashboard.TrafficAppFocusStatistic;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.ExceptionUtil;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DashboardService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zdjizhi.utils.StringUtil;

@SuppressWarnings("all")
@RestController
@RequestMapping(value = "${servicePath}/log/v1")
@Api(value = "DashboardServiceController", description = "统计分析图表基本服务接口")
public class DashboardServiceController extends BaseRestController {

	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	@Autowired
	public DashboardService dashboardService;

	/**
	 * 流量统计info滚动条数据显示 封堵监测回流丢弃
	 */
	@RequestMapping(value = "ntcTotalReport", method = RequestMethod.GET)
	@ApiOperation(value = "业务总量汇聚", httpMethod = "GET", notes = "对应流量统计info滚动条数据显示")
	public Map<String, ?> ntcTotalReport(Model model, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> resultList = dashboardService.getTotalReportList();
			if (resultList != null && resultList.size() > 0) {
				list = resultList;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("业务总量汇聚实时统计数据检索失败:" + e.getMessage());
			logger.error("业务总量汇聚实时统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"业务总量汇聚实时统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"业务总量汇聚实时统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"业务总量汇聚实时统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}

		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "业务总量汇聚实时统计数据检索成功", list,
				0);
	}

	/**
	 * 流量统计info滚动条数据显示 封堵监测回流丢弃详情趋势
	 */
	@RequestMapping(value = "ntcActionEntranceReport", method = RequestMethod.GET)
	@ApiOperation(value = "根据不同动作统计总量汇聚", httpMethod = "GET", notes = "对应流量统计info滚动条动作类型详情数据显示")
	public Map<String, ?> ntcTotalReport(Model model, HttpServletRequest request, HttpServletResponse response,
			String searchAction, String beginDate, String endDate) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			if (StringUtils.isEmpty(beginDate) || StringUtils.isEmpty(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());// 获取到完整的时间
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
				beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
			}
			List<HashMap> resultList = dashboardService.getActionTrans2(beginDate, endDate, searchAction);
			if (resultList != null && resultList.size() > 0) {
				list = resultList;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("动作趋势分析统计数据检索失败:" + e.getMessage());
			logger.error("动作趋势分析统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"动作趋势分析统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"动作趋势分析统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"动作趋势分析统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "动作趋势分析统计数据检索成功", list,
				0);
	}

	/**
	 * 根据ip46,协议tcp，udp查询带宽
	 */
	@RequestMapping(value = "trafficBandwidthTrans", method = RequestMethod.GET)
	@ApiOperation(value = "带宽根据ip46,协议tcp,udp查询详情", httpMethod = "GET", notes = "对应带宽根据IPv4,6,协议tcp,udp统计数据显示")
	public Map<String, ?> trafficBandwidthTrans(String beginDate, String endDate, String addrType, Integer transType,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			if (StringUtils.isEmpty(beginDate) || StringUtils.isEmpty(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());// 获取到完整的时间
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
				beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
			}
			List<HashMap> resultList = dashboardService.getBandwidthTrans(addrType, transType, beginDate, endDate);
			if (resultList != null && resultList.size() > 0) {
				list = resultList;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("带宽实时统计数据检索失败:" + e.getMessage());
			logger.error("带宽实时统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "带宽实时统计数据检索成功", list, 0);
	}

	/**
	 * 根据ip46,协议tcp，udp查询带宽
	 */
	@RequestMapping(value = "trafficBandwidthTransTwo", method = RequestMethod.GET)
	@ApiOperation(value = "带宽根据ip46,协议tcp,udp查询详情", httpMethod = "GET", notes = "对应带宽根据IPv4,6,协议tcp,udp统计数据显示")
	public Map<String, ?> trafficBandwidthTransTow(String beginDate, String endDate, String addrType, Integer transType,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			if (StringUtils.isEmpty(beginDate) || StringUtils.isEmpty(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());// 获取到完整的时间
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
				beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
			}
			List<HashMap> resultList = dashboardService.getBandwidthTrans2(addrType, transType, beginDate, endDate);
			if (resultList != null && resultList.size() > 0) {
				list = resultList;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("带宽实时统计数据检索失败:" + e.getMessage());
			logger.error("带宽实时统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "带宽实时统计数据检索成功", list, 0);
	}

	/**
	 *  根据ip46,协议tcp，udp查询带宽
	 */
	@RequestMapping(value = "trafficBandwidthTransThree", method = RequestMethod.GET)
	@ApiOperation(value = "带宽根据ip46,协议tcp,udp查询详情", httpMethod = "GET", notes = "对应带宽根据IPv4,6,协议tcp,udp统计数据显示")
	public Map<String,?> trafficBandwidthTransThree(String beginDate,String endDate,String searchQuotaType,Model model, HttpServletRequest request, HttpServletResponse response) {
		String[] addrTypes=dashboardService.getFiledTypeByName("addr_type");
		String[] transTypes=dashboardService.getFiledTypeByName("trans_type");
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		Map resultMap=new HashMap();
		try {
			if(StringUtils.isEmpty(beginDate)&&StringUtils.isEmpty(endDate)) {
				Calendar cal = Calendar. getInstance ();
		        cal.setTime(new Date());
		        endDate =  new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(cal.getTime());//获取到完整的时间
		        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
		        beginDate =  new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(cal.getTime());
			}
			resultMap = dashboardService.getBandwidthTransEntrance(beginDate, endDate,searchQuotaType);
			
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("带宽实时统计数据检索失败:"+e.getMessage());
			logger.error("带宽实时统计数据检索失败:"+ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"带宽实时统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "带宽实时统计数据检索成功",resultMap, 0);
	}
	/**
	 * 流量统计活跃端口统计
	 */
	@RequestMapping(value = "trafficPortActive", method = RequestMethod.GET)
	@ApiOperation(value = "活跃端口统计", httpMethod = "GET", notes = "对应流量统计活跃端口统计")
	public Map<String, ?> trafficPortActive(Model model, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> resultList = dashboardService.getPortActiveList();
			if (resultList != null && resultList.size() > 0) {
				list = resultList;
			}

		} catch (Exception e) {
			auditLogThread.setExceptionInfo("活跃端口实时统计数据检索失败:" + e.getMessage());
			logger.error("活跃端口实时统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"活跃端口实时统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"活跃端口实时统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"活跃端口实时统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "活跃端口实时统计数据检索成功", list,
				0);
	}

	/**
	 * 协议统计
	 */
	@RequestMapping(value = "trafficProtocol", method = RequestMethod.GET)
	@ApiOperation(value = "协议统计", httpMethod = "GET", notes = "对应协议统计实时统计查询服务。")
	public Map<String, ?> trafficProtocol(Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			List<Map> ipActiveChart = dashboardService.protocolChart();
			if (ipActiveChart != null && ipActiveChart.size() > 0) {
				String jsonString = JsonMapper.toJsonString(ipActiveChart);
				list = (java.util.List<HashMap>) JsonMapper.fromJsonList(jsonString, HashMap.class);
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("协议统计图表数据检索失败:" + e.getMessage());
			logger.error("协议统计图表数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"协议统计图表数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"协议统计图表数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"协议统计图表数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "协议统计图表数据检索成功", list, 0);
	}

	/**
	 * 协议统计详情图与表
	 */
	@RequestMapping(value = "trafficProtocolList", method = RequestMethod.GET)
	@ApiOperation(value = "协议统计占比与报表", httpMethod = "GET", notes = "对应协议统计详情占比与报表")
	public Map<String, ?> trafficProtocolList(Integer[] protoType,String beginDate, String endDate,Integer entranceId, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			List<Map> ipActiveList = dashboardService.getProtocolList(beginDate, endDate,protoType,entranceId);
			if (ipActiveList != null && ipActiveList.size() > 0) {
				String jsonString = JsonMapper.toJsonString(ipActiveList);
				list = (java.util.List<HashMap>) JsonMapper.fromJsonList(jsonString, HashMap.class);
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("协议统计占比与报表检索失败:" + e.getMessage());
			logger.error("协议统计占比与报表检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"协议统计占比与报表检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"协议统计占比与报表检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"协议统计占比与报表检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "协议统计占比与报表检索成功", list,
				0);
	}

	/**
	 * 活跃IP TOP10
	 */
	@RequestMapping(value = "trafficIpActive", method = RequestMethod.GET)
	@ApiOperation(value = "活跃IP统计", httpMethod = "GET", notes = "对应活跃IP实时统计查询服务。")
	public Map<String, ?> trafficIpActive(String beginDate, String endDate, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<LinkedHashMap> list = new ArrayList<LinkedHashMap>();
		try {
			Date begin = null;
			Date end = null;
			if (StringUtils.isEmpty(beginDate) || StringUtils.isEmpty(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				begin = cal.getTime();// 获取到完整的时间
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
				end = cal.getTime();
			} else {
				begin = DateUtils.parseDate(beginDate);
				end = DateUtils.parseDate(endDate);
			}

			List<LinkedHashMap> ipActiveChart = dashboardService.ipActiveChart(begin, end);
			if (ipActiveChart != null && ipActiveChart.size() > 0) {
				list = ipActiveChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("活跃IP实时统计数据检索失败:" + e.getMessage());
			logger.error("活跃IP实时统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP实时统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP实时统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP实时统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "活跃IP实时统计数据检索成功", list,
				0);
	}

	/**
	 * 活跃IP TOP10 一小时数据
	 */
	@RequestMapping(value = "trafficIpActiveOneHour", method = RequestMethod.GET)
	@ApiOperation(value = "活跃IP统计一个小时的活跃IP", httpMethod = "GET", notes = "对应活跃IP实时统计查询服务。")
	public Map<String, ?> trafficIpActiveOneHour(String beginDate, String endDate, Model model,Integer entranceId,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			Date begin = DateUtils.parseDate(beginDate);
			Date end = DateUtils.parseDate(endDate);
			List<HashMap> ipActiveChart = dashboardService.ipActiveOneHour(begin, end,entranceId);
			if (ipActiveChart != null && ipActiveChart.size() > 0) {
				list = ipActiveChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("活跃IP统计一个小时的活跃IP检索失败:" + e.getMessage());
			logger.error("活跃IP统计一个小时的活跃IP检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP统计一个小时的活跃IP检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP统计一个小时的活跃IP检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP统计一个小时的活跃IP检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "活跃IP统计一个小时的活跃IP检索成功",
				list, 0);
	}

	/**
	 * 活跃IP TOP10 一小时间隔五分钟数据
	 */
	@RequestMapping(value = "trafficIpActiveFiveMinute", method = RequestMethod.GET)
	@ApiOperation(value = "活跃IP最近一个小时的变化趋势统计", httpMethod = "GET", notes = "对最新TOP10的活跃IP，在近一个小时的变化情况进行统计")
	public Map<String, ?> trafficIpActiveFiveMinute(String beginDate, String endDate, Model model,Integer entranceId,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			Date begin = null;
			Date end = null;
			if (!StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {
				begin = DateUtils.parseDate(beginDate);
				end = DateUtils.parseDate(endDate);
			}
			List<Map> ipActiveChart = dashboardService.ipActiveFiveMinute(begin, end,entranceId);
			if (ipActiveChart != null && ipActiveChart.size() > 0) {
				list = ipActiveChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("活跃IP最近一个小时的变化趋势统计失败:" + e.getMessage());
			logger.error("活跃IP最近一个小时的变化趋势统计失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP最近一个小时的变化趋势统计失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP最近一个小时的变化趋势统计失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"活跃IP最近一个小时的变化趋势统计失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "活跃IP最近一个小时的变化趋势统计成功",
				list, 0);
	}

	/**
	 * app应用流量分析 TOP10
	 */
	@RequestMapping(value = "trafficApp", method = RequestMethod.GET)
	@ApiOperation(value = "App流量分析统计", httpMethod = "GET", notes = "对应app流量分析实时统计查询服务。")
	public Map<String, ?> trafficApp(Model model, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> appChart = dashboardService.appChart();
			if (appChart != null && appChart.size() > 0) {
				list = appChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("App流量统计数据检索失败:" + e.getMessage());
			logger.error("App流量统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"App流量统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App流量统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App流量统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "App流量统计数据检索成功", list,
				0);
	}

	/**
	 * App统计详情图与表
	 */
	@RequestMapping(value = "trafficAppList", method = RequestMethod.GET)
	@ApiOperation(value = "App统计占比与报表", httpMethod = "GET", notes = "对应App统计详情占比与报表")
	public Map<String, ?> trafficAppList(Integer[] appType, String beginDate, String endDate, Model model,Integer entranceId,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			List<Map> appList = dashboardService.getAppList(beginDate, endDate, appType,entranceId);
			if (appList != null && appList.size() > 0) {
				String jsonString = JsonMapper.toJsonString(appList);
				list = (java.util.List<HashMap>) JsonMapper.fromJsonList(jsonString, HashMap.class);
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("App统计图表数据检索失败:" + e.getMessage());
			logger.error("App统计图表数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"App统计图表数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App统计图表数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App统计图表数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "App统计图表数据检索成功", list,
				0);
	}

	@RequestMapping(value = "trafficOsList", method = RequestMethod.GET)
	@ApiOperation(value = "操作系统流量分析统计", httpMethod = "GET", notes = "对应终端用户的操作系统列表显示")
	public Map<String, ?> trafficOsList(Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> osChart = dashboardService.systemList();
			if (osChart != null && osChart.size() > 0) {
				list = osChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("操作系统流量分析统计检索失败:" + e.getMessage());
			logger.error("操作系统流量分析统计检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"操作系统流量分析统计检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"操作系统流量分析统计检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"操作系统流量分析统计检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "操作系统流量分析统计检索成功", list,
				0);
	}

	@RequestMapping(value = "trafficBrowserChart", method = RequestMethod.GET)
	@ApiOperation(value = "操作系统下浏览器流量分析统计", httpMethod = "GET", notes = "对应终端用户某个操作系统的浏览器分类统计显示")
	public Map<String, ?> trafficBrowserChart(Integer osType, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> osChart = dashboardService.getBrowserBySystem(osType);
			if (osChart != null && osChart.size() > 0) {
				list = osChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("操作系统下浏览器分类统计数据检索失败:" + e.getMessage());
			logger.error("操作系统下浏览器分类统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"操作系统下浏览器分类统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"操作系统下浏览器分类统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"操作系统下浏览器分类统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "操作系统下浏览器分类统计数据检索成功",
				list, 0);
	}

	@RequestMapping(value = "trafficBsList", method = RequestMethod.GET)
	@ApiOperation(value = "浏览器流量分析统计", httpMethod = "GET", notes = "对应终端用户的浏览器列表显示")
	public Map<String, ?> trafficBsList(Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> bsChart = dashboardService.browserList();
			if (bsChart != null && bsChart.size() > 0) {
				list = bsChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("浏览器流量分析统计数据检索失败:" + e.getMessage());
			logger.error("浏览器流量分析统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"浏览器流量分析统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"浏览器流量分析统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"浏览器流量分析统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "浏览器流量分析统计数据检索成功", list,
				0);
	}

	@RequestMapping(value = "trafficSystemChart", method = RequestMethod.GET)
	@ApiOperation(value = "浏览器下操作系统流量分析统计", httpMethod = "GET", notes = "对应终端用户某个浏览器的操作系统分类统计显示")
	public Map<String, ?> trafficSystemChart(Integer bsType, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			List<Map> bsChart = dashboardService.getSystemBybrowser(bsType);
			if (bsChart != null && bsChart.size() > 0) {
				list = bsChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("浏览器下操作系统流量统计数据检索失败:" + e.getMessage());
			logger.error("浏览器下操作系统流量统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"浏览器下操作系统流量统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"浏览器下操作系统流量统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"浏览器下操作系统流量统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "浏览器下操作系统流量统计数据检索成功",
				list, 0);
	}

	@RequestMapping(value = "trafficWebsiteList", method = RequestMethod.GET)
	@ApiOperation(value = "网站流量分析统计", httpMethod = "GET", notes = "对应网站http分类显示")
	public Map<String, ?> trafficWebsiteList( Model model, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			// 不带时间默认top10
			List<Map> websiteChart = dashboardService.getWebsiteTop10();
			if (websiteChart != null && websiteChart.size() > 0) {
				list = websiteChart;
			}

		} catch (Exception e) {
			auditLogThread.setExceptionInfo("网站流量分析统计数据检索失败:" + e.getMessage());
			logger.error("网站流量分析统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"网站流量分析统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"网站流量分析统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"网站流量分析统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "网站流量分析统计数据检索成功", list,
				0);
	}

	@RequestMapping(value = "trafficWebsiteListNoTime", method = RequestMethod.GET)
	@ApiOperation(value = "网站流量分析详情统计", httpMethod = "GET", notes = "对应网站http分类显示")
	public Map<String, ?> trafficWebsiteListNoTime(Integer domain[],Integer entranceId , @RequestParam String beginDate, @RequestParam String endDate,Model model, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			if (!StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {
				Date begin = DateUtils.parseDate(beginDate);
				Date end = DateUtils.parseDate(endDate);
				// 带查询时间查询所有
				List<Map> websiteChart = dashboardService.getWebsiteDetails(begin, end,domain,entranceId);
				if (websiteChart != null && websiteChart.size() > 0) {
					list = websiteChart;
				}
			}else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				Date begin1 = cal.getTime();// 获取到完整的时间
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
				Date end1 = cal.getTime();
				// 带查询时间查询所有
				List<Map> websiteChart = dashboardService.getWebsiteDetails(begin1, end1,domain,entranceId);
				if (websiteChart != null && websiteChart.size() > 0) {
					list = websiteChart;
				}
			}

		} catch (Exception e) {
			auditLogThread.setExceptionInfo("网站流量分析统计数据检索失败:" + e.getMessage());
			logger.error("网站流量分析统计数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"网站流量分析统计数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"网站流量分析统计数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"网站流量分析统计数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "网站流量分析统计数据检索成功", list,
				0);
	}

	@RequestMapping(value = "trafficWebTypeChart", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP网站分类分析统计", httpMethod = "GET", notes = "对应某个网站类型分类统计图")
	public Map<String, ?> trafficWebTypeChart(String beginDate, String endDate, Integer websiteServiceId, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			if (!StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {
				Date begin = DateUtils.parseDate(beginDate);
				Date end = DateUtils.parseDate(endDate);
				// 带查询时间查询所有
				List<Map> websiteChart = dashboardService.getWebsiteDetailsById(websiteServiceId, begin, end);
				if (websiteChart != null && websiteChart.size() > 0) {
					list = websiteChart;
				}
			} else {
				// 不带时间默认top10
				List<Map> websiteChart = dashboardService.getWebsiteDetailsById(websiteServiceId, null, null);
				if (websiteChart != null && websiteChart.size() > 0) {
					list = websiteChart;
				}
			}

		} catch (Exception e) {
			auditLogThread.setExceptionInfo("HTTP网站分类分析统计失败:" + e.getMessage());
			logger.error("HTTP网站分类分析统计失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站分类分析统计失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站分类分析统计失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站分类分析统计失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "HTTP网站分类分析统计成功", list,
				0);
	}

	@RequestMapping(value = "trafficTopicAndDomainChart", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP网站主题分类分析统计", httpMethod = "GET", notes = "对应某个网站主题类型分类统计图")
	public Map<String, ?> trafficTopicAndDomainChart( Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
				// 不带时间默认top10
				List<Map> websiteChart = dashboardService.getTopicTop10();
				if (websiteChart != null && websiteChart.size() > 0) {
					list = websiteChart;
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("HTTP网站主题分类分析数据检索失败:" + e.getMessage());
			logger.error("HTTP网站主题分类分析数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站主题分类分析数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站主题分类分析数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站主题分类分析数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "HTTP网站主题分类分析数据检索成功",
				list, 0);
	}
	
	@RequestMapping(value = "trafficTopicList", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP网站主题详情", httpMethod = "GET", notes = "对应某个网站主题类型分类统计")
	public Map<String, ?> trafficTopicList(String beginDate, String endDate, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			if (!StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {
				Date begin = DateUtils.parseDate(beginDate);
				Date end = DateUtils.parseDate(endDate);
				// 带查询时间查询所有
				List<Map> websiteChart = dashboardService.getTopicDetails(begin, end);
				if (websiteChart != null && websiteChart.size() > 0) {
					list = websiteChart;
				}
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				Date end1 = cal.getTime();// 获取到完整的时间
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
				Date begin1 = cal.getTime();
				// 带查询时间查询所有
				List<Map> websiteChart = dashboardService.getTopicDetails(begin1, end1);
				if (websiteChart != null && websiteChart.size() > 0) {
					list = websiteChart;
				}
			}
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("HTTP网站主题分类分析数据检索失败:" + e.getMessage());
			logger.error("HTTP网站主题分类分析数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站主题分类分析数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站主题分类分析数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站主题分类分析数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "HTTP网站主题分类分析数据检索成功",
				list, 0);
	}
	@RequestMapping(value = "trafficDomainTrans", method = RequestMethod.GET)
	@ApiOperation(value = "域名详情趋势", httpMethod = "GET", notes = "域名UV")
	public Map<String, ?> trafficDomainTrans(String beginDate, String endDate, Model model, Integer domain,Integer entranceId,
			HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List list = new ArrayList();
		try {
			
				// 带查询时间查询所有
				List websiteChart = dashboardService.getDomainTrans2(beginDate, endDate,domain,entranceId);
				if (websiteChart != null && websiteChart.size() > 0) {
					list = websiteChart;
				}
			
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("HTTP网站主题分类分析数据检索失败:" + e.getMessage());
			logger.error("HTTP网站域名分类分析数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站域名分类分析数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站域名分类分析数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"HTTP网站域名分类分析数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "HTTP网站域名分类分析数据检索成功",
				list, 0);
	}
	

	@RequestMapping(value = "trafficPortActiveFiveMinute", method = RequestMethod.GET)
	@ApiOperation(value = "流量端口详情趋势", httpMethod = "GET", notes = "域名UV")
	public Map<String, ?> trafficPortAcitve(String beginDate, String endDate, Model model, Integer[] port,
			HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<Map> list = new ArrayList<Map>();
		try {
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if((beginDate == null || "".equals(beginDate)) && (endDate == null || "".equals(endDate))){
				//如果时间全部为空查询最近一小时				
				endDate = sdf.format(cal.getTime());
				cal.add(Calendar.HOUR_OF_DAY, -1);
				beginDate = sdf.format(cal.getTime());
			}else if((beginDate == null || "".equals(beginDate)) && (endDate != null && !"".equals(endDate))){
				beginDate = DateUtils.getSpecifiedHourBefore(endDate);
			}else if((endDate == null || "".equals(endDate)) && (beginDate != null && !"".equals(beginDate))){
				endDate = DateUtils.getSpecifiedHourAfter(beginDate);
			}
			// 带查询时间查询所有
			List<Map> portActiveChart = dashboardService.findTrafficPortActive(beginDate, endDate,port);
			if (portActiveChart != null && portActiveChart.size() > 0) {
				list = portActiveChart;
			}
			
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("流量端口详情趋势数据检索失败:" + e.getMessage());
			logger.error("流量端口详情趋势数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"流量端口详情趋势数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"流量端口详情趋势数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"流量端口详情趋势数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "流量端口详情趋势数据检索成功",
				list, 0);
	}
	@RequestMapping(value = "/trendTotalReport", method = RequestMethod.GET)
	@ApiOperation(value = "流量各指标趋势统计", httpMethod = "GET", notes = "根据指标(drop,loop,New Link,Close Link,Pass,Live Link)统计各业务的趋势")
	public Map<String, ?> trendTotalReport(NtcConnNumReport ntcConnNumReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Map restMap = new HashMap();
		try {
			// 验证serachBusinessType
			if (!StringUtil.isBlank(ntcConnNumReport.getSearchBusinessType())
					&& !StringUtil.isNumeric(ntcConnNumReport.getSearchBusinessType())) {
				throw new RestServiceException("searchBusinessType参数格式错误", RestBusinessCode.param_formate_error.getValue());
			}
			restMap = dashboardService.trendTotalReport(ntcConnNumReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo("流量各指标趋势统计失败:" + e.getMessage());
			logger.error("流量各指标趋势统计失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(saveLogThread, System.currentTimeMillis() - start,
						"流量各指标趋势统计失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
						"流量各指标趋势统计失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
						"流量各指标趋势统计失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "流量各指标趋势统计成功",
				restMap, 0);
	}
	/**
	 * @Description:
	 * @author(zdx)
	 * @date 2019年1月4日 下午8:57:32
	 * @param model
	 * @param entity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "trafficAppTrend", method = RequestMethod.GET)
	@ApiOperation(value = "App趋势详情查询", httpMethod = "GET", notes = "对App趋势详情提供数据查询服务")
	public Map<String, ?> trafficAppTrend(TrafficAppFocusStatistic entity, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Map resultMap = new  HashMap();
		try {
			//验证
			checkAppTrendCondition(entity);
			if (StringUtil.isEmpty(entity.getSearchStartTime())
					&& StringUtil.isEmpty(entity.getSearchEndTime())) {
				Map<String, String> map = DateUtils.getLocalTime(null, null, Constants.LOG_LOCAL_TIME, "hour");
				entity.setSearchStartTime(map.get("startTime"));
				entity.setSearchEndTime(map.get("endTime"));
			}
			//将数据源切换到本地clickhouse
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_B);
			resultMap = dashboardService.getAppTrend(entity);
			CustomerContextHolder.clearCustomerType();
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("App趋势详情数据检索失败:" + e.getMessage());
			logger.error("App趋势详情数据检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"App趋势详情数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App趋势详情数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App趋势详情数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "App趋势详情数据检索成功",
				resultMap, 0);
	}
	
	/**
	 * @Description:
	 * @author(zdx)
	 * @date 2019年1月4日 下午8:57:12
	 * @param beginDate
	 * @param endDate
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "appConnRecordTop100", method = RequestMethod.GET)
	@ApiOperation(value = "App通联关系Top100", httpMethod = "GET", notes = "对App通联关系Top100提供查询服务。")
	public Map<String, ?> appConnRecordTop100(AppConnRecordStatistic entity,Model model, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<AppConnRecordStatistic> list = new ArrayList<AppConnRecordStatistic>();
		try {
			//验证
			checkAppTrendCondition(entity);
			if (StringUtil.isEmpty(entity.getSearchStartTime())
					&& StringUtil.isEmpty(entity.getSearchEndTime())) {
				Map<String, String> map = DateUtils.getLocalTime(null, null, Constants.LOG_LOCAL_TIME, "hour");
				entity.setSearchStartTime(map.get("startTime"));
				entity.setSearchEndTime(map.get("endTime"));
			}
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_B);
			list = dashboardService.appConnRecordTop100(entity);
			CustomerContextHolder.clearCustomerType();
		} catch (Exception e) {
			auditLogThread.setExceptionInfo("App通联关系Top100数据检索失败:" + e.getMessage());
			logger.error("App通联关系Top100检索失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(auditLogThread, System.currentTimeMillis() - start,
						"App通联关系Top100数据检索失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App通联关系Top100数据检索失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(auditLogThread, System.currentTimeMillis() - start,
						"App通联关系Top100数据检索失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "App通联关系Top100数据检索成功", list,
				0);
	}
	/**
	 */
	public void checkAppTrendCondition(AppTrendEntity entity) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int timeCount = 0;
		try {
			if (!StringUtil.isBlank(entity.getSearchStartTime())) {
				sdf.parse(entity.getSearchStartTime());
				timeCount++;
			}
		} catch (ParseException e) {
			throw new RestServiceException("searchStartTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			throw new RestServiceException("searchStartTime参数错误",
					RestBusinessCode.param_formate_error.getValue());
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchEndTime())) {
				sdf.parse(entity.getSearchEndTime());
				timeCount++;
			}
		} catch (ParseException e) {
			throw new RestServiceException("searchEndTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			throw new RestServiceException("searchEndTime参数错误", RestBusinessCode.param_formate_error.getValue());
		}
		if (timeCount == 1) {
			throw new RestServiceException("searchStartTime和searchEndTime参数必须同时填写",
					RestBusinessCode.config_integrity_error.getValue());
		}
		//验证AppId和EntranceId必须是数值
		checkNumericCondition(entity.getSearchAppId(),"searchAppId");
		checkNumericCondition(entity.getSearchEntranceId(),"searchEntranceId");
		logger.info("用户行为日志统计参数校验结束----" + System.currentTimeMillis());

	}
	public void checkNumericCondition(String condition, String condName) {
		if (!StringUtil.isEmpty(condition)) {
			Boolean flag = false;
			if (condition.contains(",")) {
				String services[] = condition.split(",");
				for (String service : services) {
					if (!StringUtil.isNumeric(service)) {
						flag = true;
						break;
					}
				}
			} else if (!StringUtil.isNumeric(condition)) {
				flag = true;
			}
			if (flag) {
				throw new RestServiceException(condName + "参数格式错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
	}
}
