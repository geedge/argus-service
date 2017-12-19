package com.nis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class FormatUtils {

	/**
	 * 将List&lt;HashMap&lt;String,Object&gt;&gt;中HashMap的key全部转化为变量形式，<br>
	 * 如{"BRH_ID":"1234","BRH_NAME":"机构"}改为{"brhId":"1234","brhName":"机构"}
	 *
	 * @param list
	 * @return
	 */
	public static List<HashMap<String, Object>> formatHashMapKeyInList(
			List<HashMap<String, Object>> list) {
		List result = new ArrayList();
		// 遍历list后进行格式化列表元素
		if (list!=null){
			for (HashMap<String, Object> h : list) {
				//  将hashMap的key转化为驼峰格式，如{"BRH_ID":"1234"}改为{"brhId":"1234"}
				result.add(formatHashMapKey(h));
			}
		}
		return result;
	}
		
	/**
	 * 将hashMap的key转化为驼峰格式，如{"BRH_ID":"1234","BRH_NAME":"机构"}改为{"brhId":"1234",
	 * "brhName":"机构"}
	 *
	 * @param hashMap
	 * @return
	 */
	public static HashMap<String, Object> formatHashMapKey(
			HashMap<String, Object> hashMap) {
		HashMap result = new HashMap();
		String key = null;
		// 遍历map
		for (Entry<String, Object> e : (Set<Entry<String, Object>>) hashMap.entrySet()) {
			key = (String) e.getKey();
			// 将hashMap的key转化为驼峰格式
			key = formatDBNameToVarName(key);
			// 封装为新的map
			result.put(key, e.getValue());
		}
		// 返回格式化后的map
		return result;
	}

	/**
	 * 数据库列名转化为属性名,如DEAL_ID=dealId; <br>
	 * 不能保证完全正确，如DBUTIL不会智能的转化为DBUtil,而会转化为dbutil, <br>
	 * 规则为全部转化为单词，然后首字母小写
	 *
	 * @param DBName
	 * @return
	 */
	public static String formatDBNameToVarName(String DBName) {
		StringBuilder result = new StringBuilder("");
		// 以"_"分割
		String[] DBNameArr = DBName.split("_");
		for (int i = 0, j = DBNameArr.length; i < j; i++) {
			// 获取以"_"分割后的字符数组的每个元素的第一个字母，
			result.append(DBNameArr[i].charAt(0));
			// 将其他字符转换成小写
			result.append(DBNameArr[i].substring(1).toLowerCase());
		}
		char c0 = result.charAt(0);
		if (c0 >= 'A' && c0 <= 'Z')
			c0 = (char) (c0 + 'a' - 'A');
		result.setCharAt(0, c0);
		return result.toString();
	}

}
