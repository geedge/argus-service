package com.nis.web.dao;

import com.nis.domain.restful.JdjInfo;
/**
 * @ClassName: JdjInfoDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月9日 上午10:02:33
 * @version V1.0
 */
@MyBatisDao
public interface JdjInfoDao extends CrudDao<JdjInfo>{
    int delete(long id);

    int insert(JdjInfo record);

    int update(JdjInfo record);
    
}