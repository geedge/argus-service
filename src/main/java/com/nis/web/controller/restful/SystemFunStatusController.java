package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.SystemFunStatus;
import com.nis.domain.restful.SystemFunStatusSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.SystemFunStatusService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: SystemFunStatusController.java 
 * @Description: 文本系统功能类业务配置服务
 * @author (zbc)
 * @date 2016年11月10日 上午09:47:00
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/cfg/v1")
@SuppressWarnings("rawtypes")
public class SystemFunStatusController  extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	
	@Autowired
	protected SystemFunStatusService systemFunStatusService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	
	/**
	 * 
	 * @param systemFunStatusSource
	 * @param request
	 * @param response
	 * @return Map
	 * @since  1.0.0
	 */
	@RequestMapping(value = "/systemFunStatusSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存文本系统功能类业务配置", httpMethod = "POST", notes = "save encrypt_proto_random")
	public Map saveSystemFunStatusBatch(@RequestBody SystemFunStatusSource systemFunStatusSource,
			HttpServletRequest request, HttpServletResponse response) {
		
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request, systemFunStatusSource);
		super.checkOpAction(thread, System.currentTimeMillis() - start, systemFunStatusSource.getOpAction(), Constants.OPACTION_POST);
		
		try{
			this.chekckData(systemFunStatusSource);
			systemFunStatusService.saveSystemFunStatusBatch(systemFunStatusSource.getSystemFunStatusList());
			
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			if(e instanceof RestServiceException) {throw e;}
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info)) {
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			} else {
                throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存文本系统功能类业务配置失败", RestBusinessCode.unknow_error.getValue());
            }



		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存文本系统功能类业务配置成功",systemFunStatusSource.getSystemFunStatusList());
	}

	
	/**
	 * 
	 * @param systemFunStatusSource
	 * @param request
	 * @param response
	 * @return Map
	 * @since  1.0.0
	 */
	@RequestMapping(value = "/systemFunStatusSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新文本系统功能类业务配置", httpMethod = "PUT", notes = "update encrypt_proto_random")
	public Map updateSystemFunStatusBatch(@RequestBody SystemFunStatusSource systemFunStatusSource,
			HttpServletRequest request, HttpServletResponse response){
		
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, systemFunStatusSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,systemFunStatusSource.getOpAction(), Constants.OPACTION_PUT);
		
		try{
			this.chekckData(systemFunStatusSource);
			systemFunStatusService.updateSystemFunStatusBatch(systemFunStatusSource.getSystemFunStatusList());
			
		}catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			if(e instanceof RestServiceException) {throw e; }
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info)) {
                OracleErrorCodeUtil.throwExceptionInfo(thread, System.currentTimeMillis() - start, info);
            } else {
                throw new RestServiceException(thread, System.currentTimeMillis() - start, "更新文本系统功能类业务配置失败", RestBusinessCode.unknow_error.getValue());
            }
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新文本系统功能类业务配置成功",systemFunStatusSource.getSystemFunStatusList());

	}
	
	
	private void chekckData(SystemFunStatusSource entity) {
		for (SystemFunStatus systemFunStatus : entity.getSystemFunStatusList()) {
			if (systemFunStatus.getOpTime() == null) {
                systemFunStatus.setOpTime(entity.getOpTime());
            }

		}
	}
}