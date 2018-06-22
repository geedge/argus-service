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
import com.nis.domain.restful.NtcDnsLog;
import com.nis.domain.restful.NtcFtpLog;
import com.nis.domain.restful.NtcHttpLog;
import com.nis.domain.restful.NtcIpLog;
import com.nis.domain.restful.NtcIpsecLog;
import com.nis.domain.restful.NtcL2tpLog;
import com.nis.domain.restful.NtcMailLog;
import com.nis.domain.restful.NtcOpenvpnLog;
import com.nis.domain.restful.NtcPptpLog;
import com.nis.domain.restful.NtcPzReport;
import com.nis.domain.restful.NtcSshLog;
import com.nis.domain.restful.NtcSslLog;
import com.nis.util.Constants;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.LogTestService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

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


    @RequestMapping(value = "/ntcHttpLogs", method = RequestMethod.GET)
    @ApiOperation(value = "HTTP日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-HTTP”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-HTTP”提供数据基础查询服务")
    public Map<String, ?> ntcHttpLogs(Page page, NtcHttpLog ntcHttpLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcHttpLog> ntcHttpLogPage = new Page<>();
        try {
            ntcHttpLogPage = testService.findNtcHttpLogPage(
                    new Page<NtcHttpLog>(request, response, NtcHttpLog.class), ntcHttpLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Http日志检索成功", ntcHttpLogPage, 0);
    }


    @RequestMapping(value = "/ntcDnsLogs", method = RequestMethod.GET)
    @ApiOperation(value = "DNS日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-DNS”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-DNS”提供数据基础查询服务。")
    public Map<String, ?> ntcDnsLogs(Page page, NtcDnsLog ntcDnsLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcDnsLog> ntcDnsLogPage = new Page<>();
        try {
            ntcDnsLogPage = testService.findNtcDnsLogPage(
                    new Page<NtcDnsLog>(request, response, NtcDnsLog.class), ntcDnsLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Dns日志检索成功", ntcDnsLogPage, 0);
    }

    @RequestMapping(value = "/ntcMailLogs", method = RequestMethod.GET)
    @ApiOperation(value = "EMAIL日志查询", httpMethod = "GET", notes = "对应配置为“邮件管理”，存储动作为阻断与监测的命中日志。对日志功能“邮件管理”提供数据基础查询服务。")
    public Map<String, ?> ntcMailLogs(Page page, NtcMailLog ntcMailLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcMailLog> ntcMailLogPage = new Page<>();
        try {
            ntcMailLogPage = testService.findNtcMailLogPage(
                    new Page<NtcMailLog>(request, response, NtcMailLog.class), ntcMailLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Mail日志检索成功", ntcMailLogPage, 0);
    }


    @RequestMapping(value = "/ntcSslLogs", method = RequestMethod.GET)
    @ApiOperation(value = "SSL日志查询", httpMethod = "GET", notes = "对应配置为“网站管理-SSL”，存储动作为阻断与监测的命中日志。对日志功能“网站管理-SSL”提供数据基础查询服务。")
    public Map<String, ?> ntcSslLogs(Page page, NtcSslLog ntcSslLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcSslLog> ntcSslLogPage = new Page<>();
        try {
            ntcSslLogPage = testService.findNtcSslLogPage(
                    new Page<NtcSslLog>(request, response, NtcSslLog.class), ntcSslLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "SSL日志检索成功", ntcSslLogPage, 0);
    }


    @RequestMapping(value = "/ntcPptpLogs", method = RequestMethod.GET)
    @ApiOperation(value = "PPTP日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-PPTP”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理-PPTP”提供数据基础查询服务。")
    public Map<String, ?> ntcPptpLogs(Page page, NtcPptpLog ntcPptpLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcPptpLog> ntcPptpLogPage = new Page<>();
        try {
            ntcPptpLogPage = testService.findNtcPptpLogPage(
                    new Page<NtcPptpLog>(request, response, NtcPptpLog.class), ntcPptpLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "PPTP日志检索成功", ntcPptpLogPage, 0);
    }


    @RequestMapping(value = "/ntcL2tpLogs", method = RequestMethod.GET)
    @ApiOperation(value = "L2TP日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-L2TP”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- L2TP”提供数据基础查询服务。")
    public Map<String, ?> ntcL2tpLogs(Page page, NtcL2tpLog ntcL2tpLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcL2tpLog> ntcL2tpLogPage = new Page<>();
        try {
            ntcL2tpLogPage = testService.findNtcL2tpLogPage(
                    new Page<NtcL2tpLog>(request, response, NtcL2tpLog.class), ntcL2tpLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "L2TP日志检索成功", ntcL2tpLogPage, 0);
    }


    @RequestMapping(value = "/ntcOpenvpnLogs", method = RequestMethod.GET)
    @ApiOperation(value = "OPENVPN日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-OPENVPN”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- OPENVPN”提供数据基础查询服务。")
    public Map<String, ?> ntcOpenvpnLogs(Page page, NtcOpenvpnLog ntcOpenvpnLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcOpenvpnLog> ntcOpenvpnLogPage = new Page<>();
        try {
            ntcOpenvpnLogPage = testService.findNtcOpenvpnLogPage(
                    new Page<NtcOpenvpnLog>(request, response, NtcOpenvpnLog.class), ntcOpenvpnLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "Openvpn日志检索成功", ntcOpenvpnLogPage, 0);
    }


    @RequestMapping(value = "/ntcIpsecLogs", method = RequestMethod.GET)
    @ApiOperation(value = "IPSEC日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-IPSEC”，存储动作为监测的命中日志。对日志功能“隧道管理- IPSEC”提供数据基础查询服务。")
    public Map<String, ?> ntcIpsecLogs(Page page, NtcIpsecLog ntcIpsecLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcIpsecLog> ntcIpsecLogPage = new Page<>();
        try {
            ntcIpsecLogPage = testService.findNtcIpsecLogPage(
                    new Page<NtcIpsecLog>(request, response, NtcIpsecLog.class), ntcIpsecLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "IPSEC日志检索成功", ntcIpsecLogPage, 0);
    }


    @RequestMapping(value = "/ntcSshLogs", method = RequestMethod.GET)
    @ApiOperation(value = "SSH日志查询", httpMethod = "GET", notes = "对应配置为“隧道管理-SSH”，存储动作为阻断与监测的命中日志。对日志功能“隧道管理- SSH”提供数据基础查询服务。")
    public Map<String, ?> ntcSshLogs(Page page, NtcSshLog ntcSshLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcSshLog> ntcSshLogPage = new Page<>();
        try {
            ntcSshLogPage = testService.findNtcSshLogPage(
                    new Page<NtcSshLog>(request, response, NtcSshLog.class), ntcSshLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "SSH日志检索成功", ntcSshLogPage, 0);
    }


    @RequestMapping(value = "/ntcFtpLogs", method = RequestMethod.GET)
    @ApiOperation(value = "FTP日志查询", httpMethod = "GET", notes = "对应配置为“文件传输-FTP”，存储动作为阻断与监测的命中日志。对日志功能“文件传输-FTP”提供数据基础查询服务。")
    public Map<String, ?> ntcFtpLogs(Page page, NtcFtpLog ntcFtpLog, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<NtcFtpLog> ntcFtpLogPage = new Page<>();
        try {
            ntcFtpLogPage = testService.findNtcFtpLogPage(
                    new Page<NtcFtpLog>(request, response, NtcSshLog.class), ntcFtpLog);

        } catch(Exception e) {
            e.printStackTrace();
        }


        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "FTP日志检索成功", ntcFtpLogPage, 0);
    }

    @RequestMapping(value = "/ntcPzReport", method = RequestMethod.GET)
    @ApiOperation(value = "配置日志总量统计", httpMethod = "GET", notes = "配置命中日志数量实时统计报表，对外提供多种数据表现形式，具体可应用于界面配置命中总量业务、配置报表业务等")
    public Map<String, ?> ntcPzReport(Page page, NtcPzReport ntcPzReport, Model model, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        SaveRequestLogThread auditLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<?> ntcPzReportPage = new Page<>();
        try {
        	ntcPzReportPage = testService.findNtcPzReport(new Page<NtcPzReport>(request, response,NtcPzReport.class), ntcPzReport);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return serviceLogResponse(auditLogThread, System.currentTimeMillis() - start, request, "配置总量统计成功", ntcPzReportPage, 0);
    }


}
