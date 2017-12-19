/**
 *@Title: ElasticsearchSqlUtil.java
 *@Package com.nis.util 
 *@Description TODO
 *@author dell
 *@date 2016年10月17日 下午4:09:17
 *@version  版本号
 */
package com.nis.util.elasticsearch;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nis.domain.LogEntity;
import com.nis.domain.Page;
import com.nis.util.Constants;
import com.nis.util.JsonMapper;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.util.httpclient.HttpClientUtil;
import com.nis.web.service.SpringContextHolder;

/**
 * @ClassName: ElasticsearchSqlUtil.java 
 * @Description: 在service中替代oracle查询
 * @author (wx)
 * @date 2016年10月17日 下午4:09:17
 * @version V1.0
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ElasticsearchSqlDao {
	private static final Logger logger=Logger.getLogger(ElasticsearchSqlDao.class);
	private static final SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
	private static final Map<Class,Map<String,String>> fieldsMapMap=new HashMap<>();
	public void init(Class clazz){
		Map<String,String> fieldsMap=new HashMap<>();
		ResultMap data=sqlSessionFactory.getConfiguration().getResultMap(clazz.getSimpleName()+"Map");
		List<ResultMapping> mappingList=data.getResultMappings();
		for(ResultMapping map:mappingList){
			fieldsMap.put(map.getColumn().toUpperCase(), map.getProperty());
		}
		fieldsMapMap.put(clazz, fieldsMap);
	}
	/**
	 * 获取elasticsearch中的mapping field
	 * getSearchFields(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param page
	 * @param clazz
	 * @return 
	 *List<String>
	 * @exception 
	 * @since  1.0.0
	 */
	private  String getSearchFields(Page<?> page,Class<?> clazz){
		String fields=page.getFields();
		
		if(StringUtil.isBlank(fields)){
				fields="*";
		}else{
			String[] fieldArray=fields.split(",");
			for(String field :fieldArray){
				if(fieldsMapMap.get(clazz).containsKey(field.toUpperCase())){
					fields=fields.replace(field, fieldsMapMap.get(clazz).get(field.toUpperCase()));
				}
			}
		}
		return fields;
	}
	/**
	 * 
	 * getWhereCondition(where 条件转换)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param entity
	 * @param givenDateFormat
	 * @return
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	private  String getWhereCondition(LogEntity<?> entity,SimpleDateFormat givenDateFormat) throws ParseException, IllegalArgumentException, IllegalAccessException{
		StringBuffer where=new StringBuffer();
		//使用反射获取entity的所有字段值
		Class clazz=entity.getClass();
		List<Field> fieldList=new ArrayList<Field>();
		fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
		fieldList.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
		
		for(Field field:fieldList){
			if(Modifier.isFinal(field.getModifiers()))continue;
			if(Modifier.isPrivate(field.getModifiers())||Modifier.isProtected(field.getModifiers())){
				field.setAccessible(true);
			}
			String type=field.getType().getSimpleName();
			Object value=field.get(entity);
			if(value==null)continue;
			if(field.getName().endsWith("StartTime")){
				String startTime=value.toString();
				if(!StringUtil.isBlank(startTime)){
					String fieldName=field.getName()
							.replace("search", "")
							.replace("Start", "");
					fieldName=fieldName.replace(fieldName.substring(0, 1),fieldName.substring(0, 1).toLowerCase());
					 where.append(" AND "+fieldName +">=");
					 where.append(givenDateFormat.parse(startTime).getTime()/1000); 
					
				}
			}else if(field.getName().endsWith("EndTime")){
				String endTime=value.toString();
				if(!StringUtil.isBlank(endTime)){
					String fieldName=field.getName()
							.replace("search", "")
							.replace("End", "");
					fieldName=fieldName.replace(fieldName.substring(0, 1),fieldName.substring(0, 1).toLowerCase());
					 where.append(" AND "+fieldName +"<");
					 where.append(givenDateFormat.parse(endTime).getTime()/1000); 
					
				}
			}else{
				String fieldName=field.getName().replace("search", "");
				fieldName=fieldName.replace(fieldName.substring(0, 1),fieldName.substring(0, 1).toLowerCase());
				if(fieldsMapMap.get(clazz).containsValue(fieldName)){
					if("Date".equals(type)){
						Date date=(Date)value;
						where.append(" AND "+fieldName+"=");
						where.append(date.getTime()/1000); 
					}else{
						where.append(" AND "+fieldName +"=");
						where.append("'"+value.toString()+"'");
					}
				}
			}
		}
		where.delete(0, " AND ".length());
		return where.toString().trim();
	}
	/**
	 * 
	 * getOderbys(排序转换)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param page
	 * @param clazz
	 * @return 
	 *String
	 * @exception 
	 * @since  1.0.0
	 */
	private  String getOderbys(Page page,Class clazz){
		String orderBy=page.getOrderBy();
		for(String field :orderBy.split(" ")){
			if(fieldsMapMap.get(clazz).containsKey(field.toUpperCase())){
				orderBy=orderBy.replace(field, fieldsMapMap.get(clazz).get(field.toUpperCase()));
			}
		}
		return orderBy;
		
	}
	
	public String geneEs4SQL(Class clazz,Page<?> page,LogEntity<?> entity) throws ParseException, IllegalArgumentException, IllegalAccessException{
		if(!fieldsMapMap.containsKey(clazz)){
			this.init(clazz);
		}
		SimpleDateFormat givenDateFormat=new SimpleDateFormat(Constants.SEARCH_DATEFORMAT);
		String indexName=clazz.getSimpleName().toUpperCase();
		String fields=getSearchFields(page, clazz);
		String where=getWhereCondition(entity, givenDateFormat);
		where=StringUtils.isBlank(where)?" ":" where "+where;
		String groupBy="";
		String orderBy=getOderbys(page,clazz);
		orderBy=StringUtils.isBlank(orderBy)?" ":" ORDER BY "+orderBy;
		String pageInfo=getPageInfo(page);
		String sql="SELECT "+fields+
				" FROM "+indexName+where+groupBy+orderBy+pageInfo;
		logger.info("es-sql:"+sql);
		return sql.replaceAll(" ", "%20").replaceAll("<", "%3C").replaceAll(">", "%3E");
	}
	public String search(LogEntity<?> entity,String acticeSys) throws ParseException, IllegalArgumentException, IllegalAccessException, ClientProtocolException, IOException{
		String sql=geneEs4SQL(entity.getClass(),entity.getPage(),entity);
		if(acticeSys.equals(Constants.ACTIVESYS_C)){
			logger.info("查询C版ES");
			return HttpClientUtil.get("http://"+Constants.SEARCH_ES_HOSTANDPORT_C+"/_sql?sql="+sql);
		}else if(acticeSys.equals(Constants.ACTIVESYS_A)){
			logger.info("查询A版ES");
			return HttpClientUtil.get("http://"+Constants.SEARCH_ES_HOSTANDPORT_A+"/_sql?sql="+sql);
		}else{
			logger.info("查询B版ES");
			return HttpClientUtil.get("http://"+Constants.SEARCH_ES_HOSTANDPORT_B+"/_sql?sql="+sql);
		}
	}
	private  String getPageInfo(Page page){
		int pageNo=page.getPageNo();
		int pageSize=page.getPageSize();
		return " limit "+(pageNo-1)*pageSize+", "+page.getPageSize();
	}
	/**
	 * 
	 * findLogs(查询方法)
	 * (初始化好list 之后传入方法，查询结果会加入到list中)
	 * @param logList
	 * @param esHostAndPort
	 * @param log
	 * @param givenFormat
	 * @throws ParseException
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException 
	 *void
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @exception 
	 * @since  1.0.0
	 */
	public void findLogs(List logList,LogEntity<?> log,String activeSys) throws ParseException, JSONException, IllegalArgumentException, IllegalAccessException, ClientProtocolException, IOException{
		Class clazz=log.getClass();
		JSONObject obj=new JSONObject(search(log,activeSys));
		long count=obj.getJSONObject("hits").getLong("total");
		long took=obj.getLong("took");
		logger.info("search by es cost: "+took+"ms");
		log.getPage().setCount(count);
		if(count>0){
			JSONArray resultJson=obj.getJSONObject("hits").getJSONArray("hits");
			for(int i=0;i<resultJson.length();i++){
				JSONObject re=(JSONObject)resultJson.get(i);
				JSONObject json=re.getJSONObject("_source");
				Iterator it=json.keys();
				//找出时间字段，由于中心时间戳没有存毫秒，在这里转换时间的时候需要将10位的时间戳转换为13位的时间戳
				//默认以Time结尾的字段为时间字段
				while(it.hasNext()){
					String key=it.next().toString();
					if(key.endsWith("Time")&&json.has(key)&&!json.isNull(key)){
						long time=json.getLong(key);
						if(String.valueOf(time).length()==10){
							json.put(key, time*1000);
						}
					}
				}
				logList.add(JsonMapper.fromJsonString(json.toString(), clazz));
			}
			
		}
	}
	/**
	 * main(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param args 
	 *void
	 * @exception 
	 * @since  1.0.0
	*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuffer sf=new StringBuffer(" AND 1=1");
		System.out.println(sf.delete(0, " AND".length()).toString());
	}
	
}
