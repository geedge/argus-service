package com.nis.domain.restful;

import com.nis.domain.LogEntity;

public class NtcKeywordsUrlLog extends LogEntity<NtcKeywordsUrlLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String website;

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

}
