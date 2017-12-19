package com.nis.test;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;


import com.thoughtworks.xstream.XStream;
/**
 * null空指针异常，可能设置jdk 下jre环境
 * @author Administrator
 *
 */
public class TestWsdl {
	private static Logger logger = Logger.getLogger(TestWsdl.class);

	public static void main(String[] args) {
		
		/*String wsdlUrl = "http://10.55.0.155:7007/ryService?wsdl";
		String methodName = "getData";
		String pageSize = "10";
		String currentPage = "1";
		try {
			String result = resolve(wsdlUrl, methodName, pageSize, currentPage);
			WSRegUserInfo wsRegUserInfo = parseUserInfo(result);
			logger.info(String.valueOf(wsRegUserInfo.getData().size()));
		} catch (Exception e) {
			logger.error("wsdl error:"+e);
		}*/
		

	}
	
	private static String resolve(String wsdlUrl, String methodName, Object... params) throws Exception {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(wsdlUrl);
		HTTPConduit http = (HTTPConduit) client.getConduit();        
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();        
        httpClientPolicy.setConnectionTimeout(500);  
        httpClientPolicy.setAllowChunking(false);   
        httpClientPolicy.setReceiveTimeout(500); 
        http.setClient(httpClientPolicy); 
    	Object[] result = client.invoke(methodName, params);
    	
    	return result[0].toString();
	}
	
	/*private static WSRegUserInfo parseUserInfo(String xml) throws Exception{
		XStream xs = new XStream();
		xs.alias("returnData", WSRegUserInfo.class);
		xs.alias("obj", RegUserInfo.class);
		xs.aliasField("name", RegUserInfo.class,"realName");
		WSRegUserInfo  wsRegUserInfo= (WSRegUserInfo) xs.fromXML(xml);
		return wsRegUserInfo;
	}*/

}
