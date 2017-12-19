/**   
* @Title: CustomRequestMappingHandlerMapping.java 
* @Package com.nis.restful 
* @Description: TODO(扩展原有hadlerMapping) 
* @author （darnell）  
* @date 2016年8月15日 上午10:26:34 
* @version V1.0   
*/
package com.nis.restful;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


/** 
* @ClassName: CustomRequestMappingHandlerMapping 
* @Description: TODO(影射扩展，版本号自定义) 
* @author (darnell)  
* @date 2016年8月15日 上午10:26:34 
* @version V1.0 
*/
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
	@Override
    protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createCondition(apiVersion);
    }
 
    @Override
    protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
    }
     
    private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value());
    }
}
