package com.nis.web.dao;

import com.nis.domain.restful.FwqInfo;
/**
 * 
 * @ClassName: FwqInfoDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月9日 上午10:01:09
 * @version V1.0
 */
@MyBatisDao
public interface FwqInfoDao extends CrudDao<FwqInfo>{
    int delete(long id);

    int insert(FwqInfo record);

    int update(FwqInfo record);
}