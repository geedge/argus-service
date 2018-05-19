/**   
* @Title: DefaultRestSuccessConverter.java 
* @Package com.nis.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月24日 下午4:24:22 
* @version V1.0   
*/
package com.nis.restful;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 
* @ClassName: DefaultRestSuccessConverter 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月24日 下午4:24:22 
* @version V1.0 
*/
public class DefaultRestSuccessConverter implements RestConverter<Map> {

	@SuppressWarnings("unchecked")
	@Override
	public Map convert(RestResult re) {
		Map successMap = new LinkedHashMap();
		successMap.put(RestConstants.REST_SERVICE_HTTP_STATUS, re.getStatus()
				.value());
		successMap.put(RestConstants.REST_SERVICE_BUSINESS_CODE, re.getBusinessCode()
				.getValue());
		successMap.put(RestConstants.REST_SERVICE_REASON, re.getBusinessCode()
				.getErrorReason());
		successMap.put(RestConstants.REST_SERVICE_MSG, re.getMsg());
		successMap.put(RestConstants.TRACE_CODE, re.getTraceCode());
		successMap.put(RestConstants.REST_SERVICE_URI, re.getFromUri());
		
		return successMap;
	}

	@Override
	public String convertToJson(RestResult re) {
		Map successMap = this.convert(re);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(successMap);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}


}
