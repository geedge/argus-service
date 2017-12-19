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
import com.nis.domain.Page;
import com.nis.domain.restful.DjAttrTypeReport;
import com.nis.domain.restful.DjDestIpCountryReport;
import com.nis.domain.restful.DjEntranceReport;
import com.nis.domain.restful.DjLwhhReport;
import com.nis.domain.restful.DjPzReport;
import com.nis.domain.restful.DjSrcIpDomeSticReport;
import com.nis.domain.restful.DjTagReport;
import com.nis.domain.restful.DjPzReportStat;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DjReportService;
import com.wordnik.swagger.annotations.ApiOperation;

import net.sf.json.JSONObject;

/**
 * @ClassName: DjReportController
 * @Description: 监测日志实时统计查询服务
 * @author (ZBC)
 * @date 2016年11月22日下午06:00:00
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class DjReportController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());

	protected String activeSys = Constants.ACTIVESYS_ALL;
	protected String logSource = "0";

	@Autowired
	protected DjReportService djReportService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "djPzReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测配置实时统计服务", httpMethod = "GET", notes = "get log list")
	public Map djPzReportSources(Page page, DjPzReport djPzReport, HttpServletRequest request,
			HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DjPzReport> pzReportPage = null;

		try {
			//设置本地默认查询时间
			resetTime(djPzReport);
			
			djReportService.queryConditionCheck(thread, start, djPzReport, DjPzReport.class, page);
			// 查询数据库
			pzReportPage = djReportService.findDjPzReportPage(new Page<DjPzReport>(request, response, DjPzReport.class),
					djPzReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测配置实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测配置实时统计成功",
				pzReportPage, activeSys, logSource);
	}

	/**
	 * 配置统计【小时】
	 * 
	 * @param page
	 * @param djPzReport
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "djPzStatSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测各配置量统计服务[小时]", httpMethod = "GET", notes = "get log list")
	public Map djPzStatSources(Page page, DjPzReportStat djPzReportStat, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if (!Constants.ACTIVESYS_A.equals(djPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_C.equals(djPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_AB.equals(djPzReportStat.getSearchStatActiveSys())) {
			djPzReportStat.setSearchStatActiveSys(Constants.ACTIVESYS_B);
		}
		activeSys = djPzReportStat.getSearchStatActiveSys();
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DjPzReportStat> pzReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djPzReportStat);
			
			djReportService.queryConditionCheck(thread, start, djPzReportStat, DjPzReportStat.class, page);

			// 查询数据库
			pzReportPage = djReportService.findDjPzReportStatPage(
					new Page<DjPzReportStat>(request, response, DjPzReportStat.class), djPzReportStat);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测各配置量统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测各配置量统计成功",
				pzReportPage, activeSys, logSource);
	}
	/**
	 * 配置统计【分钟】
	 * 
	 * @param page
	 * @param djPzReport
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "djPzStatMinutesSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测各配置量统计服务[分钟]", httpMethod = "GET", notes = "get log list")
	public Map djPzStatMinutesSources(Page page, DjPzReportStat djPzReportStat, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		if (!Constants.ACTIVESYS_A.equals(djPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_C.equals(djPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_AB.equals(djPzReportStat.getSearchStatActiveSys())) {
			djPzReportStat.setSearchStatActiveSys(Constants.ACTIVESYS_B);
		}
		activeSys = djPzReportStat.getSearchStatActiveSys();
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		
		Page<DjPzReportStat> pzReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djPzReportStat);
			
			djReportService.queryConditionCheck(thread, start, djPzReportStat, DjPzReportStat.class, page);
			
			// 查询数据库
			pzReportPage = djReportService.findDjPzReportStatMinutesPage(
					new Page<DjPzReportStat>(request, response, DjPzReportStat.class), djPzReportStat);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测各配置量统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);
			
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测各配置量统计成功",
				pzReportPage, activeSys, logSource);
	}

	/**
	 * 配置统计
	 * 
	 * @param page
	 * @param djPzReport
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "djPzSumStatSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测配置总量统计服务", httpMethod = "GET", notes = "get log list")
	public Map djPzSumStatSources(Page page, DjPzReportStat djPzReportStat, Model model, HttpServletRequest request,
			HttpServletResponse response) {

		if (!Constants.ACTIVESYS_A.equals(djPzReportStat.getSearchStatActiveSys())
				&& !Constants.ACTIVESYS_C.equals(djPzReportStat.getSearchStatActiveSys())
				) {
			djPzReportStat.setSearchStatActiveSys(Constants.ACTIVESYS_B);
		}
		activeSys = djPzReportStat.getSearchStatActiveSys();
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		JSONObject json = new JSONObject();
		try {
			//设置本地默认查询时间
			resetTime(djPzReportStat);
			
			djReportService.queryConditionCheck(thread, start, djPzReportStat, DjPzReportStat.class,
					page);

			// 查询数据库
			Long sum = djReportService.findDjPzReportSumStat(
					new Page<DjPzReportStat>(request, response, DjPzReportStat.class), djPzReportStat);
			if (sum == null)
				sum = 0l;
			json.put("sum", sum);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测配置总量统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测配置总量统计成功", json,
				activeSys, logSource);
	}

	@RequestMapping(value = "djPzPrivServiceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测配置（带私有标签）统计服务", httpMethod = "GET", notes = "get log list")
	public Map djPzPrivServiceReportSources(Page page, DjPzReport djPzReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DjPzReport> djPzReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djPzReport);
			
			djReportService.queryConditionCheck(thread, start, djPzReport, DjPzReport.class, page);
			// 查询数据库
			djPzReportPage = djReportService
					.findDjPzPrivPage(new Page<DjPzReport>(request, response, DjPzReport.class), djPzReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测配置（带私有标签）统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测配置（带私有标签）统计成功",
				djPzReportPage, activeSys, logSource);
	}
	
	
	
	@RequestMapping(value = "djAttrTypeReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测性质（带私有标签）实时统计", httpMethod = "GET", notes = "get log list")
	public Map djAttrTypeReportSources(Page page, DjAttrTypeReport djAttrTypeReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DjAttrTypeReport> attrTypeReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djAttrTypeReport);
			
			djReportService.queryConditionCheck(thread, start, djAttrTypeReport, DjPzReport.class, page);
			// 查询数据库
			attrTypeReportPage = djReportService.findAttrTypeReport(
					new Page<DjAttrTypeReport>(request, response, DjAttrTypeReport.class), djAttrTypeReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测性质（带私有标签）实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测性质（带私有标签）实时统计成功",
				attrTypeReportPage, activeSys, logSource);
	}
	
	@RequestMapping(value = "djSrcIpDomesticPrivServiceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测境内源IP(带私有标签)实时统计", httpMethod = "GET", notes = "get log list")
	public Map djSrcIpDomesticPrivServiceReportSources(Page page, DjSrcIpDomeSticReport djSrcIpDomeSticReport,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DjSrcIpDomeSticReport> srcIpDomeSticReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djSrcIpDomeSticReport);
			
			djReportService.queryConditionCheck(thread, start, djSrcIpDomeSticReport, DjSrcIpDomeSticReport.class,
					page);
			// 查询数据库
			srcIpDomeSticReportPage = djReportService.findSrcIpDomeSticReport(
					new Page<DjSrcIpDomeSticReport>(request, response, DjSrcIpDomeSticReport.class),
					djSrcIpDomeSticReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测境内源IP(带私有标签)实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测境内源IP(带私有标签)实时统计成功",
				srcIpDomeSticReportPage, activeSys, logSource);
	}
	
	@RequestMapping(value = "djDestIpCountryPrivServiceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测境内目的IP(带私有标签)实时统计", httpMethod = "GET", notes = "get log list")
	public Map djDestIpCountryPrivServiceReportSources(Page page, DjDestIpCountryReport djDestIpCountryReport,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DjDestIpCountryReport> djDestIpCountryReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djDestIpCountryReport);
			
			djReportService.queryConditionCheck(thread, start, djDestIpCountryReport, DjDestIpCountryReport.class,
					page);
			// 查询数据库
			djDestIpCountryReportPage = djReportService.findDestIpCountryReport(
					new Page<DjDestIpCountryReport>(request, response, DjDestIpCountryReport.class),
					djDestIpCountryReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测境内目的IP(带私有标签)实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测境内目的IP(带私有标签)实时统计成功",
				djDestIpCountryReportPage, activeSys, logSource);
	}
	
	@RequestMapping(value = "djEntranceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测局点（带私有标签）实时统计", httpMethod = "GET", notes = "get log list")
	public Map djEntranceReportSources(Page page, DjEntranceReport djEntranceReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DjEntranceReport> djEntranceReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djEntranceReport);
			
			djReportService.queryConditionCheck(thread, start, djEntranceReport, DjEntranceReport.class,
					page);
			// 查询数据库
			djEntranceReportPage = djReportService.findDjEntranceReport(
					new Page<DjEntranceReport>(request, response, DjEntranceReport.class), djEntranceReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测局点（带私有标签）实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测局点（带私有标签）实时统计成功",
				djEntranceReportPage, activeSys, logSource);
	}
	@RequestMapping(value = "djLwhhReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测来文函号（带私有标签）实时统计", httpMethod = "GET", notes = "get log list")
	public Map djLwhhReportSources(Page page, DjLwhhReport djLwhhReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DjLwhhReport> djLwhhReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djLwhhReport);
			
			djReportService.queryConditionCheck(thread, start, djLwhhReport, DjLwhhReport.class, page);
			// 查询数据库
			djLwhhReportPage = djReportService
					.findDjLwhhReport(new Page<DjLwhhReport>(request, response, DjLwhhReport.class), djLwhhReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测来文函号（带私有标签）实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测来文函号（带私有标签）实时统计成功",
				djLwhhReportPage, activeSys, logSource);
	}
	@RequestMapping(value = "djTagPrivServiceReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "监测标签（带私有标签）实时统计", httpMethod = "GET", notes = "get log list")
	public Map djTagPrivServiceReportSources(Page page, DjTagReport djTagReport, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DjTagReport> djTagReportPage = null;
		try {
			
			//设置本地默认查询时间
			resetTime(djTagReport);
			
			djReportService.queryConditionCheck(thread, start, djTagReport, DjTagReport.class, page);
			// 查询数据库
			djTagReportPage = djReportService
					.findDjTagReportPage(new Page<DjTagReport>(request, response, DjTagReport.class), djTagReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.debug(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测标签（带私有标签）实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "监测标签（带私有标签）实时统计成功",
				djTagReportPage, activeSys, logSource);
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
