/**
 *@Title: HttpServletRequestWrapper.java
 *@Package com.nis.filter.wrapper 
 *@Description TODO
 *@author dell
 *@date 2016年11月15日 下午12:36:14
 *@version  版本号
 */
package com.nis.filter.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StreamUtils;


/**
 * @ClassName: HttpServletRequestWrapper.java 
 * @Description: 主要目的是将request的body取出来，然后缓存起来。这样可以实现request的body多次取
 * @author (wx)
 * @date 2016年11月15日 下午12:36:14
 * @version V1.0
 */
public class RestHttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
	private final byte[] body;	
	/**
	 * 创建一个新的实例 RestHttpServletRequestWrapper.
	 *
	 * @param request
	 * @throws IOException 
	 */
	public RestHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		// TODO Auto-generated constructor stub
		body=StreamUtils.copyToByteArray(request.getInputStream());
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getInputStream()
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		final ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(body);
		return new ServletInputStream(){

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return byteArrayInputStream.read();
			}
			
		};
	}
	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getReader()
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
}
