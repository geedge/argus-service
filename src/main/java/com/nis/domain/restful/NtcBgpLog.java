package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName:NtcBgpLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年8月22日 下午2:18:06
 * @version V1.0
 */
public class NtcBgpLog extends LogEntity<NtcBgpLog> {

	private static final long serialVersionUID = -990806269407561021L;
    @ApiModelProperty(value="BGP消息类型", required=true)
    protected Integer type;
    @ApiModelProperty(value="自治系统号", required=true)
    protected String asNum;
    @ApiModelProperty(value="路由信息", required=true)
    protected String route;
    
    protected String searchAsNum;//ASN号
    
    public void setType(Integer type) {
		this.type = type;
	}
    public Integer getType() {
		return type;
	}
    public void setAsNum(String asNum) {
    	this.asNum = asNum;
    }
    public String getAsNum() {
		return asNum;
	}
    public void setRoute(String route) {
		this.route = route;
	}
    public String getRoute() {
		return route;
	}
    
    @JsonIgnore
    public String getSearchAsNum() {
		return searchAsNum;
	}
    
    public void setSearchAsNum(String searchAsNum) {
		this.searchAsNum = searchAsNum;
	}
}
