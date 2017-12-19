/**   
* @Title: DjDnsLog.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （zbc）  
* @date 2016年9月7日 上午10:16:30 
* @version V1.0   
*/
package com.nis.domain.restful;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.nis.domain.LogEntity;
import com.nis.domain.Page;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: DjDnsLog
 * @Description: DNSXX日志
 * @author (zbc)
 * @date 2016年9月7日 上午11:41:50
 * @version V1.0
 */
public class DjDnsLog extends LogEntity<DjDnsLog> {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 3747868295576943355L;

	@ApiModelProperty(value = "请求/应答", required = true)
	protected Long qr;
	@ApiModelProperty(value = "递归请求", required = true)
	protected Long rd;
	@ApiModelProperty(value = "递归应答", required = true)
	protected Long ra;
	@ApiModelProperty(value = "资源记录", required = true)
	protected String rr;
	@ApiModelProperty(value = "查询类型", required = true)
	protected Long qtype;
	@ApiModelProperty(value = "查询类", required = true)
	protected Long qclass;
	@ApiModelProperty(value = "OPCODE", required = true)
	protected Long opcode;
	@ApiModelProperty(value = "查询内容", required = true)
	protected String qname;
	@ApiModelProperty(value = "DNS：1，DNSSEC：2", required = true)
	protected Long dnsSub;

	public DjDnsLog() {
		super();
	}


	public Long getQr() {
		return qr;
	}

	public void setQr(Long qr) {
		this.qr = qr;
	}

	public Long getRd() {
		return rd;
	}

	public void setRd(Long rd) {
		this.rd = rd;
	}

	public Long getRa() {
		return ra;
	}

	public void setRa(Long ra) {
		this.ra = ra;
	}

	public String getRr() {
		return rr;
	}

	public void setRr(String rr) {
		this.rr = rr;
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

	public Long getDnsSub() {
		return dnsSub;
	}

	public void setDnsSub(Long dnsSub) {
		this.dnsSub = dnsSub;
	}
}
