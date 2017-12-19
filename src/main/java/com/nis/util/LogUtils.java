package com.nis.util;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.SysLog;
import com.nis.domain.SysMenu;
import com.nis.domain.SysUser;
import com.nis.web.dao.SysLogDao;
import com.nis.web.dao.SysMenuDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.SpringContextHolder;
import com.sun.corba.se.impl.orbutil.closure.Constant;


public class LogUtils {
	
	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
	
	private static SysLogDao logDao = SpringContextHolder.getBean(SysLogDao.class);
	private static SysMenuDao menuDao = SpringContextHolder.getBean(SysMenuDao.class);
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title){
		saveLog(request, null, null, title, 0);
	}
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title){
		saveLog(request, handler, ex, title, 0);
	}
	
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title, long consumerTime){
		SysUser user = UserUtils.getUser();
		if (user != null && user.getId() != null){
			SysLog log = new SysLog(); 
			log.setType(getOperateType(handler));
			log.setTitle(title);
			log.setConsumerTime(consumerTime);
			log.setState(ex == null ? Constants.LOG_ACCESS_SUCCESS : Constants.LOG_ACCESS_EXCEPTION);
			log.setRemoteAddr(StringUtils.getRemoteAddr(request));
			log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
			log.setParams(request.getParameterMap());
			log.setMethod(request.getMethod());
			log.setCreateBy(user.getName());
			log.setCreateDate(new Date());
			// 异步保存日志
			new SaveLogThread(log, handler, ex).start();
		}
	}

	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{
		
		private SysLog log;
		private Object handler;
		private Exception ex;
		
		public SaveLogThread(SysLog log, Object handler, Exception ex){
			super(SaveLogThread.class.getSimpleName());
			this.log = log;
			this.handler = handler;
			this.ex = ex;
		}
		
		@Override
		public void run() {
			// 获取日志标题
			if (StringUtils.isBlank(log.getTitle())){
				String permission = "";
				if (handler instanceof HandlerMethod){
					Method m = ((HandlerMethod)handler).getMethod();
					RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
					permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
				}
				log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
			}
			// 如果有异常，设置异常信息
			log.setException(Exceptions.getStackTraceAsString(ex));
			// 如果无标题并无异常日志，则不保存信息
			if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())){
				return;
			}
			// 保存日志信息
			logDao.insert(log);
		}
	}

	
	
	public static int getOperateType(Object handler) {
		int type = Constants.DEFAULT_METHOD_TYPE;
		
		if (!StringUtil.isEmpty(handler)) {
			if (handler instanceof HandlerMethod) {
				Method m = ((HandlerMethod)handler).getMethod();
				String methodName = m.getName();
				List<SysDataDictionaryItem> dictList = DictUtils.getDictList("SYS_LOG_TYPE");
				for(SysDataDictionaryItem sysDataDictionaryItem:dictList){
					if(methodName.toLowerCase().matches(sysDataDictionaryItem.getItemCode().toLowerCase()+".*")){
						type = Integer.parseInt(sysDataDictionaryItem.getItemValue());
						break;
					}
				}	
			}
			
		}
		
		
		return type;
	}
	
	
	
	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	public static String getMenuNamePath(String requestUri, String permission){
		String href = StringUtils.substringAfter(requestUri, Configurations.getStringProperty("adminPath", "/nis"));
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>)CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
		if (menuMap == null){
			menuMap = Maps.newHashMap();
			List<SysMenu> menuList = menuDao.findAllList(new SysMenu());
			for (SysMenu menu : menuList){
				// 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
				String namePath = "";
				if (menu.getParentIds() != null){
					List<String> namePathList = Lists.newArrayList();
					for (String id : StringUtils.split(menu.getParentIds(), ",")){
						
						
						if (SysMenu.getRootId().equals(Long.valueOf(id))){
							continue; // 过滤跟节点
						}
						for (SysMenu m : menuList){
							if (m.getId().equals(Long.valueOf(id))){
								namePathList.add(m.getName());
								break;
							}
						}
						
					}
					namePathList.add(menu.getName());
					namePath = StringUtils.join(namePathList, "-");
				}
				// 设置菜单名称路径
				if (StringUtils.isNotBlank(menu.getHref())){
					menuMap.put(menu.getHref(), namePath);
				}else if (StringUtils.isNotBlank(menu.getPermission())){
					for (String p : StringUtils.split(menu.getPermission())){
						menuMap.put(p, namePath);
					}
				}
				
			}
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
		}
		String menuNamePath = menuMap.get(href);
		if (menuNamePath == null){
			for (String p : StringUtils.split(permission)){
				menuNamePath = menuMap.get(p);
				if (StringUtils.isNotBlank(menuNamePath)){
					break;
				}
			}
			if (menuNamePath == null){
				return "";
			}
		}
		return menuNamePath;
	}

	
}
