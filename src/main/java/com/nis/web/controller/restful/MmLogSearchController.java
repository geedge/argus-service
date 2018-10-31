package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.restful.MmAvIpLog;
import com.nis.domain.restful.MmAvUrlLog;
import com.nis.domain.restful.MmFaceRecognizationLog;
import com.nis.domain.restful.MmFileDigestLog;
import com.nis.domain.restful.MmLogoDetectionLog;
import com.nis.domain.restful.MmPicIpLog;
import com.nis.domain.restful.MmPicUrlLog;
import com.nis.domain.restful.MmPornAudioLevelLog;
import com.nis.domain.restful.MmPornVideoLevelLog;
import com.nis.domain.restful.MmSampleAudioLog;
import com.nis.domain.restful.MmSamplePicLog;
import com.nis.domain.restful.MmSampleVideoLog;
import com.nis.domain.restful.MmSampleVoipLog;
import com.nis.domain.restful.MmSpeakerRecognizationLog;
import com.nis.domain.restful.MmVoipAccountLog;
import com.nis.domain.restful.MmVoipIpLog;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.LogDataService;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.NtcLogService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName:MmLogSearchController
 * @Description:TODO(MM配置命中日志基本服务接口)
 * @author (zdx)
 * @date 2018年7月24日 下午5:27:11
 * @version V1.0
 */

@RestController
@RequestMapping("${servicePath}/log/v1")
@Api(value = "MmLogSearcgController", description = "MM配置命中日志基本服务接口")
public class MmLogSearchController extends BaseRestController {
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@Autowired
	protected NtcLogService ntcLogService;
	@Autowired
	private LogDataService logDataService;

	@RequestMapping(value = "/mmAvIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "音视频IP日志查询", httpMethod = "GET", notes = "对日志功能“音视频IP日志”提供数据基础查询服务")
	public Map<String, ?> mmAvIpLogs(Page page, MmAvIpLog mmAvIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmAvIpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmAvIpLog, MmAvIpLog.class, page);
			logDataService.getData(page, mmAvIpLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "音视频IP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "音视频IP日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmAvUrlLogs", method = RequestMethod.GET)
	@ApiOperation(value = "音视频URL日志查询", httpMethod = "GET", notes = "对日志功能“音视频URL日志”提供数据基础查询服务")
	public Map<String, ?> mmAvUrlLogs(Page page, MmAvUrlLog mmAvUrlLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmAvUrlLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmAvUrlLog, MmAvUrlLog.class, page);
			logDataService.getData(page, mmAvUrlLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "音视频URL日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "音视频URL日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmPicIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "图片IP日志查询", httpMethod = "GET", notes = "对日志功能“图片IP日志”提供数据基础查询服务")
	public Map<String, ?> mmPicIpLogs(Page page, MmPicIpLog mmPicIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmPicIpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmPicIpLog, MmPicIpLog.class, page);
			logDataService.getData(page, mmPicIpLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "图片IP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "图片IP日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmPicUrlLogs", method = RequestMethod.GET)
	@ApiOperation(value = "图片URL日志查询", httpMethod = "GET", notes = "对日志功能“图片URL日志”提供数据基础查询服务")
	public Map<String, ?> mmPicUrlLogs(Page page, MmPicUrlLog mmPicUrlLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmPicUrlLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmPicUrlLog, MmPicUrlLog.class, page);
			logDataService.getData(page, mmPicUrlLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "图片URL日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "图片URL日志检索成功", page, 0);
	}

	@Deprecated
	@RequestMapping(value = "/mmVoipIpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "VoIP IP日志查询", httpMethod = "GET", notes = "对日志功能“VoIP IP日志”提供数据基础查询服务")
	public Map<String, ?> mmVoipIpLogs(Page page, MmVoipIpLog mmVoipIpLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmVoipIpLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmVoipIpLog, MmVoipIpLog.class, page);
			logDataService.getData(page, mmVoipIpLog);

		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "VoIP IP日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "VoIP IP日志检索成功", page,
				0);
	}

	@Deprecated
	@RequestMapping(value = "/mmVoipAccountLogs", method = RequestMethod.GET)
	@ApiOperation(value = "VoIP Account日志查询", httpMethod = "GET", notes = "对日志功能“VoIP Account日志”提供数据基础查询服务")
	public Map<String, ?> mmVoipAccountLogs(Page page, MmVoipAccountLog mmVoipLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmVoipLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmVoipLog, MmVoipAccountLog.class, page);
			logDataService.getData(page, mmVoipLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "VoIP Account日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "VoIP Account日志检索成功",
				page, 0);
	}

	@RequestMapping(value = "/mmSampleAudioLogs", method = RequestMethod.GET)
	@ApiOperation(value = "音频样例日志查询", httpMethod = "GET", notes = "对日志功能“音频样例日志”提供数据基础查询服务")
	public Map<String, ?> mmSampleAudioLogs(Page page, MmSampleAudioLog mmSampleAudioLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmSampleAudioLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSampleAudioLog, MmSampleAudioLog.class, page);
			logDataService.getData(page, mmSampleAudioLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "音频样例日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "音频样例日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmSampleVideoLogs", method = RequestMethod.GET)
	@ApiOperation(value = "视频样例日志查询", httpMethod = "GET", notes = "对日志功能“视频样例日志”提供数据基础查询服务")
	public Map<String, ?> mmSampleVideoLogs(Page page, MmSampleVideoLog mmSampleVideoLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmSampleVideoLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSampleVideoLog, MmSampleVideoLog.class, page);
			logDataService.getData(page, mmSampleVideoLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "视频样例日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "视频样例日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmPornAudioLevelLogs", method = RequestMethod.GET)
	@ApiOperation(value = "音频色情日志查询", httpMethod = "GET", notes = "对日志功能“音频色情日志”提供数据基础查询服务")
	public Map<String, ?> mmPornAudioLevelLogs(Page page, MmPornAudioLevelLog mmPornAudioLevelLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmPornAudioLevelLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmPornAudioLevelLog, MmPornAudioLevelLog.class,
					page);
			logDataService.getData(page, mmPornAudioLevelLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "音频色情日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "音频色情日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmPornVideoLevelLogs", method = RequestMethod.GET)
	@ApiOperation(value = "视频色情日志查询", httpMethod = "GET", notes = "对日志功能“视频色情日志”提供数据基础查询服务")
	public Map<String, ?> mmPornVideoLevelLogs(Page page, MmPornVideoLevelLog mmPornVideoLevelLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmPornVideoLevelLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmPornVideoLevelLog, MmPornVideoLevelLog.class,
					page);
			logDataService.getData(page, mmPornVideoLevelLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "视频色情日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "视频色情日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmSamplePicLogs", method = RequestMethod.GET)
	@ApiOperation(value = "图片样例日志查询", httpMethod = "GET", notes = "对日志功能“图片样例日志”提供数据基础查询服务")
	public Map<String, ?> mmSamplePicLogs(Page page, MmSamplePicLog mmSamplePicLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmSamplePicLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSamplePicLog, MmSamplePicLog.class, page);
			logDataService.getData(page, mmSamplePicLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "图片样例日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "图片样例日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmSampleVoipLogs", method = RequestMethod.GET)
	@ApiOperation(value = "VOIP样例日志查询", httpMethod = "GET", notes = "对日志功能“VOIP样例日志”提供数据基础查询服务")
	public Map<String, ?> mmSampleVoipLogs(Page page, MmSampleVoipLog mmSampleVoipLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmSampleVoipLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSampleVoipLog, MmSampleVoipLog.class, page);
			logDataService.getData(page, mmSampleVoipLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "VOIP样例日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "VOIP样例日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmSpeakerRecognizationLogs", method = RequestMethod.GET)
	@ApiOperation(value = "说话人识别日志查询", httpMethod = "GET", notes = "对日志功能“说话人识别日志”提供数据基础查询服务")
	public Map<String, ?> mmSpeakerRecognizationLogs(Page page, MmSpeakerRecognizationLog mmSpeakerRecognizationLog,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmSpeakerRecognizationLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmSpeakerRecognizationLog,
					MmSpeakerRecognizationLog.class, page);
			logDataService.getData(page, mmSpeakerRecognizationLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "说话人识别日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "说话人识别日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmLogoDetectionLogs", method = RequestMethod.GET)
	@ApiOperation(value = "台标识别日志查询", httpMethod = "GET", notes = "对日志功能“台标识别IP日志”提供数据基础查询服务")
	public Map<String, ?> mmLogoDetectionLogs(Page page, MmLogoDetectionLog mmLogoDetectionLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmLogoDetectionLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmLogoDetectionLog, MmLogoDetectionLog.class,
					page);
			logDataService.getData(page, mmLogoDetectionLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "台标识别日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "台标识别日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmFaceRecognizationLogs", method = RequestMethod.GET)
	@ApiOperation(value = "人脸识别日志查询", httpMethod = "GET", notes = "对日志功能“人脸识别日志”提供数据基础查询服务")
	public Map<String, ?> mmFaceRecognizationLogs(Page page, MmFaceRecognizationLog mmFaceRecognizationLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmFaceRecognizationLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmFaceRecognizationLog,
					MmFaceRecognizationLog.class, page);
			logDataService.getData(page, mmFaceRecognizationLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "人脸识别日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "人脸识别日志检索成功", page, 0);
	}

	@RequestMapping(value = "/mmFileDigestLogs", method = RequestMethod.GET)
	@ApiOperation(value = "文件摘要日志查询", httpMethod = "GET", notes = "对日志功能“文件摘要日志”提供数据基础查询服务")
	public Map<String, ?> mmFileDigestLogs(Page page, MmFileDigestLog mmFileDigestLog, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		AuditLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);

		try {
			resetTime(mmFileDigestLog);
			ntcLogService.queryConditionCheck(auditLogThread, start, mmFileDigestLog, MmFileDigestLog.class, page);
			logDataService.getData(page, mmFileDigestLog);
		} catch (Exception e) {
			e.printStackTrace();
			auditLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(auditLogThread, System.currentTimeMillis() - start, "文件摘要日志检索失败");
			}
			throw ((RestServiceException) e);
		}
		return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "文件摘要日志检索成功", page, 0);
	}

	

}
