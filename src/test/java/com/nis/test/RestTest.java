/**
 * 
 */
package com.nis.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.nis.restful.RestConstants;
import com.nis.util.JsonMapper;

/**
 * @ClassName:RestTest
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年5月22日 上午10:38:50
 * @version V1.0
 */
public class RestTest {

	@Test
	public void testFileUpload() {
		CloseableHttpResponse response = null;
		try {
			File file = new File("F:/git.rar");

//			String md5 = DigestUtils.md5Hex(file);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody("file", file);

			HttpPost request = new HttpPost(
					"http://10.0.6.120:8080/galaxy/service/cfg/v1/fileUploadSources");
			//galaxy/service/cfg/v1/fileUploadSources
			request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 600000);
			request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 600000);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("filetype", "doc");
			jsonObject.put("createTime", "2018-05-21 21:00:00");
			jsonObject.put("key", "123456789");
			jsonObject.put("fileName", "1.png");
			//jsonObject.put("checksum", "d99f9aa4f3748fa48aa6e350c628c67f");

			jsonObject.put("checksum", "faa364a36cddcbbfd65c0224f35fcfd0");
			request.setHeader("File-Desc", jsonObject.toString());
			HttpEntity entity = builder.build();
			request.setEntity(entity);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			response = httpClient.execute(request);
			System.out.println(EntityUtils.toString(response.getEntity()));
			System.out.println("aa");	
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//@Test
	public void testGetPzId() {
		CloseableHttpResponse response = null;
		try {

			HttpGet request = new HttpGet(
					"http://10.0.6.104:8080/galaxy/service/cfg/v1/configPzIdSources?sourceName=CONFIGCOMPILE");
			//galaxy/service/cfg/v1/fileUploadSources
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sourceName", "CONFIGCOMPILE");
			jsonObject.put("createTime", "2018-05-21 21:00:00");
			jsonObject.put("key", "123456789");
			jsonObject.put("fileName", "1.png");
			jsonObject.put("checksum", "d99f9aa4f3748fa48aa6e350c628c67f");

//			request.setHeader("File-Desc", jsonObject.toString());
			CloseableHttpClient httpClient = HttpClients.createDefault();

			response = httpClient.execute(request);
			//HttpEntity httpEntity = response.getEntity();
			String result = EntityUtils.toString(response.getEntity());
			Map<String, Object> resultMap = (Map<String, Object>)JsonMapper.fromJsonString(result, Map.class);
			Map<String, Object> dataMap = (Map<String, Object>)resultMap.get(RestConstants.REST_SERVICE_DATA);
			List<?> idObject = (List<?>) dataMap.get("pzIdList");
			for (Object object : idObject) {
				if(object instanceof Integer){
					System.out.println("Integer");
				}else if(object instanceof Long){
					System.out.println("Long");
				}
			}
			System.out.println("aa");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//@Test
	public void test() {
		Date date = new Date();
		System.out.println(date.getTime());
		
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.getTimeInMillis());
	}
	//@Test
	public void testSdf() {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat df1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
		//	Date t = new Date();
			Date t = df1.parse("2018-05-23 13:48:11");
			Long time = t.getTime();
			System.out.println(df1.format(t));
			System.out.println(time);
			Long time2 = (time/1000)*1000;
			System.out.println(time2+"      "+df1.format(time2));
			
			System.out.println(df1.getTimeZone().getID()+"----------------"+df1.format(t));

			df1.setTimeZone(TimeZone.getTimeZone("UTC"));
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
			df2.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			
			System.out.println(df2.getTimeZone().getID()+"----------------"+df2.format(calendar.getTime()));
			df2.setTimeZone(TimeZone.getTimeZone("GMT"));
			System.out.println(df2.getTimeZone().getID()+"----------------"+df2.format(calendar.getTime()));
			
			
			System.out.println("-----------------------------------------");
			
			df2.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = df2.parse("2018-05-23T13:48:11.077");
			
			System.out.println("2018-05-23 13:48:11"+"def:"+time+"      "+date.getTime()+"        差："+(time-date.getTime())/(60*60*1000));
			
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//            String date = "2000-10-01T10:10:10.123Z";
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'");
//            SimpleDateFormat df2 = new SimpleDateFormat(
//					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            df2.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//            //创建被转换的对象calendar，有时区信息
//            Calendar calendar = new GregorianCalendar();
//            calendar.setTime(df2.parse(date));
//            TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
//            calendar.setTimeZone(timeZone);
//
//            User test = new User();
//            test.setId("111");
//            test.setDate(calendar.getTime());
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json = objectMapper.writeValueAsString(test);
//            System.out.println(json);
//
//            User restore = objectMapper.readValue(json, User.class);
//            //System.out.print(restore.getDate().getTimeZone().getDisplayName());
//        }catch (Exception e) {
//            e.printStackTrace(System.out);
//        }
	}

}
