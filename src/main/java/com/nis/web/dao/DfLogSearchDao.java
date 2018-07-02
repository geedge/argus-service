package com.nis.web.dao;

import java.util.List;

import com.nis.domain.LogEntity;
import com.nis.domain.restful.NtcIpLog;

/**
 * 
 * <p>Title: DfLogSearchDao</p>
 * <p>Description: 日志查询dao</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年7月2日 
 *
 */
@MyBatisDao
public interface DfLogSearchDao extends CrudDao<LogEntity> {
	
//	List<NtcIpLog> findNtcIpLog(NtcIpLog log);
	
	

}
