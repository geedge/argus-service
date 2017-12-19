/**   
* @Title: DjStatLogDaily.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月13日 上午11:08:11 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.StatLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DjStatLogDaily 
* @Description: 天日志报表
* @author (ddm)  
* @date 2016年9月13日 上午11:08:11 
* @version V1.0 
*/
public class DjStatLogDaily extends StatLogEntity {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -591616210162791616L;
	
	@ApiModelProperty(value="配置ID", required=true)
	protected Long configId;
	
	protected String searchConfigId;
	
	public Long getConfigId() {
		return configId;
	}
	public void setConfigId(Long configId) {
		this.configId = configId;
	}
	@JsonIgnore
	public String getSearchConfigId() {
		return searchConfigId;
	}


	public void setSearchConfigId(String searchConfigId) {
		this.searchConfigId = searchConfigId;
	}
}
