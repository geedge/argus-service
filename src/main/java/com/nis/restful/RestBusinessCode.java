package com.nis.restful;

/*
 * 服务业务编码
 */
public enum RestBusinessCode {
	
	/**
	 * 未知错误
	 */
	unknow_error (998, "未知错误!"),
	/**
	 * 需要权限
	 */
	need_permission (1000 ,"该操作需要权限"),
	/**
	 * 资源不存在
	 */
	uri_not_found (1001,"uri地址不存在，检查uri拼写"),
	
	
	/**
	 * 参数不全
	 */
	missing_args (1002,"缺少必要的参数信息"),
	
	/**
	 * 操作行为错误,1-插入2-更新 3-删除4-查询 ，插入时选择了删除这种错误返回该异常代码
	 */
	
	op_action_error (1003,"不正确的操作行为"),
	
	config_integrity_error (1004,"配置完整性错误或数据格式错误"),
	
	param_formate_error (1005,"服务器请求参数格式错误"),
	
	insert_data_repeat (1006,"新增数据重复"),
	
	log_range_undefined (1007,"服务器请求日志范围不明确"),
	
	not_unique (1008,"数据违反唯一性"),
	
	wrong_range (1009,"数据不在有效范围"),
	
	query_success(2000, "数据获取操作成功"),
	
	add_success(2001, "数据添加操作成功"),
	
	update_success(2002, "数据更新操作成功"),
	
	delete_success(2003, "数据删除操作成功");
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private final int value; //错误码
	private final String errorReason; //错误原因
	
	public int getValue() {
		return value;
	}

	public String getErrorReason() {
		return errorReason;
	}


	private RestBusinessCode(int value, String errorReason) {
		this.value = value;
		this.errorReason = errorReason;
	}
	
	
	public static RestBusinessCode valueOf(int errorCode) {
		for (RestBusinessCode status : values()) {
			if (status.value == errorCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + errorCode + "]");
	}

}
