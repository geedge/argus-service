/**
 * 
 */
package com.nis.domain.restful;

import java.util.Date;

/**
 * @ClassName:FileDesc
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author (zdx)
 * @date 2018年5月21日 下午8:09:06
 * @version V1.0
 */
public class FileDesc {

	/**
	 * 文件格式类型
	 */
	private String filetype;
	/**
	 * 文件数据类型
	 */
	private String dataType;
	/**
	 * fileSystem
	 */
	private String fileSystem;
	/**
	 * 文件创建时间，UTC时间格式
	 */
	private Date createTime;
	/**
	 * 文件惟一key
	 */
	private String key;
	/**
	 *文件名称
	 */
	private String fileName;
	/**
	 * 文件md5校验码
	 */
	private String checksum;
	/**
	 * 
	 */
	public FileDesc() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param filetype
	 * @param createTime
	 * @param key
	 * @param fileName
	 * @param checksum
	 */
	public FileDesc(String filetype, Date createTime, String key,
			String fileName, String checksum) {
		super();
		this.filetype = filetype;
		this.createTime = createTime;
		this.key = key;
		this.fileName = fileName;
		this.checksum = checksum;
	}
	
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
}
