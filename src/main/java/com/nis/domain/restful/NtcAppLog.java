package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName:NtcAppLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月5日 下午6:21:55
 * @version V1.0
 */
public class NtcAppLog extends LogEntity<NtcAppLog> {

	private static final long serialVersionUID = -1186780816038898916L;

	@ApiModelProperty(value = "协议类型ID", required = true)
	private Integer labelProtoId;
	@ApiModelProperty(value = "协议类型来源", required = true)
	private Integer labelProtoSource;
	@ApiModelProperty(value = "行为ID", required = true)
	private Integer labelBehavId;
	@ApiModelProperty(value = "行为来源", required = true)
	private Integer labelBehavSource;
	@ApiModelProperty(value = "应用ID", required = true)
	private Integer labelAppId;
	@ApiModelProperty(value = "应用来源", required = true)
	private Integer labelAppSource;

	public Integer getLabelProtoId() {
		return labelProtoId;
	}
	public void setLabelProtoId(Integer labelProtoId) {
		this.labelProtoId = labelProtoId;
	}
	public Integer getLabelProtoSource() {
		return labelProtoSource;
	}
	public void setLabelProtoSource(Integer labelProtoSource) {
		this.labelProtoSource = labelProtoSource;
	}

	public Integer getLabelBehavId() {
		return labelBehavId;
	}

	public void setLabelBehavId(Integer labelBehavId) {
		this.labelBehavId = labelBehavId;
	}

	public Integer getLabelBehavSource() {
		return labelBehavSource;
	}

	public void setLabelBehavSource(Integer labelBehavSource) {
		this.labelBehavSource = labelBehavSource;
	}

	public Integer getLabelAppId() {
		return labelAppId;
	}

	public void setLabelAppId(Integer labelAppId) {
		this.labelAppId = labelAppId;
	}

	public Integer getLabelAppSource() {
		return labelAppSource;
	}

	public void setLabelAppSource(Integer labelAppSource) {
		this.labelAppSource = labelAppSource;
	}
}
