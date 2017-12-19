package com.nis.web.service.restful;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DjCkStatLog;
import com.nis.domain.restful.DjJitAffairDestReport;
import com.nis.domain.restful.DjJitAffairSrcReport;
import com.nis.domain.restful.DjJitFlDestReport;
import com.nis.domain.restful.DjJitFlSrcReport;
import com.nis.domain.restful.DjJitGuaranteeDestReport;
import com.nis.domain.restful.DjJitGuaranteeSrcReport;
import com.nis.domain.restful.DjJitIdDestReport;
import com.nis.domain.restful.DjJitIdSrcReport;
import com.nis.domain.restful.DjJitMissionDestReport;
import com.nis.domain.restful.DjJitMissionSrcReport;
import com.nis.domain.restful.DjJitTagDestReport;
import com.nis.domain.restful.DjJitTagSrcReport;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.web.dao.DjJitLogSearchDao;
import com.nis.web.service.BaseLogService;
import com.nis.web.service.SaveRequestLogThread;

@Service
public class DjJitLogSearchService extends BaseLogService {

	@Autowired
	private DjJitLogSearchDao djJitLogSearchDao;

	public Page<DjJitFlSrcReport> findDjJitFlSrcReport(Page<DjJitFlSrcReport> page, DjJitFlSrcReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitFlSrcReport(entity));
		return page;
	}

	public Page<DjJitFlDestReport> findDjJitFlDestReport(Page<DjJitFlDestReport> page, DjJitFlDestReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitFlDestReport(entity));
		return page;
	}

	public Page<DjJitAffairSrcReport> findDjJitAffairSrcReport(Page<DjJitAffairSrcReport> page,
			DjJitAffairSrcReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitAffairSrcReport(entity));
		return page;
	}

	public Page<DjJitAffairDestReport> findDjJitAffairDestReport(Page<DjJitAffairDestReport> page,
			DjJitAffairDestReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitAffairDestReport(entity));
		return page;
	}

	public Page<DjJitMissionSrcReport> findDjJitMissionSrcReport(Page<DjJitMissionSrcReport> page,
			DjJitMissionSrcReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitMissionSrcReport(entity));
		return page;
	}

	public Page<DjJitMissionDestReport> findDjJitMissionDestReport(Page<DjJitMissionDestReport> page,
			DjJitMissionDestReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitMissionDestReport(entity));
		return page;
	}

	public Page<DjJitGuaranteeSrcReport> findDjJitGuaranteeSrcReport(Page<DjJitGuaranteeSrcReport> page,
			DjJitGuaranteeSrcReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitGuaranteeSrcReport(entity));
		return page;
	}

	public Page<DjJitGuaranteeDestReport> findDjJitGuaranteeDestReport(Page<DjJitGuaranteeDestReport> page,
			DjJitGuaranteeDestReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitGuaranteeDestReport(entity));
		return page;
	}

	public Page<DjJitTagSrcReport> findDjJitTagSrcReport(Page<DjJitTagSrcReport> page, DjJitTagSrcReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitTagSrcReport(entity));
		return page;
	}

	public Page<DjJitTagDestReport> findDjJitTagDestReport(Page<DjJitTagDestReport> page, DjJitTagDestReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitTagDestReport(entity));
		return page;
	}

	public Page<DjJitIdSrcReport> findDjJitIdSrcReport(Page<DjJitIdSrcReport> page, DjJitIdSrcReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitIdSrcReport(entity));
		return page;
	}

	public Page<DjJitIdDestReport> findDjJitIdDestReport(Page<DjJitIdDestReport> page, DjJitIdDestReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjJitIdDestReport(entity));
		return page;
	}

	public Page<DjCkStatLog> findDjCkStatLog(Page<DjCkStatLog> page, DjCkStatLog entity) throws SQLException {
		entity.setPage(page);
		page.setList(djJitLogSearchDao.findDjCkStatLog(entity));
		return page;
	}

	public void queryDjJitFlCheck(SaveRequestLogThread thread,long start,String searchFl, String searchXz) {
		try {
			if (!StringUtil.isBlank(searchFl)) {
				Integer.parseInt(searchFl);
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"searchFl参数格式错误", RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"searchFl参数错误");
		}
		try {
			if (!StringUtil.isBlank(searchXz)) {
				Integer.parseInt(searchXz);
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"searchXz参数格式错误", RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage()+" "+e.getCause());
			throw new RestServiceException(thread,System.currentTimeMillis()-start,"searchXz参数错误");
		}
	}

}
