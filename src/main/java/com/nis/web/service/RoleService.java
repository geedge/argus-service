package com.nis.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.SysRole;
import com.nis.domain.SysUser;
import com.nis.util.StringUtil;
import com.nis.web.dao.SysRoleDao;
import com.nis.web.security.UserUtils;

@Service
public class RoleService extends BaseService {
	@Autowired
	private SysRoleDao roleDao;
	
	public List<SysRole> findAllRole() {
		return UserUtils.getRoleList();
	}

	public SysRole getRole(Long id) {
		
		return roleDao.get(id);
	}
	
	
	public SysRole getRoleByName(String name) {
		SysRole r = new SysRole();
		r.setName(name);
		return roleDao.getByName(r);
	}
	
	
	public void saveOrUpdate(SysRole role) {
		if (StringUtil.isEmpty(role.getId())){
			role.setCreateTime(new Date());
			role.setStatus(1);
			roleDao.insert(role);
			// 同步到Activiti
			//saveActivitiGroup(role);
		}else{
			roleDao.update(role);
			// 更新角色与菜单关联
			roleDao.deleteRoleMenu(role);
		}
		
		if (role.getMenuList().size() > 0){
			roleDao.insertRoleMenu(role);
		}
		
		// 同步到Activiti
		//saveActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		List<SysUser> userList = roleDao.findUserByRole(role);
		for (SysUser user : userList) {
			UserUtils.clearCache(user);
		}
		
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	public void deleteRole(SysRole role) {
		roleDao.delete(role);
		// 同步到Activiti
		//deleteActivitiGroup(role);
		// 清除用户角色缓存
		//UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		UserUtils.clearCache();
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}

	

}
