package com.nis.web.dao;

import java.util.List;

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

@MyBatisDao
public interface DfJitLogSearchDao {

	List<DfJitFlSrcReport> findDfJitFlSrcReport(DfJitFlSrcReport dfJitFlSrcReportSources);

	List<DfJitFlDestReport> findDfJitFlDestReport(DfJitFlDestReport dfJitFlDestReport);

	List<DfJitAffairSrcReport> findDfJitAffairSrcReport(DfJitAffairSrcReport dfJitAffairSrcReport);

	List<DfJitAffairDestReport> findDfJitAffairDestReport(DfJitAffairDestReport dfJitAffairDestReport);

	List<DfJitMissionSrcReport> findDfJitMissionSrcReport(DfJitMissionSrcReport dfJitMissionSrcReport);

	List<DfJitMissionDestReport> findDfJitMissionDestReport(DfJitMissionDestReport dfJitMissionDestReport);

	List<DfJitGuaranteeSrcReport> findDfJitGuaranteeSrcReport(DfJitGuaranteeSrcReport dfJitGuaranteeSrcReport);

	List<DfJitGuaranteeDestReport> findDfJitGuaranteeDestReport(DfJitGuaranteeDestReport dfJitGuaranteeDestReport);

	List<DfJitTagSrcReport> findDfJitTagSrcReport(DfJitTagSrcReport dfJitTagSrcReport);

	List<DfJitTagDestReport> findDfJitTagDestReport(DfJitTagDestReport dfJitTagDestReport);

	List<DfJitIdSrcReport> findDfJitIdSrcReport(DfJitIdSrcReport dfJitIdSrcReport);

	List<DfJitIdDestReport> findDfJitIdDestReport(DfJitIdDestReport dfJitIdDestReport);

}