package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DfAttrTypeReport
 * @Description: 管控性质实时统计
 * @author (rkg)
 * @date 2017年01月05日 下午3:41:50
 * @version V1.0
 */
public class DjAttrTypeReport extends DfReportEntity<DjAttrTypeReport> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9186632214937498812L;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "性质", required = true)
	protected Integer attrType;

	protected String searchAttrType;
	protected String searchService;

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getAttrType() {
		return attrType;
	}

	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}

	@JsonIgnore
	public String getSearchAttrType() {
		return searchAttrType;
	}

	public void setSearchAttrType(String searchAttrType) {
		this.searchAttrType = searchAttrType;
	}

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

}
