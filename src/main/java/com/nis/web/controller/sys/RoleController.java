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
import com.nis.domain.SysRole;
import com.nis.domain.SysUser;
import com.nis.util.Collections3;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {
	
	@ModelAttribute("sysRole")
	public SysRole get(@RequestParam(required=false) Long id) {
		if (!StringUtil.isEmpty(id)){
			return roleService.getRole(id);
		}else{
			return new SysRole();
		}
	}
	
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysRole role, Model model) {
		List<SysRole> list = roleService.findAllRole();
		model.addAttribute("list", list);
		return "/sys/roleList";
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(SysRole role, Model model) {
		
		model.addAttribute("role", role);
		model.addAttribute("menuList", menuService.findAllMenu());
		return "/sys/roleForm";
	}
	
	/**
	 * 角色分配页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(SysRole role, Model model) {
		List<SysUser> userList = userService.findUserByRoleId(role.getId());
		model.addAttribute("userList", userList);
		return "/sys/roleAssign";
	}
	
	
	/**
	 * 角色分配 -- 打开角色分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(SysRole role, Model model) {
		List<SysUser> userList = userService.findUserByRoleId(role.getId());
		model.addAttribute("role", role);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "id", ","));
		model.addAttribute("officeList", officeService.findAll());
		return "/sys/selectUserToRole";
	}
	
	
	/**
	 * 角色分配 -- 根据部门编号获取用户列表
	 * @param officeId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(Long officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SysUser> userList = userService.findUserByOfficeId(officeId);
		for (SysUser e : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			mapList.add(map);			
		}
		return mapList;
	}
	
	/**
	 * 角色分配
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assignrole")
	public String assignRole(SysRole role, String[] idsArr, RedirectAttributes redirectAttributes) {
		
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			
			SysUser user = systemService.assignUserToRole(role, userService.getUser(idsArr[i]));
			
			if (null != user) {
				msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
				newNum++;
			}
		}
		addMessage(redirectAttributes, "已成功分配 "+newNum+" 个用户"+msg);
		return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
	}
	
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(SysRole role, Model model, RedirectAttributes redirectAttributes) {
		
		if (!"true".equals(checkName(role.getOldName(), role.getName()))){
			addMessage(model, "保存角色'" + role.getName() + "'失败, 角色名已存在");
			return form(role, model);
		}
		roleService.saveOrUpdate(role);
		addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(SysRole role, RedirectAttributes redirectAttributes) {
		roleService.deleteRole(role);
		addMessage(redirectAttributes, "删除角色成功");
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	
	
	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && roleService.getRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}
	
	
	/**
	 * 角色分配 -- 从角色中移除用户
	 * @param userId
	 * @param roleId
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "outrole")
	public String outrole(Long userId, Long roleId, RedirectAttributes redirectAttributes) {
		SysRole role = roleService.getRole(roleId);
		SysUser user = userService.getUser(userId.toString());
		if (UserUtils.getUser().getId().equals(userId)) {
			addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
		}else {
			if (user.getUserRoleList().size() <= 1){
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。");
			}else{
				Boolean flag = systemService.outUserInRole(role, user);
				if (!flag) {
					addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
				}else {
					addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
				}
			}		
		}
		return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
	}
	


}
