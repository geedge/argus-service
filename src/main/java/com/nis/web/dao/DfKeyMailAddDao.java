package com.nis.web.dao;

import java.io.Serializable;
import java.util.List;

import com.nis.domain.restful.DfKeyMailAdd;

/**
 * @ClassName: DfKeyMailAddDao
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (ZBC)
 * @date 2016年11月09日 下午02:25:00
 * @version V1.0
 */
@MyBatisDao
public interface DfKeyMailAddDao extends CrudDao<Serializable> {

	List<DfKeyMailAdd> findDfKeyMailAddPage(DfKeyMailAdd entity);

}
