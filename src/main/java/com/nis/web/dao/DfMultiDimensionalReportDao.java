package com.nis.web.dao;

import java.util.List;

import com.nis.domain.DfReportEntity;
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

/**
 * 
 * @ClassName: DfMultiDimensionnalReportDao
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2017年8月4日下午4:46:31 
 * @version V1.0
 */
@MyBatisDao
public interface DfMultiDimensionalReportDao extends CrudDao<DfReportEntity> {
	
	List<DfLwhhAttrReport> findDfLwhhAttrReport(DfLwhhAttrReport pz);

	List<DfLwhhTagReport> findDfLwhhTagReportPage(DfLwhhTagReport pz);

	List<DfSrcIpAttrReport> findDfSrcIpAttrReportPage(DfSrcIpAttrReport pz);

	List<DfSrcIpTagReport> findDfSrcIpTagReportPage(DfSrcIpTagReport pz);

	

}
