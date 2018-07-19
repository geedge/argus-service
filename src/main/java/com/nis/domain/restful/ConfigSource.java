/**   
* @Title: ConfigSource.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午9:31:24 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateDeserializer;
import com.nis.util.JsonDateSerializer;

/** 
* @ClassName: ConfigSource 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月29日 下午9:31:24 
* @version V1.0 
*/
public  class ConfigSource extends ConfigCommonSource{
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -2567497760237621930L;
	private static final String DEFAULT_VERSION = "1.0";
	private List<ConfigCompile> configCompileList; //编译配置列表
	/**  
	* @Title:  
	* @Description: TODO  
	* @param
	*/
	public ConfigSource() {
		this.version = DEFAULT_VERSION;
	}
	
	public ConfigSource(String Version) {
		this.version = Version;
	}

	/** 
	* @return version 
	*/
	public String getVersion() {
		return version;
	}

	/** 
	* @param version 要设置的 version 
	*/
	public void setVersion(String version) {
		this.version = version;
	}

	/** 
	* @return operator 
	*/
	public String getOperator() {
		return operator;
	}

	/** 
	* @param operator 要设置的 operator 
	*/
	public void setOperator(String operator) {
		this.operator = operator;
	}

	
	/** 
	* @return opTime 
	*/
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}

	/** 
	* @param opTime 要设置的 opTime 
	*/
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	

	/** 
	* @return opAction 
	*/
	public Integer getOpAction() {
		return opAction;
	}

	/** 
	* @param opAction 要设置的 opAction 
	*/
	public void setOpAction(Integer opAction) {
		this.opAction = opAction;
	}

	/** 
	* @return configCompileList 
	*/
	public List<ConfigCompile> getConfigCompileList() {
		return configCompileList;
	}

	/** 
	* @param configCompileList 要设置的 configCompileList 
	*/
	public void setConfigCompileList(List<ConfigCompile> configCompileList) {
		this.configCompileList = configCompileList;
	}

	
	
	
	
	

	
	
	
	
}
