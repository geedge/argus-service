/**
 *@Title: JkLyqController.java
 *@Package com.nis.web.controller.restful 
 *@Description TODO
 *@author wx
 *@date 2016年9月7日 下午3:58:16
 *@version  版本号
 */
package com.nis.web.controller.restful.jk;

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

import com.nis.domain.restful.jk.JkLyq;
import com.nis.domain.restful.jk.JkLyqSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.jk.JkLyqService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: JkLyqController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/jk/v1")
public class JkLyqController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected JkLyqService jkLyqService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	/**
	 * saveJkLyqBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JkLyqSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/jkLyqSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存静态控管路由器", httpMethod = "POST", notes = "保存静态控管路由器")
	public Map saveJkLyqBatch(@RequestBody JkLyqSource jkLyqSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, jkLyqSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start, jkLyqSource.getOpAction(), Constants.OPACTION_POST);
		try{
			checKData(thread,start,jkLyqSource,Constants.OPACTION_POST);
			jkLyqService.saveJkLyqBatch(jkLyqSource.getJkLyqList());
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage()+"\r\n"+e.getCause());
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存静态控管路由器失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存静态控管路由器成功",jkLyqSource.getJkLyqList());
	}
	/**
	 * 
	 * updateJkLyqBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JkLyqSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/jkLyqSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新静态控管路由器", httpMethod = "PUT", notes = "更新静态控管路由器")
	public Map updateJkLyqBatch(@RequestBody JkLyqSource jkLyqSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, jkLyqSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,jkLyqSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			checKData(thread,start,jkLyqSource,Constants.OPACTION_PUT);
			jkLyqService.updateJkLyqBatch(jkLyqSource.getJkLyqList());
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage()+"\r\n"+e.getCause());
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新静态控管路由器失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新静态控管路由器成功",jkLyqSource.getJkLyqList());
	} 
	/**
	 * 
	 * deleteJkLyq(单条删除)
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
//	@RequestMapping(value = "/jkLyqSources/{id}", method = RequestMethod.DELETE)
//	@ApiOperation(value = "删除静态控管路由器", httpMethod = "DELETE", notes = "删除静态控管路由器")
	public Map deleteJkLyq(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
			jkLyqService.removeJkLyq(id);
			}catch(Exception e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				String info=OracleErrorCodeUtil.getOraCode(e);
				if(!StringUtil.isEmpty(info))
					OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
				else
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除静态控管路由器失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteJkLyq(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JkLyqSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
//	@RequestMapping(value = "/jkLyqSources", method = RequestMethod.DELETE)
//	@ApiOperation(value = "删除静态控管路由器", httpMethod = "DELETE", notes = "删除静态控管路由器")
	public Map deleteJkLyq(@RequestBody JkLyqSource jkLyqSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, jkLyqSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,jkLyqSource.getOpAction(), Constants.OPACTION_DELETE);
			try{
				jkLyqService.removeJkLyqBatch(jkLyqSource.getJkLyqList());
			}catch(Exception e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				String info=OracleErrorCodeUtil.getOraCode(e);
				if(!StringUtil.isEmpty(info))
					OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
				else
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除静态控管路由器失败", RestBusinessCode.unknow_error.getValue());
			}
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",jkLyqSource.getJkLyqList());
	}
	private void checKData(SaveRequestLogThread thread,long start,JkLyqSource jkLyqSource,int opAction){
		if(jkLyqSource.getJkLyqList().size()==0){
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"静态控管路由器不能为空", RestBusinessCode.unknow_error.getValue());
		}
		for(JkLyq entity:jkLyqSource.getJkLyqList()){
			if(opAction==Constants.OPACTION_POST&&entity!=null&&entity.getSfSx()!=Long.valueOf(Constants.YES)){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"只能新增有效数据", RestBusinessCode.wrong_range.getValue());
			}else if(opAction==Constants.OPACTION_PUT&&entity!=null&&entity.getSfSx()!=Long.valueOf(Constants.NO)){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"只允许将有效数据更改为无效数据", RestBusinessCode.wrong_range.getValue());
			}
			if(opAction==Constants.OPACTION_POST){
				entity.setSxFw("0");
			}
		}
	}
}
