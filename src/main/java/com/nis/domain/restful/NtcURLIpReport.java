package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcURLIpReport extends NtcReportEntity<NtcURLIpReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "url", required = true)
	private String url;
	@ApiModelProperty(value = "唯一ip的数量", required = true)
	private Long ipCount;
	@ApiModelProperty(value = "连接数量", required = true)
	private Long connCount;

	private String searchUrl;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getIpCount() {
		return ipCount;
	}

	public void setIpCount(Long ipCount) {
		this.ipCount = ipCount;
	}

	public Long getConnCount() {
		return connCount;
	}

	public void setConnCount(Long connCount) {
		this.connCount = connCount;
	}
	@JsonIgnore
	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

}
