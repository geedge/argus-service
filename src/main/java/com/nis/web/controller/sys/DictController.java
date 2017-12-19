package com.nis.web.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryName;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {
	

	@ModelAttribute
	public SysDataDictionaryName get(@RequestParam(required=false) Integer id) {
		if (!StringUtil.isEmpty(id)){
			return dictService.getDictByIdWithRelation(id);
		}else{
			return new SysDataDictionaryName();
		}
		
		
	}
	
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysDataDictionaryName sysDictName,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<SysDataDictionaryName> page = dictService.findDictList(new Page<SysDataDictionaryName>(request, response), sysDictName);
        model.addAttribute("page", page);
       
		return "/sys/dictList";
	}
	

	
	/**
	 * 进入字典添加或修改页面
	 * @param sysDictName
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"form"})
	public String form(SysDataDictionaryName sysDictName, Model model) {
		
		return "/sys/dictForm";
	}
	
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(SysDataDictionaryName sysDictName,Model model, RedirectAttributes redirectAttributes) {
		
		try {
			dictService.saveOrUpdate(sysDictName);
			addMessage(redirectAttributes, "保存字典成功");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "保存字典失败！");
		}
		
		return "redirect:" + adminPath + "/sys/dict/list";
	}
	
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	public String delete(Integer id,Model model, RedirectAttributes redirectAttributes) {
		
		try {
			dictService.deleteDict(id);
			addMessage(redirectAttributes, "删除字典成功");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "删除字典失败！");
		}
		
		
		return "redirect:" + adminPath + "/sys/dict/list";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "loadDataDict")
	public SysDataDictionaryName loadDataDict(@RequestParam(required=false) Integer id,HttpServletRequest request, HttpServletResponse response, Model model){
		
		return dictService.getDictByIdWithRelation(id);
	}

	
}
