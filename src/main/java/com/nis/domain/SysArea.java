package com.nis.domain;


public class SysArea extends TreeEntity<SysArea>{

	private static final long serialVersionUID = -6425455761476080303L;

	private String code;

    private String type;

    private String remarks;
    
    private double longitude;
    
    private double latitude;

    private Integer delFlag;
 
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
    
    

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public SysArea getParent() {
		return parent;
	}

	@Override
	public void setParent(SysArea parent) {
		this.parent = parent;
		
	}
}