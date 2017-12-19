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

import com.nis.domain.Page;
import com.nis.domain.restful.jk.JkFdZb;
import com.nis.domain.restful.jk.JkFfjInfo;
import com.nis.web.dao.jk.JkFdZbDao;
import com.nis.web.dao.jk.JkFfjInfoDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: JkFfjInfoService.java 
 * @Description: TODO
 * @author (zbc)
 * @date 2016年10月19日 下午20:04:16
 * @version V1.0
 */
@Service
public class JkFfjInfoService extends CrudService<JkFfjInfoDao, JkFfjInfo> {

	@Autowired
	public JkFfjInfoDao jkFfjInfoDao;
	
	public void saveJkFfjBatch(List<JkFfjInfo> entity) {
		super.saveBatch(entity, JkFfjInfoDao.class);
	}
	

	public void updateJkFfjBatch(List<JkFfjInfo> entity) {
		super.updateBatch(entity, JkFfjInfoDao.class);
	}
}
