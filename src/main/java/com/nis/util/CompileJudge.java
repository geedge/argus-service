package com.nis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DataDictionaryValue;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.listener.SystemConfigListener;
import com.nis.restful.CompileJudgeCode;
import com.nis.web.service.restful.ConfigSourcesService;

public class CompileJudge {

	/**
	 * 判断当前编译配置中的groupNum属性值与配置分组groupList大小是否小于等于8且大于0
	 * 
	 * @param configCompile
	 *            编译配置对象
	 * @return GroupNumAndListLtEight GroupNumGtEight GroupListGtEight
	 *         GroupNumEQZero GroupListEQZero
	 * 
	 * 
	 */
	@SuppressWarnings("null")
	public static String groupNumAndListIsOk(ConfigCompile configCompile, boolean isUpdate) {
		if (null != configCompile) {
			if (!isUpdate && Constants.BASE_VALIDATE) {
				String valCompile = valCompile(configCompile);
				if (!valCompile.equals("ok")) {
					return valCompile;
				}
			}

			if (null == configCompile.getGroupRelationList() && null != configCompile.getCompileId()) {
				return "编译配置id为" + configCompile.getCompileId() + "的配置中"
						+ CompileJudgeCode.CompileGroupIsNull.getErrorReason();
			}
			if (!isUpdate) {
				if (null == configCompile.getGroupNum()) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ CompileJudgeCode.GroupNumIsNull.getErrorReason();
				}

				if (configCompile.getGroupNum() > 8) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ CompileJudgeCode.GroupNumGtEight.getErrorReason();
				}

				if (configCompile.getGroupNum() <= 0) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ CompileJudgeCode.GroupNumEQZero.getErrorReason();
				}

				if (null == configCompile.getGroupRelationList()) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ CompileJudgeCode.GroupListIsNull.getErrorReason();
				}

				if (null != configCompile.getGroupRelationList() && configCompile.getGroupRelationList().size() <= 0) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ CompileJudgeCode.GroupListEQZero.getErrorReason();
				}
				if (null != configCompile.getGroupRelationList() && configCompile.getGroupRelationList().size() > 8) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ CompileJudgeCode.GroupListGtEight.getErrorReason();
				}

				if (configCompile.getGroupRelationList().size() != configCompile.getGroupNum()) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ CompileJudgeCode.GroupNumNEQGroupList.getErrorReason();
				}
			} else {
				if (null == configCompile.getCompileId()) {
					return "compileId字段不能为空";
				}
				if (null == configCompile.getGroupRelationList()) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ CompileJudgeCode.GroupListIsNull.getErrorReason();
				}
				if (configCompile.getIsValid() == 1) {
					if (null != configCompile.getGroupNum() && configCompile.getGroupNum() > 8) {
						return "编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ CompileJudgeCode.GroupNumGtEight.getErrorReason();
					}
					if (null != configCompile.getGroupNum() && configCompile.getGroupNum() < 0) {
						return "编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ CompileJudgeCode.GroupNumEQZero.getErrorReason();
					}

					if (null != configCompile.getGroupRelationList()
							&& configCompile.getGroupRelationList().size() > 8) {
						return "编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ CompileJudgeCode.GroupListGtEight.getErrorReason();
					}

					if (null != configCompile.getGroupRelationList()
							&& configCompile.getGroupRelationList().size() <= 0) {
						return "编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ CompileJudgeCode.GroupListEQZero.getErrorReason();
					}

					int sizeCount = 0;
					if (configCompile.getIpRegionList() == null && configCompile.getStrRegionList() == null
							&& configCompile.getNumRegionList() == null) {
						return "编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ CompileJudgeCode.CompileNotNull.getErrorReason();
					} else {
						if (configCompile.getIpRegionList() != null) {
							sizeCount += configCompile.getIpRegionList().size();
						}
						if (configCompile.getStrRegionList() != null) {
							sizeCount += configCompile.getStrRegionList().size();
						}
						if (configCompile.getNumRegionList() != null) {
							sizeCount += configCompile.getNumRegionList().size();
						}

						if (sizeCount == 0) {
							return "编译配置id为" + configCompile.getCompileId() + "的配置中"
									+ CompileJudgeCode.CompileNotNull.getErrorReason();
						}
					}
				}
			}
			return CompileJudgeCode.GroupNumAndListLtEight.getErrorReason();
		} else {
			return "编译配置id为" + configCompile.getCompileId() + "的" + CompileJudgeCode.CompileIsNull.getErrorReason();
		}

	}

	/**
	 * 判断编译配置中所有配置分组内域配置个数是否大于0
	 * 
	 * @param configCompile
	 *            编译配置对象
	 * @return CompileGroupSizeGtZero CompileGroupSizeEqZero
	 *         CompileGroupIdNotExist CompileGroupIdIsRepeat GroupListEQZero
	 */
	public static String compileGroupSizeIsGtZero(ConfigCompile configCompile, boolean isUpdate, StringBuffer sb) {
		Long compileId = configCompile.getCompileId();
		List<Long> groupList = new ArrayList<Long>();// 配置分组中groupId集合
		List<ConfigGroupRelation> groupRelationList = configCompile.getGroupRelationList();
		// 插入配置时不能是无效
		if (!isUpdate && configCompile.getIsValid() == 0) {
			return "配置id为" + configCompile.getCompileId() + "的配置"
					+ CompileJudgeCode.CompileIsNotInvalid.getErrorReason();
		}
		Integer isValid = configCompile.getIsValid();
		boolean configFlag = false;
		if (null == groupRelationList) {
			return "编译配置id为" + configCompile.getCompileId() + "的配置中"
					+ CompileJudgeCode.CompileGroupIsNull.getErrorReason();
		}

		if ((!isUpdate) || (isUpdate && configCompile.getIsValid() == 1)) {// 添加或者修改域配置
			if (groupRelationList.size() > 0) {
				for (ConfigGroupRelation configGroupRelation : groupRelationList) {
					if (!isUpdate && Constants.BASE_VALIDATE) {
						String valCompileGroup = valCompileGroup(configGroupRelation, compileId);
						if (!valCompileGroup.equals("ok")) {
							return valCompileGroup;
						}
					}

					if (!configGroupRelation.getCompileId().equals(compileId)) {
						return "编译配置id为" + configCompile.getCompileId() + "的配置中groupRelationList字段中的compileId为"
								+ configGroupRelation.getCompileId() + "的配置分组与编译配置id不一致";
					}
					if (!groupList.contains(configGroupRelation.getGroupId())) {
						groupList.add(configGroupRelation.getGroupId());
					} else {
						return "编译配置id为" + configCompile.getCompileId() + "的配置中groupRelationList存在多个groupid为"
								+ configGroupRelation.getGroupId() + "的配置分组";
					}

					if (configGroupRelation.getIsValid() == 0) {
						return "编译配置id为" + configCompile.getCompileId() + "的配置中groupRelationList中groupid为"
								+ configGroupRelation.getGroupId() + "的配置分组不能设置为无效";
					}
				}
			} else {
				return "编译配置id为" + configCompile.getCompileId() + "的配置中"
						+ CompileJudgeCode.GroupListEQZero.getErrorReason();
			}
		}

		List<Long> regionGroupIdList = new ArrayList<Long>();// 所有域配置中groupId的集合,不重复
		List<NumRegion> numRegionList = configCompile.getNumRegionList();
		if (numRegionList == null) {
			return "编译配置id为" + configCompile.getCompileId() + "的配置中"
					+ CompileJudgeCode.NumRegionIsNull.getErrorReason();
		}
		if (numRegionList.size() > 0) {
			for (NumRegion numRegion : numRegionList) {
				// if (configCompile.getIsValid() != 0 &&
				// !regionGroupIdList.contains(numRegion.getGroupId())) {
				if (!regionGroupIdList.contains(numRegion.getGroupId())) {
					regionGroupIdList.add(numRegion.getGroupId());
				}
				if (!type2TableNameIsOk(configCompile.getService(), numRegion.getTableName())) {
					return "配置id为" + configCompile.getCompileId() + "的配置中numRegionList中的regionid为"
							+ numRegion.getRegionId() + "的域配置tableName与编译配置业务类型不一致";
				}

				if (!isUpdate && Constants.BASE_VALIDATE) {
					String valNumRegion = valNumRegion(numRegion, compileId);
					if (!valNumRegion.equals("ok")) {
						return valNumRegion;
					}
				}

				if (isUpdate && isValid == 1 && numRegion.getIsValid() == 0) {
					configFlag = true;
				}
				if (configCompile.getIsValid() != 0 && !groupList.contains(numRegion.getGroupId())) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中numRegionList中groupid为"
							+ numRegion.getGroupId() + "的域配置在groupRelationList中不存在";
				}

				// 插入配置时不能是无效
				if (!isUpdate && numRegion.getIsValid() == 0) {
					return "配置id为" + configCompile.getCompileId() + "的配置中numRegionList中的regionid为"
							+ numRegion.getRegionId() + "的域配置" + CompileJudgeCode.CompileIsNotInvalid.getErrorReason();
				}

			}
		}

		List<StrRegion> strRegionList = configCompile.getStrRegionList();
		if (strRegionList == null) {
			return "编译配置id为" + configCompile.getCompileId() + "的配置中"
					+ CompileJudgeCode.StrRegionIsNull.getErrorReason();
		}
		if (strRegionList != null && strRegionList.size() > 0) {
			for (StrRegion strRegion : strRegionList) {
				// if (configCompile.getIsValid() != 0 &&
				// !regionGroupIdList.contains(strRegion.getGroupId())) {
				if (!regionGroupIdList.contains(strRegion.getGroupId())) {
					regionGroupIdList.add(strRegion.getGroupId());
				}
				if (!type2TableNameIsOk(configCompile.getService(), strRegion.getTableName())) {
					return "配置id为" + configCompile.getCompileId() + "的配置中strRegionList中的regionid为"
							+ strRegion.getRegionId() + "的域配置tableName与编译配置业务类型不一致";
				}
				if (!isUpdate && Constants.BASE_VALIDATE) {
					String valStrRegion = valStrRegion(strRegion, compileId,
							ConfigSourcesService.isStrStrongRegion(strRegion.getTableName()));
					if (!valStrRegion.equals("ok")) {
						return valStrRegion;
					}
				}
				if (!isUpdate && Constants.SERVICE_VALIDATE) {
					String serviceStrRegionVal = serviceStrRegionVal(strRegion, compileId);
					if (!serviceStrRegionVal.equals("ok")) {
						return serviceStrRegionVal;
					}
				}
				if (isUpdate && isValid == 1 && strRegion.getIsValid() == 0) {
					configFlag = true;
				}
				if (configCompile.getIsValid() != 0 && !groupList.contains(strRegion.getGroupId())) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中strRegionList中groupid为"
							+ strRegion.getGroupId() + "的域配置在groupRelationList中不存在";
				}

				// 插入配置时不能是无效
				if (!isUpdate && strRegion.getIsValid() == 0) {
					return "配置id为" + configCompile.getCompileId() + "的配置中strRegionList中的regionid为"
							+ strRegion.getRegionId() + "的域配置" + CompileJudgeCode.CompileIsNotInvalid.getErrorReason();
				}

			}
		}

		List<IpRegion> ipRegionList = configCompile.getIpRegionList();

		if (ipRegionList == null) {
			return CompileJudgeCode.IpRegionIsNull.getErrorReason();
		}
		if (ipRegionList != null && ipRegionList.size() > 0) {
			for (IpRegion ipRegion : ipRegionList) {
				// if (configCompile.getIsValid() != 0 &&
				// !regionGroupIdList.contains(ipRegion.getGroupId())) {
				if (!regionGroupIdList.contains(ipRegion.getGroupId())) {
					regionGroupIdList.add(ipRegion.getGroupId());
				}

				if (!type2TableNameIsOk(configCompile.getService(), ipRegion.getTableName())) {
					return "配置id为" + configCompile.getCompileId() + "的配置中ipRegionList中的regionid为"
							+ ipRegion.getRegionId() + "的域配置tableName与编译配置业务类型不一致";
				}
				if (!isUpdate && Constants.BASE_VALIDATE) {
					String valIpRegion = valIpRegion(ipRegion, compileId);
					if (!valIpRegion.equals("ok")) {
						return valIpRegion;
					}
				}

				if (!isUpdate && Constants.SERVICE_VALIDATE) {
					String serviceIpRegionVal = serviceIpRegionVal(ipRegion, compileId);
					if (!serviceIpRegionVal.equals("ok")) {
						return serviceIpRegionVal;
					}
				}

				if (isUpdate && isValid == 1 && ipRegion.getIsValid() == 0) {
					configFlag = true;
				}
				if (configCompile.getIsValid() != 0 && !groupList.contains(ipRegion.getGroupId())) {
					return "编译配置id为" + configCompile.getCompileId() + "的配置中ipRegionList中groupid为"
							+ ipRegion.getGroupId() + "的域配置在groupRelationList中不存在";
				}

				// 插入配置时不能是无效
				if (!isUpdate && ipRegion.getIsValid() == 0) {
					return "配置id为" + configCompile.getCompileId() + "的配置中ipRegionList中的regionid为"
							+ ipRegion.getRegionId() + "的域配置" + CompileJudgeCode.CompileIsNotInvalid.getErrorReason();
				}

			}
		}

		// if (configCompile.getIsValid() != 0 && regionGroupIdList.size() > 0
		// && groupList.size() > 0) {// 添加配置或修改域配置时判断配置分组集合中groupId是否都在域配置使用
		if (groupList.size() > 0) {// 修改域配置时判断配置分组集合中groupId是否都在域配置使用
			for (Long groupId : groupList) {
				if (!regionGroupIdList.contains(groupId)) {
					// setSbStr("配置id为" + configCompile.getCompileId() +
					// "的配置中配置分组groupid为" + groupId+ "的配置在域配置中未被使用(可能是分组复用情况).",
					// sb);//20161206业务修改:业务目前不会出现分组复用情形,如果前端传过来的json串出现分组复用情形则直接返回异常提示信息
					return "配置id为" + configCompile.getCompileId() + "的配置中配置分组groupid为" + groupId + "的配置在域配置中未被使用";
				}
				// if (configCompile.getIsValid() != 0 && isUpdate &&
				// !regionGroupIdList.contains(groupId)) {
				// return "配置id为" + configCompile.getCompileId() +
				// "的配置在修改时域配置中的groupid为" + groupId
				// + "的配置在groupRelationList中不存在";
				// }
			}
		}

		if (regionGroupIdList.size() > 0) {
			for (Long groupId : regionGroupIdList) {
				if (!groupList.contains(groupId)) {
					return "配置id为" + configCompile.getCompileId() + "的配置中域配置中的groupid为" + groupId + "的域配置在配置分组关系中不存在";
				}
			}
		}

		if (configCompile.getIsValid() != 0 && !isUpdate) {
			// if (!(regionGroupIdList.size() > 0)) {
			// return CompileJudgeCode.CompileGroupSizeEqZero;
			// }
			if (!(groupList.size() > 0)) {
				return "配置id为" + configCompile.getCompileId() + "的配置在添加时"
						+ CompileJudgeCode.GroupListEQZero.getErrorReason();
			}
		}

		// 编译为有效 域配置至少有一个为无效
		if (isUpdate && isValid == 1) {
			if (!configFlag) {
				return "配置id为" + configCompile.getCompileId() + "的配置在修改时"
						+ CompileJudgeCode.CompileIsValide.getErrorReason();
			}
		}

		return CompileJudgeCode.CompileGroupSizeGtZero.getErrorReason();
	}

	/**
	 * 验证业务类型与域配置的表名是否对应
	 * 
	 * @param serviceType
	 * @param tableName
	 * @return
	 */
	public static boolean type2TableNameIsOk(Integer serviceType, String tableName) {
		if (null != serviceType && null != tableName && !tableName.equals("")) {
			Map<Integer, Map<String, String>> tableRelation = ConfigSourcesService.getTableRelation();
			Map<String, String> map = tableRelation.get(serviceType.intValue());
			if (null != map) {
				if (map.containsKey(tableName.toUpperCase())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 判断当前编译配置中的groupNum属性与配置分组groupList大小是否相等且是否均大于0小于等于8, 且每个配置分组中域配置个数大于0
	 * 
	 * @param configCompile
	 * @return GroupNumNEQGroupList GroupNumGtEight GroupListGtEight
	 *         GroupNumEQZero GroupListEQZero CompileGroupSizeEqZero
	 *         CompileGroupIdNotExist CompileGroupIdIsRepeat CompileIsOk
	 *         只有返回CompileIsOk才说明该条配置正常否则该条配置不正常,可根据枚举中成员的注释具体在返回错误信息
	 */
	public static String compileIsOk(ConfigCompile configCompile, boolean isUpdate, StringBuffer sb) {

		String msg1 = groupNumAndListIsOk(configCompile, isUpdate);
		if (msg1 != CompileJudgeCode.GroupNumAndListLtEight.getErrorReason()) {
			return msg1;
		}

		String msg2 = compileGroupSizeIsGtZero(configCompile, isUpdate, sb);
		if (msg2 != CompileJudgeCode.CompileGroupSizeGtZero.getErrorReason()) {
			return msg2;
		}
		String latentMsg = ",数据中可能存在的问题:";
		if (sb.length() > 0 && !sb.toString().contains(latentMsg)) {
			latentMsg = latentMsg + sb.toString();
			sb.setLength(0);
			sb.append(latentMsg);
		}
		return CompileJudgeCode.CompileIsOk.getErrorReason();
	}

	public static void setSbStr(String str, StringBuffer sb) {
		if (sb.length() <= 1024) {
			sb.append(str);
		}
	}

	public static String valCompile(ConfigCompile configCompile) {
		Long compileId = configCompile.getCompileId();
		if (null == compileId) {
			return "compileId字段不能为空";
		}
		if (null == configCompile.getService()) {
			return "id为" + compileId + "的编译配置中service不能为空";
		}

		if (null == configCompile.getAction()) {
			return "id为" + compileId + "的编译配置中action不能为空";
		}
		if (null == configCompile.getDoBlacklist()) {
			return "id为" + compileId + "的编译配置中doBlacklist不能为空";
		}
		if (null == configCompile.getDoLog()) {
			return "id为" + compileId + "的编译配置中doLog不能为空";
		}
		if (null == configCompile.getEffectiveRange() || configCompile.getEffectiveRange().equals("")) {
			return "id为" + compileId + "的编译配置中effectiveRange不能为空";
		}
		if (null == configCompile.getStartTime()) {
			return "id为" + compileId + "的编译配置中startTime不能为空";
		}
		if (null == configCompile.getEndTime()) {
			return "id为" + compileId + "的编译配置中endTime不能为空";
		}
		if (null == configCompile.getUserRegion() || configCompile.getUserRegion().equals("")) {
			return "id为" + compileId + "的编译配置中userRegion不能为空";
		}
		if (null == configCompile.getIsValid()) {
			return "id为" + compileId + "的编译配置中isValid不能为空";
		}
		if (null == configCompile.getGroupNum()) {
			return "id为" + compileId + "的编译配置中groupNum不能为空";
		}
		if (null == configCompile.getFatherCfgId()) {
			return "id为" + compileId + "的编译配置中fatherCfgId不能为空";
		}
		if (null == configCompile.getOpTime()) {
			return "id为" + compileId + "的编译配置中opTime不能为空";
		}
		return "ok";
	}

	public static String valCompileGroup(ConfigGroupRelation configGroupRelation, Long compileId) {
		Long groupId = configGroupRelation.getGroupId();
		if (null == groupId) {
			return "id为" + compileId + "的编译配置中的配置分组的groupId不能为空";
		}

		if (null == configGroupRelation.getCompileId()) {
			return "id为" + compileId + "的编译配置中的配置分组id为" + groupId + "的compileId不能为空";
		}
		if (null == configGroupRelation.getIsValid()) {
			return "id为" + compileId + "的编译配置中的配置分组id为" + groupId + "的isValid不能为空";
		}

		if (null == configGroupRelation.getOpTime()) {
			return "id为" + compileId + "的编译配置中的配置分组id为" + groupId + "的opTime不能为空";
		}
		return "ok";
	}

	public static String valIpRegion(IpRegion ipRegion, Long compileId) {

		Long regionId = ipRegion.getRegionId();
		if (null == regionId) {
			return "id为" + compileId + "的编译配置中的ip类域配置的regionId不能为空";
		}
		if (null == ipRegion.getGroupId()) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的groupId不能为空";
		}
		if (null == ipRegion.getAddrType()) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的addrType不能为空";
		}
		if (null == ipRegion.getSrcIp() || ipRegion.getSrcIp().equals("")) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的srcIp不能为空";
		}

		if (null == ipRegion.getMaskSrcIp() || ipRegion.getMaskSrcIp().equals("")) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的maskSrcIp不能为空";
		}
		if (null == ipRegion.getSrcPort() || ipRegion.getSrcPort().equals("")) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的srcPort不能为空";
		}
		if (null == ipRegion.getMaskSrcPort() || ipRegion.getMaskSrcPort().equals("")) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的maskSrcPort不能为空";
		}
		if (null == ipRegion.getDstIp() || ipRegion.getDstIp().equals("")) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的dstIp不能为空";
		}
		if (null == ipRegion.getMaskDstIp() || ipRegion.getMaskDstIp().equals("")) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的maskDstIp不能为空";
		}
		if (null == ipRegion.getDstPort() || ipRegion.getDstPort().equals("")) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的dstPort不能为空";
		}
		if (null == ipRegion.getMaskDstPort() || ipRegion.getMaskDstPort().equals("")) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的maskDstPort不能为空";
		}
		if (null == ipRegion.getProtocol()) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的protocol不能为空";
		}
		if (null == ipRegion.getDirection()) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的direction不能为空";
		}
		if (null == ipRegion.getIsValid()) {
			return "id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的isValid不能为空";
		}
		return "ok";
	}

	public static String valNumRegion(NumRegion numRegion, Long compileId) {
		Long regionId = numRegion.getRegionId();
		if (null == regionId) {
			return "id为" + compileId + "的编译配置中的数值类域配置的regionId不能为空";
		}
		if (null == numRegion.getGroupId()) {
			return "id为" + compileId + "的编译配置中的数值类域配置id为" + regionId + "的groupId不能为空";
		}
		if (null == numRegion.getLowBoundary()) {
			return "id为" + compileId + "的编译配置中的数值类域配置id为" + regionId + "的lowBoundary不能为空";
		}
		if (null == numRegion.getUpBoundary()) {
			return "id为" + compileId + "的编译配置中的数值类域配置id为" + regionId + "的upBoundary不能为空";
		}
		return "ok";
	}

	public static String valStrRegion(StrRegion strRegion, Long compileId, boolean isDirtrict) {
		Long regionId = strRegion.getRegionId();
		if (null == regionId) {
			return "id为" + compileId + "的编译配置中的字符串类域配置的regionId不能为空";
		}
		if (null == strRegion.getGroupId()) {
			return "id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的groupId不能为空";
		}
		if (isDirtrict && (null == strRegion.getDistrict() || strRegion.getDistrict().equals(""))) {
			return "id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的district不能为空";
		}

		if (null == strRegion.getKeywords() || strRegion.getKeywords().equals("")) {
			return "id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的keywords不能为空";
		}

		if (null == strRegion.getExprType()) {
			return "id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的exprType不能为空";
		}

		if (null == strRegion.getMatchMethod()) {
			return "id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的matchMethod不能为空";
		}
		if (null == strRegion.getIsHexbin()) {
			return "id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的isHexbin不能为空";
		}
		return "ok";
	}

	public static String serviceIpRegionVal(IpRegion ipRegion, Long compileId) {
		if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(), ipRegion.getAddrType())) {
			return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置srcIp的格式不正确或与addrType不一致";
		}
		if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(), ipRegion.getAddrType())) {
			return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置maskSrcIp的格式不正确或与addrType不一致";
		}
		if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(), ipRegion.getAddrType())) {
			return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置dstIp的格式不正确或与addrType不一致";
		}
		if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(), ipRegion.getAddrType())) {
			return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置maskDstIp的格式不正确或与addrType不一致";
		}

		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getSrcPort())) {
			return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" + ipRegion.getRegionId() + "的域配置srcPort的格式不正确";
		}
		// 端口掩码目前先不做0-65535校验,仅做是否是数字校验
		// if (BasicProvingUtil.isPortOrPortMask(ipRegion.getMaskSrcPort())) {
		if (!BasicProvingUtil.isIntType(ipRegion.getMaskSrcPort())) {
			return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置maskSrcPort的格式不正确";
		}
		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getDstPort())) {
			return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" + ipRegion.getRegionId() + "的域配置dstPort的格式不正确";
		}
		// 端口掩码目前先不做0-65535校验,仅做是否是数字校验
		// if (BasicProvingUtil.isPortOrPortMask(ipRegion.getMaskDstPort())) {
		if (!BasicProvingUtil.isIntType(ipRegion.getMaskDstPort())) {
			return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置maskDstPort的格式不正确";
		}
		// if (null == ipRegion.getDirection() || (ipRegion.getDirection() != 1
		// && ipRegion.getDirection() != 0)) {
		// return "编译配置id为" + compileId + "的配置中ipRegionList中regionId为" +
		// ipRegion.getRegionId()
		// + "的域配置direction的值不正确,只能是0或1";
		// }
		return "ok";
	}

	public static String serviceStrRegionVal(StrRegion strRegion, Long compileId) {
		String url[] = { "DF_HTTP_URL", "DF_FTP_URL", "DJ_HTTP_URL", "DJ_FTP_URL", "DF_L2TP_URL", "DF_PPTP_URL" };
		String mail[] = { "DF_MAIL_HDR", "DJ_MAIL_HDR" };
		String keyWord[] = { "DF_HTTP_REQ_HDR", "DF_HTTP_RES_HDR", "DF_HTTP_REQ_BODY", "DF_HTTP_RES_BODY",
				"DF_MAIL_BODY", "DF_DNS_REGION", "DF_SSL_REGION", "FX_HTTP_REQ_HDR", "FX_DNS_REGION", "DJ_IP_PKT_BIN",
				"DJ_HTTP_REQ_HDR", "DJ_HTTP_RES_HDR", "DJ_HTTP_REQ_BODY", "DJ_HTTP_RES_BODY", "DJ_MAIL_BODY",
				"DJ_DNS_RES_REGION", "DJ_DNS_REQ_REGION", "DJ_SSL_REGION", "FX_HTTP_URL" };

		List<String> urlList = Arrays.asList(url);
		List<String> mailList = Arrays.asList(mail);
		List<String> keyWordList = Arrays.asList(keyWord);

		String tableName = strRegion.getTableName();
		String keywords = strRegion.getKeywords();
		if (urlList.contains(tableName.toUpperCase())) {
			List<DataDictionaryValue> dictValue = SystemConfigListener.getDictValue("url");
			if (null != dictValue && dictValue.size() > 0) {
				for (DataDictionaryValue dataDictionaryValue : dictValue) {
					if (dataDictionaryValue.getDataDictValue().equals(keywords)) {
						return "编译配置id为" + compileId + "的配置中strRegionList中regionId为" + strRegion.getRegionId()
								+ "的域配置keywords=" + keywords + "被设置为禁配";
					}
				}
			}

		} else if (mailList.contains(tableName.toUpperCase())) {
			List<DataDictionaryValue> dictValue = SystemConfigListener.getDictValue("email");
			if (null != dictValue && dictValue.size() > 0) {
				for (DataDictionaryValue dataDictionaryValue : dictValue) {
					if (dataDictionaryValue.getDataDictValue().equals(keywords)) {
						return "编译配置id为" + compileId + "的配置中strRegionList中regionId为" + strRegion.getRegionId()
								+ "的域配置keywords=" + keywords + "被设置为禁配";
					}
				}
			}
		} else if (keyWordList.contains(tableName.toUpperCase())) {
			List<DataDictionaryValue> dictValue = SystemConfigListener.getDictValue("keywords");
			if (null != dictValue && dictValue.size() > 0) {
				String[] split = keywords.split("(?<=[^\\\\])&");
				for (String str : split) {
					for (DataDictionaryValue dataDictionaryValue : dictValue) {
						if (dataDictionaryValue.getDataDictValue().equals(str)) {
							return "编译配置id为" + compileId + "的配置中strRegionList中regionId为" + strRegion.getRegionId()
									+ "的域配置keywords=" + str + "被设置为禁配";
						}
					}
				}
			}
		}
		return "ok";

	}

	public static void main(String[] args) {
		String key = "中\\&国\\&人&河南人&周口人&郸城人";
		String[] split = key.split("(?<=[^\\\\])&");
		for (String str : split) {
			System.out.println(str);
		}

	}

}
