package com.nis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nis.datasource.CustomerContextHolder;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;

public class DataSourceCInterceptor implements HandlerInterceptor {
	Logger logger = Logger.getLogger(DataSourceCInterceptor.class);
	String searchActiveSys="";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
//		searchActiveSys=request.getParameter("searchActiveSys");
//		if(searchActiveSys == null 
//				|| !(Constants.ACTIVESYS_A.equals(searchActiveSys) 
//						|| Constants.ACTIVESYS_C.equals(searchActiveSys))
//							) searchActiveSys=Constants.ACTIVESYS_B;
//		if(Constants.ACTIVESYS_A.equals(searchActiveSys)){
//			logger.info("开启数据源日志A操作库---"+System.currentTimeMillis());
//			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_F);//开启数据源F
//		}else if(Constants.ACTIVESYS_C.equals(searchActiveSys)){
//			logger.info("开启数据源日志C操作库---"+System.currentTimeMillis());
//			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_G);//开启数据源F
//		}else{
			logger.info("开启数据源日志操作库---"+System.currentTimeMillis());
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_C);//开启数据源C
//		}
		logger.info("日志数据源开启成功---"+System.currentTimeMillis());
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
		logger.info("释放数据源日志操作库---");
	}

	
	

}
