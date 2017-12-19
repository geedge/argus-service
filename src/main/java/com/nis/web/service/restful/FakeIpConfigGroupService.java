/**
 *@Title: DnsFakeIpService.java
 *@Package com.nis.web.service.restful 
 *@Description TODO
 *@author wx
 *@date 2016年9月7日 下午3:28:44
 *@version  版本号
 */
package com.nis.web.service.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.restful.FakeIpConfigGroup;
import com.nis.web.dao.FakeIpConfigGroupDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: DnsFakeIpService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class FakeIpConfigGroupService extends CrudService<FakeIpConfigGroupDao, FakeIpConfigGroup> {
	@Autowired
	public FakeIpConfigGroupDao fakeIpConfigGroupDao;
	
}
