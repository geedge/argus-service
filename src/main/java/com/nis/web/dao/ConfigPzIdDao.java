package com.nis.web.dao;

import java.io.Serializable;

import com.nis.domain.restful.ConfigPzIdSource;


/**
 * @ClassName:ConfigPzIdDao
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2017年9月20日 上午10:35:24
 * @version V1.0
 */
@MyBatisDao
public interface ConfigPzIdDao extends CrudDao<Serializable>{

	Long getConfigPzIdList(ConfigPzIdSource entity);
}
