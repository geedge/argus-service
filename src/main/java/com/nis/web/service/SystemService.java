package com.nis.web.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.OMNamespaceImpl;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nis.domain.SrcIp;
import com.nis.domain.SysRole;
import com.nis.domain.SysUser;
import com.nis.domain.WebServiceClient;
import com.nis.util.Configurations;
import com.nis.util.DateUtils;
import com.nis.util.IpUtil;
import com.nis.util.StringUtil;
import com.nis.util.TimeConstants;
import com.nis.web.dao.SrcIpDao;
import com.nis.web.dao.SysOfficeDao;
import com.nis.web.dao.UserDao;
import com.nis.web.security.Servlets;
import com.nis.web.security.SessionDAO;
import com.nis.web.security.UserUtils;

@Service
public class SystemService extends BaseService{
	
	@Autowired
	private SessionDAO sessionDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private SysOfficeDao sysOfficeDao;
	
	@Autowired
	private SrcIpDao srcIpDao;
		
	
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return  sessionDao.getActiveSessions(includeLeave);
	}
	
	
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
		return  sessionDao.getActiveSessions(includeLeave, principal, filterSession);
	}
	
	public void deleteSession(Session session) {
		sessionDao.delete(session);
	}
	
	
	public SysUser getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}
	
	public List<SrcIp> ipLookUp(String ip) {
		return srcIpDao.getIpInfo(IpUtil.getIpHostDesimal(ip));
	}
	
	
	public SysUser assignUserToRole(SysRole role, SysUser user) {
		if (user == null){
			return null;
		}
		List<Long> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getUserRoleList().clear();
		user.getUserRoleList().add(role);
		userDao.insertUserRole(user);
		UserUtils.clearCache(user);
		return user;
	}
	
	public Boolean outUserInRole(SysRole role, SysUser user) {
		List<SysRole> roles = user.getUserRoleList();
		for (SysRole e : roles){
			if (e.getId().equals(role.getId())){
				roles.remove(e);
				userDao.removeUserInRole(user.getId(),role.getId());
				return true;
			}
		}
		return false;
	}
	
	public boolean officeIsValid(Long officeId, Long companyId) {
		return userDao.officeIsExistOfCompany(officeId, companyId) >0 ?  true : false;
	}

	
	
	
	/**
	 * 用户机构
	 * @param client
	 * @return
	 */
	private String getOAServiceData(WebServiceClient client) {
		ServiceClient sender = null; 
		String result = "";
			try {
						 sender = new ServiceClient(); 
			             EndpointReference endpointReference = new EndpointReference(client.getEndPoint());  
			             Options options = new Options(); 
			             options.setAction(client.getNs()+"/"+client.getMethodName());  
			             options.setTo(endpointReference);  
			             sender.setOptions(options);
			             //添加soapheader验证消息头
			             sender.addHeader(setHeader(client.getNs(), client.getRid(), client.getSid()));
			             OMFactory fac = OMAbstractFactory.getOMFactory();  
			             // 设置命名空间  
			             OMNamespace omNs = fac.createOMNamespace(client.getNs(), "");
			             //OMElement data = fac.createOMElement("服务输入接口", omNs);
			             OMElement data = fac.createOMElement(client.getMethodName(),client.getNs(),"");
			             // 对应参数的节点  
			            //String[] strs = new String[] { "pageSize","currentPage","param" };  
			             String[] strs = client.getOpArgs();
			             // 参数值  
			             String[] vals = client.getOpVals();  
			             for (int i = 0; i < strs.length; i++) {  
			                 OMElement inner = fac.createOMElement(strs[i],"","");  
			                 inner.setText(vals[i]);  
			                 data.addChild(inner);  
			             }  
			             // 发送数据，返回结果  
			             OMElement resultE = sender.sendReceive(data);  
			             result = resultE.toString();
			             result = result.replaceAll("&lt;", "<");
			             result = result.replaceAll("&gt;", ">");
			             result = result.substring( result.indexOf("<returnData>"), result.indexOf("</returnData>")+13);
			   } catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			   }finally{
			    try {
			     sender.cleanupTransport();
			    } catch (AxisFault e) {
			     // TODO Auto-generated catch block
			     e.printStackTrace();
			    }
			   }
		return result;
		
	}
	
	/**
	 * 
	 * @param client webservice 客户端对象
	 * @return
	 */
	private String getOAServiceData_old(WebServiceClient client) throws Exception {
		String result = null;
		RPCServiceClient serviceClient = null;
		try {
			serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT, client.getConnectionTimeOut()*1000);
			EndpointReference targetEPR = new EndpointReference(client.getEndPoint());
	
			// ====================新增 添加header ========================start
			serviceClient.addHeader(setHeader(client.getNs(), client.getRid(), client.getSid()));
			// ====================新增 添加header ========================end
	
			// 原有业务逻辑不动
			options.setTo(targetEPR);
			QName opAddEntry = new QName(client.getNs(), client.getMethodName());
			
			Class[] returnTypes = new Class[] { String.class };  
			String[] opVals = new String[]{String.valueOf(client.getPageSize()),String.valueOf(client.getCurrentPage()),"", "MB_HR_CY_JSSF"};
			// 调用plus方法并输出该方法的返回值  
			result =serviceClient.invokeBlocking(opAddEntry,client.getOpVals(), returnTypes)[0].toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("get OA data error :"+e);
		} finally {
			serviceClient.cleanupTransport();
		}
		
		//result = getTestFormXml();
		return result;
	}
	
	
	/**
	 * 结果信息存入文件
	 * @param result
	 * @throws IOException 
	 */
	private void saveToFile(String prefixName, String result) throws IOException{
		String flieName = prefixName + DateUtils.formatDate(new Date(), TimeConstants.YYYYMMDDHH24MMSS);
		String filePath = Servlets.getRequest().getServletContext().getRealPath(Configurations.getStringProperty("userfiles.basedir", "")) + File.separator
				+ "upload" + File.separator + flieName + ".txt";
		FileUtils.writeStringToFile(new File(filePath), result, false);		
		
	}
	
	/**
	 * 新增设置header信息，需要修改里面rid、sid对应的值、sid为服务唯一标识、 rid为请求者唯一标识
	 */
	private static OMElement setHeader(String ns, String rid, String sid) {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		// OMNamespace指定此SOAP文档名称空间。
		OMNamespaceImpl omNs =  (OMNamespaceImpl) fac.createOMNamespace(ns, "ns1");
		// 创建header元素，并指定其在omNs指代的名称空间中,header名称固定为CyberpoliceSBReqHeader。
		OMElement method = fac.createOMElement("CyberpoliceSBReqHeader", omNs);
	
		// 指定元素的文本内容。
		OMElement ridE = fac.createOMElement("rid", omNs);
		// TODO将下面的值修改为请求者在系统中的唯一标识
		ridE.setText(rid);
		method.addChild(ridE);
	
		OMElement sidE = fac.createOMElement("sid", omNs);
		// TODO将下面的值修改要请求服务的唯一标识
		sidE.setText(sid);
		method.addChild(sidE);
	
		OMElement timeoutE = fac.createOMElement("timeout", omNs);
		// TODO将下面的值修改为请求的超时时间，单位秒
		timeoutE.setText(Configurations.getStringProperty("webservice.request.timeout", "60"));
		method.addChild(timeoutE);
	
		OMElement secE = fac.createOMElement("sec", omNs);
		// TODO将下面的值修改为请求密码，如果使用其他加密方式，则根据要求统一修改即可
		secE.setText("");
		method.addChild(secE);
		return method;
	}

	
	
	/**
	 * 获取数据字典信息
	 * @param method
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public String syncDataDict(String method) throws Exception {
		
		String methodName = null;
		String ns = Configurations.getStringProperty("webservice.data.dict.service.url", "");
		String rid = Configurations.getStringProperty("webservice.rid", "");
		String sid = Configurations.getStringProperty("webservice.data.dict.sid", "");
		if (method.equals("QL")) {
			methodName = Configurations.getStringProperty("webservice.method.name.ql", "");
		}
		WebServiceClient webServiceClient = new WebServiceClient(ns, methodName, rid, sid, null);
		
		
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		resultMap.put("dataCount", 0);
		resultMap.put("insertCount", 0);
		resultMap.put("updateCount", 0);
		resultMap.put("deleteCount", 0);
		resultMap.put("invalid", 0);
		Date syncTime = null;
		try{
			
			while(true) {
				String result = getOAServiceData(webServiceClient);
				if (StringUtil.isBlank(result)) {
					break;
				}
				logger.info(result);
				webServiceClient.incrementPage();//next page
			}
			
			logger.info("同步用户数据总计："+resultMap.get("dataCount"));  
			
			//saveDataResult(resultMap.get("dataCount"),resultMap.get("insertCount"),
					//resultMap.get("updateCount"),resultMap.get("deleteCount"),resultMap.get("invalid"),Constants.SYNC_YH,methodName.equals("getData")?Constants.SYNC_QL:Constants.SYNC_ZL,syncTime);
		}catch(Exception e){
			e.printStackTrace();
			return "同步失败,获取数据异常!"+e;
		}
 
		return   "同步完成! 数据总数量:" + resultMap.get("dataCount") +"; 插入数量:" +resultMap.get("insertCount")+"; 更新数量:" 
			+resultMap.get("updateCount") +"; 删除数量:" +resultMap.get("deleteCount")+"; 无效数量"+resultMap.get("invalid");
				
	}
	
	public String syncDataDictBM(String method) throws Exception{
		String methodName = null;
		String ns = Configurations.getStringProperty("webservice.data.dict.bm.url", "");
		String rid = Configurations.getStringProperty("webservice.rid", "");
		String sid = Configurations.getStringProperty("webservice.data.dict.bm.sid", "");
		if (method.equals("QL")) {
			methodName = Configurations.getStringProperty("webservice.method.name.ql", "");
		}
		WebServiceClient webServiceClient = new WebServiceClient(ns, methodName, rid, sid, null);
		String result = null;
		try {
			result = getOAServiceData(webServiceClient);
		} catch (Exception e) {
			return "同步失败,获取数据异常!"+e;
		}
		return result ;
	}
	
	
	public String syncDataDictCode(String method) throws Exception{
		String methodName = null;
		String ns = Configurations.getStringProperty("webservice.data.code.url", "");
		String rid = Configurations.getStringProperty("webservice.rid", "");
		String sid = Configurations.getStringProperty("webservice.data.dict.code.sid", "");
		if (method.equals("QL")) {
			methodName = Configurations.getStringProperty("webservice.method.name.ql", "");
		}
		WebServiceClient webServiceClient = new WebServiceClient(ns, methodName, rid, sid, null);
		String result = null;
		try {
			result = getOAServiceData(webServiceClient);
			saveToFile("DataDictCode", result);
		} catch (Exception e) {
			return "同步失败,获取数据异常!"+e;
		}
		return result ;
	}
	
	
	
	
	
	
	

}
