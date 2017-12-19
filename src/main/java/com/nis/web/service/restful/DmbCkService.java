/**
 *@Title: DmbCkService.java
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
import com.nis.domain.restful.DmbCk;
import com.nis.web.dao.DmbCkDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: DmbCkService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class DmbCkService extends CrudService<DmbCkDao, DmbCk> {
	@Autowired
	public DmbCkDao dmbCkDao;
	public Page<DmbCk> getDmbCk(Page<DmbCk> page, DmbCk entity) {
		return findPage(page, entity);
	}
	
	public DmbCk findById(long id) {
		return get(id);
	}
	public void saveDmbCkBatch(List<DmbCk> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,DmbCkDao.class);
	}
	public void updateDmbCkBatch(List<DmbCk> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, DmbCkDao.class);
	}
	public void removeDmbCk(long id) {
		dmbCkDao.delete(id);
	}
	public void removeDmbCkBatch(List<DmbCk> entity) {
		super.deleteBatch(entity, DmbCkDao.class);
	}
}
