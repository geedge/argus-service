package com.nis.domain;

import com.nis.util.Configurations;

public class WebServiceClient {
	private  final String endPoint = Configurations.getStringProperty("webservice.esb.endpoint", ""); //固定不变ebs服务器名称
	private  final int connectionTimeOut = Configurations.getIntProperty("webservice.request.timeout", 60000);//webservice 链接超时时间 毫秒
	private String ns;//webservice 要获取服务空间url
	private String methodName;//要调用的方法名称
	private String rid;
	private String sid;
	private int pageSize = Configurations.getIntProperty("webservice.data.pagesize", 20); //分页获取的数量
	private int currentPage = Configurations.getIntProperty("webservice.data.currentpage", 1);//当前页
	private String updateTime;//增量更新时间
	private String param="";//方法调用最后一个参数，根据实际情况填写
	
	
	public WebServiceClient(String ns, String methodName, String rid, String sid, String updateTime) {
		super();
		this.ns = ns;
		this.methodName = methodName;
		this.rid = rid;
		this.sid = sid;
		this.updateTime = updateTime;
	}
	
	
	/**
	 * 获取方法参数值列表
	 * @return
	 */
	public String[] getOpVals() {
		String[] vals = null;
		if (methodName.equals(Configurations.getStringProperty("webservice.method.name.ql", ""))) {
			vals = new String[] {String.valueOf(pageSize), String.valueOf(currentPage), param};
		} else if (methodName.equals(Configurations.getStringProperty("webservice.method.name.zl", ""))) {
			vals = new String[] {String.valueOf(pageSize), String.valueOf(currentPage), updateTime, param};
		}
		
		return vals;
	}
	
	
	/**
	 * 获取方法参数列表
	 * @return
	 */
	public String[] getOpArgs() {
		String[] args = null;
		if (methodName.equals(Configurations.getStringProperty("webservice.method.name.ql", ""))) {
			args = new String[] {"pageSize","currentPage","param"};
		} else if (methodName.equals(Configurations.getStringProperty("webservice.method.name.zl", ""))) {
			args = new String[] {"pageSize","currentPage","updateTime","param"};
		}
		
		return args;
	}
	
	
	
	public void incrementPage (){
		currentPage = currentPage +1 ;
	}
	
	
	
	public String getNs() {
		return ns;
	}
	public void setNs(String ns) {
		this.ns = ns;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getParam() {
		return param;
	}


	public void setParam(String param) {
		this.param = param;
	}


	public int getConnectionTimeOut() {
		return connectionTimeOut;
	}


	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getEndPoint() {
		return endPoint;
	}
	
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
	
	

}
