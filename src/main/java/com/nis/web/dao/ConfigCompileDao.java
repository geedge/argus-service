package com.nis.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.ConfigCompile;
import com.nis.domain.restful.ConfigCompileTest;
import com.nis.domain.restful.ConfigState;

/**
 * 
 * @ClassName: ConfigCompileDao
 * @Description: TODO(编译配置dao)
 * @author (rkg)
 * @date 2016年9月6日下午8:45:58
 * @version V1.0
 */
@MyBatisDao
public interface ConfigCompileDao {
	/**
	 * 添加编译配置信息,并返回主键id
	 * 
	 * @param configCompileDao
	 *            编译配置对象
	 * @return 主键id
	 */
	public Long saveConfigCompile(ConfigCompile configCompile);

	/**
	 * 根据主键id修改编译配置信息
	 * 
	 * @param configCompileDao
	 *            编译配置对象
	 */
	public void updateConfigCompile(ConfigCompile configCompile);
	
	
	
	/**
	 * 根据compileId设置配置有效或无效
	 * @param configCompile
	 * @param valid
	 */
	public void setCompileInvalid(@Param("compileId")Long compileId,@Param("valid")Integer valid);
	
	
	

	/**
	 * 根据主键compileId查询对应的ConfigCompile
	 * 
	 * @param compileId
	 * @return
	 */
	public ConfigCompile queryConfigCompileById(Long compileId);

	/**
	 * 根据tableName查询是否有对应的配置状态记录
	 * 
	 * @param tableName
	 * @return
	 */
	public List<ConfigState> queryConfigStateByTN(@Param("tableName") String tableName);

	/**
	 * 根据表名更新配置状态操作时间
	 * 
	 * @param tableName
	 * @param opTime
	 */
	public void updateConfigState(@Param("tableName") String tableName, @Param("opTime") Date opTime);

	/**
	 * 新增配置状态
	 * 
	 * @param tableName
	 * @param opTime
	 */
	public void saveConfigState(@Param("tableName") String tableName, @Param("opTime") Date opTime);

	/**
	 * 批量新增配置
	 * 
	 * @param configCompileList
	 */
	public void saveCompileBatch(List<ConfigCompile> configCompileList);

	/**
	 * 
	 * 条件查询配置信息
	 * 
	 * @param configCompile
	 * @return
	 */
	public List<ConfigCompileTest> queryAllCompile(@Param("searchEndTime") String searchEndTime,
			@Param("searchStartTime") String searchStartTime, @Param("service") Long service,
			@Param("compileId") Long compileId);

	/**
	 * 根据主键compileId查询对应的ConfigCompile
	 * 
	 * @param compileIdArr
	 * @return
	 */
	public List<ConfigCompileTest> queryConfigCompileByIdArr(@Param("compileIdArr") String compileIdArr);

	public Long getCompileSeq();

	public Long getGroupSeq();

	public Long getRegionSeq();
}