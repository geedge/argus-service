/**
 *@Title: DnsFakeInfoSource.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月7日 下午6:26:06
 *@version  版本号
 */
package com.nis.domain.restful;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: DnsFakeInfoSource.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午6:26:06
 * @version V1.0
 */
public class DnsFakeInfoSource extends ConfigCommonSource{
	
	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -4075912540188044069L;
	private static final String DEFAULT_VERSION = "1.0";
	private List<DnsFakeInfo> dnsFakeInfoList;
	
	
	/**
	 * 创建一个新的实例 DnsFakeInfoSource.
	 *
	 */
	public DnsFakeInfoSource() {
		super();
		// TODO Auto-generated constructor stub
		this.version=DEFAULT_VERSION;
	}
	public DnsFakeInfoSource(String version) {
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
	 * 
	 * getDnsFakeInfoList(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *List<DnsFakeInfo>
	 * @exception 
	 * @since  1.0.0
	 */
	public List<DnsFakeInfo> getDnsFakeInfoList() {
		return dnsFakeInfoList;
	}
	/**
	 * @param dnsGroupTypeList the dnsGroupTypeList to set
	 */
	public void setDnsFakeInfoList(List<DnsFakeInfo> dnsFakeIpList) {
		this.dnsFakeInfoList = dnsFakeIpList;
	}
	
}
