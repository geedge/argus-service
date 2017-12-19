package com.nis.web.service.restful;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DjDnsLog;
import com.nis.domain.restful.DjFtpLog;
import com.nis.domain.restful.DjHttpKeywordLog;
import com.nis.domain.restful.DjHttpReqLog;
import com.nis.domain.restful.DjHttpResLog;
import com.nis.domain.restful.DjIpPortLog;
import com.nis.domain.restful.DjIpsecLog;
import com.nis.domain.restful.DjL2tpLog;
import com.nis.domain.restful.DjMailLog;
import com.nis.domain.restful.DjOpenvpnLog;
import com.nis.domain.restful.DjPptpLog;
import com.nis.domain.restful.DjSshLog;
import com.nis.domain.restful.DjSslLog;
import com.nis.util.Constants;
import com.nis.web.dao.DjLogSearchDao;
import com.nis.web.service.BaseLogService;

/** 
* @ClassName: DjLogSearchService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (zbc)  
* @date 2016年9月7日 上午11:15:12 
* @version V1.0 
*/
@Service
public class DjLogSearchService extends BaseLogService {

	/**
	 * 持久层对象
	 */
	@Autowired
	protected DjLogSearchDao dao;
	
	/**
	 * 查询端口封堵分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjIpPortLog> findIpPortPage(Page<DjIpPortLog> page, DjIpPortLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjIpPortLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjHttpResLog> findHttpResPage(Page<DjHttpResLog> page, DjHttpResLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjHttpResLog> resultList=new ArrayList<>();
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
	 * @param httpReqLog
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjHttpReqLog> findHttpReqPage(Page<DjHttpReqLog> page, DjHttpReqLog httpReqLog,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		httpReqLog.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjHttpReqLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, httpReqLog,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findHttpReqLogs(httpReqLog));
		}
		return page;
	}

	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjHttpKeywordLog> findHttpKeywordPage(Page<DjHttpKeywordLog> page, DjHttpKeywordLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjHttpKeywordLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findHttpKeywordLogs(entity));
		}
		return page;
	}
	/**
	 * 查询分页数据
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjHttpKeywordLog> findHttpMultiPartPage(Page<DjHttpKeywordLog> page, DjHttpKeywordLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjHttpKeywordLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjMailLog> findMailPage(Page<DjMailLog> page, DjMailLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjMailLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjDnsLog> findDnsPage(Page<DjDnsLog> page, DjDnsLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjDnsLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjFtpLog> findFtpPage(Page<DjFtpLog> page, DjFtpLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjFtpLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjPptpLog> findPptpPage(Page<DjPptpLog> page, DjPptpLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjPptpLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjL2tpLog> findL2tpPage(Page<DjL2tpLog> page, DjL2tpLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjL2tpLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjIpsecLog> findIpsecPage(Page<DjIpsecLog> page, DjIpsecLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjIpsecLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjOpenvpnLog> findOpenvpnPage(Page<DjOpenvpnLog> page, DjOpenvpnLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjOpenvpnLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjSshLog> findSshPage(Page<DjSshLog> page, DjSshLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjSshLog> resultList=new ArrayList<>();
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
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws ClientProtocolException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Page<DjSslLog> findSslPage(Page<DjSslLog> page, DjSslLog entity,String activeSys) throws IllegalArgumentException, IllegalAccessException, ClientProtocolException, ParseException, JSONException, IOException {
		entity.setPage(page);
		if(Constants.IS_USE_ES){
			List<DjSslLog> resultList=new ArrayList<>();
			elasticsearchSqlDao.findLogs(resultList, entity,activeSys);
			page.setList(resultList);
		}else{
			page.setList(dao.findSslLogs(entity));
		}
		return page;
	}

}
