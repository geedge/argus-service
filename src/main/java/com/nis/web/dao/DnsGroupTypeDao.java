/**
 *@Title: DnsGroupTypeDao.java
 *@Package com.nis.web.dao 
 *@Description TODO
 *@author dell
 *@date 2016年9月7日 下午3:14:24
 *@version  版本号
 */
package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.DnsGroupType;

/**
 * @ClassName: DnsGroupTypeDao.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月7日 下午3:14:24
 * @version V1.0
 */
@MyBatisDao
public interface DnsGroupTypeDao extends CrudDao<DnsGroupType> {
	List<DnsGroupType> getDnsGroupType(DnsGroupType DnsGroupType);
	void save(DnsGroupType DnsGroupType);
	void saveBatch(List<DnsGroupType> DnsGroupTypeList); 
	void updateBatch(List<DnsGroupType> DnsGroupTypeList); 
	void delete(long id);
	int isValid(long id);
	int  isValidBatch(@Param("idsList")List<Long> idsList,@Param("isValid")int isValid);
	int isGroupIdUnique(@Param("idsList")List<Integer> idsList);
}
