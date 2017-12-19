package com.nis.web.dao;

import java.io.Serializable;
import java.util.List;

import com.nis.domain.restful.DfDjNestLog;

@MyBatisDao
public interface DfDjNestLogDao extends CrudDao<Serializable> {

	List<DfDjNestLog> findDfDjNestLogPage(DfDjNestLog entity);

}
