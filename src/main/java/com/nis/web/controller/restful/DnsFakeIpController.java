/**
 *@Title: DnsFakeIpController.java
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

import com.nis.domain.restful.DnsFakeIp;
import com.nis.domain.restful.DnsFakeIpSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DnsFakeIpService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: DnsFakeIpController.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/cfg/v1")
public class DnsFakeIpController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected DnsFakeIpService dnsFakeIpService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * saveDnsFakeIpBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsFakeIpSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsFakeIpSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存DNS欺骗IP地址", httpMethod = "POST", notes = "save dns fake ip")
	public Map saveDnsFakeIpBatch(@RequestBody DnsFakeIpSource dnsFakeIpSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, dnsFakeIpSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsFakeIpSource.getOpAction(), Constants.OPACTION_POST);
		try{
			if(chekckData(dnsFakeIpSource,false)){
				dnsFakeIpService.saveDnsFakeIpBatch(dnsFakeIpSource.getDnsFakeIpList());
			}else 
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存DNS欺骗IP地址失败,组号不能小于0且2-100组号不可使用",RestBusinessCode.unknow_error.getValue());
		}catch(RestServiceException e){
			throw e;
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存DNS欺骗IP地址失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存DNS欺骗IP地址成功",dnsFakeIpSource.getDnsFakeIpList());
	}
	/**
	 * 
	 * updateDnsFakeIpBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsFakeIpSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsFakeIpSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新DNS欺骗IP地址", httpMethod = "PUT", notes = "update dns fake ip")
	public Map updateDnsFakeIpBatch(@RequestBody DnsFakeIpSource dnsFakeIpSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, dnsFakeIpSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsFakeIpSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			if(chekckData(dnsFakeIpSource,true)){
				dnsFakeIpService.updateDnsFakeIpBatch(dnsFakeIpSource.getDnsFakeIpList());
//				redisDao.updateMaps(dnsFakeIpSource.getDnsFakeIpList(), "DNS_FAKE_IP", DnsFakeIp.class, "id");
			}else 
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新DNS欺骗IP地址失败,组号不能小于0且2-100组号不可使用",RestBusinessCode.unknow_error.getValue());
		}catch(RestServiceException e){
			throw e;
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新DNS欺骗IP地址失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新DNS欺骗IP地址成功",dnsFakeIpSource.getDnsFakeIpList());
	} 
	/**
	 * 
	 * deleteDnsFakeIp(单条删除)
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
	@RequestMapping(value = "/dnsFakeIpSources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除DNS欺骗IP地址", httpMethod = "DELETE", notes = "delete dns fake ip")
	public Map deleteDnsFakeIp(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
				boolean isValid=dnsFakeIpService.isValid(id);
				if(!isValid){
					dnsFakeIpService.removeDnsFakeIp(id);
//					commonConfigService.saveOrUpdateConfigState("DNS_FAKE_IP", new Date());
				}else{
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS欺骗IP地址失败,不能删除有效的DNS欺骗IP地址",RestBusinessCode.unknow_error.getValue());
				}
			}catch(RestServiceException e){
				throw e;
				
			}catch(Exception e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				e.printStackTrace();
				logger.error(e.getMessage());
				logger.error(e.getCause());
				String info=OracleErrorCodeUtil.getOraCode(e);
				if(!StringUtil.isEmpty(info))
					OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
				else
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS欺骗IP地址失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteDnsFakeIp(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsFakeIpSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsFakeIpSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除DNS欺骗IP地址", httpMethod = "DELETE", notes = "delete dns fake ip")
	public Map deleteDnsFakeIp(@RequestBody DnsFakeIpSource dnsFakeIpSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, dnsFakeIpSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsFakeIpSource.getOpAction(), Constants.OPACTION_DELETE);
		try{
			boolean isValid=dnsFakeIpService.isValid(dnsFakeIpSource.getDnsFakeIpList());
			if(isValid){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS欺骗IP地址失败,包含有效的DNS欺骗IP地址",RestBusinessCode.unknow_error.getValue());
			}else{
				dnsFakeIpService.removeDnsFakeIpBatch(dnsFakeIpSource.getDnsFakeIpList());
//				commonConfigService.saveOrUpdateConfigState("DNS_FAKE_IP", dnsFakeIpSource.getOpTime());
			}
		}catch(RestServiceException e){
			throw e;
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS欺骗IP地址失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",dnsFakeIpSource.getDnsFakeIpList());
	}
	/**
	 * 
	 * chekckData(0-1组号以及100以外的组号可以使用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entityList
	 * @param update 是否是更新请求
	 * @return 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	private boolean chekckData(DnsFakeIpSource entity,boolean update){
		int ok=1;
		Date now=new Date();	
		for(DnsFakeIp dnsFakeIp :entity.getDnsFakeIpList()){
			dnsFakeIp.setLastUpdate(now);
			if(dnsFakeIp.getOpTime()==null)
				dnsFakeIp.setOpTime(entity.getOpTime());
			if(!update){//insert需要判断默认值，保存完毕返回完整对象
				if(dnsFakeIp.getProtocol()==null)dnsFakeIp.setProtocol(0);
				if(dnsFakeIp.getDirection()==null)dnsFakeIp.setDirection(0);
			}
			if(update&&dnsFakeIp.getGroupId()==null)continue;
			if(dnsFakeIp.getGroupId().intValue()<0||(2<dnsFakeIp.getGroupId().intValue()&&dnsFakeIp.getGroupId().intValue()<=100)){
				ok=0;
				break;
			}
		}
		return ok==1;
	}
}
