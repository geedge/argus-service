/**
 *@Title: DmbCkSource.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月9日 上午9:53:04
 *@version  版本号
 */
package com.nis.domain.restful.jk;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.nis.domain.restful.ConfigCommonSource;

/**
 * @ClassName: DmbCkSource.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月9日 上午9:53:04
 * @version V1.0
 */
@Alias("jkDmbCkSource")
public class JkDmbCkSource extends ConfigCommonSource{

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 262835095511967504L;
	private static final String DEFAULT_VERSION = "1.0";
	private List<JkDmbCk> dmbCkList;
	
	
	/**
	 * 创建一个新的实例 DmbCkSource.
	 *
	 */
	public JkDmbCkSource() {
		super();
		// TODO Auto-generated constructor stub
		this.version=DEFAULT_VERSION;
	}
	public JkDmbCkSource(String version) {
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
	 * dmbCkList
	 * @return  dmbCkList
	*/
	
	public List<JkDmbCk> getDmbCkList() {
		return dmbCkList;
	}
	/**
	 * @param dmbCkList the dmbCkList to set
	 */
	public void setDmbCkList(List<JkDmbCk> dmbCkList) {
		this.dmbCkList = dmbCkList;
	}
}
