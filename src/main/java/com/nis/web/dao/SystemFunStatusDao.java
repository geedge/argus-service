package com.nis.web.dao;

import com.nis.domain.restful.SystemFunStatus;

@MyBatisDao
public interface SystemFunStatusDao extends CrudDao<SystemFunStatus> {
	
	int insert(SystemFunStatus record);
    int update(SystemFunStatus record);
}
