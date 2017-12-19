package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.ConfigPzIdSource;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.ConfigPzIdService;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${servicePath}/cfg/v1")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConfigPzIdController extends BaseRestController {

	protected final Logger logger1 = Logger.getLogger(this.getClass());
	
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	@Autowired
	protected ConfigPzIdService configPzIdService;

	@RequestMapping(value = "/configPzIdSources", method = RequestMethod.GET)
	@ApiOperation(value = "配置ID获取服务", httpMethod = "GET", notes = "get pz_id")
	public Map configPzIdSources(ConfigPzIdSource configPzIdSource, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				configPzIdSource);
		
		try {
			configPzIdService.getConfigPzIdList(configPzIdSource);
		} catch (Exception e) {
			e.printStackTrace();
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e.getMessage());
			logger.error(e.getCause());
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "配置ID获取失败");
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "配置ID获取成功",
				configPzIdSource);
	}

}
