/**
 *@Title: JdjInfoService.java
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
import com.nis.domain.restful.JdjInfo;
import com.nis.web.dao.JdjInfoDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: JdjInfoService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class JdjInfoService extends CrudService<JdjInfoDao, JdjInfo> {
	@Autowired
	public JdjInfoDao jdjInfoDao;
	public Page<JdjInfo> getJdjInfo(Page<JdjInfo> page, JdjInfo entity) {
		return findPage(page, entity);
	}
	
	public JdjInfo findById(long id) {
		return get(id);
	}
	public void saveJdjInfoBatch(List<JdjInfo> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,JdjInfoDao.class);
	}
	public void updateJdjInfoBatch(List<JdjInfo> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, JdjInfoDao.class);
	}
	public void removeJdjInfo(long id) {
		jdjInfoDao.delete(id);
	}
	public void removeJdjInfoBatch(List<JdjInfo> entity) {
		super.deleteBatch(entity, JdjInfoDao.class);
	}
}
