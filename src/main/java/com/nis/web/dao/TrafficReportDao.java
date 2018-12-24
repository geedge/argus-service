package com.nis.web.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.NtcAttrTypeReport;
import com.nis.domain.restful.NtcDestipCountryReport;
import com.nis.domain.restful.NtcEntranceReport;
import com.nis.domain.restful.NtcIpRangeReport;
import com.nis.domain.restful.NtcLwhhReport;
import com.nis.domain.restful.NtcPzReport;
import com.nis.domain.restful.NtcRadiusReport;
import com.nis.domain.restful.NtcServiceReport;
import com.nis.domain.restful.NtcSrcipDomesticReport;
import com.nis.domain.restful.NtcTagReport;
import com.nis.domain.restful.NtcURLIpReport;
import com.nis.domain.restful.dashboard.NtcTotalReport;
import com.nis.domain.restful.dashboard.TrafficTransStatistic;

/**
 * 
 * @ClassName:NtcReportDao
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月11日 下午5:47:55
 * @version V1.0
 */
@MyBatisDao
public interface TrafficReportDao extends CrudDao {
	List<NtcPzReport> findNtcPzReport(NtcPzReport pz);

	List<NtcServiceReport> findNtcServiceReport(NtcServiceReport pz);

	List<NtcTagReport> findNtcTagReport(NtcTagReport pz);

	List<NtcAttrTypeReport> findNtcAttrTypeReport(NtcAttrTypeReport pz);

	List<NtcLwhhReport> findNtcLwhhReport(NtcLwhhReport pz);

	List<NtcSrcipDomesticReport> findNtcSrcipDomesticReport(NtcSrcipDomesticReport pz);

	List<NtcDestipCountryReport> findNtcDestipCountryReport(NtcDestipCountryReport pz);

	List<NtcEntranceReport> findNtcEntranceReport(NtcEntranceReport pz);

	List<NtcURLIpReport> findNtcIpURLReport(NtcURLIpReport pz);

	List<NtcRadiusReport> findAccounList(NtcRadiusReport pz);

	List<NtcRadiusReport> findNasIpList(NtcRadiusReport pz);

	List<NtcRadiusReport> findNtcRadiusReport(NtcRadiusReport pz);

	List<NtcIpRangeReport> findNtcIpRangeReport(NtcIpRangeReport pz);

	void insertNtcIpRangeBatch(List<NtcIpRangeReport> list);

	void truncateNtcIpRange();
	
	
	
	NtcTotalReport getMaxReportTime();
	Map getMaxStatTime();
	List<Map> getTotalReportList(@Param("reportTime") Date reportTime);
	List<Map> getNetFlowPortInfoNew(@Param("statTime") Date statTime);
	List<TrafficTransStatistic> getBandwidthTrans(@Param("entranceId") Integer entranceId,@Param("beginDate") String beginDate,@Param("endDate") String endDate,@Param("addrType") String addrType,@Param("transType") Integer transType);
	List<TrafficTransStatistic> getBandwidthTrans2(@Param("beginDate") String beginDate,@Param("endDate") String endDate);
	Map getEntranceMaxReportTime();
	List<NtcEntranceReport> getActionTrans(@Param("beginDate") String beginDate,@Param("endDate") String endDate ,@Param("entranceId") Integer entranceId,@Param("serviceSql") String serviceSql);
	
	
}
