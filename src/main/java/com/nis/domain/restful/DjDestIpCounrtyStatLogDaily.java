/**   
* @Title: DfDestIpCounrtyStatLogDaily.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2017年01月05日 下午07:08:11 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.nis.domain.StatLogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfDestIpCounrtyStatLogDaily 
* @Description: 天日志报表
* @author (ddm)  
* @date 2016年9月13日 上午11:08:11 
* @version V1.0 
*/
public class DjDestIpCounrtyStatLogDaily extends StatLogEntity {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -591616210162791616L;
	
	@ApiModelProperty(value="国家", required=true)
	protected String destCountry;
	
	public String getDestCountry() {
		return destCountry;
	}
	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	
}
