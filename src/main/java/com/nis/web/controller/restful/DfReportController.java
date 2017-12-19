package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.DfReportEntity;
import com.nis.domain.LogEntity;
import com.nis.domain.Page;
import com.nis.domain.restful.DfAttrTypeReport;
import com.nis.domain.restful.DfDestIpCountryReport;
import com.nis.domain.restful.DfDestIpReport;
import com.nis.domain.restful.DfEntranceReport;
import com.nis.domain.restful.DfLwhhReport;
import com.nis.domain.restful.DfPzReport;
import com.nis.domain.restful.DfPzReportStat;
import com.nis.domain.restful.DfServiceReport;
import com.nis.domain.restful.DfSrcIpDomeSticReport;
import com.nis.domain.restful.DfSrcIpReport;
import com.nis.domain.restful.DfTagReport;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfReportService;
import com.wordnik.swagger.annotations.ApiOperation;

import net.sf.json.JSONObject;

/**
 * @ClassName: DfReportController
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2016年10月31日上午11:56:50
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class DfReportController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());

	protected String activeSys = Constants.ACTIVESYS_ALL;
	protected String logSource = "0";
	@Autowired
	protected DfReportService dfReportService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "dfPzReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控配置实时统计服务", httpMethod = "GET", notes = "get log list")
	public Map dfPzReportSources(Page page, DfPzReport dfPzReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfPzReport> pzReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfPzReport);
			
			dfReportService.queryConditionCheck(thread, start, dfPzReport, DfPzReport.class, page);

			// 查询数据库
			pzReportPage = dfReportService.findDfPzReportPage(new Page<DfPzReport>(request, response, DfPzReport.class),
					dfPzReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控配置实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控配置实时统计成功",
				pzReportPage, activeSys, logSource);
	}

	/**
	 * 配置统计[小时]
	 * 
	 * @param page
	 * @param dfPzReport
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dfPzStatSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控各配置量统计服务[小时]", httpMethod = "GET", notes = "get log list")
	public Map dfPzStatSources(Page page, DfPzReportStat dfPzReportStat, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if (!Constants.ACTIVESYS_A.equals(dfPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_C.equals(dfPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_AB.equals(dfPzReportStat.getSearchStatActiveSys())
			) {
			dfPzReportStat.setSearchStatActiveSys(Constants.ACTIVESYS_B);
		}
		activeSys = dfPzReportStat.getSearchStatActiveSys();
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfPzReportStat> pzReportPage = null;
		try {
			
			//设置本地默认查询时间
			resetTime(dfPzReportStat);
			
			dfReportService.queryConditionCheck(thread, start, dfPzReportStat, DfPzReportStat.class, page);

			// 查询数据库
			pzReportPage = dfReportService.findDfPzReportStatPage(
					new Page<DfPzReportStat>(request, response, DfPzReportStat.class), dfPzReportStat);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控各配置量统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控各配置量统计成功",
				pzReportPage, activeSys, logSource);
	}
	/**
	 * 配置统计[分钟]
	 * 
	 * @param page
	 * @param dfPzReport
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dfPzStatMinutesSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控各配置量统计服务[分钟]", httpMethod = "GET", notes = "get log list")
	public Map dfPzStatMinutesSources(Page page, DfPzReportStat dfPzReportStat, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if (!Constants.ACTIVESYS_A.equals(dfPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_C.equals(dfPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_AB.equals(dfPzReportStat.getSearchStatActiveSys())
				) {
			dfPzReportStat.setSearchStatActiveSys(Constants.ACTIVESYS_B);
		}
		activeSys = dfPzReportStat.getSearchStatActiveSys();
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		
		Page<DfPzReportStat> pzReportPage = null;
		try {
			
			//设置本地默认查询时间
			resetTime(dfPzReportStat);
			
			dfReportService.queryConditionCheck(thread, start, dfPzReportStat, DfPzReportStat.class, page);
			
			// 查询数据库
			pzReportPage = dfReportService.findDfPzReportStatMinutesPage(
					new Page<DfPzReportStat>(request, response, DfPzReportStat.class), dfPzReportStat);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控各配置量统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控各配置量统计成功",
				pzReportPage, activeSys, logSource);
	}

	/**
	 * 配置统计
	 * 
	 * @param page
	 * @param dfPzReport
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dfPzSumStatSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控配置总量统计服务", httpMethod = "GET", notes = "get log list")
	public Map dfPzSumStatSources(Page page, DfPzReportStat dfPzReportStat, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if (!Constants.ACTIVESYS_A.equals(dfPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_C.equals(dfPzReportStat.getSearchStatActiveSys())
				) {
			dfPzReportStat.setSearchStatActiveSys(Constants.ACTIVESYS_B);
		}
		activeSys = dfPzReportStat.getSearchStatActiveSys();
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		JSONObject json = new JSONObject();
		try {
			
			//设置本地默认查询时间
			resetTime(dfPzReportStat);
			
			dfReportService.queryConditionCheck(thread, start, dfPzReportStat, DfPzReportStat.class,page);

			// 查询数据库
			Long sum = dfReportService.findDfPzReportSumStat(
					new Page<DfPzReportStat>(request, response, DfPzReportStat.class), dfPzReportStat);
			if (sum == null)
				sum = 0l;
			json.put("sum", sum);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控配置总量统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控配置总量统计成功", json,
				activeSys, logSource);
	}

	@RequestMapping(value = "dfServiceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控业务类型实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfServiceReportSources(Page page, DfServiceReport dfServiceReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfServiceReport> serviceReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfServiceReport);
			
			// 校验
			dfReportService.queryConditionCheck(thread, start, dfServiceReport, DfServiceReport.class,
					page);
			// 查询数据库
			serviceReportPage = dfReportService.findDfServiceReportPage(
					new Page<DfServiceReport>(request, response, DfServiceReport.class), dfServiceReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控业务类型实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控业务类型实时统计成功",
				serviceReportPage, activeSys, logSource);
	}

	@RequestMapping(value = "dfTagReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控标签实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfTagReportSources(Page page, DfTagReport dfTagReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfTagReport> tagReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfTagReport);
			
			// 校验
			dfReportService.queryConditionCheck(thread, start, dfTagReport, DfTagReport.class, page);
			// 查询数据库
			tagReportPage = dfReportService
					.findDfTagReportPage(new Page<DfTagReport>(request, response, DfTagReport.class), dfTagReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控标签实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控标签实时统计成功",
				tagReportPage, activeSys, logSource);
	}

	@RequestMapping(value = "/dfSrcIpDomesticReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控境内源IP实时统计服务", httpMethod = "GET", notes = "管控境内源IP实时统计列表")
	public Map dfSrcIpDomesticReportSources(Page page, DfSrcIpReport dfSrcIpDomesticReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfSrcIpReport> reporPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfSrcIpDomesticReport);
			
			dfReportService.queryConditionCheck(thread, start, dfSrcIpDomesticReport, DfSrcIpReport.class, page);

			reporPage = dfReportService.findDfSrcIpReport(
					new Page<DfSrcIpReport>(request, response, DfSrcIpReport.class), dfSrcIpDomesticReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控境内源IP实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控境内源IP实时统计检索成功",
				reporPage, activeSys, logSource);
	}

	@RequestMapping(value = "/dfDestIpCountryReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控各国家目的IP实时统计服务", httpMethod = "GET", notes = "管控各国家目的IP实时统计列表")
	public Map dfDestIpCountryReportSources(Page page, DfDestIpReport dfDestIpReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfDestIpReport> reporPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfDestIpReport);
			
			dfReportService.queryConditionCheck(thread, start, dfDestIpReport, DfDestIpReport.class, page);

			reporPage = dfReportService.findDfDestIpReport(
					new Page<DfDestIpReport>(request, response, DfDestIpReport.class), dfDestIpReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控各国家目的IP实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控各国家目的IP实时统计检索成功",
				reporPage, activeSys, logSource);
	}

	@RequestMapping(value = "dfPzPrivServiceSumSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控配置总量实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfPzPrivServiceSumSources(Page page, DfPzReport dfPzReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfPzReport> pzReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfPzReport);
			
			dfReportService.queryConditionCheck(thread, start, dfPzReport, DfPzReport.class, page);

			// 查询数据库
			pzReportPage = dfReportService.findDfPriTagReport(new Page<DfPzReport>(request, response, DfPzReport.class),
					dfPzReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控配置总量实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控配置总量实时统计成功",
				pzReportPage, activeSys, logSource);
	}

	@RequestMapping(value = "dfAttrTypeReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控性质（带私有标签）实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfAttrTypeReportSources(Page page, DfAttrTypeReport dfAttrTypeReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DfAttrTypeReport> attrTypeReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfAttrTypeReport);
			
			dfReportService.queryConditionCheck(thread, start, dfAttrTypeReport, DfPzReport.class, page);
			// 查询数据库
			attrTypeReportPage = dfReportService.findAttrTypeReport(
					new Page<DfAttrTypeReport>(request, response, DfAttrTypeReport.class), dfAttrTypeReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控性质（带私有标签）实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控性质（带私有标签）实时统计成功",
				attrTypeReportPage, activeSys, logSource);
	}

	@RequestMapping(value = "dfSrcIpDomesticPrivServiceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控境内源IP(带私有标签)实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfSrcIpDomesticPrivServiceReportSources(Page page, DfSrcIpDomeSticReport dfSrcIpDomeSticReport,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DfSrcIpDomeSticReport> srcIpDomeSticReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfSrcIpDomeSticReport);
			
			dfReportService.queryConditionCheck(thread, start, dfSrcIpDomeSticReport, DfSrcIpDomeSticReport.class,
					page);
			// 查询数据库
			srcIpDomeSticReportPage = dfReportService.findSrcIpDomeSticReport(
					new Page<DfSrcIpDomeSticReport>(request, response, DfSrcIpDomeSticReport.class),
					dfSrcIpDomeSticReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控境内源IP(带私有标签)实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控境内源IP(带私有标签)实时统计成功",
				srcIpDomeSticReportPage, activeSys, logSource);
	}

	@RequestMapping(value = "dfDestIpCountryPrivServiceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控境内目的IP(带私有标签)实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfDestIpCountryPrivServiceReportSources(Page page, DfDestIpCountryReport dfDestIpCountryReport,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DfDestIpCountryReport> dfDestIpCountryReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfDestIpCountryReport);
			
			dfReportService.queryConditionCheck(thread, start, dfDestIpCountryReport, DfDestIpCountryReport.class,
					page);
			// 查询数据库
			dfDestIpCountryReportPage = dfReportService.findDestIpCountryReport(
					new Page<DfDestIpCountryReport>(request, response, DfDestIpCountryReport.class),
					dfDestIpCountryReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控境内目的IP(带私有标签)实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控境内目的IP(带私有标签)实时统计成功",
				dfDestIpCountryReportPage, activeSys, logSource);
	}

	@RequestMapping(value = "dfEntranceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控局点（带私有标签）实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfEntranceReportSources(Page page, DfEntranceReport dfEntranceReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DfEntranceReport> dfEntranceReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfEntranceReport);
			
			dfReportService.queryConditionCheck(thread, start, dfEntranceReport, DfEntranceReport.class,
					page);
			// 查询数据库
			dfEntranceReportPage = dfReportService.findDfEntranceReport(
					new Page<DfEntranceReport>(request, response, DfEntranceReport.class), dfEntranceReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控局点（带私有标签）实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控局点（带私有标签）实时统计成功",
				dfEntranceReportPage, activeSys, logSource);
	}

	@RequestMapping(value = "dfLwhhReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控来文函号（带私有标签）实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfLwhhReportSources(Page page, DfLwhhReport dfLwhhReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DfLwhhReport> dfLwhhReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfLwhhReport);
			
			dfReportService.queryConditionCheck(thread, start, dfLwhhReport, DfLwhhReport.class, page);
			// 查询数据库
			dfLwhhReportPage = dfReportService
					.findDfLwhhReport(new Page<DfLwhhReport>(request, response, DfLwhhReport.class), dfLwhhReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控来文函号（带私有标签）实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控来文函号（带私有标签）实时统计成功",
				dfLwhhReportPage, activeSys, logSource);
	}
	
	
	
	@RequestMapping(value = "dfTagPrivServiceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "管控标签（带私有标签）实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfTagPrivServiceReportSources(Page page, DfTagReport dfTagReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DfTagReport> dfTagReportPage = null;
		try {
			
			//设置本地默认查询时间
			resetTime(dfTagReport);
			
			dfReportService.queryConditionCheck(thread, start, dfTagReport, DfTagReport.class, page);
			// 查询数据库
			dfTagReportPage = dfReportService
					.findDfTagReportPage(new Page<DfTagReport>(request, response, DfTagReport.class), dfTagReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "管控标签（带私有标签）实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "管控标签（带私有标签）实时统计成功",
				dfTagReportPage, activeSys, logSource);
	}
	
	/**
	 * 
	 * @Title: resetTime
	 * @Description: (实时统计默认查询本地一个小时的数据)
	 * @param @param entity
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public void resetTime(DfReportEntity<?> entity) throws Exception {
		Map<String, String> map = DateUtils.getLocalTime(entity.getSearchReportStartTime(),
				entity.getSearchReportEndTime(), Constants.REPORT_LOCAL_TIME,"report");
		entity.setSearchReportStartTime(map.get("startTime"));
		entity.setSearchReportEndTime(map.get("endTime"));
	}
}
