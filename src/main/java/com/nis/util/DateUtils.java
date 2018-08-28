/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static final Logger logger = Logger.getLogger(DateUtils.class);
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	/**
	 * 
	 * @Description: 按类型获取默认时间
	 * @author (zdx)
	 * @date 2018年8月28日 下午4:53:40
	 * @param startTime
	 * @param endTime
	 * @param localLen
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getLocalTime(String startTime,String endTime,Long localLen,String type)throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> timeMap=new HashMap<String, String>();
		if (StringUtil.isEmpty(type)) {
			type = "minute";
		}
		//实时报表统计，默认查询最近5分钟
		if("minute".equals(type) && startTime == null && endTime == null){
			Calendar cal=Calendar.getInstance();
			timeMap.put("endTime", sdf.format(cal.getTime()));
			if(localLen != null){
				cal.add(Calendar.MINUTE, -localLen.intValue());
			}else{
				cal.add(Calendar.MINUTE, -5);
			}
			timeMap.put("startTime", sdf.format(cal.getTime()));
			logger.info("实时报表统计默认开始时间条件："+timeMap.get("startTime"));
			logger.info("默认结束时间条件："+timeMap.get("endTime"));
			
		}else if("hour".equals(type) && startTime == null && endTime == null){//小时报表默认查询最近一小时的数据
			Calendar cal=Calendar.getInstance();
			timeMap.put("endTime", sdf.format(cal.getTime()));
			cal.add(Calendar.HOUR_OF_DAY, -1);
			timeMap.put("startTime", sdf.format(cal.getTime()));
			logger.info("小时报默认开始时间条件："+timeMap.get("startTime"));
			logger.info("小时报默认结束时间条件："+timeMap.get("endTime"));
		}else if("daily".equals(type) && startTime == null && endTime == null){//日报表默认查询最近一天的数据
			Calendar cal=Calendar.getInstance();
			timeMap.put("endTime", sdf.format(cal.getTime()));
			cal.add(Calendar.DAY_OF_YEAR, -1);
			timeMap.put("startTime", sdf.format(cal.getTime()));
			logger.info("日报默认开始时间条件："+timeMap.get("startTime"));
			logger.info("日报默认结束时间条件："+timeMap.get("endTime"));
		}else if("month".equals(type) && startTime == null && endTime == null){//月报表默认查询最近一月的数据
			Calendar cal=Calendar.getInstance();
			timeMap.put("endTime", sdf.format(cal.getTime()));
			cal.add(Calendar.MONTH,-1);
			timeMap.put("startTime", sdf.format(cal.getTime()));
			logger.info("月报默认开始时间条件："+timeMap.get("startTime"));
			logger.info("月报默认结束时间条件："+timeMap.get("endTime"));
		}
		return timeMap;
	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
		Date date=new Date();
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MONTH, date.getMonth()-2);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//timeMap.put("startTime", sdf.format(cal2.getTime()));
		//timeMap.put("endTime", sdf.format(cal.getTime()));
		logger.info("月报默认开始时间条件："+sdf.format(cal.getTime()));
	}
}
