package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.domain.DfReportEntity;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: dfServiceReport
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2016年10月31日上午11:22:07 
 * @version V1.0
 */
public class DfServiceReport extends DfReportEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6219213545074023084L;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer serviceType;
	
	protected String searchService;
	
	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}
	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}
	
	public Integer getServiceType() {
		return serviceType;
	}
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}
	
}
