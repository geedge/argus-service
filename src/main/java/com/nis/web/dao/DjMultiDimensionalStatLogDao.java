package com.nis.web.dao;

import java.util.List;

import com.nis.domain.LogEntity;
import com.nis.domain.restful.DjLwhhAttrDaily;
import com.nis.domain.restful.DjLwhhAttrMonth;
import com.nis.domain.restful.DjLwhhTagDaily;
import com.nis.domain.restful.DjLwhhTagMonth;
import com.nis.domain.restful.DjSrcIpAttrDaily;
import com.nis.domain.restful.DjSrcIpAttrMonth;
import com.nis.domain.restful.DjSrcIpTagDaily;
import com.nis.domain.restful.DjSrcIpTagMonth;

/**
 * 
 * @ClassName: DjMultiDimensionnalReportDao
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2017年8月4日下午4:46:31 
 * @version V1.0
 */
@MyBatisDao
public interface DjMultiDimensionalStatLogDao extends CrudDao<LogEntity> {
	
	List<DjLwhhAttrDaily> djLwhhAttrDaily(DjLwhhAttrDaily pz);
	List<DjLwhhAttrMonth> djLwhhAttrMonth(DjLwhhAttrMonth pz);

	List<DjLwhhTagDaily> djLwhhTagDaily(DjLwhhTagDaily pz);
	List<DjLwhhTagMonth> djLwhhTagMonth(DjLwhhTagMonth pz);

	List<DjSrcIpAttrDaily> djSrcIpAttrDaily(DjSrcIpAttrDaily pz);
	List<DjSrcIpAttrMonth> djSrcIpAttrMonth(DjSrcIpAttrMonth pz);

	List<DjSrcIpTagDaily> djSrcIpTagDaily(DjSrcIpTagDaily pz);
	List<DjSrcIpTagMonth> djSrcIpTagMonth(DjSrcIpTagMonth pz);

	

}
