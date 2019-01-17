package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 */
public class NtcHttpRecordLog extends LogEntity {

    private static final long serialVersionUID = 2553033624540656138L;

    @ApiModelProperty(value="url地址", required=true)
    protected String url;
    @ApiModelProperty(value="", required=true)
    protected Integer httpSeq;
    @ApiModelProperty(value="", required=true)
    protected Integer proxyFlag;
    @ApiModelProperty(value="", required=true)
    protected Integer isn;
    @ApiModelProperty(value="入口页面", required=true)
    protected String referer;
    
    protected String searchUrl; //url查询条件
   
    protected String searchDeviceId; //串联设备编号查询条件
    protected String searchDPort; //目的端口查询条件
    protected String searchSPort; //源端口查询条件
    protected String searchReferer; //入口页面查询条件
    
	protected Long foundStartTime;//开始发现时间(时间戳类型)
	protected Long foundEndTime;//结束发现时间(时间戳类型)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
	public Integer getHttpSeq() {
		return httpSeq;
	}

	public void setHttpSeq(Integer httpSeq) {
		this.httpSeq = httpSeq;
	}

	public Integer getProxyFlag() {
		return proxyFlag;
	}

	public void setProxyFlag(Integer proxyFlag) {
		this.proxyFlag = proxyFlag;
	}

	public Integer getIsn() {
		return isn;
	}

	public void setIsn(Integer isn) {
		this.isn = isn;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	/**
	 * @return the searchUrl
	 */

	@JsonIgnore
	public String getSearchUrl() {
		return searchUrl;
	}
	
	/**
	 * @param searchUrl the searchUrl to set
	 */
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	@JsonIgnore
	public String getSearchDeviceId() {
		return searchDeviceId;
	}

	public void setSearchDeviceId(String searchDeviceId) {
		this.searchDeviceId = searchDeviceId;
	}
	@JsonIgnore
	public String getSearchDPort() {
		return searchDPort;
	}

	public void setSearchDPort(String searchDPort) {
		this.searchDPort = searchDPort;
	}
	@JsonIgnore
	public String getSearchSPort() {
		return searchSPort;
	}

	public void setSearchSPort(String searchSPort) {
		this.searchSPort = searchSPort;
	}
	@JsonIgnore
	public String getSearchReferer() {
		return searchReferer;
	}

	public void setSearchReferer(String searchReferer) {
		this.searchReferer = searchReferer;
	}
	@JsonIgnore
	public Long getFoundStartTime() {
		return foundStartTime;
	}

	public void setFoundStartTime(Long foundStartTime) {
		this.foundStartTime = foundStartTime;
	}
	@JsonIgnore
	public Long getFoundEndTime() {
		return foundEndTime;
	}

	public void setFoundEndTime(Long foundEndTime) {
		this.foundEndTime = foundEndTime;
	}

	
}
