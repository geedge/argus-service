package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DfDestIpCountryReport
 * @Description: 管控境内目的IP(带私有标签)实时统计模型
 * @author (rkg)
 * @date 2017年01月05日 下午3:41:50
 * @version V1.0
 */
public class DfDestIpCountryReport extends DfReportEntity<DfDestIpCountryReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6490028574641528475L;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "目的IP所属国家", required = true)
	protected String destCountry;

	protected String searchService;
	protected String searchDestCountry;

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

	@JsonIgnore
	public String getSearchDestCountry() {
		return searchDestCountry;
	}

	public void setSearchDestCountry(String searchDestCountry) {
		this.searchDestCountry = searchDestCountry;
	}

}
