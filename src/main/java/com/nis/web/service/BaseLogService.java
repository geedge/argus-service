package com.nis.web.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nis.domain.LogEntity;
import com.nis.domain.Page;
import com.nis.domain.restful.NtcCollectVoipLog;
import com.nis.domain.restful.NtcRadiusReport;
import com.nis.domain.restful.NtcReportEntity;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.ExceptionUtil;
import com.nis.util.elasticsearch.ElasticsearchSqlDao;
import com.zdjizhi.utils.DateUtils;
import com.zdjizhi.utils.StringUtil;
import com.zdjizhi.utils.TimeConstants;

/**
 * 基础日志Service基类
 * 
 * @author zbc
 * @version 2016-09-10
 */
@SuppressWarnings({ "rawtypes" })
public abstract class BaseLogService {

	
	
	protected static final Logger logger =LoggerFactory.getLogger(BaseLogService.class);
	@Autowired
	protected ElasticsearchSqlDao elasticsearchSqlDao;

	/**
	 * wx checkCloumnIsExist(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param clazz
	 * @param page
	 * @throws Exception void
	 * @exception @since 1.0.0
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
					throw new RestServiceException("fields中" + notExistColumn + "的字段不存在!",
							RestBusinessCode.param_formate_error.getValue());
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
					throw new RestServiceException("orderBy中" + notExistColumn + "的字段不存在!",
							RestBusinessCode.param_formate_error.getValue());
				}
			}

		}
	}

	/**
	 * 验证日志查询条件格式是否正确
	 * 
	 * @param thread
	 * @param start
	 * @param entity
	 * @param clazz
	 * @param page
	 */
	public void queryConditionCheck(AuditLogThread thread, long start, LogEntity<?> entity, Class clazz, Page page) {
		logger.info("请求参数校验开始----" + System.currentTimeMillis());

		if (!StringUtil.isBlank(entity.getSearchCfgId())) {
			String[] split = org.apache.commons.lang.StringUtils.split(entity.getSearchCfgId(), ",");
			if (split.length > 10) {
				throw new RestServiceException("searchCfgId参数个数不能超过10个",
						RestBusinessCode.param_formate_error.getValue());
			}
			for (String cfgId : split) {
				try {
					Long.parseLong(cfgId);
				} catch (Exception e) {
					throw new RestServiceException("searchCfgId参数错误", RestBusinessCode.param_formate_error.getValue());
				}

			}
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchDirection())) {
				Integer.parseInt(entity.getSearchDirection());
			}
		} catch (NumberFormatException e) {
			throw new RestServiceException("getSearchDirection参数格式错误", RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			throw new RestServiceException("getSearchDirection参数错误", RestBusinessCode.param_formate_error.getValue());
		}


		try {
			if (!StringUtil.isBlank(entity.getSearchEntranceId())) {
				Long.parseLong(entity.getSearchEntranceId());
			}
		} catch (NumberFormatException e) {
			throw new RestServiceException("searchEntranceId参数格式错误", RestBusinessCode.param_formate_error.getValue());
		} catch (Exception e) {
			throw new RestServiceException("searchEntranceId参数错误", RestBusinessCode.param_formate_error.getValue());
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchFoundEndTime())) {
				DateUtils.convertStringToDate(entity.getSearchFoundEndTime(), TimeConstants.YYYY_MM_DD_HH24_MM_SS);
			}
		} catch (Exception e) {
			throw new RestServiceException("searchFoundEndTime参数错误", RestBusinessCode.param_formate_error.getValue());
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchFoundStartTime())) {
				DateUtils.convertStringToDate(entity.getSearchFoundStartTime(), TimeConstants.YYYY_MM_DD_HH24_MM_SS);
			}
		}  catch (Exception e) {
			throw new RestServiceException("searchFoundStartTime参数错误", RestBusinessCode.param_formate_error.getValue());
		}
		//service参数改为可以传多个
		if (!StringUtil.isBlank(entity.getSearchService())) {
			String[] splits = org.apache.commons.lang.StringUtils.split(entity.getSearchService(), ",");
			for (String service : splits) {
				try {
					Integer.parseInt(service);
				} catch (Exception e) {
					throw new RestServiceException("searchService参数错误", RestBusinessCode.param_formate_error.getValue());
				}

			}
		}

		try {
			checkCloumnIsExist(thread, start, clazz, page);
		} catch (RestServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceRuntimeException(ExceptionUtil.getExceptionMsg(e),
					RestBusinessCode.service_runtime_error.getValue());
		}
		logger.info("请求参数校验结束----" + System.currentTimeMillis());

	}

	/**
	 * 验证日志查询条件格式是否正确
	 * 
	 * @param thread
	 * @param start
	 * @param entity
	 * @param clazz
	 * @param page
	 */
	public void collectConditionCheck(AuditLogThread thread, long start, NtcCollectVoipLog entity, Class clazz,
			Page page) {
		logger.info("请求参数校验开始----" + System.currentTimeMillis());
		try {
			if (!StringUtil.isBlank(entity.getSearchFoundEndTime())) {
				DateUtils.convertStringToDate(entity.getSearchFoundEndTime(), TimeConstants.YYYY_MM_DD_HH24_MM_SS);
			}
		} catch (Exception e) {
			throw new RestServiceException("searchFoundEndTime参数错误", RestBusinessCode.param_formate_error.getValue());
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchFoundStartTime())) {
				DateUtils.convertStringToDate(entity.getSearchFoundStartTime(), TimeConstants.YYYY_MM_DD_HH24_MM_SS);
			}
		}  catch (Exception e) {
			throw new RestServiceException("searchFoundStartTime参数错误", RestBusinessCode.param_formate_error.getValue());
		}

		try {
			checkCloumnIsExist(thread, start, clazz, page);
		} catch (RestServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceRuntimeException(ExceptionUtil.getExceptionMsg(e),
					RestBusinessCode.service_runtime_error.getValue());
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
		if (!StringUtil.isBlank(entity.getSearchBusinessType())
				&& !StringUtil.isNumeric(entity.getSearchBusinessType())) {
			throw new RestServiceException("searchBusinessType参数格式错误", RestBusinessCode.param_formate_error.getValue());
		}
		// searchService
		checkNumericCondition(thread, start, entity.getSearchService(), "searchService");


		try {
			if (!StringUtil.isBlank(entity.getSearchReportStartTime())) {
				DateUtils.convertStringToDate(entity.getSearchReportStartTime(), TimeConstants.YYYY_MM_DD_HH24_MM_SS);
			}
		} catch (Exception e) {
			throw new RestServiceException("searchReportStartTime参数格式错误", RestBusinessCode.param_formate_error.getValue());
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchReportEndTime())) {
				DateUtils.convertStringToDate(entity.getSearchReportEndTime(), TimeConstants.YYYY_MM_DD_HH24_MM_SS);
			}
		}  catch (Exception e) {
			throw new RestServiceException("searchReportEndTime参数错误", RestBusinessCode.param_formate_error.getValue());
		}


		if (!StringUtil.isBlank(entity.getSearchBusinessType())
				&& !StringUtil.isNumeric(entity.getSearchBusinessType())) {
			throw new RestServiceException("searchBusinessType参数格式错误", RestBusinessCode.param_formate_error.getValue());
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
		if (!StringUtil.isEmpty(condition)) {
			Boolean flag = false;
			if (condition.contains(",")) {
				String services[] = condition.split(",");
				for (String service : services) {
					if (!StringUtil.isNumeric(service)) {
						flag = true;
						break;
					}
				}
			} else if (!StringUtil.isNumeric(condition)) {
				flag = true;
			}
			if (flag) {
				throw new RestServiceException(condName + "参数格式错误", RestBusinessCode.param_formate_error.getValue());
			}
		}
	}

	/**
	 * @Description:验证用户行为日志统计查询条件
	 * @author(zdx) @date 2018年10月31日 下午4:53:27
	 * @param thread
	 * @param start
	 * @param entity
	 */
	public void checkNtcRadiusReportCondition(AuditLogThread thread, long start, NtcRadiusReport entity) {
		logger.info("用户行为日志统计参数校验开始----" + System.currentTimeMillis());
		if (!StringUtil.isBlank(entity.getSearchBusinessType())
				&& !StringUtil.isNumeric(entity.getSearchBusinessType())) {
			throw new RestServiceException("searchBusinessType参数格式错误", RestBusinessCode.param_formate_error.getValue());
		}

		if (StringUtil.isBlank(entity.getSearchReportStartTime())
				|| StringUtil.isBlank(entity.getSearchReportEndTime())) {
			throw new RestServiceException("searchReportStartTime和searchReportEndTime参数必须同时填写",
					RestBusinessCode.config_integrity_error.getValue());
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchReportStartTime())) {
				DateUtils.convertStringToDate(entity.getSearchReportStartTime(), TimeConstants.YYYY_MM_DD_HH24_MM_SS);
			}
		}  catch (Exception e) {
			throw new RestServiceException("searchReportStartTime参数错误",
					RestBusinessCode.param_formate_error.getValue());
		}

		try {
			if (!StringUtil.isBlank(entity.getSearchReportEndTime())) {
				DateUtils.convertStringToDate(entity.getSearchReportEndTime(), TimeConstants.YYYY_MM_DD_HH24_MM_SS);
			}
		} catch (Exception e) {
			throw new RestServiceException("searchReportEndTime参数错误", RestBusinessCode.param_formate_error.getValue());
		}


		// 根据用户查看IP趋势时(searchBusinessType=2)，用户名必需填写
		if ("2".equals(entity.getSearchBusinessType()) && StringUtil.isEmpty(entity.getSearchAccount())) {
			throw new RestServiceException("searchBusinessType=2时searchAccount参数必须填写",
					RestBusinessCode.config_integrity_error.getValue());
		}

		// 根据用户查看IP趋势时(searchBusinessType=3)，用户名必需填写
		if ("3".equals(entity.getSearchBusinessType()) && StringUtil.isEmpty(entity.getSearchNasIp())) {
			throw new RestServiceException("searchBusinessType=3时searchNasIp参数必须填写",
					RestBusinessCode.config_integrity_error.getValue());
		}

		logger.info("用户行为日志统计参数校验结束----" + System.currentTimeMillis());

	}

	/**
	 * 
	 * @Title: getJedisKey
	 * @Description: (日志查询/报表记录，"接口名称+查询条件"作为key，将结果存入redis中)
	 * @param @param request
	 * @param @param bool 查询数据中心缓存还是oracle缓存,false是oracle缓存
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
