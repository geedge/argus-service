package com.nis.web.controller.restful;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.DfJitAffairDestReport;
import com.nis.domain.restful.DfJitAffairSrcReport;
import com.nis.domain.restful.DfJitFlDestReport;
import com.nis.domain.restful.DfJitFlSrcReport;
import com.nis.domain.restful.DfJitGuaranteeDestReport;
import com.nis.domain.restful.DfJitGuaranteeSrcReport;
import com.nis.domain.restful.DfJitIdDestReport;
import com.nis.domain.restful.DfJitIdSrcReport;
import com.nis.domain.restful.DfJitMissionDestReport;
import com.nis.domain.restful.DfJitMissionSrcReport;
import com.nis.domain.restful.DfJitTagDestReport;
import com.nis.domain.restful.DfJitTagSrcReport;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfJitLogSearchService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * @ClassName: DfJitLogSearchController
 * @Description: 管控日志实时统计查询服务
 * @author (rkg)
 * @date 2016年9月13日上午10:32:21
 * @version V1.0
 */

@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class DfJitLogSearchController extends BaseRestController {
	private final Logger logger1 = Logger.getLogger(this.getClass());
	protected String logSource="0";
	@Autowired
	private DfJitLogSearchService dfJitLogSearchService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "/dfJitFlSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控分类性质源IP实时统计", httpMethod = "GET", notes = "管控分类性质源IP实时统计列表")
	public Map dfJitFlSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitFlSrcReport dfJitFlSrcReportSources, HttpServletRequest request,
			HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfJitFlSrcReport> dfJitFlSrcReportSourcesPage = null;
		try {
			// 公共请求参数校验
			dfJitLogSearchService.queryDfJitFlCheck(thread, start, dfJitFlSrcReportSources.getSearchFl(),
					dfJitFlSrcReportSources.getSearchXz());
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitFlSrcReportSources, DfJitFlSrcReport.class,
					page);

			dfJitFlSrcReportSourcesPage = dfJitLogSearchService.findDfJitFlSrcReport(
					new Page<DfJitFlSrcReport>(request, response, DfJitFlSrcReport.class), dfJitFlSrcReportSources);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控分类性质源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控分类性质源IP实时统计检索成功",
				dfJitFlSrcReportSourcesPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitFlDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控分类性质目的IP实时统计服务", httpMethod = "GET", notes = "管控分类性质目的IP实时统计列表")
	public Map dfJitFlDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitFlDestReport dfJitFlDestReport, HttpServletRequest request, HttpServletResponse response) {
		
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfJitFlDestReport> reporPage = null;
		try {
			// 公共请求参数校验
			dfJitLogSearchService.queryDfJitFlCheck(thread, start, dfJitFlDestReport.getSearchFl(),
					dfJitFlDestReport.getSearchXz());
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitFlDestReport, DfJitFlDestReport.class, page);

			reporPage = dfJitLogSearchService.findDfJitFlDestReport(
					new Page<DfJitFlDestReport>(request, response, DfJitFlDestReport.class), dfJitFlDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控分类性质目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控分类性质目的IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitAffairSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控事件子话题源IP实时统计服务", httpMethod = "GET", notes = "管控事件子话题源IP实时统计列表")
	public Map dfJitAffairSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitAffairSrcReport dfJitAffairSrcReport, HttpServletRequest request,
			HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(dfJitAffairSrcReport.getSearchAffair())) {
				Integer.parseInt(dfJitAffairSrcReport.getSearchAffair());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchAffair参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchAffair参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		try {
			if (!StringUtil.isBlank(dfJitAffairSrcReport.getSearchTopic())) {
				Integer.parseInt(dfJitAffairSrcReport.getSearchTopic());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchTopic参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchTopic参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		Page<DfJitAffairSrcReport> reporPage = null;
		try {

			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitAffairSrcReport, DfJitAffairSrcReport.class,
					page);

			reporPage = dfJitLogSearchService.findDfJitAffairSrcReport(
					new Page<DfJitAffairSrcReport>(request, response, DfJitAffairSrcReport.class),
					dfJitAffairSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控事件子话题源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控事件子话题源IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitAffairDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控事件子话题目的IP实时统计服务", httpMethod = "GET", notes = "管控事件子话题目的IP实时统计列表")
	public Map dfJitAffairDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitAffairDestReport dfJitAffairDestReport, HttpServletRequest request,
			HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(dfJitAffairDestReport.getSearchAffair())) {
				Integer.parseInt(dfJitAffairDestReport.getSearchAffair());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchAffair参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchAffair参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		try {
			if (!StringUtil.isBlank(dfJitAffairDestReport.getSearchTopic())) {
				Integer.parseInt(dfJitAffairDestReport.getSearchTopic());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchTopic参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchTopic参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		Page<DfJitAffairDestReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitAffairDestReport, DfJitAffairDestReport.class,
					page);

			reporPage = dfJitLogSearchService.findDfJitAffairDestReport(
					new Page<DfJitAffairDestReport>(request, response, DfJitAffairDestReport.class),
					dfJitAffairDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控事件子话题目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控事件子话题目的IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitMissionSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控任务源IP实时统计服务", httpMethod = "GET", notes = "管控任务源IP实时统计列表")
	public Map dfJitMissionSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitMissionSrcReport dfJitMissionSrcReport, HttpServletRequest request,
			HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(dfJitMissionSrcReport.getSearchMission())) {
				Integer.parseInt(dfJitMissionSrcReport.getSearchMission());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchMission参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchMission参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DfJitMissionSrcReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitMissionSrcReport, DfJitMissionSrcReport.class,
					page);

			reporPage = dfJitLogSearchService.findDfJitMissionSrcReport(
					new Page<DfJitMissionSrcReport>(request, response, DfJitMissionSrcReport.class),
					dfJitMissionSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控任务源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控任务源IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitMissionDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控任务目的IP实时统计服务", httpMethod = "GET", notes = "管控任务目的IP实时统计列表")
	public Map dfJitMissionDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitMissionDestReport dfJitMissionDestReport, HttpServletRequest request,
			HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(dfJitMissionDestReport.getSearchMission())) {
				Integer.parseInt(dfJitMissionDestReport.getSearchMission());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchMission参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchMission参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DfJitMissionDestReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitMissionDestReport,
					DfJitMissionDestReport.class, page);
			reporPage = dfJitLogSearchService.findDfJitMissionDestReport(
					new Page<DfJitMissionDestReport>(request, response, DfJitMissionDestReport.class),
					dfJitMissionDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控任务目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控任务目的IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitGuaranteeSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控保障期源IP实时统计服务", httpMethod = "GET", notes = "管控保障期源IP实时统计列表")
	public Map dfJitGuaranteeSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitGuaranteeSrcReport dfJitGuaranteeSrcReport, HttpServletRequest request,
			HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(dfJitGuaranteeSrcReport.getSearchGuarantee())) {
				Integer.parseInt(dfJitGuaranteeSrcReport.getSearchGuarantee());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchGuarantee参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchGuarantee参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DfJitGuaranteeSrcReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitGuaranteeSrcReport,
					DfJitGuaranteeSrcReport.class, page);
			reporPage = dfJitLogSearchService.findDfJitGuaranteeSrcReport(
					new Page<DfJitGuaranteeSrcReport>(request, response, DfJitGuaranteeSrcReport.class),
					dfJitGuaranteeSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控保障期源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控保障期源IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitGuaranteeDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控保障期目的IP实时统计服务", httpMethod = "GET", notes = "管控保障期目的IP实时统计列表")
	public Map dfJitGuaranteeDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitGuaranteeDestReport dfJitGuaranteeDestReport, HttpServletRequest request,
			HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(dfJitGuaranteeDestReport.getSearchGuarantee())) {
				Integer.parseInt(dfJitGuaranteeDestReport.getSearchGuarantee());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchGuarantee参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchGuarantee参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DfJitGuaranteeDestReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitGuaranteeDestReport,
					DfJitGuaranteeDestReport.class, page);
			reporPage = dfJitLogSearchService.findDfJitGuaranteeDestReport(
					new Page<DfJitGuaranteeDestReport>(request, response, DfJitGuaranteeDestReport.class),
					dfJitGuaranteeDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控保障期目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控保障期目的IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitTagSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控标签源IP实时统计服务", httpMethod = "GET", notes = "管控标签源IP实时统计列表")
	public Map dfJitTagSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitTagSrcReport dfJitTagSrcReport, HttpServletRequest request, HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		if (null != dfJitTagSrcReport.getSearchTag() && !dfJitTagSrcReport.getSearchTag().equals("")) {
			String[] tagArr = dfJitTagSrcReport.getSearchTag().split(",");
			List<String> tagList = new ArrayList<String>();
			for (int i = 0; i < tagArr.length; i++) {
				if (!tagArr[i].equals("")) {
					try {
						Integer.parseInt(tagArr[i]);
						tagList.add(tagArr[i]);
					} catch (NumberFormatException e) {
						thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
						logger1.error(e);
						RestServiceException restError = new RestServiceException(thread,
								System.currentTimeMillis() - start, "searchTag参数格式错误",
								RestBusinessCode.param_formate_error.getValue());
						restError.setActiveSys(searchActiveSys);
						restError.setLogSource(logSource);
						throw restError;
					} catch (Exception e) {
						thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
						logger1.error(e);
						if (!(e instanceof RestServiceException)) {
							e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchTag参数错误");
						}
						((RestServiceException) e).setActiveSys(searchActiveSys);
						((RestServiceException) e).setLogSource(logSource);
						throw ((RestServiceException) e);
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			for (String str : tagList) {
				sb.append(str + ",");
			}
			String tag = sb.substring(0, sb.length() - 1);
			dfJitTagSrcReport.setSearchTag(tag);
		}

		Page<DfJitTagSrcReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitTagSrcReport, DfJitTagSrcReport.class, page);
			reporPage = dfJitLogSearchService.findDfJitTagSrcReport(
					new Page<DfJitTagSrcReport>(request, response, DfJitTagSrcReport.class), dfJitTagSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控标签源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控标签源IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitTagDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控标签目的IP实时统计服务", httpMethod = "GET", notes = "管控标签目的IP实时统计列表")
	public Map dfJitTagDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitTagDestReport dfJitTagDestReport, HttpServletRequest request,
			HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		if (null != dfJitTagDestReport.getSearchTag() && !dfJitTagDestReport.getSearchTag().equals("")) {
			String[] tagArr = dfJitTagDestReport.getSearchTag().split(",");
			List<String> tagList = new ArrayList<String>();
			for (int i = 0; i < tagArr.length; i++) {
				if (!tagArr[i].equals("")) {
					try {
						Integer.parseInt(tagArr[i]);
						tagList.add(tagArr[i]);
					} catch (NumberFormatException e) {
						thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
						logger1.error(e);
						RestServiceException restError = new RestServiceException(thread,
								System.currentTimeMillis() - start, "searchTag参数格式错误",
								RestBusinessCode.param_formate_error.getValue());
						restError.setActiveSys(searchActiveSys);
						restError.setLogSource(logSource);
						throw restError;
					} catch (Exception e) {
						thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
						logger1.error(e);
						if (!(e instanceof RestServiceException)) {
							e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchTag参数错误");
						}
						((RestServiceException) e).setActiveSys(searchActiveSys);
						((RestServiceException) e).setLogSource(logSource);
						throw ((RestServiceException) e);
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			for (String str : tagList) {
				sb.append(str + ",");
			}
			String tag = sb.substring(0, sb.length() - 1);
			dfJitTagDestReport.setSearchTag(tag);
		}

		Page<DfJitTagDestReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitTagDestReport, DfJitTagDestReport.class,
					page);
			reporPage = dfJitLogSearchService.findDfJitTagDestReport(
					new Page<DfJitTagDestReport>(request, response, DfJitTagDestReport.class), dfJitTagDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控标签目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控标签目的IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitIdSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控规则源IP实时统计服务", httpMethod = "GET", notes = "管控规则源IP实时统计列表")
	public Map dfJitIdSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitIdSrcReport dfJitIdSrcReport, HttpServletRequest request, HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(dfJitIdSrcReport.getSearchId())) {
				Integer.parseInt(dfJitIdSrcReport.getSearchId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchId参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DfJitIdSrcReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitIdSrcReport, DfJitIdSrcReport.class, page);
			reporPage = dfJitLogSearchService.findDfJitIdSrcReport(
					new Page<DfJitIdSrcReport>(request, response, DfJitIdSrcReport.class), dfJitIdSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控规则源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控规则源IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}

	@RequestMapping(value = "/dfJitIdDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控规则目的IP实时统计服务", httpMethod = "GET", notes = "管控规则目的IP实时统计列表")
	public Map dfJitIdDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfJitIdDestReport dfJitIdDestReport, HttpServletRequest request, HttpServletResponse response) {
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(dfJitIdDestReport.getSearchId())) {
				Integer.parseInt(dfJitIdDestReport.getSearchId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchId参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DfJitIdDestReport> reporPage = null;
		try {
			dfJitLogSearchService.queryConditionCheck(thread, start, dfJitIdDestReport, DfJitIdDestReport.class, page);
			reporPage = dfJitLogSearchService.findDfJitIdDestReport(
					new Page<DfJitIdDestReport>(request, response, DfJitIdDestReport.class), dfJitIdDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控规则目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控规则目的IP实时统计检索成功",
				reporPage, searchActiveSys,logSource);
	}
}
