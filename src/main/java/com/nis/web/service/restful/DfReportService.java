package com.nis.web.service.restful;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DfAttrTypeReport;
import com.nis.domain.restful.DfDestIpCountryReport;
import com.nis.domain.restful.DfDestIpReport;
import com.nis.domain.restful.DfEntranceReport;
import com.nis.domain.restful.DfLwhhReport;
import com.nis.domain.restful.DfPzReport;
import com.nis.domain.restful.DfPzReportStat;
import com.nis.domain.restful.DfServiceReport;
import com.nis.domain.restful.DfSrcIpDomeSticReport;
import com.nis.domain.restful.DfSrcIpReport;
import com.nis.domain.restful.DfTagReport;
import com.nis.web.dao.DfReportDao;
import com.nis.web.service.BaseLogService;

/**
 * 
 * @ClassName: DfReportService
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2016年10月31日上午11:54:46
 * @version V1.0
 */
@Service
public class DfReportService extends BaseLogService {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DfReportDao dao;

	/**
	 * 
	 * 
	 * @Title: findDfPzReportPage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            page
	 * @param @param
	 *            entity
	 * @param @return
	 * @param @throws
	 *            SQLException
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public Page<DfPzReport> findDfPzReportPage(Page<DfPzReport> page, DfPzReport entity) throws SQLException {
		entity.setPage(page);
		page.setList(dao.findDfPzReport(entity));
		return page;
	}

	public Page<DfAttrTypeReport> findAttrTypeReport(Page<DfAttrTypeReport> page, DfAttrTypeReport entity)
			throws SQLException {
		entity.setPage(page);
		List<DfAttrTypeReport> findAttrTypeReport = dao.findAttrTypeReport(entity);
		page.setList(findAttrTypeReport);
		return page;
	}

	public Page<DfSrcIpDomeSticReport> findSrcIpDomeSticReport(Page<DfSrcIpDomeSticReport> page,
			DfSrcIpDomeSticReport entity) throws SQLException {
		entity.setPage(page);
		List<DfSrcIpDomeSticReport> findSrcIpDomeSticReport = dao.findSrcIpDomeSticReport(entity);
		if (null != findSrcIpDomeSticReport && findSrcIpDomeSticReport.size() > 0) {
			for (DfSrcIpDomeSticReport dfSrcIpDomeSticReport : findSrcIpDomeSticReport) {
				if (null != dfSrcIpDomeSticReport.getAsum() && dfSrcIpDomeSticReport.getAsum() != 0) {
					dfSrcIpDomeSticReport.setAbsum(dfSrcIpDomeSticReport.getAsum());
				} else if ((null == dfSrcIpDomeSticReport.getAsum() || dfSrcIpDomeSticReport.getAsum() == 0)
						&& (null != dfSrcIpDomeSticReport.getBsum() && dfSrcIpDomeSticReport.getBsum() != 0)) {
					dfSrcIpDomeSticReport.setAbsum(dfSrcIpDomeSticReport.getBsum());
				} else {
					dfSrcIpDomeSticReport.setAbsum(0l);
				}
			}
		}
		page.setList(findSrcIpDomeSticReport);
		return page;
	}

	public Page<DfDestIpCountryReport> findDestIpCountryReport(Page<DfDestIpCountryReport> page,
			DfDestIpCountryReport entity) throws SQLException {
		entity.setPage(page);
		List<DfDestIpCountryReport> destIpCountryReport = dao.findDestIpCountryReport(entity);
		if (null != destIpCountryReport && destIpCountryReport.size() > 0) {
			for (DfDestIpCountryReport dfDestIpCountryReport : destIpCountryReport) {
				if (null != dfDestIpCountryReport.getAsum() && dfDestIpCountryReport.getAsum() != 0) {
					dfDestIpCountryReport.setAbsum(dfDestIpCountryReport.getAsum());
				} else if ((null == dfDestIpCountryReport.getAsum() || dfDestIpCountryReport.getAsum() == 0)
						&& (null != dfDestIpCountryReport.getBsum() && dfDestIpCountryReport.getBsum() != 0)) {
					dfDestIpCountryReport.setAbsum(dfDestIpCountryReport.getBsum());
				} else {
					dfDestIpCountryReport.setAbsum(0l);
				}
			}
		}
		page.setList(destIpCountryReport);
		return page;
	}

	public Page<DfEntranceReport> findDfEntranceReport(Page<DfEntranceReport> page, DfEntranceReport entity)
			throws SQLException {
		entity.setPage(page);
		List<DfEntranceReport> findDfEntranceReport = dao.findDfEntranceReport(entity);
		if (null != findDfEntranceReport && findDfEntranceReport.size() > 0) {
			for (DfEntranceReport dfEntranceReport : findDfEntranceReport) {
				if (null != dfEntranceReport.getAsum() && dfEntranceReport.getAsum() != 0) {
					dfEntranceReport.setAbsum(dfEntranceReport.getAsum());
				} else if ((null == dfEntranceReport.getAsum() || dfEntranceReport.getAsum() == 0)
						&& (null != dfEntranceReport.getBsum() && dfEntranceReport.getBsum() != 0)) {
					dfEntranceReport.setAbsum(dfEntranceReport.getBsum());
				} else {
					dfEntranceReport.setAbsum(0l);
				}
			}
		}
		page.setList(findDfEntranceReport);
		return page;
	}

	public Page<DfLwhhReport> findDfLwhhReport(Page<DfLwhhReport> page, DfLwhhReport entity) throws SQLException {
		entity.setPage(page);
		List<DfLwhhReport> findDfLwhhReport = dao.findDfLwhhReport(entity);
		if (null != findDfLwhhReport && findDfLwhhReport.size() > 0) {
			for (DfLwhhReport dfLwhhReport : findDfLwhhReport) {
				if (null != dfLwhhReport.getAsum() && dfLwhhReport.getAsum() != 0) {
					dfLwhhReport.setAbsum(dfLwhhReport.getAsum());
				} else if ((null == dfLwhhReport.getAsum() || dfLwhhReport.getAsum() == 0)
						&& (null != dfLwhhReport.getBsum() && dfLwhhReport.getBsum() != 0)) {
					dfLwhhReport.setAbsum(dfLwhhReport.getBsum());
				} else {
					dfLwhhReport.setAbsum(0l);
				}
			}
		}
		page.setList(findDfLwhhReport);
		return page;
	}

	public Page<DfTagReport> findDfTagReport(Page<DfTagReport> page, DfTagReport entity) throws SQLException {
		entity.setPage(page);
		List<DfTagReport> findDfTagReport = dao.findDfTagReport(entity);
		page.setList(findDfTagReport);
		return page;
	}

	public Page<DfPzReport> findDfPriTagReport(Page<DfPzReport> page, DfPzReport entity) throws SQLException {
		entity.setPage(page);
		List<DfPzReport> findDfPzReport = dao.findDfPzReport(entity);
		if (null != findDfPzReport && findDfPzReport.size() > 0) {
			for (DfPzReport dfPzReport : findDfPzReport) {
				if (null != dfPzReport.getAsum() && dfPzReport.getAsum() != 0) {
					dfPzReport.setAbsum(dfPzReport.getAsum());
				} else if ((null == dfPzReport.getAsum() || dfPzReport.getAsum() == 0)
						&& (null != dfPzReport.getBsum() && dfPzReport.getBsum() != 0)) {
					dfPzReport.setAbsum(dfPzReport.getBsum());
				} else {
					dfPzReport.setAbsum(0l);
				}
			}
		}
		page.setList(findDfPzReport);
		return page;
	}

	public Page<DfPzReportStat> findDfPzReportStatPage(Page<DfPzReportStat> page, DfPzReportStat entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dao.findDfPzReportStat(entity));
		return page;
	}
	public Page<DfPzReportStat> findDfPzReportStatMinutesPage(Page<DfPzReportStat> page, DfPzReportStat entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dao.findDfPzReportStatMinutes(entity));
		return page;
	}

	public Long findDfPzReportSumStat(Page<DfPzReportStat> page, DfPzReportStat entity) throws SQLException {
		return dao.findDfPzReportSum(entity);
	}

	/**
	 * @Title: findDfSrcIpReport
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            page
	 * @param @param
	 *            entity
	 * @param @return
	 * @param @throws
	 *            SQLException
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public Page<DfSrcIpReport> findDfSrcIpReport(Page<DfSrcIpReport> page, DfSrcIpReport entity) {
		entity.setPage(page);
		page.setList(dao.findDfSrcIpReport(entity));
		return page;
	}

	/**
	 * @Title: findDfDestIpReport
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            page
	 * @param @param
	 *            entity
	 * @param @return
	 * @param @throws
	 *            SQLException
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public Page<DfDestIpReport> findDfDestIpReport(Page<DfDestIpReport> page, DfDestIpReport entity) {
		entity.setPage(page);
		page.setList(dao.findDfDestIpReport(entity));
		return page;
	}

	/**
	 * 
	 * 
	 * @Title: findDfServiceReportPage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            page
	 * @param @param
	 *            entity
	 * @param @return
	 * @param @throws
	 *            SQLException
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public Page<DfServiceReport> findDfServiceReportPage(Page<DfServiceReport> page, DfServiceReport entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dao.findDfServiceReport(entity));
		return page;
	}

	/**
	 * @Title: findDfTagReportPage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param
	 *            page
	 * @param @param
	 *            entity
	 * @param @return
	 * @param @throws
	 *            SQLException
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public Page<DfTagReport> findDfTagReportPage(Page<DfTagReport> page, DfTagReport entity) throws SQLException {
		entity.setPage(page);
		
		List<DfTagReport> findDfTagReportPage = dao.findDfTagReportPage(entity);
		page.setList(findDfTagReportPage);
		return page;
	}

}
