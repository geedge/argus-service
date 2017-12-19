/**   
* @Title: DfStatLogService.java 
* @Package com.nis.web.service.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月13日 上午11:50:12  
* @version V1.0   
*/
package com.nis.web.service.restful;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DfAttrStatLogDaily;
import com.nis.domain.restful.DfAttrStatLogMonth;
import com.nis.domain.restful.DfDestIpCounrtyStatLogDaily;
import com.nis.domain.restful.DfDestIpCounrtyStatLogMonth;
import com.nis.domain.restful.DfEntrStatLogDaily;
import com.nis.domain.restful.DfEntrStatLogMonth;
import com.nis.domain.restful.DfLwhhStatLogDaily;
import com.nis.domain.restful.DfLwhhStatLogMonth;
import com.nis.domain.restful.DfSrcIpDomesticStatLogDaily;
import com.nis.domain.restful.DfSrcIpDomesticStatLogMonth;
import com.nis.domain.restful.DfStatLogDaily;
import com.nis.domain.restful.DfStatLogMonth;
import com.nis.domain.restful.DfTagStatLogDaily;
import com.nis.domain.restful.DfTagStatLogMonth;
import com.nis.web.dao.DfStatLogDao;
import com.nis.web.service.BaseLogService;

/** 
* @ClassName: DfStatLogService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (ddm)  
* @date 2016年9月13日 上午11:50:12 
* @version V1.0 
*/
@Service
public class DfStatLogService extends BaseLogService{
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DfStatLogDao dao;
	
	/**
	 * 查询端口封堵分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfStatLogDaily> dfStatLogDaily(Page<DfStatLogDaily> page, DfStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询端口封堵分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfStatLogMonth> dfStatLogMonth(Page<DfStatLogMonth> page, DfStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询标签日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfTagStatLogDaily> dfTagStatLogDaily(Page<DfTagStatLogDaily> page, DfTagStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfTagStatLogDaily(entity));
		return page;
	}
	/**
	 *  查询标签月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfTagStatLogMonth> dfTagStatLogMonth(Page<DfTagStatLogMonth> page, DfTagStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfTagStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询性质日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfAttrStatLogDaily> dfAttrStatLogDaily(Page<DfAttrStatLogDaily> page, DfAttrStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfAttrStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询性质月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfAttrStatLogMonth> dfAttrStatLogMonth(Page<DfAttrStatLogMonth> page, DfAttrStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfAttrStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询局点日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfEntrStatLogDaily> dfEntrStatLogDaily(Page<DfEntrStatLogDaily> page, DfEntrStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfEntrStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询局点月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfEntrStatLogMonth> dfEntrStatLogMonth(Page<DfEntrStatLogMonth> page, DfEntrStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfEntrStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询来文函号日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfLwhhStatLogDaily> dfLwhhStatLogDaily(Page<DfLwhhStatLogDaily> page, DfLwhhStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfLwhhStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询来文函号月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfLwhhStatLogMonth> dfLwhhStatLogMonth(Page<DfLwhhStatLogMonth> page, DfLwhhStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfLwhhStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询境内源ip日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfSrcIpDomesticStatLogDaily> dfSrcIpDomesticStatLogDaily(Page<DfSrcIpDomesticStatLogDaily> page, DfSrcIpDomesticStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfSrcIpDomesticStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询境内源ip月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfSrcIpDomesticStatLogMonth> dfSrcIpDomesticStatLogMonth(Page<DfSrcIpDomesticStatLogMonth> page, DfSrcIpDomesticStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfSrcIpDomesticStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询国家目的ip日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfDestIpCounrtyStatLogDaily> dfDestIpCounrtyStatLogDaily(Page<DfDestIpCounrtyStatLogDaily> page, DfDestIpCounrtyStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfDestIpCounrtyStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询国家目的ip月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DfDestIpCounrtyStatLogMonth> dfDestIpCounrtyStatLogMonth(Page<DfDestIpCounrtyStatLogMonth> page, DfDestIpCounrtyStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.dfDestIpCounrtyStatLogMonth(entity));
		return page;
	}
	
}
