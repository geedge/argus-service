package com.nis.web.service.restful;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigGroupRelation;
import com.nis.domain.restful.DigestRegion;
import com.nis.domain.restful.IpRegion;
import com.nis.domain.restful.NumRegion;
import com.nis.domain.restful.StrRegion;
import com.nis.util.CamelUnderlineUtil;
import com.nis.util.Configurations;
import com.nis.util.FileUtils;
import com.nis.util.ReadMaatXmlUtil;
import com.nis.util.ServiceAndRDBIndexReal;
import com.nis.web.service.SpringContextHolder;

@Service()
public class MaatTestServiceimpl {
	// private static Logger logger =
	// LoggerFactory.getLogger(MaatTestServiceimpl.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void getKeys(List<ConfigCompile> configCompileList) {
		if (configCompileList != null && configCompileList.size() > 0) {
			for (ConfigCompile configCompile : configCompileList) {
				Integer service = configCompile.getService();
				MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
				List<Integer> redisDBList = ServiceAndRDBIndexReal.getRedisDBByService(service);
				for (Integer redisDB : redisDBList) {
					getConfigByKey(redisDB, service, configCompile, maatXmlConfig);
				}
			}
		}

	}

	public void getConfigByKey(Integer redisDB, Integer service, ConfigCompile configCompile,
			MaatXmlConfig maatXmlConfig) {
		RedisTemplate<String, String> redisTemplate = SpringContextHolder.getBean("redisTemplate" + redisDB);
		if (maatXmlConfig != null && configCompile != null) {
			Long compileId = configCompile.getCompileId();
			validData(service, 10, compileId, configCompile.getCompileId(), redisDB, null, maatXmlConfig, redisTemplate,
					configCompile,configCompile);

			List<ConfigGroupRelation> groupRelationList = configCompile.getGroupRelationList();
			for (ConfigGroupRelation configGroupRelation : groupRelationList) {
				validData(service, 11, compileId, configGroupRelation.getGroupId(), redisDB, null, maatXmlConfig,
						redisTemplate, configGroupRelation,configCompile);
			}

			List<IpRegion> ipRegionList = configCompile.getIpRegionList();
			for (IpRegion ipRegion : ipRegionList) {
				validData(service, 12, compileId, ipRegion.getRegionId(), redisDB, null, maatXmlConfig, redisTemplate,
						ipRegion,configCompile);
			}
			List<NumRegion> numRegionList = configCompile.getNumRegionList();
			for (NumRegion numRegion : numRegionList) {
				validData(service, 13, compileId, numRegion.getRegionId(), redisDB, null, maatXmlConfig, redisTemplate,
						numRegion,configCompile);
			}

			List<StrRegion> strRegionList = configCompile.getStrRegionList();
			for (StrRegion strRegion : strRegionList) {
				if (strRegion.getDistrict() == null || strRegion.getDistrict().trim().equals("")) {
					validData(service, 14, compileId, strRegion.getRegionId(), redisDB, null, maatXmlConfig,
							redisTemplate, strRegion,configCompile);
				} else {
					validData(service, 15, compileId, strRegion.getRegionId(), redisDB, null, maatXmlConfig,
							redisTemplate, strRegion,configCompile);
				}
			}

			List<DigestRegion> digestRegionList = configCompile.getDigestRegionList();
			for (DigestRegion digestRegion : digestRegionList) {
				validData(service, 16, compileId, digestRegion.getRegionId(), redisDB, null, maatXmlConfig,
						redisTemplate, digestRegion,configCompile);
			}

			List<IpRegion> ipClientRangeList = configCompile.getIpClientRangeList();
			for (IpRegion ipRegion : ipClientRangeList) {
				validData(service, 18, compileId, ipRegion.getRegionId(), redisDB, null, maatXmlConfig, redisTemplate,
						ipRegion,configCompile);
			}

		}
	}

	public void validData(Integer service, Integer type, Long compileId, Long id, Integer redisDb, String tableName,
			MaatXmlConfig maatXmlConfig, RedisTemplate<String, String> redisTemplate, Object obj,
			ConfigCompile configCompile) {
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
				String[] valSplit = maatXmlExpr.getValueExpression().split(";");
				for (String valStr : valSplit) {
					if (!StringUtils.isEmpty(valStr) && valStr.trim().startsWith("[")) {
						valStr = valStr.trim().replace("[", "").replace("]", "");
						valList.add(valStr);
					}
				}
				String val = redisTemplate.opsForValue().get(key);
				valArr = val.split("\\t");
				break;
			}
		}
		if (sysoLog(valArr, valList, obj, redisDb, key, configCompile)) {
			FileUtils
					.addStrToFile(
							"\t" + sdf.format(new Date()) + " 编译配置id为" + compileId + "的配置中,在" + redisDb + "号redis库中key="
									+ key + "的值与实际插入的值相符\n",
							Configurations.getStringProperty("maatTestLogPath", ""), true);
		}
	}

	private boolean sysoLog(String[] valArr, List<String> valList, Object obj, Integer redisDb, String key,
			ConfigCompile configCompile) {
		boolean bool = true;
		for (int i = 0; i < valList.size(); i++) {
			String attrName = valList.get(i);
			if (!attrName.toLowerCase().equals("op_time")) {
				String filedName = CamelUnderlineUtil.underlineToCamel(attrName);
				String beanVal = null;
				if (filedName.toLowerCase().equals("service") || filedName.toLowerCase().equals("action")
						|| filedName.toLowerCase().equals("userregion")) {
					beanVal=getFieldValueByFieldName(filedName, configCompile);
				}else {
					beanVal=getFieldValueByFieldName(filedName, obj);
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

	private String getFieldValueByFieldName(String fieldName, Object obj) {
		try {
			// Field field = obj.getClass().getField(fieldName);

			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
