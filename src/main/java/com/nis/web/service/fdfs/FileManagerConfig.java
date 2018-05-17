package com.nis.web.service.fdfs;

import java.io.Serializable;

/**
 * <p>Title: FileManagerConfig.java</p>
 * <p>Description: </p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月16日
 *
 */

public class FileManagerConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FILE_DEFAULT_AUTHOR = "iie-rkg";

	public static final String PROTOCOL = "http://";

	public static final String SEPARATOR = "/";
	// fdfs-tracker-nginx服务器
	public static final String TRACKER_NGNIX_ADDR = "10.0.6.192";

	public static final String TRACKER_NGNIX_PORT = "";

	public static final String CLIENT_CONFIG_FILE = "fdfs_client.conf";
}
