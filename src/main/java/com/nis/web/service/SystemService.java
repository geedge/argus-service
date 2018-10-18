package com.nis.web.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.zdjizhi.utils.IPUtil;
import com.zdjizhi.utils.TimeConstants;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.SrcIp;
import com.nis.domain.SysRole;
import com.nis.domain.SysUser;
import com.nis.domain.WebServiceClient;
import com.nis.util.Configurations;
import com.nis.util.DateUtils;
import com.zdjizhi.utils.StringUtil;
import com.nis.web.dao.SrcIpDao;
import com.nis.web.dao.SysOfficeDao;
import com.nis.web.dao.UserDao;
import com.nis.web.security.Servlets;
import com.nis.web.security.SessionDAO;
import com.nis.web.security.UserUtils;

@Service
public class SystemService extends BaseService{
	
	@Autowired
	private SessionDAO sessionDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private SysOfficeDao sysOfficeDao;
	
	@Autowired
	private SrcIpDao srcIpDao;
		
	
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return  sessionDao.getActiveSessions(includeLeave);
	}
	
	
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
		return  sessionDao.getActiveSessions(includeLeave, principal, filterSession);
	}
	
	public void deleteSession(Session session) {
		sessionDao.delete(session);
	}
	
	
	public SysUser getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}
	
	public List<SrcIp> ipLookUp(String ip) {
		return srcIpDao.getIpInfo(IPUtil.getIpHostDesimal(ip));
	}
	
	
	public SysUser assignUserToRole(SysRole role, SysUser user) {
		if (user == null){
			return null;
		}
		List<Long> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getUserRoleList().clear();
		user.getUserRoleList().add(role);
		userDao.insertUserRole(user);
		UserUtils.clearCache(user);
		return user;
	}
	
	public Boolean outUserInRole(SysRole role, SysUser user) {
		List<SysRole> roles = user.getUserRoleList();
		for (SysRole e : roles){
			if (e.getId().equals(role.getId())){
				roles.remove(e);
				userDao.removeUserInRole(user.getId(),role.getId());
				return true;
			}
		}
		return false;
	}
	
	public boolean officeIsValid(Long officeId, Long companyId) {
		return userDao.officeIsExistOfCompany(officeId, companyId) >0 ?  true : false;
	}

	
	
	


	
	/**
	 * 结果信息存入文件
	 * @param result
	 * @throws IOException 
	 */
	private void saveToFile(String prefixName, String result) throws IOException{
		String flieName = prefixName + DateUtils.formatDate(new Date(), TimeConstants.YYYYMMDDHH24MMSS);
		String filePath = Servlets.getRequest().getServletContext().getRealPath(Configurations.getStringProperty("userfiles.basedir", "")) + File.separator
				+ "upload" + File.separator + flieName + ".txt";
		FileUtils.writeStringToFile(new File(filePath), result, false);		
		
	}


	
	
	
	
	
	

}
