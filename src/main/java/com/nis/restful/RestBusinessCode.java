package com.nis.restful;

/*
 * 服务业务编码
 */
public enum RestBusinessCode {
	
	
	/**
	 * 服务内部异常
	 */
	service_runtime_error (5000000, "服务内部异常!"),
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
	op_action_error (4002000,"不正确的操作行为"),

	config_integrity_error (1004,"配置完整性错误或数据格式错误"),

	param_formate_error (1005,"服务器请求参数格式错误"),

	insert_data_repeat (1006,"新增数据重复"),

	log_range_undefined (1007,"服务器请求日志范围不明确"),

	not_unique (1008,"数据违反唯一性"),

	wrong_range (1009,"数据不在有效范围"),
	
	query_success(2001000, "数据获取操作成功"),
	
	add_success(2011000, "数据添加操作成功"),
	
	update_success(2042000, "数据更新操作成功"),
	
	delete_success(2041000, "数据删除操作成功"),
	/**
	 * 完整性异常 4001000 	 编译:4001000 	分组:4001100 	数值类:4001200	IP类:4001300	字符串类:4001400		摘要类:4001500		回调类:4001600
	 * 业务格式异常 4002000	 编译:4002000 	分组:4002100 	数值类:4002200	IP类:4002300	字符串类:4002400		摘要类:4002500		回调类:4002600
	 */
	
	/**
	 * 编译配置不能为空
	 */
	CompileIsNull(4001001, "Maat配置不能为空"),
	/**
	 * 编译配置的的compileId不能为空
	 */
	CompileIdIsNull(4001002, "Maat配置的compileId字段不能为空"),

	/**
	 * 编译配置的的service不能为空
	 */
	ServiceIsNull(4001003, "配置的service属性不能为空"),

	/**
	 * 编译配置的action不能为空
	 */
	ActionIsNull(4001004, "Maat配置的action不能为空"),

	/**
	 * 编译配置的doBlacklist不能为空
	 */
	DoBlacklistIsNull(4001005, "Maat配置的doBlacklist不能为空"),
	/**
	 * 编译配置的的doBlacklist不在有效范围
	 */
	DoBlacklistIsWrongRange(4001006, "Maat配置的doBlacklist的值不在有效范围"),
	
	/**
	 * 编译配置的的doLog不能为空
	 */
	DoLogIsNull(4001007, "Maat编译配置的doLog不能为空"),
	/**
	 * 编译配置的的doLog不在有效范围
	 */
	DoLogIsWrongRange(4001008, "Maat配置的doLog的值不在有效范围"),
	/**
	 * 编译配置的effectiveRange不能为空
	 */
	EffectiveRangeIsNull(4001009, "Maat配置的effectiveRange不能为空"),
	/**
	 * 编译配置的startTime不能为空
	 */
	StartTimeIsNull(4001010, "Maat配置的startTime不能为空"),

	/**
	 * 编译配置的的endTime不能为空
	 */
	EndTimeIsNull(4001011, "Maat配置的endTime不能为空"),
	/**
	 * 配置的userRegion不能为空
	 */
	UserRegionIsNull(4001012, "配置userRegion属性不能为空"),
	/**
	 * 配置的isValid不能为空
	 */
	IsValidIsNull(4001013, "配置isValid属性不能为空"),
	
	/**
	 * MAAT配置的域配置信息不能全为空
	 */
	RegionListIsNull(4001014, "MAAT配置的域配置信息不能全为空"),
	
	/**
	 * 编译配置的groupNum不能为空
	 */
	GroupNumIsNull(4001016, "Maat配置groupNum属性不能为空"),
	/**
	 * 编译配置的的fatherCfgId不能为空
	 */
	FatherCfgIdIsNull(4001017, "Maat配置的fatherCfgId不能为空"),
	/**
	 * 编译配置的缺少配置分组信息
	 */
	CompileGroupIsNull(4001018, "Maat配置缺少groupRelationList"),
	
	/**
	 * 编译配置的opTime不能为空
	 */
	OpTimeIsNull(4001019, "Maat配置的opTime不能为空"),
	
	/**
	 * 编译配置的contType不能为空
	 */
	ContTypeIsNull(4001020, "Maat配置的contType不能为空"),
	
	/**
	 * 编译配置的attrType不能为空
	 */
	AttrTypeIsNull(4001021, "Maat配置的attrType不能为空"),
	
	/**
	 * 编译配置的taskId不能为空
	 */
	 TaskIdIsNull(4001022, "Maat配置的taskId不能为空"),
	
	/**
	 * 编译配置的contLabel不能为空
	 */
	 ContLabelIsNull(4001023, "Maat配置的contLabel不能为空"),
	
	/**
	 * 编译配置的affairId不能为空
	*/
	AffairIdIsNull(4001024, "Maat配置的affairId不能为空"),
	
	//分组配置数据完整性业务码10100~
	
	/**
	 * 编译配置的的配置分组的CompileId不能为空
	 */
	GroupsCompileIdIsNull(4001100, "MAAT配置分组关系中compileId不能为空"),
	
	/**
	 * 编译配置的的配置分组的groupId不能为空
	 */
	GroupIdIsNull(4001101, "MAAT配置分组关系中groupId不能为空"),
	
	/**
	 * 编译配置的的配置分组的isValid不能为空
	 */
	GroupsIsValidIsNull(4001102, "MAAT配置分组关系中isValid不能为空"),
	
//	/**
//	 * 编译配置的的配置分组的opTime不能为空
//	 */
//	GroupsOpTimeIsNull(400137, "编译配置的的配置分组的opTime不能为空"),
	
	/**
	 * 域配置的groupId在配置分组关系中不存在
	 */
	RegionsGroupIdIsNotFound(4001103, "域配置的groupId在配置分组关系中不存在"),
	/**
	 *域配置的regionId不能为空
	 */
	RegionIdIsNull(4001104, "域配置的regionId不能为空"),
	
	/**
	 *域配置的groupId不能为空
	 */
	RegionsGroupIdIsNull(4001105, "域配置的groupId不能为空"),
	
	//数值类域数据完整性业务码10200~
	/**
	 * 编译配置中缺少数值类域配置信息
	 */
	NumRegionIsNull(4001200, "Maat配置中不能缺少数值类域配置信息"),
	
	/**
	 *数值域配置的lowBoundary不能为空
	 */
	LowBoundaryIsNull(4001201, "数值域配置的lowBoundary不能为空"),
	/**
	 *数值域配置的upBoundary不能为空
	 */
	UpBoundaryIsNull(4001202, "数值域配置的upBoundary不能为空"),
	
	//数值类域数据完整性业务码10300~
	/**
	 * 编译配置中缺少IP类域配置信息
	 */
	IpRegionIsNull(4001300, "Maat配置中不能缺少IP类域配置信息"),
	
	/**
	 * IP类域配置的addrType不能为空
	 */
	AddrTypeIsNull(4001301,"IP类域配置的addrType不能为空"),
	/**
	 * IP类域配置的srcIp不能为空
	 */
	SrcIpIsNull(4001302,"IP类域配置的srcIp不能为空"),
	/**
	 * IP类域配置的maskSrcIp不能为空
	 */
	MaskSrcIpIsNull(4001303,"IP类域配置的maskSrcIp不能为空"),
	
	/**
	 * IP类域配置的srcPort不能为空
	 */
	SrcPortIsNull(4001304,"IP类域配置的srcPort不能为空"),
	/**
	 * IP类域配置的maskSrcPort不能为空
	 */
	MaskSrcPortIsNull(4001305,"IP类域配置的maskSrcPort不能为空"),
	/**
	 * IP类域配置的dstIp不能为空
	 */
	DstIpIsNull(4001306,"IP类域配置的dstIp不能为空"),
	/**
	 * IP类域配置的maskDstIp不能为空
	 */
	MaskDstIpIsNull(4001307,"IP类域配置的maskDstIp不能为空"),
	
	/**
	 * IP类域配置的dstPort不能为空
	 */
	DstPortIsNull(4001308,"IP类域配置的dstPort不能为空"),
	/**
	 * IP类域配置的maskDstPort不能为空
	 */
	MaskDstPortIsNull(4001309,"IP类域配置的maskDstPort不能为空"),
	/**
	 * IP类域配置的protocol不能为空
	 */
	ProtocolIsNull(4001310,"IP类域配置的protocol不能为空"),
	/**
	 * IP类域配置的direction不能为空
	 */
	DirectionIsNull(4001311,"IP类域配置的direction不能为空"),
	
	//数值类域数据完整性业务码10400~
	/**
	 * 编译配置中缺少字符串类域配置信息
	 */
	StrRegionIsNull(4001400, "Maat配置中不能缺少字符串类域配置信息"),
	/**
	 * 字符串类域配置的keywords不能为空
	 */
	KeywordsIsNull(4001401,"字符串类域配置的keywords不能为空"),
	/**
	 * 字符串类域配置的district不能为空
	 */
	DistrictIsNull(4001402,"字符串类域配置的district不能为空"),
	/**
	 * 字符串类域配置的exprType不能为空
	 */
	ExprTypeIsNull(4001403,"字符串类域配置的exprType不能为空"),
	
	/**
	 * 字符串类域配置的matchMethod不能为空
	 */
	MatchMethodIsNull(4001404,"字符串类域配置的matchMethod不能为空"),
	
	/**
	 * 字符串类域配置的isHexbin不能为空
	 */
	IsHexbinIsNull(4001405,"字符串类域配置的isHexbin不能为空"),
	
	//摘要类域数据完整性业务码10500~
	/**
	 * 摘要类域配置的rawLen不能为空
	 */
	RawLenIsNull(4001500,"摘要类域配置的rawLen不能为空"),
	/**
	 * 摘要类域配置的digest的值只能是1到10
	 */
	DigestIsNull(4001501,"摘要类域配置的digest不能为空"),
	
	/**
	 * 摘要类域配置的cfdsLevel不能为空
	 */
	CfdsLevelIsNull(4001502,"摘要类域配置的cfdsLevel不能为空"),
	
	/**
	 * 回调类配置的cfgId字段不能为空
	 */
	CfgIdIsNull(4001600,"配置的cfgId字段不能为空"),
	/**
	 * 回调类配置的属性不能为空
	 */
	FieldIsNull(4001601,"配置属性不能为空"),
	/**
	 * 回调类上传文件不能为空
	 */
	FileIsNull(4001602,"上传文件不能为空"),
	
	/**
	 * 回调类checksum与文件MD5值不一致
	 */
	CheckSumIsWrong(4001603,"checksum与文件MD5值不一致"),
	/**
	 * minio地址和端口不能为空
	 */
	MinioUrlIsNull(4001604,"minio地址和端口不能为空"),
	
	//配置业务格式验证 20000
	/**
	 * 编译配置的groupNum属性值等于0
	 */
	GroupNumIsZero(4002001, "Maat配置groupNum不能等于0"),
	/**
	 * 编译配置的groupNum属性值大于8
	 */
	GroupNumGtEight(4002002, "Maat配置的groupNum不能大于8"),
	
	/**
	 * 编译配置的groupNum属性值与groupRelationList长度不相等
	 */
	GroupNumNEQGroupList(4002003, "Maat配置的groupNum数量与groupRelationList大小需一致"),
	/**
	 * 编译配置的的fatherCfgId不能为空
	 */
	FatherCfgIdIsWrongRange(4002004, "Maat配置的fatherCfgId值不在有效范围"),
	/**
	 * 配置的service与action不匹配
	 */
	ServiceUnmatchAction(4002005, "配置的service与action不匹配"),
	/**
	 * 编译配置的的startTime不能晚于endTime
	 */
	EndTimeGTStartTime(4002006, "Maat配置的startTime不能晚于endTime"),
	/**
	 * 配置的isValid在添加时不能为无效
	 */
	IsValidIsT(4002007, "配置增加操作，isValid值不能无效"),
	/**
	 * 配置的isValid在配置取消时不能为有效
	 */
	IsValidIsF(4002008, "配置取消操作，isValid值不能为有效"),
//	/**
//	 * 编译配置的groupRelationList的配置在添加时不能为无效
//	 */
//	GroupRegionIsValid(400121, "编译配置的分组配置在添加时不能为无效"),
//	
//	/**
//	 * 编译配置的groupRelationList的配置在添加时不能为有效
//	 */
//	GroupRegionIsNotValid(400121, "编译配置的分组配置在修改时不能为有效"),
	
	/**
	 * 配置分组与编译配置的compileId不一致
	 */
	CompileIdIsNeq(4002009,"Maat配置分组与编译配置的compileId不一致"),
	
	/**
	 * 配置的service与action关系不存在
	 */
	Service2ActionIsNull(4002010, "当前service与action关系不存在"),
	/**
	 * 状态更新操作，所有配置的isValid值必须相同
	 */
	IsValidNonUniq(4002011, "状态更新操作，所有配置的isValid值必须相同"),
	
	/**
	 * 编译配置的动作(action)为监测(1)时doLog的值不能为0
	 */
	DoLogIsWrong(4002012, "Maat编译配置的动作(action)为监测(1)时doLog的值不能为0"),
	
	/**
	 * 配置分组中同一groupId不能重复出现多次
	 */
	GroupIdIsRepeat(4002100,"Maat配置分组中groupId不能重复出现多次"),
	
	/**
	 * 当前编译配置service与域配置tableName的关系不存在
	 */
	TableNameUnmatchService(4002101, "当前编译配置service与域配置tableName的关系不存在"),
	/**
	 * 域配置中的tableName不是分组复用的域表
	 */
	TableNameUnReuse(4002102, "域配置中tableName不是分组复用的域表"),
	
	/**
	 *数值域配置的lowBoundary值不在有效范围内
	 */
	LowBoundaryIsWrongRange(4002200, "数值域-配置lowBoundary值不在有效范围内"),
	/**
	 *数值域配置的upBoundary值不在有效范围内
	 */
	UpBoundaryIsWrongRange(4002201, "数值域-配置upBoundary值不在有效范围内"),
	/**
	 *数值域-配置lowBoundary必须小于upBoundary
	 */
	LowBoundaryGTUpBoundary(4002202, "数值域-配置lowBoundary必须小于upBoundary"),
	
	/**
	 * IP类域配置的direction的值不在有效范围，只能是0或1
	 */
	DirectionIsWrongRange(4002300,"IP类域-direction的值不在有效范围，只能是0或1"),
	
	/**
	 * IP类域配置的IP或IpMask与addrType不一致
	 */
	IPUnMatchAddrType(4002301,"IP类域-IP地址或掩码与addrType不一致"),
	/**
	 * IP类域配置的port格式不正确
	 */
	PortIsNotVal(4002302,"IP类域-端口或端口掩码格式不正确"),
	/**
	 * IP类域配置的srcIp和dstIp不能相同
	 */
	SrcIpEQDstIp(4002303,"IP类域-srcIp和dstIp不能相同"),
	
	/**
	 * IP类域配置的addrType的值只能为4(IPV4)、6(IPV6)、46(4OVER6隧道)、64(6OVER4隧道)、10(全部)
	 */
	AddrTypeIsWrongRange(4002304,"IP类域配置的addrType的值只能为4(IPV4)、6(IPV6)、46(4OVER6隧道)、64(6OVER4隧道)、10(全部)"),
	/**
	 * 字符串域配置的exprType的值只能是0(无表达式)或者1(与表达式)
	 */
	ExprTypeIsWrongRange(4002400,"字符域-exprType的值只能是0(无表达式)或1(与表达式)"),

	/**
	 * 字符串域配置的matchMethod的值只能是0(无表达式)或者1(与表达式)
	 */
	MatchMethodIsWrongRange(4002401,"字符域-matchMethod的值只能是0(子串匹配),1(右匹配),2(左匹配),3(完全匹配)"),
	
	/**
	 * 字符串域配置的isHexbin的值只能是0(大小写不敏感，且非HEX)或者1(HEX)或者2(大小写敏感，且非HEX)
	 */
	IsHexbinIsWrongRange(4002402,"字符域-isHexbin的值只能是0(大小写不敏感，且非HEX)或者1(HEX)或者2(大小写敏感，且非HEX)"),
	/**
	 * 摘要类域配置的cfdsLevel的值只能是1到10
	 */
	CfdsLevelIsWrongRange(4002500,"摘要域-cfdsLevel的值只能是1到10"),
	
	/**
	 * service不能为空并且必须是分组复用的业务类型
	 */
	ServiceIsNullOrNotReuse(4002501, "service不能为空并且必须是分组复用的业务类型"),
	/**
	 * 分组复用配置中域不能全为空
	 */
	ReuseRegionIsNull(4002502, "分组复用配置中域不能全为空"),
	//回调类
	/**
	 * 回调类service配置不正确
	 */
	ServiceIsWrong(4002600,"service的值配置不正确"),
	
	/**
	 * 回调类-ipType属性值不在有效范围
	 */
	IpTypeIsWrong(4002601,"回调类-ipType属性值不在有效范围"),
	
	/**
	 * 回调类的XX属性不是数值类型
	 */
	MastNumberic(4002602,"属性值不是数值类型"),
	/**
	 * 回调类的XX属性不是日期类型或不是规范的日期串
	 */
	MastDate(4002602,"属性值不是日期类型或不是规范的日期串"),
	/**
	 * 回调类的IP或Mask与addrType不一致
	 */
	IpIsUnMatchType(4002603,"配置IP或Mask与addrType不一致"),
	/**
	 * 回调类的XX属性取值不在Range范围内
	 */
	ValueInWrongRange(4002604,"属性取值不在Range范围内"),
	/**
	 * 回调类的XX属性取值符合正则表达式验证
	 */
	ValueInWrongRegexp(4002605,"属性值与正则表达式不匹配"),
	
	/**
	 * isValid属性格式不正确
	 */
	IsValidInWrongRange(4002606,"isValid属性格式不正确"),
	
	/**
	 * 回调类配置请求参数格式不正确
	 */
	CBParamFormateError(4002607,"回调类配置请求参数格式不正确"),
	
	//服务内部异常业务码
	/**
	 * 回调类配置Service不存在或未配置tableName
	 */
	CBServiceOrTableNameNotFound(5001001,"回调类配置Service不存在或未配置tableName"),
	/**
	 * service与写入数据库序号映射关系不存在
	 */
	ServiceNoFoundDBIndex(5001002,"service与写入数据库序号映射关系不存在"),
	
	/**
	 * 向服务器上传文件失败
	 */
	FileUploadFailure(5001003,"向服务器上传文件失败"),
	
	/**
	 * FastDfs文件服务器出现异常
	 */
	FastDfsServerException(5001004,"FastDfs文件服务器出现异常"),
	/**
	 * 摘要获取失败
	 */
	GetFileDigestFailure(5001005,"摘要获取失败"),
	
	/**
	 * Redis数据库编号不在有效范围内
	 */
	DbIndexNotInRange(5002001,"Redis数据库编号不在有效范围内"),
	
	/**
	 * 配置信息不能为空
	 */
	ConfigSourceIsNull(5002002,"配置信息不能为空"),
	
	/**
	 * 无法连接Redis数据库
	 */
	CannotConnectionRedis(5002003,"无法连接Redis数据库"),
	
	/**
	 * 向Redis数据库保存配置时发生错误
	 */
	SaveDataInError(5002004,"向Redis数据库保存配置时发生错误"),
	
	/**
	 * key在Redis中不存在
	 */
	KeyNotExistsInRedis(5002005,"key在Redis中不存在"),
	
	/**
	 * 删除Redis数据库中配置时发生错误
	 */
	DeleteDataInError(5002006,"删除Redis数据库中配置时发生错误"),
	
	/**
	 * 向Redis数据库中设置值失败
	 */
	ZsetFailed(5002007,"向Redis数据库中设置值失败"),
	
	/**
	 * redisTemplate为空
	 */
	RedisTemplateIsNull(5002008,"redisTemplate为空"),
	
	/**
	 * 判断key是否存在失败
	 */
	ExistsKeyFailed(5002008,"判断key是否存在失败"),
	
	/**
	 * 当前service不允许单独添加域配置
	 */
	ServiceNotAllowAddReion(5002009,"当前service不允许单独添加域配置"),
	/**
	 * 删除分组复用域配置时,无法在关联关系中找到对应的域配置信息,之前该域没有添加过,或者关联关系被损坏请检查
	 */
	RegionIsNotExist(5002010,"删除分组复用域配置时,无法在关联关系中找到对应的域配置信息,之前该域没有添加过,或者关联关系被损坏请检查"),
	
	
	/**
	 *配置文件内容有误 
	 */
	PropertiesIsError(5002011,"配置文件内容有误"),
	
	/**
	 * 无法从Map中获取配置的属性值
	 */
	GetMaatVersionFailure(5003001,"无法从Map中获取配置的属性值"),
	
	/**
	 * 在配置文件中未找到service对应的表名
	 */
	NotFoundTableName(5003002,"未找到service对应的表名"),
	
	/**
	 * 在配置文件中未找到service对应的Redis入库规则
	 */
	NotFoundRedisRule(5003003,"未找到service对应的Redis入库规则"),
	/**
	 * Redis数据库中MAAT配置的对应关系不存在
	 */
	RelationNotExistsInRedis(5003004,"Redis数据库中MAAT配置的对应关系不存在"),
	/**
	 * Redis关联关系库中未找到配置对应的redisdb信息
	 */
	RedisDBRelationNotExistsInRedis(5003005,"Redis关联关系库中未找到配置对应的redisdb信息"),
	/**
	 * 保存文件到redis发生了异常
	 */
	SaveFileToRedisError(5003006,"保存文件到redis发生了异常"),
	
	
	
	
	
	
	/**
	 * Map中缺少编译配置信息
	 */
	NotFoundCompileInfo(5004001,"Map中缺少编译配置信息"),
	
	/**
	 * Map中缺少分组配置信息
	 */
	NotFoundGroupInfo(5004002,"Map中缺少分组配置信息"),
	
	/**
	 * 无法从Map中获取配置的属性值
	 */
	NotFoundValueByKey(5004003,"无法从Map中获取配置的属性值"),
	
	/**
	 * 状态更新操作中service与配置ID的应对关系不能为空
	 */
	ServiceAndCompileMapIsNull(5004004,"状态更新操作中service与配置Id的应对关系不能为空"),
	
	/**
	 * 状态更新操作中与service对应的配置Id列表不能为空
	 */
	CompileIdListIsNull(5004005,"状态更新操作中与service对应的配置Id列表不能为空"),
	
	/**
	 * 状态更新操作中更新内容Map不能为空
	 */
	ConfigInfoMapIsNull(5004005,"状态更新操作中更新内容Map不能为空"),
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
