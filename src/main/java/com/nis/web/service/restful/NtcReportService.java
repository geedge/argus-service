package com.nis.web.service.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.Page;
import com.nis.domain.restful.NtcAttrTypeReport;
import com.nis.domain.restful.NtcDestipCountryReport;
import com.nis.domain.restful.NtcEntranceReport;
import com.nis.domain.restful.NtcLwhhReport;
import com.nis.domain.restful.NtcPzReport;
import com.nis.domain.restful.NtcServiceReport;
import com.nis.domain.restful.NtcSrcipDomesticReport;
import com.nis.domain.restful.NtcTagReport;
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

	public Page<?> findNtcPzReport(Page<NtcPzReport> page, NtcPzReport entity) throws Exception {
		entity.setPage(page);
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
		} else{
			return page;
		}

	}

	public Page<NtcServiceReport> findNtcServiceReport(Page<NtcServiceReport> page, NtcServiceReport entity)
			throws Exception {
		entity.setPage(page);
		page.setList(dao.findNtcServiceReport(entity));
		return page;
	}

	public Page<NtcTagReport> findNtcTagReport(Page<NtcTagReport> page, NtcTagReport entity) throws Exception {
		entity.setPage(page);
		page.setList(dao.findNtcTagReport(entity));
		return page;
	}

	public Page<NtcAttrTypeReport> findNtcAttrTypeReport(Page<NtcAttrTypeReport> page, NtcAttrTypeReport entity)
			throws Exception {
		entity.setPage(page);
		page.setList(dao.findNtcAttrTypeReport(entity));
		return page;
	}

	public Page<NtcLwhhReport> findNtcLwhhReport(Page<NtcLwhhReport> page, NtcLwhhReport entity) throws Exception {
		entity.setPage(page);
		page.setList(dao.findNtcLwhhReport(entity));
		return page;
	}

	public Page<NtcSrcipDomesticReport> findNtcSrcipDomesticReport(Page<NtcSrcipDomesticReport> page,
			NtcSrcipDomesticReport entity) throws Exception {
		entity.setPage(page);
		page.setList(dao.findNtcSrcipDomesticReport(entity));
		return page;
	}

	public Page<NtcDestipCountryReport> findNtcDestipCountryReport(Page<NtcDestipCountryReport> page,
			NtcDestipCountryReport entity) throws Exception {
		entity.setPage(page);
		page.setList(dao.findNtcDestipCountryReport(entity));
		return page;
	}

	public Page<NtcEntranceReport> findNtcEntranceReport(Page<NtcEntranceReport> page, NtcEntranceReport entity)
			throws Exception {
		entity.setPage(page);
		page.setList(dao.findNtcEntranceReport(entity));
		return page;
	}
}
