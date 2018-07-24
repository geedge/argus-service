package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:MmSamplePicLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月24日 上午9:21:19
 * @version V1.0
 */
public class MmSamplePicLog  extends LogEntity<MmSamplePicLog> {
	private static final long serialVersionUID = -452630606801144954L;
	@ApiModelProperty(value="节目ID", required=true)
    protected String pid;
    @ApiModelProperty(value="节目访问地址", required=true)
    protected String url;
    @ApiModelProperty(value="封堵现场片段路径", required=true)
    protected String logUri;
    @ApiModelProperty(value="入口页面", required=true)
    protected String refer;
    @ApiModelProperty(value="有害级别", required=true)
    protected Integer level;
    @ApiModelProperty(value="封堵类型", required=true)
    protected Integer fdType;
    @ApiModelProperty(value="协议", required=true)
    protected String protocol;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogUri() {
		return logUri;
	}
	public void setLogUri(String logUri) {
		this.logUri = logUri;
	}
	public String getRefer() {
		return refer;
	}
	public void setRefer(String refer) {
		this.refer = refer;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getFdType() {
		return fdType;
	}
	public void setFdType(Integer fdType) {
		this.fdType = fdType;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
