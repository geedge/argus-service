package com.nis.web.service.restful;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.domain.restful.CommonSourceFieldCfg;
import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DigestRegion;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.BasicProvingUtil;
import com.nis.util.CamelUnderlineUtil;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.FileUtils;
import com.nis.util.JedisUtils;
import com.nis.util.ReadCommSourceXmlUtil;
import com.nis.util.ReadMaatXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.zdjizhi.utils.StringUtil;

import net.sf.json.JSONObject;

@Service()
public class MaatTestServiceimpl {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final int TAPREDISDB = Configurations.getIntProperty("tapRedisDb", 5);

	public void getMaatKeys(List<ConfigCompile> configCompileList) throws Exception {
		if (configCompileList != null && configCompileList.size() > 0) {
			for (ConfigCompile configCompile : configCompileList) {
				Integer service = configCompile.getService();
				MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
				Map<String, String[]> tapFieldMap = ServiceAndRDBIndexReal.getMaatToValveMap().get(service);
				//用于判断是否是入阀门时添加的额外字段
				Map<String, String> keyMap = new HashMap<String, String>();
				if(null!=tapFieldMap){
					Iterator iterator =  tapFieldMap.keySet().iterator();
					while (iterator.hasNext()) {
						String [] tapFields = tapFieldMap.get(iterator.next().toString());
						for (String obj : tapFields) {
							keyMap.put(CamelUnderlineUtil.underlineToCamel(obj).toLowerCase(), obj.toLowerCase());
						}
					}
				}
				List<Integer> redisDBList = ServiceAndRDBIndexReal.getRedisDBByService(service);
				if (ServiceAndRDBIndexReal.isAddASU(service)) {
					if (!redisDBList.contains(TAPREDISDB)) {
						throw new RuntimeException("业务类型:" + service + ",需要向阀门" + TAPREDISDB + "号库分发,但是当前只往"
								+ redisDBList + "库下发,请检查阀门编号或者当前业务对应的配置文件是否正确");
					}
				}
				for (Integer redisDB : redisDBList) {
					getConfigByKey(redisDB, service, configCompile, maatXmlConfig,keyMap);
				}

			}
		}

	}

	public void getConfigByKey(Integer redisDB, Integer service, ConfigCompile configCompile,
			MaatXmlConfig maatXmlConfig,Map<String, String> keyMap) throws Exception {
		if (maatXmlConfig != null && configCompile != null) {
			Long compileId = configCompile.getCompileId();
			validData(service, 10, compileId, configCompile.getCompileId(), redisDB, null, maatXmlConfig, configCompile,
					configCompile,keyMap);

			List<ConfigGroupRelation> groupRelationList = configCompile.getGroupRelationList();
			for (ConfigGroupRelation configGroupRelation : groupRelationList) {
				validData(service, 11, compileId, configGroupRelation.getGroupId(), redisDB, null, maatXmlConfig,
						configGroupRelation, configCompile,keyMap);
			}

			List<IpRegion> ipRegionList = configCompile.getIpRegionList();
			for (IpRegion ipRegion : ipRegionList) {
				validData(service, 12, compileId, ipRegion.getRegionId(), redisDB, ipRegion.getTableName(),
						maatXmlConfig, ipRegion, configCompile,keyMap);
			}
			List<NumRegion> numRegionList = configCompile.getNumRegionList();
			for (NumRegion numRegion : numRegionList) {
				validData(service, 13, compileId, numRegion.getRegionId(), redisDB, numRegion.getTableName(),
						maatXmlConfig, numRegion, configCompile,keyMap);
			}

			List<StrRegion> strRegionList = configCompile.getStrRegionList();
			for (StrRegion strRegion : strRegionList) {
				if (strRegion.getDistrict() == null || strRegion.getDistrict().trim().equals("")) {
					validData(service, 14, compileId, strRegion.getRegionId(), redisDB, strRegion.getTableName(),
							maatXmlConfig, strRegion, configCompile,keyMap);
				} else {
					validData(service, 15, compileId, strRegion.getRegionId(), redisDB, strRegion.getTableName(),
							maatXmlConfig, strRegion, configCompile,keyMap);
				}
			}

			List<DigestRegion> digestRegionList = configCompile.getDigestRegionList();
			for (DigestRegion digestRegion : digestRegionList) {
				validData(service, 16, compileId, digestRegion.getRegionId(), redisDB, digestRegion.getTableName(),
						maatXmlConfig, digestRegion, configCompile,keyMap);
			}

			List<IpRegion> ipClientRangeList = configCompile.getIpClientRangeList();
			for (IpRegion ipRegion : ipClientRangeList) {
				validData(service, 18, compileId, ipRegion.getRegionId(), redisDB, ipRegion.getTableName(),
						maatXmlConfig, ipRegion, configCompile,keyMap);
			}

		}
	}

	public void validData(Integer service, Integer type, Long compileId, Long id, Integer redisDb, String tableName,
			MaatXmlConfig maatXmlConfig, Object obj, ConfigCompile configCompile,Map<String, String> keyMap) throws Exception {
		List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
		String[] valArr = null;
		String key = null;
		List<String> valList = new ArrayList<String>();
		for (MaatXmlExpr maatXmlExpr : expressionList) {
			if (type == maatXmlExpr.getType().intValue()) {
				StringBuffer keyBF = new StringBuffer();
				String[] keySplit = maatXmlExpr.getKeyExpression().split(";");
				for (String keyStr : keySplit) {
					if (!StringUtils.isEmpty(keyStr)
							&& keyStr.trim().startsWith("[") & keyStr.toLowerCase().contains("id")) {
						if (type == 11) {
							keyBF.append(id + "" + compileId);
						} else {
							keyBF.append(id);
						}

					} else if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("{")) {
						keyStr = keyStr.trim().replace("{", "").replace("}", "");
						if (keyStr.toLowerCase().contains("table_name")) {
							String maatTableName = ServiceAndRDBIndexReal.getMaatTableName(service, type,
									tableName == null ? null : tableName);
							if (maatTableName == null) {
								throw new RuntimeException(
										"未从业务类型和表对应关系中,找到业务类型:" + service + ",配置类型:" + type + ",对应的真实表名");
							} else {
								keyBF.append(maatTableName);
							}
						}

					} else {
						keyBF.append(keyStr.trim());
					}
				}
				key = keyBF.toString().toUpperCase();
				String[] valSplit = maatXmlExpr.getValueExpression().replace(";&nbsp;0;\\n", "").replace("\\t", "")
						.split(";");
				for (String valStr : valSplit) {
					if (valStr != null && !valStr.trim().equals("")) {
						if (!StringUtils.isEmpty(valStr) && valStr.trim().startsWith("[")) {
							valStr = valStr.trim().replace("[", "").replace("]", "");
							if (TAPREDISDB == redisDb && type != 10 && type != 11
									&& valStr.toLowerCase().equals("op_time")) {// 域配置并且是op_time时在op_time前面添加如下内容
								Map<String, String[]> map2 = ServiceAndRDBIndexReal.getMaatToValveMap().get(service);
								if (map2 != null && map2.size() > 0) {
									String[] arr = null;
									if (type == 12) {// IP类
										arr = map2.get("ipRegion");
									} else if (type == 13) {// 数值类
										arr = map2.get("numRegion");
									} else if (type == 14) {// 字符串类
										arr = map2.get("strRegion");
									} else if (type == 15) {// 增强字符串类
										arr = map2.get("strStrRegion");
									} else if (type == 16) {// 文件摘要类
										arr = map2.get("fileDigestRegion");
									} else if (type == 17) {// 文件摘要类
										arr = map2.get("fileLikeRegion");
									} else if (type == 18) {// 文件摘要类
										arr = map2.get("ipClientRange");
									}
									if (arr != null && arr.length > 0) {
										for (String str : arr) {
											valList.add(str.toLowerCase());
										}
									}
								}
							}
							valList.add(valStr);
						} else {
							valList.add(valStr);
						}
					}
				}
				String val = JedisUtils.get(key, redisDb);
				FileUtils.addStrToFile("\t\t入库的key=" + key + "\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);
				FileUtils.addStrToFile("\t\t入库的val=" + val, Configurations.getStringProperty("maatTestLogPath", ""),
						true);
				FileUtils.addStrToFile("\t\t表达式" + maatXmlExpr.getValueExpression() + "\n",
						Configurations.getStringProperty("maatTestLogPath", ""), true);

				valArr = val.split("\\t");
				break;
			}
		}
		if (sysoLog(valArr, valList, obj, redisDb, key, configCompile, service,keyMap)) {
			if (TAPREDISDB == redisDb && type != 10 && type != 11) {// 域配置并且是op_time时在op_time前面添加如下内容
				Map<String, String[]> map2 = ServiceAndRDBIndexReal.getMaatToValveMap().get(service);
				if (map2 != null && map2.size() > 0) {
					String[] arr = null;
					if (type == 12) {// IP类
						arr = map2.get("ipRegion");
					} else if (type == 13) {// 数值类
						arr = map2.get("numRegion");
					} else if (type == 14) {// 字符串类
						arr = map2.get("strRegion");
					} else if (type == 15) {// 增强字符串类
						arr = map2.get("strStrRegion");
					} else if (type == 16) {// 文件摘要类
						arr = map2.get("fileDigestRegion");
					} else if (type == 17) {// 文件摘要类
						arr = map2.get("fileLikeRegion");
					} else if (type == 18) {// 文件摘要类
						arr = map2.get("ipClientRange");
					}
					if (arr != null && arr.length > 0) {
						StringBuffer sb = new StringBuffer();
						for (String str : arr) {
							sb.append(str.toLowerCase() + ",");
						}
						FileUtils.addStrToFile(
								"\t" + sdf.format(new Date()) + " 编译配置id为" + compileId + "的配置中,在" + redisDb
										+ "号redis库中key=" + key + "的值与实际插入的值相符(该库是阀门库并额外在optime字段前添加了两个字段"
										+ sb.substring(0, sb.length() - 1) + ")\n",
								Configurations.getStringProperty("maatTestLogPath", ""), true);
						return;
					}
				}
			}
			FileUtils
					.addStrToFile(
							"\t" + sdf.format(new Date()) + " 编译配置id为" + compileId + "的配置中,在" + redisDb + "号redis库中key="
									+ key + "的值与实际插入的值相符\n",
							Configurations.getStringProperty("maatTestLogPath", ""), true);

		}
	}

	private boolean sysoLog(String[] valArr, List<String> valList, Object obj, Integer redisDb, String key,
			ConfigCompile configCompile, Integer service,Map<String, String> keyMap) throws Exception {

		if (valList == null || valList.size() == 0) {
			FileUtils.addStrToFile(
					"\t" + sdf.format(new Date()) + " error:service=" + service + "时拼接value属性名,value表达式为空请检查\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);
			return false;
		}
		if (valArr == null || valArr.length == 0) {
			FileUtils.addStrToFile(
					"\t" + sdf.format(new Date()) + " error:service=" + service + "时拼接value值错误,value数组为空\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);
			return false;
		}
		if (valList == null || key.trim().equals("")) {
			FileUtils.addStrToFile("\t" + sdf.format(new Date()) + " error:service=" + service + "时拼接key=null请检查\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);
			return false;
		}
		boolean bool = true;
		for (int i = 0; i < valList.size(); i++) {
			String attrName = valList.get(i);
			if (!attrName.toLowerCase().equals("op_time")) {
				String filedName = CamelUnderlineUtil.underlineToCamel(attrName);
				String beanVal = null;
				//分发到阀门库添加的额外字段值从编译配置中获取
				if (keyMap.containsKey(filedName.toLowerCase())) {
					beanVal = getFieldValueByFieldName(filedName, configCompile);
				} else {
					beanVal = getFieldValueByFieldName(filedName, obj);
				}
				String redisVal = valArr[i];
				if (!beanVal.trim().equals(redisVal.trim())) {
					FileUtils.addStrToFile(
							"\t" + sdf.format(new Date()) + " error:" + redisDb + "号redis库中key=" + key + "的值第" + i + "位"
									+ attrName + "的值:" + redisVal + "与实际传的值不一样,实际值是:{}\n",
							Configurations.getStringProperty("maatTestLogPath", ""), true);
					bool = false;
				}
			}
		}
		return bool;
	}

	private String getFieldValueByFieldName(String fieldName, Object obj) throws Exception {
		// Field field = obj.getClass().getField(fieldName);

		Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(obj).toString();
	}

	public void getUnMaatKeys(String jsonStr, Integer service) {
		MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
		List<Integer> redisDBList = ServiceAndRDBIndexReal.getRedisDBByService(service);
		for (Integer redisDB : redisDBList) {
			JsonArray jsonObjectList = new JsonParser().parse(jsonStr).getAsJsonArray();
			for (int i = 0; i < jsonObjectList.size(); i++) {
				JsonObject jsonObj = (JsonObject) jsonObjectList.get(i);
				Map<String, String> srcMap = JSONObject.fromObject(JSONObject.fromObject((jsonObj.toString())));
				validUnMaatConfig(service, redisDB, maatXmlConfig, srcToDstMap(srcMap, service));
			}
		}
	}

	/**
	 * 根据applicationConfig-parser.xml将srcmap转换为dstmap,即将srcName替换为dstName
	 * @param srcMap
	 * @param service
	 * @return
	 */
	private Map<String, String> srcToDstMap(Map<String, String> srcMap, Integer service) {
		Map<String, String> dstMap = new HashMap<String, String>();
		List<CommonSourceFieldCfg> commonSourceFieldCfgList = ReadCommSourceXmlUtil
				.getCommonSourceCfgByService(srcMap.get("service").toString().trim());
		if (StringUtil.isEmpty(commonSourceFieldCfgList)) {
			throw new RestServiceException(RestBusinessCode.ServiceIsWrong.getErrorReason() + ",请检查service配置是否正确",
					RestBusinessCode.ServiceIsWrong.getValue());
		}
		// 获取IP类型
		Integer ipType = null;
		String ipTypeName = "";

		for (CommonSourceFieldCfg commonSourceFieldCfg : commonSourceFieldCfgList) {
			if (commonSourceFieldCfg.getDstName().equals("addr_type")) {
				Object obj = srcMap.get(commonSourceFieldCfg.getSrcName());
				String dstVal = obj.toString().trim();
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
				Object obj = srcMap.get(commonSourceFieldCfg.getSrcName());
				String dstVal = obj.toString().trim();
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
			if (commonSourceFieldCfg.getIsRequired() && !srcMap.containsKey(commonSourceFieldCfg.getSrcName())) {
				throw new RestServiceException(commonSourceFieldCfg.getSrcName() + "参数不能为空",
						RestBusinessCode.FieldIsNull.getValue());
			}
			Object obj = srcMap.get(commonSourceFieldCfg.getSrcName());
			String dstVal = obj.toString().trim();
			// 字段类型 String Number Date Ip Port
			String dstStr = StringUtil.isEmpty(dstVal) ? commonSourceFieldCfg.getDefaultVal() : dstVal;
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
//					dstStr = dstStr.substring(dstStr.indexOf("group"));
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
					throw new RestServiceException(commonSourceFieldCfg.getSrcName() + "参数格式不正确或与" + ipTypeName + "不一致",
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
					throw new RestServiceException(
							commonSourceFieldCfg.getSrcName() + "参数不在有效范围(" + commonSourceFieldCfg.getRange() + ")",
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
				throw new RestServiceException(
						commonSourceFieldCfg.getSrcName() + "参数格式与正则(" + commonSourceFieldCfg.getRegexp() + ")不匹配",
						RestBusinessCode.ValueInWrongRegexp.getValue());
			}
		}
		return dstMap;
	}

	public void validUnMaatConfig(Integer service, Integer redisDb, MaatXmlConfig maatXmlConfig,
			Map<String, String> map) {
		Long compileId = Long.parseLong(map.get("cfg_id"));// 配置id固定叫cfg_id
		List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
		String[] valArr = null;
		String key = null;
		List<String> valList = new ArrayList<String>();
		for (MaatXmlExpr maatXmlExpr : expressionList) {
			StringBuffer keyBF = new StringBuffer();
			String[] keySplit = maatXmlExpr.getKeyExpression().split(";");
			for (String keyStr : keySplit) {
				if (!StringUtils.isEmpty(keyStr)
						&& keyStr.trim().startsWith("[") & keyStr.toLowerCase().contains("id")) {
					keyBF.append(compileId);
				} else if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("{")) {
					keyStr = keyStr.trim().replace("{", "").replace("}", "");
					if (keyStr.toLowerCase().contains("un_maat_table_name")) {
						String unMaatTableName = ServiceAndRDBIndexReal.getUnMaatTableName(service);
						if (unMaatTableName == null) {
							throw new RuntimeException("未从业务类型和表对应关系中,找到业务类型:" + service + ",对应的真实表名");
						} else {
							keyBF.append(unMaatTableName);
						}
					}
				} else {
					keyBF.append(keyStr.trim());
				}
			}
			key = keyBF.toString().toUpperCase();
			String[] valSplit = maatXmlExpr.getValueExpression().replace(";&nbsp;0;\\n", "").replace(";&nbsp", "\\t")
					.replace("\\n", "").replace("\\t", "").split(";");
			for (String valStr : valSplit) {
				if (valStr != null && !valStr.trim().equals("")) {
					if (!StringUtils.isEmpty(valStr) && valStr.trim().startsWith("[")) {
						valStr = valStr.trim().replace("[", "").replace("]", "");
						valList.add(valStr);// xml中是字符串的\t这里判断的时候需要转义为\\t,但是添加的时候需要添加\t不是\\t
					} else {
						valList.add(valStr);
					}
				}
			}
			String val = JedisUtils.get(key, redisDb).replace(" ", "\t");

			FileUtils.addStrToFile("\t\t入库的key=" + key + "\n", Configurations.getStringProperty("maatTestLogPath", ""),
					true);
			FileUtils.addStrToFile("\t\t入库的val=" + val, Configurations.getStringProperty("maatTestLogPath", ""), true);
			FileUtils.addStrToFile("\t\t表达式" + maatXmlExpr.getValueExpression() + "\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);

			valArr = val.split("\\t");
			break;
		}
		if (sysoUnMaatLog(valArr, valList, redisDb, key, map, service)) {
			FileUtils
					.addStrToFile(
							"\t" + sdf.format(new Date()) + " 编译配置id为" + compileId + "的配置中,在" + redisDb + "号redis库中key="
									+ key + "的值与实际插入的值相符\n",
							Configurations.getStringProperty("maatTestLogPath", ""), true);
		}
	}

	private boolean sysoUnMaatLog(String[] valArr, List<String> valList, Integer redisDb, String key,
			Map<String, String> map, Integer service) {

		if (valList == null || valList.size() == 0) {
			FileUtils.addStrToFile(
					"\t" + sdf.format(new Date()) + " error:service=" + service + "时拼接value属性名,value表达式为空请检查\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);
			return false;
		}
		if (valArr == null || valArr.length == 0) {
			FileUtils.addStrToFile(
					"\t" + sdf.format(new Date()) + " error:service=" + service + "时拼接value值错误,value数组为空\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);
			return false;
		}
		if (valList == null || key.trim().equals("")) {
			FileUtils.addStrToFile("\t" + sdf.format(new Date()) + " error:service=" + service + "时拼接key=null请检查\n",
					Configurations.getStringProperty("maatTestLogPath", ""), true);
			return false;
		}
		boolean bool = true;
		for (int i = 0; i < valList.size(); i++) {
			String attrName = valList.get(i);
			if (!attrName.toLowerCase().equals("op_time")) {
				// String filedName = CamelUnderlineUtil.underlineToCamel(attrName);
				if (!isNum(attrName)) {
					String beanVal = map.get(attrName);
					String redisVal = valArr[i];
					if (!attrName.contains("redisFile")&&(beanVal == null || redisVal == null || !beanVal.trim().equals(redisVal.trim()))) {
						FileUtils.addStrToFile(
								"\t" + sdf.format(new Date()) + " error:" + redisDb + "号redis库中key=" + key + "的值第" + i
										+ "位" + attrName + "的值:" + redisVal + "与实际传的值不一样,实际值是:{}\n",
								Configurations.getStringProperty("maatTestLogPath", ""), true);
						return false;
					}
				}
			}
		}
		return bool;
	}

	public boolean isNum(String str) {
		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
