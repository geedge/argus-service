/**   
* @Title: ApiVersion.java 
* @Package com.nis.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月14日 下午3:48:50 
* @version V1.0   
*/
package com.nis.restful;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.Mapping;

/** 
* @ClassName: ApiVersion 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月14日 下午3:48:50 
* @version V1.0 
*/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
	/**
	 * 
	* @Title: value 
	* @Description: TODO(版本号) 
	* @param @return    入参
	* @return int    返回类型
	* @author （darnell） 
	* @throws
	* @date 2016年8月14日 下午3:49:20 
	* @version V1.0
	 */
	int value();

}
