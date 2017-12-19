package com.nis.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.nis.domain.SysRole;
import com.nis.domain.SysUser;
import com.nis.util.StringUtils;

/**
 * Service基类
 * @author ThinkGem
 * @version 2014-05-16
 */
public abstract class BaseService {
	
	
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
	/**
	 * 数据范围过滤
	 * @param user 当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param officeAlias 机构表别名，多个用“,”逗号隔开。
	 * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String dataScopeFilter(SysUser user, String officeAlias, String userAlias) {

		
		StringBuilder sqlString = new StringBuilder();
		
		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<Integer> dataScope = Lists.newArrayList();
		
		if (StringUtils.isBlank(user.getLoginId())){
			return "";
		}
		
		// 超级管理员，跳过权限过滤
		if (user.isAdmin()){
			boolean isDataScopeAll = isContainsDataScopeAll(user.getUserRoleList());
			
			
			
			for (SysRole r : user.getUserRoleList()) {
				for (String oa : StringUtils.split(officeAlias, ",")){
					if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(oa)){
						
						sqlString.append(createScopeSql(r.getDataScope(),oa,user));
						dataScope.add(r.getDataScope());
					}
				}
			}
			// 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (!isDataScopeAll){
				if (StringUtils.isNotBlank(userAlias)){
					for (String ua : StringUtils.split(userAlias, ",")){
						sqlString.append(" OR " + ua + ".id = '" + user.getId() + "'");
					}
				}else {
					for (String oa : StringUtils.split(officeAlias, ",")){
						//sqlString.append(" OR " + oa + ".id  = " + user.getOffice().getId());
						sqlString.append(" OR " + oa + ".id IS NULL");
					}
				}
			}else{
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())){
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}

	
	
	
	/**
	 * 测试数据是否包含全集
	 * @return
	 */
	private static boolean isContainsDataScopeAll(List<SysRole> roleList) {
		boolean isDataScopeAll = false;
		
		for(SysRole role : roleList) {
			if(SysRole.DATA_SCOPE_ALL.equals(role.getDataScope())){
				isDataScopeAll = true;
				break;
			}
		}
		
		return isDataScopeAll;
		
	}
	
	
	/**
	 * 过滤机构信息
	 * @param dataScope  数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；6：所在单位及以下数据；7：所在单位数据）
	 * @return
	 */
	private static String createScopeSql(int dataScope,String officeAlias,SysUser user) {
		StringBuilder scopeSql = new StringBuilder(1024);
	
		if (SysRole.DATA_SCOPE_COMPANY_AND_CHILD.equals(dataScope)){
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getCompany().getId());
			scopeSql.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getCompany().getParentIds() + user.getCompany().getId() + ",%'");
		}
		else if (SysRole.DATA_SCOPE_COMPANY.equals(dataScope)){
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getCompany().getId());
			// 包括本公司下的部门 （type=1:公司；type=2：单位 3.部门）
			scopeSql.append(" OR (" + officeAlias + ".parent_id = '" + user.getCompany().getId() + "' AND " + officeAlias + ".type>1)");
		}
		else if (SysRole.DATA_SCOPE_OFFICE_AND_CHILD.equals(dataScope)){
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getOffice().getId());
			scopeSql.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getOffice().getParentIds() + user.getOffice().getId() + ",%'");
		}
		else if (SysRole.DATA_SCOPE_OFFICE.equals(dataScope)){
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getOffice().getId());
		}
		else if (SysRole.DATA_SCOPE_ENTITY_AND_CHILD.equals(dataScope)){
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getEntity().getId());
			scopeSql.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getEntity().getParentIds() + user.getEntity().getId() + ",%'");
			
		} else if (SysRole.DATA_SCOPE_ENTITY.equals(dataScope)){
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getEntity().getId());
			// 包括本公司下的部门 （type=1:公司；type=2：单位 3.部门）
			scopeSql.append(" OR (" + officeAlias + ".parent_id = '" + user.getEntity().getId() + "' AND " + officeAlias + ".type>1)");
			
		}
		
		return scopeSql.toString();
	}
	
	
	
	
	
	
	

}
