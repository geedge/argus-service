/**
 *@Title: DnsFakeIpService.java
 *@Package com.nis.web.service.restful 
 *@Description TODO
 *@author wx
 *@date 2016年9月7日 下午3:28:44
 *@version  版本号
 */
package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.DnsFakeIp;
import com.nis.domain.restful.FakeIpConfigCompile;
import com.nis.domain.restful.FakeIpConfigGroup;
import com.nis.util.Constants;
import com.nis.web.dao.CrudDao;
import com.nis.web.dao.DnsFakeIpDao;
import com.nis.web.dao.FakeIpConfigCompileDao;
import com.nis.web.dao.FakeIpConfigGroupDao;
import com.nis.web.service.CrudService;
import com.nis.web.service.SpringContextHolder;

/**
 * @ClassName: DnsFakeIpService.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class DnsFakeIpService extends CrudService<DnsFakeIpDao, DnsFakeIp> {
	@Autowired
	public DnsFakeIpDao dnsFakeIpDao;
	@Autowired
	public FakeIpConfigCompileDao fakeIpConfigCompileDao;
	@Autowired
	public FakeIpConfigGroupDao fakeIpConfigGroupDao;
	public Page<DnsFakeIp> getDnsFakeIp(Page<DnsFakeIp> page, DnsFakeIp entity) {
		return findPage(page, entity);
	}
	
	public DnsFakeIp findById(long id) {
		return get(id);
	}
	public void saveDnsFakeIpBatch(List<DnsFakeIp> entity) {
		// TODO Auto-generated method stub
		this.saveBatch(entity,DnsFakeIpDao.class);
	}
	public void updateDnsFakeIpBatch(List<DnsFakeIp> entity) {
		// TODO Auto-generated method stub
		super.updateBatch(entity, DnsFakeIpDao.class);
	}
	public void removeDnsFakeIp(long id) {
		dnsFakeIpDao.delete(id);
	}
	public void removeDnsFakeIpBatch(List<DnsFakeIp> entity) {
		super.deleteBatch(entity, DnsFakeIpDao.class);
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
		return dnsFakeIpDao.isValid(id)==Integer.parseInt(Constants.YES);
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
	public boolean isValid(List<DnsFakeIp> entity){
		boolean vaild=false;
		List<Long> idsList=new ArrayList<Long>(); 
		for(DnsFakeIp DnsFakeIp : entity){
			idsList.add(DnsFakeIp.getId());
			if(idsList.size()%1000==0&&idsList.size()!=0){
				int count=dnsFakeIpDao.isValidBatch(idsList,Integer.parseInt(Constants.YES));
				idsList.clear();
				if(count>0){
					vaild=true;
					break;
				}
			}
		}
		if(idsList.size()>0){
			int count=dnsFakeIpDao.isValidBatch(idsList,Integer.parseInt(Constants.YES));
			idsList.clear();
			if(count>0)vaild=true;
		}
		return vaild;
	}

	/* (non-Javadoc)
	 * @see com.nis.web.service.CrudService#saveBatch(java.util.List, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveBatch(List<DnsFakeIp> data, @SuppressWarnings("rawtypes") Class mClass) {
		Date now=new Date();
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
	    SqlSession batchSqlSession = null;
	    try{
	        batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
	        for(int index = 0; index < data.size();index++){
	        	DnsFakeIp t = data.get(index);
	            ((DnsFakeIpDao) batchSqlSession.getMapper(mClass)).insert(t);
	            if(t.getGroupId()==0||t.getGroupId()==1){
	            	//先生成编译，获得编译号
	            	FakeIpConfigCompile fakeIpConfigCompile=new FakeIpConfigCompile();
		            long compileId=fakeIpConfigCompileDao.getCompileId();
		            fakeIpConfigCompile.setLastUpdate(now);
		            System.out.println(compileId);
		            fakeIpConfigCompile.setCompileId(compileId);
		            if(t.getGroupId()==0){
		            	fakeIpConfigCompile.setAction(2);
		            }else if(t.getGroupId()==1)
		            	fakeIpConfigCompile.setAction(1);
		            fakeIpConfigCompile.setOpTime(t.getOpTime());
		            fakeIpConfigCompile.setActiveSys(3);
		            ((FakeIpConfigCompileDao) batchSqlSession.getMapper(FakeIpConfigCompileDao.class)).insert(fakeIpConfigCompile);
		            //生成分组
		            FakeIpConfigGroup fakeIpConfigGroup=new FakeIpConfigGroup();
		            fakeIpConfigGroup.setLastUpdate(now);
		            fakeIpConfigGroup.setGroupId(t.getGroupId());
		            fakeIpConfigGroup.setCompileId(compileId);
		            fakeIpConfigGroup.setOpTime(t.getOpTime());
		            ((FakeIpConfigGroupDao) batchSqlSession.getMapper(FakeIpConfigGroupDao.class)).insert(fakeIpConfigGroup);
	            }
	            
	        }
	        batchSqlSession.commit();
	//	        }catch (Exception e){
	//	        	batchSqlSession.rollback();
	//	            throw e;
	    }finally {
	        if(batchSqlSession != null){
	            batchSqlSession.close();
	        }
	    }
	}
	
}
