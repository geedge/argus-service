/**   
* @Title: WebConfig.java 
* @Package com.nis.restful 
* @Description: TODO(去掉前面说的<mvc:annotation-driven/>这个配置，我们通过JavaConfig的方式注入) 
* @author （darnell）  
* @date 2016年8月15日 上午10:34:18 
* @version V1.0   
*/
package com.nis.restful;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/** 
* @ClassName: WebConfig 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月15日 上午10:34:18 
* @version V1.0 
*/
@Configuration
public class WebConfig extends WebMvcConfigurationSupport{
	@Override
	@Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        return handlerMapping;
    }
}
