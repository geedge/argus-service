package com.nis.web.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nis.domain.Page;
import com.nis.domain.SwaggerLog;
import com.nis.domain.SysLog;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/sys/log")
public class SysLogController extends BaseController {

	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = "list")
	public String list(SysLog sysLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysLog> page = logService.findPage(new Page<SysLog>(request, response), sysLog);
		model.addAttribute("page", page);
		return "/sys/logList";
	}

	@RequiresPermissions("sys:swaggerLogList:view")
	@RequestMapping(value = "swaggerLogList")
	public String swaggerLogList(SwaggerLog swaggerLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		if (swaggerLog.getOpAction() != null && swaggerLog.getOpAction() == 4) {
			swaggerLog.setTableName("services_log_request_log");
		} else {
			swaggerLog.setTableName("services_config_request_log");
		}
		swaggerLog.setRequestContent(StringEscapeUtils.unescapeHtml4(swaggerLog.getRequestContent()));
		Page<SwaggerLog> page = swaggerLogService.findSwaggerLogPage(new Page<SwaggerLog>(request, response),
				swaggerLog);
		model.addAttribute("page", page);
		return "/sys/swaggerLogList";
	}

}
