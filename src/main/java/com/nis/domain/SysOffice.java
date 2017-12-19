package com.nis.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class SysOffice extends TreeEntity<SysOffice>  implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7057026197597361766L;

	private SysArea area;

    private String code;

    private Integer type;// 机构类型（1：公司；2：部门；3：小组）

    private Integer grade;// 机构等级（1：一级；2：二级；3：三级；4：四级）
    
    private Integer jobType;//部门职责分类

	private String address;

    private String zipCode;

    private String master;

    private String phone;

    private String fax;

    private String email;

    private Integer useable;

    private SysUser primaryPerson;

    private SysUser deputyPerson;

    private String remarks;

    private Integer delFlag;
    
    private Date createTime;
    
    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private List<String> childDeptList;//快速添加子部门

	public SysOffice(){
		super();
		this.type = 2;
	}

	public SysOffice(Long id){
		super(id);
	}
	
   
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master == null ? null : master.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getUseable() {
        return useable;
    }

    public void setUseable(Integer useable) {
        this.useable = useable;
    }


    
	public SysOffice getParent() {
		return parent;
	}

	public void setParent(SysOffice parent) {
		this.parent = parent;
	}

	public SysArea getArea() {
		return area;
	}

	public void setArea(SysArea area) {
		this.area = area;
	}

	public SysUser getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(SysUser primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	public SysUser getDeputyPerson() {
		return deputyPerson;
	}

	public void setDeputyPerson(SysUser deputyPerson) {
		this.deputyPerson = deputyPerson;
	}

	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
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
    
    public Integer getJobType() {
		return jobType;
	}

	public void setJobType(Integer jobType) {
		this.jobType = jobType;
	}



    

}