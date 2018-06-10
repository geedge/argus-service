package com.nis.web.service.restful;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.Page;
import com.nis.domain.restful.DfIpPortLog;
import com.nis.domain.restful.NtcIpLog;
import com.nis.util.Constants;
import com.nis.web.service.BaseLogService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by darnell on 2018/6/10.
 * 日志测试业务类，用于接口或临时验证测试使用
 */
@Service
public class LogTestService extends BaseLogService {

    protected final Logger logger = Logger.getLogger(this.getClass());


    /**
     * IP地址日志检索测试业务方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcIpLog> findNtcIpLogPage(Page<NtcIpLog> page, NtcIpLog entity) throws Exception{
        List<NtcIpLog> ntcIpLogs = Lists.newArrayList();
        NtcIpLog ntcIpLog = new NtcIpLog();
        ntcIpLog.setId(35L);
        ntcIpLog.setCfgId(531L);
        ntcIpLog.setServiceType(16);
        ntcIpLog.setFoundTime(new Date());
        ntcIpLog.setRecvTime(new Date());
        ntcIpLog.setAddrType(4);
        ntcIpLog.setProtocol("IPv4_TCP");
        ntcIpLog.setClientIp("192.168.10.106");
        ntcIpLog.setClientPort(80);
        ntcIpLog.setServerIp("202.106.0.20");
        ntcIpLog.setServerPort(8443);
        ntcIpLog.setCljIp("10.0.6.240");
        ntcIpLog.setDeviceId(1356981);
        ntcIpLog.setDirection(0);
        ntcIpLog.setStreamType(0);
        ntcIpLog.setEntranceId(21L);
        ntcIpLog.setUserRegion("531");
        ntcIpLog.setClientLocate("本地地址");
        ntcIpLog.setServerLocate("中国 北京 电信宽带光纤");
        ntcIpLogs.add(ntcIpLog);

        ntcIpLog = new NtcIpLog();
        ntcIpLog.setId(36L);
        ntcIpLog.setCfgId(532L);
        ntcIpLog.setServiceType(128);
        ntcIpLog.setFoundTime(new Date());
        ntcIpLog.setRecvTime(new Date());
        ntcIpLog.setAddrType(4);
        ntcIpLog.setProtocol("IPv4_TCP");
        ntcIpLog.setClientIp("192.168.10.106");
        ntcIpLog.setClientPort(80);
        ntcIpLog.setServerIp("202.106.0.20");
        ntcIpLog.setServerPort(8443);
        ntcIpLog.setCljIp("10.0.6.240");
        ntcIpLog.setDeviceId(1356981);
        ntcIpLog.setDirection(0);
        ntcIpLog.setStreamType(0);
        ntcIpLog.setEntranceId(21L);
        ntcIpLog.setUserRegion("531");
        ntcIpLog.setClientLocate("本地地址");
        ntcIpLog.setServerLocate("中国 北京 电信宽带光纤");
        ntcIpLogs.add(ntcIpLog);
        page.setCount(ntcIpLogs.size());

        page.setList(ntcIpLogs);


        return page;
    }




}
