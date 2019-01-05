package com.nis.domain.restful.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppTrendEntity{
	
	private String searchAppId;
	private String searchStartTime;
	private String searchEndTime;
	private String searchEntranceId;
	
	@JsonIgnore
	public String getSearchAppId() {
		return searchAppId;
	}
	public void setSearchAppId(String searchAppId) {
		this.searchAppId = searchAppId;
	}
	@JsonIgnore
	public String getSearchStartTime() {
		return searchStartTime;
	}
	public void setSearchStartTime(String searchStartTime) {
		this.searchStartTime = searchStartTime;
	}
	@JsonIgnore
	public String getSearchEndTime() {
		return searchEndTime;
	}
	public void setSearchEndTime(String searchEndTime) {
		this.searchEndTime = searchEndTime;
	}
	@JsonIgnore
	public String getSearchEntranceId() {
		return searchEntranceId;
	}
	public void setSearchEntranceId(String searchEntranceId) {
		this.searchEntranceId = searchEntranceId;
	}
}
