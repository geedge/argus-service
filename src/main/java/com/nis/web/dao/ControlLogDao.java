/**   
* @Title: ControlLogDao.java 
* @Package com.nis.web.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月15日 下午4:13:49 
* @version V1.0   
*/
package com.nis.web.dao;

import java.util.List;


import com.nis.domain.ControlLog;

/** 
* @ClassName: ControlLogDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月15日 下午4:13:49 
* @version V1.0 
*/
@MyBatisDao
public interface ControlLogDao extends CrudDao<ControlLog> {
	
	List<ControlLog> getLogInfo(ControlLog controlLog);
	
	void saveControlLog(ControlLog controlLog);
	
	void removeControlLog(long id);
}
