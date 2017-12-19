package com.nis.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.SysDataDictionaryName;
import com.nis.web.dao.SysDictDao;
import com.nis.web.service.SpringContextHolder;





/**
 * 系统数据字典工具类
 * @author Administrator
 *
 */
public class DictUtils {
	
	private final static SysDictDao dictDao = SpringContextHolder.getBean(SysDictDao.class);
	
	
	public static Map<String, List<SysDataDictionaryItem>> getDictData() {
		
		 Map<String, List<SysDataDictionaryItem>> dictMap = (Map<String, List<SysDataDictionaryItem>>)CacheUtils.get(Constants.CACHE_DICT_MAP);
		 
		 if (StringUtil.isEmpty(dictMap)) {
			 dictMap = Maps.newHashMap();
			 List<SysDataDictionaryName> dicList = dictDao.findAllList(new SysDataDictionaryName());
			 for (SysDataDictionaryName dict : dicList) {
				 dictMap.put(dict.getMark(), dict.getDictItemList());
			 }
			 CacheUtils.put(Constants.CACHE_DICT_MAP, dictMap);
		 }
		
		return dictMap;
	}
	
	/**
	 * 获取词典对应key的所有词条，code:value
	 * @param key
	 * @return
	 */
	public static Map<String,String> getDictOption(String key) {
		List<SysDataDictionaryItem> itemList = getDictData().get(key);
		Map<String, String> itemMap = Maps.newHashMap();
		for (SysDataDictionaryItem item : itemList) {
			itemMap.put(item.getItemCode(), item.getItemValue());
		}
		
		return itemMap;
	}
	
	/**
	 * 获取词典对应key的所有词条，value:code
	 * @param key
	 * @return
	 */
	public static Map<String,String> getDictOptionInReversion(String key) {
		List<SysDataDictionaryItem> itemList = getDictData().get(key);
		Map<String, String> itemMap = Maps.newHashMap();
		for (SysDataDictionaryItem item : itemList) {
			itemMap.put(item.getItemValue(), item.getItemCode());
		}
		
		return itemMap;
	}
	
	
	
	public static List<SysDataDictionaryItem> getDictList(String key){
		 List<SysDataDictionaryItem>  itemList = getDictData().get(key);
		 if (StringUtil.isEmpty(itemList)) {
			 itemList = Lists.newArrayList();
		 }
		return itemList;
	}
	
	public static String getDictLabels(String dictKey, String itemCodes, String defaultValue){
		
		Map<String, String> itemMap = getDictOption(dictKey);
		
		if (!StringUtil.isEmpty(itemMap)) {
			List<String> valueList = Lists.newArrayList();
			for (String itemCode : StringUtils.split(itemCodes, ",")){
				valueList.add(itemMap.get(itemCode));
			}
			return StringUtils.join(valueList, ",");
			
		}
		
		return defaultValue;
	}
	
	
	/**
	 * 获取字典词条标签
	 * @param value
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	public static String getDictLabel(String dictKey, String itemCode, String defaultValue){
		String itemLabel = getDictOption(dictKey).get(itemCode);
		
		return StringUtil.isBlank(itemLabel) ? defaultValue : itemLabel;
	}
	
	public static String getDictLabel(String dictKey, String itemCode){
		
		return getDictLabel(dictKey, itemCode, "默认");
	}
	
	
	
	public static String getDictCode(String dictKey, String itemValue, String defaultValue){
		String itemCode = getDictOptionInReversion(dictKey).get(itemValue);
		
		return StringUtil.isBlank(itemCode) ? defaultValue : itemCode;
	}
	
	
	public static String getDictCode(String dictKey, String itemValue){
		return getDictCode(dictKey, itemValue, "默认");
	}
	
	
	/**
	 * 返回字典列表（JSON）
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String key){
		return JsonMapper.toJsonString(getDictList(key));
	}
	
	
	

}
