package com.nis.domain.restful.jk;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.BaseEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * DmbCk entity. @author MyEclipse Persistence Tools
 */
@Alias("jkDmbck")
public class JkDmbCk extends BaseEntity<JkDmbCk>{

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -5491965286339219052L;
	// Fields
	private Long gId;
	@ApiModelProperty(value="国际出口ID", required=true)
	private Long gjCkId;
	@ApiModelProperty(value="是否生效", required=true)
	private Long sfSx;
//	@ApiModelProperty(value="生效范围", required=true)
	private String sxFw;
	@ApiModelProperty(value="出口名称", required=true)
	private String ckMc;
	@ApiModelProperty(value="出口位置", required=true)
	private String ckWz;
//	@ApiModelProperty(value="通讯方法", required=false)
//	private String ckTxff;
//	@ApiModelProperty(value="备注", required=false)
//	private String ckBz;
//	@ApiModelProperty(value="预留", required=false)
//	private String yl;
//	@ApiModelProperty(value="预留1", required=false)
//	private Long yl1;
//	@ApiModelProperty(value="预留2", required=false)
//	private Long yl2;
//	@ApiModelProperty(value="预留3", required=false)
//	private String yl3;
//	@ApiModelProperty(value="预留4", required=false)
//	private String yl4;
//	@ApiModelProperty(value="预留5", required=false)
//	private String yl5;
//	@ApiModelProperty(value="国际出口编号", required=true)
//	private Long ckNo;
	@ApiModelProperty(value="国际出口编号扩容", required=false)
	private String ckNoRaw;
	@ApiModelProperty(value="运营商类型", required=true)
	private Long isp;
	@ApiModelProperty(value="出口地点", required=true)
	private Long dd;

	// Constructors

	/** default constructor */
	public JkDmbCk() {
	}

	

	/* (non-Javadoc)
	 * @see com.nis.domain.BaseEntity#getId()
	 */
	@Override
	@JsonIgnore
	public Long getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	public Long getGjCkId() {
		return this.gjCkId;
	}

	public void setGjCkId(Long gjCkId) {
		this.gjCkId = gjCkId;
	}
	@JsonIgnore
	public Long getGId() {
		return this.gId;
	}

	public void setGId(Long gId) {
		this.gId = gId;
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

	public String getCkMc() {
		return this.ckMc;
	}

	public void setCkMc(String ckMc) {
		this.ckMc = ckMc;
	}

	public String getCkWz() {
		return this.ckWz;
	}

	public void setCkWz(String ckWz) {
		this.ckWz = ckWz;
	}

	public String getCkNoRaw() {
		return this.ckNoRaw;
	}

	public void setCkNoRaw(String ckNoRaw) {
		this.ckNoRaw = ckNoRaw;
	}

	public Long getIsp() {
		return this.isp;
	}

	public void setIsp(Long isp) {
		this.isp = isp;
	}

	public Long getDd() {
		return this.dd;
	}

	public void setDd(Long dd) {
		this.dd = dd;
	}

}