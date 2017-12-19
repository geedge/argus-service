package com.nis.web.service.restful;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DjLwhhAttrDaily;
import com.nis.domain.restful.DjLwhhAttrMonth;
import com.nis.domain.restful.DjLwhhTagDaily;
import com.nis.domain.restful.DjLwhhTagMonth;
import com.nis.domain.restful.DjSrcIpAttrDaily;
import com.nis.domain.restful.DjSrcIpAttrMonth;
import com.nis.domain.restful.DjSrcIpTagDaily;
import com.nis.domain.restful.DjSrcIpTagMonth;
import com.nis.web.dao.DjMultiDimensionalStatLogDao;
import com.nis.web.service.BaseLogService;

/**
 * 
 * @ClassName: DjMultiDimensionsReportService
 * @Description: TODO(dj多维实时统计)
 * @author (DDM)
 * @date 2017年8月4日下午3:14:07 
 * @version V1.0
 */
@Service
public class DjMultiDimensionalStatLogService extends BaseLogService {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DjMultiDimensionalStatLogDao dao;

	public Page<DjLwhhAttrDaily> djLwhhAttrDaily(Page<DjLwhhAttrDaily> page, DjLwhhAttrDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djLwhhAttrDaily(entity));
		return page;
	}	
	public Page<DjLwhhAttrMonth> djLwhhAttrMonth(Page<DjLwhhAttrMonth> page, DjLwhhAttrMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djLwhhAttrMonth(entity));
		return page;
	}	
	
	
	public Page<DjLwhhTagDaily> djLwhhTagDaily(Page<DjLwhhTagDaily> page, DjLwhhTagDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djLwhhTagDaily(entity));
		return page;
	}	
	public Page<DjLwhhTagMonth> djLwhhTagMonth(Page<DjLwhhTagMonth> page, DjLwhhTagMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djLwhhTagMonth(entity));
		return page;
	}	
	
	
	public Page<DjSrcIpAttrDaily> djSrcIpAttrDaily(Page<DjSrcIpAttrDaily> page, DjSrcIpAttrDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djSrcIpAttrDaily(entity));
		return page;
	}	
	public Page<DjSrcIpAttrMonth> djSrcIpAttrMonth(Page<DjSrcIpAttrMonth> page, DjSrcIpAttrMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djSrcIpAttrMonth(entity));
		return page;
	}	
	
	
	public Page<DjSrcIpTagDaily> djSrcIpTagDaily(Page<DjSrcIpTagDaily> page, DjSrcIpTagDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djSrcIpTagDaily(entity));
		return page;
	}	
	public Page<DjSrcIpTagMonth> djSrcIpTagMonth(Page<DjSrcIpTagMonth> page, DjSrcIpTagMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djSrcIpTagMonth(entity));
		return page;
	}	

}
