/**   
* @Title: DjLogSearchController.java 
* @Package com.nis.web.controller.restful
* @Description: TODO(用一句话描述该文件做什么) 
* @author (zbc) 
* @date 2016年9月8日下午8:11:58
* @version V1.0   
*/
package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.DjFlowControlStop;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.IntervalTimeSearchService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * @ClassName: DjLogSearchController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (zbc)
 * @date 2016年9月8日下午8:11:58
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class IntervalTimeSearchController  extends BaseRestController{
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	protected IntervalTimeSearchService timeSearchService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	protected String logSource = "0";
	
	@RequestMapping(value="/djFlowControlStopSources", method = RequestMethod.GET)
	@ApiOperation(value="监测规则流控信息获取" , httpMethod = "GET",  notes="get log list")
	public Map djFlowControlStopList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjFlowControlStop flowControlStop, HttpServletRequest request, 
			HttpServletResponse response, Model model) {
		
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys)
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)
				)searchActiveSys=Constants.ACTIVESYS_B;
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		//请求参数校验
		timeSearchService.queryConditionCheck(thread,start,flowControlStop,page);
		
		Page<DjFlowControlStop> flowControlStopPage = null;
		try {
			flowControlStopPage = timeSearchService.findFlowControlStopPage(new Page<DjFlowControlStop>(request, response,DjFlowControlStop.class), flowControlStop);
			
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "监测规则流控信息检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "监测规则流控信息检索成功",flowControlStopPage
				,searchActiveSys, logSource);
	}
}
