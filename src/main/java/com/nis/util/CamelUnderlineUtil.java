/**
 * 
 */
package com.nis.util;

import com.zdjizhi.utils.StringUtil;

/**
 * @ClassName:CamelUnderlineUtil
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年5月25日 下午4:44:54
 * @version V1.0
 */
public class CamelUnderlineUtil {

	private static final char UNDERLINE ='_';
	public static String camelToUnderline(String param) {
		if (StringUtil.isEmpty(param)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int len = param.length();
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static String underlineToCamel(String param){
		if (StringUtil.isEmpty(param)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int len = param.length();
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c==UNDERLINE) {
				if(++i<len){
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
