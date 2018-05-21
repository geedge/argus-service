package com.nis.web.controller.restful;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigCompileTest;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.ConfigSourceTest;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.IpRegionTest;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.NumRegionTest;
import com.nis.domain.restful.StrRegion;
import com.nis.domain.restful.StrRegionTest;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.JsonDateValueProcessor;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.ConfigSourcesService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@RestController
@RequestMapping("${servicePath}/cfg/v1/test")
@Api(value = "SaveConfigTestController", description = "配置存储服务，包括管控、监测、白名单的控制类")
public class SaveConfigTestController extends BaseRestController {
	private long allCount = 0l;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger(ConfigTestController.class);
	@Autowired
	protected ConfigSourcesService configSourcesService;

	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "/testSaveByJDBCThread", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量业务配置存储", httpMethod = "GET", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）存储")
	public Map testSaveByJDBCThread(@RequestParam(value = "configCount", required = false) Long configCount,
			@RequestParam(value = "startNum", required = false) Long startNum,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, HttpServletRequest request,
			HttpServletResponse response) {
		try {

			startTime = "2016-11-01 08:20:20";
			endTime = "2016-11-8 15:55:33";

			if (configCount == null) {
				configCount = 0l;
			}
			allCount = 0l;
			long dataCount = 0l;

			if (startTime == null || startTime.equals("")) {
				startTime = sdf.format(new Date());
			}

			if (endTime == null || endTime.equals("")) {
				endTime = sdf.format(new Date());
			}
			Date date = new Date();
			ConfigSourceTest configSource = new ConfigSourceTest();
			configSource.setOpTime(date);
			configSource.setOpAction(1);
			configSource.setOperator("rkg");
			configSource.setVersion("0.1");
			List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
			if (startNum == null || startNum == 0) {
				startNum = 1l;
			}
			long count = System.currentTimeMillis() + startNum;
			Integer[] zeroArr = { 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
			Integer[] oneArr = { 15, 16, 17, 18, 32, 33, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62 };
			Integer[] twoArr = { 2 };
			for (Integer x = 0; x < zeroArr.length; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					allCount++;
					ConfigCompile configCompile = setCompileTest(zeroArr[x].longValue(), 0, date, startTime, endTime,
							count);
					configCompileList.add(configCompile);
				}
			}
			for (Integer x = 15; x < oneArr.length + 15; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					allCount++;
					ConfigCompile configCompile = setCompileTest(oneArr[x - 15].longValue(), 1, date, startTime,
							endTime, count);
					configCompileList.add(configCompile);
				}
			}
			for (Integer x = 50; x < twoArr.length + 50; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					allCount++;
					ConfigCompile configCompile = setCompileTest(twoArr[x - 50].longValue(), 2, date, startTime,
							endTime, count);
					configCompileList.add(configCompile);
				}
			}
			configSource.setConfigCompileList(configCompileList);
			SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST,
					request, configSource);

			// JsonConfig jsonConfig = new JsonConfig();
			// jsonConfig.registerJsonValueProcessor(Date.class, new
			// JsonDateValueProcessor());
			// JSONArray jsonArray = JSONArray.fromObject(configSource,
			// jsonConfig);
			// String jsonStr = jsonArray.getString(0);
			// String distisct = "\"district\":\"\",";
			// if (jsonStr.contains(distisct)) {
			// jsonStr = jsonStr.replaceAll(distisct, "");
			// }
			// System.out.println(jsonStr);
			System.out.println("开始执行插入操作" + dataCount + "条配置总计" + allCount + "条数据");
			long start = System.currentTimeMillis();
			StringBuffer sb = new StringBuffer();
			configSourcesService.saveByJDBCThread(thread, start, configCompileList, sb);
			Long end = System.currentTimeMillis();
			Long time = (end - start) / 1000;
			System.out.println("插入了" + dataCount + "条配置总计" + allCount + "条数据总共需要" + time + "秒");
			validateConfigSourceTest(thread, start, configSource);
			return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "编译配置数据插入成功",
					configSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/testSaveByMybatis", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量业务配置存储", httpMethod = "GET", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）存储")
	public Map testSaveByMybatis(@RequestParam(value = "configCount", required = false) Long configCount,
			@RequestParam(value = "batchSize", required = false) Integer batchSize,
			@RequestParam(value = "startNum", required = false) Long startNum, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (startNum == null || startNum == 0) {
				startNum = 1l;
			}
			allCount = 0l;
			long dataCount = 0l;
			String startTime = "2016-11-01 08:20:20";
			String endTime = "2016-11-8 15:55:33";
			Date date = new Date();
			ConfigSourceTest configSource = new ConfigSourceTest();
			configSource.setOpTime(date);
			configSource.setOpAction(1);
			configSource.setOperator("rkg");
			configSource.setVersion("0.1");
			List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
			long count = System.currentTimeMillis() + startNum;
			Integer[] zeroArr = { 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
			Integer[] oneArr = { 15, 16, 17, 18, 32, 33, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62 };
			Integer[] twoArr = { 2 };
			for (Integer x = 0; x < zeroArr.length; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					allCount++;
					ConfigCompile configCompile = setCompileTest(zeroArr[x].longValue(), 0, date, startTime, endTime,
							count);
					configCompileList.add(configCompile);
				}
			}
			for (Integer x = 15; x < oneArr.length + 15; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					allCount++;
					ConfigCompile configCompile = setCompileTest(oneArr[x - 15].longValue(), 1, date, startTime,
							endTime, count);
					configCompileList.add(configCompile);
				}
			}
			for (Integer x = 50; x < twoArr.length + 50; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					allCount++;
					ConfigCompile configCompile = setCompileTest(twoArr[x - 50].longValue(), 2, date, startTime,
							endTime, count);
					configCompileList.add(configCompile);
				}
			}
			configSource.setConfigCompileList(configCompileList);
			SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST,
					request, configSource);

			System.out.println("开始执行插入操作" + dataCount + "条配置总计" + allCount + "条数据");
			long start = System.currentTimeMillis();
			StringBuffer sb = new StringBuffer();
			configSourcesService.insertConfigSourceData(thread, start, configCompileList, sb);
			Long end = System.currentTimeMillis();
			Long time = (end - start) / 1000;
			System.out.println("插入了" + dataCount + "条配置总计" + allCount + "条数据总共需要" + time + "秒");
			validateConfigSourceTest(thread, start, configSource);
			return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "编译配置数据插入成功",
					configSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ConfigCompileTest setCompileTest(Long serviceType, Integer action, Date date, String startTime,
			String endTime, Long index) throws Exception {

		Long compileId = System.currentTimeMillis() + index;
		//System.out.println("compileId--------------------------" + compileId);
		ConfigCompileTest configCompile = new ConfigCompileTest();
		configCompile.setCompileId(compileId);
		configCompile.setService(serviceType);
		configCompile.setAction(1);
		configCompile.setContType(0);
		configCompile.setAttrType(0);
		configCompile.setContLabel("0");
		configCompile.setTaskId(0);
		configCompile.setGuaranteeId(0);
		configCompile.setAffAirId(0);
		configCompile.setTopIcId(0);
		configCompile.setDoBlackList(1l);
		configCompile.setDoLog(0);
		configCompile.setEffectiveRange("0");
		configCompile.setConfigPercent(100l);
		configCompile.setConfigOption(1);
		configCompile.setStartTime(new Date(1482472334731l));
		configCompile.setEndTime(new Date(1482472734731l));
		//configCompile.setStartTime(sdf.parse("2016-12-22 08:20:20"));
		//configCompile.setEndTime(sdf.parse("2016-12-23 08:20:20"));
		// configCompile.setStartTime(startTime);
		// configCompile.setEndTime(endTime);

		configCompile.setActiveSys(2);
		configCompile.setLastUpdate(date);
		configCompile.setProcSeq(compileId);
		configCompile.setUserRegion("0");
		configCompile.setIsValid(1);
		configCompile.setGroupNum(1);
		configCompile.setFatherCfgId(0l);
		configCompile.setOpTime(date);
		Long groupId = System.currentTimeMillis() + (long) ((Math.random()) * 100000);
		configCompile.setGroupRelationList(setGroupList(compileId, date, groupId));
		Map<String, String> tableMap = configSourcesService.getTableMap(serviceType.intValue());
		List<String> strTable = new ArrayList<String>();
		String ipTable = null;
		String numTable = null;

		if (null != tableMap && tableMap.size() > 0) {
			for (String str : tableMap.keySet()) {
				if (tableMap.get(str).equals("ip")) {
					ipTable = str;
				} else if (tableMap.get(str).equals("num")) {
					numTable = str;
				} else if (tableMap.get(str).equals("str")) {
					strTable.add(str);
				}
			}
		}

		configCompile.setStrRegionList(setAllStrRegionList(serviceType, groupId, date, strTable, index));
		configCompile.setIpRegionList(setIpRegionList(serviceType, groupId, date, ipTable, index));
		configCompile.setNumRegionList(setNumRegionList(serviceType, groupId, date, numTable, index));
		return configCompile;

	}

	public List<ConfigGroupRelation> setGroupList(Long compileId, Date date, Long groupId) {
		List<ConfigGroupRelation> groupRelationList = new ArrayList<ConfigGroupRelation>();
		ConfigGroupRelation configGroupRelation = new ConfigGroupRelation();
		configGroupRelation.setGroupId(groupId);
		configGroupRelation.setId(groupId);
		configGroupRelation.setCompileId(compileId);
		configGroupRelation.setIsValid(1);
		configGroupRelation.setOpTime(date);
		groupRelationList.add(configGroupRelation);
		allCount++;
		return groupRelationList;
	}

	public List<StrRegion> setStrRegionList(Long service, Long groupId, Date date, List<String> tableName, Long index) {
		List<StrRegion> strRegionList = new ArrayList<StrRegion>();
		if (tableName != null & tableName.size() > 0) {
			Long regionId = System.currentTimeMillis() + index + 10000000000000l;
			//System.out.println("Strid--------------------------" + regionId);
			StrRegionTest strRegion = new StrRegionTest();
			strRegion.setRegionId(regionId);
			strRegion.setGroupId(groupId);
			strRegion.setKeywords("百度");
			strRegion.setExprType(0);
			strRegion.setMatchMethod(0);
			strRegion.setIsHexbin(0);
			strRegion.setIsValid(1);
			strRegion.setOpTime(date);
			if (configSourcesService.isStrStrongRegion(tableName.get(0))) {
				strRegion.setDistrict("南京");
			}
			strRegion.setTableName(tableName.get(0));
			strRegion.setProcSeq(regionId);
			strRegionList.add(strRegion);

			StrRegionTest strRegion1 = new StrRegionTest();
			Long regionId1 = System.currentTimeMillis() + index + 100000;
			//System.out.println("Strid===================================" + regionId);
			strRegion1.setRegionId(regionId1);
			strRegion1.setGroupId(groupId);
			strRegion1.setKeywords("新浪");
			strRegion1.setExprType(0);
			strRegion1.setMatchMethod(0);
			strRegion1.setIsHexbin(0);
			strRegion1.setIsValid(1);
			strRegion1.setOpTime(date);
			strRegion1.setProcSeq(regionId1);
			if (tableName.size() > 1) {
				strRegion1.setTableName(tableName.get(1));
				if (configSourcesService.isStrStrongRegion(tableName.get(1))) {
					strRegion1.setDistrict("北京");
				}
			} else {
				strRegion1.setTableName(tableName.get(0));
				if (configSourcesService.isStrStrongRegion(tableName.get(0))) {
					strRegion1.setDistrict("上海");
				}
			}
			strRegionList.add(strRegion1);
			allCount++;
			return strRegionList;
		} else {
			return strRegionList;
		}

	}

	public List<StrRegion> setAllStrRegionList(Long service, Long groupId, Date date, List<String> tableName,
			Long index) {
		List<StrRegion> strRegionList = new ArrayList<StrRegion>();
		int num = 0;
		if (tableName != null & tableName.size() > 0) {
			for (String tbName : tableName) {
				num++;
				Long regionId = System.currentTimeMillis() + index + 999999999999999l * num;
				//System.out.println("Strid--------------------------" + regionId);
				StrRegionTest strRegion = new StrRegionTest();
				strRegion.setRegionId(regionId);
				strRegion.setGroupId(groupId);
				strRegion.setKeywords("百度");
				strRegion.setExprType(0);
				strRegion.setMatchMethod(0);
				strRegion.setIsHexbin(2);
				strRegion.setIsValid(1);
				strRegion.setOpTime(date);
				if (configSourcesService.isStrStrongRegion(tbName)) {
					strRegion.setDistrict("南京");
				}
				strRegion.setTableName(tbName);
				strRegion.setProcSeq(regionId);
				strRegionList.add(strRegion);
				allCount++;
			}
			return strRegionList;
		} else {
			return strRegionList;
		}
	}

	public List<IpRegion> setIpRegionList(Long service, Long groupId, Date date, String tableName, Long index) {
		List<IpRegion> ipRegionList = new ArrayList<IpRegion>();
		if (tableName != null) {

			IpRegionTest ipRegion = new IpRegionTest();
			Long regionId = System.currentTimeMillis() + index + 20000000000000l;
			//System.out.println("Ipid--------------------------" + regionId);
			ipRegion.setRegionId(regionId);
			ipRegion.setGroupId(groupId);
			ipRegion.setAddrType(4);
			ipRegion.setSrcIp("0.0.0.0");
			ipRegion.setMaskSrcIp("0.0.0.0");
			ipRegion.setSrcPort("0");
			ipRegion.setMaskSrcPort("0");
			ipRegion.setDstIp("0.0.0.2");
			ipRegion.setMaskDstIp("0.0.0.0");
			ipRegion.setDstPort("0");
			ipRegion.setMaskDstPort("0");
			ipRegion.setProtocol(0);
			ipRegion.setDirection(0);
			ipRegion.setIsValid(1);
			ipRegion.setOpTime(date);
			ipRegion.setTableName(tableName);
			ipRegion.setProcSeq(regionId);
			ipRegionList.add(ipRegion);
			allCount++;
			return ipRegionList;
		} else {
			return ipRegionList;
		}
	}

	public List<NumRegion> setNumRegionList(Long service, Long groupId, Date date, String tableName, Long index) {
		List<NumRegion> numRegionList = new ArrayList<NumRegion>();
		if (tableName != null) {
			NumRegionTest numRegion = new NumRegionTest();
			Long regionId = System.currentTimeMillis() + index + 30000000000000l;
			//System.out.println("numid--------------------------" + regionId);
			numRegion.setRegionId(regionId);
			numRegion.setGroupId(groupId);
			numRegion.setLowBoundary(4294967294l);
			numRegion.setUpBoundary(4294967295l);
			numRegion.setIsValid(1);
			numRegion.setOpTime(date);
			numRegion.setTableName(tableName);
			numRegion.setProcSeq(regionId);
			numRegionList.add(numRegion);
			allCount++;
			return numRegionList;
		} else {
			return numRegionList;
		}
	}

	private void validateConfigSourceTest(SaveRequestLogThread thread, long start, ConfigSourceTest configSource) {
		String errorInfo = "";
		List<ConfigCompile> configCompileList = configSource.getConfigCompileList();
		if (StringUtils.isEmpty(configSource.getOperator())) {
			errorInfo = "get operator is Empty";
		}
		if (StringUtils.isEmpty(configSource.getVersion())) {
			errorInfo = "get version is Empty";
		}

		if (!isBlank(configSource.getOpTime())) {
			errorInfo = "get OpTime is Empty";
		}

		if (configCompileList.size() <= 0) {
			errorInfo = "编译配置不能为空";
		}
		if (!errorInfo.equals("")) {
			throw new RestServiceException(thread, System.currentTimeMillis() - start, errorInfo,
					RestBusinessCode.missing_args.getValue());
		}

	}

	private boolean isBlank(Date datetime) {
		if (null != datetime) {
			return true;
		}
		return false;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value = "/testUpdateByJDBCThread", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量业务配置修改", httpMethod = "GET", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）存储")
	public Map testUpdateByJDBCThread(@RequestParam(value = "compileId", required = false) Long compileId,
			HttpServletRequest request, HttpServletResponse response) {
		allCount = 0l;
		long dataCount = 0l;
		List<ConfigCompile> compileList = new ArrayList<ConfigCompile>();
		ConfigCompileTest configCompileTest = new ConfigCompileTest();
		configCompileTest.setIsValid(1);
		if (null != compileId) {
			configCompileTest.setCompileId(compileId);
		}
		List<ConfigCompileTest> queryAllCompile = configSourcesService.queryAllCompile(configCompileTest);
		for (int i = 0; i < queryAllCompile.size(); i++) {
			ConfigCompile compileInfo = getCompileInfo(queryAllCompile.get(i));
			compileInfo.setOpTime(new Date());
			dataCount++;
			allCount++;
			// compileInfo.setIsValid(0);
			compileList.add(compileInfo);
		}
		ConfigSourceTest configSource = new ConfigSourceTest();
		configSource.setOpTime(new Date());
		configSource.setOpAction(1);
		configSource.setOperator("rkg");
		configSource.setVersion("0.1");
		configSource.setConfigCompileList(compileList);

		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request,
				configSource);
		long start = System.currentTimeMillis();
		try {
			System.out.println("开始执行修改操作" + dataCount + "条配置总计" + allCount + "条数据");
			StringBuffer sb = new StringBuffer();
			configSourcesService.updateByJDBCThread(thread, start, configSource.getConfigCompileList(),
					configSource.getOpTime(), sb);

			Long end = System.currentTimeMillis();
			Long time = (end - start) / 1000;
			System.out.println("修改了" + dataCount + "条配置总计" + allCount + "条数据总共需要" + time + "秒");
		} catch (Exception e) {
			logger.error(e);
			if (!e.getMessage().contains("Connection is closed!")) {
				RestServiceException rse = (RestServiceException) e;

				throw new RestServiceException(thread, System.currentTimeMillis() - start, rse.getMessage(),
						rse.getErrorCode());
			} else {
				throw new RestServiceException(thread, System.currentTimeMillis() - start, "数据保存发生异常！",
						RestBusinessCode.unknow_error.getValue());
			}

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "编译配置数据修改成功",
				configSource);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value = "/updateConfigSource", method = RequestMethod.GET)
	@ApiOperation(value = "测试批量业务配置修改", httpMethod = "GET", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）存储")
	public Map updateConfigSource(@RequestParam(value = "compileId", required = false) Long compileId,
			HttpServletRequest request, HttpServletResponse response) {
		allCount = 0l;
		long dataCount = 0l;
		List<ConfigCompile> compileList = new ArrayList<ConfigCompile>();
		ConfigCompileTest configCompileTest = new ConfigCompileTest();
		configCompileTest.setIsValid(1);
		if (null != compileId) {
			configCompileTest.setCompileId(compileId);
		}

		List<ConfigCompileTest> queryAllCompile = configSourcesService.queryAllCompile(configCompileTest);
		for (int i = 0; i < queryAllCompile.size(); i++) {
			ConfigCompile compileInfo = getCompileInfo(queryAllCompile.get(i));
			// compileInfo.setIsValid(0);
			compileInfo.setOpTime(new Date());
			compileList.add(compileInfo);
			dataCount++;
			allCount++;
		}
		ConfigSourceTest configSource = new ConfigSourceTest();
		configSource.setOpTime(new Date());
		configSource.setOpAction(1);
		configSource.setOperator("rkg");
		configSource.setVersion("0.1");
		configSource.setConfigCompileList(compileList);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		JSONArray jsonArray = JSONArray.fromObject(configSource, jsonConfig);
		String jsonStr = jsonArray.getString(0);
		String distisct = "\"district\":\"\",";
		if (jsonStr.contains(distisct)) {
			jsonStr = jsonStr.replaceAll(distisct, "");
		}

		// System.out.println(jsonStr);

		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request,
				configSource);
		long start = System.currentTimeMillis();
		try {
			System.out.println("开始执行修改操作" + dataCount + "条配置总计" + allCount + "条数据");
			StringBuffer sb = new StringBuffer();
			configSourcesService.updateConfigSource(thread, start, configSource.getConfigCompileList(),
					configSource.getOpTime(), sb);
			Long end = System.currentTimeMillis();
			Long time = (end - start) / 1000;
			System.out.println("修改了" + dataCount + "条配置总计" + allCount + "条数据总共需要" + time + "秒");
		} catch (Exception e) {
			logger.error(e);
			if (!e.getMessage().contains("Connection is closed!")) {
				RestServiceException rse = (RestServiceException) e;

				throw new RestServiceException(thread, System.currentTimeMillis() - start, rse.getMessage(),
						rse.getErrorCode());
			} else {
				throw new RestServiceException(thread, System.currentTimeMillis() - start, "数据保存发生异常！",
						RestBusinessCode.unknow_error.getValue());
			}

		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "编译配置数据修改成功",
				configSource);
	}

	public ConfigCompile getCompileInfo(ConfigCompile configCompileTest) {
		List<ConfigGroupRelation> groupByCompileList = configSourcesService
				.getGroupByCompile(configCompileTest.getCompileId());
		configCompileTest.setGroupRelationList(groupByCompileList);
		Long[] groupIdArr = new Long[groupByCompileList.size()];
		for (int i = 0; i < groupByCompileList.size(); i++) {
			groupIdArr[i] = groupByCompileList.get(i).getGroupId();
			allCount++;
		}

		Map<String, String> tableMap = configSourcesService.getTableMap(configCompileTest.getService().intValue());
		List<String> strTable = new ArrayList<String>();
		String ipTable = null;
		String numTable = null;

		if (null != tableMap && tableMap.size() > 0) {
			for (String str : tableMap.keySet()) {
				if (tableMap.get(str).equals("ip")) {
					ipTable = str;
				} else if (tableMap.get(str).equals("num")) {
					numTable = str;
				} else if (tableMap.get(str).equals("str")) {
					strTable.add(str);
				}
			}
		}
		if (strTable.size() > 0) {
			// List<StrRegion> strList =new ArrayList<StrRegion>();
			List<StrRegion> strRegionList = configSourcesService.getStrRegionByGId(strTable, groupIdArr);
			if (null != strRegionList && strRegionList.size() > 0) {
				for (StrRegion strRegion : strRegionList) {
					// strList.add(strRegion)
					strRegion.setIsValid(0);
					allCount++;
				}
				configCompileTest.setStrRegionList(strRegionList);
			} else {
				List<StrRegion> strList = new ArrayList<StrRegion>();
				configCompileTest.setStrRegionList(strList);
			}
		} else {
			List<StrRegion> strList = new ArrayList<StrRegion>();
			configCompileTest.setStrRegionList(strList);
		}

		if (ipTable != null) {

			List<IpRegion> ipRegionByGId = configSourcesService.getIpRegionByGId(ipTable, groupIdArr);
			if (null != ipRegionByGId && ipRegionByGId.size() > 0) {
				for (IpRegion ipRegion : ipRegionByGId) {
					ipRegion.setIsValid(0);
					allCount++;
				}
				configCompileTest.setIpRegionList(ipRegionByGId);
			} else {
				List<IpRegion> ipList = new ArrayList<IpRegion>();
				configCompileTest.setIpRegionList(ipList);
			}
		} else {
			List<IpRegion> ipList = new ArrayList<IpRegion>();
			configCompileTest.setIpRegionList(ipList);
		}

		if (numTable != null) {
			List<NumRegion> numRegionList = configSourcesService.getNumRegionByGId(numTable, groupIdArr);
			if (null != numRegionList && numRegionList.size() > 0) {
				for (NumRegion numRegion : numRegionList) {
					numRegion.setIsValid(0);
					allCount++;
				}
				configCompileTest.setNumRegionList(numRegionList);
			} else {
				List<NumRegion> numList = new ArrayList<NumRegion>();
				configCompileTest.setNumRegionList(numList);
			}
		} else {
			List<NumRegion> numList = new ArrayList<NumRegion>();
			configCompileTest.setNumRegionList(numList);
		}
		return configCompileTest;
	}

	@RequestMapping(value = "/findCompile", method = RequestMethod.POST)
	public Map findCompile(ConfigCompileTest configCompileTest, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread1 = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				configCompileTest);
		Page page = new Page();
		int no = configCompileTest.getPagesNo() == null ? 1 : configCompileTest.getPagesNo();
		int size = configCompileTest.getPagesSize() == null ? 10 : configCompileTest.getPagesSize();
		page.setPageNo(no);
		page.setPageSize(size);
		Page<ConfigCompileTest> queryAllCompile = null;
		try {
			if (configCompileTest.getSearchStartTime() != null && !configCompileTest.getSearchStartTime().equals("")) {
				configCompileTest.setStartTime(sdf.parse(configCompileTest.getSearchStartTime()));
				// configCompile.setStartTime(configCompile.getSearchStartTime());
			}
			if (configCompileTest.getSearchEndTime() != null && !configCompileTest.getSearchEndTime().equals("")) {
				configCompileTest.setEndTime(sdf.parse(configCompileTest.getSearchEndTime()));

				// configCompile.setEndTime(configCompile.getSearchEndTime());
			}
			queryAllCompile = configSourcesService.queryAllCompile(page, configCompileTest);
		} catch (SQLException e) {
			thread1.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "编译配置获取失败");
		} catch (Exception e) {
			thread1.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "编译配置获取失败");
		}
		return serviceResponse(thread1, System.currentTimeMillis() - start, request, response, "编译配置获取成功",
				queryAllCompile);
	}
}
