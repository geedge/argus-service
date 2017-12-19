package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DfSrcIpDomeSticReport
 * @Description: 管控境内源IP(带私有标签)实时统计模型
 * @author (rkg)
 * @date 2017年01月05日 下午3:41:50
 * @version V1.0
 */
public class DfSrcIpDomeSticReport extends DfReportEntity<DfSrcIpDomeSticReport> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7360674711430956834L;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "源IP所在省", required = true)
	protected String srcProvince;
	@ApiModelProperty(value = "源IP所在市", required = true)
	protected String srcCity;

	protected String searchService;
	protected String searchSrcProvince;
	protected String searchSrcCity;

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

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

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

	@JsonIgnore
	public String getSearchSrcProvince() {
		return searchSrcProvince;
	}

	public void setSearchSrcProvince(String searchSrcProvince) {
		this.searchSrcProvince = searchSrcProvince;
	}

	@JsonIgnore
	public String getSearchSrcCity() {
		return searchSrcCity;
	}

	public void setSearchSrcCity(String searchSrcCity) {
		this.searchSrcCity = searchSrcCity;
	}

}
