/**
 *@Title: DnsResponseStrategy.java
 *@Package com.nis.web.controller.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月5日 下午4:12:27
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

import com.nis.domain.restful.DnsResponseStrategy;
import com.nis.domain.restful.DnsResponseStrategySource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DnsResponseStrategyService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: DnsResponseStrategyController.java
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月5日 下午4:12:27
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/cfg/v1")
public class DnsResponseStrategyController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected DnsResponseStrategyService dnsResponseStrategyService;
//	@Autowired
//	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	/**
	 * 
	 * saveDnsResponseStrategyBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param dnsResponseStrategySource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsResponseStrategySources", method = RequestMethod.POST)
	@ApiOperation(value = "保存DNS响应策略", httpMethod = "POST", notes = "save dns response strategy")
	public Map saveDnsResponseStrategyBatch(@RequestBody DnsResponseStrategySource dnsResponseStrategySource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, dnsResponseStrategySource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsResponseStrategySource.getOpAction(), Constants.OPACTION_POST);
		try{
			chekckData(thread,start,dnsResponseStrategySource,Constants.OPACTION_POST);
			dnsResponseStrategyService.saveDnsResponseStrategyBatch(thread,start,dnsResponseStrategySource.getDnsResponseStrategyList());
//			redisDao.saveMaps(dnsResponseStrategySource.getDnsResponseStrategyList(), "DNS_RESPONSE_STRATEGY", DnsResponseStrategy.class, "id");
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			if(e instanceof RestServiceException){
				throw e;
			}
			e.printStackTrace();
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存DNS响应策略失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存DNS响应策略成功",dnsResponseStrategySource.getDnsResponseStrategyList());
	}
	/**
	 * 
	 * updateDnsResponseStrategyBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param dnsResponseStrategySource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsResponseStrategySources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新DNS响应策略", httpMethod = "PUT", notes = "update dns response strategy")
	public Map updateDnsResponseStrategyBatch(@RequestBody DnsResponseStrategySource dnsResponseStrategySource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, dnsResponseStrategySource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsResponseStrategySource.getOpAction(), Constants.OPACTION_PUT);
		try{
			chekckData(thread,start,dnsResponseStrategySource,Constants.OPACTION_PUT);
			dnsResponseStrategyService.updateDnsResponseStrategyBatch(thread,start,dnsResponseStrategySource.getDnsResponseStrategyList());
//			commonConfigService.saveOrUpdateConfigState("DNS_RESPONSE_STRATEGY", dnsResponseStrategySource.getOpTime());
//			redisDao.updateMaps(dnsResponseStrategySource.getDnsResponseStrategyList(), "DNS_RESPONSE_STRATEGY", DnsResponseStrategy.class, "id");
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error(e.getCause());
			if(e instanceof RestServiceException){
				throw e;
			}
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新DNS响应策略失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新DNS响应策略成功",dnsResponseStrategySource.getDnsResponseStrategyList());
	} 
	/**
	 * 
	 * deleteDnsResponseStrategy(单条删除)
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
	@RequestMapping(value = "/dnsResponseStrategySources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除DNS响应策略", httpMethod = "DELETE", notes = "delete dns response strategy")
	public Map deleteDnsResponseStrategy(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();	
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
				boolean isValid=dnsResponseStrategyService.isValid(id);
				if(!isValid){
					dnsResponseStrategyService.removeDnsResponseStrategy(id);
//					commonConfigService.saveOrUpdateConfigState("DNS_RESPONSE_STRATEGY", new Date());
				}else{
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS响应策略失败,不能删除有效的DNS响应策略", RestBusinessCode.unknow_error.getValue());
				}
			}catch(RestServiceException e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS响应策略失败", RestBusinessCode.unknow_error.getValue());
				
			}
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteDnsResponseStrategy(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param dnsResponseStrategySource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dnsResponseStrategySources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除DNS响应策略", httpMethod = "DELETE", notes = "delete dns response strategy")
	public Map deleteDnsResponseStrategy(@RequestBody DnsResponseStrategySource dnsResponseStrategySource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();	
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, dnsResponseStrategySource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dnsResponseStrategySource.getOpAction(), Constants.OPACTION_DELETE);
		try{
			boolean isValid=dnsResponseStrategyService.isValid(dnsResponseStrategySource.getDnsResponseStrategyList());
			if(isValid){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS响应策略失败,包含有效的DNS响应策略",RestBusinessCode.unknow_error.getValue());
			}else
			dnsResponseStrategyService.removeDnsResponseStrategyBatch(dnsResponseStrategySource.getDnsResponseStrategyList());
		}catch(RestServiceException e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除DNS响应策略失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",dnsResponseStrategySource.getDnsResponseStrategyList());
	}
	/**
	 * 
	 * chekckData(验证数据，减少遍历次数，尽量在此方法内验证数据)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	private void chekckData(SaveRequestLogThread thread,long start,DnsResponseStrategySource entity,int opAction){
		Date now =new Date();
		
		for(DnsResponseStrategy dnsResponseStrategy :entity.getDnsResponseStrategyList()){
			checkReqStrateId(thread,start,dnsResponseStrategy,opAction);
			dnsResponseStrategy.setLastUpdate(now);
			if(dnsResponseStrategy.getOpTime()==null)
				dnsResponseStrategy.setOpTime(entity.getOpTime());
			if(opAction==Constants.OPACTION_POST){//新增数据时设置默认值
				if(dnsResponseStrategy.getResGroupOneId()==null)dnsResponseStrategy.setResGroupOneId(0);
				if(dnsResponseStrategy.getResGroupOneNum()==null)dnsResponseStrategy.setResGroupOneNum(0);
				if(dnsResponseStrategy.getResGroupTwoId()==null)dnsResponseStrategy.setResGroupTwoId(0);
				if(dnsResponseStrategy.getResGroupTwoNum()==null)dnsResponseStrategy.setResGroupTwoNum(0);
				if(dnsResponseStrategy.getResGroupThreeId()==null)dnsResponseStrategy.setResGroupThreeId(0);
				if(dnsResponseStrategy.getResGroupThreeNum()==null)dnsResponseStrategy.setResGroupThreeNum(0);
				if(dnsResponseStrategy.getResGroupFourId()==null)dnsResponseStrategy.setResGroupFourId(0);
				if(dnsResponseStrategy.getResGroupFourNum()==null)dnsResponseStrategy.setResGroupFourNum(0);
				if(dnsResponseStrategy.getResGroupFiveId()==null)dnsResponseStrategy.setResGroupFiveId(0);
				if(dnsResponseStrategy.getResGroupFiveNum()==null)dnsResponseStrategy.setResGroupFiveNum(0);
				if(dnsResponseStrategy.getAddGroup()==null)dnsResponseStrategy.setAddGroup(0);
				if(dnsResponseStrategy.getAuthGroup()==null)dnsResponseStrategy.setAuthGroup(0);
			}
		}
		
	}
	private void checkReqStrateId(SaveRequestLogThread thread,long start,DnsResponseStrategy dnsResponseStrategy,int opAction){
			//update时可以为空
			if(opAction==Constants.OPACTION_PUT&&dnsResponseStrategy.getReqStrateId()==null)return;
			if(dnsResponseStrategy.getReqStrateId()==null){
				throw new RestServiceException(thread, System.currentTimeMillis(), "请求策略号为空", RestBusinessCode.missing_args.getValue());
			}else if(dnsResponseStrategy.getReqStrateId()<=100){
				throw new RestServiceException(thread, System.currentTimeMillis(), "请求策略号小于等于100", RestBusinessCode.wrong_range.getValue());
			} 
	}
}
