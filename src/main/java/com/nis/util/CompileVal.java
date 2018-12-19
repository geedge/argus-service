package com.nis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DataDictionaryValue;
import com.nis.domain.restful.DigestRegion;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.listener.SystemConfigListener;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.web.service.restful.ConfigSourcesService;
import com.zdjizhi.utils.StringUtil;

public class CompileVal {

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
	public static void groupNumAndListIsOk(ConfigCompile configCompile, boolean isUpdate) throws Exception {
		if (null != configCompile) {
			if (!isUpdate && Constants.BASE_VALIDATE) {
				valCompile(configCompile);
			}
			if (null != configCompile.getGroupNum() && configCompile.getGroupNum() <= 0) {
				throw new RestServiceException(
						"编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ RestBusinessCode.GroupNumIsZero.getErrorReason(),
						RestBusinessCode.GroupNumIsZero.getValue());
			}
			if (null != configCompile.getGroupNum() && configCompile.getGroupNum() > 8) {
				throw new RestServiceException(
						"编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ RestBusinessCode.GroupNumGtEight.getErrorReason(),
						RestBusinessCode.GroupNumGtEight.getValue());
			}

			if (null == configCompile.getGroupRelationList() || configCompile.getGroupRelationList().size() == 0) {
				throw new RestServiceException(
						"编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ RestBusinessCode.CompileGroupIsNull.getErrorReason(),
						RestBusinessCode.CompileGroupIsNull.getValue());
			}

			if (configCompile.getGroupRelationList().size() != configCompile.getGroupNum()) {
				throw new RestServiceException(
						"编译配置id为" + configCompile.getCompileId() + "的配置中"
								+ RestBusinessCode.GroupNumNEQGroupList.getErrorReason(),
						RestBusinessCode.GroupNumNEQGroupList.getValue());
			}

			if (!isUpdate) {
				if (configCompile.getIsValid() != 1) {

					throw new RestServiceException("编译配置id为" + configCompile.getCompileId() + "的配置在添加时不能为无效",
							RestBusinessCode.IsValidIsT.getValue());

				}
			} else {
				throw new RestServiceException("编译配置id为" + configCompile.getCompileId() + "的配置在修改时不能为有效",
						RestBusinessCode.IsValidIsF.getValue());
			}
		} else {
			throw new RestServiceException(RestBusinessCode.CompileIsNull.getErrorReason(),
					RestBusinessCode.CompileIsNull.getValue());
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
	public static void compileGroupSizeIsGtZero(ConfigCompile configCompile, boolean isUpdate, StringBuffer sb)
			throws Exception {
		Long compileId = configCompile.getCompileId();

		if (!isUpdate && Constants.SERVICE_VALIDATE) {
			serviceConfigCompileVal(configCompile);
		}

		List<Long> groupList = new ArrayList<Long>();// 配置分组中groupId集合
		List<ConfigGroupRelation> groupRelationList = configCompile.getGroupRelationList();
		if (groupRelationList.size() > 0) {
			for (ConfigGroupRelation configGroupRelation : groupRelationList) {
				if (!isUpdate && (null == configGroupRelation.getIsValid() || configGroupRelation.getIsValid() != 1)) {
					throw new RestServiceException(
							"编译配置id为" + configCompile.getCompileId() + "的配置groupRelationList字段中的groupId为"
									+ configGroupRelation.getGroupId() + "的配置在添加时不能为无效",
							RestBusinessCode.IsValidIsT.getValue());
				}
				if (isUpdate && (null == configGroupRelation.getIsValid() || configGroupRelation.getIsValid() != 0)) {
					throw new RestServiceException(
							"编译配置id为" + configCompile.getCompileId() + "的配置groupRelationList字段中的groupId为"
									+ configGroupRelation.getGroupId() + "的配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}

				if (!isUpdate && Constants.BASE_VALIDATE) {
					valCompileGroup(configGroupRelation, compileId);
				}

				if (!configGroupRelation.getCompileId().equals(compileId)) {
					throw new RestServiceException(
							"编译配置id为" + configCompile.getCompileId() + "的配置中groupRelationList字段中的groupId为"
									+ configGroupRelation.getGroupId() + "的配置分组与编译配置id不一致",
							RestBusinessCode.CompileIdIsNeq.getValue());
				}
				if (!groupList.contains(configGroupRelation.getGroupId())) {
					groupList.add(configGroupRelation.getGroupId());
				} else {
					throw new RestServiceException(
							"编译配置id为" + configCompile.getCompileId() + "的配置中groupRelationList存在多个groupId为"
									+ configGroupRelation.getGroupId() + "的配置分组",
							RestBusinessCode.GroupIdIsRepeat.getValue());
				}
			}
		}
		//只有分组复用的业务类型，域可以不埴写，其他的maat配置至少填写一个域配置
		Boolean hasRegionFlag = false;
		List<Long> regionGroupIdList = new ArrayList<Long>();// 所有域配置中groupId的集合,不重复
		List<NumRegion> numRegionList = configCompile.getNumRegionList();
		if (numRegionList == null) {
			throw new RestServiceException(
					"编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ RestBusinessCode.NumRegionIsNull.getErrorReason(),
					RestBusinessCode.NumRegionIsNull.getValue());
		}
		if (numRegionList.size() > 0) {
			hasRegionFlag = true;
			for (NumRegion numRegion : numRegionList) {
				// if (configCompile.getIsValid() != 0 &&
				// !regionGroupIdList.contains(numRegion.getGroupId())) {

				if (!StringUtil.isEmpty(numRegion.getTableName())
						&& !type2TableNameIsOk(configCompile.getService(), numRegion.getTableName())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中numRegionList中的regionId为"
									+ numRegion.getRegionId() + "的域配置tableName与编译配置业务类型不一致",
							RestBusinessCode.TableNameUnmatchService.getValue());
				}
				if (!isUpdate && numRegion.getIsValid() != 1) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中numRegionList中的regionId为"
									+ numRegion.getRegionId() + "的域配置在添加时不能为无效",
							RestBusinessCode.IsValidIsT.getValue());
				}
				if (isUpdate && numRegion.getIsValid() != 0) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中numRegionList中的regionId为"
									+ numRegion.getRegionId() + "的域配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}

				if (groupList.size() > 0 && !groupList.contains(numRegion.getGroupId())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中numRegionList中的regionId为"
									+ numRegion.getRegionId() + "的配置的groupId在配置分组关系中不存在",
							RestBusinessCode.RegionsGroupIdIsNotFound.getValue());
				}

				if (!regionGroupIdList.contains(numRegion.getGroupId())) {
					regionGroupIdList.add(numRegion.getGroupId());
				}

				if (!isUpdate && Constants.BASE_VALIDATE) {
					valNumRegion(numRegion, compileId);
				}
				if (!isUpdate && Constants.SERVICE_VALIDATE) {
					serviceNumRegionVal(numRegion, compileId);
				}
			}
		}

		List<StrRegion> strRegionList = configCompile.getStrRegionList();
		if (strRegionList == null) {
			throw new RestServiceException(
					"编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ RestBusinessCode.StrRegionIsNull.getErrorReason(),
					RestBusinessCode.StrRegionIsNull.getValue());
		}
		if (strRegionList != null && strRegionList.size() > 0) {
			hasRegionFlag = true;
			for (StrRegion strRegion : strRegionList) {
				// if (configCompile.getIsValid() != 0 &&
				// !regionGroupIdList.contains(strRegion.getGroupId())) {
				if (!StringUtil.isEmpty(strRegion.getTableName())
						&& !type2TableNameIsOk(configCompile.getService(), strRegion.getTableName())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中strRegionList中的regionId为"
									+ strRegion.getRegionId() + "的域配置tableName与编译配置业务类型不一致",
							RestBusinessCode.TableNameUnmatchService.getValue());
				}
				if (!isUpdate && strRegion.getIsValid() != 1) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中strRegionList中的regionId为"
									+ strRegion.getRegionId() + "的域配置在添加时不能为无效",
							RestBusinessCode.IsValidIsT.getValue());
				}
				if (isUpdate && strRegion.getIsValid() != 0) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中strRegionList中的regionId为"
									+ strRegion.getRegionId() + "的域配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}

				if (groupList.size() > 0 && !groupList.contains(strRegion.getGroupId())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中strRegionList中的regionId为"
									+ strRegion.getRegionId() + "的配置的groupId在配置分组关系中不存在",
							RestBusinessCode.RegionsGroupIdIsNotFound.getValue());
				}

				if (!regionGroupIdList.contains(strRegion.getGroupId())) {
					regionGroupIdList.add(strRegion.getGroupId());
				}

				if (!isUpdate && Constants.BASE_VALIDATE) {
					valStrRegion(strRegion, compileId, ConfigSourcesService.isStrStrongRegion(strRegion.getTableName(),
							configCompile.getService()));
				}
				if (!isUpdate && Constants.SERVICE_VALIDATE) {
					serviceStrRegionVal(strRegion, compileId, ConfigSourcesService
							.isStrStrongRegion(strRegion.getTableName(), configCompile.getService()));
				}
			}
		}

		List<IpRegion> ipRegionList = configCompile.getIpRegionList();

		if (ipRegionList == null) {
			throw new RestServiceException(
					"编译配置id为" + configCompile.getCompileId() + "的配置中"
							+ RestBusinessCode.IpRegionIsNull.getErrorReason(),
					RestBusinessCode.IpRegionIsNull.getValue());
		}
		if (ipRegionList != null && ipRegionList.size() > 0) {
			hasRegionFlag = true;
			for (IpRegion ipRegion : ipRegionList) {
				// if (configCompile.getIsValid() != 0 &&
				// !regionGroupIdList.contains(ipRegion.getGroupId())) {
				if (!StringUtil.isEmpty(ipRegion.getTableName())
						&& !type2TableNameIsOk(configCompile.getService(), ipRegion.getTableName())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中ipRegionList中的regionId为"
									+ ipRegion.getRegionId() + "的域配置tableName与编译配置业务类型不一致",
							RestBusinessCode.TableNameUnmatchService.getValue());
				}

				if (!isUpdate && (null == ipRegion.getIsValid() || ipRegion.getIsValid() != 1)) {
					throw new RestServiceException("配置id为" + configCompile.getCompileId()
							+ "的配置中ipRegionList中的regionId为" + ipRegion.getRegionId() + "的域配置在添加时不能为无效",
							RestBusinessCode.IsValidIsT.getValue());
				}
				if (isUpdate && (null == ipRegion.getIsValid() || ipRegion.getIsValid() != 0)) {
					throw new RestServiceException("配置id为" + configCompile.getCompileId()
							+ "的配置中ipRegionList中的regionId为" + ipRegion.getRegionId() + "的域配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}

				if (groupList.size() > 0 && !groupList.contains(ipRegion.getGroupId())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中ipRegionList中的regionId为"
									+ ipRegion.getRegionId() + "的配置的groupId在配置分组关系中不存在",
							RestBusinessCode.RegionsGroupIdIsNotFound.getValue());
				}
				if (!regionGroupIdList.contains(ipRegion.getGroupId())) {
					regionGroupIdList.add(ipRegion.getGroupId());
				}

				if (!isUpdate && Constants.BASE_VALIDATE) {
					valIpRegion(ipRegion, compileId);
				}

				if (!isUpdate && Constants.SERVICE_VALIDATE) {
					serviceIpRegionVal(ipRegion, compileId, "ipRegionList");
				}
			}
		}
		List<DigestRegion> digestRegionList = configCompile.getDigestRegionList();

		// 不判断是否为空

		// 验证
		if (digestRegionList != null && digestRegionList.size() > 0) {
			hasRegionFlag = true;
			for (DigestRegion digestRegion : digestRegionList) {

				// 不验证表名和业务类型是否对应

				if (!isUpdate && digestRegion.getIsValid() != 1) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中digestRegionList中的regionId为"
									+ digestRegion.getRegionId() + "的域配置在添加时不能为无效",
							RestBusinessCode.IsValidIsT.getValue());
				}
				if (isUpdate && digestRegion.getIsValid() != 0) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中digestRegionList中的regionId为"
									+ digestRegion.getRegionId() + "的域配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}

				if (groupList.size() > 0 && !groupList.contains(digestRegion.getGroupId())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中digestRegion中的regionId为"
									+ digestRegion.getRegionId() + "的配置的groupId在配置分组关系中不存在",
							RestBusinessCode.RegionsGroupIdIsNotFound.getValue());
				}
				if (!regionGroupIdList.contains(digestRegion.getGroupId())) {
					regionGroupIdList.add(digestRegion.getGroupId());
				}

				if (!isUpdate && Constants.BASE_VALIDATE) {
					valDigestRegion(digestRegion, compileId);
				}

				if (!isUpdate && Constants.SERVICE_VALIDATE) {
					serviceDigestRegionVal(digestRegion, compileId);
				}
			}
		}

		List<IpRegion> ipClientRangeList = configCompile.getIpClientRangeList();

		if (ipClientRangeList != null && ipClientRangeList.size() > 0) {
			hasRegionFlag = true;
			for (IpRegion ipRegion : ipClientRangeList) {
				// if (configCompile.getIsValid() != 0 &&
				// !regionGroupIdList.contains(ipRegion.getGroupId())) {
				if (!StringUtil.isEmpty(ipRegion.getTableName())
						&& !type2TableNameIsOk(configCompile.getService(), ipRegion.getTableName())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中ipClientRangeList中的regionId为"
									+ ipRegion.getRegionId() + "的域配置tableName与编译配置业务类型不一致",
							RestBusinessCode.TableNameUnmatchService.getValue());
				}

				if (!isUpdate && (null == ipRegion.getIsValid() || ipRegion.getIsValid() != 1)) {
					throw new RestServiceException("配置id为" + configCompile.getCompileId()
							+ "的配置中ipClientRangeList中的regionId为" + ipRegion.getRegionId() + "的域配置在添加时不能为无效",
							RestBusinessCode.IsValidIsT.getValue());
				}
				if (isUpdate && (null == ipRegion.getIsValid() || ipRegion.getIsValid() != 0)) {
					throw new RestServiceException("配置id为" + configCompile.getCompileId()
							+ "的配置中ipClientRangeList中的regionId为" + ipRegion.getRegionId() + "的域配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}

				if (groupList.size() > 0 && !groupList.contains(ipRegion.getGroupId())) {
					throw new RestServiceException(
							"配置id为" + configCompile.getCompileId() + "的配置中ipClientRangeList中的regionId为"
									+ ipRegion.getRegionId() + "的配置的groupId在配置分组关系中不存在",
							RestBusinessCode.RegionsGroupIdIsNotFound.getValue());
				}
				if (!regionGroupIdList.contains(ipRegion.getGroupId())) {
					regionGroupIdList.add(ipRegion.getGroupId());
				}

				if (!isUpdate && Constants.BASE_VALIDATE) {
					valIpRegion(ipRegion, compileId);
				}

				if (!isUpdate && Constants.SERVICE_VALIDATE) {
					serviceIpRegionVal(ipRegion, compileId, "ipClientRangeList");
				}
			}
		}
		
		if (!hasRegionFlag&&!ServiceAndRDBIndexReal.serviceIsReuse(configCompile.getService())) {
			throw new RestServiceException("配置id为" + configCompile.getCompileId() + "的业务类型属于分组复用，域配置信息不能全为空",RestBusinessCode.RegionListIsNull.getValue());
		}
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
			List<String> list = new ArrayList<String>();
			Map<Integer, List<String>> sercieNameMap = ServiceAndRDBIndexReal.getSercieNameMap().get(serviceType);
			if (sercieNameMap != null && sercieNameMap.size() > 0) {
				for (Integer type : sercieNameMap.keySet()) {

					List<String> tableList = sercieNameMap.get(type);
					if (tableList != null && tableList.size() > 0) {
						for (String table : tableList) {
							list.add(table.toLowerCase());
						}
					}

				}

			}
			// Map<Integer, Map<String, String>> tableRelation =
			// ConfigSourcesService.getTableRelation();
			// Map<String, String> map = tableRelation.get(serviceType.intValue());
			if (list.contains(tableName.toLowerCase())) {
				return true;
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
	 * @throws Exception 
	 */
	public static void compileIsOk(ConfigCompile configCompile, boolean isUpdate, StringBuffer sb) throws Exception {

		groupNumAndListIsOk(configCompile, isUpdate);
		compileGroupSizeIsGtZero(configCompile, isUpdate, sb);
		String latentMsg = ",数据中可能存在的问题:";
		if (sb.length() > 0 && !sb.toString().contains(latentMsg)) {
			latentMsg = latentMsg + sb.toString();
			sb.setLength(0);
			sb.append(latentMsg);
		}

	}

	public static void setSbStr(String str, StringBuffer sb) {
		if (sb.length() <= 1024) {
			sb.append(str);
		}
	}

	public static void valCompile(ConfigCompile configCompile) throws Exception {
		Long compileId = configCompile.getCompileId();
		if (null == compileId) {
			throw new RestServiceException(RestBusinessCode.CompileIdIsNull.getErrorReason(),
					RestBusinessCode.CompileIdIsNull.getValue());
		}
		if (null == configCompile.getService()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中service不能为空",
					RestBusinessCode.ServiceIsNull.getValue());
		}

		if (null == configCompile.getAction()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中action不能为空",
					RestBusinessCode.ActionIsNull.getValue());
		}
		if (StringUtil.isEmpty(configCompile.getContType())) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中contType不能为空",
					RestBusinessCode.ContTypeIsNull.getValue());
		}
		
		if (StringUtil.isEmpty(configCompile.getAttrType())) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中attrType不能为空",
					RestBusinessCode.AttrTypeIsNull.getValue());
		}
		if (StringUtil.isEmpty(configCompile.getTaskId())) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中taskId不能为空",
					RestBusinessCode.TaskIdIsNull.getValue());
		}
		
		if (StringUtil.isEmpty(configCompile.getContLabel())) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中contLabel不能为空",
					RestBusinessCode.ContLabelIsNull.getValue());
		}
		if (StringUtil.isEmpty(configCompile.getAffairId())) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中affairId不能为空",
					RestBusinessCode.AffairIdIsNull.getValue());
		}
		if (null == configCompile.getDoBlacklist()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中doBlacklist不能为空",
					RestBusinessCode.DoBlacklistIsNull.getValue());
		}
		if (null == configCompile.getDoLog()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中doLog不能为空",
					RestBusinessCode.DoLogIsNull.getValue());
		}else if ("1".equals(configCompile.getAction().toString())&&configCompile.getDoLog().toString().equals("0")) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置,动作(action)为监测(1)时doLog的值不能为0",
					RestBusinessCode.DoLogIsWrong.getValue());
		}
		if (null == configCompile.getEffectiveRange() || configCompile.getEffectiveRange().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中effectiveRange不能为空",
					RestBusinessCode.EffectiveRangeIsNull.getValue());
		}
		if (null == configCompile.getStartTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中startTime不能为空",
					RestBusinessCode.StartTimeIsNull.getValue());
		}
		if (null == configCompile.getEndTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中endTime不能为空",
					RestBusinessCode.EndTimeIsNull.getValue());
		}
		if (null == configCompile.getUserRegion() ||
			configCompile.getUserRegion().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中userRegion不能为空",
					RestBusinessCode.UserRegionIsNull.getValue());
		}
		if (null == configCompile.getIsValid()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中isValid不能为空",
					RestBusinessCode.IsValidIsNull.getValue());
		}
		if (null == configCompile.getGroupNum()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中groupNum不能为空",
					RestBusinessCode.GroupNumIsNull.getValue());
		}
		if (null == configCompile.getFatherCfgId()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中fatherCfgId不能为空",
					RestBusinessCode.FatherCfgIdIsNull.getValue());
		}
		if (null == configCompile.getOpTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中opTime不能为空",
					RestBusinessCode.OpTimeIsNull.getValue());
		}
	}

	public static void valCompileGroup(ConfigGroupRelation configGroupRelation, Long compileId) throws Exception {
		Long groupId = configGroupRelation.getGroupId();
		if (null == groupId) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的配置分组的groupId不能为空",
					RestBusinessCode.GroupIdIsNull.getValue());
		}

		if (null == configGroupRelation.getCompileId()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的配置分组id为" + groupId + "的配置compileId不能为空",
					RestBusinessCode.GroupsCompileIdIsNull.getValue());
		}
		if (null == configGroupRelation.getIsValid()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的配置分组id为" + groupId + "的配置isValid不能为空",
					RestBusinessCode.GroupsIsValidIsNull.getValue());
		}

		if (null == configGroupRelation.getOpTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的配置分组id为" + groupId + "的配置opTime不能为空",
					RestBusinessCode.OpTimeIsNull.getValue());
		}
	}

	public static void valIpRegion(IpRegion ipRegion, Long compileId) {
		Long regionId = ipRegion.getRegionId();
		if (null == regionId) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置的regionId不能为空",
					RestBusinessCode.RegionIdIsNull.getValue());
		}
		if (null == ipRegion.getGroupId()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置groupId不能为空",
					RestBusinessCode.RegionsGroupIdIsNull.getValue());
		}
		if (null == ipRegion.getAddrType()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置addrType不能为空",
					RestBusinessCode.AddrTypeIsNull.getValue());
		}
		if (null == ipRegion.getSrcIp() || ipRegion.getSrcIp().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置srcIp不能为空",
					RestBusinessCode.SrcIpIsNull.getValue());
		}

		if (null == ipRegion.getMaskSrcIp() || ipRegion.getMaskSrcIp().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置maskSrcIp不能为空",
					RestBusinessCode.MaskSrcIpIsNull.getValue());
		}
		if (null == ipRegion.getSrcPort() || ipRegion.getSrcPort().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置srcPort不能为空",
					RestBusinessCode.SrcPortIsNull.getValue());
		}
		if (null == ipRegion.getMaskSrcPort() || ipRegion.getMaskSrcPort().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置maskSrcPort不能为空",
					RestBusinessCode.MaskSrcPortIsNull.getValue());
		}
		if (null == ipRegion.getDstIp() || ipRegion.getDstIp().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置dstIp不能为空",
					RestBusinessCode.DstIpIsNull.getValue());
		}
		if (null == ipRegion.getMaskDstIp() || ipRegion.getMaskDstIp().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置maskDstIp不能为空",
					RestBusinessCode.MaskDstIpIsNull.getValue());
		}
		if (null == ipRegion.getDstPort() || ipRegion.getDstPort().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置dstPort不能为空",
					RestBusinessCode.DstPortIsNull.getValue());
		}
		if (null == ipRegion.getMaskDstPort() || ipRegion.getMaskDstPort().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置maskDstPort不能为空",
					RestBusinessCode.MaskDstPortIsNull.getValue());
		}
		if (null == ipRegion.getProtocol()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置protocol不能为空",
					RestBusinessCode.ProtocolIsNull.getValue());
		}
		if (null == ipRegion.getDirection()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置direction不能为空",
					RestBusinessCode.DirectionIsNull.getValue());
		}
		if (null == ipRegion.getIsValid()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置isValid不能为空",
					RestBusinessCode.IsValidIsNull.getValue());
		}
		if (null == ipRegion.getOpTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的ip类域配置id为" + regionId + "的配置opTime不能为空",
					RestBusinessCode.OpTimeIsNull.getValue());
		}
	}

	public static void valNumRegion(NumRegion numRegion, Long compileId) throws Exception {
		Long regionId = numRegion.getRegionId();
		if (null == regionId) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的数值类域配置的regionId不能为空",
					RestBusinessCode.RegionIdIsNull.getValue());
		}
		if (null == numRegion.getGroupId()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的数值类域配置id为" + regionId + "的配置groupId不能为空",
					RestBusinessCode.RegionsGroupIdIsNull.getValue());
		}
		if (null == numRegion.getLowBoundary()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的数值类域配置id为" + regionId + "的配置lowBoundary不能为空",
					RestBusinessCode.LowBoundaryIsNull.getValue());
		}
		if (null == numRegion.getUpBoundary()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的数值类域配置id为" + regionId + "的配置upBoundary不能为空",
					RestBusinessCode.UpBoundaryIsNull.getValue());
		}
		if (null == numRegion.getOpTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的数值类域配置id为" + regionId + "的配置opTime不能为空",
					RestBusinessCode.OpTimeIsNull.getValue());
		}
	}

	public static void valStrRegion(StrRegion strRegion, Long compileId, boolean isDirtrict) throws Exception {
		Long regionId = strRegion.getRegionId();
		if (null == regionId) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的字符串类域配置的regionId不能为空",
					RestBusinessCode.RegionIdIsNull.getValue());
		}
		if (null == strRegion.getGroupId()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的配置groupId不能为空",
					RestBusinessCode.RegionsGroupIdIsNull.getValue());
		}
		if (isDirtrict && (null == strRegion.getDistrict() || strRegion.getDistrict().equals(""))) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的配置district不能为空",
					RestBusinessCode.DistrictIsNull.getValue());
		}

		if (null == strRegion.getKeywords() || strRegion.getKeywords().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的配置keywords不能为空",
					RestBusinessCode.KeywordsIsNull.getValue());
		}

		if (null == strRegion.getExprType()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的配置exprType不能为空",
					RestBusinessCode.ExprTypeIsNull.getValue());
		}

		if (null == strRegion.getMatchMethod()) {
			throw new RestServiceException(
					"编译配置id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的配置matchMethod不能为空",
					RestBusinessCode.MatchMethodIsNull.getValue());
		}
		if (null == strRegion.getIsHexbin()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的配置isHexbin不能为空",
					RestBusinessCode.MatchMethodIsNull.getValue());
		}
		if (null == strRegion.getOpTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的字符串类域配置id为" + regionId + "的配置opTime不能为空",
					RestBusinessCode.OpTimeIsNull.getValue());
		}
	}

	public static void valDigestRegion(DigestRegion digestRegion, Long compileId) throws Exception {
		Long regionId = digestRegion.getRegionId();
		if (null == regionId) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的摘要类域配置的regionId不能为空",
					RestBusinessCode.RegionIdIsNull.getValue());
		}
		if (null == digestRegion.getGroupId()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的摘要类域配置id为" + regionId + "的配置groupId不能为空",
					RestBusinessCode.RegionsGroupIdIsNull.getValue());
		}
		if (null == digestRegion.getRawLen()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的摘要类域配置id为" + regionId + "的配置rawLen不能为空",
					RestBusinessCode.RawLenIsNull.getValue());
		}
		if (null == digestRegion.getDigest() || digestRegion.getDigest().equals("")) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的摘要类域配置id为" + regionId + "的配置digest不能为空",
					RestBusinessCode.DigestIsNull.getValue());
		}
		if (null == digestRegion.getCfdsLevel()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的摘要类域配置id为" + regionId + "的配置cfdsLevel不能为空",
					RestBusinessCode.CfdsLevelIsNull.getValue());
		}
		if (null == digestRegion.getOpTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的编译配置中的摘要类域配置id为" + regionId + "的配置opTime不能为空",
					RestBusinessCode.OpTimeIsNull.getValue());
		}
	}

	private static void validateIpAndMask(IpRegion ipRegion, Long compileId, String listName) throws Exception {
		// 源IP信息和目的IP信息格式为IPV4或IPV6
		if (ipRegion.getAddrType().intValue() == 4 || ipRegion.getAddrType().intValue() == 6) {
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(), ipRegion.getAddrType())) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置srcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(), ipRegion.getAddrType())) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置maskSrcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(), ipRegion.getAddrType())) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置dstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(), ipRegion.getAddrType())) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置maskDstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
		} else if (ipRegion.getAddrType().intValue() == 46) {// 4OVER6:源IP信息格式为IPV4、目的IP信息格式IPV6
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(), 4)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置srcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(), 4)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置maskSrcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(), 6)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置dstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(), 6)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置maskDstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
		} else if (ipRegion.getAddrType().intValue() == 64) {// 6OVER4:源IP信息格式为IPV6、目的IP信息格式IPV4
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(), 6)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置srcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(), 6)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置maskSrcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(), 4)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置dstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(), 4)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置maskDstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
		} else if (ipRegion.getAddrType().intValue() == 10) {// all:符合IP格式即可
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(), null)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置srcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(), null)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置maskSrcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(), null)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置dstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(), null)) {
				throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
						+ ipRegion.getRegionId() + "的域配置maskDstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
		}

	}

	public static void serviceIpRegionVal(IpRegion ipRegion, Long compileId, String listName) throws Exception {
		int addrType = ipRegion.getAddrType().intValue();
		if (!(addrType == 4|| addrType == 6 || addrType == 46|| addrType == 64 || addrType ==10)) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为" + ipRegion.getRegionId()
					+ "的域配置addrType的值只能为4(IPV4)、6(IPV6)、46(4OVER6隧道)、64(6OVER4隧道)、10(全部)",
					RestBusinessCode.AddrTypeIsWrongRange.getValue());
		}
		validateIpAndMask(ipRegion, compileId, listName);

		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getSrcPort())) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
					+ ipRegion.getRegionId() + "的域配置srcPort的格式不正确", RestBusinessCode.PortIsNotVal.getValue());
		}

		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getMaskSrcPort())) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
					+ ipRegion.getRegionId() + "的域配置maskSrcPort的格式不正确", RestBusinessCode.PortIsNotVal.getValue());
		}
		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getDstPort())) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
					+ ipRegion.getRegionId() + "的域配置dstPort的格式不正确", RestBusinessCode.PortIsNotVal.getValue());
		}

		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getMaskDstPort())) {
			// if (!BasicProvingUtil.isIntType(ipRegion.getMaskDstPort())) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
					+ ipRegion.getRegionId() + "的域配置maskDstPort的格式不正确", RestBusinessCode.PortIsNotVal.getValue());
		}

		if (ipRegion.getSrcIp().equals(ipRegion.getDstIp())) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
					+ ipRegion.getRegionId() + "的域配置srcIp和dstIp不能相同", RestBusinessCode.SrcIpEQDstIp.getValue());
		}
		if (ipRegion.getDirection() != 1 && ipRegion.getDirection() != 0) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中" + listName + "中regionId为"
					+ ipRegion.getRegionId() + "的域配置direction的值不正确,只能是0或1",
					RestBusinessCode.DirectionIsWrongRange.getValue());
		}

		// zdx protocol验证？
		// if (ipRegion.getTableName().toLowerCase().equals("dj_ip_port")) {
		// if (ipRegion.getProtocol() < 0 || ipRegion.getProtocol() > 255) {
		// return "编译配置id为" + compileId + "的配置中"+listName+"中regionId为" +
		// ipRegion.getRegionId()
		// + "的域配置tableName为dj_ip_port时,protocol的取值范围只能是0-255";
		// }
		// } else {
		// if (ipRegion.getProtocol() != 0) {
		// return "编译配置id为" + compileId + "的配置中"+listName+"中regionId为" +
		// ipRegion.getRegionId() + "的域配置tableName为"
		// + ipRegion.getTableName() + "时,protocol的值只能是0";
		// }
		//
		// }
	}

	public static void serviceNumRegionVal(NumRegion numRegion, Long compileId) throws Exception {
		Long lowBoundary = numRegion.getLowBoundary();
		Long upBoundary = numRegion.getUpBoundary();
		if (lowBoundary <= upBoundary) {
			if (lowBoundary > 4294967295l || lowBoundary < 0l) {
				throw new RestServiceException(
						"编译配置id为" + compileId + "的配置中numRegionList中regionId为" + numRegion.getRegionId()
								+ "的域配置lowBoundary的值不能大于2的32次方减一(4294967295)或者小于0",
						RestBusinessCode.LowBoundaryIsWrongRange.getValue());
			}
			if (upBoundary > 4294967295l || upBoundary < 0l) {
				throw new RestServiceException(
						"编译配置id为" + compileId + "的配置中numRegionList中regionId为" + numRegion.getRegionId()
								+ "的域配置upBoundary的值不能大于2的32次方减一(4294967295)或者小于0",
						RestBusinessCode.UpBoundaryIsWrongRange.getValue());
			}
		} else {
			throw new RestServiceException(
					"编译配置id为" + compileId + "的配置中numRegionList中regionId为" + numRegion.getRegionId()
							+ "的域配置lowBoundary的值大于upBoundary,应该是小于或等于",
					RestBusinessCode.LowBoundaryGTUpBoundary.getValue());
		}
	}

	public static void serviceConfigCompileVal(ConfigCompile configCompile) throws Exception {
		Long compileId = configCompile.getCompileId();
		Integer action = ServiceAndRDBIndexReal.getActionByService(configCompile.getService());
		if (StringUtil.isEmpty(action)) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中service与action的关系不存在",
					RestBusinessCode.Service2ActionIsNull.getValue());
		}

		if (action.compareTo(configCompile.getAction()) != 0) {
			throw new RestServiceException(
					"编译配置id为" + compileId + "的配置中service的值为" + configCompile.getService() + "时action只能为" + action,
					RestBusinessCode.ServiceUnmatchAction.getValue());
		}
		if (configCompile.getDoBlacklist() != 1) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中doBlacklist的值只能是1",
					RestBusinessCode.DoBlacklistIsWrongRange.getValue());
		}

		if (configCompile.getDoLog() != 0 && configCompile.getDoLog() != 1 && configCompile.getDoLog() != 2) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中doLog的值只能是0(不需要),1(需要),2(只记录结构化日志，不记录非结构化日志)",
					RestBusinessCode.DoLogIsWrongRange.getValue());
		}

		if (configCompile.getFatherCfgId() != 0) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中fatherCfgId的值只能是0",
					RestBusinessCode.FatherCfgIdIsWrongRange.getValue());
		}
		if (configCompile.getStartTime().getTime() > configCompile.getEndTime().getTime()) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中startTime不能比endTime晚",
					RestBusinessCode.EndTimeGTStartTime.getValue());
		}
	}

	public static void serviceStrRegionVal(StrRegion strRegion, Long compileId, boolean isDirtrict) throws Exception {

		Integer exprType = strRegion.getExprType();
		Integer matchMethod = strRegion.getMatchMethod();
		Integer isHexbin = strRegion.getIsHexbin();
		if (exprType != 0 && exprType != 1) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中strRegionList中regionId为"
					+ strRegion.getRegionId() + "的域配置exprType的值只能是0(无表达式)或者1(与表达式)",
					RestBusinessCode.ExprTypeIsWrongRange.getValue());
		}
		if (matchMethod != 0 && matchMethod != 1 && matchMethod != 2 && matchMethod != 3) {
			throw new RestServiceException(
					"编译配置id为" + compileId + "的配置中strRegionList中regionId为" + strRegion.getRegionId()
							+ "的域配置matchMethod的值只能是0(子串匹配),1(右匹配),2(左匹配),3(完全匹配)",
					RestBusinessCode.MatchMethodIsWrongRange.getValue());
		}

		if (isHexbin != 0 && isHexbin != 1 && isHexbin != 2) {
			throw new RestServiceException(
					"编译配置id为" + compileId + "的配置中strRegionList中regionId为" + strRegion.getRegionId()
							+ "的域配置isHexbin的值只能是0(大小写不敏感，且非HEX)或者1(HEX)或者2(大小写敏感，且非HEX)",
					RestBusinessCode.IsHexbinIsWrongRange.getValue());
		}
		// strRegProhibitConfigWord(strRegion,compileId);

	}

	public static void serviceDigestRegionVal(DigestRegion digestRegion, Long compileId) throws Exception {

		Integer cfdsLevel = digestRegion.getCfdsLevel();
		if (!(cfdsLevel >= 1 && cfdsLevel <= 10)) {
			throw new RestServiceException("编译配置id为" + compileId + "的配置中digestRegionList中regionId为"
					+ digestRegion.getRegionId() + "的域配置cfdsLevel的值只能是1到10",
					RestBusinessCode.CfdsLevelIsWrongRange.getValue());
		}

	}

	/**
	 * 禁配词业务校验
	 * 
	 * @param strRegion
	 * @param compileId
	 * @return
	 */

	public static String strRegProhibitConfigWord(StrRegion strRegion, Long compileId) {
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

}
