package com.nis.domain;

import java.io.Serializable;
import java.util.List;

public class MaatXmlConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer service;
	private List<MaatXmlSeq> sequenceList;
	private List<MaatXmlExpr> expressionList;

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public List<MaatXmlSeq> getSequenceList() {
		return sequenceList;
	}

	public void setSequenceList(List<MaatXmlSeq> sequenceList) {
		this.sequenceList = sequenceList;
	}

	public List<MaatXmlExpr> getExpressionList() {
		return expressionList;
	}

	public void setExpressionList(List<MaatXmlExpr> expressionList) {
		this.expressionList = expressionList;
	}

}
