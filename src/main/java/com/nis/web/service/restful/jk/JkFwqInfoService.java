/**
 *@Title: JkFfjInfoService.java
 *@Package com.nis.web.service.restful 
 *@Description TODO
 *@author (zbc)
 *@date 2016年10月19日 下午20:04:16
 *@version  版本号
 */
package com.nis.web.service.restful.jk;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.restful.jk.JkFwqInfo;
import com.nis.web.dao.jk.JkFwqInfoDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: JkFfjInfoService.java 
 * @Description: TODO
 * @author (zbc)
 * @date 2016年10月19日 下午20:04:16
 * @version V1.0
 */
@Service
public class JkFwqInfoService extends CrudService<JkFwqInfoDao, JkFwqInfo> {

	@Autowired
	public JkFwqInfoDao jkFwqInfoDao;
	
	public void savejkFwqInfoBatch(List<JkFwqInfo> entity) {
		super.saveBatch(entity, JkFwqInfoDao.class);
	}
	

	public void updatejkFwqInfoBatch(List<JkFwqInfo> entity) {
		super.updateBatch(entity, JkFwqInfoDao.class);
	}

}
