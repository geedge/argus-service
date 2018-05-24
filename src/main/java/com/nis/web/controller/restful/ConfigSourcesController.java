package com.nis.web.controller.restful;

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
import com.nis.util.OracleErrorCodeUtil;
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

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	@ApiOperation(value = "test redis", httpMethod = "GET", response = Map.class, notes = "测试redis事务的crontroller")
	@ApiParam(value = "test redis", name = "测试redis事务的crontroller", required = true)
	public String testRedis(Integer type) {
		try {
			List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
			if (type == 1) {
				Map<String, String> map = new HashMap<String, String>();
				Integer service = 98;
				long id = configRedisService.getIncrId("seq_compileid");
				// int id=2;
				map.put("cfg_id", id + "");
				map.put("is_valid", "1");
				map.put("dst_file", "home");
				map.put("dst_file_md5", "fasdfdasfsdafdsafadsf");
				map.put("time_stamp", new Date().getTime() + "");
				map.put("level", "20");
				map.put("file_id", id + "");
				map.put("service", service + "");
				listMap.add(map);
				configRedisService.saveUnMaatConfig(listMap, service);
			} else {
				Integer service = 100;

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
				configRedisService.saveUnMaatConfig(listMap, service);
			}
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}

	@RequestMapping(value = "/saveMaat", method = RequestMethod.GET)
	@ApiOperation(value = "test redis", httpMethod = "GET", response = Map.class, notes = "测试redis事务的crontroller")
	@ApiParam(value = "test redis", name = "测试redis事务的crontroller", required = true)
	public String saveMaat(Integer service) {
		try {
			List<MaatConfig> listMap = new ArrayList<MaatConfig>();
			listMap.add(getMaatConfig(service));
			listMap.add(getMaatConfig(service));
			configRedisService.saveMaatConfig(listMap, service);
			// new ConfigRedisServiceimpl().GETMaatConfig(listMap);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}

	public MaatConfig getMaatConfig(Integer service) {
		String time = new Date().getTime() + "";
		Map<String, String> configMap = new HashMap<String, String>();
		long seq_compileid = configRedisService.getIncrId("SEQ_COMPILEID");
		long seq_groupid = configRedisService.getIncrId("SEQ_GROUPID");
		long seq_regionid = configRedisService.getIncrId("SEQ_REGIONID");
		// int id=2;
		configMap.put("cfg_id", seq_compileid + "");
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
		Map<String, String> groupMap = new HashMap<String, String>();
		groupMap.put("group_id", seq_groupid + "");
		groupMap.put("cfg_id", seq_compileid + "");
		groupMap.put("is_valid", "1");
		groupMap.put("op_time", time);
		groupMap.put("service", service + "");
		Map<String, String> strMap = new HashMap<String, String>();

		strMap.put("group_id", seq_groupid + "");
		strMap.put("region_id", seq_regionid + "");
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

		MaatConfig maatConfig = new MaatConfig();
		maatConfig.setCompileMap(configMap);
		maatConfig.setGroupMap(groupMap);
		maatConfig.setService(service);
		maatConfig.setIpRegionMap(strMap);
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
			String msg = configSourcesService.saveByJDBCThread(thread, start, configSource.getConfigCompileList(), sb);
			if (msg.equals("error")) {
				String errorCode = "";
				Exception exception = ConfigSourcesService.getMsgList().get(0);
				String str = exception.getMessage();
				if (null != str && str.length() > 0) {
					int index = str.toUpperCase().indexOf("ORA-");
					if (index != -1) {
						errorCode = str.substring(index + 4, index + 9);
					}
				}
				Map<Integer, String> throwExceptionInfo = OracleErrorCodeUtil.throwExceptionInfo(errorCode);
				for (int errorNum : throwExceptionInfo.keySet()) {
					if (errorNum == 998) {
						thread.setExceptionInfo(exception.toString());
					} else {
						thread.setExceptionInfo(throwExceptionInfo.get(errorNum));
					}
					throw new RestServiceException(thread, System.currentTimeMillis() - start,
							throwExceptionInfo.get(errorNum), errorNum);
				}
			}
			// configSourcesService.insertConfigSourceData(thread, start,
			// configSource.getConfigCompileList(), sb);

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
			if (StringUtil.isEmpty(compile.getGroupRelationList()) && StringUtil.isEmpty(compile.getIpRegionList())
					&& StringUtil.isEmpty(compile.getStrRegionList())
					&& StringUtil.isEmpty(compile.getNumRegionList())) {
				msg = configSourcesService.updateByJDBCThread3(thread, start, configSource.getConfigCompileList(),
						configSource.getOpTime(), sb);
			} else {
				msg = configSourcesService.updateByJDBCThread12(thread, start, configSource.getConfigCompileList(),
						configSource.getOpTime(), sb);
			}

			if (msg.equals("error")) {
				String errorCode = "";
				Exception exception = ConfigSourcesService.getMsgList().get(0);
				String message = exception.getMessage();
				if (null != message && message.length() > 0) {
					int index = message.toUpperCase().indexOf("ORA-");
					if (index != -1) {
						errorCode = message.substring(index + 4, index + 9);

					}
				}
				Map<Integer, String> throwExceptionInfo = OracleErrorCodeUtil.throwExceptionInfo(errorCode);
				for (int errorNum : throwExceptionInfo.keySet()) {
					if (errorNum == 998) {
						thread.setExceptionInfo(exception.toString());
					} else {
						thread.setExceptionInfo(throwExceptionInfo.get(errorNum));
					}
					throw new RestServiceException(thread, System.currentTimeMillis() - start,
							throwExceptionInfo.get(errorNum), errorNum);

				}
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
						if ((configCompile.getActiveSys() != null) || (configCompile.getEffectiveRange() != null
								&& !configCompile.getEffectiveRange().equals(""))) {
							count++;
							configSourcesService.setCompileInvalid(configCompile);
						}
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
						if ((configCompile.getActiveSys() != null) || (configCompile.getEffectiveRange() != null
								&& !configCompile.getEffectiveRange().equals(""))) {
							try {
								configSourcesService.setCompileValid(configCompile);
							} catch (Exception e1) {
								String errorCode = "";
								String message = e.getMessage();
								if (null != message && message.length() > 0) {
									int index = message.toUpperCase().indexOf("ORA-");
									if (index != -1) {
										errorCode = message.substring(index + 4, index + 9);

									}
								}
								Map<Integer, String> throwExceptionInfo = OracleErrorCodeUtil
										.throwExceptionInfo(errorCode);
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
						}
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
	@RequestMapping(value = "/cfg/v1/commonSources", method = RequestMethod.POST, produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "回调配置存储", httpMethod = "POST", response = Map.class, notes = "回调配置存储服务")
	//@ApiParam(value = "回调配置数据源", name = "JSONObject", requirerue)
	public Map createCommonConfigSource(@RequestBody String jsonString , HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,null);
		StringBuffer sb = new StringBuffer();
		
		configSourcesService.savaCommonSources(thread, start, jsonString, sb);

		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response,
				"配置数据插入成功" , Constants.IS_DEBUG ? jsonString  : null);
	}
	
	@RequestMapping(value = "/cfg/v1/fileUploadSources", method = RequestMethod.POST)
	@ApiOperation(value = "样例文件上传服务", httpMethod = "POST", response = Map.class, notes = "样例文件上传服务")
	@ApiParam(value = "样例文件上传服务", name = "MultipartFile", required = true)
	public Map fileUploadSource(@RequestBody MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,null);
		String filePath ="";
		try {
			FileDesc fileDesc = (FileDesc) JSONObject.toBean(JSONObject.fromObject(request.getHeader("File-Desc")),FileDesc.class);
			if (!StringUtil.isEmpty(fileDesc.getChecksum())) {
				//验证Md5
				String md5 = DigestUtils.md5Hex(file.getBytes());
				System.out.println("----------------------------MD5:'"+md5+"'==='"+fileDesc.getChecksum()+"'");
				if (!md5.equals(fileDesc.getChecksum())) {
					thread.setExceptionInfo("checksum与文件MD5值不一致");
					throw new RestServiceException(thread, System.currentTimeMillis() - start, "checksum与文件MD5值不一致",
							RestBusinessCode.config_integrity_error.getValue());
				}
				String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
				FastDFSFile fdsfile = new FastDFSFile(file.getBytes(),file.getOriginalFilename(), ext);
//				NameValuePair[] meta_list = new NameValuePair[5];
//				meta_list[0] = new NameValuePair("fileName", file.getOriginalFilename());
//				meta_list[1] = new NameValuePair("fileLength", String.valueOf(file.getSize()));
//				meta_list[2] = new NameValuePair("fileExt", ext);
//				meta_list[3] = new NameValuePair("fileAuthor", "rkg");
//				meta_list[4] = new NameValuePair("fileMd5", md5);
				filePath = FileManager.upload(fdsfile, null);
			}else{
				thread.setExceptionInfo("请求头信息中缺少checksum参数");
				throw new RestServiceException(thread, System.currentTimeMillis() - start, "请求头信息中缺少checksum参数",
						RestBusinessCode.missing_args.getValue());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("accessUrl", filePath.substring(filePath.indexOf("group")));
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response,
				"文件上传成功" ,jsonObj);
	}
	
	private boolean isBlank(Date datetime) {
		if (null != datetime) {
			return true;
		}
		return false;
	}

}
