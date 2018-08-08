package com.nis.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nis.util.DateUtils;
import com.nis.util.LogUtils;
import com.nis.web.service.BaseService;

public class LogInterceptor extends BaseService implements HandlerInterceptor {

	private static final ThreadLocal<Long> timeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");

	/**
	 * 复制输入流
	 * @param inputStream
	 * @return
	 */
	public static InputStream cloneInputStream(ServletInputStream inputStream) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buffer)) > -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byteArrayOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		return byteArrayInputStream;
	}

	/**
	 * 获取非get请求的参数
	 * @param request
	 * @return
	 */
	private static String getBodyString(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = cloneInputStream(request.getInputStream());
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		// logger.info("请求路径是:" + requestURI);
		// logger.info("请求参数是:" + getBodyString(request));
		long beginTime = System.currentTimeMillis();// 1、开始时间
		timeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
		if (logger.isDebugEnabled()) {
			logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS").format(beginTime),
					request.getRequestURI());
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			logger.info("ViewName: " + modelAndView.getViewName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long beginTime = timeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
		long endTime = System.currentTimeMillis(); // 2、结束时间

		long consumerTime = endTime - beginTime;

		// 打印JVM信息。
		if (logger.isDebugEnabled()) {

			Object[] params = new Object[] { new SimpleDateFormat("hh:mm:ss.SSS").format(endTime),
					DateUtils.formatDateTime(consumerTime), request.getRequestURI(),
					Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024,
					Runtime.getRuntime().freeMemory() / 1024 / 1024, (Runtime.getRuntime().maxMemory()
							- Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024 };
			logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m", params);

		}

		String requestURI = request.getRequestURI();
		// swagger日志请求和行为日志请求不记录请求日志
		if (!requestURI.equals("/galaxy/nis/sys/log/swaggerLogList")
				&& !requestURI.equals("/galaxy/nis/sys/log/list")) {
			// 保存日志
			LogUtils.saveLog(request, handler, ex, null, consumerTime);
		}
	}

}
