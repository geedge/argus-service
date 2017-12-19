package com.nis.util;

import java.util.HashMap;
import java.util.Map;

import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.web.service.SaveRequestLogThread;

public class OracleErrorCodeUtil {
	/**
	 * 返回异常信息内容
	 * 
	 * @param e
	 * @return
	 */
	public static String getOraCode(Exception e) {
		if (e == null) {
			return "";
		}

		String errorMessage = e.getMessage();
		if (null != errorMessage && errorMessage.length() > 0) {
			int index = errorMessage.toUpperCase().indexOf("ORA-");
			if (index != -1) {
				return errorMessage.substring(index + 4, index + 9);

			}
		}
		return "";
	}

	/**
	 * 根据错误码抛出对应的异常,case内容为空的时候会按照下个case的异常内容来抛出异常，修改时请注意
	 */

	public static void throwExceptionInfo(SaveRequestLogThread thread, long time, String errorCode) {

		switch (errorCode) {

		case "00001":
			throw new RestServiceException(thread, time, "数据库对应的id已经存在!",
					RestBusinessCode.insert_data_repeat.getValue());
		case "00942":
		case "00903":
			throw new RestServiceException(thread, time, "数据库中未找到对应的表!",
					RestBusinessCode.param_formate_error.getValue());
		case "01401":
		case "12899":
			throw new RestServiceException(thread, time, "数据长度大于规定长度!",
					RestBusinessCode.param_formate_error.getValue());
		case "01400":
		case "01407":
			throw new RestServiceException(thread, time, "必须数据不能为空或数据不完整!",
					RestBusinessCode.param_formate_error.getValue());
		case "01722":
			throw new RestServiceException(thread, time, "无效的数字!", RestBusinessCode.param_formate_error.getValue());
		case "01465":
			throw new RestServiceException(thread, time, "无效的十六进制数字!", RestBusinessCode.param_formate_error.getValue());
		default:
			throw new RestServiceException(thread, time, "数据操作发生异常!", RestBusinessCode.unknow_error.getValue());

		}

	}

	public static Map<Integer, String> throwExceptionInfo(String errorCode) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		switch (errorCode) {
		case "00001":
			map.put(RestBusinessCode.insert_data_repeat.getValue(), "数据库对应的id已经存在!");
			return map;
		case "00942":
		case "00903":
			map.put(RestBusinessCode.param_formate_error.getValue(), "数据库中未找到对应的表!");
			return map;
		case "01401":
		case "12899":
			map.put(RestBusinessCode.param_formate_error.getValue(), "数据长度大于规定长度!");
			return map;
		case "01400":
		case "01407":
			map.put(RestBusinessCode.param_formate_error.getValue(), "必须数据不能为空或数据不完整!");
			return map;
		case "01722":
			map.put(RestBusinessCode.param_formate_error.getValue(), "无效的数字!");
			return map;
		case "01465":
			map.put(RestBusinessCode.param_formate_error.getValue(), "无效的十六进制数字!");
			return map;
		default:
			map.put(RestBusinessCode.unknow_error.getValue(), "数据操作发生异常!");
			return map;
		}

	}
}
