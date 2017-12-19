package com.nis.domain.restful;

import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfDestIpReport extends DfReportEntity<DfDestIpReport>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6785031994558744311L;
	
	
	@ApiModelProperty(value = "目的IP所属国家", required = true)
	protected String destCountry;
	
	public String getDestCountry() {
		return destCountry;
	}
	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

}
