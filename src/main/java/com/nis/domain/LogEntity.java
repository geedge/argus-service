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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
	
	@ApiModelProperty(value="日志编号", required=true)
	protected Long id;
	
	@ApiModelProperty(value="配置ID", required=true)
	protected Long cfgId;
	@ApiModelProperty(value="发现时间", required=true)
	protected Date foundTime;
	@ApiModelProperty(value="接收时间", required=true)
	protected Date recvTime;
	@ApiModelProperty(value="协议类型", required=true)
	protected String protocol;
	@ApiModelProperty(value="IP地址类型", required=true)
	protected Integer addrType;
	@ApiModelProperty(value="服务端ip地址", required=true)
	protected String serverIp;
	@ApiModelProperty(value="客户端ip地址", required=true)
	protected String clientIp;
	@ApiModelProperty(value="服务端端口", required=true)
	protected Integer serverPort;
	@ApiModelProperty(value="客户端端口", required=true)
	protected Integer clientPort;
    @ApiModelProperty(value="业务类型", required=true)
	protected Integer serviceType;
	@ApiModelProperty(value="串联设备编号", required=true)
	protected Integer deviceId;
	@ApiModelProperty(value="传输方向", required=true, notes = "0：域内->域外，1：域外->域内，描述的是CLIENT_IP信息")
	protected Integer direction;
	@ApiModelProperty(value="流类型", required=true, notes = "0：c2s，1：s2c；2；double")
	protected Integer streamType;
	@ApiModelProperty(value="出入口编号", required=true)
	protected Long entranceId;
	@ApiModelProperty(value="处理机IP", required=true)
	protected String cljIp;

	@ApiModelProperty(value="用户嵌套地址列表", required=true)
	protected String nestAddrList;

	@ApiModelProperty(value="用户自定义域", required=true)
	private String userRegion;

	@ApiModelProperty(value="服务端地址定位信息", required=true)
	protected String serverLocate;
	@ApiModelProperty(value="客户端地址定位信息", required=true)
	protected String clientLocate;
	
	
	protected Long foundTimeCluster;
	protected Long recvTimeCluster;



	protected String searchCfgId;
	protected String searchFoundStartTime;
	protected String searchFoundEndTime;
	protected Long searchFoundStartTimeCluster;
	protected Long searchFoundEndTimeCluster;
	protected String searchProtocol;
	protected String searchServerIp;
	protected String searchClientIp;
	protected Integer searchDirection;
	protected String searchServiceType;
	protected String searchEntranceId;
	protected String searchCljIp;

	
	@JsonIgnore
	public Long getFoundTimeCluster() {
		return foundTimeCluster;
	}
	@JsonIgnore
	public Long getRecvTimeCluster() {
		return recvTimeCluster;
	}
	public void setFoundTimeCluster(Long foundTimeCluster) {
		this.foundTimeCluster = foundTimeCluster;
	}
	public void setRecvTimeCluster(Long recvTimeCluster) {
		this.recvTimeCluster = recvTimeCluster;
	}
	
	@JsonIgnore
	public Long getSearchFoundStartTimeCluster() {
		return searchFoundStartTimeCluster;
	}
	public void setSearchFoundStartTimeCluster(Long searchFoundStartTimeCluster) {
		this.searchFoundStartTimeCluster = searchFoundStartTimeCluster;
	}
	@JsonIgnore
	public Long getSearchFoundEndTimeCluster() {
		return searchFoundEndTimeCluster;
	}
	public void setSearchFoundEndTimeCluster(Long searchFoundEndTimeCluster) {
		this.searchFoundEndTimeCluster = searchFoundEndTimeCluster;
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
	public Long getId() {
		return id;
	}




	/** 
	* @param id 要设置的 id 
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
	* @param cfgId 要设置的 cfgId 
	*/
	public void setCfgId(Long cfgId) {
		this.cfgId = cfgId;
	}




	/** 
	* @return foundTime 
	*/
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getFoundTime() {
		if(foundTime ==null && this.foundTimeCluster != null){
			foundTime=new Date(this.foundTimeCluster*1000);
		}
		return foundTime;
	}




	/** 
	* @param foundTime 要设置的 foundTime 
	*/
	public void setFoundTime(Date foundTime) {
		this.foundTime = foundTime;
	}




	/** 
	* @return recvTime 
	*/
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getRecvTime() {
		if(recvTime ==null && this.recvTimeCluster != null){
			recvTime=new Date(this.recvTimeCluster*1000);
		}
		return recvTime;
	}




	/** 
	* @param recvTime 要设置的 recvTime 
	*/
	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}




	/** 
	* @return protocol 
	*/
	public String getProtocol() {
		return protocol;
	}




	/** 
	* @param protocol 要设置的 protocol 
	*/
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}




	/** 
	* @return serverIp 
	*/
	public String getServerIp() {
		return serverIp;
	}




	/** 
	* @param serverIp 要设置的 serverIp 
	*/
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}




	/** 
	* @return clientIp 
	*/
	public String getClientIp() {
		return clientIp;
	}




	/** 
	* @param clientIp 要设置的 clientIp 
	*/
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}




	/** 
	* @return serverPort 
	*/
	public Integer getServerPort() {
		return serverPort;
	}




	/** 
	* @param serverPort 要设置的 serverPort 
	*/
	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}




	/** 
	* @return clientPort 
	*/
	public Integer getClientPort() {
		return clientPort;
	}




	/** 
	* @param clientPort 要设置的 clientPort 
	*/
	public void setClientPort(Integer clientPort) {
		this.clientPort = clientPort;
	}





	/** 
	* @return serviceType 
	*/
	public Integer getServiceType() {
		return serviceType;
	}




	/** 
	* @param serviceType 要设置的 serviceType 
	*/
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}




	/** 
	* @return entranceId 
	*/
	public Long getEntranceId() {
		return entranceId;
	}




	/** 
	* @param entranceId 要设置的 entranceId 
	*/
	public void setEntranceId(Long entranceId) {
		this.entranceId = entranceId;
	}




	/** 
	* @return cljIp 
	*/
	public String getCljIp() {
		return cljIp;
	}




	/** 
	* @param cljIp 要设置的 cljIp 
	*/
	public void setCljIp(String cljIp) {
		this.cljIp = cljIp;
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




	@JsonIgnore
	@XmlTransient
	public Page<T> getPage() {
		if (page == null){
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
		if (sqlMap == null){
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
	public String getDbName(){
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
	
	
	
	 /** 
	* @return searchFoundStartTime 
	*/
	@JsonIgnore
	public String getSearchFoundStartTime() {
		return searchFoundStartTime;
	}




	/** 
	* @param searchFoundStartTime 要设置的 searchFoundStartTime 
	*/
	public void setSearchFoundStartTime(String searchFoundStartTime) {
		this.searchFoundStartTime = searchFoundStartTime;
	}




	/** 
	* @return searchFoundEndTime 
	*/
	@JsonIgnore
	public String getSearchFoundEndTime() {
		return searchFoundEndTime;
	}




	/** 
	* @param searchFoundEndTime 要设置的 searchFoundEndTime 
	*/
	public void setSearchFoundEndTime(String searchFoundEndTime) {
		this.searchFoundEndTime = searchFoundEndTime;
	}




	/** 
	* @return searchCfgId 
	*/
	@JsonIgnore
	public String getSearchCfgId() {
		return searchCfgId;
	}




	/** 
	* @param searchCfgId 要设置的 searchCfgId 
	*/
	public void setSearchCfgId(String searchCfgId) {
		this.searchCfgId = searchCfgId;
	}




	/** 
	* @return searchProtocol 
	*/
	@JsonIgnore
	public String getSearchProtocol() {
		return searchProtocol;
	}




	/** 
	* @param searchProtocol 要设置的 searchProtocol 
	*/
	public void setSearchProtocol(String searchProtocol) {
		this.searchProtocol = searchProtocol;
	}




	/** 
	* @return searchServerIp 
	*/
	@JsonIgnore
	public String getSearchServerIp() {
		return searchServerIp;
	}




	/** 
	* @param searchServerIp 要设置的 searchServerIp 
	*/
	public void setSearchServerIp(String searchServerIp) {
		this.searchServerIp = searchServerIp;
	}




	/** 
	* @return searchClientIp 
	*/
	@JsonIgnore
	public String getSearchClientIp() {
		return searchClientIp;
	}


	/** 
	* @param searchClientIp 要设置的 searchClientIp 
	*/
	public void setSearchClientIp(String searchClientIp) {
		this.searchClientIp = searchClientIp;
	}




	/** 
	* @return searchEntranceId 
	*/
	@JsonIgnore
	public String getSearchEntranceId() {
		return searchEntranceId;
	}


	/** 
	* @param searchEntranceId 要设置的 searchEntranceId 
	*/
	public void setSearchEntranceId(String searchEntranceId) {
		this.searchEntranceId = searchEntranceId;
	}




	/** 
	* @return searchCljIp 
	*/
	@JsonIgnore
	public String getSearchCljIp() {
		return searchCljIp;
	}




	/** 
	* @param searchCljIp 要设置的 searchCljIp 
	*/
	
	public void setSearchCljIp(String searchCljIp) {
		this.searchCljIp = searchCljIp;
	}


	/** 
	* @return searchServiceType 
	*/
	@JsonIgnore
	public String getSearchServiceType() {
		return searchServiceType;
	}

	/** 
	* @param searchServiceType 要设置的 searchServiceType 
	*/
	public void setSearchServiceType(String searchServiceType) {
		this.searchServiceType = searchServiceType;
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

    public Integer getStreamType() {
        return streamType;
    }

    public void setStreamType(Integer streamType) {
        this.streamType = streamType;
    }

    public String getNestAddrList() {
        return nestAddrList;
    }

    public void setNestAddrList(String nestAddrList) {
        this.nestAddrList = nestAddrList;
    }

    @JsonIgnore
    public Integer getSearchDirection() {
        return searchDirection;
    }

    public void setSearchDirection(Integer searchDirection) {
        this.searchDirection = searchDirection;
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
	 
}
