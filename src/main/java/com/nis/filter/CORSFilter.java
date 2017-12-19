package com.nis.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nis.filter.wrapper.RestHttpServletRequestWrapper;
import com.nis.util.Constants;

/** 
* @ClassName: CORSFilter 
* @Description: TODO(解决跨域问题的过滤器) 
* @author (DDM)  
* @date 2016年9月20日 上午9:12:50 
* @version V1.0 
*/
@WebFilter
public class CORSFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		//将请求封装起来，使得body中的内容可以多次获取
		req=new RestHttpServletRequestWrapper((HttpServletRequest)req);
		HttpServletResponse response=(HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin",Constants.TARGET_URL);
		response.setHeader("Access-Control-Allow-Methods","POST,GET,PUT,OPT,IONS,DELETE");
		response.setHeader("Access-Control-Max-Age",Constants.ACCESS_CONTROL_MAX_AGE);
		response.setHeader("Access-Control-Allow-Headers","*");
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	
}
