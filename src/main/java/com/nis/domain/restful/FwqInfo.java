package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 
 * @ClassName: FwqInfo.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月9日 上午9:14:52
 * @version V1.0
 */
public class FwqInfo extends BaseEntity<FwqInfo>{
    /**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7679262194839780624L;
	@ApiModelProperty(value="配置ID", required=true)
	private Long id;
	@ApiModelProperty(value="服务器名称", required=true)
    private String name;
	@ApiModelProperty(value="国际出入口编号", required=true)
    private Long entranceId;
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

    public Long getEntranceId() {
        return entranceId;
    }

    public void setEntranceId(Long entranceId) {
        this.entranceId = entranceId;
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