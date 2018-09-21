package com.nis.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nis.restful.RestBusinessCode;
import com.nis.restful.RestServiceException;

import io.minio.MinioClient;

public class MinioUtil {

	private static String minio_url = Configurations.getStringProperty("minio_url", "http://192.168.10.61:9000");;
	private static String minio_accessKey = Configurations.getStringProperty("minio_accessKey", "");;
	private static String minio_secretKey = Configurations.getStringProperty("minio_secretKey", "");;
	private static String minio_bucketName = Configurations.getStringProperty("minio_bucketName", "test");;

	/**
	 * 上传文件到minio
	 * @param inputStream 上传的文件流
	 * @param fileName 文件名称
	 * @param contentType 文件类型
	 * @return 返回文件上传路径
	 * @throws Exception
	 */
	public static String uploadFile(InputStream inputStream, String fileName, String contentType) throws Exception {
		MinioClient minioClient = null;
		if (minio_url != null && !minio_url.trim().equals("")) {
			if (minio_accessKey != null && !minio_accessKey.trim().equals("") && minio_secretKey != null
					&& !minio_secretKey.trim().equals("")) {
				minioClient = new MinioClient(minio_url, minio_accessKey, minio_secretKey);
			} else {
				minioClient = new MinioClient(minio_url);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDHHMMss");
			String ymd = sdf.format(new Date());
			String objectName = ymd + "-" + fileName;
			minioClient.putObject(minio_bucketName, objectName, inputStream, inputStream.available(), contentType);
			String url = minioClient.getObjectUrl(minio_bucketName, objectName);
			return url;
		}else {
			throw new RestServiceException("minio服务器地址为空,请联系管理员检查jdbc.properties中minio_url的值", RestBusinessCode.MinioUrlIsNull.getValue());
		}
	}
}
