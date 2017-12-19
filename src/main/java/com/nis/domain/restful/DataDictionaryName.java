package com.nis.domain.restful;

import java.io.Serializable;
import java.util.List;

public class DataDictionaryName implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1639528299931243248L;
	private Long dictNameId;
	private String dataDictName;
	private Long isValid;
	private List<DataDictionaryValue> dictValueList;

	public DataDictionaryName() {
		super();
	}

	public DataDictionaryName(Long dictNameId, Long isValid) {
		super();
		this.dictNameId = dictNameId;
		this.isValid = isValid;
	}

	public DataDictionaryName(String dataDictName) {
		super();
		this.dataDictName = dataDictName;
	}

	public DataDictionaryName(Long dictNameId, String dataDictName, List<DataDictionaryValue> dictValueList) {
		super();
		this.dictNameId = dictNameId;
		this.dataDictName = dataDictName;
		this.dictValueList = dictValueList;
	}

	public Long getDictNameId() {
		return dictNameId;
	}

	public void setDictNameId(Long dictNameId) {
		this.dictNameId = dictNameId;
	}

	public String getDataDictName() {
		return dataDictName;
	}

	public void setDataDictName(String dataDictName) {
		this.dataDictName = dataDictName;
	}

	public List<DataDictionaryValue> getDictValueList() {
		return dictValueList;
	}

	public void setDictValueList(List<DataDictionaryValue> dictValueList) {
		this.dictValueList = dictValueList;
	}

	public Long getIsValid() {
		return isValid;
	}

	public void setIsValid(Long isValid) {
		this.isValid = isValid;
	}
}
