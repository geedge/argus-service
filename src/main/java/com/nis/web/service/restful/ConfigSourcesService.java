package com.nis.web.service.restful;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nis.domain.restful.CommonSourceFieldCfg;
import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DigestRegion;
import com.nis.domain.restful.GroupReuse;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.MaatConfig;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.restful.ServiceRuntimeException;
import com.nis.util.BasicProvingUtil;
import com.nis.util.CamelUnderlineUtil;
import com.nis.util.CompileVal;
import com.nis.util.Constants;
import com.nis.util.GroupReuseVal;
import com.nis.util.JedisClusterUtils;
import com.nis.util.JedisUtils;
import com.nis.util.JsonMapper;
import com.nis.util.ReadCommSourceXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.zdjizhi.utils.StringUtil;
import com.nis.web.service.AuditLogThread;
import com.nis.web.service.BaseService;

/**
 * @ClassName: ControlService
 * @Description: TODO(编译配置持久化)
 * @author (zx)
 * @date 2016年9月6日 下午1:08:12
 * @version V1.0
 */
@Service
public class ConfigSourcesService extends BaseService {

	private Map<Integer, Map<String, String>> map;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	ConfigRedisService configRedisService;

	/**
	 * 
	 * @Description: 判断tableName是否是增强字符串类域配置表
	 * @date 2018年8月10日 下午4:14:36
	 * @param tableName
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public static boolean isStrStrongRegion(String tableName, Integer service) throws Exception {
		if (null != tableName && !tableName.equals("")) {
			List<String> tableList = new ArrayList<String>();
			Map<Integer, List<String>> sercieNameMap = ServiceAndRDBIndexReal.getSercieNameMap().get(service);
			if (sercieNameMap != null && sercieNameMap.size() > 0) {
				for (Integer type : sercieNameMap.keySet()) {
					// 12代表是ip类域配置,13代表是数值类配置,14代表是字符串类域配置,15代表是增强字符串类域配置,
					if ("15".equals(type.toString())) {
						List<String> tableNameList = sercieNameMap.get(type);
						if (tableNameList != null && tableNameList.size() > 0) {
							for (String table : tableNameList) {
								tableList.add(table.toUpperCase());
							}
						}
					}
				}
			}
			if (tableList.contains(tableName)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public Map<Integer, Map<String, String>> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Map<String, String>> map) {
		this.map = map;
	}

	/**
	 * 
	 * @Description:
	 * @author (zdx)
	 * @date 2018年5月25日 下午3:49:46
	 * @param thread
	 * @param start
	 * @param configCompileList
	 * @param sb
	 * @return
	 */
	public void saveMaatConfig(AuditLogThread thread, long start, List<ConfigCompile> configCompileList,
			StringBuffer sb) throws Exception {
		Map<Integer, List<MaatConfig>> maatMap = new HashMap<Integer, List<MaatConfig>>();
		Map<Integer, List<MaatConfig>> configMap = new HashMap<Integer, List<MaatConfig>>();

		if (configCompileList != null && configCompileList.size() > Constants.MAX_LIST_SIZE) {
			thread.setSaveContentFlag(false);
		}
		for (ConfigCompile configCompile : configCompileList) {
			Integer service = Integer.valueOf(configCompile.getService().toString());
			MaatConfig maatConfig = new MaatConfig();
			CompileVal.compileIsOk(configCompile, false, sb);

			maatConfig.setService(service);
			// 编译
			maatConfig.setCompileMap(convertObjectToMap(configCompile, ConfigCompile.class));
			// 分组
			List<Map<String, String>> dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getGroupRelationList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (ConfigGroupRelation group : configCompile.getGroupRelationList()) {
					dstMaplList.add(convertObjectToMap(group, ConfigGroupRelation.class));
				}
			}
			maatConfig.setGroupMapList(dstMaplList);
			// 字符串域
			dstMaplList = null;
			List<Map<String, String>> strongMapList = null;
			if (!StringUtil.isEmpty(configCompile.getStrRegionList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (StrRegion region : configCompile.getStrRegionList()) {
					if (StringUtil.isEmpty(region.getDistrict())) {
						dstMaplList.add(convertObjectToMap(region, StrRegion.class));
					} else {
						if (StringUtil.isEmpty(strongMapList)) {
							strongMapList = new ArrayList<Map<String, String>>();
						}
						strongMapList.add(convertObjectToMap(region, StrRegion.class));
					}
				}
			}
			maatConfig.setStrRegionMapList(dstMaplList);
			// 增强字符串域
			if (!StringUtil.isEmpty(strongMapList) && strongMapList.size() > 0) {
				maatConfig.setStrStrRegionMapList((strongMapList));
			}
			// 数值域
			dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getNumRegionList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (NumRegion region : configCompile.getNumRegionList()) {
					dstMaplList.add(convertObjectToMap(region, NumRegion.class));
				}
			}
			maatConfig.setNumRegionMapList(dstMaplList);

			// Ip域
			dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getIpRegionList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (IpRegion region : configCompile.getIpRegionList()) {
					dstMaplList.add(convertObjectToMap(region, IpRegion.class));
				}
			}
			maatConfig.setIpRegionMapList(dstMaplList);

			// 摘要类域
			dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getDigestRegionList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (DigestRegion region : configCompile.getDigestRegionList()) {
					dstMaplList.add(convertObjectToMap(region, DigestRegion.class));
				}
			}

			maatConfig.setFileDigestRegionMapList(dstMaplList);

			// 文本相似性域
			// dstMaplList = null;
			// maatConfig.setFileLikeRegionMapList(dstMaplList);

			// 生效范围IP域
			dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getIpClientRangeList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (IpRegion region : configCompile.getIpClientRangeList()) {
					dstMaplList.add(convertObjectToMap(region, IpRegion.class));
				}
			}
			maatConfig.setIpClientRangeMapList(dstMaplList);

			if (maatMap.containsKey(service)) {
				maatMap.get(service).add(maatConfig);
			} else {
				List<MaatConfig> maatCfgList = new ArrayList<MaatConfig>();
				maatCfgList.add(maatConfig);
				maatMap.put(service, maatCfgList);

			}
		}

		// 调用接口入redis

		Iterator serviceIterator = maatMap.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					// 分发到阀门有些业务需要添加编译属性到域配置
					List<MaatConfig> newMaatConfigList = new ArrayList<MaatConfig>();
					newMaatConfigList.addAll(maatMap.get(service));
					if (dbIndex.intValue() == ServiceAndRDBIndexReal.getValveDBIndex().intValue()) {
						Map<Integer, Map<String, String[]>> maatToValueMap = ServiceAndRDBIndexReal.getMaatToValveMap();
						if (maatToValueMap.containsKey(service)) {

							Map<String, String[]> regionAndFiledMap = maatToValueMap.get(service);
							for (int i = 0; i < newMaatConfigList.size(); i++) {
								MaatConfig maatConfig = newMaatConfigList.get(i);
								MaatConfig newMaatConfig = (MaatConfig) JsonMapper
										.fromJsonString(JsonMapper.toJsonString(maatConfig), MaatConfig.class);
								Iterator iterator = regionAndFiledMap.keySet().iterator();
								while (iterator.hasNext()) {
									String regionName = iterator.next().toString();
									PropertyDescriptor pd;
									try {
										pd = new PropertyDescriptor(regionName + "MapList", MaatConfig.class);
										Method method = pd.getReadMethod();
										Object object = method.invoke(newMaatConfig);

										if (object != null) {

											List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();
											listMaps.addAll((List<Map<String, String>>) object);
											String[] fields = regionAndFiledMap.get(regionName);
											for (String fieldName : fields) {
												String value = newMaatConfig.getCompileMap()
														.get(fieldName.toLowerCase());
												if (!StringUtil.isEmpty(value)) {
													for (Map<String, String> map : listMaps) {
														map.put(fieldName.toLowerCase(), value);
													}
												}
											}
											method = pd.getWriteMethod();
											method.invoke(newMaatConfig, listMaps);
										}
										newMaatConfigList.set(i, newMaatConfig);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										throw new RestServiceException("未找到域列表，请检查配置文件中域类型是否正确！:" + e.getMessage(),
												RestBusinessCode.service_runtime_error.getValue());
									}

								}
							}
						}
					}

					if (configMap.containsKey(dbIndex)) {
						configMap.get(dbIndex).addAll(newMaatConfigList);
					} else {
						List<MaatConfig> list = new ArrayList<MaatConfig>();
						list.addAll(newMaatConfigList);
						configMap.put(dbIndex, list);
					}

				}
			} else {
				throw new ServiceRuntimeException(RestBusinessCode.ServiceNoFoundDBIndex.getErrorReason(),
						RestBusinessCode.ServiceNoFoundDBIndex.getValue());
			}
		}
		logger.info("---------------调用Redis maat配置新增接口---------------------");
		configRedisService.saveMaatConfig(configMap);
	}

	/**
	 * @Description:
	 * @author(zdx) @date 2018年12月3日 下午6:48:32
	 * @param configCompileList
	 * @throws Exception
	 */

	public void saveMaatConfig(List<ConfigCompile> configCompileList) throws Exception {
		Map<Integer, List<MaatConfig>> maatMap = new HashMap<Integer, List<MaatConfig>>();
		Map<Integer, List<MaatConfig>> configMap = new HashMap<Integer, List<MaatConfig>>();

		for (ConfigCompile configCompile : configCompileList) {
			Integer service = Integer.valueOf(configCompile.getService().toString());
			MaatConfig maatConfig = new MaatConfig();

			maatConfig.setService(service);
			// 编译
			maatConfig.setCompileMap(convertObjectToMap(configCompile, ConfigCompile.class));
			// 分组
			List<Map<String, String>> dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getGroupRelationList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (ConfigGroupRelation group : configCompile.getGroupRelationList()) {
					dstMaplList.add(convertObjectToMap(group, ConfigGroupRelation.class));
				}
			}
			maatConfig.setGroupMapList(dstMaplList);
			// 字符串域
			dstMaplList = null;
			List<Map<String, String>> strongMapList = null;
			if (!StringUtil.isEmpty(configCompile.getStrRegionList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (StrRegion region : configCompile.getStrRegionList()) {
					if (StringUtil.isEmpty(region.getDistrict())) {
						dstMaplList.add(convertObjectToMap(region, StrRegion.class));
					} else {
						if (StringUtil.isEmpty(strongMapList)) {
							strongMapList = new ArrayList<Map<String, String>>();
						}
						strongMapList.add(convertObjectToMap(region, StrRegion.class));
					}
				}
			}
			maatConfig.setStrRegionMapList(dstMaplList);
			// 增强字符串域
			if (!StringUtil.isEmpty(strongMapList) && strongMapList.size() > 0) {
				maatConfig.setStrStrRegionMapList((strongMapList));
			}
			// 数值域
			dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getNumRegionList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (NumRegion region : configCompile.getNumRegionList()) {
					dstMaplList.add(convertObjectToMap(region, NumRegion.class));
				}
			}
			maatConfig.setNumRegionMapList(dstMaplList);

			// Ip域
			dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getIpRegionList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (IpRegion region : configCompile.getIpRegionList()) {
					dstMaplList.add(convertObjectToMap(region, IpRegion.class));
				}
			}
			maatConfig.setIpRegionMapList(dstMaplList);

			// 摘要类域
			dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getDigestRegionList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (DigestRegion region : configCompile.getDigestRegionList()) {
					dstMaplList.add(convertObjectToMap(region, DigestRegion.class));
				}
			}

			maatConfig.setFileDigestRegionMapList(dstMaplList);

			// 文本相似性域
			// dstMaplList = null;
			// maatConfig.setFileLikeRegionMapList(dstMaplList);

			// 生效范围IP域
			dstMaplList = null;
			if (!StringUtil.isEmpty(configCompile.getIpClientRangeList())) {
				dstMaplList = new ArrayList<Map<String, String>>();
				for (IpRegion region : configCompile.getIpClientRangeList()) {
					dstMaplList.add(convertObjectToMap(region, IpRegion.class));
				}
			}
			maatConfig.setIpClientRangeMapList(dstMaplList);

			if (maatMap.containsKey(service)) {
				maatMap.get(service).add(maatConfig);
			} else {
				List<MaatConfig> maatCfgList = new ArrayList<MaatConfig>();
				maatCfgList.add(maatConfig);
				maatMap.put(service, maatCfgList);

			}
		}

		// 调用接口入redis

		Iterator serviceIterator = maatMap.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					// 分发到阀门有些业务需要添加编译属性到域配置
					List<MaatConfig> newMaatConfigList = new ArrayList<MaatConfig>();
					newMaatConfigList.addAll(maatMap.get(service));
					if (dbIndex.intValue() == ServiceAndRDBIndexReal.getValveDBIndex().intValue()) {
						Map<Integer, Map<String, String[]>> maatToValueMap = ServiceAndRDBIndexReal.getMaatToValveMap();
						if (maatToValueMap.containsKey(service)) {

							Map<String, String[]> regionAndFiledMap = maatToValueMap.get(service);
							for (int i = 0; i < newMaatConfigList.size(); i++) {
								MaatConfig maatConfig = newMaatConfigList.get(i);
								MaatConfig newMaatConfig = (MaatConfig) JsonMapper
										.fromJsonString(JsonMapper.toJsonString(maatConfig), MaatConfig.class);
								Iterator iterator = regionAndFiledMap.keySet().iterator();
								while (iterator.hasNext()) {
									String regionName = iterator.next().toString();
									PropertyDescriptor pd;
									try {
										pd = new PropertyDescriptor(regionName + "MapList", MaatConfig.class);
										Method method = pd.getReadMethod();
										Object object = method.invoke(newMaatConfig);

										if (object != null) {

											List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();
											listMaps.addAll((List<Map<String, String>>) object);
											String[] fields = regionAndFiledMap.get(regionName);
											for (String fieldName : fields) {
												String value = newMaatConfig.getCompileMap()
														.get(fieldName.toLowerCase());
												if (!StringUtil.isEmpty(value)) {
													for (Map<String, String> map : listMaps) {
														map.put(fieldName.toLowerCase(), value);
													}
												}
											}
											method = pd.getWriteMethod();
											method.invoke(newMaatConfig, listMaps);
										}
										newMaatConfigList.set(i, newMaatConfig);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										throw new RestServiceException("未找到域列表，请检查配置文件中域类型是否正确！:" + e.getMessage(),
												RestBusinessCode.service_runtime_error.getValue());
									}

								}
							}
						}
					}

					if (configMap.containsKey(dbIndex)) {
						configMap.get(dbIndex).addAll(newMaatConfigList);
					} else {
						List<MaatConfig> list = new ArrayList<MaatConfig>();
						list.addAll(newMaatConfigList);
						configMap.put(dbIndex, list);
					}

				}
			} else {
				throw new ServiceRuntimeException(RestBusinessCode.ServiceNoFoundDBIndex.getErrorReason(),
						RestBusinessCode.ServiceNoFoundDBIndex.getValue());
			}
		}
		logger.info("---------------调用Redis maat配置新增接口---------------------");
		configRedisService.saveMaatConfig(configMap);
	}

	private Map<String, String> convertObjectToMap(Object obj, Class clazz) throws Exception {
		Map<String, String> dstMap = new HashMap<String, String>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) {
				continue;
			}

			String dstName = CamelUnderlineUtil.camelToUnderline(field.getName());
			PropertyDescriptor pd;
			pd = new PropertyDescriptor(field.getName(), clazz);
			Method method = pd.getReadMethod();
			Object dstObject = method.invoke(obj);
			if (dstObject instanceof Date) {
				dstObject = convertToTimeStamp16((Date) dstObject);
			}
			if (dstObject instanceof ArrayList) {
				dstObject = "";
			}
			dstMap.put(dstName, StringUtil.isEmpty(dstObject) ? "" : dstObject.toString());
		}
		return dstMap;
	}

	public void updateConfigSources(AuditLogThread thread, long start, List<ConfigCompile> compileList, Date opTime,
			StringBuffer sb) throws Exception {
		Map<Integer, List<Long>> compileMap = new HashMap<Integer, List<Long>>();
		if (compileList != null && compileList.size() > Constants.MAX_LIST_SIZE) {
			thread.setSaveContentFlag(false);
		}
		if (null != compileList && compileList.size() > 0) {
			for (ConfigCompile config : compileList) {
				checkCompileOptForUpdate(config);
				if (config.getOpTime() == null) {
					config.setOpTime(opTime);
				}

				// compileAllList.add(config);
				if (compileMap.containsKey(config.getService())) {
					compileMap.get(config.getService()).add(config.getCompileId());
				} else {
					List<Long> idList = new ArrayList<Long>();
					idList.add(config.getCompileId());
					compileMap.put(config.getService(), idList);
				}
			}

		} else {
			throw new RestServiceException("编译配置不能为空" + sb.toString(), RestBusinessCode.CompileIsNull.getValue());
		}
		// Map<DBIndex,Map<Service,List<CompileId>>
		// Map<Integer, Map<Integer, List<Long>>> restMap = new HashMap<Integer,
		// Map<Integer, List<Long>>>();
		// Iterator serviceIterator = compileMap.keySet().iterator();
		// while (serviceIterator.hasNext()) {
		// Integer service = Integer.valueOf(serviceIterator.next().toString());
		// List<Integer> dbIndexList =
		// ServiceAndRDBIndexReal.getRedisDBByService(service);
		// if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
		// for (Integer dbIndex : dbIndexList) {
		// if (restMap.containsKey(dbIndex)) {
		// restMap.get(dbIndex).put(service, compileMap.get(service));
		// } else {
		// Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
		// map.put(service, compileMap.get(service));
		// restMap.put(dbIndex, map);
		// }
		// }
		// } else {
		// ServiceRuntimeException e = new ServiceRuntimeException("service值为" + service
		// + ",与写入数据库序号映射关系不存在",
		// RestBusinessCode.ServiceNoFoundDBIndex.getValue());
		// }
		// }
		// 所有的都删除成功返回true
		if (!configRedisService.delMaatConfig(compileMap)) {
			throw new ServiceRuntimeException("取消MAAT配置时出现异常，具体原因不详，请联系管理员",
					RestBusinessCode.service_runtime_error.getValue());
		}
	}

	private String convertToTimeStamp16(Date date) {
		// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return date.getTime() + "000";
	}

	/**
	 * 
	 * @Description:验证compileId+isValid
	 * @author (zdx)
	 * @date 2017年8月16日 上午11:48:46
	 * @param config
	 * @return
	 */
	private void checkCompileOptForUpdate(ConfigCompile config) throws Exception {

		if (StringUtil.isEmpty(config.getCompileId())) {
			throw new RestServiceException(RestBusinessCode.CompileIdIsNull.getErrorReason(),
					RestBusinessCode.CompileIdIsNull.getValue());
		}
		if (StringUtil.isEmpty(config.getService())) {
			throw new RestServiceException(RestBusinessCode.ServiceIsNull.getErrorReason(),
					RestBusinessCode.ServiceIsNull.getValue());
		}
		if (StringUtil.isEmpty(config.getIsValid())) {
			throw new RestServiceException("编译配置id为" + config.getCompileId() + "的IsValid字段不能为空",
					RestBusinessCode.IsValidIsNull.getValue());
		}
		if (config.getIsValid() != 0) {
			throw new RestServiceException("编译配置id为" + config.getCompileId() + "的配置在修改时不能为有效",
					RestBusinessCode.IsValidIsF.getValue());
		}
	}

	public void saveCommonSources(AuditLogThread thread, long start, String jsonString) throws Exception {
		JsonArray jsonObjectList = null;
		try {
			jsonObjectList = new JsonParser().parse(jsonString).getAsJsonArray();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RestServiceException(RestBusinessCode.CBParamFormateError.getErrorReason() + "," + e.getMessage(),
					RestBusinessCode.CBParamFormateError.getValue());
		}
		if (jsonObjectList != null && jsonObjectList.size() > Constants.MAX_LIST_SIZE) {
			thread.setSaveContentFlag(false);
		}
		Map<Integer, List<Map<String, String>>> dstMaps = new HashMap<Integer, List<Map<String, String>>>();
		for (int i = 0; i < jsonObjectList.size(); i++) {
			JsonObject jsonObj = (JsonObject) jsonObjectList.get(i);
			Map<String, Object> srcMap = JSONObject.fromObject(JSONObject.fromObject((jsonObj.toString())));
			if (srcMap.containsKey("service")) {
				Map<String, String> dstMap = new HashMap<String, String>();
				List<CommonSourceFieldCfg> commonSourceFieldCfgList = ReadCommSourceXmlUtil
						.getCommonSourceCfgByService(srcMap.get("service").toString().trim());
				if (StringUtil.isEmpty(commonSourceFieldCfgList)) {
					throw new RestServiceException(
							RestBusinessCode.ServiceIsWrong.getErrorReason() + ",请检查service配置是否正确",
							RestBusinessCode.ServiceIsWrong.getValue());
				}
				// 获取IP类型
				Integer ipType = null;
				String ipTypeName = "";

				for (CommonSourceFieldCfg commonSourceFieldCfg : commonSourceFieldCfgList) {
					if (commonSourceFieldCfg.getDstName().equals("addr_type")) {
						String dstVal = srcMap.get(commonSourceFieldCfg.getSrcName()).toString();
						ipTypeName = commonSourceFieldCfg.getSrcName();
						// regexp 特殊格式正则验证
						Boolean valFlag = true;
						if (!StringUtil.isEmpty(commonSourceFieldCfg.getRegexp())) {
							Pattern pattern = Pattern.compile(commonSourceFieldCfg.getRegexp());
							Matcher matcher = pattern.matcher(dstVal);
							valFlag = valFlag & matcher.matches();
						}
						if (!valFlag) {
							throw new RestServiceException(RestBusinessCode.IpTypeIsWrong.getErrorReason(),
									RestBusinessCode.IpTypeIsWrong.getValue());
						}
						ipType = Integer.parseInt(dstVal);
					}
					// 验证service与action是否匹配
					if (commonSourceFieldCfg.getDstName().equals("action")) {
						String dstVal = srcMap.get(commonSourceFieldCfg.getSrcName()).toString().trim();
						if (Integer.valueOf(dstVal).compareTo(ServiceAndRDBIndexReal
								.getActionByService(Integer.valueOf(srcMap.get("service").toString().trim()))) != 0) {
							throw new RestServiceException(RestBusinessCode.ServiceUnmatchAction.getErrorReason(),
									RestBusinessCode.ServiceUnmatchAction.getValue());
						}
					}
				}
				if (ipType == null) {
					ipType = 4;
				}
				for (CommonSourceFieldCfg commonSourceFieldCfg : commonSourceFieldCfgList) {
					// 是否必填
					if (commonSourceFieldCfg.getIsRequired()
							&& !srcMap.containsKey(commonSourceFieldCfg.getSrcName())) {
						throw new RestServiceException(commonSourceFieldCfg.getSrcName() + "参数不能为空",
								RestBusinessCode.FieldIsNull.getValue());
					}
					// 字段类型 String Number Date Ip Port
					String dstStr = StringUtil.isEmpty(srcMap.get(commonSourceFieldCfg.getSrcName()))
							? commonSourceFieldCfg.getDefaultVal()
							: srcMap.get(commonSourceFieldCfg.getSrcName()).toString();
					if (!StringUtil.isEmpty(dstStr) && dstStr.startsWith("[") && dstStr.endsWith("]")) {
						dstStr = srcMap.get(dstStr.substring(1, dstStr.length() - 1)).toString();
					}

					if ("dstFile".equals(commonSourceFieldCfg.getSrcName())) {
						if ("dst_file".equals(commonSourceFieldCfg.getDstName())) {
							String maatTableName = ServiceAndRDBIndexReal
									.getUnMaatTableName(Integer.valueOf(srcMap.get("service").toString().trim()));
							String dstPath = Constants.MM_SAMPLE_DST_PATH.replace("{tableType}",
									maatTableName.substring(maatTableName.lastIndexOf("_") + 1));
							dstStr = dstPath.replace("{fileName}", dstStr.substring(dstStr.lastIndexOf("/") + 1));
						} else if ("file_id".equals(commonSourceFieldCfg.getDstName())) {
							// dstStr = dstStr.substring(dstStr.indexOf("group"));
						}
					}
					switch (commonSourceFieldCfg.getFieldType()) {
					case "Number":
						if (!StringUtil.isNumeric(dstStr)) {
							throw new RestServiceException(commonSourceFieldCfg.getSrcName() + "参数不能格式不正确,必需是数值型",
									RestBusinessCode.MastNumberic.getValue());
						}
						break;
					case "Date":
						try {
							// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
							// System.out.println("--------------------TimeZone:"
							// + sdf.getTimeZone());
							sdf.setLenient(false);
							Date date = sdf.parse(dstStr);
							dstStr = date.getTime() + "000";
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							throw new RestServiceException(commonSourceFieldCfg.getSrcName() + "参数格式不正确或不是规范的日期串",
									RestBusinessCode.MastDate.getValue());
						}
						break;
					case "IP":
						Boolean isSrcInfo = commonSourceFieldCfg.getIsSrcInfo();
						Integer valAddrType = ipType;
						if (ipType.intValue() == 46) {
							valAddrType = isSrcInfo ? 4 : 6;
						} else if (ipType.intValue() == 64) {
							valAddrType = isSrcInfo ? 6 : 4;
						} else if (ipType.intValue() == 10) {
							valAddrType = null;
						}
						if (!BasicProvingUtil.isIpOrIpMask(dstStr, valAddrType)) {
							throw new RestServiceException(
									commonSourceFieldCfg.getSrcName() + "参数格式不正确或与" + ipTypeName + "不一致",
									RestBusinessCode.IpIsUnMatchType.getValue());
						}
						break;
					case "Port":
						if (!BasicProvingUtil.isPortOrPortMask(dstStr)) {
							throw new RestServiceException(commonSourceFieldCfg.getSrcName() + "参数不能格式不正确，不是合法的Port",
									RestBusinessCode.PortIsNotVal.getValue());
						}
						break;

					}
					// range取值范围验证
					if (!StringUtil.isEmpty(commonSourceFieldCfg.getRange())) {
						String[] range = commonSourceFieldCfg.getRange().split("-");
						Boolean flag = false;
						if (range.length == 2) {
							if (!(Long.valueOf(range[0]).compareTo(Long.valueOf(dstStr)) <= 0
									&& Long.valueOf(range[1]).compareTo(Long.valueOf(dstStr)) >= 0)) {
								flag = true;
							}

						} else if (commonSourceFieldCfg.getRange().startsWith("-")) {// 只有最大值限制
							if (Long.valueOf(range[0]).compareTo(Long.valueOf(dstStr)) <= 0) {
								flag = true;
							}
						} else if (commonSourceFieldCfg.getRange().endsWith("-")) {// 只有最小值限制
							if (Long.valueOf(range[0]).compareTo(Long.valueOf(dstStr)) >= 0) {
								flag = true;
							}
						}
						if (flag) {
							throw new RestServiceException(commonSourceFieldCfg.getSrcName() + "参数不在有效范围("
									+ commonSourceFieldCfg.getRange() + ")",
									RestBusinessCode.ValueInWrongRange.getValue());
						}
					}
					// regexp 特殊格式正则验证
					Boolean valFlag = true;
					if (!StringUtil.isEmpty(commonSourceFieldCfg.getRegexp())) {
						Pattern pattern = Pattern.compile(commonSourceFieldCfg.getRegexp());
						Matcher matcher = pattern.matcher(dstStr);
						valFlag = valFlag & matcher.matches();
					}
					if (valFlag) {
						dstMap.put(commonSourceFieldCfg.getDstName(), dstStr);
					} else {
						throw new RestServiceException(commonSourceFieldCfg.getSrcName() + "参数格式与正则("
								+ commonSourceFieldCfg.getRegexp() + ")不匹配",
								RestBusinessCode.ValueInWrongRegexp.getValue());
					}
				}
				if (StringUtil.isEmpty(dstMaps.get(Integer.valueOf(srcMap.get("service").toString())))) {
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					list.add(dstMap);
					dstMaps.put(Integer.valueOf(srcMap.get("service").toString()), list);
				} else {
					List<Map<String, String>> list = dstMaps.get(Integer.valueOf(srcMap.get("service").toString()));
					list.add(dstMap);

				}
			} else {
				throw new RestServiceException("service参数不能为空", RestBusinessCode.ServiceIsNull.getValue());
			}
		}
		logger.info("------------------调用非maat配置新增接口-------------------");
		// 按service分库
		Map<Integer, List<Map<String, String>>> configMap = new HashMap<Integer, List<Map<String, String>>>();
		Iterator serviceIterator = dstMaps.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					if (configMap.containsKey(dbIndex)) {
						configMap.get(dbIndex).addAll(dstMaps.get(service));
					} else {
						List<Map<String, String>> list = new ArrayList<Map<String, String>>();
						list.addAll(dstMaps.get(service));
						configMap.put(dbIndex, list);
					}
				}
			} else {
				throw new ServiceRuntimeException("service与写入数据库序号映射关系不存在",
						RestBusinessCode.ServiceNoFoundDBIndex.getValue());
			}

		}
		configRedisService.saveUnMaatConfig(configMap);
	}

	public void saveCommonSources(String jsonString) throws Exception {
		JsonArray jsonObjectList = null;
		try {
			jsonObjectList = new JsonParser().parse(jsonString).getAsJsonArray();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RestServiceException(RestBusinessCode.CBParamFormateError.getErrorReason() + "," + e.getMessage(),
					RestBusinessCode.CBParamFormateError.getValue());
		}
		Map<Integer, List<Map<String, String>>> dstMaps = new HashMap<Integer, List<Map<String, String>>>();
		for (int i = 0; i < jsonObjectList.size(); i++) {
			JsonObject jsonObj = (JsonObject) jsonObjectList.get(i);
			Map<String, Object> srcMap = JSONObject.fromObject(JSONObject.fromObject((jsonObj.toString())));
			if (srcMap.containsKey("service")) {
				Map<String, String> dstMap = new HashMap<String, String>();
				List<CommonSourceFieldCfg> commonSourceFieldCfgList = ReadCommSourceXmlUtil
						.getCommonSourceCfgByService(srcMap.get("service").toString().trim());

				// 获取IP类型
				Integer ipType = null;
				String ipTypeName = "";

				for (CommonSourceFieldCfg commonSourceFieldCfg : commonSourceFieldCfgList) {
					if (commonSourceFieldCfg.getDstName().equals("addr_type")) {
						String dstVal = srcMap.get(commonSourceFieldCfg.getSrcName()).toString();
						ipType = Integer.parseInt(dstVal);
					}
				}
				if (ipType == null) {
					ipType = 4;
				}
				for (CommonSourceFieldCfg commonSourceFieldCfg : commonSourceFieldCfgList) {
					// 字段类型 String Number Date Ip Port
					String dstStr = StringUtil.isEmpty(srcMap.get(commonSourceFieldCfg.getSrcName()))
							? commonSourceFieldCfg.getDefaultVal()
							: srcMap.get(commonSourceFieldCfg.getSrcName()).toString();
					if (!StringUtil.isEmpty(dstStr) && dstStr.startsWith("[") && dstStr.endsWith("]")) {
						dstStr = srcMap.get(dstStr.substring(1, dstStr.length() - 1)).toString();
					}

					if ("dstFile".equals(commonSourceFieldCfg.getSrcName())) {
						if ("dst_file".equals(commonSourceFieldCfg.getDstName())) {
							String maatTableName = ServiceAndRDBIndexReal
									.getUnMaatTableName(Integer.valueOf(srcMap.get("service").toString().trim()));
							String dstPath = Constants.MM_SAMPLE_DST_PATH.replace("{tableType}",
									maatTableName.substring(maatTableName.lastIndexOf("_") + 1));
							dstStr = dstPath.replace("{fileName}", dstStr.substring(dstStr.lastIndexOf("/") + 1));
						} else if ("file_id".equals(commonSourceFieldCfg.getDstName())) {
							// dstStr = dstStr.substring(dstStr.indexOf("group"));
						}
					}
					dstMap.put(commonSourceFieldCfg.getDstName(), dstStr);
				}
				if (StringUtil.isEmpty(dstMaps.get(Integer.valueOf(srcMap.get("service").toString())))) {
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					list.add(dstMap);
					dstMaps.put(Integer.valueOf(srcMap.get("service").toString()), list);
				} else {
					List<Map<String, String>> list = dstMaps.get(Integer.valueOf(srcMap.get("service").toString()));
					list.add(dstMap);

				}
			}
		}
		logger.info("------------------调用非maat配置新增接口-------------------");
		// 按service分库
		Map<Integer, List<Map<String, String>>> configMap = new HashMap<Integer, List<Map<String, String>>>();
		Iterator serviceIterator = dstMaps.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					if (configMap.containsKey(dbIndex)) {
						configMap.get(dbIndex).addAll(dstMaps.get(service));
					} else {
						List<Map<String, String>> list = new ArrayList<Map<String, String>>();
						list.addAll(dstMaps.get(service));
						configMap.put(dbIndex, list);
					}
				}
			} else {
				throw new ServiceRuntimeException("service与写入数据库序号映射关系不存在",
						RestBusinessCode.ServiceNoFoundDBIndex.getValue());
			}

		}
		configRedisService.saveUnMaatConfig(configMap);
	}

	/**
	 * 
	 * @Description:回调类配置状态更新（停/启用)
	 * @author (zdx)
	 * @date 2018年8月10日 下午4:28:04
	 * @param thread
	 * @param start
	 * @param jsonString
	 * @param opTime
	 * @param sb
	 * @throws Exception
	 */
	public void updateCommonSources(AuditLogThread thread, long start, String jsonString, Date opTime, StringBuffer sb)
			throws Exception {
		JsonArray jsonObjectList = null;
		try {
			jsonObjectList = new JsonParser().parse(jsonString).getAsJsonArray();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RestServiceException(RestBusinessCode.CBParamFormateError.getErrorReason() + "," + e.getMessage(),
					RestBusinessCode.CBParamFormateError.getValue());
		}
		if (jsonObjectList != null && jsonObjectList.size() > Constants.MAX_LIST_SIZE) {
			thread.setSaveContentFlag(false);
		}
		// <service,cfgIdList>
		Map<Integer, List<Long>> cfgMap = new HashMap<Integer, List<Long>>();
		// 所有状态更新的配置isValid的值必须相同
		Map<String, String> validIdMap = new HashMap<String, String>();
		for (int i = 0; i < jsonObjectList.size(); i++) {
			JsonObject jsonObj = (JsonObject) jsonObjectList.get(i);
			Map<String, Object> srcMap = JSONObject.fromObject(JSONObject.fromObject((jsonObj.toString())));
			checkOptForUpdate(srcMap);
			validIdMap.put(srcMap.get("isValid").toString(), srcMap.get("isValid").toString());
			Integer service = Integer.valueOf(srcMap.get("service").toString());

			List<CommonSourceFieldCfg> list = ReadCommSourceXmlUtil.getCommonSourceCfgByService(service + "");
			String srcName = "cfgId";
			for (CommonSourceFieldCfg commonSourceFieldCfg : list) {
				if (commonSourceFieldCfg.getIsCfgId()) {
					srcName = commonSourceFieldCfg.getSrcName();
				}
			}
			Long cfgId = Long.valueOf(srcMap.get(srcName).toString());
			if (cfgMap.containsKey(service)) {
				cfgMap.get(service).add(cfgId);
			} else {
				List<Long> idList = new ArrayList<Long>();
				idList.add(cfgId);
				cfgMap.put(service, idList);
			}
		}

		if (validIdMap.size() > 1) {
			throw new RestServiceException(RestBusinessCode.IsValidNonUniq.getErrorReason(),
					RestBusinessCode.IsValidNonUniq.getValue());
		}
		Map<Integer, Map<Integer, List<Long>>> restMap = new HashMap<Integer, Map<Integer, List<Long>>>();
		Iterator serviceIterator = cfgMap.keySet().iterator();
		while (serviceIterator.hasNext()) {
			Integer service = Integer.valueOf(serviceIterator.next().toString());
			List<Integer> dbIndexList = ServiceAndRDBIndexReal.getRedisDBByService(service);
			if (!StringUtil.isEmpty(dbIndexList) && dbIndexList.size() > 0) {
				for (Integer dbIndex : dbIndexList) {
					if (restMap.containsKey(dbIndex)) {
						restMap.get(dbIndex).put(service, cfgMap.get(service));
					} else {
						Map<Integer, List<Long>> map = new HashMap<Integer, List<Long>>();
						map.put(service, cfgMap.get(service));
						restMap.put(dbIndex, map);
					}
				}
			} else {
				throw new ServiceRuntimeException("service与写入数据库序号映射关系不存在",
						RestBusinessCode.ServiceNoFoundDBIndex.getValue());
			}
		}
		try {
			// 停用时isInvalid=true,启用时isInvalid = false
			configRedisService.delUnMaatConfig(restMap, validIdMap.containsKey("0") ? true : false);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
			// throw new
			// ServiceRuntimeException(RestBusinessCode.service_runtime_error.getErrorReason(),RestBusinessCode.service_runtime_error.getValue());
		}
	}

	/**
	 * 
	 * @Description:对回调类配置状态更新操作数据进行格式验证
	 * @author (zdx)
	 * @date 2018年8月10日 下午3:00:13
	 * @param srcMap
	 * @throws Exception
	 */
	private void checkOptForUpdate(Map<String, Object> srcMap) throws Exception {
		Object service = srcMap.get("service");
		Object isValid = srcMap.get("isValid");

		if (StringUtil.isEmpty(service)) {
			throw new RestServiceException(RestBusinessCode.ServiceIsNull.getErrorReason(),
					RestBusinessCode.ServiceIsNull.getValue());
		} else if (!StringUtil.isNumeric(service.toString())) {
			throw new RestServiceException("service字段格式不正确," + RestBusinessCode.MastNumberic.getErrorReason(),
					RestBusinessCode.MastNumberic.getValue());
		}
		List<CommonSourceFieldCfg> list = ReadCommSourceXmlUtil.getCommonSourceCfgByService(service + "");
		String srcName = "cfgId";
		for (CommonSourceFieldCfg commonSourceFieldCfg : list) {
			if (commonSourceFieldCfg.getIsCfgId()) {
				srcName = commonSourceFieldCfg.getSrcName();
			}
		}
		Object cfgId = srcMap.get(srcName);
		if (StringUtil.isEmpty(cfgId)) {
			throw new RestServiceException(RestBusinessCode.CfgIdIsNull.getErrorReason(),
					RestBusinessCode.CfgIdIsNull.getValue());
		} else if (!StringUtil.isNumeric(cfgId.toString())) {
			throw new RestServiceException("cfgId字段格式不正确," + RestBusinessCode.MastNumberic.getErrorReason(),
					RestBusinessCode.MastNumberic.getValue());
		}
		if (StringUtil.isEmpty(isValid)) {
			throw new RestServiceException("配置id为" + srcMap.get("cfgId") + "的isValid字段不能为空",
					RestBusinessCode.IsValidIsNull.getValue());
		} else {
			if (!StringUtil.isNumeric(service.toString())) {
				throw new RestServiceException(RestBusinessCode.IsValidInWrongRange.getErrorReason(),
						RestBusinessCode.IsValidInWrongRange.getValue());
			}
			// 配置取消改为状态更新（停/启用）
			// else if (!isValid.equals("0")) {
			// CompileVal.setBusinessCode(RestBusinessCode.IsValidIsF.getValue());
			// return RestBusinessCode.IsValidIsF.getErrorReason();
			// }
		}
	}

	/**
	 * 
	 * @Description:在linux系统下，使用摘要获取工具获取文件摘要信息
	 * @author (zdx)
	 * @date 2018年8月10日 下午4:29:38
	 * @param realPath
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String getDigestGen(String realPath, String filePath) throws Exception {
		logger.info("----------------开始获取摘要：getDigestGen");
		String content = "";
		String digestGenToolPath = Constants.DIGEST_GEN_TOOL_PATH;
		String chmodCommond = "chmod +x " + realPath + digestGenToolPath;// 执行权限命令
		// System.out.println("----------------chmodCommod:"+chmodCommond);
		String commondStr = realPath + digestGenToolPath + " -f " + filePath; // 执行
		// 执行摘要获取命令 digest -f /home/aa.txt
		logger.info("------------commondStr:" + commondStr);
		Runtime.getRuntime().exec(chmodCommond);
		Process p = Runtime.getRuntime().exec(commondStr);
		byte[] b = new byte[1024];
		StringBuffer sb = new StringBuffer();
		while (p.getInputStream().read(b) != -1) {
			sb.append(new String(b, "UTF-8"));
		}
		content = sb.toString();
		if (!StringUtil.isBlank(content)) {
			content = StringUtil.stripAll(content);
		}
		logger.info("摘要工具获取到的内容-------------------->>" + content);
		if (StringUtil.isEmpty(content)) {
			throw new Exception("调用摘要工具获取到的内容为空");
		}
		String[] digestGenReslt = content.split(" ");
		if (digestGenReslt.length >= 4 && !StringUtil.isEmpty(digestGenReslt[3])) {
			return digestGenReslt[3];
		}
		return null;
	}

	/**
	 * 
	 * @Description:增加分组复用域配置
	 * @author (zdx)
	 * @date 2018年5月25日 下午3:49:46
	 * @param thread
	 * @param start
	 * @param configCompileList
	 * @param sb
	 * @return
	 */
	public void addGroupReuseSources(AuditLogThread thread, long start, List<GroupReuse> groupReuseList,
			StringBuffer sb) throws Exception {
		List<MaatConfig> list = new ArrayList<MaatConfig>();
		// 验证
		GroupReuseVal.valGroupReuse(groupReuseList, false);

		for (GroupReuse groupReuse : groupReuseList) {
			MaatConfig maatConfig = new MaatConfig();
			List<Map<String, String>> dstMapList = null;
			// 字符串域
			List<Map<String, String>> strongMapList = null;
			if (!StringUtil.isEmpty(groupReuse.getStrRegionList())) {
				dstMapList = new ArrayList<Map<String, String>>();
				for (StrRegion region : groupReuse.getStrRegionList()) {
					if (StringUtil.isEmpty(region.getDistrict())) {
						dstMapList.add(convertObjectToMap(region, StrRegion.class));
					} else {
						if (StringUtil.isEmpty(strongMapList)) {
							strongMapList = new ArrayList<Map<String, String>>();
						}
						strongMapList.add(convertObjectToMap(region, StrRegion.class));
					}
				}
			}
			if (StringUtil.isEmpty(maatConfig.getStrRegionMapList())) {
				maatConfig.setStrRegionMapList(dstMapList);
			} else {
				maatConfig.getStrRegionMapList().addAll(dstMapList);
			}

			// 增强字符串域
			if (!StringUtil.isEmpty(strongMapList) && strongMapList.size() > 0) {
				if (StringUtil.isEmpty(maatConfig.getStrStrRegionMapList())) {
					maatConfig.setStrStrRegionMapList(strongMapList);
				} else {
					maatConfig.getStrStrRegionMapList().addAll(strongMapList);
				}
			}
			// 数值域
			dstMapList = null;
			if (!StringUtil.isEmpty(groupReuse.getNumRegionList())) {
				dstMapList = new ArrayList<Map<String, String>>();
				for (NumRegion region : groupReuse.getNumRegionList()) {
					dstMapList.add(convertObjectToMap(region, NumRegion.class));
				}
			}

			if (StringUtil.isEmpty(maatConfig.getNumRegionMapList())) {
				maatConfig.setNumRegionMapList(dstMapList);
			} else {
				maatConfig.getNumRegionMapList().addAll(dstMapList);
			}

			// Ip域
			dstMapList = null;
			if (!StringUtil.isEmpty(groupReuse.getIpRegionList())) {
				dstMapList = new ArrayList<Map<String, String>>();
				for (IpRegion region : groupReuse.getIpRegionList()) {
					dstMapList.add(convertObjectToMap(region, IpRegion.class));
				}
			}
			if (StringUtil.isEmpty(maatConfig.getIpRegionMapList())) {
				maatConfig.setIpRegionMapList(dstMapList);
			} else {
				maatConfig.getIpRegionMapList().addAll(dstMapList);
			}

			if ((maatConfig.getStrRegionMapList() != null
					&& maatConfig.getStrRegionMapList().size() > Constants.MAX_LIST_SIZE)
					|| (maatConfig.getStrStrRegionMapList() != null
							&& maatConfig.getStrStrRegionMapList().size() > Constants.MAX_LIST_SIZE)
					|| (maatConfig.getIpRegionMapList() != null
							&& maatConfig.getIpRegionMapList().size() > Constants.MAX_LIST_SIZE)
					|| (maatConfig.getNumRegionMapList() != null
							&& maatConfig.getNumRegionMapList().size() > Constants.MAX_LIST_SIZE)) {
				thread.setSaveContentFlag(false);
			}
			// maatConfig.setService(groupReuse.getService());
			list.add(maatConfig);
		}

		// 调用接口入redis
		logger.info("---------------调用Redis 分组复用配置新增接口---------------------");
		configRedisService.saveGroupReuseConfig(list);
//		configRedisService.saveGroupReuseConfigByPipeLine(list);
	}

	/**
	 * 
	 * @Description:删除分组复用域配置
	 * @author (zdx)
	 * @date 2018年8月23日 下午7:37:14
	 * @param thread
	 * @param start
	 * @param groupReuseList
	 * @param opTime
	 * @param sb
	 * @throws Exception
	 */
	public void deleteGroupReuseSources(AuditLogThread thread, long start, List<GroupReuse> groupReuseList, Date opTime,
			StringBuffer sb) throws Exception {
		// Map<groupId,List<"tableName,regionId">
		Map<Long, List<String>> reuseMap = new HashMap<Long, List<String>>();

		for (GroupReuse groupReuse : groupReuseList) {
			// 验证格式
			checkGroupReuseForUpdate(groupReuse);

			// 字符串域
			if (!StringUtil.isEmpty(groupReuse.getStrRegionList())) {
				for (StrRegion region : groupReuse.getStrRegionList()) {
					if (reuseMap.containsKey(region.getGroupId())) {
						reuseMap.get(region.getGroupId()).add(region.getTableName() + "," + region.getRegionId());
					} else {
						List<String> regionList = new ArrayList<String>();
						regionList.add(region.getTableName() + "," + region.getRegionId());
						reuseMap.put(region.getGroupId(), regionList);
					}
				}
			}

			// 数值域
			if (!StringUtil.isEmpty(groupReuse.getNumRegionList())) {
				for (NumRegion region : groupReuse.getNumRegionList()) {
					if (reuseMap.containsKey(region.getGroupId())) {
						reuseMap.get(region.getGroupId()).add(region.getTableName() + "," + region.getRegionId());
					} else {
						List<String> regionList = new ArrayList<String>();
						regionList.add(region.getTableName() + "," + region.getRegionId());
						reuseMap.put(region.getGroupId(), regionList);
					}
				}
			}

			// Ip域
			if (!StringUtil.isEmpty(groupReuse.getIpRegionList())) {
				for (IpRegion region : groupReuse.getIpRegionList()) {
					if (reuseMap.containsKey(region.getGroupId())) {
						reuseMap.get(region.getGroupId()).add(region.getTableName() + "," + region.getRegionId());
					} else {
						List<String> regionList = new ArrayList<String>();
						regionList.add(region.getTableName() + "," + region.getRegionId());
						reuseMap.put(region.getGroupId(), regionList);
					}
				}
			}
		}
		logger.info("调用接口删除Redis中分组复用的域配置接口");
		if (reuseMap != null && reuseMap.size() > Constants.MAX_LIST_SIZE) {
			thread.setSaveContentFlag(false);
		}
		// 所有的都删除成功返回true
		if (!configRedisService.delGroupReuseConfig(reuseMap)) {
//			if (!configRedisService.delGroupReuseConfigByPipeline(reuseMap)) {
			throw new ServiceRuntimeException("删除分组利用域配置时出现异常，具体原因不详，请联系管理员",
					RestBusinessCode.service_runtime_error.getValue());
		}
	}

	/**
	 * 
	 * @Description:验证service+regionId+isValid+tableName
	 * @author (zdx)
	 * @date 2017年8月16日 上午11:48:46
	 * @param config
	 * @return
	 */
	private void checkGroupReuseForUpdate(GroupReuse config) throws Exception {

		Boolean hasRegionFlag = false;
		Map<String, List<String>> groupReuseRegionMap = ServiceAndRDBIndexReal.getGroupReuseRegionMap();
		if (groupReuseRegionMap.containsKey("numRegion") && !StringUtil.isEmpty(config.getNumRegionList())
				&& config.getNumRegionList().size() > 0) {
			hasRegionFlag = true;
			List<NumRegion> numRegionList = config.getNumRegionList();
			for (NumRegion numRegion : numRegionList) {
				if (StringUtil.isEmpty(numRegion.getTableName())
						|| !groupReuseRegionMap.get("numRegion").contains(numRegion.getTableName())) {
					throw new RestServiceException(
							"numRegionList中的regionId为" + numRegion.getRegionId() + "的域配置tableName为空或不是分组复用的域表",
							RestBusinessCode.TableNameUnReuse.getValue());
				}
				if (numRegion.getIsValid() != 0) {
					throw new RestServiceException(
							"numRegionList中的regionId为" + numRegion.getRegionId() + "的域配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}
			}
		} else if (groupReuseRegionMap.containsKey("strRegion") && !StringUtil.isEmpty(config.getStrRegionList())
				&& config.getStrRegionList().size() > 0) {
			hasRegionFlag = true;
			List<StrRegion> strRegionList = config.getStrRegionList();
			for (StrRegion strRegion : strRegionList) {
				if (StringUtil.isEmpty(strRegion.getTableName())
						|| !groupReuseRegionMap.get("strRegion").contains(strRegion.getTableName())) {
					throw new RestServiceException(
							"strRegionList中的regionId为" + strRegion.getRegionId() + "的域配置tableName为空或不是分组复用的域表",
							RestBusinessCode.TableNameUnReuse.getValue());
				}
				if (strRegion.getIsValid() != 0) {
					throw new RestServiceException(
							"strRegionList中的regionId为" + strRegion.getRegionId() + "的域配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}

			}
		} else if (groupReuseRegionMap.containsKey("ipRegion") && !StringUtil.isEmpty(config.getIpRegionList())
				&& config.getIpRegionList().size() > 0) {
			hasRegionFlag = true;
			List<IpRegion> ipRegionList = config.getIpRegionList();
			for (IpRegion ipRegion : ipRegionList) {

				if (StringUtil.isEmpty(ipRegion.getTableName())
						|| !groupReuseRegionMap.get("ipRegion").contains(ipRegion.getTableName())) {
					throw new RestServiceException(
							"ipRegionList中的regionId为" + ipRegion.getRegionId() + "的域配置tableName为空或不是分组复用的域表",
							RestBusinessCode.TableNameUnReuse.getValue());
				}
				if (null == ipRegion.getIsValid() || ipRegion.getIsValid() != 0) {
					throw new RestServiceException("ipRegionList中的regionId为" + ipRegion.getRegionId() + "的域配置在修改时不能为有效",
							RestBusinessCode.IsValidIsF.getValue());
				}
			}
		}

		if (!hasRegionFlag) {
			// 所有的域类型都不包括，可能是配置文件配错了
			throw new ServiceRuntimeException(RestBusinessCode.ReuseRegionIsNull.getErrorReason(),
					RestBusinessCode.ReuseRegionIsNull.getValue());
		}
	}

	/**
	 * 设置配置全量同步状态
	 * 
	 * @param value
	 */
	public void setAllConfigSyncStatus(String value) {
		JedisClusterUtils.set("allConfigSyncStatus", value, Constants.CONFIGSYNCLOCKTIME.intValue());
	}

	/**
	 * 获取配置全量同步状态
	 * 
	 * @return
	 */
	public String getAllConfigSyncStatus() {
		return JedisClusterUtils.get("allConfigSyncStatus");
	}

	/**
	 * 将界面发过来的数据存储到rediscluster中
	 * 
	 * @param key
	 * @param value
	 */
	public void setRedisClusterKey(String key, String value) {
		JedisClusterUtils.set(key, value, 86400);// 24小时超时
	}

	/**
	 * 将所有业务的配置key记录下来,方便读取
	 * 
	 * @param value
	 */
	public void setAllServiceKey(String value) {
		JedisCluster resource = JedisClusterUtils.getResource();
		resource.append("allConfigSyncKey", value + ";");
	}

	public Map<String, Integer> getAllConfig() {
		Jedis resource = JedisUtils.getResource(0);
		Set<String> effectiveSet = new HashSet<>();
		Set<String> obsoleteSet = new HashSet<>();
		for (int i = 2; i < 6; i++) {
			resource.select(i);
			effectiveSet.addAll(resource.keys("EFFECTIVE_RULE:*_COMPILE*"));
			obsoleteSet.addAll(resource.keys("OBSOLETE_RULE:*_COMPILE*"));
		}
		Map<String, Integer> map = new HashMap<>();
		map.put("effectiveMaatKeys", effectiveSet.size());
		map.put("obsoleteMaatKeys", obsoleteSet.size());
		JedisUtils.returnBrokenResource(resource);
		return map;
	}

	public Map<String, Integer> getAllConfigByScan() {
		Jedis resource = JedisUtils.getResource(0);
		Set<String> effectiveSet = new HashSet<>();
		Set<String> obsoleteSet = new HashSet<>();
		for (int i = 2; i < 6; i++) {
			resource.select(i);
			effectiveSet.addAll(getKeyByScan("EFFECTIVE_RULE:*_COMPILE*", resource));
			obsoleteSet.addAll(getKeyByScan("OBSOLETE_RULE:*_COMPILE*", resource));
		}
		Map<String, Integer> map = new HashMap<>();
		map.put("effectiveMaat", effectiveSet.size());
		map.put("obsoleteMaat", obsoleteSet.size());
		JedisUtils.returnBrokenResource(resource);
		return map;
	}

	public List<String> getKeyByScan(String pattern, Jedis resource) {
		List<String> list = new ArrayList<>();
		int count = 1000;
		String cursor = ScanParams.SCAN_POINTER_START;
		ScanParams scanParams = new ScanParams();
		scanParams.count(count);
		scanParams.match(pattern);

		do {
			ScanResult<String> scanResult = resource.scan(cursor, scanParams);
			list.addAll(scanResult.getResult());
			cursor = scanResult.getStringCursor();
		} while (!"0".equals(cursor));
		return list;
	}
}
