package com.nis.web.dao;

import com.nis.domain.restful.IpLocationPublic;
import java.math.BigDecimal;

public interface IpLocationPublicDao extends CrudDao<IpLocationPublic>{
    int deleteByPrimaryKey(long id);

    int insert(IpLocationPublic record);

    int insertSelective(IpLocationPublic record);

    IpLocationPublic selectByPrimaryKey(long id);

    int updateByPrimaryKeySelective(IpLocationPublic record);

    int updateByPrimaryKey(IpLocationPublic record);
}