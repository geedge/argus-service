package com.nis.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * 
 * @ClassName: JsonDateValueProcessor
 * @Description: TODO(处理对象中日期类型转换为json时格式问题,后期会删除)
 * @author (rkg)
 * @date 2016年9月27日下午3:43:48
 * @version V1.0
 */
public class JsonDateValueProcessor implements JsonValueProcessor {
	private String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	//private String format = "yyyy-MM-dd";

	public JsonDateValueProcessor() {
		super();
	}

	public JsonDateValueProcessor(String format) {
		super();
		this.format = format;
	}

	@Override
	public Object processArrayValue(Object parm, JsonConfig arg1) {
		return process(parm);
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig arg2) {
		return process(value);
	}

	private Object process(Object value) {
		if (value instanceof Date) {
			long time= ((Date) value).getTime()-8*60*60*1000;
			//value=
			//SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(time);

		}
		return value == null ? "" : value.toString();

	}

}
