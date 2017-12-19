package com.nis.web.dao;

import java.util.List;

import com.nis.domain.DfReportEntity;
import com.nis.domain.restful.DjLwhhAttrReport;
import com.nis.domain.restful.DjLwhhTagReport;
import com.nis.domain.restful.DjSrcIpAttrReport;
import com.nis.domain.restful.DjSrcIpTagReport;

/**
 * 
 * @ClassName: DjMultiDimensionnalReportDao
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2017年8月4日下午4:46:31 
 * @version V1.0
 */
@MyBatisDao
public interface DjMultiDimensionalReportDao extends CrudDao<DfReportEntity> {
	
	List<DjLwhhAttrReport> findDjLwhhAttrReport(DjLwhhAttrReport pz);

	List<DjLwhhTagReport> findDjLwhhTagReportPage(DjLwhhTagReport pz);

	List<DjSrcIpAttrReport> findDjSrcIpAttrReportPage(DjSrcIpAttrReport pz);

	List<DjSrcIpTagReport> findDjSrcIpTagReportPage(DjSrcIpTagReport pz);

	

}
