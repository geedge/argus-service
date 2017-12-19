/**
 *@Title: JkFfjInfoSource.java
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
 * @ClassName: JkFfjInfoSource.java 
 * @Description: TODO
 * @author (zbc)
 * @date 2016年10月19日 下午20:04:16
 * @version V1.0
 */
public class JkFfjInfoSource extends ConfigCommonSource {
	
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 8841765964336072965L;
	
	
	private static final String DEFAULT_VERSION = "1.0";
	private List<JkFfjInfo> jkffjList;
	
	public JkFfjInfoSource() {
		super();
		super.version=DEFAULT_VERSION;
	}
	
	public JkFfjInfoSource(String version) {
		super();
		super.version = version;
	}
	
	
	public List<JkFfjInfo> getJkffjList() {
		return jkffjList;
	}
	
	public void setJkffjList(List<JkFfjInfo> jkffjList) {
		this.jkffjList = jkffjList;
	}
}
