package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 	@ClassName:NtcStreamMediaLog
 * 	@Description:TODO(这里用一句话描述这个类的作用)
 *	@author zdx
 *	@date 2018年10月9日 下午6:33:11
 *	@version V1.0
 */
public class NtcStreamMediaLog  extends LogEntity<NtcStreamMediaLog> {
	private static final long serialVersionUID = 8993339583170276251L;
    @ApiModelProperty(value="节目访问地址", required=true)
    protected String url;
    @ApiModelProperty(value="传输协议", required=true)
    protected String protocol;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
