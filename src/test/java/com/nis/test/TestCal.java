/**
 * 
 */
package com.nis.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @ClassName:TestCal
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年6月12日 上午9:52:57
 * @version V1.0
 */
public class TestCal {

	/**
	 * @Description:
	 * @author (zdx)
	 * @date 2018年6月12日 上午9:52:57
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			SimpleDateFormat df1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			System.out.println("defaultTimeZone:------------------------------:"+df1.getTimeZone());
		//	Date t = new Date();
			Date t = df1.parse("2018-05-23 13:48:11");
			Long time = t.getTime();
			System.out.println(df1.format(t));
			System.out.println(time);
			Long time2 = (time/1000)*1000;
			System.out.println(time2+"      "+df1.format(time2));
			
			System.out.println(df1.getTimeZone().getID()+"----------------"+df1.format(t));

			
			df1.setTimeZone(TimeZone.getTimeZone("UTC"));
			System.out.println("setTimeZone:--------------:"+df1.getTimeZone());
			System.out.println(df1.format(t));
			
			
			System.out.println("-------------------------------------------------------------------");
			
			SimpleDateFormat df2 = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date t2 = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);
			System.out.println(df2.getTimeZone().getID()+"----------------"+df2.format(calendar.getTime()));


			df2.setTimeZone(TimeZone.getTimeZone("UTC"));
			System.out.println(df2.getTimeZone().getID()+"----------------"+df2.format(calendar.getTime()));
			
			df2.setTimeZone(TimeZone.getTimeZone("UTC-8"));			
			System.out.println(df2.getTimeZone().getID()+"----------------"+df2.format(calendar.getTime()));
			
			df2.setTimeZone(TimeZone.getTimeZone("UTC+8"));			
			System.out.println(df2.getTimeZone().getID()+"----------------"+df2.format(calendar.getTime())); 
			
			df2.setTimeZone(TimeZone.getTimeZone("GMT+8"));			
			System.out.println(df2.getTimeZone().getID()+"----------------"+df2.format(calendar.getTime()));
			
			df2.setTimeZone(TimeZone.getTimeZone("GMT"));
			System.out.println(df2.getTimeZone().getID()+"----------------"+df2.format(calendar.getTime()));
			
			
			System.out.println("-----------------------------------------");
			
			df2.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = df2.parse("2018-05-23T13:48:11.077");
			
			System.out.println("2018-05-23 13:48:11"+"def:"+time+"      "+date.getTime()+"        差："+(time-date.getTime())/(60*60*1000));
			
			System.out.println("=====--------------------------------------------------");
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTimeZone(TimeZone.getTimeZone("GMT"));
			calendar2.setTimeInMillis(1527229532956l);
			df1.setTimeZone(TimeZone.getTimeZone("GMT"));
			System.out.println(calendar2.getTime()+df1.getTimeZone().toString()+"----"+df1.format(calendar2.getTime()));
			
			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			df1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			calendar3.setTimeInMillis(1527229532956l);
			System.out.println(calendar3.getTime()+df1.getTimeZone().toString()+"----"+df1.format(calendar3.getTime()));
			
			
			Date date2 = new Date();
			System.out.println("Date:"+date2);
			df1.setTimeZone(TimeZone.getTimeZone("UTC+0800"));			
			System.out.println("UTC+0800"+df1.getTimeZone().toString()+"   "+df1.format(date2));
			
			df1.setTimeZone(TimeZone.getTimeZone("GMT"));			
			System.out.println("GMT"+df1.getTimeZone().toString()+"   "+df1.format(date2));
			 
			df1.setTimeZone(TimeZone.getTimeZone("GMT+8"));			
			System.out.println("GMT+8"+df1.getTimeZone().toString()+"   "+df1.format(date2));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
