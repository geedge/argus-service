package com.nis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nis.datasource.CustomerContextHolder;

public class DataSourceAInterceptor implements HandlerInterceptor {
	Logger logger = Logger.getLogger(DataSourceAInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.debug("开启数据源DATA_SOURCE_A---" + System.currentTimeMillis());
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_A);// 开启数据源A
		logger.debug("数据DATA_SOURCE_A源开启成功---" + System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		logger.info("postHandle---");

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		CustomerContextHolder.clearCustomerType();
		logger.info("释放数据源日志操作库---");
	}

}
