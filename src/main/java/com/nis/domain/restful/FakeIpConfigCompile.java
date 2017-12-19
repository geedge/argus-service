package com.nis.domain.restful;

import java.util.Date;

import com.nis.domain.BaseEntity;

public class FakeIpConfigCompile extends BaseEntity<FakeIpConfigCompile>{
	
    /**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -4840694519393346179L;

	private Long compileId;

    private Integer service;

    private Integer action;

    private Integer doBlacklist;

    private Integer doLog;

    private String effectiveRange;

    private String userRegion;

    private Integer isValid;

    private Date opTime;

    private Integer groupNum;
    private Integer activeSys;
    private Date lastUpdate;
	private Long procSeq;
    
	/**
	 * activeSys
	 * @return  activeSys
	*/
	
	public Integer getActiveSys() {
		return activeSys;
	}

	/**
	 * @param activeSys the activeSys to set
	 */
	public void setActiveSys(Integer activeSys) {
		this.activeSys = activeSys;
	}

	/**
	 * lastUpdate
	 * @return  lastUpdate
	*/
	
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * procSeq
	 * @return  procSeq
	*/
	
	public Long getProcSeq() {
		return procSeq;
	}

	/**
	 * @param procSeq the procSeq to set
	 */
	public void setProcSeq(Long procSeq) {
		this.procSeq = procSeq;
	}

	/**
	 * 创建一个新的实例 FakeIpConfigCompile.
	 *
	 */
	public FakeIpConfigCompile() {
		this.service=0;
		this.doBlacklist=0;
		this.doLog=0;
		this.effectiveRange="0";
		this.userRegion="0";
		this.isValid=1;
		this.opTime=new Date();
		this.groupNum=1;
	}

	public Long getCompileId() {
        return compileId;
    }

    public void setCompileId(Long compileId) {
        this.compileId = compileId;
    }

    public Integer getService() {
        return service;
    }

    public void setService(Integer service) {
        this.service = service;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getDoBlacklist() {
        return doBlacklist;
    }

    public void setDoBlacklist(Integer doBlacklist) {
        this.doBlacklist = doBlacklist;
    }

    public Integer getDoLog() {
        return doLog;
    }

    public void setDoLog(Integer doLog) {
        this.doLog = doLog;
    }

    public String getEffectiveRange() {
        return effectiveRange;
    }

    public void setEffectiveRange(String effectiveRange) {
        this.effectiveRange = effectiveRange;
    }

    public String getUserRegion() {
        return userRegion;
    }

    public void setUserRegion(String userRegion) {
        this.userRegion = userRegion == null ? null : userRegion.trim();
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }
}