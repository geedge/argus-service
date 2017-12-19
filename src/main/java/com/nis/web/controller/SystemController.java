package com.nis.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("${adminPath}/sys/")
public class SystemController  extends BaseController{
	
	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response,ModelMap model){
		return "/sys/sysIndex";
	}
	
	
	

}
