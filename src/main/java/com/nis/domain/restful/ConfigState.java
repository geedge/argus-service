package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @ClassName: ConfigState
 * @Description: TODO(配置状态更新记录表)
 * @author (rkg)
 * @date 2016年9月13日下午4:39:06
 * @version V1.0
 */
public class ConfigState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6394128231495072838L;
	private String tableName;
	private Date opTime;

	public ConfigState() {
		super();
	}

	public ConfigState(String tableName, Date opTime) {
		super();
		this.tableName = tableName;
		this.opTime = opTime;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

}
