package com.nis.web.dao;

import java.util.List;

import com.nis.domain.SysRole;
import com.nis.domain.SysUser;
@MyBatisDao
public interface SysRoleDao extends CrudDao<SysRole>{

	SysRole getByName(SysRole r);

	void insertRoleMenu(SysRole role);

	void deleteRoleMenu(SysRole role);
	
	List<SysUser> findUserByRole(SysRole role);
   
}