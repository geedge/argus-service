package com.nis.domain.restful.jk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.BaseEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * JkLyq entity. @author MyEclipse Persistence Tools
 */

public class JkLyq extends BaseEntity<JkLyq> {

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -4644455398744485045L;
	// Fields
//	@ApiModelProperty(value="全局编号", required=true)
	private Long gId;
	@ApiModelProperty(value="路由器编号", required=true)
	private Long lyqId;
	@ApiModelProperty(value="路由器名称", required=true)
	private String lyqMc;
	@ApiModelProperty(value="控管服务器编号", required=true)
	private Long fwqId;
	@ApiModelProperty(value="虚拟主机编号", required=true)
	private Long kgfs;
	@ApiModelProperty(value="国际出口编号", required=true)
	private Long gjCkId;
	@ApiModelProperty(value="是否生效", required=true)
	private Long sfSx;
	@ApiModelProperty(value="生效范围", required=true)
	private String sxFw;
//	@ApiModelProperty(value="操作员", required=true)
//	private Long czyId;
//	@ApiModelProperty(value="操作时间", required=true)
//	private Date czSj;
//	@ApiModelProperty(value="备注", required=false)
//	private String bz;
//	@ApiModelProperty(value="预留", required=false)
//	private String yl;
	@ApiModelProperty(value="监听的端口", required=false)
	private Long fwqDk;

	// Constructors

	/** default constructor */
	public JkLyq() {
	}

	// Property accessors
	/* (non-Javadoc)
	 * @see com.nis.domain.BaseEntity#getId()
	 */
	@Override
	@JsonIgnore
	public Long getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}
	public Long getLyqId() {
		return this.lyqId;
	}

	public void setLyqId(Long lyqId) {
		this.lyqId = lyqId;
	}
	@JsonIgnore
	public Long getGId() {
		return this.gId;
	}

	public void setGId(Long gId) {
		this.gId = gId;
	}

	public String getLyqMc() {
		return this.lyqMc;
	}

	public void setLyqMc(String lyqMc) {
		this.lyqMc = lyqMc;
	}

	public Long getFwqId() {
		return this.fwqId;
	}

	public void setFwqId(Long fwqId) {
		this.fwqId = fwqId;
	}

	public Long getKgfs() {
		return this.kgfs;
	}

	public void setKgfs(Long kgfs) {
		this.kgfs = kgfs;
	}

	public Long getGjCkId() {
		return this.gjCkId;
	}

	public void setGjCkId(Long gjCkId) {
		this.gjCkId = gjCkId;
	}

	public Long getSfSx() {
		return this.sfSx;
	}

	public void setSfSx(Long sfSx) {
		this.sfSx = sfSx;
	}
	@JsonIgnore
	public String getSxFw() {
		return this.sxFw;
	}

	public void setSxFw(String sxFw) {
		this.sxFw = sxFw;
	}

	public Long getFwqDk() {
		return this.fwqDk;
	}

	public void setFwqDk(Long fwqDk) {
		this.fwqDk = fwqDk;
	}

}