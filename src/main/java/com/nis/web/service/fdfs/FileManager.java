package com.nis.web.service.fdfs;

import java.io.File;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * <p>Title: FileManager.java</p>
 * <p>Description: </p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月16日
 *
 */

public class FileManager extends FileManagerConfig {

	private static final long serialVersionUID = 1L;
	private static TrackerClient trackerClient;
	private static TrackerServer trackerServer;
	private static StorageServer storageServer;
	private static StorageClient storageClient;

	static {
		try {
			String classPath = new File(FileManager.class.getResource("/").getFile()).getCanonicalPath();
			String fdfsClientConfigFilePath = classPath + File.separator + CLIENT_CONFIG_FILE;
			ClientGlobal.init(fdfsClientConfigFilePath);
			trackerClient = new TrackerClient();
			trackerServer = trackerClient.getConnection();
			storageClient = new StorageClient(trackerServer, storageServer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向fastdfs上传文件
	 * @param file
	 * @param valuePairs
	 * @return
	 */
	public static String upload(FastDFSFile file, NameValuePair[] valuePairs) {
		String[] uploadResults = null;
		try {
			// uploadResults = storageClient.upload_file(file.getContent(), file.getExt(),
			// valuePairs);
			uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), valuePairs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String groupName = uploadResults[0];
		String remoteFileName = uploadResults[1];

		String fileAbsolutePath = PROTOCOL + TRACKER_NGNIX_ADDR
		// + trackerServer.getInetSocketAddress().getHostName()
		// + SEPARATOR + TRACKER_NGNIX_PORT
				+ SEPARATOR + groupName + SEPARATOR + remoteFileName;
		return fileAbsolutePath;
	}

	/**
	 * 从fastdfs下载文件
	 * @param groupName
	 * @param remoteFileName
	 * @param specFileName
	 * @return
	 */
	public static ResponseEntity<byte[]> download(String groupName, String remoteFileName, String specFileName) {
		byte[] content = null;
		HttpHeaders headers = new HttpHeaders();
		try {
			content = storageClient.download_file(groupName, remoteFileName);
			headers.setContentDispositionFormData("attachment",
					new String(specFileName.getBytes("UTF-8"), "iso-8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);
	}

	/**
	 * 根据组和文件名删除文件
	 * @param group  文件所属组
	 * @param filePath 文件存放路径,不包含group,例如文件url是http://10.0.6.192/group1/M00/00/01/CgAGwFr786aAegOQAADz3NN2rlY283.jpg,那么filePath=/M00/00/01/CgAGwFr786aAegOQAADz3NN2rlY283.jpg
	 * @return 返回0代表成功,其他则失败
	 */
	public static Integer delete_file(String group, String filePath) {
		Integer result = -1;
		try {
			result = storageClient.delete_file(group, filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
