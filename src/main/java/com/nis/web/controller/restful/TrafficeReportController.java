package com.nis.web.controller.restful;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import com.nis.domain.Page;
import com.nis.domain.restful.NtcRadiusReport;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.ExceptionUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.TrafficReportService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zdjizhi.utils.StringUtil;

/**
 * 
 * <p>
 * Title: SingleDimensionReport
 * </p>
 * <p>
 * Description:首页流量统计查询controller,改为从clickhouse中获取
 * </p>
 * <p>
 * Company: IIE
 * </p>
 * 
 * @author rkg
 * @date 2018年12月20日
 *
 */
@RestController
@RequestMapping("${servicePath}/log/v2")
@Api(value = "TrafficeReportController", description = "流量统计查询报表controller")
public class TrafficeReportController extends BaseRestController {

	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@Autowired
	protected TrafficReportService trafficReportService;

	@RequestMapping(value = "/ntcRadiusReport", method = RequestMethod.GET)
	@ApiOperation(value = "用户行为统计查询服务", httpMethod = "GET", notes = "用户行为统计查询服务，基于用户名或接入IP维度聚合")
	public Map<String, ?> ntcRadiusReport(Page page, NtcRadiusReport ntcRadiusReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetReportTime(ntcRadiusReport, true);
			trafficReportService.checkNtcRadiusReportCondition(saveLogThread, start, ntcRadiusReport);
			trafficReportService.findNtcRadiusReport(page, ntcRadiusReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo("用户行为统计查询失败:" + e.getMessage());
			logger.error("用户行为统计查询失败:" + ExceptionUtil.getExceptionMsg(e));
			if (e instanceof RestServiceException) {
				throw new RestServiceException(saveLogThread, System.currentTimeMillis() - start,
						"用户行为统计查询失败:" + e.getMessage(), ((RestServiceException) e).getErrorCode());
			} else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
						"用户行为统计查询失败:" + e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			} else {
				throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
						"用户行为统计查询失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
			}
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "用户行为统计查询成功", page, 0);
	}

	/**
	 * 根据ip46,协议tcp，udp查询带宽
	 */
	@RequestMapping(value = "trafficBandwidthTransThree", method = RequestMethod.GET)
	@ApiOperation(value = "带宽根据ip46,协议tcp,udp查询详情", httpMethod = "GET", notes = "对应带宽根据IPv4,6,协议tcp,udp统计数据显示")
	public Map<String, ?> trafficBandwidthTransThree(@RequestParam String beginDate, @RequestParam String endDate,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		String[] addrTypes = { "4", "6" };// ipv4,ipv6是固定的不会变
		String[] transTypes = { "6", "17" };// 传输层协议,tcp,udp
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Map<String, List<HashMap>> resultMap = new HashMap<String, List<HashMap>>();
		try {
			if (StringUtils.isEmpty(beginDate) && StringUtils.isEmpty(endDate)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());// 获取到完整的时间
				cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
				beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
			}
			String addrType = null;
			Integer transType = null;
			if (addrTypes != null) {
				for (int i = 0; i < addrTypes.length; i++) {
					addrType = addrTypes[i];
					List<HashMap> resultList = trafficReportService.getBandwidthTrans(addrType, transType, beginDate,
							endDate);
					if (resultList != null && resultList.size() > 0) {
						resultMap.put("ipv" + addrType + "Type1", resultList);
					}

					resultList = trafficReportService.getBandwidthTrans2(addrType, transType, beginDate, endDate);
					if (resultList != null && resultList.size() > 0) {
						resultMap.put("ipv" + addrType + "Type2", resultList);
					}
					addrType = null;
				}
			}
			if (transTypes != null) {
				for (int i = 0; i < transTypes.length; i++) {
					transType = Integer.valueOf(transTypes[i]);
					List<HashMap> resultList = trafficReportService.getBandwidthTrans(addrType, transType, beginDate,
							endDate);
					if (resultList != null && resultList.size() > 0) {
						resultMap.put("trans" + transType + "Type1", resultList);
					}
					resultList = trafficReportService.getBandwidthTrans2(addrType, transType, beginDate, endDate);
					if (resultList != null && resultList.size() > 0) {
						resultMap.put("trans" + transType + "Type2", resultList);
					}
					transType = null;
				}
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
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "带宽实时统计数据检索成功",
				resultMap, 0);
	}

	/**
	 * @Description:
	 * @author (zdx)
	 * @date 2018年7月12日 上午11:01:30
	 * @param entity
	 * @throws Exception
	 */
	public void resetReportTime(NtcRadiusReport entity, Boolean isTotal) throws Exception {
		// 日志总量统计查询全部
//		if ("1".equals(entity.getSearchBusinessType()) && isTotal) {
//			return;
//		}
		if (StringUtil.isEmpty(entity.getSearchReportStartTime())
				&& StringUtil.isEmpty(entity.getSearchReportEndTime())) {
			String dateType = "hour"; // 默认为分钟
			if (!isTotal) {
				if (entity.getSearchBusinessType().equals("2")) {
					dateType = "hour";
				} else if (entity.getSearchBusinessType().equals("3")) {
					dateType = "daily";
				} else if (entity.getSearchBusinessType().equals("4")) {
					dateType = "month";
				}
			} else {
				if (entity.getSearchBusinessType().equals("3")) {
					dateType = "hour";
				} else if (entity.getSearchBusinessType().equals("4")) {
					dateType = "daily";
				} else if (entity.getSearchBusinessType().equals("5")) {
					dateType = "month";
				}
			}
			Map<String, String> map = DateUtils.getLocalTime(entity.getSearchReportStartTime(),
					entity.getSearchReportEndTime(), Constants.PZ_REPORT_TIME, dateType);
			entity.setSearchReportStartTime(map.get("startTime"));
			entity.setSearchReportEndTime(map.get("endTime"));
		}
	}
}
