/**
 * 
 */
package com.nis.domain.restful;

/**
 * @ClassName:CommonSourceCfg
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年5月19日 下午8:00:45
 * @version V1.0
 */
public class CommonSourceFieldCfg {

	private String fieldType;
	private String srcName;
	private String dstName;
	private String range;
	private Boolean isRequired=false;
	private String defaultVal;
	private String regexp;
	private Boolean isSrcInfo=false;
	private Boolean isCfgId=false;
	
	public CommonSourceFieldCfg() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param fieldType
	 * @param srcName
	 * @param dstName
	 * @param range
	 * @param isRequired
	 * @param defauleVal
	 * @param regexp
	 */
	public CommonSourceFieldCfg(String fieldType, String srcName,
			String dstName, String range, Boolean isRequired,
			String defaultVal, String regexp,Boolean isSrcInfo,Boolean isCfgId) {
		super();
		this.fieldType = fieldType;
		this.srcName = srcName;
		this.dstName = dstName;
		this.range = range;
		this.isRequired = isRequired;
		this.defaultVal = defaultVal;
		this.regexp = regexp;
		this.isSrcInfo = isSrcInfo;
		this.isCfgId = isCfgId;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public String getDstName() {
		return dstName;
	}

	public void setDstName(String dstName) {
		this.dstName = dstName;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getRegexp() {
		return regexp;
	}

	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	/**
	 * @return the isSrcInfo
	 */
	public Boolean getIsSrcInfo() {
		return isSrcInfo;
	}
	
	/**
	 * @param isSrcInfo the isSrcInfo to set
	 */
	public void setIsSrcInfo(Boolean isSrcInfo) {
		this.isSrcInfo = isSrcInfo;
	}
	
	/**
	 * @return the isCfgId
	 */
	public Boolean getIsCfgId() {
		return isCfgId;
	}
	
	/**
	 * @param isCfgId the isCfgId to set
	 */
	public void setIsCfgId(Boolean isCfgId) {
		this.isCfgId = isCfgId;
	}
	
}
