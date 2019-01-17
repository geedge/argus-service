package com.nis.web.dao;

import java.util.List;

import com.nis.domain.restful.NtcSslRecordLog;

@MyBatisDao
public interface NtcSslRecordLogsDao {
	
	//ssl泛收  查询
	List<NtcSslRecordLog> findNtcSslRecordLogList(NtcSslRecordLog obj);
}
