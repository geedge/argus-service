package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.StrRegion;

/**
 * 
 * @ClassName: StrRegionDao
 * @Description: TODO(字符串类域配置dao)
 * @author (rkg)
 * @date 2016年9月6日下午8:50:23
 * @version V1.0
 */
@MyBatisDao
public interface StrRegionDao {



	/**
	 * 根据主键id修改字符类域配置信息
	 * 
	 * @param strRegion
	 *            字符类域配置
	 */
	public void updateStrRegion(StrRegion strRegion);

	/**
	 * 根据主键regionId查询字符串域配置信息
	 * 
	 * @param strRegion
	 * @return
	 */
	public StrRegion queryStrRegionById(StrRegion strRegion);
	/**
	 * 根据groupId获取该分组下所有的字符串类域配置
	 * @param tableName
	 * @param groupIdArr
	 * @return
	 */
	public List<StrRegion> queryStrRegionByGroupId(@Param("tableName")String tableName,@Param("groupIdArr")Long[] groupIdArr);
	/**
	 * 根据groupId获取该分组下所有的增强字符串类域配置
	 * @param tableName
	 * @param groupIdArr
	 * @return
	 */
	public List<StrRegion> queryStrStrongRegionByGroupId(@Param("tableName")String tableName,@Param("groupIdArr")Long[] groupIdArr);
	
	/**
	 * 向字符串域中批量插入数据
	 * 
	 * @param tableName
	 * @param strRegionList
	 */
	public void saveStrRegionBatch(@Param("tableName") String tableName,
			@Param("strRegionList") List<StrRegion> strRegionList);

	/**
	 * 向增强字符串域中批量插入数据
	 * 
	 * @param tableName
	 * @param strRegionList
	 */
	public void saveStrStrongRegionBatch(@Param("tableName") String tableName,
			@Param("strRegionList") List<StrRegion> strRegionList);
}
