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
import com.nis.domain.restful.DfLwhhAttrReport;
import com.nis.domain.restful.DfLwhhReport;
import com.nis.domain.restful.DfLwhhTagReport;
import com.nis.domain.restful.DfPzReport;
import com.nis.domain.restful.DfPzReportStat;
import com.nis.domain.restful.DfServiceReport;
import com.nis.domain.restful.DfSrcIpAttrReport;
import com.nis.domain.restful.DfSrcIpDomeSticReport;
import com.nis.domain.restful.DfSrcIpReport;
import com.nis.domain.restful.DfSrcIpTagReport;
import com.nis.domain.restful.DfTagReport;
import com.nis.web.dao.DfMultiDimensionalReportDao;
import com.nis.web.dao.DfReportDao;
import com.nis.web.service.BaseLogService;

/**
 * 
 * @ClassName: DfMultiDimensionsReportService
 * @Description: TODO(df多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午3:14:07 
 * @version V1.0
 */
@Service
public class DfMultiDimensionalReportService extends BaseLogService {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DfMultiDimensionalReportDao dao;

	
	public Page<DfLwhhAttrReport> findDfLwhhAttrReportPage(Page<DfLwhhAttrReport> page, DfLwhhAttrReport entity) throws SQLException {
		entity.setPage(page);
		List<DfLwhhAttrReport> dfLwhhAttrReports = dao.findDfLwhhAttrReport(entity);
		if (null != dfLwhhAttrReports && dfLwhhAttrReports.size() > 0) {
			for (DfLwhhAttrReport dfLwhhAttrReport : dfLwhhAttrReports) {
				if (null != dfLwhhAttrReport.getAsum() && dfLwhhAttrReport.getAsum() != 0) {
					dfLwhhAttrReport.setAbsum(dfLwhhAttrReport.getAsum());
				} else if ((null == dfLwhhAttrReport.getAsum() || dfLwhhAttrReport.getAsum() == 0)
						&& (null != dfLwhhAttrReport.getBsum() && dfLwhhAttrReport.getBsum() != 0)) {
					dfLwhhAttrReport.setAbsum(dfLwhhAttrReport.getBsum());
				} else {
					dfLwhhAttrReport.setAbsum(0l);
				}
			}
		}
		page.setList(dfLwhhAttrReports);
		return page;
	}
	
	public Page<DfLwhhTagReport> findDfLwhhTagReportPage(Page<DfLwhhTagReport> page, DfLwhhTagReport entity) throws SQLException {
		entity.setPage(page);
		List<DfLwhhTagReport> dfLwhhTagReports = dao.findDfLwhhTagReportPage(entity);
		if (null != dfLwhhTagReports && dfLwhhTagReports.size() > 0) {
			for (DfLwhhTagReport dfLwhhTagReport : dfLwhhTagReports) {
				if (null != dfLwhhTagReport.getAsum() && dfLwhhTagReport.getAsum() != 0) {
					dfLwhhTagReport.setAbsum(dfLwhhTagReport.getAsum());
				} else if ((null == dfLwhhTagReport.getAsum() || dfLwhhTagReport.getAsum() == 0)
						&& (null != dfLwhhTagReport.getBsum() && dfLwhhTagReport.getBsum() != 0)) {
					dfLwhhTagReport.setAbsum(dfLwhhTagReport.getBsum());
				} else {
					dfLwhhTagReport.setAbsum(0l);
				}
			}
		}
		page.setList(dfLwhhTagReports);
		return page;
	}
	
	public Page<DfSrcIpAttrReport> findDfSrcIpAttrReportPage(Page<DfSrcIpAttrReport> page, DfSrcIpAttrReport entity) throws SQLException {
		entity.setPage(page);
		List<DfSrcIpAttrReport> dfSrcIpAttrReports = dao.findDfSrcIpAttrReportPage(entity);
		if (null != dfSrcIpAttrReports && dfSrcIpAttrReports.size() > 0) {
			for (DfSrcIpAttrReport dfSrcIpAttrReport : dfSrcIpAttrReports) {
				if (null != dfSrcIpAttrReport.getAsum() && dfSrcIpAttrReport.getAsum() != 0) {
					dfSrcIpAttrReport.setAbsum(dfSrcIpAttrReport.getAsum());
				} else if ((null == dfSrcIpAttrReport.getAsum() || dfSrcIpAttrReport.getAsum() == 0)
						&& (null != dfSrcIpAttrReport.getBsum() && dfSrcIpAttrReport.getBsum() != 0)) {
					dfSrcIpAttrReport.setAbsum(dfSrcIpAttrReport.getBsum());
				} else {
					dfSrcIpAttrReport.setAbsum(0l);
				}
			}
		}
		page.setList(dfSrcIpAttrReports);
		return page;
	}
	
	public Page<DfSrcIpTagReport> findDfSrcIpTagReportPage(Page<DfSrcIpTagReport> page, DfSrcIpTagReport entity) throws SQLException {
		entity.setPage(page);
		List<DfSrcIpTagReport> dfSrcIpTagReports = dao.findDfSrcIpTagReportPage(entity);
		if (null != dfSrcIpTagReports && dfSrcIpTagReports.size() > 0) {
			for (DfSrcIpTagReport dfSrcIpTagReport : dfSrcIpTagReports) {
				if (null != dfSrcIpTagReport.getAsum() && dfSrcIpTagReport.getAsum() != 0) {
					dfSrcIpTagReport.setAbsum(dfSrcIpTagReport.getAsum());
				} else if ((null == dfSrcIpTagReport.getAsum() || dfSrcIpTagReport.getAsum() == 0)
						&& (null != dfSrcIpTagReport.getBsum() && dfSrcIpTagReport.getBsum() != 0)) {
					dfSrcIpTagReport.setAbsum(dfSrcIpTagReport.getBsum());
				} else {
					dfSrcIpTagReport.setAbsum(0l);
				}
			}
		}
		page.setList(dfSrcIpTagReports);
		return page;
	}

}
