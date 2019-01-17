package com.nis.web.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcSslRecordLog;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.web.dao.NtcSslRecordLogsDao;
import com.zdjizhi.utils.StringUtil;

@Service
public class NtcSslRecordLogsService extends BaseService{
	
	@Autowired
	protected NtcSslRecordLogsDao  ntcSslRecordLogsDao;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//查询 ssl泛收 日志数据
	public  Page<?>  getNtcSslRecordLogsList(Page page ,NtcSslRecordLog entity) throws Exception{
		//把String类型的时间 转换成 时间戳类型
		if (!StringUtil.isBlank(entity.getSearchFoundStartTime())) {
			try {
				entity.setFoundStartTime(sdf.parse(entity.getSearchFoundStartTime().trim()).getTime() / 1000);
			} catch (Exception e) {
				throw new RestServiceException("searchFoundStartTime参数错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
		if (!StringUtil.isBlank(entity.getSearchFoundEndTime())) {
			try {
				entity.setFoundEndTime(sdf.parse(entity.getSearchFoundEndTime().trim()).getTime() / 1000);
			} catch (Exception e) {
				throw new RestServiceException("searchFoundEndTime参数错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
		if (!StringUtil.isBlank(entity.getSearchCfgId())) {
			try {
				entity.setCfgId(Long.valueOf(entity.getSearchCfgId()));
			} catch (Exception e) {
				throw new RestServiceException("searchCfgId参数错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
		if (!StringUtil.isBlank(entity.getSearchEntranceId())) {
			try {
				entity.setEntranceId(Long.valueOf(entity.getSearchEntranceId()));
			} catch (Exception e) {
				throw new RestServiceException("searchEntranceId", RestBusinessCode.param_formate_error.getValue());
			}
		}
		entity.setPage(page);
		List<NtcSslRecordLog> findNtcSslRecordLogList = ntcSslRecordLogsDao.findNtcSslRecordLogList(entity);
		page.setList(findNtcSslRecordLogList);
		return page;
	}
	
	
}
