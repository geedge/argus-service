package com.nis.web.controller.restful;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nis.web.service.AuditLogThread;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigSource;
import com.nis.domain.restful.FileDesc;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.CompileVal;
import com.nis.util.Constants;
import com.nis.util.FileUtils;
import com.nis.util.OracleErrorCodeUtil;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.fdfs.FastDFSFile;
import com.nis.web.service.fdfs.FileManager;
import com.nis.web.service.restful.ConfigRedisService;
import com.nis.web.service.restful.ConfigSourcesService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONObject;

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
@Api(value = "ConfigSourcesController", description = "配置规则服务，主要对MAAT规则和回调规则做解析、存储与更新操作")
public class ConfigSourcesController extends BaseRestController {

	@Autowired
	protected ConfigSourcesService configSourcesService;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@Autowired
	ConfigRedisService configRedisService;



	@RequestMapping(value = "/cfg/v1/configSources", method = RequestMethod.POST)
	@ApiOperation(value = "MAAT规则存储接口", httpMethod = "POST", response = Map.class, notes = "接收MAAT规则数据，存储到流量处理平台配置线中")
	@ApiParam(value = "MAAT规则对象", name = "configSource", required = true)
	public Map createMaatConfigSource(@RequestBody ConfigSource configSource, HttpServletRequest request,
			HttpServletResponse response) {
		CompileVal.setBusinessCode(null);
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				configSource);

		StringBuffer sb = new StringBuffer();
		if (null != configSource && null != configSource.getConfigCompileList()
				&& configSource.getConfigCompileList().size() > 0) {
            checkOpAction(thread, System.currentTimeMillis() - start, configSource.getOpAction(), Constants.OPACTION_POST);
			// 验证配置编译数据
			validateConfigSource(thread, start, configSource);
			String msg = configSourcesService.saveMaatConfig(thread, start, configSource.getConfigCompileList(), sb);

            if ("error".equals(msg)) {
				Exception exception = ConfigSourcesService.getMsgList().get(0);
				if (exception instanceof RestServiceException) {
					throw new RestServiceException(thread, System.currentTimeMillis() - start,
						"Maat 规则存储异常:" + exception.getMessage(), CompileVal.getBusinessCode());
				}else{
					throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
						"Maat 规则存储异常:" + exception.getMessage(), CompileVal.getBusinessCode());
				}
				
			}
		} else {
			thread.setExceptionInfo("Maat规则不能为空");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "Maat规则不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response,
				"Maat规则下发成功" + sb.toString(), Constants.IS_DEBUG ? configSource : null);
	}

	@RequestMapping(value = "/cfg/v1/configSources", method = RequestMethod.PUT)
	@ApiOperation(value = "MAAT规则状态更新接口", httpMethod = "PUT", response = Map.class, notes = "接收Maat规则,对其状态置为失效")
	@ApiParam(value = "MAAT规则对象", name = "configSource", required = true)
	public Map updateConfigSource(@RequestBody ConfigSource configSource, HttpServletRequest request,
			HttpServletResponse response) {
		CompileVal.setBusinessCode(null);
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request,
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
				if (exception instanceof RestServiceException) {
					throw new RestServiceException(thread, System.currentTimeMillis() - start,
							"MAAT规则状态更新时出现异常:" + exception.getMessage(),CompileVal.getBusinessCode());
				}else {
					throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
							"MAAT规则状态更新时出现异常:" + exception.getMessage(),CompileVal.getBusinessCode());
				}
				
			}
			// configSourcesService.updateConfigSource(thread, start,
			// configSource.getConfigCompileList(),
			// configSource.getOpTime(), sb);

		} else {
			thread.setExceptionInfo("Maat规则不能为空");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "Maat规则不能为空" + sb.toString(),
					RestBusinessCode.missing_args.getValue());
		}
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response,
				"Maat规则状态更新成功" + sb.toString(), Constants.IS_DEBUG ? configSource : null);
	}

	@RequestMapping(value = "/cfg/v1/configModifySources", method = RequestMethod.PUT)
	@ApiOperation(value = "MAAT规则修改接口", httpMethod = "PUT", response = Map.class, notes = "接收Maat规则,对其规则内容进行修改")
	@ApiParam(value = "MAAT规则对象", name = "configSource", required = true)
	public Map configModifySources(@RequestBody ConfigSource configSource, HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request,
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
					throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
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

	private void validateConfigSource(AuditLogThread thread, long start, ConfigSource configSource) {
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
			errorInfo = "Maat规则不能为空";
    }
		if (!errorInfo.equals("")) {
			thread.setExceptionInfo(errorInfo);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, errorInfo,
					RestBusinessCode.missing_args.getValue());
		}

	}

	@RequestMapping(value = "/cfg/v1/commonSources", method = RequestMethod.POST, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "回调（通用）规则存储接口", httpMethod = "POST", response = Map.class, notes = "接收回调规则数据,格式为结构化行列式JSON,存储到流量处理平台配置线中")
	public Map createCommonConfigSource(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		CompileVal.setBusinessCode(null);
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);

		String msg = configSourcesService.saveCommonSources(thread, start, jsonString);
		if (msg.equals("error")) {
			Exception exception = ConfigSourcesService.getMsgList().get(0);
			if (exception instanceof RestServiceException) {
				throw new RestServiceException(thread, System.currentTimeMillis() - start,
					"回调规则存储异常:" + exception.getMessage(), CompileVal.getBusinessCode());
			}else {
				throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
					"回调规则存储异常:" + exception.getMessage(), CompileVal.getBusinessCode());
			}
			
		}
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response, "回调规则下发成功",
				Constants.IS_DEBUG ? jsonString : null);
	}


	@RequestMapping(value = "/cfg/v1/commonSources", method = RequestMethod.PUT)
	@ApiOperation(value = "回调（通用）规则状态更新接口", httpMethod = "PUT", response = Map.class, notes = "接收回调规则,对其状态置为失效")
	public Map updateCommonConfigSource(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		CompileVal.setBusinessCode(null);
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_PUT, request,
				jsonString);
		StringBuffer sb = new StringBuffer();
		String msg = configSourcesService.updateCommonSources(thread, start, jsonString, new Date(), sb);
		if (msg.equals("error")) {
			Exception exception = ConfigSourcesService.getMsgList().get(0);
			if (exception instanceof RestServiceException) {
				throw new RestServiceException(thread, System.currentTimeMillis() - start,
						"回调规则状态更新异常:" + exception.getMessage(), CompileVal.getBusinessCode());
			}else {
				throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
						"回调规则状态更新异常:" + exception.getMessage(), CompileVal.getBusinessCode());
			}
		}
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response, "回调规则状态更新成功",
				Constants.IS_DEBUG ? jsonString : null);
	}

	@RequestMapping(value = "/cfg/v1/fileUploadSources", method = RequestMethod.POST)
	@ApiOperation(value = "样例文件上传服务接口", httpMethod = "POST", response = Map.class, notes = "样例文件上传服务")
	@ApiParam(value = "样例文件上传服务", name = "MultipartFile", required = true)
	public Map fileUploadSource(@RequestBody MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		ConfigSourcesService.setMsgList(new ArrayList<Exception>());// 清除上次记录的日志信息
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				null);
		String filePath = "";
		try {
			FileDesc fileDesc = (FileDesc) JSONObject.toBean(JSONObject.fromObject(request.getHeader("File-Desc")),
					FileDesc.class);
			if (null == file) {
				thread.setExceptionInfo("请选择上传文件到file参数");
				throw new RestServiceException(thread, System.currentTimeMillis() - start, "请选择上传文件到file参数",
						RestBusinessCode.FileIsNull.getValue());

			}
			if (!StringUtil.isEmpty(fileDesc.getChecksum())) {
				// 验证Md5
				String md5 = DigestUtils.md5Hex(file.getInputStream());
				// String md5 = DigestUtils.md5Hex(file.getBytes());
				System.out.println("----------------------------MD5:'" + md5 + "'==='" + fileDesc.getChecksum() + "'");
				if (!md5.equals(fileDesc.getChecksum())) {
					thread.setExceptionInfo("checksum与文件MD5值不一致");
					throw new RestServiceException(thread, System.currentTimeMillis() - start, "checksum与文件MD5值不一致",
							RestBusinessCode.CheckSumIsWrong.getValue());
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
		}catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(RestBusinessCode.FileUploadFailure.getErrorReason()+":"+e.getMessage());
			throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
					RestBusinessCode.FileUploadFailure.getErrorReason()+":"+ e.getMessage(), RestBusinessCode.FileUploadFailure.getValue());
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			if (e instanceof RestServiceException) {
				throw new RestServiceException(thread, System.currentTimeMillis() - start,
					e.getMessage(), ((RestServiceException) e).getErrorCode());
			}else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
					e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			}else {
				throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
					e.getMessage(), RestBusinessCode.FileUploadFailure.getValue());
			}
			
		}
		JSONObject jsonObj = new JSONObject();
		// jsonObj.put("accessUrl", filePath.substring(filePath.indexOf("group")));
		jsonObj.put("accessUrl", filePath);
		return compileServiceResponse(thread, System.currentTimeMillis() - start, request, response, "文件上传成功", jsonObj);
	}

	@RequestMapping(value = "/cfg/v1/fileDigestSources", method = RequestMethod.POST)
	@ApiOperation(value = "文件摘要获取接口", httpMethod = "POST", response = Map.class, notes = "文件摘要获取")
	@ApiParam(value = "摘要文件", name = "MultipartFile", required = true)
	public Map fileDigestSources(@RequestBody MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				file, null);
		if (file == null) {
			thread.setExceptionInfo("请上传获取摘要的文件到file");
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "请上传获取摘要的文件到file参数",
					RestBusinessCode.FileIsNull.getValue());
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
							RestBusinessCode.CheckSumIsWrong.getValue());
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
				resultObject.put("accessUrl", filePath);
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(RestBusinessCode.FileUploadFailure.getErrorReason()+":"+e.getMessage());
			throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
					RestBusinessCode.FileUploadFailure.getErrorReason()+":"+ e.getMessage(), RestBusinessCode.FileUploadFailure.getValue());
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
			if (e instanceof RestServiceException) {
				throw new RestServiceException(thread, System.currentTimeMillis() - start,
					e.getMessage(), ((RestServiceException) e).getErrorCode());
			}else if (e instanceof ServiceRuntimeException) {
				throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
					e.getMessage(), ((ServiceRuntimeException) e).getErrorCode());
			}else {
				throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start,
					e.getMessage(), RestBusinessCode.FileUploadFailure.getValue());
			}
			
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
			logger.error(RestBusinessCode.GetFileDigestFailure.getValue()+":"+e.getMessage()+",请检查摘要获取工具是否安装成功");
//			thread.setExceptionInfo("摘要获取过程中出现异常");
			throw new ServiceRuntimeException(thread, System.currentTimeMillis() - start, RestBusinessCode.GetFileDigestFailure.getValue()+":"+e.getMessage()+",请检查摘要获取工具是否安装成功",
					RestBusinessCode.GetFileDigestFailure.getValue());
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
