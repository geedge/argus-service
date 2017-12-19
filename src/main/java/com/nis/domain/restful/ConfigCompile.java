/**   
* @Title: DfConfigCompile.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午9:36:28 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DfConfigCompile
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (darnell)
 * @date 2016年8月29日 下午9:36:28
 * @version V1.0
 */
public class ConfigCompile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 546753235262247839L;
	@ApiModelProperty(value = "配置ID", required = true)
	private Long compileId;
	@ApiModelProperty(value = "业务ID", required = true)
	private Long service;
	@ApiModelProperty(value = "动作", required = true)
	private Integer action;
	@ApiModelProperty(value = "有害类型", required = false)
	private Integer contType;
	@ApiModelProperty(value = "有害性质", required = false)
	private Integer attrType;
	@ApiModelProperty(value = "内容标签", required = false)
	private String contLabel;
	@ApiModelProperty(value = "任务", required = false)
	private Integer taskId;
	@ApiModelProperty(value = "保障期", required = false)
	private Integer guaranteeId;
	@ApiModelProperty(value = "事件", required = false)
	private Integer affAirId;
	@ApiModelProperty(value = "子话题", required = false)
	private Integer topIcId;
	@ApiModelProperty(value = "是否黑名单", required = true)
	private Long doBlackList;
	@ApiModelProperty(value = "是否生成日志", required = true)
	private Integer doLog;
	@ApiModelProperty(value = "生效范围", required = true)
	private Integer activeSys;
	@ApiModelProperty(value = "生效系统", required = true)
	private String effectiveRange;
	@ApiModelProperty(value = "管控比例", required = true)
	private Long configPercent;
	@ApiModelProperty(value = "管控方式", required = true)
	private Integer configOption;
	@ApiModelProperty(value = "开始时间", required = true)
	private Date startTime;
	@ApiModelProperty(value = "结束时间", required = true)
	private Date endTime;
	@ApiModelProperty(value = "用户自定义域", required = true)
	private String userRegion;
	@ApiModelProperty(value = "有效标志", required = true)
	private Integer isValid;
	@ApiModelProperty(value = "包含分组数量", required = true)
	private Integer groupNum;
	@ApiModelProperty(value = "父配置ID", required = true)
	private Long fatherCfgId;
	@ApiModelProperty(value = "操作时间", required = true)
	private Date opTime;
	@ApiModelProperty(value = "本地更新时间	", required = true)
	private Date lastUpdate;

	@ApiModelProperty(value = "编译分组关系列表", required = true)
	private List<ConfigGroupRelation> groupRelationList;



	@ApiModelProperty(value = "字符串域分组列表", access = "", required = true)
	private List<StrRegion> strRegionList;

	@ApiModelProperty(value = "IP域分组列表", required = true)
	private List<IpRegion> ipRegionList;

	@ApiModelProperty(value = "数值域分组列表", required = true)
	private List<NumRegion> numRegionList;
	
	@ApiModelProperty(value="版本序列号", required=true)
	private Long procSeq;
	
	@JsonIgnore
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Integer getActiveSys() {
		return activeSys;
	}

	public void setActiveSys(Integer activeSys) {
		this.activeSys = activeSys;
	}

	public Long getCompileId() {
		return compileId;
	}

	public void setCompileId(Long compileId) {
		this.compileId = compileId;
	}

	/**
	 * @return service
	 */
	public Long getService() {
		return service;
	}

	/**
	 * @param service
	 *            要设置的 service
	 */
	public void setService(Long service) {
		this.service = service;
	}

	/**
	 * @return action
	 */
	public Integer getAction() {
		return action;
	}

	/**
	 * @param action
	 *            要设置的 action
	 */
	public void setAction(Integer action) {
		this.action = action;
	}

	public Integer getContType() {
		return contType;
	}

	public void setContType(Integer contType) {
		this.contType = contType;
	}

	public Integer getAttrType() {
		return attrType;
	}

	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}

	public String getContLabel() {
		return contLabel;
	}

	public void setContLabel(String contLabel) {
		this.contLabel = contLabel;
	}

	/**
	 * @return doBlackList
	 */
	public Long getDoBlackList() {
		return doBlackList;
	}

	/**
	 * @param doBlackList
	 *            要设置的 doBlackList
	 */
	public void setDoBlackList(Long doBlackList) {
		this.doBlackList = doBlackList;
	}

	/**
	 * @return doLog
	 */
	public Integer getDoLog() {
		return doLog;
	}

	/**
	 * @param doLog
	 *            要设置的 doLog
	 */
	public void setDoLog(Integer doLog) {
		this.doLog = doLog;
	}

	/**
	 * @return effectiveRange
	 */
	public String getEffectiveRange() {
		return effectiveRange;
	}

	/**
	 * @param effectiveRange
	 *            要设置的 effectiveRange
	 */
	public void setEffectiveRange(String effectiveRange) {
		this.effectiveRange = effectiveRange;
	}

	/**
	 * @return configPercent
	 */
	public Long getConfigPercent() {
		return configPercent;
	}

	/**
	 * @param configPercent
	 *            要设置的 configPercent
	 */
	public void setConfigPercent(Long configPercent) {
		this.configPercent = configPercent;
	}

	/**
	 * @return configOption
	 */
	public Integer getConfigOption() {
		return configOption;
	}

	/**
	 * @param configOption
	 *            要设置的 configOption
	 */
	public void setConfigOption(Integer configOption) {
		this.configOption = configOption;
	}

	/**
	 * @return startTime
	 */
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            要设置的 startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return endTime
	 */
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            要设置的 endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return userRegion
	 */
	public String getUserRegion() {
		return userRegion;
	}

	/**
	 * @param userRegion
	 *            要设置的 userRegion
	 */
	public void setUserRegion(String userRegion) {
		this.userRegion = userRegion;
	}

	/**
	 * @return isValid
	 */
	public Integer getIsValid() {
		return isValid;
	}

	/**
	 * @param isValid
	 *            要设置的 isValid
	 */
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return groupNum
	 */
	public Integer getGroupNum() {
		return groupNum;
	}

	/**
	 * @param groupNum
	 *            要设置的 groupNum
	 */
	public void setGroupNum(Integer groupNum) {
		this.groupNum = groupNum;
	}

	/**
	 * @return fatherCfgId
	 */
	public Long getFatherCfgId() {
		return fatherCfgId;
	}

	/**
	 * @param fatherCfgId
	 *            要设置的 fatherCfgId
	 */
	public void setFatherCfgId(Long fatherCfgId) {
		this.fatherCfgId = fatherCfgId;
	}

	/**
	 * @return opTime
	 */
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * @param opTime
	 *            要设置的 opTime
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	/**
	 * @return groupRelationList
	 */
	public List<ConfigGroupRelation> getGroupRelationList() {
		return groupRelationList;
	}

	/**
	 * @param groupRelationList
	 *            要设置的 groupRelationList
	 */
	public void setGroupRelationList(List<ConfigGroupRelation> groupRelationList) {
		this.groupRelationList = groupRelationList;
	}

	/**
	 * @return strRegionList
	 */
	public List<StrRegion> getStrRegionList() {
		return strRegionList;
	}

	/**
	 * @param strRegionList
	 *            要设置的 strRegionList
	 */
	public void setStrRegionList(List<StrRegion> strRegionList) {
		this.strRegionList = strRegionList;
	}

	/**
	 * @return ipRegionList
	 */
	public List<IpRegion> getIpRegionList() {
		return ipRegionList;
	}

	/**
	 * @param ipRegionList
	 *            要设置的 ipRegionList
	 */
	public void setIpRegionList(List<IpRegion> ipRegionList) {
		this.ipRegionList = ipRegionList;
	}

	/**
	 * @return numRegionList
	 */
	@ApiModelProperty(value = "数值域分组列表", required = true)
	public List<NumRegion> getNumRegionList() {
		return numRegionList;
	}

	/**
	 * @param numRegionList
	 *            要设置的 numRegionList
	 */
	public void setNumRegionList(List<NumRegion> numRegionList) {
		this.numRegionList = numRegionList;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getGuaranteeId() {
		return guaranteeId;
	}

	public void setGuaranteeId(Integer guaranteeId) {
		this.guaranteeId = guaranteeId;
	}

	public Integer getAffAirId() {
		return affAirId;
	}

	public void setAffAirId(Integer affAirId) {
		this.affAirId = affAirId;
	}

	public Integer getTopIcId() {
		return topIcId;
	}

	public void setTopIcId(Integer topIcId) {
		this.topIcId = topIcId;
	}

	@JsonIgnore
	public Long getProcSeq() {
		return procSeq;
	}
	public void setProcSeq(Long procSeq) {
		this.procSeq = procSeq;
	}
}
