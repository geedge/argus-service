package com.nis.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcHttpRecordLog;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.web.dao.NtcHttpRecordLogsDao;
import com.zdjizhi.utils.StringUtil;

@Service
public class NtcHttpRecordLogsService extends BaseService{
	
	@Autowired
	protected NtcHttpRecordLogsDao  ntcHttpRecordLogsDao;
	
	//查询 http泛收 日志数据
	public  Page<?>  getNtcHttpRecordLogsList(Page page ,NtcHttpRecordLog entity) {
		if (!StringUtil.isBlank(entity.getSearchEntranceId())) {
			try {
				entity.setEntranceId(Long.valueOf(entity.getSearchEntranceId()));
			} catch (Exception e) {
				throw new RestServiceException("searchEntranceId参数错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
		if (!StringUtil.isBlank(entity.getSearchDeviceId())) {
			try {
				entity.setDeviceId(Integer.valueOf(entity.getSearchDeviceId()));
			} catch (Exception e) {
				throw new RestServiceException("searchDeviceId参数错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
		if (!StringUtil.isBlank(entity.getSearchDPort())) {
			try {
				entity.setdPort(Integer.valueOf(entity.getSearchDPort()));
			} catch (Exception e) {
				throw new RestServiceException("searchDPort参数错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
		if (!StringUtil.isBlank(entity.getSearchSPort())) {
			try {
				entity.setsPort(Integer.valueOf(entity.getSearchSPort()));
			} catch (Exception e) {
				throw new RestServiceException("searchSPort参数错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
		entity.setPage(page);
		List<NtcHttpRecordLog> findNtcHttpRecordLogList = ntcHttpRecordLogsDao.findNtcHttpRecordLogList(entity);
		page.setList(findNtcHttpRecordLogList);
		if (page.getLast() > 100) {
			page.setLast(100);
		}
		return page;
	}
}
