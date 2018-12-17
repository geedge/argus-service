package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.poi.ss.formula.functions.T;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.nis.domain.Page;

public class NtcIpRangeReport<T>  implements Serializable {

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
	private String searchType;
	private String searchCountry;

	
	
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


}
