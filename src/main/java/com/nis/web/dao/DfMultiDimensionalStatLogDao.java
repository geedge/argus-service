package com.nis.web.dao;

import java.util.List;

import com.nis.domain.LogEntity;
import com.nis.domain.restful.DfLwhhAttrDaily;
import com.nis.domain.restful.DfLwhhAttrMonth;
import com.nis.domain.restful.DfLwhhTagDaily;
import com.nis.domain.restful.DfLwhhTagMonth;
import com.nis.domain.restful.DfSrcIpAttrDaily;
import com.nis.domain.restful.DfSrcIpAttrMonth;
import com.nis.domain.restful.DfSrcIpTagDaily;
import com.nis.domain.restful.DfSrcIpTagMonth;

/**
 * 
 * @ClassName: DfMultiDimensionnalReportDao
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2017年8月4日下午4:46:31 
 * @version V1.0
 */
@MyBatisDao
public interface DfMultiDimensionalStatLogDao extends CrudDao<LogEntity> {
	
	List<DfLwhhAttrDaily> dfLwhhAttrDaily(DfLwhhAttrDaily pz);
	List<DfLwhhAttrMonth> dfLwhhAttrMonth(DfLwhhAttrMonth pz);

	List<DfLwhhTagDaily> dfLwhhTagDaily(DfLwhhTagDaily pz);
	List<DfLwhhTagMonth> dfLwhhTagMonth(DfLwhhTagMonth pz);

	List<DfSrcIpAttrDaily> dfSrcIpAttrDaily(DfSrcIpAttrDaily pz);
	List<DfSrcIpAttrMonth> dfSrcIpAttrMonth(DfSrcIpAttrMonth pz);

	List<DfSrcIpTagDaily> dfSrcIpTagDaily(DfSrcIpTagDaily pz);
	List<DfSrcIpTagMonth> dfSrcIpTagMonth(DfSrcIpTagMonth pz);

	

}
