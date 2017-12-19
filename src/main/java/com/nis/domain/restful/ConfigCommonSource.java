/**   
* @Title: ConfigResource.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月31日 下午8:32:43 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nis.util.JsonDateSerializer;

/** 
* @ClassName: ConfigResource 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (darnell)  
* @date 2016年8月31日 下午8:32:43 
* @version V1.0 
*/
public abstract class ConfigCommonSource implements Serializable {
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 152715081015545848L;
	protected String version;//默认版本1.0
	protected String operator;//操作人员
	protected Date opTime;//操作时间
	protected Integer opAction;//操作行为 1-插入 2-更新 3-删除 4-查询（暂无）
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
	
	
	

}
