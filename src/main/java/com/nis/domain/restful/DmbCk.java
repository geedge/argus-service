package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 
 * @ClassName: DmbCk.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月9日 上午9:22:14
 * @version V1.0
 */
public class DmbCk extends BaseEntity<DmbCk>{
    /**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -2268609209854016029L;
	@ApiModelProperty(value="国际出口ID", required=true)
	private Long id;
	@ApiModelProperty(value="出口名称", required=true)
    private String name;
	@ApiModelProperty(value="出口位置", required=true)
    private String position;
	@ApiModelProperty(value="通讯方法", required=true)
    private String comOption;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getComOption() {
        return comOption;
    }

    public void setComOption(String comOption) {
        this.comOption = comOption == null ? null : comOption.trim();
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