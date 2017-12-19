/**
 *@Title: JkFdZbService.java
 *@Package com.nis.web.service.restful 
 *@Description TODO
 *@author wx
 *@date 2016年9月7日 下午3:28:44
 *@version  版本号
 */
package com.nis.web.service.restful.jk;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.jk.JkFdZb;
import com.nis.web.dao.jk.JkFdZbDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: JkFdZbService.java 
 * @Description: TODO
 * @author (DDM)
 * @date 2016年10月19日 下午21:28:44
 * @version V1.0
 */
@Service
public class JkFdZbService extends CrudService<JkFdZbDao, JkFdZb> {
	@Autowired
	public JkFdZbDao dmbCkDao;
	public Page<JkFdZb> getJkFdZb(Page<JkFdZb> page, JkFdZb entity) {
		return findPage(page, entity);
	}
	
	public JkFdZb findById(long zbId) {
		return get(zbId);
	}
	public void saveJkFdZbBatch(List<JkFdZb> entity) {
		super.saveBatch(entity,JkFdZbDao.class);
	}
	public void updateJkFdZbBatch(List<JkFdZb> entity) {
		super.updateBatch(entity, JkFdZbDao.class);
	}
	public void removeJkFdZb(long zbId) {
		dmbCkDao.delete(zbId);
	}
	public void removeJkFdZbBatch(List<JkFdZb> entity) {
		super.deleteBatch(entity, JkFdZbDao.class);
	}
}
