package com.nis.web.dao;

import java.util.List;

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

@MyBatisDao
public interface DjJitLogSearchDao {

	List<DjJitFlSrcReport> findDjJitFlSrcReport(DjJitFlSrcReport djJitFlSrcReportSources);

	List<DjJitFlDestReport> findDjJitFlDestReport(DjJitFlDestReport djJitFlDestReport);

	List<DjJitAffairSrcReport> findDjJitAffairSrcReport(DjJitAffairSrcReport djJitAffairSrcReport);

	List<DjJitAffairDestReport> findDjJitAffairDestReport(DjJitAffairDestReport djJitAffairDestReport);

	List<DjJitMissionSrcReport> findDjJitMissionSrcReport(DjJitMissionSrcReport djJitMissionSrcReport);

	List<DjJitMissionDestReport> findDjJitMissionDestReport(DjJitMissionDestReport djJitMissionDestReport);

	List<DjJitGuaranteeSrcReport> findDjJitGuaranteeSrcReport(DjJitGuaranteeSrcReport djJitGuaranteeSrcReport);

	List<DjJitGuaranteeDestReport> findDjJitGuaranteeDestReport(DjJitGuaranteeDestReport djJitGuaranteeDestReport);

	List<DjJitTagSrcReport> findDjJitTagSrcReport(DjJitTagSrcReport djJitTagSrcReport);

	List<DjJitTagDestReport> findDjJitTagDestReport(DjJitTagDestReport djJitTagDestReport);

	List<DjJitIdSrcReport> findDjJitIdSrcReport(DjJitIdSrcReport djJitIdSrcReport);

	List<DjJitIdDestReport> findDjJitIdDestReport(DjJitIdDestReport djJitIdDestReport);

	List<DjCkStatLog> findDjCkStatLog(DjCkStatLog djCkStatLog);

}