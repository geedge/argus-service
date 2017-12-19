package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DjJitMissionDestReport extends DfJitLogEntity<DjJitMissionDestReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4200263721978195939L;

	@ApiModelProperty(value = "任务", required = true)
	private Integer mission;

	/**
	 * 查询部分
	 */
	private String searchMission;

	public DjJitMissionDestReport() {
		super();
	}

	public Integer getMission() {
		return mission;
	}

	public void setMission(Integer mission) {
		this.mission = mission;
	}

	@JsonIgnore
	public String getSearchMission() {
		return searchMission;
	}

	public void setSearchMission(String searchMission) {
		this.searchMission = searchMission;
	}

}