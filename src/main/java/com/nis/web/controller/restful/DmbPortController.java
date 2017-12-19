/**
 *@Title: DmbPortController.java
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.DmbPort;
import com.nis.domain.restful.DmbPortSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DmbPortService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: DmbPortController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/cfg/v1")
public class DmbPortController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected DmbPortService dmbPortService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
//	@Autowired
//	protected CommonConfigService commonConfigService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * saveDmbPortBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DmbPortSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dmbPortSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存运营商端口信息", httpMethod = "POST", notes = "save provider port info")
	public Map saveDmbPortBatch(@RequestBody DmbPortSource dmbPortSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, dmbPortSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dmbPortSource.getOpAction(), Constants.OPACTION_POST);
		try{
			this.chekckData(dmbPortSource);
			dmbPortService.saveDmbPortBatch(dmbPortSource.getDmbPortList());
//			commonConfigService.saveOrUpdateConfigState("DMB_PORT", dmbPortSource.getOpTime());
//			redisDao.saveMaps(dmbPortSource.getDmbPortList(), "DMB_PORT", DmbPort.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存运营商端口信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存运营商端口信息成功",dmbPortSource.getDmbPortList());
	}
	/**
	 * 
	 * updateDmbPortBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DmbPortSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dmbPortSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新运营商端口信息", httpMethod = "PUT", notes = "update provider port info")
	public Map updateDmbPortBatch(@RequestBody DmbPortSource dmbPortSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, dmbPortSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dmbPortSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			this.chekckData(dmbPortSource);
			dmbPortService.updateDmbPortBatch(dmbPortSource.getDmbPortList());
//			commonConfigService.saveOrUpdateConfigState("DMB_PORT", dmbPortSource.getOpTime());
//			redisDao.updateMaps(dmbPortSource.getDmbPortList(), "DMB_PORT", DmbPort.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新运营商端口信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新运营商端口信息成功",dmbPortSource.getDmbPortList());
	} 
	/**
	 * 
	 * deleteDmbPort(单条删除)
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
	@RequestMapping(value = "/dmbPortSources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除运营商端口信息", httpMethod = "DELETE", notes = "delete provider port info")
	public Map deleteDmbPort(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);	
		try{
					dmbPortService.removeDmbPort(id);
//					commonConfigService.saveOrUpdateConfigState("DMB_PORT", new Date());
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除运营商端口信息失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteDmbPort(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DmbPortSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dmbPortSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除运营商端口信息", httpMethod = "DELETE", notes = "delete provider port info")
	public Map deleteDmbPort(@RequestBody DmbPortSource dmbPortSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, dmbPortSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dmbPortSource.getOpAction(), Constants.OPACTION_DELETE);
		try{
			dmbPortService.removeDmbPortBatch(dmbPortSource.getDmbPortList());
//			commonConfigService.saveOrUpdateConfigState("DMB_PORT", dmbPortSource.getOpTime());
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除运营商端口信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",dmbPortSource.getDmbPortList());
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
	private void chekckData(DmbPortSource entity){
		Date now=new Date();	
		for(DmbPort dmbPort :entity.getDmbPortList()){
			dmbPort.setLastUpdate(now);
			if(dmbPort.getOpTime()==null)
				dmbPort.setOpTime(entity.getOpTime());
		}
	}
}
