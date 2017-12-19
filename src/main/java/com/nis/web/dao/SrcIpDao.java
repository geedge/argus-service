package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.SrcIp;
@MyBatisDao
public interface SrcIpDao extends CrudDao<SrcIp> {
	
	
	List<SrcIp> getIpInfo(@Param("ip") Long ipaddr);
	
	int deleteByPrimaryKey(Integer seqId);

    int insert(SrcIp record);

    int insertSelective(SrcIp record);

    SrcIp selectByPrimaryKey(Integer seqId);

    int updateByPrimaryKeySelective(SrcIp record);

    int updateByPrimaryKey(SrcIp record);
}