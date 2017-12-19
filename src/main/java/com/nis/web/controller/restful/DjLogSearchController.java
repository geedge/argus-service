/**   
* @Title: DjLogSearchController.java 
* @Package com.nis.web.controller.restful
* @Description: TODO(用一句话描述该文件做什么) 
* @author (zbc) 
* @date 2016年9月7日上午8:45:58
* @version V1.0   
*/
package com.nis.web.controller.restful;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nis.domain.LogEntity;
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
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.HiveJDBC;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.util.redis.RedisDao;
import com.nis.util.redis.SaveRedisThread;
import com.nis.web.controller.BaseRestController;
import com.nis.web.service.HiveSqlService;
import com.nis.web.service.SaveRequestLogThread;
import com.nis.web.service.ServicesRequestLogService;
import com.nis.web.service.restful.DjLogSearchService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 
 * @ClassName: DjLogSearchController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (zbc)
 * @date 2016年9月7日上午8:45:58
 * @version V1.0
 */
@RestController
@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DjLogSearchController extends BaseRestController {
	protected final Logger logger1 = Logger.getLogger(this.getClass());
	protected String logSource = "0";
	@Autowired
	protected DjLogSearchService djLogService;
	@Autowired
	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "/djIpPortLogs", method = RequestMethod.GET)
	@ApiOperation(value = "端口监测分页获取", httpMethod = "GET", notes = "get log list")
	public Map djIpPortLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjIpPortLog ipPortLog, Model model, HttpServletRequest request, HttpServletResponse response) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys) ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjIpPortLog> ipPortLogPage = null;

		try {
			resetTime(ipPortLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, ipPortLog, DjIpPortLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(ipPortLog.getSearchFoundStartTime(),
					ipPortLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;

				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjIpPortLog> ipPortList = new ArrayList<DjIpPortLog>();
					ipPortLogPage = new Page<DjIpPortLog>();
					List<String> strlist = redisDao.getList(key, startNum, endNum);
					if (null != strlist && strlist.size() > 0) {
						if (null != strlist && strlist.size() > 0) {
							for (String str : strlist) {
								DjIpPortLog ipPort = (DjIpPortLog) JsonMapper.fromJsonString(str, DjIpPortLog.class);
								ipPortList.add(ipPort);
							}
						}
						ipPortLogPage.setList(ipPortList);
					} else {
						ipPortLogPage.setList(new ArrayList());
					}
				} else {
					ipPortLogPage = new Page<DjIpPortLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjIpPortLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, ipPortLog, "DJ_IP_PORT_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjIpPortLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						ipPortLogPage.setList(new ArrayList());
					} else {
						List list = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							list = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (list.size() > 0) {
							String jsonString = JsonMapper.toJsonString(list);
							List<DjIpPortLog> List = (java.util.List<DjIpPortLog>) JsonMapper.fromJsonList(jsonString,
									DjIpPortLog.class);
							ipPortLogPage.setList(List);

						} else {
							ipPortLogPage.setList(new ArrayList());
						}
					}
				}
				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					// 判断key是否存在
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						ipPortLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(ipPortLog, countKey, "DJ_IP_PORT_LOG",
								getCol2Col(), searchActiveSys);
						ipPortLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(ipPortLog, countKey, "DJ_IP_PORT_LOG",
							getCol2Col(), searchActiveSys);
					ipPortLogPage.setCount(Long.valueOf(hivePageCount));
				}
				ipPortLogPage.setPageNo(page.getPageNo());
				ipPortLogPage.setPageSize(page.getPageSize());

			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					ipPortLogPage = (Page<DjIpPortLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					ipPortLogPage = djLogService
							.findIpPortPage(new Page<DjIpPortLog>(request, response, DjIpPortLog.class), ipPortLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, ipPortLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "端口监测日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "端口监测日志检索成功",
				ipPortLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djHttpReqLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP协议请求监测分页获取", httpMethod = "GET", notes = "get log list")
	public Map djHttpReqLogList(@RequestParam(value = "searchActiveSys", required = false) String searchActiveSys,
			Page page, DjHttpReqLog httpReqLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys) ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjHttpReqLog> httpReqLogPage = null;

		try {
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, httpReqLog, DjHttpReqLog.class, page);

			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpReqLog.getSearchFoundStartTime(),
							httpReqLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjHttpReqLog> httpReqList = new ArrayList<DjHttpReqLog>();
					httpReqLogPage = new Page<DjHttpReqLog>();
					List<String> strlist = redisDao.getList(key, startNum, endNum);
					if (null != strlist && strlist.size() > 0) {
						if (null != strlist && strlist.size() > 0) {
							for (String str : strlist) {
								DjHttpReqLog djHttpReqLog = (DjHttpReqLog) JsonMapper.fromJsonString(str,
										DjHttpReqLog.class);
								httpReqList.add(djHttpReqLog);
							}
						}
						httpReqLogPage.setList(httpReqList);
					} else {
						httpReqLogPage.setList(new ArrayList());
					}
				} else {
					httpReqLogPage = new Page<DjHttpReqLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjHttpReqLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, httpReqLog, "DJ_HTTP_REQ_LOG", getCol2Col(),
							orderBy, searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjHttpReqLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						httpReqLogPage.setList(new ArrayList());
					} else {
						List list = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							list = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (list.size() > 0) {
							String jsonString = JsonMapper.toJsonString(list);
							List<DjHttpReqLog> List = (java.util.List<DjHttpReqLog>) JsonMapper.fromJsonList(jsonString,
									DjHttpReqLog.class);
							httpReqLogPage.setList(List);

						} else {
							httpReqLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						httpReqLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpReqLog, countKey, "DJ_HTTP_REQ_LOG",
								getCol2Col(), searchActiveSys);
						httpReqLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(httpReqLog, countKey, "DJ_HTTP_REQ_LOG",
							getCol2Col(), searchActiveSys);
					httpReqLogPage.setCount(Long.valueOf(hivePageCount));
				}
				httpReqLogPage.setPageNo(page.getPageNo());
				httpReqLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					httpReqLogPage = (Page<DjHttpReqLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					httpReqLogPage = djLogService
							.findHttpReqPage(new Page<DjHttpReqLog>(request, response, DjHttpReqLog.class), httpReqLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, httpReqLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}

		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "HTTP协议请求日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "HTTP协议请求日志检索成功",
				httpReqLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djHttpResLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP协议响应监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djHttpResLogsList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjHttpResLog djHttpResLog, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjHttpResLog> djHttpResLogPage = null;
		try {
			resetTime(djHttpResLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, djHttpResLog, DjHttpResLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(djHttpResLog.getSearchFoundStartTime(),
							djHttpResLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjHttpResLog> httpResList = new ArrayList<DjHttpResLog>();
					djHttpResLogPage = new Page<DjHttpResLog>();
					List<String> list = redisDao.getList(key, startNum, endNum);
					if (null != list && list.size() > 0) {
						if (null != list && list.size() > 0) {
							for (String str : list) {
								DjHttpResLog httpResLog = (DjHttpResLog) JsonMapper.fromJsonString(str,
										DjHttpResLog.class);
								httpResList.add(httpResLog);
							}
						}
						djHttpResLogPage.setList(httpResList);
					} else {
						djHttpResLogPage.setList(new ArrayList());
					}
				} else {

					djHttpResLogPage = new Page<DjHttpResLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjHttpResLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, djHttpResLog, "DJ_HTTP_RES_LOG", getCol2Col(),
							orderBy, searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjHttpResLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						djHttpResLogPage.setList(new ArrayList());
					} else {
						List list = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							list = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (list.size() > 0) {

							String jsonString = JsonMapper.toJsonString(list);
							List<DjHttpResLog> List = (java.util.List<DjHttpResLog>) JsonMapper.fromJsonList(jsonString,
									DjHttpResLog.class);
							djHttpResLogPage.setList(List);
						} else {
							djHttpResLogPage.setList(new ArrayList());
						}
					}
				}
				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						djHttpResLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(djHttpResLog, countKey, "DJ_HTTP_RES_LOG",
								getCol2Col(), searchActiveSys);
						djHttpResLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(djHttpResLog, countKey, "DJ_HTTP_RES_LOG",
							getCol2Col(), searchActiveSys);
					djHttpResLogPage.setCount(Long.valueOf(hivePageCount));
				}
				djHttpResLogPage.setPageNo(page.getPageNo());
				djHttpResLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					djHttpResLogPage = (Page<DjHttpResLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					djHttpResLogPage = djLogService.findHttpResPage(
							new Page<DjHttpResLog>(request, response, DjHttpResLog.class), djHttpResLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, djHttpResLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "HTTP协议响应监测日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "HTTP协议响应监测日志检索成功",
				djHttpResLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djHttpKeywordLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP协议关键字、请求多域、响应多域监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djHttpKeywordLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjHttpKeywordLog httpKeywordLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		String searchServiceType = httpKeywordLog.getSearchServiceType();
		if (StringUtils.isEmpty(searchServiceType)) {
			httpKeywordLog.setSearchServiceType("51"); // 0x33
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjHttpKeywordLog> httpKeywordLogPage = null;

		try {
			resetTime(httpKeywordLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, httpKeywordLog, DjHttpKeywordLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpKeywordLog.getSearchFoundStartTime(),
							httpKeywordLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjHttpKeywordLog> list = new ArrayList<DjHttpKeywordLog>();
					httpKeywordLogPage = new Page<DjHttpKeywordLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjHttpKeywordLog bean = (DjHttpKeywordLog) JsonMapper.fromJsonString(str,
										DjHttpKeywordLog.class);
								list.add(bean);
							}
						}
						httpKeywordLogPage.setList(list);
					} else {
						httpKeywordLogPage.setList(new ArrayList());
					}
				} else {

					httpKeywordLogPage = new Page<DjHttpKeywordLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjHttpKeywordLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, httpKeywordLog, "DJ_HTTP_KEYWORD_LOG",
							getCol2Col(), orderBy, searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjHttpKeywordLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						httpKeywordLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjHttpKeywordLog> List = (java.util.List<DjHttpKeywordLog>) JsonMapper
									.fromJsonList(jsonString, DjHttpKeywordLog.class);
							httpKeywordLogPage.setList(List);
						} else {
							httpKeywordLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						httpKeywordLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpKeywordLog, countKey,
								"DJ_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
						httpKeywordLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(httpKeywordLog, countKey,
							"DJ_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
					httpKeywordLogPage.setCount(Long.valueOf(hivePageCount));
				}
				httpKeywordLogPage.setPageNo(page.getPageNo());
				httpKeywordLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}

				if (keyExists) {
					httpKeywordLogPage = (Page<DjHttpKeywordLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					httpKeywordLogPage = djLogService.findHttpKeywordPage(
							new Page<DjHttpKeywordLog>(request, response, DjHttpKeywordLog.class), httpKeywordLog,searchActiveSys);
					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, httpKeywordLogPage, Constants.ORACLE_EXPIRE).start();
				}

			}

		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "HTTP协议关键字、请求多域、响应多域日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response,
				"HTTP协议关键字、请求多域、响应多域日志检索成功", httpKeywordLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djHttpReqMultiPartLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP协议请求多域监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djHttpReqMultiPartLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjHttpKeywordLog httpReqMultiLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		httpReqMultiLog.setSearchServiceType("61"); // 0x3d

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjHttpKeywordLog> httpReqMulitiLogPage = null;

		try {
			resetTime(httpReqMultiLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, httpReqMultiLog, DjHttpKeywordLog.class, page);

			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpReqMultiLog.getSearchFoundStartTime(),
							httpReqMultiLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjHttpKeywordLog> list = new ArrayList<DjHttpKeywordLog>();
					httpReqMulitiLogPage = new Page<DjHttpKeywordLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjHttpKeywordLog bean = (DjHttpKeywordLog) JsonMapper.fromJsonString(str,
										DjHttpKeywordLog.class);
								list.add(bean);
							}
						}
						httpReqMulitiLogPage.setList(list);
					} else {
						httpReqMulitiLogPage.setList(new ArrayList());
					}
				} else {

					httpReqMulitiLogPage = new Page<DjHttpKeywordLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjHttpKeywordLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, httpReqMultiLog, "DJ_HTTP_KEYWORD_LOG",
							getCol2Col(), orderBy, searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjHttpKeywordLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						httpReqMulitiLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjHttpKeywordLog> List = (java.util.List<DjHttpKeywordLog>) JsonMapper
									.fromJsonList(jsonString, DjHttpKeywordLog.class);
							httpReqMulitiLogPage.setList(List);
						} else {
							httpReqMulitiLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						httpReqMulitiLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpReqMultiLog, countKey,
								"DJ_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
						httpReqMulitiLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(httpReqMultiLog, countKey,
							"DJ_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
					httpReqMulitiLogPage.setCount(Long.valueOf(hivePageCount));
				}
				httpReqMulitiLogPage.setPageNo(page.getPageNo());
				httpReqMulitiLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}

				if (keyExists) {
					httpReqMulitiLogPage = (Page<DjHttpKeywordLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					httpReqMulitiLogPage = djLogService.findHttpMultiPartPage(
							new Page<DjHttpKeywordLog>(request, response, DjHttpKeywordLog.class), httpReqMultiLog,searchActiveSys);
					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, httpReqMulitiLogPage, Constants.ORACLE_EXPIRE).start();
				}

			}

		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "HTTP协议请求多域监测日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "HTTP协议请求多域监测日志检索成功",
				httpReqMulitiLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djHttpResMultiPartLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP协议响应多域监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djHttpResMultiPartLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjHttpKeywordLog httpResMultiLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		httpResMultiLog.setSearchServiceType("62"); // 0x3e

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjHttpKeywordLog> httpResMulitiLogPage = null;

		try {
			resetTime(httpResMultiLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, httpResMultiLog, DjHttpKeywordLog.class, page);

			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpResMultiLog.getSearchFoundStartTime(),
							httpResMultiLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjHttpKeywordLog> list = new ArrayList<DjHttpKeywordLog>();
					httpResMulitiLogPage = new Page<DjHttpKeywordLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjHttpKeywordLog bean = (DjHttpKeywordLog) JsonMapper.fromJsonString(str,
										DjHttpKeywordLog.class);
								list.add(bean);
							}
						}
						httpResMulitiLogPage.setList(list);
					} else {
						httpResMulitiLogPage.setList(new ArrayList());
					}
				} else {

					httpResMulitiLogPage = new Page<DjHttpKeywordLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjHttpKeywordLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, httpResMultiLog, "DJ_HTTP_KEYWORD_LOG",
							getCol2Col(), orderBy, searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjHttpKeywordLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						httpResMulitiLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjHttpKeywordLog> List = (java.util.List<DjHttpKeywordLog>) JsonMapper
									.fromJsonList(jsonString, DjHttpKeywordLog.class);
							httpResMulitiLogPage.setList(List);
						} else {
							httpResMulitiLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						httpResMulitiLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpResMultiLog, countKey,
								"DJ_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
						httpResMulitiLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(httpResMultiLog, countKey,
							"DJ_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
					httpResMulitiLogPage.setCount(Long.valueOf(hivePageCount));
				}
				httpResMulitiLogPage.setPageNo(page.getPageNo());
				httpResMulitiLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}

				if (keyExists) {
					httpResMulitiLogPage = (Page<DjHttpKeywordLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					httpResMulitiLogPage = djLogService.findHttpMultiPartPage(
							new Page<DjHttpKeywordLog>(request, response, DjHttpKeywordLog.class), httpResMultiLog,searchActiveSys);
					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, httpResMulitiLogPage, Constants.ORACLE_EXPIRE).start();
				}

			}

		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "HTTP协议响应多域监测日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "HTTP协议响应多域监测日志检索成功",
				httpResMulitiLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djMailLogs", method = RequestMethod.GET)
	@ApiOperation(value = "MAIL监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djMailLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjMailLog mailLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjMailLog> mailLogPage = null;

		try {
			resetTime(mailLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, mailLog, DjMailLog.class, page);

			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(mailLog.getSearchFoundStartTime(),
					mailLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjMailLog> list = new ArrayList<DjMailLog>();
					mailLogPage = new Page<DjMailLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjMailLog bean = (DjMailLog) JsonMapper.fromJsonString(str, DjMailLog.class);
								list.add(bean);
							}
						}
						mailLogPage.setList(list);
					} else {
						mailLogPage.setList(new ArrayList());
					}
				} else {

					mailLogPage = new Page<DjMailLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjMailLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, mailLog, "DJ_MAIL_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjMailLog.class, "foundTime",
							"recvTime");
					if (tableMapping == null) {
						mailLogPage.setList(new ArrayList());

					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjMailLog> List = (java.util.List<DjMailLog>) JsonMapper.fromJsonList(jsonString,
									DjMailLog.class);
							mailLogPage.setList(List);
						} else {
							mailLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						mailLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(mailLog, countKey, "DJ_MAIL_LOG",
								getCol2Col(), searchActiveSys);
						mailLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(mailLog, countKey, "DJ_MAIL_LOG", getCol2Col(),
							searchActiveSys);
					mailLogPage.setCount(Long.valueOf(hivePageCount));
				}

				mailLogPage.setPageNo(page.getPageNo());
				mailLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}

				if (keyExists) {
					mailLogPage = (Page<DjMailLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					mailLogPage = djLogService.findMailPage(new Page<DjMailLog>(request, response, DjMailLog.class),
							mailLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, mailLogPage, Constants.ORACLE_EXPIRE).start();
				}

			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "MAIL日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "MAIL日志检索成功", mailLogPage,
				searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djDnsLogs", method = RequestMethod.GET)
	@ApiOperation(value = "DNS监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djDnsLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjDnsLog dnsLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		logger1.info("djDnsLogList接口请求方法开始----" + System.currentTimeMillis());
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjDnsLog> dnsLogPage = null;

		try {
			resetTime(dnsLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, dnsLog, DjDnsLog.class, page);

			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(dnsLog.getSearchFoundStartTime(),
					dnsLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logger1.info("hive开始----" + System.currentTimeMillis());
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjDnsLog> dnsList = new ArrayList<DjDnsLog>();
					dnsLogPage = new Page<DjDnsLog>();
					List<String> list = redisDao.getList(key, startNum, endNum);
					if (null != list && list.size() > 0) {
						if (null != list && list.size() > 0) {
							for (String str : list) {
								DjDnsLog djDnsLog = (DjDnsLog) JsonMapper.fromJsonString(str, DjDnsLog.class);
								dnsList.add(djDnsLog);
							}
						}
						dnsLogPage.setList(dnsList);
					} else {
						dnsLogPage.setList(new ArrayList());
					}
				} else {

					dnsLogPage = new Page<DjDnsLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjDnsLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, dnsLog, "DJ_DNS_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjDnsLog.class, "foundTime",
							"recvTime");
					if (tableMapping == null) {
						dnsLogPage.setList(new ArrayList());
					} else {
						List list = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							list = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							list = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (list.size() > 0) {
							String jsonString = JsonMapper.toJsonString(list);
							List<DjDnsLog> List = (java.util.List<DjDnsLog>) JsonMapper.fromJsonList(jsonString,
									DjDnsLog.class);
							dnsLogPage.setList(List);
						} else {
							dnsLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						dnsLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(dnsLog, countKey, "DJ_DNS_LOG",
								getCol2Col(), searchActiveSys);
						dnsLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(dnsLog, countKey, "DJ_DNS_LOG", getCol2Col(),
							searchActiveSys);
					dnsLogPage.setCount(Long.valueOf(hivePageCount));
				}
				dnsLogPage.setPageNo(page.getPageNo());
				dnsLogPage.setPageSize(page.getPageSize());
				logger1.info("hive结束----" + System.currentTimeMillis());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					logger1.info("redis Key校验开始----" + System.currentTimeMillis());
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
					logger1.info("redis Key校验结束----" + System.currentTimeMillis());
				}

				if (keyExists) {
					logger1.info("redis查询开始----" + System.currentTimeMillis());
					dnsLogPage = (Page<DjDnsLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
					logger1.info("redis查询结束----" + System.currentTimeMillis());
				} else {
					logger1.info("oracle查询开始----" + System.currentTimeMillis());
					dnsLogPage = djLogService.findDnsPage(new Page<DjDnsLog>(request, response, DjDnsLog.class),
							dnsLog,searchActiveSys);
					logger1.info("oracle查询结束----" + System.currentTimeMillis());
					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, dnsLogPage, Constants.ORACLE_EXPIRE).start();
				}

			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "DNS日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		} finally {
			logger1.info("djDnsLogList接口请求方法结束----" + System.currentTimeMillis());
		}

		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "DNS日志检索成功", dnsLogPage,
				searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djFtpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "FTP监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djFtpLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjFtpLog ftpLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjFtpLog> ftpLogPage = null;

		try {
			resetTime(ftpLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, ftpLog, DjFtpLog.class, page);

			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(ftpLog.getSearchFoundStartTime(),
					ftpLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjFtpLog> list = new ArrayList<DjFtpLog>();
					ftpLogPage = new Page<DjFtpLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjFtpLog bean = (DjFtpLog) JsonMapper.fromJsonString(str, DjFtpLog.class);
								list.add(bean);
							}
						}
						ftpLogPage.setList(list);
					} else {
						ftpLogPage.setList(new ArrayList());
					}
				} else {

					ftpLogPage = new Page<DjFtpLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjFtpLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, ftpLog, "DJ_FTP_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjFtpLog.class, "foundTime",
							"recvTime");
					if (tableMapping == null) {
						ftpLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {

							String jsonString = JsonMapper.toJsonString(strList);
							List<DjFtpLog> List = (java.util.List<DjFtpLog>) JsonMapper.fromJsonList(jsonString,
									DjFtpLog.class);
							ftpLogPage.setList(List);
						} else {
							ftpLogPage.setList(new ArrayList());
						}
					}
				}
				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						ftpLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(ftpLog, countKey, "DJ_FTP_LOG",
								getCol2Col(), searchActiveSys);
						ftpLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(ftpLog, countKey, "DJ_FTP_LOG", getCol2Col(),
							searchActiveSys);
					ftpLogPage.setCount(Long.valueOf(hivePageCount));
				}
				ftpLogPage.setPageNo(page.getPageNo());
				ftpLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					ftpLogPage = (Page<DjFtpLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					ftpLogPage = djLogService.findFtpPage(new Page<DjFtpLog>(request, response, DjFtpLog.class),
							ftpLog,searchActiveSys);
					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, ftpLogPage, Constants.ORACLE_EXPIRE).start();
				}

			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "FTP日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "FTP日志检索成功", ftpLogPage,
				searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djPPTPLogs", method = RequestMethod.GET)
	@ApiOperation(value = "PPTP监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djPptpLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjPptpLog pptpLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjPptpLog> pptpLogPage = null;

		try {
			resetTime(pptpLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, pptpLog, DjPptpLog.class, page);

			// 单独对PPTP的EncryptMode字段校验
			if (!StringUtil.isBlank(pptpLog.getSearchEncryptMode())) {
				Integer.parseInt(pptpLog.getSearchEncryptMode());
			}
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(pptpLog.getSearchFoundStartTime(),
					pptpLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjPptpLog> list = new ArrayList<DjPptpLog>();
					pptpLogPage = new Page<DjPptpLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjPptpLog bean = (DjPptpLog) JsonMapper.fromJsonString(str, DjPptpLog.class);
								list.add(bean);
							}
						}

						pptpLogPage.setList(list);

					} else {
						pptpLogPage.setList(new ArrayList());
					}
				} else {

					pptpLogPage = new Page<DjPptpLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjPptpLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, pptpLog, "DJ_PPTP_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjPptpLog.class, "foundTime",
							"recvTime");
					if (tableMapping == null) {
						pptpLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjPptpLog> List = (java.util.List<DjPptpLog>) JsonMapper.fromJsonList(jsonString,
									DjPptpLog.class);
							pptpLogPage.setList(List);

						} else {
							pptpLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						pptpLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(pptpLog, countKey, "DJ_PPTP_LOG",
								getCol2Col(), searchActiveSys);
						pptpLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(pptpLog, countKey, "DJ_PPTP_LOG", getCol2Col(),
							searchActiveSys);
					pptpLogPage.setCount(Long.valueOf(hivePageCount));
				}

				pptpLogPage.setPageNo(page.getPageNo());
				pptpLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					pptpLogPage = (Page<DjPptpLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					pptpLogPage = djLogService.findPptpPage(new Page<DjPptpLog>(request, response, DjPptpLog.class),
							pptpLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, pptpLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchEncryptMode参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(searchActiveSys);
			restE.setLogSource(logSource);
			throw restE;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "PPTP日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "PPTP日志检索成功", pptpLogPage,
				searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djL2tpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "L2TP监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djL2tpLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjL2tpLog l2tpLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjL2tpLog> l2tpLogPage = null;

		try {
			resetTime(l2tpLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, l2tpLog, DjL2tpLog.class, page);

			// 单独对Ipsec的EncryptMode字段校验
			if (!StringUtil.isBlank(l2tpLog.getSearchEncryptMode())) {
				Integer.parseInt(l2tpLog.getSearchEncryptMode());
			}
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(l2tpLog.getSearchFoundStartTime(),
					l2tpLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjL2tpLog> list = new ArrayList<DjL2tpLog>();
					l2tpLogPage = new Page<DjL2tpLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjL2tpLog bean = (DjL2tpLog) JsonMapper.fromJsonString(str, DjL2tpLog.class);
								list.add(bean);
							}
						}

						l2tpLogPage.setList(list);

					} else {
						l2tpLogPage.setList(new ArrayList());
					}
				} else {

					l2tpLogPage = new Page<DjL2tpLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjL2tpLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, l2tpLog, "DJ_L2TP_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjL2tpLog.class, "foundTime",
							"recvTime");
					if (tableMapping == null) {
						l2tpLogPage.setList(new ArrayList());

					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjL2tpLog> List = (java.util.List<DjL2tpLog>) JsonMapper.fromJsonList(jsonString,
									DjL2tpLog.class);
							l2tpLogPage.setList(List);

						} else {
							l2tpLogPage.setList(new ArrayList());
						}
					}
				}
				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						l2tpLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(l2tpLog, countKey, "DJ_L2TP_LOG",
								getCol2Col(), searchActiveSys);
						l2tpLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(l2tpLog, countKey, "DJ_L2TP_LOG", getCol2Col(),
							searchActiveSys);
					l2tpLogPage.setCount(Long.valueOf(hivePageCount));
				}
				l2tpLogPage.setPageNo(page.getPageNo());
				l2tpLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					l2tpLogPage = (Page<DjL2tpLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					l2tpLogPage = djLogService.findL2tpPage(new Page<DjL2tpLog>(request, response, DjL2tpLog.class),
							l2tpLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, l2tpLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"EncryptMode参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(searchActiveSys);
			restE.setLogSource(logSource);
			throw restE;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "L2TP日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "L2TP日志检索成功", l2tpLogPage,
				searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djIpsecLogs", method = RequestMethod.GET)
	@ApiOperation(value = "IPSEC监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djIpsecLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjIpsecLog ipsecLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjIpsecLog> ipsecLogPage = null;

		try {
			resetTime(ipsecLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, ipsecLog, DjIpsecLog.class, page);

			// 单独对Ipsec的exProtocol字段校验
			if (!StringUtil.isBlank(ipsecLog.getSearchExProtocol())) {
				Integer.parseInt(ipsecLog.getSearchExProtocol());
			}
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(ipsecLog.getSearchFoundStartTime(),
					ipsecLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjIpsecLog> list = new ArrayList<DjIpsecLog>();
					ipsecLogPage = new Page<DjIpsecLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjIpsecLog bean = (DjIpsecLog) JsonMapper.fromJsonString(str, DjIpsecLog.class);
								list.add(bean);
							}
						}

						ipsecLogPage.setList(list);

					} else {
						ipsecLogPage.setList(new ArrayList());
					}
				} else {

					ipsecLogPage = new Page<DjIpsecLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjIpsecLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, ipsecLog, "DJ_IPSEC_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjIpsecLog.class, "foundTime",
							"recvTime");
					if (tableMapping == null) {
						ipsecLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjIpsecLog> List = (java.util.List<DjIpsecLog>) JsonMapper.fromJsonList(jsonString,
									DjIpsecLog.class);
							ipsecLogPage.setList(List);
						} else {
							ipsecLogPage.setList(new ArrayList());
						}
					}
				}
				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						ipsecLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(ipsecLog, countKey, "DJ_IPSEC_LOG",
								getCol2Col(), searchActiveSys);
						ipsecLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(ipsecLog, countKey, "DJ_IPSEC_LOG",
							getCol2Col(), searchActiveSys);
					ipsecLogPage.setCount(Long.valueOf(hivePageCount));
				}
				ipsecLogPage.setPageNo(page.getPageNo());
				ipsecLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					ipsecLogPage = (Page<DjIpsecLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					ipsecLogPage = djLogService.findIpsecPage(new Page<DjIpsecLog>(request, response, DjIpsecLog.class),
							ipsecLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, ipsecLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"exProtocol参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(searchActiveSys);
			restE.setLogSource(logSource);
			throw restE;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "IPSEC日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "IPSEC日志检索成功",
				ipsecLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djOpenVpnLogs", method = RequestMethod.GET)
	@ApiOperation(value = "OPENVPN监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djOpenvpnLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjOpenvpnLog openvpnLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjOpenvpnLog> openvpnLogPage = null;

		try {
			resetTime(openvpnLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, openvpnLog, DjOpenvpnLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(openvpnLog.getSearchFoundStartTime(),
							openvpnLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据\
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjOpenvpnLog> list = new ArrayList<DjOpenvpnLog>();
					openvpnLogPage = new Page<DjOpenvpnLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjOpenvpnLog bean = (DjOpenvpnLog) JsonMapper.fromJsonString(str, DjOpenvpnLog.class);
								list.add(bean);
							}
						}

						openvpnLogPage.setList(list);

					} else {
						openvpnLogPage.setList(new ArrayList());
					}
				} else {

					openvpnLogPage = new Page<DjOpenvpnLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjOpenvpnLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, openvpnLog, "DJ_OPENVPN_LOG", getCol2Col(),
							orderBy, searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjOpenvpnLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						openvpnLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjOpenvpnLog> List = (java.util.List<DjOpenvpnLog>) JsonMapper.fromJsonList(jsonString,
									DjOpenvpnLog.class);
							openvpnLogPage.setList(List);

						} else {
							openvpnLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						openvpnLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(openvpnLog, countKey, "DJ_OPENVPN_LOG",
								getCol2Col(), searchActiveSys);
						openvpnLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(openvpnLog, countKey, "DJ_OPENVPN_LOG",
							getCol2Col(), searchActiveSys);
					openvpnLogPage.setCount(Long.valueOf(hivePageCount));
				}

				openvpnLogPage.setPageNo(page.getPageNo());
				openvpnLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					openvpnLogPage = (Page<DjOpenvpnLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					openvpnLogPage = djLogService
							.findOpenvpnPage(new Page<DjOpenvpnLog>(request, response, DjOpenvpnLog.class), openvpnLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, openvpnLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "OPENVPN日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "OPENVPN日志检索成功",
				openvpnLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djSSHLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSH监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djSshLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjSshLog sshLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjSshLog> sshLogPage = null;

		try {
			resetTime(sshLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, sshLog, DjSshLog.class, page);

			// 单独对SSH的EncryptMode字段校验
			if (!StringUtil.isBlank(sshLog.getSearchEncryptMode())) {
				Long.parseLong(sshLog.getSearchEncryptMode());
			}

			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(sshLog.getSearchFoundStartTime(),
					sshLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjSshLog> list = new ArrayList<DjSshLog>();
					sshLogPage = new Page<DjSshLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjSshLog bean = (DjSshLog) JsonMapper.fromJsonString(str, DjSshLog.class);
								list.add(bean);
							}
						}

						sshLogPage.setList(list);

					} else {
						sshLogPage.setList(new ArrayList());
					}
				} else {

					sshLogPage = new Page<DjSshLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjSshLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, sshLog, "DJ_SSH_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjSshLog.class, "foundTime",
							"recvTime");
					if (tableMapping == null) {
						sshLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjSshLog> List = (java.util.List<DjSshLog>) JsonMapper.fromJsonList(jsonString,
									DjSshLog.class);
							sshLogPage.setList(List);

						} else {
							sshLogPage.setList(new ArrayList());
						}
					}
				}
				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						sshLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(sshLog, countKey, "DJ_SSH_LOG",
								getCol2Col(), searchActiveSys);
						sshLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(sshLog, countKey, "DJ_SSH_LOG", getCol2Col(),
							searchActiveSys);
					sshLogPage.setCount(Long.valueOf(hivePageCount));
				}
				sshLogPage.setPageNo(page.getPageNo());
				sshLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					sshLogPage = (Page<DjSshLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					sshLogPage = djLogService.findSshPage(new Page<DjSshLog>(request, response, DjSshLog.class),
							sshLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, sshLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			RestServiceException restE = new RestServiceException(thread, System.currentTimeMillis() - start,
					"searchEncryptMode参数格式错误", RestBusinessCode.param_formate_error.getValue());
			restE.setActiveSys(searchActiveSys);
			restE.setLogSource(logSource);
			throw restE;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "SSH日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "SSH日志检索成功", sshLogPage,
				searchActiveSys, logSource);
	}

	@RequestMapping(value = "/djSSLLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSL监测日志获取", httpMethod = "GET", notes = "get log list")
	public Map djSslLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DjSslLog sslLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) 
				&& !Constants.ACTIVESYS_C.equals(searchActiveSys)  ) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DjSslLog> sslLogPage = null;

		try {
			resetTime(sslLog);
			// 请求参数校验
			djLogService.queryConditionCheck(thread, start, sslLog, DjSslLog.class, page);

			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(sslLog.getSearchFoundStartTime(),
					sslLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DjSslLog> list = new ArrayList<DjSslLog>();
					sslLogPage = new Page<DjSslLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DjSslLog bean = (DjSslLog) JsonMapper.fromJsonString(str, DjSslLog.class);
								list.add(bean);
							}
						}
						sslLogPage.setList(list);
					} else {
						sslLogPage.setList(new ArrayList());
					}
				} else {
					sslLogPage = new Page<DjSslLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DjSslLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, sslLog, "DJ_SSL_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DjSslLog.class, "foundTime",
							"recvTime");
					if (tableMapping == null) {
						sslLogPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DjSslLog> List = (java.util.List<DjSslLog>) JsonMapper.fromJsonList(jsonString,
									DjSslLog.class);
							sslLogPage.setList(List);
						} else {
							sslLogPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = djLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						sslLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(sslLog, countKey, "DJ_SSL_LOG",
								getCol2Col(), searchActiveSys);
						sslLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(sslLog, countKey, "DJ_SSL_LOG", getCol2Col(),
							searchActiveSys);
					sslLogPage.setCount(Long.valueOf(hivePageCount));
				}
				sslLogPage.setPageNo(page.getPageNo());
				sslLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = djLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					sslLogPage = (Page<DjSslLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					sslLogPage = djLogService.findSslPage(new Page<DjSslLog>(request, response, DjSslLog.class),
							sslLog,searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, sslLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "SSL日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "SSL日志检索成功", sslLogPage,
				searchActiveSys, logSource);
	}

	public Map<String, Map<String, String>> getCol2Col() {
		Map<String, Map<String, String>> col2col = new HashMap<String, Map<String, String>>();
		Map<String, String> startMap = new HashMap<String, String>();
		startMap.put("start", "foundTime");
		col2col.put("searchFoundStartTime", startMap);
		Map<String, String> endMap = new HashMap<String, String>();
		endMap.put("end", "foundTime");
		col2col.put("searchFoundEndTime", endMap);
		return col2col;
	}

	/**
	 * @Title: resetTime
	 * @Description: TODO(日志开始结束时间为空则默认为本地存储时间)
	 * @param @param
	 *            entity
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public void resetTime(LogEntity<?> entity) throws Exception{
		Map<String, String> map = DateUtils.getLocalTime(entity.getSearchFoundStartTime(),
				entity.getSearchFoundEndTime(), Constants.LOG_LOCAL_TIME,"log");
		entity.setSearchFoundStartTime(map.get("startTime"));
		entity.setSearchFoundEndTime(map.get("endTime"));
	}
}
