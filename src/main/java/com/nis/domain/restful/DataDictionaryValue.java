package com.nis.domain.restful;

import java.io.Serializable;

import com.nis.domain.Page;

public class DataDictionaryValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8485746474419535801L;
	private Long dictValueId;
	private Long dictNameId;
	private String dataDictValue;
	private Long isValid;
	/**
	 * 界面查询需要的字段,不新建vo了直接放到这个po里
	 */
	private String dataDictName;
	private Integer pagesNo; // 当前页码
	private Integer pagesSize;// 每页显示条数
	private Long pagesCount;// 总记录数，设置为“-1”表示不查询总数
	private Page<DataDictionaryValue> page;

	public DataDictionaryValue() {
		super();
	}

	public DataDictionaryValue(Long dictValueId) {
		super();
		this.dictValueId = dictValueId;
	}

	public DataDictionaryValue(Long dictValueId, Long isValid) {
		super();
		this.dictValueId = dictValueId;
		this.isValid = isValid;
	}

	public DataDictionaryValue(Long dictNameId, String dataDictValue) {
		super();
		this.dictNameId = dictNameId;
		this.dataDictValue = dataDictValue;
	}

	public DataDictionaryValue(Long dictValueId, Long dictNameId, String dataDictValue) {
		super();
		this.dictValueId = dictValueId;
		this.dictNameId = dictNameId;
		this.dataDictValue = dataDictValue;
	}

	public Long getDictValueId() {
		return dictValueId;
	}

	public void setDictValueId(Long dictValueId) {
		this.dictValueId = dictValueId;
	}

	public Long getDictNameId() {
		return dictNameId;
	}

	public void setDictNameId(Long dictNameId) {
		this.dictNameId = dictNameId;
	}

	public String getDataDictValue() {
		return dataDictValue;
	}

	public void setDataDictValue(String dataDictValue) {
		this.dataDictValue = dataDictValue;
	}

	public String getDataDictName() {
		return dataDictName;
	}

	public void setDataDictName(String dataDictName) {
		this.dataDictName = dataDictName;
	}

	public Integer getPagesNo() {
		return pagesNo;
	}

	public void setPagesNo(Integer pagesNo) {
		this.pagesNo = pagesNo;
	}

	public Integer getPagesSize() {
		return pagesSize;
	}

	public void setPagesSize(Integer pagesSize) {
		this.pagesSize = pagesSize;
	}

	public Long getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(Long pagesCount) {
		this.pagesCount = pagesCount;
	}

	public Page<DataDictionaryValue> getPage() {
		return page;
	}

	public void setPage(Page<DataDictionaryValue> page) {
		this.page = page;
	}

	public Long getIsValid() {
		return isValid;
	}

	public void setIsValid(Long isValid) {
		this.isValid = isValid;
	}

}
