package com.nis.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断关键字的内容不能包含空格、tab、回车等不可见字符，即ANSII范围0x00至0x1F(0-31)及0x7F(127)。
 * 
 * @author RenKaiGe-Office
 *
 */
public class AsciiJudge {

	public static void main(String[] args) {
		String str = "fdsf你说说你发的是佛山东方啥的飞sdf    啥打法是否(\\&)";
		boolean bool = asciiControlChar(str);
		System.out.println(bool);
	}

	/**
	 * 判断字符串是否包含控制字符
	 * 
	 * @param str
	 *            需要验证的字符串,可以为空字符串但是不能为null
	 * @return true代表包含控制字符,false代表不是控制字符或为null
	 */
	public static boolean asciiControlChar(String str) {
		if (null != str) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < 32; i++) {
				list.add(String.valueOf(i));
			}
			list.add("127");
			char[] charArr = str.toCharArray();
			for (char c : charArr) {
				String num = Integer.valueOf(c).toString();
				if (list.contains(num)) {
					return true;

				}
			}
		}
		return false;
	}
}
