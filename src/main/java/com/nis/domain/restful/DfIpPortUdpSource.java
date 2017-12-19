/**
 *@Title: DfIpPortUdpSource.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月13日 上午9:12:48
 *@version  版本号
 */
package com.nis.domain.restful;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: DfIpPortUdpSource.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月13日 上午9:12:48
 * @version V1.0
 */
public class DfIpPortUdpSource extends ConfigCommonSource{

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 5423347084696349283L;
	private static final String DEFAULT_VERSION = "1.0";
	private List<DfIpPortUdp> dfIpPortUdpList;
	
	/**
	 * 创建一个新的实例 DfIpPortUdpSource.
	 *
	 */
	public DfIpPortUdpSource() {
		super();
		// TODO Auto-generated constructor stub
		this.version=DEFAULT_VERSION;
	}

	
	/**
	 * 创建一个新的实例 DfIpPortUdpSource.
	 *
	 */
	public DfIpPortUdpSource(String version) {
		this.version=version;
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
	public Date getOpTime() {
		return opTime;
	}

	/** 
	* @param opTime 要设置的 opTime 
	*/
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
	 * dfIpPortUdpList
	 * @return  dfIpPortUdpList
	*/
	
	public List<DfIpPortUdp> getDfIpPortUdpList() {
		return dfIpPortUdpList;
	}


	/**
	 * @param dfIpPortUdpList the dfIpPortUdpList to set
	 */
	public void setDfIpPortUdpList(List<DfIpPortUdp> dfIpPortUdpList) {
		this.dfIpPortUdpList = dfIpPortUdpList;
	}
	
}
