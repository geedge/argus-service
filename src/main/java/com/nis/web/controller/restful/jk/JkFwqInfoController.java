/**
 *@Title: JkFwqInfoController.java
 *@Package com.nis.web.controller.restful 
 *@Description TODO
 *@author (zbc)
 *@date 2016年10月19日 下午20:04:16
 *@version V1.0
 */
package com.nis.web.controller.restful.jk;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.jk.JkFfjInfo;
import com.nis.domain.restful.jk.JkFfjInfoSource;
import com.nis.domain.restful.jk.JkFwqInfo;
import com.nis.domain.restful.jk.JkFwqInfoSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.jk.JkFwqInfoService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: JkFwqInfoController.java 
 * @Description: TODO
 * @author (zbc)
 * @date 2016年10月19日 下午20:04:16
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/jk/v1")
public class JkFwqInfoController  extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	protected JkFwqInfoService jkFwqInfoService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	
	
	@RequestMapping(value = "/jkFwqSources", method = RequestMethod.POST)
	@ApiOperation(value = "保存静态控管服务器信息", httpMethod = "POST", notes = "save server info")
	public Map saveJkFfjInfoBatch(@RequestBody JkFwqInfoSource jkFwqSource, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request, jkFwqSource);
		super.checkOpAction(thread, System.currentTimeMillis() - start, jkFwqSource.getOpAction(), Constants.OPACTION_POST);
		validJkFdZb(thread,System.currentTimeMillis()-start,jkFwqSource.getJkFwqInfoList(),Constants.OPACTION_POST);
		try {
			jkFwqInfoService.savejkFwqInfoBatch(jkFwqSource.getJkFwqInfoList());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage()+"\r\n"+e.getCause());
			if(e instanceof RestServiceException) throw e;
			String info=OracleErrorCodeUtil.getOraCode(e);
			if(!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread,System.currentTimeMillis()-start,info);
			else
				throw new RestServiceException(thread,System.currentTimeMillis()-start,"保存静态控管服务器信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"保存静态控管服务器信息成功",jkFwqSource.getJkFwqInfoList());
	}
	
	@RequestMapping(value = "/jkFwqSources", method = RequestMethod.PUT)
	@ApiOperation(value = "更新静态控管服务器信息", httpMethod = "PUT", notes = "update server info")
	public Map updateJkFfjInfoBatch(@RequestBody JkFwqInfoSource jkFwqSource, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request, jkFwqSource);
		super.checkOpAction(thread, System.currentTimeMillis() - start, jkFwqSource.getOpAction(), Constants.OPACTION_PUT);
		validJkFdZb(thread,System.currentTimeMillis()-start,jkFwqSource.getJkFwqInfoList(),Constants.OPACTION_PUT);
		try {
			jkFwqInfoService.updatejkFwqInfoBatch(jkFwqSource.getJkFwqInfoList());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			e.printStackTrace();
			logger.error(e.getMessage()+"\r\n"+e.getCause());
			if(e instanceof RestServiceException) throw e;
			String info = OracleErrorCodeUtil.getOraCode(e);
			if (!StringUtil.isEmpty(info))
				OracleErrorCodeUtil.throwExceptionInfo(thread, System.currentTimeMillis() - start, info);
			else
				throw new RestServiceException(thread, System.currentTimeMillis() - start, "更新静态控管服务器信息失败", RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread,System.currentTimeMillis()-start,request,response,"更新静态控管服务器信息成功",jkFwqSource.getJkFwqInfoList());
	}
	
	private void validJkFdZb(SaveRequestLogThread thread, long start, List<JkFwqInfo> jkFwqList, int opAction) {
		String errorInfo = "";
		
		if (jkFwqList.size() <= 0) {
			errorInfo = "静态控管服务器信息不能为空";
			thread.setExceptionInfo(errorInfo);
			throw new RestServiceException(thread,System.currentTimeMillis()-start,errorInfo, RestBusinessCode.missing_args.getValue());
		}else{
			for (JkFwqInfo jkFwq : jkFwqList) {
				if(opAction==Constants.OPACTION_POST&&jkFwq != null && jkFwq.getSfSx()!=Long.valueOf(Constants.YES)){
					errorInfo = "只能新增有效数据";
					thread.setExceptionInfo(errorInfo);
					throw new RestServiceException(thread,System.currentTimeMillis()-start,errorInfo, RestBusinessCode.wrong_range.getValue());
					
				}else if(opAction==Constants.OPACTION_PUT&&jkFwq != null && jkFwq.getSfSx() !=Long.valueOf(Constants.NO)){
					errorInfo = "只允许将有效数据更改为无效数据";
					thread.setExceptionInfo(errorInfo);
					throw new RestServiceException(thread,System.currentTimeMillis()-start,errorInfo, RestBusinessCode.wrong_range.getValue());
					
				}
				if(!"".equals(errorInfo)){
					break;
				}
				if(opAction==Constants.OPACTION_POST){
					jkFwq.setSxFw("0");
				}
			}
		}
	}
}
