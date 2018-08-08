package com.nis.web.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nis.domain.DfJitLogEntity;
import com.nis.domain.DfReportEntity;
import com.nis.domain.LogEntity;
import com.nis.domain.Page;
import com.nis.domain.StatLogEntity;
import com.nis.domain.restful.NtcReportEntity;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.StringUtil;
import com.nis.util.elasticsearch.ElasticsearchSqlDao;

/**
 * 基础日志Service基类
 * 
 * @author zbc
 * @version 2016-09-10
 */
@SuppressWarnings({ "rawtypes" })
public abstract class BaseLogService {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	protected ElasticsearchSqlDao elasticsearchSqlDao;

	/**
	 * wx checkCloumnIsExist(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param clazz
	 * @param page
	 * @throws Exception
	 *             void
	 * @exception @since
	 *                1.0.0
	 */
	public void checkCloumnIsExist(AuditLogThread thread, long start, Class clazz, Page page) throws Exception {
		String[] fieldsColoumn = null;
		String[] orderByColoumn = null;
		String notExistColumn = "";
		String orderByStr = "";
		// 所有字段名
		List<String> columnList = new ArrayList<String>();
		// 所有属性名
		List<String> propertyList = new ArrayList<String>();
		// Map<String, String> columnMap=new HashMap<String, String>();

		if (page != null) {
			// 解析Fileds的字段/属性名称
			if (!StringUtil.isBlank(page.getFields())) {
				fieldsColoumn = page.getFields().split(",");
			}
			// 解析orderBy的字段/属性名称
			if (!StringUtil.isBlank(page.getOrderBy())) {
				orderByStr = page.getOrderBy().replace(" asc", "");
				orderByStr = orderByStr.replace(" ASC", "");
				orderByStr = orderByStr.replace(" DESC", "");
				orderByStr = orderByStr.replace(" desc", "");
				orderByColoumn = orderByStr.split(",");
			}
			// 从resultMap中获取字段名称和属性名称
			if (fieldsColoumn != null || orderByColoumn != null) {
				SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
				ResultMap map = sqlSessionFactory.getConfiguration().getResultMap(clazz.getSimpleName() + "Map");
				List<ResultMapping> mapping = map.getResultMappings();
				for (ResultMapping mapp : mapping) {
					columnList.add(mapp.getColumn().toLowerCase());
					propertyList.add(mapp.getProperty());
				}
			}
			if (fieldsColoumn != null) {
				notExistColumn = "";
				for (String column : fieldsColoumn) {
					if (!StringUtil.isBlank(column)) {
						column = column.trim();
					} else {
						break;
					}
					// 数据库字段不区分大小写，属性区分大小写
					if (!columnList.contains(column.toLowerCase()) && !propertyList.contains(column)) {
						notExistColumn += "," + column;
					}
				}
				if (!StringUtil.isBlank(notExistColumn)) {
					notExistColumn = notExistColumn.substring(1);
					throw new RestServiceException(thread, System.currentTimeMillis() - start,
							"fields中" + notExistColumn + "的字段不存在!", RestBusinessCode.param_formate_error.getValue());
				}
			}
			if (orderByColoumn != null) {
				notExistColumn = "";
				for (String column : orderByColoumn) {
					if (!StringUtil.isBlank(column)) {
						column = column.trim();
					} else {
						break;
					}
					// 数据库字段不区分大小写，属性区分大小写
					if (!columnList.contains(column.toLowerCase()) && !propertyList.contains(column)) {
						notExistColumn += "," + column;
					}
				}
				if (!StringUtil.isBlank(notExistColumn)) {
					notExistColumn = notExistColumn.substring(1);
					throw new RestServiceException(thread, System.currentTimeMillis() - start,
							"orderBy中" + notExistColumn + "的字段不存在!", RestBusinessCode.param_formate_error.getValue());
				}
			}

		}
	}

	/**
	 * 验证日志查询条件格式是否正确
	 * @param thread
	 * @param start
	 * @param entity
	 * @param clazz
	 * @param page
	 */
	public void queryConditionCheck(AuditLogThread thread, long start, LogEntity<?> entity, Class clazz,
									Page page) {
		logger.info("请求参数校验开始----" + System.currentTimeMillis());
		try {
			if (!StringUtil.isBlank(entity.getSearchCfgId())) {
				Long.parseLong(entity.getSearchCfgId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchCfgId参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchCfgId参数错误");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchDirection())) {
				Integer.parseInt(entity.getSearchDirection());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "getSearchDirection参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "getSearchDirection参数错误");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchEntranceId())) {
				Long.parseLong(entity.getSearchEntranceId());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchEntranceId参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchEntranceId参数错误");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchFoundEndTime())) {
				sdf.setLenient(false);
				sdf.parse(entity.getSearchFoundEndTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchFoundEndTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchFoundEndTime参数格式格式");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchFoundStartTime())) {
				sdf.setLenient(false);
				sdf.parse(entity.getSearchFoundStartTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchFoundStartTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchFoundStartTime参数错误");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchService())) {
				Integer.parseInt(entity.getSearchService());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数错误");
		}
		try {
			checkCloumnIsExist(thread, start, clazz, page);
		} catch (RestServiceException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "请求参数错误");
		}
		logger.info("请求参数校验结束----" + System.currentTimeMillis());

	}
	/**
	 * 
	 * @Description:
	 * @author (zdx)
	 * @date 2018年7月12日 上午11:43:32
	 * @param thread
	 * @param start
	 * @param entity
	 * @param clazz
	 * @param page
	 */
	public void queryReportConditionCheck(AuditLogThread thread, long start, NtcReportEntity<?> entity, Class clazz,
										  Page page) {
		logger.info("实时报表统计查询参数校验开始----" + System.currentTimeMillis());
		if (!StringUtil.isBlank(entity.getSearchBusinessType())&&!StringUtil.isNumeric(entity.getSearchBusinessType())) {
			logger.error(RestBusinessCode.param_formate_error.getErrorReason()+",searchBusinessType参数格式错误");
			thread.setExceptionInfo("searchBusinessType参数格式错误");
			throw new RestServiceException(thread,
					System.currentTimeMillis() - start,
					"searchBusinessType参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		}
		//searchService
		checkNumericCondition(thread, start, entity.getSearchService(), "searchService");
		
		try {
			if (!StringUtil.isBlank(entity.getSearchReportStartTime())) {
				sdf.parse(entity.getSearchReportStartTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchReportStartTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchReportStartTime参数错误");
		}
		
		try {
			if (!StringUtil.isBlank(entity.getSearchReportEndTime())) {
				sdf.parse(entity.getSearchReportEndTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchReportEndTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchReportEndTime参数错误");
		}
		if (!StringUtil.isBlank(entity.getSearchBusinessType())&&!StringUtil.isNumeric(entity.getSearchBusinessType())) {
			logger.error(RestBusinessCode.param_formate_error.getErrorReason()+",searchBusinessType参数格式错误");
			thread.setExceptionInfo("searchBusinessType参数格式错误");
			throw new RestServiceException(thread,
					System.currentTimeMillis() - start,
					"searchBusinessType参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		}
		
		logger.info("实时报表统计查询参数校验结束----" + System.currentTimeMillis());

	}
	/**
	 * @Description:用于验证数值类型格式，多个值以","分隔
	 * @author (zdx)
	 * @date 2018年7月12日 下午12:47:35
	 * @param thread
	 * @param start
	 * @param condition
	 * @param condName
	 */
	public void checkNumericCondition(AuditLogThread thread, long start, String condition, String condName) {
		if (!StringUtil.isEmpty(condition)){
			Boolean flag = false;
			if (condition.contains(",")) {
				String services[] =condition.split(",");
				for (String service : services) {
					if(!StringUtil.isNumeric(service)) {
						flag = true;
						break;
					}
				}
			}else if(!StringUtil.isNumeric(condition)) {
				flag = true;
			}
			if (flag) {
				logger.error(RestBusinessCode.param_formate_error.getErrorReason()+","+condName+"参数格式错误");
				thread.setExceptionInfo(condName+"参数格式错误");
				throw new RestServiceException(thread,
						System.currentTimeMillis() - start,
						condName+"参数格式错误",
						RestBusinessCode.param_formate_error.getValue());
			}
		}
	}
	/**
	 * wx 报表查询条件检查
	 * 
	 * @param clazz
	 *            需要检验的实体名称[需要保证resultMap的id命名方式为[className]Map]
	 * @param page
	 *            需要校验的page对象
	 */
	public void queryConditionCheck(AuditLogThread thread, long start, StatLogEntity entity, Class clazz,
									Page page) {

		try {
			if (!StringUtil.isBlank(entity.getSearchStatStartTime())) {
				sdf.parse(entity.getSearchStatStartTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchStatStartTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchStatStartTime参数格式错误");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchStatEndTime())) {
				sdf.parse(entity.getSearchStatEndTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchStatEndTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchStatEndTime参数错误");
		}
		try {
			if (!StringUtil.isBlank(entity.getSearchService())) {
				Integer.parseInt(entity.getSearchService());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数错误");
		}

		try {
			checkCloumnIsExist(thread, start, clazz, page);
		} catch (RestServiceException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "请求参数错误");
		}

	}

	/**
	 * 管控实时统计公共查询字段验证
	 * 
	 * @param entity
	 */
	public void queryConditionCheck(AuditLogThread thread, long start, DfJitLogEntity<?> entity, Class clazz,
									Page page) {
		try {
			if (!StringUtil.isBlank(entity.getSearchReportEndTime())) {
				sdf.parse(entity.getSearchReportEndTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchReportEndTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchReportEndTime参数格式格式");
		}
		try {
			if (!StringUtil.isBlank(entity.getSearchReportStartTime())) {
				sdf.parse(entity.getSearchReportStartTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchReportStartTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchReportStartTime参数错误");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchService())) {
				Integer.parseInt(entity.getSearchService());
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "searchService参数错误");
		}

		try {
			String searchAttrType = getSearchField(clazz, entity, "searchAttrType");
			if (!StringUtil.isBlank(searchAttrType)) {
				Integer.parseInt(searchAttrType);
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchAttrType参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchAttrType参数错误");
		}
		try {
			String searchLwhh = getSearchField(clazz, entity, "searchLwhh");
			if (!StringUtil.isBlank(searchLwhh)) {
				Integer.parseInt(searchLwhh);
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchLwhh参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchLwhh参数错误");
		}

		try {
			checkCloumnIsExist(thread, start, clazz, page);
		} catch (RestServiceException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, System.currentTimeMillis() - start, "请求参数错误");
		}

	}

	/**
	 * 实时统计公共查询字段验证
	 * 
	 * @param entity
	 */
	public void queryConditionCheck(AuditLogThread thread, long start, DfReportEntity entity, Class clazz,
									Page page) {
		try {
			if (!StringUtil.isBlank(entity.getSearchReportEndTime())) {
				sdf.parse(entity.getSearchReportEndTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchReportEndTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchReportEndTime参数格式格式");
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchReportStartTime())) {
				sdf.parse(entity.getSearchReportStartTime());
			}
		} catch (ParseException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchReportStartTime参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchReportStartTime参数错误");
		}

		try {
			String searchAttrType = getSearchField(clazz, entity, "searchAttrType");
			if (!StringUtil.isBlank(searchAttrType)) {
				Integer.parseInt(searchAttrType);
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchAttrType参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchAttrType参数错误");
		}
		try {
			String searchLwhh = getSearchField(clazz, entity, "searchLwhh");
			if (!StringUtil.isBlank(searchLwhh)) {
				Integer.parseInt(searchLwhh);
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchLwhh参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchLwhh参数错误");
		}
		try {
			String searchId = getSearchField(clazz, entity, "searchId");
			if (!StringUtil.isBlank(searchId)) {
				Integer.parseInt(searchId);
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchId参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchId参数错误");
		}

		try {
			String searchService = getSearchField(clazz, entity, "searchService");
			if (!StringUtil.isBlank(searchService)) {
				Integer.parseInt(searchService);
			}
		} catch (NumberFormatException e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchService参数格式错误",
					RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			throw new RestServiceException(thread, start, "searchService参数错误");
		}

		try {
			checkCloumnIsExist(thread, start, clazz, page);
		} catch (RestServiceException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			thread.setExceptionInfo(e.getMessage() + " " + e.getCause());
			logger.error(e);
			throw new RestServiceException(thread, start, "请求参数错误");
		}

	}

	/**
	 * 
	 * @Title: getJedisKey
	 * @Description: (日志查询/报表记录，"接口名称+查询条件"作为key，将结果存入redis中)
	 * @param @param
	 *            request
	 * @param @param
	 *            bool 查询数据中心缓存还是oracle缓存,false是oracle缓存
	 * @param @return
	 * @return Map 返回类型
	 * @author （DDM）
	 * @version V1.0
	 */
	public String getJedisKey(HttpServletRequest request, boolean bool) {
		String key = "";
		String serviceName = "";
		// 获取访问url
		String uri = request.getRequestURI();
		serviceName = uri.substring(uri.lastIndexOf("/") + 1, uri.length());
		Map<String, String[]> parapeterMapNew = new HashMap<String, String[]>();
		// 获取传入参数
		Map<String, String[]> parapeterMap = request.getParameterMap();
		if (parapeterMap == null) {
			parapeterMap = new HashMap<String, String[]>();
		}
		// 将patameterMap内容克隆到一个新的map中
		for (String keyStr : parapeterMap.keySet()) {
			parapeterMapNew.put(keyStr, parapeterMap.get(keyStr));
		}

		if (!bool) {
			// 判断是否存在pageNo、pageSize，不存在则添加默认的pageNum和pageSize
			if (parapeterMapNew.get("pageNo") == null) {
				String[] strArr = { "1" };
				parapeterMapNew.put("pageNo", strArr);
			}
			if (parapeterMapNew.get("pageSize") == null) {
				String[] strArr = { "30" };
				parapeterMapNew.put("pageSize", strArr);
			}
		} else {
			if (parapeterMapNew.get("pageNo") != null) {
				parapeterMapNew.remove("pageNo");
			}
			if (parapeterMapNew.get("pageSize") != null) {
				parapeterMapNew.remove("pageSize");
			}
		}
		// mapKey排序
		parapeterMapNew.entrySet();

		// 拼凑查询条件
		for (String keyStr : parapeterMapNew.keySet()) {
			if (parapeterMapNew.get(keyStr) != null) {
				key += "&" + keyStr + "=" + parapeterMapNew.get(keyStr)[0];
			} else {
				key += "&" + keyStr + "=";
			}
		}
		if (!StringUtil.isBlank(key)) {
			key = key.substring(1);
		}
		// 返回serviceName+查询条件
		key = serviceName + "?" + key;

		return key;
	}

	protected String getSearchField(Class<?> clazz, Serializable entity, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (String) field.get(entity);
		} catch (Exception e) {
			return null;
		}
	}
}
