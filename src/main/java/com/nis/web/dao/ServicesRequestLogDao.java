package com.nis.web.dao;

import java.util.List;

import com.nis.domain.ServicesRequestLog;
import com.nis.domain.restful.ServicesRequestLogBean;

/**
 * @ClassName: JdjInfoDao.java
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月9日 上午10:02:33
 * @version V1.0
 */
@MyBatisDao
public interface ServicesRequestLogDao {

	int insert(ServicesRequestLog record);
	int insertConfigLog(ServicesRequestLog record);
	int insertLogLog(ServicesRequestLog record);
	
	public void deleteById(Long id);

	public void deleteAll();

	public List<ServicesRequestLogBean> getAllLog(ServicesRequestLogBean requestLog);
}