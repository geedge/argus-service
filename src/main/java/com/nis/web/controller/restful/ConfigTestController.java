package com.nis.web.controller.restful;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.crypt.AESUtil;
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
import com.nis.util.Configurations;
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

/**
 * @ClassName: ConfigSourceTestsController
 * @Description: 配置存储服务
 * @author (zx)
 * @date 2016年9月5日 下午6:20:33
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}")
@Api(value = "ConfigTestController", description = "配置存储服务，包括管控、监测、白名单的控制类")
public class ConfigTestController extends BaseRestController {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Logger logger = LoggerFactory.getLogger(ConfigTestController.class);
	@Autowired
	protected ConfigSourcesService configSourcesService;

	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;
	private long allCount = 0l;

	@RequestMapping(value = "/cfg/v1/insertBatch", method = RequestMethod.POST)
	@ApiOperation(value = "批量业务配置存储", httpMethod = "POST", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）存储")
	public Map insertBatchaaaaa(@RequestParam(value = "configCount", required = false) Long configCount,
			@RequestParam(value = "action", required = false) Integer action,
			@RequestParam(value = "serviceType", required = false) Long serviceType,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuffer sb = new StringBuffer();
		allCount = 0l;
		long dataCount = 0l;
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread1 = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		if (configCount == null) {
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "批量增加条数不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		if (startTime == null || startTime.equals("")) {
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "开始时间不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		if (endTime == null || endTime.equals("")) {
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "结束时间不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		System.out.println("configCount" + configCount);
		System.out.println("action" + action);
		System.out.println("serviceType" + serviceType);
		System.out.println("startTime" + startTime);
		System.out.println("endTime" + endTime);
		Integer[] zeroArr = { 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
		Integer[] oneArr = { 15, 16, 32, 33, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60 };
		Integer[] twoArr = { 2 };

		try {
			Date date = new Date();
			// String date = "2016-09-26T03:39:30.577Z";
			ConfigSourceTest configSource = new ConfigSourceTest();
			configSource.setOpTime(date);
			configSource.setOpAction(1);
			configSource.setOperator("rkg");
			configSource.setVersion("0.1");
			List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
			long count = System.currentTimeMillis() + 1;

			for (Integer x = 0; x < zeroArr.length; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					ConfigCompile configCompile = setCompileTest(zeroArr[x].longValue(), 0, date, startTime, endTime,
							count);
					configCompileList.add(configCompile);
				}
			}
			for (Integer x = 15; x < oneArr.length + 15; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					ConfigCompile configCompile = setCompileTest(oneArr[x - 15].longValue(), 1, date, startTime,
							endTime, count);
					configCompileList.add(configCompile);
				}
			}
			for (Integer x = 50; x < twoArr.length + 50; x++) {
				for (Long i = 1l; i <= configCount; i++) {
					count++;
					dataCount++;
					ConfigCompile configCompile = setCompileTest(twoArr[x - 50].longValue(), 2, date, startTime,
							endTime, count);
					configCompileList.add(configCompile);
				}
			}

			configSource.setConfigCompileList(configCompileList);
			validateConfigSourceTest(thread1, start, configSource);

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			JSONArray jsonArray = JSONArray.fromObject(configSource, jsonConfig);
			String jsonStr = jsonArray.getString(0);
			String distisct = "\"district\":\"\",";
			if (jsonStr.contains(distisct)) {
				jsonStr = jsonStr.replaceAll(distisct, "");
			}
			SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST,
					request, configSource);
			System.out.println("开始执行插入操作" + dataCount + "条配置总计" + allCount + "条数据");
			Long benginTime = System.currentTimeMillis();
			configSourcesService.insertConfigSourceData(thread, System.currentTimeMillis() - start,
					configSource.getConfigCompileList(), sb);
			Long endSaveTime = System.currentTimeMillis();
			Long time = (endSaveTime - benginTime) / 1000;
			System.out.println("插入了" + dataCount + "条配置总计" + allCount + "条数据总共需要" + time + "秒");
			return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "编译配置数据插入成功",
					configSource);
		} catch (Exception e) {
			thread1.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			return serviceResponse(thread1, System.currentTimeMillis() - start, request, response, "编译配置数据插入失败", null);
		}
	}

	@RequestMapping(value = "/test/v1/insertBatchss", method = RequestMethod.POST)
	@ApiOperation(value = "批量业务配置存储", httpMethod = "POST", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）存储")
	public Map insertBatch(@RequestParam(value = "configCount", required = false) Long configCount,
			@RequestParam(value = "action", required = false) Integer action,
			@RequestParam(value = "serviceType", required = false) Long serviceType,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuffer sb = new StringBuffer();
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread1 = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		if (configCount == null) {
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "批量增加条数不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		if (serviceType == null) {
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "业务类型不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		if (startTime == null || startTime.equals("")) {
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "开始时间不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		if (endTime == null || endTime.equals("")) {
			throw new RestServiceException(thread1, System.currentTimeMillis() - start, "结束时间不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		System.out.println("configCount" + configCount);
		System.out.println("action" + action);
		System.out.println("serviceType" + serviceType);
		System.out.println("startTime" + startTime);
		System.out.println("endTime" + endTime);

		try {
			Date date = new Date();
			// String date = "2016-09-26T03:39:30.577Z";
			ConfigSourceTest configSource = new ConfigSourceTest();
			configSource.setOpTime(date);
			configSource.setOpAction(1);
			configSource.setOperator("rkg");
			configSource.setVersion("0.1");
			List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
			for (Long i = 0l; i < configCount; i++) {
				ConfigCompile configCompile = setCompileTest(serviceType, action, date, startTime, endTime, i);
				configCompileList.add(configCompile);
			}
			configSource.setConfigCompileList(configCompileList);
			validateConfigSourceTest(thread1, start, configSource);
			Long benginTime = System.currentTimeMillis();
			SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST,
					request, configSource);
			configSourcesService.insertConfigSourceData(thread, System.currentTimeMillis() - start,
					configSource.getConfigCompileList(), sb);
			Long endSaveTime = System.currentTimeMillis();
			Long time = (endSaveTime - benginTime) / 1000;
			System.out.println("插入" + configCount + "条数据总共需要" + time + "秒");

			return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "编译配置数据插入成功",
					configSource);
		} catch (Exception e) {
			thread1.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			return serviceResponse(thread1, System.currentTimeMillis() - start, request, response, "编译配置数据插入失败", null);
		}
	}

	@RequestMapping(value = "/test/v1/findCompile", method = RequestMethod.POST)
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

	public ConfigSourceTest setConfigSourceTest() throws Exception {
		Long configCount = 2l;
		Date date = new Date();
		// String date = "2016-09-26T03:39:30.577Z";
		ConfigSourceTest configSource = new ConfigSourceTest();
		configSource.setOpTime(date);
		configSource.setOpAction(1);
		configSource.setOperator("rkg");
		configSource.setVersion("0.1");
		List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
		Long serviceType = 1l;
		Integer action = 0;

		String startTime = "2016-09-26";
		String endTime = "2016-09-29";
		for (Long i = 0l; i < configCount; i++) {
			ConfigCompile configCompile = setCompileTest(serviceType, action, date, startTime, endTime, i);
			configCompileList.add(configCompile);
		}
		configSource.setConfigCompileList(configCompileList);
		// validateConfigSourceTest(thread1, start,configSource);
		return configSource;
	}

	@RequestMapping(value = "/test/v1/saveByQuery", method = RequestMethod.POST)
	public String saveByQuery(String id, HttpServletRequest request, HttpServletResponse response) {
		System.out.println(id);
		try {
			// String url =
			// "http://127.0.0.1:8888/gk/service/cfg/v1/configSources";
			String url = Configurations.getStringProperty("httpUrl", "");
			ConfigSourceTest configSource = new ConfigSourceTest();
			configSource.setOpTime(new Date());
			configSource.setOpAction(1);
			configSource.setOperator("rkg");
			configSource.setVersion("0.1");
			id = id.replace("[", "");
			id = id.replace("]", "");
			id = id.substring(1);
			List<ConfigCompile> configCompileList = new ArrayList<ConfigCompile>();
			List<ConfigCompileTest> queryConfigCompileByIdArr = configSourcesService.queryConfigCompileByIdArr(id);
			for (int i = 0; i < queryConfigCompileByIdArr.size(); i++) {
				ConfigCompile configCompile = queryConfigCompileByIdArr.get(i);
				ConfigCompile compileInfo = getCompileInfo(configCompile);
				configCompileList.add(compileInfo);
			}
			configSource.setConfigCompileList(configCompileList);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			JSONArray jsonArray = JSONArray.fromObject(configSource, jsonConfig);
			String jsonStr = jsonArray.getString(0);
			String distisct = "\"district\":\"\",";
			if (jsonStr.contains(distisct)) {
				jsonStr = jsonStr.replaceAll(distisct, "");
			}
			System.out.println(jsonStr);
			String result = sendPost(url, jsonStr);
			System.out.println(result);
			if (result.equals("")) {
				return "error";
			} else {
				return "ok";
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return "error";
	}

	@RequestMapping(value = "/test/v1/saveAllByQuery", method = RequestMethod.POST)
	public String saveAllByQuery(ConfigCompileTest configCompileTest, HttpServletRequest request,
			HttpServletResponse respons) throws Exception {
		try {
			if (configCompileTest.getSearchStartTime() != null && !configCompileTest.getSearchStartTime().equals("")) {
				configCompileTest.setStartTime(sdf.parse(configCompileTest.getSearchStartTime()));
				// configCompile.setStartTime(configCompile.getSearchStartTime());
			}
			if (configCompileTest.getSearchEndTime() != null && !configCompileTest.getSearchEndTime().equals("")) {
				configCompileTest.setEndTime(sdf.parse(configCompileTest.getSearchEndTime()));

				// configCompile.setEndTime(configCompile.getSearchEndTime());
			}
			List<ConfigCompile> compileList = new ArrayList<ConfigCompile>();
			List<ConfigCompileTest> queryAllCompile = configSourcesService.queryAllCompile(configCompileTest);

			for (int i = 0; i < queryAllCompile.size(); i++) {
				ConfigCompile configCompile = queryAllCompile.get(i);
				ConfigCompile compileInfo = getCompileInfo(configCompile);
				compileList.add(compileInfo);
			}

			ConfigSourceTest configSource = new ConfigSourceTest();
			configSource.setOpTime(new Date());
			configSource.setOpAction(1);
			configSource.setOperator("rkg");
			configSource.setVersion("0.1");
			configSource.setConfigCompileList(compileList);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			// String url =
			// "http://127.0.0.1:8888/gk/service/cfg/v1/configSources";
			String url = Configurations.getStringProperty("httpUrl", "");
			JSONArray jsonArray = JSONArray.fromObject(configSource, jsonConfig);
			String jsonStr = jsonArray.getString(0);
			String distisct = "\"district\":\"\",";
			if (jsonStr.contains(distisct)) {
				jsonStr = jsonStr.replaceAll(distisct, "");
			}
			System.out.println(jsonStr);
			String result = sendPost(url, jsonStr);
			System.out.println(result);
			if (result.equals("")) {
				return "error";
			} else {
				return "ok";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	public ConfigCompile getCompileInfo(ConfigCompile configCompileTest) {
		List<ConfigGroupRelation> groupByCompileList = configSourcesService
				.getGroupByCompile(configCompileTest.getCompileId());
		configCompileTest.setGroupRelationList(groupByCompileList);
		Long[] groupIdArr = new Long[groupByCompileList.size()];
		for (int i = 0; i < groupByCompileList.size(); i++) {
			groupIdArr[i] = groupByCompileList.get(i).getGroupId();
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

			List<StrRegion> strRegionList = configSourcesService.getStrRegionByGId(strTable, groupIdArr);
			if (null != strRegionList && strRegionList.size() > 0) {
				configCompileTest.setStrRegionList(strRegionList);
			}
		}

		if (ipTable != null) {
			if (ipTable.equals("DF_IP_PORT")) {
				System.out.println("DF_IP_PORT" + configCompileTest.getService());
			}
			if (ipTable.equals("FX_IP_PORT")) {
				System.out.println("FX_IP_PORT" + configCompileTest.getService());
			}
			List<IpRegion> ipRegionByGId = configSourcesService.getIpRegionByGId(ipTable, groupIdArr);
			if (null != ipRegionByGId && ipRegionByGId.size() > 0) {
				configCompileTest.setIpRegionList(ipRegionByGId);
			}
		}

		if (numTable != null) {
			List<NumRegion> numRegionList = configSourcesService.getNumRegionByGId(numTable, groupIdArr);
			if (null != numRegionList && numRegionList.size() > 0) {
				configCompileTest.setNumRegionList(numRegionList);
			}
		}

		return configCompileTest;
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的url
	 * @param param
	 *            请求参数,格式为 name1=value1&name2=value2的形式
	 * @return 所代表远程资源的相应结果
	 */
	public String sendPost(String url, String param) {
		// ConfigSourceTest
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(200000);
			conn.setReadTimeout(3000000);
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			out.close();
			InputStream inputStream = conn.getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常" + e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String sendGet(String url, String param) {
		BufferedReader in = null;
		String result = "";
		try {
			String urlNameString = url + "?" + param;
			urlNameString = "http://127.0.0.1:8888/gk/service/log/v1/dfStatLogDailySources?pageNo=1&pageSize=2";
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

			conn.connect();

			Map<String, List<String>> headerFields = conn.getHeaderFields();
			for (String key : headerFields.keySet()) {
				System.out.println(key);
			}

			InputStream inputStream = conn.getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送POST请求出现异常" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ConfigCompileTest setCompileTest(Long serviceType, Integer action, Date date, String startTime,
			String endTime, Long index) throws Exception {

		Long compileId = System.currentTimeMillis() + index;
		System.out.println("compileId--------------------------" + compileId);
		ConfigCompileTest configCompile = new ConfigCompileTest();
		configCompile.setCompileId(compileId);
		configCompile.setService(serviceType);
		configCompile.setAction(action);
		configCompile.setContType(0);
		configCompile.setAttrType(0);
		configCompile.setContLabel("0");
		configCompile.setTaskId(0);
		configCompile.setGuaranteeId(0);
		configCompile.setAffAirId(0);
		configCompile.setTopIcId(0);
		configCompile.setDoBlackList(0l);
		configCompile.setDoLog(0);
		configCompile.setEffectiveRange("0");
		configCompile.setConfigPercent(100l);
		configCompile.setConfigOption(1);
		configCompile.setStartTime(sdf.parse(startTime));
		configCompile.setEndTime(sdf.parse(endTime));
		// configCompile.setStartTime(startTime);
		// configCompile.setEndTime(endTime);

		configCompile.setActiveSys(0);
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

		configCompile.setStrRegionList(setStrRegionList(serviceType, groupId, date, strTable, index));
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
			Long regionId = System.currentTimeMillis() + index;
			System.out.println("Strid--------------------------" + regionId);
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
			System.out.println("Strid===================================" + regionId);
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

	public List<IpRegion> setIpRegionList(Long service, Long groupId, Date date, String tableName, Long index) {
		List<IpRegion> ipRegionList = new ArrayList<IpRegion>();
		if (tableName != null) {

			IpRegionTest ipRegion = new IpRegionTest();
			Long regionId = System.currentTimeMillis() + index;
			System.out.println("Ipid--------------------------" + regionId);
			ipRegion.setRegionId(regionId);
			ipRegion.setGroupId(groupId);
			ipRegion.setAddrType(4);
			ipRegion.setSrcIp("0.0.0.0");
			ipRegion.setMaskSrcIp("0.0.0.0");
			ipRegion.setSrcPort("0");
			ipRegion.setMaskSrcPort("0");
			ipRegion.setDstIp("0.0.0.0");
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
			Long regionId = System.currentTimeMillis() + index;
			System.out.println("numid--------------------------" + regionId);
			numRegion.setRegionId(regionId);
			numRegion.setGroupId(groupId);
			numRegion.setLowBoundary(index);
			numRegion.setUpBoundary(index);
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

	public static synchronized String getId() {

		long nowDate = new Date().getTime();
		String hexString = Integer.toHexString((int) nowDate);
		return hexString;
	}

	public static void main(String[] args) {
		try {

//			Calendar cl = Calendar.getInstance();
//			cl.add(Calendar.SECOND, 10);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//			System.out.println(sdf.format(cl.getTime().getTime() - 8 * 60 * 60 * 1000));
			// props.setProperty("jdbc.product.password", new
			// String(AESUtil.decrypt(Base64.decodeBase64(productPassword),
			// productScretKey)));
			String Key = AESUtil.initKeyString();
			String password = new String(Base64.encodeBase64(AESUtil.encrypt("changan@2016".getBytes(), Key)));
			String watchpwd = new String(AESUtil.decrypt(Base64.decodeBase64(password), Key));
			// String decryptKey = new
			// String(AESUtil.decrypt(Base64.decodeBase64("IkkVipTYFzqpSaX3nYwjRQ=="),
			// "lsBezRxQ38m60L5pWhzoyA=="));
			System.out.println(Key);
			System.out.println(password);
			System.out.println(watchpwd);
			List<String> list = new ArrayList<String>();
			// list.add("1");
			// new ConfigTestController().setStrRegionList(1l, 2l, new Date(),
			// list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
