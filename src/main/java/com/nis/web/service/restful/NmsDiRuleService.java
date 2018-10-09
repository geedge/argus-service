package com.nis.web.service.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nis.domain.restful.NmsDiRule;
import com.nis.web.dao.NmsDiRuleDao;

@Service
public class NmsDiRuleService {
	@Autowired
	NmsDiRuleDao nmsDiRuleDao;

	@Transactional
	public void saveNmsDiRuleInfo(List<NmsDiRule> nmsDiRuleList) {
		for (NmsDiRule nmsDiRule : nmsDiRuleList) {
			nmsDiRuleDao.insert(nmsDiRule);
		}
	}
}
