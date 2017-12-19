package com.nis.web.service.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DfKeyConvertUrl;
import com.nis.domain.restful.DfKeyMailAdd;
import com.nis.domain.restful.DjFlowControlStop;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.web.dao.DfKeyConvertUrlDao;
import com.nis.web.dao.IntervalTimeSearchDao;
import com.nis.web.service.BaseLogService;
import com.nis.web.service.SaveRequestLogThread;

/** 
* @ClassName: DfKeyConvertUrlService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (DDM)  
* @date 2016年9月27日上午10:12:58
* @version V1.0 
*/
@Service
public class DfKeyConvertUrlService extends BaseLogService {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected final Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DfKeyConvertUrlDao dao;

	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfKeyConvertUrl> findFlowControlStopPage(Page<DfKeyConvertUrl> page,
			DfKeyConvertUrl entity) {
		entity.setPage(page);
		page.setList(dao.findDfKeyConvertUrlPage(entity));
		return page;
	}
	
	/**
	 * 关键字业务转换url查询条件检查
	 * wx
	 * @param entity
	 */
	public void queryConditionCheck(SaveRequestLogThread thread,long start,DfKeyConvertUrl entity,Page page) {
		
		try {
			if (!StringUtil.isBlank(entity.getSearchId())) {
				Long.parseLong(entity.getSearchId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchId参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchId参数错误");
		}
		
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
			checkCloumnIsExist(thread,start,DfKeyConvertUrl.class, page);
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
