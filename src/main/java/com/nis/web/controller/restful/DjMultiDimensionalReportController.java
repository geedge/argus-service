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
import com.nis.domain.restful.DjLwhhAttrReport;
import com.nis.domain.restful.DjLwhhTagReport;
import com.nis.domain.restful.DjSrcIpAttrReport;
import com.nis.domain.restful.DjSrcIpTagReport;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DjMultiDimensionalReportService;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * 
 * @ClassName: DjMultiDimensionalReportController
 * @Description: TODO(dj多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午2:53:01 
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class DjMultiDimensionalReportController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());

	protected String activeSys = Constants.ACTIVESYS_ALL;
	protected String logSource = "0";
	@Autowired
	protected DjMultiDimensionalReportService djMultiDimensionalReportService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	
	@RequestMapping(value = "djLwhhAttrReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号、性质多维实时统计", httpMethod = "GET", notes = "get log list")
	public Map djServiceReportSources(Page page, DjLwhhAttrReport djLwhhAttrReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DjLwhhAttrReport> lwhhAttrReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djLwhhAttrReport);
			// 校验
			djMultiDimensionalReportService.queryConditionCheck(thread, start, djLwhhAttrReport, DjLwhhAttrReport.class,
					page);
			// 查询数据库
			lwhhAttrReportPage = djMultiDimensionalReportService.findDjLwhhAttrReportPage(
					new Page<DjLwhhAttrReport>(request, response, DjLwhhAttrReport.class), djLwhhAttrReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "来文函号、性质多维实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "来文函号、性质多维实时统计成功",
				lwhhAttrReportPage, activeSys, logSource);
	}
	
	
	
	@RequestMapping(value = "djLwhhTagReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号、标签多维实时统计", httpMethod = "GET", notes = "get log list")
	public Map djServiceReportSources(Page page, DjLwhhTagReport djLwhhTagReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DjLwhhTagReport> lwhhTagReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djLwhhTagReport);
			// 校验
			djMultiDimensionalReportService.queryConditionCheck(thread, start, djLwhhTagReport, DjLwhhTagReport.class,
					page);
			// 查询数据库
			lwhhTagReportPage = djMultiDimensionalReportService.findDjLwhhTagReportPage(
					new Page<DjLwhhTagReport>(request, response, DjLwhhTagReport.class), djLwhhTagReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "来文函号、标签多维实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "来文函号、标签多维实时统计成功",
				lwhhTagReportPage, activeSys, logSource);
	}
	
	@RequestMapping(value = "djSrcIpAttrReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号、性质多维实时统计", httpMethod = "GET", notes = "get log list")
	public Map djServiceReportSources(Page page, DjSrcIpAttrReport djSrcIpAttrReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DjSrcIpAttrReport> srcIpAttrReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djSrcIpAttrReport);
			
			// 校验
			djMultiDimensionalReportService.queryConditionCheck(thread, start, djSrcIpAttrReport, DjSrcIpAttrReport.class,
					page);
			// 查询数据库
			srcIpAttrReportPage = djMultiDimensionalReportService.findDjSrcIpAttrReportPage(
					new Page<DjSrcIpAttrReport>(request, response, DjSrcIpAttrReport.class), djSrcIpAttrReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "境内源ip、性质多维实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "境内源ip、性质多维实时统计成功",
				srcIpAttrReportPage, activeSys, logSource);
	}
	
	
	
	@RequestMapping(value = "djSrcIpTagReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号、标签多维实时统计", httpMethod = "GET", notes = "get log list")
	public Map djServiceReportSources(Page page, DjSrcIpTagReport djSrcIpTagReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DjSrcIpTagReport> srcIpTagReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(djSrcIpTagReport);
			// 校验
			djMultiDimensionalReportService.queryConditionCheck(thread, start, djSrcIpTagReport, DjSrcIpTagReport.class,
					page);
			// 查询数据库
			srcIpTagReportPage = djMultiDimensionalReportService.findDjSrcIpTagReportPage(
					new Page<DjSrcIpTagReport>(request, response, DjSrcIpTagReport.class), djSrcIpTagReport);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "境内源ip、标签多维实时统计失败");
			}
			((RestServiceException) e).setLogSource(logSource);
			((RestServiceException) e).setActiveSys(activeSys);
			throw ((RestServiceException) e);

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "境内源ip、标签多维实时统计成功",
				srcIpTagReportPage, activeSys, logSource);
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
