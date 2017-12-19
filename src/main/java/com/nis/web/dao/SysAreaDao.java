package com.nis.web.dao;

import java.util.List;

import com.nis.domain.SysArea;
@MyBatisDao
public interface SysAreaDao extends TreeDao<SysArea>{

	List<SysArea> findByParentIdsLike(SysArea area);
    
}