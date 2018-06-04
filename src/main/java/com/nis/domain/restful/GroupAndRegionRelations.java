package com.nis.domain.restful;

import java.io.Serializable;
/**
 * 
 * <p>Title: GroupAndRegionRelations</p>
 * <p>Description: 记录下发配置时编译id与分组,域配置之间的关系,用于更新id关系数据字典</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月25日 
 *
 */
public class GroupAndRegionRelations implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long groupId;
	private String regionId;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

}
