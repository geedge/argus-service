/**   
* @Title: RestErrorResolver.java 
* @Package com.nis.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月24日 上午11:35:18 
* @version V1.0   
*/
package com.nis.restful;

import javax.servlet.http.HttpServletRequest;

/** 
* @ClassName: RestErrorResolver 
* @Description: TODO(异常和错误对象的转换接口) 
* @author (darnell)  
* @date 2016年8月24日 上午11:35:18 
* @version V1.0 
*/
public interface RestErrorResolver {
	/**
	 * 
	 * @param request 请求
	 * @param handler 处理方法
	 * @param ex 异常
	 * @return
	 */
	RestResult resolveError(HttpServletRequest request, Object handler, Exception ex);
}
