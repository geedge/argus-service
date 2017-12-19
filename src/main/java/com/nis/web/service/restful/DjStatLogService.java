/**   
* @Title: DjStatLogService.java 
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
import com.nis.domain.restful.DjAttrStatLogDaily;
import com.nis.domain.restful.DjAttrStatLogMonth;
import com.nis.domain.restful.DjDestIpCounrtyStatLogDaily;
import com.nis.domain.restful.DjDestIpCounrtyStatLogMonth;
import com.nis.domain.restful.DjEntrStatLogDaily;
import com.nis.domain.restful.DjEntrStatLogMonth;
import com.nis.domain.restful.DjLwhhStatLogDaily;
import com.nis.domain.restful.DjLwhhStatLogMonth;
import com.nis.domain.restful.DjSrcIpDomesticStatLogDaily;
import com.nis.domain.restful.DjSrcIpDomesticStatLogMonth;
import com.nis.domain.restful.DjStatLogDaily;
import com.nis.domain.restful.DjStatLogMonth;
import com.nis.domain.restful.DjTagStatLogDaily;
import com.nis.domain.restful.DjTagStatLogMonth;
import com.nis.web.dao.DjStatLogDao;
import com.nis.web.service.BaseLogService;

/** 
* @ClassName: DjStatLogService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (ddm)  
* @date 2016年9月13日 上午11:50:12 
* @version V1.0 
*/
@Service
public class DjStatLogService extends BaseLogService{
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DjStatLogDao dao;
	
	/**
	 * 查询端口封堵分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjStatLogDaily> djStatLogDaily(Page<DjStatLogDaily> page, DjStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询端口封堵分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjStatLogMonth> djStatLogMonth(Page<DjStatLogMonth> page, DjStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询标签日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjTagStatLogDaily> djTagStatLogDaily(Page<DjTagStatLogDaily> page, DjTagStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djTagStatLogDaily(entity));
		return page;
	}
	/**
	 *  查询标签月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjTagStatLogMonth> djTagStatLogMonth(Page<DjTagStatLogMonth> page, DjTagStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djTagStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询性质日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjAttrStatLogDaily> djAttrStatLogDaily(Page<DjAttrStatLogDaily> page, DjAttrStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djAttrStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询性质月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjAttrStatLogMonth> djAttrStatLogMonth(Page<DjAttrStatLogMonth> page, DjAttrStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djAttrStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询局点日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjEntrStatLogDaily> djEntrStatLogDaily(Page<DjEntrStatLogDaily> page, DjEntrStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djEntrStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询局点月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjEntrStatLogMonth> djEntrStatLogMonth(Page<DjEntrStatLogMonth> page, DjEntrStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djEntrStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询来文函号日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjLwhhStatLogDaily> djLwhhStatLogDaily(Page<DjLwhhStatLogDaily> page, DjLwhhStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djLwhhStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询来文函号月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjLwhhStatLogMonth> djLwhhStatLogMonth(Page<DjLwhhStatLogMonth> page, DjLwhhStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djLwhhStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询境内源ip日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjSrcIpDomesticStatLogDaily> djSrcIpDomesticStatLogDaily(Page<DjSrcIpDomesticStatLogDaily> page, DjSrcIpDomesticStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djSrcIpDomesticStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询境内源ip月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjSrcIpDomesticStatLogMonth> djSrcIpDomesticStatLogMonth(Page<DjSrcIpDomesticStatLogMonth> page, DjSrcIpDomesticStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djSrcIpDomesticStatLogMonth(entity));
		return page;
	}
	/**
	 * 查询国家目的ip日报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjDestIpCounrtyStatLogDaily> djDestIpCounrtyStatLogDaily(Page<DjDestIpCounrtyStatLogDaily> page, DjDestIpCounrtyStatLogDaily entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djDestIpCounrtyStatLogDaily(entity));
		return page;
	}
	/**
	 * 查询国家目的ip月报表
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<DjDestIpCounrtyStatLogMonth> djDestIpCounrtyStatLogMonth(Page<DjDestIpCounrtyStatLogMonth> page, DjDestIpCounrtyStatLogMonth entity) throws SQLException{
		entity.setPage(page);
		page.setList(dao.djDestIpCounrtyStatLogMonth(entity));
		return page;
	}
	
}
