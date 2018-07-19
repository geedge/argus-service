/**
 *@Title: DnsGroupTypeController.java
 *@Package com.nis.web.controller.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月7日 下午3:58:16
 *@version  版本号
 */
package com.nis.web.controller.restful;

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

import com.nis.domain.restful.DnsGroupType;
import com.nis.domain.restful.DnsGroupTypeSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DnsGroupTypeService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: DnsGroupTypeController.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/cfg/v1")
public class DnsGroupTypeController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected DnsGroupTypeService dnsGroupTypeService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
//	@Autowired
//	protected CommonConfigService commonConfigService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * 
	 * saveDnsGroupTypeBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsGroupTypeSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsGroupTypeSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存DNS分组类型", httpMethod = "POST", notes = "save dns group type")
	public Map saveDnsGroupTypeBatch(@RequestBody DnsGroupTypeSource dnsGroupTypeSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, dnsGroupTypeSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsGroupTypeSource.getOpAction(), Constants.OPACTION_POST);
		try{
//				if(chekckData(dnsGroupTypeSource/*,false*/))
				chekckData(thread,start,dnsGroupTypeSource,Constants.OPACTION_POST);
				dnsGroupTypeService.saveDnsGroupTypeBatch(thread,start,dnsGroupTypeSource.getDnsGroupTypeList());
//				commonConfigService.saveOrUpdateConfigState("DNS_GROUP_TYPE", dnsGroupTypeSource.getOpTime());
//				redisDao.saveMaps(dnsGroupTypeSource.getDnsGroupTypeList(), "DNS_GROUP_TYPE", DnsGroupType.class, "id");
//				else 
//					throw new RestServiceException("保存DNS分组类型失败,组号不能小于0且2-100组号不可使用",RestBusinessCode.unknow_error.getValue());
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存DNS分组类型失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存DNS分组类型成功",dnsGroupTypeSource.getDnsGroupTypeList());
	}
	/**
	 * 
	 * updateDnsGroupTypeBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsGroupTypeSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsGroupTypeSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新DNS分组类型", httpMethod = "PUT", notes = "update dns group type")
	public Map updateDnsGroupTypeBatch(@RequestBody DnsGroupTypeSource dnsGroupTypeSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, dnsGroupTypeSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsGroupTypeSource.getOpAction(), Constants.OPACTION_PUT);
		try{
//					if(chekckData(dnsGroupTypeSource/*,true*/))
				chekckData(thread,start,dnsGroupTypeSource,Constants.OPACTION_PUT);
				dnsGroupTypeService.updateDnsGroupTypeBatch(thread,start,dnsGroupTypeSource.getDnsGroupTypeList());
//				commonConfigService.saveOrUpdateConfigState("DNS_GROUP_TYPE", dnsGroupTypeSource.getOpTime());
//				redisDao.updateMaps(dnsGroupTypeSource.getDnsGroupTypeList(), "DNS_GROUP_TYPE", DnsGroupType.class, "id");
//					else 
//						throw new RestServiceException("更新DNS分组类型失败,组号不能小于0且2-100组号不可使用",RestBusinessCode.unknow_error.getValue());
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新DNS分组类型失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新DNS分组类型成功",dnsGroupTypeSource.getDnsGroupTypeList());
	} 
	/**
	 * 
	 * deleteDnsGroupType(单条删除)
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
	@RequestMapping(value = "/dnsGroupTypeSources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除DNS分组类型", httpMethod = "DELETE", notes = "delete dns group type")
	public Map deleteDnsGroupType(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
				boolean isValid=dnsGroupTypeService.isValid(id);
				if(!isValid){
					dnsGroupTypeService.removeDnsGroupType(id);
//					commonConfigService.saveOrUpdateConfigState("DNS_GROUP_TYPE", new Date());
				}else{
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"不能删除有效的DNS分组类型");
				}
			}catch(RestServiceException e){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS分组类型失败,"+e.getMessage(), RestBusinessCode.unknow_error.getValue());
				
			}catch(Exception e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				e.printStackTrace();
				logger.error(e.getMessage());
				logger.error(e.getCause());
				String info=OracleErrorCodeUtil.getOraCode(e);
				if(!StringUtil.isEmpty(info))
					OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
				else
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS分组类型失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteDnsGroupType(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param DnsGroupTypeSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsGroupTypeSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除DNS分组类型", httpMethod = "DELETE", notes = "delete dns group type")
	public Map deleteDnsGroupType(@RequestBody DnsGroupTypeSource dnsGroupTypeSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		AuditLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, dnsGroupTypeSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsGroupTypeSource.getOpAction(), Constants.OPACTION_DELETE);
		try{
			boolean isValid=dnsGroupTypeService.isValid(dnsGroupTypeSource.getDnsGroupTypeList());
			if(isValid){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"包含有效的DNS分组类型");
			}else{
			dnsGroupTypeService.removeDnsGroupTypeBatch(dnsGroupTypeSource.getDnsGroupTypeList());
//			commonConfigService.saveOrUpdateConfigState("DNS_GROUP_TYPE", dnsGroupTypeSource.getOpTime());
			}
		}catch(RestServiceException e){
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS分组类型失败,"+e.getMessage(), RestBusinessCode.unknow_error.getValue());
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS分组类型失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",dnsGroupTypeSource.getDnsGroupTypeList());
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
	private void chekckData(AuditLogThread thread, long start, DnsGroupTypeSource entity, int opAction){
		for(DnsGroupType dnsGroupType :entity.getDnsGroupTypeList()){
			if(dnsGroupType.getOpTime()==null)
				dnsGroupType.setOpTime(entity.getOpTime());
			if(opAction==Constants.OPACTION_PUT&&dnsGroupType.getGroupId()==null)continue;
			else if(dnsGroupType.getGroupId()==null){
				throw new RestServiceException(thread, System.currentTimeMillis(), "组号为空", RestBusinessCode.missing_args.getValue());
			}else if(dnsGroupType.getGroupId().intValue()<=100){
				throw new RestServiceException(thread, System.currentTimeMillis(), "组号小于等于100", RestBusinessCode.wrong_range.getValue());
			}
		}
	}
}
