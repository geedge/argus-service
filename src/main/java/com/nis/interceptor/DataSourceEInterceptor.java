package com.nis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nis.datasource.CustomerContextHolder;

public class DataSourceEInterceptor implements HandlerInterceptor {
	Logger logger = Logger.getLogger(DataSourceEInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		CustomerContextHolder.clearCustomerType();
		logger.info("释放数据静控操作库---");
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		logger.info("postHandle---");
		
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		logger.info("开启数据源静控操作库---");
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_E);//开启数据源C
		
		return true;
	}

}
