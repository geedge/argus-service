package com.nis.web.dao.jk;

import com.nis.domain.restful.jk.JkFfjInfo;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.MyBatisDao;
/**
 * 
 * @ClassName: JkFfjDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月9日 上午10:01:09
 * @version V1.0
 */
@MyBatisDao
public interface JkFfjInfoDao extends CrudDao<JkFfjInfo>{
	int delete(long id);
    int insert(JkFfjInfo record);
    int update(JkFfjInfo record);
	
}