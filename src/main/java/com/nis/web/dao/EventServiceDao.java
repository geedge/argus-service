package com.nis.web.dao;

import com.nis.domain.restful.EventKeyProtection;
import com.nis.domain.restful.EventMonitorOrBlock;

import java.util.List;

/**
 * @author GouGe
 * @data 2018/12/24 17:00
 */
@MyBatisDao
public interface EventServiceDao {

    List<EventKeyProtection> getEventsKeyProtection(EventKeyProtection eventKeyProtection);

    List<EventMonitorOrBlock> getEventsMonitorOrBlock(EventMonitorOrBlock eventMonitorOrBlock);

}
