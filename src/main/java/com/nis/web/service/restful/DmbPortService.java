/**
 *@Title: DmbPortService.java
 *@Package com.nis.web.service.restful 
 *@Description TODO
 *@author wx
 *@date 2016年9月7日 下午3:28:44
 *@version  版本号
 */
package com.nis.web.service.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DmbPort;
import com.nis.web.dao.DmbPortDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: DmbPortService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class DmbPortService extends CrudService<DmbPortDao, DmbPort> {
	@Autowired
	public DmbPortDao dmbPortDao;
	public Page<DmbPort> getDmbPort(Page<DmbPort> page, DmbPort entity) {
		return findPage(page, entity);
	}
	
	public DmbPort findById(long id) {
		return get(id);
	}
	public void saveDmbPortBatch(List<DmbPort> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,DmbPortDao.class);
	}
	public void updateDmbPortBatch(List<DmbPort> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, DmbPortDao.class);
	}
	public void removeDmbPort(long id) {
		dmbPortDao.delete(id);
	}
	public void removeDmbPortBatch(List<DmbPort> entity) {
		super.deleteBatch(entity, DmbPortDao.class);
	}
}
