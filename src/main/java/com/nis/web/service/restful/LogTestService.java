package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.nis.domain.LogEntity;
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
import com.nis.web.service.BaseLogService;

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
        ntcIpLog.setService(16);
        ntcIpLog.setFoundTime(new Date());
        ntcIpLog.setRecvTime(new Date());
        ntcIpLog.setAddrType(4);
        ntcIpLog.setTransProto("IPv4_TCP");
        ntcIpLog.setsIp("192.168.10.106");
        ntcIpLog.setsPort("80");
        ntcIpLog.setdIp("202.106.0.20");
        ntcIpLog.setdPort("8443");
        ntcIpLog.setCapIp("10.0.6.240");
        ntcIpLog.setDeviceId(1356981);
        ntcIpLog.setDirection(0);
        ntcIpLog.setStreamDir(0);
        ntcIpLog.setEntranceId(21L);
        ntcIpLog.setUserRegion("531");
        ntcIpLogs.add(ntcIpLog);

        ntcIpLog = new NtcIpLog();
        ntcIpLog.setId(36L);
        ntcIpLog.setCfgId(532L);
        ntcIpLog.setService(128);
        ntcIpLog.setFoundTime(new Date());
        ntcIpLog.setRecvTime(new Date());
        ntcIpLog.setAddrType(4);
        ntcIpLog.setTransProto("IPv4_TCP");
        ntcIpLog.setsIp("192.168.10.106");
        ntcIpLog.setsPort("80");
        ntcIpLog.setdIp("202.106.0.20");
        ntcIpLog.setdPort("8443");
        ntcIpLog.setCapIp("10.0.6.240");
        ntcIpLog.setDeviceId(1356981);
        ntcIpLog.setDirection(0);
        ntcIpLog.setStreamDir(0);
        ntcIpLog.setEntranceId(21L);
        ntcIpLog.setUserRegion("531");
        ntcIpLogs.add(ntcIpLog);
        page.setCount(ntcIpLogs.size());

        page.setList(ntcIpLogs);


        return page;
    }

    /**
     * http 日志检索测试业务方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcHttpLog> findNtcHttpLogPage(Page<NtcHttpLog> page, NtcHttpLog entity) throws Exception {

        List<NtcHttpLog> ntcHttpLogs = Lists.newArrayList();

        NtcHttpLog ntcHttpLog = new NtcHttpLog();
        ntcHttpLog = (NtcHttpLog) createTestData(ntcHttpLog);
        ntcHttpLog.setId(35L);
        ntcHttpLog.setCfgId(1532L);
        ntcHttpLog.setService(17);
        ntcHttpLog.setUserRegion("1532");
        ntcHttpLog.setUrl("http://jd.com/get/api/json");
        ntcHttpLog.setReqHdrFile("reqHdrFile1532.txt");
        ntcHttpLog.setReqBodyFile("reqBodyFile1532");
        ntcHttpLog.setResHdrFile("resHdrFile1532.txt");
        ntcHttpLog.setResBodyFile("resBodyFile1532"); 
        ntcHttpLogs.add(ntcHttpLog);


        ntcHttpLog = new NtcHttpLog();
        ntcHttpLog = (NtcHttpLog) createTestData(ntcHttpLog);
        ntcHttpLog.setId(36L);
        ntcHttpLog.setCfgId(1533L);
        ntcHttpLog.setService(129);
        ntcHttpLog.setUserRegion("1533");
        ntcHttpLog.setUrl("http://jd.com/get/api/json/abc");
        ntcHttpLog.setReqHdrFile("reqHdrFile1533.txt");
        ntcHttpLog.setReqBodyFile("reqBodyFile1533");
        ntcHttpLog.setResHdrFile("resHdrFile1533.txt");
        ntcHttpLog.setResBodyFile("resBodyFile1533"); 
        ntcHttpLogs.add(ntcHttpLog);


        page.setCount(ntcHttpLogs.size());
        page.setList(ntcHttpLogs);

        return page;
    }

    /**
     * dns 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcDnsLog> findNtcDnsLogPage(Page<NtcDnsLog> page, NtcDnsLog entity) throws Exception {

        List<NtcDnsLog> ntcDnsLogs = Lists.newArrayList();

        NtcDnsLog ntcDnsLog = new NtcDnsLog();
        ntcDnsLog = (NtcDnsLog) createTestData(ntcDnsLog);
        ntcDnsLog.setId(37L);
        ntcDnsLog.setCfgId(2532L);
        ntcDnsLog.setService(18);
        ntcDnsLog.setUserRegion("2532");
        ntcDnsLog.setQr(0);
        ntcDnsLog.setRd(1);
        ntcDnsLog.setRa(0);
        ntcDnsLog.setRr("query");
        ntcDnsLog.setqType(0);
        ntcDnsLog.setQclass(0);
        ntcDnsLog.setOpcode(0);
        ntcDnsLog.setQname("www.sina.com.cn");
        ntcDnsLog.setCname("sina");
        ntcDnsLog.setDnsSub(1);

        ntcDnsLogs.add(ntcDnsLog);


        ntcDnsLog = new NtcDnsLog();
        ntcDnsLog = (NtcDnsLog) createTestData(ntcDnsLog);
        ntcDnsLog.setId(38L);
        ntcDnsLog.setCfgId(2533L);
        ntcDnsLog.setService(130);
        ntcDnsLog.setUserRegion("2533");
        ntcDnsLog.setQr(1);
        ntcDnsLog.setRd(1);
        ntcDnsLog.setRa(0);
        ntcDnsLog.setRr("query");
        ntcDnsLog.setqType(1);
        ntcDnsLog.setQclass(1);
        ntcDnsLog.setOpcode(1);
        ntcDnsLog.setQname("www.sina.com.cn");
        ntcDnsLog.setCname("sina");
        ntcDnsLog.setDnsSub(2);

        ntcDnsLogs.add(ntcDnsLog);


        page.setCount(ntcDnsLogs.size());
        page.setList(ntcDnsLogs);

        return page;
    }

    /**
     * SSL 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcSslLog> findNtcSslLogPage(Page<NtcSslLog> page, NtcSslLog entity) throws Exception {

        List<NtcSslLog> ntcSslLogs = Lists.newArrayList();

        NtcSslLog ntcSslLog = new NtcSslLog();
        ntcSslLog = (NtcSslLog) createTestData(ntcSslLog);
        ntcSslLog.setId(39L);
        ntcSslLog.setCfgId(4532L);
        ntcSslLog.setService(19);
        ntcSslLog.setUserRegion("4532");
        ntcSslLog.setVersion("版本 3");
        ntcSslLog.setSni("*.cnblogs.cn");
        ntcSslLog.setCa("DigCert Inc");
        ntcSslLog.setSan("*.cnblogs.cn");


        ntcSslLogs.add(ntcSslLog);


        ntcSslLog = new NtcSslLog();
        ntcSslLog = (NtcSslLog) createTestData(ntcSslLog);
        ntcSslLog.setId(40L);
        ntcSslLog.setCfgId(4533L);
        ntcSslLog.setService(131);
        ntcSslLog.setUserRegion("4533");
        ntcSslLog.setVersion("版本 3");
        ntcSslLog.setSni("*.cnblogs.cn");
        ntcSslLog.setCa("DigCert Inc");
        ntcSslLog.setSan("*.cnblogs.cn");


        ntcSslLogs.add(ntcSslLog);


        page.setCount(ntcSslLogs.size());
        page.setList(ntcSslLogs);

        return page;
    }


    /**
     *  Mail日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcMailLog> findNtcMailLogPage(Page<NtcMailLog> page, NtcMailLog entity) throws Exception {

        List<NtcMailLog> ntcMailLogs = Lists.newArrayList();

        NtcMailLog ntcMailLog = new NtcMailLog();
        ntcMailLog = (NtcMailLog) createTestData(ntcMailLog);
        ntcMailLog.setId(38L);
        ntcMailLog.setCfgId(3532L);
        ntcMailLog.setService(20);
        ntcMailLog.setUserRegion("3532");
        ntcMailLog.setMailProto("SMTP");
        ntcMailLog.setMailFrom("darnell@163.com");
        ntcMailLog.setMailTo("iamfree@163.com");
        ntcMailLog.setSubject("领导工资问题，涨涨");
        ntcMailLog.setEmlFile("http://127.0.0.1/abc.eml");
        ntcMailLogs.add(ntcMailLog);


        ntcMailLog = new NtcMailLog();
        ntcMailLog = (NtcMailLog) createTestData(ntcMailLog);
        ntcMailLog.setId(39L);
        ntcMailLog.setCfgId(3533L);
        ntcMailLog.setService(132);
        ntcMailLog.setUserRegion("3533");
        ntcMailLog.setMailProto("smtp");
        ntcMailLog.setMailFrom("iamfree@163.com");
        ntcMailLog.setMailTo("darnell@163.com");
        ntcMailLog.setSubject("滚");
        ntcMailLog.setEmlFile("http://127.0.0.1/abc.eml");

        ntcMailLogs.add(ntcMailLog);


        page.setCount(ntcMailLogs.size());
        page.setList(ntcMailLogs);

        return page;
    }


    /**
     * PPTP 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcPptpLog> findNtcPptpLogPage(Page<NtcPptpLog> page, NtcPptpLog entity) throws Exception {

        List<NtcPptpLog> ntcPptpLogs = Lists.newArrayList();

        NtcPptpLog ntcPptpLog = new NtcPptpLog();
        ntcPptpLog = (NtcPptpLog) createTestData(ntcPptpLog);
        ntcPptpLog.setId(41L);
        ntcPptpLog.setCfgId(5532L);
        ntcPptpLog.setService(22);
        ntcPptpLog.setUserRegion("5532");
        ntcPptpLog.setTunnelType(1);
        ntcPptpLog.setEncryptMode(1);



        ntcPptpLogs.add(ntcPptpLog);


        ntcPptpLog = new NtcPptpLog();
        ntcPptpLog = (NtcPptpLog) createTestData(ntcPptpLog);
        ntcPptpLog.setId(42L);
        ntcPptpLog.setCfgId(4533L);
        ntcPptpLog.setService(134);
        ntcPptpLog.setUserRegion("5533");
        ntcPptpLog.setTunnelType(2);
        ntcPptpLog.setEncryptMode(6);



        ntcPptpLogs.add(ntcPptpLog);


        page.setCount(ntcPptpLogs.size());
        page.setList(ntcPptpLogs);

        return page;
    }


    /**
     * L2TP 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcL2tpLog> findNtcL2tpLogPage(Page<NtcL2tpLog> page, NtcL2tpLog entity) throws Exception {

        List<NtcL2tpLog> ntcL2tpLogs = Lists.newArrayList();

        NtcL2tpLog ntcL2tpLog = new NtcL2tpLog();
        ntcL2tpLog = (NtcL2tpLog) createTestData(ntcL2tpLog);
        ntcL2tpLog.setId(43L);
        ntcL2tpLog.setCfgId(6532L);
        ntcL2tpLog.setService(23);
        ntcL2tpLog.setUserRegion("6532");
        ntcL2tpLog.setTunnelType(1);
        ntcL2tpLog.setEncryptMode(1);



        ntcL2tpLogs.add(ntcL2tpLog);
        ntcL2tpLog = new NtcL2tpLog();
        ntcL2tpLog = (NtcL2tpLog) createTestData(ntcL2tpLog);
        ntcL2tpLog.setId(44L);
        ntcL2tpLog.setCfgId(6533L);
        ntcL2tpLog.setService(135);
        ntcL2tpLog.setUserRegion("6533");
        ntcL2tpLog.setTunnelType(2);
        ntcL2tpLog.setEncryptMode(2);



        ntcL2tpLogs.add(ntcL2tpLog);


        page.setCount(ntcL2tpLogs.size());
        page.setList(ntcL2tpLogs);

        return page;
    }

    /**
     *OPENVPN 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcOpenvpnLog> findNtcOpenvpnLogPage(Page<NtcOpenvpnLog> page, NtcOpenvpnLog entity) throws Exception {

        List<NtcOpenvpnLog> ntcOpenvpnLogs = Lists.newArrayList();

        NtcOpenvpnLog ntcOpenvpnLog = new NtcOpenvpnLog();
        ntcOpenvpnLog = (NtcOpenvpnLog) createTestData(ntcOpenvpnLog);
        ntcOpenvpnLog.setId(45L);
        ntcOpenvpnLog.setCfgId(6532L);
        ntcOpenvpnLog.setService(24);
        ntcOpenvpnLog.setUserRegion("6532");
        ntcOpenvpnLog.setVersion("版本1");
        ntcOpenvpnLog.setEncryptMode("AES");
        ntcOpenvpnLog.setHmac(1);
        ntcOpenvpnLog.setTunnelType(1);



        ntcOpenvpnLogs.add(ntcOpenvpnLog);


        ntcOpenvpnLog = new NtcOpenvpnLog();
        ntcOpenvpnLog = (NtcOpenvpnLog) createTestData(ntcOpenvpnLog);
        ntcOpenvpnLog.setId(46L);
        ntcOpenvpnLog.setCfgId(4533L);
        ntcOpenvpnLog.setService(136);
        ntcOpenvpnLog.setUserRegion("6533");
        ntcOpenvpnLog.setVersion("版本1");
        ntcOpenvpnLog.setEncryptMode("MD5");
        ntcOpenvpnLog.setHmac(1);
        ntcOpenvpnLog.setTunnelType(2);



        ntcOpenvpnLogs.add(ntcOpenvpnLog);


        page.setCount(ntcOpenvpnLogs.size());
        page.setList(ntcOpenvpnLogs);

        return page;
    }


    /**
     * IPSEC 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcIpsecLog> findNtcIpsecLogPage(Page<NtcIpsecLog> page, NtcIpsecLog entity) throws Exception {

        List<NtcIpsecLog> ntcIpsecLogs = Lists.newArrayList();

        NtcIpsecLog ntcIpsecLog = new NtcIpsecLog();
        ntcIpsecLog = (NtcIpsecLog) createTestData(ntcIpsecLog);
        ntcIpsecLog.setId(47L);
        ntcIpsecLog.setCfgId(7532L);
        ntcIpsecLog.setService(137);
        ntcIpsecLog.setUserRegion("7532");
        ntcIpsecLog.setExProtocol(1);
        ntcIpsecLog.setIsakmpMode(1);

        ntcIpsecLogs.add(ntcIpsecLog);


        ntcIpsecLog = new NtcIpsecLog();
        ntcIpsecLog = (NtcIpsecLog) createTestData(ntcIpsecLog);

        ntcIpsecLog.setId(48L);
        ntcIpsecLog.setCfgId(7533L);
        ntcIpsecLog.setService(137);
        ntcIpsecLog.setUserRegion("7533");
        ntcIpsecLog.setExProtocol(2);
        ntcIpsecLog.setIsakmpMode(2);




        ntcIpsecLogs.add(ntcIpsecLog);


        page.setCount(ntcIpsecLogs.size());
        page.setList(ntcIpsecLogs);

        return page;
    }

    /**
     * SSH 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcSshLog> findNtcSshLogPage(Page<NtcSshLog> page, NtcSshLog entity) throws Exception {

        List<NtcSshLog> ntcSshLogs = Lists.newArrayList();

        NtcSshLog ntcSshLog = new NtcSshLog();
        ntcSshLog = (NtcSshLog) createTestData(ntcSshLog);
        ntcSshLog.setId(49L);
        ntcSshLog.setCfgId(8532L);
        ntcSshLog.setService(26);
        ntcSshLog.setUserRegion("8532");
        ntcSshLog.setVersion("版本2");
        ntcSshLog.setHostKey("*&SDAEAS");
        ntcSshLog.setHostCookie("87we8wewe888888");
        ntcSshLog.setEncryptMode("SHA-1");
        ntcSshLog.setMac("CRC");
        ntcSshLog.setTunnelType(1);


        ntcSshLogs.add(ntcSshLog);


        ntcSshLog = new NtcSshLog();
        ntcSshLog = (NtcSshLog) createTestData(ntcSshLog);
        ntcSshLog.setId(50L);
        ntcSshLog.setCfgId(8533L);
        ntcSshLog.setService(138);
        ntcSshLog.setUserRegion("8533");
        ntcSshLog.setVersion("版本2");
        ntcSshLog.setHostKey("*&SDAEAS111");
        ntcSshLog.setHostCookie("87we8wewe88228888");
        ntcSshLog.setEncryptMode("SHA-1");
        ntcSshLog.setMac("CRC");
        ntcSshLog.setTunnelType(2);


        ntcSshLogs.add(ntcSshLog);

        page.setCount(ntcSshLogs.size());
        page.setList(ntcSshLogs);

        return page;
    }

    /**
     * FTP 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<NtcFtpLog> findNtcFtpLogPage(Page<NtcFtpLog> page, NtcFtpLog entity) throws Exception {

        List<NtcFtpLog> ntcFtpLogs = Lists.newArrayList();

        NtcFtpLog ntcFtpLog = new NtcFtpLog();
        ntcFtpLog = (NtcFtpLog) createTestData(ntcFtpLog);
        ntcFtpLog.setId(51L);
        ntcFtpLog.setCfgId(9532L);
        ntcFtpLog.setService(21);
        ntcFtpLog.setUserRegion(" 9532");
        ntcFtpLog.setFtpUrl("ftp://127.0.0.1/a/b/c.txt");
        ntcFtpLog.setFtpContent("我能解附件内容");



        ntcFtpLogs.add(ntcFtpLog);


        ntcFtpLog = new NtcFtpLog();
        ntcFtpLog = (NtcFtpLog) createTestData(ntcFtpLog);
        ntcFtpLog.setId(52L);
        ntcFtpLog.setCfgId(9533L);
        ntcFtpLog.setService(133); 
        ntcFtpLog.setUserRegion("9533");
        ntcFtpLog.setFtpUrl("ftp://127.0.0.1/a/b/d.txt");
        ntcFtpLog.setFtpContent("我能解附件内容");

        ntcFtpLogs.add(ntcFtpLog);

        page.setCount(ntcFtpLogs.size());
        page.setList(ntcFtpLogs);

        return page;
    }

    /**
     * FTP 日志检索测试方法
     * @param page
     * @param entity
     * @return
     * @throws Exception
     */
    public Page<?> findNtcPzReport(Page<NtcPzReport> page, NtcPzReport entity) throws Exception {

        

        if ("1".equals(entity.getSearchBusinessType())) {
        	Page<Map> page1 = new Page<Map>();
        	page1.setPageNo(page.getPageNo());
        	page1.setPageSize(page.getPageSize());
        	List<Map> restList = new ArrayList<Map>();
        	Map<String, Long> restMap  = new HashMap<String, Long>();
        	restMap.put("cfgId", 20l);
        	restMap.put("sum", 1000l);
        	restList.add(restMap);
        	Map<String, Long> restMap2  = new HashMap<String, Long>();
        	restMap2.put("cfgId", 30l);
        	restMap2.put("sum", 1001l);
        	restList.add(restMap2);
        	page1.setCount(restList.size());
	        page1.setList(restList);
	        return page1;
        }else if ("2".equals(entity.getSearchBusinessType())){
        	
        	List<NtcPzReport> ntcPzReports = Lists.newArrayList();
        	Date date = new Date();
			NtcPzReport ntcPzReport = new NtcPzReport();
			ntcPzReport.setCfgId(20l);
			ntcPzReport.setSum(400l);
			ntcPzReport.setService(1);
			ntcPzReport.setReportTime(date);
	        ntcPzReports.add(ntcPzReport);
	        
	        NtcPzReport ntcPzReport3 = new NtcPzReport();
			ntcPzReport3.setCfgId(30l);
			ntcPzReport3.setSum(351l);
			ntcPzReport3.setService(1);
			ntcPzReport3.setReportTime(date);
	        ntcPzReports.add(ntcPzReport3);
	        Date date1 = new Date();
	        date1.setMinutes(date1.getMinutes()+5);
	        NtcPzReport ntcPzReport1 = new NtcPzReport();
			ntcPzReport1.setCfgId(20l);
			ntcPzReport1.setSum(400l);
			ntcPzReport1.setService(1);
			ntcPzReport1.setReportTime(date1);
	        ntcPzReports.add(ntcPzReport1);
	        
	        NtcPzReport ntcPzReport4 = new NtcPzReport();
			ntcPzReport4.setCfgId(30l);
			ntcPzReport4.setSum(500l);
			ntcPzReport4.setService(1);
			ntcPzReport4.setReportTime(date1);
	        ntcPzReports.add(ntcPzReport4);
	        
	        Date date2 = new Date();
	        date2.setMinutes(date2.getMinutes()+10);
	        NtcPzReport ntcPzReport2 = new NtcPzReport();
			ntcPzReport2.setCfgId(20l);
			ntcPzReport2.setSum(200l);
			ntcPzReport2.setService(1);
			ntcPzReport2.setReportTime(date2);
	        ntcPzReports.add(ntcPzReport2);
	        
	        NtcPzReport ntcPzReport5 = new NtcPzReport();
			ntcPzReport5.setCfgId(30l);
			ntcPzReport5.setSum(150l);
			ntcPzReport5.setService(1);
			ntcPzReport5.setReportTime(date2);
	        ntcPzReports.add(ntcPzReport5);
	        
	        page.setCount(ntcPzReports.size());
	        page.setList(ntcPzReports);
	        return page;
		}
        
        return null;
        
    }




    private Object createTestData(LogEntity logEntity) {
        logEntity.setTransProto("IPv4_TCP");
        logEntity.setFoundTime(new Date());
        logEntity.setRecvTime(new Date());
        logEntity.setAddrType(4);
        logEntity.setsIp("192.168.10.106");
        logEntity.setsPort("80");
        logEntity.setdIp("202.106.0.20");
        logEntity.setdPort("8443");
        logEntity.setCapIp("10.0.6.240");
        logEntity.setDeviceId(1356981);
        logEntity.setDirection(0);
        logEntity.setStreamDir(0);
        logEntity.setEntranceId(21L);

        return logEntity;

    }







}
