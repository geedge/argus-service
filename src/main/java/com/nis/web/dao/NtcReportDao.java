package com.nis.web.dao;

import java.util.List;

import com.nis.domain.restful.NtcAttrTypeReport;
import com.nis.domain.restful.NtcDestipCountryReport;
import com.nis.domain.restful.NtcEntranceReport;
import com.nis.domain.restful.NtcLwhhReport;
import com.nis.domain.restful.NtcPzReport;
import com.nis.domain.restful.NtcServiceReport;
import com.nis.domain.restful.NtcSrcipDomesticReport;
import com.nis.domain.restful.NtcTagReport;

/**
 * 
 * @ClassName:NtcReportDao
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月11日 下午5:47:55
 * @version V1.0
 */
@MyBatisDao
public interface NtcReportDao extends CrudDao {
	List<NtcPzReport> findNtcPzReport(NtcPzReport pz);
	List<NtcServiceReport> findNtcServiceReport(NtcServiceReport pz);
	List<NtcTagReport> findNtcTagReport(NtcTagReport pz);
	List<NtcAttrTypeReport> findNtcAttrTypeReport(NtcAttrTypeReport pz);
	List<NtcLwhhReport> findNtcLwhhReport(NtcLwhhReport pz);
	List<NtcSrcipDomesticReport> findNtcSrcipDomesticReport(NtcSrcipDomesticReport pz);
	List<NtcDestipCountryReport> findNtcDestipCountryReport(NtcDestipCountryReport pz);
	List<NtcEntranceReport> findNtcEntranceReport(NtcEntranceReport pz);
}
