package com.nis.util;

import java.util.List;
import java.util.Map;

import com.nis.domain.restful.GroupReuse;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.web.service.restful.ConfigSourcesService;

public class GroupReuseVal {

	public static void valGroupReuse(List<GroupReuse> groupReuseList,
			Boolean isUpdate) throws Exception {
		for (GroupReuse groupReuse : groupReuseList) {
			Map<String, List<String>> groupReuseRegionMap = ServiceAndRDBIndexReal
					.getGroupReuseRegionMap();
			// 验证各类型域配置
			Boolean hasRegionFlag = false;
			if (groupReuseRegionMap.containsKey("numRegion")
					&& !StringUtil.isEmpty(groupReuse.getNumRegionList())
					&& groupReuse.getNumRegionList().size() > 0) {
				hasRegionFlag = true;
				List<NumRegion> numRegionList = groupReuse.getNumRegionList();
				for (NumRegion numRegion : numRegionList) {
					if (!StringUtil.isEmpty(numRegion.getTableName())	
							&& groupReuseRegionMap.get("numRegion").contains(numRegion.getTableName())) {
						throw new RestServiceException("numRegionList中的regionId为"
								+ numRegion.getRegionId()
								+ "的域配置tableName不是分组复用的域表",
								RestBusinessCode.TableNameUnReuse
										.getValue());
					}
					if (!isUpdate && numRegion.getIsValid() != 1) {
						throw new RestServiceException("numRegionList中的regionId为"
								+ numRegion.getRegionId() + "的域配置在添加时不能为无效",
								RestBusinessCode.IsValidIsT.getValue());
					}
					if (isUpdate && numRegion.getIsValid() != 0) {
						throw new RestServiceException("numRegionList中的regionId为"
								+ numRegion.getRegionId() + "的域配置在修改时不能为有效",
								RestBusinessCode.IsValidIsF.getValue());
					}

					if (!isUpdate && Constants.BASE_VALIDATE) {
						valNumRegion(numRegion);
					}
					if (!isUpdate && Constants.SERVICE_VALIDATE) {
						serviceNumRegionVal(numRegion);
					}
				}
			} else if (groupReuseRegionMap.containsKey("strRegion")
					&& !StringUtil.isEmpty(groupReuse.getStrRegionList())
					&& groupReuse.getStrRegionList().size() > 0) {
				hasRegionFlag = true;
				List<StrRegion> strRegionList = groupReuse.getStrRegionList();
				for (StrRegion strRegion : strRegionList) {
					// if (groupReuse.getIsValid() != 0 &&
					// !regionGroupIdList.contains(strRegion.getGroupId()))
					// {
					
					if (!StringUtil.isEmpty(strRegion.getTableName())	
							&& groupReuseRegionMap.get("strRegion").contains(strRegion.getTableName())) {
						throw new RestServiceException("strRegionList中的regionId为"
								+ strRegion.getRegionId()
								+ "的域配置tableName不是分组复用的域表",
								RestBusinessCode.TableNameUnReuse
										.getValue());
					}
					if (!isUpdate && strRegion.getIsValid() != 1) {
						throw new RestServiceException("strRegionList中的regionId为"
								+ strRegion.getRegionId() + "的域配置在添加时不能为无效",
								RestBusinessCode.IsValidIsT.getValue());
					}
					if (isUpdate && strRegion.getIsValid() != 0) {
						throw new RestServiceException("strRegionList中的regionId为"
								+ strRegion.getRegionId() + "的域配置在修改时不能为有效",
								RestBusinessCode.IsValidIsF.getValue());
					}

					if (!isUpdate && Constants.BASE_VALIDATE) {
						valStrRegion(strRegion);
					}
					if (!isUpdate && Constants.SERVICE_VALIDATE) {
						serviceStrRegionVal(strRegion);
					}
				}
			} else if (groupReuseRegionMap.containsKey("ipRegion")
					&& !StringUtil.isEmpty(groupReuse.getIpRegionList())
					&& groupReuse.getIpRegionList().size() > 0) {
				hasRegionFlag = true;
				List<IpRegion> ipRegionList = groupReuse.getIpRegionList();
				for (IpRegion ipRegion : ipRegionList) {
					// if (groupReuse.getIsValid() != 0 &&
					// !regionGroupIdList.contains(ipRegion.getGroupId())) {
					
					if (!StringUtil.isEmpty(ipRegion.getTableName())	
							&& groupReuseRegionMap.get("ipRegion").contains(ipRegion.getTableName())) {
						throw new RestServiceException("ipRegionList中的regionId为"
								+ ipRegion.getRegionId()
								+ "的域配置tableName不是分组复用的域表",
								RestBusinessCode.TableNameUnReuse
										.getValue());
					}

					if (!isUpdate
							&& (null == ipRegion.getIsValid() || ipRegion
									.getIsValid() != 1)) {
						throw new RestServiceException("ipRegionList中的regionId为"
								+ ipRegion.getRegionId() + "的域配置在添加时不能为无效",
								RestBusinessCode.IsValidIsT.getValue());
					}
					if (isUpdate
							&& (null == ipRegion.getIsValid() || ipRegion
									.getIsValid() != 0)) {
						throw new RestServiceException("ipRegionList中的regionId为"
								+ ipRegion.getRegionId() + "的域配置在修改时不能为有效",
								RestBusinessCode.IsValidIsF.getValue());
					}

					if (!isUpdate && Constants.BASE_VALIDATE) {
						valIpRegion(ipRegion);
					}

					if (!isUpdate && Constants.SERVICE_VALIDATE) {
						serviceIpRegionVal(ipRegion);
					}
				}
			}
			if (!hasRegionFlag) {
				// 所有的域类型都不包括，可能是配置文件配错了
				throw new ServiceRuntimeException(RestBusinessCode.ReuseRegionIsNull.getErrorReason(),
						RestBusinessCode.ReuseRegionIsNull.getValue());
			}
		}
	}

	public static void valIpRegion(IpRegion ipRegion) {
		Long regionId = ipRegion.getRegionId();
		if (null == regionId) {
			throw new RestServiceException("ipRegionList中的regionId不能为空",
					RestBusinessCode.RegionIdIsNull.getValue());
		}
		if (null == ipRegion.getGroupId()) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置groupId不能为空",
					RestBusinessCode.RegionsGroupIdIsNull.getValue());
		}
		if (null == ipRegion.getAddrType()) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置addrType不能为空",
					RestBusinessCode.AddrTypeIsNull.getValue());
		}
		if (null == ipRegion.getSrcIp() || ipRegion.getSrcIp().equals("")) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置srcIp不能为空",
					RestBusinessCode.SrcIpIsNull.getValue());
		}

		if (null == ipRegion.getMaskSrcIp()
				|| ipRegion.getMaskSrcIp().equals("")) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置maskSrcIp不能为空",
					RestBusinessCode.MaskSrcIpIsNull.getValue());
		}
		if (null == ipRegion.getSrcPort() || ipRegion.getSrcPort().equals("")) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置srcPort不能为空",
					RestBusinessCode.SrcPortIsNull.getValue());
		}
		if (null == ipRegion.getMaskSrcPort()
				|| ipRegion.getMaskSrcPort().equals("")) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置maskSrcPort不能为空",
					RestBusinessCode.MaskSrcPortIsNull.getValue());
		}
		if (null == ipRegion.getDstIp() || ipRegion.getDstIp().equals("")) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置dstIp不能为空",
					RestBusinessCode.DstIpIsNull.getValue());
		}
		if (null == ipRegion.getMaskDstIp()
				|| ipRegion.getMaskDstIp().equals("")) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置maskDstIp不能为空",
					RestBusinessCode.MaskDstIpIsNull.getValue());
		}
		if (null == ipRegion.getDstPort() || ipRegion.getDstPort().equals("")) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置dstPort不能为空",
					RestBusinessCode.DstPortIsNull.getValue());
		}
		if (null == ipRegion.getMaskDstPort()
				|| ipRegion.getMaskDstPort().equals("")) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置maskDstPort不能为空",
					RestBusinessCode.MaskDstPortIsNull.getValue());
		}
		if (null == ipRegion.getProtocol()) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置protocol不能为空",
					RestBusinessCode.ProtocolIsNull.getValue());
		}
		if (null == ipRegion.getDirection()) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置direction不能为空",
					RestBusinessCode.DirectionIsNull.getValue());
		}
		if (null == ipRegion.getIsValid()) {
			throw new RestServiceException("ipRegionList中id为" + regionId + "的配置isValid不能为空",
					RestBusinessCode.IsValidIsNull.getValue());
		}
	}

	public static void valNumRegion(NumRegion numRegion)
			throws Exception {
		Long regionId = numRegion.getRegionId();
		if (null == regionId) {
			throw new RestServiceException("数值类域配置的regionId不能为空",
					RestBusinessCode.RegionIdIsNull.getValue());
		}
		if (null == numRegion.getGroupId()) {
			throw new RestServiceException("数值类域配置id为" + regionId + "的配置groupId不能为空",
					RestBusinessCode.RegionsGroupIdIsNull.getValue());
		}
		if (null == numRegion.getLowBoundary()) {
			throw new RestServiceException("数值类域配置id为" + regionId + "的配置lowBoundary不能为空",
					RestBusinessCode.LowBoundaryIsNull.getValue());
		}
		if (null == numRegion.getUpBoundary()) {
			throw new RestServiceException("数值类域配置id为" + regionId + "的配置upBoundary不能为空",
					RestBusinessCode.UpBoundaryIsNull.getValue());
		}
	}

	public static void valStrRegion(StrRegion strRegion) throws Exception {
		Long regionId = strRegion.getRegionId();
		if (null == regionId) {
			throw new RestServiceException("strRegionList中regionId不能为空",
					RestBusinessCode.RegionIdIsNull.getValue());
		}
		if (null == strRegion.getGroupId()) {
			throw new RestServiceException("strRegionList中id为" + regionId + "的配置groupId不能为空",
					RestBusinessCode.RegionsGroupIdIsNull.getValue());
		}

		if (null == strRegion.getKeywords()
				|| strRegion.getKeywords().equals("")) {
			throw new RestServiceException("strRegionList中id为" + regionId + "的配置keywords不能为空",
					RestBusinessCode.KeywordsIsNull.getValue());
		}

		if (null == strRegion.getExprType()) {
			throw new RestServiceException("strRegionList中id为" + regionId + "的配置exprType不能为空",
					RestBusinessCode.ExprTypeIsNull.getValue());
		}

		if (null == strRegion.getMatchMethod()) {
			throw new RestServiceException("strRegionList中id为" + regionId + "的配置matchMethod不能为空",
					RestBusinessCode.MatchMethodIsNull.getValue());
		}
		if (null == strRegion.getIsHexbin()) {
			throw new RestServiceException("strRegionList中id为" + regionId + "的配置isHexbin不能为空",
					RestBusinessCode.MatchMethodIsNull.getValue());
		}
	}


	private static void validateIpAndMask(IpRegion ipRegion) throws Exception {
		// 源IP信息和目的IP信息格式s为IPV4或IPV6
		if (ipRegion.getAddrType().intValue() == 4
				|| ipRegion.getAddrType().intValue() == 6) {
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(),
					ipRegion.getAddrType())) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置srcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(),
					ipRegion.getAddrType())) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置maskSrcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(),
					ipRegion.getAddrType())) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置dstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(),
					ipRegion.getAddrType())) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置maskDstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
		} else if (ipRegion.getAddrType().intValue() == 46) {// 4OVER6:源IP信息格式为IPV4、目的IP信息格式IPV6
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(), 4)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置srcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(), 4)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置maskSrcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(), 6)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置dstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(), 6)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置maskDstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
		} else if (ipRegion.getAddrType().intValue() == 64) {// 6OVER4:源IP信息格式为IPV6、目的IP信息格式IPV4
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(), 6)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置srcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(), 6)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置maskSrcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(), 4)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置dstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(), 4)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置maskDstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
		} else if (ipRegion.getAddrType().intValue() == 10) {// all:符合IP格式即可
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getSrcIp(), null)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置srcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskSrcIp(), null)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置maskSrcIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getDstIp(), null)) {
				throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
						+ "的域配置dstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
			if (!BasicProvingUtil.isIpOrIpMask(ipRegion.getMaskDstIp(), null)) {
				throw new RestServiceException("ipRegionList中中regionId为" + ipRegion.getRegionId()
						+ "的域配置maskDstIp的格式不正确或与addrType不一致",
						RestBusinessCode.IPUnMatchAddrType.getValue());
			}
		}

	}

	public static void serviceIpRegionVal(IpRegion ipRegion) throws Exception {

		validateIpAndMask(ipRegion);

		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getSrcPort())) {
			throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置srcPort的格式不正确",
					RestBusinessCode.PortIsNotVal.getValue());
		}

		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getMaskSrcPort())) {
			throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置maskSrcPort的格式不正确",
					RestBusinessCode.PortIsNotVal.getValue());
		}
		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getDstPort())) {
			throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置dstPort的格式不正确",
					RestBusinessCode.PortIsNotVal.getValue());
		}

		if (!BasicProvingUtil.isPortOrPortMask(ipRegion.getMaskDstPort())) {
			// if (!BasicProvingUtil.isIntType(ipRegion.getMaskDstPort())) {
			throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置maskDstPort的格式不正确",
					RestBusinessCode.PortIsNotVal.getValue());
		}

		if (ipRegion.getSrcIp().equals(ipRegion.getDstIp())) {
			throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置srcIp和dstIp不能相同",
					RestBusinessCode.SrcIpEQDstIp.getValue());
		}
		if (ipRegion.getDirection() != 1 && ipRegion.getDirection() != 0) {
			throw new RestServiceException("ipRegionList中regionId为" + ipRegion.getRegionId()
					+ "的域配置direction的值不正确,只能是0或1",
					RestBusinessCode.DirectionIsWrongRange.getValue());
		}

		// zdx protocol验证？
		// if (ipRegion.getTableName().toLowerCase().equals("dj_ip_port")) {
		// if (ipRegion.getProtocol() < 0 || ipRegion.getProtocol() > 255) {
		// return "service为" + service + "的配置中"+listName+"中regionId为" +
		// ipRegion.getRegionId()
		// + "的域配置tableName为dj_ip_port时,protocol的取值范围只能是0-255";
		// }
		// } else {
		// if (ipRegion.getProtocol() != 0) {
		// return "service为" + service + "的配置中"+listName+"中regionId为" +
		// ipRegion.getRegionId() + "的域配置tableName为"
		// + ipRegion.getTableName() + "时,protocol的值只能是0";
		// }
		//
		// }
	}

	public static void serviceNumRegionVal(NumRegion numRegion)
			throws Exception {
		Long lowBoundary = numRegion.getLowBoundary();
		Long upBoundary = numRegion.getUpBoundary();
		if (lowBoundary <= upBoundary) {
			if (lowBoundary > 4294967295l || lowBoundary < 0l) {
				throw new RestServiceException("numRegionList中regionId为"
						+ numRegion.getRegionId()
						+ "的域配置lowBoundary的值不能大于2的32次方减一(4294967295)或者小于0",
						RestBusinessCode.LowBoundaryIsWrongRange.getValue());
			}
			if (upBoundary > 4294967295l || upBoundary < 0l) {
				throw new RestServiceException("numRegionList中regionId为"
						+ numRegion.getRegionId()
						+ "的域配置upBoundary的值不能大于2的32次方减一(4294967295)或者小于0",
						RestBusinessCode.UpBoundaryIsWrongRange.getValue());
			}
		} else {
			throw new RestServiceException("numRegionList中regionId为" + numRegion.getRegionId()
					+ "的域配置lowBoundary的值大于upBoundary,应该是小于或等于",
					RestBusinessCode.LowBoundaryGTUpBoundary.getValue());
		}
	}

	public static void serviceStrRegionVal(StrRegion strRegion) throws Exception {

		Integer exprType = strRegion.getExprType();
		Integer matchMethod = strRegion.getMatchMethod();
		Integer isHexbin = strRegion.getIsHexbin();
		if (exprType != 0 && exprType != 1) {
			throw new RestServiceException("strRegionList中regionId为" + strRegion.getRegionId()
					+ "的域配置exprType的值只能是0(无表达式)或者1(与表达式)",
					RestBusinessCode.ExprTypeIsWrongRange.getValue());
		}
		if (matchMethod != 0 && matchMethod != 1 && matchMethod != 2
				&& matchMethod != 3) {
			throw new RestServiceException("strRegionList中regionId为" + strRegion.getRegionId()
					+ "的域配置matchMethod的值只能是0(子串匹配),1(右匹配),2(左匹配),3(完全匹配)",
					RestBusinessCode.MatchMethodIsWrongRange.getValue());
		}

		if (isHexbin != 0 && isHexbin != 1 && isHexbin != 2) {
			throw new RestServiceException("strRegionList中regionId为"
							+ strRegion.getRegionId()
							+ "的域配置isHexbin的值只能是0(大小写不敏感，且非HEX)或者1(HEX)或者2(大小写敏感，且非HEX)",
					RestBusinessCode.IsHexbinIsWrongRange.getValue());
		}
		// strRegProhibitConfigWord(strRegion,service);

	}

}
