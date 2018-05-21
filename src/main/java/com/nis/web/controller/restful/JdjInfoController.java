/**
 *@Title: JdjInfoController.java
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

import com.nis.domain.restful.JdjInfo;
import com.nis.domain.restful.JdjInfoSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.JdjInfoService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: JdjInfoController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/cfg/v1")
public class JdjInfoController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected JdjInfoService jdjInfoService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
//	@Autowired
//	protected CommonConfigService commonConfigService;
//	@Autowired
//	protected RedisDao redisDao;
	/**
	 * saveJdjInfoBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JdjInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/jdjInfoSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存节点机信息", httpMethod = "POST", notes = "save node info")
	public Map saveJdjInfoBatch(@RequestBody JdjInfoSource jdjInfoSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, jdjInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,jdjInfoSource.getOpAction(), Constants.OPACTION_POST);
		try{
			this.chekckData(jdjInfoSource,false);
			jdjInfoService.saveJdjInfoBatch(jdjInfoSource.getJdjInfoList());
//			commonConfigService.saveOrUpdateConfigState("JDJ_INFO", jdjInfoSource.getOpTime());
//			redisDao.saveMaps(jdjInfoSource.getJdjInfoList(), "JDJ_INFO", JdjInfo.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存节点机信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存节点机信息成功",jdjInfoSource.getJdjInfoList());
	}
	/**
	 * 
	 * updateJdjInfoBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JdjInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/jdjInfoSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新节点机信息", httpMethod = "PUT", notes = "update node info")
	public Map updateJdjInfoBatch(@RequestBody JdjInfoSource jdjInfoSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, jdjInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,jdjInfoSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			this.chekckData(jdjInfoSource,true);
			jdjInfoService.updateJdjInfoBatch(jdjInfoSource.getJdjInfoList());
//			commonConfigService.saveOrUpdateConfigState("JDJ_INFO", jdjInfoSource.getOpTime());
//			redisDao.updateMaps(jdjInfoSource.getJdjInfoList(), "JDJ_INFO", JdjInfo.class, "id");
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新节点机信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新节点机信息成功",jdjInfoSource.getJdjInfoList());
	} 
	/**
	 * 
	 * deleteJdjInfo(单条删除)
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
	@RequestMapping(value = "/jdjInfoSources/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除节点机信息", httpMethod = "DELETE", notes = "delete node info")
	public Map deleteJdjInfo(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
					jdjInfoService.removeJdjInfo(id);
//					commonConfigService.saveOrUpdateConfigState("JDJ_INFO", new Date());
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
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除节点机信息失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteJdjInfo(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JdjInfoSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/jdjInfoSources", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除节点机信息", httpMethod = "DELETE", notes = "delete node info")
	public Map deleteJdjInfo(@RequestBody JdjInfoSource jdjInfoSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, jdjInfoSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,jdjInfoSource.getOpAction(), Constants.OPACTION_DELETE);
		try{
			jdjInfoService.removeJdjInfoBatch(jdjInfoSource.getJdjInfoList());
//			commonConfigService.saveOrUpdateConfigState("JDJ_INFO", jdjInfoSource.getOpTime());
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
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除节点机信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",jdjInfoSource.getJdjInfoList());
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
	private void chekckData(JdjInfoSource entity, boolean update){
		Date now =new Date();
		for(JdjInfo jdjInfo :entity.getJdjInfoList()){
			jdjInfo.setLastUpdate(now);
			if(jdjInfo.getOpTime()==null)
				jdjInfo.setOpTime(entity.getOpTime());
			if(!update){//新增时判断默认值
				if(jdjInfo.getJcIp()==null)jdjInfo.setJcIp("0.0.0.0");
			}
		}
	}
}
