package com.nis.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.SysDataDictionaryName;
import com.nis.util.DateUtil;
import com.nis.util.StringUtil;
import com.nis.web.dao.SysDictDao;
import com.nis.web.security.UserUtils;

@Service
public class DictService extends BaseService {
	/**@Resource**/
	@Autowired
	private SysDictDao dictDao;
	

	public Page<SysDataDictionaryName> findDictList(Page<SysDataDictionaryName> page, SysDataDictionaryName sysDictName) {
		// 设置分页参数 
		sysDictName.setPage(page);
		// 执行分页查询
		page.setList(dictDao.findDictList(sysDictName));
		return page;
	}
	
	
	public void saveOrUpdate(SysDataDictionaryName sysDictName) {
		
		
		
		if(StringUtil.isEmpty(sysDictName.getId())) {//新增
			
			sysDictName.setCreateTime(new Date());
			sysDictName.setModifyTime(new Date());
			sysDictName.setStatus(1);
			dictDao.insertDictName(sysDictName);
			
			List<SysDataDictionaryItem> dictItemList = sysDictName.getDictItemList();
			if(!StringUtil.isEmpty(dictItemList) && dictItemList.size()>0) {
				for (SysDataDictionaryItem dictItem : dictItemList) {
					dictItem.setDictionaryId(sysDictName.getId().intValue());
					dictDao.insertDictItem(dictItem);
				}
			}
			
		}else {//修改
			
			//累加修改记录
			String newRevision = "用户"+UserUtils.getUser().getName()+",在"+DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD_HH24_MM_SS)+"修改！";
			StringBuilder revisionBuilder = new StringBuilder(newRevision);
			String oldRevision = sysDictName.getRevision();
			if(!StringUtil.isBlank(oldRevision)){
				if(oldRevision.split("\\|").length<10){
					revisionBuilder.append("|").append(oldRevision);
				}else {
					revisionBuilder.append("|").append(oldRevision.substring(0,oldRevision.lastIndexOf("|")));
				}
				
			}
			sysDictName.setRevision(revisionBuilder.toString());
			sysDictName.setModifyTime(new Date());
			sysDictName.setStatus(1);
			dictDao.updateDictName(sysDictName);
			
			
			dictDao.deleteDictItem(sysDictName.getId().intValue());//删除所有词条项信息
			
			List<SysDataDictionaryItem> dictItemList = sysDictName.getDictItemList();
			if(!StringUtil.isEmpty(dictItemList) && dictItemList.size()>0) {
				for (SysDataDictionaryItem dictItem : dictItemList) {
					dictItem.setDictionaryId(sysDictName.getId().intValue());
					dictDao.insertDictItem(dictItem);
				}
			}
			
			
		}
		
		
		
		
	}
	
	
	public void deleteDict(Integer dictId) {
		
		//删除词典项
		dictDao.deleteDictItem(dictId);
		//删除词典标识
		dictDao.deleteDictName(dictId);
		
	}
	

	public SysDataDictionaryName getDictByIdWithRelation(Integer id) {
		
		return dictDao.getDictById(1,id);
	}
	
	

}
