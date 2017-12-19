package com.nis.web.dao;

import com.nis.domain.restful.FakeIpConfigGroup;
/**
 * 
 * @ClassName: FakeIpConfigGroupDao.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月8日 下午3:25:17
 * @version V1.0
 */
@MyBatisDao
public interface FakeIpConfigGroupDao extends CrudDao<FakeIpConfigGroup>{
    int insert(FakeIpConfigGroup record);
//    int delete(long record);
    int insertSelective(FakeIpConfigGroup record);
}