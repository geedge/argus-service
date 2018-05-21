/**
 *@Title: FwqInfoController.java
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

import com.nis.domain.restful.FwqInfo;
import com.nis.domain.restful.FwqInfoSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.FwqInfoService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: FwqInfoController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/cfg/v1")
public class FwqInfoController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected FwqInfoService fwqInfoService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
//	@Autowired
//	protected CommonConfigService commonConfigService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * saveFwqInfoBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param FwqInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/fwqInfoSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存服务器信息", httpMethod = "POST", notes = "save server info")
	public Map saveFwqInfoBatch(@RequestBody FwqInfoSource fwqInfoSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, fwqInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,fwqInfoSource.getOpAction(), Constants.OPACTION_POST);
		try{
			chekckData(fwqInfoSource);
			fwqInfoService.saveFwqInfoBatch(fwqInfoSource.getFwqInfoList());
//			commonConfigService.saveOrUpdateConfigState("FWQ_INFO", fwqInfoSource.getOpTime());
//			redisDao.saveMaps(fwqInfoSource.getFwqInfoList(), "FWQ_INFO", FwqInfo.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存服务器信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存服务器信息成功",fwqInfoSource.getFwqInfoList());
	}
	/**
	 * 
	 * updateFwqInfoBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param FwqInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/fwqInfoSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新服务器信息", httpMethod = "PUT", notes = "update server info")
	public Map updateFwqInfoBatch(@RequestBody FwqInfoSource fwqInfoSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, fwqInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,fwqInfoSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			chekckData(fwqInfoSource);
			fwqInfoService.updateFwqInfoBatch(fwqInfoSource.getFwqInfoList());
//			commonConfigService.saveOrUpdateConfigState("FWQ_INFO", fwqInfoSource.getOpTime());
//			redisDao.updateMaps(fwqInfoSource.getFwqInfoList(), "FWQ_INFO", FwqInfo.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新服务器信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新服务器信息成功",fwqInfoSource.getFwqInfoList());
	} 
	/**
	 * 
	 * deleteFwqInfo(单条删除)
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
	@RequestMapping(value = "/fwqInfoSources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除服务器信息", httpMethod = "DELETE", notes = "delete server info")
	public Map deleteFwqInfo(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
				fwqInfoService.removeFwqInfo(id);
//				commonConfigService.saveOrUpdateConfigState("FWQ_INFO", new Date());
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除服务器信息失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteFwqInfo(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param FwqInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/fwqInfoSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除服务器信息", httpMethod = "DELETE", notes = "delete server info")
	public Map deleteFwqInfo(@RequestBody FwqInfoSource fwqInfoSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, fwqInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,fwqInfoSource.getOpAction(), Constants.OPACTION_DELETE);
		try{
			fwqInfoService.removeFwqInfoBatch(fwqInfoSource.getFwqInfoList());
//			commonConfigService.saveOrUpdateConfigState("FWQ_INFO", fwqInfoSource.getOpTime());
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除服务器信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",fwqInfoSource.getFwqInfoList());
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
	private void chekckData(FwqInfoSource entity){
		Date now =new Date();
		for(FwqInfo fwqInfo :entity.getFwqInfoList()){
			fwqInfo.setLastUpdate(now);
			if(fwqInfo.getOpTime()==null)
				fwqInfo.setOpTime(entity.getOpTime());
		}
	}
}
