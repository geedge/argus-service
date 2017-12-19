package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfJitLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DjJitMissionSrcReport extends DfJitLogEntity<DjJitMissionSrcReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1244851836673765943L;

	@ApiModelProperty(value = "任务", required = true)
	private Integer mission;

	/**
	 * 查询部分
	 */
	private String searchMission;

	public DjJitMissionSrcReport() {
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