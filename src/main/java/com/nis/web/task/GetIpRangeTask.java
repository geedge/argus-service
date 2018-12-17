package com.nis.web.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.avro.file.SyncableFileOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nis.web.dao.impl.LocalLogJDBCByDruid;

@Component
@PropertySource(value = { "classpath:nis.properties", "classpath:jdbc.properties" })
public class GetIpRangeTask {

	private static Logger logger = LoggerFactory.getLogger(GetIpRangeTask.class);
	@Autowired
	private LocalLogJDBCByDruid localLogJDBCByDruid;

//	@Scheduled(cron = "0/5 * * * * ?")
	public void test() {
		try {
			getAllIp4();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Long, String> getNumAndIpReal(List<String> allIp) throws InterruptedException, ExecutionException {
		logger.info("开始将ip转换为数字");
		Map<Long, String> map = new HashMap<>();
		int ever = 10000;
		int count = allIp.size() / ever;
		if (allIp.size() % ever != 0) {
			count++;
		}
		List<Future<Map<Long, String>>> futures = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < count; i++) {
			int start = i * ever;
			int end = i * ever + ever;
			if (end > allIp.size()) {
				end = allIp.size();
			}
			IpToLongThread ipToLongThread = new IpToLongThread(allIp.subList(start, end));
			Future<Map<Long, String>> res = executor.submit(ipToLongThread);// 异步提交, non blocking.
			futures.add(res);
		}
		executor.shutdown();
		for (Future<Map<Long, String>> future : futures) {
			try {
				map.putAll(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		logger.info("{}个ip转换为数字成功,转换了{}个", allIp.size(), map.size());
		return map;
	}

	private static String testbb(Long[] NoNum) {
		int state = 0;
		String result = "";
		for (int i = 0; i < NoNum.length; i++) {
			if (i == NoNum.length - 1)
				state = 2;
			if (state == 0) {
				if (NoNum[i + 1].longValue() == NoNum[i].longValue() + 1) {
					result += String.valueOf(NoNum[i]);
					result += "-";
					state = 1;
				} else {
					result += String.valueOf(NoNum[i]);
					result += ",";
				}
			} else if (state == 1) {
				if (NoNum[i + 1] != NoNum[i] + 1) {
					result += String.valueOf(NoNum[i]);
					result += ",";
					state = 0;
				}
			} else {
				result += String.valueOf(NoNum[i]);
			}
		}
		return result;
	}

	public void getAllIp4() throws Exception {
		List<String> allIp = localLogJDBCByDruid.getAllIp("1");
		Map<Long, String> map = getNumAndIpReal(allIp);
		Set<Long> keySet = map.keySet();
		List<Long> ipList = new ArrayList<>(keySet);
		Long[] array = new Long[ipList.size()];
		Collections.sort(ipList);
		ipList.toArray(array);
		String result = testbb(array);
		if (result != null && !result.trim().equals("")) {
			String substring = result;
			if (result.endsWith(",")) {
				substring = result.substring(0, result.length() - 1);
			}
			String[] split = substring.split(",");
			List<Long> list = new ArrayList<>();
			for (String str : split) {
				String[] ipAddr = str.split("-");
				if (ipAddr.length > 1) {
					for (String ipStr : ipAddr) {
						System.out.print(ipStr + "[" + map.get(Long.parseLong(ipStr)) + "]");
					}
					System.out.println();
				} else {
					list.add(Long.parseLong(str));
				}
			}
			Collections.sort(list);
			for (int i = 0; i < list.size(); i = i + 2) {
				System.out.print(list.get(i) + "[" + map.get(list.get(i)) + "]");
				if (i < list.size() - 1) {
					System.out.print(list.get(i + 1) + "[" + map.get(list.get(i + 1)) + "]");
				}
				System.out.println();
			}

		}
	}

	public String longToIp(long ip) {
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}

}
