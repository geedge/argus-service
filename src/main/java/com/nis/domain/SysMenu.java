package com.nis.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SysMenu extends BaseEntity<SysMenu>{
	
	private static final long serialVersionUID = 8586837252075033023L;
	
	
	private SysMenu parent;	// 父级菜单
	
    private Long parentId;
    
	private String parentIds;

    private String name;

    private Integer sort;

    private String href;

    private String target;

    private String icon;

    private Integer isShow;

    private String permission;

    private SysUser createBy;	// 创建者

    private Date createDate;

    private SysUser updateBy;

    private Date updateDate;

    private String remarks;

	private Integer delFlag;

    private Integer quickAction;
    
    private String menuBg;
    
    private List<SysMenu> children = new ArrayList<SysMenu>();
    
    public SysMenu(){
		super();
		this.sort = 30;
		this.isShow = 1;
	}
	
	public SysMenu(Long id){
		super(id);
	}

    public List<SysMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}

	public Long getParentId() {
		return (parent != null && parent.getId() != null) ? parent.getId() : 0 ;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds == null ? null : parentIds.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    
    public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href == null ? null : href.trim();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target == null ? null : target.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }
    
    public String getMenuBg() {
  		return menuBg;
  	}

  	public void setMenuBg(String menuBg) {
  		this.menuBg = menuBg;
  	}

    

    public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission == null ? null : permission.trim();
    }

   

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

   

    public SysUser getCreateBy() {
		return createBy;
	}

	public void setCreateBy(SysUser createBy) {
		this.createBy = createBy;
	}

	public SysUser getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(SysUser updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    

    public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getQuickAction() {
        return quickAction;
    }

    public void setQuickAction(Integer quickAction) {
        this.quickAction = quickAction;
    }
    
    @JsonIgnore
	public static void sortList(List<SysMenu> list, List<SysMenu> sourcelist, Long parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			SysMenu menu = sourcelist.get(i);
			if (menu.getParent()!=null && menu.getParent().getId()!=null
					&& menu.getParent().getId().equals(parentId)){
				list.add(menu);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						SysMenu child = sourcelist.get(j);
						if (child.getParent()!=null && child.getParent().getId()!=null
								&& child.getParent().getId().equals(menu.getId())){
							sortList(list, sourcelist, menu.getId(), true);
							break;
						}
					}
				}
			}
		}
	}
    
    @JsonIgnore
	public static Long getRootId(){
		return 1l;
	}
    
    @JsonBackReference
	public SysMenu getParent() {
		return parent;
	}

	public void setParent(SysMenu parent) {
		this.parent = parent;
	}
	
}