package com.nis.web.dao;

import java.util.List;

import com.nis.domain.restful.ConfigGroupRelation;

/**
 * 
 * @ClassName: ConfigGroupRelationDao
 * @Description: TODO(配置分组Dao)
 * @author (rkg)
 * @date 2016年9月6日下午8:49:21
 * @version V1.0
 */
@MyBatisDao
public interface ConfigGroupRelationDao {
	/**
	 * 添加配置分组信息,并返回主键
	 * 
	 * @param record
	 * @return 主键
	 * @throws Exception
	 */
	public Long saveConfigGroupRelation(ConfigGroupRelation record) ;

	/**
	 * 根据主键Id修改配置分组信息
	 * 
	 * @param record
	 * @throws Exception
	 */
	public void updateConfigGroupRelation(ConfigGroupRelation record) ;

	/**
	 * 根据compileId查询对应的配置分组信息
	 * 
	 * @param compileId
	 * @return
	 */
	public List<ConfigGroupRelation> queryCompileGroupByPID(Long compileId);
	/**
	 * 根据groupid查询是否有对应的有效的编译配置
	 * @param GROUP_ID
	 * @return
	 */
	public List<ConfigGroupRelation> queryCompileGroupByGID(Long GROUP_ID);

	/**
	 * 批量新增表配置
	 * @param configGroupRelationList
	 */
	public void saveGroupBatch(List<ConfigGroupRelation> configGroupRelationList);
}