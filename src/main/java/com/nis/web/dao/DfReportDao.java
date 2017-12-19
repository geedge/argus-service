package com.nis.web.dao;

import java.util.List;

import com.nis.domain.DfReportEntity;
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

/**
 * 
 * @ClassName: DfReportDao
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2016年10月31日上午11:51:26
 * @version V1.0
 */
@MyBatisDao
public interface DfReportDao extends CrudDao<DfReportEntity> {
	List<DfPzReport> findDfPzReport(DfPzReport pz);

	List<DfAttrTypeReport> findAttrTypeReport(DfAttrTypeReport pz);

	List<DfSrcIpDomeSticReport> findSrcIpDomeSticReport(DfSrcIpDomeSticReport pz);

	List<DfDestIpCountryReport> findDestIpCountryReport(DfDestIpCountryReport pz);

	List<DfEntranceReport> findDfEntranceReport(DfEntranceReport pz);

	List<DfLwhhReport> findDfLwhhReport(DfLwhhReport pz);

	List<DfTagReport> findDfTagReportPage(DfTagReport pz);

	List<DfPzReport> findDfPriTagReport(DfPzReport pz);

	List<DfPzReportStat> findDfPzReportStat(DfPzReportStat pz);
	
	List<DfPzReportStat> findDfPzReportStatMinutes(DfPzReportStat pz);

	Long findDfPzReportSum(DfPzReportStat pz);

	List<DfSrcIpReport> findDfSrcIpReport(DfSrcIpReport entity);

	List<DfDestIpReport> findDfDestIpReport(DfDestIpReport entity);

	List<DfServiceReport> findDfServiceReport(DfServiceReport pz);

	List<DfTagReport> findDfTagReport(DfTagReport pz);

}
