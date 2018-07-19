package com.nis.web.service.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DfKeyMailAdd;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.web.dao.DfKeyMailAddDao;
import com.nis.web.service.BaseLogService;
import com.nis.web.service.AuditLogThread;

/**
 * @ClassName: DfKeyMailAddService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (ZBC)
 * @date 2016年11月09日 下午02:25:00
 * @version V1.0
 */

@Service
@SuppressWarnings({ "rawtypes" })
public class DfKeyMailAddService  extends BaseLogService {
	protected final Logger logger = Logger.getLogger(this.getClass());
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	protected DfKeyMailAddDao dao;
	
	public Page<DfKeyMailAdd> findDfKeyMailAddPage(Page<DfKeyMailAdd> page, DfKeyMailAdd entity) {
		entity.setPage(page);
		page.setList(dao.findDfKeyMailAddPage(entity));
		return page;
	}
	

	/**
	 * 查询条件检查
	 */
	public void queryConditionCheck(AuditLogThread thread, long start, DfKeyMailAdd entity, Page page) {
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
			checkCloumnIsExist(thread,start,DfKeyMailAdd.class, page);
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
