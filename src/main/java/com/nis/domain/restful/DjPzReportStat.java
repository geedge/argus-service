package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.DfReportEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName: DjPzReportStat
 * @Description: TODO(一句话描述这个类)
 * @author (DDM)
 * @date 2016年10月31日上午11:11:22
 * @version V1.0
 */
public class DjPzReportStat extends DfReportEntity<DjPzReportStat> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4204790492680773920L;
	@ApiModelProperty(value = "私有标签", required = true)
	protected Integer service;
	@ApiModelProperty(value = "编译配置ID", required = true)
	protected Long configId;
	@ApiModelProperty(value = "日志总量", required = true)
	protected Long sum;

	protected String searchService;
	protected String searchStatActiveSys;

	@JsonIgnore
	public String getSearchStatActiveSys() {
		return searchStatActiveSys;
	}

	public void setSearchStatActiveSys(String searchStatActiveSys) {
		this.searchStatActiveSys = searchStatActiveSys;
	}

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	@Override
	@JsonIgnore
	public Long getAsum() {
		return super.getAsum();
	}

	@Override
	@JsonIgnore
	public Long getBsum() {
		return super.getBsum();
	}

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}

	@Override
	@JsonIgnore
	public Date getReportTime() {
		// TODO Auto-generated method stub
		return super.getReportTime();
	}
	@Override
	@JsonIgnore
	public Long getCsum() {
		return super.getCsum();
	}
	/* (non-Javadoc)
	 * @see com.nis.domain.DfReportEntity#getAbsum()
	 */
	@Override
	@JsonIgnore
	public Long getAbsum() {
		// TODO Auto-generated method stub
		return super.getAbsum();
	}
	
}
