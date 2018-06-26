package com.nis.web.controller.restful;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigSource;
import com.nis.domain.restful.FileDesc;
import com.nis.domain.restful.MaatConfig;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.FileUtils;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.fdfs.FastDFSFile;
import com.nis.web.service.fdfs.FileManager;
import com.nis.web.service.restful.ConfigRedisService;
import com.nis.web.service.restful.ConfigSourcesService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * @ClassName: ConfigSourcesController
 * @Description: 配置存储服务
 * @author (zx)
 * @date 2016年9月5日 下午6:20:33
 * @version V1.0
 */
// @Scope("prototype")
@RestController
@RequestMapping("${servicePath}")
@Api(value = "ConfigSourcesController", description = "配置存储服务，包括管控、监测、白名单的控制类")
public class ConfigSourcesController extends BaseRestController {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected ConfigSourcesService configSourcesService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@Autowired
	ConfigRedisService configRedisService;

	@RequestMapping(value = "/cfg/v1/saveUnMaat", method = RequestMethod.GET)
	@ApiOperation(value = "test redis", httpMethod = "GET", response = Map.class, notes = "测试redis事务的crontroller")
	@ApiParam(value = "test redis", name = "测试redis事务的crontroller", required = true)
	public String saveUnMaat(Integer type) {
		try {
			List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
			if (type == 1) {
				Map<String, String> map = new HashMap<String, String>();
				Integer service = 261;
				long id = configRedisService.getIncrId("seq_compileid");
				// int id=2;
				map.put("cfg_id", id + "");
				map.put("is_valid", "1");
				map.put("dst_file", "home");
				map.put("dst_file_md5", "fasdfdasfsdafdsafadsf");
				map.put("op_time", new Date().getTime() + "");
				map.put("level", "20");
				map.put("file_id", id + "");
				map.put("service", service + "");
				listMap.add(map);

				Map<Integer, List<Map<String, String>>> configMap = new HashMap<Integer, List<Map<String, String>>>();
				configMap.put(1, listMap);
				configRedisService.saveUnMaatConfig(configMap);
			} else {
				Integer service = 265;
				Map<String, String> map = new HashMap<String, String>();
				long id = configRedisService.getIncrId("seq_compileid");
				// int id=2;
				map.put("cfg_id", id + "");
				map.put("is_valid", "1");
				map.put("service", "100");
				map.put("description", "123" + id);
				map.put("level", "20");
				listMap.add(map);
				map.put("service", service + "");

				Map<Integer, List<Map<String, String>>> configMap = new HashMap<Integer, List<Map<String, String>>>();
				configMap.put(1, listMap);
				configRedisService.saveUnMaatConfig(configMap);
			}
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}

	@RequestMapping(value = "/cfg/v1/delUnMaat", method = RequestMethod.GET)
	@ApiOperation(value = "test redis", httpMethod = "GET", response = Map.class, notes = "测试redis事务的crontroller")
	@ApiParam(value = "test redis", name = "测试redis事务的crontroller", required = true)
	public String delUnMaat(int redisDB, int service, long id) {
		try {
			Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
			ArrayList<Long> arrayList = new ArrayList<Long>();
			arrayList.add(id);
			map.put(service, arrayList);
			Map<Integer, Map<Integer, List<Long>>> idMap = new HashMap<Integer, Map<Integer, List<Long>>>();
			idMap.put(redisDB, map);
			configRedisService.delUnMaatConfig(idMap);
			// configRedisService.saveMaatConfig(listMap, service);
			// new ConfigRedisServiceimpl().GETMaatConfig(listMap);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}

	@RequestMapping(value = "/cfg/v1/delMaat", method = RequestMethod.GET)
	@ApiOperation(value = "test redis", httpMethod = "GET", response = Map.class, notes = "测试redis事务的crontroller")
	@ApiParam(value = "test redis", name = "测试redis事务的crontroller", required = true)
	public String delMaat(int redisDB, int service, long id) {
		try {
			Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
			ArrayList<Long> arrayList = new ArrayList<Long>();
			arrayList.add(id);
			map.put(service, arrayList);
			Map<Integer, Map<Integer, List<Long>>> idMap = new HashMap<Integer, Map<Integer, List<Long>>>();
			idMap.put(redisDB, map);

			boolean delMaatConfig = configRedisService.delMaatConfig(idMap);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}

	@RequestMapping(value = "/cfg/v1/saveMaat", method = RequestMethod.GET)
	@ApiOperation(value = "test redis", httpMethod = "GET", response = Map.class, notes = "测试redis事务的crontroller")
	@ApiParam(value = "test redis", name = "测试redis事务的crontroller", required = true)
	public String saveMaat(Integer service) {
		try {
			Map<Integer, List<MaatConfig>> configMap = new HashMap<Integer, List<MaatConfig>>();
			List<Integer> redisDBByService = ServiceAndRDBIndexReal.getRedisDBByService(service);
			long seq_groupid = configRedisService.getIncrId("SEQ_GROUPID");
			List<MaatConfig> listMap = new ArrayList<MaatConfig>();
			long seq_regionid2 = configRedisService.getIncrId("SEQ_REGIONID");
			listMap.add(getMaatConfig1(service, seq_groupid,seq_regionid2));
			//listMap.add(getMaatConfig2(service, seq_groupid,seq_regionid2));

			for (Integer db : redisDBByService) {
				configMap.put(db, listMap);
			}
			configRedisService.saveMaatConfig(configMap);
			// configRedisService.saveMaatConfig(listMap, service);
			// new ConfigRedisServiceimpl().GETMaatConfig(listMap);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}

	public MaatConfig getMaatConfig1(Integer service, long seq_groupid,long seq_regionid2) {
		String time = new Date().getTime() + "";
		Map<String, String> configMap1 = new HashMap<String, String>();
		long seq_compileid = configRedisService.getIncrId("SEQ_COMPILEID");
		long seq_regionid1 = configRedisService.getIncrId("SEQ_REGIONID");
		// int id=2;
		configMap1.put("compile_id", seq_compileid + "");
		configMap1.put("service", service + "");
		configMap1.put("action", "0");
		configMap1.put("do_blacklist", "1");
		configMap1.put("do_log", "1");
		configMap1.put("effective_range", "0");
		configMap1.put("user_region", "0");
		configMap1.put("is_valid", "1");
		configMap1.put("group_num", "1");
		configMap1.put("father_cfg_id", "0");
		configMap1.put("op_time", time);

		List<Map<String, String>> groupMapList = new ArrayList<Map<String, String>>();
		Map<String, String> groupMap1 = new HashMap<String, String>();
		groupMap1.put("group_id", seq_groupid + "");
		groupMap1.put("compile_id", seq_compileid + "");
		groupMap1.put("is_valid", "1");
		groupMap1.put("op_time", time);
		groupMap1.put("service", service + "");
		groupMapList.add(groupMap1);
		Long incrId = configRedisService.getIncrId("SEQ_GROUPID");
		Map<String, String> groupMap2 = new HashMap<String, String>();
		groupMap2.put("group_id", incrId + "");
		groupMap2.put("compile_id", seq_compileid + "");
		groupMap2.put("is_valid", "1");
		groupMap2.put("op_time", time);
		groupMap2.put("service", service + "");
		groupMapList.add(groupMap2);
		List<Map<String, String>> ipRegionMapList = new ArrayList<Map<String, String>>();
		Map<String, String> strMap = new HashMap<String, String>();
		strMap.put("group_id", incrId + "");
		strMap.put("region_id", seq_regionid1 + "");
		strMap.put("addr_type", "4");
		strMap.put("src_ip", "0.0.0.0");
		strMap.put("mask_src_ip", "255.255.255.255");
		strMap.put("src_port", "0");
		strMap.put("mask_src_port", "0");
		strMap.put("dst_ip", "0.0.0.0");
		strMap.put("mask_dst_ip", "255.255.255.255");
		strMap.put("dst_port", "0");
		strMap.put("mask_dst_port", "0");
		strMap.put("protocol", "0");
		strMap.put("direction", "0");
		strMap.put("is_valid", "1");
		strMap.put("op_time", time);
		strMap.put("service", service + "");

		ipRegionMapList.add(strMap);

		strMap = new HashMap<String, String>();
		strMap.put("group_id", seq_groupid + "");
		strMap.put("region_id", seq_regionid2 + "");
		strMap.put("addr_type", "4");
		strMap.put("src_ip", "0.0.0.0");
		strMap.put("mask_src_ip", "255.255.255.255");
		strMap.put("src_port", "0");
		strMap.put("mask_src_port", "0");
		strMap.put("dst_ip", "0.0.0.0");
		strMap.put("mask_dst_ip", "255.255.255.255");
		strMap.put("dst_port", "0");
		strMap.put("mask_dst_port", "0");
		strMap.put("protocol", "0");
		strMap.put("direction", "0");
		strMap.put("is_valid", "1");
		strMap.put("op_time", time);
		strMap.put("service", service + "");
		ipRegionMapList.add(strMap);

		MaatConfig maatConfig = new MaatConfig();
		maatConfig.setCompileMap(configMap1);
		maatConfig.setGroupMapList(groupMapList);
		maatConfig.setService(service);
		maatConfig.setIpRegionMapList(ipRegionMapList);
		return maatConfig;
	}

	public MaatConfig getMaatConfig2(Integer service, long seq_groupid,long seq_regionid2) {
		String time = new Date().getTime() + "";
		Map<String, String> configMap = new HashMap<String, String>();
		long seq_compileid = configRedisService.getIncrId("SEQ_COMPILEID");

		// int id=2;
		configMap.put("compile_id", seq_compileid + "");
		configMap.put("service", service + "");
		configMap.put("action", "0");
		configMap.put("do_blacklist", "1");
		configMap.put("do_log", "1");
		configMap.put("effective_range", "0");
		configMap.put("user_region", "0");
		configMap.put("is_valid", "1");
		configMap.put("group_num", "1");
		configMap.put("father_cfg_id", "0");
		configMap.put("op_time", time);

		List<Map<String, String>> groupMapList = new ArrayList<Map<String, String>>();
		Map<String, String> groupMap = new HashMap<String, String>();
		groupMap.put("group_id", seq_groupid + "");
		groupMap.put("compile_id", seq_compileid + "");
		groupMap.put("is_valid", "1");
		groupMap.put("op_time", time);
		groupMap.put("service", service + "");
		groupMapList.add(groupMap);

		List<Map<String, String>> ipRegionMapList = new ArrayList<Map<String, String>>();
		Map<String, String> strMap = new HashMap<String, String>();
		strMap = new HashMap<String, String>();
		strMap.put("group_id", seq_groupid + "");
		strMap.put("region_id", seq_regionid2 + "");
		strMap.put("addr_type", "4");
		strMap.put("src_ip", "0.0.0.0");
		strMap.put("mask_src_ip", "255.255.255.255");
		strMap.put("src_port", "0");
		strMap.put("mask_src_port", "0");
		strMap.put("dst_ip", "0.0.0.0");
		strMap.put("mask_dst_ip", "255.255.255.255");
		strMap.put("dst_port", "0");
		strMap.put("mask_dst_port", "0");
		strMap.put("protocol", "0");
		strMap.put("direction", "0");
		strMap.put("is_valid", "1");
		strMap.put("op_time", time);
		strMap.put("service", service + "");
		ipRegionMapList.add(strMap);

		MaatConfig maatConfig = new MaatConfig();
		maatConfig.setCompileMap(configMap);
		maatConfig.setGroupMapList(groupMapList);
		maatConfig.setService(service);
		maatConfig.setIpRegionMapList(ipRegionMapList);
		return maatConfig;
	}

	@RequestMapping(value = "/cfg/v1/configSources", method = RequestMethod.POST)
	@ApiOperation(value = "业务配置存储", httpMethod = "POST", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）存储")
	@ApiParam(value = "业务配置数据源", name = "configSource", required = true)
	public Map createConfigSource(@RequestBody ConfigSource configSource, HttpServletRequest request,
			HttpServletResponse response) {

		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				configSource);
		StringBuffer sb = new StringBuffer();
		if (null != configSource && null != configSource.getConfigCompileList()
				&& configSource.getConfigCompileList().size() > 0) {
			int opAction = configSource.getOpAction();
			checkOpAction(thread, System.currentTimeMillis() - start, opAction, 1);
			// 验证配置编译数据
			validateConfigSource(thread, start, configSource);
			Long benginTime = System.currentTimeMillis();
			String msg = configSourcesService.saveMaatConfig(thread, start, configSource.getConfigCompileList(), sb);
//			String msg = "";
			if ("error".equals(msg)) {
				Exception exception = ConfigSourcesService.getMsgList().get(0);
				throw new RestServiceException(thread, System.currentTimeMillis() - start,
						"存储编译配置时出现异常" + exception.getMessage(), RestBusinessCode.unknow_error.getValue());
			}
			Long endSaveTime = System.currentTimeMillis();
			Long time = (endSaveTime - benginTime) / 1000;
			System.out.println("插入数据成功总共需要" + time + "秒");
		} else {
			thread.setExceptionInfo("编译配置数据不能为空");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "编译配置数据不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response,
				"编译配置数据插入成功" + sb.toString(), Constants.IS_DEBUG ? configSource : null);
	}

	@RequestMapping(value = "/cfg/v1/configSources", method = RequestMethod.PUT)
	@ApiOperation(value = "业务配置状态更新", httpMethod = "PUT", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）进行配置失效")
	@ApiParam(value = "业务配置数据源", name = "configSource", required = true)
	public Map updateConfigSource(@RequestBody ConfigSource configSource, HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request,
				configSource);
		StringBuffer sb = new StringBuffer();
		if (null == configSource.getOpTime()) {
			configSource.setOpTime(new Date());
		}
		if (null != configSource && null != configSource.getConfigCompileList()
				&& configSource.getConfigCompileList().size() > 0) {
			int opAction = configSource.getOpAction();
			checkOpAction(thread, System.currentTimeMillis() - start, opAction, 2);
			String msg = "";
			ConfigCompile compile = configSource.getConfigCompileList().get(0);
			// zdx 20180528改为调用rkg接口取消配置
			msg = configSourcesService.updateConfigSources(thread, start, configSource.getConfigCompileList(),
					configSource.getOpTime(), sb);

			if (msg.equals("error")) {
				Exception exception = ConfigSourcesService.getMsgList().get(0);
				throw new RestServiceException(thread, System.currentTimeMillis() - start,
						"修改编译配置状态时出现异常" + exception.getMessage(), RestBusinessCode.unknow_error.getValue());
			}
			// configSourcesService.updateConfigSource(thread, start,
			// configSource.getConfigCompileList(),
			// configSource.getOpTime(), sb);

		} else {
			thread.setExceptionInfo("编译配置数据不能为空");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "编译配置数据不能为空" + sb.toString(),
					RestBusinessCode.missing_args.getValue());
		}
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response,
				"编译配置修改成功" + sb.toString(), Constants.IS_DEBUG ? configSource : null);
	}

	@RequestMapping(value = "/cfg/v1/configModifySources", method = RequestMethod.PUT)
	@ApiOperation(value = "编译配置修改", httpMethod = "PUT", response = Map.class, notes = "对有效的配置（封堵|监测|白名单）存储")
	@ApiParam(value = "编译配置修改", name = "configSource", required = true)
	public Map configModifySources(@RequestBody ConfigSource configSource, HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request,
				configSource);
		StringBuffer sb = new StringBuffer();
		if (null != configSource && null != configSource.getConfigCompileList()
				&& configSource.getConfigCompileList().size() > 0) {
			int opAction = configSource.getOpAction();
			checkOpAction(thread, System.currentTimeMillis() - start, opAction, 2);
			// 验证配置编译数据
			validateConfigSource(thread, start, configSource);
			try {
				List<ConfigCompile> configCompileList = configSource.getConfigCompileList();
				if (null != configCompileList && configCompileList.size() > 0) {
					int count = 0;
					for (ConfigCompile configCompile : configCompileList) {
						Long compileId = configCompile.getCompileId();
						if (null == compileId) {
							thread.setExceptionInfo("编译配置id不能为空");
							throw new RestServiceException(thread, System.currentTimeMillis() - start, "编译配置id不能为空",
									RestBusinessCode.missing_args.getValue());
						}
						// if ((configCompile.getActiveSys() != null) ||
						// (configCompile.getEffectiveRange() != null
						// && !configCompile.getEffectiveRange().equals(""))) {
						// count++;
						// configSourcesService.setCompileInvalid(configCompile);
						// }
					}
					if (count > 0) {
						Thread.sleep(14000);
					}
					String str = configSourcesService.configModifySources(configCompileList);
					if (str.equals("idIsNull")) {
						thread.setExceptionInfo("编译配置id不能为空");
						throw new RestServiceException(thread, System.currentTimeMillis() - start, "编译配置id不能为空",
								RestBusinessCode.missing_args.getValue());
					}

				} else {
					thread.setExceptionInfo("编译配置数据不能为空");
					throw new RestServiceException(thread, System.currentTimeMillis() - start, "编译配置数据不能为空",
							RestBusinessCode.missing_args.getValue());
				}

			} catch (Exception e) {
				List<ConfigCompile> configCompileList = configSource.getConfigCompileList();
				if (null != configCompileList && configCompileList.size() > 0) {
					for (ConfigCompile configCompile : configCompileList) {
						Long compileId = configCompile.getCompileId();
						if (null == compileId) {
							thread.setExceptionInfo("编译配置id不能为空");
							throw new RestServiceException(thread, System.currentTimeMillis() - start, "编译配置id不能为空",
									RestBusinessCode.missing_args.getValue());
						}
						// if ((configCompile.getActiveSys() != null) ||
						// (configCompile.getEffectiveRange() != null
						// && !configCompile.getEffectiveRange().equals(""))) {
						// try {
						// configSourcesService.setCompileValid(configCompile);
						// } catch (Exception e1) {
						// String errorCode = "";
						// String message = e.getMessage();
						// if (null != message && message.length() > 0) {
						// int index = message.toUpperCase().indexOf("ORA-");
						// if (index != -1) {
						// errorCode = message.substring(index + 4, index + 9);
						//
						// }
						// }
						// Map<Integer, String> throwExceptionInfo = OracleErrorCodeUtil
						// .throwExceptionInfo(errorCode);
						// for (int errorNum : throwExceptionInfo.keySet()) {
						// if (errorNum == 998) {
						// thread.setExceptionInfo(e.toString());
						// } else {
						// thread.setExceptionInfo(throwExceptionInfo.get(errorNum));
						// }
						// throw new RestServiceException(thread, System.currentTimeMillis() - start,
						// throwExceptionInfo.get(errorNum), errorNum);
						// }
						// }
						// }
					}
				}

				String errorCode = "";
				String message = e.getMessage();
				if (null != message && message.length() > 0) {
					int index = message.toUpperCase().indexOf("ORA-");
					if (index != -1) {
						errorCode = message.substring(index + 4, index + 9);

					}
				}
				Map<Integer, String> throwExceptionInfo = OracleErrorCodeUtil.throwExceptionInfo(errorCode);
				for (int errorNum : throwExceptionInfo.keySet()) {
					if (errorNum == 998) {
						thread.setExceptionInfo(e.toString());
					} else {
						thread.setExceptionInfo(throwExceptionInfo.get(errorNum));
					}
					throw new RestServiceException(thread, System.currentTimeMillis() - start,
							throwExceptionInfo.get(errorNum), errorNum);

				}
			}
		} else {
			thread.setExceptionInfo("编译配置数据不能为空");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "编译配置数据不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response,
				"修改编译配置数据成功" + sb.toString(), Constants.IS_DEBUG ? configSource : null);
	}

	private void validateConfigSource(SaveRequestLogThread thread, long start, ConfigSource configSource) {
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
			thread.setExceptionInfo(errorInfo);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, errorInfo,
					RestBusinessCode.missing_args.getValue());
		}

	}

	@RequestMapping(value = "/cfg/v1/commonSources", method = RequestMethod.POST, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "回调配置存储", httpMethod = "POST", response = Map.class, notes = "回调配置存储服务")
	public Map createCommonConfigSource(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		StringBuffer sb = new StringBuffer();

		String msg = configSourcesService.saveCommonSources(thread, start, jsonString, sb);
		if (msg.equals("error")) {
			Exception exception = ConfigSourcesService.getMsgList().get(0);
			throw new RestServiceException(thread, System.currentTimeMillis() - start,
					"存储回调配置时出现异常" + exception.getMessage(), RestBusinessCode.unknow_error.getValue());
		}
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response, "配置数据插入成功",
				Constants.IS_DEBUG ? jsonString : null);
	}

	@RequestMapping(value = "/cfg/v1/commonSources", method = RequestMethod.PUT)
	@ApiOperation(value = "回调配置状态修改", httpMethod = "PUT", response = Map.class, notes = "回调配置状态修改服务")
	@ApiParam(value = "回调配置状态修改", name = "updateStatConfigSource", required = true)
	public Map updateCommonConfigSource(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request,
				jsonString);
		StringBuffer sb = new StringBuffer();

		String msg = configSourcesService.updateCommonSources(thread, start, jsonString, new Date(), sb);
		if (msg.equals("error")) {
			Exception exception = ConfigSourcesService.getMsgList().get(0);
			throw new RestServiceException(thread, System.currentTimeMillis() - start,
					"修改回调配置状态时出现异常" + exception.getMessage(), RestBusinessCode.unknow_error.getValue());
		}
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response, "配置状态修改成功",
				Constants.IS_DEBUG ? jsonString : null);
	}

	@RequestMapping(value = "/cfg/v1/fileUploadSources", method = RequestMethod.POST)
	@ApiOperation(value = "样例文件上传服务", httpMethod = "POST", response = Map.class, notes = "样例文件上传服务")
	@ApiParam(value = "样例文件上传服务", name = "MultipartFile", required = true)
	public Map fileUploadSource(@RequestBody MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		String filePath = "";
		try {
			FileDesc fileDesc = (FileDesc) JSONObject.toBean(JSONObject.fromObject(request.getHeader("File-Desc")),
					FileDesc.class);
			if (null == file) {
				thread.setExceptionInfo("请选择上传文件到file参数");
				throw new RestServiceException(thread, System.currentTimeMillis() - start, "请选择上传文件到file参数",
						RestBusinessCode.missing_args.getValue());

			}
			if (!StringUtil.isEmpty(fileDesc.getChecksum())) {
				// 验证Md5
				String md5 = DigestUtils.md5Hex(file.getInputStream());
				// String md5 = DigestUtils.md5Hex(file.getBytes());
				System.out.println("----------------------------MD5:'" + md5 + "'==='" + fileDesc.getChecksum() + "'");
				if (!md5.equals(fileDesc.getChecksum())) {
					thread.setExceptionInfo("checksum与文件MD5值不一致");
					throw new RestServiceException(thread, System.currentTimeMillis() - start, "checksum与文件MD5值不一致",
							RestBusinessCode.config_integrity_error.getValue());
				}
				String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
				FastDFSFile fdsfile = new FastDFSFile(file.getBytes(), file.getOriginalFilename(), ext);
				// NameValuePair[] meta_list = new NameValuePair[5];
				// meta_list[0] = new NameValuePair("fileName", file.getOriginalFilename());
				// meta_list[1] = new NameValuePair("fileLength",
				// String.valueOf(file.getSize()));
				// meta_list[2] = new NameValuePair("fileExt", ext);
				// meta_list[3] = new NameValuePair("fileAuthor", "rkg");
				// meta_list[4] = new NameValuePair("fileMd5", md5);
				logger.info("-----------------调用接口上传文件---------------");
				filePath = FileManager.upload(fdsfile, null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonObj = new JSONObject();
		// jsonObj.put("accessUrl", filePath.substring(filePath.indexOf("group")));
		jsonObj.put("accessUrl", filePath);
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response, "文件上传成功", jsonObj);
	}

	@RequestMapping(value = "/cfg/v1/fileDigestSources", method = RequestMethod.POST)
	@ApiOperation(value = "文件摘要获取", httpMethod = "POST", response = Map.class, notes = "文件摘要获取")
	@ApiParam(value = "摘要文件", name = "MultipartFile", required = true)
	public Map fileDigestSources(@RequestBody MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				file, null);
		if (file == null) {
			thread.setExceptionInfo("请上传获取摘要的文件到file");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "请上传获取摘要的文件到file参数",
					RestBusinessCode.missing_args.getValue());
		}
		JSONObject resultObject = new JSONObject();
		FileDesc fileDesc = (FileDesc) JSONObject.toBean(JSONObject.fromObject(request.getHeader("File-Desc")),
				FileDesc.class);
		try {
			if (!StringUtil.isEmpty(fileDesc.getChecksum())) {
				// 验证Md5

				String md5 = DigestUtils.md5Hex(file.getInputStream());
				System.out.println("----------------------------MD5:'" + md5 + "'==='" + fileDesc.getChecksum() + "'");
				if (!md5.equals(fileDesc.getChecksum())) {
					thread.setExceptionInfo("checksum与文件MD5值不一致");
					throw new RestServiceException(thread, System.currentTimeMillis() - start, "checksum与文件MD5值不一致",
							RestBusinessCode.config_integrity_error.getValue());
				}
				String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
				FastDFSFile fdsfile = new FastDFSFile(file.getBytes(), file.getOriginalFilename(), ext);
				// NameValuePair[] meta_list = new NameValuePair[5];
				// meta_list[0] = new NameValuePair("fileName", file.getOriginalFilename());
				// meta_list[1] = new NameValuePair("fileLength",
				// String.valueOf(file.getSize()));
				// meta_list[2] = new NameValuePair("fileExt", ext);
				// meta_list[3] = new NameValuePair("fileAuthor", "rkg");
				// meta_list[4] = new NameValuePair("fileMd5", md5);
				logger.info("-----------------调用接口上传文件---------------");
				String filePath = FileManager.upload(fdsfile, null);
				// resultObject.put("path", filePath.substring(filePath.indexOf("group")));
				resultObject.put("path", filePath);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("文件上传过程中出现异常");
			thread.setExceptionInfo("文件上传过程中出现异常");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "文件上传过程中出现异常",
					RestBusinessCode.unknow_error.getValue());
		}
		try {
			String tempFilePath = request.getRealPath(File.separator) + "upload" + File.separator
					+ (new Date()).getTime() + file.getOriginalFilename();
			file.transferTo(new File(tempFilePath));
			//System.out.println("------------" + tempFilePath);
			System.out.println("摘要获取开始：---------------");
			String digestStr = configSourcesService.getDigestGen(request.getRealPath(File.separator), tempFilePath);
			System.out.println("摘要获取结束：---------------:"+digestStr);
			resultObject.put("digest", digestStr);
			resultObject.put("rawLen", file.getSize());
			FileUtils.deleteFile(tempFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("摘要获取过程中出现异常");
			thread.setExceptionInfo("摘要获取过程中出现异常");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "摘要获取过程中出现异常",
					RestBusinessCode.unknow_error.getValue());
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "摘要获取成功", resultObject);
	}

	private boolean isBlank(Date datetime) {
		if (null != datetime) {
			return true;
		}
		return false;
	}

}
