package com.nis.domain.restful.jk;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * JkFdZb entity.
 */

public class JkFdZb extends BaseEntity<JkFdZb>{

	/**
	 * 
	 */
	private static final Long serialVersionUID = 6166248976615344964L;
	// Fields
	@ApiModelProperty(value="全局编号", required=true)
	private Long gId;
	@ApiModelProperty(value="总表编号", required=true)
	private Long zbId;
	@ApiModelProperty(value="封堵方式", required=true)
	private Long fdFs;
	@ApiModelProperty(value="是否封堵", required=true)
	private Long sfFd;
	@ApiModelProperty(value="IP地址", required=true)
	private String jkIp;
	@ApiModelProperty(value="掩码", required=true)
	private Long jkYm;
	@ApiModelProperty(value="生效运营商", required=true)
	private String ispRaw;
	@ApiModelProperty(value="生效范围", required=true)
	private String sxFw;
	@ApiModelProperty(value="最后修改时间", required=true)
	private Date lastUpdate;
	/*@ApiModelProperty(value="IP地址类型", required=true)
	private Integer addrType;*/
//	@ApiModelProperty(value="网站名称", required=true)
//	private String jkWzMc;
//	@ApiModelProperty(value="域名", required=true)
//	private String jkDns;
//	@ApiModelProperty(value="有害分类", required=true)
//	private Long jkFl;
//	@ApiModelProperty(value="有害性质", required=true)
//	private Long jkXz;
//	@ApiModelProperty(value="网站语言", required=true)
//	private Long jkYy;
//	@ApiModelProperty(value="通知单位", required=true)
//	private String jkTzdw;
//	@ApiModelProperty(value="来文函号", required=true)
//	private String lwhh;
//	@ApiModelProperty(value="预审员", required=true)
//	private Long czyId;
//	@ApiModelProperty(value="预审时间", required=true)
//	private Date jkSj1;
//	@ApiModelProperty(value="审核员", required=true)
//	private Long jkShy;
//	@ApiModelProperty(value="审核时间", required=true)
//	private Date jkSj2;
//	@ApiModelProperty(value="审批单号", required=true)
//	private String spd;
//	@ApiModelProperty(value="控管操作员", required=true)
//	private Long kgczyid;
//	@ApiModelProperty(value="控管操作时间", required=true)
//	private Date kgczysj;
//	@ApiModelProperty(value="用户组ID", required=true)
//	private Long groupid;
//	@ApiModelProperty(value="任务", required=true)
//	private String rw;
//	@ApiModelProperty(value="备注信息", required=true)
//	private String jkBz;
//	@ApiModelProperty(value="首次封堵时间", required=true)
//	private String yl;
//	@ApiModelProperty(value="有害网址配置ID", required=true)
//	private Long yhwzId;
//	@ApiModelProperty(value="截止日期", required=true)
//	private Date yxrq;
//	@ApiModelProperty(value="登记人", required=true)
//	private Long djyid;
//	@ApiModelProperty(value="登记时间 ", required=true)
//	private Date djsj;
//	@ApiModelProperty(value="复核人", required=true)
//	private Long fhyid;
//	@ApiModelProperty(value="复核时间", required=true)
//	private Date fhsj;
//	@ApiModelProperty(value="有害网址预审员", required=true)
//	private Long yhwzysy;
//	@ApiModelProperty(value="有害网址预审时间 ", required=true)
//	private Date yhwzyssj;
//	@ApiModelProperty(value="有害网址审核员", required=true)
//	private Long yhwzshy;
//	@ApiModelProperty(value="有害网址审核时间 ", required=true)
//	private Date yhwzshsj;
//	@ApiModelProperty(value="国际出口ID", required=true)
//	private Long ipzyYsy;
//	@ApiModelProperty(value="预审时间", required=true)
//	private Date ipzyYssj;
//	@ApiModelProperty(value="审核员", required=true)
//	private Long ipzyShy;
//	@ApiModelProperty(value="审核时间", required=true)
//	private Date ipzyShsj;
//	@ApiModelProperty(value="整型IP地址", required=true)
//	private Long ipNum;
//	@ApiModelProperty(value="生效运营商范围", required=true)
//	private Long isp;
//	@ApiModelProperty(value="预留1", required=true)
//	private Long yl1;
//	@ApiModelProperty(value="预留2", required=true)
//	private Long yl2;
//	@ApiModelProperty(value="预留3", required=true)
//	private String yl3;
//	@ApiModelProperty(value="预留4", required=true)
//	private String yl4;
//	@ApiModelProperty(value="预留5", required=true)
//	private String yl5;

	public Long getGId() {
		return gId;
	}
	@JsonIgnore
	public void setGId(Long gId) {
		this.gId = gId;
	}
	// Property accessors

	public Long getZbId() {
		return this.zbId;
	}

	public void setZbId(Long zbId) {
		this.zbId = zbId;
	}

	public Long getFdFs() {
		return this.fdFs;
	}

	public void setFdFs(Long fdFs) {
		this.fdFs = fdFs;
	}

	public Long getSfFd() {
		return this.sfFd;
	}

	public void setSfFd(Long sfFd) {
		this.sfFd = sfFd;
	}

	/*public Integer getAddrType() {
		return addrType;
	}
	
	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}
	*/
	public String getJkIp() {
		return this.jkIp;
	}

	public void setJkIp(String jkIp) {
		this.jkIp = jkIp;
	}

	public Long getJkYm() {
		return this.jkYm;
	}

	public void setJkYm(Long jkYm) {
		this.jkYm = jkYm;
	}
	
	public String getSxFw() {
		return this.sxFw;
	}

	public void setSxFw(String sxFw) {
		this.sxFw = sxFw;
	}
	
	 public String getIspRaw() {
		return ispRaw;
	}
	 public void setIspRaw(String ispRaw) {
		this.ispRaw = ispRaw;
	}
	 @JsonIgnore
	 /*@JsonSerialize(using=JsonDateSerializer.class)*/ 
	public Date getLastUpdate() {
		return lastUpdate;
	}
	 public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@JsonIgnore
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}
	
}