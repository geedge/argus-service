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
 * @ClassName: ConfigSourceStartStop
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (darnell)
 * @date 2016年8月31日 下午8:32:43
 * @version V1.0
 */
public class ConfigSourceStartStop extends ConfigCommonSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_VERSION = "2.0";
	private List<ConfigCompile> configCompileStartStopList; // 编译配置列表

	/**
	 * @Title:
	 * @Description: TODO
	 * @param
	 */
	public ConfigSourceStartStop() {
		this.version = DEFAULT_VERSION;
	}

	public ConfigSourceStartStop(String Version) {
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
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getOpTime() {
		return opTime;
	}

	/**
	 * @param opTime 要设置的 opTime
	 */
	@JsonDeserialize(using = JsonDateDeserializer.class)
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

	public List<ConfigCompile> getConfigCompileStartStopList() {
		return configCompileStartStopList;
	}

	public void setConfigCompileStartStopList(List<ConfigCompile> configCompileStartStopList) {
		this.configCompileStartStopList = configCompileStartStopList;
	}

}
