/**
 *@Title: DnsGroupTypeService.java
 *@Package com.nis.web.service.restful 
 *@Description TODO
 *@author dell
 *@date 2016年9月7日 下午3:28:44
 *@version  版本号
 */
package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DnsGroupType;
import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;
import com.nis.util.Constants;
import com.nis.web.dao.DnsGroupTypeDao;
import com.nis.web.service.CrudService;
import com.nis.web.service.SaveRequestLogThread;

/**
 * @ClassName: DnsGroupTypeService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class DnsGroupTypeService extends CrudService<DnsGroupTypeDao, DnsGroupType> {
	@Autowired
	public DnsGroupTypeDao dnsGroupTypeDao;
	public Page<DnsGroupType> getDnsGroupType(Page<DnsGroupType> page, DnsGroupType entity) {
		return findPage(page, entity);
	}
	
	public DnsGroupType findById(long id) {
		return get(id);
	}
	public void saveDnsGroupTypeBatch(SaveRequestLogThread thread,long start,List<DnsGroupType> entity) {
		// TODO Auto-generated method stub
		List<Integer> idsList=new ArrayList<>();
		for(DnsGroupType e:entity){
			if(e.getGroupId()!=null)
			idsList.add(e.getGroupId());
		}
		if(idsList.size()>0){
		boolean unique=this.groupIdValidation(idsList);
			if(!unique)
				throw new RestServiceException(thread, System.currentTimeMillis(), "分组号不唯一", RestBusinessCode.not_unique.getValue());
		}
		super.saveBatch(entity,DnsGroupTypeDao.class);
	}
	public void updateDnsGroupTypeBatch(SaveRequestLogThread thread,long start,List<DnsGroupType> entity) {
		// TODO Auto-generated method stub
//		List<Integer> idsList=new ArrayList<>();
//		for(DnsGroupType e:entity){
//			if(e.getGroupId()!=null)
//			idsList.add(e.getGroupId());
//		}
//		if(idsList.size()>0){
//			boolean unique=this.groupIdValidation(idsList);
//			if(!unique)
//				throw new RestServiceException(thread, System.currentTimeMillis(), "分组号不唯一", RestBusinessCode.not_unique.getValue());
//
//		}
			super.updateBatch(entity, DnsGroupTypeDao.class);
	}
	public void removeDnsGroupType(long id) {
		dnsGroupTypeDao.delete(id);
	}
	public void removeDnsGroupTypeBatch(List<DnsGroupType> entity) {
		super.deleteBatch(entity, DnsGroupTypeDao.class);
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
		return dnsGroupTypeDao.isValid(id)==Integer.parseInt(Constants.YES);
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
	public boolean isValid(List<DnsGroupType> entity){
		boolean vaild=false;
		List<Long> idsList=new ArrayList<Long>(); 
		for(DnsGroupType dnsGroupType : entity){
			idsList.add(dnsGroupType.getId());
			if(idsList.size()%1000==0&&idsList.size()!=0){
				int count=dnsGroupTypeDao.isValidBatch(idsList,Integer.parseInt(Constants.YES));
				idsList.clear();
				if(count>0){
					vaild=true;
					break;
				}
			}
		}
		if(idsList.size()>0){
			int count=dnsGroupTypeDao.isValidBatch(idsList,Integer.parseInt(Constants.YES));
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
	public boolean groupIdValidation(List<Integer> idsList){
		int count=0;
		if(idsList.size()>=1000){
			int i=0;
			for(;i<idsList.size()/1000;i++){
				List<Integer> subList=idsList.subList(i*1000,(i+1)*1000);
				count=dnsGroupTypeDao.isGroupIdUnique(subList);
				if(count>0){
					return false;
				}
			}
			if(idsList.size()>(i*1000)){
				List<Integer> subList=idsList.subList(i*1000,idsList.size());
				count=dnsGroupTypeDao.isGroupIdUnique(subList);
			}
		}else{
			count=dnsGroupTypeDao.isGroupIdUnique(idsList);
		}
		return count==0;
	}
}
