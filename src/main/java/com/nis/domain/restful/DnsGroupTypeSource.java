/**
 *@Title: DnsGroupTypeSource.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月7日 下午3:32:18
 *@version  版本号
 */
package com.nis.domain.restful;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: DnsGroupTypeSource.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:32:18
 * @version V1.0
 */
public class DnsGroupTypeSource extends ConfigCommonSource{
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 4829973369982149866L;
	private static final String DEFAULT_VERSION = "1.0";
	private List<DnsGroupType> dnsGroupTypeList;
	
	
	/**
	 * 创建一个新的实例 DnsGroupTypeSource.
	 *
	 */
	public DnsGroupTypeSource() {
		super();
		// TODO Auto-generated constructor stub
		this.version=DEFAULT_VERSION;
	}
	public DnsGroupTypeSource(String version) {
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
	 * dnsResponseStrategyList
	 * @return  dnsResponseStrategyList
	*/
	/**
	 * dnsGroupTypeList
	 * @return  dnsGroupTypeList
	*/
	
	public List<DnsGroupType> getDnsGroupTypeList() {
		return dnsGroupTypeList;
	}
	/**
	 * @param dnsGroupTypeList the dnsGroupTypeList to set
	 */
	public void setDnsGroupTypeList(List<DnsGroupType> dnsGroupTypeList) {
		this.dnsGroupTypeList = dnsGroupTypeList;
	}
	
}
