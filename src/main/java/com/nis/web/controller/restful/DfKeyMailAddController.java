/**   
 * @Title: dfKeyMailAddController.java 
 * @Package com.nis.web.controller.restful
 * @Description: 关键字业务转换邮件地址日志服务 
 * @author (ZBC) 
 * @date 2016年11月09日 下午02:25:00
 * @version V1.0
 */
package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nis.web.service.AuditLogThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.DfKeyMailAdd;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfKeyMailAddService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * @ClassName: dfKeyMailAddController
 * @Description: 关键字业务转换邮件地址日志服务
 * @author (ZBC)
 * @date 2016年11月09日 下午02:25:00
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes" })
public class DfKeyMailAddController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	@Autowired
	protected DfKeyMailAddService dfKeyMailAddService;
	
	protected int logSource = 0;

	@RequestMapping(value = "/dfKeyMailAddSources", method = RequestMethod.GET)
	@ApiOperation(value = "关键字业务转换邮件地址日志信息获取", httpMethod = "GET", notes = "get log list")
	public Map dfKeyConvertUrlSources(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfKeyMailAdd dfKeyMailAdd, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		
		if(!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)
				) {
			searchActiveSys=Constants.ACTIVESYS_B;
		}
		
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
		// 请求参数校验
		dfKeyMailAddService.queryConditionCheck(thread, start, dfKeyMailAdd, page);

		Page<DfKeyMailAdd> dfKeyMailAddPage = null;
		try {
			dfKeyMailAddPage = dfKeyMailAddService.findDfKeyMailAddPage(
					new Page<DfKeyMailAdd>(request, response, DfKeyMailAdd.class), dfKeyMailAdd);

		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "关键字业务转换邮件地址日志信息检索失败");
			}

			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(thread, System.currentTimeMillis() - start, request, "关键字业务转换邮件地址日志信息检索成功",
				dfKeyMailAddPage, logSource);
	}
}
