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
import com.nis.domain.restful.DfLwhhAttrReport;
import com.nis.domain.restful.DfLwhhTagReport;
import com.nis.domain.restful.DfSrcIpAttrReport;
import com.nis.domain.restful.DfSrcIpTagReport;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfMultiDimensionalReportService;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * 
 * @ClassName: DfMultiDimensionalReportController
 * @Description: TODO(df多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午2:53:01 
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class DfMultiDimensionalReportController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());

	protected String activeSys = Constants.ACTIVESYS_ALL;
	protected String logSource = "0";
	@Autowired
	protected DfMultiDimensionalReportService dfMultiDimensionalReportService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	
	@RequestMapping(value = "dfLwhhAttrReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号、性质多维实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfServiceReportSources(Page page, DfLwhhAttrReport dfLwhhAttrReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfLwhhAttrReport> lwhhAttrReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfLwhhAttrReport);
			// 校验
			dfMultiDimensionalReportService.queryConditionCheck(thread, start, dfLwhhAttrReport, DfLwhhAttrReport.class,
					page);
			// 查询数据库
			lwhhAttrReportPage = dfMultiDimensionalReportService.findDfLwhhAttrReportPage(
					new Page<DfLwhhAttrReport>(request, response, DfLwhhAttrReport.class), dfLwhhAttrReport);
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
	
	
	
	@RequestMapping(value = "dfLwhhTagReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号、标签多维实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfServiceReportSources(Page page, DfLwhhTagReport dfLwhhTagReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfLwhhTagReport> lwhhTagReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfLwhhTagReport);
			// 校验
			dfMultiDimensionalReportService.queryConditionCheck(thread, start, dfLwhhTagReport, DfLwhhTagReport.class,
					page);
			// 查询数据库
			lwhhTagReportPage = dfMultiDimensionalReportService.findDfLwhhTagReportPage(
					new Page<DfLwhhTagReport>(request, response, DfLwhhTagReport.class), dfLwhhTagReport);
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
	
	@RequestMapping(value = "dfSrcIpAttrReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号、性质多维实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfServiceReportSources(Page page, DfSrcIpAttrReport dfSrcIpAttrReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfSrcIpAttrReport> srcIpAttrReportPage = null;
		try {
			//设置本地默认查询时间
			resetTime(dfSrcIpAttrReport);
			// 校验
			dfMultiDimensionalReportService.queryConditionCheck(thread, start, dfSrcIpAttrReport, DfSrcIpAttrReport.class,
					page);
			// 查询数据库
			srcIpAttrReportPage = dfMultiDimensionalReportService.findDfSrcIpAttrReportPage(
					new Page<DfSrcIpAttrReport>(request, response, DfSrcIpAttrReport.class), dfSrcIpAttrReport);
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
	
	
	
	@RequestMapping(value = "dfSrcIpTagReportSources", method = RequestMethod.GET)
	@ApiOperation(value = "来文函号、标签多维实时统计", httpMethod = "GET", notes = "get log list")
	public Map dfServiceReportSources(Page page, DfSrcIpTagReport dfSrcIpTagReport, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		Page<DfSrcIpTagReport> srcIpTagReportPage = null;
		try {
			
			//设置本地默认查询时间
			resetTime(dfSrcIpTagReport);
			
			// 校验
			dfMultiDimensionalReportService.queryConditionCheck(thread, start, dfSrcIpTagReport, DfSrcIpTagReport.class,
					page);
			// 查询数据库
			srcIpTagReportPage = dfMultiDimensionalReportService.findDfSrcIpTagReportPage(
					new Page<DfSrcIpTagReport>(request, response, DfSrcIpTagReport.class), dfSrcIpTagReport);
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
