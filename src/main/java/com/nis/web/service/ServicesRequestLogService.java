/**
 *@Title: ServicesRequestLogService.java
 *@Package com.nis.web.service.restful 
 *@Description TODO
 *@author wx
 *@date 2016年9月7日 下午3:28:44
 *@version  版本号
 */
package com.nis.web.service;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.datasource.DynamicDataSource;
import com.nis.domain.Page;
import com.nis.domain.ServicesRequestLog;
import com.nis.domain.restful.ServicesRequestLogBean;
import com.nis.util.Constants;
import com.nis.util.StringUtils;
import com.nis.web.dao.ServicesRequestLogDao;

/**
 * @ClassName: ServicesRequestLogService.java
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月7日 下午3:28:44
 * @version V1.0
 */
@Service
public class ServicesRequestLogService {
	protected final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	public ServicesRequestLogDao servicesRequestLogDao;

	private void saveLog(ServicesRequestLog log) {
		servicesRequestLogDao.insert(log);
	}

	/**
	 * 
	 * saveRequestLog(记录请求日志) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param requestAddr
	 *            request中的参数
	 * @param requestURI
	 *            request中的参数
	 * @param queryString
	 *            request中的参数
	 * @param contextPath
	 *            request中的参数
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @param operator
	 *            操作人
	 * @param version
	 *            版本
	 * @param opAction
	 *            操作行为
	 * @param opTime
	 *            操作时间
	 * @param content
	 *            请求体
	 * @param requestTime
	 *            请求到达服务器时间
	 * @param consumerTime
	 *            耗时 void
	 * @throws UnknownHostException 
	 * @exception @since
	 *                1.0.0
	 */
	public void saveRequestLog(String requestAddr, String requestURI, String queryString, String contextPath,
			String operator, String version, int opAction, Date opTime, Object content, Date requestTime,
			long consumerTime, int businessCode, String exceptionInfo) {
		logger.info("开始记录日志---");
		logger.info("请求IP---" + requestAddr);
		logger.info("请求路径---" + requestURI);
		logger.info("请求内容---" + queryString);
		ServicesRequestLog log = new ServicesRequestLog();
		log.setRequestIp(requestAddr);
		log.setOperator(operator);
		log.setVersion(version);
		log.setOpAction(opAction);
		log.setVersion(version);
		log.setOpTime(opTime);
		log.setRequestTime(requestTime);
		log.setConsumerTime(consumerTime);
		log.setBusinessCode(businessCode);
		log.setExceptionInfo(exceptionInfo);
		try {
			if(Constants.SERVCER_HOST!=null){
				log.setServerIp(Constants.SERVCER_HOST);
			}else if(isWindows()){
				Constants.SERVCER_HOST=InetAddress.getLocalHost().getHostAddress();
				log.setServerIp(Constants.SERVCER_HOST);
			}else{
				InetAddress ip=null;
				boolean bFindIP=false;
				Enumeration<NetworkInterface> netInterfaces=(Enumeration<NetworkInterface>)
						NetworkInterface.getNetworkInterfaces();
				while(netInterfaces.hasMoreElements()){
					if(bFindIP){
						break;
					}
					NetworkInterface ni=(NetworkInterface)netInterfaces.nextElement();
					Enumeration<InetAddress> ips=ni.getInetAddresses();
					while(ips.hasMoreElements()){
						ip=(InetAddress)ips.nextElement();
						if(ip.isSiteLocalAddress()&&!ip.isLoopbackAddress()&&ip.getHostAddress().indexOf(":")==-1){
							bFindIP=true;
							break;
						}
					}
				}
				if(null !=ip){
					Constants.SERVCER_HOST=ip.getHostAddress();
					log.setServerIp(ip.getHostAddress());
				}
			}
			
		} catch (UnknownHostException e) {
			logger.error("无法获取当前服务器ip");
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			logger.error("无法获取当前服务器ip");
			e.printStackTrace();
		}
		String type = null;
		Pattern p = Pattern.compile(contextPath + "/service/([^/]+)/([^/]+)/");
		Matcher m = p.matcher(requestURI);
		if (m.find()) {
			type = m.group(1);
			logger.info("请求类型：" + type);
		}
		if (log.getVersion() == null)
			log.setVersion(m.group(2));
		if (opAction == Constants.OPACTION_GET) {// get请求不会携带boby
			if ("file".equals(type)) {// 文件下载
				log.setRequestContent(content.toString());
			} else {
				if (!StringUtils.isBlank(queryString)) {
					log.setRequestContent(queryString);
				}
			}
		} else {
			if ("file".equals(type)) {// 文件上传
				log.setRequestContent(content.toString());
			} else {
				if (content != null) {
					log.setRequestContent(content.toString());
				}

			}

		}
		this.saveLog(log);
		logger.info("记录日志完成---");
	}
	public boolean isWindows(){
		boolean isWindows=false;
		if(System.getProperty("os.name").toLowerCase().indexOf("windows")>-1){
			isWindows=true;
		}
		return isWindows;
	}
	/**
	 * 直接从数据源获取connection保存日志的方法，用saveLog()方法就行 save(这里用一句话描述这个方法的作用)
	 * (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 *             void
	 * @exception @since
	 *                1.0.0
	 */
	public void save(ServicesRequestLog data) throws IllegalArgumentException, IllegalAccessException {
		SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
		// map由于无序，不能用 columnMap生成sql语句
		Map<String, String> columnMap = new HashMap<String, String>();
		for (ResultMapping mapp : sqlSessionFactory.getConfiguration()
				.getResultMap(data.getClass().getSimpleName() + "Map").getResultMappings()) {
			columnMap.put(mapp.getProperty(), mapp.getColumn());
		}
		//
		DynamicDataSource dataSource = SpringContextHolder.getBean(DynamicDataSource.class);
		StringBuffer sql = new StringBuffer();
		String table = StringUtils.toUnderScoreCase(data.getClass().getSimpleName()).toUpperCase();
		sql.append("INSERT INTO ");
		sql.append(table);
		sql.append("(ID,");
		Field[] fields = data.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			if (!"id".equals(fieldName) && columnMap.containsKey(fieldName) && fields[i].get(data) != null) {
				sql.append(columnMap.get(fieldName));
				sql.append(",");
			}
		}
		if (sql.lastIndexOf(",") == sql.toString().length() - 1) {
			sql.deleteCharAt(sql.toString().length() - 1);
		}
		sql.append(") values(SEQ_SERVICES_REQUEST_LOG.Nextval,");
		for (int i = 0; i < fields.length; i++) {
			if (columnMap.containsKey(fields[i].getName()) && fields[i].get(data) != null) {
				sql.append("?");
				sql.append(",");
			}
		}
		if (sql.lastIndexOf(",") == sql.toString().length() - 1) {
			sql.deleteCharAt(sql.toString().length() - 1);
		}
		sql.append(")");
		logger.info("日志新增sql:" + sql.toString());
		Connection connection = null;
		PreparedStatement prepareStatement = null;
		StringReader reader = null;
		try {
			connection = dataSource.getConnection();
			prepareStatement = connection.prepareStatement(sql.toString());
			int i = 1;
			for (Field field : fields) {
				if (columnMap.containsKey(field.getName())) {
					String type = field.getType().getSimpleName();
					if (field.get(data) == null)
						continue;
					if (type.equals("String")) {
						if (field.getName().equals("requestContent")) {
							String content = (String) field.get(data);
							reader = new StringReader(content);
							prepareStatement.setCharacterStream(i, reader, content.length());
						} else
							prepareStatement.setString(i, (String) (field.get(data)));
					} else if (type.equals("Long")) {
						prepareStatement.setLong(i, (Long) (field.get(data)));
					} else if (type.equals("Integer")) {
						prepareStatement.setInt(i, (Integer) (field.get(data)));
					} else if (type.equals("Date")) {
						java.sql.Date sqlDate = new java.sql.Date(((Date) field.get(data)).getTime());
						prepareStatement.setDate(i, sqlDate);
					}
					i++;
				}
			}
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			try {
				if (prepareStatement != null) {
					prepareStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
				if (reader != null)
					reader.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		try {
			if (prepareStatement != null) {
				prepareStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
			if (reader != null)
				reader.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void deleteById(String id) {
		String[] split = id.split(",");
		for (String str : split) {
			if(!str.equals("")){
				servicesRequestLogDao.deleteById(Long.parseLong(str));
			}
		}
	}

	public void deleteAll() {
		servicesRequestLogDao.deleteAll();
	}

	public Page<ServicesRequestLogBean> getAllLog(Page<ServicesRequestLogBean> page,
			ServicesRequestLogBean requestLog) {
		requestLog.setPage(page);
		page.setList(servicesRequestLogDao.getAllLog(requestLog));
		return page;
	}

}
