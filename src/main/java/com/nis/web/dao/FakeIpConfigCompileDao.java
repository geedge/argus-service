package com.nis.web.dao;

import com.nis.domain.restful.FakeIpConfigCompile;
/**
 * 
 * @ClassName: FakeIpConfigCompileDao.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月8日 下午3:23:07
 * @version V1.0
 */
@MyBatisDao
public interface FakeIpConfigCompileDao extends CrudDao<FakeIpConfigCompile> {
    int delete(Long compileId);
    long getCompileId();
    int insert(FakeIpConfigCompile record);

    int insertSelective(FakeIpConfigCompile record);

    FakeIpConfigCompile selectByPrimaryKey(Long compileId);

    int updateByPrimaryKeySelective(FakeIpConfigCompile record);

    int updateByPrimaryKey(FakeIpConfigCompile record);
}