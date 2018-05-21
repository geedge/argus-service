/**   
* @Title: DfLogSearchController.java 
* @Package com.nis.web.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月5日 下午10:52:37  
* @version V1.0   
*/
package com.nis.web.controller.restful;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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

import com.nis.datasource.CustomerContextHolder;
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
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Configurations;
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
import com.nis.web.service.restful.DfLogSearchService;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @ClassName: DfLogSearchController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (ddm)
 * @date 2016年9月5日 下午10:52:37
 * @version V1.0
 */
@RestController
//@RequestMapping("${servicePath}/log/v1")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DfLogSearchController extends BaseRestController {
	protected final Logger logger1 = Logger.getLogger(this.getClass());

	protected String logSource = "0";

	@Autowired
	protected DfLogSearchService dfLogService;
	@Autowired
	protected RedisDao redisDao;
	@Autowired
	protected ServicesRequestLogService servicesRequestLogService;

	@RequestMapping(value = "/dfIpPortLogs", method = RequestMethod.GET)
	@ApiOperation(value = "端口封堵分页获取", httpMethod = "GET", notes = "get log list")
	public Map dfIpPortLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfIpPortLog ipPortLog, Model model, HttpServletRequest request, HttpServletResponse response) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}
		long start = System.currentTimeMillis();

		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		Page<DfIpPortLog> ipPortLogPage =  new Page<DfIpPortLog>();
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		try {
			resetTime(ipPortLog);
			logger1.info("请求参数检验开始---" + System.currentTimeMillis());
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, ipPortLog, DfIpPortLog.class, page);
			logger1.info("请求参数检验结束---" + System.currentTimeMillis());
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(ipPortLog.getSearchFoundStartTime(),
					ipPortLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (ipPortLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (ipPortLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						ipPortLog.setTableName(Configurations.getStringProperty(DfIpPortLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						ipPortLog.setTableName(Configurations
								.getStringProperty(DfIpPortLog.class.getSimpleName() + "B", "t_xa_df_ip_port_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						ipPortLogPage = (Page<DfIpPortLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(ipPortLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						ipPortLogPage = dfLogService.findIpPortPageCluster(
								new Page<DfIpPortLog>(request, response, DfIpPortLog.class), ipPortLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, ipPortLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						logger1.info("hive-redis查询开始---" + System.currentTimeMillis());
						List<DfIpPortLog> list = new ArrayList<DfIpPortLog>();
						ipPortLogPage = new Page<DfIpPortLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfIpPortLog bean = (DfIpPortLog) JsonMapper.fromJsonString(str, DfIpPortLog.class);
								list.add(bean);
							}
							ipPortLogPage.setList(list);

						} else {
							ipPortLogPage.setList(new ArrayList());
						}
						logger1.info("hive-redis查询结束---" + System.currentTimeMillis());
					} else {
						logger1.info("hive查询开始---" + System.currentTimeMillis());
						ipPortLogPage = new Page<DfIpPortLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfIpPortLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, ipPortLog, "DF_IP_PORT_LOG", getCol2Col(), orderBy,
								searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfIpPortLog.class,
								"foundTime", "recvTime");
						if (tableMapping == null) {
							ipPortLogPage.setList(new ArrayList());

						} else {
							List strList = new ArrayList();
							if (tableMapping.get("obj").size() > page.getPageSize()) {
								strList = tableMapping.get("obj").subList(0, page.getPageSize());
							} else {
								strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
							}
							if (strList.size() > 0) {
								String jsonString = JsonMapper.toJsonString(strList);
								List<DfIpPortLog> List = (java.util.List<DfIpPortLog>) JsonMapper.fromJsonList(jsonString,
										DfIpPortLog.class);
								ipPortLogPage.setList(List);

							} else {
								ipPortLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							ipPortLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(ipPortLog, countKey, "DF_IP_PORT_LOG",
									getCol2Col(), searchActiveSys);
							ipPortLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(ipPortLog, countKey, "DF_IP_PORT_LOG",
								getCol2Col(), searchActiveSys);
						ipPortLogPage.setCount(Long.valueOf(hivePageCount));
					}
					ipPortLogPage.setPageNo(page.getPageNo());
					ipPortLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					long startTime = System.currentTimeMillis();
					logger1.info("Redis查询开始：" + startTime);

					ipPortLogPage = (Page<DfIpPortLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);

					long endTime = System.currentTimeMillis();
					logger1.info(ipPortLogPage.getList().size());
					logger1.info("Redis查询结束：" + endTime);
					logger1.info("Redis时长：" + ((endTime - startTime) / 1000));
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					long startTime = System.currentTimeMillis();
					logger1.info("orcl查询开始时间：" + startTime);
					ipPortLogPage = dfLogService.findIpPortPage(
							new Page<DfIpPortLog>(request, response, DfIpPortLog.class), ipPortLog, searchActiveSys);
					long endTime = System.currentTimeMillis();
					logger1.info("orcl查询结束时间：" + endTime);
					logger1.info("orcl时长：" + ((endTime - startTime) / 1000));
					if (Constants.IS_OPEN_REDIS) {
						logger1.info("redis存储开始时间：" + endTime);
						new SaveRedisThread(key, ipPortLogPage, Constants.ORACLE_EXPIRE).start();
						logger1.info("redis存储结束时间：" + endTime);
					}
				}
			}

		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "端口封堵日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "端口封堵日志检索成功",
				ipPortLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/dfHttpReqLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP协议请求封堵分页获取", httpMethod = "GET", notes = "get log list")
	public Map dfHttpReqLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			// @RequestParam(value = "type", required = false, defaultValue =
			// "oracle") String type,
			Page page, DfHttpReqLog httpReqLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfHttpReqLog> httpReqLogPage = new Page<DfHttpReqLog>();
		try {
			resetTime(httpReqLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, httpReqLog, DfHttpReqLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpReqLog.getSearchFoundStartTime(),
							httpReqLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (httpReqLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (httpReqLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						httpReqLog.setTableName(Configurations.getStringProperty(DfHttpReqLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						httpReqLog.setTableName(Configurations
								.getStringProperty(DfHttpReqLog.class.getSimpleName() + "B", "t_xa_df_http_req_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						httpReqLogPage = (Page<DfHttpReqLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(httpReqLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						httpReqLogPage = dfLogService.findHttpReqPageCluster(
								new Page<DfHttpReqLog>(request, response, DfHttpReqLog.class), httpReqLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, httpReqLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfHttpReqLog> list = new ArrayList<DfHttpReqLog>();
						httpReqLogPage = new Page<DfHttpReqLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfHttpReqLog bean = (DfHttpReqLog) JsonMapper.fromJsonString(str, DfHttpReqLog.class);
								list.add(bean);
							}
							httpReqLogPage.setList(list);
	
						} else {
							httpReqLogPage.setList(new ArrayList());
						}
					} else {
	
						httpReqLogPage = new Page<DfHttpReqLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfHttpReqLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, httpReqLog, "DF_HTTP_REQ_LOG", getCol2Col(),
								orderBy, searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfHttpReqLog.class,
								"foundTime", "recvTime");
						if (tableMapping == null) {
							httpReqLogPage.setList(new ArrayList());
						} else {
							List strList = new ArrayList();
							if (tableMapping.get("obj").size() > page.getPageSize()) {
								strList = tableMapping.get("obj").subList(0, page.getPageSize());
							} else {
								strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
							}
							if (strList.size() > 0) {
								String jsonString = JsonMapper.toJsonString(strList);
								List<DfHttpReqLog> List = (java.util.List<DfHttpReqLog>) JsonMapper.fromJsonList(jsonString,
										DfHttpReqLog.class);
								httpReqLogPage.setList(List);
							} else {
								httpReqLogPage.setList(new ArrayList());
							}
						}
					}
	
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							httpReqLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(httpReqLog, countKey, "DF_HTTP_REQ_LOG",
									getCol2Col(), searchActiveSys);
							httpReqLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpReqLog, countKey, "DF_HTTP_REQ_LOG",
								getCol2Col(), searchActiveSys);
						httpReqLogPage.setCount(Long.valueOf(hivePageCount));
					}
	
					httpReqLogPage.setPageNo(page.getPageNo());
					httpReqLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}

				// 存在则直接从redis中查询
				if (keyExists) {
					httpReqLogPage = (Page<DfHttpReqLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					httpReqLogPage = dfLogService.findHttpReqPage(
							new Page<DfHttpReqLog>(request, response, DfHttpReqLog.class), httpReqLog,
							searchActiveSys);
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

	@RequestMapping(value = "/dfHttpResLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP协议响应封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfHttpResLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfHttpResLog httpResLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfHttpResLog> httpResLogPage = new Page<DfHttpResLog>();

		try {
			resetTime(httpResLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, httpResLog, DfHttpResLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpResLog.getSearchFoundStartTime(),
							httpResLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (httpResLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (httpResLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						httpResLog.setTableName(Configurations.getStringProperty(DfHttpResLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						httpResLog.setTableName(Configurations
								.getStringProperty(DfHttpResLog.class.getSimpleName() + "B", "t_xa_df_http_res_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						httpResLogPage = (Page<DfHttpResLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(httpResLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						httpResLogPage = dfLogService.findHttpResPageCluster(
								new Page<DfHttpResLog>(request, response, DfHttpResLog.class), httpResLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, httpResLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfHttpResLog> list = new ArrayList<DfHttpResLog>();
						httpResLogPage = new Page<DfHttpResLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfHttpResLog bean = (DfHttpResLog) JsonMapper.fromJsonString(str, DfHttpResLog.class);
								list.add(bean);
							}
							httpResLogPage.setList(list);
						} else {
							httpResLogPage.setList(new ArrayList());
						}
					} else {
	
						httpResLogPage = new Page<DfHttpResLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfHttpResLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, httpResLog, "DF_HTTP_RES_LOG", getCol2Col(),
								orderBy, searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfHttpResLog.class,
								"foundTime", "recvTime");
						if (tableMapping == null) {
							httpResLogPage.setList(new ArrayList());
						} else {
							List strList = new ArrayList();
							if (tableMapping.get("obj").size() > page.getPageSize()) {
								strList = tableMapping.get("obj").subList(0, page.getPageSize());
							} else {
								strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
							}
							if (strList.size() > 0) {
								String jsonString = JsonMapper.toJsonString(strList);
								List<DfHttpResLog> List = (java.util.List<DfHttpResLog>) JsonMapper.fromJsonList(jsonString,
										DfHttpResLog.class);
								httpResLogPage.setList(List);
							} else {
								httpResLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							httpResLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(httpResLog, countKey, "DF_HTTP_RES_LOG",
									getCol2Col(), searchActiveSys);
							httpResLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpResLog, countKey, "DF_HTTP_RES_LOG",
								getCol2Col(), searchActiveSys);
						httpResLogPage.setCount(Long.valueOf(hivePageCount));
					}
					httpResLogPage.setPageNo(page.getPageNo());
					httpResLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					httpResLogPage = (Page<DfHttpResLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					httpResLogPage = dfLogService.findHttpResPage(
							new Page<DfHttpResLog>(request, response, DfHttpResLog.class), httpResLog, searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, httpResLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "HTTP协议响应日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);

		}

		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "HTTP协议响应日志检索成功",
				httpResLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/dfHttpKeywordLogs", method = RequestMethod.GET)
	@ApiOperation(value = "HTTP协议关键字、请求多域、响应多域封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfHttpKeywordLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfHttpKeywordLog httpKeywordLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		String searchServiceType = httpKeywordLog.getSearchServiceType();
		if (StringUtils.isEmpty(searchServiceType)) {
			httpKeywordLog.setSearchServiceType("5"); // 0x05
		}
		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DfHttpKeywordLog> httpKeywordLogPage = new Page<DfHttpKeywordLog>();

		try {
			resetTime(httpKeywordLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, httpKeywordLog, DfHttpKeywordLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpKeywordLog.getSearchFoundStartTime(),
							httpKeywordLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (httpKeywordLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (httpKeywordLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						httpKeywordLog.setTableName(Configurations.getStringProperty(DfHttpKeywordLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						httpKeywordLog.setTableName(Configurations
								.getStringProperty(DfHttpKeywordLog.class.getSimpleName() + "B", "t_xa_df_http_keyword_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						httpKeywordLogPage = (Page<DfHttpKeywordLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(httpKeywordLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						httpKeywordLogPage = dfLogService.findHttpKeywordPageCluster(
								new Page<DfHttpKeywordLog>(request, response, DfHttpKeywordLog.class), httpKeywordLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, httpKeywordLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfHttpKeywordLog> list = new ArrayList<DfHttpKeywordLog>();
						httpKeywordLogPage = new Page<DfHttpKeywordLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfHttpKeywordLog bean = (DfHttpKeywordLog) JsonMapper.fromJsonString(str,
										DfHttpKeywordLog.class);
								list.add(bean);
							}
							httpKeywordLogPage.setList(list);
	
						} else {
							httpKeywordLogPage.setList(new ArrayList());
						}
					} else {
	
						httpKeywordLogPage = new Page<DfHttpKeywordLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfHttpKeywordLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, httpKeywordLog, "DF_HTTP_KEYWORD_LOG",
								getCol2Col(), orderBy, searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfHttpKeywordLog.class,
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
								List<DfHttpKeywordLog> List = (java.util.List<DfHttpKeywordLog>) JsonMapper
										.fromJsonList(jsonString, DfHttpKeywordLog.class);
								httpKeywordLogPage.setList(List);
	
							} else {
								httpKeywordLogPage.setList(new ArrayList());
							}
	
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							httpKeywordLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(httpKeywordLog, countKey,
									"DF_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
							httpKeywordLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpKeywordLog, countKey,
								"DF_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
						httpKeywordLogPage.setCount(Long.valueOf(hivePageCount));
					}
					httpKeywordLogPage.setPageNo(page.getPageNo());
					httpKeywordLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					httpKeywordLogPage = (Page<DfHttpKeywordLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					httpKeywordLogPage = dfLogService.findHttpKeywordPage(
							new Page<DfHttpKeywordLog>(request, response, DfHttpKeywordLog.class), httpKeywordLog,
							searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, httpKeywordLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "HTTP协议关键字、请求多域、响应多域封堵日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		return serviceResponse(thread, System.currentTimeMillis() - start, request, response,
				"HTTP协议关键字、请求多域、响应多域封堵日志检索成功", httpKeywordLogPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/dfHttpReqMultiPartLogs", method = RequestMethod.GET)
	@ApiOperation(value = "Http协议请求多域封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfHttpReqMultiPartLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfHttpKeywordLog httpReqMultiLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		httpReqMultiLog.setSearchServiceType("17"); // 0x11

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DfHttpKeywordLog> httpReqMultiPage = new Page<DfHttpKeywordLog>();

		try {
			resetTime(httpReqMultiLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, httpReqMultiLog, DfHttpKeywordLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpReqMultiLog.getSearchFoundStartTime(),
							httpReqMultiLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DfHttpKeywordLog> list = new ArrayList<DfHttpKeywordLog>();
					httpReqMultiPage = new Page<DfHttpKeywordLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						for (String str : strList) {
							DfHttpKeywordLog bean = (DfHttpKeywordLog) JsonMapper.fromJsonString(str,
									DfHttpKeywordLog.class);
							list.add(bean);
						}
						httpReqMultiPage.setList(list);

					} else {
						httpReqMultiPage.setList(new ArrayList());
					}
				} else {

					httpReqMultiPage = new Page<DfHttpKeywordLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DfHttpKeywordLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, httpReqMultiLog, "DF_HTTP_KEYWORD_LOG",
							getCol2Col(), orderBy, searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfHttpKeywordLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						httpReqMultiPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DfHttpKeywordLog> List = (java.util.List<DfHttpKeywordLog>) JsonMapper
									.fromJsonList(jsonString, DfHttpKeywordLog.class);
							httpReqMultiPage.setList(List);

						} else {
							httpReqMultiPage.setList(new ArrayList());
						}

					}
				}
				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						httpReqMultiPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpReqMultiLog, countKey,
								"DF_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
						httpReqMultiPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(httpReqMultiLog, countKey,
							"DF_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
					httpReqMultiPage.setCount(Long.valueOf(hivePageCount));
				}
				httpReqMultiPage.setPageNo(page.getPageNo());
				httpReqMultiPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					httpReqMultiPage = (Page<DfHttpKeywordLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					httpReqMultiPage = dfLogService.findHttpMultiPartPage(
							new Page<DfHttpKeywordLog>(request, response, DfHttpKeywordLog.class), httpReqMultiLog,
							searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, httpReqMultiPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "Http协议请求多域封堵日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "Http协议请求多域封堵日志检索成功",
				httpReqMultiPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/dfHttpResMultiPartLogs", method = RequestMethod.GET)
	@ApiOperation(value = "Http协议响应多域封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfHttpResMultiPartLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfHttpKeywordLog httpResMultiLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
			searchActiveSys = Constants.ACTIVESYS_B;
			whichHive = "&HIVEB";
		} else {
			whichHive = "&HIVEA";
		}

		httpResMultiLog.setSearchServiceType("18"); // 0x12

		long start = System.currentTimeMillis();
		SaveRequestLogThread thread = super.saveRequestLog(servicesRequestLogService, Constants.OPACTION_GET, request,
				null);
		boolean keyExists = false;
		String key = "";
		String countKey = "";
		Page<DfHttpKeywordLog> httpResMultiPage = new Page<DfHttpKeywordLog>();

		try {
			resetTime(httpResMultiLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, httpResMultiLog, DfHttpKeywordLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(httpResMultiLog.getSearchFoundStartTime(),
							httpResMultiLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DfHttpKeywordLog> list = new ArrayList<DfHttpKeywordLog>();
					httpResMultiPage = new Page<DfHttpKeywordLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						for (String str : strList) {
							DfHttpKeywordLog bean = (DfHttpKeywordLog) JsonMapper.fromJsonString(str,
									DfHttpKeywordLog.class);
							list.add(bean);
						}
						httpResMultiPage.setList(list);

					} else {
						httpResMultiPage.setList(new ArrayList());
					}
				} else {

					httpResMultiPage = new Page<DfHttpKeywordLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DfHttpKeywordLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, httpResMultiLog, "DF_HTTP_KEYWORD_LOG",
							getCol2Col(), orderBy, searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfHttpKeywordLog.class,
							"foundTime", "recvTime");
					if (tableMapping == null) {
						httpResMultiPage.setList(new ArrayList());
					} else {
						List strList = new ArrayList();
						if (tableMapping.get("obj").size() > page.getPageSize()) {
							strList = tableMapping.get("obj").subList(0, page.getPageSize());
						} else {
							strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
						}
						if (strList.size() > 0) {
							String jsonString = JsonMapper.toJsonString(strList);
							List<DfHttpKeywordLog> List = (java.util.List<DfHttpKeywordLog>) JsonMapper
									.fromJsonList(jsonString, DfHttpKeywordLog.class);
							httpResMultiPage.setList(List);
						} else {
							httpResMultiPage.setList(new ArrayList());
						}
					}
				}

				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						httpResMultiPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(httpResMultiLog, countKey,
								"DF_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
						httpResMultiPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(httpResMultiLog, countKey,
							"DF_HTTP_KEYWORD_LOG", getCol2Col(), searchActiveSys);
					httpResMultiPage.setCount(Long.valueOf(hivePageCount));
				}
				httpResMultiPage.setPageNo(page.getPageNo());
				httpResMultiPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					httpResMultiPage = (Page<DfHttpKeywordLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					httpResMultiPage = dfLogService.findHttpMultiPartPage(
							new Page<DfHttpKeywordLog>(request, response, DfHttpKeywordLog.class), httpResMultiLog,
							searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, httpResMultiPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "Http协议响应多域封堵日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "Http协议响应多域封堵日志检索成功",
				httpResMultiPage, searchActiveSys, logSource);
	}

	@RequestMapping(value = "/dfMailLogs", method = RequestMethod.GET)
	@ApiOperation(value = "MAIL封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfMailLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfMailLog mailLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfMailLog> mailLogPage = new Page<DfMailLog>();

		try {
			resetTime(mailLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, mailLog, DfMailLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(mailLog.getSearchFoundStartTime(),
					mailLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (mailLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (mailLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						mailLog.setTableName(Configurations.getStringProperty(DfMailLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						mailLog.setTableName(Configurations
								.getStringProperty(DfMailLog.class.getSimpleName() + "B", "t_xa_df_mail_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						mailLogPage = (Page<DfMailLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(mailLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						mailLogPage = dfLogService.findMailPageCluster(
								new Page<DfMailLog>(request, response, DfMailLog.class), mailLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, mailLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfMailLog> list = new ArrayList<DfMailLog>();
						mailLogPage = new Page<DfMailLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfMailLog bean = (DfMailLog) JsonMapper.fromJsonString(str, DfMailLog.class);
								list.add(bean);
							}
							mailLogPage.setList(list);
	
						} else {
							mailLogPage.setList(new ArrayList());
						}
					} else {
	
						mailLogPage = new Page<DfMailLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfMailLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, mailLog, "DF_MAIL_LOG", getCol2Col(), orderBy,
								searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfMailLog.class, "foundTime",
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
								List<DfMailLog> List = (java.util.List<DfMailLog>) JsonMapper.fromJsonList(jsonString,
										DfMailLog.class);
								mailLogPage.setList(List);
							} else {
								mailLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							mailLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(mailLog, countKey, "DF_MAIL_LOG",
									getCol2Col(), searchActiveSys);
							mailLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(mailLog, countKey, "DF_MAIL_LOG", getCol2Col(),
								searchActiveSys);
						mailLogPage.setCount(Long.valueOf(hivePageCount));
					}
	
					mailLogPage.setPageNo(page.getPageNo());
					mailLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					mailLogPage = (Page<DfMailLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					mailLogPage = dfLogService.findMailPage(new Page<DfMailLog>(request, response, DfMailLog.class),
							mailLog, searchActiveSys);

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

	@RequestMapping(value = "/dfDnsLogs", method = RequestMethod.GET)
	@ApiOperation(value = "DNS封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfDnsLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfDnsLog dnsLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		logger1.info("dfDnsLogList接口请求方法开始----" + System.currentTimeMillis());
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfDnsLog> dnsLogPage = new Page<DfDnsLog>();

		try {
			resetTime(dnsLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, dnsLog, DfDnsLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(dnsLog.getSearchFoundStartTime(),
					dnsLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logger1.info("hive开始----" + System.currentTimeMillis());
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (dnsLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (dnsLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						dnsLog.setTableName(Configurations.getStringProperty(DfDnsLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						dnsLog.setTableName(Configurations
								.getStringProperty(DfDnsLog.class.getSimpleName() + "B", "t_xa_df_dns_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						dnsLogPage = (Page<DfDnsLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(dnsLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						dnsLogPage = dfLogService.findDnsPageCluster(
								new Page<DfDnsLog>(request, response, DfDnsLog.class), dnsLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, dnsLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfDnsLog> list = new ArrayList<DfDnsLog>();
						dnsLogPage = new Page<DfDnsLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfDnsLog bean = (DfDnsLog) JsonMapper.fromJsonString(str, DfDnsLog.class);
								list.add(bean);
							}
							dnsLogPage.setList(list);
	
						} else {
							dnsLogPage.setList(new ArrayList());
						}
					} else {
	
						dnsLogPage = new Page<DfDnsLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfDnsLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, dnsLog, "DF_DNS_LOG", getCol2Col(), orderBy,
								searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfDnsLog.class, "foundTime",
								"recvTime");
						if (tableMapping == null) {
							dnsLogPage.setList(new ArrayList());
						} else {
							List strList = new ArrayList();
							if (tableMapping.get("obj").size() > page.getPageSize()) {
								strList = tableMapping.get("obj").subList(0, page.getPageSize());
							} else {
								strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
							}
							if (strList.size() > 0) {
								String jsonString = JsonMapper.toJsonString(strList);
								List<DfDnsLog> List = (java.util.List<DfDnsLog>) JsonMapper.fromJsonList(jsonString,
										DfDnsLog.class);
								dnsLogPage.setList(List);
	
							} else {
								dnsLogPage.setList(new ArrayList());
							}
	
						}
					}
	
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							dnsLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(dnsLog, countKey, "DF_DNS_LOG",
									getCol2Col(), searchActiveSys);
							dnsLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(dnsLog, countKey, "DF_DNS_LOG", getCol2Col(),
								searchActiveSys);
						dnsLogPage.setCount(Long.valueOf(hivePageCount));
					}
	
					dnsLogPage.setPageNo(page.getPageNo());
					dnsLogPage.setPageSize(page.getPageSize());
					logger1.info("hive结束----" + System.currentTimeMillis());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					logger1.info("redis Key校验开始----" + System.currentTimeMillis());
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
					logger1.info("redis Key校验结束----" + System.currentTimeMillis());
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					logger1.info("redis查询开始----" + System.currentTimeMillis());
					dnsLogPage = (Page<DfDnsLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
					logger1.info("redis查询结束----" + System.currentTimeMillis());
				} else {
					logger1.info("oracle查询开始----" + System.currentTimeMillis());
					// 不存在则查询数据库并保存查询结果到redis中
					dnsLogPage = dfLogService.findDnsPage(new Page<DfDnsLog>(request, response, DfDnsLog.class), dnsLog,
							searchActiveSys);
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
			logger1.info("dfDnsLogList接口请求方法结束----" + System.currentTimeMillis());
		}
		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "DNS日志检索成功", dnsLogPage,
				searchActiveSys, logSource);
	}

	@RequestMapping(value = "/dfFtpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "FTP封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfFtpLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfFtpLog ftpLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfFtpLog> ftpLogPage = new Page<DfFtpLog>();

		try {
			resetTime(ftpLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, ftpLog, DfFtpLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(ftpLog.getSearchFoundStartTime(),
					ftpLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// if (true) {
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int endNum = startNum + page.getPageSize() - 1;
				if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, true) + whichHive;
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				if (keyExists) {
					List<DfFtpLog> list = new ArrayList<DfFtpLog>();
					ftpLogPage = new Page<DfFtpLog>();
					List<String> strList = redisDao.getList(key, startNum, endNum);
					if (null != strList && strList.size() > 0) {
						for (String str : strList) {
							DfFtpLog bean = (DfFtpLog) JsonMapper.fromJsonString(str, DfFtpLog.class);
							list.add(bean);
						}

						ftpLogPage.setList(list);

					} else {
						ftpLogPage.setList(new ArrayList());
					}
				} else {

					ftpLogPage = new Page<DfFtpLog>();
					String orderBy = "";
					if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
						orderBy = page.getOrderBySql(DfFtpLog.class.getSimpleName(), page.getOrderBy());
					} else {
						orderBy = "found_Time";
					}
					ResultSet rs = HiveSqlService.getResultSet(page, ftpLog, "DF_FTP_LOG", getCol2Col(), orderBy,
							searchActiveSys);
					Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfFtpLog.class, "foundTime",
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
							List<DfFtpLog> List = (java.util.List<DfFtpLog>) JsonMapper.fromJsonList(jsonString,
									DfFtpLog.class);
							ftpLogPage.setList(List);

						} else {
							ftpLogPage.setList(new ArrayList());
						}
					}
				}
				if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
					countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
					// 判断key是否存在
					boolean countKeyExists = redisDao.exists(countKey);
					if (countKeyExists) {
						String count = redisDao.getString(countKey);
						ftpLogPage.setCount(Long.valueOf(count));
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(ftpLog, countKey, "DF_FTP_LOG",
								getCol2Col(), searchActiveSys);
						ftpLogPage.setCount(Long.valueOf(hivePageCount));
					}
				} else {
					Long hivePageCount = HiveSqlService.getHivePageCount(ftpLog, countKey, "DF_FTP_LOG", getCol2Col(),
							searchActiveSys);
					ftpLogPage.setCount(Long.valueOf(hivePageCount));
				}
				ftpLogPage.setPageNo(page.getPageNo());
				ftpLogPage.setPageSize(page.getPageSize());
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					ftpLogPage = (Page<DfFtpLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					ftpLogPage = dfLogService.findFtpPage(new Page<DfFtpLog>(request, response, DfFtpLog.class), ftpLog,
							searchActiveSys);

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

	@RequestMapping(value = "/dfPPTPLogs", method = RequestMethod.GET)
	@ApiOperation(value = "PPTP封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfPptpLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfPptpLog pptpLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfPptpLog> pptpLogPage = new Page<DfPptpLog>();

		try {
			resetTime(pptpLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, pptpLog, DfPptpLog.class, page);

			// 单独对PPTP的EncryptMode字段校验
			if (!StringUtil.isBlank(pptpLog.getSearchEncryptMode())) {
				Integer.parseInt(pptpLog.getSearchEncryptMode());
			}
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(pptpLog.getSearchFoundStartTime(),
					pptpLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (pptpLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (pptpLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						pptpLog.setTableName(Configurations.getStringProperty(DfPptpLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						pptpLog.setTableName(Configurations
								.getStringProperty(DfPptpLog.class.getSimpleName() + "B", "t_xa_df_pptp_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						pptpLogPage = (Page<DfPptpLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(pptpLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						pptpLogPage = dfLogService.findPptpPageCluster(
								new Page<DfPptpLog>(request, response, DfPptpLog.class), pptpLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, pptpLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfPptpLog> list = new ArrayList<DfPptpLog>();
						pptpLogPage = new Page<DfPptpLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfPptpLog bean = (DfPptpLog) JsonMapper.fromJsonString(str, DfPptpLog.class);
								list.add(bean);
							}
							pptpLogPage.setList(list);
	
						} else {
							pptpLogPage.setList(new ArrayList());
						}
					} else {
	
						pptpLogPage = new Page<DfPptpLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfPptpLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, pptpLog, "DF_PPTP_LOG", getCol2Col(), orderBy,
								searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfPptpLog.class, "foundTime",
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
								List<DfPptpLog> List = (java.util.List<DfPptpLog>) JsonMapper.fromJsonList(jsonString,
										DfPptpLog.class);
								pptpLogPage.setList(List);
	
							} else {
								pptpLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							pptpLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(pptpLog, countKey, "DF_PPTP_LOG",
									getCol2Col(), searchActiveSys);
							pptpLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(pptpLog, countKey, "DF_PPTP_LOG", getCol2Col(),
								searchActiveSys);
						pptpLogPage.setCount(Long.valueOf(hivePageCount));
					}
					pptpLogPage.setPageNo(page.getPageNo());
					pptpLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					pptpLogPage = (Page<DfPptpLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					pptpLogPage = dfLogService.findPptpPage(new Page<DfPptpLog>(request, response, DfPptpLog.class),
							pptpLog, searchActiveSys);

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

	@RequestMapping(value = "/dfL2tpLogs", method = RequestMethod.GET)
	@ApiOperation(value = "L2TP封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfL2tpLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfL2tpLog l2tpLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfL2tpLog> l2tpLogPage = new Page<DfL2tpLog>();

		try {
			resetTime(l2tpLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, l2tpLog, DfL2tpLog.class, page);

			// 单独对Ipsec的EncryptMode字段校验
			if (!StringUtil.isBlank(l2tpLog.getSearchEncryptMode())) {
				Integer.parseInt(l2tpLog.getSearchEncryptMode());
			}
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(l2tpLog.getSearchFoundStartTime(),
					l2tpLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (l2tpLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (l2tpLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						l2tpLog.setTableName(Configurations.getStringProperty(DfL2tpLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						l2tpLog.setTableName(Configurations
								.getStringProperty(DfL2tpLog.class.getSimpleName() + "B", "t_xa_df_l2tp_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						l2tpLogPage = (Page<DfL2tpLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(l2tpLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						l2tpLogPage = dfLogService.findL2tpPageCluster(
								new Page<DfL2tpLog>(request, response, DfL2tpLog.class), l2tpLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, l2tpLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfL2tpLog> list = new ArrayList<DfL2tpLog>();
						l2tpLogPage = new Page<DfL2tpLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfL2tpLog bean = (DfL2tpLog) JsonMapper.fromJsonString(str, DfL2tpLog.class);
								list.add(bean);
							}
	
							l2tpLogPage.setList(list);
	
						} else {
							l2tpLogPage.setList(new ArrayList());
						}
					} else {
	
						l2tpLogPage = new Page<DfL2tpLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfL2tpLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, l2tpLog, "DF_L2TP_LOG", getCol2Col(), orderBy,
								searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfL2tpLog.class, "foundTime",
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
								List<DfL2tpLog> List = (java.util.List<DfL2tpLog>) JsonMapper.fromJsonList(jsonString,
										DfL2tpLog.class);
								l2tpLogPage.setList(List);
							} else {
								l2tpLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							l2tpLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(l2tpLog, countKey, "DF_L2TP_LOG",
									getCol2Col(), searchActiveSys);
							l2tpLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(l2tpLog, countKey, "DF_L2TP_LOG", getCol2Col(),
								searchActiveSys);
						l2tpLogPage.setCount(Long.valueOf(hivePageCount));
					}
					l2tpLogPage.setPageNo(page.getPageNo());
					l2tpLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					l2tpLogPage = (Page<DfL2tpLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					l2tpLogPage = dfLogService.findL2tpPage(new Page<DfL2tpLog>(request, response, DfL2tpLog.class),
							l2tpLog, searchActiveSys);

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

	@RequestMapping(value = "/dfIpsecLogs", method = RequestMethod.GET)
	@ApiOperation(value = "IPSEC封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfIpsecLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfIpsecLog ipsecLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfIpsecLog> ipsecLogPage = new Page<DfIpsecLog>();

		try {
			resetTime(ipsecLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, ipsecLog, DfIpsecLog.class, page);

			// 单独对Ipsec的exProtocol字段校验
			if (!StringUtil.isBlank(ipsecLog.getSearchExProtocol())) {
				Integer.parseInt(ipsecLog.getSearchExProtocol());
			}
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(ipsecLog.getSearchFoundStartTime(),
					ipsecLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (ipsecLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (ipsecLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						ipsecLog.setTableName(Configurations.getStringProperty(DfIpsecLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						ipsecLog.setTableName(Configurations
								.getStringProperty(DfIpsecLog.class.getSimpleName() + "B", "t_xa_df_ipsec_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						ipsecLogPage = (Page<DfIpsecLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(ipsecLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						ipsecLogPage = dfLogService.findIpsecPageCluster(
								new Page<DfIpsecLog>(request, response, DfIpsecLog.class), ipsecLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, ipsecLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfIpsecLog> list = new ArrayList<DfIpsecLog>();
						ipsecLogPage = new Page<DfIpsecLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfIpsecLog bean = (DfIpsecLog) JsonMapper.fromJsonString(str, DfIpsecLog.class);
								list.add(bean);
							}
	
							ipsecLogPage.setList(list);
	
						} else {
							ipsecLogPage.setList(new ArrayList());
						}
					} else {
	
						ipsecLogPage = new Page<DfIpsecLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfIpsecLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, ipsecLog, "DF_IPSEC_LOG", getCol2Col(), orderBy,
								searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfIpsecLog.class, "foundTime",
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
								List<DfIpsecLog> List = (java.util.List<DfIpsecLog>) JsonMapper.fromJsonList(jsonString,
										DfIpsecLog.class);
								ipsecLogPage.setList(List);
	
							} else {
								ipsecLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							ipsecLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(ipsecLog, countKey, "DF_IPSEC_LOG",
									getCol2Col(), searchActiveSys);
							ipsecLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(ipsecLog, countKey, "DF_IPSEC_LOG",
								getCol2Col(), searchActiveSys);
						ipsecLogPage.setCount(Long.valueOf(hivePageCount));
					}
					ipsecLogPage.setPageNo(page.getPageNo());
					ipsecLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					ipsecLogPage = (Page<DfIpsecLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					ipsecLogPage = dfLogService.findIpsecPage(new Page<DfIpsecLog>(request, response, DfIpsecLog.class),
							ipsecLog, searchActiveSys);

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

	@RequestMapping(value = "/dfOpenVpnLogs", method = RequestMethod.GET)
	@ApiOperation(value = "OPENVPN封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfOpenvpnLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfOpenvpnLog openvpnLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfOpenvpnLog> openvpnLogPage = new Page<DfOpenvpnLog>();

		try {
			resetTime(openvpnLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, openvpnLog, DfOpenvpnLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(openvpnLog.getSearchFoundStartTime(),
							openvpnLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (openvpnLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (openvpnLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						openvpnLog.setTableName(Configurations.getStringProperty(DfOpenvpnLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						openvpnLog.setTableName(Configurations
								.getStringProperty(DfOpenvpnLog.class.getSimpleName() + "B", "t_xa_df_openvpn_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						openvpnLogPage = (Page<DfOpenvpnLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(openvpnLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						openvpnLogPage = dfLogService.findOpenvpnPageCluster(
								new Page<DfOpenvpnLog>(request, response, DfOpenvpnLog.class), openvpnLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, openvpnLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfOpenvpnLog> list = new ArrayList<DfOpenvpnLog>();
						openvpnLogPage = new Page<DfOpenvpnLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfOpenvpnLog bean = (DfOpenvpnLog) JsonMapper.fromJsonString(str, DfOpenvpnLog.class);
								list.add(bean);
							}
	
							openvpnLogPage.setList(list);
	
						} else {
							openvpnLogPage.setList(new ArrayList());
						}
					} else {
	
						openvpnLogPage = new Page<DfOpenvpnLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfOpenvpnLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, openvpnLog, "DF_OPENVPN_LOG", getCol2Col(),
								orderBy, searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfOpenvpnLog.class,
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
								List<DfOpenvpnLog> List = (java.util.List<DfOpenvpnLog>) JsonMapper.fromJsonList(jsonString,
										DfOpenvpnLog.class);
								openvpnLogPage.setList(List);
	
							} else {
	
								openvpnLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							openvpnLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(openvpnLog, countKey, "DF_OPENVPN_LOG",
									getCol2Col(), searchActiveSys);
							openvpnLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(openvpnLog, countKey, "DF_OPENVPN_LOG",
								getCol2Col(), searchActiveSys);
						openvpnLogPage.setCount(Long.valueOf(hivePageCount));
					}
					openvpnLogPage.setPageNo(page.getPageNo());
					openvpnLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}

				if (keyExists) {
					openvpnLogPage = (Page<DfOpenvpnLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					openvpnLogPage = dfLogService.findOpenvpnPage(
							new Page<DfOpenvpnLog>(request, response, DfOpenvpnLog.class), openvpnLog, searchActiveSys);

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

	@RequestMapping(value = "/dfSSHLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSH封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfSshLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfSshLog sshLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfSshLog> sshLogPage = new Page<DfSshLog>();

		try {
			resetTime(sshLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, sshLog, DfSshLog.class, page);

			// 单独对SSH的EncryptMode字段校验
			if (!StringUtil.isBlank(sshLog.getSearchEncryptMode())) {
				Long.parseLong(sshLog.getSearchEncryptMode());
			}
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(sshLog.getSearchFoundStartTime(),
					sshLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (sshLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (sshLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						sshLog.setTableName(Configurations.getStringProperty(DfSshLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						sshLog.setTableName(Configurations
								.getStringProperty(DfSshLog.class.getSimpleName() + "B", "t_xa_df_ssh_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						sshLogPage = (Page<DfSshLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(sshLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						sshLogPage = dfLogService.findSshPageCluster(
								new Page<DfSshLog>(request, response, DfSshLog.class), sshLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, sshLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfSshLog> list = new ArrayList<DfSshLog>();
						sshLogPage = new Page<DfSshLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfSshLog bean = (DfSshLog) JsonMapper.fromJsonString(str, DfSshLog.class);
								list.add(bean);
							}
	
							sshLogPage.setList(list);
	
						} else {
							sshLogPage.setList(new ArrayList());
						}
					} else {
	
						sshLogPage = new Page<DfSshLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfSshLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, sshLog, "DF_SSH_LOG", getCol2Col(), orderBy,
								searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfSshLog.class, "foundTime",
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
								List<DfSshLog> List = (java.util.List<DfSshLog>) JsonMapper.fromJsonList(jsonString,
										DfSshLog.class);
								sshLogPage.setList(List);
							} else {
								sshLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							sshLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(sshLog, countKey, "DF_SSH_LOG",
									getCol2Col(), searchActiveSys);
							sshLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(sshLog, countKey, "DF_SSH_LOG", getCol2Col(),
								searchActiveSys);
						sshLogPage.setCount(Long.valueOf(hivePageCount));
					}
					sshLogPage.setPageNo(page.getPageNo());
					sshLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					sshLogPage = (Page<DfSshLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					sshLogPage = dfLogService.findSshPage(new Page<DfSshLog>(request, response, DfSshLog.class), sshLog,
							searchActiveSys);

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

	@RequestMapping(value = "/dfSSLLogs", method = RequestMethod.GET)
	@ApiOperation(value = "SSL封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfSslLogList(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfSslLog sslLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfSslLog> sslLogPage = new Page<DfSslLog>();
		try {
			resetTime(sslLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, sslLog, DfSslLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE || (HiveSqlService.ifTimeGreaterThan48(sslLog.getSearchFoundStartTime(),
					sslLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (sslLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (sslLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						sslLog.setTableName(Configurations.getStringProperty(DfSslLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						sslLog.setTableName(Configurations
								.getStringProperty(DfSslLog.class.getSimpleName() + "B", "t_xa_df_ssl_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						sslLogPage = (Page<DfSslLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(sslLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						sslLogPage = dfLogService.findSslPageCluster(
								new Page<DfSslLog>(request, response, DfSslLog.class), sslLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, sslLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfSslLog> list = new ArrayList<DfSslLog>();
						sslLogPage = new Page<DfSslLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfSslLog bean = (DfSslLog) JsonMapper.fromJsonString(str, DfSslLog.class);
								list.add(bean);
							}
	
							sslLogPage.setList(list);
	
						} else {
							sslLogPage.setList(new ArrayList());
						}
					} else {
	
						sslLogPage = new Page<DfSslLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfSslLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, sslLog, "DF_SSL_LOG", getCol2Col(), orderBy,
								searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfSslLog.class, "foundTime",
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
								List<DfSslLog> List = (java.util.List<DfSslLog>) JsonMapper.fromJsonList(jsonString,
										DfSslLog.class);
								sslLogPage.setList(List);
							} else {
								sslLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							sslLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(sslLog, countKey, "DF_SSL_LOG",
									getCol2Col(), searchActiveSys);
							sslLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(sslLog, countKey, "DF_SSL_LOG", getCol2Col(),
								searchActiveSys);
						sslLogPage.setCount(Long.valueOf(hivePageCount));
					}
					sslLogPage.setPageNo(page.getPageNo());
					sslLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					sslLogPage = (Page<DfSslLog>) JsonMapper.fromJsonString(redisDao.getString(key), Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					sslLogPage = dfLogService.findSslPage(new Page<DfSslLog>(request, response, DfSslLog.class), sslLog,
							searchActiveSys);

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

	@RequestMapping(value = "/dfTunnelRandomLogs", method = RequestMethod.GET)
	@ApiOperation(value = "隧道协议随机封堵日志获取", httpMethod = "GET", notes = "get log list")
	public Map dfTunnelRandomLogs(
			@RequestParam(value = "searchActiveSys", required = false, defaultValue = Constants.ACTIVESYS_B) String searchActiveSys,
			Page page, DfTunnelRandomLog tunnelRandomLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String whichHive = "&HIVEB";
		if (!Constants.ACTIVESYS_A.equals(searchActiveSys) && !Constants.ACTIVESYS_C.equals(searchActiveSys)) {
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
		Page<DfTunnelRandomLog> tunnelRandomLogPage = new Page<DfTunnelRandomLog>();
		try {
			resetTime(tunnelRandomLog);
			// 请求参数校验
			dfLogService.queryConditionCheck(thread, start, tunnelRandomLog, DfTunnelRandomLog.class, page);
			if (Constants.ONLY_SEL_FROM_HIVE
					|| (HiveSqlService.ifTimeGreaterThan48(tunnelRandomLog.getSearchFoundStartTime(),
							tunnelRandomLog.getSearchFoundEndTime()) && Constants.SEL_FROM_HIVE)) {// 从数据库中查询数据
				logSource = "1";
				// 神通数据库查询打开 && (( A版&&查询开始时间>神通数据库数据A最早时间)||(
				// B版&&查询开始时间>神通数据库数据B最早时间)）
				if (Constants.IS_SELECT_CLUSTER && ((Constants.ACTIVESYS_A.equals(searchActiveSys)
						&& (tunnelRandomLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_A_START_TIME)
						|| (Constants.ACTIVESYS_B.equals(searchActiveSys)
								&& (tunnelRandomLog.getSearchFoundStartTimeCluster()*1000) > Constants.CLUSTER_B_START_TIME))) {
					// 打开神通数据库
					logger.info("开启神通数据库---" + System.currentTimeMillis());
					CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_H);// 开启数据源F
					// 拼凑rediskeyA、B版
					if (Constants.ACTIVESYS_A.equals(searchActiveSys)) {
						whichHive = "&CLUSTERA";
						// A版表名动态设置
						tunnelRandomLog.setTableName(Configurations.getStringProperty(DfTunnelRandomLog.class.getSimpleName() + "A",
								"t_xa_df_ip_port_log_hit_mpp").trim());
					} else if (Constants.ACTIVESYS_B.equals(searchActiveSys)) {
						whichHive = "&CLUSTERB";
						// B版表名动态设置
						tunnelRandomLog.setTableName(Configurations
								.getStringProperty(DfTunnelRandomLog.class.getSimpleName() + "B", "t_xa_df_tunnel_random_log_mpp")
								.trim());
					}

					if (Constants.IS_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, false);
						// 加上AB版的条件
						key = key + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					// 存在则直接从redis中查询
					if (keyExists) {
						long startTime = System.currentTimeMillis();
						logger1.info("Redis查询开始：" + startTime);

						tunnelRandomLogPage = (Page<DfTunnelRandomLog>) JsonMapper.fromJsonString(redisDao.getString(key),
								Page.class);

						long endTime = System.currentTimeMillis();
						logger1.info(tunnelRandomLogPage.getList().size());
					} else {
						// 不存在则查询数据库并保存查询结果到redis中
						long startTime = System.currentTimeMillis();
						tunnelRandomLogPage = dfLogService.findTunnelRandomPageCluster(
								new Page<DfTunnelRandomLog>(request, response, DfTunnelRandomLog.class), tunnelRandomLog,
								searchActiveSys);
						long endTime = System.currentTimeMillis();
						if (Constants.IS_OPEN_REDIS) {
							new SaveRedisThread(key, tunnelRandomLogPage, Constants.ORACLE_EXPIRE).start();
						}
					}

				} else {
					int startNum = (page.getPageNo() - 1) * page.getPageSize();
					int endNum = startNum + page.getPageSize() - 1;
					if (Constants.IS_OPEN_REDIS && Constants.DATACENTER_OPEN_REDIS) {
						// 根据查询条件获取key
						key = dfLogService.getJedisKey(request, true) + whichHive;
						// 判断key是否存在
						keyExists = redisDao.exists(key);
					}
					if (keyExists) {
						List<DfTunnelRandomLog> list = new ArrayList<DfTunnelRandomLog>();
						tunnelRandomLogPage = new Page<DfTunnelRandomLog>();
						List<String> strList = redisDao.getList(key, startNum, endNum);
						if (null != strList && strList.size() > 0) {
							for (String str : strList) {
								DfTunnelRandomLog bean = (DfTunnelRandomLog) JsonMapper.fromJsonString(str,
										DfTunnelRandomLog.class);
								list.add(bean);
							}
	
							tunnelRandomLogPage.setList(list);
	
						} else {
							tunnelRandomLogPage.setList(new ArrayList());
						}
					} else {
	
						tunnelRandomLogPage = new Page<DfTunnelRandomLog>();
						String orderBy = "";
						if (null != page.getOrderBy() && !page.getOrderBy().equals("")) {
							orderBy = page.getOrderBySql(DfSslLog.class.getSimpleName(), page.getOrderBy());
						} else {
							orderBy = "found_Time";
						}
						ResultSet rs = HiveSqlService.getResultSet(page, tunnelRandomLog, "DF_TUNNEL_RANDOM_LOG",
								getCol2Col(), orderBy, searchActiveSys);
						Map<String, List> tableMapping = HiveJDBC.tableMapping(page, key, rs, DfSslLog.class, "foundTime",
								"recvTime");
						if (tableMapping == null) {
							tunnelRandomLogPage.setList(new ArrayList());
						} else {
							List strList = new ArrayList();
							if (tableMapping.get("obj").size() > page.getPageSize()) {
								strList = tableMapping.get("obj").subList(0, page.getPageSize());
							} else {
								strList = tableMapping.get("obj").subList(0, tableMapping.get("obj").size());
							}
							if (strList.size() > 0) {
								String jsonString = JsonMapper.toJsonString(strList);
								List<DfTunnelRandomLog> List = (java.util.List<DfTunnelRandomLog>) JsonMapper
										.fromJsonList(jsonString, DfTunnelRandomLog.class);
								tunnelRandomLogPage.setList(List);
							} else {
								tunnelRandomLogPage.setList(new ArrayList());
							}
						}
					}
					if (Constants.IS_OPEN_REDIS && Constants.IS_GET_HIVECOUNT) {
						countKey = dfLogService.getJedisKey(request, true) + "&pageCount" + whichHive;
						// 判断key是否存在
						boolean countKeyExists = redisDao.exists(countKey);
						if (countKeyExists) {
							String count = redisDao.getString(countKey);
							tunnelRandomLogPage.setCount(Long.valueOf(count));
						} else {
							Long hivePageCount = HiveSqlService.getHivePageCount(tunnelRandomLog, countKey,
									"DF_TUNNEL_RANDOM_LOG", getCol2Col(), searchActiveSys);
							tunnelRandomLogPage.setCount(Long.valueOf(hivePageCount));
						}
					} else {
						Long hivePageCount = HiveSqlService.getHivePageCount(tunnelRandomLog, countKey,
								"DF_TUNNEL_RANDOM_LOG", getCol2Col(), searchActiveSys);
						tunnelRandomLogPage.setCount(Long.valueOf(hivePageCount));
					}
					tunnelRandomLogPage.setPageNo(page.getPageNo());
					tunnelRandomLogPage.setPageSize(page.getPageSize());
				}
			} else {// 从oracle中查询数据
				if (Constants.IS_OPEN_REDIS) {
					// 根据查询条件获取key
					key = dfLogService.getJedisKey(request, false);
					// 判断key是否存在
					keyExists = redisDao.exists(key);
				}
				// 存在则直接从redis中查询
				if (keyExists) {
					tunnelRandomLogPage = (Page<DfTunnelRandomLog>) JsonMapper.fromJsonString(redisDao.getString(key),
							Page.class);
				} else {
					// 不存在则查询数据库并保存查询结果到redis中
					tunnelRandomLogPage = dfLogService.findTunnelRandomPage(
							new Page<DfTunnelRandomLog>(request, response, DfTunnelRandomLog.class), tunnelRandomLog,
							searchActiveSys);

					if (Constants.IS_OPEN_REDIS)
						new SaveRedisThread(key, tunnelRandomLogPage, Constants.ORACLE_EXPIRE).start();
				}
			}
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			e.printStackTrace();
			logger1.error(e);
			if (!(e instanceof RestServiceException)) {
				e = new RestServiceException(thread, System.currentTimeMillis() - start, "隧道协议随机封堵日志检索失败");
			}
			((RestServiceException) e).setActiveSys(searchActiveSys);
			((RestServiceException) e).setLogSource(logSource);
			throw ((RestServiceException) e);
		}

		return serviceResponse(thread, System.currentTimeMillis() - start, request, response, "隧道协议随机封堵日志检索成功",
				tunnelRandomLogPage, searchActiveSys, logSource);
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
	public void resetTime(LogEntity<?> entity) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> map = DateUtils.getLocalTime(entity.getSearchFoundStartTime(),
				entity.getSearchFoundEndTime(), Constants.LOG_LOCAL_TIME, "log");
		entity.setSearchFoundStartTime(map.get("startTime"));
		entity.setSearchFoundEndTime(map.get("endTime"));
		entity.setSearchFoundStartTimeCluster(sdf.parse(map.get("startTime")).getTime() / 1000);
		entity.setSearchFoundEndTimeCluster(sdf.parse(map.get("endTime")).getTime() / 1000);
	}
}
