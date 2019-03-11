package com.nis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nis.datasource.CustomerContextHolder;

public class DataSourceBInterceptor implements HandlerInterceptor {
	private static final Logger logger =LoggerFactory.getLogger(DataSourceBInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info("开启数据源配置操作库---");
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_B);//开启数据源B(我们自己的clickhouse)
		logger.info("mysql日志数据源开启成功---"+System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("postHandle---");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		CustomerContextHolder.clearCustomerType();
		logger.info("释放数据源配置操作库---");
	}

	
}
