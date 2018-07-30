package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wordnik.swagger.annotations.ApiModelProperty;


public class NtcSrcipDomesticReport extends NtcReportEntity<NtcSrcipDomesticReport>{

	private static final long serialVersionUID = -2146157609572355782L;
	@JsonInclude(value=Include.NON_NULL)
	@ApiModelProperty(value="所属省", required=true)
	protected String srcProvince;
	@JsonInclude(value=Include.NON_NULL)
	@ApiModelProperty(value="所属市", required=true)
	protected String srcCity;
	
	/**
	 * @param srcProvince the srcProvince to set
	 */
	public void setSrcProvince(String srcProvince) {
		this.srcProvince = srcProvince;
	}
	
	/**
	 * @return the srcProvince
	 */
	public String getSrcProvince() {
		return srcProvince;
	}
	
	/**
	 * @param srcCity the srcCity to set
	 */
	public void setSrcCity(String srcCity) {
		this.srcCity = srcCity;
	}
	
	/**
	 * @return the srcCity
	 */
	public String getSrcCity() {
		return srcCity;
	}
}
