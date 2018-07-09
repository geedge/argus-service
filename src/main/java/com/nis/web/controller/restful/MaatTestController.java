package com.nis.web.controller.restful;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.nis.util.ExceptionUtil;
import com.nis.util.FileUtils;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.util.StringUtil;
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
	// private static Logger logger =
	// LoggerFactory.getLogger(MaatTestController.class);
	@Autowired
	MaatTestServiceimpl maatTestServiceimpl;
	@Autowired
	ConfigRedisService configRedisService;
	@Autowired
	ConfigSourcesService configSourcesService;
	@Autowired
	ServicesRequestLogService servicesRequestLogService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping(value = "/cfg/v1/testDelMaat", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量删除maat配置", httpMethod = "GET", response = String.class, notes = "测试批量删除maat配置,configId为配置id,多个配置id用逗号分隔")
	@ApiParam(value = "测试批量删除maat配置", name = "testDelMaat", required = true)
	public String testDelMaat(@RequestParam(required = true) String configId,
			@RequestParam(required = true) Integer serviceType) {
		String[] configArr = null;
		if (configId.contains(",")) {
			configArr = configId.split(",");
		} else {
			configArr = new String[1];
			configArr[0] = configId;
		}
		Map<Integer, List<Long>> compileMap = new HashMap<Integer, List<Long>>();
		for (String id : configArr) {
			if (compileMap.containsKey(serviceType)) {
				compileMap.get(serviceType).add(Long.valueOf(id.trim()));
			} else {
				List<Long> idList = new ArrayList<Long>();
				idList.add(Long.valueOf(id.trim()));
				compileMap.put(serviceType, idList);
			}
		}
		FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "开始删除业务类型" + serviceType + "下的配置" + configId + "\n",
				Configurations.getStringProperty("maatTestLogPath", ""), true);

		Map<Integer, Map<Integer, List<Long>>> restMap = new HashMap<Integer, Map<Integer, List<Long>>>();
		Iterator<Integer> serviceIterator = compileMap.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					if (restMap.containsKey(dbIndex)) {
						restMap.get(dbIndex).put(service, compileMap.get(service));
					} else {
						Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
						map.put(service, compileMap.get(service));
						restMap.put(dbIndex, map);
					}
				}
			} else {
				FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "获取业务类型" + service + "对应的redisDb失败\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);
				return "error";
			}
		}
		try {
			if (!configRedisService.delMaatConfig(restMap)) {
				FileUtils.addStrToFile(
						sdf.format(new Date()) + "\t" + "删除业务类型" + serviceType + "下的配置" + configId + "失败\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);
				return "error";
			} else {
				FileUtils.addStrToFile(
						sdf.format(new Date()) + "\t" + "删除业务类型" + serviceType + "下的配置" + configId + "成功\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);

			}
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addStrToFile(
					sdf.format(new Date()) + "\t" + "删除业务类型" + serviceType + "下的配置" + configId + "失败,失败原因:"
							+ ExceptionUtil.getExceptionMsg(e) + "\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);

			return "error";
		}
		return "ok";
	}

	@RequestMapping(value = "/cfg/v1/testSaveMaatMore", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量保存maat配置", httpMethod = "GET", response = String.class, notes = "测试批量保存maat配置,service:需要保存的业务类型,saveCount:保存几条配置")
	@ApiParam(value = "测试批量保存maat配置", name = "testSaveMaat", required = true)
	public String testSaveMaatMore(@RequestParam(required = true) Integer service,
			@RequestParam(required = true) Integer saveCount, Integer forCount) {
		List<String> list = new ArrayList<String>();
		for (int a = 0; a < forCount; a++) {
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
			FileUtils.addStrToFile(
					sdf.format(new Date()) + "\t" + "业务类型" + service + "添加" + saveCount + "条数据成功,配置id是" + compileIdList
							+ "\n" + sdf.format(new Date()) + "\t开始验证添加的数据各字段是否正确\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);
			// 验证数据是否在正确
			maatTestServiceimpl.getKeys(configCompileList);
			list.add(testDelMaat(compileIdList.toString().replace("[", "").replace("]", ""), service));
		}
		return list.toString();
		// return "http://127.0.0.1:8080/galaxy/service/cfg/v1/testDelMaat?serviceType="
		// + service + "&configId="
		// + compileIdList.toString().replace("[", "").replace("]", "");
	}

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
		FileUtils.addStrToFile(
				sdf.format(new Date()) + "\t" + "业务类型" + service + "添加" + saveCount + "条数据成功,配置id是" + compileIdList
						+ "\n" + sdf.format(new Date()) + "\t开始验证添加的数据各字段是否正确\n",
				Configurations.getStringProperty("maatTestLogPath", ""), true);
		// 验证数据是否在正确
		maatTestServiceimpl.getKeys(configCompileList);
		return "http://127.0.0.1:8080/galaxy/service/cfg/v1/testDelMaat?serviceType=" + service + "&configId="
				+ compileIdList.toString().replace("[", "").replace("]", "");
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
