/**
 * 
 */
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName:CancleConfigSource
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年6月1日 下午6:08:09
 * @version V1.0
 */
public class UpdateStatConfigSource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 277603807201929340L;
	private List<UpdateStatConfig> UpdateStatCfgList;

	/**
	 * 
	 */
	public UpdateStatConfigSource() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cancleConfigList
	 */
	public UpdateStatConfigSource(List<UpdateStatConfig> UpdateStatCfgList) {
		super();
		this.UpdateStatCfgList = UpdateStatCfgList;
	}

	public List<UpdateStatConfig> getUpdateStatCfgList() {
		return UpdateStatCfgList;
	}

	public void setUpdateStatCfgList(List<UpdateStatConfig> updateStatCfgList) {
		UpdateStatCfgList = updateStatCfgList;
	}
}
