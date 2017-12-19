package com.nis.domain.restful;

import java.util.List;

/**
 * @ClassName: SystemFunStatusSource.java 
 * @Description: TODO
 * @author (zbc)
 * @date 2016年11月10日 上午09:47:00
 * @version V1.0
 */
public class SystemFunStatusSource extends ConfigCommonSource {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8598830667489468219L;
	private static final String DEFAULT_VERSION = "1.0";
	
	private List<SystemFunStatus> systemFunStatusList;

	
	public SystemFunStatusSource() {
		super();
		super.version = DEFAULT_VERSION;
	}

	
	public List<SystemFunStatus> getSystemFunStatusList() {
		return systemFunStatusList;
	}
	public void setSystemFunStatusList(List<SystemFunStatus> systemFunStatusList) {
		this.systemFunStatusList = systemFunStatusList;
	}
}
