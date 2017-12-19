package com.nis.web.service.restful;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DjPzReportStat;
import com.nis.domain.restful.DjAttrTypeReport;
import com.nis.domain.restful.DjDestIpCountryReport;
import com.nis.domain.restful.DjEntranceReport;
import com.nis.domain.restful.DjLwhhReport;
import com.nis.domain.restful.DjSrcIpDomeSticReport;
import com.nis.domain.restful.DjTagReport;
import com.nis.domain.restful.DjPzReport;
import com.nis.web.dao.DjReportDao;
import com.nis.web.service.BaseLogService;

/**
 * 
 * @ClassName: DjReportService
 * @Description: TODO(一句话描述这个类)
 * @author (ZBC)
 * @date 2016年11月22日下午06:00:00
 * @version V1.0
 */
@Service
public class DjReportService extends BaseLogService {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DjReportDao dao;

	/**
	 * @Title: findDjPzReportPage
	 * @param page
	 * @param djPzReport
	 * @return
	 */
	public Page<DjPzReport> findDjPzReportPage(Page<DjPzReport> page, DjPzReport entity) {
		entity.setPage(page);
		page.setList(dao.findDjPzReport(entity));
		return page;
	}

	public Page<DjPzReportStat> findDjPzReportStatPage(Page<DjPzReportStat> page, DjPzReportStat entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dao.findDjPzReportStat(entity));
		return page;
	}
	public Page<DjPzReportStat> findDjPzReportStatMinutesPage(Page<DjPzReportStat> page, DjPzReportStat entity)
			throws SQLException {
		entity.setPage(page);
		page.setList(dao.findDjPzReportStatMinutes(entity));
		return page;
	}

	public Page<DjPzReport> findDjPzPrivPage(Page<DjPzReport> page, DjPzReport entity) throws SQLException {
		entity.setPage(page);
		List<DjPzReport> findDjPzPrivPage = dao.findDjPzPrivPage(entity);
		if (null != findDjPzPrivPage && findDjPzPrivPage.size() > 0) {
			for (DjPzReport djPzReport : findDjPzPrivPage) {
				if (null != djPzReport.getAsum() && djPzReport.getAsum() != 0) {
					djPzReport.setAbsum(djPzReport.getAsum());
				} else if ((null == djPzReport.getAsum() || djPzReport.getAsum() == 0)
						&& (null != djPzReport.getBsum() && djPzReport.getBsum() != 0)) {
					djPzReport.setAbsum(djPzReport.getBsum());
				} else {
					djPzReport.setAbsum(0l);
				}
			}
		}
		page.setList(findDjPzPrivPage);
		return page;
	}

	public Long findDjPzReportSumStat(Page<DjPzReportStat> page, DjPzReportStat entity) throws SQLException {
		return dao.findDjPzReportSum(entity);
	}
	
	
	
	
	
	public Page<DjAttrTypeReport> findAttrTypeReport(Page<DjAttrTypeReport> page, DjAttrTypeReport entity)
			throws SQLException {
		entity.setPage(page);
		List<DjAttrTypeReport> findAttrTypeReport = dao.findAttrTypeReport(entity);
		page.setList(findAttrTypeReport);
		return page;
	}
	
	
	public Page<DjSrcIpDomeSticReport> findSrcIpDomeSticReport(Page<DjSrcIpDomeSticReport> page,
			DjSrcIpDomeSticReport entity) throws SQLException {
		entity.setPage(page);
		List<DjSrcIpDomeSticReport> findSrcIpDomeSticReport = dao.findSrcIpDomeSticReport(entity);
		if (null != findSrcIpDomeSticReport && findSrcIpDomeSticReport.size() > 0) {
			for (DjSrcIpDomeSticReport djSrcIpDomeSticReport : findSrcIpDomeSticReport) {
				if (null != djSrcIpDomeSticReport.getAsum() && djSrcIpDomeSticReport.getAsum() != 0) {
					djSrcIpDomeSticReport.setAbsum(djSrcIpDomeSticReport.getAsum());
				} else if ((null == djSrcIpDomeSticReport.getAsum() || djSrcIpDomeSticReport.getAsum() == 0)
						&& (null != djSrcIpDomeSticReport.getBsum() && djSrcIpDomeSticReport.getBsum() != 0)) {
					djSrcIpDomeSticReport.setAbsum(djSrcIpDomeSticReport.getBsum());
				} else {
					djSrcIpDomeSticReport.setAbsum(0l);
				}
			}
		}
		page.setList(findSrcIpDomeSticReport);
		return page;
	}

	
	public Page<DjDestIpCountryReport> findDestIpCountryReport(Page<DjDestIpCountryReport> page,
			DjDestIpCountryReport entity) throws SQLException {
		entity.setPage(page);
		List<DjDestIpCountryReport> destIpCountryReport = dao.findDestIpCountryReport(entity);
		if (null != destIpCountryReport && destIpCountryReport.size() > 0) {
			for (DjDestIpCountryReport djDestIpCountryReport : destIpCountryReport) {
				if (null != djDestIpCountryReport.getAsum() && djDestIpCountryReport.getAsum() != 0) {
					djDestIpCountryReport.setAbsum(djDestIpCountryReport.getAsum());
				} else if ((null == djDestIpCountryReport.getAsum() || djDestIpCountryReport.getAsum() == 0)
						&& (null != djDestIpCountryReport.getBsum() && djDestIpCountryReport.getBsum() != 0)) {
					djDestIpCountryReport.setAbsum(djDestIpCountryReport.getBsum());
				} else {
					djDestIpCountryReport.setAbsum(0l);
				}
			}
		}
		page.setList(destIpCountryReport);
		return page;
	}
	
	public Page<DjEntranceReport> findDjEntranceReport(Page<DjEntranceReport> page, DjEntranceReport entity)
			throws SQLException {
		entity.setPage(page);
		List<DjEntranceReport> findDjEntranceReport = dao.findDjEntranceReport(entity);
		if (null != findDjEntranceReport && findDjEntranceReport.size() > 0) {
			for (DjEntranceReport djEntranceReport : findDjEntranceReport) {
				if (null != djEntranceReport.getAsum() && djEntranceReport.getAsum() != 0) {
					djEntranceReport.setAbsum(djEntranceReport.getAsum());
				} else if ((null == djEntranceReport.getAsum() || djEntranceReport.getAsum() == 0)
						&& (null != djEntranceReport.getBsum() && djEntranceReport.getBsum() != 0)) {
					djEntranceReport.setAbsum(djEntranceReport.getBsum());
				} else {
					djEntranceReport.setAbsum(0l);
				}
			}
		}
		page.setList(findDjEntranceReport);
		return page;
	}
	
	public Page<DjLwhhReport> findDjLwhhReport(Page<DjLwhhReport> page, DjLwhhReport entity) throws SQLException {
		entity.setPage(page);
		List<DjLwhhReport> findDjLwhhReport = dao.findDjLwhhReport(entity);
		if (null != findDjLwhhReport && findDjLwhhReport.size() > 0) {
			for (DjLwhhReport djLwhhReport : findDjLwhhReport) {
				if (null != djLwhhReport.getAsum() && djLwhhReport.getAsum() != 0) {
					djLwhhReport.setAbsum(djLwhhReport.getAsum());
				} else if ((null == djLwhhReport.getAsum() || djLwhhReport.getAsum() == 0)
						&& (null != djLwhhReport.getBsum() && djLwhhReport.getBsum() != 0)) {
					djLwhhReport.setAbsum(djLwhhReport.getBsum());
				} else {
					djLwhhReport.setAbsum(0l);
				}
			}
		}
		page.setList(findDjLwhhReport);
		return page;
	}
	
	public Page<DjTagReport> findDjTagReportPage(Page<DjTagReport> page, DjTagReport entity) throws SQLException {
		entity.setPage(page);
		
		List<DjTagReport> findDjTagReportPage = dao.findDjTagReportPage(entity);
		page.setList(findDjTagReportPage);
		return page;
	}

}
