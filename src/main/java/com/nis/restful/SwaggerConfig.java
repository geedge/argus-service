/**   
* @Title: SwaggerConfig.java 
* @Package com.nis.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月15日 下午2:16:51 
* @version V1.0   
*/
package com.nis.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

/** 
* @ClassName: SwaggerConfig 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月15日 下午2:16:51 
* @version V1.0 
*/
@Configuration
@EnableSwagger
public class SwaggerConfig {
	 private SpringSwaggerConfig springSwaggerConfig;
	 @Autowired
	    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig)
	    {
	        this.springSwaggerConfig = springSwaggerConfig;
	    }

	    /**
	     * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc
	     * framework - allowing for multiple swagger groups i.e. same code base
	     * multiple swagger resource listings.
	     */
	    @Bean
	    public SwaggerSpringMvcPlugin customImplementation()
	    {
	        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
	                .apiInfo(apiInfo())
	                .includePatterns(".service*.*")//api过滤
	                .swaggerGroup("XmPlatform")
	                .apiVersion("1.0");
	               // .includePatterns(".*?");
	    }

	    private ApiInfo apiInfo()
	    {
	    	ApiInfo apiInfo = new ApiInfo(
	                "文本综合业务 API",
	                "API Document 管理",
	                "V1.0",
	                "doufenghu@iie.ac.cn",
	                "My Apps API Licence Type",
	                "http://127.0.0.1:8080/gk/swagger/");
	        return apiInfo;
	    }
}
