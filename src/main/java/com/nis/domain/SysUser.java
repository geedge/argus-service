package com.nis.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.nis.util.Collections3;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.util.excel.ExcelField;
import com.nis.util.excel.fieldtype.RoleListType;



public class SysUser extends BaseEntity<SysUser> {
	private static final long serialVersionUID = -4871709358302801032L;
	private String loginId;
	private String photo;	// 头像
	private String name;
	private String email;//电子邮箱
	private String password;
	private String newPassword;	// 新密码
	private SysRole role;
	


	private String oldLoginId;// 原登录名
	private Date createTime;
	private Integer status;
	private Integer identity;//用户身份标识 1-信访办 0-办理人员
	private List<SysRole> userRoleList = new ArrayList<SysRole>();
	private SysOffice company;//所属公司
	private SysOffice entity;//归属单位
	private SysOffice office;//用户所在部门

	

	public SysUser() {
		super();
	}

	public SysUser(Long id, String loginId) {
		super();
		this.id = id;
		this.loginId = loginId;
	}
	
	

	
	public SysUser(Long id, String loginId, String name, String email,
			String password, String oldLoginId, Date createTime, Integer status) {
		super();
		this.id = id;
		this.loginId = loginId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.oldLoginId = oldLoginId;
		this.createTime = createTime;
		this.status = status;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	
	
	public SysOffice getEntity() {
		return entity;
	}

	public void setEntity(SysOffice entity) {
		this.entity = entity;
	}

	public String getOldLoginId() {
		return oldLoginId;
	}
	public void setOldLoginId(String oldLoginId) {
		this.oldLoginId = oldLoginId;
	}
	
	@ExcelField(title="姓名", align=2, sort=40)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Integer getStatus() {
		return status;
	}
	
	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}
	
	public SysOffice getOffice() {
		return office;
	}

	public void setOffice(SysOffice office) {
		this.office = office;
	}


	
	public SysOffice getCompany() {
		return company;
	}

	public void setCompany(SysOffice company) {
		this.company = company;
	}

	public SysRole getRole() {
		return role;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
	public List<SysRole> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<SysRole> userRoleList) {
		this.userRoleList = userRoleList;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	
	public boolean isAdmin(){
		return isAdmin(this.loginId);
	}
	
	public static boolean isAdmin(String loginId){
		return loginId != null && "admin".equals(loginId);
	}
	
	
	@JsonIgnore
	public List<Long> getRoleIdList() {
		List<Long> roleIdList = Lists.newArrayList();
		for (SysRole role : userRoleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		userRoleList = Lists.newArrayList();
		for (Long roleId : roleIdList) {
			SysRole role = new SysRole();
			role.setId(roleId);
			userRoleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return Collections3.extractToString(userRoleList, "name", ",");
	}
	


	

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="创建日期", type=1, align=1, sort=110)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@ExcelField(title="登录名", align=1, sort=20)
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	

}