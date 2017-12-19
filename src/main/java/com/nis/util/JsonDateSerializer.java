/**
 *@Title: JsonDateSerializer.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月9日 下午8:36:59
 *@version  版本号
 */
package com.nis.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nis.util.DateUtil;

/**
 * @ClassName: JsonDateSerializer.java 
 * @Description: TODO 用于非get请求时讲返回结果呈现界面时的Json格式转换，用法 在Date类型的get方法上加上 @JsonSerialize(using=JsonDateSerializer.class)
 * @author (wx)
 * @date 2016年9月9日 下午8:36:59
 * @version V1.0
 */
public class JsonDateSerializer extends JsonSerializer<Date> {
	private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		//gen.writeString(sdf.format(date));
		gen.writeString(DateUtil.getFormatDate(date, DateUtil.YYYY_MM_DD_HH24_MM_SS));
	}

}
