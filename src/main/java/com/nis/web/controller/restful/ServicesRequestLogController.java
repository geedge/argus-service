package com.nis.web.controller.restful;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.Page;
import com.nis.domain.ServicesRequestLog;
import com.nis.domain.restful.ServicesRequestLogBean;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;

@RestController
//@RequestMapping("${servicePath}")
public class ServicesRequestLogController extends BaseRestController {
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "/log/v1/getAllLog", method = RequestMethod.POST)
	public Map findCompile(ServicesRequestLogBean log, Page page, HttpServletRequest request,
			HttpServletResponse response) {
		Page<ServicesRequestLogBean> queryAllCompile = null;

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_POST, request,
				log);
		try {
			queryAllCompile = servicesRequestLogService
					.getAllLog(new Page<ServicesRequestLogBean>(request, response, ServicesRequestLogBean.class), log);
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "获取系统操作日志失败");
			}
			throw ((RestServiceException) e);
		}

		return testServiceResponse( System.currentTimeMillis() - start, request, response, "获取系统操作日志成功",
				queryAllCompile);
	}

	@RequestMapping(value = "/log/v1/deleteById", method = RequestMethod.GET)
	public String deleteById(String id, HttpServletRequest request, HttpServletResponse response) {
		id = id.replace("[", "");
		id = id.replace("]", "");
		try {
			servicesRequestLogService.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "ok";
	}

	@RequestMapping(value = "/log/v1/deleteAll", method = RequestMethod.GET)
	public String deleteAll(HttpServletRequest request, HttpServletResponse response) {
		try {
			servicesRequestLogService.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "ok";
	}

}
