package com.nis.web.service.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.StatLogEntity;
import com.nis.domain.restful.DfDjLogStatistics;
import com.nis.domain.restful.DfDjPzLogStatistics;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.web.dao.DfDjLogStatDao;
import com.nis.web.service.BaseLogService;
import com.nis.web.service.SaveRequestLogThread;

/**
 * @ClassName: DfdjLogStatService
 * @Description: 封堵/监测日志统计查询
 * @author (zbc)
 * @date 2016年11月14日 下午4:00:00
 * @version V1.0
 */
@Service
@SuppressWarnings({ "rawtypes" })
public class DfdjLogStatService extends BaseLogService {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	protected DfDjLogStatDao dao;
	

	public DfDjLogStatistics findDfLogStatistics(DfDjLogStatistics entity) {
		List<DfDjLogStatistics> list = dao.findDfLogStatistics(entity);
		return entity;
	}

	public Page<DfDjPzLogStatistics> findDfPzLogStatistics(Page<DfDjPzLogStatistics> page, DfDjPzLogStatistics entity) {
		entity.setPage(page);
		page.setList(dao.findDfPzLogStatistics(entity));
		return page;
	}
	
	
	public DfDjLogStatistics findDjLogStatistics(DfDjLogStatistics entity) {
		List<DfDjLogStatistics> list = dao.findDjLogStatistics(entity);
		return entity;
	}

	public Page<DfDjPzLogStatistics> findDjPzLogStatistics(Page<DfDjPzLogStatistics> page, DfDjPzLogStatistics entity) {
		entity.setPage(page);
		page.setList(dao.findDjPzLogStatistics(entity));
		return page;
	}
	

	@Override
	public void queryConditionCheck(SaveRequestLogThread thread, long start, StatLogEntity entity, Class clazz, Page page) {
		/*
		 * searchConfigId为多条件格式
		 */
		try {
			if (!StringUtil.isBlank(entity.getSearchStatStartTime())) {
				sdf.parse(entity.getSearchStatStartTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchStatStartTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchStatStartTime参数格式错误");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchStatEndTime())) {
				sdf.parse(entity.getSearchStatEndTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchStatEndTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchStatEndTime参数错误");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchService())) {
				Integer.parseInt(entity.getSearchService());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数错误");
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
