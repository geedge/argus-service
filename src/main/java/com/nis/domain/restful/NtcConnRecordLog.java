package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName:NtcConnRecordLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author rkg
 * @date 2018年12月12日 下午2:00:16
 * @version V1.0
 */
public class NtcConnRecordLog extends LogEntity<NtcConnRecordLog> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "", required = true)
	protected Long appLabel;
	@ApiModelProperty(value = "", required = true)
	protected String c2sPktNum;
	@ApiModelProperty(value = "", required = true)
	protected String s2cPktNum;
	@ApiModelProperty(value = "", required = true)
	protected String c2sByteNum;
	@ApiModelProperty(value = "", required = true)
	protected String s2cByteNum;

	public Long getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(Long appLabel) {
		this.appLabel = appLabel;
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
