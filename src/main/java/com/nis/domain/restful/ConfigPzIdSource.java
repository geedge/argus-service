package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName:ConfigPzIdSource
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2017年9月13日 上午10:39:01
 * @version V1.0
 */
public class ConfigPzIdSource implements Serializable{

	private static final long serialVersionUID = 6435602537565546676L;
	@ApiModelProperty(value="表名",required = true)
	private String sourceName = "CONFIG_COMPILE";
	@ApiModelProperty(value="配置Id数量",required = true)
	private Integer num = 1;
	@ApiModelProperty(value="配置Id列表",required = true)	
	private List<Long> pzIdList;
	/**
	 * 
	 */
	public ConfigPzIdSource() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sourceName
	 * @param num
	 * @param pzIdList
	 */
	public ConfigPzIdSource(String sourceName, Integer num, List<Long> pzIdList) {
		super();
		this.sourceName = sourceName;
		this.num = num;
		this.pzIdList = pzIdList;
	}



	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}


	public List<Long> getPzIdList() {
		return pzIdList;
	}

	public void setPzIdList(List<Long> pzIdList) {
		this.pzIdList = pzIdList;
	}
}
