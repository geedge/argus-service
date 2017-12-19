/**   
* @Title: DfSslLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月7日 下午2:34:10
* @version V1.0   
*/
package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfSslLog 
* @Description: SSLXX日志
* @author (ddm)  
* @date 2016年9月7日 下午2:34:10
* @version V1.0 
*/
public class DjSslLog extends LogEntity<DjSslLog>{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1191890804422075638L;
	
	@ApiModelProperty(value="版本信息", required=true)
	protected String version;
	@ApiModelProperty(value="SNI", required=true)
	protected String sni;

	@ApiModelProperty(value="个体证书转储路径", required=true)
	protected String individualCertFile;
	@ApiModelProperty(value="中级证书转储路径", required=true)
	protected String middleCertFile;
	@ApiModelProperty(value="根证书转储路径", required=true)
	protected String rootCertFile;
	@ApiModelProperty(value="其他证书链转储路径", required=true)
	protected String chainCertFile;
	

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSni() {
		return sni;
	}
	public void setSni(String sni) {
		this.sni = sni;
	}

	public String getIndividualCertFile() {
		return individualCertFile;
	}
	public void setIndividualCertFile(String individualCertFile) {
		this.individualCertFile = individualCertFile;
	}
	public String getMiddleCertFile() {
		return middleCertFile;
	}
	public void setMiddleCertFile(String middleCertFile) {
		this.middleCertFile = middleCertFile;
	}
	public String getRootCertFile() {
		return rootCertFile;
	}
	public void setRootCertFile(String rootCertFile) {
		this.rootCertFile = rootCertFile;
	}
	public String getChainCertFile() {
		return chainCertFile;
	}
	public void setChainCertFile(String chainCertFile) {
		this.chainCertFile = chainCertFile;
	}
	
}
