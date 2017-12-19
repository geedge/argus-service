package com.nis.web.controller.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.nis.domain.restful.DjCkStatLog;
import com.nis.domain.restful.DjJitAffairDestReport;
import com.nis.domain.restful.DjJitAffairSrcReport;
import com.nis.domain.restful.DjJitFlDestReport;
import com.nis.domain.restful.DjJitFlSrcReport;
import com.nis.domain.restful.DjJitGuaranteeDestReport;
import com.nis.domain.restful.DjJitGuaranteeSrcReport;
import com.nis.domain.restful.DjJitIdDestReport;
import com.nis.domain.restful.DjJitIdSrcReport;
import com.nis.domain.restful.DjJitMissionDestReport;
import com.nis.domain.restful.DjJitMissionSrcReport;
import com.nis.domain.restful.DjJitTagDestReport;
import com.nis.domain.restful.DjJitTagSrcReport;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DjJitLogSearchService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * @ClassName: DjJitLogSearchController
 * @Description: 监测日志实时统计查询服务
 * @author (rkg)
 * @date 2016年9月13日上午10:32:21
 * @version V1.0
 */

@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class DjJitLogSearchController extends BaseRestController {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final Logger logger1 = Logger.getLogger(this.getClass());
	protected String logSource = "0";
	@Autowired
	private DjJitLogSearchService djJitLogSearchService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "/djJitFlSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测分类性质源IP实时统计", httpMethod = "GET", notes = "监测分类性质源IP实时统计列表")
	public Map djJitFlSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitFlSrcReport djJitFlSrcReportSources, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DjJitFlSrcReport> djJitFlSrcReportSourcesPage = null;
		try {
			// 公共请求参数校验
			djJitLogSearchService.queryDjJitFlCheck(thread, start, djJitFlSrcReportSources.getSearchFl(),
					djJitFlSrcReportSources.getSearchXz());
			djJitLogSearchService.queryConditionCheck(thread, start, djJitFlSrcReportSources, DjJitFlSrcReport.class,
					page);
			djJitFlSrcReportSourcesPage = djJitLogSearchService.findDjJitFlSrcReport(
					new Page<DjJitFlSrcReport>(request, response, DjJitFlSrcReport.class), djJitFlSrcReportSources);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测分类性质源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测分类性质源IP实时统计检索成功",
				djJitFlSrcReportSourcesPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitFlDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测分类性质目的IP实时统计服务", httpMethod = "GET", notes = "监测分类性质目的IP实时统计列表")
	public Map djJitFlDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitFlDestReport djJitFlDestReport, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DjJitFlDestReport> reporPage = null;
		try {
			// 公共请求参数校验
			djJitLogSearchService.queryDjJitFlCheck(thread, start, djJitFlDestReport.getSearchFl(),
					djJitFlDestReport.getSearchXz());
			djJitLogSearchService.queryConditionCheck(thread, start, djJitFlDestReport, DjJitFlDestReport.class, page);
			reporPage = djJitLogSearchService.findDjJitFlDestReport(
					new Page<DjJitFlDestReport>(request, response, DjJitFlDestReport.class), djJitFlDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测分类性质目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测分类性质目的IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitAffairSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测事件子话题源IP实时统计服务", httpMethod = "GET", notes = "监测事件子话题源IP实时统计列表")
	public Map djJitAffairSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitAffairSrcReport djJitAffairSrcReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djJitAffairSrcReport.getSearchAffair())) {
				Integer.parseInt(djJitAffairSrcReport.getSearchAffair());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchAffair参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchAffair参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		try {
			if (!StringUtil.isBlank(djJitAffairSrcReport.getSearchTopic())) {
				Integer.parseInt(djJitAffairSrcReport.getSearchTopic());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchTopic参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchTopic参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DjJitAffairSrcReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitAffairSrcReport, DjJitAffairSrcReport.class,
					page);
			reporPage = djJitLogSearchService.findDjJitAffairSrcReport(
					new Page<DjJitAffairSrcReport>(request, response, DjJitAffairSrcReport.class),
					djJitAffairSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测事件子话题源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测事件子话题源IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitAffairDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测事件子话题目的IP实时统计服务", httpMethod = "GET", notes = "监测事件子话题目的IP实时统计列表")
	public Map djJitAffairDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitAffairDestReport djJitAffairDestReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djJitAffairDestReport.getSearchAffair())) {
				Integer.parseInt(djJitAffairDestReport.getSearchAffair());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchAffair参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchAffair参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		try {
			if (!StringUtil.isBlank(djJitAffairDestReport.getSearchTopic())) {
				Integer.parseInt(djJitAffairDestReport.getSearchTopic());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchTopic参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchTopic参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		Page<DjJitAffairDestReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitAffairDestReport, DjJitAffairDestReport.class,
					page);
			reporPage = djJitLogSearchService.findDjJitAffairDestReport(
					new Page<DjJitAffairDestReport>(request, response, DjJitAffairDestReport.class),
					djJitAffairDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测事件子话题目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测事件子话题目的IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitMissionSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测任务源IP实时统计服务", httpMethod = "GET", notes = "监测任务源IP实时统计列表")
	public Map djJitMissionSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitMissionSrcReport djJitMissionSrcReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djJitMissionSrcReport.getSearchMission())) {
				Integer.parseInt(djJitMissionSrcReport.getSearchMission());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchMission参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchMission参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DjJitMissionSrcReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitMissionSrcReport, DjJitMissionSrcReport.class,
					page);
			reporPage = djJitLogSearchService.findDjJitMissionSrcReport(
					new Page<DjJitMissionSrcReport>(request, response, DjJitMissionSrcReport.class),
					djJitMissionSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测任务源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测任务源IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitMissionDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测任务目的IP实时统计服务", httpMethod = "GET", notes = "监测任务目的IP实时统计列表")
	public Map djJitMissionDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitMissionDestReport djJitMissionDestReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djJitMissionDestReport.getSearchMission())) {
				Integer.parseInt(djJitMissionDestReport.getSearchMission());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchMission参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchMission参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DjJitMissionDestReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitMissionDestReport,
					DjJitMissionDestReport.class, page);
			reporPage = djJitLogSearchService.findDjJitMissionDestReport(
					new Page<DjJitMissionDestReport>(request, response, DjJitMissionDestReport.class),
					djJitMissionDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测任务目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测任务目的IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitGuaranteeSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测保障期源IP实时统计服务", httpMethod = "GET", notes = "监测保障期源IP实时统计列表")
	public Map djJitGuaranteeSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitGuaranteeSrcReport djJitGuaranteeSrcReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djJitGuaranteeSrcReport.getSearchGuarantee())) {
				Integer.parseInt(djJitGuaranteeSrcReport.getSearchGuarantee());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchGuarantee参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchGuarantee参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DjJitGuaranteeSrcReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitGuaranteeSrcReport,
					DjJitGuaranteeSrcReport.class, page);
			reporPage = djJitLogSearchService.findDjJitGuaranteeSrcReport(
					new Page<DjJitGuaranteeSrcReport>(request, response, DjJitGuaranteeSrcReport.class),
					djJitGuaranteeSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测保障期源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测保障期源IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitGuaranteeDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测保障期目的IP实时统计服务", httpMethod = "GET", notes = "监测保障期目的IP实时统计列表")
	public Map djJitGuaranteeDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitGuaranteeDestReport djJitGuaranteeDestReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djJitGuaranteeDestReport.getSearchGuarantee())) {
				Integer.parseInt(djJitGuaranteeDestReport.getSearchGuarantee());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchGuarantee参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchGuarantee参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DjJitGuaranteeDestReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitGuaranteeDestReport,
					DjJitGuaranteeDestReport.class, page);
			reporPage = djJitLogSearchService.findDjJitGuaranteeDestReport(
					new Page<DjJitGuaranteeDestReport>(request, response, DjJitGuaranteeDestReport.class),
					djJitGuaranteeDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测保障期目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测保障期目的IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitTagSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测标签源IP实时统计服务", httpMethod = "GET", notes = "监测标签源IP实时统计列表")
	public Map djJitTagSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitTagSrcReport djJitTagSrcReport, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		if (null != djJitTagSrcReport.getSearchTag() && !djJitTagSrcReport.getSearchTag().equals("")) {
			String[] tagArr = djJitTagSrcReport.getSearchTag().split(",");
			List<String> tagList = new ArrayList<String>();
			for (int i = 0; i < tagArr.length; i++) {
				if (!tagArr[i].equals("")) {
					try {
						Integer.parseInt(tagArr[i]);
						tagList.add(tagArr[i]);
					} catch (NumberFormatException e) {
						thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
						logger1.error(e);
						RestServiceException restError = new RestServiceException(thread,
								System.currentTimeMillis() - start, "searchTag参数格式错误",
								RestBusinessCode.param_formate_error.getValue());
						restError.setActiveSys(searchActiveSys);
						restError.setLogSource(logSource);
						throw restError;
					} catch (Exception e) {
						thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
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
			djJitTagSrcReport.setSearchTag(tag);
		}

		Page<DjJitTagSrcReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitTagSrcReport, DjJitTagSrcReport.class, page);
			reporPage = djJitLogSearchService.findDjJitTagSrcReport(
					new Page<DjJitTagSrcReport>(request, response, DjJitTagSrcReport.class), djJitTagSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测标签源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测标签源IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitTagDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测标签目的IP实时统计服务", httpMethod = "GET", notes = "监测标签目的IP实时统计列表")
	public Map djJitTagDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitTagDestReport djJitTagDestReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		if (null != djJitTagDestReport.getSearchTag() && !djJitTagDestReport.getSearchTag().equals("")) {
			String[] tagArr = djJitTagDestReport.getSearchTag().split(",");
			List<String> tagList = new ArrayList<String>();
			for (int i = 0; i < tagArr.length; i++) {
				if (!tagArr[i].equals("")) {
					try {
						Integer.parseInt(tagArr[i]);
						tagList.add(tagArr[i]);
					} catch (NumberFormatException e) {
						thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
						logger1.error(e);
						RestServiceException restError = new RestServiceException(thread,
								System.currentTimeMillis() - start, "searchTag参数格式错误",
								RestBusinessCode.param_formate_error.getValue());
						restError.setActiveSys(searchActiveSys);
						restError.setLogSource(logSource);
						throw restError;
					} catch (Exception e) {
						thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
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
			djJitTagDestReport.setSearchTag(tag);
		}
		Page<DjJitTagDestReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitTagDestReport, DjJitTagDestReport.class,
					page);
			reporPage = djJitLogSearchService.findDjJitTagDestReport(
					new Page<DjJitTagDestReport>(request, response, DjJitTagDestReport.class), djJitTagDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测标签目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测标签目的IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitIdSrcReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测规则源IP实时统计服务", httpMethod = "GET", notes = "监测规则源IP实时统计列表")
	public Map djJitIdSrcReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitIdSrcReport djJitIdSrcReport, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djJitIdSrcReport.getSearchId())) {
				Integer.parseInt(djJitIdSrcReport.getSearchId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchId参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DjJitIdSrcReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitIdSrcReport, DjJitIdSrcReport.class, page);
			reporPage = djJitLogSearchService.findDjJitIdSrcReport(
					new Page<DjJitIdSrcReport>(request, response, DjJitIdSrcReport.class), djJitIdSrcReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测规则源IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测规则源IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djJitIdDestReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测规则目的IP实时统计服务", httpMethod = "GET", notes = "监测规则目的IP实时统计列表")
	public Map djJitIdDestReportSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjJitIdDestReport djJitIdDestReport, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djJitIdDestReport.getSearchId())) {
				Integer.parseInt(djJitIdDestReport.getSearchId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchId参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DjJitIdDestReport> reporPage = null;
		try {
			djJitLogSearchService.queryConditionCheck(thread, start, djJitIdDestReport, DjJitIdDestReport.class, page);
			reporPage = djJitLogSearchService.findDjJitIdDestReport(
					new Page<DjJitIdDestReport>(request, response, DjJitIdDestReport.class), djJitIdDestReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测规则目的IP实时统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测规则目的IP实时统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djCkStatLogSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测日志国际出入口统计服务", httpMethod = "GET", notes = "监测日志国际出入口统计列表")
	public Map djCkStatLogSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjCkStatLog djCkStatLog, HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		// 公共请求参数校验
		try {
			if (!StringUtil.isBlank(djCkStatLog.getSearchCfgId())) {
				Long.parseLong(djCkStatLog.getSearchCfgId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchCfgId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchId参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		try {
			if (!StringUtil.isBlank(djCkStatLog.getSearchGjCkId())) {
				Long.parseLong(djCkStatLog.getSearchGjCkId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchGjCkId参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchGjCkId参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		try {
			if (!StringUtil.isBlank(djCkStatLog.getSearchActiveSys())) {
				Integer.parseInt(djCkStatLog.getSearchActiveSys());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchActiveSys参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchGjCkId参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		try {
			if (!StringUtil.isBlank(djCkStatLog.getSearchCapStartTime())) {
				sdf.parse(djCkStatLog.getSearchCapStartTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchCapStartTime参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchCapStartTime参数格式格式");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		try {
			if (!StringUtil.isBlank(djCkStatLog.getSearchCapEndTime())) {
				sdf.parse(djCkStatLog.getSearchCapEndTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchCapEndTime参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchCapEndTime参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			throw ((RestServiceException) e);
		}

		try {
			if (!StringUtil.isBlank(djCkStatLog.getSearchService())) {
				Integer.parseInt(djCkStatLog.getSearchService());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			RestServiceException restError = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchService参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restError.setActiveSys(searchActiveSys);
			restError.setLogSource(logSource);
			throw restError;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		try {
			djJitLogSearchService.checkCloumnIsExist(thread, start, DjCkStatLog.class, page);
		} catch (RestServiceException e) {
			logger1.error(e);
			e.setActiveSys(searchActiveSys);
			e.setLogSource(logSource);
			throw e;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "请求参数错误");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		Page<DjCkStatLog> reporPage = null;
		try {
			reporPage = djJitLogSearchService
					.findDjCkStatLog(new Page<DjCkStatLog>(request, response, DjCkStatLog.class), djCkStatLog);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测日志国际出入口统计检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测日志国际出入口统计检索成功",
				reporPage, searchActiveSys, logSource);
	}

}
