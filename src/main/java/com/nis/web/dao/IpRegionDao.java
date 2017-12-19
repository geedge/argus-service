package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.IpRegion;

/**
 * 
 * @ClassName: IpRegionDao
 * @Description: TODO(ip类域配置dao)
 * @author (rkg)
 * @date 2016年9月6日下午8:49:52
 * @version V1.0
 */
@MyBatisDao
public interface IpRegionDao {

	/**
	 * 保存ip类域配置信息,并返回主键id
	 * 
	 * @param ipRegion
	 *            ip类域配置
	 * @return
	 */
	public Long saveIpRegion(IpRegion ipRegion);

	/**
	 * 根据主键id修改ip类域配置信息
	 * 
	 * @param ipRegion
	 */
	public void updateIpRegion(IpRegion ipRegion);

	/**
	 * 根据主键regionId查询ip域配置
	 * 
	 * @param ipRegion
	 * @return
	 */
	public IpRegion queryIpRegionById(IpRegion ipRegion);
	/**
	 * 根据groupId获取该分组下所有的ip类域配置
	 * @param tableName
	 * @param groupIdArr
	 * @return
	 */
	public List<IpRegion> queryIpRegionByGroupId(@Param("tableName")String tableName,@Param("groupIdArr")Long[] groupIdArr);
	
	
	/**
	 * 往指定ip域表中批量添加数据
	 * @param tableName
	 * @param ipRegionList
	 */
	public void saveIpRegionBatch(@Param("tableName")String tableName,@Param("ipRegionList")List<IpRegion> ipRegionList);
}
