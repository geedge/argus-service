package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private static final Long serialVersionUID = 1L;
	@ApiModelProperty(value = "", required = true)
	protected String appLabel;
	@ApiModelProperty(value = "", required = true)
	protected String c2sPktNum;
	@ApiModelProperty(value = "", required = true)
	protected String s2cPktNum;
	@ApiModelProperty(value = "", required = true)
	protected String c2sByteNum;
	@ApiModelProperty(value = "", required = true)
	protected String s2cByteNum;

	@ApiModelProperty(value = "", required = true)
	protected Long protoId;
	@ApiModelProperty(value = "", required = true)
	protected Long appId;
	@ApiModelProperty(value = "", required = true)
	protected Long webId;

	@ApiModelProperty(value = "运营商标识,entrance_id,device_id", required = true)
	protected String ispNum;

	protected String searchAppId;// app
	protected String searchWebId;// web
	protected String searchProtoId;// proto
	protected String searchIspCode;// 运营商

	// protected String searchServerLocate;//serverLocate
	// protected String searchClientLocate;//clientLocate

	@JsonIgnore
	public String getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(String appLabel) {
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

	public Long getProtoId() {
		return protoId;
	}

	public void setProtoId(Long protoId) {
		this.protoId = protoId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getWebId() {
		return webId;
	}

	public void setWebId(Long webId) {
		this.webId = webId;
	}

	@JsonIgnore
	public String getSearchAppId() {
		return searchAppId;
	}

	public void setSearchAppId(String searchAppId) {
		this.searchAppId = searchAppId;
	}

	@JsonIgnore
	public String getSearchWebId() {
		return searchWebId;
	}

	public void setSearchWebId(String searchWebId) {
		this.searchWebId = searchWebId;
	}

	@JsonIgnore
	public String getSearchProtoId() {
		return searchProtoId;
	}

	public void setSearchProtoId(String searchProtoId) {
		this.searchProtoId = searchProtoId;
	}

	@JsonIgnore
	public String getSearchIspCode() {
		return searchIspCode;
	}

	public void setSearchIspCode(String searchIspCode) {
		this.searchIspCode = searchIspCode;
	}

	public String getIspNum() {
		return ispNum;
	}

	public void setIspNum(String ispNum) {
		this.ispNum = ispNum;
	}


}
