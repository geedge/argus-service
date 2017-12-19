package com.nis.web.dao;

import com.nis.domain.restful.DfIpPortUdp;
import com.nis.domain.restful.JdjInfo;
/**
 * @ClassName: JdjInfoDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月9日 上午10:02:33
 * @version V1.0
 */
@MyBatisDao
public interface DfIpPortUdpDao extends CrudDao<DfIpPortUdp>{
    int delete(long id);

    int insert(DfIpPortUdp record);

    int update(DfIpPortUdp record);
    
}