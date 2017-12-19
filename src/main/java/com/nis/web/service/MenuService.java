package com.nis.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.SysMenu;
import com.nis.domain.SysUser;
import com.nis.util.CacheUtils;
import com.nis.util.LogUtils;
import com.nis.util.StringUtil;
import com.nis.web.dao.SysMenuDao;
import com.nis.web.security.UserUtils;

@Service
public class MenuService extends BaseService {
	@Autowired
	private SysMenuDao menuDao;

	public List<SysMenu> findAllMenu() {
		return UserUtils.getMenuList();
	}

	public SysMenu getMenu(Long id) {
		
		return menuDao.get(id);
	}
	
	
	public void saveOrUpdateMenu(SysMenu menu) {
		
		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));
		
		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds(); 
		
		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");
		SysUser user = UserUtils.getUser();

		// 保存或更新实体
		if (StringUtil.isEmpty(menu.getId())){
			
			menu.setUpdateBy(user);
			menu.setCreateBy(user);
			menu.setCreateDate(new Date());
			menu.setUpdateDate(menu.getCreateDate());
			menu.setDelFlag(1);
			menuDao.insert(menu);
		}else{
			menu.setUpdateBy(user);
			menu.setUpdateDate(new Date());
			menuDao.update(menu);
		}
		
		// 更新子节点 parentIds
		SysMenu m = new SysMenu();
		m.setParentIds("%,"+menu.getId()+",%");
		List<SysMenu> list = menuDao.findByParentIdsLike(m);
		for (SysMenu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	public void deleteMenu(SysMenu menu) {
		
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
		
	}

	public void updateMenuSort(SysMenu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

}
