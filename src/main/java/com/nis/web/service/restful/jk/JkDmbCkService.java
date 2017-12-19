/**
 *@Title: DmbCkService.java
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

import com.nis.domain.restful.jk.JkDmbCk;
import com.nis.web.dao.jk.JkDmbCkDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: DmbCkService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class JkDmbCkService extends CrudService<JkDmbCkDao, JkDmbCk> {
	@Autowired
	public JkDmbCkDao jkDmbCkDao;
	
	public JkDmbCk findById(long id) {
		return get(id);
	}
	public void saveDmbCkBatch(List<JkDmbCk> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,JkDmbCkDao.class);
	}
	public void updateDmbCkBatch(List<JkDmbCk> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, JkDmbCkDao.class);
	}
	
	public void removeDmbCk(long id) {
		jkDmbCkDao.delete(id);
	}
	public void removeDmbCkBatch(List<JkDmbCk> entity) {
		super.deleteBatch(entity, JkDmbCkDao.class);
	}
}
