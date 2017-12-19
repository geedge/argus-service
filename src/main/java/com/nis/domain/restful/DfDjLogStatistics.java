package com.nis.domain.restful;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.StatLogEntity;

/**
 * @ClassName: DfDjLogStatistics
 * @Description: 封堵日志总量统计
 * @author (zbc)
 * @date 2016年11月14日 下午4:00:00
 * @version V1.0
 */
public class DfDjLogStatistics extends StatLogEntity<DfDjLogStatistics> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2664087481901057237L;
	
	
	@Override
	@JsonIgnore
	public Integer getService() {
		return super.getService();
	}

	@Override
	@JsonIgnore
	public Date getStatTime() {
		return super.getStatTime();
	}
	
}
