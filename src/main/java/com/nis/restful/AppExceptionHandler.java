/**   
* @Title: AppExceptionHandler.java 
* @Package com.nis.exceptions 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月24日 上午11:29:42 
* @version V1.0   
*/
package com.nis.restful;

import javax.servlet.http.HttpServletRequest;

import com.nis.util.Configurations;

/** 
* @ClassName: AppExceptionHandler 
* @Description: TODO(异常接收) 
* @author (darnell)  
* @date 2016年8月24日 上午11:29:42 
* @version V1.0 
*/
public class AppExceptionHandler extends RestServiceExceptionHandler{
	@Override
	public boolean isRestServiceException(HttpServletRequest request){
		String restServicePath = Configurations.getStringProperty("servicePath", "service");
		if(request.getServletPath().startsWith(restServicePath)) {
			return true;
		} else {
			return false;
		}
	}
}
