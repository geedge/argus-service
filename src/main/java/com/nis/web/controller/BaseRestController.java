/**   
* @Title: BaseRestController.java 
* @Package com.nis.web.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月24日 下午5:30:23 
* @version V1.0   
*/
package com.nis.web.controller;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nis.domain.restful.ConfigCommonSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestConstants;
import com.nis.restful.RestResult;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;

/**
 * @ClassName: BaseRestController
 * @Description: TODO(Rest服务基础业务控制类)
 * @author (darnell)
 * @date 2016年8月24日 下午5:30:23
 * @version V1.0
 */
public class BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	/**
	 * 
	 * @Title: serviceResponse
	 * @Description: TODO(服务成功结果响应)
	 * @param @param
	 *            request
	 * @param @param
	 *            response
	 * @param @param
	 *            msg
	 * @param @return
	 *            入参
	 * @return Map 返回类型
	 * @author （darnell）
	 * @throws @date
	 *             2016年8月24日 下午7:53:37
	 * @version V1.0
	 */

	public Map serviceResponse(SaveRequestLogThread thread, long time, HttpServletRequest request,
			HttpServletResponse response, String msg) {
		RestResult restResult = new RestResult();
		String requestMethod = request.getMethod();
		if (requestMethod.equals(RequestMethod.GET.name())) {
			restResult.setStatus(HttpStatus.OK);
			restResult.setBusinessCode(RestBusinessCode.query_success);
		} else if (requestMethod.equals(RequestMethod.DELETE.name())) {
			restResult.setStatus(HttpStatus.NO_CONTENT);
			restResult.setBusinessCode(RestBusinessCode.delete_success);
		} else if (requestMethod.equals(RequestMethod.PUT.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.update_success);
		} else if (requestMethod.equals(RequestMethod.POST.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.add_success);
		}
		restResult.setFromUri(request.getRequestURI());
		restResult.setMsg(msg);
		restResult.setTraceCode(thread.getTraceCode());
		thread.setConsumerTime(time);
		thread.setBusinessCode(restResult.getBusinessCode().getValue());
		new Thread(thread).start();
		return convert(restResult);
	}

	/**
	 * 
	 * @Title: serviceResponse
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            request
	 * @param @param
	 *            response
	 * @param @param
	 *            msg
	 * @param @param
	 *            data
	 * @param @return
	 *            入参
	 * @return Map 返回类型
	 * @author （darnell）
	 * @throws @date
	 *             2016年8月24日 下午8:40:44
	 * @version V1.0
	 */
	public Map serviceResponse(SaveRequestLogThread thread, long time, HttpServletRequest request,
			HttpServletResponse response, String msg, Object data) {
		RestResult restResult = new RestResult();
		String requestMethod = request.getMethod();
		if (requestMethod.equals(RequestMethod.GET.name())) {
			restResult.setStatus(HttpStatus.OK);
			restResult.setBusinessCode(RestBusinessCode.query_success);
		} else if (requestMethod.equals(RequestMethod.DELETE.name())) {
			restResult.setStatus(HttpStatus.NO_CONTENT);
			restResult.setBusinessCode(RestBusinessCode.delete_success);
		} else if (requestMethod.equals(RequestMethod.PUT.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.update_success);
		} else if (requestMethod.equals(RequestMethod.POST.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.add_success);
		}
		restResult.setFromUri(request.getRequestURI());
		restResult.setData(data);
		restResult.setMsg(msg);
		restResult.setTraceCode(thread.getTraceCode());
		thread.setConsumerTime(time);
		thread.setBusinessCode(restResult.getBusinessCode().getValue());
		new Thread(thread).start();
		return convert(restResult);
	}
	
	
	
	
	/**
	 * 不将日志插入到数据库中,只返回到前台
	 * @param time
	 * @param request
	 * @param response
	 * @param msg
	 * @param data
	 * @return
	 */
	public Map testServiceResponse(long time, HttpServletRequest request,
			HttpServletResponse response, String msg, Object data) {
		RestResult restResult = new RestResult();
		String requestMethod = request.getMethod();
		if (requestMethod.equals(RequestMethod.GET.name())) {
			restResult.setStatus(HttpStatus.OK);
			restResult.setBusinessCode(RestBusinessCode.query_success);
		} else if (requestMethod.equals(RequestMethod.DELETE.name())) {
			restResult.setStatus(HttpStatus.NO_CONTENT);
			restResult.setBusinessCode(RestBusinessCode.delete_success);
		} else if (requestMethod.equals(RequestMethod.PUT.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.update_success);
		} else if (requestMethod.equals(RequestMethod.POST.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.add_success);
		}
		restResult.setFromUri(request.getRequestURI());
		restResult.setData(data);
		restResult.setMsg(msg);
		return convert(restResult);
	}

	/**
	 * 
	 * @Title: serviceResponse
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            request
	 * @param @param
	 *            response
	 * @param @param
	 *            msg
	 * @param @param
	 *            data
	 * @param @return
	 *            入参
	 * @return Map 返回类型
	 * @author （darnell）
	 * @throws @date
	 *             2016年8月24日 下午8:40:44
	 * @version V1.0
	 */
	public Map compileServiceResponse(SaveRequestLogThread thread, long time, HttpServletRequest request,
			HttpServletResponse response, String msg, Object data) {
		RestResult restResult = new RestResult();
		String requestMethod = request.getMethod();
		if (requestMethod.equals(RequestMethod.GET.name())) {
			restResult.setStatus(HttpStatus.OK);
			restResult.setBusinessCode(RestBusinessCode.query_success);
		} else if (requestMethod.equals(RequestMethod.DELETE.name())) {
			restResult.setStatus(HttpStatus.NO_CONTENT);
			restResult.setBusinessCode(RestBusinessCode.delete_success);
		} else if (requestMethod.equals(RequestMethod.PUT.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.update_success);
		} else if (requestMethod.equals(RequestMethod.POST.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.add_success);
		}
		restResult.setFromUri(request.getRequestURI());
		restResult.setData(data);
		restResult.setMsg(msg);
		restResult.setTraceCode(thread.getTraceCode());
		thread.setConsumerTime(time);
		thread.setExceptionInfo(msg);
		thread.setBusinessCode(restResult.getBusinessCode().getValue());
		new Thread(thread).start();
		return convert(restResult);
	}

    /**
     * 日志结果响应格式规范
     * @param auditLogThread
     * @param executedTime
     * @param request
     * @param msg
     * @param data
     * @param logSource
     * @return
     */
	public Map serviceLogResponse(SaveRequestLogThread auditLogThread, long executedTime, HttpServletRequest request,
                                  String msg, Object data, Integer logSource) {
		RestResult restResult = new RestResult();
		String requestMethod = request.getMethod();
		if (requestMethod.equals(RequestMethod.GET.name())) {
			restResult.setStatus(HttpStatus.OK);
			restResult.setBusinessCode(RestBusinessCode.query_success);
		} else if (requestMethod.equals(RequestMethod.DELETE.name())) {
			restResult.setStatus(HttpStatus.NO_CONTENT);
			restResult.setBusinessCode(RestBusinessCode.delete_success);
		} else if (requestMethod.equals(RequestMethod.PUT.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.update_success);
		} else if (requestMethod.equals(RequestMethod.POST.name())) {
			restResult.setStatus(HttpStatus.CREATED);
			restResult.setBusinessCode(RestBusinessCode.add_success);
		}
		restResult.setFromUri(request.getRequestURI());
		restResult.setData(data);
		restResult.setMsg(msg);
		restResult.setLogSource(logSource);
		restResult.setTraceCode(auditLogThread.getTraceCode());
        auditLogThread.setConsumerTime(executedTime);
        auditLogThread.setBusinessCode(restResult.getBusinessCode().getValue());
		new Thread(auditLogThread).start();
		return convert(restResult);
	}

	private Map convert(RestResult re) {
		Map successMap = new LinkedHashMap();
		successMap.put(RestConstants.REST_SERVICE_HTTP_STATUS, re.getStatus().value());
		successMap.put(RestConstants.REST_SERVICE_BUSINESS_CODE, re.getBusinessCode().getValue());
		successMap.put(RestConstants.REST_SERVICE_REASON, re.getBusinessCode().getErrorReason());
		successMap.put(RestConstants.REST_SERVICE_MSG, re.getMsg());
		successMap.put(RestConstants.REST_SERVICE_URI, re.getFromUri());
		if (re.getActiveSys() != null) {
			successMap.put(RestConstants.REST_SERVICE_ACTIVE_SYS, re.getActiveSys());
		}
		if (re.getLogSource() != null) {
			successMap.put(RestConstants.REST_SERVICE_LOG_SOURCE, re.getLogSource());
		}
		successMap.put(RestConstants.TRACE_CODE, re.getTraceCode());
		successMap.put(RestConstants.REST_SERVICE_DATA, re.getData());
		return successMap;
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	/**
	 * 
	 * checkOpAction(校验请求body的opAction) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param thread
	 *            记录日志线程
	 * @param opAction
	 * @param need
	 *            void
	 * @exception @since
	 *                1.0.0
	 */
	public void checkOpAction(SaveRequestLogThread thread, long time, int opAction, int need) {
		if (need != opAction) {
			switch (need) {
			case Constants.OPACTION_POST:
				throw new RestServiceException(thread, time,
						"操作行为错误,您使用的是POST请求,POST请求对应的opAction为" + Constants.OPACTION_POST + ",您请求中的opAction为" + opAction
								+ ",对应的请求方式为" + convertOpAction(opAction) + "!",
						RestBusinessCode.op_action_error.getValue());
			case Constants.OPACTION_PUT:
				throw new RestServiceException(thread, time,
						"操作行为错误,您使用的是PUT请求,PUT请求对应的opAction为" + Constants.OPACTION_PUT + ",您请求中的opAction为" + opAction
								+ ",对应的请求方式为" + convertOpAction(opAction) + "!",
						RestBusinessCode.op_action_error.getValue());
			case Constants.OPACTION_DELETE:
				throw new RestServiceException(thread, time,
						"操作行为错误,您使用的是DELETE请求,DELETE请求对应的opAction为" + Constants.OPACTION_DELETE + ",您请求中的opAction为"
								+ opAction + ",对应的请求方式为" + convertOpAction(opAction) + "!",
						RestBusinessCode.op_action_error.getValue());
			case Constants.OPACTION_GET:
				throw new RestServiceException(thread, time,
						"操作行为错误,您使用的是GET请求,GET请求对应的opAction为" + Constants.OPACTION_GET + ",您请求中的opAction为" + opAction
								+ ",对应的请求方式为" + convertOpAction(opAction) + "!",
						RestBusinessCode.op_action_error.getValue());
			default:
				throw new RestServiceException(thread, time, "操作行为错误,未找到对应的服务！",
						RestBusinessCode.op_action_error.getValue());
			}
		}
	}

	private String convertOpAction(int opAction) {
		switch (opAction) {
		case Constants.OPACTION_POST:
			return "POST";
		case Constants.OPACTION_PUT:
			return "PUT";
		case Constants.OPACTION_DELETE:
			return "DELETE";
		case Constants.OPACTION_GET:
			return "GET";
		default:
			return "未知";
		}
	}

	protected SaveRequestLogThread saveRequestLog(ServicesRequestLogService service, int opAction,
			HttpServletRequest request, Object data) {
		logger.debug("SaveRequestLogThread初始化开始----" + System.currentTimeMillis());
		SaveRequestLogThread thread = SaveRequestLogThread.getNewSaveRequestLogThread(request);
		thread.setService(service);
		thread.setOpAction(opAction);
		thread.setTraceCode(sdf.format(new Date())+(Math.round((Math.random()*9+1)*10000)+""));
		if (data != null && ConfigCommonSource.class.isAssignableFrom(data.getClass())) {
			ConfigCommonSource source = (ConfigCommonSource) data;
			thread.setOperator(source.getOperator());
			thread.setVersion(source.getVersion());
			thread.setOpTime(source.getOpTime());
		}else{
			thread.setOperator("admin");
			thread.setOpTime(new Date());
		}
		logger.info("SaveRequestLogThread初始化结束----" + System.currentTimeMillis());
		return thread;

	}

	// 文件上传用
	protected SaveRequestLogThread saveRequestLog(ServicesRequestLogService service, int opAction,
			HttpServletRequest request, Object data, Object fileInfo) {
		SaveRequestLogThread thread = SaveRequestLogThread.getNewSaveRequestLogThread(request);
		thread.setService(service);
	//	thread.setContent(fileInfo);
		thread.setOpAction(opAction);
		thread.setTraceCode(sdf.format(new Date())+(Math.round((Math.random()*9+1)*10000)+""));
		if (data != null && ConfigCommonSource.class.isAssignableFrom(data.getClass())) {
			System.out.println(data.getClass().getSimpleName());
			ConfigCommonSource source = (ConfigCommonSource) data;
			thread.setOperator(source.getOperator());
			thread.setVersion(source.getVersion());
			thread.setOpTime(source.getOpTime());
		}
		return thread;

	}
}
