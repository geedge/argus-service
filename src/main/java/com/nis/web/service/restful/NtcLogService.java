package com.nis.web.service.restful;



import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcConnRecordLog;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.BaseLogService;
import com.zdjizhi.utils.StringUtil;

/**
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月10日 上午9:47:39
 * @version V1.0
 */
@Service
public class NtcLogService extends BaseLogService {

	/**
	 * 验证日志查询条件格式是否正确
	 * 
	 * @param thread
	 * @param start
	 * @param entity
	 * @param clazz
	 * @param page
	 */
	public void ntcConnRecordLogsConditionCheck(AuditLogThread thread, long start, NtcConnRecordLog entity, Class clazz, Page page) {
		logger.info("ntcConnRecordLogsQueryConditionCheck start----" + System.currentTimeMillis());

		queryConditionCheck(thread, start, entity, clazz, page);

		//app protocol website
		if (StringUtil.isNotBlank(entity.getSearchAppId())) {
			String[] split = org.apache.commons.lang.StringUtils.split(entity.getSearchAppId(), ",");
			if (split.length > 10) {
				throw new RestServiceException("searchAppId  max(10)",
						RestBusinessCode.param_formate_error.getValue());
			}
			for (String appId : split) {
				try {
					Long.parseLong(appId);
				} catch (Exception e) {
					throw new RestServiceException("searchAppId error", RestBusinessCode.param_formate_error.getValue());
				}

			}
		}
		if (StringUtil.isNotBlank(entity.getSearchProtoId())) {
			String[] split = org.apache.commons.lang.StringUtils.split(entity.getSearchProtoId(), ",");
			if (split.length > 10) {
				throw new RestServiceException("searchProtoId max(10)",
						RestBusinessCode.param_formate_error.getValue());
			}
			for (String protoId : split) {
				try {
					Long.parseLong(protoId);
				} catch (Exception e) {
					throw new RestServiceException("searchAppId error", RestBusinessCode.param_formate_error.getValue());
				}

			}
		}
		if (StringUtil.isNotBlank(entity.getSearchWebId())) {
			String[] split = org.apache.commons.lang.StringUtils.split(entity.getSearchWebId(), ",");
			if (split.length > 10) {
				throw new RestServiceException("searchWebId  max(10)",
						RestBusinessCode.param_formate_error.getValue());
			}
			for (String appId : split) {
				try {
					Long.parseLong(appId);
				} catch (Exception e) {
					throw new RestServiceException("searchWebId 参数错误", RestBusinessCode.param_formate_error.getValue());
				}

			}
		}


		if (StringUtil.isNotBlank(entity.getSearchIspCode())) {
			String[] split = org.apache.commons.lang.StringUtils.split(entity.getSearchIspCode(), ",");
			if (split.length > 10) {
				throw new RestServiceException("searchIspCode  max(10)",
						RestBusinessCode.param_formate_error.getValue());
			}
			for (String ispCode : split) {
				try {
					Long.parseLong(ispCode);
				} catch (Exception e) {
					throw new RestServiceException("searchIspCode 参数错误", RestBusinessCode.param_formate_error.getValue());
				}

			}
		}


		logger.info("ntcConnRecordLogsQueryConditionCheck end----" + System.currentTimeMillis());

	}
}
