/**   
* @Title: DjIpsecLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （zbc）  
* @date 2016年9月7日 下午19:16:20 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DjIpsecLog 
* @Description: IPSEC密钥交换协议监测日志
* @author (zbc)  
* @date 2016年9月7日 下午19:16:20 
* @version V1.0 
*/
public class DjIpsecLog extends LogEntity<DjIpsecLog>{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 607608013629342421L;
	
	@ApiModelProperty(value="交换协议", required=true)
	protected Integer exProtocol;
	@ApiModelProperty(value="ISAKMP模式", required=true)
	protected Long isakmpMode;
	
	protected String searchExProtocol;
	
	
	@JsonIgnore
	public String getSearchExProtocol() {
		return searchExProtocol;
	}
	public void setSearchExProtocol(String searchExProtocol) {
		this.searchExProtocol = searchExProtocol;
	}
	public Integer getExProtocol() {
		return exProtocol;
	}
	public void setExProtocol(Integer exProtocol) {
		this.exProtocol = exProtocol;
	}
	public Long getIsakmpMode() {
		return isakmpMode;
	}
	public void setIsakmpMode(Long isakmpMode) {
		this.isakmpMode = isakmpMode;
	}
}
