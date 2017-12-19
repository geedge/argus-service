package com.nis.domain.restful.jk;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class JkFwqInfo extends BaseEntity<JkFwqInfo> {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 2875730011488077179L;
	
	@ApiModelProperty(value="全局编号", required=true)
	protected Long gId;
	@ApiModelProperty(value="服务器编号", required=true)
	protected Long fwqId;
	@ApiModelProperty(value="服务器名称", required=true)
	protected String fwqMc;
	@ApiModelProperty(value="服务器IP地址", required=true)
	protected String fwqIp;
	@ApiModelProperty(value="是否生效", required=true)
	protected Long sfSx;
	@ApiModelProperty(value="生效范围", required=true)
	protected String sxFw;
	
	
	public Long getgId() {
		return gId;
	}
	@JsonIgnore
	public void setgId(Long gId) {
		this.gId = gId;
	}
	public Long getFwqId() {
		return fwqId;
	}
	public void setFwqId(Long fwqId) {
		this.fwqId = fwqId;
	}
	public String getFwqMc() {
		return fwqMc;
	}
	public void setFwqMc(String fwqMc) {
		this.fwqMc = fwqMc;
	}
	public String getFwqIp() {
		return fwqIp;
	}
	public void setFwqIp(String fwqIp) {
		this.fwqIp = fwqIp;
	}
	public Long getSfSx() {
		return sfSx;
	}
	public void setSfSx(Long sfSx) {
		this.sfSx = sfSx;
	}
	@JsonIgnore
	public String getSxFw() {
		return sxFw;
	}
	public void setSxFw(String sxFw) {
		this.sxFw = sxFw;
	}

	
	@JsonIgnore
	@Override
	public Long getId() {
		return super.getId();
	}
	
}
