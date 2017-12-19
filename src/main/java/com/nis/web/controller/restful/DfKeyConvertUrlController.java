/**   
* @Title: DfKeyConvertUrlController.java 
* @Package com.nis.web.controller.restful
* @Description: TODO(用一句话描述该文件做什么) 
* @author (DDM) 
* @date 2016年9月27日 上午10:17:20
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
import com.nis.domain.restful.DfKeyConvertUrl;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfKeyConvertUrlService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * @ClassName: DfKeyConvertUrlController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (DDM)
 * @date  2016年9月27日 上午10:17:20
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class DfKeyConvertUrlController  extends BaseRestController{
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	protected DfKeyConvertUrlService dfKeyConvertUrlService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	protected String logSource = "0";
	
	@RequestMapping(value="/dfKeyConvertUrlSources", method = RequestMethod.GET)
	@ApiOperation(value="关键字业务转换URL日志信息获取" , httpMethod = "GET",  notes="get log list")
	public Map dfKeyConvertUrlSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfKeyConvertUrl dfKeyConvertUrl, HttpServletRequest request, 
			HttpServletResponse response, Model model) {
		
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)
				) searchActiveSys=Constants.ACTIVESYS_B;
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_GET,request, null);
		//请求参数校验
		dfKeyConvertUrlService.queryConditionCheck(thread,start,dfKeyConvertUrl,page);
		
		Page<DfKeyConvertUrl> dfKeyConvertUrlPage = null;
		try {
			dfKeyConvertUrlPage = dfKeyConvertUrlService.findFlowControlStopPage(new Page<DfKeyConvertUrl>(request, response,DfKeyConvertUrl.class), dfKeyConvertUrl);
			
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "关键字业务转换URL日志信息检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request, response, "关键字业务转换URL日志信息检索成功",dfKeyConvertUrlPage
				,searchActiveSys, logSource);
	}
}
