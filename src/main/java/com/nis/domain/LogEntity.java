/**   
 * @Title: LogEntity.java 
 * @Package com.nis.domain 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author （darnell）  
 * @date 2016年9月1日 上午10:16:54 
 * @version V1.0   
 */
package com.nis.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Maps;
import com.nis.util.Configurations;
import com.nis.util.JsonDateSerializer;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: LogEntity
 * @Description: TODO(日志公共实体部分)
 * @author (darnell)
 * @date 2016年9月1日 上午10:16:54
 * @version V1.0
 */
public abstract class LogEntity<T> implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "日志编号", required = true)
	protected Long id;

	@ApiModelProperty(value = "配置ID", required = true)
	protected Long cfgId;
	@ApiModelProperty(value = "发现时间", required = true)
	protected Date foundTime;
	@ApiModelProperty(value = "接收时间", required = true)
	protected Date recvTime;
	@ApiModelProperty(value = "协议类型", required = true)
	protected String transProto;
	@ApiModelProperty(value = "IP地址类型", required = true)
	protected Integer addrType;
	@ApiModelProperty(value = "服务端ip地址", required = true)
	protected String dIp;
	@ApiModelProperty(value = "客户端ip地址", required = true)
	protected String sIp;
	@ApiModelProperty(value = "服务端端口", required = true)
	protected Integer dPort;
	@ApiModelProperty(value = "客户端端口", required = true)
	protected Integer sPort;
	@ApiModelProperty(value = "业务类型", required = true)
	protected Integer service;
	@ApiModelProperty(value = "出入口编号", required = true)
	protected Long entranceId;
	@ApiModelProperty(value = "串联设备编号", required = true)
	protected Integer deviceId;
	@ApiModelProperty(value = "传输方向", required = true, notes = "0：域内->域外，1：域外->域内，描述的是CLIENT_IP信息")
	protected Integer direction;
	@ApiModelProperty(value = "流类型", required = true, notes = "0：c2s，1：s2c；2；double")
	protected Integer streamDir;
	@ApiModelProperty(value = "处理机IP", required = true)
	protected String capIp;
	@ApiModelProperty(value = "嵌套地址列表", required = true)
	protected String addrList;
	@ApiModelProperty(value = "用户自定义域", required = true)
	protected String userRegion;
	@ApiModelProperty(value = "服务端地址定位信息", required = true)
	protected String serverLocate;
	@ApiModelProperty(value = "客户端地址定位信息", required = true)
	protected String clientLocate;
	@ApiModelProperty(value = "客户端ASN", required = true)
	protected String sAsn;
	@ApiModelProperty(value = "服务端ASN", required = true)
	protected String dAsn;
	@ApiModelProperty(value = "客户端用户名", required = true)
	protected String sSubscribeId;
	@ApiModelProperty(value = "服务器用户名", required = true)
	protected String dSubscribeId;
	@ApiModelProperty(value = "现场日志文件地址", required = true)
	protected String sceneFile;
	
	@ApiModelProperty(value = "串联设备链路号", required = true)
	protected Long linkId;	
	@ApiModelProperty(value = "原始二层封装格式", required = true)
	protected Integer encapType;
	@ApiModelProperty(value = "MAC_IN_MAC的内层源MAC", required = true)
	protected String innerSmac;
	@ApiModelProperty(value = "MAC_IN_MAC的内层目标MAC", required = true)
	protected String innerDmac;
	
	
	protected String searchCfgId;//配置id
	protected String searchTransProto;//协议类型
	protected String searchDIp;//服务端ip地址
	protected String searchSIp;//客户端ip地址
	protected String searchDirection;//传输方向
	protected String searchService;//业务类型
	protected String searchEntranceId;//出入口编号
	protected String searchCapIp;//处理机IP
	protected String searchFoundStartTime;//开始发现时间
	protected String searchFoundEndTime;//结束发现时间
	
	//add by dxy 2019-01-05 仅限于NtcConnRecordLog需求，考虑以后可能会放开其他日志查询，所以放到公共类
	protected String searchSAsn;//sAsn
	protected String searchDAsn;//dAsn
	protected String searchServerLocate;//serverLocate
	protected String searchClientLocate;//clientLocate
	protected String searchSSubscribeId;//sSubscribeId
	protected String searchDSubscribeId;//dSubscribeId
	protected String searchIspCode;// 运营商
	
	


	@JsonIgnore
	public String getSearchIspCode() {
		return searchIspCode;
	}

	public void setSearchIspCode(String searchIspCode) {
		this.searchIspCode = searchIspCode;
	}

	@JsonIgnore
	public String getSearchSAsn() {
		return searchSAsn;
	}

	public void setSearchSAsn(String searchSAsn) {
		this.searchSAsn = searchSAsn;
	}

	@JsonIgnore
	public String getSearchDAsn() {
		return searchDAsn;
	}

	public void setSearchDAsn(String searchDAsn) {
		this.searchDAsn = searchDAsn;
	}

	@JsonIgnore
	public String getSearchSSubscribeId() {
		return searchSSubscribeId;
	}

	public void setSearchSSubscribeId(String searchSSubscribeId) {
		this.searchSSubscribeId = searchSSubscribeId;
	}


	@JsonIgnore
	public String getSearchDSubscribeId() {
		return searchDSubscribeId;
	}

	public void setSearchDSubscribeId(String searchDSubscribeId) {
		this.searchDSubscribeId = searchDSubscribeId;
	}


	@JsonIgnore
	public String getSearchServerLocate() {
		return searchServerLocate;
	}

	public void setSearchServerLocate(String searchServerLocate) {
		this.searchServerLocate = searchServerLocate;
	}


	@JsonIgnore
	public String getSearchClientLocate() {
		return searchClientLocate;
	}

	public void setSearchClientLocate(String searchClientLocate) {
		this.searchClientLocate = searchClientLocate;
	}

	/**
	 * 当前实体分页对象
	 */
	protected Page<T> page;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	/**
	 * @Title:
	 * @Description: TODO
	 * @param
	 */
	public LogEntity() {
		super();
	}

	/**
	 * @return id
	 */
	@JsonIgnore
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return cfgId
	 */
	public Long getCfgId() {
		return cfgId;
	}

	/**
	 * @param cfgId
	 *            要设置的 cfgId
	 */
	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}

	/**
	 * @return foundTime
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getFoundTime() {
		return foundTime;
	}

	/**
	 * @param foundTime
	 *            要设置的 foundTime
	 */
	public void setFoundTime(Date foundTime) {
		this.foundTime = foundTime;
	}

	/**
	 * @return recvTime
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getRecvTime() {
		return recvTime;
	}

	/**
	 * @param recvTime
	 *            要设置的 recvTime
	 */
	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	/**
	 * @return entranceId
	 */
	public Long getEntranceId() {
		return entranceId;
	}

	/**
	 * @param entranceId
	 *            要设置的 entranceId
	 */
	public void setEntranceId(Long entranceId) {
		this.entranceId = entranceId;
	}

	public String getTransProto() {
		return transProto;
	}

	public void setTransProto(String transProto) {
		this.transProto = transProto;
	}

	public String getdIp() {
		return dIp;
	}

	public void setdIp(String dIp) {
		this.dIp = dIp;
	}

	public String getsIp() {
		return sIp;
	}

	public void setsIp(String sIp) {
		this.sIp = sIp;
	}

	public Integer getdPort() {
		return dPort;
	}

	public void setdPort(Integer dPort) {
		this.dPort = dPort;
	}

	public Integer getsPort() {
		return sPort;
	}

	public void setsPort(Integer sPort) {
		this.sPort = sPort;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getStreamDir() {
		return streamDir;
	}

	public void setStreamDir(Integer streamDir) {
		this.streamDir = streamDir;
	}

	public String getCapIp() {
		return capIp;
	}

	public void setCapIp(String capIp) {
		this.capIp = capIp;
	}

	public String getAddrList() {
		return addrList;
	}

	public void setAddrList(String addrList) {
		this.addrList = addrList;
	}

	@JsonIgnore
	@XmlTransient
	public Page<T> getPage() {
		if (page == null) {
			page = new Page<T>();
		}
		return page;
	}

	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null) {
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	/**
	 * 获取数据库名称
	 */
	@JsonIgnore
	public String getDbName() {
		return Configurations.getStringProperty("jdbc.type", "mysql");
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		LogEntity<?> that = (LogEntity<?>) obj;
		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	public Integer getAddrType() {
		return addrType;
	}

	public void setAddrType(Integer addrType) {
		this.addrType = addrType;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public String getUserRegion() {
		return userRegion;
	}

	public void setUserRegion(String userRegion) {
		this.userRegion = userRegion;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@JsonIgnore
	public String getSearchCfgId() {
		return searchCfgId;
	}

	public void setSearchCfgId(String searchCfgId) {
		this.searchCfgId = searchCfgId;
	}

	@JsonIgnore
	public String getSearchTransProto() {
		return searchTransProto;
	}

	public void setSearchTransProto(String searchTransProto) {
		this.searchTransProto = searchTransProto;
	}

	@JsonIgnore
	public String getSearchDIp() {
		return searchDIp;
	}

	public void setSearchDIp(String searchDIp) {
		this.searchDIp = searchDIp;
	}

	@JsonIgnore
	public String getSearchSIp() {
		return searchSIp;
	}

	public void setSearchSIp(String searchSIp) {
		this.searchSIp = searchSIp;
	}

	@JsonIgnore
	public String getSearchDirection() {
		return searchDirection;
	}

	public void setSearchDirection(String searchDirection) {
		this.searchDirection = searchDirection;
	}

	@JsonIgnore
	public String getSearchService() {
		return searchService;
	}

	public void setSearchService(String searchService) {
		this.searchService = searchService;
	}

	@JsonIgnore
	public String getSearchEntranceId() {
		return searchEntranceId;
	}

	public void setSearchEntranceId(String searchEntranceId) {
		this.searchEntranceId = searchEntranceId;
	}

	@JsonIgnore
	public String getSearchCapIp() {
		return searchCapIp;
	}

	public void setSearchCapIp(String searchCapIp) {
		this.searchCapIp = searchCapIp;
	}

	@JsonIgnore
	public String getSearchFoundStartTime() {
		return searchFoundStartTime;
	}

	public void setSearchFoundStartTime(String searchFoundStartTime) {
		this.searchFoundStartTime = searchFoundStartTime;
	}

	@JsonIgnore
	public String getSearchFoundEndTime() {
		return searchFoundEndTime;
	}

	public void setSearchFoundEndTime(String searchFoundEndTime) {
		this.searchFoundEndTime = searchFoundEndTime;
	}

	public String getServerLocate() {
		return serverLocate;
	}

	public void setServerLocate(String serverLocate) {
		this.serverLocate = serverLocate;
	}

	public String getClientLocate() {
		return clientLocate;
	}

	public void setClientLocate(String clientLocate) {
		this.clientLocate = clientLocate;
	}
	/**
	 * @param sAsn the sAsn to set
	 */
	public void setsAsn(String sAsn) {
		this.sAsn = sAsn;
	}
	/**
	 * @return the sAsn
	 */
	public String getsAsn() {
		return sAsn;
	}
	/**
	 * @param dAsn the dAsn to set
	 */
	public void setdAsn(String dAsn) {
		this.dAsn = dAsn;
	}
	/**
	 * @return the dAsn
	 */
	public String getdAsn() {
		return dAsn;
	}
	/**
	 * @param sSubscribeId the sSubscribeId to set
	 */
	public void setsSubscribeId(String sSubscribeId) {
		this.sSubscribeId = sSubscribeId;
	}
	/**
	 * @return the sSubscribeId
	 */
	public String getsSubscribeId() {
		return sSubscribeId;
	}
	/**
	 * @param dSubscribeId the dSubscribeId to set
	 */
	public void setdSubscribeId(String dSubscribeId) {
		this.dSubscribeId = dSubscribeId;
	}
	/**
	 * @return the dSubscribeId
	 */
	public String getdSubscribeId() {
		return dSubscribeId;
	}
	/**
	 * @param sceneFile the sceneFile to set
	 */
	public void setSceneFile(String sceneFile) {
		this.sceneFile = sceneFile;
	}
	/**
	 * @return the sceneFile
	 */
	public String getSceneFile() {
		return sceneFile;
	}

	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public Integer getEncapType() {
		return encapType;
	}

	public void setEncapType(Integer encapType) {
		this.encapType = encapType;
	}

	public String getInnerSmac() {
		return innerSmac;
	}

	public void setInnerSmac(String innerSmac) {
		this.innerSmac = innerSmac;
	}

	public String getInnerDmac() {
		return innerDmac;
	}

	public void setInnerDmac(String innerDmac) {
		this.innerDmac = innerDmac;
	}
	
	
}
