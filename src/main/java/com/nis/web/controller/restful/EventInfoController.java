package com.nis.web.controller.restful;

import com.nis.domain.Page;
import com.nis.domain.restful.EventKeyProtection;
import com.nis.domain.restful.EventMonitorOrBlock;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.Constants;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.restful.EventService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author GouGe
 * @data 2018/12/24 15:21
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@Api(value = "EventInfoController", description = "事件封堵、监测、保障接口")
@SuppressWarnings("all")
public class EventInfoController extends BaseRestController {
    @Autowired
    ServicesRequestLogService servicesRequestLogService;

    @Autowired
    EventService eventService;

    @RequestMapping(value = "/ntcEventKeyProtection", method = RequestMethod.GET)
    @ApiOperation(value = "重点保障事件总览", httpMethod = "GET", notes = "获取重点保障事件信息", response = Map.class)
    public Map<String, ?> getEventKeyProtection(EventKeyProtection eventKeyProtection, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);

        Page<EventKeyProtection> eventKeyProtectionPage = null;
        try {
            eventKeyProtectionPage = eventService.getEventsKeyProtection(eventKeyProtection);
        } catch (Exception e) {
            saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
            e.printStackTrace();
            logger.error(e.getMessage());
            if (!(e instanceof RestServiceException)) {
                throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
                        "获取重点保障事件信息失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
            } else {
                throw ((RestServiceException) e);
            }
        }
        return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "获取重点保障事件信息成功",
                eventKeyProtectionPage, 0);
    }

    @RequestMapping(value = "/ntcEventsMonitorOrBlock", method = RequestMethod.GET)
    @ApiOperation(value = "事件监测、封堵", httpMethod = "GET", notes = "获取事件监测、封堵信息", response = Map.class)
    public Map<String, ?> getEventsMonitorOrBlock(EventMonitorOrBlock eventMonitorOrBlock, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        AuditLogThread saveLogThread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request, null);
        
        int typeId = eventMonitorOrBlock.getReportType();
        String logMark = null;
        if(typeId == 1){
        	logMark = "事件封堵";
        }else if(typeId == 2){
        	logMark = "事件监测";
        }else{
        	logMark = "事件类型未定义、默认查询事件封堵";
        	eventMonitorOrBlock.setReportType(1);
        }
        Page<EventMonitorOrBlock> eventMonitorPage = null;
        try {
            eventMonitorPage = eventService.getEventsMonitorOrBlock(eventMonitorOrBlock,logMark);
        } catch (Exception e) {
            saveLogThread.setExceptionInfo(e.getMessage() + " " + e.getCause());
            e.printStackTrace();
            logger.error(e.getMessage());
            if (!(e instanceof RestServiceException)) {
                throw new ServiceRuntimeException(saveLogThread, System.currentTimeMillis() - start,
                        "获取"+logMark+"信息失败:" + e.getMessage(), RestBusinessCode.service_runtime_error.getValue());
            } else {
                throw ((RestServiceException) e);
            }
        }
        return serviceLogResponse(saveLogThread, System.currentTimeMillis() - start, request, "获取"+logMark+"信息成功",
                eventMonitorPage, 0);
    }
}