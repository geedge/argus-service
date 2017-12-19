package com.nis.domain;

import java.io.Serializable;


public class SysDataDictionaryItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String itemCode;
	private String itemValue;
	private String itemDesc;
	private int itemSort;
	private SysDataDictionaryName sysDataDictionaryName;
	private int dictionaryId;
	
	public int getItemSort() {
		return itemSort;
	}
	public void setItemSort(int itemSort) {
		this.itemSort = itemSort;
	}
	private int status;
	
	private int type;
	
	public Long getId() {
		return id;
	}
	public String getItemCode() {
		return itemCode;
	}
	public String getItemValue() {
		return itemValue;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public int getStatus() {
		return status;
	}
	public int getType() {
		return type;
	}
	public SysDataDictionaryName getSysDataDictionaryName() {
		return sysDataDictionaryName;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setSysDataDictionaryName(SysDataDictionaryName sysDataDictionaryName) {
		this.sysDataDictionaryName = sysDataDictionaryName;
	}
	public int getDictionaryId() {
		return dictionaryId;
	}
	public void setDictionaryId(int dictionaryId) {
		this.dictionaryId = dictionaryId;
	}


}
