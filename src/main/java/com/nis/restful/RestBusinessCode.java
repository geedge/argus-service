package com.nis.restful;

/*
 * 服务业务编码
 */
public enum RestBusinessCode {
	
	
	/**
	 * 服务内部异常
	 */
	service_runtime_error (30000, "服务内部异常!"),
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
	
	delete_success(2003, "数据删除操作成功"),
	/**
	 * 完整性验证 10000 	 编译:10000 	分组:10100 	数值类:10200	IP类:10300	字符串类:10400		摘要类:10500		回调类:10600
	 * 配置格式验证 20000	 编译:20000 	分组:20100 	数值类:20200	IP类:20300	字符串类:20400		摘要类:20500		回调类:20600
	 */
	
	/**
	 * 编译配置不能为空
	 */
	CompileIsNull(10001, "Maat配置不能为空"),
	/**
	 * 编译配置的的compileId不能为空
	 */
	CompileIdIsNull(10002, "Maat配置的compileId字段不能为空"),

	/**
	 * 编译配置的的service不能为空
	 */
	ServiceIsNull(10003, "配置的service属性不能为空"),

	/**
	 * 编译配置的action不能为空
	 */
	ActionIsNull(10004, "Maat配置的action不能为空"),

	/**
	 * 编译配置的的compileId不能为空
	 */
	DoBlacklistIsNull(10005, "Maat配置的doBlacklist不能为空"),
	/**
	 * 编译配置的的compileId不能为空
	 */
	DoBlacklistIsWrongRange(10006, "Maat配置的doBlacklist的值不在有效范围"),
	
	/**
	 * 编译配置的的doLog不能为空
	 */
	DoLogIsNull(10007, "Maat配置的doLog不能为空"),
	/**
	 * 编译配置的的doLog不能为空
	 */
	DoLogIsWrongRange(10008, "Maat配置的doLog的值不在有效范围"),
	/**
	 * 编译配置的effectiveRange不能为空
	 */
	EffectiveRangeIsNull(10009, "Maat配置的effectiveRange不能为空"),
	/**
	 * 编译配置的startTime不能为空
	 */
	StartTimeIsNull(10010, "Maat配置的startTime不能为空"),

	/**
	 * 编译配置的的endTime不能为空
	 */
	EndTimeIsNull(10011, "Maat配置的endTime不能为空"),
	
	/**
	 * 配置的isValid不能为空
	 */
	IsValidIsNull(10013, "配置isValid属性不能为空"),
	
	/**
	 * 编译配置的groupNum不能为空
	 */
	GroupNumIsNull(10016, "Maat配置groupNum属性，不能为空"),
	/**
	 * 编译配置的的fatherCfgId不能为空
	 */
	FatherCfgIdIsNull(10017, "Maat配置的fatherCfgId不能为空"),
	/**
	 * 编译配置的缺少配置分组信息
	 */
	CompileGroupIsNull(10018, "Maat配置的缺少groupRelationList"),
	
	/**
	 * 编译配置的opTime不能为空
	 */
	OpTimeIsNull(10019, "Maat配置的opTime不能为空"),
	
	//分组配置数据完整性业务码10100~
	
	/**
	 * 编译配置的的配置分组的CompileId不能为空
	 */
	GroupsCompileIdIsNull(10100, "Maat配置的配置分组的compileId不能为空"),
	
	/**
	 * 编译配置的的配置分组的groupId不能为空
	 */
	GroupIdIsNull(10101, "Maat配置的的配置分组的groupId不能为空"),
	
	/**
	 * 编译配置的的配置分组的isValid不能为空
	 */
	GroupsIsValidIsNull(10102, "编译配置的的配置分组的isValid不能为空"),
	
//	/**
//	 * 编译配置的的配置分组的opTime不能为空
//	 */
//	GroupsOpTimeIsNull(1037, "编译配置的的配置分组的opTime不能为空"),
	
	/**
	 * 域配置的groupid在配置分组关系中不存在
	 */
	RegionsGroupIdIsNotFound(10103, "域配置的groupid在配置分组关系中不存在"),
	/**
	 *域配置的regionId不能为空
	 */
	RegionIdIsNull(10104, "域配置的regionId不能为空"),
	
	/**
	 *域配置的groupId不能为空
	 */
	RegionsGroupIdIsNull(10105, "域配置的groupId不能为空"),
	
	//数值类域数据完整性业务码10200~
	/**
	 * 编译配置中缺少数值类域配置信息
	 */
	NumRegionIsNull(10200, "Maat配置中不能缺少数值类域配置信息"),
	
	/**
	 *数值域配置的lowBoundary不能为空
	 */
	LowBoundaryIsNull(10201, "数值域配置的lowBoundary不能为空"),
	/**
	 *数值域配置的upBoundary不能为空
	 */
	UpBoundaryIsNull(10202, "数值域配置的upBoundary不能为空"),
	
	//数值类域数据完整性业务码10300~
	/**
	 * 编译配置中缺少IP类域配置信息
	 */
	IpRegionIsNull(10300, "Maat配置中不能缺少IP类域配置信息"),
	
	/**
	 * IP类域配置的addrType不能为空
	 */
	AddrTypeIsNull(10301,"IP类域配置的addrType不能为空"),
	/**
	 * IP类域配置的srcIp不能为空
	 */
	SrcIpIsNull(10302,"IP类域配置的srcIp不能为空"),
	/**
	 * IP类域配置的maskSrcIp不能为空
	 */
	MaskSrcIpIsNull(10303,"IP类域配置的maskSrcIp不能为空"),
	
	/**
	 * IP类域配置的srcPort不能为空
	 */
	SrcPortIsNull(10304,"IP类域配置的srcPort不能为空"),
	/**
	 * IP类域配置的maskSrcPort不能为空
	 */
	MaskSrcPortIsNull(10305,"IP类域配置的maskSrcPort不能为空"),
	/**
	 * IP类域配置的dstIp不能为空
	 */
	DstIpIsNull(10306,"IP类域配置的dstIp不能为空"),
	/**
	 * IP类域配置的maskDstIp不能为空
	 */
	MaskDstIpIsNull(10307,"IP类域配置的maskDstIp不能为空"),
	
	/**
	 * IP类域配置的dstPort不能为空
	 */
	DstPortIsNull(10308,"IP类域配置的dstPort不能为空"),
	/**
	 * IP类域配置的maskDstPort不能为空
	 */
	MaskDstPortIsNull(10309,"IP类域配置的maskDstPort不能为空"),
	/**
	 * IP类域配置的protocol不能为空
	 */
	ProtocolIsNull(10310,"IP类域配置的protocol不能为空"),
	/**
	 * IP类域配置的direction不能为空
	 */
	DirectionIsNull(10311,"IP类域配置的direction不能为空"),
	
	//数值类域数据完整性业务码10400~
	/**
	 * 编译配置中缺少字符串类域配置信息
	 */
	StrRegionIsNull(10400, "Maat配置中不能缺少字符串类域配置信息"),
	/**
	 * 字符串类域配置的keywords不能为空
	 */
	KeywordsIsNull(10401,"字符串类域配置的keywords不能为空"),
	/**
	 * 字符串类域配置的district不能为空
	 */
	DistrictIsNull(10402,"字符串类域配置的district不能为空"),
	/**
	 * 字符串类域配置的exprType不能为空
	 */
	ExprTypeIsNull(10403,"字符串类域配置的keywords不能为空"),
	
	/**
	 * 字符串类域配置的matchMethod不能为空
	 */
	MatchMethodIsNull(10404,"字符串类域配置的matchMethod不能为空"),
	
	/**
	 * 字符串类域配置的isHexbin不能为空
	 */
	IsHexbinIsNull(10405,"字符串类域配置的isHexbin不能为空"),
	
	//摘要类域数据完整性业务码10500~
	/**
	 * 摘要类域配置的rawLen不能为空
	 */
	RawLenIsNull(10500,"摘要类域配置的rawLen不能为空"),
	/**
	 * 摘要类域配置的digest的值只能是1到10
	 */
	DigestIsNull(10501,"摘要类域配置的digest不能为空"),
	
	/**
	 * 摘要类域配置的cfdsLevel不能为空
	 */
	CfdsLevelIsNull(10502,"摘要类域配置的cfdsLevel不能为空"),
	
	/**
	 * 回调类配置的cfgId字段不能为空
	 */
	CfgIdIsNull(10600,"配置的cfgId字段不能为空"),
	/**
	 * 回调类配置的属性不能为空
	 */
	FieldIsNull(10601,"配置属性不能为空"),
	/**
	 * 回调类上传文件不能为空
	 */
	FileIsNull(10602,"上传文件不能为空"),
	
	/**
	 * 回调类checksum与文件MD5值不一致
	 */
	CheckSumIsWrong(10603,"checksum与文件MD5值不一致"),
	
	//配置业务格式验证 20000
	/**
	 * 编译配置的groupNum属性值等于0
	 */
	GroupNumIsZero(20001, "Maat配置groupNum属性值，不能等于0"),
	/**
	 * 编译配置的groupNum属性值大于8
	 */
	GroupNumGtEight(20002, "Maat配置的groupNum属性值大于8"),
	
	/**
	 * 编译配置的groupNum属性值与groupRelationList长度不相等
	 */
	GroupNumNEQGroupList(20003, "Maat配置的groupNum属性值与groupRelationList长度不等"),
	/**
	 * 编译配置的的fatherCfgId不能为空
	 */
	FatherCfgIdIsWrongRange(20004, "Maat配置的fatherCfgId值不在有效范围"),
	/**
	 * 编译配置的service与action不匹配
	 */
	ServiceUnmatchAction(20005, "Maat配置的service与action不匹配"),
	/**
	 * 编译配置的的startTime不能晚于endTime
	 */
	EndTimeGTStartTime(20006, "Maat配置的startTime不能晚于endTime"),
	/**
	 * 配置的isValid在添加时不能为无效
	 */
	IsValidIsT(20007, "配置增加操作，isValid值不能无效"),
	/**
	 * 配置的isValid在配置取消时不能为有效
	 */
	IsValidIsF(20008, "配置取消操作，isValid值不能为有效"),
//	/**
//	 * 编译配置的groupRelationList的配置在添加时不能为无效
//	 */
//	GroupRegionIsValid(1021, "编译配置的分组配置在添加时不能为无效"),
//	
//	/**
//	 * 编译配置的groupRelationList的配置在添加时不能为有效
//	 */
//	GroupRegionIsNotValid(1021, "编译配置的分组配置在修改时不能为有效"),
	
	/**
	 * 配置分组与编译配置的compileId不一致
	 */
	CompileIdIsNeq(20009,"Maat配置分组与编译配置的compileId不一致"),
	
	/**
	 * 配置分组中同一groupId出现多次
	 */
	GroupIdIsRepeat(20100,"Maat配置分组中同一groupId不能出现多次"),
	
	/**
	 * tableName与编译配置业务类型不一致
	 */
	TableNameUnmatchService(20101, "tableName与编译配置业务类型不匹配"),
	
	/**
	 *数值域配置的lowBoundary值不在有效范围内
	 */
	LowBoundaryIsWrongRange(20200, "数值域配置的lowBoundary值不在有效范围内"),
	/**
	 *数值域配置的upBoundary值不在有效范围内
	 */
	UpBoundaryIsWrongRange(20201, "数值域配置的upBoundary值不在有效范围内"),
	/**
	 *数值域配置的lowBoundary大于upBoundary值
	 */
	LowBoundaryGTUpBoundary(20202, "数值域配置的lowBoundary大于upBoundary值"),
	
	/**
	 * IP类域配置的direction的值不在有效范围
	 */
	DirectionIsWrongRange(20300,"Maat配置的direction的值只能是0或1"),
	
	/**
	 * IP类域配置的IP或IpMask与addrType不一致
	 */
	IPUnMatchAddrType(20301,"Maat配置IP或IpMask与addrType不一致"),
	/**
	 * IP类域配置的port格式不正确
	 */
	PortIsNotVal(20302,"配置port或portMask格式不正确"),
	/**
	 * IP类域配置的srcIp和dstIp不能相同
	 */
	SrcIpEQDstIp(20303,"Maat配置的srcIp和dstIp不能相同"),
	
	/**
	 * 字符串域配置的exprType的值只能是0(无表达式)或者1(与表达式)
	 */
	ExprTypeIsWrongRange(20400,"Maat配置的exprType的值只能是0(无表达式)或者1(与表达式)"),

	/**
	 * 字符串域配置的matchMethod的值只能是0(无表达式)或者1(与表达式)
	 */
	MatchMethodIsWrongRange(20401,"Maat配置的matchMethod的值只能是0(子串匹配),1(右匹配),2(左匹配),3(完全匹配)"),
	
	/**
	 * 字符串域配置的isHexbin的值只能是0(大小写不敏感，且非HEX)或者1(HEX)或者2(大小写敏感，且非HEX)
	 */
	IsHexbinIsWrongRange(20402,"Maat配置的isHexbin的值只能是0(大小写不敏感，且非HEX)或者1(HEX)或者2(大小写敏感，且非HEX)"),
	/**
	 * 摘要类域配置的cfdsLevel的值只能是1到10
	 */
	CfdsLevelIsWrongRange(20500,"Maat配置cfdsLevel的值只能是1到10"),
	
	
	//回调类
	/**
	 * 回调类service配置不正确
	 */
	ServiceIsWrong(20600,"service的值配置不正确"),
	
	/**
	 * 回调类ipType属性只能为4(IPV4)、6(IPV6)
	 */
	IpTypeIsWrong(20601,"ipType属性只能为4(IPV4)、6(IPV6)"),
	
	/**
	 * 回调类的XX属性不是数值类型
	 */
	MastNumberic(20602,"属性值不是数值类型"),
	/**
	 * 回调类的XX属性不是数值类型
	 */
	MastDate(20602,"属性值不是日期类型"),
	/**
	 * 回调类的XX属性不是数值类型
	 */
	IpIsUnMatchType(20603,"配置IP或Mask与addrType不一致"),
	/**
	 * 回调类的XX属性取值不在Range范围内
	 */
	ValueInWrongRange(20604,"属性取值不在Range范围内"),
	/**
	 * 回调类的XX属性取值符合正则表达式验证
	 */
	ValueInWrongRegexp(20605,"属性值与正则表达式不匹配"),
	
	IsValidInWrongRange(20606,"isValid属性格式不正确"),
	;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
