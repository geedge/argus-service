/**
 *@Title: EncryptProtoRandomSource.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月27日 上午8:57:57
 *@version  版本号
 */
package com.nis.domain.restful;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: EncryptProtoRandomSource.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月27日 上午8:57:57
 * @version V1.0
 */
public class EncryptProtoRandomSource extends ConfigCommonSource{

	/**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -4303849898919494205L;
	private static final String DEFAULT_VERSION = "1.0";
	private List<EncryptProtoRandom> encryptProtoRandomList;
	
	/**
	 * 创建一个新的实例 EncryptProtoRandomSource.
	 *
	 */
	public EncryptProtoRandomSource() {
		super();
		this.version=DEFAULT_VERSION;
	}

	
	/**
	 * 创建一个新的实例 EncryptProtoRandomSource.
	 *
	 * @param encryptProtoRandomList
	 */
	public EncryptProtoRandomSource(List<EncryptProtoRandom> encryptProtoRandomList) {
		this.encryptProtoRandomList = encryptProtoRandomList;
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
	 * encryptProtoRandomList
	 * @return  encryptProtoRandomList
	*/
	
	public List<EncryptProtoRandom> getEncryptProtoRandomList() {
		return encryptProtoRandomList;
	}


	/**
	 * @param encryptProtoRandomList the encryptProtoRandomList to set
	 */
	public void setEncryptProtoRandomList(List<EncryptProtoRandom> encryptProtoRandomList) {
		this.encryptProtoRandomList = encryptProtoRandomList;
	}
	
}
