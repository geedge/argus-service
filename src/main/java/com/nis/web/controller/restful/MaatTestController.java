package com.nis.web.controller.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DigestRegion;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.util.Configurations;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.ConfigRedisService;
import com.nis.web.service.restful.ConfigSourcesService;
import com.nis.web.service.restful.MaatTestServiceimpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${servicePath}")
@Api(value = "ConfigSourcesController", description = "测试maat及非maat配置入库,取消功能")
public class MaatTestController {
	private static Logger logger = LoggerFactory.getLogger(MaatTestController.class);
	@Autowired
	MaatTestServiceimpl maatTestServiceimpl;
	@Autowired
	ConfigRedisService configRedisService;
	@Autowired
	ConfigSourcesService configSourcesService;
	@Autowired
	ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "/cfg/v1/testSaveMaat", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量保存maat配置", httpMethod = "GET", response = String.class, notes = "测试批量保存maat配置,service:需要保存的业务类型,saveCount:保存几条配置")
	@ApiParam(value = "测试批量保存maat配置", name = "testSaveMaat", required = true)
	public String testSaveMaat(@RequestParam(required = true) Integer service,
			@RequestParam(required = true) Integer saveCount) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = new SaveRequestLogThread();
		StringBuffer sb = new StringBuffer();
		List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
		for (int i = 0; i < saveCount; i++) {
			configCompileList.add(getConfigCompile(service));
		}
		// 保存测试配置
		configSourcesService.saveMaatConfig(thread, start, configCompileList, sb);
		List<Long> compileIdList = new ArrayList<Long>();
		for (ConfigCompile configCompile : configCompileList) {
			compileIdList.add(configCompile.getCompileId());
		}
		logger.warn("业务类型{}添加{}条数据成功,配置id是{}", service, saveCount, compileIdList);
		// 验证数据是否在正确
		maatTestServiceimpl.getKeys(configCompileList);
		return "ok";
	}

	private ConfigCompile getConfigCompile(Integer service) {
		ConfigCompile configCompile = new ConfigCompile();
		Long compileId = configRedisService.getIncrId("SEQ_COMPILEID");
		configCompile.setCompileId(compileId);
		configCompile.setService(service);
		configCompile.setAction(ServiceAndRDBIndexReal.getActionByService(service));
		configCompile.setContType("101");
		configCompile.setAttrType("101001");
		configCompile.setContLabel("10000001");
		configCompile.setTaskId(999);
		configCompile.setAffairId(111111111);
		configCompile.setDoBlacklist(1);
		configCompile.setDoLog(1);
		configCompile.setEffectiveRange("0");
		configCompile.setUserRegion(compileId + "");
		configCompile.setIsValid(1);
		configCompile.setGroupNum(1);
		configCompile.setFatherCfgId(0L);
		configCompile.setOpTime(new Date());

		configCompile.setStartTime(new Date());
		configCompile.setEndTime(new Date());

		List<ConfigGroupRelation> groupRelationList = new ArrayList<ConfigGroupRelation>();
		ConfigGroupRelation configGroupRelation = getConfigGroupRelation(compileId);
		Long groupId = configGroupRelation.getGroupId();
		groupRelationList.add(configGroupRelation);
		configCompile.setGroupRelationList(groupRelationList);

		String type = Configurations.getStringProperty(service + "", "");
		if (type != null && !type.trim().equals("")) {
			String[] typeArrs = type.split(";");
			for (String typeStr : typeArrs) {
				String[] typeArr = typeStr.split(":");
				int tableType = Integer.parseInt(typeArr[0]);
				String tableNameArr[] = typeArr[1].split(",");
				if (tableType == 12) {
					List<IpRegion> list = new ArrayList<IpRegion>();
					if (tableNameArr.length == 1) {
						list.add(getIpRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							list.add(getIpRegion(service, tableType, groupId, tableName));
						}
					}

					configCompile.setIpRegionList(list);
				} else if (tableType == 13) {
					List<NumRegion> list = new ArrayList<NumRegion>();
					if (tableNameArr.length == 1) {
						list.add(getNumRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							list.add(getNumRegion(service, tableType, groupId, tableName));
						}
					}
					configCompile.setNumRegionList(list);

				} else if (tableType == 14) {
					List<StrRegion> list = new ArrayList<StrRegion>();
					if (tableNameArr.length == 1) {
						list.add(getStrRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							list.add(getStrRegion(service, tableType, groupId, tableName));
						}
					}
					configCompile.setStrRegionList(list);
				} else if (tableType == 15) {

					List<StrRegion> list = new ArrayList<StrRegion>();
					if (tableNameArr.length == 1) {
						list.add(getStrRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							list.add(getStrRegion(service, tableType, groupId, tableName));
						}
					}
					configCompile.setStrRegionList(list);

				} else if (tableType == 16) {
					List<DigestRegion> list = new ArrayList<DigestRegion>();

					if (tableNameArr.length == 1) {
						list.add(getDigestRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							list.add(getDigestRegion(service, tableType, groupId, tableName));
						}
					}

					configCompile.setDigestRegionList(list);
				} else if (tableType == 18) {
					List<IpRegion> list = new ArrayList<IpRegion>();
					if (tableNameArr.length == 1) {
						list.add(getIpRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							list.add(getIpRegion(service, tableType, groupId, tableName));
						}
					}
					configCompile.setIpClientRangeList(list);
				}
			}
		}

		if (configCompile.getIpRegionList() == null) {
			List<IpRegion> list = new ArrayList<IpRegion>();
			configCompile.setIpRegionList(list);
		}
		if (configCompile.getNumRegionList() == null) {
			List<NumRegion> list = new ArrayList<NumRegion>();
			configCompile.setNumRegionList(list);
		}
		if (configCompile.getStrRegionList() == null) {
			List<StrRegion> list = new ArrayList<StrRegion>();
			configCompile.setStrRegionList(list);
		}
		if (configCompile.getDigestRegionList() == null) {
			List<DigestRegion> list = new ArrayList<DigestRegion>();
			configCompile.setDigestRegionList(list);
		}
		if (configCompile.getIpClientRangeList() == null) {
			List<IpRegion> list = new ArrayList<IpRegion>();
			configCompile.setIpClientRangeList(list);
		}

		return configCompile;

	}

	private ConfigGroupRelation getConfigGroupRelation(long compileId) {
		ConfigGroupRelation group = new ConfigGroupRelation();
		group.setCompileId(compileId);
		group.setGroupId(configRedisService.getIncrId("SEQ_GROUPID"));
		group.setIsValid(1);
		group.setOpTime(new Date());

		return group;
	}

	private DigestRegion getDigestRegion(Integer service, int type, long groupId, String tableName) {
		DigestRegion digestRegion = new DigestRegion();
		digestRegion.setGroupId(groupId);
		digestRegion.setRegionId(configRedisService.getIncrId("SEQ_REGIONID"));
		digestRegion.setTableName(ServiceAndRDBIndexReal.getMaatTableName(service, type, tableName));
		digestRegion.setRawLen(1L);
		digestRegion.setDigest("2");
		digestRegion.setCfdsLevel(2);
		digestRegion.setIsValid(1);
		digestRegion.setOpTime(new Date());
		return digestRegion;
	}

	private NumRegion getNumRegion(Integer service, int type, long groupId, String tableName) {
		NumRegion numRegion = new NumRegion();
		numRegion.setGroupId(groupId);
		numRegion.setRegionId(configRedisService.getIncrId("SEQ_REGIONID"));
		numRegion.setTableName(ServiceAndRDBIndexReal.getMaatTableName(service, type, tableName));
		numRegion.setLowBoundary(1L);
		numRegion.setUpBoundary(2L);
		numRegion.setIsValid(1);
		numRegion.setOpTime(new Date());
		return numRegion;
	}

	private StrRegion getStrRegion(Integer service, int type, long groupId, String tableName) {
		StrRegion strRegion = new StrRegion();
		strRegion.setGroupId(groupId);
		strRegion.setRegionId(configRedisService.getIncrId("SEQ_REGIONID"));
		strRegion.setTableName(ServiceAndRDBIndexReal.getMaatTableName(service, type, tableName));
		strRegion.setDistrict("");
		strRegion.setKeywords("keywords,UUID:" + UUID.randomUUID());
		strRegion.setExprType(1);
		strRegion.setMatchMethod(1);
		strRegion.setIsHexbin(1);
		strRegion.setIsValid(1);
		strRegion.setOpTime(new Date());
		return strRegion;
	}

	private IpRegion getIpRegion(Integer service, int type, long groupId, String tableName) {
		IpRegion ipRegion = new IpRegion();
		ipRegion.setGroupId(groupId);
		ipRegion.setRegionId(configRedisService.getIncrId("SEQ_REGIONID"));
		ipRegion.setTableName(ServiceAndRDBIndexReal.getMaatTableName(service, type, tableName));
		ipRegion.setAddrType(4);
		ipRegion.setSrcIp("1.1.1.2");
		ipRegion.setMaskSrcIp("255.255.255.0");
		ipRegion.setSrcPort("1");
		ipRegion.setMaskSrcPort("1");
		ipRegion.setDstIp("2.2.2.2");
		ipRegion.setMaskDstIp("255.255.255.1");
		ipRegion.setDstPort("2");
		ipRegion.setMaskDstPort("2");
		ipRegion.setProtocol(0);
		ipRegion.setDirection(0);
		ipRegion.setIsValid(1);
		ipRegion.setOpTime(new Date());
		return ipRegion;

	}

}
