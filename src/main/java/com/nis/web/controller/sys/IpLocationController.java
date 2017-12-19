package com.nis.web.controller.sys;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nis.domain.SrcIp;
import com.nis.web.controller.BaseController;
@Controller
@RequestMapping("${adminPath}/sys/ipLocation")
public class IpLocationController extends BaseController{
	


	/**
	 * 进入首页IP定位
	 * @param office
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String index(Model model) {
		
		return "/sys/ipLocation";
	}
	
	
	/**
	 * 检索获取当前位置信息
	 * @param srcIp
	 * @param model
	 * @return
	 */
	@RequestMapping("search")
	public String search(SrcIp srcIp, Model model) {
		systemService.ipLookUp(srcIp.getSearchIp()).get(0);
		
		return "/sys/iplocationResult";
	}


}
