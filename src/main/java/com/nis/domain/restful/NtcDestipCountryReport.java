package com.nis.domain.restful;

import com.wordnik.swagger.annotations.ApiModelProperty;


public class NtcDestipCountryReport extends NtcReportEntity<NtcDestipCountryReport>{

	private static final long serialVersionUID = 7911364106357601141L;
	@ApiModelProperty(value="所属国家", required=true)
	protected String destCountry;
	
	/**
	 * @param destCountry the destCountry to set
	 */
	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}
	
	/**
	 * @return the destCountry
	 */
	public String getDestCountry() {
		return destCountry;
	}
}
