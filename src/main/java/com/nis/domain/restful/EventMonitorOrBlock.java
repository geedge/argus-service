package com.nis.domain.restful;

import java.util.Date;
import java.util.List;

import com.nis.domain.Page;
import org.apache.commons.httpclient.util.DateUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.formula.functions.T;

import javax.xml.bind.annotation.XmlTransient;

/**
 * @author GouGe
 * @data 2018/12/24 17:58
 */
public class EventMonitorOrBlock {
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
    private String taskIds;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "ip数量")
    private long ipNum;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "http数量")
    private long httpNum;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "邮件数量")
    private long mailNum;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "视频次数")
    private long vedioNum;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "代理次数")
    private long agentNum;

    private String searchReportStartTime;
    private String searchReportEndTime;
    private String searchTaskStartTime;
    private String searchTaskEndTime;
    private int reportType;
    private List<Long> taskIdList;
    protected Page<T> page;

    @JsonIgnore
    public List<Long> getTaskIdList() {
        return taskIdList;
    }

    public void setTaskIdList(List<Long> taskIdList) {
        this.taskIdList = taskIdList;
    }

    @JsonIgnore
    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

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

    public long getIpNum() {
        return ipNum;
    }

    public void setIpNum(long ipNum) {
        this.ipNum = ipNum;
    }

    public long getHttpNum() {
        return httpNum;
    }

    public void setHttpNum(long httpNum) {
        this.httpNum = httpNum;
    }

    public long getMailNum() {
        return mailNum;
    }

    public void setMailNum(long mailNum) {
        this.mailNum = mailNum;
    }

    public long getVedioNum() {
        return vedioNum;
    }

    public void setVedioNum(long vedioNum) {
        this.vedioNum = vedioNum;
    }

    public long getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(long agentNum) {
        this.agentNum = agentNum;
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

    public String getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String taskIds) {
        this.taskIds = taskIds;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    @JsonIgnore
    @XmlTransient
    public Page<T> getPage() {
        if (page == null) {
            page = new Page<T>();
        }
        return page;
    }

    public Page<T> setPage(Page<T> page) {
        this.page = page;
        return page;
    }

    @Override
    public String toString() {
        return "EventMonitorOrBlock{" +
                "taskTime=" + taskTime +
                ", reportTime=" + reportTime +
                ", taskName='" + taskName + '\'' +
                ", taskIds='" + taskIds + '\'' +
                ", ipNum=" + ipNum +
                ", httpNum=" + httpNum +
                ", mailNum=" + mailNum +
                ", vedioNum=" + vedioNum +
                ", agentNum=" + agentNum +
                ", reportType=" + reportType +
                ", taskIdList=" + taskIdList +
                '}';
    }
}
