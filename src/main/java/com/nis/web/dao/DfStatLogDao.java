/**   
* @Title: DfStatLogDao.java 
* @Package com.nis.web.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月13日 上午11:21:31 
* @version V1.0   
*/
package com.nis.web.dao;

import java.util.List;

import com.nis.domain.LogEntity;
import com.nis.domain.restful.DfAttrStatLogDaily;
import com.nis.domain.restful.DfAttrStatLogMonth;
import com.nis.domain.restful.DfDestIpCounrtyStatLogDaily;
import com.nis.domain.restful.DfDestIpCounrtyStatLogMonth;
import com.nis.domain.restful.DfEntrStatLogDaily;
import com.nis.domain.restful.DfEntrStatLogMonth;
import com.nis.domain.restful.DfLwhhStatLogDaily;
import com.nis.domain.restful.DfLwhhStatLogMonth;
import com.nis.domain.restful.DfSrcIpDomesticStatLogDaily;
import com.nis.domain.restful.DfSrcIpDomesticStatLogMonth;
import com.nis.domain.restful.DfStatLogDaily;
import com.nis.domain.restful.DfStatLogMonth;
import com.nis.domain.restful.DfTagStatLogDaily;
import com.nis.domain.restful.DfTagStatLogMonth;

/** 
* @ClassName: DfStatLogDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author (ddm)  
* @date 2016年9月13日 上午11:21:31 
* @version V1.0 
*/
@MyBatisDao
public interface DfStatLogDao extends CrudDao<LogEntity> {
	
	List<DfStatLogDaily> dfStatLogDaily(DfStatLogDaily daily);
	List<DfStatLogMonth> dfStatLogMonth(DfStatLogMonth mongth);
	List<DfTagStatLogDaily> dfTagStatLogDaily(DfTagStatLogDaily daily);
	List<DfTagStatLogMonth> dfTagStatLogMonth(DfTagStatLogMonth mongth);
	List<DfAttrStatLogDaily> dfAttrStatLogDaily(DfAttrStatLogDaily daily);
	List<DfAttrStatLogMonth> dfAttrStatLogMonth(DfAttrStatLogMonth mongth);
	List<DfEntrStatLogDaily> dfEntrStatLogDaily(DfEntrStatLogDaily daily);
	List<DfEntrStatLogMonth> dfEntrStatLogMonth(DfEntrStatLogMonth mongth);
	List<DfLwhhStatLogDaily> dfLwhhStatLogDaily(DfLwhhStatLogDaily daily);
	List<DfLwhhStatLogMonth> dfLwhhStatLogMonth(DfLwhhStatLogMonth mongth);
	List<DfSrcIpDomesticStatLogDaily> dfSrcIpDomesticStatLogDaily(DfSrcIpDomesticStatLogDaily daily);
	List<DfSrcIpDomesticStatLogMonth> dfSrcIpDomesticStatLogMonth(DfSrcIpDomesticStatLogMonth mongth);
	List<DfDestIpCounrtyStatLogDaily> dfDestIpCounrtyStatLogDaily(DfDestIpCounrtyStatLogDaily daily);
	List<DfDestIpCounrtyStatLogMonth> dfDestIpCounrtyStatLogMonth(DfDestIpCounrtyStatLogMonth mongth);
	
}
