package com.nis.web.service.restful;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DjLwhhAttrReport;
import com.nis.domain.restful.DjLwhhReport;
import com.nis.domain.restful.DjLwhhTagReport;
import com.nis.domain.restful.DjSrcIpAttrReport;
import com.nis.domain.restful.DjSrcIpTagReport;
import com.nis.web.dao.DjMultiDimensionalReportDao;
import com.nis.web.dao.DjReportDao;
import com.nis.web.service.BaseLogService;

/**
 * 
 * @ClassName: DjMultiDimensionsReportService
 * @Description: TODO(dj多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午3:14:07 
 * @version V1.0
 */
@Service
public class DjMultiDimensionalReportService extends BaseLogService {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DjMultiDimensionalReportDao dao;

	
	public Page<DjLwhhAttrReport> findDjLwhhAttrReportPage(Page<DjLwhhAttrReport> page, DjLwhhAttrReport entity) throws SQLException {
		entity.setPage(page);
		List<DjLwhhAttrReport> djLwhhAttrReports = dao.findDjLwhhAttrReport(entity);
		if (null != djLwhhAttrReports && djLwhhAttrReports.size() > 0) {
			for (DjLwhhAttrReport djLwhhAttrReport : djLwhhAttrReports) {
				if (null != djLwhhAttrReport.getAsum() && djLwhhAttrReport.getAsum() != 0) {
					djLwhhAttrReport.setAbsum(djLwhhAttrReport.getAsum());
				} else if ((null == djLwhhAttrReport.getAsum() || djLwhhAttrReport.getAsum() == 0)
						&& (null != djLwhhAttrReport.getBsum() && djLwhhAttrReport.getBsum() != 0)) {
					djLwhhAttrReport.setAbsum(djLwhhAttrReport.getBsum());
				} else {
					djLwhhAttrReport.setAbsum(0l);
				}
			}
		}
		page.setList(djLwhhAttrReports);
		return page;
	}
	
	public Page<DjLwhhTagReport> findDjLwhhTagReportPage(Page<DjLwhhTagReport> page, DjLwhhTagReport entity) throws SQLException {
		entity.setPage(page);
		List<DjLwhhTagReport> djLwhhTagReports = dao.findDjLwhhTagReportPage(entity);
		if (null != djLwhhTagReports && djLwhhTagReports.size() > 0) {
			for (DjLwhhTagReport djLwhhTagReport : djLwhhTagReports) {
				if (null != djLwhhTagReport.getAsum() && djLwhhTagReport.getAsum() != 0) {
					djLwhhTagReport.setAbsum(djLwhhTagReport.getAsum());
				} else if ((null == djLwhhTagReport.getAsum() || djLwhhTagReport.getAsum() == 0)
						&& (null != djLwhhTagReport.getBsum() && djLwhhTagReport.getBsum() != 0)) {
					djLwhhTagReport.setAbsum(djLwhhTagReport.getBsum());
				} else {
					djLwhhTagReport.setAbsum(0l);
				}
			}
		}
		page.setList(djLwhhTagReports);
		return page;
	}
	
	public Page<DjSrcIpAttrReport> findDjSrcIpAttrReportPage(Page<DjSrcIpAttrReport> page, DjSrcIpAttrReport entity) throws SQLException {
		entity.setPage(page);
		List<DjSrcIpAttrReport> djSrcIpAttrReports = dao.findDjSrcIpAttrReportPage(entity);
		if (null != djSrcIpAttrReports && djSrcIpAttrReports.size() > 0) {
			for (DjSrcIpAttrReport djSrcIpAttrReport : djSrcIpAttrReports) {
				if (null != djSrcIpAttrReport.getAsum() && djSrcIpAttrReport.getAsum() != 0) {
					djSrcIpAttrReport.setAbsum(djSrcIpAttrReport.getAsum());
				} else if ((null == djSrcIpAttrReport.getAsum() || djSrcIpAttrReport.getAsum() == 0)
						&& (null != djSrcIpAttrReport.getBsum() && djSrcIpAttrReport.getBsum() != 0)) {
					djSrcIpAttrReport.setAbsum(djSrcIpAttrReport.getBsum());
				} else {
					djSrcIpAttrReport.setAbsum(0l);
				}
			}
		}
		page.setList(djSrcIpAttrReports);
		return page;
	}
	
	public Page<DjSrcIpTagReport> findDjSrcIpTagReportPage(Page<DjSrcIpTagReport> page, DjSrcIpTagReport entity) throws SQLException {
		entity.setPage(page);
		List<DjSrcIpTagReport> djSrcIpTagReports = dao.findDjSrcIpTagReportPage(entity);
		if (null != djSrcIpTagReports && djSrcIpTagReports.size() > 0) {
			for (DjSrcIpTagReport djSrcIpTagReport : djSrcIpTagReports) {
				if (null != djSrcIpTagReport.getAsum() && djSrcIpTagReport.getAsum() != 0) {
					djSrcIpTagReport.setAbsum(djSrcIpTagReport.getAsum());
				} else if ((null == djSrcIpTagReport.getAsum() || djSrcIpTagReport.getAsum() == 0)
						&& (null != djSrcIpTagReport.getBsum() && djSrcIpTagReport.getBsum() != 0)) {
					djSrcIpTagReport.setAbsum(djSrcIpTagReport.getBsum());
				} else {
					djSrcIpTagReport.setAbsum(0l);
				}
			}
		}
		page.setList(djSrcIpTagReports);
		return page;
	}

}
