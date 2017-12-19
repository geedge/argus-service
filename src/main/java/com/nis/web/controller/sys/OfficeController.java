package com.nis.web.controller.sys;

import java.io.PrintWriter;
import java.util.Date;
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
import com.nis.domain.SysOffice;
import com.nis.domain.SysUser;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.controller.BaseController;
import com.nis.web.security.UserUtils;

@Controller
@RequestMapping("${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@ModelAttribute
	public SysOffice get(@RequestParam(required=false) Long id) {
		if (!StringUtil.isEmpty(id)){
			return officeService.get(id);
		}else{
			return new SysOffice();
		}
	}

	
	/**
	 * 进入机构首页
	 * @param office
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping("")
	public String index(SysOffice office,Model model) {
		
		return "/sys/officeIndex";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping("list")
	public String list(SysOffice sysOffice, Model model) {
		
		model.addAttribute("list", officeService.findAllOfficeList(sysOffice));
		return "/sys/officeList";
	}
	
	
	
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(SysOffice office, Model model) {
		SysUser user = UserUtils.getUser();
		if (office.getParent() == null || office.getParent().getId() == null){
			office.setParent(user.getOffice());
		} else {
			office.setParent(officeService.get(office.getParent().getId()));
		}
	
		if (office.getArea() == null) {
			office.setArea(user.getOffice().getArea());
		}
		// 自动获取排序号
		if (StringUtil.isEmpty(office.getId()) && office.getParent() != null) {
			int size = 0;
			List<SysOffice> list = officeService.findAll();
			for (int i=0; i<list.size(); i++){
				SysOffice e = list.get(i);
				if (e.getParent() != null && e.getParent().getId() !=null
						&& e.getParent().getId().equals(office.getParent().getId())){
					size++;
				}
			}
			office.setCode(office.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size+1 : 1), 3, "0"));
		}
		model.addAttribute("office", office);
		return "/sys/officeForm";
	}
	
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "saveOrUpdate")
	public String saveOrUpdate(SysOffice office, Model model, RedirectAttributes redirectAttributes) {
		
		officeService.saveOrUpdate(office);
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		String id = office.getParentId().equals(0l) ? "" : String.valueOf(office.getParentId());
		
		return "redirect:" + adminPath + "/sys/office/list?id="+id+"&parentIds="+office.getParentIds();
	}
	
	
	
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(SysOffice office, RedirectAttributes redirectAttributes) {
//		if (Office.isRoot(id)){
//			addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
//		}else{
			officeService.delete(office);
			addMessage(redirectAttributes, "删除机构成功");
//		}
		String id = office.getParentId().equals(0l) ? "" : String.valueOf(office.getParentId());
		
		return "redirect:" + adminPath + "/sys/office/list?id="+id+"&parentIds="+office.getParentIds();
	}

	
	

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2 ：单位；3：部门；4：信访办；5：其它；6：携带用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SysOffice> list = officeService.findList(isAll);
		for (int i=0; i<list.size(); i++){
			SysOffice e = list.get(i);
			if ((StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",") == -1))
					&& (type == null || (type != null && e.getType() <= Integer.valueOf(type)))
					&& (grade == null || (grade != null && e.getGrade() <= grade.intValue()))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if (type != null && "6".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	

}
