package com.nis.web.dao;

import java.util.List;

import com.nis.domain.restful.DfDjLogStatistics;
import com.nis.domain.restful.DfDjPzLogStatistics;

/**
 * @ClassName: DfDjLogStatDao
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (zbc)
 * @date 2016年11月14日 下午4:00:00
 * @version V1.0
 */
@MyBatisDao
public interface DfDjLogStatDao {
	
	List<DfDjLogStatistics> findDfLogStatistics(DfDjLogStatistics entity);

	List<DfDjPzLogStatistics> findDfPzLogStatistics(DfDjPzLogStatistics entity);
	
	List<DfDjLogStatistics> findDjLogStatistics(DfDjLogStatistics entity);

	List<DfDjPzLogStatistics> findDjPzLogStatistics(DfDjPzLogStatistics entity);
}
