package com.nis.restful;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("rawtypes")
@Component
public class DefaultRestErrorConverter implements RestConverter<Map> {

	@SuppressWarnings("unchecked")
	@Override
	public Map convert(RestResult re) {
		Map errorMap = new LinkedHashMap();
		errorMap.put(RestConstants.REST_SERVICE_HTTP_STATUS, re.getStatus()
				.value());
		errorMap.put(RestConstants.REST_SERVICE_BUSINESS_CODE, re.getBusinessCode()
				.getValue());
		errorMap.put(RestConstants.REST_SERVICE_REASON, re.getBusinessCode()
				.getErrorReason());
		errorMap.put(RestConstants.REST_SERVICE_MSG, re.getMsg());
		errorMap.put(RestConstants.REST_SERVICE_URI, re.getFromUri());
		
		if(re.getActiveSys() != null){
			errorMap.put(RestConstants.REST_SERVICE_ACTIVE_SYS, re.getActiveSys());
		}
		if(re.getLogSource() != null){
			errorMap.put(RestConstants.REST_SERVICE_LOG_SOURCE, re.getLogSource());
		}

		return errorMap;
	}

	@Override
	public String convertToJson(RestResult re) {
		Map errorMap = this.convert(re);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(errorMap);
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
