/**
 * 
 */
package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.restful.ConfigPzIdSource;
import com.nis.web.dao.ConfigPzIdDao;
import com.nis.web.service.BaseLogService;

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
	
	public ConfigPzIdSource getConfigPzIdList(ConfigPzIdSource entity){
		List<Long> pzIdList = new ArrayList<Long>();
		entity.setSourceName(entity.getSourceName().toUpperCase());
		for (int i = 0; i < entity.getNum(); i++) {
			pzIdList.add(dao.getConfigPzIdList(entity));
		}
		entity.setPzIdList(pzIdList);
		return entity;
	}
}
