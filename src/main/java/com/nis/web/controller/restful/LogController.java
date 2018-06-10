package com.nis.web.controller.restful;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcIpLog;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.LogTestService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by darnell on 2018/6/10.
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@Api(value = "LogController", description = "配置命中日志基本服务接口")
public class LogController extends BaseRestController{
    @Autowired
    public LogTestService testService;

    @Autowired
    protected ServicesRequestLogService servicesRequestLogService;

    @RequestMapping(value = "/ntcIpLogs", method = RequestMethod.GET)
    @ApiOperation(value = "IP地址日志查询", httpMethod = "GET", notes = "对应配置为IP地址管理，存储动作为阻断与监测的命中日志。对日志功能IP地址提供数据基础查询服务")
    public Map<String, ?> ntcIpLogs(Page page, NtcIpLog ntcIpLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcIpLog> ntcIpLogPage = new Page<>();
        try {
            ntcIpLogPage = testService.findNtcIpLogPage(
                    new Page<NtcIpLog>(request, response, NtcIpLog.class), ntcIpLog);

        } catch(Exception e) {
            e.printStackTrace();
        }





        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "IP地址日志检索成功", ntcIpLogPage, 0);
    }


}
