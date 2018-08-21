package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.AbnormalMachine;
import com.nis.domain.restful.TrafficNmsServerStatistic;

@MyBatisDao
public interface TrafficNmsServerStatisticDao extends CrudDao<TrafficNmsServerStatistic> {

	public void insertAbnormalMachine(@Param("nmsServerId") Integer nmsServerId,@Param("list")  List<AbnormalMachine> abnormalMachineList);
}