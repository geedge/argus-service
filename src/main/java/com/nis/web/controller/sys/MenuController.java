package com.nis.web.controller.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.SysMenu;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

@Controller
@RequestMapping("${adminPath}/sys/menu")
public class MenuController extends BaseController {
	
	@ModelAttribute("sysMenu")
	public SysMenu get(@RequestParam(required=false) Long id) {
		if (!StringUtil.isEmpty(id)){
			return menuService.getMenu(id);
		}else{
			return new SysMenu();
		}
	}
	
	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<SysMenu> list = Lists.newArrayList();
		List<SysMenu> sourcelist = menuService.findAllMenu();
		SysMenu.sortList(list, sourcelist, SysMenu.getRootId(), true);
        model.addAttribute("list", list);
		return "/sys/menuList";
	}
	
	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "form")
	public String form(SysMenu menu, Model model) {
		if (menu.getParent() == null || menu.getParent().getId() == null) {
			menu.setParent(new SysMenu(SysMenu.getRootId()));
		}
		menu.setParent(menuService.getMenu(menu.getParent().getId()));
		// 获取排序号，最末节点排序号+30
		if (StringUtil.isEmpty(menu.getId())) {
			List<SysMenu> list = Lists.newArrayList();
			List<SysMenu> sourcelist = menuService.findAllMenu();
			SysMenu.sortList(list, sourcelist, menu.getParentId(), false);
			if (list.size() > 0){
				menu.setSort(list.get(list.size()-1).getSort() + 30);
			}
		}
		model.addAttribute("menu", menu);
		return "/sys/menuForm";
	}
	
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(SysMenu menu, Model model, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isAdmin()){
			addMessage(redirectAttributes, "越权操作，只有超级管理员才能添加或修改数据！");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		
		menuService.saveOrUpdateMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/menu/";
	}
	
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "delete")
	public String delete(SysMenu menu, RedirectAttributes redirectAttributes) {
		
//		if (Menu.isRoot(id)){
//			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
//		}else{
		menuService.deleteMenu(menu);
		addMessage(redirectAttributes, "删除菜单成功");
//		}
		return "redirect:" + adminPath + "/sys/menu/";
	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(Long[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    	for (int i = 0; i < ids.length; i++) {
    		SysMenu menu = new SysMenu(ids[i]);
    		menu.setSort(sorts[i]);
    		menuService.updateMenuSort(menu);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:" + adminPath + "/sys/menu/";
	}

	
	
	
	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SysMenu> list = menuService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			SysMenu menu = list.get(i);
			if (StringUtils.isBlank(extId) || (extId !=null && !extId.equals(menu.getId()) && menu.getParentIds().indexOf(","+extId+",") == -1)) {
				if(isShowHide != null && isShowHide.equals("0") && menu.getIsShow().equals(0)){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", menu.getId());
				map.put("pId", menu.getParentId());
				map.put("name",menu.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

}
