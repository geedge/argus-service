/**
 *@Title: FwqInfoService.java
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
import com.nis.domain.restful.FwqInfo;
import com.nis.web.dao.FwqInfoDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: FwqInfoService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class FwqInfoService extends CrudService<FwqInfoDao, FwqInfo> {
	@Autowired
	public FwqInfoDao fwqInfoDao;
	public Page<FwqInfo> getFwqInfo(Page<FwqInfo> page, FwqInfo entity) {
		return findPage(page, entity);
	}
	
	public FwqInfo findById(long id) {
		return get(id);
	}
	public void saveFwqInfoBatch(List<FwqInfo> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,FwqInfoDao.class);
	}
	public void updateFwqInfoBatch(List<FwqInfo> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, FwqInfoDao.class);
	}
	public void removeFwqInfo(long id) {
		fwqInfoDao.delete(id);
	}
	public void removeFwqInfoBatch(List<FwqInfo> entity) {
		super.deleteBatch(entity, FwqInfoDao.class);
	}
}
