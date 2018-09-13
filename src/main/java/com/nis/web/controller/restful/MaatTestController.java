package com.nis.web.controller.restful;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.CommonSourceFieldCfg;
import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DigestRegion;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.util.Configurations;
import com.nis.util.ExceptionUtil;
import com.nis.util.FileUtils;
import com.nis.util.JsonMapper;
import com.nis.util.ReadCommSourceXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.util.StringUtil;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.ConfigRedisService;
import com.nis.web.service.restful.ConfigSourcesService;
import com.nis.web.service.restful.MaatTestServiceimpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("${servicePath}/test")
@Api(value = "MaatTestController", description = "测试maat及unMaat配置入库,取消功能")
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
		FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "开始删除业务类型" + serviceType + "下的maat配置" + configId + "\n",
				Configurations.getStringProperty("maatTestLogPath", ""), true);

//		Map<Integer, Map<Integer, List<Long>>> restMap = new HashMap<Integer, Map<Integer, List<Long>>>();
//		Iterator<Integer> serviceIterator = compileMap.keySet().iterator();
//		while (serviceIterator.hasNext()) {
//			Integer service = Integer.valueOf(serviceIterator.next().toString());
//			List<Integer> dbIndexList = ServiceAndRDBIndexReal.getRedisDBByService(service);
//			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
//				for (Integer dbIndex : dbIndexList) {
//					if (restMap.containsKey(dbIndex)) {
//						restMap.get(dbIndex).put(service, compileMap.get(service));
//					} else {
//						Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
//						map.put(service, compileMap.get(service));
//						restMap.put(dbIndex, map);
//					}
//				}
//			} else {
//				FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "error:获取业务类型" + service + "对应的redisDb失败\n",
//						Configurations.getStringProperty("maatTestLogPath", ""), true);
//				return "error";
//			}
//		}
		try {
			if (!configRedisService.delMaatConfig(compileMap)) {
				FileUtils.addStrToFile(
						sdf.format(new Date()) + "\t" + "error:删除业务类型" + serviceType + "下的maat配置" + configId + "失败\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);
				return "error";
			} else {
				FileUtils.addStrToFile(
						sdf.format(new Date()) + "\t" + "删除业务类型" + serviceType + "下的maat配置" + configId + "成功\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);

			}
		} catch (Exception e) {
			String exceptionMsg = ExceptionUtil.getExceptionMsg(e);
			logger.error(exceptionMsg);
			FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "error:删除业务类型" + serviceType + "下的maat配置" + configId
					+ "失败,失败原因:" + exceptionMsg + "\n", Configurations.getStringProperty("maatTestLogPath", ""), true);

			return "error";
		}
		return "ok";
	}

	@RequestMapping(value = "/cfg/v1/testSaveMaat", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量保存maat配置", httpMethod = "GET", response = String.class, notes = "测试批量保存maat配置,service:需要保存的业务类型,service为-1表示下发所有maat类配置,saveCount:保存几条配置")
	@ApiParam(value = "测试批量保存maat配置", name = "testSaveMaat", required = true)
	public Map testSaveMaat(@RequestParam(required = true) Integer service,
			@RequestParam(required = true) Integer saveCount) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = new AuditLogThread();
		StringBuffer sb = new StringBuffer();
		List<Long> compileIdList = null;

		Map<Integer, String> map = new HashMap<Integer, String>();

		try {
			// 获取所有maat类的业务类型
			Map<Integer, Map<Integer, List<String>>> sercieNameMap = ServiceAndRDBIndexReal.getSercieNameMap();
			if (sercieNameMap != null) {
				if (service == -1) {
					Set<Integer> keySet = sercieNameMap.keySet();
					for (Integer ser : keySet) {
						compileIdList = new ArrayList<Long>();
						List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
						for (int i = 0; i < saveCount; i++) {
							configCompileList.add(getConfigCompile(ser));
						}
						String jsonString = JsonMapper.toJsonString(configCompileList);
						FileUtils.addStrToFile("下发的json串是"+jsonString+"\n",
								Configurations.getStringProperty("maatTestLogPath", ""), true);
						// 保存测试配置
						configSourcesService.saveMaatConfig(thread, start, configCompileList, sb);
						for (ConfigCompile configCompile : configCompileList) {
							compileIdList.add(configCompile.getCompileId());
						}
						FileUtils.addStrToFile(
								sdf.format(new Date()) + "\t" + "业务类型" + ser + "添加" + saveCount + "条maat配置成功,配置id是"
										+ compileIdList + "\n" + sdf.format(new Date()) + "\t开始验证添加的数据各字段是否正确\n",
								Configurations.getStringProperty("maatTestLogPath", ""), true);
						// 验证数据是否在正确
						maatTestServiceimpl.getMaatKeys(configCompileList);
						map.put(ser, "http://127.0.0.1:8080/galaxy/service/test/cfg/v1/testDelMaat?serviceType=" + ser
								+ "&configId=" + compileIdList.toString().replace("[", "").replace("]", ""));

						testDelMaat(compileIdList.toString().replace("[", "").replace("]", ""), ser);

					}
				} else {
					if (sercieNameMap.containsKey(service)) {
						compileIdList = new ArrayList<Long>();
						List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
						for (int i = 0; i < saveCount; i++) {
							configCompileList.add(getConfigCompile(service));
						}
						String jsonString = JsonMapper.toJsonString(configCompileList);
						FileUtils.addStrToFile("下发的json串是"+jsonString+"\n",
								Configurations.getStringProperty("maatTestLogPath", ""), true);
						// 保存测试配置
						configSourcesService.saveMaatConfig(thread, start, configCompileList, sb);
						for (ConfigCompile configCompile : configCompileList) {
							compileIdList.add(configCompile.getCompileId());
						}
						FileUtils.addStrToFile(
								sdf.format(new Date()) + "\t" + "业务类型" + service + "添加" + saveCount + "条maat配置成功,配置id是"
										+ compileIdList + "\n" + sdf.format(new Date()) + "\t开始验证添加的数据各字段是否正确\n",
								Configurations.getStringProperty("maatTestLogPath", ""), true);
						// 验证数据是否在正确
						maatTestServiceimpl.getMaatKeys(configCompileList);
						map.put(service, "http://127.0.0.1:8080/galaxy/service/test/cfg/v1/testDelMaat?serviceType="
								+ service + "&configId=" + compileIdList.toString().replace("[", "").replace("]", ""));
						if (!testDelMaat(compileIdList.toString().replace("[", "").replace("]", ""), service)
								.equals("ok")) {
							map.put(-4, "del config error");
						}
					} else {

						map.put(-2, "is not maat service");
					}
				}
			} else {
				map.put(-3, "service map is null");
			}

		} catch (Exception e) {
			String exceptionMsg = ExceptionUtil.getExceptionMsg(e);
			logger.error(exceptionMsg);
			FileUtils.addStrToFile(
					sdf.format(new Date()) + "\t" + "error:添加业务类型" + service + "下的maat配置失败,失败原因:" + exceptionMsg + "\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);
			map.put(-4, "error");
		}
		return map;
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

				List<IpRegion> ipList = new ArrayList<IpRegion>();
				List<NumRegion> numList = new ArrayList<NumRegion>();
				List<StrRegion> strList = new ArrayList<StrRegion>();
				List<DigestRegion> digestList = new ArrayList<DigestRegion>();
//				configCompile.setIpRegionList(ipList);
//				configCompile.setNumRegionList(numList);
//				configCompile.setStrRegionList(strList);
//				configCompile.setDigestRegionList(digestList);
//				configCompile.setIpClientRangeList(ipList);
				if (tableType == 12 || tableType == 18) {
					ipList = new ArrayList<IpRegion>();
					if (tableNameArr.length == 1) {
						ipList.add(getIpRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							ipList.add(getIpRegion(service, tableType, groupId, tableName));
						}
					}
					if (tableType == 12) {
						configCompile.setIpRegionList(ipList);
					} else if (tableType == 18) {
						configCompile.setIpClientRangeList(ipList);
					}

				} else if (tableType == 13) {
					numList = new ArrayList<NumRegion>();
					if (tableNameArr.length == 1) {
						numList.add(getNumRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							numList.add(getNumRegion(service, tableType, groupId, tableName));
						}
					}
					configCompile.setNumRegionList(numList);
				} else if (tableType == 14 || tableType == 15) {
					strList = new ArrayList<StrRegion>();

					if (tableNameArr.length == 1) {
						strList.add(getStrRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							strList.add(getStrRegion(service, tableType, groupId, tableName));
						}
					}
					configCompile.setStrRegionList(strList);
				} else if (tableType == 16) {
					digestList = new ArrayList<DigestRegion>();
					if (tableNameArr.length == 1) {
						digestList.add(getDigestRegion(service, tableType, groupId, null));
					} else {
						for (String tableName : tableNameArr) {
							digestList.add(getDigestRegion(service, tableType, groupId, tableName));
						}
					}
					configCompile.setDigestRegionList(digestList);
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
		if(type==15) {
			strRegion.setDistrict("增强字符串测试groupid"+groupId);
		}else {
			strRegion.setDistrict("");
		}
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

	@RequestMapping(value = "/cfg/v1/testSaveUnMaat", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量保存unmaat配置", httpMethod = "GET", response = Map.class, notes = "测试批量保存unmaat配置,service需要保存的业务类型,service为-1表示下发所有unmaat类配置,saveCount为每类配置下发的条数")
	@ApiParam(value = "测试批量保存unmaat配置", name = "测试批量删除unmaat配置", required = true)
	public Map testSaveUnMaat(@RequestParam(required = true) Integer service,
			@RequestParam(required = true) Integer saveCount, HttpServletRequest request,
			HttpServletResponse response) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		try {
			if (service == -1) {
				Map<Integer, String> unMaatSercieNameMap = ServiceAndRDBIndexReal.getUnMaatSercieNameMap();
				if (unMaatSercieNameMap != null && unMaatSercieNameMap.size() > 0) {
					Set<Integer> keySet = unMaatSercieNameMap.keySet();
					for (Integer ser : keySet) {
						getUnMaatStr(ser, saveCount, map);
					}
				}
			} else {
				getUnMaatStr(service, saveCount, map);
			}
		} catch (Exception e) {
			String exceptionMsg = ExceptionUtil.getExceptionMsg(e);
			logger.error(exceptionMsg);

			FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "error:添加业务类型" + service + "下的unMaat类配置失败,失败原因:"
					+ exceptionMsg + "\n", Configurations.getStringProperty("maatTestLogPath", ""), true);

			map.put(-4, "error");
		}
		return map;
	}

	@RequestMapping(value = "/cfg/v1/testDelUnMaat", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量删除unmaat配置", httpMethod = "GET", response = String.class, notes = "测试批量删除unmaat配置,configId为配置id,多个配置id用逗号分隔")
	@ApiParam(value = "测试批量删除unmaat配置", name = "testDelMaat", required = true)
	public String testDelUnMaat(@RequestParam(required = true) String configId,
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
		FileUtils.addStrToFile(
				sdf.format(new Date()) + "\t" + "开始删除业务类型" + serviceType + "下的unmaat类配置" + configId + "\n",
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
				FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "error:获取业务类型" + service + "对应的redisDb失败\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);
				return "error";
			}
		}
		try {
			if (!configRedisService.delUnMaatConfig(restMap, true)) {
				FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "error:删除业务类型" + serviceType + "下的unmaat类配置"
						+ configId + "失败\n", Configurations.getStringProperty("maatTestLogPath", ""), true);
				return "error";
			} else {
				FileUtils.addStrToFile(
						sdf.format(new Date()) + "\t" + "删除业务类型" + serviceType + "下的unmaat类配置" + configId + "成功\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);

			}
		} catch (Exception e) {
			String exceptionMsg = ExceptionUtil.getExceptionMsg(e);
			logger.error(exceptionMsg);
			FileUtils
					.addStrToFile(
							sdf.format(new Date()) + "\t" + "error:删除业务类型" + serviceType + "下的unMaat类配置" + configId
									+ "失败,失败原因:" + exceptionMsg + "\n",
							Configurations.getStringProperty("maatTestLogPath", ""), true);

			return "error";
		}
		return "ok";
	}

	/**
	 * 组装json串,然后下发配置,在验证配置是否正确,然后删除配置
	 * @param service
	 * @param saveCount
	 * @param map
	 */
	private void getUnMaatStr(Integer service, Integer saveCount, Map<Integer, String> map) {
		try {
			ArrayList<Long> compileIdList = new ArrayList<Long>();
			List<CommonSourceFieldCfg> commonSourceFieldCfgList = ReadCommSourceXmlUtil
					.getCommonSourceCfgByService(service.toString());
			if (commonSourceFieldCfgList != null && commonSourceFieldCfgList.size() > 0) {
				StringBuffer jsonString = new StringBuffer();
				jsonString.append("[");
				for (int i = 0; i < saveCount; i++) {
					jsonString.append("{");
					for (CommonSourceFieldCfg commonSourceFieldCfg : commonSourceFieldCfgList) {
						String fieldType = commonSourceFieldCfg.getFieldType();
						String srcName = commonSourceFieldCfg.getSrcName();
						jsonString.append("\"");
						jsonString.append(srcName);
						jsonString.append("\":");
						if (fieldType.equals("String")) {
							if (srcName.toLowerCase().equals("service")) {
								jsonString.append("\"" + service + "\"");
							} else if (srcName.toLowerCase().equals("dstfile")) {
								jsonString.append(
										"\"http://10.0.6.249/group1/M00/00/02/CgAG-VtziqWAIzafAAITEbfJ8Zw992.jpg\"");
							} else {
								jsonString.append("\"" + UUID.randomUUID() + "\"");
							}
						} else if (fieldType.equals("Number")) {
							if (srcName.toLowerCase().equals("addrtype")) {
								jsonString.append("4");
							} else if (srcName.toLowerCase().equals("action")) {
								jsonString.append(ServiceAndRDBIndexReal.getActionByService(service));
							} else if (srcName.toLowerCase().equals("protocol")) {
								jsonString.append("0");
							} else if (srcName.toLowerCase().equals("direction")) {
								jsonString.append("0");
							} else if (srcName.toLowerCase().equals("location")) {
								jsonString.append("1");
							} else if (srcName.toLowerCase().equals("isvalid")) {
								jsonString.append("1");
							} else if (srcName.toLowerCase().equals("cfgid")||srcName.toLowerCase().equals("keyringid")||srcName.toLowerCase().equals("fileid")) {
								Long incrId = configRedisService.getIncrId("seq_compileid");
								compileIdList.add(incrId);
								jsonString.append(incrId);
							} else {
								if (commonSourceFieldCfg.getDefaultVal() != null) {
									jsonString.append(commonSourceFieldCfg.getDefaultVal());
								} else {
									jsonString.append(System.currentTimeMillis());
								}
							}
						} else if (fieldType.equals("IP")) {
							if (srcName.toLowerCase().equals("srcip")) {
								jsonString.append("\"1.1.1.1\"");
							} else {
								jsonString.append("\"2.2.2.2\"");
							}
						} else if (fieldType.equals("Port")) {
							jsonString.append("\"80\"");
						} else if (fieldType.equals("Date")) {
							jsonString.append("\"" + sdf.format(new Date()) + "\"");
						}
						jsonString.append(",");
					}
					String str = jsonString.substring(0, jsonString.lastIndexOf(","));
					jsonString.setLength(0);
					jsonString.append(str);
					jsonString.append("},");
				}
				String jsonStr = jsonString.substring(0, jsonString.lastIndexOf(","));
				jsonStr += "]";
				map.put(service, "http://127.0.0.1:8080/galaxy/service/test/cfg/v1/testDelUnMaat?serviceType=" + service
						+ "&configId=" + compileIdList.toString().replace("[", "").replace("]", ""));
				configSourcesService.saveCommonSources(null, -1, jsonStr);
				FileUtils.addStrToFile(
						sdf.format(new Date()) + "\t" + "业务类型" + service + "添加" + saveCount + "条数据成功,配置id是"
								+ compileIdList + "\n" + sdf.format(new Date()) + "\t开始验证添加的数据各字段是否正确\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);

				// 验证数据是否在正确
				maatTestServiceimpl.getUnMaatKeys(jsonStr, service);
				// 删除配置
				if (!testDelUnMaat(compileIdList.toString().replace("[", "").replace("]", ""), service).equals("ok")) {
					map.put(-4, "del unmaat error");
				}
			}
		} catch (Exception e) {
			String exceptionMsg = ExceptionUtil.getExceptionMsg(e);
			logger.error(exceptionMsg);
			FileUtils.addStrToFile(sdf.format(new Date()) + "\t" + "error:添加业务类型" + service + "下的unMaat类配置失败,失败原因:"
					+ exceptionMsg + "\n", Configurations.getStringProperty("maatTestLogPath", ""), true);
			map.put(-4, "error");
		}

	}
}
