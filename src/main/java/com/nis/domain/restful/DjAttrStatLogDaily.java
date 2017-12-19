/**   
* @Title: DfTagStatLogDaily.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2017年01月05日 下午07:08:11 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.StatLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfTagStatLogDaily 
* @Description: 天日志报表
* @author (ddm)  
* @date 2016年9月13日 上午11:08:11 
* @version V1.0 
*/
public class DjAttrStatLogDaily extends StatLogEntity {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -591616210162791616L;
	
	@ApiModelProperty(value="性质", required=true)
	protected Integer attrType;
	
	protected String searchAttrType;
	
	public Integer getAttrType() {
		return attrType;
	}
	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}
	@JsonIgnore
	public String getSearchAttrType() {
		return searchAttrType;
	}
	public void setSearchAttrType(String searchAttrType) {
		this.searchAttrType = searchAttrType;
	}
}
