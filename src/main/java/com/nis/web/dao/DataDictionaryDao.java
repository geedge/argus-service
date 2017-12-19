package com.nis.web.dao;

import java.util.List;
import com.nis.domain.restful.DataDictionaryName;
import com.nis.domain.restful.DataDictionaryValue;

@MyBatisDao
public interface DataDictionaryDao {
	/**
	 * 获取所有有效的数据字典
	 * 
	 * @return
	 */
	public List<DataDictionaryValue> getAllDictName(DataDictionaryValue dataDictionaryValue);
	/**
	 * 获取所有有效的数据字典
	 * @return
	 */
	public List<DataDictionaryName> selAllDictName();

	/**
	 * 新增数据字典名称并返回数据字典名称
	 * 
	 * @param dataDictionaryName
	 * @return
	 */
	public Long addDictName(DataDictionaryName dataDictionaryName);

	/**
	 * 新增数据字典值
	 * 
	 * @param dataDictionaryValue
	 */
	public void addDictValue(DataDictionaryValue dataDictionaryValue);

	/**
	 * 修改数据字典名称或将该数据字典置为无效
	 * 
	 * @param dataDictionaryName
	 */
	public void updateDictName(DataDictionaryName dataDictionaryName);

	/**
	 * 修改数据字典值
	 * 
	 * @param dataDictionaryValue
	 */
	public void updateDictValue(DataDictionaryValue dataDictionaryValue);
}
