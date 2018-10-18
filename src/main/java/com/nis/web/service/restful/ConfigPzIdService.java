/**
 * 
 */
package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.restful.ConfigPzIdSource;
import com.zdjizhi.utils.StringUtil;
import com.nis.web.dao.ConfigPzIdDao;
import com.nis.web.service.BaseLogService;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.Static;

/**
 * @ClassName:ConfigPzIdService
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2017年9月20日 上午10:43:36
 * @version V1.0
 */
@Service
public class ConfigPzIdService extends BaseLogService {

	protected final Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 持久层对象
	 */
	@Autowired
	protected ConfigPzIdDao dao;
	
	@Autowired
	ConfigRedisService configRedisService;
	
	private static Map<String, String> sourceNameMap = new HashMap<String, String>();
	static {
		sourceNameMap.put("CONFIG_COMPILE", "SEQ_COMPILEID");
		sourceNameMap.put("CONFIG_GROUP", "SEQ_GROUPID");
		sourceNameMap.put("CONFIG_REGION", "SEQ_REGIONID");
	}
	
	public ConfigPzIdSource getConfigPzIdList(ConfigPzIdSource entity){
		List<Long> pzIdList = new ArrayList<Long>();
		entity.setSourceName(entity.getSourceName().toUpperCase());
		String seqName= StringUtil.isEmpty(sourceNameMap.get(entity.getSourceName()))?"SEQ_COMPILEID":sourceNameMap.get(entity.getSourceName());
		for (int i = 0; i < entity.getNum(); i++) {
//			pzIdList.add(dao.getConfigPzIdList(entity)); //直接从数据库序列号获取
			pzIdList.add(configRedisService.getIncrId(seqName));
		}
		entity.setPzIdList(pzIdList);
		return entity;
	}
}
