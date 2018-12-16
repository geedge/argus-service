package com.nis.domain.restful;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcURLReport extends NtcReportEntity<NtcURLReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "url", required = true)
	private String url;
	@ApiModelProperty(value = "日志数量", required = true)
	private Long urlCount;
	@ApiModelProperty(value = "ip数量列表", required = true)
	private List<NtcIpURLReport> dataList;
	
	
	private String searchUrl;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getUrlCount() {
		return urlCount;
	}

	public void setUrlCount(Long urlCount) {
		this.urlCount = urlCount;
	}

	public List<NtcIpURLReport> getDataList() {
		return dataList;
	}

	public void setDataList(List<NtcIpURLReport> dataList) {
		this.dataList = dataList;
	}
	@JsonIgnore
	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	
	

}
