/**   
* @Title: StatLogEntity.java 
* @Package com.nis.domain 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月13日 上午10:49:01 
* @version V1.0   
*/
package com.nis.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.nis.util.Configurations;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: StatLogEntity 
* @Description: TODO(日志报表公共实体部分) 
* @author (ddm)  
* @date 2016年9月1日 上午10:16:54 
* @version V1.0 
*/
public abstract class StatLogEntity<T> implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -5210683756718456730L;
	
	@ApiModelProperty(value="私有标签", required=true)
	protected Integer service;		
	@ApiModelProperty(value="生效系统", required=true)
	protected Integer activeSys;	
	@ApiModelProperty(value="A版总计", required=true)
	protected Long asum;		
	@ApiModelProperty(value="B版总计", required=true)
	protected Long bsum;	
	@ApiModelProperty(value="全A+单B数量", required=true)
	protected Long absum;	
	@ApiModelProperty(value="统计时间", required=true)
	protected Date statTime;
	
	protected String searchStatStartTime;
	protected String searchStatEndTime;
	protected String searchService;
	protected String searchStatActiveSys;
	
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getStatTime() {
		return statTime;
	}
	public void setStatTime(Date statTime) {
		this.statTime = statTime;
	}
	
	public Integer getActiveSys() {
		return activeSys;
	}
	public void setActiveSys(Integer activeSys) {
		this.activeSys = activeSys;
	}

	public Integer getService() {
		return service;
	}


	public void setService(Integer service) {
		this.service = service;
	}


	public Long getAbsum() {
		return absum;
	}
	public Long getBsum() {
		return bsum;
	}
	public void setAbsum(Long absum) {
		this.absum = absum;
	}
	public void setBsum(Long bsum) {
		this.bsum = bsum;
	}
	public Long getAsum() {
		return asum;
	}
	public void setAsum(Long asum) {
		this.asum = asum;
	}


	@JsonIgnore
	public String getSearchStatStartTime() {
		return searchStatStartTime;
	}


	public void setSearchStatStartTime(String searchStatStartTime) {
		this.searchStatStartTime = searchStatStartTime;
	}

	@JsonIgnore
	public String getSearchStatEndTime() {
		return searchStatEndTime;
	}


	public void setSearchStatEndTime(String searchStatEndTime) {
		this.searchStatEndTime = searchStatEndTime;
	}

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}


	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}
	
	@JsonIgnore
	public String getSearchStatActiveSys() {
		return searchStatActiveSys;
	}
	public void setSearchStatActiveSys(String searchStatActiveSys) {
		this.searchStatActiveSys = searchStatActiveSys;
	}

	/**
	 * 当前实体分页对象
	 */
	protected Page<T> page;
	
	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	/**  
	* @Title:  
	* @Description: TODO  
	* @param     入参   
	*/
	public StatLogEntity() {
		super();
	}


	@JsonIgnore
	@XmlTransient
	public Page<T> getPage() {
		if (page == null){
			page = new Page<T>();
		}
		return page;
	}
	
	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	/**
	 * 获取数据库名称
	 */
	@JsonIgnore
	public String getDbName(){
		return Configurations.getStringProperty("jdbc.type", "mysql");
	}
	

	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
	 
}
