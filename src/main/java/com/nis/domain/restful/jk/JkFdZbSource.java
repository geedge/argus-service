/**
 *@Title: JkFdZbSource.java
 *@Package com.nis.domain.restful.gk
 *@Description TODO
 *@author ddm
 *@date 2016年10月20日 上午9:13:04
 *@version  V1.0
 */
package com.nis.domain.restful.jk;

import java.util.Date;
import java.util.List;

import com.nis.domain.restful.ConfigCommonSource;

/**
 * @ClassName: JkFdZbSource.java 
 * @Description: TODO
 * @author (wx)
*@date 2016年10月20日 上午9:13:04
 * @version V1.0
 */
public class JkFdZbSource extends ConfigCommonSource{

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 78331728636991746L;
	
	
	private static final String DEFAULT_VERSION = "1.0";
	private List<JkFdZb> jkFdZbList;
	
	
	/**
	 * 创建一个新的实例 JkFdZbSource.
	 *
	 */
	public JkFdZbSource() {
		super();
		this.version=DEFAULT_VERSION;
	}
	public JkFdZbSource(String version) {
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
	 * jkFdZbList
	 * @return  jkFdZbList
	*/
	
	public List<JkFdZb> getJkFdZbList() {
		return jkFdZbList;
	}
	/**
	 * @param jkFdZbList the jkFdZbList to set
	 */
	public void setJkFdZbList(List<JkFdZb> jkFdZbList) {
		this.jkFdZbList = jkFdZbList;
	}
}
