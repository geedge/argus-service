package com.nis.web.dao;

import java.util.List;

import com.nis.domain.DfReportEntity;
import com.nis.domain.restful.NtcPzReport;

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
	
}
