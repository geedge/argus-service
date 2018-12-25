package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;

import org.apache.commons.httpclient.util.DateUtil;

/**
 * @author GouGe
 * @data 2018/12/24 15:35
 */
@ApiModel(value = "重点保障事件对象", description = "用来封装重点事件保障")
public class EventKeyProtection {
    private static final long serialVersionUID = -3597060253861898787L;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "审核时间")
    private Date taskTime;
    
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "统计时间")
    private Date reportTime;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "事件名称")
    private String taskName;
    
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "事件ID")
    private long taskId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "来函件数")
    private long letterNum;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "配置总数")
    private long configNum;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "监测数量")
    private long monitorNum;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "管控数量")
    private long controlNum;

    private String searchReportStartTime;
    private String searchReportEndTime;
    private String searchTaskStartTime;
    private String searchTaskEndTime;


    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getLetterNum() {
        return letterNum;
    }

    public void setLetterNum(long letterNum) {
        this.letterNum = letterNum;
    }

    public long getConfigNum() {
        return configNum;
    }

    public void setConfigNum(long configNum) {
        this.configNum = configNum;
    }

    public long getMonitorNum() {
        return monitorNum;
    }

    public void setMonitorNum(long monitorNum) {
        this.monitorNum = monitorNum;
    }

    public long getControlNum() {
        return controlNum;
    }

    public void setControlNum(long controlNum) {
        this.controlNum = controlNum;
    }

    @JsonIgnore
    public String getSearchReportStartTime() {
        return searchReportStartTime;
    }

    public void setSearchReportStartTime(String searchReportStartTime) {
        this.searchReportStartTime = searchReportStartTime;
    }

    @JsonIgnore
    public String getSearchReportEndTime() {
        return searchReportEndTime;
    }

    public void setSearchReportEndTime(String searchReportEndTime) {
        this.searchReportEndTime = searchReportEndTime;
    }

    @JsonIgnore
    public String getSearchTaskStartTime() {
        return searchTaskStartTime;
    }

    public void setSearchTaskStartTime(String searchTaskStartTime) {
        this.searchTaskStartTime = searchTaskStartTime;
    }

    @JsonIgnore
    public String getSearchTaskEndTime() {
        return searchTaskEndTime;
    }

    public void setSearchTaskEndTime(String searchTaskEndTime) {
        this.searchTaskEndTime = searchTaskEndTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
    
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	@Override
	public String toString() {
		return "EventKeyProtection [taskTime=" + taskTime + ", reportTime=" + reportTime + ", taskName=" + taskName
				+ ", taskId=" + taskId + ", letterNum=" + letterNum + ", configNum=" + configNum + ", monitorNum="
				+ monitorNum + ", controlNum=" + controlNum + "]";
	}

}
