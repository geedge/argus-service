package com.nis.web.controller.restful.jk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.jk.JkFdZb;
import com.nis.domain.restful.jk.JkFdZbSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.jk.JkFdZbService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * @ClassName: JkFdZbController
 * @Description: 静态控管封堵/解封
 * @author (DDM)
 * @date 2016年10月20日 上午10:20:33
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/jk/v1")
@Api(value = "JkFdZbController", description = "静态控管封堵/解封")
public class JkFdZbController extends BaseRestController {
	Logger logger = LoggerFactory.getLogger(JkFdZbController.class);
	SimpleDateFormat ymdhms=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	ServicesRequestLogService servicesRequestLogService;
	@Autowired
	protected JkFdZbService jkFdZbService;
	
	/**
	 * saveJkFdZbBatch(多条新增)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JkFdZbSource
	 * @param request
	 * @param response
	 * @return  Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/jkFdZbSources", method = RequestMethod.POST)
	@ApiOperation(value = "静态控管封堵", httpMethod = "POST", notes = "save provider info")
	public Map saveJkFdZbBatch(@RequestBody JkFdZbSource jkFdZbSource, HttpServletRequest request, HttpServletResponse response) {
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_POST,request, jkFdZbSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start, jkFdZbSource.getOpAction(), Constants.OPACTION_POST);
		validJkFdZb(thread,System.currentTimeMillis()-start,jkFdZbSource.getJkFdZbList(),Constants.OPACTION_POST);
		try{
			Date date=new Date();
			if(jkFdZbSource.getJkFdZbList() != null && jkFdZbSource.getJkFdZbList().size() >0){
				for (JkFdZb jkFdZb : jkFdZbSource.getJkFdZbList()) {
					jkFdZb.setLastUpdate(date);
				}
			}
			jkFdZbService.saveJkFdZbBatch(jkFdZbSource.getJkFdZbList());
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage()+"\r\n"+e.getCause());
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"静态控管封堵失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"静态控管封堵成功",jkFdZbSource.getJkFdZbList());
	}
	/**
	 * 
	 * updateJkFdZbBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param JkFdZbSource
	 * @param request
	 * @param response
	 * @return 
	 *Map
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/jkFdZbSources", method = RequestMethod.PUT)
	@ApiOperation(value = "静态控管解封", httpMethod = "PUT", notes = "update provider info")
	public Map updateJkFdZbBatch(@RequestBody JkFdZbSource jkFdZbSource, HttpServletRequest request, HttpServletResponse response){
		long start=System.currentTimeMillis();
		SaveRequestLogThread thread=super.saveRequestLog(servicesRequestLogService,Constants.OPACTION_PUT,request, jkFdZbSource);
		super.checkOpAction(thread,System.currentTimeMillis()-start,jkFdZbSource.getOpAction(), Constants.OPACTION_PUT);
		
		validJkFdZb(thread,System.currentTimeMillis()-start,jkFdZbSource.getJkFdZbList(),Constants.OPACTION_PUT);
		
		try{
			Date date=new Date();
			if(jkFdZbSource.getJkFdZbList() != null && jkFdZbSource.getJkFdZbList().size() >0){
				for (JkFdZb jkFdZb : jkFdZbSource.getJkFdZbList()) {
					jkFdZb.setLastUpdate(date);
				}
			}
			jkFdZbService.updateJkFdZbBatch(jkFdZbSource.getJkFdZbList());
		}catch(Exception e){
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage()+"\r\n"+e.getCause());
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"静态控管解封失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"静态控管解封成功",jkFdZbSource.getJkFdZbList());
	}
	/**
	 *	校验是否存在无效数据
	 * 
	 * @Title: validJkFdZb
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param thread
	 * @param @param start
	 * @param @param jkFdZbList
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public void validJkFdZb(SaveRequestLogThread thread,long start, List<JkFdZb>  jkFdZbList,int opAction){
		String errorInfo = "";
		
		if (jkFdZbList.size() <= 0) {
			errorInfo = "静控封堵记录不能为空";
			thread.setExceptionInfo(errorInfo);
			throw new RestServiceException(thread,System.currentTimeMillis()-start,errorInfo, RestBusinessCode.missing_args.getValue());
		}else{
			for (JkFdZb jkFdZb : jkFdZbList) {
				if(opAction==Constants.OPACTION_POST&&jkFdZb != null && jkFdZb.getSfFd() !=Long.valueOf(Constants.YES)){
					errorInfo = "只能新增有效数据";
					thread.setExceptionInfo(errorInfo);
					throw new RestServiceException(thread,System.currentTimeMillis()-start,errorInfo, RestBusinessCode.wrong_range.getValue());
					
				}else if(opAction==Constants.OPACTION_PUT && jkFdZb != null && jkFdZb.getSfFd() !=Long.valueOf(Constants.NO)){
					errorInfo = "只允许将有效数据更改为无效数据";
					thread.setExceptionInfo(errorInfo);
					throw new RestServiceException(thread,System.currentTimeMillis()-start,errorInfo, RestBusinessCode.wrong_range.getValue());
					
				}
				if(!"".equals(errorInfo)){
					break;
				}
				if(opAction==Constants.OPACTION_POST){
					jkFdZb.setSxFw("0");
				}
			}
		}
	}
	

}
