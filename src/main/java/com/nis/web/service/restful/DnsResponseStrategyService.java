/**
 *@Title: DnsResponseStrategy.java
 *@Package com.nis.web.controller.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月5日 下午3:37:54
 *@version  1.0
 */
package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DnsResponseStrategy;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.dao.DnsResponseStrategyDao;
import com.nis.web.service.CrudService;
import com.nis.web.service.SaveRequestLogThread;

/**
 * @ClassName: DnsResponseStrategy.java 
 * @Description: TODO
 * @author (dell)
 * @date 2016年9月5日 下午3:37:54
 * @version V1.0
 */
@Service
public class DnsResponseStrategyService extends CrudService<DnsResponseStrategyDao, DnsResponseStrategy> {
	@Autowired
	public DnsResponseStrategyDao dnsResponseStrategyDao;
	public Page<DnsResponseStrategy> getDnsResponseStrategy(Page<DnsResponseStrategy> page, DnsResponseStrategy dnsResponseStrategy) {
		return findPage(page, dnsResponseStrategy);
	}
	
	public DnsResponseStrategy findById(long id) {
		return get(id);
	}
	/**
	 * 
	 * saveDnsResponseStrategyBatch(多条插入)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void saveDnsResponseStrategyBatch(List<DnsResponseStrategy> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,DnsResponseStrategyDao.class);
	}
	/**
	 * 
	 * saveDnsResponseStrategyBatch(多条插入)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void saveDnsResponseStrategyBatch(SaveRequestLogThread thread,long start,List<DnsResponseStrategy> entity) {
		// TODO Auto-generated method stub
		List idsList=new ArrayList<>();
		for(DnsResponseStrategy e:entity){
			if(e.getReqStrateId()!=null)
			idsList.add(e.getReqStrateId());
		}
		if(idsList.size()>0){
			boolean uinque=this.reqStrateIdValidation(idsList);
			if(!uinque){
				throw new RestServiceException(thread, System.currentTimeMillis(), "请求策略号不唯一", RestBusinessCode.not_unique.getValue());
			}
		}
		
		super.saveBatch(entity,DnsResponseStrategyDao.class);
	}
	/**
	 * 
	 * updateDnsResponseStrategyBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void updateDnsResponseStrategyBatch(List<DnsResponseStrategy> entity) {
		// TODO Auto-generated method stub
		
		super.updateBatch(entity, DnsResponseStrategyDao.class);
	}
	/**
	 * 
	 * updateDnsResponseStrategyBatch(多条更新)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void updateDnsResponseStrategyBatch(SaveRequestLogThread thread,long start,List<DnsResponseStrategy> entity) {
//		List idsList=new ArrayList<>();
//		for(DnsResponseStrategy e:entity){
//			if(e.getReqStrateId()!=null)
//			idsList.add(e.getReqStrateId());
//		}
//		if(idsList.size()>0){
//			boolean uinque=this.reqStrateIdValidation(idsList);
//			if(!uinque){
//				throw new RestServiceException(thread, System.currentTimeMillis(), "请求策略号不唯一", RestBusinessCode.not_unique.getValue());
//			}
//		}
		
		// TODO Auto-generated method stub
		super.updateBatch(entity, DnsResponseStrategyDao.class);
	}
	/**
	 * 
	 * removeDnsResponseStrategy(单条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param id 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void removeDnsResponseStrategy(long id) {
		dnsResponseStrategyDao.delete(id);
	}
	/**
	 * 
	 * removeDnsResponseStrategyBatch(多条删除)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void removeDnsResponseStrategyBatch(List<DnsResponseStrategy> entity) {
		super.deleteBatch(entity, DnsResponseStrategyDao.class);
	}
	/**
	 * 
	 * isValid(单条有效验证)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param id
	 * @return 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean isValid(long id){
		return dnsResponseStrategyDao.isValid(id)==Integer.parseInt(Constants.YES);
	}
	/**
	 * 
	 * isValid(多条有效验证)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity
	 * @return 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean isValid(List<DnsResponseStrategy> entity){
		boolean vaild=false;
		List<Long> idsList=new ArrayList<Long>(); 
		for(DnsResponseStrategy dnsResponseStrategy : entity){
			idsList.add(dnsResponseStrategy.getId());
			if(idsList.size()%1000==0&&idsList.size()!=0){
				int count=dnsResponseStrategyDao.isValidBatch(idsList,Integer.parseInt(Constants.YES));
				idsList.clear();
				if(count>0){
					vaild=true;
					break;
				}
			}
		}
		if(idsList.size()>0){
			int count=dnsResponseStrategyDao.isValidBatch(idsList,Integer.parseInt(Constants.YES));
			idsList.clear();
			if(count>0)vaild=true;
		}
		return vaild;
	}
	/**
	 * 验证字段唯一性
	 * reqStrateIdValidation(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return 
	 *boolean
	 * @exception 
	 * @since  1.0.0
	 */
	public boolean reqStrateIdValidation(List<Integer> idsList){
		int count=0;
		if(idsList.size()>=1000){
			int i=0;
			for(;i<idsList.size()/1000;i++){
				List<Integer> subList=idsList.subList(i*1000,(i+1)*1000);
				count=dnsResponseStrategyDao.isReqStrateIdUnique(subList);
				if(count>0){
					return false;
				}
			}
			if(idsList.size()>(i*1000)){
				List<Integer> subList=idsList.subList(i*1000,idsList.size());
				count=dnsResponseStrategyDao.isReqStrateIdUnique(subList);
			}
		}else{
			count=dnsResponseStrategyDao.isReqStrateIdUnique(idsList);
		}
		return count==0;
	}
}
