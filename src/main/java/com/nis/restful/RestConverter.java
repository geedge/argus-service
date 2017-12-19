package com.nis.restful;


public interface RestConverter<T> {

	/**
	 * 将错误对象转化成map，用于页面的输出
	 * @return
	 */
	
	T convert(RestResult re);
	
	/**
	 * 转化为json字串
	 * @param re
	 * @return
	 */
	String convertToJson(RestResult re);
	
}
