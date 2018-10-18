/**
 * 
 */
package com.nis.util;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @ClassName:JsonDateDeserializer
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年6月12日 上午11:06:03
 * @version V1.0
 */
public class JsonDateDeserializer extends JsonDeserializer<Date> {

//	private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		Date date = null;
		if (null!=p&&!"".equals(p.getText())) {
			date = com.zdjizhi.utils.DateUtils.convertStringToDate(p.getText(), com.zdjizhi.utils.DateUtils.YYYY_MM_DD_HH24_MM_SS);
		}
		return date;
	}

}
