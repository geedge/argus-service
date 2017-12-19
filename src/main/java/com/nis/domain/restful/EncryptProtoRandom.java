package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.BaseEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class EncryptProtoRandom extends BaseEntity<EncryptProtoRandom>{
    /**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 9207461727413894109L;
	@ApiModelProperty(value="序号", required=true)
	private Long id;
	@ApiModelProperty(value="比例值", required=true)
    private Integer randomValue;
	@ApiModelProperty(value="协议编号", required=true)
    private Integer proto;
	@ApiModelProperty(value="操作时间", required=true)
    private Date opTime;
	@ApiModelProperty(value="有效标志", required=true)
    private Integer isValid;
    private Long procSeq;
    private Date lastUpdate;
    
    /* (non-Javadoc)
	 * @see com.nis.domain.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	/* (non-Javadoc)
	 * @see com.nis.domain.BaseEntity#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		super.setId(id);
	}
	
    public Integer getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(Integer randomValue) {
        this.randomValue = randomValue;
    }

    public Integer getProto() {
        return proto;
    }

    public void setProto(Integer proto) {
        this.proto = proto;
    }
    @JsonSerialize(using=JsonDateSerializer.class)
    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
    @JsonIgnore
    public Long getProcSeq() {
        return procSeq;
    }
    
    public void setProcSeq(Long procSeq) {
        this.procSeq = procSeq;
    }
    @JsonIgnore
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}