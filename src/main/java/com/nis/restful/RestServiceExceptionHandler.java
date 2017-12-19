/**   
* @Title: RestServiceExceptionHandler.java 
* @Package com.nis.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月24日 上午11:31:47 
* @version V1.0   
*/
package com.nis.restful;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
* @ClassName: RestServiceExceptionHandler 
* @Description: TODO(rest异常处理，需要判断是否为rest异常，如果不是，则交给正常页面异常处理，否则抛出json格式错误.判断是否rest异常需要具体项目具体实现，默认统一为true) 
* @author (darnell)  
* @date 2016年8月24日 上午11:31:47 
* @version V1.0 
*/
public class RestServiceExceptionHandler extends DefaultHandlerExceptionResolver{
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(RestServiceExceptionHandler.class);
			
	/**
	 * 通过xml文件配置，可能存在自定义的异常类型
	 */
	private RestErrorResolver errorResolver;
	
	@Autowired
	private RestConverter<?> errorConverter; //错误信息转换器
	
	/**
	 * 确认是否rest服务方法，需要子类重写
	 * 如果不是则不做处理，直接交给父类处理
	 * @param request
	 * @return 是否为rest服务
	 */
	public boolean isRestServiceException(HttpServletRequest request){
		return true;
	}
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		if (isRestServiceException(request)) {
			RestResult error = errorResolver.resolveError(request, handler, ex);
			response.setStatus(error.getStatus().value());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonGenerator jsonGenerator = mapper.getFactory()
						.createGenerator(response.getOutputStream(),
								JsonEncoding.UTF8);
				mapper.writeValue(jsonGenerator, errorConverter.convert(error));
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new ModelAndView();
		} else {
			return super.doResolveException(request, response, handler, ex);
		}
			
	}

	public RestErrorResolver getErrorResolver() {
		return errorResolver;
	}

	public void setErrorResolver(RestErrorResolver errorResolver) {
		this.errorResolver = errorResolver;
	}
	
}
