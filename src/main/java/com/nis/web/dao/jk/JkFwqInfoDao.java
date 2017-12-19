package com.nis.web.dao.jk;

import com.nis.domain.restful.jk.JkFwqInfo;
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
public interface JkFwqInfoDao extends CrudDao<JkFwqInfo>{

    int insert(JkFwqInfo record);

    int update(JkFwqInfo record);
	
}