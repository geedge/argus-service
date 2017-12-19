package com.nis.web.service.restful;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DfLwhhAttrDaily;
import com.nis.domain.restful.DfLwhhAttrMonth;
import com.nis.domain.restful.DfLwhhTagDaily;
import com.nis.domain.restful.DfLwhhTagMonth;
import com.nis.domain.restful.DfSrcIpAttrDaily;
import com.nis.domain.restful.DfSrcIpAttrMonth;
import com.nis.domain.restful.DfSrcIpTagDaily;
import com.nis.domain.restful.DfSrcIpTagMonth;
import com.nis.web.dao.DfMultiDimensionalStatLogDao;
import com.nis.web.service.BaseLogService;

/**
 * 
 * @ClassName: DfMultiDimensionsReportService
 * @Description: TODO(df多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午3:14:07 
 * @version V1.0
 */
@Service
public class DfMultiDimensionalStatLogService extends BaseLogService {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DfMultiDimensionalStatLogDao dao;

	public Page<DfLwhhAttrDaily> dfLwhhAttrDaily(Page<DfLwhhAttrDaily> page, DfLwhhAttrDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfLwhhAttrDaily(entity));
		return page;
	}	
	public Page<DfLwhhAttrMonth> dfLwhhAttrMonth(Page<DfLwhhAttrMonth> page, DfLwhhAttrMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfLwhhAttrMonth(entity));
		return page;
	}	
	
	
	public Page<DfLwhhTagDaily> dfLwhhTagDaily(Page<DfLwhhTagDaily> page, DfLwhhTagDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfLwhhTagDaily(entity));
		return page;
	}	
	public Page<DfLwhhTagMonth> dfLwhhTagMonth(Page<DfLwhhTagMonth> page, DfLwhhTagMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfLwhhTagMonth(entity));
		return page;
	}	
	
	
	public Page<DfSrcIpAttrDaily> dfSrcIpAttrDaily(Page<DfSrcIpAttrDaily> page, DfSrcIpAttrDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfSrcIpAttrDaily(entity));
		return page;
	}	
	public Page<DfSrcIpAttrMonth> dfSrcIpAttrMonth(Page<DfSrcIpAttrMonth> page, DfSrcIpAttrMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfSrcIpAttrMonth(entity));
		return page;
	}	
	
	
	public Page<DfSrcIpTagDaily> dfSrcIpTagDaily(Page<DfSrcIpTagDaily> page, DfSrcIpTagDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfSrcIpTagDaily(entity));
		return page;
	}	
	public Page<DfSrcIpTagMonth> dfSrcIpTagMonth(Page<DfSrcIpTagMonth> page, DfSrcIpTagMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfSrcIpTagMonth(entity));
		return page;
	}	

}
