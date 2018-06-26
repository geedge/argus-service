package com.nis.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * <p>Title: ExceptionUtils.java</p>
 * <p>Description: 获取异常信息内容</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年3月5日
 *
 */

public class ExceptionUtil {
	public static String getExceptionMsg(Exception e) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream pout = new PrintStream(out);
		e.printStackTrace(pout);
		String msg = new String(out.toByteArray());
		pout.close();
		try {
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return msg;

	}
}
