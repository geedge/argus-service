package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.NumRegion;

/**
 * 
 * @ClassName: NumRegionDao
 * @Description: TODO(数值类域配置dao)
 * @author (rkg)
 * @date 2016年9月6日下午8:50:11
 * @version V1.0
 */
@MyBatisDao
public interface NumRegionDao {

	/**
	 * 保存数值类域配置信息,并返回主键id
	 * 
	 * @param numRegion
	 *            数值类域配置
	 * @return
	 */
	public Long saveNumRegion(NumRegion numRegion);

	/**
	 * 根据主键id修改数值类域配置信息
	 * 
	 * @param numRegion
	 *            数值类域配置
	 */
	public void updateNumRegion(NumRegion numRegion);

	/**
	 * 根据主键regionId查询NumRegion
	 * 
	 * @param numRegion
	 * @return
	 */
	public NumRegion queryNumRegionById(NumRegion numRegion);

	/**
	 * 根据groupId获取该分组下所有的数值类域配置
	 * 
	 * @param tableName获取表名
	 * @param groupIdArr
	 * @return
	 */
	public List<NumRegion> queryNumRegionByGroupId(@Param("tableName")String tableName,@Param("groupIdArr")Long[] groupIdArr);
	
	
	
	/**
	 * 向数值域配置表中批量添加数据
	 * @param tableName
	 * @param numRegionList
	 */
	public void saveNumRegionBatch(@Param("tableName")String tableName,@Param("numRegionList")List<NumRegion> numRegionList);
}
