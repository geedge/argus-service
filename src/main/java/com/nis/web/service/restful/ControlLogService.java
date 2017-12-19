/**   
* @Title: ControlService.java 
* @Package com.nis.web.service.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月15日 下午4:08:12 
* @version V1.0   
*/
package com.nis.web.service.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.ControlLog;
import com.nis.domain.Page;
import com.nis.web.dao.ControlLogDao;
import com.nis.web.service.CrudService;

/** 
* @ClassName: ControlService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月15日 下午4:08:12 
* @version V1.0 
*/
@Service
public class ControlLogService extends CrudService<ControlLogDao, ControlLog>{
	
	@Autowired
	public ControlLogDao logDao;
	
	public Page<ControlLog> getLogInfo(Page<ControlLog> page, ControlLog log) {
		return findPage(page, log);
	}
	
	public ControlLog findById(long id) {
		return get(id);
	}
	
	public void createControlLog(ControlLog log) {
		logDao.saveControlLog(log);
	}
	
	public void deleteControlLog(long id) {
		logDao.removeControlLog(id);
	}

}
