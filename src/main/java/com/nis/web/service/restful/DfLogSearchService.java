/**   
* @Title: DfLogSearchService.java 
* @Package com.nis.web.service.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月15日 下午2:08:12 
* @version V1.0   
*/
package com.nis.web.service.restful;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.LogEntity;
import com.nis.domain.Page;
import com.nis.domain.restful.DfDnsLog;
import com.nis.domain.restful.DfFtpLog;
import com.nis.domain.restful.DfHttpKeywordLog;
import com.nis.domain.restful.DfHttpReqLog;
import com.nis.domain.restful.DfHttpResLog;
import com.nis.domain.restful.DfIpPortLog;
import com.nis.domain.restful.DfIpsecLog;
import com.nis.domain.restful.DfL2tpLog;
import com.nis.domain.restful.DfMailLog;
import com.nis.domain.restful.DfOpenvpnLog;
import com.nis.domain.restful.DfPptpLog;
import com.nis.domain.restful.DfSshLog;
import com.nis.domain.restful.DfSslLog;
import com.nis.domain.restful.DfTunnelRandomLog;
//import com.nis.util.MysqlJDBC;
import com.nis.util.Constants;
import com.nis.web.dao.DfLogSearchDao;
import com.nis.web.dao.DfLogSearchDaoCluster;
import com.nis.web.service.BaseLogService;

/** 
* @ClassName: DfLogSearchService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (ddm)  
* @date 2016年9月5日 下午2:08:12 
* @version V1.0 
*/
@Service
public class DfLogSearchService extends BaseLogService{
	protected final Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 持久层对象
	 */
	@Autowired
	protected DfLogSearchDao dao;
	@Autowired
	protected DfLogSearchDaoCluster daoCluster;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public LogEntity get(Long id) {
		return dao.get(id);
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public LogEntity get(LogEntity entity) {
		return dao.get(entity);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<LogEntity> findList(LogEntity entity) {
		return dao.findList(entity);
	}
	/**
	 * 查询端口封堵分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfIpPortLog> findIpPortPage(Page<DfIpPortLog> page, DfIpPortLog entity,String activeSys) throws Exception{
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfIpPortLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findIpPortLogs(entity));
		}
		
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfHttpReqLog> findHttpReqPage(Page<DfHttpReqLog> page, DfHttpReqLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfHttpReqLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findHttpReqLogs(entity));
		}
		/*if(type.equals("jdbc")){
			logger.info("**********oralce JDBC数据查询开始时间"+System.currentTimeMillis());
			page.setList(MysqlJDBC.queryByCondition(page, entity));
			logger.info("**********oralce JDBC数据查询结束时间"+System.currentTimeMillis());
		}else if(type.equals("es")){
			logger.info("**********es数据查询开始时间"+System.currentTimeMillis());
			List<DfHttpReqLog> dfHttpReqLogs=new ArrayList<DfHttpReqLog>();
			elasticsearchSqlDao.findLogs(dfHttpReqLogs, entity);
			page.setList(dfHttpReqLogs);
			logger.info("**********es数据查询开始时间"+System.currentTimeMillis());
		}else{
			logger.info("**********oralce Mybatis数据查询开始时间"+System.currentTimeMillis());
			page.setList(dao.findHttpReqLogs(entity));
			logger.info("**********oralce Mybatis数据查询开始结束"+System.currentTimeMillis());
		}*/
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfHttpResLog> findHttpResPage(Page<DfHttpResLog> page, DfHttpResLog entity,String activeSys) throws Exception{
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfHttpResLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findHttpResLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfHttpKeywordLog> findHttpKeywordPage(Page<DfHttpKeywordLog> page, DfHttpKeywordLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfHttpKeywordLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findHttpKeywordLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @throws Exception 
	 */
	public Page<DfHttpKeywordLog> findHttpMultiPartPage(Page<DfHttpKeywordLog> page, DfHttpKeywordLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfHttpKeywordLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findHttpKeywordLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfMailLog> findMailPage(Page<DfMailLog> page, DfMailLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfMailLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findMailLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfDnsLog> findDnsPage(Page<DfDnsLog> page, DfDnsLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfDnsLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findDnsLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfFtpLog> findFtpPage(Page<DfFtpLog> page, DfFtpLog entity,String activeSys) throws SQLException, IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfFtpLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findFtpLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfPptpLog> findPptpPage(Page<DfPptpLog> page, DfPptpLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfPptpLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findPptpLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfL2tpLog> findL2tpPage(Page<DfL2tpLog> page, DfL2tpLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfL2tpLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findL2tpLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfIpsecLog> findIpsecPage(Page<DfIpsecLog> page, DfIpsecLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfIpsecLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findIpsecLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfOpenvpnLog> findOpenvpnPage(Page<DfOpenvpnLog> page, DfOpenvpnLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfOpenvpnLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findOpenvpnLogs(entity));
		}
		return page;
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfSshLog> findSshPage(Page<DfSshLog> page, DfSshLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfSshLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findSshLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfSslLog> findSslPage(Page<DfSslLog> page, DfSslLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfSslLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
		page.setList(dao.findSslLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfTunnelRandomLog> findTunnelRandomPage(Page<DfTunnelRandomLog> page, DfTunnelRandomLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DfTunnelRandomLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findTunnelRandomLogs(entity));
		}
		return page;
	}
	
	
	/**
	 * 查询端口封堵分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfIpPortLog> findIpPortPageCluster(Page<DfIpPortLog> page, DfIpPortLog entity,String activeSys) throws Exception{
		entity.setPage(page);
		page.setList(daoCluster.findIpPortLogsCluster(entity));
		
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfHttpReqLog> findHttpReqPageCluster(Page<DfHttpReqLog> page, DfHttpReqLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		page.setList(daoCluster.findHttpReqLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfHttpResLog> findHttpResPageCluster(Page<DfHttpResLog> page, DfHttpResLog entity,String activeSys) throws Exception{
		entity.setPage(page);
		page.setList(daoCluster.findHttpResLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfHttpKeywordLog> findHttpKeywordPageCluster(Page<DfHttpKeywordLog> page, DfHttpKeywordLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		page.setList(daoCluster.findHttpKeywordLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @throws Exception 
	 */
	public Page<DfHttpKeywordLog> findHttpMultiPartPageCluster(Page<DfHttpKeywordLog> page, DfHttpKeywordLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		page.setList(daoCluster.findHttpKeywordLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfMailLog> findMailPageCluster(Page<DfMailLog> page, DfMailLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		page.setList(daoCluster.findMailLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfDnsLog> findDnsPageCluster(Page<DfDnsLog> page, DfDnsLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		page.setList(daoCluster.findDnsLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfFtpLog> findFtpPageCluster(Page<DfFtpLog> page, DfFtpLog entity,String activeSys) throws SQLException, IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		page.setList(daoCluster.findFtpLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfPptpLog> findPptpPageCluster(Page<DfPptpLog> page, DfPptpLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		page.setList(daoCluster.findPptpLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfL2tpLog> findL2tpPageCluster(Page<DfL2tpLog> page, DfL2tpLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		page.setList(daoCluster.findL2tpLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfIpsecLog> findIpsecPageCluster(Page<DfIpsecLog> page, DfIpsecLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		page.setList(daoCluster.findIpsecLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfOpenvpnLog> findOpenvpnPageCluster(Page<DfOpenvpnLog> page, DfOpenvpnLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		page.setList(daoCluster.findOpenvpnLogsCluster(entity));
		return page;
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfSshLog> findSshPageCluster(Page<DfSshLog> page, DfSshLog entity,String activeSys) throws Exception {
		entity.setPage(page);
		page.setList(daoCluster.findSshLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfSslLog> findSslPageCluster(Page<DfSslLog> page, DfSslLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		page.setList(daoCluster.findSslLogsCluster(entity));
		return page;
	}
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public Page<DfTunnelRandomLog> findTunnelRandomPageCluster(Page<DfTunnelRandomLog> page, DfTunnelRandomLog entity,String activeSys)  throws Exception{
		entity.setPage(page);
		page.setList(daoCluster.findTunnelRandomLogsCluster(entity));
		return page;
	}
	
}
