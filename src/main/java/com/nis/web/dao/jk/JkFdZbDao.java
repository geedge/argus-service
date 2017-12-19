package com.nis.web.dao.jk;

import com.nis.domain.restful.jk.JkFdZb;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;
/**
 * 
 * @ClassName: JkFdZbDao.java 
 * @Description: TODO
 * @author (ddm)
 * @date 2016年10月19日 下午19:03:33
 * @version V1.0
 */
@MyBatisDao
public interface JkFdZbDao extends CrudDao<JkFdZb>{
    int delete(long id);
    int insert(JkFdZb record);
    int update(JkFdZb record);
}