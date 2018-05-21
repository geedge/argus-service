package com.nis.web.service.restful;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nis.datasource.DynamicJedisDataBase;
import com.nis.domain.MaatXmlConfig;
import com.nis.domain.MaatXmlExpr;
import com.nis.domain.MaatXmlSeq;
import com.nis.util.ReadMaatXmlUtil;
import com.nis.web.dao.impl.BaseRedisDao;

@Service("configRedisServiceimpl")
public class ConfigRedisServiceimpl extends BaseRedisDao<String, String> implements ConfigRedisService {
	@Transactional
	public void saveConfigYSPDemoCompile(int service, Map<String, String> map) {
		if (map != null && map.size() > 0) {
			MaatXmlConfig maatXmlConfig = ReadMaatXmlUtil.getMaatConfigByService(service);
			if (maatXmlConfig != null) {
				List<MaatXmlExpr> expressionList = maatXmlConfig.getExpressionList();
				for (MaatXmlExpr maatXmlExpr : expressionList) {
					StringBuffer keyBF = new StringBuffer();
					String[] keySplit = maatXmlExpr.getKeyExpression().split(";");
					for (String keyStr : keySplit) {
						if (!StringUtils.isEmpty(keyStr) && keyStr.trim().startsWith("[")) {
							keyStr = keyStr.trim().replace("[", "").replace("]", "");
							keyBF.append(map.get(keyStr));
						} else {
							keyBF.append(keyStr.trim());
						}
					}
					StringBuffer valBF = new StringBuffer();
					String[] valSplit = maatXmlExpr.getValueExpression().split(";");
					for (String valStr : valSplit) {
						if (!StringUtils.isEmpty(valStr) && valStr.trim().startsWith("[")) {
							valStr = valStr.trim().replace("[", "").replace("]", "");
							valBF.append(map.get(valStr));
						} else if (valStr.equals("&nbsp")) {
							valBF.append(" ");
						} else {
							valBF.append(valStr.trim());
						}
					}

					// DynamicJedisDataBase.setRedisDataBase(maatXmlExpr.getRedisDB(),
					// redisTemplate);
					// System.out.println(keyBF.toString());
					// System.out.println(valBF.toString());
					//redisTemplate.boundValueOps(keyBF.toString()).

							redisTemplate.opsForValue().set(keyBF.toString(), valBF.toString());
					// redisTemplate.boundValueOps(keyBF.toString()).set(valBF.toString());

				}
				List<MaatXmlSeq> seqList = maatXmlConfig.getSequenceList();
				for (MaatXmlSeq maatXmlSeq : seqList) {
					DynamicJedisDataBase.setRedisDataBase(maatXmlSeq.getRedisDB(), redisTemplate);
					String seqKey = maatXmlSeq.getSequenceKey();
					Integer operation = maatXmlSeq.getOperation();
					if (operation == 1) {
						redisTemplate.boundValueOps(seqKey).increment(1);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("\\t");
	}

	public void test() {

		DynamicJedisDataBase.setRedisDataBase(2, redisTemplate);
		for (int i = 0; i < 10; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 5) {
				// int a = 1 / 0;
			}
		}
		// 切换redis数据库
		DynamicJedisDataBase.setRedisDataBase(3, redisTemplate);
		for (int i = 10; i < 20; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 5) {
				// int a = 1 / 0;
			}
		}
		DynamicJedisDataBase.setRedisDataBase(4, redisTemplate);
		for (int i = 20; i < 30; i++) {
			redisTemplate.boundZSetOps("1").add("a" + i, i);
			if (i == 25) {
				int a = 1 / 0;
			}
		}
	}

	@Override
	public Long getIncrId(String key) {
		 DynamicJedisDataBase.setRedisDataBase(2, redisTemplate);
		// RedisAtomicLong atomicLong = new RedisAtomicLong(key,
		// redisTemplate.getConnectionFactory());
		//
		// Long id = atomicLong.getAndIncrement() + 1;
		// if ((id == null || id.longValue() == 0) && liveTime > 0) {
		// atomicLong.expire(liveTime, TimeUnit.SECONDS);
		// }
		// return id;

		return redisTemplate.boundValueOps(key).increment(1l);
	}

}
