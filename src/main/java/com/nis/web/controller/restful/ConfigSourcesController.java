package com.nis.web.controller.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigSource;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.TestRedisService;
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
	TestRedisService testRedisServiceimpl;

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	@ApiOperation(value = "test redis", httpMethod = "GET", response = Map.class, notes = "测试redis事务的crontroller")
	@ApiParam(value = "test redis", name = "测试redis事务的crontroller", required = true)
	public String testRedis() {
		try {
			testRedisServiceimpl.saveConfigCompile();
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
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
				"编译配置数据插入成功" + sb.toString(), Constants.IS_DEBUG?configSource:null);
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
				"编译配置修改成功" + sb.toString(), Constants.IS_DEBUG?configSource:null);
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
				"修改编译配置数据成功" + sb.toString(), Constants.IS_DEBUG?configSource:null);
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

	private boolean isBlank(Date datetime) {
		if (null != datetime) {
			return true;
		}
		return false;
	}

}
