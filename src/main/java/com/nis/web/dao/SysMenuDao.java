package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.SysMenu;

@MyBatisDao
public interface SysMenuDao extends CrudDao<SysMenu>{




	List<SysMenu> findSysMenuByUserId(@Param("userId") Long userId);

	List<SysMenu> findByParentIdsLike(SysMenu m);

	void updateParentIds(SysMenu e);

	void updateSort(SysMenu menu);
	
}