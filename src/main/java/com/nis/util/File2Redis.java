package com.nis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.nis.restful.RestBusinessCode;
import com.nis.restful.ServiceRuntimeException;

import redis.clients.jedis.Transaction;

public class File2Redis {
	private static String fileProtocol = Configurations.getStringProperty("fileProtocol", "minio://");

	public static String file2Redis(String url, Transaction transaction) {
		try {
			byte[] fileByte = downLoadFromUrl(url);
			String md5 = MD5Utils.getMd5ByIS(new ByteArrayInputStream(fileByte));
			String key = "__FILE_" + md5;
			transaction.set(key.getBytes(), fileByte);
			return fileProtocol + key;
		} catch (Exception e) {
			throw new ServiceRuntimeException("保存文件到redis发生了异常:" + e.getMessage(),
					RestBusinessCode.SaveFileToRedisError.getValue());
		}
	}

	public static byte[] downLoadFromUrl(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为30秒
		conn.setConnectTimeout(3 * 10000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		ByteArrayOutputStream baosOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = inputStream.read(buffer)) > -1) {
			baosOutputStream.write(buffer, 0, len);
		}
		baosOutputStream.flush();
		return baosOutputStream.toByteArray();
	}

}
