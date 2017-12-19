/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.web.service;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.nis.domain.BaseEntity;
import com.nis.domain.Page;
import com.nis.util.Constants;
import com.nis.web.dao.CrudDao;

/**
 * Service基类
 * @author ThinkGem
 * @version 2014-05-16
 */
public abstract class CrudService<D extends CrudDao<T>, T extends BaseEntity<T>> extends BaseService {
	
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(Long id) {
		return dao.get(id);
	}
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	public void save(T entity) {
		if (entity.getIsNewRecord()){
			dao.insert(entity);
		}else{
			dao.update(entity);
		}
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	public void delete(T entity) {
		dao.delete(entity);
	}
	/**
	 * 
	 * saveBatch(批量全部保存数据)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param data 数据集合
	 * @param mClass 传入的dao.xml里的mapper 
	 * @author wx
	 * @since  1.0.0
	 */
	
	@SuppressWarnings("unchecked")
	public void saveBatch(List<T> data,@SuppressWarnings("rawtypes")Class mClass) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
        SqlSession batchSqlSession = null;
        try{
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            for(int index = 0; index < data.size();index++){
                T t = data.get(index);
                ((CrudDao<T>) batchSqlSession.getMapper(mClass)).insert(t);
            }
            batchSqlSession.commit();
//        }catch (Exception e){
//        	batchSqlSession.rollback();
//            throw e;
        }finally {
            if(batchSqlSession != null){
                batchSqlSession.close();
            }
        }
    }
	/**
	 * 
	 * updateBatch(批量更新全部数据)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param data 数据集合
	 * @param mClass 传入的dao.xml里的mapper 
	 * @author wx
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public void updateBatch(List<T> data,@SuppressWarnings("rawtypes") Class mClass) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
        SqlSession batchSqlSession = null;
        try{
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            for(int index = 0; index < data.size();index++){
                T t = data.get(index);
                ((CrudDao<T>) batchSqlSession.getMapper(mClass)).update(t);
            }
            batchSqlSession.commit();
//        }catch (Exception e){
//        	batchSqlSession.rollback();
//            throw e;
        }finally {
            if(batchSqlSession != null){
                batchSqlSession.close();
            }
        }
    }
	
	/**
	 * 
	 * deleteBatch(批量删除全部数据)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param data 数据集合
	 * @param mClass 传入的dao.xml里的mapper 
	 * @author wx
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public void deleteBatch(List<T> data,@SuppressWarnings("rawtypes") Class mClass) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < data.size();index++){
				T t = data.get(index);
				((CrudDao<T>) batchSqlSession.getMapper(mClass)).delete(t);
			}
			batchSqlSession.commit();
//        }catch (Exception e){
//        	batchSqlSession.rollback();
//            throw e;
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
	
}
