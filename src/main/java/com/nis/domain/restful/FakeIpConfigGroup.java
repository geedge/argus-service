package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.BaseEntity;

public class FakeIpConfigGroup extends BaseEntity<FakeIpConfigGroup>{
    /**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 2903914645692214990L;

	private Integer groupId;

    private Long compileId;

    private Integer isValid;

    private Date opTime;
    private Date lastUpdate;
	private Long procSeq;
    private Long id;
    
	/**
	 * id
	 * @return  id
	*/
	
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * 创建一个新的实例 FakeIpConfigGroup.
	 *
	 */
	public FakeIpConfigGroup() {
		this.isValid=1;
		this.opTime=new Date();
	}

	public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Long getCompileId() {
        return compileId;
    }

    public void setCompileId(Long compileId) {
        this.compileId = compileId;
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
}