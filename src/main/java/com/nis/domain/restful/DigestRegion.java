package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

public class DigestRegion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long regionId;
	private Long groupId;
	private String tableName;
	private Long rawLen;
	private String digest;
	private Integer cfdsLevel;
	private int isValid ;
	private Date opTime = new Date();
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Long getRawLen() {
		return rawLen;
	}
	public void setRawLen(Long rawLen) {
		this.rawLen = rawLen;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public Integer getCfdsLevel() {
		return cfdsLevel;
	}
	public void setCfdsLevel(Integer cfdsLevel) {
		this.cfdsLevel = cfdsLevel;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	public Date getOpTime() {
		return opTime;
	}
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	
	

	
	
	

}
