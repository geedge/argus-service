/**   
* @Title: DfConfigCompile.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （darnell）  
* @date 2016年8月29日 下午9:36:28 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName:RegionRepeat
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年8月22日 下午6:57:42
 * @version V1.0
 */
public class GroupReuse implements Serializable {
	private static final long serialVersionUID = 5803814776173252917L;

	@ApiModelProperty(value = "业务ID", required = true)
	private Integer service;

	@ApiModelProperty(value = "字符串域分组列表", access = "", required = true)
	private List<StrRegion> strRegionList;

	@ApiModelProperty(value = "IP域分组列表", required = true)
	private List<IpRegion> ipRegionList;

	@ApiModelProperty(value = "数值域分组列表", required = true)
	private List<NumRegion> numRegionList;
	
	/**
	 * @return service
	 */
	public Integer getService() {
		return service;
	}

	/**
	 * @param service
	 *            要设置的 service
	 */
	public void setService(Integer service) {
		this.service = service;
	}

	
	/**
	 * @return strRegionList
	 */
	public List<StrRegion> getStrRegionList() {
		return strRegionList;
	}

	/**
	 * @param strRegionList
	 *            要设置的 strRegionList
	 */
	public void setStrRegionList(List<StrRegion> strRegionList) {
		this.strRegionList = strRegionList;
	}

	/**
	 * @return ipRegionList
	 */
	public List<IpRegion> getIpRegionList() {
		return ipRegionList;
	}

	/**
	 * @param ipRegionList
	 *            要设置的 ipRegionList
	 */
	public void setIpRegionList(List<IpRegion> ipRegionList) {
		this.ipRegionList = ipRegionList;
	}

	/**
	 * @return numRegionList
	 */
	@ApiModelProperty(value = "数值域分组列表", required = true)
	public List<NumRegion> getNumRegionList() {
		return numRegionList;
	}

	/**
	 * @param numRegionList
	 *            要设置的 numRegionList
	 */
	public void setNumRegionList(List<NumRegion> numRegionList) {
		this.numRegionList = numRegionList;
	}
	
}
