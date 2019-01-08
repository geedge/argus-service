package com.nis.domain.restful;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nis.domain.LogEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @ClassName:NtcP2pLog
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年8月7日 上午9:23:52
 * @version V1.0
 */
public class NtcP2pLog extends LogEntity<NtcP2pLog> {

	private static final long serialVersionUID = -8085222950278735219L;
	
	@ApiModelProperty(value = "P2P协议", required = true)
	private String p2pProto;
	@ApiModelProperty(value = "文件标识", required = true)
	private String p2pFileId;
	@ApiModelProperty(value = "Emule搜索关键词", required = true)
	private String p2pKeyword;
	
	protected String searchP2pFileId;
	protected String searchP2pKeyword;
	/**
	 * @return the p2pProto
	 */
	public String getP2pProto() {
		return p2pProto;
	}
	/**
	 * @param p2pProto the p2pProto to set
	 */
	public void setP2pProto(String p2pProto) {
		this.p2pProto = p2pProto;
	}
	/**
	 * @return the p2pFileId
	 */
	public String getP2pFileId() {
		return p2pFileId;
	}
	/**
	 * @param p2pFileId the p2pFileId to set
	 */
	public void setP2pFileId(String p2pFileId) {
		this.p2pFileId = p2pFileId;
	}
	/**
	 * @return the p2pKeyword
	 */
	public String getP2pKeyword() {
		return p2pKeyword;
	}
	/**
	 * @param p2pKeyword the p2pKeyword to set
	 */
	public void setP2pKeyword(String p2pKeyword) {
		this.p2pKeyword = p2pKeyword;
	}
	
	@JsonIgnore
	public String getSearchP2pFileId() {
		return searchP2pFileId;
	}
	
	public void setSearchP2pFileId(String searchP2pFileId) {
		this.searchP2pFileId = searchP2pFileId;
	}
	
	@JsonIgnore
	public String getSearchP2pKeyword() {
		return searchP2pKeyword;
	}
	
	public void setSearchP2pKeyword(String searchP2pKeyword) {
		this.searchP2pKeyword = searchP2pKeyword;
	}
}
