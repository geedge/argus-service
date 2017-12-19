/**   
* @Title: DfKeyConvertUrlDao.java 
* @Package com.nis.web.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （DDM）  
* @date 2016年9月27日下午9:59:58
* @version V1.0   
*/
package com.nis.web.dao;

import java.io.Serializable;
import java.util.List;

import com.nis.domain.restful.DfKeyConvertUrl;
import com.nis.domain.restful.DjFlowControlStop;

/** 
* @ClassName: DfKeyConvertUrlDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author （DDM）  
* @date 2016年9月27日下午9:59:58
* @version V1.0
*/
@MyBatisDao
public interface DfKeyConvertUrlDao extends CrudDao<Serializable>{

	List<DfKeyConvertUrl> findDfKeyConvertUrlPage(DfKeyConvertUrl entity);
}
