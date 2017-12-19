/**
 *@Title: DnsResponseStrategyDao.java
 *@Package com.nis.web.dao 
 *@Description TODO
 *@author dell
 *@date 2016年9月5日 下午5:45:51
 *@version  版本号
 */
package com.nis.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nis.domain.restful.DnsResponseStrategy;

/**
 * @ClassName: DnsResponseStrategyDao.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月5日 下午5:45:51
 * @version V1.0
 */
@MyBatisDao
public interface DnsResponseStrategyDao extends CrudDao<DnsResponseStrategy> {
	
	List<DnsResponseStrategy> getDnsResponseStrategy(DnsResponseStrategy dnsResponseStrategy);
	
	void save(DnsResponseStrategy dnsResponseStrategy);
	void saveBatch(List<DnsResponseStrategy> dnsResponseStrategyList); 
	void updateBatch(List<DnsResponseStrategy> dnsResponseStrategyList); 
	void delete(long id);
	int isValid(long id);
	int  isValidBatch(@Param("idsList")List<Long> idsList,@Param("isValid")int isValid);
	int isReqStrateIdUnique(@Param("idsList")List<Integer> idsList);
}
