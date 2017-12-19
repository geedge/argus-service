/**
 *@Title: EncryptProtoRandomService.java
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
import com.nis.domain.restful.EncryptProtoRandom;
import com.nis.web.dao.EncryptProtoRandomDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: EncryptProtoRandomService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class EncryptProtoRandomService extends CrudService<EncryptProtoRandomDao, EncryptProtoRandom> {
	@Autowired
	public EncryptProtoRandomDao encryptProtoRandomDao;
	public Page<EncryptProtoRandom> getEncryptProtoRandom(Page<EncryptProtoRandom> page, EncryptProtoRandom entity) {
		return findPage(page, entity);
	}
	
	public EncryptProtoRandom findById(long id) {
		return get(id);
	}
	public void saveEncryptProtoRandomBatch(List<EncryptProtoRandom> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,EncryptProtoRandomDao.class);
	}
	public void updateEncryptProtoRandomBatch(List<EncryptProtoRandom> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, EncryptProtoRandomDao.class);
	}
	public void removeEncryptProtoRandom(Long id) {
		encryptProtoRandomDao.delete(id);
	}
	public void removeEncryptProtoRandomBatch(List<EncryptProtoRandom> entity) {
		super.deleteBatch(entity, EncryptProtoRandomDao.class);
	}
}
