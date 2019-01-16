package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 
 * @ClassName:IspInfoDao
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (rkg)
 * @date 2019年1月16日 下午5:47:55
 * @version V1.0
 */
@MyBatisDao
public interface IspInfoDao {

	List<String> getIspNum(@Param("ispCode") String ispCode);
}
