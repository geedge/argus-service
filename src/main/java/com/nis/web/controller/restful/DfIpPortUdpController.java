/**
 *@Title: DfIpPortUdpController.java
 *@Package com.nis.web.controller.restful 
 *@Description TODO
 *@author wx
 *@date 2016年9月7日 下午3:58:16
 *@version  版本号
 */
package com.nis.web.controller.restful;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nis.web.service.AuditLogThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.DfIpPortUdp;
import com.nis.domain.restful.DfIpPortUdpSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DfIpPortUdpService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: DfIpPortUdpController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/cfg/v1")
public class DfIpPortUdpController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	@Autowired
	protected DfIpPortUdpService dfIpPortUdpService;
//	@Autowired
//	protected CommonConfigService commonConfigService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * saveDfIpPortUdpBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DfIpPortUdpSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dfIpPortUdpSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存IP+端口Udp协议G设备封堵配置", httpMethod = "POST", notes = "save ip port udp")
	public Map saveDfIpPortUdpBatch(@RequestBody DfIpPortUdpSource dfIpPortUdpSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, dfIpPortUdpSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dfIpPortUdpSource.getOpAction(), Constants.OPACTION_POST);
		try{
			this.chekckData(dfIpPortUdpSource,false);
			dfIpPortUdpService.saveDfIpPortUdpBatch(dfIpPortUdpSource.getDfIpPortUdpList());
//			commonConfigService.saveOrUpdateConfigState("DF_IP_PORT_UDP", dfIpPortUdpSource.getOpTime());
//			redisDao.saveMaps(dfIpPortUdpSource.getDfIpPortUdpList(), "DF_IP_PORT_UDP", DfIpPortUdp.class, "cfgId");
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存IP+端口Udp协议G设备封堵配置失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存IP+端口Udp协议G设备封堵配置成功",dfIpPortUdpSource.getDfIpPortUdpList());						
	}
	/**
	 * 
	 * updateDfIpPortUdpBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DfIpPortUdpSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dfIpPortUdpSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新IP+端口Udp协议G设备封堵配置", httpMethod = "PUT", notes = "update ip port udp")
	public Map updateDfIpPortUdpBatch(@RequestBody DfIpPortUdpSource dfIpPortUdpSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, dfIpPortUdpSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dfIpPortUdpSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			this.chekckData(dfIpPortUdpSource,true);
			dfIpPortUdpService.updateDfIpPortUdpBatch(dfIpPortUdpSource.getDfIpPortUdpList());
//			commonConfigService.saveOrUpdateConfigState("DF_IP_PORT_UDP", dfIpPortUdpSource.getOpTime());
//			redisDao.updateMaps(dfIpPortUdpSource.getDfIpPortUdpList(), "DF_IP_PORT_UDP", DfIpPortUdp.class, "cfgId");
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新IP+端口Udp协议G设备封堵配置失败", RestBusinessCode.unknow_error.getValue());
				
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新IP+端口Udp协议G设备封堵配置成功",dfIpPortUdpSource.getDfIpPortUdpList());
	} 
	/**
	 * 
	 * deleteDfIpPortUdp(单条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param id
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dfIpPortUdpSources/{cfgId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除IP+端口Udp协议G设备封堵配置", httpMethod = "DELETE", notes = "delete ip port udp")
	public Map deleteDfIpPortUdp(@PathVariable("cfgId") long id, HttpServletRequest request, HttpServletResponse response){	
			long start=System.currentTimeMillis();
			AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);

			try{
					dfIpPortUdpService.removeDfIpPortUdp(id);
//					commonConfigService.saveOrUpdateConfigState("DF_IP_PORT_UDP", new Date());
			}catch(Exception e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				e.printStackTrace();
				logger.error(e.getMessage());
				logger.error(e.getCause());
				if(e instanceof RestServiceException) throw e;
				String info=OracleErrorCodeUtil.getOraCode(e);
				if(!StringUtil.isEmpty(info))
					OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
				else
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除IP+端口Udp协议G设备封堵配置失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteDfIpPortUdp(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DfIpPortUdpSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dfIpPortUdpSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除IP+端口Udp协议G设备封堵配置", httpMethod = "DELETE", notes = "delete ip port udp")
	public Map deleteDfIpPortUdp(@RequestBody DfIpPortUdpSource dfIpPortUdpSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, dfIpPortUdpSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dfIpPortUdpSource.getOpAction(), Constants.OPACTION_DELETE);
			try{
				dfIpPortUdpService.removeDfIpPortUdpBatch(dfIpPortUdpSource.getDfIpPortUdpList());
//				commonConfigService.saveOrUpdateConfigState("DF_IP_PORT_UDP", dfIpPortUdpSource.getOpTime());
			}catch(Exception e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				e.printStackTrace();
				logger.error(e.getMessage());
				logger.error(e.getCause());
				if(e instanceof RestServiceException) throw e;
				String info=OracleErrorCodeUtil.getOraCode(e);
				if(!StringUtil.isEmpty(info))
					OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
				else
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除IP+端口Udp协议G设备封堵配置失败", RestBusinessCode.unknow_error.getValue());
			}
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",dfIpPortUdpSource.getDfIpPortUdpList());
	}
	/**
	 * 
	 * chekckData(操作验证时间)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity
	 * @return 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	private void chekckData(DfIpPortUdpSource entity,boolean update){
		Date now=new Date();
		for(DfIpPortUdp dfIpPortUdp :entity.getDfIpPortUdpList()){
			dfIpPortUdp.setLastUpdate(now);
			if(dfIpPortUdp.getOpTime()==null)
				dfIpPortUdp.setOpTime(entity.getOpTime());
			if(!update){//新增时判断非空值
				if(dfIpPortUdp.getEffectiveRange()==null)dfIpPortUdp.setEffectiveRange("0");
				if(dfIpPortUdp.getProtocol()==null)dfIpPortUdp.setProtocol(0);
				if(dfIpPortUdp.getDirection()==null)dfIpPortUdp.setDirection(0);
			}
		}
	}
}
