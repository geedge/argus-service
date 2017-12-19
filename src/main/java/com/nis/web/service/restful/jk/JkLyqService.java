/**
 *@Title: JkLyqService.java
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

import com.nis.domain.restful.jk.JkLyq;
import com.nis.web.dao.jk.JkLyqDao;
import com.nis.web.service.CrudService;
import com.nis.web.service.SaveRequestLogThread;

/**
 * @ClassName: JkLyqService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class JkLyqService extends CrudService<JkLyqDao, JkLyq> {
	@Autowired
	public JkLyqDao jkLyqDao;
	
	public JkLyq findById(long id) {
		return get(id);
	}
	public void saveJkLyqBatch(List<JkLyq> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,JkLyqDao.class);
	}
	public void updateJkLyqBatch(List<JkLyq> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, JkLyqDao.class);
	}
	public void removeJkLyq(long id) {
		jkLyqDao.delete(id);
	}
	public void removeJkLyqBatch(List<JkLyq> entity) {
		super.deleteBatch(entity, JkLyqDao.class);
	}
}
