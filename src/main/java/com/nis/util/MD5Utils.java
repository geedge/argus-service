/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.ckfinder.connector.ServletContextFactory;
import com.google.common.collect.Lists;
import com.nis.web.service.SpringContextHolder;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author ThinkGem
 * @version 2013-05-22
 */
public class MD5Utils{
	
	/**
	 * 字符串转为MD532位小写
	 * @param plain
	 * @return
	 */
	public static String md5LowerCase(String plain) throws Exception{
		String re_md5 = new String();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plain.getBytes());
			byte b[] = md.digest();
		
			int i;
		
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
			i += 256;
			if (i < 16)
			buf.append("0");
			buf.append(Integer.toHexString(i));
			}
			
			re_md5 = buf.toString();
			return re_md5;
		}
	public static byte[] createChecksum(String filename) throws IOException, NoSuchAlgorithmException{
		InputStream fis=new FileInputStream(filename);
		byte[] buffer=new byte[1024];
		MessageDigest complete=MessageDigest.getInstance("MD5");
		int numRead;
		do{
			numRead=fis.read(buffer);
			if(numRead>0){
				complete.update(buffer,0,numRead);
			}
		}while(numRead!=-1);
		fis.close();
		return complete.digest();
	}
	public static String getMD5Checksum(String filename) throws NoSuchAlgorithmException, IOException{
		byte[] b=createChecksum(filename);
		StringBuffer result=new StringBuffer();
		for(int i=0;i<b.length;i++){
			result.append(Integer.toString((b[i] & 0xff)+ 0x100,16).substring(1));
		}
		return result.toString();
	}
}
