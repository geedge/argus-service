/**   
* @Title: ServiceController.java 
* @Package com.nis.web.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月14日 下午2:16:33 
* @version V1.0   
*/
package com.nis.web.controller.restful;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.ControlLog;
import com.nis.domain.Page;
import com.nis.domain.restful.ConfigSource;
import com.nis.domain.restful.DfIpPortLog;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.restful.ControlLogService;
import com.wordnik.swagger.annotations.ApiOperation;

/** 
* @ClassName: ServiceController 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月14日 下午2:16:33 
* @version V1.0 
*/
/*@RestController
@RequestMapping("${servicePath}")*/
public class ServiceController extends BaseRestController{
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected ControlLogService logService;	 
	
//	@RequestMapping(value="/log/v1/logs", method = RequestMethod.GET)
//	@ApiOperation(value="日志分页获取" , httpMethod = "GET",  notes="get log list")
//	public Map logList(ControlLog log, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<ControlLog> content = logService.findPage(new Page<ControlLog>(request, response), log);
//		return serviceResponse(request, response, "数据删除", content);
//	}
	
	
	
	
//	@RequestMapping(value="/log/v1/logs/{id}", method = RequestMethod.GET)
//	public Map getControlLog(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
//		
//		ControlLog log = logService.findById(id);
//		if (StringUtil.isEmpty(log)) {
//			throw new RestServiceException("未获取管控日志，日志ID为："+id, RestBusinessCode.missing_args.getValue());
//		}
//		return serviceResponse(request, response, "数据删除", log);
//	}
	
	
	@RequestMapping(value="/log/v1/logs", method = RequestMethod.POST)
	public ControlLog createControlLog(@RequestBody ControlLog controlLog) {
		logService.createControlLog(controlLog);
		
		return controlLog;
		
	}
	
/*	@RequestMapping(value="/cfg/v1/configSources", method = RequestMethod.POST)
	public ConfigSource createConfigSource(@RequestBody ConfigSource configSource) {
		System.out.println(configSource.getOperator());
		
		return null;
		
	}*/
	
	
//	@RequestMapping(value="/log/v1/logs/{id}", method = RequestMethod.DELETE)
//	public Map deleteControlLog(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response) {
//		logService.deleteControlLog(id);
//		return serviceResponse(request, response, "数据删除");
//		
//	}

	
	
	

}
