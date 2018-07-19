/**
 *@Title: EncryptProtoRandomController.java
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

import com.nis.domain.restful.EncryptProtoRandom;
import com.nis.domain.restful.EncryptProtoRandomSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.EncryptProtoRandomService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: EncryptProtoRandomController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/cfg/v1")
public class EncryptProtoRandomController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected EncryptProtoRandomService encryptProtoRandomService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
//	@Autowired
//	protected CommonConfigService commonConfigService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * saveEncryptProtoRandomBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param EncryptProtoRandomSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/encryptProtoRandomSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存加密类协议随机封堵配置", httpMethod = "POST", notes = "save encrypt_proto_random")
	public Map saveEncryptProtoRandomBatch(@RequestBody EncryptProtoRandomSource encryptProtoRandomSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, encryptProtoRandomSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,encryptProtoRandomSource.getOpAction(), Constants.OPACTION_POST);
		try{
			this.chekckData(encryptProtoRandomSource);
			encryptProtoRandomService.saveEncryptProtoRandomBatch(encryptProtoRandomSource.getEncryptProtoRandomList());
//			commonConfigService.saveOrUpdateConfigState("DMB_CK", encryptProtoRandomSource.getOpTime());
//			redisDao.saveMaps(encryptProtoRandomSource.getEncryptProtoRandomList(), "DMB_CK", EncryptProtoRandom.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存加密类协议随机封堵配置失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存加密类协议随机封堵配置成功",encryptProtoRandomSource.getEncryptProtoRandomList());
	}
	/**
	 * 
	 * updateEncryptProtoRandomBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param EncryptProtoRandomSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/encryptProtoRandomSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新加密类协议随机封堵配置", httpMethod = "PUT", notes = "update encrypt_proto_random")
	public Map updateEncryptProtoRandomBatch(@RequestBody EncryptProtoRandomSource encryptProtoRandomSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, encryptProtoRandomSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,encryptProtoRandomSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			this.chekckData(encryptProtoRandomSource);
			encryptProtoRandomService.updateEncryptProtoRandomBatch(encryptProtoRandomSource.getEncryptProtoRandomList());
//			commonConfigService.saveOrUpdateConfigState("DMB_CK", encryptProtoRandomSource.getOpTime());
//			redisDao.updateMaps(encryptProtoRandomSource.getEncryptProtoRandomList(), "DMB_CK", EncryptProtoRandom.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新加密类协议随机封堵配置失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新加密类协议随机封堵配置成功",encryptProtoRandomSource.getEncryptProtoRandomList());
	} 
	/**
	 * 
	 * deleteEncryptProtoRandom(单条删除)
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
	@RequestMapping(value = "/encryptProtoRandomSources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除加密类协议随机封堵配置", httpMethod = "DELETE", notes = "delete encrypt_proto_random")
	public Map deleteEncryptProtoRandom(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
					encryptProtoRandomService.removeEncryptProtoRandom(id);
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除加密类协议随机封堵配置失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteEncryptProtoRandom(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param EncryptProtoRandomSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/encryptProtoRandomSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除加密类协议随机封堵配置", httpMethod = "DELETE", notes = "delete encrypt_proto_random")
	public Map deleteEncryptProtoRandom(@RequestBody EncryptProtoRandomSource encryptProtoRandomSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, encryptProtoRandomSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,encryptProtoRandomSource.getOpAction(), Constants.OPACTION_DELETE);
			try{
				encryptProtoRandomService.removeEncryptProtoRandomBatch(encryptProtoRandomSource.getEncryptProtoRandomList());
//				commonConfigService.saveOrUpdateConfigState("DMB_CK", encryptProtoRandomSource.getOpTime());
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除加密类协议随机封堵配置失败", RestBusinessCode.unknow_error.getValue());
			}
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",encryptProtoRandomSource.getEncryptProtoRandomList());
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
	private void chekckData(EncryptProtoRandomSource entity){
		Date now=new Date();
		for(EncryptProtoRandom encryptProtoRandom :entity.getEncryptProtoRandomList()){
			encryptProtoRandom.setLastUpdate(now);
			if(encryptProtoRandom.getOpTime()==null)
				encryptProtoRandom.setOpTime(entity.getOpTime());
		}
	}
}
