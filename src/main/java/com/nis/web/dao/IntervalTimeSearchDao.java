/**   
* @Title: IntervalTimeSearchDao.java 
* @Package com.nis.web.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （zbc）  
* @date 2016年9月8日下午8:11:58 
* @version V1.0   
*/
package com.nis.web.dao;

import java.io.Serializable;
import java.util.List;

import com.nis.domain.restful.DjFlowControlStop;

/** 
* @ClassName: IntervalTimeSearchDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (zbc)  
* @date 2016年9月8日下午8:11:58
* @version V1.0
*/
@MyBatisDao
public interface IntervalTimeSearchDao extends CrudDao<Serializable>{

	List<DjFlowControlStop> findFlowControlStopPage(DjFlowControlStop entity);
}
