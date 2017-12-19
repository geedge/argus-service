/**   
* @Title: DjLogSearchDao.java 
* @Package com.nis.web.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （zbc）  
* @date 2016年9月7日 下午2:32:25 
* @version V1.0   
*/
package com.nis.web.dao;

import java.util.List;

import com.nis.domain.LogEntity;
import com.nis.domain.restful.DjDnsLog;
import com.nis.domain.restful.DjFtpLog;
import com.nis.domain.restful.DjHttpKeywordLog;
import com.nis.domain.restful.DjHttpReqLog;
import com.nis.domain.restful.DjHttpResLog;
import com.nis.domain.restful.DjIpPortLog;
import com.nis.domain.restful.DjIpsecLog;
import com.nis.domain.restful.DjL2tpLog;
import com.nis.domain.restful.DjMailLog;
import com.nis.domain.restful.DjOpenvpnLog;
import com.nis.domain.restful.DjPptpLog;
import com.nis.domain.restful.DjSshLog;
import com.nis.domain.restful.DjSslLog;

/** 
* @ClassName: DjLogSearchDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (zbc)  
* @date 2016年9月7日 下午2:32:25 
* @version V1.0
*/
@MyBatisDao
@SuppressWarnings("rawtypes")
public interface DjLogSearchDao extends CrudDao<LogEntity>{

	List<DjIpPortLog> findIpPortLogs(DjIpPortLog log);

	List<DjHttpReqLog> findHttpReqLogs(DjHttpReqLog log);

	List<DjHttpResLog> findHttpResLogs(DjHttpResLog log);

	List<DjHttpKeywordLog> findHttpKeywordLogs(DjHttpKeywordLog log);

	List<DjMailLog> findMailLogs(DjMailLog log);

	List<DjDnsLog> findDnsLogs(DjDnsLog log);

	List<DjFtpLog> findFtpLogs(DjFtpLog log);

	List<DjPptpLog> findPptpLogs(DjPptpLog log);

	List<DjL2tpLog> findL2tpLogs(DjL2tpLog log);

	List<DjIpsecLog> findIpsecLogs(DjIpsecLog log);

	List<DjOpenvpnLog> findOpenvpnLogs(DjOpenvpnLog log);

	List<DjSshLog> findSshLogs(DjSshLog log);

	List<DjSslLog> findSslLogs(DjSslLog log);
}
