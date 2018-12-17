package com.nis.web.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpToLongThread implements Callable<Map<Long, String>> {

	private static Logger logger = LoggerFactory.getLogger(IpToLongThread.class);
	private List<String> list;

	public IpToLongThread(List<String> list) {
		super();
		this.list = list;
	}

	public IpToLongThread() {
		super();
	}

	@Override
	public Map<Long, String> call() throws Exception {
		Map<Long, String> map=new HashMap<>();
		logger.info("线程{}开始执行", Thread.currentThread().getName());
		for (int i = 0; i < list.size(); i++) {
			map.put(ipToLong(list.get(i)), list.get(i));
		}
		logger.info("线程{}执行结束", Thread.currentThread().getName());
		return map;
	}

	public long ipToLong(String ipAddress) {
		long result = 0;
		String[] ipAddressInArray = ipAddress.split("\\.");
		for (int i = 3; i >= 0; i--) {
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
			// left shifting 24,16,8,0 and bitwise OR

			// 1. 192 << 24
			// 1. 168 << 16
			// 1. 1 << 8
			// 1. 2 << 0
			result |= ip << (i * 8);

		}
		return result;
	}

}
