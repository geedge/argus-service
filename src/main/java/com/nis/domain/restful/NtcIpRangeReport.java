package com.nis.domain.restful;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.Page;

public class NtcIpRangeReport<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ipStart;
	private String ipEnd;
	private Long ipStartNum;
	private Long ipEndNum;
	private String ipSub;
	private String country;
	private Integer areaType;

	private String desc;
	private String searchDesc;// 界面上的desc查询条件
	private String searchIp;// 界面上的ip查询条件
	private String searchType;// 界面上的境内,境外查询条件
	private String searchCountry;// 界面上的城市查询条件

	/**
	 * 当前实体分页对象
	 */
	protected Page<T> page;

	public String getIpStart() {
		return ipStart;
	}

	public void setIpStart(String ipStart) {
		this.ipStart = ipStart;
	}

	public String getIpEnd() {
		return ipEnd;
	}

	public void setIpEnd(String ipEnd) {
		this.ipEnd = ipEnd;
	}

	public Long getIpStartNum() {
		return ipStartNum;
	}

	public void setIpStartNum(Long ipStartNum) {
		this.ipStartNum = ipStartNum;
	}

	public Long getIpEndNum() {
		return ipEndNum;
	}

	public void setIpEndNum(Long ipEndNum) {
		this.ipEndNum = ipEndNum;
	}

	public String getIpSub() {
		return ipSub;
	}

	public void setIpSub(String ipSub) {
		this.ipSub = ipSub;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getAreaType() {
		return areaType;
	}

	public void setAreaType(Integer areaType) {
		this.areaType = areaType;
	}

	@JsonIgnore
	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	@JsonIgnore
	public String getSearchCountry() {
		return searchCountry;
	}

	public void setSearchCountry(String searchCountry) {
		this.searchCountry = searchCountry;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	@JsonIgnore
	public String getSearchDesc() {
		return searchDesc;
	}

	public void setSearchDesc(String searchDesc) {
		this.searchDesc = searchDesc;
	}

	@JsonIgnore
	public String getSearchIp() {
		return searchIp;
	}

	public void setSearchIp(String searchIp) {
		this.searchIp = searchIp;
	}

	@JsonIgnore
	@XmlTransient
	public Page<T> getPage() {
		if (page == null) {
			page = new Page<T>();
		}
		return page;
	}

	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

}
