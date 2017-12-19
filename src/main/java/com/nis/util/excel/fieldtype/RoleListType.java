package com.nis.util.excel.fieldtype;

import java.util.List;

import com.google.common.collect.Lists;
import com.nis.domain.SysRole;
import com.nis.util.Collections3;
import com.nis.util.StringUtils;
import com.nis.web.service.RoleService;
import com.nis.web.service.SpringContextHolder;

public class RoleListType {

	private static RoleService roleService = SpringContextHolder.getBean(RoleService.class);
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<SysRole> roleList = Lists.newArrayList();
		List<SysRole> allRoleList = roleService.findAllRole();
		for (String s : StringUtils.split(val, ",")){
			for (SysRole e : allRoleList){
				if (StringUtils.trimToEmpty(s).equals(e.getName())){
					roleList.add(e);
				}
			}
		}
		return roleList.size()>0?roleList:null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null){
			@SuppressWarnings("unchecked")
			List<SysRole> roleList = (List<SysRole>)val;
			return Collections3.extractToString(roleList, "name", ", ");
		}
		return "";
	}
	
}
