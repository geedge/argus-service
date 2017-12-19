/**   
* @Title: DfSrcipDomesticStatLogDaily.java 
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
* @ClassName: DfSrcipDomesticStatLogDaily 
* @Description: 天日志报表
* @author (ddm)  
* @date 2016年9月13日 上午11:08:11 
* @version V1.0 
*/
public class DfSrcIpDomesticStatLogDaily extends StatLogEntity {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -591616210162791616L;
	
	@ApiModelProperty(value="省", required=true)
	protected String srcProvince;

	@ApiModelProperty(value="市", required=true)
	protected String srcCity;

	public String getSrcProvince() {
		return srcProvince;
	}

	public void setSrcProvince(String srcProvince) {
		this.srcProvince = srcProvince;
	}

	public String getSrcCity() {
		return srcCity;
	}

	public void setSrcCity(String srcCity) {
		this.srcCity = srcCity;
	}
	
	
}
