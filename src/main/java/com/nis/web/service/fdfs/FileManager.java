package com.nis.web.service.fdfs;

import java.io.File;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;

/**
 * <p>Title: FileManager.java</p>
// * <p>Description: 上传文件到fdfs的工具类</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月16日
 *
 */

public class FileManager extends FileManagerConfig {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(FileManager.class);
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
			logger.error("创建tracker|storage失败,请检查fdfs配置文件或fdfs服务,nginx服务是否正常", e);
			throw new ServiceRuntimeException("创建tracker|storage失败,请检查fdfs配置文件或fdfs服务,nginx服务是否正常",RestBusinessCode.FastDfsServerException.getValue());
		}
	}

	/**
	 * 向fastdfs上传文件
	 * @param file 文件对象
	 * @param valuePairs 设置meta信息,这个可以为空,设置了好像也没有什么用
	 * @return 返回文件在fdfs上的http访问地址(可直接在浏览器下载),null代表上传失败
	 */
	public static String upload(FastDFSFile file, NameValuePair[] valuePairs) {
		String[] uploadResults = null;
		try {
			// uploadResults = storageClient.upload_file(file.getContent(), file.getExt(),
			// valuePairs);
			uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), valuePairs);
			String groupName = uploadResults[0];// group名称:group1|group2...
			String remoteFileName = uploadResults[1];// 文件路径:M00/00/01/CgAGwFr9LTiAEoSaAADz3NN2rlY365.jpg
			String fileAbsolutePath = PROTOCOL + TRACKER_NGNIX_ADDR
			// + trackerServer.getInetSocketAddress().getHostName()
			// + SEPARATOR + TRACKER_NGNIX_PORT
					+ SEPARATOR + groupName + SEPARATOR + remoteFileName;
			return fileAbsolutePath;
		} catch (Exception e) {
			logger.error("上传文件到fastfds服务器失败,请检查fdfs配置文件或fdfs服务,nginx服务是否正常", file.getName(), e);
			throw new ServiceRuntimeException("上传文件" + file.getName() + "到fastfds服务器失败,请检查fdfs配置文件或fdfs服务,nginx服务是否正常",
					RestBusinessCode.FastDfsServerException.getValue());
		}
	}

	/**
	 * 从fastdfs下载文件
	 * @param groupName 文件所属组:group1
	 * @param remoteFileName 文件路径:M00/00/01/CgAGwFr9LriANfj6AADz3NN2rlY448.jpg
	 * @param specFileName 重命名文件:对CgAGwFr9LriANfj6AADz3NN2rlY448.jpg重命名,例如重命名为:1.jpg
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
			logger.error("从fastfds服务器下载文件{}失败,请检查fdfs配置文件或fdfs服务,nginx服务是否正常", remoteFileName, e);
			throw new ServiceRuntimeException("从fastfds服务器下载文件" + remoteFileName + "失败,请检查fdfs配置文件或fdfs服务,nginx服务是否正常",
					RestBusinessCode.FastDfsServerException.getValue());
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
			logger.error("删除文件:{}失败,所属组:{},请检查fdfs配置文件或fdfs服务,nginx服务是否正常", filePath, group, e);
			throw new ServiceRuntimeException("删除文件:" + filePath + "失败,所属组:" + group + ",请检查fdfs配置文件或fdfs服务,nginx服务是否正常",
					RestBusinessCode.FastDfsServerException.getValue());

		}
		return result;
	}
}
