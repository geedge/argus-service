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
public class DjTagStatLogDaily extends StatLogEntity {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -591616210162791616L;
	
	@ApiModelProperty(value="标签", required=true)
	protected Integer tag;
	
	protected String searchTag;
	
	public Integer getTag() {
		return tag;
	}
	public void setTag(Integer tag) {
		this.tag = tag;
	}
	@JsonIgnore
	public String getSearchTag() {
		return searchTag;
	}
	public void setSearchTag(String searchTag) {
		this.searchTag = searchTag;
	}
}
