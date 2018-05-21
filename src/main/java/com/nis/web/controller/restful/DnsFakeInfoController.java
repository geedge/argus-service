/**
 *@Title: DnsFakeInfoController.java
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

import com.nis.domain.restful.DnsFakeInfo;
import com.nis.domain.restful.DnsFakeInfoSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DnsFakeInfoService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: DnsFakeInfoController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/cfg/v1")
public class DnsFakeInfoController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected DnsFakeInfoService dnsFakeInfoService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
//	@Autowired
//	protected CommonConfigService commonConfigService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * saveDnsFakeInfoBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsFakeInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsFakeInfoSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存DNS欺骗信息", httpMethod = "POST", notes = "save dns fake info")
	public Map saveDnsFakeInfoBatch(@RequestBody DnsFakeInfoSource dnsFakeInfoSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, dnsFakeInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsFakeInfoSource.getOpAction(), Constants.OPACTION_POST);
		try{
			if(chekckData(dnsFakeInfoSource,false)){
				dnsFakeInfoService.saveDnsFakeInfoBatch(dnsFakeInfoSource.getDnsFakeInfoList());
//				commonConfigService.saveOrUpdateConfigState("DNS_FAKE_INFO", dnsFakeInfoSource.getOpTime());
//				redisDao.saveMaps(dnsFakeInfoSource.getDnsFakeInfoList(), "DNS_FAKE_INFO", DnsFakeInfo.class, "id");
			}else 
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存DNS欺骗信息失败,组号不能小于0且2-100组号不可使用",RestBusinessCode.unknow_error.getValue());
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
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存DNS欺骗信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存DNS欺骗信息成功",dnsFakeInfoSource.getDnsFakeInfoList());
	}
	/**
	 * 
	 * updateDnsFakeInfoBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsFakeInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsFakeInfoSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新DNS欺骗信息", httpMethod = "PUT", notes = "update dns fake info")
	public Map updateDnsFakeInfoBatch(@RequestBody DnsFakeInfoSource dnsFakeInfoSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, dnsFakeInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsFakeInfoSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			if(chekckData(dnsFakeInfoSource,true)){
				dnsFakeInfoService.updateDnsFakeInfoBatch(dnsFakeInfoSource.getDnsFakeInfoList());
//				commonConfigService.saveOrUpdateConfigState("DNS_FAKE_INFO", dnsFakeInfoSource.getOpTime());
//				redisDao.updateMaps(dnsFakeInfoSource.getDnsFakeInfoList(), "DNS_FAKE_INFO", DnsFakeInfo.class, "id");
			}else 
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新DNS欺骗信息失败,组号不能小于0且2-100组号不可使用",RestBusinessCode.unknow_error.getValue());
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新DNS欺骗信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新DNS欺骗信息成功",dnsFakeInfoSource.getDnsFakeInfoList());
	} 
	/**
	 * 
	 * deleteDnsFakeInfo(单条删除)
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
	@RequestMapping(value = "/dnsFakeInfoSources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除DNS欺骗信息", httpMethod = "DELETE", notes = "delete dns fake info")
	public Map deleteDnsFakeInfo(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
				boolean isValid=dnsFakeInfoService.isValid(id);
				if(!isValid){
					dnsFakeInfoService.removeDnsFakeInfo(id);
//					commonConfigService.saveOrUpdateConfigState("DNS_FAKE_INFO", new Date());
				}else{
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS欺骗信息失败,不能删除有效的DNS欺骗信息",RestBusinessCode.unknow_error.getValue());
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS欺骗信息失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteDnsFakeInfo(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsFakeInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsFakeInfoSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除DNS欺骗信息", httpMethod = "DELETE", notes = "delete dns fake info")
	public Map deleteDnsFakeInfo(@RequestBody DnsFakeInfoSource dnsFakeInfoSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, dnsFakeInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsFakeInfoSource.getOpAction(), Constants.OPACTION_DELETE);
		try{
			boolean isValid=dnsFakeInfoService.isValid(dnsFakeInfoSource.getDnsFakeInfoList());
			if(isValid){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS欺骗信息失败,包含有效的DNS欺骗信息",RestBusinessCode.unknow_error.getValue());
			}else{
				dnsFakeInfoService.removeDnsFakeInfoBatch(dnsFakeInfoSource.getDnsFakeInfoList());
//				commonConfigService.saveOrUpdateConfigState("DNS_FAKE_INFO", dnsFakeInfoSource.getOpTime());
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS欺骗信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",dnsFakeInfoSource.getDnsFakeInfoList());
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
	private boolean chekckData(DnsFakeInfoSource entity,boolean update){
		int ok=1;
		Date now=new Date();
		for(DnsFakeInfo dnsFakeInfo :entity.getDnsFakeInfoList()){
			dnsFakeInfo.setLastUpdate(now);
			if(dnsFakeInfo.getOpTime()==null)
				dnsFakeInfo.setOpTime(entity.getOpTime());
//			if(update&&dnsFakeInfo.getGroupId()==null)continue;
//			if(dnsFakeInfo.getGroupId().intValue()<0||(2<dnsFakeInfo.getGroupId().intValue()&&dnsFakeInfo.getGroupId().intValue()<=100)){
//				ok=0;
//				break;
//			}
		}
		return ok==1;
	}
}
