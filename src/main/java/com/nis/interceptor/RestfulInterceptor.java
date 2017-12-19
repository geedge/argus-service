/**   
* @Title: RestfulInterceptor.java 
* @Package com.nis.interceptor 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月14日 下午1:55:50 
* @version V1.0   
*/
package com.nis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nis.web.service.BaseService;

/** 
* @ClassName: RestfulInterceptor 
* @Description: TODO(Restful 服务的拦截器) 
* @author (darnell)  
* @date 2016年8月14日 下午1:55:50 
* @version V1.0 
*/
public class RestfulInterceptor extends BaseService implements HandlerInterceptor {
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("---RestFul服务启动");
		return true;
	}


	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		logger.info(request.getRequestURI());
		if (modelAndView != null){
			logger.info("ViewName: " + modelAndView.getViewName());
		}

	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.info("---RestFul服务结束");

	}

	
}
