/**
 *@Title: JkDmbCkController.java
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

import com.nis.domain.restful.jk.JkDmbCk;
import com.nis.domain.restful.jk.JkDmbCkSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.jk.JkDmbCkService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: JkDmbCkController.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:58:16
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/jk/v1")
public class JkDmbCkController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected JkDmbCkService jkDmbCkService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	/**
	 * saveDmbCkBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JkDmbCkSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dmbCkSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存因特网国际出入口", httpMethod = "POST", notes = "保存因特网国际出入口")
	public Map saveDmbCkBatch(@RequestBody JkDmbCkSource dmbCkSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, dmbCkSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start, dmbCkSource.getOpAction(), Constants.OPACTION_POST);
		try{
			checkData(thread,start,dmbCkSource,Constants.OPACTION_POST);
			jkDmbCkService.saveDmbCkBatch(dmbCkSource.getDmbCkList());
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存因特网国际出入口失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存因特网国际出入口成功",dmbCkSource.getDmbCkList());
	}
	/**
	 * 
	 * updateDmbCkBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JkDmbCkSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dmbCkSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新因特网国际出入口", httpMethod = "PUT", notes = "更新因特网国际出入口")
	public Map updateDmbCkBatch(@RequestBody JkDmbCkSource dmbCkSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, dmbCkSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dmbCkSource.getOpAction(), Constants.OPACTION_PUT);
		try{
			checkData(thread,start,dmbCkSource,Constants.OPACTION_PUT);
			jkDmbCkService.updateDmbCkBatch(dmbCkSource.getDmbCkList());
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e);
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"更新因特网国际出入口失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新因特网国际出入口成功",dmbCkSource.getDmbCkList());
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
//	@RequestMapping(value = "/dmbCkSources/{id}", method = RequestMethod.DELETE)
//	@ApiOperation(value = "删除因特网国际出入口", httpMethod = "DELETE", notes = "删除因特网国际出入口")
	public Map deleteDmbCk(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){	
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, id);
		try{
				jkDmbCkService.removeDmbCk(id);
			}catch(Exception e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				e.printStackTrace();
				logger.error(e);
				String info=OracleErrorCodeUtil.getOraCode(e);
				if(!StringUtil.isEmpty(info))
					OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
				else
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除因特网国际出入口失败", RestBusinessCode.unknow_error.getValue());
				
			}
				
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"删除成功",id);					
	}
	/**
	 * 
	 * deleteDmbCk(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JkDmbCkSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
//	@RequestMapping(value = "/dmbCkSources", method = RequestMethod.DELETE)
//	@ApiOperation(value = "删除因特网国际出入口", httpMethod = "DELETE", notes = "删除因特网国际出入口")
	public Map deleteDmbCk(@RequestBody JkDmbCkSource dmbCkSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_DELETE,request, dmbCkSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,dmbCkSource.getOpAction(), Constants.OPACTION_DELETE);
			try{
				jkDmbCkService.removeDmbCkBatch(dmbCkSource.getDmbCkList());
			}catch(Exception e){
				thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
				e.printStackTrace();
				logger.error(e);
				String info=OracleErrorCodeUtil.getOraCode(e);
				if(!StringUtil.isEmpty(info))
					OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
				else
					throw new RestServiceException(thread,System.currentTimeMillis()-start,"删除因特网国际出入口失败", RestBusinessCode.unknow_error.getValue());
			}
			return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"批量删除成功",dmbCkSource.getDmbCkList());
	}
	private void checkData(SaveRequestLogThread thread,long start,JkDmbCkSource dmbCkSource,int opAction){
		if(dmbCkSource.getDmbCkList().size()==0){
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"因特网国际出入口不能为空", RestBusinessCode.unknow_error.getValue());
		}
		for(JkDmbCk e:dmbCkSource.getDmbCkList()){
			if(opAction==Constants.OPACTION_POST&&e!=null&&e.getSfSx()!=Long.valueOf(Constants.YES)){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"只能新增有效数据", RestBusinessCode.wrong_range.getValue());
			}else if(opAction==Constants.OPACTION_PUT&&e!=null&&e.getSfSx()!=Long.valueOf(Constants.NO)){
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"只允许将有效数据更改为无效数据", RestBusinessCode.wrong_range.getValue());
			}
			if(opAction==Constants.OPACTION_POST){
				e.setSxFw("0");
			}
			
		}
	}
}
