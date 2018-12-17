package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;

public class NtcKeywordsUrlLog extends LogEntity<NtcKeywordsUrlLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String website;
	protected String searchUrl; // url查询条件
	protected String searchWebsite; // website查询条件

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@JsonIgnore
	public String getSearchUrl() {
		return searchUrl;
	}

	/**
	 * @param searchUrl the searchUrl to set
	 */
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	@JsonIgnore
	public String getSearchWebsite() {
		return searchWebsite;
	}

	public void setSearchWebsite(String searchWebsite) {
		this.searchWebsite = searchWebsite;
	}

}
