/**
 *@Title: DfIpPortUdpService.java
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
import com.nis.domain.restful.DfIpPortUdp;
import com.nis.web.dao.DfIpPortUdpDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: DfIpPortUdpService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class DfIpPortUdpService extends CrudService<DfIpPortUdpDao, DfIpPortUdp> {
	@Autowired
	public DfIpPortUdpDao dfIpPortUdpDao;
	public void saveDfIpPortUdpBatch(List<DfIpPortUdp> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,DfIpPortUdpDao.class);
	}
	public void updateDfIpPortUdpBatch(List<DfIpPortUdp> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, DfIpPortUdpDao.class);
	}
	public void removeDfIpPortUdp(long id) {
		dfIpPortUdpDao.delete(id);
	}
	public void removeDfIpPortUdpBatch(List<DfIpPortUdp> entity) {
		super.deleteBatch(entity, DfIpPortUdpDao.class);
	}
}
