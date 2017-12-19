package com.nis.web.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nis.domain.SysOffice;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.web.dao.SysOfficeDao;
import com.nis.web.security.UserUtils;
@Service
public class OfficeService extends TreeService<SysOfficeDao, SysOffice>{
	
	public List<SysOffice> findAll() {
		return UserUtils.getOfficeList();
	}
	
	public List<SysOffice> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	public SysOffice  selectByPrimaryKey(Integer id){
		if(id != null){
			
			return  dao.selectByPrimaryKey(id);
		}else{
			return null;
		}
	}
	
	public List<SysOffice> findAllOfficeList(SysOffice office){
		//office.getSqlMap().put("dsf", dataScopeFilter(UserUtils.getUser(), "a", ""));
		if (!StringUtil.isEmpty(office.getId()) && office.getId() != 1l) {
			office.setParentIds(office.getParentIds()+office.getId()+","+"%");
		} else {
			office.setParentIds(office.getParentIds()+"%");
		}
		
		return dao.findByParentIdsLike(office);
		
	}
	
   public void saveOrUpdate(SysOffice office) {
	   if (StringUtil.isEmpty(office.getId())) {
		   office.setCreateTime(new Date());
		   office.setDelFlag(1);
	   }
	   
	   this.save(office);
	   
	   
	   if(office.getChildDeptList() != null) {
			SysOffice childOffice = null;
			for(String id : office.getChildDeptList()){
				childOffice = new SysOffice();
				childOffice.setName(DictUtils.getDictLabel("SYS_OFFICE_COMMON", id, "未知"));
				childOffice.setParent(office);
				childOffice.setArea(office.getArea());
				childOffice.setType(2);
				childOffice.setGrade(office.getGrade()+1);
				childOffice.setUseable(1);
				childOffice.setCreateTime(new Date());
				childOffice.setDelFlag(1);
				this.save(childOffice);
			}
		}
	   
	   
	   UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	   
   }
   
   public void delete(SysOffice office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	public List<SysOffice> selectOfficeForDeptment(Map map) {
		 List<SysOffice> sysOfficeList=dao.selectOfficeForDeptment(map);
		return sysOfficeList;
	}
	
	public List<SysOffice> selectOfficeForLetter(Map<String, Object> hmap) {
		 List<SysOffice> sysOfficeList=dao.selectOfficeForLetter(hmap);
			return sysOfficeList;
		 
	}
	public List<SysOffice> selectLowerDeptement(Map<String, Object> hmap) {
		List<SysOffice> sysOfficeList=dao.selectLowerDeptement(hmap);
		return sysOfficeList;
		
	}

	public List<SysOffice> selectSysOffice(SysOffice sysOffice) {
		 
		return dao.selectSysOffice(sysOffice);
		 
	}



}
