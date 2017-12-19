/**   
* @Title: DfDnsLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （ddm）  
* @date 2016年9月7日 上午10:16:30 
* @version V1.0   
*/
package com.nis.domain.restful;

import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** 
* @ClassName: DfDnsLog 
* @Description: DNSXX日志
* @author (ddm)  
* @date 2016年9月7日 上午11:41:50 
* @version V1.0 
*/
public class DfDnsLog extends LogEntity<DfDnsLog>{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 4159362642752335782L;
	
	
	@ApiModelProperty(value="递归请求", required=true)
	protected Long rd;	
	@ApiModelProperty(value="查询类型", required=true)
	protected Long qtype;	
	@ApiModelProperty(value="查询类", required=true)
	protected Long qclass;	
	@ApiModelProperty(value="OPCODE", required=true)
	protected Long opcode;	
	@ApiModelProperty(value="查询内容", required=true)
	protected String qname;	
	@ApiModelProperty(value="欺骗包的应答类型", required=true)
	protected String cheatType;	
	@ApiModelProperty(value="欺骗包RCODE", required=true)
	protected Long cheatRcode;	
	@ApiModelProperty(value="欺骗策略", required=true)
	protected String cheatStrategy;	
	@ApiModelProperty(value="欺骗记录", required=true)
	protected String cheatRr;
	public Long getRd() {
		return rd;
	}
	public void setRd(Long rd) {
		this.rd = rd;
	}
	public Long getQtype() {
		return qtype;
	}
	public void setQtype(Long qtype) {
		this.qtype = qtype;
	}
	public Long getQclass() {
		return qclass;
	}
	public void setQclass(Long qclass) {
		this.qclass = qclass;
	}
	public Long getOpcode() {
		return opcode;
	}
	public void setOpcode(Long opcode) {
		this.opcode = opcode;
	}
	public String getQname() {
		return qname;
	}
	public void setQname(String qname) {
		this.qname = qname;
	}
	public void setCheatRcode(Long cheatRcode) {
		this.cheatRcode = cheatRcode;
	}
	public void setCheatType(String cheatType) {
		this.cheatType = cheatType;
	}
	public Long getCheatRcode() {
		return cheatRcode;
	}
	public String getCheatType() {
		return cheatType;
	}
	public String getCheatStrategy() {
		return cheatStrategy;
	}
	public void setCheatStrategy(String cheatStrategy) {
		this.cheatStrategy = cheatStrategy;
	}
	public String getCheatRr() {
		return cheatRr;
	}
	public void setCheatRr(String cheatRr) {
		this.cheatRr = cheatRr;
	}
	
	

}
