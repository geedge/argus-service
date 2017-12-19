package com.nis.web.dao;

import com.nis.domain.restful.DmbPort;
/**
 * 
 * @ClassName: DmbPortDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月9日 上午10:04:55
 * @version V1.0
 */
@MyBatisDao
public interface DmbPortDao extends CrudDao<DmbPort>{
    int delete(long id);

    int insert(DmbPort record);

    int update(DmbPort record);
}