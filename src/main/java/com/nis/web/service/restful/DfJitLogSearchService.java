package com.nis.web.service.restful;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DfJitAffairDestReport;
import com.nis.domain.restful.DfJitAffairSrcReport;
import com.nis.domain.restful.DfJitFlDestReport;
import com.nis.domain.restful.DfJitFlSrcReport;
import com.nis.domain.restful.DfJitGuaranteeDestReport;
import com.nis.domain.restful.DfJitGuaranteeSrcReport;
import com.nis.domain.restful.DfJitIdDestReport;
import com.nis.domain.restful.DfJitIdSrcReport;
import com.nis.domain.restful.DfJitMissionDestReport;
import com.nis.domain.restful.DfJitMissionSrcReport;
import com.nis.domain.restful.DfJitTagDestReport;
import com.nis.domain.restful.DfJitTagSrcReport;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.web.dao.DfJitLogSearchDao;
import com.nis.web.service.BaseLogService;
import com.nis.web.service.SaveRequestLogThread;

@Service
public class DfJitLogSearchService extends BaseLogService {

	@Autowired
	private DfJitLogSearchDao dfJitLogSearchDao;

	public Page<DfJitFlSrcReport> findDfJitFlSrcReport(Page<DfJitFlSrcReport> page, DfJitFlSrcReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitFlSrcReport(entity));
		return page;
	}

	public Page<DfJitFlDestReport> findDfJitFlDestReport(Page<DfJitFlDestReport> page, DfJitFlDestReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitFlDestReport(entity));
		return page;
	}

	public Page<DfJitAffairSrcReport> findDfJitAffairSrcReport(Page<DfJitAffairSrcReport> page,
			DfJitAffairSrcReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitAffairSrcReport(entity));
		return page;
	}

	public Page<DfJitAffairDestReport> findDfJitAffairDestReport(Page<DfJitAffairDestReport> page,
			DfJitAffairDestReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitAffairDestReport(entity));
		return page;
	}


	public Page<DfJitMissionSrcReport> findDfJitMissionSrcReport(Page<DfJitMissionSrcReport> page,
			DfJitMissionSrcReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitMissionSrcReport(entity));
		return page;
	}

	public Page<DfJitMissionDestReport> findDfJitMissionDestReport(Page<DfJitMissionDestReport> page,
			DfJitMissionDestReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitMissionDestReport(entity));
		return page;
	}

	public Page<DfJitGuaranteeSrcReport> findDfJitGuaranteeSrcReport(Page<DfJitGuaranteeSrcReport> page,
			DfJitGuaranteeSrcReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitGuaranteeSrcReport(entity));
		return page;
	}

	public Page<DfJitGuaranteeDestReport> findDfJitGuaranteeDestReport(Page<DfJitGuaranteeDestReport> page,
			DfJitGuaranteeDestReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitGuaranteeDestReport(entity));
		return page;
	}

	public Page<DfJitTagSrcReport> findDfJitTagSrcReport(Page<DfJitTagSrcReport> page, DfJitTagSrcReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitTagSrcReport(entity));
		return page;
	}

	public Page<DfJitTagDestReport> findDfJitTagDestReport(Page<DfJitTagDestReport> page, DfJitTagDestReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitTagDestReport(entity));
		return page;
	}

	public Page<DfJitIdSrcReport> findDfJitIdSrcReport(Page<DfJitIdSrcReport> page, DfJitIdSrcReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitIdSrcReport(entity));
		return page;
	}

	public Page<DfJitIdDestReport> findDfJitIdDestReport(Page<DfJitIdDestReport> page, DfJitIdDestReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dfJitLogSearchDao.findDfJitIdDestReport(entity));
		return page;
	}

	public void queryDfJitFlCheck(SaveRequestLogThread thread,long start,String searchFl, String searchXz) {
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
