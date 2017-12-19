package com.nis.util;

import java.util.ArrayList;
import java.util.List;

import com.nis.domain.SysMenu;
import com.nis.web.security.UserUtils;

/**
 * 将树构建成上下层结构
 * @author Administrator
 *
 */
public final class TreeUtil {
	
	private List<SysMenu> menuList = new ArrayList<SysMenu>();
	
	public TreeUtil(List<SysMenu> menuList) {
		this.menuList = menuList;
	}
	
	public List<SysMenu> buildTree(){
		List<SysMenu> newMenuList = new ArrayList<SysMenu>();
		
		for (SysMenu menu : menuList) {
			if (menu.getParent().getId().equals(1l)) {
				if (isBusinessOfAdmin(menu.getName())) {
					continue;
				}
				build(menu);
				newMenuList.add(menu);
			}
		}
		
		return newMenuList;
	}
	
	/**
	 * 过滤admin的业务功能
	 * @return
	 */
	private boolean isBusinessOfAdmin(String menuName) {
		if (UserUtils.getUser().isAdmin() 
				&& menuName.equals(Constants.SYS_BUSINESS_MENU_NAME) ) {
			return true;
		}
		return false;
	}
	
	
	
	private void build(SysMenu rootMenu) {
		
		List<SysMenu> children = getChildren(rootMenu);
		
		if ( !StringUtil.isEmpty(children) ) {
			rootMenu.setChildren(children);
			for (SysMenu child : children) {
				if (StringUtils.isBlank(child.getHref())) { //根据url是否为空判断结束
					build(child);
				}
			}
		}
		
	}
	
	private List<SysMenu> getChildren(SysMenu rootMenu){
		
		List<SysMenu> children = new ArrayList<SysMenu>();
		
		for(SysMenu child : menuList) {
			if (rootMenu.getId().equals(child.getParent().getId())) {
				children.add(child);
			}
		}
		
		return children;
		
	}
	
	

}
