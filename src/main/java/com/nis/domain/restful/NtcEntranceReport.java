package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class NtcEntranceReport extends NtcReportEntity<NtcEntranceReport> {

	private static final long serialVersionUID = -3973713578165502900L;

	@ApiModelProperty(value = "局点（出入口）", required = true)
	protected Integer entranceId;

	protected String searchEntrance;

	/**
	 * @return the entranceId
	 */
	public Integer getEntranceId() {
		return entranceId;
	}
	
	/**
	 * @param entranceId the entranceId to set
	 */
	public void setEntranceId(Integer entranceId) {
		this.entranceId = entranceId;
	}

	@JsonIgnore
	/**
	 * @return the searchEntrance
	 */
	public String getSearchEntrance() {
		return searchEntrance;
	}
	
	/**
	 * @param searchEntrance the searchEntrance to set
	 */
	public void setSearchEntrance(String searchEntrance) {
		this.searchEntrance = searchEntrance;
	}
}
