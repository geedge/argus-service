package com.nis.web.service.fdfs;

/**
 * <p>Title: FastDFSFile.java</p>
 * <p>Description: file对象</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月16日
 *
 */

public class FastDFSFile extends FileManagerConfig {
	private static final long serialVersionUID = 1L;

	private byte[] content;
	private String name;
	private String ext;
	private String length;
	private String author = FILE_DEFAULT_AUTHOR;

	/**
	 * 根据byte数组和文件后缀构建FastDFSFile对象
	 * @param content 文件转化为byte
	 * @param ext 文件后缀名
	 */
//	public FastDFSFile(byte[] content, String ext) {
//		this.content = content;
//		this.ext = ext;
//	}

	/**
	 * 根据byte数组和文件后缀构建FastDFSFile对象
	 * @param content 文件转化为byte
	 * @param name 文件名称
	 * @param ext 文件后缀名
	 */
	public FastDFSFile(byte[] content, String name, String ext) {
		this.content = content;
		this.name = name;
		this.ext = ext;
	}

	public FastDFSFile(byte[] content, String name, String ext, String length, String author) {
		this.content = content;
		this.name = name;
		this.ext = ext;
		this.length = length;
		this.author = author;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
