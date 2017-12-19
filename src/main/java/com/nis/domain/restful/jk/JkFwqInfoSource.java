/**
 *@Title: JkFwqInfoSource.java
 *@Package com.nis.domain.restful 
 *@Description TODO
 *@author dell
 * @date 2016年10月19日 下午20:04:16
 *@version  版本号
 */
package com.nis.domain.restful.jk;

import java.util.List;

import com.nis.domain.restful.ConfigCommonSource;

/**
 * @ClassName: JkFwqInfoSource.java 
 * @Description: TODO
 * @author (zbc)
 * @date 2016年10月19日 下午20:04:16
 * @version V1.0
 */
public class JkFwqInfoSource  extends ConfigCommonSource {

	/**
	 *  @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -2670423354555878411L;
	
	private static final String DEFAULT_VERSION = "1.0";
	private List<JkFwqInfo> jkFwqList;

	
	public JkFwqInfoSource() {
		super();
		super.version = DEFAULT_VERSION;
	}
	
	public JkFwqInfoSource(String version) {
		super();
		super.version = version;
	}
	
	
	
	public List<JkFwqInfo> getJkFwqInfoList() {
		return jkFwqList;
	}
	
	public void setJkFwqList(List<JkFwqInfo> jkFwqList) {
		this.jkFwqList = jkFwqList;
	}
}
