package com.nis.domain.restful;

import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class DfSrcIpReport extends DfReportEntity<DfSrcIpReport>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4386680443050994654L;
	
	
	@ApiModelProperty(value = "源IP所属省", required = true)
	protected String srcProvince;
	@ApiModelProperty(value = "源IP所属市")
	protected String srcCity;
	
	public String getSrcProvince() {
		return srcProvince;
	}
	public void setSrcProvince(String srcProvince) {
		this.srcProvince = srcProvince;
	}
	
	public String getSrcCity() {
		return srcCity;
	}
	public void setSrcCity(String srcCity) {
		this.srcCity = srcCity;
	}

}
