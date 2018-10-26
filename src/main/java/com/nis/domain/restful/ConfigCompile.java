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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nis.util.JsonDateDeserializer;
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
	private Integer service;
	@ApiModelProperty(value = "动作", required = true)
	private Integer action;
	@ApiModelProperty(value = "分类", required = false)
	private String contType;
	@ApiModelProperty(value = "性质", required = false)
	private String attrType;
	@ApiModelProperty(value = "标签", required = false)
	private String contLabel;
	@ApiModelProperty(value = "任务", required = false)
	private Integer taskId;
	@ApiModelProperty(value = "来文函号ID", required = false)
	private Integer affairId;
	@ApiModelProperty(value = "是否黑名单", required = true)
	private Integer doBlacklist;
	@ApiModelProperty(value = "是否生成日志", required = true)
	private Integer doLog;
	@ApiModelProperty(value = "生效范围", required = true)
	private String effectiveRange;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "开始时间", required = true)
	private Date startTime;
	@ApiModelProperty(value = "结束时间", required = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date opTime;

	@ApiModelProperty(value = "编译分组关系列表", required = true)
	private List<ConfigGroupRelation> groupRelationList;

	@ApiModelProperty(value = "字符串域分组列表", access = "", required = true)
	private List<StrRegion> strRegionList;

	@ApiModelProperty(value = "IP域分组列表", required = true)
	private List<IpRegion> ipRegionList;

	@ApiModelProperty(value = "数值域分组列表", required = true)
	private List<NumRegion> numRegionList;

	@ApiModelProperty(value = "摘要域分组列表", required = true)
	private List<DigestRegion> digestRegionList;

	@ApiModelProperty(value = "生效范围IP域分组列表", required = true)
	private List<IpRegion> ipClientRangeList;

	public Long getCompileId() {
		return compileId;
	}

	public void setCompileId(Long compileId) {
		this.compileId = compileId;
	}

	/**
	 * @return service
	 */
	public Integer getService() {
		return service;
	}

	/**
	 * @param service
	 *            要设置的 service
	 */
	public void setService(Integer service) {
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

	public String getContType() {
		return contType;
	}

	public void setContType(String contType) {
		this.contType = contType;
	}

	public String getAttrType() {
		return attrType;
	}

	public void setAttrType(String attrType) {
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
	public Integer getDoBlacklist() {
		return doBlacklist;
	}

	/**
	 * @param doBlackList
	 *            要设置的 doBlackList
	 */
	public void setDoBlacklist(Integer doBlacklist) {
		this.doBlacklist = doBlacklist;
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
	 * @return startTime
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            要设置的 startTime
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return endTime
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            要设置的 endTime
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
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
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * @param opTime
	 *            要设置的 opTime
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
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

	public List<DigestRegion> getDigestRegionList() {
		return digestRegionList;
	}

	public void setDigestRegionList(List<DigestRegion> digestRegionList) {
		this.digestRegionList = digestRegionList;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getAffairId() {
		return affairId;
	}

	public void setAffairId(Integer affairId) {
		this.affairId = affairId;
	}

	public List<IpRegion> getIpClientRangeList() {
		return ipClientRangeList;
	}

	public void setIpClientRangeList(List<IpRegion> ipClientRangeList) {
		this.ipClientRangeList = ipClientRangeList;
	}

}
