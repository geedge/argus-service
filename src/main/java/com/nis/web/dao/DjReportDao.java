package com.nis.web.dao;

import java.util.List;

import com.nis.domain.DfReportEntity;
import com.nis.domain.restful.DjPzReportStat;
import com.nis.domain.restful.DjAttrTypeReport;
import com.nis.domain.restful.DjDestIpCountryReport;
import com.nis.domain.restful.DjEntranceReport;
import com.nis.domain.restful.DjLwhhReport;
import com.nis.domain.restful.DjSrcIpDomeSticReport;
import com.nis.domain.restful.DjTagReport;
import com.nis.domain.restful.DjPzReport;

/**
 * @ClassName: DjReportDao
 * @Description: TODO(一句话描述这个类)
 * @author (ZBC)
 * @date 2016年11月22日下午06:00:00
 * @version V1.0
 */
@SuppressWarnings("rawtypes")
@MyBatisDao
public interface DjReportDao extends CrudDao<DfReportEntity> {

	List<DjPzReport> findDjPzReport(DjPzReport entity);

	List<DjPzReport> findDjPzPrivPage(DjPzReport entity);

	List<DjPzReportStat> findDjPzReportStat(DjPzReportStat pz);
	List<DjPzReportStat> findDjPzReportStatMinutes(DjPzReportStat pz);

	// List<DjPzReport> findDjPzPrivPage(DjPzReport pz);

	Long findDjPzReportSum(DjPzReportStat pz);
	
	
	List<DjAttrTypeReport> findAttrTypeReport(DjAttrTypeReport pz);

	List<DjSrcIpDomeSticReport> findSrcIpDomeSticReport(DjSrcIpDomeSticReport pz);

	List<DjDestIpCountryReport> findDestIpCountryReport(DjDestIpCountryReport pz);

	List<DjEntranceReport> findDjEntranceReport(DjEntranceReport pz);

	List<DjLwhhReport> findDjLwhhReport(DjLwhhReport pz);

	List<DjTagReport> findDjTagReportPage(DjTagReport pz);
}
