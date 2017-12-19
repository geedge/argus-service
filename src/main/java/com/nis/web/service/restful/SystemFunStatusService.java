package com.nis.web.service.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.restful.SystemFunStatus;
import com.nis.web.dao.SystemFunStatusDao;
import com.nis.web.service.CrudService;

@Service
public class SystemFunStatusService  extends CrudService<SystemFunStatusDao, SystemFunStatus> {

	@Autowired
	public SystemFunStatusDao systemFunStatusDao;
	
	public void saveSystemFunStatusBatch(List<SystemFunStatus> entity) {
		super.saveBatch(entity, SystemFunStatusDao.class);
	}

	public void updateSystemFunStatusBatch(List<SystemFunStatus> entity) {
		super.updateBatch(entity, SystemFunStatusDao.class);
	}

}
