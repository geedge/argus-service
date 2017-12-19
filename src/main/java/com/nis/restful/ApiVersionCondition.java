/**   
* @Title: ApiVersionCondition.java 
* @Package com.nis.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月15日 上午10:14:21 
* @version V1.0   
*/
package com.nis.restful;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

/** 
* @ClassName: ApiVersionCondition 
* @Description: TODO(定义一个条件筛选器,增加版本号匹配规则) 
* @author (darnell)  
* @date 2016年8月15日 上午10:14:21 
* @version V1.0 
*/
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
	// 路径中版本的前缀， 这里用 /v[1-9]/的形式
	private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)");
	private int apiVersion;
	
	public ApiVersionCondition(int apiVersion){
        this.apiVersion = apiVersion;
    }
	
	/** 
	* @return apiVersion 
	*/
	public int getApiVersion() {
		return apiVersion;
	}
	
	
	@Override
	public ApiVersionCondition combine(ApiVersionCondition other) {
		// 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(other.getApiVersion());
	}

	@Override
	public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
		// 优先匹配最新的版本号
        return other.getApiVersion() - this.apiVersion;
	}

	@Override
	public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
		System.out.println("&&&&&&&&&" + request.getPathInfo());
		Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getPathInfo());
		System.out.println("---"+apiVersion);
		if(m.find()){
            Integer version = Integer.valueOf(m.group(1));
            System.out.println("****"+version+"---"+apiVersion);
            if(version >= this.apiVersion) // 如果请求的版本号大于配置版本号， 则满足
                return this;
        }
		
		return null;
	}

}
