package com.nis.web.dao.jk;

import com.nis.domain.restful.jk.JkDmbCk;
import com.nis.domain.restful.jk.JkLyq;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;
/**
 * 
 * @ClassName: DmbCkDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月9日 上午10:03:33
 * @version V1.0
 */
@MyBatisDao
public interface JkLyqDao extends CrudDao<JkLyq>{
    int delete(long id);
    int insert(JkLyq record);
    int update(JkLyq record);
    int updateValid(JkLyq record);
}