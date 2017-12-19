package com.nis.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class ShiroTest {
	
	@Test
	public void testHelloworld() {
		init();
		Subject subject = login("zhang","123");
		System.out.println(subject.getPrincipal());
		System.out.println(subject.hasRole("role1"));
		System.out.println(subject.hasRole("role8"));
	}
	
	private void init(){
		//1. 获取SecurityManager 工厂，此处使用INI配置文件初始化SecurityManager
		Factory<SecurityManager>  factory  =  new IniSecurityManagerFactory("classpath:shiro.ini");
		//2.得道SecurityManager 实例，并绑定给SecurityUtils。
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}
	
	private Subject login(String username,String password) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		subject.login(token);
		return subject;
	}

}
