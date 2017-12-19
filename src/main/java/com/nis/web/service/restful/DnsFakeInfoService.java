/**
 *@Title: DnsFakeInfoService.java
 *@Package com.nis.web.service.restful 
 *@Description TODO
 *@author wx
 *@date 2016年9月7日 下午3:28:44
 *@version  版本号
 */
package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DnsFakeInfo;
import com.nis.util.Constants;
import com.nis.web.dao.DnsFakeInfoDao;
import com.nis.web.service.CrudService;

/**
 * @ClassName: DnsFakeInfoService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class DnsFakeInfoService extends CrudService<DnsFakeInfoDao, DnsFakeInfo> {
	@Autowired
	public DnsFakeInfoDao dnsFakeInfoDao;
	public Page<DnsFakeInfo> getDnsFakeInfo(Page<DnsFakeInfo> page, DnsFakeInfo entity) {
		return findPage(page, entity);
	}
	
	public DnsFakeInfo findById(long id) {
		return get(id);
	}
	public void saveDnsFakeInfoBatch(List<DnsFakeInfo> entity) {
		// TODO Auto-generated method stub
		super.saveBatch(entity,DnsFakeInfoDao.class);
	}
	public void updateDnsFakeInfoBatch(List<DnsFakeInfo> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, DnsFakeInfoDao.class);
	}
	public void removeDnsFakeInfo(long id) {
		dnsFakeInfoDao.delete(id);
	}
	public void removeDnsFakeInfoBatch(List<DnsFakeInfo> entity) {
		super.deleteBatch(entity, DnsFakeInfoDao.class);
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
		return dnsFakeInfoDao.isValid(id)==Integer.parseInt(Constants.YES);
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
	public boolean isValid(List<DnsFakeInfo> entity){
		boolean vaild=false;
		List<Long> idsList=new ArrayList<Long>(); 
		for(DnsFakeInfo DnsFakeInfo : entity){
			idsList.add(DnsFakeInfo.getId());
			if(idsList.size()%1000==0&&idsList.size()!=0){
				int count=dnsFakeInfoDao.isValidBatch(idsList,Integer.parseInt(Constants.YES));
				idsList.clear();
				if(count>0){
					vaild=true;
					break;
				}
			}
		}
		if(idsList.size()>0){
			int count=dnsFakeInfoDao.isValidBatch(idsList,Integer.parseInt(Constants.YES));
			idsList.clear();
			if(count>0)vaild=true;
		}
		return vaild;
	}	
}
