package com.nis.web.dao;

import java.util.List;

import com.nis.domain.restful.NtcHttpRecordLog;

@MyBatisDao
public interface NtcHttpRecordLogsDao {
	
	//http泛收  查询
	List<NtcHttpRecordLog> findNtcHttpRecordLogList(NtcHttpRecordLog obj);
}
