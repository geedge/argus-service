package com.nis.web.dao;

import com.nis.domain.restful.DmbCk;
/**
 * 
 * @ClassName: DmbCkDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月9日 上午10:03:33
 * @version V1.0
 */
@MyBatisDao
public interface DmbCkDao extends CrudDao<DmbCk>{
    int delete(long id);
    int insert(DmbCk record);
    int update(DmbCk record);
}