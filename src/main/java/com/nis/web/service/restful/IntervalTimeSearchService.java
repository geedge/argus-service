package com.nis.web.service.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.nis.web.service.AuditLogThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DjFlowControlStop;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.web.dao.IntervalTimeSearchDao;
import com.nis.web.service.BaseLogService;

/** 
* @ClassName: IntervalTimeSearchService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (zbc)  
* @date 2016年9月8日下午8:11:58
* @version V1.0 
*/
@Service
public class IntervalTimeSearchService extends BaseLogService {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected final Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 持久层对象
	 */
	@Autowired
	protected IntervalTimeSearchDao dao;

	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjFlowControlStop> findFlowControlStopPage(Page<DjFlowControlStop> page,
			DjFlowControlStop entity) {
		entity.setPage(page);
		page.setList(dao.findFlowControlStopPage(entity));
		return page;
	}
	/**
	 * 流控日志查询条件检查
	 * wx 
	 * @param entity
	 */
	public void queryConditionCheck(AuditLogThread thread, long start, DjFlowControlStop entity, Page page) {
		/*try {
			if (!StringUtil.isBlank(entity.getSearchCfgId())) {
				Long.parseLong(entity.getSearchCfgId());
			}
		} catch (NumberFormatException e) {
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchCfgId参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchCfgId参数错误");
		}*/
		
		try {
			if (!StringUtil.isBlank(entity.getOptStartTime())) {
				sdf.parse(entity.getOptStartTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"optStartTime参数格式错误", RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"optStartTime参数格式错误");
		}
		
		try {
			if (!StringUtil.isBlank(entity.getOptEndTime())) {
				sdf.parse(entity.getOptEndTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"optEndTime参数格式错误", RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"optEndTime参数错误");
		}
		
		try {
			checkCloumnIsExist(thread,start,DjFlowControlStop.class, page);
		} catch (RestServiceException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw e;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "请求参数错误");
		}
		
	}

}
