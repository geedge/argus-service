package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcPzReport;
import com.nis.web.dao.NtcReportDao;
import com.nis.web.service.BaseLogService;

/**
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年7月10日 上午9:47:39
 * @version V1.0
 */
@Service
public class NtcReportService extends BaseLogService {

	@Autowired
	protected NtcReportDao dao;

	public Page<?> findNtcPzReport(Page<NtcPzReport> page, NtcPzReport entity)
			throws Exception {
		page.setList(dao.findNtcPzReport(entity));
		if ("1".equals(entity.getSearchBusinessType())) {
			Page<Map> pageTotal = new Page<Map>();
			pageTotal.setPageNo(page.getPageNo());
			pageTotal.setPageSize(page.getPageSize());
			List<Map> restList = new ArrayList<Map>();
			List<NtcPzReport> list = page.getList();
			for (NtcPzReport ntcPzReport : list) {
				Map<String, Long> restMap = new HashMap<String, Long>();
				restMap.put("cfgId", ntcPzReport.getCfgId());
				restMap.put("sum", ntcPzReport.getSum());
				restList.add(restMap);
			}
			pageTotal.setCount(restList.size());
			pageTotal.setList(restList);
			return pageTotal;
		} else if ("2".equals(entity.getSearchBusinessType())) {
			return page;
		}
		return null;

	}

}
