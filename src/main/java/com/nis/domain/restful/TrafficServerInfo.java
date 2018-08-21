package com.nis.domain.restful;

import java.util.Date;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * <p>Title: NmsServerInfoVo</p>
 * <p>Description: 记录nms服务器信息,全网机器总量,正常在线机器总数,异常机器列表</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年8月17日 
 *
 */
@ApiModel(value = "NmsServerInfoVo对象", description = "nms上报的服务器相关信息对象类")
public class TrafficServerInfo {

	@ApiModelProperty(value = "commitTime", notes = "上报时间")
	private Date commitTime;// 提交时间
	@ApiModelProperty(value = "area", notes = "区域")
	private String area;// 区域信息
	@ApiModelProperty(value = "total", notes = "主机总数量")
	private Integer total;// 机器总数量
	@ApiModelProperty(value = "normal", notes = "正常主机数量")
	private Integer normal;// 正常数量
	@ApiModelProperty(value = "abnormal", notes = "异常主机数量")
	private Integer abnormal;//
	@ApiModelProperty(value = "abnormalMachineList", notes = "异常主机列表")
	private List<AbnormalMachine> abnormalMachineList;

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getNormal() {
		return normal;
	}

	public void setNormal(Integer normal) {
		this.normal = normal;
	}

	public Integer getAbnormal() {
		return abnormal;
	}

	public void setAbnormal(Integer abnormal) {
		this.abnormal = abnormal;
	}

	public List<AbnormalMachine> getAbnormalMachineList() {
		return abnormalMachineList;
	}

	public void setAbnormalMachineList(List<AbnormalMachine> abnormalMachineList) {
		this.abnormalMachineList = abnormalMachineList;
	}

}
