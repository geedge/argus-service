/**   
* @Title: DjStatLogDao.java 
* @Package com.nis.web.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月13日 上午11:21:31 
* @version V1.0   
*/
package com.nis.web.dao;

import java.util.List;

import com.nis.domain.LogEntity;
import com.nis.domain.restful.DjAttrStatLogDaily;
import com.nis.domain.restful.DjAttrStatLogMonth;
import com.nis.domain.restful.DjDestIpCounrtyStatLogDaily;
import com.nis.domain.restful.DjDestIpCounrtyStatLogMonth;
import com.nis.domain.restful.DjEntrStatLogDaily;
import com.nis.domain.restful.DjEntrStatLogMonth;
import com.nis.domain.restful.DjLwhhStatLogDaily;
import com.nis.domain.restful.DjLwhhStatLogMonth;
import com.nis.domain.restful.DjSrcIpDomesticStatLogDaily;
import com.nis.domain.restful.DjSrcIpDomesticStatLogMonth;
import com.nis.domain.restful.DjStatLogDaily;
import com.nis.domain.restful.DjStatLogMonth;
import com.nis.domain.restful.DjTagStatLogDaily;
import com.nis.domain.restful.DjTagStatLogMonth;

/** 
* @ClassName: DjStatLogDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (ddm)  
* @date 2016年9月13日 上午11:21:31 
* @version V1.0 
*/
@MyBatisDao
public interface DjStatLogDao extends CrudDao<LogEntity> {
	
	List<DjStatLogDaily> djStatLogDaily(DjStatLogDaily daily);
	List<DjStatLogMonth> djStatLogMonth(DjStatLogMonth mongth);
	List<DjTagStatLogDaily> djTagStatLogDaily(DjTagStatLogDaily daily);
	List<DjTagStatLogMonth> djTagStatLogMonth(DjTagStatLogMonth mongth);
	List<DjAttrStatLogDaily> djAttrStatLogDaily(DjAttrStatLogDaily daily);
	List<DjAttrStatLogMonth> djAttrStatLogMonth(DjAttrStatLogMonth mongth);
	List<DjEntrStatLogDaily> djEntrStatLogDaily(DjEntrStatLogDaily daily);
	List<DjEntrStatLogMonth> djEntrStatLogMonth(DjEntrStatLogMonth mongth);
	List<DjLwhhStatLogDaily> djLwhhStatLogDaily(DjLwhhStatLogDaily daily);
	List<DjLwhhStatLogMonth> djLwhhStatLogMonth(DjLwhhStatLogMonth mongth);
	List<DjSrcIpDomesticStatLogDaily> djSrcIpDomesticStatLogDaily(DjSrcIpDomesticStatLogDaily daily);
	List<DjSrcIpDomesticStatLogMonth> djSrcIpDomesticStatLogMonth(DjSrcIpDomesticStatLogMonth mongth);
	List<DjDestIpCounrtyStatLogDaily> djDestIpCounrtyStatLogDaily(DjDestIpCounrtyStatLogDaily daily);
	List<DjDestIpCounrtyStatLogMonth> djDestIpCounrtyStatLogMonth(DjDestIpCounrtyStatLogMonth mongth);
	
}
