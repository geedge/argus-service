package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;

public class NmsDiRuleList implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<NmsDiRule> nmsDiRuleList;

	public List<NmsDiRule> getNmsDiRuleList() {
		return nmsDiRuleList;
	}

	public void setNmsDiRuleList(List<NmsDiRule> nmsDiRuleList) {
		this.nmsDiRuleList = nmsDiRuleList;
	}

}
