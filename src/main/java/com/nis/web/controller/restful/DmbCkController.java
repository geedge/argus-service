/**
 *@Title: DmbCkController.java
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

import com.nis.domain.restful.DmbCk;
import com.nis.domain.restful.DmbCkSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DmbCkService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: DmbCkController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/cfg/v1")
public class DmbCkController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected DmbCkService dmbCkService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
//	@Autowired
//	protected CommonConfigService commonConfigService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * saveDmbCkBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DmbCkSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dmbCkSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存运营商信息", httpMethod = "POST", notes = "save provider info")
	public Map saveDmbCkBatch(@RequestBody DmbCkSource dmbCkSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, dmbCkSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start, dmbCkSource.getOpAction(), Constants.OPACTION_POST);
		try{
			this.chekckData(dmbCkSource);
			dmbCkService.saveDmbCkBatch(dmbCkSource.getDmbCkList());
//			commonConfigService.saveOrUpdateConfigState("DMB_CK", dmbCkSource.getOpTime());
//			redisDao.saveMaps(dmbCkSource.getDmbCkList(), "DMB_CK", DmbCk.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存运营商信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存运营商信息成功",dmbCkSource.getDmbCkList());
	}
	/**
	 * 
	 * updateDmbCkBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DmbCkSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dmbCkSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新运营商信息", httpMethod = "PUT", notes = "update provider info")
	public Map updateDmbCkBatch(@RequestBody DmbCkSource dmbCkSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, dmbCkSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dmbCkSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			this.chekckData(dmbCkSource);
			dmbCkService.updateDmbCkBatch(dmbCkSource.getDmbCkList());
//			commonConfigService.saveOrUpdateConfigState("DMB_CK", dmbCkSource.getOpTime());
//			redisDao.updateMaps(dmbCkSource.getDmbCkList(), "DMB_CK", DmbCk.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新运营商信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新运营商信息成功",dmbCkSource.getDmbCkList());
	} 
	/**
	 * 
	 * deleteDmbCk(单条删除)
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
	@RequestMapping(value = "/dmbCkSources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除运营商信息", httpMethod = "DELETE", notes = "delete provider info")
	public Map deleteDmbCk(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
					dmbCkService.removeDmbCk(id);
//					commonConfigService.saveOrUpdateConfigState("DMB_CK", new Date());
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除运营商信息失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteDmbCk(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DmbCkSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dmbCkSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除运营商信息", httpMethod = "DELETE", notes = "delete provider info")
	public Map deleteDmbCk(@RequestBody DmbCkSource dmbCkSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, dmbCkSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dmbCkSource.getOpAction(), Constants.OPACTION_DELETE);
			try{
				dmbCkService.removeDmbCkBatch(dmbCkSource.getDmbCkList());
//				commonConfigService.saveOrUpdateConfigState("DMB_CK", dmbCkSource.getOpTime());
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除运营商信息失败", RestBusinessCode.unknow_error.getValue());
			}
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",dmbCkSource.getDmbCkList());
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
	private void chekckData(DmbCkSource entity){
		Date now=new Date();
		for(DmbCk dmbCk :entity.getDmbCkList()){
			dmbCk.setLastUpdate(now);
			if(dmbCk.getOpTime()==null)
				dmbCk.setOpTime(entity.getOpTime());
		}
	}
}
