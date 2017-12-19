/**   
 * @Title: DfKeyMailAdd.java 
 * @Package com.nis.domain.restful 
 * @Description: 关键字业务转换邮件地址日志
 * @author (ZBC)
 * @date 2016年11月09日 下午02:25:00
 * @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.Page;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DfKeyMailAdd
 * @Description: 关键字业务转换邮件地址日志
 * @author (ZBC)
 * @date 2016年11月09日 下午02:25:00
 * @version V1.0
 */
public class DfKeyMailAdd implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2380470456434492094L;
	
	
	@ApiModelProperty(value = "序列号", required = true)
	protected Integer id;
	@ApiModelProperty(value = "命中关键字ID", required = true)
	protected Integer keyId;
	@ApiModelProperty(value = "邮件地址", required = true)
	protected String mailAddr;
	@ApiModelProperty(value = "操作时间", required = true)
	protected Date opTime;
	
	protected String searchId;
	protected String optStartTime;
	protected String optEndTime;
	
	@JsonIgnore
	public String getSearchId() {
		return searchId;
	}
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	@JsonIgnore
	public String getOptStartTime() {
		return optStartTime;
	}

	public void setOptStartTime(String optStartTime) {
		this.optStartTime = optStartTime;
	}

	@JsonIgnore
	public String getOptEndTime() {
		return optEndTime;
	}

	public void setOptEndTime(String optEndTime) {
		this.optEndTime = optEndTime;
	}

	/**
	 * 当前实体分页对象
	 */
	protected Page<DfKeyMailAdd> page;

	@JsonIgnore
	@XmlTransient
	public Page<DfKeyMailAdd> getPage() {
		if (page == null) {
			page = new Page<DfKeyMailAdd>();
		}
		return page;
	}

	public Page<DfKeyMailAdd> setPage(Page<DfKeyMailAdd> page) {
		this.page = page;
		return page;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getKeyId() {
		return keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}

	public String getMailAddr() {
		return mailAddr;
	}
	
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
