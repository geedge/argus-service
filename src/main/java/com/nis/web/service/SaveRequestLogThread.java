/**
 *@Title: SaveLogThread.java
 *@Package com.nis.web.service.logthread 
 *@Description TODO
 *@author dell
 *@date 2016年10月14日 下午6:26:41
 *@version  版本号
 */
package com.nis.web.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.nis.datasource.CustomerContextHolder;

/**
 * 审计日志工作线程
 * @ClassName: SaveLogThread.java 
 * @Description: 用于记录业务操作日志，后期用于审计及相关联调验证工作
 * @author (dell)
 * @date 2016年10月14日 下午6:26:41
 * @version V1.0
 */
public class SaveRequestLogThread implements Runnable {
	private Logger logger = Logger.getLogger(SaveRequestLogThread.class);
	private ServicesRequestLogService service;
	private String remoteAddr;
	private String requestURI;
	private String queryString;
	private String contextPath;
	private String operator;
	private String version;
	private int opAction;
	private Date opTime;
	private Object content;
	private Date requestTime;
	private long consumerTime;
	private int businessCode;
	private String exceptionInfo;
	private String traceCode;
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */

	@Override
	public void run() {
		logger.info("线程开始执行！");
		// 新开线程切换数据源，不影响action中的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_B);// 开启数据源B
		// TODO Auto-generated method stub
		if (service != null) {
			service.saveRequestLog(remoteAddr, requestURI, queryString, contextPath, operator, version, opAction,
					opTime, content, requestTime, consumerTime, businessCode, exceptionInfo, traceCode);
		} else {
			logger.error("service 为空！");
		}
		CustomerContextHolder.clearCustomerType();
	}

	public SaveRequestLogThread() {
		super();
	}

	/**
	 * 创建一个新的实例 SaveRequestLogThread.
	 *
	 * @param service
	 * @param requestURI
	 * @param queryString
	 * @param contextPath
	 * @param operator
	 * @param version
	 * @param opAction
	 * @param opTime
	 * @param content
	 * @param requestTime
	 * @param consumerTime
	 */
	public SaveRequestLogThread(ServicesRequestLogService service, String remoteAddr, String requestURI,
			String queryString, String contextPath, String operator, String version, int opAction, Date opTime,
			Object content, Date requestTime, long consumerTime, String exceptionInfo) {
		super();
		this.service = service;
		this.remoteAddr = remoteAddr;
		this.requestURI = requestURI;
		this.queryString = queryString;
		this.contextPath = contextPath;
		this.operator = operator;
		this.version = version;
		this.opAction = opAction;
		this.opTime = opTime;
		this.content = content;
		this.requestTime = requestTime;
		this.consumerTime = consumerTime;
		this.exceptionInfo = exceptionInfo;
	}

	public SaveRequestLogThread(ServicesRequestLogService service, String operator, String version, int opAction,
			Date opTime, Object content, Date requestTime, long consumerTime, String exceptionInfo) {
		super();
		this.service = service;
		this.operator = operator;
		this.version = version;
		this.opAction = opAction;
		this.opTime = opTime;
		this.content = content;
		this.requestTime = requestTime;
		this.consumerTime = consumerTime;
		this.exceptionInfo = exceptionInfo;
	}

	public static InputStream cloneInputStream(ServletInputStream inputStream) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buffer)) > -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byteArrayOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		return byteArrayInputStream;
	}

	/**
	 * 获取非get请求的参数
	 * @param request
	 * @return
	 */
	private static String getBodyString(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = cloneInputStream(request.getInputStream());
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static SaveRequestLogThread getNewSaveRequestLogThread(HttpServletRequest request) {
		SaveRequestLogThread thread = new SaveRequestLogThread();
		thread.setRemoteAddr(request.getRemoteAddr());
		thread.setRequestURI(request.getRequestURI());
		if (request.getMethod().toLowerCase().equals("get")) {
			thread.setQueryString(request.getQueryString());
		} else {
			String contentType = request.getContentType();
			if(contentType.equals("application/json")) {
				thread.setQueryString(getBodyString(request));
			}
		}
		thread.setContextPath(request.getContextPath());
		thread.setRequestTime(new Date());
		BufferedReader reader = null;
		String line = null;
		StringBuilder bulider = new StringBuilder();
		try {
			reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				bulider.append(new String(line.getBytes(), "utf-8"));
				bulider.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (bulider.toString().length() > 0) {
			thread.setContent(bulider.toString());
		}
		return thread;
	}

	/**
	 * service
	 * @return  service
	*/

	public ServicesRequestLogService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(ServicesRequestLogService service) {
		this.service = service;
	}

	/**
	 * remoteAddr
	 * @return  remoteAddr
	*/

	public String getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * @param remoteAddr the remoteAddr to set
	 */
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * requestURI
	 * @return  requestURI
	*/

	public String getRequestURI() {
		return requestURI;
	}

	/**
	 * @param requestURI the requestURI to set
	 */
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	/**
	 * queryString
	 * @return  queryString
	*/

	public String getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * contextPath
	 * @return  contextPath
	*/

	public String getContextPath() {
		return contextPath;
	}

	/**
	 * @param contextPath the contextPath to set
	 */
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	/**
	 * operator
	 * @return  operator
	*/

	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * version
	 * @return  version
	*/

	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * opAction
	 * @return  opAction
	*/

	public int getOpAction() {
		return opAction;
	}

	/**
	 * @param opAction the opAction to set
	 */
	public void setOpAction(int opAction) {
		this.opAction = opAction;
	}

	/**
	 * opTime
	 * @return  opTime
	*/

	public Date getOpTime() {
		return opTime;
	}

	/**
	 * @param opTime the opTime to set
	 */
	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	/**
	 * content
	 * @return  content
	*/

	public Object getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Object content) {
		this.content = content;
	}

	/**
	 * requestTime
	 * @return  requestTime
	*/

	public Date getRequestTime() {
		return requestTime;
	}

	/**
	 * @param requestTime the requestTime to set
	 */
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	/**
	 * consumerTime
	 * @return  consumerTime
	*/

	public long getConsumerTime() {
		return consumerTime;
	}

	/**
	 * @param consumerTime the consumerTime to set
	 */
	public void setConsumerTime(long consumerTime) {
		this.consumerTime = consumerTime;
	}

	/**
	 * businessCode
	 * @return  businessCode
	*/

	public int getBusinessCode() {
		return businessCode;
	}

	/**
	 * @param businessCode the businessCode to set
	 */
	public void setBusinessCode(int businessCode) {
		this.businessCode = businessCode;
	}

	/**
	 * exceptionInfo
	 * @return  exceptionInfo
	*/

	public String getExceptionInfo() {
		return exceptionInfo;
	}

	/**
	 * @param exceptionInfo the exceptionInfo to set
	 */
	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	public String getTraceCode() {
		return traceCode;
	}

	public void setTraceCode(String traceCode) {
		this.traceCode = traceCode;
	}

}
