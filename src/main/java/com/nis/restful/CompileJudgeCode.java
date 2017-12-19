package com.nis.restful;

public enum CompileJudgeCode {
	/**
	 * 当前编译配置中的groupNum属性值与配置分组groupList大小不等
	 */
	GroupNumNEQGroupList(1, "groupNum属性值与groupRelationList大小不等"),
	/**
	 * 当前编译配置中的groupNum属性值与配置分组groupList大小相等
	 */
	GroupNumEQGroupList(2, "groupNum属性值与groupRelationList大小不等"),
	/**
	 * 当前编译配置中的groupNum属性值与配置分组groupList大小均小于等于8且均大于0
	 */
	GroupNumAndListLtEight(3, "groupNum属性值与groupRelationList大小均小于等于8且均大于0"),
	/**
	 * 当前编译配置中的groupNum属性值大于8
	 */
	GroupNumGtEight(4, "groupNum属性值大于8"),
	/**
	 * 当前编译配置中的配置分组groupList大小大于8
	 */
	GroupListGtEight(5, "groupRelationList大小大于8"),

	/**
	 * 当前编译配置中的groupNum属性值等于0
	 */
	GroupNumEQZero(6, "groupNum属性值等于0"),
	/**
	 * 当前编译配置中的配置分组groupList等于0
	 */
	GroupListEQZero(7, "groupRelationList大小不能为0"),

	/**
	 * 配置分组中域配置个数大于0
	 */
	CompileGroupSizeGtZero(8, "配置分组中的域配置均正常"),

	/**
	 * 配置分组中域配置个数为0
	 */
	//CompileGroupSizeEqZero(9, "groupRelationList中有一个或多个groupId在strRegionList或ipRegionList或numRegionList中不存在"),
	/**
	 * 配置分组集合中存在相同的配置分组id
	 */
	CompileGroupIdIsRepeat(10, "groupRelationList中存在相同的groupId"),
	/**
	 * 有域配置中的配置分组id没有对应的配置分组(在配置分组集合中找不到该配置分组id)
	 */
	//CompileGroupIdNotExist(11, "有一个或多个strRegionList或ipRegionList或numRegionList中的groupId在groupRelationList中不存在"),

	/**
	 * 配置分组中的compileId与编译配置中的compileId不一致
	 */
	//CoupileGroupListPidIsError(12, "groupRelationList中有一个或多个compileId与编译配置中compileId不一致"),
	/**
	 * 配置分组 前台不能设置分组为无效
	 */
	CompileGroupNotIsInvalid(13, "配置分组不能设置为无效"),
	/**
	 * 编译为无效 域配置全部不能为无效
	 */
	CompileALLIsInvalid(14, "编译配置和域配置不能同时为无效"),
	/**
	 * 编译为有效 域配置至少有一个为无效
	 */
	CompileIsValide(15, "当编译为有效时域配置至少有一个为无效"),
	/**
	 * 添加配置不能为无效
	 */
	CompileIsNotInvalid(16, "添加时不能为无效"),
	/**
	 * 当编译配置有效时 域配置不能都是空
	 */
	CompileNotNull(17, "当编译配置有效时，域配置不能都是空"),

	/**
	 * 缺少配置分组信息
	 */
	CompileGroupIsNull(18, "缺少配置分组信息"),

	/**
	 * 缺少ip类域配置信息
	 */
	IpRegionIsNull(19, "缺少ip类域配置信息"),

	/**
	 * 缺少字符串类域配置信息
	 */
	StrRegionIsNull(20, "缺少字符串类域配置信息"),

	/**
	 * 缺少数值类域配置信息
	 */
	NumRegionIsNull(21, "缺少数值类域配置信息"),
	/**
	 * 编译配置不能为空
	 */
	CompileIsNull(22, "编译配置不能为空"),
	/**
	 * groupNum不能为空
	 */
	GroupNumIsNull(23, "groupNum不能为空"),
	/**
	 * groupRelationList不能为空
	 */
	GroupListIsNull(24, "groupRelationList不能为空"),
	/**
	 * groupRelationList不能为空
	 */
	//ServiceType2TableName(25, "域配置中的表名与编译配置的业务类型不一致"),
	/**
	 * 当前编译配置中的groupNum属性与配置分组groupList大小相等且均大于0小于等于8, 且每个配置分组中域配置个数大于0
	 */
	CompileIsOk(26, "编译配置完全正确");

	private final int value; // 错误码
	private String errorReason; // 错误原因

	public int getValue() {
		return value;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	private CompileJudgeCode(int value, String errorReason) {
		this.value = value;
		this.errorReason = errorReason;
	}

	public static CompileJudgeCode valueOf(int errorCode) {
		for (CompileJudgeCode status : values()) {
			if (status.value == errorCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + errorCode + "]");
	}
}