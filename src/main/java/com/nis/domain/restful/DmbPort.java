package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 
 * @ClassName: DmbPort.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月9日 上午9:23:08
 * @version V1.0
 */
public class DmbPort extends BaseEntity<DmbPort>{
    /**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -6308234862307401069L;
	@ApiModelProperty(value="端口ID", required=true)
	private Long id;
	@ApiModelProperty(value="国际出口ID", required=true)
    private Long entranceId;
	@ApiModelProperty(value="端口名称", required=true)
    private String portName;
	@ApiModelProperty(value="是否使用", required=true)
    private Integer isUsed;
	@ApiModelProperty(value="管理IP", required=true)
    private String ipDz;
	@ApiModelProperty(value="分流设备", required=true)
    private String portEquipment;
	@ApiModelProperty(value="ODF端口", required=true)
    private String odfPort;
	@ApiModelProperty(value="机房地址", required=true)
    private String addr;
	@ApiModelProperty(value="骨干设备端口", required=true)
    private String ckPort;
	@ApiModelProperty(value="操作员", required=true)
    private Long czyid;
	@ApiModelProperty(value="操作时间", required=true)
    private Date opTime;
	private Date lastUpdate;
	private Long procSeq;
	
    /**
	 * lastUpdate
	 * @return  lastUpdate
	*/
	@JsonIgnore
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * procSeq
	 * @return  procSeq
	*/
	@JsonIgnore
	public Long getProcSeq() {
		return procSeq;
	}

	/**
	 * @param procSeq the procSeq to set
	 */
	public void setProcSeq(Long procSeq) {
		this.procSeq = procSeq;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntranceId() {
        return entranceId;
    }

    public void setEntranceId(Long entranceId) {
        this.entranceId = entranceId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName == null ? null : portName.trim();
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getIpDz() {
        return ipDz;
    }

    public void setIpDz(String ipDz) {
        this.ipDz = ipDz == null ? null : ipDz.trim();
    }

    public String getPortEquipment() {
        return portEquipment;
    }

    public void setPortEquipment(String portEquipment) {
        this.portEquipment = portEquipment == null ? null : portEquipment.trim();
    }

    public String getOdfPort() {
        return odfPort;
    }

    public void setOdfPort(String odfPort) {
        this.odfPort = odfPort == null ? null : odfPort.trim();
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr == null ? null : addr.trim();
    }

    public String getCkPort() {
        return ckPort;
    }

    public void setCkPort(String ckPort) {
        this.ckPort = ckPort == null ? null : ckPort.trim();
    }
    public Long getCzyid() {
        return czyid;
    }

    public void setCzyid(Long czyid) {
        this.czyid = czyid;
    }
    @JsonSerialize(using=JsonDateSerializer.class)
    public Date getOpTime() {
        return opTime;
    }
    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }
}