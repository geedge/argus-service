package com.nis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nis.datasource.CustomerContextHolder;

public class DataSourceDInterceptor implements HandlerInterceptor {
	Logger logger = Logger.getLogger(DataSourceDInterceptor.class);

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info("开启测试配置库---");
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_D);//开启数据源C
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		logger.info("postHandle---");

	}

	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		CustomerContextHolder.clearCustomerType();
		logger.info("释放测试配置库---");
	}

	
	

}
