/**   
* @Title: DfLogSearchDao.java 
* @Package com.nis.web.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月5日 下午44:6:25 
* @version V1.0   
*/
package com.nis.web.dao;

import java.util.List;


import com.nis.domain.ControlLog;
import com.nis.domain.LogEntity;
import com.nis.domain.restful.DfDnsLog;
import com.nis.domain.restful.DfFtpLog;
import com.nis.domain.restful.DfIpsecLog;
import com.nis.domain.restful.DfL2tpLog;
import com.nis.domain.restful.DfMailLog;
import com.nis.domain.restful.DfOpenvpnLog;
import com.nis.domain.restful.DfPptpLog;
import com.nis.domain.restful.DfSshLog;
import com.nis.domain.restful.DfSslLog;
import com.nis.domain.restful.DfTunnelRandomLog;

/** 
* @ClassName: DfLogSearchDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (ddm)  
* @date 2016年9月5日 下午2:6:25 
* @version V1.0 
*/
@MyBatisDao
public interface DfLogSearchDaoCluster extends CrudDao<LogEntity> {

	List<DfMailLog> findMailLogsCluster(DfMailLog log);
	List<DfDnsLog> findDnsLogsCluster(DfDnsLog log);
	List<DfFtpLog> findFtpLogsCluster(DfFtpLog log);
	List<DfPptpLog> findPptpLogsCluster(DfPptpLog log);
	List<DfL2tpLog> findL2tpLogsCluster(DfL2tpLog log);
	List<DfIpsecLog> findIpsecLogsCluster(DfIpsecLog log);
	List<DfOpenvpnLog> findOpenvpnLogsCluster(DfOpenvpnLog log);
	List<DfSshLog> findSshLogsCluster(DfSshLog log);
	List<DfSslLog> findSslLogsCluster(DfSslLog log);
	List<DfTunnelRandomLog> findTunnelRandomLogsCluster(DfTunnelRandomLog log);
	
}
