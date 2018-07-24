package com.nis.web.controller.restful;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcAttrTypeReport;
import com.nis.domain.restful.NtcDestipCountryReport;
import com.nis.domain.restful.NtcEntranceReport;
import com.nis.domain.restful.NtcLwhhReport;
import com.nis.domain.restful.NtcPzReport;
import com.nis.domain.restful.NtcReportEntity;
import com.nis.domain.restful.NtcServiceReport;
import com.nis.domain.restful.NtcSrcipDomesticReport;
import com.nis.domain.restful.NtcTagReport;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.NtcReportService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * <p>Title: SingleDimensionReport</p>
 * <p>Description: 单维度统计查询报表controller</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年7月24日 
 *
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@Api(value = "SingleDimensionReport", description = "单维度统计查询报表controller")
public class SingleDimensionReport extends BaseRestController {
	
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	@Autowired
	protected NtcReportService ntcReportService;

	@RequestMapping(value = "/ntcPzReport", method = RequestMethod.GET)
	@ApiOperation(value = "配置日志总量统计", httpMethod = "GET", notes = "配置命中日志数量实时统计报表，对外提供多种数据表现形式，具体可应用于界面配置命中总量业务、配置报表业务等")
	public Map<String, ?> ntcPzReport(Page page, NtcPzReport ntcPzReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
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
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
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
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
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
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
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
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
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
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET,
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
	public Map<String, ?> ntcDestipCountryReport(Page page, NtcDestipCountryReport ntcDestipCountryReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcDestipCountryReport);
			// 验证实时报表
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcDestipCountryReport,
					NtcDestipCountryReport.class, page);
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcDestipCountryReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcDestipCountryReport(
					new Page<NtcDestipCountryReport>(request, response, NtcDestipCountryReport.class),
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
	public Map<String, ?> ntcEntranceReport(Page page, NtcEntranceReport ntcEntranceReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<?> reportPage = null;
		try {
			resetReportTime(ntcEntranceReport);
			ntcReportService.queryReportConditionCheck(saveLogThread, start, ntcEntranceReport, NtcEntranceReport.class,
					page);
			// 验证searchEntrance
			ntcReportService.checkNumericCondition(saveLogThread, start, ntcEntranceReport.getSearchEntrance(),
					"searchEntrance");
			String orderBy = "";
			if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
				orderBy = Page.getOrderBySql(NtcEntranceReport.class.getSimpleName(), page.getOrderBy());
			} else {
				orderBy = "report_time";
			}
			page.setOrderBy(orderBy);
			reportPage = ntcReportService.findNtcEntranceReport(
					new Page<NtcEntranceReport>(request, response, NtcEntranceReport.class), ntcEntranceReport);
		} catch (Exception e) {
			saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(saveLogThread, System.currentTimeMillis() - start, "运营商局点统计查询失败");
			}
			throw ((RestServiceException) e);
		}

		return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "运营商局点统计查询成功", reportPage,
				0);
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
}
