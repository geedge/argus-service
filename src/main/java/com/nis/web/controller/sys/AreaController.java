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
import com.nis.domain.SysArea;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;

@Controller
@RequestMapping("${adminPath}/sys/area")
public class AreaController extends BaseController {
	
	
	@ModelAttribute
	public SysArea get(@RequestParam(required=false) Long id) {
		if (!StringUtil.isEmpty(id)){
			return areaService.get(id);
		}else{
			return new SysArea();
		}
	}
	
	
	@RequiresPermissions("sys:area:view")
	@RequestMapping("index")
	public String index(SysArea area, Model model) {
		
		return "/sys/areaIndex";
	}
	
	
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list"})
	public String list(SysArea sysArea, Model model) {
		model.addAttribute("list", areaService.findAllAreaList(sysArea));
		return "/sys/areaList";
	}
	
	
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(SysArea area, Model model) {
		
		if (!StringUtil.isEmpty(area.getParent().getId())) {
			area.setParent(areaService.get(area.getParent().getId()));
		}
		model.addAttribute("area", area);
		return "/sys/areaForm";
	}
	
	
	
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(SysArea sysArea, Model model, RedirectAttributes redirectAttributes) {
		areaService.saveOrUpdate(sysArea);
		addMessage(redirectAttributes, "保存区域'" + sysArea.getName() + "'成功");
		String id = sysArea.getParentId().equals(0l) ? "" : String.valueOf(sysArea.getParentId());
		
		return "redirect:" + adminPath + "/sys/area/list?id="+id+"&parentIds="+sysArea.getParent().getParentIds();
	}
	
	
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(SysArea area, RedirectAttributes redirectAttributes) {
		areaService.delete(area);
		
		addMessage(redirectAttributes, "删除区域成功");
		
		return "redirect:" + adminPath + "/sys/area/list?id="+area.getParentId()+"&parentIds="+area.getParent().getParentIds();
	}
	
	
	
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SysArea> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			SysArea e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	
	

}
