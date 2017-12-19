package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.DnsFakeInfo;
/**
 * 
 * @ClassName: DnsFakeInfoDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月8日 下午8:31:24
 * @version V1.0
 */
@MyBatisDao
public interface DnsFakeInfoDao extends CrudDao<DnsFakeInfo>{
    int delete(long id);
    int insert(DnsFakeInfo record);
    DnsFakeInfo selectByPrimaryKey(long id);
    int update(DnsFakeInfo record);
    int updateByPrimaryKey(DnsFakeInfo record);
    int isValid(long id);
	int  isValidBatch(@Param("idsList")List<Long> idsList,@Param("isValid")int isValid);
}