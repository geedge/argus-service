package com.nis.web.dao;


import java.util.List;
import java.util.Map;

import com.nis.domain.SysOffice;

@MyBatisDao
public interface SysOfficeDao extends TreeDao<SysOffice> {
    int deleteByPrimaryKey(Integer id);

    int insert(SysOffice record);

    int insertSelective(SysOffice record);

    SysOffice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysOffice record);

    int updateByPrimaryKey(SysOffice record);

	List<SysOffice> selectSysOffice(SysOffice sysOffice);
	
	List<SysOffice> selectLowerDeptement(Map<String, Object> hmap);

	List<SysOffice> selectOfficeForDeptment(Map map);

	List<SysOffice> selectOfficeForLetter(Map<String, Object> hmap);
	

}