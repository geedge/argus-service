package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfTunnelRandomLog 
* @Description: 隧道协议随机封堵
* @author (ddm)  
* @date 2016年12月23日 上午11:18:14 
* @version V1.0 
*/
public class DfTunnelRandomLog extends LogEntity<DfTunnelRandomLog>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 111411360809146865L;
	@ApiModelProperty(value="隧道协议信息", required=true)
	protected Long tunnelName;
	
	public Long getTunnelName() {
		return tunnelName;
	}
	public void setTunnelName(Long tunnelName) {
		this.tunnelName = tunnelName;
	}
		
}
