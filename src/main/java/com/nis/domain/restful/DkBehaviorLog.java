package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:DkBehaviorLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月12日 下午5:09:21
 * @version V1.0
 */
public class DkBehaviorLog  extends LogEntity<DkBehaviorLog> {
	private static final long serialVersionUID = -589390375816690510L;

	@ApiModelProperty(value = "协议类型ID", required = true)
    protected Integer labelProtoId;
	@ApiModelProperty(value = "操作系统ID", required = true)
    protected Integer labelOsId;
	@ApiModelProperty(value = "浏览器ID", required = true)
    protected Integer labelBsId;
	@ApiModelProperty(value = "行为ID", required = true)
    protected Integer labelBehavId;
	@ApiModelProperty(value = "应用ID", required = true)
    protected Integer labelAppId;
	
	@ApiModelProperty(value = "c2s包数", required = true)
    protected String c2sPktNum;
    @ApiModelProperty(value = "s2c包数", required = true)
    protected String s2cPktNum;
    @ApiModelProperty(value = "c2s字节数", required = true)
    protected String c2sByteNum;
    @ApiModelProperty(value = "s2c字节数", required = true)
    protected String s2cByteNum;
	
    /**
	 * @param labelAppId the labelAppId to set
	 */
	public void setLabelAppId(Integer labelAppId) {
		this.labelAppId = labelAppId;
	}
	/**
	 * @return the labelAppId
	 */
	public Integer getLabelAppId() {
		return labelAppId;
	}
	/**
	 * @param labelBehavId the labelBehavId to set
	 */
	public void setLabelBehavId(Integer labelBehavId) {
		this.labelBehavId = labelBehavId;
	}
	/**
	 * @return the labelBehavId
	 */
	public Integer getLabelBehavId() {
		return labelBehavId;
	}
	/**
	 * @param labelBsId the labelBsId to set
	 */
	public void setLabelBsId(Integer labelBsId) {
		this.labelBsId = labelBsId;
	}
	/**
	 * @return the labelBsId
	 */
	public Integer getLabelBsId() {
		return labelBsId;
	}
	/**
	 * @param labelOsId the labelOsId to set
	 */
	public void setLabelOsId(Integer labelOsId) {
		this.labelOsId = labelOsId;
	}
	/**
	 * @return the labelOsId
	 */
	public Integer getLabelOsId() {
		return labelOsId;
	}
	/**
	 * @param labelProtoId the labelProtoId to set
	 */
	public void setLabelProtoId(Integer labelProtoId) {
		this.labelProtoId = labelProtoId;
	}
	
	/**
	 * @return the labelProtoId
	 */
	public Integer getLabelProtoId() {
		return labelProtoId;
	}
	
	public String getC2sPktNum() {
		return c2sPktNum;
	}
	public void setC2sPktNum(String c2sPktNum) {
		this.c2sPktNum = c2sPktNum;
	}
	public String getS2cPktNum() {
		return s2cPktNum;
	}
	public void setS2cPktNum(String s2cPktNum) {
		this.s2cPktNum = s2cPktNum;
	}
	public String getC2sByteNum() {
		return c2sByteNum;
	}
	public void setC2sByteNum(String c2sByteNum) {
		this.c2sByteNum = c2sByteNum;
	}
	public String getS2cByteNum() {
		return s2cByteNum;
	}
	public void setS2cByteNum(String s2cByteNum) {
		this.s2cByteNum = s2cByteNum;
	}
	
}
