/**   
* @Title: LogEntity.java 
* @Package com.nis.domain 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年9月1日 上午10:16:54 
* @version V1.0   
*/
package com.nis.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: DfJitLogEntity
 * @Description: TODO(实时统计公共实体部分)
 * @author (rkg)
 * @date 2016年9月13日下午2:54:12
 * @version V1.0
 */
public abstract class DfJitLogEntity<T> implements Serializable {

	@ApiModelProperty(value = "id", required = true)
	protected Long statId;
	@ApiModelProperty(value = "统计时间", required = true)
	protected Date reportTime;
	@ApiModelProperty(value = "ip所属国家", required = false)
	protected String nation;
	@ApiModelProperty(value = "ip所属省", required = false)
	protected String province;
	@ApiModelProperty(value = "ip所属市", required = false)
	protected String city;
	@ApiModelProperty(value = "日志数量", required = true)
	protected Long sum;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;

	/**
	 * 公共查询部分
	 */
	protected String searchReportStartTime;
	protected String searchReportEndTime;
	protected String searchService;

	/**
	 * 当前实体分页对象
	 */
	protected Page<T> page;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	public DfJitLogEntity() {
		super();
	}

	public Long getStatId() {
		return statId;
	}

	public void setStatId(Long statId) {
		this.statId = statId;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	@JsonIgnore
	public String getSearchReportStartTime() {
		return searchReportStartTime;
	}

	public void setSearchReportStartTime(String searchReportStartTime) {
		this.searchReportStartTime = searchReportStartTime;
	}

	@JsonIgnore
	public String getSearchReportEndTime() {
		return searchReportEndTime;
	}

	public void setSearchReportEndTime(String searchReportEndTime) {
		this.searchReportEndTime = searchReportEndTime;
	}


	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

	@JsonIgnore
	@XmlTransient
	public Page<T> getPage() {
		if (page == null) {
			page = new Page<T>();
		}
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null) {
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

}
