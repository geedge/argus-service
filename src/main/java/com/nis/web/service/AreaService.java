package com.nis.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.nis.domain.SysArea;
import com.nis.util.StringUtil;
import com.nis.web.dao.SysAreaDao;
import com.nis.web.security.UserUtils;

@Service
public class AreaService extends TreeService<SysAreaDao, SysArea> {
	@Autowired
	private SysAreaDao areaDao;

	public List<SysArea> findAll() {
		return UserUtils.getAreaList();
	}

	
	public List<SysArea> findAllAreaList(SysArea area) {
		if (!StringUtil.isEmpty(area.getId())) {
			area.setParentIds(area.getParentIds()+area.getId()+","+"%");
		} else {
			//area.setParentIds(area.getParentIds()+"%");
			return Lists.newArrayList();
		}
		
		return areaDao.findByParentIdsLike(area);
	}


	public void saveOrUpdate(SysArea sysArea) {
		if (StringUtil.isEmpty(sysArea.getId())) {
			sysArea.setDelFlag(1);
		}
		this.save(sysArea);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

}
