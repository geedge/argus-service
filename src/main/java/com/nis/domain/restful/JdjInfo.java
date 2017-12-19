package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 
 * @ClassName: JdjInfo.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月9日 上午9:17:52
 * @version V1.0
 */
public class JdjInfo extends BaseEntity<JdjInfo>{
    /**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 1972462456394453087L;
	@ApiModelProperty(value="配置ID", required=true)
	private Long id;
	@ApiModelProperty(value="节点机名称", required=true)
    private String name;
	@ApiModelProperty(value="服务器ID", required=true)
    private Long fwqId;
	@ApiModelProperty(value="监测IP", required=true)
    private String jcIp;
	@ApiModelProperty(value="封堵IP", required=true)
    private String fdIp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getFwqId() {
        return fwqId;
    }

    public void setFwqId(Long fwqId) {
        this.fwqId = fwqId;
    }

    public String getJcIp() {
        return jcIp;
    }

    public void setJcIp(String jcIp) {
        this.jcIp = jcIp == null ? null : jcIp.trim();
    }

    public String getFdIp() {
        return fdIp;
    }

    public void setFdIp(String fdIp) {
        this.fdIp = fdIp == null ? null : fdIp.trim();
    }

    public Long getCzyid() {
        return czyid;
    }

    public void setCzyid(Long czyid) {
        this.czyid = czyid;
    }
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JsonSerialize(using=JsonDateSerializer.class)
    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }
}