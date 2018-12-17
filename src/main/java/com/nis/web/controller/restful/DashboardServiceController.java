package com.nis.web.controller.restful;

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

@SuppressWarnings("unchecked")
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
			List<HashMap> resultList = dashboardService.getActionTrans(beginDate, endDate, searchAction);
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
	public Map<String,?> trafficBandwidthTransThree(String beginDate,String endDate,Model model, HttpServletRequest request, HttpServletResponse response) {
		String[] addrTypes=dashboardService.getFiledTypeByName("addr_type");
		String[] transTypes=dashboardService.getFiledTypeByName("trans_type");
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		Map<String,List<HashMap>> resultMap=new HashMap<String,List<HashMap>>();
		try {
			if(StringUtils.isEmpty(beginDate)||StringUtils.isEmpty(endDate)) {
				Calendar cal = Calendar. getInstance ();
		        cal.setTime(new Date());
		        endDate =  new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(cal.getTime());//获取到完整的时间
		        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
		        beginDate =  new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(cal.getTime());
			}
			String addrType=null;
			Integer transType=null;
			if(addrTypes!=null){
				for(int i=0;i<addrTypes.length;i++){
					addrType=addrTypes[i];
					List<HashMap> resultList = dashboardService.getBandwidthTrans(addrType, transType,beginDate,endDate);
					if (resultList!=null&&resultList.size() > 0) {
						resultMap.put("ipv"+addrType+"Type1", resultList);
					}
					
					resultList = dashboardService.getBandwidthTrans2(addrType, transType,beginDate,endDate);
					if (resultList!=null&&resultList.size() > 0) {
						resultMap.put("ipv"+addrType+"Type2", resultList);
					}
					addrType=null;
				}
			}
			if(transTypes!=null){
				for(int i=0;i<transTypes.length;i++){
					transType=Integer.valueOf(transTypes[i]);
					List<HashMap> resultList = dashboardService.getBandwidthTrans(addrType, transType,beginDate,endDate);
					if (resultList!=null&&resultList.size() > 0) {
						resultMap.put("trans"+transType+"Type1", resultList);
					}
					resultList = dashboardService.getBandwidthTrans2(addrType, transType,beginDate,endDate);
					if (resultList!=null&&resultList.size() > 0) {
						resultMap.put("trans"+transType+"Type2", resultList);
					}
					transType=null;
				}
			}
			
			
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
	 *  根据ip46,协议tcp，udp查询带宽
	 */
	@RequestMapping(value = "trafficBandwidthTransFour", method = RequestMethod.GET)
	@ApiOperation(value = "带宽根据ip46,协议tcp,udp查询详情", httpMethod = "GET", notes = "对应带宽根据IPv4,6,协议tcp,udp统计数据显示")
	public Map<String,?> trafficBandwidthTransFour(String beginDate,String endDate,Model model, HttpServletRequest request, HttpServletResponse response) {
		String[] addrTypes=dashboardService.getFiledTypeByName("addr_type");
		String[] transTypes=dashboardService.getFiledTypeByName("trans_type");
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		Map<String, List<HashMap>> resultMap=new HashMap<String, List<HashMap>>();
		try {
			resultMap = dashboardService.getBandwidthTransEntrance(beginDate, endDate);
			
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
	public Map<String, ?> trafficProtocolList(String beginDate, String endDate, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			List<Map> ipActiveList = dashboardService.getProtocolList(beginDate, endDate);
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
	public Map<String, ?> trafficIpActiveOneHour(String beginDate, String endDate, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			Date begin = DateUtils.parseDate(beginDate);
			Date end = DateUtils.parseDate(endDate);
			List<HashMap> ipActiveChart = dashboardService.ipActiveOneHour(begin, end);
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
	public Map<String, ?> trafficIpActiveFiveMinute(String beginDate, String endDate, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			Date begin = null;
			Date end = null;
			if (!StringUtils.isEmpty(beginDate) && !StringUtils.isEmpty(endDate)) {
				begin = DateUtils.parseDate(beginDate);
				end = DateUtils.parseDate(endDate);
			}
			List<HashMap> ipActiveChart = dashboardService.ipActiveFiveMinute(begin, end);
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
	public Map<String, ?> trafficAppList(Integer appType, String beginDate, String endDate, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			List<Map> appList = dashboardService.getAppList(beginDate, endDate, appType);
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
	public Map<String, ?> trafficWebsiteListNoTime(Integer domain,Integer entranceId , @RequestParam String beginDate, @RequestParam String endDate,Model model, HttpServletRequest request,
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
		List<HashMap> list = new ArrayList<HashMap>();
		try {
			
				// 带查询时间查询所有
				List<HashMap> websiteChart = dashboardService.getDomainTrans(beginDate, endDate,domain,entranceId);
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
}
