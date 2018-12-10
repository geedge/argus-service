package com.nis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 常用日历操作辅助类
 * 
 */
public class CalendarUtils {
	/**
	 * //小时报2018-08-01 12:00:00 //日报2018-08-15 00:00:00 //月报2018-08-01 00:00:00
	 */
	static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static final SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd HH");
	static final SimpleDateFormat UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	/**
	 * 获得当前小时或者前一个小时 0/-1
	 * 
	 * @throws ParseException
	 */
	public static Date getPrevOneHourTime(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, num);
		String format5 = format4.format(calendar.getTime());
		format5 += ":00:00";
		Date parse = null;
		try {
			parse = format2.parse(format5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parse;
	}

	/**
	 * 获取time时间的前整5分钟的时间点 eg: 2018-12-12 12:06:36 结果为：2018-12-12 12:05:00
	 * 2018-12-12 12:04:36 结果为：2018-12-12 12:00:00
	 * 
	 * 传入0 获得当前时间最近的5分钟刻度时间 传入 -5 获得当前时间最近的5分钟刻度时间的前5分钟
	 *
	 * @param time
	 * @return 取前一个5分钟的时间，若返回为null则检查传入的string类型时间格式
	 */
	public static Date getRangeOf5min(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Date date = calendar.getTime();
		Date parse = null;
		String time = null;

		if (date != null) {
			time = format2.format(date);
		} else {
			return null;
		}
		int length = time.length();
		int cut = 4;
		if (length <= cut) {
			return null;
		}
		int max = 5;
		// 获取分钟数的个位
		int compare;
		try {
			compare = Integer.parseInt(time.substring(length - cut, length - cut + 1));
			time = time.substring(0, length - cut);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (compare >= 0 && compare < max) {
			time = time.concat("0:00");
		} else if (compare >= max) {
			time = time.concat("5:00");
		}

		if (num == -5) {
			Date parse1 = null;
			try {
				parse1 = format2.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
			calendar.setTime(parse1);
			calendar.add(Calendar.MINUTE, num);
			calendar.set(Calendar.SECOND, 0);
			return calendar.getTime();
		}

		try {
			parse = format2.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return parse;
	}

	/**
	 * 根据传参获得当天的0点或者前一天的0点(0/当天.-24/前一天)
	 * 
	 * @param times
	 * @return
	 */
	public static Date getTPrevHour(Date date, int times) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, times);
		return calendar.getTime();
	}

	/**
	 * local时间转换成UTC时间
	 * 
	 * @param localTime
	 * @return
	 */
	public static String localToUTC(Date localTime) {
		Date localDate = null;
		if (null != localTime) {
			localDate = localTime;
		}
		long localTimeInMillis = localDate.getTime();
		/** long时间转换成Calendar */
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(localTimeInMillis);
		/** 取得时间偏移量 */
		int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
		/** 取得夏令时差 */
		int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
		/** 从本地时间里扣除这些差量，即可以取得UTC时间 */
		calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		/** 取得的时间就是UTC标准时间 */
		Date utcDate = new Date(calendar.getTimeInMillis());
		String utcTime = UTC.format(utcDate);
		return utcTime;
	}

	public static void main(String[] args) throws ParseException {

		Date endtime = CalendarUtils.getRangeOf5min(0);
		Date startTime = CalendarUtils.getTPrevHour(endtime, -2);
		//
		// long startTimes = startTime.getTime();
		// long endTimes = endtime.getTime();
		//
		// System.out.println("now:" + format2.format(new Date()));
		System.out.println("start:" + format2.format(startTime));
		System.out.println("end:" + format2.format(endtime));
		System.out.println(endtime.compareTo(startTime));
		System.out.println(format2.parse("2018-12-10 14:15:00").compareTo(endtime));
		//
		// // System.out.println(startTimes);
		// // System.out.println(endTimes);
		// // System.out.println((endTimes - startTimes) / 300000);
		// int n = 0;
		// for (; startTimes <= (endTimes - 300000); startTimes += 300000) {
		// n++;
		// System.out.println(format2.format(new Date(startTimes)));
		// System.out.println(format2.format(new Date((startTimes + 300000))));
		// System.out.println("---");
		// }
		// System.out.println(n);
	}
}
