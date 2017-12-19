package com.nis.web.service.restful;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DfDjNestLog;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.web.dao.DfDjNestLogDao;
import com.nis.web.service.BaseLogService;
import com.nis.web.service.SaveRequestLogThread;

/**
 * @ClassName: DfDjNestLogService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (zbc)
 * @date 2016年11月11日 下午4:40:00
 * @version V1.0
 */
@Service
public class DfDjNestLogService extends BaseLogService {
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	protected DfDjNestLogDao dao;
	

	public Page<DfDjNestLog> findDfDjNestLogPage(Page<DfDjNestLog> page, DfDjNestLog entity) {
		entity.setPage(page);
		page.setList(dao.findDfDjNestLogPage(entity));
		return page;
	}
	
	public void queryConditionCheck(SaveRequestLogThread thread, long start, DfDjNestLog entity, Class<DfDjNestLog> clazz, Page<?> page) {
		
		if(StringUtil.isBlank(entity.getSearchLayerId())) {
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchLayerId查询参数不能为空",
					RestBusinessCode.missing_args.getValue());
		}

		try {
			checkCloumnIsExist(thread,start,clazz, page);
		} catch (RestServiceException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "请求参数错误");
		}

	}

}
