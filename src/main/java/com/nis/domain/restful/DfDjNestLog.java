package com.nis.domain.restful;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.Page;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DfDjNestLog
 * @Description: 嵌套信息日志
 * @author (ZBC)
 * @date 2016年11月09日 下午02:25:00
 * @version V1.0
 */
public class DfDjNestLog  implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4112513339553998553L;
	
	
	@ApiModelProperty(value = "本层嵌套ID", required = true)
	protected String layerId;
	@ApiModelProperty(value = "发现时间", required = true)
	protected Date foundTime;
	@ApiModelProperty(value = "接收时间", required = true)
	protected Date recvTime;
	@ApiModelProperty(value = "本层嵌套层数", required = true)
	protected Integer layerCnt;
	@ApiModelProperty(value = "嵌套协议类型", required = true)
	protected String nestProtocol;
	@ApiModelProperty(value = "嵌套服务端ip地址", required = true)
	protected String nestServerIp;
	@ApiModelProperty(value = "嵌套客户端ip地址", required = true)
	protected String nestClientIp;
	@ApiModelProperty(value = "嵌套服务端端口", required = true)
	protected Integer nestServerPort;
	@ApiModelProperty(value = "嵌套客户端端口", required = true)
	protected Integer nestClientPort;
	@ApiModelProperty(value = "外层嵌套关联ID", required = true)
	protected String overId;
	
	

	protected String searchLayerId;
	protected String searchActiveSys;
	
	

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getFoundTime() {
		return foundTime;
	}
	public void setFoundTime(Date foundTime) {
		this.foundTime = foundTime;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getRecvTime() {
		return recvTime;
	}
	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}
	
	public Integer getLayerCnt() {
		return layerCnt;
	}
	public void setLayerCnt(Integer layerCnt) {
		this.layerCnt = layerCnt;
	}
	
	public String getNestProtocol() {
		return nestProtocol;
	}
	public void setNestProtocol(String nestProtocol) {
		this.nestProtocol = nestProtocol;
	}
	
	public String getNestServerIp() {
		return nestServerIp;
	}
	public void setNestServerIp(String nestServerIp) {
		this.nestServerIp = nestServerIp;
	}
	
	public String getNestClientIp() {
		return nestClientIp;
	}
	public void setNestClientIp(String nestClientIp) {
		this.nestClientIp = nestClientIp;
	}
	
	public Integer getNestServerPort() {
		return nestServerPort;
	}
	public void setNestServerPort(Integer nestServerPort) {
		this.nestServerPort = nestServerPort;
	}
	
	public Integer getNestClientPort() {
		return nestClientPort;
	}
	public void setNestClientPort(Integer nestClientPort) {
		this.nestClientPort = nestClientPort;
	}
	
	public String getOverId() {
		return overId;
	}
	public void setOverId(String overId) {
		this.overId = overId;
	}
	
	public String getLayerId() {
		return layerId;
	}
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	@JsonIgnore
	public String getSearchLayerId() {
		return searchLayerId;
	}
	public void setSearchLayerId(String searchLayerId) {
		this.searchLayerId = searchLayerId;
	}
	
	@JsonIgnore
	public String getSearchActiveSys() {
		return searchActiveSys;
	}
	public void setSearchActiveSys(String searchActiveSys) {
		this.searchActiveSys = searchActiveSys;
	}
	
	
	
	/**
	 * 当前实体分页对象
	 */
	protected Page<DfDjNestLog> page;

	@JsonIgnore
	@XmlTransient
	public Page<DfDjNestLog> getPage() {
		if (page == null) {
			page = new Page<DfDjNestLog>();
		}
		return page;
	}

	public Page<DfDjNestLog> setPage(Page<DfDjNestLog> page) {
		this.page = page;
		return page;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
