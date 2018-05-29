package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * <p>Title: CompileAndGroupRelations</p>
 * <p>Description: 记录下发配置时编译id与分组,域配置之间的关系,用于更新id关系数据字典</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月25日 
 *
 */
public class CompileAndGroupRelations implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long compileId;
	private List<GroupAndRegionRelations> groupIdList;

	public Long getCompileId() {
		return compileId;
	}

	public void setCompileId(Long compileId) {
		this.compileId = compileId;
	}

	public List<GroupAndRegionRelations> getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(List<GroupAndRegionRelations> groupIdList) {
		this.groupIdList = groupIdList;
	}

}
