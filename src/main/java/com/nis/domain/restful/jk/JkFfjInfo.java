package com.nis.domain.restful.jk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.BaseEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: JkFfj.java 
 * @Description: TODO
 * @author (zbc)
 * @date 2016年10月19日 下午20:04:16
 * @version V1.0
 */
public class JkFfjInfo extends BaseEntity<JkFfjInfo> {
	
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -3343607423394648914L;
	
	@ApiModelProperty(value="全局编号", required=true)
	protected Long gId;
	@ApiModelProperty(value="分发机编号", required=true)
	protected Long ffjId;
	@ApiModelProperty(value="分发机名称", required=true)
	protected String ffjMc;
	@ApiModelProperty(value="分发机IP", required=true)
	protected String ffjIp;
	@ApiModelProperty(value="分发机角色", required=true)
	protected Long ffjJs;
	@ApiModelProperty(value="控管范围", required=true)
	protected Long kgFw;
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
	public Long getFfjId() {
		return ffjId;
	}
	public void setFfjId(Long ffjId) {
		this.ffjId = ffjId;
	}
	public String getFfjMc() {
		return ffjMc;
	}
	public void setFfjMc(String ffjMc) {
		this.ffjMc = ffjMc;
	}
	public String getFfjIp() {
		return ffjIp;
	}
	public void setFfjIp(String ffjIp) {
		this.ffjIp = ffjIp;
	}
	public Long getFfjJs() {
		return ffjJs;
	}
	public void setFfjJs(Long ffjJs) {
		this.ffjJs = ffjJs;
	}
	public Long getKgFw() {
		return kgFw;
	}
	public void setKgFw(Long kgFw) {
		this.kgFw = kgFw;
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
		sxFw="0";
		this.sxFw = sxFw;
	}
	
	
	@JsonIgnore
	@Override
	public Long getId() {
		return super.getId();
	}
	
}
