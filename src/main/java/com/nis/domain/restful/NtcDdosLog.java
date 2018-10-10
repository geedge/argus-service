package com.nis.domain.restful;

import java.util.Date;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:NtcDdosLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月5日 下午6:01:18
 * @version V1.0
 */
public class NtcDdosLog  extends LogEntity<NtcDdosLog> {

	private static final long serialVersionUID = -2918327495094410549L;
	@ApiModelProperty(value = "攻击类型", required = true)
    protected Integer attackType;
    @ApiModelProperty(value = "攻击起始时间", required = true)
    protected Date attackStartTime;
    @ApiModelProperty(value = "最后一次攻击包时间", required = true)
    protected Date lastAttackTime;
    @ApiModelProperty(value = "攻击最大流量, 包数/秒", required = true)
    protected String attackMaxPps;
    @ApiModelProperty(value = "攻击最大流量, bit数/秒", required = true)
    protected String attackMaxBps;
    @ApiModelProperty(value = "攻击累积包数", required = true)
    protected String attackTotalPkt;
    @ApiModelProperty(value = "攻击累积字节数", required = true)
    protected String attackTotalByte;
    @ApiModelProperty(value = "攻击流量是否被丢弃", required = true)
    protected Integer isBlcok;
    
    protected String searchIsBlcok; //攻击流量是否被丢弃
	public Integer getAttackType() {
		return attackType;
	}
	public void setAttackType(Integer attackType) {
		this.attackType = attackType;
	}
	public Date getAttackStartTime() {
		return attackStartTime;
	}
	public void setAttackStartTime(Date attackStartTime) {
		this.attackStartTime = attackStartTime;
	}
	public Date getLastAttackTime() {
		return lastAttackTime;
	}
	public void setLastAttackTime(Date lastAttackTime) {
		this.lastAttackTime = lastAttackTime;
	}
	public String getAttackMaxPps() {
		return attackMaxPps;
	}
	public void setAttackMaxPps(String attackMaxPps) {
		this.attackMaxPps = attackMaxPps;
	}
	public String getAttackMaxBps() {
		return attackMaxBps;
	}
	public void setAttackMaxBps(String attackMaxBps) {
		this.attackMaxBps = attackMaxBps;
	}
	public String getAttackTotalPkt() {
		return attackTotalPkt;
	}
	public void setAttackTotalPkt(String attackTotalPkt) {
		this.attackTotalPkt = attackTotalPkt;
	}
	public String getAttackTotalByte() {
		return attackTotalByte;
	}
	public void setAttackTotalByte(String attackTotalByte) {
		this.attackTotalByte = attackTotalByte;
	}
	public Integer getIsBlcok() {
		return isBlcok;
	}
	public void setIsBlcok(Integer isBlcok) {
		this.isBlcok = isBlcok;
	}    

	public String getSearchIsBlcok() {
		return searchIsBlcok;
	}
	
	public void setSearchIsBlcok(String searchIsBlcok) {
		this.searchIsBlcok = searchIsBlcok;
	}
}
